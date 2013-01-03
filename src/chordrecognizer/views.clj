(ns chordrecognizer.views
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
      [:title "Hello World"]
      (include-css "css/piano.css")]
    [:body
      [:h1 "Hello World"]
      [:p#chord]
      (piano-divs)
      (include-clojurescript "/js/main.js")
      (javascript-tag "chordrecognizer.piano.initPiano();")]))
