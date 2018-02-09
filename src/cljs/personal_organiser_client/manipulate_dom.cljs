(ns personal-organiser-client.manipulate-dom
  (:require [clojure.string :as clstr])
  (:require-macros [personal-organiser-client.html-generator  :as hg]))

; (.querySelector js/document selector)
; (.querySelectorAll js/document selector)

; element.removeEventListener("mousemove", myFunction);

(defn html?
  ""
  [data]
  (< -1(.indexOf (aget (type data) "name") "HTML")))

(defn convert-to-vector
  "Convert html NodeList object to clojure vector"
  [node-list]
  (let [result            (atom [])
        node-list-length  (alength node-list)]
   (doseq [node-index (range 0 node-list-length)]
    (swap! result conj (aget node-list node-index))
    )
   @result))

(defn query-selector
  "Select first element of what css selector fetches from document
   returns single element (html node)"
  [selector]
  (.querySelector js/document selector))

(defn query-selector-all
  "Select all element of what css selector fetches from document
   returns collection of elements (html nodes)"
  [selector]
  (convert-to-vector (.querySelectorAll js/document selector)))

(defn query-selector-on-element
  "Select first element of what css selector fetches from element param
   returns single element (html node)"
  [element
   selector]
  (.querySelector element selector))

(defn query-selector-all-on-element
  "Select all element of what css selector fetches from element param
   returns collection of elements (html nodes)"
  [element
   selector]
  (.querySelectorAll element selector))

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

(defn set-value
  "Sets elements value"
  [element
   new-value]
  (aset element "value" new-value))

(defn get-child-nodes
  "Fetch child nodes of element param"
  [element]
  (convert-to-vector (aget element "childNodes")))

(defn get-parent-node
  "Get parentNode property"
  [element]
  (aget element "parentNode"))

(defn parse-html
  "Parses html from string"
  [html-content]
  (let [parser                  (js/DOMParser.)
        html-dom-content        (.parseFromString parser html-content "text/html")
        html-element            (first (get-child-nodes html-dom-content))
        head-element            (first (get-child-nodes html-element))
        body-element            (first (rest (get-child-nodes html-element)))
        concrete-head-elements  (get-child-nodes head-element)
        concrete-body-elements  (get-child-nodes body-element)]
       (if (= (count concrete-body-elements) 0)
           concrete-head-elements
           concrete-body-elements))
  )

(defn empty-nodes
  "Empty elements feched by selector"
  [selector]
  (let [selected-nodes   (query-selector-all selector)]
   (doseq [sl-node selected-nodes]
    (let [child-nodes      (get-child-nodes sl-node)]
     (doseq [ch-node child-nodes]
      (.removeChild sl-node ch-node))
     ))
   ))

(defn inner-html
  "Set html-content in innerHTML property of elements feched by selector"
  [selector
   html-content]
  (let [selected-nodes   (query-selector-all selector)]
   (doseq [sl-node selected-nodes]
    (aset sl-node "innerHTML" html-content))
   ))

(defn determine-param-type
  ""
  [exec-fn
   param]
  (if (string? param)
   (exec-fn param)
   (if (html? param)
    param
    []))
  )

