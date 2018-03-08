(ns personal-organiser-client.login.html
 (:require [personal-organiser-client.generate-html :refer [gen crt]]
           [personal-organiser-client.ajax :refer [ajax get-response]]))

(defn form
 ""
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
 ""
 [gnav
  mnav
  logout]
 (gen
  (crt "nav"
       [(crt "a"
             "Home"
             {:id "aHomeId"})
        (crt "a"
             "Grocery"
             {:id "aGroceryId"}
             {:onclick {:evt-fn gnav}})
        (crt "a"
             "Meal"
             {:id "aMealId"}
             {:onclick {:evt-fn mnav}})
        (crt "a"
             "Plan ishrane"
             {:id "aPlanishraneId"})
        (crt "a"
             "Organism"
             {:id "aOrganismId"})
        (crt "a"
             "Log out"
             {:id "aLogoutId"}
             {:onclick {:evt-fn logout}})
        ]))
 )

(defn template
 ""
 []
 (gen
  [(crt "div"
        ""
        {:class "header"})
   (crt "div"
        ""
        {:class "sidebar-menu"})
   (crt "div"
        ""
        {:class "content"})
   (crt "div"
        ""
        {:class "footer"})])
 )

(defn footer
 ""
 []
 (gen
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
        {:class "made-by"})])
 )

