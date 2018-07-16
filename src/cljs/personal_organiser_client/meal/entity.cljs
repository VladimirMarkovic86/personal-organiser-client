(ns personal-organiser-client.meal.entity
 (:require [ajax-lib.core :refer [ajax get-response]]
           [htmlcss-lib.core :refer [gen div input table thead tbody
                                     tr th td h4 select option]]
           [js-lib.core :as md]
           [framework-lib.core :refer [gen-table]]
           [utils-lib.core :refer [round-decimals]]
           [cljs.reader :as reader]))

(def entity-type
     "meal")

(def entity-sub-type
     "grocery")

(def get-entities-url
     "/clojure/get-entities")

(defn- populate-select
  "Generate options for select HTML element"
  [xhr]
  (let [response (get-response xhr)
        entities (:data response)
        options (atom [])]
    (doseq [{opt-value :_id
             opt-label :gname
             opt-calories :calories
             opt-proteins :proteins
             opt-fats :fats
             opt-carbonhydrates :carbonhydrates} entities]
      (swap!
        options
        conj
        (option
          opt-label
          {:value opt-value
           :opt-calories opt-calories
           :opt-proteins opt-proteins
           :opt-fats opt-fats
           :opt-carbonhydrates opt-carbonhydrates}))
     )
    (md/append-element
      ".entity #slctGrocery"
      (gen @options))
    (md/remove-all-event
      ".entity #slctGrocery"
      "onfocus"))
 )

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
  "Add ingredient in table"
  [{selected-g-value :value
    opt-calories :opt-calories
    opt-proteins :opt-proteins
    opt-fats :opt-fats
    opt-carbonhydrates :opt-carbonhydrates
    selected-g-label :label}
   grams-g
   quantity-g]
  (let [i-table (md/query-selector
                  ".entity #i-table")
        ing-exists (md/query-selector
                     (str
                       ".entity #i-table td[value=\""
                       selected-g-value "\"]"))]
    (if i-table
      (if ing-exists
        (.log js/console "Ingrediant exists")
        (md/append-element
          "#i-table tbody"
          (gen
            (tr
              [(td
                 selected-g-label
                 {:value selected-g-value})
               (td
                 grams-g
                 {:value grams-g
                  :style {:text-align "right"}})
               (td
                 quantity-g
                 {:value quantity-g
                  :style {:text-align "right"}})])
           ))
       )
      (do
        (md/remove-element
          "#i-table-placeholder table")
        (md/append-element
          "#i-table-placeholder"
          (gen
            (div
              (table
                [(thead
                   (tr
                     [(th
                        "Grocery"
                        {:style {:width "200px"}})
                      (th
                        "Grams"
                        {:style {:width "40px"}})
                      (th
                        "Quantity"
                        {:style {:width "40px"}})])
                  )
                 (tbody
                   (tr
                     [(td
                        selected-g-label
                        {:value selected-g-value})
                      (td
                        grams-g
                        {:value grams-g
                         :style {:text-align "right"}})
                      (td
                        quantity-g
                        {:value quantity-g
                         :style {:text-align "right"}})])
                  )]
                {:id "i-table"
                 :style {:border-spacing "initial"}})
              {:class "entities"}))
         ))
     ))
 )

