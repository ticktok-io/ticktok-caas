(ns ticktok-rest-plugin.domain-test
  (:require [clojure.test :refer :all]
            [midje.sweet :refer :all]
            [clojure.data.json :as json]
            [ticktok-rest-plugin.domain :as dom]))

(facts "about clock validation"

  (tabular
   (fact "should return nil for invalid clock request"
     (dom/parse-clock ?clock) => nil)

   ?clock
   {:name "someName"}
   {:url "someUrl"}
   {:schedule "every.1.seconds"}))
