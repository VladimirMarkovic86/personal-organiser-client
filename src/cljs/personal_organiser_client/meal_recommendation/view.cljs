(ns personal-organiser-client.meal-recommendation.view
  (:require [htmlcss-lib.core :refer [gen div select option input
                                      table thead tr th tbody td
                                      label]]
            [js-lib.core :as md]
            [personal-organiser-client.meal-recommendation.controller :as mrc]
            [language-lib.core :refer [get-label]]
            [utils-lib.core :as utils]))

(defn collapse-all
  "Collapse all ingredient rows"
  []
  (let [ingredient-elements (md/query-selector-all-on-element
                              ".meal-recommendation-display"
                              ".expand")]
    (doseq [ingredient-el ingredient-elements]
      (md/remove-class
        ingredient-el
        "expand"))
   ))

(defn expand-ingredients-rows
  "Expands ingredients of clicked meal row"
  [evt-p
   element
   event]
  (if (md/contains-class
        element
        "expand")
    (collapse-all)
    (let [element-a (atom
                      (.-nextSibling
                        element))
          contains-ingredient-class (atom
                                      (md/contains-class
                                        @element-a
                                        "ingredient-row"))]
      (collapse-all)
      (md/add-class
        element
        "expand")
      (while @contains-ingredient-class
        (md/add-class
          @element-a
          "expand")
        (reset!
          element-a
          (.-nextSibling
            @element-a))
        (reset!
          contains-ingredient-class
          (when @element-a
            (md/contains-class
              @element-a
              "ingredient-row"))
         ))
     ))
 )

(defn build-meal-recommendation-table
  "Builds meal recommendation table out of data that was sent through parameter"
  [meal-recommendations
   meal-type]
  (let [meal-recommendations-trs (atom [])]
    (doseq [{mname :mname
             m-label-code :label-code
             calories-sum :calories-sum
             proteins-sum :proteins-sum
             fats-sum :fats-sum
             carbonhydrates-sum :carbonhydrates-sum
             ingredients :ingredients} meal-recommendations]
      (let [rounded-calories-sum (utils/round-decimals
                                   calories-sum
                                   2)
            rounded-proteins-sum (utils/round-decimals
                                   proteins-sum
                                   2)
            rounded-fats-sum (utils/round-decimals
                               fats-sum
                               2)
            rounded-carbonhydrates-sum (utils/round-decimals
                                         carbonhydrates-sum
                                         2)
            mname (if (and (utils/is-number?
                             m-label-code)
                           (pos?
                             m-label-code))
                    (get-label
                      m-label-code)
                    mname)]
        (swap!
          meal-recommendations-trs
          conj
          (tr
            [(td
               mname
               {:title mname
                :style {:text-align "left"
                        :width "40%"}})
             (td
               rounded-calories-sum
               {:title rounded-calories-sum
                :style {:text-align "right"
                        :width "12%"}})
             (td
               rounded-proteins-sum
               {:title rounded-proteins-sum
                :style {:text-align "right"
                        :width "12%"}})
             (td
               rounded-fats-sum
               {:title rounded-fats-sum
                :style {:text-align "right"
                        :width "12%"}})
             (td
               rounded-carbonhydrates-sum
               {:title rounded-carbonhydrates-sum
                :style {:text-align "right"
                        :width "12%"}})
             (td
               ""
               {:style {:width "12%"}})]
            {:class "meal-row"}
            {:onclick {:evt-fn expand-ingredients-rows}}))
       )
      (doseq [{gname :gname
               g-label-code :label-code
               calories :calories
               proteins :proteins
               fats :fats
               carbonhydrates :carbonhydrates
               grams :grams} ingredients]
        (let [rounded-calories (utils/round-decimals
                                 calories
                                 2)
              rounded-proteins (utils/round-decimals
                                 proteins
                                 2)
              rounded-fats (utils/round-decimals
                             fats
                             2)
              rounded-carbonhydrates (utils/round-decimals
                                       carbonhydrates
                                       2)
              rounded-grams (utils/round-decimals
                              grams
                              2)
              gname (if (and (utils/is-number?
                               g-label-code)
                             (pos?
                               g-label-code))
                      (get-label
                        g-label-code)
                      gname)]
          (swap!
            meal-recommendations-trs
            conj
            (tr
              [(td
                 gname
                 {:title gname
                  :style {:text-align "right"
                          :width "40%"}})
               (td
                 rounded-calories
                 {:title rounded-calories
                  :style {:text-align "right"
                          :width "12%"}})
               (td
                 rounded-proteins
                 {:title rounded-proteins
                  :style {:text-align "right"
                          :width "12%"}})
               (td
                 rounded-fats
                 {:title rounded-fats
                  :style {:text-align "right"
                          :width "12%"}})
               (td
                 rounded-carbonhydrates
                 {:title rounded-carbonhydrates
                  :style {:text-align "right"
                          :width "12%"}})
               (td
                 rounded-grams
                 {:title rounded-grams
                  :style {:text-align "right"
                          :width "12%"}})]
              {:class "ingredient-row"}))
         ))
     )
    (gen
      (table
        [(thead
           [(tr
              (th
                meal-type
                {:colspan 6}))
            (tr
              [(th
                 (get-label
                   1010))
               (th
                 (get-label
                   1011)
                 {:title (get-label
                           1011)})
               (th
                 (get-label
                   1012)
                 {:title (get-label
                           1012)})
               (th
                 (get-label
                   1013)
                 {:title (get-label
                           1013)})
               (th
                 (get-label
                   1014)
                 {:title (get-label
                           1014)})
               (th
                 (get-label
                   1027)
                 {:title (get-label
                           1027)})]
             )])
         (tbody
           @meal-recommendations-trs)])
     ))
 )

