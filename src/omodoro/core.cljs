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
                           :pom-or-break :pom ;; :pom :sbreak :lbreak
                           :current-task nil
                           :current-timer-state :new #_(:new :ticking :paused :finished)
                           :task-id nil
                           :interval nil}
                        :day
                          {:commitment 6
                           :completed 1
                           :until-break nil
                           :history [{:pom-uuid nil
                                      :events [{:start nil}
                                               :paused nil
                                               :start nil
                                               :finished nil]
                                      :task-id nil}]}
                        :user
                          {:name "Fredrik"}
                        :route "#timer"
                        :settings {:timer-only? :true
                                   :pom-length 25
                                   :short-break 5
                                   :long-break 30
                                   :poms-in-set 4
                                   :auto-break true
                                   :auto-pom true
                                   :play-ticking-sound? :true}}}))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; Set calculated defaults in app state
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
(defn set-default-time! []
  "Set seconds on clock to value in settings"
  (swap! state
         assoc-in [:app :clock :seconds]
         (* 60 (get-in @state [:app :settings :pom-length]))))

(defn set-next-break! []
  "Set :unti-break to the number of poms in a set specified in settings"
  (swap! state
         assoc-in [:app :day :until-break]
         (get-in @state [:app :settings :poms-in-set])))

(defn set-defaults! []
  "Set app state to user specified defaults"
  (set-default-time!)
  (set-next-break!))

(set-defaults!)

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; Routing
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(defn get-component [route]
  "Return the function that will be used to render the view"
  (let [routes {"#intro" routes/intro
                "#timer" routes/timer
                "#planning" routes/planning
                "#settings" routes/settings
                "#analytics" routes/analytics}]
    (get routes route routes/four-oh-four)))

(defn set-route! [state]
  "Update the route in the atom, which will trigger a re-render"
  (let [hash (.. js/document -location -hash)]
    (swap! state assoc-in [:app :route] hash)
    (js/console.log "new route: " (get-in @state [:app :route]))))

(defn init-window-hash! []
  "Set the initial window hash to app-state's value on initial load"
  (aset (.. js/document -location) "hash" (get-in @state [:app :route] "#timer")))

(defn init-routing []
  "Listen to changes in the address bar and call set-route!"
  (js/window.addEventListener "popstate" #(set-route! state)))

(init-window-hash!)
(init-routing)


;; (aget js/window "location" "hash")
;; (aset js/window "location" "hash" "#timer")
;; (aset js/window "location" "hash" "#planning")
;; (js/console.log  (get-in @state [:app :route]))
(js/console.log "hi from repl")

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; Main Render Functions
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(defn render-app [app owner opts]
  (reify
    om/IRender
    (render [_]
      (let [comp-to-render (get-component (get-in app [:app :route] app))]
        (comp-to-render (:app app))))))

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
