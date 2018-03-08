(ns personal-organiser-client.meal.entity
 (:require [personal-organiser-client.ajax :refer [ajax get-response]]))

(def entity-type "meal")

(def entity-sub-type "grocery")

(def get-entities-url "/clojure/get-entities")

(defn get-groceries-success
 ""
 [xhr]
 (let [response (get-response xhr)
       entities (:data response)]
  (def groceries entities)
  ))

(defn- get-groceries
 ""
 []
 (ajax
  {:url get-entities-url
   :success-fn get-groceries-success
   :entity {:entity-type entity-sub-type
            :entity-filter {}
            :projection [:_id
                         :gname]
            :projection-include true
            :qsort {:gname 1}
            :pagination false
            :current-page 0
            :rows 0
            :collation {:locale "sr"}}
   }))

(def form-fields
  {:entity-type     entity-type
   :entity-id       :_id
   :entity-fields   {:mname  {:label  "Name"
                              :field-type  "input"
                              :data-type  "text"}
                     :calories-sum  {:label  "Calories sum"
                                     :field-type  "input"
                                     :data-type  "number"
                                     :step  "0.1"
                                     :disabled  true}
                     :proteins-sum  {:label  "Proteins sum"
                                     :field-type  "input"
                                     :data-type  "number"
                                     :step  "0.1"
                                     :disabled  true}
                     :fats-sum  {:label  "Fats sum"
                                 :field-type  "input"
                                 :data-type  "number"
                                 :step  "0.1"
                                 :disabled true}
                     :carbonhydrates-sum  {:label  "Carbonhydrates sum"
                                           :field-type  "input"
                                           :data-type  "number"
                                           :step  "0.1"
                                           :disabled true}
                     :description  {:label  "Description"
                                    :field-type  "textarea"
                                    :data-type  "text"}
                     :image  {:label  "Image"
                              :field-type  "input"
                              :data-type  "file"}
                     :mtype  {:label  "Type of meal"
                              :field-type  "radio"
                              :data-type  "text"
                              :options  ["Breakfast" "Lunch" "Dinner"]}
                     :ingredients  {:label  "Ingredients"
                                    :field-type  "sub-form"
                                    :data-type  "form"
                                    :sub-fields
                                     {:grocery
                                      {:label  "Grocery"
                                       :field-type  "select"
                                       :data-type  "select-one"
                                       :options (do (get-groceries)
                                                    groceries)
                                       }
                                      :grams
                                       {:label  "Grams"
                                        :field-type  "input"
                                        :data-type  "number"}
                                      :quantity
                                       {:label  "Quantity"
                                        :field-type  "input"
                                        :data-type  "number"}}
                                    :sub-fields-order
                                     [:grocery
                                      :grams
                                      :quantity]}}
   :fields-order  [:mname
                   :calories-sum
                   :proteins-sum
                   :fats-sum
                   :carbonhydrates-sum
                   :description
                   :image
                   :mtype
                   :ingredients]})

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
      :table-class "groceries"
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

