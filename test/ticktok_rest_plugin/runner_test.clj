(ns ticktok-rest-plugin.runner-test
  (:require [clojure.test :refer :all]
            [midje.sweet :refer :all]
            [org.httpkit.client :as http]
            [ticktok-rest-plugin.runner :as runner]
            [clojure.data.json :as json]
            [clojure.string :as string]
            [ticktok-rest-plugin.fake-client :as fake]))

(defn wait-a-bit []
  (Thread/sleep 3000)
  true)

(defn start-client []
  (fake/start!))

(defn stop-client-and-runner []
  (fake/stop!)
  (runner/close!))

(facts  "about running a clock"

  (facts  "when failed to subscribe"
    
    (fact  "should fail on client error"

      (runner/run {}))
    => (throws RuntimeException #"Failed to subscribe clock" #(contains? (ex-data %) :clock)))

  (facts "when successfully subscribe"

    (with-state-changes [(before :contents (start-client))
                         (after :contents (stop-client-and-runner))]

      (let [ck {:name "valid.clock"
                :schedule "every.1.seconds"
                :url fake/endpoint}]

        (fact "should post url client upon tick"

          (runner/run ck) => truthy
          (wait-a-bit) => true
          (fake/invoked?) => true)))))