(defn- add-ingredient
  "Add ingredient in table and calculate calories, proteins, fats and carbonhydrates"
  []
  (let [selected-g-slctr "#slctGrocery"
        grams-g-slctr "#numGrams"
        quantity-g-slctr "#numQuantity"
        selected-g (md/get-selected-options
                     selected-g-slctr)
        grams-g (md/get-value
                  grams-g-slctr)
        quantity-g (md/get-value
                     quantity-g-slctr)
        {selected-g-value :value} selected-g
        ing-exists (md/query-selector
                     (str
                       ".entity #i-table td[value=\""
                       selected-g-value
                       "\"]"))
        invalid-fields (atom [])]
    (md/remove-class
      selected-g-slctr
      "error")
    (md/remove-class
      grams-g-slctr
      "error")
    (md/remove-class
      quantity-g-slctr
      "error")
    (md/remove-class
      selected-g-slctr
      "success")
    (md/remove-class
      grams-g-slctr
      "success")
    (md/remove-class
      quantity-g-slctr
      "success")
    (when (or ing-exists
              (= selected-g
                 {:value "-1"
                  :label "- Select one -"}))
      (swap!
        invalid-fields
        conj
        selected-g-slctr))
    (when (empty? grams-g)
      (swap!
        invalid-fields
        conj
        grams-g-slctr))
    (when (empty? quantity-g)
      (swap!
        invalid-fields
        conj
        quantity-g-slctr))
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
         (md/set-value
           "#txtCaloriessum"
           (round-decimals
             calories
             2))
         (md/set-value
           "#txtProteinssum"
           (round-decimals
             proteins
             2))
         (md/set-value
           "#txtFatssum"
           (round-decimals
             fats
             2))
         (md/set-value
           "#txtCarbonhydratessum"
           (round-decimals
             carbonhydrates
             2))
         ))
      (doseq [invalid-field @invalid-fields]
        (md/add-class
          invalid-field
          "error"))
     ))
 )

(defn- read-form
  "Read meal form"
  [entity]
  (swap!
    entity
    conj
    {:mname (str
              (md/get-value
                "#txtName"))
     :calories-sum (str
                     (md/get-value
                       "#txtCaloriessum"))
     :proteins-sum (str
                     (md/get-value
                       "#txtProteinssum"))
     :fats-sum (str
                 (md/get-value
                   "#txtFatssum"))
     :carbonhydrates-sum (str
                           (md/get-value
                             "#txtCarbonhydratessum"))
     :description (str
                    (md/get-value
                      "#taDescription"))
     :image (aget
              (md/query-selector
                "#imgImage")
              "src")
     :mtype (md/cb-checked-values
              "cbTypeofmeal")
     :portion (str
                (md/checked-value
                  "rPortion"))})
  (let [itr (atom 0)
        ingrediant (atom [])
        ingrediants (atom [])
        tds (md/query-selector-all
              "#i-table-placeholder tbody td")]
    (doseq [td tds]
      (if (< @itr 3)
        (do
          (swap!
            ingrediant
            conj
            (md/get-attr
              td
              "value"))
          (when (= @itr
                   0)
           (swap!
             ingrediant
             conj
             (md/get-inner-html
               td))
           )
          (swap!
            itr
            inc))
        (do
          (swap!
            ingrediants
            conj
            @ingrediant)
          (reset!
            ingrediant
            [])
          (swap!
            ingrediant
            conj
            (md/get-attr
              td
              "value"))
          (swap!
            ingrediant
            conj
            (md/get-inner-html
              td))
          (reset!
            itr
            1))
       ))
    (swap!
      ingrediants
      conj
      @ingrediant)
    (swap!
      entity
      conj
      {:ingrediants @ingrediants}))
  ;(.log js/console (str @entity))
  (str
    @entity))

