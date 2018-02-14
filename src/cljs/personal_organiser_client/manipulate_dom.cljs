(ns personal-organiser-client.manipulate-dom
  (:require-macros [personal-organiser-client.html-generator  :as hg]))

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

(defn replace-single
  ""
  [str-content
   replace-this
   replace-with]
  (.replace str-content
            replace-this
            replace-with))

(defn replace-all
  "Replace every occurance of supplied string"
  [str-content
   replace-this
   replace-with]
  (let [result (atom str-content)]
   (if (< -1 (.indexOf @result replace-this))
    (recur (reset! result
                   (.replace str-content
                             replace-this
                             replace-with))
           replace-this
           replace-with)
    @result))
  )

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

(defn get-inner-html
  "Get innerHTML property of first element feched by selector"
  [selector]
  (let [sl-node   (query-selector selector)]
    (aget sl-node "innerHTML"))
  )

(defn set-inner-html
  "Set html-content as innerHTML property of elements feched by selector"
  [selector
   html-content]
  (let [selected-nodes   (query-selector-all selector)]
   (doseq [sl-node selected-nodes]
    (aset sl-node "innerHTML" html-content))
   ))

(defn get-outer-html
  "Get outerHTML property of first element feched by selector"
  [selector]
  (let [sl-node   (query-selector selector)]
    (aget sl-node "outerHTML"))
  )

(defn set-outer-html
  "Set html-content as outerHTML property of elements feched by selector"
  [selector
   html-content]
  (let [selected-nodes   (query-selector-all selector)]
   (doseq [sl-node selected-nodes]
    (aset sl-node "outerHTML" html-content))
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

(defn- add-fn-to-event
  "Add function to event for particular element"
  [element
   event-type
   event-function
   & [fn-params]]
  (let [event-funcs      (str event-type "-funcs")]
   (aset element
         event-funcs
         (assoc (aget element event-funcs)
                (str event-function)
                #(event-function fn-params element))
    )
   (if-not (aget element event-type)
    (aset element
          event-type
          #(doseq [func (into []
                              (map val
                                   (aget element
                                         event-funcs))
                         )]
            (func))
     )
    nil))
  )

(defn event
  "Bind function to event on elements fetched by selector
  
   (event element \"onclick\" event-function [1 2 3]))
   
   element         Represents selector or html element
   event-type      Represents html event, like onclick, onload...
   event-function  Represents function name
   [fn-params]     Represents vector that will be passed to event function"
  [element
   event-type
   event-function
   & [fn-params]]
  (let [selected-nodes   (determine-param-type query-selector-all element)]
   (if (vector? selected-nodes)
    (doseq [sl-node selected-nodes]
     (add-fn-to-event sl-node event-type event-function fn-params))
    (add-fn-to-event selected-nodes event-type event-function fn-params))
   ))

(defn- remove-fn-from-event
  ""
  [element
   event-type
   event-function]
  (let [event-funcs      (str event-type "-funcs")]
   (aset element
         event-funcs
         (dissoc (aget element event-funcs)
                 (str event-function))
    )
   (if (empty? (aget element event-funcs))
    (do (aset element
              event-type
              nil)
        (aset element
              event-funcs
              nil))
    nil))
  )

(defn remove-event
  "Remove function from event on elements fetched by selector
  
   (remove-event element \"onclick\" event-function))
   
   element         Represents selector or html element
   event-type      Represents html event, like onclick, onload...
   event-function  Represents function name"
  [element
   event-type
   event-function]
  (let [selected-nodes   (determine-param-type query-selector-all element)]
   (if (vector? selected-nodes)
    (doseq [sl-node selected-nodes]
     (remove-fn-from-event sl-node event-type event-function))
    (remove-fn-from-event element event-type event-function)
    ))
  )

(defn- remove-all-fns-from-event
  ""
  [element
   event-type]
  (let [event-funcs      (str event-type "-funcs")]
   (aset element
         event-type
         nil)
   (aset element
         event-funcs
         nil))
   )

(defn remove-all-event
  ""
  [element
   event-type]
  (let [selected-nodes   (determine-param-type query-selector-all element)]
   (if (vector? selected-nodes)
    (doseq [sl-node selected-nodes]
     (remove-all-fns-from-event sl-node event-type))
    (remove-all-fns-from-event element event-type))
   ))

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
  "Add class to elements collection"
  [elements
   single-class]
  (let [elements (determine-param-type query-selector-all elements)]
   (if (vector? elements)
    (doseq [element elements]
     (.add (get-class-list element) single-class))
    (.add (get-class-list elements) single-class))
   ))

(defn remove-class
  "Remove class from elements collection"
  [elements
   single-class]
  (let [elements (determine-param-type query-selector-all elements)]
   (if (vector? elements)
    (doseq [element elements]
     (.remove (get-class-list element) single-class))
    (.remove (get-class-list elements) single-class))
   ))

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
         replaced-duration    (replace-all fade-template
                                           "animation-duration"
                                           delay-time-as-string)
         replaced-id          (replace-all replaced-duration
                                           "style-identification"
                                           style-id)
         replaced-name-class  (replace-all replaced-id
                                           "animation-name-class"
                                           animation-name-class)
         replaced-from        (replace-all replaced-name-class
                                           "from-opacity"
                                           (str from-opacity))
         replaced-to          (replace-all replaced-from
                                           "to-opacity"
                                           (str to-opacity))]
    (append-element "body div.styles" replaced-to))
   nil))

