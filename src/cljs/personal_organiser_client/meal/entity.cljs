(ns personal-organiser-client.meal.entity
 (:require [personal-organiser-client.ajax :as ajx :refer [ajax get-response]]
           [personal-organiser-client.generate-html :refer [gen crt]]
           [personal-organiser-client.manipulate-dom :as md]
           [personal-organiser-client.display-data :refer [table]]
           [personal-organiser-client.utils :refer [round-decimals]]
           [cljs.reader :as reader]))

(def entity-type "meal")

(def entity-sub-type "grocery")

(def get-entities-url "/clojure/get-entities")

(defn- populate-select
 "Generate options for select HTML element"
 [xhr]
 (let [response (get-response xhr)
       entities (:data response)
       options (atom [])]
  (doseq [[opt-value
           opt-label
           opt-calories
           opt-proteins
           opt-fats
           opt-carbonhydrates] entities]
   (swap! options conj (crt "option"
                            opt-label
                            {:value opt-value
                             :opt-calories opt-calories
                             :opt-proteins opt-proteins
                             :opt-fats opt-fats
                             :opt-carbonhydrates opt-carbonhydrates}))
   )
  (md/append-element ".entity #slctGrocery" (gen @options))
  (md/remove-all-event ".entity #slctGrocery" "onfocus")
  ))

(def select-conf
 {:entity-type entity-sub-type
  :entity-filter {}
  :projection [:gname
               :calories
               :proteins
               :fats
               :carbonhydrates]
  :projection-include true
  :qsort {:gname 1}
  :pagination false
  :current-page 0
  :rows 0
  :collation {:locale "sr"}})

(defn- get-options
 ""
 []
 (ajax
  {:url get-entities-url
   :success-fn populate-select
   :entity select-conf}))

(defn- add-ingrediant-in-table
 ""
 [{selected-g-value :value
   opt-calories :opt-calories
   opt-proteins :opt-proteins
   opt-fats :opt-fats
   opt-carbonhydrates :opt-carbonhydrates
   selected-g-label :label}
  grams-g
  quantity-g]
 (let [i-table (md/query-selector ".entity #i-table")
       ing-exists (md/query-selector (str ".entity #i-table td[value=\""
                                          selected-g-value "\"]"))]
  (if i-table
   (if ing-exists
    (.log js/console "Ingrediant exists")
    (md/append-element
    "#i-table tbody"
    (gen
     (crt "tr"
          [(crt "td"
                selected-g-label
                {:value selected-g-value})
           (crt "td"
                grams-g
                {:value grams-g
                 :style {:text-align "right"}})
           (crt "td"
                quantity-g
                {:value quantity-g
                 :style {:text-align "right"}})])
     ))
    )
   (do (md/remove-element
        "#i-table-placeholder table")
       (md/append-element
        "#i-table-placeholder"
        (gen
         (crt "div"
              (crt "table"
                   [(crt "thead"
                         (crt "tr"
                              [(crt "th"
                                    "Grocery"
                                    {:style {:width "200px"}})
                               (crt "th"
                                    "Grams"
                                    {:style {:width "40px"}})
                               (crt "th"
                                    "Quantity"
                                    {:style {:width "40px"}})]))
                    (crt "tbody"
                         (crt "tr"
                              [(crt "td"
                                    selected-g-label
                                    {:value selected-g-value})
                               (crt "td"
                                    grams-g
                                    {:value grams-g
                                     :style {:text-align "right"}})
                               (crt "td"
                                    quantity-g
                                    {:value quantity-g
                                     :style {:text-align "right"}})]
                          )
                     )]
                   {:id "i-table"
                    :style {:border-spacing "initial"}})
              {:class "entities"})
         )
        ))
   ))
 )