(defn- sub-form
  "Generate ingredients sub form"
  [data
   disabled]
  [(tr
     (td
       (h4
         "Ingredients"
         {:id "lblIngredients"
          :style {:text-align "center"}})
       {:colspan 3}))
   (tr
     [(td
        "Grocery")
      (td
        (select
          (option
            "- Select one -"
            {:value "-1"})
          (if disabled
            {:id "slctGrocery"
             :disabled "disabled"}
            {:id "slctGrocery"})
          {:onfocus {:evt-fn get-options}}))
      (td)])
   (tr
     [(td
        "Grams")
      (td
        (input
          ""
          (if disabled
            {:id "numGrams"
             :name "numGrams"
             :type "number"
             :disabled "disabled"}
            {:id "numGrams"
             :name "numGrams"
             :type "number"}))
       )
      (td)])
   (tr
     [(td
        "Quantity")
      (td
        (input
          ""
          (if disabled
            {:id "numQuantity"
             :name "numQuantity"
             :type "number"
             :disabled "disabled"}
            {:id "numQuantity"
             :name "numQuantity"
             :type "number"}))
       )
      (td)])
   (tr
     [(td)
      (td
        (input
          ""
          (if disabled
            {:id "btnAddIngredient"
             :name "btnAddIngredient"
             :type "button"
             :value "Add"
             :disabled "disabled"}
            {:id "btnAddIngredient"
             :name "btnAddIngredient"
             :type "button"
             :value "Add"})
          {:onclick {:evt-fn add-ingredient}}))
      (td)])
   (if-let [ingrediants (:ingrediants data)]
     (tr
       (td
         (div
           (table
             [(thead
                (tr
                  [(th
                     "Grocery"
                     {:style {:width "200px"}})
                   (th
                     "Grams"
                     {:style {:width "40px"}})
                   (th
                     "Quantity"
                     {:style {:width "40px"}})]))
              (tbody
                (let [ingrediants-vector (atom [])]
                  (doseq [[_id gname grams quantity] ingrediants]
                    (swap!
                      ingrediants-vector
                      conj
                      (tr
                        [(td
                           gname
                           {:value _id})
                         (td
                           grams
                           {:value grams
                            :style {:text-align "right"}})
                         (td
                           quantity
                           {:value quantity
                            :style {:text-align "right"}})])
                     ))
                 @ingrediants-vector))]
             {:id "i-table"
              :style {:border-spacing "initial"}})
           {:class "entities"})
         {:id "i-table-placeholder"
          :colspan 3
          :style {:width "390px"}}))
    (tr
      (td
        (table
          (tr
            (td
              "No ingredients"))
          {:style {:margin-left "calc(50% - 60px)"}})
        {:id "i-table-placeholder"
         :colspan 3
         :style {:width "390px"}}))
    )])

(def form-conf
     {:id :_id
      :type entity-type
      :fields {:mname {:label "Name"
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
                       :field-type "checkbox"
                       :data-type "text"
                       :options ["Breakfast"
                                 "Lunch"
                                 "Dinner"]}
               :portion {:label "Portion"
                          :field-type "radio"
                          :data-type "text"
                          :options ["Main course"
                                    "Sauce"
                                    "Beverage"
                                    "Soup"
                                    "Sweets, Cakes, Compote, Ice cream"
                                    "Salad"]}
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
                     :portion
                     :ingredients]
      :specific-read-form read-form})

(def columns
     {:projection [:mname
                   :calories-sum
                   :proteins-sum
                   :fats-sum
                   :carbonhydrates-sum
                 ; :description
                 ; :image
                 ; :mtype
                 ; :ingredients
                   ]
      :style
       {:mname
         {:content "Name"
          :th {:style {:width "200px"}}
          :td {:style {:width "200px"
                       :text-align "left"}}
          }
        :calories-sum
         {:content "Cal sum"
          :th {:style {:width "40px"}
               :title "Calories sum"}
          :td {:style {:text-align "right"}}
          }
        :proteins-sum
         {:content "Prot sum"
          :th {:style {"width"      "40px"}
               :title "Proteins sum"}
          :td {:style {"text-align" "right"}}
          }
        :fats-sum
         {:content "Fats sum"
          :th {:style {:width "40px"}}
          :td {:style {:text-align "right"}}
          }
        :carbonhydrates-sum
         {:content    "Ch sum"
          :th {:style {"width" "40px"}
               :title "Carbonhydrates sum"}
          :td {:style {:text-align "right"}}
          }
        :description
         {:content    "Desc"
          :th {:style {"width" "40px"}
               :title "Description"}
          :td {:style {:text-align "right"}}
          }
        :image
         {:content    "Img"
          :th {:style {"width" "40px"}
               :title "Image"}
          :td {:style {:text-align "right"}}
          }
        :mtype
         {:content    "M. t"
          :th {:style {"width" "40px"}
               :title "Meal type"}
          :td {:style {:text-align "right"}}
          }
        :ingredients
         {:content    "Ing"
          :th {:style {"width" "40px"}
               :title "Ingredients"}
          :td {:style {:text-align "right"}}
          }}
       })

(def query
     {:entity-type  entity-type
      :entity-filter  {}
      :projection  (:projection columns)
      :projection-include  true
      :qsort  {:mname 1}
      :pagination  true
      :current-page  0
      :rows  25
      :collation {:locale "sr"}})

(def table-conf
     {:query query
      :columns columns
      :form-conf form-conf
      :actions #{:details :edit :delete}
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

