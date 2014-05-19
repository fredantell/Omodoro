(ns omodoro.core
  (:require-macros [cljs.core.async.macros :refer [go]])
  (:require [cljs.nodejs :as node]
            [cljs.core.async :refer [<! timeout]]
            [om.core :as om :include-macros true]
            [om.dom :as dom :include-macros true]))

(enable-console-print!)

(defn tick [ms f]
  (go (loop [then (+ ms (.now js/Date))]
        (let [now (.now js/Date)]
          (<! (timeout (- then now)))
          (when (f)
            (recur (+ ms then)))))))

(def state (atom {:seconds (* 25 60)}))

(defn countdown [seconds]
  (let [minutes (int (/ seconds 60))
        seconds (rem seconds 60)]
    (dom/p nil (str minutes ":" seconds))))

(om/root
 (fn [app-state owner]
   (reify
     om/IWillMount
     (will-mount [this]
       (go (tick 1000 (fn []
                        (when-not (zero? (:seconds @app-state))
                          (om/transact! app-state :seconds dec))))))
     om/IRender
     (render [this]
       (countdown (:seconds app-state)))))
 state
 {:target (. js/document (getElementById "root"))})

