(ns chordrecognizer.core
  (:use ring.adapter.jetty))

(defn -main []
  (let [port (Integer/parseInt (get (System/getenv) "PORT" "8080"))]
    (run-jetty app {:port port})))