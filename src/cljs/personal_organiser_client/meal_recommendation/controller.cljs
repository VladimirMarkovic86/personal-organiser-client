(ns personal-organiser-client.meal-recommendation.controller
  (:require [ajax-lib.core :refer [sjax get-response]]
            [common-middle.request-urls :as rurls]
            [js-lib.core :as md]
            [utils-lib.core :as utils]
            [personal-organiser-middle.request-urls :as prurls]
            [personal-organiser-middle.collection-names :refer [organism-cname]]))

(defn get-organisms
  "Get all organisms"
  []
  (let [xhr (sjax
              {:url rurls/get-entities-url
               :entity {:entity-type organism-cname
                        :entity-filter {}
                        :projection [:_id :first-name :last-name]
                        :projection-include true
                        :pagination false}})
        response (get-response
                   xhr)
        organisms (:data response)]
    organisms))

(defn calculate-meal-recommendations
  "Calculate meal recommendations"
  [display-fn]
  (let [select-organism-el (md/query-selector-on-element
                             ".meal-recommendation"
                             "#meal-recommendation-organisms")
        selected-organism-opt (aget
                                (.-selectedOptions
                                  select-organism-el)
                                0)
        selected-organism-opt-value (.-value
                                      selected-organism-opt)
        breakfast-calories-share-el (md/query-selector-on-element
                                      ".meal-recommendation"
                                      "#breakfast-calories-share")
        breakfast-calories-share-value (.-valueAsNumber
                                         breakfast-calories-share-el)
        breakfast-calories-share-value (if (utils/is-number?
                                             breakfast-calories-share-value)
                                         breakfast-calories-share-value
                                         (do
                                           (aset
                                             breakfast-calories-share-el
                                             "valueAsNumber"
                                             35)
                                           35))
        breakfast-calories-share (/ breakfast-calories-share-value
                                    100)
        lunch-calories-share-el (md/query-selector-on-element
                                  ".meal-recommendation"
                                  "#lunch-calories-share")
        lunch-calories-share-value (.-valueAsNumber
                                     lunch-calories-share-el)
        lunch-calories-share-value (if (utils/is-number?
                                         lunch-calories-share-value)
                                     lunch-calories-share-value
                                     (do
                                       (aset
                                         lunch-calories-share-el
                                         "valueAsNumber"
                                         40)
                                       40))
        lunch-calories-share (/ lunch-calories-share-value
                                100)
        dinner-calories-share-el (md/query-selector-on-element
                                   ".meal-recommendation"
                                   "#dinner-calories-share")
        dinner-calories-share-value (.-valueAsNumber
                                     dinner-calories-share-el)
        dinner-calories-share-value (if (utils/is-number?
                                          dinner-calories-share-value)
                                      dinner-calories-share-value
                                      (do
                                        (aset
                                          dinner-calories-share-el
                                          "valueAsNumber"
                                          25)
                                        25))
        dinner-calories-share (/ dinner-calories-share-value
                                 100)]
    (when (not= selected-organism-opt-value
                "-1")
      (let [xhr (sjax
                  {:url prurls/calculate-meal-recommendations-url
                   :entity {:organism-id selected-organism-opt-value
                            :breakfast-calories-share breakfast-calories-share
                            :lunch-calories-share lunch-calories-share
                            :dinner-calories-share dinner-calories-share}})
            response (get-response
                       xhr)
            meal-recommendations (:data response)]
        (display-fn
          meal-recommendations))
     ))
 )

