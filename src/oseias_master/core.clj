(ns oseias-master.core
  (:require [reitit.ring :as ring]
            [reitit.swagger :as swagger]
            [reitit.swagger-ui :as swagger-ui]
            [ring.adapter.jetty :refer [run-jetty]]
            [ring.middleware.json :refer [wrap-json-body wrap-json-response]]
            [ring.util.response :as response]
            [clojure.string :as str]))

(defn avaliar-rolagem [rolagem]
  (let [valor-acao (+ (:d6 rolagem) (:bonus rolagem))]
    (cond
      ;; Sucesso Decisivo
      (and (> valor-acao (:d10a rolagem))
           (> valor-acao (:d10b rolagem))) "Sucesso Decisivo"

      ;; Sucesso Parcial
      (or (> valor-acao (:d10a rolagem))
          (> valor-acao (:d10b rolagem))) "Sucesso Parcial"

      ;; Falha
      :else "Falha")))

(defn avaliar-progresso [progresso rolagem]
  (let [valor-acao (progresso)]
    (cond
      ;; Sucesso Decisivo
      (and (> valor-acao (:d10a rolagem))
           (> valor-acao (:d10b rolagem))) "Sucesso Decisivo"

      ;; Sucesso Parcial
      (or (> valor-acao (:d10a rolagem))
          (> valor-acao (:d10b rolagem))) "Sucesso Parcial"

      ;; Falha
      :else "Falha")))

(defn avancos-por-dano [classe]
  (let [mapeamento {:Problemático 12
                    :Perigoso     8
                    :Formidável   4
                    :Extremo      2
                    :Épico        1}]
    (get mapeamento (keyword classe) 0)))