(defn fade-in-iteration
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
                      (set-outer-html (str "style#" style-id) "")
                      (set-inner-html 
                       "div.styles"
                       (replace-all (get-inner-html "div.styles")
                                    "\n"
                                    "")))
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
       (fade-in-iteration ch-node sl-node anim-name-class style-id delay-time))
      (fade-in-iteration child-nodes sl-node anim-name-class style-id delay-time))
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
                     (set-outer-html (str "." anim-name-class) "")
                     (set-outer-html selector ""))
                 (set-outer-html (str "style#" style-id) "")
                 (set-inner-html 
                  "div.styles"
                  (replace-all (get-inner-html "div.styles")
                               "\n"
                               ""))
              )
            delay-time))
  )

(hg/deftmpl column-style "public/css/cell-style.css")

(defn append-cell-style
  ""
  [th-td-style
   style-template]
  (doseq [[p-name p-value] th-td-style]
   (swap! style-template
          replace-single
          "/**/"
          (str p-name
               ": "
               p-value
               ";\n/**/"))
   )
  @style-template)

(defn- append-column-style
  ""
  [style-id
   selector
   th-td-style]
  (if-not (element-exists (str "style#" style-id))
   (let [replaced-id         (replace-single column-style
                                                 "style-identification"
                                                 style-id)
         replaced-selector   (replace-single replaced-id
                                                 "selector-placeholder"
                                                 selector)
         template-final      (if (map? @th-td-style)
                              (do (if-not (contains? @th-td-style "width")
                                   (swap! th-td-style assoc "width" "auto")
                                   nil)
                                  (if-not (contains? @th-td-style "text-align")
                                   (swap! th-td-style assoc "text-align" "center")
                                   nil)
                                  (if-not (contains? @th-td-style "text-overflow")
                                   (swap! th-td-style assoc "text-overflow" "ellipsis")
                                   nil)
                                  (if-not (contains? @th-td-style "overflow")
                                   (swap! th-td-style assoc "overflow" "hidden")
                                   nil)
                                  (if-not (contains? @th-td-style "padding")
                                   (swap! th-td-style assoc "padding" "0 5px")
                                   nil)
                               (append-cell-style (into [] @th-td-style)
                                                  (atom replaced-selector))
                               )
                              (append-cell-style [["width"         "auto"]
                                                  ["text-align"    "center"]
                                                  ["text-overflow" "ellipsis"]
                                                  ["overflow"      "hidden"]
                                                  ["padding"       "0 5px"]]
                                                 (atom replaced-selector))
                              )]
    (append-element "body div.styles" template-final))
   nil))

