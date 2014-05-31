(ns omodoro.core
  (:require [cljs.nodejs :as node]
            [om.core :as om :include-macros true]
            [om.dom :as dom :include-macros true]
            [cljs.nodejs :as n]
            [omodoro.components.dailycommit :as dc]))

(enable-console-print!)

(def gui (n/require "nw.gui"))  ;; kernel_time.core.gui.Window.get().zoomLevel = 2
(def state (atom {:seconds (* 25 60)
                  :commitment 4}))

(defn tick! [data]
  (let [tick-once! (fn []
                     (when-not (zero? (:seconds @data))
                       (om/transact! data :seconds dec)))]
    (js/setInterval tick-once! 1000)))

(defn pad [n]
  (if (<= 0 n 9)
    (str "0" n)
    (str n)))

(defn clock [seconds]
  (let [minutes (int (/ seconds 60))
        seconds (rem seconds 60)]
    (dom/p #js {:id "clock"}
           (str (pad minutes) ":" (pad seconds)))))


(om/root
 (fn [app-state owner]
   (reify
     om/IInitState
     (init-state [this]
       {:interval nil})
     om/IDidMount
     (did-mount [this]
       (om/set-state! owner :interval (tick! app-state)))
     om/IRenderState
     (render-state [this {:keys [interval]}]
       (dom/div nil
         (dom/button #js {:onClick
                          (fn [e]
                            (js/clearInterval interval)
                            (om/update! app-state :seconds (* 25 60))
                            (om/set-state! owner :interval (tick! app-state)))}
                     "reset")
         (clock (:seconds app-state))
         (om/build dc/commit-widget app-state)))))
 state
 {:target (. js/document (getElementById "root"))})

