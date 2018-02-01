(ns personal-organiser-client.manipulate-dom
  (:require [clojure.string :as clstr])
  )

; (.querySelector js/document selector)
; (.querySelectorAll js/document selector)

; element.removeEventListener("mousemove", myFunction); 

(defn query-selector
  ""
  [selector]
  (.querySelector js/document selector))

(defn query-selector-all
  ""
  [selector]
  (.querySelectorAll js/document selector))

(defn get-by-id
  ""
  [element-id]
  (.getElementById js/document element-id))

(defn get-by-class
  ""
  [element-class]
  (.getElementsByClassName js/document element-class))

(defn get-value
  ""
  [element]
  (aget element "value"))

(defn remove-child-nodes
  ""
  [selector]
  (let [selected-nodes   (query-selector-all selector)
        sl-index-range   (range (- (aget selected-nodes "length") 1) -1 -1)]
   (doseq [sl-node-index sl-index-range]
    (let [selected-node    (aget selected-nodes sl-node-index)
          child-nodes      (aget selected-node "childNodes")
          ch-index-range   (range (- (aget child-nodes "length") 1) -1 -1)]
     (doseq [ch-node-index ch-index-range]
      (.removeChild selected-node (aget (aget selected-node "childNodes") ch-node-index))
      ))
    ))
  )

(defn inner-html
  ""
  [selector
   html-content]
  (let [selected-nodes   (query-selector-all selector)
        sl-index-range   (range (- (aget selected-nodes "length") 1) -1 -1)]
   (doseq [sl-node-index sl-index-range]
    (let [selected-node    (aget selected-nodes sl-node-index)]
     (aset selected-node "innerHTML" html-content))
    ))
  )

(defn event
  ""
  [selector
   event-type
   execute-function]
  (let [selected-nodes   (query-selector-all selector)
        sl-index-range   (range (- (aget selected-nodes "length") 1) -1 -1)]
   (doseq [sl-node-index sl-index-range]
    (let [selected-node    (aget selected-nodes sl-node-index)]
     (.addEventListener selected-node event-type execute-function))
    ))
  )

(defn parse-html
  ""
  [html-content]
  (let [parser             (js/DOMParser.)
        html-dom-content   (.parseFromString parser html-content "text/html")
        html-element       (aget (aget html-dom-content "childNodes") 0)
        body-element       (aget (aget html-element "childNodes") 1)
        concrete-element   (aget body-element "childNodes")]
   concrete-element))

(defn prepend-element
  ""
  [selector
   html-content]
  (let [selected-nodes     (query-selector-all selector)
        sl-index-range     (range (- (aget selected-nodes "length") 1) -1 -1)
        concrete-element   (parse-html (clstr/replace html-content #"\n" ""))
        child-nodes-length (aget concrete-element "length")
        ch-index-range     (range (- child-nodes-length 1) -1 -1)]
   (doseq [sl-node-index sl-index-range]
    (let [selected-node      (aget selected-nodes sl-node-index)]
     (doseq [ch-node-index ch-index-range]
      (let [concrete-content   (aget concrete-element ch-node-index)
            insert-before-this (aget (aget selected-node "childNodes") 0)]
       (.insertBefore selected-node concrete-content insert-before-this))
      ))
    ))
  )

(defn append-element
  ""
  [selector
   html-content]
  (let [selected-nodes     (query-selector-all selector)
        sl-index-range     (range (- (aget selected-nodes "length") 1) -1 -1)
        concrete-element   (parse-html (clstr/replace html-content #"\n" ""))
        child-nodes-length (aget concrete-element "length")
        ch-index-range     (range (- child-nodes-length 1) -1 -1)]
   (doseq [sl-node-index sl-index-range]
    (let [selected-node    (aget selected-nodes sl-node-index)]
     (doseq [ch-node-index ch-index-range]
      (let [concrete-content   (aget concrete-element ch-node-index)]
       (.appendChild selected-node concrete-content))
      ))
    ))
  )

(defn content
  ""
  [selector
   html-content]
  (remove-child-nodes selector)
  (prepend-element selector html-content))

(defn remove-node
  ""
  [selector]
  (let [selected-nodes   (query-selector-all selector)
        sl-index-range   (range (- (aget selected-nodes "length") 1) -1 -1)]
   (doseq [sl-node-index sl-index-range]
    (let [selected-node    (aget selected-nodes sl-node-index)
          parent-node    (aget selected-node "parentNode")]
     (.removeChild parent-node selected-node))
    ))
  )

(defn timeout
  ""
  [execute-fn
   delay-time]
  (js/setTimeout execute-fn
                 delay-time))

(defn get-attr
  ""
  [element
   attr-name]
  (.getAttribute element attr-name))

(defn set-attr
  ""
  [element
   attr-name
   value]
  (.setAttribute element attr-name value))

(defn set-attrs
  ""
  [elements
   attr-name
   value]
  (let [sl-nodes-length  (aget elements "length")
        sl-index-range   (range (- sl-nodes-length 1) -1 -1)]
   (doseq [sl-node-index sl-index-range]
    (set-attr (aget elements sl-node-index) attr-name value))
   ))

(defn add-class
  ""
  [element
   single-class]
  (let [element-class-list (aget element "classList")]
   (.add element-class-list single-class))
  )

(defn add-classes
  ""
  [elements
   single-class]
  (let [sl-nodes-length  (aget elements "length")
        sl-index-range   (range (- sl-nodes-length 1) -1 -1)]
   (doseq [sl-node-index sl-index-range]
    (add-class (aget elements sl-node-index) single-class))
   ))

(defn remove-class
  ""
  [element
   single-class]
  (let [element-class-list (aget element "classList")]
   (.remove element-class-list single-class))
  )

(defn remove-classes
  ""
  [elements
   single-class]
  (let [sl-nodes-length  (aget elements "length")
        sl-index-range   (range (- sl-nodes-length 1) -1 -1)]
   (doseq [sl-node-index sl-index-range]
    (remove-class (aget elements sl-node-index) single-class)
    ))
  )

(defn fade-in
  ""
  [selector
   html-content
   ^int delay-time]
  (let [selected-nodes     (query-selector-all selector)
        sl-index-range     (range (- (aget selected-nodes "length") 1) -1 -1)
        concrete-element   (parse-html (clstr/replace html-content #"\n" ""))
        child-nodes-length (aget concrete-element "length")
        ch-index-range     (range (- child-nodes-length 1) -1 -1)]
   (doseq [sl-node-index sl-index-range]
    (let [selected-node    (aget selected-nodes sl-node-index)]
     (doseq [ch-node-index ch-index-range]
      (let [concrete-content   (aget concrete-element ch-node-index)
            insert-before-this (aget (aget selected-node "childNodes") 0)]
       (add-class concrete-content "fade-in")
       (.insertBefore selected-node concrete-content insert-before-this)
       (timeout #(remove-class concrete-content "fade-in")
                delay-time))
      ))
    ))
  )

(defn fade-out
  ""
  [selector
   ^int delay-time]
  (let [elements         (query-selector-all selector)
        sl-nodes-length  (aget elements "length")
        sl-index-range   (range (- sl-nodes-length 1) -1 -1)]
   (doseq [sl-node-index sl-index-range]
    (add-class (aget elements sl-node-index) "fade-out"))
   )
  (timeout #(remove-node selector)
           delay-time))

