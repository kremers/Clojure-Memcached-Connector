(ns clojurestack.domain)
(defrecord Todo [task open created])
(defn todo [task open created] (Todo. task open created))

