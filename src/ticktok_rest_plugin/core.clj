(ns ticktok-rest-plugin.core
  (:require
   [compojure.core :refer :all]
   [compojure.handler :as handler]
   [compojure.route :refer :all]
   [ring.middleware.json :as middleware]
   [clojure.data.json :as json]
   [ticktok-rest-plugin.runner :as runner]
   [ticktok-rest-plugin.domain :as dom]))

(defn response 
  ([body]
   (response 201 body))
  ([status body]
   {:status status
    :body (json/write-str body)}))

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
