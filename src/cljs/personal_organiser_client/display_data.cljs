(ns personal-organiser-client.display-data
  (:require [personal-organiser-client.ajax :refer [ajax get-response]]
            [personal-organiser-client.manipulate-dom :as md]
            [personal-organiser-client.utils :as utils]
            [personal-organiser-client.generate-html :refer [gen crt stl anmtn slctr]]
            [cljs.reader :as reader]
            [clojure.string :as cstr]))

(def get-entities-url "/clojure/get-entities")

(def get-entity-url "/clojure/get-entity")

(def update-entity-url "/clojure/update-entity")

(def insert-entity-url "/clojure/insert-entity")

(def delete-entity-url "/clojure/delete-entity")

(def field-type-input "input")

(def field-type-radio "radio")

(def field-type-checkbox "checkbox")

(def field-type-textarea "textarea")

(def field-type-select "select")

(def sub-form "sub-form")

(def field-type-image "image")

(def anim-time 100)

(defn- th-td-attrs
 ""
 [th-td
  content]
 (let [default-style (conj
                      {:width "auto"
                       :white-space "nowrap"
                       :text-align "center"
                       :text-overflow "ellipsis"
                       :overflow "hidden"
                       :padding "0 5px"}
                      th-td)]
  {:style default-style
   :title content}))

(defn- generate-ths
  "Generate th and append style for that th and td column"
  [columns-styles
   th-vector]
  (let [th-index (count @th-vector)]
   (if (< th-index (count columns-styles))
    (let [column-style (columns-styles th-index)
          content (:content column-style)
          header-style (th-td-attrs (:header column-style)
                                    content)
          attrs (:attrs column-style)]
     (swap! th-vector
            conj
            (crt "th"
                 (crt "div"
                      content
                      header-style)
                 attrs))
     (recur columns-styles
            th-vector))
    @th-vector))
  )

(defn- handle-paging
 "Handle click event on pagination link"
 [{conf :conf
   {table-conf :table-conf
    table-fn :table-fn} :conf
   pagination :pagination
   page :page}
  sl-node]
  (if (= page "first")
   (table-fn (assoc conf
                    :table-conf (assoc table-conf
                                       :current-page 0))
    )
   (if (= page "previous")
    (table-fn (assoc conf
                     :table-conf (assoc table-conf
                                        :current-page (dec (:current-page table-conf))
                                  ))
     )
    (if (= page "next")
     (table-fn (assoc conf
                      :table-conf (assoc table-conf
                                         :current-page (inc (:current-page table-conf))
                                   ))
      )
     (if (= page "last")
      (table-fn (assoc conf
                       :table-conf (assoc table-conf
                                          :current-page (dec (utils/round-up
                                                              (:total-row-count pagination)
                                                              (:rows pagination))
                                                         ))
                 ))
      (table-fn (assoc conf
                       :table-conf (assoc table-conf
                                          :current-page (dec (js/parseInt page))
                                    ))
       ))
     ))
   ))

