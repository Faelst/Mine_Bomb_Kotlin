package View

import Model.Area
import Model.EventArea
import java.awt.Color
import java.awt.Font
import javax.swing.BorderFactory
import javax.swing.JButton
import javax.swing.SwingUtilities
import javax.swing.border.Border

private val COR_BG_NORMAL = Color(184, 184, 184)
private val COR_BG_MARCACAO = Color(8, 179, 247)
private val COR_BG_EXPLOSAO = Color(189, 66, 68)
private val COR_TXT_VERDE = Color(0, 100, 0)

class BotaoCampo(private val campo: Area) : JButton() {

    init {
        font = font.deriveFont(Font.BOLD)
        background = COR_BG_NORMAL
        isOpaque = true
        border = BorderFactory.createBevelBorder(0)
        addMouseListener(MouseCliqueListener(campo, { it.open() }, { it.alterSelected() }))

        campo.onEvent(this::aplicarEstilo)
    }

    private fun aplicarEstilo(campo: Area, evento: EventArea) {
        when (evento) {
            EventArea.EXPLOSION -> aplicarEstiloExplosao()
            EventArea.OPEN -> aplicarEstiloAberto()
            EventArea.MARKED -> aplicarEstiloMarcado()
            else -> aplicarEstiloPadrao()
        }

        SwingUtilities.invokeLater() {
            repaint()
            validate()
        }
    }

    private fun aplicarEstiloExplosao() {
        background = COR_BG_EXPLOSAO
        text = "X"
    }

    private fun aplicarEstiloAberto() {
        background = COR_BG_NORMAL
        border = BorderFactory.createLineBorder(Color.GRAY)

        foreground = when (campo.amountParetsMined) {
            1 -> COR_TXT_VERDE
            2 -> Color.BLUE
            3 -> Color.YELLOW
            4, 5, 6 -> Color.RED
            else -> Color.PINK
        }
        text = if (campo.amountParetsMined > 0) campo.amountParetsMined.toString() else ""
    }

    private fun aplicarEstiloMarcado() {
        background = COR_BG_MARCACAO
        foreground = Color.BLACK
        text = "M"
    }

    private fun aplicarEstiloPadrao() {
        background = COR_BG_NORMAL
        border = BorderFactory.createBevelBorder(0)
        text = ""
    }
}
