(ns personal-organiser-client.meal.entity
  (:require [ajax-lib.core :refer [ajax get-response]]
            [htmlcss-lib.core :refer [gen div input table thead tbody
                                      tr th td h4 select option]]
            [js-lib.core :as md]
            [framework-lib.core :refer [gen-table]]
            [utils-lib.core :refer [round-decimals]]
            [cljs.reader :as reader]
            [language-lib.core :refer [get-label]]
            [common-client.allowed-actions.controller :refer [allowed-actions]]))

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
                        (get-label 1009)
                        {:style {:width "200px"}})
                      (th
                        (get-label 1027)
                        {:style {:width "40px"}})
                      (th
                        (get-label 1028)
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
                                   "#calories-sum"))
                               0)
              proteins-sum (or (reader/read-string
                                 (md/get-value
                                   "#proteins-sum"))
                               0)
              fats-sum (or (reader/read-string
                             (md/get-value
                               "#fats-sum"))
                           0)
              carbonhydrates-sum (or (reader/read-string
                                       (md/get-value
                                         "#carbonhydrates-sum"))
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
           "#calories-sum"
           (round-decimals
             calories
             2))
         (md/set-value
           "#proteins-sum"
           (round-decimals
             proteins
             2))
         (md/set-value
           "#fats-sum"
           (round-decimals
             fats
             2))
         (md/set-value
           "#carbonhydrates-sum"
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
                "#mname"))
     :calories-sum (str
                     (md/get-value
                       "#calories-sum"))
     :proteins-sum (str
                     (md/get-value
                       "#proteins-sum"))
     :fats-sum (str
                 (md/get-value
                   "#fats-sum"))
     :carbonhydrates-sum (str
                           (md/get-value
                             "#carbonhydrates-sum"))
     :description (str
                    (md/get-value
                      "#description"))
     :image (aget
              (md/query-selector
                "#image")
              "src")
     :mtype (md/cb-checked-values
              "mtype")
     :portion (str
                (md/checked-value
                  "portion"))})
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
   attrs]
  (let [disabled (:disabled attrs)]
    [(tr
       (td
         (h4
           "Ingredients"
           {:id "lblIngredients"
            :style {:text-align "center"}})
         {:colspan 3}))
     (tr
       [(td
          (get-label 1009))
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
          (get-label 1027))
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
          (get-label 1028))
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
                       (get-label 1009)
                       {:style {:width "200px"}})
                     (th
                       (get-label 1027)
                       {:style {:width "40px"}})
                     (th
                       (get-label 1028)
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
 )

(def form-conf
     {:id :_id
      :type entity-type
      :entity-name (get-label 1017)
      :fields {:mname {:label (get-label 1010)
                       :input-el "text"
                       :attrs {:required "required"}}
               :calories-sum {:label (get-label 1018)
                              :input-el "number"
                              :attrs {:step "0.1"
                                      :disabled true}}
               :proteins-sum {:label (get-label 1019)
                              :input-el "number"
                              :attrs {:step "0.1"
                                      :disabled true}}
               :fats-sum {:label (get-label 1020)
                          :input-el "number"
                          :attrs {:step "0.1"
                                  :disabled true}}
               :carbonhydrates-sum {:label (get-label 1021)
                                    :input-el "number"
                                    :attrs {:step "0.1"
                                            :disabled true}}
               :description  {:label (get-label 1015)
                              :input-el "textarea"
                              :attrs {:required "required"}}
               :image {:label (get-label 1022)
                       :input-el "img"}
               :mtype {:label (get-label 1023)
                       :input-el "checkbox"
                       :attrs {:required "required"}
                       :options ["Breakfast"
                                 "Lunch"
                                 "Dinner"]}
               :portion {:label (get-label 1024)
                         :input-el "radio"
                         :attrs {:required "required"}
                         :options ["Main course"
                                   "Sauce"
                                   "Beverage"
                                   "Soup"
                                   "Sweets, Cakes, Compote, Ice cream"
                                   "Salad"]}
               :ingredients {:label (get-label 1025)
                             :input-el "sub-form"
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
         {:content (get-label 1010)
          :th {:style {:width "200px"}}
          :td {:style {:width "200px"
                       :text-align "left"}}
          }
        :calories-sum
         {:content (get-label 1018)
          :th {:style {:width "40px"}
               :title (get-label 1018)}
          :td {:style {:text-align "right"}}
          }
        :proteins-sum
         {:content (get-label 1019)
          :th {:style {"width" "40px"}
               :title (get-label 1019)}
          :td {:style {"text-align" "right"}}
          }
        :fats-sum
         {:content (get-label 1020)
          :th {:style {:width "40px"}
               :title (get-label 1020)}
          :td {:style {:text-align "right"}}
          }
        :carbonhydrates-sum
         {:content (get-label 1021)
          :th {:style {"width" "40px"}
               :title (get-label 1021)}
          :td {:style {:text-align "right"}}
          }
        :description
         {:content (get-label 1015)
          :th {:style {"width" "40px"}
               :title (get-label 1015)}
          :td {:style {:text-align "right"}}
          }
        :image
         {:content (get-label 1022)
          :th {:style {"width" "40px"}
               :title (get-label 1022)}
          :td {:style {:text-align "right"}}
          }
        :mtype
         {:content (get-label 1023)
          :th {:style {"width" "40px"}
               :title (get-label 1023)}
          :td {:style {:text-align "right"}}
          }
        :ingredients
         {:content (get-label 1024)
          :th {:style {"width" "40px"}
               :title (get-label 1024)}
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

(defn table-conf-fn
  ""
  []
  {:query query
   :columns columns
   :form-conf form-conf
   :actions [:details :edit :delete]
   :allowed-actions @allowed-actions
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

