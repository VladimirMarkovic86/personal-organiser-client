(ns personal-organiser-client.grocery.entity
 (:require [personal-organiser-client.display-data :refer [table]]))

(def entity-type "grocery")

(def form-fields
  {:entity-type     entity-type
   :entity-id       :_id
   :entity-fields   {:gname           {:label      "Name"
                                       :field-type "input"
                                       :data-type  "text"}
                     :calories        {:label      "Calories"
                                       :field-type "input"
                                       :data-type  "number"
                                       :step       "0.1"}
                     :proteins        {:label      "Proteins"
                                       :field-type "input"
                                       :data-type  "number"
                                       :step       "0.1"}
                     :fats            {:label      "Fats"
                                       :field-type "input"
                                       :data-type  "number"
                                       :step       "0.1"}
                     :carbonhydrates  {:label      "Carbonhydrates"
                                       :field-type "input"
                                       :data-type  "number"
                                       :step       "0.1"}
                     :description     {:label      "Description"
                                       :field-type "textarea"
                                       :data-type  "text"}
                     :origin          {:label      "Origin"
                                       :field-type "radio"
                                       :data-type  "text"
                                       :options    ["All" "Vegetarian"]}}
   :fields-order  [:gname
                   :calories
                   :proteins
                   :fats
                   :carbonhydrates
                   :description
                   :origin]})

(def columns
 [:gname
  :calories
  :proteins
  :fats
  :carbonhydrates
; :description
  :origin
  ])

(def columns-styles
 [{:content "Name"
   :th {:style {:width "200px"}}
   :td {:style {:width "200px"
                :text-align "left"}}
   }
  {:content "Cal"
   :th {:style {:width "40px"}
        :title "Calories"}
   :td {:style {:width "40px"
                :text-align "right"}}
   }
  {:content "Prot"
   :th {:style {:width "40px"}
        :title "Proteins"}
   :td {:style {:width "40px"
                :text-align "right"}}
   }
  {:content "Fats"
   :th {:style {:width "40px"}
        :title "Fats"}
   :td {:style {:width "40px"
                :text-align "right"}}
   }
  {:content "Carbs"
   :th {:style {:width "40px"}
        :title "Carbonhydrates"}
   :td {:style {:width "40px"
                :text-align "right"}}
   }
; {:content "Description"
;  :th {"width" "50px"}
;  :td {"width" "50px"}}
  {:content "Orig"
   :th {:style {:width "40px"}
        :title "Origin"}
   :td {:style {:width "40px"}}}]
 )

(def table-query
     {:entity-type  entity-type
      :entity-filter  {}
      :projection  columns
      :projection-include  true
      :qsort  {:gname 1}
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
      :table-fn table
      ;:animation md/fade-in
      :animation-duration 100})

