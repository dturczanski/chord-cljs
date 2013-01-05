(ns chordrecognizer.routes
 (:use compojure.core
        chordrecognizer.views
        [hiccup.middleware :only (wrap-base-url)]
        [hiccup.bootstrap.middleware :only (wrap-bootstrap-resources)])
  (:require [compojure.route :as route]
            [compojure.handler :as handler]
            [compojure.response :as response]
            [overtone.music.pitch :as pitch]))

(def invalid-request-body-error
  {:type ::invalid-request-body
   :message "The JSON or Clojure request body is invalid."})

(defn read-body [body]
  (let [data (try
               (str (slurp body))
               (catch java.lang.NullPointerException e (throw e)))]
    (when (pos? (count data))
      (try
          (binding [*read-eval* false] 
            (read-string data))
          (catch java.lang.Exception _
            (throw invalid-request-body-error))))))

(defn make-safe-for-cljs-reader [k]
  "Adds a prefix to the keyword. Handy for avoiding errors with keyword beginning with numbers."
  (keyword (str "_" (name k))))

(defn recognize-chord [notes]
  (if-let [chord (pitch/find-chord notes)]
      (assoc chord :chord-type (make-safe-for-cljs-reader (:chord-type chord)))
      nil))

(defroutes main-routes
  (GET "/" [] (index-page))
  (POST "/chord" {body :body} (print-str (recognize-chord (read-body body))))
  (route/resources "/")
  (route/not-found "Page not found"))

(def app
  (-> (handler/site main-routes)
      (wrap-base-url)
      (wrap-bootstrap-resources)))
