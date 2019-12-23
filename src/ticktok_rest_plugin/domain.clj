(ns ticktok-rest-plugin.domain
  (:require [clojure.spec.alpha :as s]
            [clojure.data.json :as json]))

(s/def ::name string?)

(s/def ::url string?)

(s/def ::schedule string?)

(s/def ::clock-request (s/keys :req-un [::name ::url ::schedule]))

(defn parse-clock [clock-req]
  (let [clock (s/conform ::clock-request clock-req)]
    (if (= ::s/invalid clock)
      nil
      clock)))

(defn explain [clock-req]
  (s/explain ::clock-request clock-req))