(defn generate-pagination
  "Generate pagination row in thead"
  [current-page
   number-of-pages
   link-first
   link-previous
   link-next
   link-last
   assoc-page]
  (let [page-vector (atom [])]
   (swap! page-vector conj (if link-first
                            (crt "div"
                                 (crt "a"
                                      "first"
                                      {:page "first"}
                                      (assoc-page "first")))
                            (crt "div"))
    )
   (swap! page-vector conj (if link-previous
                            (crt "div"
                                 (crt "a"
                                      "previous"
                                      {:page "previous"}
                                      (assoc-page "previous"))
                                  )
                            (crt "div"))
    )
   (if (and (= current-page (dec number-of-pages))
            (< -1 (dec (dec current-page))
             ))
    (swap! page-vector conj (crt "div"
                                 (crt "a"
                                      (dec current-page)
                                      {:page (dec current-page)}
                                      (assoc-page (dec current-page))
                                  ))
     )
    nil)
   (if (< -1 (dec current-page))
    (swap! page-vector conj (crt "div"
                                 (crt "a"
                                      current-page
                                      {:page current-page}
                                      (assoc-page current-page))
                             ))
    nil)
   (swap! page-vector conj (crt "div"
                                (inc current-page)
                                {:class "current-page"})
    )
   (if (< (inc current-page) number-of-pages)
    (swap! page-vector conj (crt "div"
                                 (crt "a"
                                      (inc (inc current-page))
                                      {:page (inc (inc current-page))}
                                      (assoc-page (inc (inc current-page))
                                       ))
                             ))
    nil)
   (if (and (= current-page 0)
            (< (inc (inc current-page)) number-of-pages))
    (swap! page-vector conj (crt "div"
                                 (crt "a"
                                      (inc (inc (inc current-page))
                                       )
                                      {:page (inc (inc (inc current-page))
                                              )}
                                      (assoc-page (inc (inc (inc current-page))
                                                   ))
                                  ))
     )
    nil)
   (swap! page-vector conj (if link-next
                            (crt "div"
                                 (crt "a"
                                      "next"
                                      {:page "next"}
                                      (assoc-page "next"))
                             )
                            (crt "div"))
    )
   (swap! page-vector conj (if link-last
                            (crt "div"
                                 (crt "a"
                                      "last"
                                      {:page "last"}
                                      (assoc-page "last"))
                             )
                            (crt "div"))
    )
   @page-vector))

(defn- generate-thead
  "Generate thead for table"
  [columns-styles
   table-class
   actions
   pagination
   conf]
  (crt "thead"
       [(crt "tr"
             (if-not (empty? actions)
              (generate-ths (conj columns-styles
                                  {:content    "Actions"
                                   :attrs {:colspan (count actions)}})
                            (atom []))
              (generate-ths columns-styles
                            (atom [])))
         )
        (crt "tr"
             (crt "th"
                  (crt "div"
                       (let [current-page (:current-page pagination)
                             rows (:rows pagination)
                             total-row-count (:total-row-count pagination)
                             first-page-index 0
                             second-page-index 1
                             number-of-pages (utils/round-up total-row-count rows)
                             last-page-index (dec number-of-pages)
                             one-before-last (dec last-page-index)
                             assoc-page (fn [page]
                                         {:onclick
                                          {:evt-fn handle-paging
                                           :evt-p {:conf conf
                                                   :pagination pagination
                                                   :page page}}}
                                         )]
                        (if (< number-of-pages 4)
                         (generate-pagination current-page
                                              number-of-pages
                                              false
                                              false
                                              false
                                              false
                                              assoc-page)
                         (do (if (= current-page
                                    first-page-index)
                              (generate-pagination current-page
                                                   number-of-pages
                                                   false
                                                   false
                                                   true
                                                   true
                                                   assoc-page)
                              (if (= current-page
                                     last-page-index)
                               (generate-pagination current-page
                                                    number-of-pages
                                                    true
                                                    true
                                                    false
                                                    false
                                                    assoc-page)
                               (generate-pagination current-page
                                                    number-of-pages
                                                    true
                                                    true
                                                    true
                                                    true
                                                    assoc-page))
                              ))
                         ))
                       {:class "pagination"})
                  {:colspan (+ (count actions)
                               (count columns-styles))})
         )]))

(defn action-link
 ""
 [[content
   evt-fn
   evt-p
   ent-id]]
 (crt "a"
      content
      {:title content}
      {:onclick {:evt-fn evt-fn
                 :evt-p (assoc evt-p
                               :ent-id ent-id)}}
  ))

