(ns oseias-master.core.service.aventura
  (:require [oseias-master.core.model.efeito :as efeito]
            [oseias-master.core.model.movimentos :as mov]
            [oseias-master.core.service.dados :as dados]))

(defn enfrentar-perigo [[checagem narrativa]] 
  (let [escolha    (efeito/quando checagem "todos" "um" "n/a") 
        
        efeitos    (efeito/quando checagem
                                  (efeito/mecanica [:momentum 1])
                                  (map efeito/mecanica [[:momentum -1] [:saude -1] [:espirito -1] [:suprimentos -1]])
                                  ()) 
        
        movimentos (efeito/quando checagem
                                  (vals (remove #(= :enfrentar-perigo %) (mov/movimentos :aventura)))
                                  (map (mov/movimentos :punicao) [:suportar-dano :suportar-estresse])
                                  ((mov/movimentos :destino) :pagar-preco))]
    (efeito/efeito efeitos escolha narrativa movimentos)))

(defn garantir-vantagem [checagem narrativa]
  (let [escolha    (efeito/quando checagem "um" "todos" "todos") 
        efeitos    (efeito/quando checagem
                                  (map efeito/mecanica [[:bonus-rolagem 1 :momentum :2]])
                                  (map efeito/mecanica [[:momentum 1]])
                                  ())  
        movimentos (efeito/quando checagem
                                  (vals (mov/movimentos :aventura))
                                  (map (mov/movimentos :punicao) [:suportar-dano :suportar-estresse])
                                  ((mov/movimentos :destino) :pagar-preco))]
    (efeito/efeito efeitos escolha narrativa movimentos)))

(defn coletar-info [body]
  (let [vinculo (:vinculo body)] {}))

(defn acampar [body] {})

(defn empreender-jornada [body] {})

(defn terminar-jornada [body] {})

(defn curar [body] {})


(defn acao []
  (let [;;avaliar
        ;; {consequencia: { efeito: "string", movimentos: ["string"], narrativa: "string" }, progresso: 0 }
        ]))