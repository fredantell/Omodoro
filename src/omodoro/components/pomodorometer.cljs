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
      (let [poms-committed-to (:commitment app)
            num-completed-poms (:completed app)
            pom-in-progress? (some #{:ticking :paused} (list (:current-timer-state app)))
            num-incomplete-poms (- poms-committed-to num-completed-poms (when pom-in-progress? 1))
            ]
        (dom/div nil
                 (apply dom/div
                        #js {:className "pomodoroMeter"}
                        (concat  (repeatedly num-completed-poms  filled-circle)
                                 (when pom-in-progress? (list (half-circle)))
                                 (repeatedly num-incomplete-poms empty-circle)
                                 )))))))