(defn- generate-tr
  "Generate tr elements for table body"
  [columns-styles
   data-vectors
   actions]
  (let [trs (atom [])]
   (doseq [data-vector data-vectors]
    (let [row-id (first data-vector)
          data-vector (utils/remove-index-from-vector
                       data-vector
                       0)]
     (swap! trs conj (crt "tr"
                          (let [tds (atom [])
                                td-index (atom 0)]
                           (doseq [data data-vector]
                            (let [column-style (get columns-styles @td-index)
                                  td (th-td-attrs (:column column-style)
                                                  data)]
                             (swap! tds conj (crt "td"
                                                  (crt "div"
                                                       data
                                                       td))
                              )
                             (swap! td-index inc))
                            )
                           (doseq [action actions]
                            (swap! tds conj (crt "td"
                                                 (crt "div"
                                                      (action-link (conj action
                                                                         row-id))
                                                  ))
                             ))
                           @tds))
      ))
    )
   @trs))

(defn- generate-tbody
 "Generate tbody for table"
 [columns-styles
  data-vectors
  actions]
 (crt "tbody"
      (generate-tr columns-styles
                   data-vectors
                   actions))
 )

(defn- input-field
  "Render input field"
  [data-type
   data
   label
   step
   disabled]
  (let [id (str "txt"
                 label)
        attrs {:id id
               :name id
               :type data-type
               :value data
               :required "required"}
        attrs (if step
               (assoc attrs
                      :step step)
               attrs)
        attrs (if disabled
               (assoc attrs
                      :disabled "disabled")
               attrs)]
   (crt "input"
        ""
        attrs))
 )

(defn- radio-field
  "Render radio field with different options"
  [data
   label
   options
   disabled]
  (let [rs (atom [])]
   (doseq [option options]
    (let [r-name (str "r"
                      label)
          id (str r-name
                  (md/replace-all option
                                  " "
                                  ""))
          r-attrs {:id id
                   :name r-name
                   :type "radio"
                   :value option
                   :required "required"}
          r-attrs (if (= data option)
                   (assoc r-attrs
                          :checked "checked")
                   r-attrs)
          r-attrs (if disabled
                   (assoc r-attrs
                          :disabled "disabled")
                   r-attrs)
          l-attrs {:id (str "lbl"
                            id)
                   :for id}]
     (swap! rs conj (crt "div"
                         [(crt "input"
                               ""
                               r-attrs)
                          (crt "label"
                               option
                               l-attrs)]))
     ))
   @rs))

(defn- cb-checked?
  "Query current option if it is checked"
  [selected-cbs
   current-index
   option]
  (if (< current-index (count selected-cbs))
   (if (= option (selected-cbs current-index))
    true
    (recur selected-cbs
           (inc current-index)
           option))
   false))

(defn- checkbox-field
  "Render checkbox fields with different options"
  [selected-cbs
   label
   options
   disabled]
  (let [cbs (atom [])]
   (doseq [option options]
    (let [id (str "cb"
                  label
                  (md/replace-all option
                                  " "
                                  ""))
          cb-attrs {:id id
                    :name id
                    :type "checkbox"
                    :value option}
          cb-attrs (if (cb-checked? selected-cbs
                                    0
                                    option)
                    (assoc cb-attrs
                           :checked "checked")
                    cb-attrs)
          cb-attrs (if disabled
                    (assoc cb-attrs
                           :disabled "disabled")
                    cb-attrs)
          l-attrs {:id (str "lbl"
                            id)
                   :for id}]
     (swap! cbs conj (crt "div"
                          [(crt "input"
                                ""
                                cb-attrs)
                           (crt "label"
                                option
                                l-attrs)]))
     ))
   @cbs))

(defn- textarea-field
  "Render textarea field"
  [data
   label
   disabled]
  (let [id (str "ta"
                label)
        attrs {:id id
               :name id
               :required "required"}
        attrs (if disabled
               (assoc attrs
                      :disabled "disabled")
               attrs)]
   (crt "textarea"
        data
        attrs))
 )

