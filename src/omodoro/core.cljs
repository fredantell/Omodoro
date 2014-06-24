(ns omodoro.core
  (:require [cljs.nodejs :as node]
            [cljs.core.async :refer [chan <! timeout]]
            [om.core :as om :include-macros true]
            [om.dom :as dom :include-macros true]
            [cljs.nodejs :as n]
            [omodoro.routes.routes :as routes]
            [omodoro.routes.timer :as timer]
            ;;[omodoro.routes.intro :as intro]
            ;;[omodoro.routes.four-oh-four :as fof]
            ;; [omodoro.components.dailycommit :as dc]
            ;; [omodoro.components.clock :as clock]
            ;; [omodoro.components.pomodorometer :as pmtr]
            #_[omodoro.components.menubar :as mb]))

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

(def state (atom {:app {:clock
                         {:seconds (* 25 60)
                            :current-task nil
                            :current-timer-state :new #_(:new :ticking :paused :finished)
                            :task-id nil}
                        :day
                          {:commitment 4
                           :completed 1}
                        :user
                          {:name "Fredrik"}
                        :route "#timer"
                        :settings {:timer-only? :true
                                    :pom-length 25
                                    :short-break 5
                                    :long-break 30
                                    :play-ticking-sound? :true}}}))

(defn set-defaults! []
  "Set app state to user specified defaults"
  (swap! state
         assoc-in [:app :clock :seconds]
         (* 60 (get-in @state [:app :settings :pom-length]))))

(set-defaults!)


;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; Routing
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(defn get-component [route]
  "Return the function that will be used to render the view"
  (js/console.log "get-component fired")
  (let [routes {"#intro" routes/intro
                "#timer" routes/timer}]
    (get routes route routes/four-oh-four))
  #_(condp = route
    "#intro" routes/intro
    "#timer" routes/timer
    routes/four-oh-four))

(defn set-route! [state]
  "Update the route in the atom, which will trigger a re-render"
  (let [hash (.. js/document -location -hash)]
    (swap! state assoc-in [:app :route] hash)
    (js/console.log "new route: " (get-in @state [:app :route]))))

(defn init-routing []
  "Listen to changes in the address bar and call set-route!"
  (js/window.addEventListener "popstate" #(set-route! state)))
(init-routing)


(defn render-app [app owner opts]
  (reify
    om/IRender
    (render [_]
      (let [comp-to-render (get-component (get-in app [:app :route] app))]
        (dom/div nil
          #_(js/console.log (:app app))
          #_(om/build timer/init (:app app)))
          #_(routes/timer (:app app) owner opts)
          #_(routes/timer (:app app))
          (comp-to-render (:app app))
          ))))

   
(om/root
 render-app
 state
 {:target (. js/document (getElementById "root"))})







;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

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
