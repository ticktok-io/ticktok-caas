(ns ticktok-rest-plugin.fake-client
  (:require
   [compojure.core :refer :all]
   [compojure.handler :as handler]
   [compojure.route :refer :all]
   [ring.middleware.json :as middleware]
   [clojure.data.json :as json]
   [org.httpkit.server :as http]
   [clojure.core.async :as async :refer [chan put! <!! close!]]))

(def port 8084)

(def endpoint "http://localhost:8084/fakeurl")

(def state (atom {:server nil
                  :invocations nil}))

(defn invocations []
  (:invocations @state))

(defn invoked?  []
  (let [m (<!! (invocations))]
    (some? m)))

(defn fake-handler [req]
  (println "fake client invoked!")
  (swap! state update :invocations #(do
                                      (put! % "tick")
                                      %))
  {:status 200})

(defn stop! []
  (let [inst (:server @state)]
    (when-not (nil? inst)
      (inst :timeout 100)
      (swap! state assoc :server nil :invocations nil)
      (println "fake client stopped")))
  nil)

(defroutes api-routes
  (POST "/fakeurl" [] fake-handler))

(def app
  (-> (handler/site api-routes)))

(defn start! []
  (swap! state assoc :server (http/run-server #'app {:port port}) :invocations (chan 1))
  (println "fake client started"))

