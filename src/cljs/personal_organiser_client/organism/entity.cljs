(ns personal-organiser-client.organism.entity
  (:require [framework-lib.core :refer [gen-table]]))

(def entity-type
     "organism")

(def form-conf
     {:id :_id
      :type entity-type
      :fields {:first-name {:label "First name"
                            :input-el "text"}
               :last-name {:label "Last name"
                           :input-el "text"}
               :email {:label "Email"
                       :input-el "text"}
               :height {:label "Height"
                        :input-el "number"
                        :attrs {:step "0.1"}}
               :weight {:label "Weight"
                        :input-el "number"
                        :attrs {:step "0.1"}}
               :birthday {:label "Birthday"
                          :input-el "date"}
               :gender {:label "Gender"
                        :input-el "radio"
                        :options ["Male" "Female"]}
               :diet {:label "Diet"
                      :input-el "radio"
                      :options ["All" "Vegetarian"]}
               :activity {:label "Activity"
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
         {:content "First name"
          :th {:style {:width "100px"}}
          :td {:style {:width "100px"
                       :text-align "left"}}
          }
        :last-name
         {:content "Last name"
          :th {:style {:width "100px"}}
          :td {:style {:width "100px"
                       :text-align "left"}}
          }
        :email
         {:content "e-mail"
          :th {:style {:width "100px"}}
          :td {:style {:width "100px"
                       :text-align "left"}}
          }
        :height
         {:content "Height"
          :th {:style {:width "40px"}}
          :td {:style {:width "40px"
                       :text-align "right"}}
          }
        :weight
         {:content "Weight"
          :th {:style {:width "40px"}}
          :td {:style {:width "40px"
                       :text-align "right"}}
          }
        :birthday
         {:content "Birthday"
          :th {:style {:width "40px"}}
          :td {:style {:width "40px"}}
          }
        :gender
         {:content "Gender"
          :th {:style {:width "40px"}}
          :td {:style {:width "40px"}}
          }
        :diet
         {:content "Diet"
          :th {:style {:width "40px"}}
          :td {:style {:width "40px"}}
          }
        :activity
         {:content "Activity"
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