(defn event
  "Bind function to event on elements fetched by selector
  
   (event param \"click\" #(execute-function))
   
   param - it could be selector as string or html element"
  [param
   event-type
   execute-function]
  (let [selected-nodes   (determine-param-type query-selector-all param)]
   (if (vector? selected-nodes)
    (doseq [sl-node selected-nodes]
     (.addEventListener sl-node event-type #(execute-function sl-node))
     )
    (.addEventListener selected-nodes event-type #(execute-function selected-nodes))
    ))
  )

(defn prepend-element
  "Prepend html string in elements fetched by selector"
  [selector
   html-content]
  (let [selected-nodes      (query-selector-all selector)
        child-nodes         (determine-param-type parse-html html-content)]
   (doseq [sl-node selected-nodes]
    (doseq [ch-node child-nodes]
     (let [insert-before-this (first (get-child-nodes sl-node))]
      (.insertBefore sl-node ch-node insert-before-this))
     ))
   ))

(defn append-element
  "Append html string in elements fetched by selector"
  [selector
   html-content]
  (let [selected-nodes     (query-selector-all selector)
        child-nodes        (determine-param-type parse-html html-content)]
   (doseq [sl-node selected-nodes]
    (if (vector? child-nodes)
     (doseq [ch-node child-nodes]
      (.appendChild sl-node ch-node))
     (.appendChild sl-node child-nodes))
    ))
  )

(defn content
  "Empty fetched elements by selector and append html string"
  [selector
   html-content]
  (empty-nodes selector)
  (append-element selector html-content))

(defn remove-node
  "Remove elements fetched by selector"
  [selector]
  (let [selected-nodes   (query-selector-all selector)]
   (doseq [sl-node selected-nodes]
    (.removeChild (get-parent-node sl-node) sl-node))
   ))

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
  (doseq [element elements]
   (set-attr element attr-name value))
  )

(defn get-class-list
  "Get classList property"
  [element]
  (aget element "classList"))

(defn get-node-name
  "Get nodeName property"
  [element]
  (aget element "nodeName"))

(defn add-class
  "Add class to an element"
  [element
   single-class]
  (let [element-class-list (get-class-list element)]
   (.add element-class-list single-class))
  )

(defn add-classes
  "Add class to elements collection"
  [elements
   single-class]
  (doseq [element elements]
   (add-class element single-class))
  )

(defn remove-class
  "Remove class from an element"
  [element
   single-class]
  (let [element-class-list (get-class-list element)]
   (.remove element-class-list single-class))
  )

(defn remove-classes
  "Remove class from elements collection"
  [elements
   single-class]
  (doseq [element elements]
   (remove-class element single-class))
  )

(hg/deftmpl fade-template "public/css/animation/fade-template.css")

(defn element-exists
  "Check if fade in style exists in DOM"
  [selector]
  (let [selected-elements (query-selector-all selector)]
   (not (empty? selected-elements))
   ))

(defn- fade-anim-append
  "Append fade in style to head"
  [delay-time
   style-id
   animation-name-class
   from-opacity
   to-opacity]
  (if-not (element-exists (str "style#" style-id))
   (let [delay-time-as-string (str (float (/ delay-time 1000))
                               )
         replaced-duration    (clstr/replace fade-template
                                             #"animation-duration"
                                             delay-time-as-string)
         replaced-id          (clstr/replace replaced-duration
                                             #"style-identification"
                                             style-id)
         replaced-name-class  (clstr/replace replaced-id
                                             #"animation-name-class"
                                             animation-name-class)
         replaced-from        (clstr/replace replaced-name-class
                                             #"from-opacity"
                                             (str from-opacity))
         replaced-to          (clstr/replace replaced-from
                                             #"to-opacity"
                                             (str to-opacity))]
    (append-element "head" replaced-to))
   nil))

(defn fade-in-ieration
  ""
  [ch-node
   sl-node
   anim-name-class
   style-id
   delay-time]
  (let [node-name           (get-node-name ch-node)
        insert-before-this  (query-selector-on-element sl-node "div.scripts")]
   (if-not (= "#text" node-name)
    (do (add-class ch-node anim-name-class)
        (.insertBefore sl-node ch-node insert-before-this)
        (timeout #(do (remove-class ch-node anim-name-class)
                      (remove-node (str "style#" style-id)))
                 delay-time))
    nil))
  )

(defn fade-in
  "Fade in html string content in elements fetched by selector during delay time
  
  
  parameters:
   selector              query document with this selector
   html-content          String or HTMLObject that will be inserted
   delay-time            fade-in duration time
   
   style-identification  id of style html element that will be generated and appended
   animation-name-class  generating style html element, this parameter will be used for
                           animation and class name
   from-opacity          number from 0.0 to 1.0 that will specify starting opacity of
                           fading element
   to-opacity            number from 0.0 to 1.0 that will specify ending opacity of
                           fading element
  "
  [selector
   html-content
   ^int delay-time
   & [style-identification
      animation-name-class
      from-opacity
      to-opacity]]
  (let [style-id           (str style-identification "fade-in")
        anim-name-class    (str animation-name-class "fade-in")
        from-opac          (or from-opacity 0)
        to-opac            (or to-opacity 1)]
   (fade-anim-append delay-time
                     style-id
                     anim-name-class
                     from-opac
                     to-opac)
   (let [selected-nodes     (query-selector-all selector)
         child-nodes        (determine-param-type parse-html html-content)]
    (doseq [sl-node selected-nodes]
     (if (vector? child-nodes)
      (doseq [ch-node child-nodes]
       (fade-in-ieration ch-node sl-node anim-name-class style-id delay-time))
      (fade-in-ieration child-nodes sl-node anim-name-class style-id delay-time))
     ))
   ))

(defn fade-out
  "Fade out html string content in elements fetched by selector during delay time
  
  parameters:
   selector              query document with this selector
   delay-time            fade-out duration time
   
   style-identification  id of style html element that will be generated and appended
   only-content          when removing faded element
                           true for only for content to be remove
                           false for element, fetched with selector, to be removed
   animation-name-class  generating style html element, this parameter will be used for
                           animation and class name
   from-opacity          number from 0.0 to 1.0 that will specify starting opacity of
                           fading element
   to-opacity            number from 0.0 to 1.0 that will specify ending opacity of
                           fading element
  "
  [selector
   ^int delay-time
   & [style-identification
      only-content
      animation-name-class
      from-opacity
      to-opacity]]
  (let [style-id           (str style-identification "fade-out")
        anim-name-class    (str animation-name-class "fade-out")
        from-opac          (or from-opacity 1)
        to-opac            (or to-opacity 0)]
   (fade-anim-append delay-time
                     style-id
                     anim-name-class
                     from-opac
                     to-opac)
   (let [selected-nodes   (query-selector-all selector)]
    (doseq [sl-node selected-nodes]
     (let [child-nodes    (get-child-nodes sl-node)]
      (if only-content
       (doseq [ch-node child-nodes]
        (let [node-name  (get-node-name ch-node)]
         (if-not (= "#text" node-name)
          (do (remove-class ch-node (str animation-name-class "fade-in"))
              (add-class ch-node anim-name-class))
          nil))
        )
       (add-class sl-node anim-name-class))
      ))
    )
   (timeout #(do (if only-content
                     (remove-node (str "." anim-name-class))
                     (remove-node selector))
                 (remove-node (str "style#" style-id))
              )
            delay-time))
  )

