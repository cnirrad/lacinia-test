(ns test-app.routes.services
  (:require [ring.util.http-response :refer :all]
            [compojure.api.sweet :refer :all]
            [schema.core :as s]
            [test-app.middleware :refer [token]]
            [test-app.schema :refer [graphql-handler]]
            [compojure.api.meta :refer [restructure-param]]
            [buddy.auth.accessrules :refer [restrict]]
            [buddy.auth :refer [authenticated?]]))

(defn access-error [_ _]
  (unauthorized {:error "unauthorized"}))

(defn wrap-restricted [handler rule]
  (restrict handler {:handler  rule
                     :on-error access-error}))

(defmethod restructure-param :auth-rules
  [_ rule acc]
  (update-in acc [:middleware] conj [wrap-restricted rule]))

(defmethod restructure-param :current-user
  [_ binding acc]
  (update-in acc [:letks] into [binding `(:identity ~'+compojure-api-request+)]))


(defn execute-query [q v]
  (graphql-handler q v))

(def GraphQLRequest
  {(s/required-key "query") s/Str
   (s/optional-key "variables") {s/Str s/Str}})

(defapi service-routes
  {:swagger {:ui "/swagger-ui"
             :spec "/swagger.json"
             :data {:info {:version "1.0.0"
                           :title "Sample API"
                           :description "Sample Services"}}}}

  (POST "/login" []
    :return String
    :form-params [username :- String, password :- String]
    :summary "Login using a username and password."
    (ok (token username password)))
  (GET "/authenticated" []
       :auth-rules authenticated?
       :current-user user
       (ok {:user user}))
  (context "/api" []
    :tags ["graphql"]

    (GET "/query" []
         :query-params [query, vars]
         (let [result (execute-query query vars)]
           (if (-> result :errors seq)
             (bad-request result)
             (ok result))))

    (POST "/query" []
          :body-params [query, variables]
        (let [result (execute-query query variables)]
         (if (-> result :errors seq)
           (bad-request result)
           (ok result))))

    (GET "/plus" []
      :return       Long
      :query-params [x :- Long, {y :- Long 1}]
      :summary      "x+y with query-parameters. y defaults to 1."
      (ok (+ x y)))

    (POST "/minus" []
      :return      Long
      :body-params [x :- Long, y :- Long]
      :summary     "x-y with body-parameters."
      (ok (- x y)))

    (GET "/times/:x/:y" []
      :return      Long
      :path-params [x :- Long, y :- Long]
      :summary     "x*y with path-parameters"
      (ok (* x y)))

    (POST "/divide" []
      :return      Double
      :form-params [x :- Long, y :- Long]
      :summary     "x/y with form-parameters"
      (ok (/ x y)))

    (GET "/power" []
      :return      Long
      :header-params [x :- Long, y :- Long]
      :summary     "x^y with header-parameters"
      (ok (long (Math/pow x y))))))
