(ns oseias-master.core.model.movimentos)

(def movimentos {:aventura       {:enfrentar-perigo   "Enfrentar o perigo"
                                  :garantir-vantagem  "Garantir uma vantagem"
                                  :reabastecer        "Reabastecer"
                                  :coletar-info       "Coletar Informações"
                                  :curar              "Curar"
                                  :montar-acampamento "Montar Acampamento"
                                  :jornada            "Empreender uma jornada"
                                  :fim-jornada        "Alcançar eu destino"}
                 :relacionamento {:convencer      "Convencer"
                                  :estadia        "Estadia"
                                  :ajudar         "Fornecer Ajuda"
                                  :forjar-vinculo "Forjar um vinculo"
                                  :testar-vinculo "Testar seu vinculo"
                                  :circulo        "Desenhar o círculo"
                                  :ajudar-aliado  "Ajudar seu aliado"
                                  :epilogo        "Escrever seu epílogo"}
                 :combate        {:brigar        "Entrar numa briga"
                                  :atacar        "Atacar"
                                  :virar-mare    "Virar a maré"
                                  :confrontar    "Confrontar"
                                  :encerrar-luta "Encerrar a luta"}
                 :punicao        {:suportar-dano          "Suportar dano"
                                  :morte                  "Enfrentar a morte"
                                  :suportar-estresse      "Suportar estresse"
                                  :desolacao              "Enfrentar a desolação"
                                  :suprimentos-consumidos "Suprimentos consumidos"
                                  :reves                  "Encarar um revés"}
                 :missao         {:voto      "Juramentar um Voto de Ferro"
                                  :marco     "Alcançar um marco"
                                  :renunciar "Renunciar seu voto"
                                  :avancar   "Avançar"
                                  :cumprir   "Cumprir seu voto"}
                 :destino        {:pagar-preco "Pagar o preço"
                                  :oraculo     "Perguntar ao oráculo"}})