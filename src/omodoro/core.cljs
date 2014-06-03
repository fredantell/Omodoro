(ns omodoro.core
  (:require [cljs.nodejs :as node]
            [cljs.core.async :refer [chan <! timeout]]
            [om.core :as om :include-macros true]
            [om.dom :as dom :include-macros true]
            [cljs.nodejs :as n]
            [omodoro.components.dailycommit :as dc]
            [omodoro.components.clock :as clock]
            [omodoro.components.pomodorometer :as pmtr]))

(enable-console-print!)

;; Next steps:
;; DONE: Add component to display day's goal and day's progress
;; Improve clock widget with hover pausing / proper reset
;; Build Timer done widget
;; Add ability to play sound
;; Add component to display day's goal and day's progress
;; Start arranging components in a guided workflow

;; Gamification - give prizes for who has longest streak
;; prop bets vs other players - bet you can't do 5 days in a row
;; 

(def gui (n/require "nw.gui"))  ;; kernel_time.core.gui.Window.get().zoomLevel = 2
(def state (atom {:app {:seconds (* 25 60)
                        :commitment 4
                        :completed 1
                        :current-task nil
                        :current-timer-state :new #_(:new :ticking :paused :finished)}
                  :settings {:timer-only? :true
                             :pom-length 25
                             :short-break 5
                             :long-break 30
                             :play-ticking-sound? :true}}))

(defn render-app [app owner opts]
  (reify
    om/IRender
    (render [_]
      (dom/div nil
               (om/build pmtr/pomodoro-meter (:app app))
               (om/build clock/clock-widget (:app app))
               (om/build dc/commit-widget (:app app))))))

(om/root
 render-app
 state
 {:target (. js/document (getElementById "root"))})

