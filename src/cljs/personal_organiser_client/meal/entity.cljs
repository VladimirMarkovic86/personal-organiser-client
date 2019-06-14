(ns personal-organiser-client.meal.entity
  (:require [ajax-lib.core :refer [sjax get-response]]
            [htmlcss-lib.core :refer [gen div label input table
                                      thead tbody tr th td
                                      select option span]]
            [js-lib.core :as md]
            [framework-lib.core :refer [gen-table]]
            [validator-lib.core :refer [validate-field]]
            [utils-lib.core :as utils]
            [cljs.reader :as reader]
            [clojure.set :as cset]
            [language-lib.core :refer [get-label]]
            [common-middle.request-urls :as rurls]
            [common-client.allowed-actions.controller :refer [allowed-actions]]
            [personal-organiser-middle.meal.entity :as pomme]
            [personal-organiser-middle.grocery.entity :as pomge]
            [personal-organiser-middle.collection-names :refer [meal-cname
                                                                grocery-cname]]))

(def entity-type
     meal-cname)

(def entity-sub-type
     grocery-cname)

(def select-conf
     {:entity-type entity-sub-type
      :entity-filter {}
      :projection [:gname
                   :label-code
                   :calories
                   :proteins
                   :fats
                   :carbonhydrates
                   :diet]
      :projection-include true
      :qsort {:gname 1}
      :pagination false
      :current-page 0
      :rows 0
      :collation {:locale "sr"}})

(def validate-form-atom-fn
     (atom nil))

(def groceries
     (atom nil))

(defn get-groceries
  "Generate options for select HTML element"
  []
  (when (empty?
          @groceries)
    (let [xhr (sjax
                {:url rurls/get-entities-url
                 :entity select-conf})
          response (get-response xhr)]
      (reset!
        groceries
        (:data response))
     ))
  @groceries)

(defn grocery-select-options
  "Build options vector for grocery select element"
  []
  (let [entities (get-groceries)
        options (atom [(option
                         (get-label 33)
                         {:value "-1"})])]
    (doseq [{opt-value :_id
             opt-label :gname
             opt-label-code :label-code
             opt-calories :calories
             opt-proteins :proteins
             opt-fats :fats
             opt-carbonhydrates :carbonhydrates
             opt-diet :diet} entities]
      (swap!
        options
        conj
        (option
          (if (and (utils/is-number?
                     opt-label-code)
                   (pos?
                     opt-label-code))
            (get-label
              opt-label-code)
            opt-label)
          nil
          nil
          {:value opt-value
           :opt-label-code opt-label-code
           :opt-calories opt-calories
           :opt-proteins opt-proteins
           :opt-fats opt-fats
           :opt-carbonhydrates opt-carbonhydrates
           :opt-diet opt-diet}))
     )
    @options))

(defn generate-ingredient-row
  "Generates ingredient row for ingredient table"
  [selected-g-label
   selected-g-value
   grams-g
   quantity-g]
  (gen
    (tr
      [(td
         selected-g-label
         {:value selected-g-value
          :title selected-g-label
          :style {:text-align "left"}})
       (td
         grams-g
         {:title grams-g
          :style {:text-align "right"}}
         nil
         {:valueAsNumber grams-g})
       (td
         quantity-g
         {:title quantity-g
          :style {:text-align "right"}}
         nil
         {:valueAsNumber quantity-g})])
   ))

(defn add-ingredient-in-table
  "Add ingredient in table"
  [selected-g-opt
   grams-g
   quantity-g]
  (let [selected-g-value (.-value
                           selected-g-opt)
        selected-g-label (.-innerHTML
                           selected-g-opt)
        i-table (md/query-selector
                  ".entity #ingredients table:not(.no-results)")
        ing-exists (md/query-selector
                     (str
                       ".entity #ingredients table:not(.no-results) td[value=\""
                       selected-g-value "\"]"))]
    (if i-table
      (if ing-exists
        (.log
          js/console
          "Ingredient exists")
        (md/append-element
          ".entity #ingredients table tbody"
          (generate-ingredient-row
            selected-g-label
            selected-g-value
            grams-g
            quantity-g))
       )
      (let [div-element (md/query-selector-on-element
                          ".entity"
                          "#ingredients div.no-results.selection-items")]
        (md/remove-element-content
          div-element)
        (md/remove-class
          div-element
          "no-results")
        (md/remove-class
          div-element
          "selection-items")
        (md/add-class
          div-element
          "entities")
        (md/append-element
          div-element
          (gen
            (table
              [(thead
                 (tr
                   [(th
                      (get-label 1009)
                      {:title (get-label 1009)
                       :style {:width "50%"}})
                    (th
                      (get-label 1027)
                      {:title (get-label 1027)
                       :style {:width "25%"}})
                    (th
                      (get-label 1028)
                      {:title (get-label 1028)
                       :style {:width "25%"}})])
                )
               (tbody
                 (generate-ingredient-row
                   selected-g-label
                   selected-g-value
                   grams-g
                   quantity-g))]
             ))
         ))
     ))
 )

