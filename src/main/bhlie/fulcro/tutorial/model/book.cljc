(ns bhlie.fulcro.tutorial.model.book
  (:require #?@(:clj [[com.wsscode.pathom.connect :as pc]
                      #_[com.wsscode.pathom.core :as p]])))

(def books  {1 {::id 1
                ::title "Filthy Synecdoche"
                ::author "Ben Lieberman"}
             2 {::id 2
                ::title "Anti-Oedipus"
                ::author "Gilles Deleuze and Felix Guattari"}})

#?(:clj
   (pc/defresolver book-resolver [{::keys [id]}]
     {::pc/input #{::id}
      ::pc/output [::id ::title ::author]}
     (get books id)))