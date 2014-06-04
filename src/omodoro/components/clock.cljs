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
                                (om/update! data :current-timer-state :finished)
                                ())))
        reset-time! (fn []
                      (when (= :new (:current-timer-state @data))
                        (om/update! data :seconds (* 25 60))))
        
        manage-clock! (fn []
                        (do
                          (js/console.log "managing clock!!")
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

(defn reset-button [app owner interval]
  (dom/button #js {:onClick
                   (fn [e]
                     (om/update! app :seconds (* 25 60)))}
              "reset"))

(defn clock-widget [app owner opts]
  (reify
    om/IInitState
    (init-state [_]
      {:interval nil
       :chan (chan)})
    om/IDidMount
    (did-mount [_]
      (om/set-state! owner :interval (tick! app)))
    om/IWillMount
    (will-mount [_]
      (let [c (om/get-state owner :chan)]
        (js/console.log c))
      (go (loop []
            #_(<! ))))
    om/IRenderState
    (render-state [_ {:keys [interval]}]
      (dom/div nil
        (clock app)
        (when (= :paused (:current-timer-state app))
          (reset-button app owner interval))))))

