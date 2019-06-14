(ns personal-organiser-client.preferences.html
  (:require [htmlcss-lib.core :refer [div label]]
            [language-lib.core :refer [get-label]]
            [common-client.preferences.html :as ccph]
            [personal-organiser-middle.grocery.entity :as pomge]
            [personal-organiser-middle.meal.entity :as pomme]
            [personal-organiser-middle.organism.entity :as pomoe]))

(defn build-specific-display-tab-content-fn
  "Builds specific display tab content"
  []
  [(div
     [(label
        (get-label
          1267))
      (div
        (ccph/generate-column-number-dropdown-options
          @pomge/card-columns-a))
      (div
        (ccph/generate-row-number-dropdown-options
          @pomge/table-rows-a))
      ]
     {:class "parameter"
      :parameter-name "grocery-entity"})
   (div
     [(label
        (get-label
          1268))
      (div
        (ccph/generate-column-number-dropdown-options
          @pomme/card-columns-a))
      (div
        (ccph/generate-row-number-dropdown-options
          @pomme/table-rows-a))
      ]
     {:class "parameter"
      :parameter-name "meal-entity"})
   (div
     [(label
        (get-label
          1269))
      (div
        (ccph/generate-column-number-dropdown-options
          @pomoe/card-columns-a))
      (div
        (ccph/generate-row-number-dropdown-options
          @pomoe/table-rows-a))
      ]
     {:class "parameter"
      :parameter-name "organism-entity"})
   ])