(defn display-meal-recommendations
  "Displays server response on recommended meals"
  [{breakfast-recommendations :breakfast-recommendations
    lunch-recommendations :lunch-recommendations
    dinner-recommendations :dinner-recommendations}]
  (let [breakfast-recommendation-table (build-meal-recommendation-table
                                         breakfast-recommendations
                                         (get-label
                                           1030))
        lunch-recommendation-table (build-meal-recommendation-table
                                     lunch-recommendations
                                     (get-label
                                       1031))
        dinner-recommendation-table (build-meal-recommendation-table
                                      dinner-recommendations
                                      (get-label
                                        1032))]
    (md/remove-element-content
      ".meal-recommendation-display")
    (md/append-element
      ".meal-recommendation-display"
      breakfast-recommendation-table)
    (md/append-element
      ".meal-recommendation-display"
      lunch-recommendation-table)
    (md/append-element
      ".meal-recommendation-display"
      dinner-recommendation-table))
 )

(defn meal-recommendation-pure-html
  "Construct html meal recommendation view and append it"
  []
  (md/remove-element-content
    ".content")
  (let [organisms (mrc/get-organisms)
        options (atom
                  [(option
                     (get-label 33)
                     {:value "-1"})])
        meal-recommendation-form (gen
                                   (div
                                     [(div
                                        [(label
                                           [(get-label
                                              1026)
                                            (select
                                              (do
                                                (doseq [{first-name :first-name
                                                         last-name :last-name
                                                         _id :_id} organisms]
                                                  (swap!
                                                    options
                                                    conj
                                                    (option
                                                      (str
                                                        last-name
                                                        " "
                                                        first-name)
                                                      {:value _id}))
                                                 )
                                                @options)
                                              {:id "meal-recommendation-organisms"})])
                                         (label
                                           [(get-label
                                              1260)
                                            (input
                                              ""
                                              {:id "breakfast-calories-share"
                                               :type "number"
                                               :placeholder (get-label
                                                              1260)}
                                              nil
                                              {:valueAsNumber 35})])
                                         (label
                                           [(get-label
                                              1261)
                                            (input
                                              ""
                                              {:id "lunch-calories-share"
                                               :type "number"
                                               :placeholder (get-label
                                                              1261)}
                                              nil
                                              {:valueAsNumber 40})])
                                         (label
                                           [(get-label
                                              1262)
                                            (input
                                              ""
                                              {:id "dinner-calories-share"
                                               :type "number"
                                               :placeholder (get-label
                                                              1262)}
                                              nil
                                              {:valueAsNumber 25})])
                                         (input
                                           ""
                                           {:id "meal-recommendation-calculate"
                                            :class "btn"
                                            :value (get-label 1068)
                                            :type "button"}
                                           {:onclick
                                             {:evt-fn mrc/calculate-meal-recommendations
                                              :evt-p display-meal-recommendations}})]
                                        {:class "meal-recommendation-commands"})
                                      (div
                                        ""
                                        {:class "meal-recommendation-display"})]
                                     {:class "meal-recommendation"}))]
    (md/append-element
      ".content"
      meal-recommendation-form))
 )