(defn add-ingredient
  "Add ingredient in table and calculate calories, proteins, fats and carbonhydrates"
  []
  (let [selected-g-slctr "#select-grocery"
        selected-g-el (md/query-selector-on-element
                        ".entity"
                        selected-g-slctr)
        selected-g-el-parent (.-parentElement
                               selected-g-el)
        selected-g-opt (aget
                         (.-selectedOptions
                           selected-g-el)
                         0)
        selected-g-value (.-value
                           selected-g-opt)
        ing-exists (md/query-selector
                     (str
                       ".entity #ingredients table:not(.no-results) td[value=\""
                       selected-g-value
                       "\"]"))
        grams-g-slctr "#grams-number"
        grams-g-el (md/query-selector-on-element
                     ".entity"
                     grams-g-slctr)
        grams-g (.-valueAsNumber
                  grams-g-el)
        grams-g-el-parent (.-parentElement
                            grams-g-el)
        quantity-g-slctr "#quantity-number"
        quantity-g-el (md/query-selector-on-element
                        ".entity"
                        quantity-g-slctr)
        quantity-g (.-valueAsNumber
                     quantity-g-el)
        quantity-g-el-parent (.-parentElement
                               quantity-g-el)
        invalid-fields (atom [])]
    (md/remove-class
      selected-g-el-parent
      "error")
    (md/remove-class
      grams-g-el-parent
      "error")
    (md/remove-class
      quantity-g-el-parent
      "error")
    (when (or ing-exists
              (= selected-g-value
                 "-1"))
      (swap!
        invalid-fields
        conj
        selected-g-el-parent))
    (when-not (utils/is-number?
                grams-g)
      (swap!
        invalid-fields
        conj
        grams-g-el-parent))
    (when-not (utils/is-number?
                quantity-g)
      (swap!
        invalid-fields
        conj
        quantity-g-el-parent))
    (if (empty?
          @invalid-fields)
      (let [calories-sum-el (md/query-selector-on-element
                              ".entity"
                              "#calories-sum")
            calories-sum-as-num (.-valueAsNumber
                                  calories-sum-el)
            calories-sum (if (utils/is-number?
                               calories-sum-as-num)
                           calories-sum-as-num
                           0)
            proteins-sum-el (md/query-selector-on-element
                              ".entity"
                              "#proteins-sum")
            proteins-sum-as-num (.-valueAsNumber
                                  proteins-sum-el)
            proteins-sum (if (utils/is-number?
                               proteins-sum-as-num)
                           proteins-sum-as-num
                           0)
            fats-sum-el (md/query-selector-on-element
                          ".entity"
                          "#fats-sum")
            fats-sum-as-num (.-valueAsNumber
                              fats-sum-el)
            fats-sum (if (utils/is-number?
                           fats-sum-as-num)
                       fats-sum-as-num
                       0)
            carbonhydrates-sum-el (md/query-selector-on-element
                                    ".entity"
                                    "#carbonhydrates-sum")
            carbonhydrates-sum-as-num (.-valueAsNumber
                                        carbonhydrates-sum-el)
            carbonhydrates-sum (if (utils/is-number?
                                     carbonhydrates-sum-as-num)
                                 carbonhydrates-sum-as-num
                                 0)
            weight (* (/ grams-g
                         100)
                      quantity-g)
            calories (+ calories-sum
                        (* (aget
                             selected-g-opt
                             "opt-calories")
                           weight))
            proteins (+ proteins-sum
                        (* (aget
                             selected-g-opt
                             "opt-proteins")
                           weight))
            fats (+ fats-sum
                    (* (aget
                         selected-g-opt
                         "opt-fats")
                       weight))
            carbonhydrates (+ carbonhydrates-sum
                              (* (aget
                                   selected-g-opt
                                   "opt-carbonhydrates")
                                 weight))
            opt-diet (aget
                       selected-g-opt
                       "opt-diet")
            previous-diet (md/checked-value
                            "diet")
            diet-el (if (or (= opt-diet
                               "not_vegetarian")
                            (= previous-diet
                               "not_vegetarian"))
                      (md/query-selector-on-element
                        ".entity"
                        "#dietnot_vegetarian")
                      (md/query-selector-on-element
                        ".entity"
                        "#dietvegetarian"))]
       (add-ingredient-in-table
         selected-g-opt
         grams-g
         quantity-g)
       (md/set-value
         "#calories-sum"
         (utils/round-decimals
           calories
           2))
       (md/set-value
         "#proteins-sum"
         (utils/round-decimals
           proteins
           2))
       (md/set-value
         "#fats-sum"
         (utils/round-decimals
           fats
           2))
       (md/set-value
         "#carbonhydrates-sum"
         (utils/round-decimals
           carbonhydrates
           2))
       (aset
         diet-el
         "checked"
         true))
      (doseq [invalid-field @invalid-fields]
        (md/add-class
          invalid-field
          "error"))
     ))
   (@validate-form-atom-fn
     validate-field
     (atom false))
 )