(defn- add-ingredient
 ""
 []
 (let [selected-g-slctr "#slctGrocery"
       grams-g-slctr "#numGrams"
       quantity-g-slctr "#numQuantity"
       selected-g (md/get-selected-options selected-g-slctr)
       grams-g (md/get-value grams-g-slctr)
       quantity-g (md/get-value quantity-g-slctr)
       {selected-g-value :value} selected-g
       ing-exists (md/query-selector (str ".entity #i-table td[value=\""
                                      selected-g-value "\"]"))
       invalid-fields (atom [])]
  (md/remove-class selected-g-slctr "error")
  (md/remove-class grams-g-slctr "error")
  (md/remove-class quantity-g-slctr "error")
  (md/remove-class selected-g-slctr "success")
  (md/remove-class grams-g-slctr "success")
  (md/remove-class quantity-g-slctr "success")
  (when (or ing-exists
            (= selected-g
               {:value "-1"
                :label "- Select one -"}))
   (swap! invalid-fields conj selected-g-slctr))
  (when (empty? grams-g)
   (swap! invalid-fields conj grams-g-slctr))
  (when (empty? quantity-g)
   (swap! invalid-fields conj quantity-g-slctr))
  (if (empty? @invalid-fields)
   (do
    (add-ingrediant-in-table
     selected-g
     grams-g
     quantity-g)
    (let [calories-sum (or (reader/read-string
                            (md/get-value
                             "#txtCaloriessum"))
                           0)
          proteins-sum (or (reader/read-string
                            (md/get-value
                             "#txtProteinssum"))
                           0)
          fats-sum (or (reader/read-string
                        (md/get-value
                         "#txtFatssum"))
                       0)
          carbonhydrates-sum (or (reader/read-string
                                  (md/get-value
                                   "#txtCarbonhydratessum"))
                                 0)
          weight (* (/ grams-g
                       100)
                    quantity-g)
          calories (+ calories-sum
                      (* (reader/read-string
                          (:opt-calories selected-g))
                         weight))
          proteins (+ proteins-sum
                      (* (reader/read-string
                          (:opt-proteins selected-g))
                         weight))
          fats (+ fats-sum
                  (* (reader/read-string
                      (:opt-fats selected-g))
                     weight))
          carbonhydrates (+ carbonhydrates-sum
                            (* (reader/read-string
                                (:opt-carbonhydrates selected-g))
                               weight))]
     (md/set-value "#txtCaloriessum" (round-decimals calories
                                                      2))
     (md/set-value "#txtProteinssum" (round-decimals proteins
                                                      2))
     (md/set-value "#txtFatssum" (round-decimals fats
                                                  2))
     (md/set-value "#txtCarbonhydratessum" (round-decimals carbonhydrates
                                                            2)))
    )
   (doseq [invalid-field @invalid-fields]
    (md/add-class invalid-field "error"))
   ))
 )

(defn- read-form
 ""
 [entity]
 (swap! entity conj {:mname (str (md/get-value "#txtName"))})
 (swap! entity conj {:calories-sum (str (md/get-value "#txtCaloriessum"))})
 (swap! entity conj {:proteins-sum (str (md/get-value "#txtProteinssum"))})
 (swap! entity conj {:fats-sum (str (md/get-value "#txtFatssum"))})
 (swap! entity conj {:carbonhydrates-sum (str (md/get-value "#txtCarbonhydratessum"))})
 (swap! entity conj {:description (str (md/get-value "#taDescription"))})
 (swap! entity conj {:image (aget (md/query-selector "#imgImage") "src")})
 (swap! entity conj {:mtype (str (md/checked-value "rTypeofmeal"))})
 (let [itr (atom 0)
       ingrediant (atom [])
       ingrediants (atom [])
       tds (md/query-selector-all "#i-table-placeholder tbody td")]
  (doseq [td tds]
   (if (< @itr 3)
    (do
     (swap! ingrediant conj (md/get-attr td "value"))
     (when (= @itr
              0)
      (swap! ingrediant conj (md/get-inner-html td))
      )
     (swap! itr inc))
    (do
     (swap! ingrediants conj @ingrediant)
     (reset! ingrediant [])
     (swap! ingrediant conj (md/get-attr td "value"))
     (swap! ingrediant conj (md/get-inner-html td))
     (reset! itr 1))
    ))
   (swap! ingrediants conj @ingrediant)
   (swap! entity conj {:ingrediants @ingrediants})
  )
 (str @entity))

(defn- sub-form
 ""
 [data
  disabled]
 [(crt "tr"
       (crt "td"
            (crt "h4"
                 "Ingredients"
                 {:id "lblIngredients"
                  :style {:text-align "center"}})
            {:colspan 3}))
  (crt "tr"
       [(crt "td"
             "Grocery")
        (crt "td"
             (crt "select"
                  (crt "option"
                       "- Select one -"
                       {:value "-1"})
                  (if disabled
                   (conj {:id "slctGrocery"}
                         {:disabled "disabled"})
                   {:id "slctGrocery"})
                  {:onfocus {:evt-fn get-options}}))
        (crt "td")])
  (crt "tr"
       [(crt "td"
             "Grams")
        (crt "td"
             (crt "input"
                  ""
                  (if disabled
                   (conj {:id "numGrams"
                          :name "numGrams"
                          :type "number"}
                         {:disabled "disabled"})
                   {:id "numGrams"
                    :name "numGrams"
                    :type "number"})
                  ))
        (crt "td")])
  (crt "tr"
       [(crt "td"
             "Quantity")
        (crt "td"
             (crt "input"
                  ""
                  (if disabled
                   (conj {:id "numQuantity"
                          :name "numQuantity"
                          :type "number"}
                         {:disabled "disabled"})
                   {:id "numQuantity"
                    :name "numQuantity"
                    :type "number"})))
        (crt "td")])
  (crt "tr"
       [(crt "td")
        (crt "td"
             (crt "input"
                  ""
                  (if disabled
                   (conj {:id "btnAddIngredient"
                          :name "btnAddIngredient"
                          :type "button"
                          :value "Add"}
                         {:disabled "disabled"})
                   {:id "btnAddIngredient"
                    :name "btnAddIngredient"
                    :type "button"
                    :value "Add"})
                  {:onclick {:evt-fn add-ingredient}}))
        (crt "td")])
  (if-let [ingrediants (:ingrediants data)]
   (crt "tr"
        (crt "td"
             (crt "div"
                  (crt "table"
                       [(crt "thead"
                             (crt "tr"
                                  [(crt "th"
                                        "Grocery"
                                        {:style {:width "200px"}})
                                   (crt "th"
                                        "Grams"
                                        {:style {:width "40px"}})
                                   (crt "th"
                                        "Quantity"
                                        {:style {:width "40px"}})]))
                        (crt "tbody"
                             (let [ingrediants-vector (atom [])]
                              (doseq [[_id gname grams quantity] ingrediants]
                               (swap!
                                 ingrediants-vector
                                 conj
                                 (crt "tr"
                                      [(crt "td"
                                            gname
                                            {:value _id})
                                       (crt "td"
                                            grams
                                            {:value grams
                                             :style {:text-align "right"}})
                                       (crt "td"
                                            quantity
                                            {:value quantity
                                             :style {:text-align "right"}})]))
                               )
                              @ingrediants-vector)
                         )]
                       {:id "i-table"
                        :style {:border-spacing "initial"}})
                  {:class "entities"})
             {:id "i-table-placeholder"
              :colspan 3
              :style {:width "390px"}}))
   (crt "tr"
        (crt "td"
             (crt "table"
                  (crt "tr"
                       (crt "td"
                            "No ingredients"))
                  {:style {:margin-left "calc(50% - 60px)"}}
              )
             {:id "i-table-placeholder"
              :colspan 3
              :style {:width "390px"}})))
  #_(crt "tr"
       (crt "td"
            (crt "input"
                 ""
                 {:id "btnTest"
                  :value "Test"
                  :type "button"}
                 {:onclick {:evt-fn read-form
                            :evt-p (atom {})}}))
   )
   
   ])