(defn- generate-thead
  "Generate thead for table"
  [table-node
   data-header]
  (swap! table-node str "<thead><tr>")
  (let [data-header-values (into [] (map vals data-header))]
   (doseq [hvalue data-header-values]
    (let [hvalue-unwrapped (first hvalue)]
     (swap! table-node str "<th"
                           (if (:colspan hvalue-unwrapped)
                               (str " colspan=" (:colspan hvalue-unwrapped))
                               "")
                           ">"
                           (:content hvalue-unwrapped)
                           "</th>"))
    ))
  (swap! table-node str "</tr></thead>"))

(defn- generate-tbody
  "Generate tbody for table"
  [table-node
   data-list]
  (swap! table-node str "<tbody>")
  (doseq [data-row data-list]
   (swap! table-node str "<tr>")
    (doseq [data-col-value data-row]
     (swap! table-node str "<td>"
                           data-col-value
                           "</td>"))
   (swap! table-node str "</tr>"))
  (swap! table-node str "</tbody>"))

(defn table-with-data
  "Generate table with data"
  [data-header
   data-list
   table-attrs]
  (let [table-node (atom "")]
   (swap! table-node str "<table ")
   (doseq [[attr-name attr-value] table-attrs]
    (swap! table-node str " " attr-name "=\"" attr-value "\""))
   (swap! table-node str " >")
   (generate-thead table-node data-header)
   (generate-tbody table-node data-list)
   (swap! table-node str "</table>")
   @table-node))

(def anim-time 300)

(defn start-please-wait
  "Display please wait message"
  []
  (fade-in "body"
           "<div class=\"please-wait-anim\" ></div>"
           anim-time
           "please-wait-anim-id"
           "please-wait-anim")
  (fade-in "body"
           "<div class=\"please-wait-background\" ></div>"
           anim-time
           "please-wait-background-id"
           "please-wait-background"
           0
           0.2))

(defn end-please-wait
  "Hide please wait message"
  []
  (fade-out "div.please-wait-anim"
            anim-time
            "please-wait-anim-id"
            false
            "please-wait-anim")
  (fade-out "div.please-wait-background"
            anim-time
            "please-wait-background-id"
            false
            "please-wait-background"
            0.2
            0))

(defn is-checked?
  ""
  [element]
  (get-attr element "checked"))

(defn checked-value-with-index
  ""
  [radio-group-elements
   index]
  (if (< index (count radio-group-elements))
   (if (is-checked? (radio-group-elements index))
    (get-value (radio-group-elements index))
    (recur radio-group-elements (inc index))
    )
   nil))

(defn checked-value
  ""
  [radio-group-name]
  (let [radio-group-elements (query-selector-all (str "input[name='" radio-group-name "']"))]
   (checked-value-with-index radio-group-elements 0))
  )

