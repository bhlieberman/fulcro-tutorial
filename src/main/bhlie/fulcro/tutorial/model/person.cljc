(ns bhlie.fulcro.tutorial.model.person 
  #_{:clj-kondo/ignore [:unused-namespace]}
  (:require [com.wsscode.pathom.connect :as pc] 
            [bhlie.fulcro.tutorial.model.book :as-alias book]))

(def people {1 {::id 1
                ::name "Ben Lieberman"
                ::age 30
                :books #{1}}
             2 {::id 2
                ::name "Gilles Deleuze"
                ::age "dead"
                ::books #{2}}
             3 {::id 3
                ::name "Felix Guattari"
                ::age "dead"
                ::books #{2}}})

#?(:clj
   (pc/defresolver person-resolver [{::keys [id]}]
     {::pc/input #{::id}
      ::pc/output [:id ::name ::age {:person/books [::book/id]}]}
     (-> people 
         (get id)
         (update ::books (fn [ids] (mapv
                                    (fn [id] {::book/id id})
                                    ids))))))