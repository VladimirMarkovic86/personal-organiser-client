(ns personal-organiser-client.preferences.controller
  (:require [common-client.preferences.controller :as ccpc]
            [personal-organiser-middle.grocery.entity :as pomge]
            [personal-organiser-middle.meal.entity :as pomme]
            [personal-organiser-middle.organism.entity :as pomoe]))

(defn set-specific-preferences-fn
  "Sets preferences specific for this project"
  [preferences]
  (let [specific-preferences (:specific preferences)
        {{{table-rows-g :table-rows
           card-columns-g :card-columns} :grocery-entity
          {table-rows-m :table-rows
           card-columns-m :card-columns} :meal-entity
          {table-rows-o :table-rows
           card-columns-o :card-columns} :organism-entity} :display} specific-preferences]
    (reset!
      pomge/table-rows-a
      (or table-rows-g
          10))
    (reset!
      pomge/card-columns-a
      (or card-columns-g
          0))
    (reset!
      pomme/table-rows-a
      (or table-rows-m
          10))
    (reset!
      pomme/card-columns-a
      (or card-columns-m
          0))
    (reset!
      pomoe/table-rows-a
      (or table-rows-o
          10))
    (reset!
      pomoe/card-columns-a
      (or card-columns-o
          0))
   ))

(defn gather-specific-preferences-fn
  "Gathers preferences from common project"
  []
  {:display {:grocery-entity {:table-rows @pomge/table-rows-a
                              :card-columns @pomge/card-columns-a}
             :meal-entity {:table-rows @pomme/table-rows-a
                           :card-columns @pomme/card-columns-a}
             :organism-entity {:table-rows @pomoe/table-rows-a
                               :card-columns @pomoe/card-columns-a}}
   })

(defn popup-specific-preferences-set-fn
  "Gathers specific preferences from popup and sets values in atoms"
  []
  [(ccpc/generic-preferences-set
     "grocery-entity"
     pomge/card-columns-a
     pomge/table-rows-a)
   (ccpc/generic-preferences-set
     "meal-entity"
     pomme/card-columns-a
     pomme/table-rows-a)
   (ccpc/generic-preferences-set
     "organism-entity"
     pomoe/card-columns-a
     pomoe/table-rows-a)
   ]
  )

