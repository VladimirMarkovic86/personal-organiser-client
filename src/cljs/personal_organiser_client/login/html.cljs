(ns personal-organiser-client.login.html
 (:require [htmlcss-lib.core :refer [gen table tr td label
                                     input div a nav]]
           [ajax-lib.core :refer [ajax get-response]]
           [personal-organiser-client.grocery.controller :as gc :refer [nav-link]]
           [personal-organiser-client.meal.controller :as mc :refer [nav-link]]
           [personal-organiser-client.organism.controller :as oc :refer [nav-link]]))

(defn form
 "Generate table HTML element that contains login form"
 [login-evt]
 (gen
  (table
   [(tr
     [(td
       (label "email"
              {:for "txtEmailId"}))
      (td
       (input ""
              {:id "txtEmailId"
               :name "txtEmailN"
               :type "text"
               :required "required"}))]
     )
    (tr
     [(td
       (label "password"
              {:for "pswLoginId"}))
      (td
       (input ""
              {:id "pswLoginId"
               :name "pswLoginN"
               :type "password"
               :required "required"}))]
     )
    (tr
      [(td)
       (td
        (input ""
               {:id "btnLoginId"
                :name "btnLoginN"
                :type "button"
                :value "Login"}
               login-evt))]
     )
    (tr
     [(td)
      (td
       [(div
         (a "forgot password?"
            {:id "forgot-password"}))
        (div
         (a "register"
            {:id "register"}))]
       )]
     )
    (tr
     (td ""
         {:id "error-msgs"
          :colspan "2"})
     )]
   {:class "login"}))
 )

(defn nav-fn
 "Header navigation menu"
 [logout-fn]
 (nav
  [(a "Home"
      {:id "aHomeId"})
   (a "Grocery"
      {:id "aGroceryId"}
      {:onclick {:evt-fn gc/nav-link}})
   (a "Meal"
      {:id "aMealId"}
      {:onclick {:evt-fn mc/nav-link}})
   (a "Plan ishrane"
      {:id "aPlanishraneId"})
   (a "Organism"
      {:id "aOrganismId"}
      {:onclick {:evt-fn oc/nav-link}})
   (a "Log out"
      {:id "aLogoutId"}
      {:onclick {:evt-fn logout-fn}})
   ])
 )

(defn footer
 "Footer of main page"
 []
 [(div ""
       {:class "clj img"})
  (div ""
       {:class "lein img"})
  (div ""
       {:class "drools img"})
  (div
   [(div "made by Vladimir Marković")
    (div "email: markovic.vladimir86@gmail.com")]
   {:class "made-by"})]
 )

(defn template
 "Template of main page"
 [logout-fn]
 (gen
  [(div (nav-fn logout-fn)
        {:class "header"})
   (div ""
        {:class "sidebar-menu"})
   (div ""
        {:class "content"})
   (div (footer)
        {:class "footer"})])
 )



