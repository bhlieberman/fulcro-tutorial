(ns bhlie.fulcro.tutorial.server
  (:require [clojure.core.async :as a]
            [com.fulcrologic.fulcro.server.api-middleware :as fmw :refer [not-found-handler wrap-api]]
            [com.wsscode.pathom.connect :as pc]
            [com.wsscode.pathom.core :as p]
            [ring.middleware.content-type :refer [wrap-content-type]]
            [ring.middleware.not-modified :refer [wrap-not-modified]]
            [ring.middleware.resource :refer [wrap-resource]]
            [bhlie.fulcro.tutorial.model.book :refer [book-resolver]]
            [bhlie.fulcro.tutorial.model.person :refer [person-resolver]]))

(def my-resolvers [person-resolver book-resolver])

(def parser
  (p/parallel-parser
   {::p/env {::p/reader [p/map-reader
                         pc/parallel-reader
                         pc/open-ident-reader]
             ::pc/mutation-join-globals [:tempids]}
    ::p/mutate pc/mutate-async
    ::p/plugins [(pc/connect-plugin {::pc/register my-resolvers})
                 (p/post-process-parser-plugin p/elide-not-found)
                 p/error-handler-plugin]}))

(def middleware
  (-> not-found-handler
      (wrap-api {:uri "/api"
                 :parser (fn [query] (a/<!! (parser {} query)))})
      (fmw/wrap-transit-params)
      (fmw/wrap-transit-response)
      (wrap-resource "public")
      wrap-content-type
      wrap-not-modified))