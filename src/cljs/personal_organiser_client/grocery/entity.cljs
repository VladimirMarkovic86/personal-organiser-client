(ns personal-organiser-client.grocery.entity
  (:require [framework-lib.core :refer [gen-table]]
            [htmlcss-lib.core :refer [gen crt table tr th
                                      td h1 label input]]
            [language-lib.core :refer [get-label]]
            [common-client.allowed-actions.controller :refer [allowed-actions]]
            [personal-organiser-middle.collection-names :refer [grocery-cname]]))

(def entity-type
     grocery-cname)

;(def grocery-data
; (atom
;  {:_id ""
;   :gname ""
;   :calories 0.0
;   :proteins 0.0
;   :fats 0.0
;   :carbonhydrates 0.0
;   :description ""
;   :origin ""}))

;(defn- vitmin-data
; ""
; []
; {:vitaminA 0.00115})

(def popup-form
     (table
       [(tr
         [(th "Vitamins")
          (th "Daily needs")
          (th "Minerals")
          (th "Daily needs")])
        (tr
         [(td (label "A"))
          (td (input ""
                     {:id "vitaminA"
                      :value "0.00115"
                      :type "number"
                      :step "0.00001"}))
          (td (label "Calcium"))
          (td (input ""
                     {:id "calcium"
                      :value "0"
                      :type "number"
                      :step "0.00001"}))])
        (tr
         [(td (label "B"))
          (td (input ""
                     {:id "vitaminB"
                      :value "0.00115"
                      :type "number"
                      :step "0.00001"}))
          (td (label "Phosphorus"))
          (td (input ""
                     {:id "phosphorus"
                      :value "0"
                      :type "number"
                      :step "0.00001"}))])
        (tr
         [(td (label "B1"))
          (td (input ""
                     {:id "vitaminB1"
                      :value "0.0011"
                      :type "number"
                      :step "0.00001"}))
          (td (label "Manganese"))
          (td (input ""
                     {:id "manganese"
                      :value "0.001"
                      :type "number"
                      :step "0.00001"}))])
        (tr
         [(td (label "B2"))
          (td (input ""
                     {:id "vitaminB2"
                      :value "0.0011"
                      :type "number"
                      :step "0.00001"}))
          (td (label "Iron"))
          (td (input ""
                     {:id "iron"
                      :value "0.015"
                      :type "number"
                      :step "0.001"}))])
        (tr
         [(td (label "B3"))
          (td (input ""
                     {:id "vitaminB3"
                      :value "0.0011"
                      :type "number"
                      :step "0.00001"}))
          (td (label "Natrium"))
          (td (input ""
                     {:id "natrium"
                      :value "0"
                      :type "number"
                      :step "0.001"}))])
        (tr
         [(td (label "B5"))
          (td (input ""
                     {:id "vitaminB5"
                      :value "0.007"
                      :type "number"
                      :step "0.0001"}))
          (td (label "Kalium"))
          (td (input ""
                     {:id "kalium"
                      :value "0"
                      :type "number"
                      :step "0.001"}))])
        (tr
         [(td (label "B6"))
          (td (input ""
                     {:id "vitaminB6"
                      :value "0.0016"
                      :type "number"
                      :step "0.0001"}))
          (td (label "Cooper"))
          (td (input ""
                     {:id "copper"
                      :value "0.0000000005"
                      :type "number"
                      :step "0.00000000001"}))])
        (tr
         [(td (label "B12"))
          (td (input ""
                     {:id "vitaminB12"
                      :value "0.000003"
                      :type "number"
                      :step "0.0000001"}))
          (td (label "Magnesium"))
          (td (input ""
                     {:id "magnesium"
                      :value "0.35"
                      :type "number"
                      :step "0.001"}))])
        (tr
         [(td (label "Lipoic acid"))
          (td (input ""
                     {:id "lipoic_acid"
                      :value "0.000003"
                      :type "number"
                      :step "0.0000001"}))
          (td (label "Iodine"))
          (td (input ""
                     {:id "iodine"
                      :value "0.0002"
                      :type "number"
                      :step "0.00001"}))])
        (tr
         [(td (label "H"))
          (td (input ""
                     {:id "vitaminH"
                      :value "0.000065"
                      :type "number"
                      :step "0.0000001"}))
          (td (label "Chlorine"))
          (td (input ""
                     {:id "chlorine"
                      :value "0"
                      :type "number"
                      :step "0.00001"}))])
        (tr
         [(td (label "H'"))
          (td (input ""
                     {:id "vitaminH'"
                      :value "0.000065"
                      :type "number"
                      :step "0.0000001"}))
          (td (label "Sulfur"))
          (td (input ""
                     {:id "sulfur"
                      :value "0"
                      :type "number"
                      :step "0.00001"}))])
        (tr
         [(td (label "I"))
          (td (input ""
                     {:id "vitaminI"
                      :value "0.000065"
                      :type "number"
                      :step "0.0000001"}))
          (td (label "Zinc"))
          (td (input ""
                     {:id "zinc"
                      :value "0.0125"
                      :type "number"
                      :step "0.00001"}))])
        (tr
         [(td (label "C"))
          (td (input ""
                     {:id "vitaminC"
                      :value "0.1"
                      :type "number"
                      :step "0.01"}))
          (td (label "Chrome"))
          (td (input ""
                     {:id "chrome"
                      :value "0.0125"
                      :type "number"
                      :step "0.00001"}))])
        (tr
         [(td (label "D"))
          (td (input ""
                     {:id "vitaminC"
                      :value "0.0000075"
                      :type "number"
                      :step "0.0000001"}))
          (td (label "Cobalt"))
          (td (input ""
                     {:id "cobalt"
                      :value "0.00000000005"
                      :type "number"
                      :step "0.000000000001"}))])
        (tr
         [(td (label "E"))
          (td (input ""
                     {:id "vitaminE"
                      :value "0.0125"
                      :type "number"
                      :step "0.00001"}))
          (td (label "Molybdenum"))
          (td (input ""
                     {:id "Molybdenum"
                      :value "0"
                      :type "number"
                      :step "0.000000000001"}))])
        (tr
         [(td (label "F"))
          (td (input ""
                     {:id "vitaminF"
                      :value "0.0125"
                      :type "number"
                      :step "0.00001"}))
          (td)
          (td)])
        (tr
         [(td (label "J"))
          (td (input ""
                     {:id "vitaminJ"
                      :value "0.0125"
                      :type "number"
                      :step "0.00001"}))
          (td)
          (td)])
        (tr
         [(td (label "K"))
          (td (input ""
                     {:id "vitaminK"
                      :value "0.000065"
                      :type "number"
                      :step "0.0000001"}))
          (td)
          (td)])
        (tr
         [(td (label "P"))
          (td (input ""
                     {:id "vitaminP"
                      :value "0.000065"
                      :type "number"
                      :step "0.0000001"}))
          (td)
          (td)])]
      ))

