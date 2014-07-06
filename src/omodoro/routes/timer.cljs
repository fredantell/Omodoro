;; This will contain the page build for the timing workflow/route
;; 
(ns omodoro.routes.timer
  (:require [om.core :as om :include-macros true]
            [om.dom :as dom :include-macros true]
            [omodoro.components.clock :as clock]
            [omodoro.components.pomodorometer :as pmtr]
            [omodoro.components.menubar :as mb]))

(defn timer-styles [clock-state]
  (let [task-id (:task-id clock-state)
        break? (= task-id :break)
        break-styles #js {:style
                          #js {:background-color "rgb(48,72,95)"}}
        work-styles #js {:style
                         #js {:background-color "rgb(252,44,30)"}}]
    (if break? break-styles work-styles)))

(defn init [app owner opts]
  (reify
    om/IRender
    (render [_]
      (dom/div (timer-styles (:clock app))
               #_(js/console.log "Timer Init:" app #_(:current-timer-state app))
               (dom/div #js {:id "app-container"}
                        (om/build clock/clock-widget {:clock (:clock app)
                                                      :day (:day app)
                                                      :settings (:settings app)})
                        (om/build pmtr/pomodoro-meter {:clock (:clock app)
                                                      :day (:day app)}))
               (dom/div #js {:id "menu-container"}
                        (om/build mb/menubar (:app app)))))))
