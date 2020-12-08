package Model

import java.awt.Event

enum class EventArea {
    OPEN,
    MARKED,
    DISMARKED,
    EXPLOSION,
    REESTART
}

data class Area(val row: Int, val column: Int) {

    private val parents = ArrayList<Area>()
    private val callbacks = ArrayList<(Area, EventArea) -> Unit>()

    var marked: Boolean = false;
    var open: Boolean = false;
    var undermined: Boolean = false;

    val dismarked: Boolean get() = !marked
    val closed: Boolean get() = !open
    val security: Boolean get() = !undermined
    val objectiveSucess: Boolean get() = security && open || undermined && marked
    val amountParetsMined: Int get() = parents.filter { it.undermined }.size
    val parentsSecurity: Boolean get() = parents.map { it.security }.reduce { result, security -> result && security }

    fun addParent(parent: Area) {
        parents.add(parent)
    }

    fun onEvent(callback: (Area, EventArea) -> Unit) {
        callbacks.add(callback)
    }

    fun open() {
        if (closed) {
            open = true
            if (undermined) {
                callbacks.forEach { it(this, EventArea.EXPLOSION) }
            } else {
                callbacks.forEach { it(this, EventArea.OPEN) }
                parents.filter { it.closed && it.security && parentsSecurity }.forEach{ it.open() }
            }
        }
    }

    fun alterSelected(){
        if(closed){
            marked = !marked
            val event = if(marked) EventArea.MARKED else EventArea.DISMARKED
            callbacks.forEach{
                it(this, event)
            }
        }
    }

    fun minar(){
        undermined = true
    }

    fun reestart(){
        open = false
        undermined = false
        marked = false
        callbacks.forEach{ it(this, EventArea.REESTART)}
    }

}