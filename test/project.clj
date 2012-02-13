(defproject clojurestack "1.0.0-SNAPSHOT"
  :description "this is an example for clojure, enlive, sandbar and memcached"
  :repositories {"spy" "http://files.couchbase.com/maven2/"}
  :dev-dependencies [[lein-ring "0.5.2"]]
  :dependencies [[org.clojure/clojure "1.3.0"]
		 [ring "1.0.0"] 
		 [clj-time "0.3.3"]
		 [me.shenfeng/async-ring-adapter "1.0.0"]
		 [me.shenfeng/enlive "1.2.0-SNAPSHOT"]
		 [net.cgrand/moustache "1.1.0"]
		 [spy/spymemcached "2.7.3"]
                 [org.clojure/tools.logging "0.2.3"]
                 [org.slf4j/slf4j-api "1.6.4"]
                 [cheshire "2.0.4"]
                 [ch.qos.logback/logback-classic "1.0.0"]
                  ]  
   :main clojurestack.core
   :source-path "src"
   :java-source-path "jsrc"
   :ring {:handler clojurestack.core/routes}
   :properties { :project.build.sourceEncoding "UTF-8" }
)