(defn- select-field
  "Render select field"
  [option-vector
   label
   disabled]
  (let [id (str "sl"
                label)
        sl-attrs {:id id
                  :name id
                  :required "required"}
        sl-attrs (if disabled
                  (assoc sl-attrs
                         :disabled "disabled")
                  sl-attrs)]
   (crt "select"
        (let [options (atom [])]
         (doseq [[opt-val
                  opt-lbl] option-vector]
          (swap! options conj (crt "option"
                                   opt-lbl
                                   {:value opt-val}))
          )
         @options)
        sl-attrs))
 )

(defn- render-img
 ""
 []
 (let [file-field (md/query-selector "#txtImage")
       file-field-parent (md/get-parent-node file-field)
       file (aget (aget file-field "files") 0)
       img (md/query-selector "#imgImage")
       fileReader (js/FileReader.)
       onload (aset fileReader "onload"
               ((fn [aimg]
                (fn [e]
                 (aset aimg "src" (aget (aget e "target") "result"))))
                  img))
       dataURL (.readAsDataURL fileReader file)
       ]))

(defn- image-field
 ""
 [data
  label
  disabled]
 [(crt "div"
       (crt "img"
            ""
            {:id (str "img"
                      label)
             :name (str "img"
                        label)
             :style {:width "100px"
                     :height "100px"}
             :src data}))
  (crt "div"
       (let [id (str "txt"
                     label)
            attrs {:id id
                   :name id
                   :type "file"
                   :required "required"}
            attrs (if disabled
                   (assoc attrs
                          :disabled "disabled")
                   attrs)]
       (crt "input"
            ""
            attrs
            {:onchange {:evt-fn render-img}}))
       )
  ]
 )

(defn insert-update-entity-success
  "After successful entity insert or update display table again"
  [xhr
   {conf :conf
    {table-fn :table-fn} :conf}]
  (table-fn conf))

(defn insert-update-entity
 "Insert or update entity"
 [conf
  sl-node]
 (let [action               (:action conf)
       entity-conf          (:entity-conf conf)
       entity-type          (:entity-type entity-conf)
       entity-fields        (:entity-fields entity-conf)
       entity-keys          (vec (keys entity-fields))
       table-node           (md/query-selector (str ".entity"))
       request-body         {:entity-type  entity-type}
       input-element-id     (md/query-selector-on-element table-node
                                                          "#txt_id")
       entity-id            (md/get-value input-element-id)
       entity               (atom {})
       specific-read-form   (:specific-read-form entity-conf)]
  (if specific-read-form
   (specific-read-form
    entity)
   (doseq [e-key entity-keys]
    (let [entity-field   (e-key entity-fields)
          label          (:label entity-field)
          field-type     (:field-type entity-field)
          data-type      (:data-type entity-field)
          id-prefix      (case field-type
                          "input"  "txt"
                          "radio"  "r"
                          "checkbox"  "cb"
                          "textarea"  "ta"
                          "")
          element-id (str id-prefix
                          (md/replace-all label
                                          " "
                                          ""))]
     (case field-type
      "radio"  (swap! entity conj {e-key (md/checked-value element-id)})
      "checkbox"  "cb"
      (let [input-element  (md/query-selector-on-element table-node (str "#" element-id))
            input-element-type  (md/get-type input-element)
            input-element-value  (md/get-value input-element)
            input-element-value  (if (= input-element-type
                                        "number")
                                  (reader/read-string input-element-value)
                                  input-element-value)]
       (swap! entity conj {e-key input-element-value}))
      ))
    ))
  (ajax
   {:url                  (if (= "Insert" action)
                           insert-entity-url
                           update-entity-url)
    :success-fn           insert-update-entity-success
    :entity               (assoc request-body :entity @entity :_id entity-id)
    :conf                 conf}))
 )

