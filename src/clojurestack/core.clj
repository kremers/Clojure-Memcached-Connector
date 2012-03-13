(ns clojurestack.core
  (:import [java.io BufferedReader] [java.io InputStreamReader])
  (:use [clojurestack.memcache]
        [ring.middleware file file-info params reload]
        [clojure.tools.logging :only (info error)]
	[net.cgrand.moustache :only [app]]
        [ring.util.response :only [response header]]
        [ring.util.codec :as c]
        [cheshire.core]
        [ring.adapter.netty]
        [stencil.core]
        [clojurestack.domain]))

(def db-nodes "127.0.0.1:11211")
(defn make-404 [req] (response (str "404\n---\nCeci n'est pas une 404")))
(defn get-todos [] (sort-by (comp :created :value) (cgetall "todos")))

(defmacro with-req-body [[binding] req & body] `(with-open [r# (~req :body)] (let [~binding (BufferedReader. (InputStreamReader. r#))] ~@body)))
(defn save-todo [name] (let [entry (todo name true (gen-timestamp)) ] (cadd "todos" entry) entry))
(defn save-todo-from-req [req] (save-todo (:name (first (seq (with-req-body [buf] req (parsed-seq buf true)))))))  
(defn remove-todo-from-req [req] (.get (cdeleteitem "todos" (:id (first (with-req-body [buf] req (parsed-seq buf true)))))))
(defn mark-todo-in-req [req] (.get (let [id (:id (first (seq (with-req-body [buf] req (parsed-seq buf true))))) old (cget id)] (creplace id (update-in old [:open] #(not %)))))) 

; To fix issues thapy.memcached.internal.OperationFuture appear for Jetty (not Netty) with UTF-8 responses
(defn utf8response "Ring skeleton with headers." [body] {:status  200 :headers { "Content-Type" "text/html;charset=UTF-8" "Character-Encoding" "UTF-8"} :body body})

(def routes
  (app
   (wrap-file "resources")
   (wrap-file-info)
   (wrap-params) 
    [""]        (fn [req] (with-mc db-nodes (utf8response (render-file "main" (hash-map "todos" (get-todos))))))
    ["addtodo"] (fn [req] (with-mc db-nodes (utf8response (generate-string (save-todo-from-req req))))) 
    ["removetodo"] (fn [req] (with-mc db-nodes (utf8response (generate-string (remove-todo-from-req req)))))
    ["donetodo"] (fn [req] (with-mc db-nodes (utf8response (generate-string (mark-todo-in-req req))))) 
    ["edittodo"]     (fn [req] true)
    [&]	       make-404
  ) 
)

