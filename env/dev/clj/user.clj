(ns user
  (:require [luminus-migrations.core :as migrations]
            [test-app.config :refer [env]]
            [mount.core :as mount]
            [test-app.figwheel :refer [start-fw stop-fw cljs]]
            test-app.core))

(defn start []
  (mount/start-without #'test-app.core/repl-server))

(defn stop []
  (mount/stop-except #'test-app.core/repl-server))

(defn restart []
  (stop)
  (start))

(defn migrate []
  (migrations/migrate ["migrate"] (select-keys env [:database-url])))

(defn rollback []
  (migrations/migrate ["rollback"] (select-keys env [:database-url])))


