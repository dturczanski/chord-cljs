(ns chordrecognizer.piano
  (:use [domina :only [log attr by-id set-text! classes set-classes!]]
        [domina.css :only [sel]]
        [domina.events :only [listen! target]])
  (:require
    [cljs.reader :as reader]
    [clojure.string :as string]
    [goog.net.XhrIo :as xhrio]
    [goog.net.EventType :as event-type]
    [goog.events :as events]
    [goog.structs.Map :as Map]))

  
  
(defn request [uri callback method data]
  (let [req (new goog.net.XhrIo)]
    (events/listen req goog.net.EventType/COMPLETE callback)
    (. req (send uri
                 method
                 ;; replacing unicode characters until read-string
                 ;; can deal with \x (which is equivalent to \u00).
                 ;; See: http://dev.clojure.org/jira/browse/CLJ-1025
                 (string/replace (pr-str data) #"\\x" "\\u00")
                 (new goog.structs.Map
                      "Content-Type" "text/plain; charset=utf-8")))))

(def keys-pressed (atom #{}))

(defn update-chord-guess [chord]
  (if-not (nil? chord)
    (let [root (name (:root chord))
          type (.substring (name (:chord-type chord)) 1)]
      (set-text! (by-id "chord") (str root " " type)))
    (set-text! (by-id "chord") (str "Chord not recognized"))))


(defn get-chord-guess []
  (request "/chord" (fn [e]
                      (update-chord-guess nil)
                      (let [xhr (.-target e)
                            status (. xhr (getStatus))]
                        (if (= status 200)
                          (let [chord (reader/read-string
                                        (. xhr (getResponseText)))]
                            (update-chord-guess chord))))) 
           "POST" @keys-pressed))

(defn key-clicked [e]
  (let [key-div (.-target (.-evt e))
        key (js/parseInt (attr key-div "value"))
        class-set (set (classes key-div))
        alter-keys! (fn[action]
                      (set-classes! key-div (action class-set "pressed"))
                      (swap! keys-pressed action key))]
    (if (class-set "pressed")
      (alter-keys! disj)
      (alter-keys! conj)))
  ;(log (map #(str % ", ")  @keys-pressed))
  (get-chord-guess))

(defn ^:export initPiano[]
  (let [keys (.getElementsByClassName js/document "key")]
    (listen! keys :click key-clicked)))