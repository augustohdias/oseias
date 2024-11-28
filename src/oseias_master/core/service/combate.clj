(ns oseias-master.core.service.combate
  (:require [clojure.string :as str]
            [oseias-master.core.service.avaliacoes :refer [avaliar-rolagem avaliar-progresso]]))

(defn- avancos-por-dano [classe]
  (get {:Problemático 12
        :Perigoso 8
        :Formidável 4
        :Extremo 2
        :Épico 1} (keyword classe) 0))

(defn- calcular-resultado [rolagem avaliacao]
  {:avaliacao avaliacao
   :critico (= (:d10a rolagem) (:d10b rolagem))})

(defn- movimentos-por-avaliacao [sucesso? falha? sucesso-movimentos parcial-movimentos falha-movimentos]
  (cond
    sucesso? sucesso-movimentos
    falha? falha-movimentos
    :else parcial-movimentos))

;; Computa Progresso se baseando no dano
(defn computar-progresso [body]
  (let [progresso       (:progresso body)
        dano-personagem (:danoPersonagem body)
        avanco          (:avanco body)
        novo-progresso  (+ (* dano-personagem avanco) progresso)]
    {:progresso novo-progresso}))

;; Movimento Atacar
(defn atacar [body]
  (let [inimigo           (:inimigo body)
        rolagem           (:rolagem body)
        avaliacao         (avaliar-rolagem rolagem)
        classe            (:classe inimigo)
        avanco            (if (str/includes? avaliacao "Sucesso") (avancos-por-dano classe) 0)
        sucesso-decisivo? (str/includes? avaliacao "Decisivo")]
    {:avanco-por-dano avanco
     :resultado       (calcular-resultado rolagem avaliacao)
     :sucesso         sucesso-decisivo?
     :movimentos      (movimentos-por-avaliacao
                       sucesso-decisivo?
                       (= avaliacao "Falha")
                       ["Atacar" "Garantir Vantagem" "Perguntar ao Oráculo" "Encerrar Luta"]
                       ["Confrontar" "Virar a Maré" "Enfrentar Perigo" "Garantir Vantagem" "Ajudar Aliado" "Perguntar ao Oráculo"]
                       ["Pagar o Preço"])}))

;; Movimento Confrontar
(defn confrontar [body]
  (let [inimigo           (:inimigo body)
        rolagem           (:rolagem body)
        avaliacao         (avaliar-rolagem rolagem)
        classe            (:classe inimigo)
        avanco            (if (str/includes? avaliacao "Sucesso") (avancos-por-dano classe) 0)
        sucesso-decisivo? (str/includes? avaliacao "Decisivo")]
    {:avanco-por-dano avanco
     :resultado       (calcular-resultado rolagem avaliacao)
     :sucesso         sucesso-decisivo?
     :movimentos      (if sucesso-decisivo?
                        ["Atacar" "Garantir Vantagem" "Convencer" "Perguntar ao Oráculo" "Encerrar Luta"]
                        ["Pagar o Preço"])}))

;; Movimento Brigar
(defn brigar [body]
  (let [inimigo          (:inimigo body)
        rolagem          (:rolagem body)
        avaliacao        (avaliar-rolagem rolagem)
        classe           (:classe inimigo)
        avanco           (if (str/includes? avaliacao "Sucesso") (avancos-por-dano classe) 0)
        sucesso?         (str/includes? avaliacao "Sucesso")
        sucesso-parcial? (str/includes? avaliacao "Parcial")
        com-iniciativa   ["Atacar" "Garantir Vantagem" "Convencer" "Perguntar ao Oráculo"]
        sem-iniciativa   ["Confrontar" "Garantir Vantagem" "Convencer" "Perguntar ao Oráculo"]]

    (merge
     {:avanco-por-dano avanco
      :resultado       (calcular-resultado rolagem avaliacao)
      :movimentos      (if sucesso? com-iniciativa ["Pagar o Preço"])}
     (when sucesso-parcial?
       {:consequencia {:escolha-um [{:efeito     "Ter iniciativa"
                                     :movimentos com-iniciativa}
                                    {:efeito     "+2 Momentum"
                                     :movimentos sem-iniciativa}]}}))))

;; Movimento Virar a Maré
(defn virar-mare [body]
  (let [rolagem   (:rolagem body)
        avaliacao (avaliar-rolagem rolagem)
        sucesso?  (str/includes? avaliacao "Sucesso")]
    {:resultado  (calcular-resultado rolagem avaliacao)
     :sucesso    sucesso?
     :movimentos (if sucesso? ["Atacar"] ["Pagar o Preço"])}))

;; Movimento Encerrar
(defn encerrar [body]
  (let [progresso        (:progresso body)
        rolagem          (:rolagem body)
        avaliacao        (avaliar-progresso progresso rolagem)
        sucesso?         (str/includes? avaliacao "Sucesso")
        sucesso-parcial? (str/includes? avaliacao "Parcial")]
    
    (merge {:resultado  (calcular-resultado rolagem avaliacao)
            :movimentos (if sucesso? ["Continuar Campanha"] ["Pagar o Preço"])} 
           (when sucesso-parcial? {:consequencia {:escolha-um [{:efeito "Suportar Dano"}
                                                               {:efeito "Suportar Estresse"}
                                                               {:efeito "Novo inimigo aparece"}
                                                               {:efeito "Item perdido ou quebrado"}
                                                               {:efeito "Objetivo se torna inalcançável"}
                                                               {:efeito "Marcado de vingança"}]}}))))
