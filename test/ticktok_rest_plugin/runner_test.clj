(ns ticktok-rest-plugin.runner-test
  (:require [clojure.test :refer :all]
            [midje.sweet :refer :all]
            [ticktok-rest-plugin.runner :as runner]))

(facts  "about running a clock"

  (facts  "when failed to subscribe"
    
    (fact  "should fail on client error"

      (runner/run {}))
    => (throws RuntimeException #"Failed to subscribe clock" #(contains? (ex-data %) :clock))))
