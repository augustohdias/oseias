(ns oseias-master.core.model.efeito)

(defn efeito [mecanica escolha narrativa movimentos]
  {:mecanica   {:efeito mecanica :escolha escolha}
   :narrativa  narrativa
   :movimentos movimentos})

(defn mecanica [args] (let [[tipo qtd] args
                            qtdstr     (if (> qtd 0) (str "+" qtd) (str qtd))
                            mecanicas  {:momentum      (str qtdstr " Momentum")
                                        :saude         (str qtdstr " Saúde")
                                        :espirito      (str qtdstr " Espirito")
                                        :bonus-rolagem (str qtdstr " Rolagem")
                                        :suprimentos   (str qtdstr " Suprimento")}] (mecanicas (keyword tipo))))

(def narrativa {:sucesso-critico "Algo muito bom acontece, que avance com os objetivos de voto do personagem ou que beneficie o personagem permanentemente."
                :sucesso-decisivo    "Algo bom acontece, que beneficie o personagem."
                :sucesso-parcial "A complexidade em que o personagem se encontra se aprofunda ou a cena atual se complica ligeiramente."
                :falha   "A complexidade em que o personagem se encontra é aprofundada em alguma perda."
                :falha-critica   "A complexidade em que o personagem se encontra sofre uma consequência terrível ou algum objetivo não é mais alcançável."})

(defn quando [checagem a b c] (cond (boolean (#{:sucesso-decisivo :sucesso-critico} checagem)) a
                                    (= checagem :sucesso-parcial) b
                                    (boolean (#{:falha :falha-critica} checagem)) c))