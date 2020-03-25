(ns ticktok-rest-plugin.core-test
  (:require [clojure.test :refer :all]
            [midje.sweet :refer :all]
            [ring.mock.request :as mock]
            [ticktok-rest-plugin.core :as plugin]
            [clojure.data.json :as json]
            [clojure.string :as string]
            [ticktok-rest-plugin.fake-client :as fake]))

(def endpoint "/clocks")

(defn schedule-on [clock]
  (let [{:keys [status body error]} (plugin/app  (-> (mock/request :post endpoint)
                                                     (mock/json-body clock)))]
    status))

(defn start-client []
  (fake/start!))

(defn stop-client []
  (fake/stop!))

(facts "about subscribing clock"

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
          (fake/invoked?) => true)))))
