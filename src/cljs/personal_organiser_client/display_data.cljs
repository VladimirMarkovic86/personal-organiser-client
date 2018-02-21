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
  "Append particular style to style template"
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

(def new-styles (atom (set '())
                 ))

(def remove-styles (atom (set '())
                    ))

(defn- append-column-style
  "Generate and append column style"
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
  "Append td th styles"
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
                        (atom th-td-style))
   ))

(defn- generate-th
  "Generate th and append style for that th and td column"
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
                    (swap! td-index inc))
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

(defn- round-up
  "Round up result from dividing"
  [number1 number2]
  (if (= 0 (mod number1 number2))
   (int (/ number1 number2))
   (inc (int (/ number1 number2))
    ))
  )

(defn generate-pagination
  "Generate pagination row in thead"
  [table-node
   current-page
   number-of-pages
   link-first
   link-previous
   link-next
   link-last]
  (if link-first
   (swap! table-node str "<div><a page=\"first\" >first</a></div>")
   (swap! table-node str "<div></div>"))
  (if link-previous
   (swap! table-node str "<div><a page=\"previous\" >previous</a></div>")
   (swap! table-node str "<div></div>"))
  (if (and (= current-page (dec number-of-pages))
           (< -1 (dec (dec current-page))
            ))
   (swap! table-node str "<div><a page=\""
                         (dec current-page)
                         "\" >"
                         (dec current-page)
                         "</a></div>")
   nil)
  (if (< -1 (dec current-page))
   (swap! table-node str "<div><a page=\""
                         current-page
                         "\" >"
                         current-page
                         "</a></div>")
   nil)
  (swap! table-node str "<div><a class=\"current-page\" >"
                        (inc current-page)
                        "</a></div>")
  (if (< (inc current-page) number-of-pages)
   (swap! table-node str "<div><a page=\""
                         (inc (inc current-page))
                         "\" >"
                         (inc (inc current-page))
                         "</a></div>")
   nil)
  (if (and (= current-page 0)
           (< (inc (inc current-page)) number-of-pages))
   (swap! table-node str "<div><a page=\""
                         (inc (inc (inc current-page))
                          )
                         "\" >"
                         (inc (inc (inc current-page))
                          )
                         "</a></div>")
   nil)
  (if link-next
   (swap! table-node str "<div><a page=\"next\" >next</a></div>")
   (swap! table-node str "<div></div>"))
  (if link-last
   (swap! table-node str "<div><a page=\"last\" >last</a></div>")
   (swap! table-node str "<div></div>"))
  )

(defn- generate-thead
  "Generate thead for table"
  [cell-styles
   table-node
   table-class
   actions
   pagination]
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
  
  (swap! table-node str "</tr>")
  (swap! table-node str "<tr><th"
                        " colspan=\""
                        (+ (count actions) (count cell-styles))
                        "\" ><div class=\"pagination\">")
  (let [current-page      (:current-page pagination)
        rows              (:rows pagination)
        total-row-count   (:total-row-count pagination)
        first-page-index  0
        second-page-index 1
        number-of-pages   (round-up total-row-count rows)
        last-page-index   (dec number-of-pages)
        one-before-last   (dec last-page-index)]
   (if (< number-of-pages 4)
    (generate-pagination table-node
                         current-page
                         number-of-pages
                         false
                         false
                         false
                         false)
    (do (if (= current-page
               first-page-index)
         (generate-pagination table-node
                              current-page
                              number-of-pages
                              false
                              false
                              true
                              true)
         (if (= current-page
                last-page-index)
          (generate-pagination table-node
                               current-page
                               number-of-pages
                               true
                               true
                               false
                               false)
          (generate-pagination table-node
                               current-page
                               number-of-pages
                               true
                               true
                               true
                               true))
         ))
    ))
  (swap! table-node str "</div></th></tr></thead>"))

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

(defn- render-input
  "Render input field"
  [table-str
   data-type
   data
   label
   step
   disabled]
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
                       "\""
                       disabled
                       " required>"))

