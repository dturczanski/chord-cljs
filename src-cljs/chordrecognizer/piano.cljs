(ns chordrecognizer.piano
  (:use [domina :only [log attr by-id set-text! classes set-classes!]]
        [domina.css :only [sel]]
        [domina.events :only [listen! target]]))
  ;(:require [goog.events :as goog.events])

(def keys-pressed (atom #{}))

(defn update-chord-guess [chord]
  (set-text! (by-id "chord") (str "Chord: " chord)))

(defn key-clicked [e]
  (let [key-div (.-target (.-evt e))
        key (js/parseInt (attr key-div "value"))
        class-set (set (classes key-div))]
    (if (class-set "pressed")
      (do
        (set-classes! key-div (disj class-set "pressed"))
        (swap! keys-pressed disj key))
      (do 
        (set-classes! key-div (conj class-set "pressed"))
        (swap! keys-pressed conj key))
      ))
  (log (map #(str % ", ")  @keys-pressed))
  (update-chord-guess (map #(str % ", ")  @keys-pressed)))

(defn ^export initPiano[]
  (let [keys (.getElementsByClassName js/document "key")]
    (listen! keys :click key-clicked)))

(defn ^export sell [expr] (sel expr))