(defn read-form
  "Read meal form"
  []
  (let [itr (atom 0)
        ingredient (atom [])
        ingredients (atom [])
        tds (md/query-selector-all
              ".entity #ingredients table tbody td")]
    (doseq [td tds]
      (if (< @itr 3)
        (do
          (if (= @itr
                 0)
            (swap!
              ingredient
              conj
              (md/get-attr
                td
                "value"))
            (swap!
              ingredient
              conj
              (.-valueAsNumber
                td))
           )
          (swap!
            itr
            inc))
        (do
          (swap!
            ingredients
            conj
            @ingredient)
          (reset!
            ingredient
            [])
          (swap!
            ingredient
            conj
            (md/get-attr
              td
              "value"))
          (reset!
            itr
            1))
       ))
    (swap!
      ingredients
      conj
      @ingredient)
    @ingredients))

(defn get-grocery-name
  "Gets grocery name by _id parameter"
  [_id]
  (let [groceries (get-groceries)
        groceries-set (into
                        #{}
                        groceries)
        groceries-result (cset/select
                           (fn [{opt-value :_id}]
                             (= opt-value
                                _id))
                           groceries-set)
        {opt-label :gname
         opt-label-code :label-code} (first
                                       groceries-result)]
    (if (and (utils/is-number?
               opt-label-code)
             (pos?
               opt-label-code))
      (get-label
        opt-label-code)
      opt-label))
 )

(defn sub-form
  "Generate ingredients sub form"
  [data
   attrs]
  (let [disabled (:disabled attrs)]
    [(div
       (label
         [(get-label 1040)
          (div
            [(label
               [(get-label 1009)
                (let [attrs {:id "select-grocery"
                             :placeholder (get-label 1009)
                             :title (get-label 1009)}
                      attrs (if disabled
                              (assoc
                                attrs
                                :disabled "disabled")
                              attrs)]
                  (select
                    (grocery-select-options)
                    attrs))]
              )
             (label
               [(get-label 1027)
                (let [attrs {:id "grams-number"
                             :type "number"
                             :placeholder (get-label 1027)
                             :title (get-label 1027)}
                      attrs (if disabled
                              (assoc
                                attrs
                                :disabled "disabled")
                              attrs)]
                  (input
                    ""
                    attrs))]
              )
             (label
               [(get-label 1028)
                (let [attrs {:id "quantity-number"
                             :type "number"
                             :placeholder (get-label 1028)
                             :title (get-label 1028)}
                      attrs (if disabled
                              (assoc
                                attrs
                                :disabled "disabled")
                              attrs)]
                  (input
                    ""
                    attrs))]
              )
             (let [attrs {:id "add-ingredient"
                          :class "btn sub-form"
                          :type "button"
                          :value (get-label 1041)}
                   attrs (if disabled
                           (assoc
                             attrs
                             :disabled "disabled")
                           attrs)]
               (input
                 ""
                 attrs
                 {:onclick {:evt-fn add-ingredient}}))]
            {:class "selection-items"})])
      )
     (div
       (label
         [(get-label 1040)
          (if-let [ingredients (:ingredients data)]
            (div
              (table
                [(thead
                   (tr
                     [(th
                        (get-label 1009)
                        {:title (get-label 1009)
                         :style {:width "50%"}})
                      (th
                        (get-label 1027)
                        {:title (get-label 1027)
                         :style {:width "25%"}})
                      (th
                        (get-label 1028)
                        {:title (get-label 1028)
                         :style {:width "25%"}})])
                  )
                 (tbody
                   (let [ingredients-vector (atom [])]
                     (doseq [[_id
                              grams
                              quantity] ingredients]
                       (swap!
                         ingredients-vector
                         conj
                         (generate-ingredient-row
                           (get-grocery-name
                             _id)
                           _id
                           grams
                           quantity))
                      )
                    @ingredients-vector))]
               )
              {:class "entities"})
            (div
              (div
                (get-label 1039))
              {:class "no-results selection-items"}))
          (input
            ""
            {:id "ingredients-validation-field"
             :type "hidden"})
          (span)])
      {:id "ingredients"})])
 )

