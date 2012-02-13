(ns clojurestack.test.memcache
    (:use [clojurestack.core] [clojure.test] [clojure.tools.logging :only (info error)])
    (:require [clojurestack.memcache :as test]))

(def testdb "127.0.0.1:11211")

(deftest set-item
  (test/with-mc testdb
    (test/set-item "set-item-test" "example")
    (is (= (test/get-item "set-item-test") "example"))  
  ))

(deftest get-item
  (test/with-mc testdb
    (is (= nil (test/get-item "notAvailable")))
    ))


(deftest delete-item
  (test/with-mc testdb
    (set-item)
    (test/delete-item "set-item-test")
    (is (= nil (test/get-item "set-item-test")))
  ))

(deftest replace-item
  (test/with-mc testdb
    (set-item)
    (test/replace-item "set-item-test" "xyz")
    (is (= (test/get-item "set-item-test") "xyz"))))

; Todo - nil handling
