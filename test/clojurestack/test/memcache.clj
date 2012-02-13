(ns clojurestack.test.memcache
    (:use [clojurestack.core] [clojure.test] [clojure.tools.logging :only (info error)])
    (:require [clojurestack.memcache :as test]))

(def testdb "127.0.0.1:11211")

(deftest cset
  (test/with-mc testdb
    (test/cset "set-item-test" "example")
    (is (= (test/cget "set-item-test") "example"))  
  ))

(deftest cget
  (test/with-mc testdb
    (is (= nil (test/cget "notAvailable")))
    ))


(deftest cdelete
  (test/with-mc testdb
    (cset)
    (test/cdelete "set-item-test")
    (is (= nil (test/cget "set-item-test")))
  ))

(deftest creplace
  (test/with-mc testdb
    (cset)
    (test/creplace "set-item-test" "xyz")
    (is (= (test/cget "set-item-test") "xyz"))))

; Todo - nil handling