(defn validate-form
  "Validate password special field"
  [validate-field-fn
   is-valid]
  (let [hidden-input (md/query-selector-on-element
                       ".entity"
                       "#ingredients-validation-field")
        ingredients (read-form)]
    (if (and (= (count
                  ingredients)
                1)
             (empty?
               (get
                 ingredients
                 0))
         )
      (validate-field-fn
        hidden-input
        is-valid
        (get-label 1029)
        true)
      (validate-field-fn
        hidden-input
        is-valid))
   ))

(reset!
  validate-form-atom-fn
  validate-form)

(defn mtype-labels
  "Returns meal type property labels"
  []
  [[(get-label 1030)
    pomme/mtype-breakfast]
   [(get-label 1031)
    pomme/mtype-lunch]
   [(get-label 1032)
    pomme/mtype-dinner]])

(defn part-of-meal-labels
  "Returns part of meal property labels"
  []
  [[(get-label 1067)
    pomme/part-of-meal-all-in-one]
   [(get-label 1033)
    pomme/part-of-meal-main-course]
   [(get-label 1034)
    pomme/part-of-meal-sauce]
   [(get-label 1035)
    pomme/part-of-meal-beverage]
   [(get-label 1036)
    pomme/part-of-meal-soup]
   [(get-label 1037)
    pomme/part-of-meal-sweets-cakes-compote-ice-cream]
   [(get-label 1038)
    pomme/part-of-meal-salad]])

(defn diet-labels
  "Returns diet property labels"
  []
  [[(get-label 1044)
    pomge/diet-vegetarian]
   [(get-label 1042)
    pomge/diet-not-vegetarian]])

(defn form-conf-fn
  "Form configuration for meal entity"
  []
  {:id :_id
   :type entity-type
   :entity-name (get-label 1017)
   :fields {:mname {:label (get-label 1010)
                    :input-el "text"
                    :attrs {:placeholder (get-label 1010)
                            :title (get-label 1010)
                            :required true}}
            :label-code {:label (get-label 24)
                         :input-el "number"
                         :attrs {:step "1"
                                 :placeholder (get-label 24)
                                 :title (get-label 24)}}
            :calories-sum {:label (get-label 1018)
                           :input-el "number"
                           :attrs {:step "0.001"
                                   :placeholder (get-label 1018)
                                   :title (get-label 1018)
                                   :disabled true}}
            :proteins-sum {:label (get-label 1019)
                           :input-el "number"
                           :attrs {:step "0.001"
                                   :placeholder (get-label 1019)
                                   :title (get-label 1019)
                                   :disabled true}}
            :fats-sum {:label (get-label 1020)
                       :input-el "number"
                       :attrs {:step "0.001"
                               :placeholder (get-label 1020)
                               :title (get-label 1020)
                               :disabled true}}
            :carbonhydrates-sum {:label (get-label 1021)
                                 :input-el "number"
                                 :attrs {:step "0.001"
                                         :placeholder (get-label 1021)
                                         :title (get-label 1021)
                                         :disabled true}}
            :description  {:label (get-label 1015)
                           :input-el "textarea"
                           :attrs {:placeholder (get-label 1015)
                                   :title (get-label 1015)
                                   :required true}}
            :image {:label (get-label 1022)
                    :input-el "img"}
            :mtype {:label (get-label 1023)
                    :input-el "select"
                    :attrs {:multiple true
                            :required true}
                    :options (mtype-labels)}
            :part-of-meal {:label (get-label 1024)
                           :input-el "select"
                           :attrs {:required true}
                           :options (part-of-meal-labels)}
            :ingredients {:label (get-label 1025)
                          :input-el "sub-form"
                          :sub-form-fieldset sub-form
                          :sub-form-fieldset-read read-form
                          :sub-form-validation validate-form}
            :diet {:label (get-label 1007)
                   :input-el "radio"
                   :attrs {:required true
                           :disabled true}
                   :options (diet-labels)}}
   :fields-order [:mname
                  :label-code
                  :calories-sum
                  :proteins-sum
                  :fats-sum
                  :carbonhydrates-sum
                  :diet
                  :mtype
                  :part-of-meal
                  :description
                  :ingredients
                  :image]})

