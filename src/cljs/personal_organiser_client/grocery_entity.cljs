(ns personal-organiser-client.grocery-entity)

(def grocery-entity
  {:entity-type     "grocery"
   :entity-id       :gname
   :entity-fields   {:gname           {:label      "Name"
                                       :field-type "input"
                                       :data-type  "text"}
                     :calories        {:label      "Calories"
                                       :field-type "input"
                                       :data-type  "number"}
                     :fats            {:label      "Fats"
                                       :field-type "input"
                                       :data-type  "number"}
                     :proteins        {:label      "Proteins"
                                       :field-type "input"
                                       :data-type  "number"
                                       :step       "0.1"}
                     :carbonhydrates  {:label      "Carbonhydrates"
                                       :field-type "input"
                                       :data-type  "number"}
                     :water           {:label      "Water"
                                       :field-type "input"
                                       :data-type  "number"}
                     :description     {:label      "Description"
                                       :field-type "textarea"
                                       :data-type  "text"}
                     :origin          {:label      "Origin"
                                       :field-type "radio"
                                       :data-type  "text"
                                       :options    ["All" "Vegetarian"]}}
    })

(def columns
 [:gname
  :calories
  :fats
; :proteins
  :carbonhydrates
  :water
; :description
; :origin
  ])

(def header-and-cell-styles
 [{:content    "Name"
   :column     {"text-align" "left"}}
  {:content    "Calories"
   :column     {"text-align" "right"}}
  {:content    "Fats"
   :column     {"text-align" "right"}}
; {:content    "Proteins"}
  {:content    "Carbonhydrates"
   :header     {"width"      "50px"
                "text-align" "right"}
   :column     {"text-align" "right"}}
  {:content    "Water"
   :column     {"text-align" "right"}}
; {:content    "Description"
;  :header     {"width" "50px"}
;  :column     {"width" "50px"}}
; {:content "Origin"}
  ])

(def table-grocery-conf
     {:entity-type "grocery"
      :query       {}
      :projection  columns
      :qsort       [{:column :gname :direction :asc}
                    {:column :calories :direction :desc}]})