(defn form-conf-fn
  "Form configuration for grocery entity"
  []
  {:id :_id
   :type entity-type
   :entity-name (get-label 1009)
   :fields {:gname {:label (get-label 1010)
                    :input-el "text"
                    :attrs {:required "required"}}
            :calories {:label (get-label 1011)
                       :input-el "number"
                       :attrs {:step "0.1"
                               :required "required"}}
            :proteins {:label (get-label 1012)
                       :input-el "number"
                       :attrs {:step "0.1"
                               :required "required"}}
            :fats {:label (get-label 1013)
                   :input-el "number"
                   :attrs {:step "0.1"
                           :required "required"}}
            :carbonhydrates {:label (get-label 1014)
                             :input-el "number"
                             :attrs {:step "0.1"
                                     :required "required"}}
            :description {:label (get-label 1015)
                          :input-el "textarea"
                          :attrs {:required "required"}}
            :origin {:label (get-label 1016)
                     :input-el "radio"
                     :attrs {:required "required"}
                     :options [[(get-label 1042)
                                "all"]
                               [(get-label 1043)
                                "vegetarian"]]}
            ;:vitmin {:label "Vitamins and Minerals"
            ;         :field-type "popup"
            ;         :popup popup-form}
            }
   :fields-order [:gname
                  :calories
                  :proteins
                  :fats
                  :carbonhydrates
                  :description
                  :origin
                  ;:vitmin
                  ]})

(defn columns-fn
  "Table columns for grocery entity"
  []
  {:projection [:gname
                :calories
                :proteins
                :fats
                :carbonhydrates
                ;:description
                :origin
                ]
   :style
    {:gname
      {:content (get-label 1010)
       :th {:style {:max-width "200px"}}
       :td {:style {:max-width "200px"
                    :text-align "left"}}}
     :calories
      {:content (get-label 1011)
       :th {:style {:max-width "40px"}
            :title (get-label 1011)}
       :td {:style {:max-width "40px"
                    :text-align "right"}}
       }
     :proteins
      {:content (get-label 1012)
       :th {:style {:max-width "40px"}
            :title (get-label 1012)}
       :td {:style {:max-width "40px"
                    :text-align "right"}}
       }
     :fats
      {:content (get-label 1013)
       :th {:style {:max-width "40px"}
            :title (get-label 1013)}
       :td {:style {:max-width "40px"
                    :text-align "right"}}
       }
     :carbonhydrates
      {:content (get-label 1014)
       :th {:style {:max-width "40px"}
            :title (get-label 1014)}
       :td {:style {:max-width "40px"
                    :text-align "right"}}
       }
     :description
      {:content (get-label 1015)
       :th {:style {:max-width "40px"}
            :title (get-label 1015)}
       :td {:style {:max-width "40px"
                    :text-align "left"}}
       }
     :origin
      {:content (get-label 1016)
       :th {:style {:max-width "40px"}
            :title (get-label 1016)}
       :td {:style {:max-width "40px"}}
       }}
    })

(defn query-fn
  "Table query for grocery entity"
  []
  {:entity-type entity-type
   :entity-filter {}
   :projection (:projection (columns-fn))
   :projection-include true
   :qsort {:gname 1}
   :pagination true
   :current-page 0
   :rows 25
   :collation {:locale "sr"}})

(defn table-conf-fn
  "Table configuration for grocery entity"
  []
  {:query (query-fn)
   :columns (columns-fn)
   :form-conf (form-conf-fn)
   :actions [:details :edit :delete]
   :allowed-actions @allowed-actions
   :search-on true
   :search-fields [:gname :description :origin]
   :render-in ".content"
   :table-class "entities"
   :table-fn gen-table})

