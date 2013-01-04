(defproject chordrecognizer "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :source-paths ["src"]
  :dependencies [[org.clojure/clojure "1.4.0"]
                 [overtone "0.8.0-RC14"]
                 [compojure "1.0.4"]
                 [hiccup "1.0.0"]
                 [hiccup-bootstrap "0.1.1"]
                 [org.clojure/google-closure-library "0.0-2029"]
                 [org.clojure/google-closure-library-third-party "0.0-2029"]
                 [domina "1.0.1"]]
  :plugins [[lein-cljsbuild "0.2.9"]
            [lein-ring "0.7.0"]]
  :cljsbuild {
    :builds [{:source-path "src-cljs"
              :compiler {:output-to "resources/public/js/main.js"
                         :optimizations :whitespace
                         :pretty-print true}}]}
  :ring {:handler chordrecognizer.routes/app})
