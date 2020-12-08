package View

import Model.Tabuleiro
import java.awt.GridLayout
import javax.swing.JPanel

class PainelTabuleiro (
    tabuleiro: Tabuleiro
) : JPanel()
{

    init {
        layout = GridLayout(tabuleiro.qtdeLinhas, tabuleiro.qtdeColunas)
        tabuleiro.forEachCampo { campo ->
            val botao = BotaoCampo(campo)
            add(botao)
        }
    }

}