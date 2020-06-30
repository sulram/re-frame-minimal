(ns app.core
  (:require [reagent.core :as r]
            [reagent.dom :as rdom]
            [re-frame.core :as rf]))

;; All the minimal app on the same file to simplify

;; -- DB -----------------------------------------------

;; create app db
(def app-db
  {:count 0})
  ;;(r/atom {:count 0}))

;; -- EVENTS -----------------------------------------------

;; define initial state
(rf/reg-event-db
 ::initialize-db
 (fn [_ _] app-db))

;; dec
(rf/reg-event-db
 ::decrement
 (fn [db _]
   (update-in db [:count] dec)))
  ;;(swap! db update-in [:count] dec)))

;; inc
(rf/reg-event-db
 ::increment
 (fn [db _]
   (update-in db [:count] inc)))
  ;;(swap! db update-in [:count] inc)))

;; -- QUERY -----------------------------------------------

(rf/reg-sub
 ::count
 (fn [db]
   (:count db)))
  ;;  (get-in db [:count])))

;; -- VIEWS -----------------------------------------------

;; a button
(defn btn [key text color]
  [:a
   {:href "#"
    :class-name (str "px-1 m-1 bg-" color "-400 hover:bg-" color "-500")
    :on-click #(rf/dispatch [key])}
   text])

;; the main view
(defn app-view []
  (let [count  (rf/subscribe [::count])]
    [:div {:id "app-core"
           :class-name "h-full flex justify-center content-center"}
     [:div {:class-name "self-center p-10 bg-white rounded"}
      [:h1 {:class-name "text-5xl"} "minimal"]
      [:div
       [:div {:class-name "my-2"} @count]
       (btn ::decrement "-" "red")
       (btn ::increment "+" "green")]]]))

;; -- MAIN -----------------------------------------------

;; shadow-cljs hot-reload
(defn ^:dev/after-load mount-root []
  (rf/dispatch-sync [::initialize-db])
  (js/console.log (str "mounted at " (js/Date)))
  (let [root-el (.getElementById js/document "app")]
    (rdom/unmount-component-at-node root-el)
    (rdom/render [app-view] root-el)))

;; the init function
(defn ^:export init []
  (mount-root))
