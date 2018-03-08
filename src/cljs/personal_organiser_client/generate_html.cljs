(ns personal-organiser-client.generate-html)

(def test-html
 {:el  "html"
  :cont  [{:el  "head"}
          {:el  "body"
           :cont [{:el  "div"
                   :attrs  [["style" (str "width: 50px;"
                                          "height: 50px;"
                                          "background-color: red;"
                                          "position: absolute;")]
                            ["id" "test"]]
                   :events  [["onclick" (fn [] (.log js/console "1"))]]}]
           }]})

(defn testna-fn
  ""
  [this
   param1]
  (.log js/console this)
  (.log js/console param1))

(def new-select
 {:el "select"
  :events {:onclick {:func testna-fn
                     :param {:key1 "jedan"
                             :key2 "dva"}}
           }
  :attrs {:style {:width "50px"
                  :height "50px"
                  :background-color "red"
                  :position "absolute"}
          :id "test"}
  :cont [{:el "option"
          :attrs [["value" "1"]]
          :cont "jedan"}
         {:el "option"
          :attrs [["value" "2"]]
          :cont "dva"}]})

;(gh/gen
; (gh/cr
;  "select"
;  [(gh/cr "option"
;          "jedan"
;          {:value "1"})
;   (gh/cr "option"
;          "dva"
;          {:value "2"})]
;  {:style {:width "50px"
;           :height "50px"
;           :background-color "red"
;           :position "absolute"}}))

(defn crt
  ""
  [el
   & [cont
      attrs
      events]]
  {:el el
   :events events
   :attrs attrs
   :cont cont})

(defn- generate-html
  ""
  [data]
  (if (map? data)
    (let [el (:el data)
          cont (:cont data)
          new-element (.createElement js/document el)
          attrs (:attrs data)
          events (:events data)]
     (doseq [[attr-name
              attr-value] attrs]
      (let [attr-cont (atom "")]
       (if (and (= attr-name
                   :style)
                (map? attr-value))
        (doseq [[prop-name
                 prop-value] attr-value]
         (swap! attr-cont str (name prop-name)
                              ": "
                              prop-value
                              "; "))
        (swap! attr-cont str attr-value))
       (.setAttribute new-element (name attr-name)
                                  @attr-cont))
      )
     (doseq [[evt-name
              {evt-fn :evt-fn
               evt-p :evt-p}] events]
      (aset new-element (name evt-name)
                        #(evt-fn evt-p
                                 new-element)
       ))
     (if (or (string? cont)
             (number? cont))
      (aset new-element "innerHTML" cont)
      (if (vector? cont)
       (doseq [cont-element cont]
        (.appendChild new-element (generate-html cont-element))
        )
       (if (map? cont)
        (.appendChild new-element (generate-html cont))
        ""))
      )
     new-element)
   (if (vector? data)
    (let [generated-htmls (atom [])]
     (doseq [data-element data]
      (swap! generated-htmls conj (generate-html data-element))
      )
     @generated-htmls)
    nil))
  )

(defn anmtn
  ""
  [animation-name
   from-props
   to-props]
  {:anim (str "@keyframes "
              animation-name)
   :from from-props
   :to to-props})

(defn slctr
  ""
  [selector
   props-map]
  {:sel selector
   :props props-map})

(defn stl
  ""
  [id
   & cont]
  {:attrs {:id id
           :type "text/css"}
   :cont (vec cont)})

(defn- form-style-content
  ""
  [data]
  (let [sel (:sel data)
        props (:props data)
        anim (:anim data)
        from-props (:from data)
        to-props (:to data)
        content (atom "")]
   (if sel
    (do
     (swap! content str sel
                        " { ")
     (doseq [[prop-name
              prop-value] props]
      (swap! content str (name prop-name)
                         ": "
                         prop-value
                         "; ")
      ))
    (do
     (swap! content str anim
                        " { "
                        "from { ")
     (doseq [[prop-name
              prop-value] from-props]
      (swap! content str (name prop-name)
                         ": "
                         prop-value
                         "; "))
     (swap! content str "} ")
     (swap! content str "to { ")
     (doseq [[prop-name
              prop-value] to-props]
      (swap! content str (name prop-name)
                         ": "
                         prop-value
                         "; "))
     (swap! content str "} "))
    )
   (swap! content str "} ")
   @content))

(defn- generate-style
  ""
  [data]
  (if (map? data)
   (let [new-element (.createElement js/document "style")
         attrs (:attrs data)
         cont (:cont data)]
    (if (string? cont)
     (aset new-element "innerHTML" cont)
     (if (vector? cont)
      (doseq [cont-element cont]
       (aset new-element "innerHTML" (str (aget new-element "innerHTML")
                                          " "
                                          (form-style-content cont-element))
        ))
      (if (map? cont)
       (aset new-element "innerHTML" (form-style-content cont))
       (aset new-element "innerHTML" (form-style-content data))
       ))
     )
    (doseq [[attr-name
             attr-value] attrs]
     (.setAttribute new-element (name attr-name)
                                attr-value))
    new-element)
   nil))

(defn gen
  ""
  [data
  & [tag-type]]
  (if (= tag-type
         "style")
   (generate-style data)
   (generate-html data))
  )

