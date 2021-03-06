(ns test-app.schema
  (:require
   [test-app.db.core :as db]
   [clojure.data.json :as json]
   [clojure.java.io :as io]
   [clojure.edn :as edn]
   [com.walmartlabs.lacinia :refer [execute]]
   [com.walmartlabs.lacinia.schema :refer [compile]]
   [com.walmartlabs.lacinia.util :refer [attach-resolvers]]))

(defn resolve-users [context args value]
  (let [term (str "%" (:term args) "%")]
    (db/search-user {:term term})))


(defn read-schema
  []
  (-> (io/resource "schema.edn")
      slurp
      edn/read-string
      (attach-resolvers {:resolve-users resolve-users})
      compile))

(def graphql-schema (read-schema))

(defn graphql-handler [query vars]
  (let [result (execute graphql-schema query vars nil)]
    (println result)
    result))
