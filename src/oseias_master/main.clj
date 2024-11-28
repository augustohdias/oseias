(ns oseias-master.main
  (:gen-class)
  (:require [oseias-master.core.adapters.web.handlers :as handlers]
            [reitit.ring :as ring]
            [reitit.swagger :as swagger]
            [reitit.swagger-ui :as swagger-ui]
            [ring.adapter.jetty :refer [run-jetty]]
            [ring.middleware.json :refer [wrap-json-body wrap-json-response]]))

(def app
  (-> (ring/ring-handler
       (ring/router
        [["/swagger.json"
          {:get {:no-doc true
                 :swagger {:info {:title "Oseias Master API"
                                  :description "API para gerenciar combates e personagens"}}
                 :handler (swagger/create-swagger-handler)}}]
         ["/combate"
          {:middleware [[wrap-json-body {:keywords? true}] wrap-json-response]}
          ["/atacar" {:post {:summary "Atacar"
                             :handler handlers/atacar-handler}}]
          ["/confrontar" {:post {:summary "Confrontar"
                                 :handler handlers/confrontar-handler}}]
          ["/brigar" {:post {:summary "Entrar numa Briga"
                             :handler handlers/brigar-handler}}]
          ["/virar-mare" {:post {:summary "Virar a Maré"
                                 :handler handlers/virar-mare-handler}}]
          ["/encerrar" {:post {:summary "Encerrar"
                               :handler handlers/encerrar-handler}}]]])
       (swagger-ui/create-swagger-ui-handler {:url "/swagger.json"}))
      wrap-json-response))

(defn -main []
  (let [port (Integer/parseInt (or (System/getenv "PORT") "3000"))]
    (println (str "Servidor iniciando na porta " port "..."))
    (run-jetty app {:port port
                    :join? false})))