(defn- render-radio
  "Render radio field with different options"
  [table-str
   data
   label
   options
   disabled]
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
                         disabled
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
  "Query current option if it is checked"
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
  "Render checkbox fields with different options"
  [table-str
   selected-cbs
   label
   options
   disabled]
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
                         disabled
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
  "Render textarea field"
  [table-str
   data
   label
   disabled]
  (swap! table-str str "<textarea id=\"ta"
                       label
                       "\" name=\"ta"
                       label
                       "\""
                       disabled
                       " required>"
                       data
                       "</textarea>"))

(defn- uni-error
  "Handle details error"
  [xhr]
  (let [response      (ajx/get-response xhr)
        error-message (:error-message response)]
   (reset! new-styles (set '())
    )
   (md/fade-out-and-fade-in ".content"
                            anim-time
                            (str "<div>" error-message "</div>")
                            new-styles
                            remove-styles))
  )

(defn insert-update-entity-success
  "After successful entity insert or update display table again"
  [xhr
   ajax-params]
  (let [table-conf (:conf ajax-params)]
   (table [table-conf])
   ))

(defn insert-update-entity
  "Insert or update entity"
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
  "Generate entity form"
  [xhr
   ajax-params]
  (let [response         (ajx/get-response xhr)
        entity-data      (:data response)
        conf             (:conf ajax-params)
        form-type        (:form-type conf)
        disabled         (if (= form-type "Details")
                          " disabled=\"disabled\" "
                          "")
        action           (:action conf)
        action-fn        (:action-fn conf)
        action-fn-param  (:action-fn-param conf)
        edit-conf        (:edit-conf conf)
        entity-type      (:entity-type edit-conf)
        entity-fields    (:entity-fields edit-conf)
        entity-keys      (vec (keys entity-fields))
        table-str        (atom "")]
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
                                step
                                disabled)
      "radio"     (render-radio table-str
                                data
                                label-no-spaces
                                options
                                disabled)
      "checkbox"  (render-checkbox table-str
                                   data
                                   label-no-spaces
                                   options
                                   disabled)
      "textarea"  (render-textarea table-str
                                   data
                                   label-no-spaces
                                   disabled)
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
              [(if action-fn-param
                action-fn-param
                conf)])
    (reset! new-styles (set '())
     )
    (md/fade-out-and-fade-in ".content"
                             anim-time
                             table-node
                             new-styles
                             remove-styles))
   ))

(defn entity-form
  "Request data about particular entity for display, edit/update"
  [[conf]
   sl-node]
  (let [from-details   (:from-details conf)
        conf           (assoc conf :from-details nil)
        entity         (:edit-conf conf)
        entity-type    (:entity-type entity)
        entity-id      (:entity-id entity)
        entity-fields  (:entity-fields entity)
        request-body   {:entity-type  entity-type
                        :query        {entity-id (if from-details
                                                  (md/get-value
                                                   (md/query-selector-on-element
                                                    (md/query-selector
                                                     (str "."
                                                          entity-type)
                                                     )
                                                    (str "#txt"
                                                         (:label
                                                          (entity-id entity-fields))
                                                     ))
                                                   )
                                                  (md/get-attr
                                                   (md/ancestor sl-node 3)
                                                   "row"))}}]
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
     :conf                 conf}))
  )

(defn create-entity
  "Call generate-form function with create entity parameters"
  [[conf]]
  (generate-form nil
                {:conf (assoc conf
                              :form-type  "Create"
                              :action     "Insert"
                              :action-fn  insert-update-entity)}))

(defn edit-entity-from-table
  "Call entity-form function from generated entities table with edit entity parameters"
  [[conf]
   sl-node]
  (entity-form [(assoc conf
                       :form-type  "Edit"
                       :action     "Update"
                       :action-fn  insert-update-entity)]
               sl-node)
  )

(defn edit-entity-from-details
  "Call entity-form function from generated details form with edit entity parameters"
  [[conf]
   sl-node]
  (entity-form [(assoc conf
                       :form-type     "Edit"
                       :action        "Update"
                       :action-fn     insert-update-entity
                       :from-details  true)]
               sl-node)
  )

