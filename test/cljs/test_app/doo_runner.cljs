(ns test-app.doo-runner
  (:require [doo.runner :refer-macros [doo-tests]]
            [test-app.core-test]))

(doo-tests 'test-app.core-test)

