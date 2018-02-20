(ns personal-organiser-client.display-data
  (:require [personal-organiser-client.ajax                :as ajx]
            [personal-organiser-client.manipulate-dom      :as md]
            [personal-organiser-client.http.mime-type      :as mt]
            [personal-organiser-client.http.request-header :as rh]
            [personal-organiser-client.http.entity-header  :as eh]
            [cljs.reader                                   :as reader])
  (:require-macros [personal-organiser-client.html-generator  :as hg]))

(def get-entities-url "/clojure/get-entities")

(def get-entity-url "/clojure/get-entity")

(def update-entity-url "/clojure/update-entity")

(def insert-entity-url "/clojure/insert-entity")

(def delete-entity-url "/clojure/delete-entity")

(def anim-time 100)

(hg/deftmpl column-style "public/css/cell-style.css")

(defn append-cell-style
  ""
  [th-td-style
   style-template]
  (doseq [[p-name p-value] th-td-style]
   (swap! style-template
          md/replace-single
          "/**/"
          (str p-name
               ": "
               p-value
               ";\n/**/"))
   )
  @style-template)

(def new-styles (atom (set '())))

(def remove-styles (atom (set '())))

(defn- append-column-style
  ""
  [style-id
   selector
   th-td-style]
  (swap! new-styles conj style-id)
  (if-not (md/element-exists (str "style#" style-id))
   (let [replaced-id         (md/replace-single column-style
                                                "style-identification"
                                                style-id)
         replaced-selector   (md/replace-single replaced-id
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
    (md/append-element "body div.styles" template-final))
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
   table-class
   actions]
  (swap! table-node str "<thead><tr>")
  (if-not (empty? actions)
   (generate-th (conj cell-styles
                      {:content    "Actions"
                       :colspan    (count actions)})
                table-node
                table-class
                0)
   (generate-th cell-styles
                table-node
                table-class
                0))
  
  (swap! table-node str "</tr></thead>"))

(defn- generate-tbody
  "Generate tbody for table"
  [data-vectors
   table-node
   actions]
  (swap! table-node str "<tbody>")
  (doseq [data-vector data-vectors]
   (swap! table-node str "<tr row=\""
                         (first data-vector)
                         "\" >")
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
   (doseq [action actions]
    (swap! table-node str "<td><div"
                          " title=\""
                          action
                          "\" ><a "
                          action
                          " >"
                          action
                          "</a></div></td>"))
   (swap! table-node str "</tr>"))
  (swap! table-node str "</tbody>"))

(defn details-entity
  ""
  []
  
  )

(defn- render-input
  ""
  [table-str
   data-type
   data
   label
   step]
  (swap! table-str str "<input id=\"txt"
                       label
                       "\" name=\"txt"
                       label
                       "\" type=\""
                       data-type
                       "\""
                       (if step
                        (str " step=\""
                             step
                             "\"")
                        "")
                       " value=\""
                       data
                       "\" required>"))

(defn- render-radio
  ""
  [table-str
   data
   label
   options]
  (doseq [option options]
   (let [input-id  (str label
                        (md/replace-all option
                                        " "
                                        ""))]
    (swap! table-str str "<div><input id=\"r"
                         input-id
                         "\" name=\"r"
                         label
                         "\" type=\"radio\" value=\""
                         option
                         "\""
                         (if (= data option)
                          "checked"
                          "")
                         " required>"
                         "<label id=\"lbl"
                         input-id
                         "\" for=\"r"
                         input-id
                         "\">"
                         option
                         "</label></div>"))
   ))

(defn- is-cb-checked
  ""
  [selected-cbs
   current-index
   option]
  (if (< current-index (count selected-cbs))
   (if (= option (selected-cbs current-index))
    "checked"
    (recur selected-cbs
           (inc current-index)
           option))
   ""))

(defn- render-checkbox
  ""
  [table-str
   selected-cbs
   label
   options]
  (doseq [option options]
   (let [input-id  (str label (md/replace-all option " " ""))]
    (swap! table-str str "<div><input id=\"cb"
                         input-id
                         "\" name=\"cb"
                         input-id
                         "\" type=\"checkbox\" value=\""
                         option
                         "\""
                         (is-cb-checked selected-cbs
                                        0
                                        option)
                         " required>"
                         "<label id=\"lbl"
                         input-id
                         "\" for=\"cb"
                         input-id
                         "\">"
                         option
                         "</label></div>"))
   ))

(defn- render-textarea
  ""
  [table-str
   data
   label]
  (swap! table-str str "<textarea id=\"ta"
                       label
                       "\" name=\"ta"
                       label
                       "\" required>"
                       data
                       "</textarea>"))

(defn- uni-error
  "Handle details error"
  [xhr]
  (let [response      (ajx/get-response xhr)
        error-message (:error-message response)]
   (reset! new-styles (set '()))
   (md/fade-out-and-fade-in ".content"
                            anim-time
                            (str "<div>" error-message "</div>")
                            new-styles
                            remove-styles))
  )

(defn insert-update-entity-success
  ""
  [xhr
   ajax-params]
  (let [table-conf (:conf ajax-params)]
   (table [table-conf])
   ))

(defn insert-update-entity
  ""
  [[conf]
   sl-node]
  (let [action               (:action conf)
        edit-conf            (:edit-conf conf)
        entity-type          (:entity-type edit-conf)
        entity-fields        (:entity-fields edit-conf)
        entity-keys          (vec (keys entity-fields))
        table-node           (md/query-selector (str "." entity-type))
        request-body         {:entity-type  entity-type}
        entity               (atom {})]
   (doseq [e-key entity-keys]
    (let [entity-field   (e-key entity-fields)
          label          (:label entity-field)
          field-type     (:field-type entity-field)
          data-type      (:data-type entity-field)
          id-prefix      (case field-type
                          "input"      "txt"
                          "radio"     "r"
                          "checkbox"  "cb"
                          "textarea"  "ta"
                          "")
          element-id     (str id-prefix label)]
     (case field-type
      "radio"     (swap! entity conj {e-key (md/checked-value element-id)})
      "checkbox"  "cb"
      (let [input-element  (md/query-selector-on-element table-node (str "#" element-id))]
       (swap! entity conj {e-key (md/get-value input-element)}))
      ))
    )
   (ajx/uni-ajax-call
    {:url                  (if (= "Insert" action)
                            insert-entity-url
                            update-entity-url)
     :request-method       "POST"
     :success-fn           insert-update-entity-success
     :error-fn             uni-error
     :request-header-map
      {(rh/accept)       (mt/text-plain)
       (eh/content-type) (mt/text-plain)}
     :request-property-map
      {"responseType" (mt/text-plain)}
     :entity               (assoc request-body :entity @entity)
     :conf                 conf}))
  )

(defn- generate-form
  ""
  [xhr
   ajax-params]
  (let [response       (ajx/get-response xhr)
        entity-data    (:data response)
        form-type      (:form-type ajax-params)
        action         (:action ajax-params)
        action-fn      (:action-fn ajax-params)
        conf           (:conf ajax-params)
        edit-conf      (:edit-conf conf)
        entity-type    (:entity-type edit-conf)
        entity-fields  (:entity-fields edit-conf)
        entity-keys    (vec (keys entity-fields))
        table-str      (atom "")]
   (swap! table-str str "<div class=\""
                        entity-type
                        "\" ><table>")
   (swap! table-str str "<tr><td colspan=\"3\"><h3>"
                        form-type
                        " "
                        entity-type
                        "</h3></td></tr>")
   (doseq [e-key entity-keys]
    (let [field-conf       (e-key entity-fields)
          label            (:label field-conf)
          label-no-spaces  (md/replace-all label " " "")
          field-type       (:field-type field-conf)
          data-type        (:data-type field-conf)
          step             (:step field-conf)
          options          (:options field-conf)
          data             (e-key entity-data)]
     (swap! table-str str "<tr><td><label id=\"lbl"
                          label-no-spaces
                          "\" for=\"txt"
                          label-no-spaces
                          "\">"
                          label
                          "</label></td><td>")
     (case field-type
      "input"     (render-input table-str
                                data-type
                                data
                                label-no-spaces
                                step)
      "radio"     (render-radio table-str
                                data
                                label-no-spaces
                                options)
      "checkbox"  (render-checkbox table-str
                                   data
                                   label-no-spaces
                                   options)
      "textarea"  (render-textarea table-str
                                   data
                                   label-no-spaces)
      nil)
     (swap! table-str str "</td><td id=\"td"
                          label
                          "\"></td></tr>"))
    )
   (swap! table-str str "<tr><td>"
                        "<input id=\"btnCancel"
                        "\" type=\"button\""
                        " value=\"Cancel\""
                        " style=\"float: right;\" >"
                        "</td>"
                        "<td><input id=\"btn"
                              action
                              "\" type=\"button\""
                              " value=\""
                              action
                              "\">"
                        "</td><td></td></tr>"
                        "</table></div>")
   (let [table-node  (first (md/parse-html @table-str))
         action-btn  (md/query-selector-on-element table-node (str "#btn" action))
         cancel-btn  (md/query-selector-on-element table-node (str "#btnCancel"))]
    (md/event cancel-btn
              "onclick"
              table
              [conf])
    (md/event action-btn
              "onclick"
              action-fn
              [conf])
    (reset! new-styles (set '()))
    (md/fade-out-and-fade-in ".content"
                             anim-time
                             table-node
                             new-styles
                             remove-styles))
   ))

(defn entity-details
  ""
  [[conf]]
  (generate-form nil
                 (assoc {}
                        :conf       conf
                        :form-type  "Details"
                        :action     "Edit"
                        :action-fn  entity-edit))
  )

(defn create-entity
  ""
  [[conf]]
  (generate-form nil
                 (assoc {}
                        :conf       conf
                        :form-type  "Create"
                        :action     "Insert"
                        :action-fn  insert-update-entity))
  )

(defn entity-edit
  ""
  [[conf]
   sl-node]
  (let [entity        (:edit-conf conf)
        entity-type   (:entity-type entity)
        entity-id     (:entity-id entity)
        request-body  {:entity-type  entity-type
                       :query        {entity-id (md/get-attr
                                                 (md/ancestor sl-node 3)
                                                 "row")}}]
   (ajx/uni-ajax-call
    {:url                  get-entity-url
     :request-method       "POST"
     :success-fn           generate-form
     :error-fn             uni-error
     :request-header-map
      {(rh/accept)       (mt/text-plain)
       (eh/content-type) (mt/text-plain)}
     :request-property-map
      {"responseType" (mt/text-plain)}
     :entity               request-body
     :conf                 conf
     :form-type            "Edit"
     :action               "Update"
     :action-fn            insert-update-entity}))
  )

(defn entity-delete
  ""
  []
  
  )

(defn entity-table-success
  ""
  [xhr
   ajax-params]    
  (let [table-str               (atom "")
        conf                    (:conf ajax-params)
        table-class             (:table-class conf)
        header-and-cell-styles  (:header-and-cell-styles conf)
        response                (ajx/get-response xhr)
        entities                (:data response)
        render-in               (:render-in conf)
        animation               (:animation conf)
        animation-duration      (:animation-duration conf)
        details-conf            (:details-conf conf)
        edit-conf               (:edit-conf conf)
        delete-conf             (:delete-conf conf)
        actions                 (reduce
                                 (fn [acc elem]
                                     (if elem
                                      (conj acc elem)
                                      acc))
                                 []
                                 [(if details-conf
                                          "details"
                                          nil)
                                  (if edit-conf
                                          "edit"
                                          nil)
                                  (if delete-conf
                                          "delete"
                                          nil)])]
   (swap! table-str str "<div class=\""
                         table-class
                         "\" ><table>")
   (generate-thead header-and-cell-styles
                   table-str
                   table-class
                   actions)
   (generate-tbody entities
                   table-str
                   actions)
   (swap! table-str
          str
          "</table></div>")
   (let [table-node (first (md/parse-html @table-str))]
    (if details-conf
      (let [sl-nodes (md/query-selector-all-on-element table-node "a[details]")]
       (doseq [sl-node sl-nodes]
        (md/event sl-node
                  "onclick"
                  entity-details
                  [conf]))
       )
      nil)
     (if edit-conf
      (let [sl-nodes (md/query-selector-all-on-element table-node "a[edit]")]
       (doseq [sl-node sl-nodes]
        (md/event sl-node
                  "onclick"
                  entity-edit
                  [conf]))
       )
      nil)
     (if delete-conf
;      (add-event table-node selector entity-delete delete-conf render-in)
      nil
      nil)
     (md/fade-out-and-fade-in
      render-in
      animation-duration
      table-node
      new-styles
      remove-styles))
   ))

(defn table
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
  [[conf]]
  (ajx/uni-ajax-call
   {:url                  get-entities-url
    :request-method       "POST"
    :success-fn           entity-table-success
    :error-fn             uni-error
    :request-header-map
     {(rh/accept)       (mt/text-plain)
      (eh/content-type) (mt/text-plain)}
    :request-property-map
     {"responseType" (mt/text-plain)}
    :entity               (:table-conf conf)
    :conf                 conf}))

