(ns clouse.irc
  (:require [irclj.core :as irc]))

(def nick "padwatch")
(def channel "#padwatch")

(def connection (atom nil))

(defn eat-log [& args]
  (comment pprint args))

(defn disconnect! []
  (when @connection
    (swap! connection irc/kill)))

(defn connect! []
  (disconnect!)
  (reset! connection (irc/connect "irc.freenode.net"
                                  6667 nick
                                  :callbacks {:raw-log eat-log}))
  (irc/join @connection channel))

(defn message! [row-info]
  (let [useful {:title (:title row-info) ; TODO: extract helper
                :where (:where row-info)
                :style (:style row-info)
                :sqft (:sqft row-info)
                :url (:url row-info) ; TODO: tinyurl
                :walkscore (:walkscore row-info)}]
    (irc/message @connection channel (pr-str useful))))
