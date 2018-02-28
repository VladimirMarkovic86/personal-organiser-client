(ns personal-organiser-client.grocery-entity)

(def entity-type "grocery")

(def grocery-entity
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

(def columns-b
 [:proteins
  :description])

(def header-and-cell-styles
 [{:content    "Name"
   :header     {"width"      "200px"}
   :column     {"width"      "200px"
                "text-align" "left"}}
  {:content    "Calories"
   :header     {"width"      "40px"}
   :column     {"text-align" "right"}}
  {:content    "Proteins"
   :header     {"width"      "40px"}
   :column     {"text-align" "right"}}
  {:content    "Fats"
   :header     {"width"      "40px"}
   :column     {"text-align" "right"}}
  {:content    "Carbonhydrates"
   :header     {"width"      "40px"}
   :column     {"text-align" "right"}}
; {:content    "Description"
;  :header     {"width" "50px"}
;  :column     {"width" "50px"}}
  {:content    "Origin"
   :header     {"width"      "40px"}
   :column     {"width"      "40px"}}
  ])

(def table-grocery-conf
     {:entity-type  entity-type
      :entity-filter  {}
      :projection  columns
      :projection-include  true
      :qsort  {:gname 1}
      :pagination  true
      :current-page  0
      :rows  25
      :collation {:locale "sr"}})

