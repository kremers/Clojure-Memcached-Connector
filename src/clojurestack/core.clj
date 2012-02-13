(ns clojurestack.core
  (:import [java.io BufferedReader] [java.io InputStreamReader])
  (:use [clojurestack.templates :as t] [clojurestack.memcache]
        [ring.middleware file file-info params reload]
        [clojure.tools.logging :only (info error)]
	[net.cgrand.moustache :only [app]]
        [ring.util.response :only [response header]]
        [ring.util.codec :as c]
        [cheshire.core]
        [ring.adapter.netty]))

(def db-nodes "127.0.0.1:11211")
(defrecord todo [task open created])
(defn make-404 [req] (response (str "404\n---\nCeci n'est pas une 404")))
(defn get-todos [] (with-mc db-nodes (get-item "todos")))

(defmacro with-req-body [[binding] req & body] `(with-open [r# (~req :body)] (let [~binding (BufferedReader. (InputStreamReader. r#))] ~@body)))
(defn save-todo [name] (let [todos (get-todos) entry (todo. name true (gen-timestamp)) x (conj todos entry)] (with-mc db-nodes (replace-item "todos" x)) entry))
(defn save-todo-from-req [req] (save-todo (:name (first (seq (with-req-body [buf] req (parsed-seq buf true)))))))  

; To fix issues that appear for Jetty (not Netty) with UTF-8 responses
(defn utf8response "Ring skeleton with headers." [body] {:status  200 :headers { "Content-Type" "text/html;charset=UTF-8" "Character-Encoding" "UTF-8"} :body body})

(def routes
  (app
   (wrap-file "resources")
   (wrap-file-info)
   (wrap-params) 
   (wrap-reload '[clojurestack.templates])
   [""]        (fn [req] (utf8response (t/index (map :task (get-todos)))))
   ["addtodo"] (fn [req] (utf8response (generate-string (save-todo-from-req req)))) 
   ["change"]  (fn [req] (response (t/index {:message "We changed the message!"})))
   [&]	       make-404
  ) 
)

; add response for save-todo-from-req