;; Movimento brigar
(defn atacar [body]
  (let [inimigo     (:inimigo body)
        
        rolagem     (:rolagem body)

        avaliacao   (avaliar-rolagem rolagem)
        
        classe      (:classe inimigo)

        avanco      (if (str/includes? avaliacao "Sucesso") (avancos-por-dano classe) 0)
        
        sucesso     (str/includes? avaliacao "Decisivo")

        com-sucesso '("Atacar" "Garantir Vantagem" "Perguntar ao Oráculo" "Encerrar Luta")

        sem-sucesso '("Confrontar" "Virar a Maré" "Enfrentar Perigo" "Garantir Vantagem" "Ajudar Aliado" "Suportar Dano" "Suportar Estresse" "Perguntar ao Oráculo")
        
        falha       '("Pagar o Preço")]
        

    {:avancoPorDano avanco
     :resultado     {:avaliacao avaliacao
                     :critico   (= (:d10a rolagem) (:d10b rolagem))}
     :sucesso       sucesso
     :movimentos    (if sucesso com-sucesso (if (= avaliacao "Falha") falha sem-sucesso))}))

;; Movimento confrontar
(defn confrontar [body]
  (let [inimigo     (:inimigo body)
        
        rolagem     (:rolagem body)
        
        avaliacao   (avaliar-rolagem rolagem)
        
        classe      (:classe inimigo)
        
        avanco      (if (str/includes? avaliacao "Sucesso") (avancos-por-dano classe) 0)
        
        sucesso     (str/includes? avaliacao "Decisivo")
        
        com-sucesso '("Atacar" "Garantir Vantagem" "Perguntar ao Oráculo" "Encerrar Luta")
        
        sem-sucesso '("Pagar o Preço")]
        
    
    {:avancoPorDano avanco
     :resultado     {:avaliacao avaliacao
                     :critico   (= (:d10a rolagem) (:d10b rolagem))}
     :sucesso       sucesso
     :movimentos    (if sucesso com-sucesso sem-sucesso)}))

;; Movimento confrontar
(defn brigar [body]
    (let [inimigo     (:inimigo body)
          
          rolagem     (:rolagem body)
          
          avaliacao   (avaliar-rolagem rolagem)
          
          classe      (:classe inimigo)
          
          avanco      (if (str/includes? avaliacao "Sucesso") (avancos-por-dano classe) 0)
          
          decisivo    (str/includes? avaliacao "Decisivo")
          
          com-sucesso '("Escolher Efeito")
          
          falha       '("Pagar o Preço")]
          
      
      {:avancoPorDano avanco
       :resultado     {:avaliacao avaliacao
                       :critico   (= (:d10a rolagem) (:d10b rolagem))}
       :decisivo      decisivo
       :movimentos    (if decisivo com-sucesso falha)}))

;; Movimento Virar a Maré
(defn virar-mare [body]
  (let [inimigo    (:inimigo body)

        rolagem    (:rolagem body)

        avaliacao  (avaliar-rolagem rolagem)

        classe     (:classe inimigo)

        avanco     (if (str/includes? avaliacao "Sucesso") (avancos-por-dano classe) 0)

        sucesso    (str/includes? avaliacao "Sucesso")

        no-sucesso '("Atacar")

        na-falha   '("Pagar o Preço")]
    
    
    {:resultado  {:avaliacao avaliacao
                  :critico   (= (:d10a rolagem) (:d10b rolagem))}
     :sucesso    sucesso
     :movimentos (if sucesso no-sucesso na-falha)}))

(defn encerrar [body]
  (let [inimigo    (:inimigo body)

        progresso  (:progresso body)

        rolagem    (:rolagem body)

        avaliacao  (avaliar-progresso progresso rolagem) 

        sucesso    (str/includes? avaliacao "Sucesso")
        
        no-sucesso '("Escolher Efeito")

        na-falha   '("Pagar o Preço")] 
    
    {:resultado  {:avaliacao avaliacao
                  :critico   (= (:d10a rolagem) (:d10b rolagem))}
     :sucesso    sucesso
     :movimentos (if sucesso no-sucesso na-falha)}))

(defn comprimir [body]
  (let [nome        (:nome body)
        momentum    (:momentum body)
        saude       (:saude body)
        espirito    (:espirito body)
        suprimentos (:suprimentos body)
        atributos   (:atributos body)
        gume        (:gume atributos)
        coracao     (:coracao atributos)
        ferro       (:ferro atributos)
        sombra      (:sombra atributos)
        centelha    (:centelha atributos)
        ]
    {:personagem (apply str [nome ":" momentum ":" saude ":" espirito ":" suprimentos ":" gume ":" coracao ":" ferro ":" sombra ":" centelha])}
    )
  )

(defn decomprimir [body]
  (let [personagem (str/split (:personagem body) #":")
        nome (get personagem 0)
        momentum (Integer/parseInt (get personagem 1))
        saude (Integer/parseInt (get personagem 2))
        espirito (Integer/parseInt (get personagem 3))
        suprimentos (Integer/parseInt (get personagem 4))
        gume (Integer/parseInt (get personagem 5))
        coracao (Integer/parseInt (get personagem 6))
        ferro (Integer/parseInt (get personagem 7))
        sombra (Integer/parseInt (get personagem 8))
        centelha (Integer/parseInt (get personagem 9))
        atributos {:gume gume
                   :coracao coracao
                   :ferro ferro
                   :sombra sombra
                   :centelha centelha}
        ]
    {:nome nome
     :momentum momentum
     :saude saude
     :espirito espirito
     :suprimentos suprimentos
     :atributos atributos}))

;; Função para criar um middleware com JSON
(defn wrap-json [handler]
  (-> handler
      (wrap-json-body {:keywords? true})
      wrap-json-response))

;; Rotas com Documentação OpenAPI
(def app
  ( -> (ring/ring-handler
        (ring/router
         [["/swagger.json"
           {:get {:no-doc  true
                  :swagger {:info {:title       "Oseias Master API"
                                   :description "API para gerenciar combates e personagens"}}
                  :handler (swagger/create-swagger-handler)}}]

          ["/combate"
           {:middleware [wrap-json]}
           ["/atacar"
            {:post {:summary     "Atacar"
                    :description "Executa checagens para o movimento 'Atacar' e fornece possíveis movimentos subsequentes."
                    :parameters  {:body {:inimigo {:classe string?}
                                         :rolagem {:d6    int?
                                                   :bonus int?
                                                   :d10a  int?
                                                   :d10b  int?}}}
                    :responses   {200 {:description "Resultado do ataque"
                                       :content     {:application/json {:schema {:type :object}}}}}
                    :handler     (fn [request] (response/response (atacar (:body request))))}}]

           ["/confrontar"
            {:post {:summary     "Confrontar"
                    :description "Executa checagens para o movimento 'Confronta' e fornece possíveis movimentos subsequentes."
                    :parameters  {:body {:inimigo {:classe string?}
                                         :rolagem {:d6    int?
                                                   :bonus int?
                                                   :d10a  int?
                                                   :d10b  int?}}}
                    :responses   {200 {:description "Resultado do confronto"
                                       :content     {:application/json {:schema {:type :object}}}}}
                    :handler     (fn [request] (response/response (confrontar (:body request))))}}]

           ["/brigar"
            {:post {:summary     "Entrar numa Briga"
                    :description "Executa checagens para o movimento 'Entrar numa Briga' e fornece possíveis movimentos subsequentes."
                    :parameters  {:body {:inimigo {:classe string?}
                                         :rolagem {:d6    int?
                                                   :bonus int?
                                                   :d10a  int?
                                                   :d10b  int?}}}
                    :responses   {200 {:description "Resultado da briga"
                                       :content     {:application/json {:schema {:type :object}}}}}
                    :handler     (fn [request] (response/response (brigar (:body request))))}}]

           ["/virar-mare"
            {:post {:summary     "Virar a Maré"
                    :description "Executa checagens de dado para o movimento 'Virar a Maré' e fornece possíveis movimentos subsequentes."
                    :parameters  {:body {:inimigo {:classe string?}
                                         :rolagem {:d6    int?
                                                   :bonus int?
                                                   :d10a  int?
                                                   :d10b  int?}}}
                    :responses   {200 {:description "Resultado de Virar a Maré"
                                       :content     {:application/json {:schema {:type :object}}}}}
                    :handler     (fn [request] (response/response (virar-mare (:body request))))}}]

           ["/encerrar"
            {:post {:summary     "Encerrar Luta"
                    :description "Executa checagens de dado para o movimento 'Encerrar a Luta' e fornece possíveis movimentos subsequentes."
                    :parameters  {:body {:inimigo   {:classe string?}
                                         :progresso int?
                                         :rolagem   {:d10a int?
                                                     :d10b int?}}}
                    :responses   {200 {:description "Resultado de Encerrar Luta"
                                       :content     {:application/json {:schema {:type :object}}}}}
                    :handler     (fn [request] (response/response (encerrar (:body request))))}}]]

          ["/personagem"
           {:middleware [wrap-json]}
           ["/comprimir"
            {:post {:summary     "Comprimir Personagem"
                    :description "Comprime os atributos do personagem em uma string."
                    :parameters  {:body {:nome        string?
                                         :momentum    int?
                                         :saude       int?
                                         :espirito    int?
                                         :suprimentos int?
                                         :atributos   {:gume     int?
                                                       :coracao  int?
                                                       :ferro    int?
                                                       :sombra   int?
                                                       :centelha int?}}}
                    :responses   {200 {:description "Personagem comprimido"
                                       :content     {:application/json {:schema {:type :object}}}}}
                    :handler     (fn [request] (response/response (comprimir (:body request))))}}]

           ["/decomprimir"
            {:post {:summary     "Decomprimir Personagem"
                    :description "Decomprime os atributos do personagem a partir de uma string."
                    :parameters  {:body {:personagem string?}}
                    :responses   {200 {:description "Personagem decomprimido"
                                       :content     {:application/json {:schema {:type :object}}}}}
                    :handler     (fn [request] (response/response (decomprimir (:body request))))}}]]])
   ;; Swagger UI
        (swagger-ui/create-swagger-ui-handler {:url "/swagger.json"}))
    wrap-json-response))

(defn -main [& args]
  ;; Lê a porta da variável de ambiente PORT ou usa 3000 como padrão para desenvolvimento
  (let [port (Integer/parseInt (or (System/getenv "PORT") "3000"))]
    (println (str "Servidor iniciando na porta " port "..."))
    ;; Inicialize o servidor Jetty ou outra lógica aqui
    (run-jetty app {:port port :join? false})))
