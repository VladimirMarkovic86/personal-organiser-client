(ns personal-organiser-client.organism.entity
  (:require [framework-lib.core :refer [gen-table]]))

(def entity-type
     "organism")

(def form-conf
     {:id :_id
      :type entity-type
      :fields {:first-name {:label "First name"
                            :field-type "input"
                            :data-type "text"}
               :last-name {:label "Last name"
                           :field-type "input"
                           :data-type "text"}
               :email {:label "Email"
                       :field-type "input"
                       :data-type "text"}
               :height {:label "Height"
                        :field-type "input"
                        :data-type "number"
                        :step "0.1"}
               :weight {:label "Weight"
                        :field-type "input"
                        :data-type "number"
                        :step "0.1"}
               :birthday {:label "Birthday"
                          :field-type "input"
                          :data-type "date"}
               :gender {:label "Gender"
                        :field-type "radio"
                        :data-type "text"
                        :options ["Male" "Female"]}
               :diet {:label "Diet"
                      :field-type "radio"
                      :data-type "text"
                      :options ["All" "Vegetarian"]}
               :activity {:label "Activity"
                          :field-type "radio"
                          :data-type "text"
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
      :actions #{:details :edit :delete}
      :render-in ".content"
      :table-class "entities"
      :table-fn gen-table})

