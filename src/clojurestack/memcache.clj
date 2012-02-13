(ns clojurestack.memcache 
  (:refer-clojure :exclude [extend])
  (:import (net.spy.memcached MemcachedClient BinaryConnectionFactory AddrUtil)
           (java.net InetSocketAddress)
	   (java.util.concurrent TimeUnit)
           (java.util UUID))
  (:use [clj-time.core]
        [clj-time.format]
        [clojure.tools.logging :only (info error debug)])
)

(System/setProperty "net.spy.log.LoggerImpl" "net.spy.log.slf4j.Slf4jLogger")

(def ^{:dynamic true} *db* {:connection nil :level 0})
(defn get-connection [mchosts] (MemcachedClient. (BinaryConnectionFactory.) (AddrUtil/getAddresses mchosts)))
(defn gen-timestamp [] (clj-time.format/unparse (formatters :date-hour-minute-second) (now)))

(defn find-connection
  "Returns the current database connection (or nil if there is none)"
  [] (:connection *db*))

(defmacro with-mc
  "Macro to manage the Memcached Session"
  [spec & body]
  `( with-mc* ~spec (fn [] ~@body)))

(defn with-mc*
    "Manages the memcache connection"
    [mchosts func]
    (let [^MemcachedClient con (get-connection mchosts)]
      (debug "set up new connection")
      (Thread/sleep 5) ; fix to workaround bug #...
      (binding [*db* (assoc *db* :connection con :level 0)]
      (try (func) (finally (.shutdown con 5 TimeUnit/SECONDS))))))

(defn cget
  "Retrieves an item from a specific queue.
   If the item does not exist, nil is returned"
  [qname]
  (try (read-string(.get (find-connection) (str qname)))
   (catch NullPointerException e nil)))

(defn cset
  "Adds an item to a specific queue"
  [ queue-name data & {:keys [timeout] :or {timeout 0}}]
  (.set (find-connection) queue-name timeout (str (binding [*print-dup* true] (prn-str data)))))

(defn cdelete
  "removes item"
  [qname]
  (.delete (find-connection) qname))

(defn creplace 
  "replaces item" 
  [key value & {:keys [timeout] :or {timeout 0}}] 
  (let [replaceFuture (.replace (find-connection) key timeout (binding [*print-dup* true] (prn-str value)))] (if (= false (.get replaceFuture)) (cset key value) replaceFuture)))

;  (if false (.get(.replace (find-connection) key timeout (binding [*print-dup* true] (prn-str value))) (set-item key value) true))

(defn cstats
  "gets the stats for the queues"
  []

  (let [stats (.getStats (find-connection))]
    (into '{} (for [[k v] stats] [k (into '{} v)]))))

; Now add connection management functinality
(defn uuid []  (.toString (UUID/randomUUID)) )

(defn cadd
  "Adds an item to a collection, if the collection does not exist create and append. 
  The collections ('buckets') are managed with a root node and leafs for the entries.
  Leaf and root nodes does not have any kind of timeout."
  [bucket value]
  (let [leafkey (str bucket "_" (uuid)) node (cset leafkey value)] (if-let [current (cget bucket)] (creplace bucket (conj current leafkey)) (cset bucket [leafkey])))
)