(defn- generate-form-trs
 ""
 [xhr
  {conf :conf
   {table-fn :table-fn
    form-type :form-type
    action :action
    action-fn :action-fn
    action-fn-param :action-fn-param
    {entity-type :entity-type
     entity-fields :entity-fields
     entity-keys :fields-order} :entity-conf} :conf}]
 (let [response (if-not (nil? xhr)
                 (get-response xhr)
                 nil)
       entity-data (:data response)
       disabled (if (= form-type "Details")
                  true
                  false)
       trs (atom [])]
  (swap! trs conj (crt "tr"
                       (crt "td"
                            (crt "h3"
                                 (str form-type
                                      " "
                                      entity-type))
                            {:colspan 3})))
  (swap! trs conj (crt "tr"
                       (crt "td"
                            (crt "input"
                                 ""
                                 {:id "txt_id"
                                  :name "txt_id"
                                  :type "hidden"
                                  :value (:_id entity-data)})
                            {:colspan 3})))
  (doseq [e-key entity-keys]
    (let [field-conf       (e-key entity-fields)
          label            (:label field-conf)
          label-no-spaces  (if (string? label)
                            (md/replace-all label " " "")
                            "lblDefault")
          field-type       (:field-type field-conf)
          data-type        (:data-type field-conf)
          step             (:step field-conf)
          disabled         (if (:disabled field-conf)
                            (:disabled field-conf)
                            disabled)
          options          (:options field-conf)
          data             (e-key entity-data)
          sub-form-trs (:sub-form-trs field-conf)]
      (if (= field-type
             sub-form)
       (doseq [sub-form-tr (sub-form-trs entity-data disabled)]
        (swap! trs conj sub-form-tr))
       (swap! trs conj
        (crt "tr"
             [(crt "td"
                   (crt "label"
                        label
                        {:id (str "lbl"
                                  label-no-spaces)
                         :for (str "txt"
                                   label-no-spaces)}))
              
              (crt "td"
                   (if (= field-type
                          field-type-input)
                    (input-field data-type
                                 data
                                 label-no-spaces
                                 step
                                 disabled)
                    (if (= field-type
                           field-type-radio)
                     (radio-field data
                                  label-no-spaces
                                  options
                                  disabled)
                     (if (= field-type
                            field-type-checkbox)
                      (checkbox-field data
                                      label-no-spaces
                                      options
                                      disabled)
                      (if (= field-type
                             field-type-textarea)
                       (textarea-field data
                                       label-no-spaces
                                       disabled)
                       (if (= field-type
                              field-type-image)
                        (image-field data
                                     label-no-spaces
                                     disabled)
                        "")))
                     ))
               )
              (crt "td"
                   ""
                   {:id (str "td"
                             label)})]
            )))
      )
   )
  (swap! trs conj (crt "tr"
                       [(crt "td"
                             (crt "input"
                                  ""
                                  {:id "btnCancel"
                                   :type "button"
                                   :value "Cancel"
                                   :style {:float "right"}}
                                  {:onclick {:evt-fn table-fn
                                             :evt-p conf}}))
                        (crt "td"
                             (crt "input"
                                  ""
                                  {:id (str "btn"
                                            action)
                                   :type "button"
                                   :value action}
                                  {:onclick {:evt-fn action-fn
                                             :evt-p (if action-fn-param
                                                     action-fn-param
                                                     conf)}}
                              ))
                        (crt "td")]))
  @trs))

(defn- generate-form
  "Generate entity form"
  [xhr
   ajax-params]
  (let [{{{entity-type :entity-type} :entity-conf} :conf} ajax-params
        table-node (gen
                    (crt "div"
                         (crt "table"
                              (generate-form-trs
                               xhr
                               ajax-params))
                         {:class "entity"}))]
   (md/fade-out-and-fade-in ".content"
                             anim-time
                             table-node))
 )

