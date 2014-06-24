;; This will contain the page build for the timing workflow/route
;; 
(ns omodoro.routes.timer
  (:require [om.core :as om :include-macros true]
            [om.dom :as dom :include-macros true]
            [omodoro.components.clock :as clock]
            [omodoro.components.pomodorometer :as pmtr]
            [omodoro.components.menubar :as mb]))

(defn init [app owner opts]
  (reify
    om/IRender
    (render [_]
      (dom/div nil
               #_(js/console.log "Timer Init:" app #_(:current-timer-state app))
               (dom/div #js {:id "app-container"}
                        (om/build clock/clock-widget (:clock app))
                        (om/build pmtr/pomodoro-meter (:day app)))
               (dom/div #js {:id "menu-container"}
                        (om/build mb/menubar (:app app)))))))
