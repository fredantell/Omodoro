(ns omodoro.core
  (:require [cljs.nodejs :as node]
            [cljs.core.async :refer [chan <! timeout]]
            [om.core :as om :include-macros true]
            [om.dom :as dom :include-macros true]
            [cljs.nodejs :as n]
            [omodoro.routes.intro :as intro]
            [omodoro.routes.four-oh-four :as fof]
            [omodoro.components.dailycommit :as dc]
            [omodoro.components.clock :as clock]
            [omodoro.components.pomodorometer :as pmtr]
            [omodoro.components.menubar :as mb]))

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

(def gui (n/require "nw.gui"))  ;; omodoro.core.gui.Window.get().zoomLevel = 2
(def state (atom {:app {:seconds (* 25 60)
                        :committed? true
                        :commitment 4
                        :completed 1
                        :current-task nil
                        :current-timer-state :new #_(:new :ticking :paused :finished)}
                  :route "intro"
                  :settings {:timer-only? :true
                             :pom-length 25
                             :short-break 5
                             :long-break 30
                             :play-ticking-sound? :true}}))

(comment
  {:clock {:paused? false
           :started nil
           :seconds (* 25 60) ;; pom-length - (now - started)
           :task-id agi39s
           }
   :day {:goal 8
         :completed 1}
   :user {}}

  {:settings {:pom-length 25
              :short-break 5
              :long-break 30
              :play-ticking? true
              :cycle [:p1 :s1 :p2 :s2 :p3 :s3 :p4 :l]
              :sound :default
              :max-estimate 7
              
              }}

  {:tasks [{:uuid agi39s
            :name "Make Widget"
            :estimate 4
            :spent [{:date :date
                     :time :time}
                    {:date :date
                     :time :time}] ;; take length to get count
            :added :date
            }
           ]}

  {:history [{:event :start ;; :start :pause :interruption :cancel :restart 
              :time 1200
              :task-id agi39s
              ;; derive state: task info -> estimate, current, 
              }
             {:event :pause
              :time 1205
              :task-id agi39s}
             {:task-id agi39s
              :started 1200
              :ended 1225
              :completed? true
              :interruptions [:task :pause]}]})

(defn get-component [state]
  (condp = (:route state)
    :intro intro/intro
    :timer timer/timer
    fof/four-oh-four))

(defn set-route! [e state]
  (let [hash (.. js/document -location -hash)]
    (. e preventDefault)
    (swap! state assoc :route hash)
    (js/console.log "new route: " (:route state))))
(defn init-routing []
  (js/window.addEventListener "popstate" #(set-route! % state)))
(init-routing)


(defn render-app [app owner opts]
  (reify
    om/IRender
    (render [_]
      (dom/div nil
               (om/build intro/init app))
      #_(if (get-in app [:app :committed?])
          (dom/div nil
                   (dom/div #js {:id "app-container"}
                            (om/build clock/clock-widget (:app app))
                            (om/build dc/commit-widget (:app app))
                            (om/build pmtr/pomodoro-meter (:app app)))
                   (dom/div #js {:id "menu-container"}
                            (om/build mb/menubar (:app app))))
          (dom/div nil
                   (om/build dc/commit-widget (:app app)))))))
   
(om/root
 render-app
 state
 {:target (. js/document (getElementById "root"))})

