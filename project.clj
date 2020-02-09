(defproject ticktok-rest-plugin "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "https://ticktok.io"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.10.0"]
                 [http-kit "2.3.0"]
                 [compojure "1.6.1"]
                 [ring "1.7.1"]
                 [ring/ring-json "0.4.0"]
                 [org.clojure/data.json "0.2.6"]
                 [ticktok "1.0.8"]
                 [aero "1.1.3"]]
  :profiles {:dev {:dependencies [[midje "1.9.9"]
                                  [org.clojure/core.async "0.6.532"]]
                   :plugins [[lein-midje "3.2.1"]
                             [lein-heroku "0.5.3"]]}}
  :uberjar-name "ticktok-rest-plugin-standalone.jar"
  :heroku {:app-name  "ticktok-rest-plugin"
           :jdk-version "1.8"})
