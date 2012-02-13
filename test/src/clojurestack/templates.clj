(ns clojurestack.templates
  (:require [net.cgrand.enlive-html :as html]
            [ring.util.codec :as c]))

(html/deftemplate index "clojurestack/templates/template1.html"
    [items]
    [:ol#todolist :li]  (html/clone-for [item items] (html/content item)) 
    ;[:p#message] (html/content (:message ctxt))
  
)
