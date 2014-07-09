(ns omodoro.components.pomodorometer
  (:require 
   [om.core :as om :include-macros true]
   [om.dom :as dom :include-macros true]))

(defn empty-circle []
  (dom/i #js {:className "fa fa-circle-thin"}))
(defn half-circle []
  (dom/i #js {:className "fa fa-dot-circle-o"}))
(defn filled-circle []
  (dom/i #js {:className "fa fa-circle"}))



(defn pomodoro-meter [{:keys [clock day]} owner opts]
  (reify
    om/IRender
    (render [_]
      (let [poms-committed-to (:commitment day)
            num-completed-poms (:completed day)
            pom-in-progress? (and
                              (= :pom (:pom-or-break clock))
                              (some #{:ticking :paused} (list (:current-timer-state clock))))
            num-incomplete-poms (- poms-committed-to num-completed-poms (when pom-in-progress? 1))]
        (dom/div nil
          (apply dom/div
                 #js {:className "pomodoroMeter"}
                 (concat  (repeatedly num-completed-poms  filled-circle)
                          (when pom-in-progress? (list (half-circle)))
                          (repeatedly num-incomplete-poms empty-circle))))))))

