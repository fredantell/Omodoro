(ns omodoro.components.clock
  (:require-macros [cljs.core.async.macros :refer [go]])
  (:require [om.core :as om :include-macros true]
            [om.dom :as dom :include-macros true]
            [cljs.core.async :refer [chan <! >! put! timeout]]))

;; Next Steps:
;; Add hover play/pause interaction
;; Add ability to pause and reset
;; Add ability to one off override duration of a cycle
;; If the pomodoro is paused for more than 3 minutes then auto reset

(defn tick! [data]
  (let [tick-once! (fn []
                     (when-not (zero? (:seconds @data))
                       (om/transact! data :seconds dec)))]
    (js/setInterval tick-once! 1000)))

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

(defn clock [{:keys [seconds current-timer-state]}]
  (let [minutes (int (/ seconds 60))
        seconds (rem seconds 60)]
    (dom/div #js {:id "clock-container"}
      (dom/div #js {:id "clock"}
        (overlay current-timer-state)
        (str (pad minutes) ":" (pad seconds))
        ))))

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
        (reset-button app owner interval)
        (clock app)))))