(defn columns-fn
  "Table columns for meal entity"
  []
  {:projection [:mname
                :label-code
                :calories-sum
                :proteins-sum
                :fats-sum
                :carbonhydrates-sum
                ;:description
                ;:image
                ;:mtype
                ;:part-of-meal
                ;:ingredients
                ]
   :display-fields [;:mname
                    :label-code
                    :calories-sum
                    :proteins-sum
                    :fats-sum
                    :carbonhydrates-sum
                    ;:description
                    ;:image
                    ;:mtype
                    ;:part-of-meal
                    ;:ingredients
                    ]
   :style
    {:mname
      {:content (get-label 1010)
       :th {:style {:width "12%"}}
       :td {:style {:width "12%"
                    :text-align "left"}}
       }
     :label-code
      {:content (get-label 1010)
       :th {:style {:width "22%"}}
       :td {:style {:width "22%"
                    :text-align "left"}}
       :original-field :mname}
     :calories-sum
      {:content (get-label 1018)
       :th {:style {:width "12%"}}
       :td {:style {:width "12%"
                    :text-align "right"}}
       }
     :proteins-sum
      {:content (get-label 1019)
       :th {:style {:width "12%"}}
       :td {:style {:width "12%"
                    :text-align "right"}}
       }
     :fats-sum
      {:content (get-label 1020)
       :th {:style {:width "12%"}}
       :td {:style {:width "12%"
                    :text-align "right"}}
       }
     :carbonhydrates-sum
      {:content (get-label 1021)
       :th {:style {:width "12%"}}
       :td {:style {:width "12%"
                    :text-align "right"}}
       }
     :description
      {:content (get-label 1015)
       :th {:style {:width "12%"}}
       :td {:style {:width "12%"
                    :text-align "right"}}
       }
     :image
      {:content (get-label 1022)
       :th {:style {:width "12%"}}
       :td {:style {:width "12%"
                    :text-align "right"}}
       }
     :mtype
      {:content (get-label 1023)
       :th {:style {:width "12%"}}
       :td {:style {:width "12%"
                    :text-align "right"}}
       :labels (into
                 #{}
                 (mtype-labels))}
     :part-of-meal
      {:content (get-label 1024)
       :th {:style {:width "12%"}}
       :td {:style {:width "12%"
                    :text-align "right"}}
       :labels (into
                 #{}
                 (part-of-meal-labels))}
     :ingredients
      {:content (get-label 1025)
       :th {:style {:width "12%"}}
       :td {:style {:width "12%"
                    :text-align "right"}}
       }}
   })

(defn query-fn
  "Table query for meal entity"
  []
  {:entity-type entity-type
   :entity-filter {}
   :projection (:projection (columns-fn))
   :projection-include true
   :qsort {:mname 1}
   :pagination true
   :current-page 0
   :rows (pomme/calculate-rows)
   :collation {:locale "sr"}})

(defn table-conf-fn
  "Table configuration for meal entity"
  []
  {:preferences pomme/preferences
   :query-fn query-fn
   :query (query-fn)
   :columns (columns-fn)
   :form-conf (form-conf-fn)
   :actions [:details :edit :delete]
   :allowed-actions @allowed-actions
   :reports-on true
   :search-on true
   :search-fields [:mname
                   :description
                   :mtype]
   :render-in ".content"
   :table-class "entities"
   :table-fn gen-table})

;:mname
;:calories-sum
;:proteins-sum
;:fats-sum
;:carbonhydrates-sum
;:mtype ["Breakfast" "Lunch" "Dinner"]
;:description
;:image
;:ingredients [{:id  "grocery1-id"
;               :grams  "grams of grocery1"
;               :quantity  "quantity of grocery1"}
;              {:id  "grocery2-id"
;               :grams  "grams of grocery2"
;               :quantity  "quantity of grocery2"}]

