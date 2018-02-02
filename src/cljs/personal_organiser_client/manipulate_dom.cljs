(ns personal-organiser-client.manipulate-dom
  (:require [clojure.string :as clstr])
  (:require-macros [personal-organiser-client.html-generator  :as hg]))

; (.querySelector js/document selector)
; (.querySelectorAll js/document selector)

; element.removeEventListener("mousemove", myFunction); 

(defn parse-html
  "Parses html from string"
  [html-content]
  (let [parser                  (js/DOMParser.)
        html-dom-content        (.parseFromString parser html-content "text/html")
        html-element            (aget (aget html-dom-content "childNodes") 0)
        head-element            (aget (aget html-element "childNodes") 0)
        body-element            (aget (aget html-element "childNodes") 1)
        concrete-head-element   (aget head-element "childNodes")
        concrete-body-element   (aget body-element "childNodes")]
       (if (= (aget concrete-body-element "length") 0)
           concrete-head-element
           concrete-body-element))
  )

(defn query-selector
  "Select first element of what css selector fetches from document
   returns single element (html node)"
  [selector]
  (.querySelector js/document selector))

(defn query-selector-all
  "Select all element of what css selector fetches from document
   returns collection of elements (html nodes)"
  [selector]
  (.querySelectorAll js/document selector))

(defn query-selector-on-element
  "Select first element of what css selector fetches from element param
   returns single element (html node)"
  [selector
   element]
  (.querySelector element selector))

(defn query-selector-all-on-element
  "Select all element of what css selector fetches from element param
   returns collection of elements (html nodes)"
  [selector
   element]
  (.querySelectorAll element selector))

(defn query-selector-on-html-string
  "Select first element of what css selector fetches from html string content param
   returns single element (html node)"
  [selector
   html-content]
  (let [element (parse-html html-content)]
   (.querySelector element selector))
  )

(defn query-selector-all-on-html-string
  "Select all element of what css selector fetches from html string content param
   returns collection of elements (html nodes)"
  [selector
   html-content]
  (let [element (parse-html html-content)]
   (.querySelectorAll element selector))
  )

(defn get-by-id
  "Select element by id
   returns single element (html node)"
  [element-id]
  (.getElementById js/document element-id))

(defn get-by-class
  "Select elements by class
   returns collection of elements (html nodes)"
  [element-class]
  (.getElementsByClassName js/document element-class))

(defn get-value
  "Returns elements value"
  [element]
  (aget element "value"))

