(ns personal-organiser-client.organism.entity
  (:require [framework-lib.core :refer [gen-table]]
            [language-lib.core :refer [get-label]]
            [common-client.allowed-actions.controller :refer [allowed-actions]]
            [personal-organiser-middle.collection-names :refer [organism-cname]]
            [personal-organiser-middle.grocery.entity :as pomge]
            [personal-organiser-middle.organism.entity :as pomoe]))

(def entity-type
     organism-cname)

(defn gender-labels
  "Returns gender property labels"
  []
  [[(get-label 1050)
    pomoe/gender-male]
   [(get-label 1051)
    pomoe/gender-female]])

(defn diet-labels
  "Returns diet property labels"
  []
  [[(get-label 1044)
    pomge/diet-vegetarian]
   [(get-label 1042)
    pomge/diet-not-vegetarian]])

(defn activity-labels
  "Returns activity property labels"
  []
  [[(get-label 1045)
    pomoe/activity-mainly-sitting]
   [(get-label 1046)
    pomoe/activity-easy-physical-labor]
   [(get-label 1047)
    pomoe/activity-medium-physical-labor]
   [(get-label 1048)
    pomoe/activity-hard-physical-labor]
   [(get-label 1049)
    pomoe/activity-very-hard-physical-labor]])

(defn form-conf-fn
  "Form configuration for organism entity"
  []
  {:id :_id
   :type entity-type
   :entity-name (get-label 1026)
   :fields {:first-name {:label (get-label 1001)
                         :input-el "text"
                         :attrs {:placeholder (get-label 1001)
                                 :title (get-label 1001)
                                 :required true}}
            :last-name {:label (get-label 1002)
                        :input-el "text"
                        :attrs {:placeholder (get-label 1002)
                                :title (get-label 1002)
                                :required true}}
            :email {:label (get-label 14)
                    :input-el "email"
                    :attrs {:placeholder (get-label 14)
                            :title (get-label 14)
                            :required true}}
            :height {:label (get-label 1003)
                     :input-el "number"
                     :attrs {:step "0.1"
                             :placeholder (get-label 1003)
                             :title (get-label 1003)
                             :required true}}
            :weight {:label (get-label 1004)
                     :input-el "number"
                     :attrs {:step "0.1"
                             :placeholder (get-label 1004)
                             :title (get-label 1004)
                             :required true}}
            :birthday {:label (get-label 1005)
                       :input-el "date"
                       :attrs {:placeholder (get-label 1005)
                               :title (get-label 1005)
                               :required true}}
            :gender {:label (get-label 1006)
                     :input-el "radio"
                     :attrs {:required true}
                     :options (gender-labels)}
            :diet {:label (get-label 1007)
                   :input-el "radio"
                   :attrs {:required true}
                   :options (diet-labels)}
            :activity {:label (get-label 1008)
                       :input-el "select"
                       :attrs {:required true}
                       :options (activity-labels)}}
   :fields-order [:first-name
                  :last-name
                  :email
                  :birthday
                  :height
                  :weight
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
                :diet
                :activity
                ]
   :style
    {:first-name
      {:content (get-label 1001)
       :th {:style {:width "8%"}}
       :td {:style {:width "8%"
                    :text-align "left"}}
       }
     :last-name
      {:content (get-label 1002)
       :th {:style {:width "8%"}}
       :td {:style {:width "8%"
                    :text-align "left"}}
       }
     :email
      {:content (get-label 14)
       :th {:style {:width "10%"}}
       :td {:style {:width "10%"
                    :text-align "left"}}
       }
     :height
      {:content (get-label 1003)
       :th {:style {:width "6%"}}
       :td {:style {:width "6%"
                    :text-align "right"}}
       }
     :weight
      {:content (get-label 1004)
       :th {:style {:width "6%"}}
       :td {:style {:width "6%"
                    :text-align "right"}}
       }
     :birthday
      {:content (get-label 1005)
       :th {:style {:width "8%"}}
       :td {:style {:width "8%"}}
       }
     :gender
      {:content (get-label 1006)
       :th {:style {:width "8%"}}
       :td {:style {:width "8%"}}
       :labels (into
                 #{}
                 (gender-labels))
       }
     :diet
      {:content (get-label 1007)
       :th {:style {:width "8%"}}
       :td {:style {:width "8%"}}
       :labels (into
                 #{}
                 (diet-labels))
       }
     :activity
      {:content (get-label 1008)
       :th {:style {:width "8%"}}
       :td {:style {:width "8%"}}
       :labels (into
                 #{}
                 (activity-labels))}}
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
   :rows 10
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

