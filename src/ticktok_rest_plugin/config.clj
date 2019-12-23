(ns ticktok-rest-plugin.config
  (:require [aero.core :as aero]))

(defn config
  ([]
   (config :dev))
  ([profile]
   (aero/read-config "config.edn" {:profile profile})))

(defn ticktok-host [config]
  (get-in config [:ticktok :host]))

(defn ticktok-token [config]
  (get-in config [:ticktok :token]))