(defn entity-details
  "Call entity-form function from generated entities table with details entity parameters"
  [[conf]
   sl-node]
  (entity-form [(assoc conf
                       :form-type  "Details"
                       :action     "Edit"
                       :action-fn  edit-entity-from-details
                       :action-fn-param  (assoc conf
                                                :form-type  "Edit"
                                                :action     "Update"
                                                :action-fn  insert-update-entity))]
               sl-node
   ))

(defn enitiy-delete-success
  "Entity delete success"
  [xhr
   ajax-params]
  (table [(:conf ajax-params)]))

(defn entity-delete
  "Request entity to be deleted from server"
  [[conf]
   sl-node]
  (let [entity         (:edit-conf conf)
        entity-type    (:entity-type entity)
        entity-id      (:entity-id entity)
        request-body   {:entity-type  entity-type
                        :query        {entity-id (md/get-attr
                                                  (md/ancestor sl-node 3)
                                                  "row")}}]
   (ajx/uni-ajax-call
    {:url                  delete-entity-url
     :request-method       "DELETE"
     :success-fn           enitiy-delete-success
     :error-fn             uni-error
     :request-header-map
      {(rh/accept)       (mt/text-plain)
       (eh/content-type) (mt/text-plain)}
     :request-property-map
      {"responseType" (mt/text-plain)}
     :entity               request-body
     :conf                 conf}))
  )

(defn handle-paging
  "Handle click event on pagination link"
  [[conf]
   sl-node]
  (let [pagination  (:pagination conf)
        conf        (:conf conf)
        page-attr   (md/get-attr sl-node "page")
        table-conf  (:table-conf conf)]
   (if (= page-attr "first")
    (table [(assoc conf
                   :table-conf (assoc table-conf
                                      :current-page 0))])
    (if (= page-attr "previous")
     (table [(assoc conf
                    :table-conf (assoc table-conf
                                       :current-page (dec (:current-page table-conf))
                                 ))])
     (if (= page-attr "next")
      (table [(assoc conf
                     :table-conf (assoc table-conf
                                        :current-page (inc (:current-page table-conf))
                                  ))])
      (if (= page-attr "last")
       (table [(assoc conf
                      :table-conf (assoc table-conf
                                         :current-page (dec (round-up
                                                             (:total-row-count pagination)
                                                             (:rows pagination))
                                                        ))
                )])
       (table [(assoc conf
                      :table-conf (assoc table-conf
                                         :current-page (dec (js/parseInt page-attr))
                                   ))]))
      ))
    ))
  )

(defn entity-table-success
  "Generate entity table after retrieving entities"
  [xhr
   ajax-params]    
  (let [table-str               (atom "")
        conf                    (:conf ajax-params)
        table-class             (:table-class conf)
        header-and-cell-styles  (:header-and-cell-styles conf)
        response                (ajx/get-response xhr)
        entities                (:data response)
        pagination              (:pagination response)
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
   (if (empty? entities)
    (let [table-node (first (md/parse-html (str "<div class=\""
                                                table-class
                                                "\" >No entities</div>"))
                      )]
     (md/fade-out-and-fade-in
      render-in
      animation-duration
      table-node
      new-styles
      remove-styles))
    (do (generate-thead header-and-cell-styles
                        table-str
                        table-class
                        actions
                        pagination)
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
                       edit-entity-from-table
                       [conf]))
            )
           nil)
          (if delete-conf
           (let [sl-nodes (md/query-selector-all-on-element table-node "a[delete]")]
            (doseq [sl-node sl-nodes]
             (md/event sl-node
                       "onclick"
                       entity-delete
                       [conf]))
            )
           nil)
          (let [sl-nodes (md/query-selector-all-on-element table-node "a[page]")]
           (doseq [sl-node sl-nodes]
             (md/event sl-node
                       "onclick"
                       handle-paging
                       [{:conf conf
                         :pagination pagination}]))
           )
          (md/fade-out-and-fade-in
           render-in
           animation-duration
           table-node
           new-styles
           remove-styles))
        ))
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

