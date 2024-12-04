(ns oseias-master.core.service.combate
  (:require [clojure.string :as str]
            [oseias-master.core.service.avaliacoes :refer [avaliar-rolagem avaliar-progresso calcular-resultado]]))

(defn- avancos-por-dano [classe]
  (get {:Problemático 12
        :Perigoso 8
        :Formidável 4
        :Extremo 2
        :Épico 1} (keyword classe) 0))

(defn- movimentos-por-avaliacao [sucesso? falha? sucesso-movimentos parcial-movimentos falha-movimentos]
  (cond
    sucesso? sucesso-movimentos
    falha? falha-movimentos
    :else parcial-movimentos))

;; Movimento Atacar
(defn atacar [body]
  (let [inimigo           (:inimigo body)
        rolagem           (:rolagem body)
        progresso-atual   (:progresso body)
        avaliacao         (avaliar-rolagem rolagem)
        classe            (:classe inimigo)
        avanco            (if (str/includes? avaliacao "Sucesso") (avancos-por-dano classe) 0)
        sucesso-decisivo? (str/includes? avaliacao "Decisivo")
        dano-personagem   (if sucesso-decisivo? (+ (:danoPersonagem body) 1) (:danoPersonagem body))
        falha?            (str/includes? avaliacao "Falha")]
    {:avanco-por-dano avanco
     :progresso       (min 10 (+ (progresso-atual) (* dano-personagem avanco)))
     :resultado       (calcular-resultado rolagem avaliacao)
     :sucesso         sucesso-decisivo?
     :movimentos      (movimentos-por-avaliacao
                       sucesso-decisivo?
                       falha?
                       ["Atacar" "Garantir Vantagem" "Perguntar ao Oráculo" "Encerrar Luta"]
                       ["Confrontar" "Virar a Maré" "Enfrentar Perigo" "Garantir Vantagem" "Ajudar Aliado" "Perguntar ao Oráculo"]
                       ["Pagar o Preço"])}))

;; Movimento Confrontar
(defn confrontar [body]
  (let [inimigo           (:inimigo body)
        rolagem           (:rolagem body)
        progresso-atual   (:progresso body)
        avaliacao         (avaliar-rolagem rolagem)
        classe            (:classe inimigo)
        avanco            (if (str/includes? avaliacao "Sucesso") (avancos-por-dano classe) 0)
        sucesso-decisivo? (str/includes? avaliacao "Decisivo")
        dano-personagem   (:danoPersonagem body)]
    (merge {:avanco-por-dano avanco
            :progresso       (min 10 (+ (progresso-atual) (* dano-personagem avanco)))
            :resultado       (calcular-resultado rolagem avaliacao)
            :sucesso         sucesso-decisivo?
            :movimentos      (if sucesso-decisivo?
                               ["Atacar" "Garantir Vantagem" "Convencer" "Perguntar ao Oráculo" "Encerrar Luta"]
                               ["Pagar o Preço"])} (when sucesso-decisivo?
                                                     {:consequencia {:escolha [{:efeito "Causar +1 de dano"}
                                                                                  {:efeito "Receber +1 Momentum"}]}}))))

;; Movimento Brigar
(defn brigar [body]
  (let [inimigo           (:inimigo body)
        rolagem           (:rolagem body)
        avaliacao         (avaliar-rolagem rolagem)
        classe            (:classe inimigo)
        avanco            (if (str/includes? avaliacao "Sucesso") (avancos-por-dano classe) 0)
        sucesso-decisivo? (str/includes? avaliacao "Decisivo")
        sucesso-parcial?  (str/includes? avaliacao "Parcial")
        falha?            (str/includes? avaliacao "Falha")
        com-iniciativa    ["Atacar" "Garantir Vantagem" "Convencer" "Perguntar ao Oráculo"]
        sem-iniciativa    ["Confrontar" "Garantir Vantagem" "Convencer" "Perguntar ao Oráculo"]]

    (merge
     {:avanco-por-dano avanco
      :resultado       (calcular-resultado rolagem avaliacao)
      :movimentos      (cond sucesso-decisivo? com-iniciativa falha? ["Pagar o Preço"] :else '())}
     (when sucesso-parcial?
       {:consequencia {:apenas-um [{:efeito     "Ter iniciativa"
                                    :movimentos com-iniciativa}
                                   {:efeito     "Receber +2 Momentum"
                                    :movimentos sem-iniciativa}]}}))))

;; Movimento Virar a Maré
(defn virar-mare [body]
  (let [rolagem   (:rolagem body)
        avaliacao (avaliar-rolagem rolagem)
        sucesso?  (str/includes? avaliacao "Sucesso")]
    (merge {:resultado  (calcular-resultado rolagem avaliacao)
            :movimentos (if sucesso? ["Atacar"] ["Pagar o Preço"])} 
           (when :consequencia {:todos [{:efeito "Bonus de Rolagem +1"} 
                                        {:efeito "+1 Momentum"}]}))))

;; Movimento Encerrar
(defn encerrar [body]
  (let [progresso        (:progresso body)
        rolagem          (:rolagem body)
        avaliacao        (avaliar-progresso progresso rolagem)
        sucesso?         (str/includes? avaliacao "Sucesso")
        sucesso-parcial? (str/includes? avaliacao "Parcial")]
    
    (merge {:resultado  (calcular-resultado rolagem avaliacao)
            :movimentos (if sucesso? ["Continuar Campanha"] ["Pagar o Preço"])} 
           (when sucesso-parcial? {:consequencia {:escolha [{:efeito "Suportar Dano"}
                                                               {:efeito "Suportar Estresse"}
                                                               {:efeito "Novo inimigo aparece"}
                                                               {:efeito "Item perdido ou quebrado"}
                                                               {:efeito "Objetivo se torna inalcançável"}
                                                               {:efeito "Marcado de vingança"}]}}))))
