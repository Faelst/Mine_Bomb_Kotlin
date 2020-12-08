package View

import Model.Tabuleiro
import Model.TabuleiroEvento
import javax.swing.JFrame
import javax.swing.JOptionPane
import javax.swing.SwingUtilities


class TelaPrincipal : JFrame() {
    private val tabuleiro = Tabuleiro(qtdeLinhas = 30, qtdeColunas = 60, qtdeMinas = 50)
    private val painelTabuleiro = PainelTabuleiro(tabuleiro)

    init {
        tabuleiro.onEvento(this::mostrarResultado)
        add(painelTabuleiro)

        setSize(690, 438)
        setLocationRelativeTo(null)
        defaultCloseOperation = EXIT_ON_CLOSE
        title = "Campo Minado"
        isVisible = true
    }

    private fun mostrarResultado(evento: TabuleiroEvento) {
        SwingUtilities.invokeLater() {
            val msg = when (evento) {
                TabuleiroEvento.VITORIA -> "Voce Ganhou !"
                TabuleiroEvento.DERROTA -> " Voce Perdeu!"
            }
            JOptionPane.showMessageDialog(this, msg)
            tabuleiro.reiniciar()

            painelTabuleiro.repaint()
            painelTabuleiro.validate()
        }
    }
}

fun main(args: Array<String>) {
    TelaPrincipal()
}

