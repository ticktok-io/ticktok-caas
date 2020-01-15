(ns ticktok-rest-plugin.core
  (:require
   [compojure.core :refer :all]
   [compojure.handler :as handler]
   [compojure.route :refer :all]
   [ring.middleware.json :as middleware]
   [clojure.data.json :as json]
   [org.httpkit.server :as http]
   [ticktok-rest-plugin.runner :as runner]
   [ticktok-rest-plugin.domain :as dom]))

(def default-port 8081)

(defonce server (atom nil))

(defn response 
  ([body]
   (response 201 body))
  ([status body]
   {:status status
    :body (json/write-str body)}))

(defn- clock-from [clock-req]
  (let [cb #(println "some callback")
        clock (select-keys clock-req [:name :schedule])
        clock (merge clock {:callback cb})]
    clock))

(defn clocks-handler [req]
  (let [clock-req (dom/parse-clock (:body req))]
    (if (some? clock-req)      
      (do
        (runner/run clock-req)
        (response clock-req))
      (response 400 {:error "Failed to parse clock"
                     :reason (dom/explain req)}))))

(defroutes api-routes
  (POST "/clocks" []
        clocks-handler))

(def app
  (-> (handler/site api-routes)
      (middleware/wrap-json-body {:keywords? true})))


(defn start!
  ([]
   (start! default-port))
  ([port]
   (reset! server (http/run-server #'app {:port port}))))

(defn stop! []
  (when-not (nil? @server)
    (@server :timeout 100)
    (reset! server nil)
    (runner/close!))
  nil)

(defn -main [& [port]]
  (let [port (Integer. (or port (env :port) default-port))]
    (start! port)))
