(ns ticktok-rest-plugin.runner
  (:require [ticktok.core :as tk]
            [ticktok-rest-plugin.config :as config]
            [clj-http.client :as client]))

(def config (config/config))

(def ticktok-config {:host (config/ticktok-host config)
                     :token (config/ticktok-token config)})

(defn close! []
  (tk/ticktok :close)
  true)

(defn- exception-handler [e ck]
  (let [exp (Throwable->map e)
        explain {:error (:cause exp)
                 :clock ck}]
    (throw (ex-info "Failed to subscribe clock" explain))))

(defn- try-run
  ([ck]
   (try-run ticktok-config ck))
  ([config ck]
   (try 
     (println "going to run clock " ck)
     (tk/ticktok :schedule config ck)
     ck
     (catch Exception e
       (exception-handler e ck)))))

(defn- callback-for [url]
  (fn []
    (println "callback got tick")
    (let [{:keys [status body error]} (client/post url)]
      (println "client repond with " status)
      body)))

(defn- with-callback [{:keys [name schedule url]}]
  (let [ck {:name name
            :schedule schedule
            :callback (callback-for url)}]
    ck))

(defn run [clock]
  (try-run (with-callback clock)))