(def form-fields
  {:entity-type entity-type
   :entity-id :_id
   :entity-fields {:mname {:label "Name"
                           :field-type "input"
                           :data-type "text"}
                   :calories-sum {:label "Calories sum"
                                  :field-type "input"
                                  :data-type "number"
                                  :step "0.1"
                                  :disabled true}
                   :proteins-sum {:label "Proteins sum"
                                  :field-type "input"
                                  :data-type "number"
                                  :step "0.1"
                                  :disabled true}
                   :fats-sum {:label "Fats sum"
                              :field-type "input"
                              :data-type "number"
                              :step "0.1"
                              :disabled true}
                   :carbonhydrates-sum {:label "Carbonhydrates sum"
                                        :field-type "input"
                                        :data-type "number"
                                        :step "0.1"
                                        :disabled true}
                   :description  {:label "Description"
                                  :field-type "textarea"
                                  :data-type "text"}
                   :image {:label "Image"
                           :field-type "image"
                           :data-type "file"}
                   :mtype {:label "Type of meal"
                           :field-type "radio"
                           :data-type "text"
                           :options ["Breakfast" "Lunch" "Dinner"]}
                   :ingredients {:label "Ingrediants"
                                 :field-type "sub-form"
                                 :sub-form-trs sub-form}}
   :fields-order [:mname
                  :calories-sum
                  :proteins-sum
                  :fats-sum
                  :carbonhydrates-sum
                  :description
                  :image
                  :mtype
                  :ingredients]
   :specific-read-form read-form})

(def columns
 [:mname
  :calories-sum
  :proteins-sum
  :fats-sum
  :carbonhydrates-sum
; :description
; :image
; :mtype
; :ingredients
  ])

(def columns-styles
 [{:content    "Name"
   :header     {"width"      "200px"}
   :column     {"width"      "200px"
                "text-align" "left"}}
  {:content    "Calories sum"
   :header     {"width"      "40px"}
   :column     {"text-align" "right"}}
  {:content    "Proteins sum"
   :header     {"width"      "40px"}
   :column     {"text-align" "right"}}
  {:content    "Fats sum"
   :header     {"width"      "40px"}
   :column     {"text-align" "right"}}
  {:content    "Carbonhydrates sum"
   :header     {"width"      "40px"}
   :column     {"text-align" "right"}}
; {:content    "Description"
;  :header     {"width" "50px"}
;  :column     {"width" "50px"}}
; {:content    "Image"
;  :header     {"width" "50px"}
;  :column     {"width" "50px"}}
; {:content    "Meal type"
;  :header     {"width" "50px"}
;  :column     {"width" "50px"}}
; {:content    "Ingredients"
;  :header     {"width" "50px"}
;  :column     {"width" "50px"}}
  ])

(def table-query
     {:entity-type  entity-type
      :entity-filter  {}
      :projection  columns
      :projection-include  true
      :qsort  {:mname 1}
      :pagination  true
      :current-page  0
      :rows  25
      :collation {:locale "sr"}})

(def table-conf
     {:table-conf table-query
      :columns-styles columns-styles
      :entity-conf form-fields
      :details true
      :edit true
      :delete true
      :render-in ".content"
      ;:table-class "entities"
      :table-fn table
      ;:animation md/fade-in
      :animation-duration 100})

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

