(ns bhlie.fulcro.tutorial.app
  (:require
   [com.fulcrologic.fulcro.application :as app]
   [com.fulcrologic.fulcro.components :as comp :refer [defsc]]
   [com.fulcrologic.fulcro.dom :as dom]))

(defonce app (app/fulcro-app))

(defsc Book [this {:book/keys [id title] :as props}]
  {:ident :book/id
   :query [:book/title :book/id]
   :initial-state {:book/id :param/id
                   :book/title :param/title}}
  (dom/div
   "Title: " title))

(def ui-book (comp/factory Book {:keyfn :book/id}))

(defsc Person [this {:person/keys [id name books] :as props}]
  {:initial-state {:person/name :param/name
                   :person/id :param/id
                   :person/books [{:id 1
                                   :title "Filthy Synecdoche"}]}
   :query [:person/id :person/name {:person/books (comp/get-query Book)}]
   :ident :person/id}
  (dom/div :.ui.segment
   (dom/div "Name: " name)
   (dom/h3 "Books")
   (dom/ul (map ui-book books))))

(def ui-person (comp/factory Person {:keyfn :person/id}))

(defsc Root [this {:root/keys [person] :as props}]
  {:initial-state {:root/person {:id 1
                                 :name "Ben"}}
   :query [{:root/person (comp/get-query Person)}]}
  (dom/div
   (ui-person person)))

(defn ^:export init
  "Shadow-cljs sets this up to be our entry-point function. See shadow-cljs.edn `:init-fn` in the modules of the main build."
  []
  (app/mount! app Root "root")
  (js/console.log "Loaded"))

(defn ^:export refresh
  "During development, shadow-cljs will call this on every hot reload of source. See shadow-cljs.edn"
  []
  ;; re-mounting will cause forced UI refresh, update internals, etc.
  (app/mount! app Root "root")
  ;; As of Fulcro 3.3.0, this addition will help with stale queries when using dynamic routing:
  (comp/refresh-dynamic-queries! app)
  (js/console.log "Hot reload"))