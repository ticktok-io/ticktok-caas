(ns ticktok-rest-plugin.config
  (:require [aero.core :as aero]))

(defn config []
  (aero/read-config (clojure.java.io/resource "resources/config.edn")))

(defn ticktok-host [config]
  (get-in config [:ticktok :host]))

(defn ticktok-token [config]
  (get-in config [:ticktok :token]))
