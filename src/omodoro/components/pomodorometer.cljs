(ns omodoro.components.pomodorometer
  (:require 
   [om.core :as om :include-macros true]
   [om.dom :as dom :include-macros true]))

(defn empty-circle []
  (dom/i #js {:className "fa fa-circle-thin fa-3x"}))
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
                        (into
                         (repeatedly num-incomplete-poms empty-circle)
                         (repeatedly num-completed-poms  filled-circle))))))))

