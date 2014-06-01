(ns omodoro.components.pomodorometer
  (:require 
   [om.core :as om :include-macros true]
   [om.dom :as dom :include-macros true]))

(defn empty-circle []
  (dom/i #js {:className "fa fa-circle-thin fa-3x"}))
(defn half-circle []
  (dom/i #js {:className "fa fa-dot-circle-o fa-3x"}))
(defn filled-circle []
  (dom/i #js {:className "fa fa-circle fa-3x"}))



(defn pomodoro-meter [app owner opts]
  (reify
    om/IRender
    (render [_]
      (let [num-completed-poms (:completed app)
            num-incomplete-poms (- (:commitment app) (:completed app))]
        (dom/div nil
                 (apply dom/div
                        #js {:className "pomodoroMeter"}
                        (concat  (repeatedly num-completed-poms  filled-circle)
                                 (list (half-circle))
                                 (repeatedly num-incomplete-poms empty-circle)
                                 )))))))

