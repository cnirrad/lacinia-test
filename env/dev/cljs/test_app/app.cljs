(ns ^:figwheel-no-load test-app.app
  (:require [test-app.core :as core]
            [devtools.core :as devtools]))

(enable-console-print!)

(devtools/install!)

(core/init!)
