(ns personal-organiser-client.organism.entity
  (:require [framework-lib.core :refer [gen-table]]
            [language-lib.core :refer [get-label]]
            [common-client.allowed-actions.controller :refer [allowed-actions]]
            [personal-organiser-middle.collection-names :refer [organism-cname]]))

(def entity-type
     organism-cname)

(defn form-conf-fn
  "Form configuration for organism entity"
  []
  {:id :_id
   :type entity-type
   :entity-name (get-label 1026)
   :fields {:first-name {:label (get-label 1001)
                         :input-el "text"
                         :attrs {:required "required"}}
            :last-name {:label (get-label 1002)
                        :input-el "text"
                        :attrs {:required "required"}}
            :email {:label (get-label 14)
                    :input-el "email"
                    :attrs {:required "required"}}
            :height {:label (get-label 1003)
                     :input-el "number"
                     :attrs {:step "0.1"
                             :required "required"}}
            :weight {:label (get-label 1004)
                     :input-el "number"
                     :attrs {:step "0.1"
                             :required "required"}}
            :birthday {:label (get-label 1005)
                       :input-el "date"
                       :attrs {:required "required"}}
            :gender {:label (get-label 1006)
                     :input-el "radio"
                     :attrs {:required "required"}
                     :options [[(get-label 1050)
                                "male"]
                               [(get-label 1051)
                                "female"]]}
            :diet {:label (get-label 1007)
                   :input-el "radio"
                   :attrs {:required "required"}
                   :options [[(get-label 1042)
                              "all"]
                             [(get-label 1044)
                              "vegetarian"]]}
            :activity {:label (get-label 1008)
                       :input-el "radio"
                       :attrs {:required "required"}
                       :options [[(get-label 1045)
                                  "mainly_sitting"]
                                 [(get-label 1046)
                                  "easy_physical_labor"]
                                 [(get-label 1047)
                                  "medium_physical_labor"]
                                 [(get-label 1048)
                                  "hard_physical_labor"]
                                 [(get-label 1049)
                                  "very_hard_physical_labor"]]}}
   :fields-order [:first-name
                  :last-name
                  :email
                  :height
                  :weight
                  :birthday
                  :gender
                  :diet
                  :activity]})

(defn columns-fn
  "Table columns for organism entity"
  []
  {:projection [:first-name
                :last-name
                ;:email
                :height
                :weight
                :birthday
                :gender
                ;:diet
                ;:activity
                ]
   :style
    {:first-name
      {:content (get-label 1001)
       :th {:style {:width "100px"}}
       :td {:style {:width "100px"
                    :text-align "left"}}
       }
     :last-name
      {:content (get-label 1002)
       :th {:style {:width "100px"}}
       :td {:style {:width "100px"
                    :text-align "left"}}
       }
     :email
      {:content (get-label 14)
       :th {:style {:width "100px"}}
       :td {:style {:width "100px"
                    :text-align "left"}}
       }
     :height
      {:content (get-label 1003)
       :th {:style {:width "40px"}}
       :td {:style {:width "40px"
                    :text-align "right"}}
       }
     :weight
      {:content (get-label 1004)
       :th {:style {:width "40px"}}
       :td {:style {:width "40px"
                    :text-align "right"}}
       }
     :birthday
      {:content (get-label 1005)
       :th {:style {:width "40px"}}
       :td {:style {:width "40px"}}
       }
     :gender
      {:content (get-label 1006)
       :th {:style {:width "40px"}}
       :td {:style {:width "40px"}}
       }
     :diet
      {:content (get-label 1007)
       :th {:style {:width "40px"}}
       :td {:style {:width "40px"}}
       }
     :activity
      {:content (get-label 1008)
       :th {:style {:width "40px"}}
       :td {:style {:width "40px"}}
       }}
    })

(defn query-fn
  "Table query for organism entity"
  []
  {:entity-type entity-type
   :entity-filter {}
   :projection (:projection (columns-fn))
   :projection-include true
   :qsort {:first-name 1}
   :pagination true
   :current-page 0
   :rows 25
   :collation {:locale "sr"}})

(defn table-conf-fn
  "Table configuration for organism entity"
  []
  {:query (query-fn)
   :columns (columns-fn)
   :form-conf (form-conf-fn)
   :actions [:details :edit :delete]
   :allowed-actions @allowed-actions
   :search-on true
   :search-fields [:first-name
                   :last-name
                   :email
                   :gender
                   :diet
                   :activity]
   :render-in ".content"
   :table-class "entities"
   :table-fn gen-table})

