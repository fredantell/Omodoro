(ns omodoro.components.clock
  (:require-macros [cljs.core.async.macros :refer [go]])
  (:require [om.core :as om :include-macros true]
            [om.dom :as dom :include-macros true]
            [cljs.core.async :refer [chan <! >! put! timeout]]))

;; Next Steps:
;; Done: Control ticking of the clock - start, pause, reset
;; Done: When clock is finished, set state to :finished, then inc completed pomodoro
;; Done: Add hover play/pause interaction
;; Add ability to one off override duration of a cycle
;; If the pomodoro is paused for more than 3 minutes then auto reset

(defn tick! [data]
  (let [tick-once! (fn []
                     (when (and
                            (= :ticking (:current-timer-state @data))
                            (< 0 (:seconds @data)))
                       (om/transact! data :seconds dec)))
        check-finished! (fn []
                          (when (and
                                 (= 0 (:seconds @data))
                                 (= :ticking (:current-timer-state @data)))
                              (do
                                (om/transact! data :completed inc)
                                (om/update! data :current-timer-state :finished))))
        reset-time! (fn []
                      (when (= :new (:current-timer-state @data))
                        (om/update! data :seconds (* 25 60))))
        
        manage-clock! (fn []
                        (do
                          #_(js/console.log "managing clock!!")
                          (tick-once!)
                          (check-finished!)
                          (reset-time!)))]
    (js/setInterval manage-clock! 1000)))

(defn pad [n]
  (if (<= 0 n 9)
    (str "0" n)
    (str n)))

;; store in app state status of clock
;; if clock is new, then show overlay with play button
;; if clock is ticking, then hide overlay with pause button
;; if clock is paused, then show overlay with play button
;; if clock is finished, then show overlay with reset button

(defn overlay [cts]
  (let [overlay-markup
        (fn [overlay-class icon-class]
          (dom/div #js {:className (str "overlay " overlay-class)}
                   (dom/i #js {:className (str "fa " icon-class)})))]
    (condp = cts
      :new (overlay-markup "show" "fa-play-circle")
      :ticking (overlay-markup "hide" "fa-pause")
      :paused (overlay-markup "show" "fa-play-circle")
      :finished (overlay-markup "show" "fa-history")
      (js/console.log "Default class: " cts))))

(defn click-clock! [cts]
  "Transition the state of the timer when the clock is clicked.
If the state on the left gets clicked, it should become the state on the right."
  (condp = cts
    :new :ticking
    :ticking :paused
    :paused :ticking
    :finished :new
    (js/console.log "Click-clock cts: " cts)))

(defn clock [{:keys [seconds current-timer-state] :as app}]
  (let [minutes (int (/ seconds 60))
        seconds (rem seconds 60)]
    (dom/div #js {:id "clock-container"}
      (dom/div #js {:id "clock"
                    :onClick #(om/transact! app :current-timer-state click-clock!)}
        (overlay current-timer-state)
        (str (pad minutes) ":" (pad seconds))))))

(defn reset-button [app owner]
  (dom/button #js {:onClick
                   (fn [e]
                     (om/update! app :seconds (* 25 60)))}
              "reset"))

(defn task-info [app]
  (let [display-task? (if (= nil (:task-id app)) "none" "block")]
    (dom/div #js {:style #js {:display display-task?}}
             "This is task info")))

(defn pause-resume-reset-btns [app]
  (let [classname (if (= :paused (:current-timer-state app)) "timerPaused" "")
        change-cts-to #(om/update! app :current-timer-state %)]
    (dom/div #js {:className (str "timerBtnContainer " classname)}
             (dom/div #js {:className "timerBtn" :id "pause-btn"
                           :onClick #(change-cts-to :paused)} "PAUSE")
             (dom/div #js {:className "timerBtn" :id "resume-btn"
                           :onClick #(change-cts-to :ticking)} "RESUME")
             (dom/div #js {:className "timerBtn" :id "reset-btn"
                           :onClick #(change-cts-to :new)} "RESET"))))

(defn clock-widget [app owner opts]
  (reify
    om/IDidMount
    (did-mount [_]
      (when (= nil (:interval app))
        (om/update! app :interval (tick! app))))
    om/IRender
    (render [_]
      (dom/div nil
        (clock app)
        (dom/div #js {:className "taskInfo" })

        (js/console.log (get-in app [:current-timer-state]))

        (when (and
               #_(= :ticking (:current-timer-state app))
               (not= :new (:current-timer-state app)))
          (pause-resume-reset-btns app))))))

