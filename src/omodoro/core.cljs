(ns omodoro.core
  (:require-macros [cljs.core.async.macros :refer [go]])
  (:require [cljs.nodejs :as node]
            [cljs.core.async :refer [<! timeout]]
            [om.core :as om :include-macros true]
            [om.dom :as dom :include-macros true]))

(enable-console-print!)

(def state (atom {:seconds (* 25 60)}))

(defn countdown [seconds]
  (let [minutes (int (/ seconds 60))
        seconds (rem seconds 60)]
    (dom/p nil (str minutes ":" seconds))))

(defn tick! [data]
  (letfn [(tick-once! []
            (when-not (zero? (:seconds @data))
              (om/transact! data :seconds dec)))]
    (js/setInterval tick-once! 1000)))

(om/root
 (fn [app-state owner]
   (reify
     om/IInitState
     (init-state [this]
       {:interval nil})
     om/IDidMount
     (did-mount [this]
       (om/set-state! owner :interval (tick! app-state)))
     om/IRender
     (render [this]
       (countdown (:seconds app-state)))))
 state
 {:target (. js/document (getElementById "root"))})

