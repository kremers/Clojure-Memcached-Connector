(ns clojurestack.domain (:gen-class))
(defrecord Todo [task open created])
(defn todo [task open created] (Todo. task open created))

