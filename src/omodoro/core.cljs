(ns omodoro.core
  (:require-macros [cljs.core.async.macros :refer [go]])
  (:require [cljs.nodejs :as node]
            [cljs.core.async :refer [<! timeout]]
            [om.core :as om :include-macros true]
            [om.dom :as dom :include-macros true]))

(enable-console-print!)

(def state (atom {:seconds (* 25 60)}))

(defn countdown [data owner]
  (reify
    om/IRender
    (render [this]
      (let [minutes (int (/ (:seconds data) 60))
            seconds (rem (:seconds data) 60)]
        (dom/p nil (str minutes ":" seconds))))))

(om/root
 (fn [app-state owner]
   (reify
     om/IWillMount
     (will-mount [this]
       (go (loop []
             (when-not (zero? (:seconds @app-state))
               (<! (timeout 1000))
               (om/transact! app-state :seconds dec)
               (recur)))))
     om/IRender
     (render [this]
       (om/build countdown app-state))))
 state
 {:target (. js/document (getElementById "root"))})

