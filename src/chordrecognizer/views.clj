(ns chordrecognizer.views
  (:use hiccup.bootstrap.page)
  (:require
    [hiccup
      [page :refer [html5]]
      [element :refer [javascript-tag]]
      [page :refer [include-js include-css]]]))

(defn- piano-divs []
  (let [black-keys #{1 3 6 8 10}
        white? (fn[key] 
                 (nil? (black-keys (mod key 12))))]
    (map #(vector :div {:value % :id (str "key" %) :class (if (white? %) "key" "key black")}) (range 48 84))))


; When using {:optimizations :whitespace}, the Google Closure compiler combines
; its JavaScript inputs into a single file, which obviates the need for a "deps.js"
; file for dependencies. However, true to ":whitespace", the compiler does not remove
; the code that tries to fetch the (nonexistent) "deps.js" file. Thus, we have to turn
; off that feature here by setting CLOSURE_NO_DEPS.
;
; Note that this would not be necessary for :simple or :advanced optimizations.
(defn- include-clojurescript [path]
  (list
    (javascript-tag "var CLOSURE_NO_DEPS = true;")
    (include-js path)))

(defn index-page []
  (html5
    [:head
      [:title "Chord recognizer"]
      (include-js "//ajax.googleapis.com/ajax/libs/jquery/1.8.3/jquery.min.js")
      (include-bootstrap)
      (include-css "css/piano.css")]
    [:body
      [:p 
       [:i {:class "icon-music"}] 
       "Select keys and get the chord! - "
       [:a {:href "http://jvmsoup.com"} "author's blog"]]
      [:div#piano (piano-divs)]
      [:h2#chord]
      (include-clojurescript "/js/main.js")
      (javascript-tag "chordrecognizer.piano.initPiano();")]))
