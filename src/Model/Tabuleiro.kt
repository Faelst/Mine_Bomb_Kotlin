package Model

import java.util.*
import kotlin.collections.ArrayList

enum class TabuleiroEvento {
    VITORIA,
    DERROTA
}

class Tabuleiro(
    val qtdeLinhas: Int,
    val qtdeColunas: Int,
    private val qtdeMinas: Int
) {
    private val campos = ArrayList<ArrayList<Area>>()
    private val callback = ArrayList<(TabuleiroEvento) -> Unit>()

    init {
        gerarCampos()
        associarVizinhos()
        sortearMinas()
    }

    private fun gerarCampos() {
        for (linha in 0 until qtdeLinhas) {
            campos.add(ArrayList())

            for (coluna in 0 until qtdeColunas) {
                val novoCampo = Area(linha, coluna)
                novoCampo.onEvent(this::verificaDerrotaOuVitoria)
                campos[linha].add(novoCampo)
            }
        }
    }

    private fun associarVizinhos() {
        forEachCampo { associarVizinhos(it) }
    }

    private fun associarVizinhos(campo: Area) {
        val (linha, coluna) = campo

        val linhas = arrayOf(linha - 1, linha, linha + 1)
        val colunas = arrayOf(coluna - 1, coluna, coluna + 1)

        linhas.forEach { l ->
            colunas.forEach { c ->
                val atual = campos.getOrNull(l)?.getOrNull(c)
                atual?.takeIf { campo != it }?.let { campo.addParent(it) }
            }
        }
    }

    private fun sortearMinas() {
        val gerador = Random()

        var linhaSorteada = -1
        var colunaSorteada = 1
        var qtdeMinasAtual = 0

        while (qtdeMinasAtual < this.qtdeMinas) {
            linhaSorteada = gerador.nextInt(qtdeLinhas)
            colunaSorteada = gerador.nextInt(qtdeColunas)

            val campoSorteado = campos[linhaSorteada][colunaSorteada]

            if (campoSorteado.security) {
                campoSorteado.minar()
                qtdeMinasAtual++
            }

        }
    }

    private fun objetivoAlcancado(): Boolean {
        var jogadorGanhou = true;

        forEachCampo { if (!it.objectiveSucess) jogadorGanhou = false }

        return jogadorGanhou;
    }

    private fun verificaDerrotaOuVitoria(campo: Area, evento: EventArea){
        if (evento == EventArea.EXPLOSION){
            callback.forEach{ it(TabuleiroEvento.DERROTA) }
        }else if (objetivoAlcancado()){
            callback.forEach { it(TabuleiroEvento.VITORIA) }
        }
    }

    fun forEachCampo(callback: (Area) -> Unit) {
        campos.forEach { linha -> linha.forEach(callback) }
    }

    fun onEvento ( callback: (TabuleiroEvento) -> Unit){
        this.callback.add(callback)
    }

    fun reiniciar(){
        forEachCampo { it.reestart() }
        sortearMinas();
    }

}




































