(defn- entity-form
  "Request data about particular entity for display, edit/update"
  [conf
   sl-node]
  (let [from-details (:from-details conf)
        conf (assoc conf :from-details nil)
        ent-id (:ent-id conf)
        entity (:entity-conf conf)
        ent-id-key (:entity-id entity)
        entity-type (:entity-type entity)
        entity-fields (:entity-fields entity)
        request-body {:entity-type  entity-type
                      :entity-filter  {ent-id-key ent-id}}]
   (ajax
    {:url                  get-entity-url
     :success-fn           generate-form
     :entity               request-body
     :conf                 conf}))
  )

(defn create-entity
  "Call generate-form function with create entity parameters"
  [conf]
  (generate-form nil
                {:conf (assoc conf
                              :form-type  "Create"
                              :action     "Insert"
                              :action-fn  insert-update-entity)}))

(defn- edit-entity-from-table
  "Call entity-form function from generated entities table with edit entity parameters"
  [conf
   sl-node]
  (entity-form (assoc conf
                      :form-type  "Edit"
                      :action     "Update"
                      :action-fn  insert-update-entity)
               sl-node))

(defn- edit-entity-from-details
  "Call entity-form function from generated details form with edit entity parameters"
  [conf
   sl-node]
  (entity-form (assoc conf
                      :form-type     "Edit"
                      :action        "Update"
                      :action-fn     insert-update-entity
                      :from-details  true)
               sl-node))

(defn- entity-details
  "Call entity-form function from generated entities table with details entity parameters"
  [conf
   sl-node]
  (entity-form (assoc conf
                      :form-type "Details"
                      :action "Edit"
                      :action-fn edit-entity-from-details
                      :action-fn-param (assoc conf
                                              :form-type  "Edit"
                                              :action     "Update"
                                              :action-fn  insert-update-entity))
               sl-node))

(defn- entity-delete-success
  "Entity delete success"
  [xhr
   {conf :conf
    {table-fn :table-fn} :conf}]
  (table-fn conf))

(defn- entity-delete
  "Request entity to be deleted from server"
  [conf
   sl-node]
  (let [entity (:entity-conf conf)
        ent-id (:ent-id conf)
        ent-id-key (:entity-id entity)
        entity-type (:entity-type entity)
        request-body {:entity-type  entity-type
                      :entity-filter  {ent-id-key ent-id}}]
   (ajax
    {:url delete-entity-url
     :request-method "DELETE"
     :success-fn entity-delete-success
     :entity request-body
     :conf conf}))
  )

(defn- entity-table-success
 "Generate entity table after retrieving entities"
 [xhr
  {conf :conf
   {table-fn :table-fn} :conf}]
 (let [table-class (or (:table-class conf) "entities")
       columns-styles (:columns-styles conf)
       response (get-response xhr)
       entities (:data response)
       pagination (:pagination response)
       render-in (:render-in conf)
       animation (:animation conf)
       animation-duration (:animation-duration conf)
       actions []
       actions (if (:details conf)
                (conj actions
                      ["details"
                       entity-details
                       conf])
                actions)
       actions (if (:edit conf)
                (conj actions
                      ["edit"
                       edit-entity-from-table
                       conf])
                actions)
       actions (if (:delete conf)
                (conj actions
                      ["delete"
                       entity-delete
                       conf])
                actions)]
  (if (empty? entities)
   (let [table-node (gen
                     (crt "div"
                          "No entities"
                          {:class table-class}))]
    (md/fade-out-and-fade-in
     render-in
     animation-duration
     table-node))
   (let [table-node (gen
                     (crt "div"
                          (crt "table"
                               [(generate-thead
                                 columns-styles
                                 table-class
                                 actions
                                 pagination
                                 conf)
                                (generate-tbody
                                 columns-styles
                                 entities
                                 actions)])
                          {:class table-class}))]
       (md/fade-out-and-fade-in
        render-in
        animation-duration
        table-node))
   ))
 )

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
 [conf]
 (ajax
  {:url get-entities-url
   :success-fn entity-table-success
   :entity (:table-conf conf)
   :conf conf}))

