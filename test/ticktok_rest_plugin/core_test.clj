(ns ticktok-rest-plugin.core-test
  (:require [clojure.test :refer :all]
            [midje.sweet :refer :all]
            [org.httpkit.client :as http]
            [ticktok-rest-plugin.core :as plugin]
            [clojure.data.json :as json]
            [clojure.string :as string]
            [ticktok-rest-plugin.fake-client :as fake]))

(def port 8081)

(def host (str "http://localhost:" port))

(defn schedule-on [clock]
  (let [options {:headers  {"Content-Type" "application/json"}
                 :body (json/write-str clock)}
        endpoint (string/join [host "/clocks"])
        {:keys [status body error]} @(http/post endpoint
                                                options)]
    status))

(defn start-server []
  (plugin/start! port))

(defn stop-server []
  (plugin/stop!))

(defn start-client []
  (fake/start!))

(defn stop-client []
  (fake/stop!))

(facts "about subscribing clock"

  (with-state-changes [(before :contents (start-server))
                       (after :contents (stop-server))]

    (facts "when failed to subscribe clock"
      
      (let [missing-clock {:name "missing"}]

        (fact "should fail if request is missing url or name or schedule"

          (schedule-on missing-clock) => 400)))

    (facts "when successfully subscribe"

      (with-state-changes [(before :contents (start-client))
                           (after :contents (stop-client))]

        (let [valid-clock {:name "valid.clock"
                           :schedule "every.1.seconds"
                           :url fake/endpoint}]

          (fact "should post url client upon tick"

            (schedule-on valid-clock) => truthy
            (fake/invoked?) => true))))))

