(defproject omodoro "0.1.0-SNAPSHOT"
  :description "A pomodoro timer."
  :url "http://example.com/FIXME"

  :dependencies [[org.clojure/clojure "1.5.1"]
                 [org.clojure/clojurescript "0.0-2202"]
                 [org.clojure/core.async "0.1.303.0-886421-alpha"]
                 [om "0.6.2"]
                 [com.cemerick/piggieback "0.1.3"]
                 [weasel "0.2.0"]]

  :plugins [[lein-cljsbuild "1.0.3"]]

  :repl-options {:nrepl-middleware [cemerick.piggieback/wrap-cljs-repl]}

  :injections [(require 'cemerick.piggieback 'weasel.repl.websocket)
               (defn browser-repl []
                 (cemerick.piggieback/cljs-repl
                   :repl-env (weasel.repl.websocket/repl-env :port 9001)))]

  :source-paths ["src"]

  :cljsbuild {
    :builds [{:id "omodoro"
              :source-paths ["src" "dev"]
              :compiler {
                :output-to "omodoro.js"
                :output-dir "out"
                :optimizations :none
                :source-map true}}]})
