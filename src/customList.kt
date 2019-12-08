import java.lang.IllegalStateException

sealed class customList <T> {
    companion object{
        operator fun <T> invoke (vararg values : T) : customList<T>{
            var empty = Nil as customList<T>
            val res = values.foldRight(empty , {v , l -> l.addFirst(v) })
            return res
        }
    }

    class Node <T> (val head : T, val tail : customList <T>) : customList<T> (){
        override fun toString() = "${head.toString()} , ${tail.toString()}"
    }

    object Nil : customList<Nothing> () {
        override fun toString() = "NIL"
    }

    fun addFirst (head : T) : customList<T> = Node (head, this)

    fun removeFirst() : customList<T> = when(this){
        is Nil -> throw IllegalStateException()
        is Node<T> ->  this.tail
    }

}