(defn- append-td-th-style
  ""
  [th-td-style
   table-class
   column-index
   cell-type]
  (let [style-id   (str table-class
                        "-"
                        cell-type
                        "-"
                        (inc column-index))
        selector   (str "."
                        table-class
                        " table tr "
                        cell-type
                        ":nth-child("
                        (inc column-index)
                        ") div")]
   (append-column-style style-id
                        selector
                        (atom th-td-style)))
  )

(defn- generate-th
  ""
  [cell-styles
   table-node
   table-class
   th-index]
  (if (< th-index (count cell-styles))
   (let [cell-style     (cell-styles th-index)
         th-style       (:header cell-style)
         column-style   (:column cell-style)
         header-colspan (:colspan cell-style)]
    (swap! table-node
           str
           "<th"
           (do ; header style
            (append-td-th-style th-style
                                table-class
                                th-index
                                "th")
            (if header-colspan
             (do ; columns style
                 (if (and column-style
                          (= (count column-style)
                             header-colspan))
                  (let [td-index (atom 0)]
                   (doseq [td-style column-style]
                    (append-td-th-style td-style
                                        table-class
                                        (+ th-index
                                           @td-index)
                                        "td")
                    (swap! td-index inc @td-index))
                   )
                  (doseq [td-index (range 0 header-colspan)]
                   (append-td-th-style td-index
                                       table-class
                                       (+ th-index
                                          td-index)
                                       "td"))
                  )
                 (str " colspan=" header-colspan " "))
             (do ; columns style
                 (append-td-th-style column-style
                                     table-class
                                     th-index
                                     "td")
                 ""))
            )
           "><div"
           " title=\""
           (if (:title cell-style)
            (:title cell-style)
            (:content cell-style))
           "\" >"
           (:content cell-style)
           "</div></th>")
    (recur cell-styles table-node table-class (inc th-index))
    )
   nil))

(defn- generate-thead
  "Generate thead for table"
  [cell-styles
   table-node
   table-class]
  (swap! table-node str "<thead><tr>")
  (generate-th cell-styles table-node table-class 0)
  (swap! table-node str "</tr></thead>"))

(defn- generate-tbody
  "Generate tbody for table"
  [data-vectors
   table-node]
  (swap! table-node str "<tbody>")
  (doseq [data-vector data-vectors]
   (swap! table-node str "<tr>")
    (doseq [data data-vector]
     (swap! table-node str "<td><div"
                           " title=\""
                           (if (map? data)
                            (str (:title data)
                                 "\" >"
                                 (:data data))
                            (str data
                                 "\" >"
                                 data))
                           "</div></td>"))
   (swap! table-node str "</tr>"))
  (swap! table-node str "</tbody>"))

(defn table-with-data
  "Generate table with data
  
  cell-style     Represents vector of maps with attributes
                  [{:content    \"Name\"
                    :title      \"Name\"
                    :header     [[\"width\"      \"100px\"]
                                 [\"text-align\" \"center\"]]
                    :column     [[\"width\"      \"100%\"]
                                 [\"text-align\" \"left\"]]
                    :colspan    2}]
  data-vectors   Represents vector of vectors with n elements that represent
                  values of table columns
                   example of data format:
                    [[\"data of column 1 row 1\"
                      \"data of column 2 row 1\"
                      {:title \"title of column 3 row 1\"
                       :data  \"data of column 3 row 1\"}]
                     [\"data of column 1 row 2\"
                      \"data of column 2 row 2\"
                      {:title \"title of column 3 row 2\"
                       :data  \"data of column 3 row 2\"}]]
  table-class    Represents class of div that contains table that will be generated"
  [cell-styles
   data-vectors
   table-class]
  (let [table-node (atom "")]
   (swap! table-node str "<div class=\""
                         table-class
                         "\" ><table>")
   (generate-thead cell-styles table-node table-class)
   (generate-tbody data-vectors table-node)
   (swap! table-node str "</table></div>")
   @table-node))

(def anim-time 100)

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

