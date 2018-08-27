(ns personal-organiser-client.organism.entity
  (:require [framework-lib.core :refer [gen-table]]
            [language-lib.core :refer [get-label]]))

(def entity-type
     "organism")

(def form-conf
     {:id :_id
      :type entity-type
      :entity-name (get-label 52)
      :fields {:first-name {:label (get-label 27)
                            :input-el "text"}
               :last-name {:label (get-label 28)
                           :input-el "text"}
               :email {:label (get-label 14)
                       :input-el "text"}
               :height {:label (get-label 29)
                        :input-el "number"
                        :attrs {:step "0.1"}}
               :weight {:label (get-label 30)
                        :input-el "number"
                        :attrs {:step "0.1"}}
               :birthday {:label (get-label 31)
                          :input-el "date"}
               :gender {:label (get-label 32)
                        :input-el "radio"
                        :options ["Male" "Female"]}
               :diet {:label (get-label 33)
                      :input-el "radio"
                      :options ["All" "Vegetarian"]}
               :activity {:label (get-label 34)
                          :input-el "radio"
                          :options ["Mainly sitting"
                                    "Easy physical labor"
                                    "Medium physical labor"
                                    "Hard physical labor"
                                    "Very hard physical labor"]}}
      :fields-order [:first-name
                     :last-name
                     :email
                     :height
                     :weight
                     :birthday
                     :gender
                     :diet
                     :activity]})

(def columns
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
         {:content (get-label 27)
          :th {:style {:width "100px"}}
          :td {:style {:width "100px"
                       :text-align "left"}}
          }
        :last-name
         {:content (get-label 28)
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
         {:content (get-label 29)
          :th {:style {:width "40px"}}
          :td {:style {:width "40px"
                       :text-align "right"}}
          }
        :weight
         {:content (get-label 30)
          :th {:style {:width "40px"}}
          :td {:style {:width "40px"
                       :text-align "right"}}
          }
        :birthday
         {:content (get-label 31)
          :th {:style {:width "40px"}}
          :td {:style {:width "40px"}}
          }
        :gender
         {:content (get-label 32)
          :th {:style {:width "40px"}}
          :td {:style {:width "40px"}}
          }
        :diet
         {:content (get-label 33)
          :th {:style {:width "40px"}}
          :td {:style {:width "40px"}}
          }
        :activity
         {:content (get-label 34)
          :th {:style {:width "40px"}}
          :td {:style {:width "40px"}}
          }}
       })

(def query
     {:entity-type  entity-type
      :entity-filter  {}
      :projection  (:projection columns)
      :projection-include  true
      :qsort  {:first-name 1}
      :pagination  true
      :current-page  0
      :rows  25
      :collation {:locale "sr"}})

(def table-conf
     {:query query
      :columns columns
      :form-conf form-conf
      :actions [:details :edit :delete]
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

