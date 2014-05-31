(ns omodoro.core
  (:require [cljs.nodejs :as node]
            [cljs.core.async :refer [chan <! timeout]]
            [om.core :as om :include-macros true]
            [om.dom :as dom :include-macros true]
            [cljs.nodejs :as n]
            [omodoro.components.dailycommit :as dc]
            [omodoro.components.clock :as clock]))

(enable-console-print!)

;; Next steps:
;; DONE: Add component to display day's goal and day's progress
;; Improve clock widget with hover pausing / proper reset
;; Build Timer done widget
;; Add ability to play sound
;; Start arranging components in a guided workflow

(def gui (n/require "nw.gui"))  ;; kernel_time.core.gui.Window.get().zoomLevel = 2
(def state (atom {:seconds (* 25 60)
                  :commitment 4
                  :completed 1
                  :current-task nil}))

(defn render-app [app owner opts]
  (reify
    om/IRender
    (render [_]
      (dom/div nil
               (om/build clock/clock-widget app)
               (om/build dc/commit-widget app)))))

(om/root
 render-app
 state
 {:target (. js/document (getElementById "root"))})