(defn remove-child-nodes
  "Empty elements feched by selector"
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
  "Set html-content in innerHTML property of elements feched by selector"
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
  "Bind function to event on elements fetched by selector
  
   (event selector \"click\" #(execute-function))"
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

(defn prepend-element
  "Prepend html string in elements fetched by selector"
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
  "Append html string in elements fetched by selector"
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
  "Empty fetched elements by selector and append html string"
  [selector
   html-content]
  (remove-child-nodes selector)
  (append-element selector html-content))

(defn remove-node
  "Remove elements fetched by selector"
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
  "Delay function execution by miliseconds"
  [execute-fn
   delay-time]
  (js/setTimeout execute-fn
                 delay-time))

(defn get-attr
  "Get attribute's value of element"
  [element
   attr-name]
  (.getAttribute element attr-name))

(defn set-attr
  "Set attribute's value of element"
  [element
   attr-name
   value]
  (.setAttribute element attr-name value))

(defn set-attrs
  "Set attribute's value of elements collection"
  [elements
   attr-name
   value]
  (let [sl-nodes-length  (aget elements "length")
        sl-index-range   (range (- sl-nodes-length 1) -1 -1)]
   (doseq [sl-node-index sl-index-range]
    (set-attr (aget elements sl-node-index) attr-name value))
   ))

(defn add-class
  "Add class to an element"
  [element
   single-class]
  (let [element-class-list (aget element "classList")]
   (.add element-class-list single-class))
  )

(defn add-classes
  "Add class to elements collection"
  [elements
   single-class]
  (let [sl-nodes-length  (aget elements "length")
        sl-index-range   (range (- sl-nodes-length 1) -1 -1)]
   (doseq [sl-node-index sl-index-range]
    (add-class (aget elements sl-node-index) single-class))
   ))

(defn remove-class
  "Remove class from an element"
  [element
   single-class]
  (let [element-class-list (aget element "classList")]
   (.remove element-class-list single-class))
  )

(defn remove-classes
  "Remove class from elements collection"
  [elements
   single-class]
  (let [sl-nodes-length  (aget elements "length")
        sl-index-range   (range (- sl-nodes-length 1) -1 -1)]
   (doseq [sl-node-index sl-index-range]
    (remove-class (aget elements sl-node-index) single-class))
   )
  )

(hg/deftmpl fade-in-style "public/css/animation/fade-in.css")

(defn element-exists
  "Check if fade in style exists in DOM"
  [selector]
  (let [selected-elements (query-selector-all selector)]
   (if (= (aget selected-elements "length") 0)
    false
    true))
  )

(defn fade-in-anim-append
  "Append fade in style to head"
  [delay-time]
  (if-not (element-exists "style#fade-in")
   (let [fade-in-style-with-duration (clstr/replace fade-in-style
                                                    #"animation-duration"
                                                    (str (float (/ delay-time 1000))
                                                     ))]
    (append-element "head" fade-in-style-with-duration))
   nil))

(defn fade-in-anim-remove
  "Remove fade in style from head"
  []
  (remove-node "style#fade-in"))

(defn fade-in
  "Fade in html string content in elements fetched by selector during delay time"
  [selector
   html-content
   ^int delay-time]
  (fade-in-anim-append delay-time)
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
       (timeout #(do (remove-class concrete-content "fade-in")
                     (fade-in-anim-remove))
                delay-time))
      ))
    ))
  )

(hg/deftmpl fade-out-style "public/css/animation/fade-out.css")

(defn fade-out-anim-append
  "Append fade out style to head"
  [delay-time]
  (if-not (element-exists "style#fade-out")
   (let [fade-out-style-with-duration (clstr/replace fade-out-style
                                                     #"animation-duration"
                                                     (str (float (/ delay-time 1000))
                                                      ))]
    (append-element "head" fade-out-style-with-duration))
   ))

(defn fade-out-anim-remove
  "Remove fade out style from head"
  []
  (remove-node "style#fade-out"))

(defn fade-out
  "Fade out html string content in elements fetched by selector during delay time"
  [selector
   ^int delay-time]
  (fade-out-anim-append delay-time)
  (let [elements         (query-selector-all selector)
        sl-nodes-length  (aget elements "length")
        sl-index-range   (range (- sl-nodes-length 1) -1 -1)]
   (doseq [sl-node-index sl-index-range]
    (add-class (aget elements sl-node-index) "fade-out"))
   )
  (timeout #(do (remove-node selector)
                (fade-out-anim-remove))
           delay-time))

(defn generate-thead
  ""
  [table-node
   data-header]
  (swap! table-node str "<thead><tr>")
  (doseq [[hkey hvalue] (into [] data-header)]
   (swap! table-node str "<th>" hvalue "</th>"))
  (swap! table-node str "</tr></thead>"))

(defn generate-tbody
  ""
  [table-node
   data-header
   data-list]
  (swap! table-node str "<tbody>")
  (doseq [data-row data-list]
   (swap! table-node str "<tr>")
   (let [attr-index (atom 0)]
    (doseq [[hkey hvalue] (into [] data-header)]
     (swap! table-node str "<td>" (hkey data-row) "</td>")
     (swap! attr-index inc))
    )
   (swap! table-node str "</tr>"))
  (swap! table-node str "</tbody>"))

(defn table-with-data
  ""
  [data-header
   data-list]
  (let [table-node (atom "")]
   (swap! table-node str "<table>")
   (generate-thead table-node data-header)
   (generate-tbody table-node data-header data-list)
   (swap! table-node str "</table>")
   @table-node))

