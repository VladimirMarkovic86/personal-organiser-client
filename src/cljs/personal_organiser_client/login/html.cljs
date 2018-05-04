(ns personal-organiser-client.login.html
 (:require [personal-organiser-client.generate-html :refer [gen crt]]
           [personal-organiser-client.ajax :refer [ajax get-response]]
           [personal-organiser-client.grocery.controller :as gc :refer [nav-link]]
           [personal-organiser-client.meal.controller :as mc :refer [nav-link]]
           [personal-organiser-client.organism.controller :as oc :refer [nav-link]]))

(defn form
 "Generate table HTML element that contains login form"
 [login-evt]
 (gen
  (crt "table"
       [(crt "tr"
             [(crt "td"
               (crt "label"
                    "email"
                    {:for "txtEmailId"}))
              (crt "td"
                   (crt "input"
                        ""
                        {:id "txtEmailId"
                         :name "txtEmailN"
                         :type "text"
                         :required "required"}))]
         )
        (crt "tr"
             [(crt "td"
                   (crt "label"
                        "password"
                        {:for "pswLoginId"}))
              (crt "td"
                   (crt "input"
                        ""
                        {:id "pswLoginId"
                         :name "pswLoginN"
                         :type "password"
                         :required "required"}))]
         )
        (crt "tr"
             [(crt "td")
              (crt "td"
                   (crt "input"
                        ""
                        {:id "btnLoginId"
                         :name "btnLoginN"
                         :type "button"
                         :value "Login"}
                        login-evt))]
         )
        (crt "tr"
             [(crt "td")
              (crt "td"
                   [(crt "div"
                         (crt "a"
                              "forgot password?"
                              {:id "forgot-password"}))
                    (crt "div"
                         (crt "a"
                              "register"
                              {:id "register"}))]
                   )]
         )
        (crt "tr"
             (crt "td"
                  ""
                  {:id "error-msgs"
                   :colspan "2"}
                  )
         )]
   {:class "login"}))
 )

(defn nav
 "Header navigation menu"
 [logout-fn]
 (crt "nav"
      [(crt "a"
            "Home"
            {:id "aHomeId"})
       (crt "a"
            "Grocery"
            {:id "aGroceryId"}
            {:onclick {:evt-fn gc/nav-link}})
       (crt "a"
            "Meal"
            {:id "aMealId"}
            {:onclick {:evt-fn mc/nav-link}})
       (crt "a"
            "Plan ishrane"
            {:id "aPlanishraneId"})
       (crt "a"
            "Organism"
            {:id "aOrganismId"}
            {:onclick {:evt-fn oc/nav-link}})
       (crt "a"
            "Log out"
            {:id "aLogoutId"}
            {:onclick {:evt-fn logout-fn}})
       ])
 )

(defn footer
 "Footer of main page"
 []
 [(crt "div"
       ""
       {:class "clj img"})
  (crt "div"
       ""
       {:class "lein img"})
  (crt "div"
       ""
       {:class "drools img"})
  (crt "div"
       [(crt "div"
             "made by Vladimir Marković")
        (crt "div"
             "email: markovic.vladimir86@gmail.com")]
       {:class "made-by"})]
 )

(defn template
 "Template of main page"
 [logout-fn]
 (gen
  [(crt "div"
        (nav logout-fn)
        {:class "header"})
   (crt "div"
        ""
        {:class "sidebar-menu"})
   (crt "div"
        ""
        {:class "content"})
   (crt "div"
        (footer)
        {:class "footer"})])
 )



