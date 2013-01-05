(ns chordrecognizer.core
  (:require [chordrecognizer.routes :as routes])
  (:use ring.adapter.jetty))

(defn -main []
  (let [port (Integer/parseInt (get (System/getenv) "PORT" "8080"))]
    (run-jetty routes/app {:port port})))