import java.lang.IllegalStateException

fun main() {

    var someList = customList<Int>()

    someList = someList.addFirst(5)
    someList = someList.addFirst(6)
    someList = someList.addFirst(8)
    someList = someList.addFirst(3)
    someList = someList.addFirst(20)

    var any = fold(someList, false, {b1 : Boolean, i1 : Int -> i1>8||b1})
    println(any)



    var productList = customList<Product>()

    productList = productList.addFirst(Product("Apfel", 3.99, 4))
    productList = productList.addFirst(Product("Banane", 4.50, 8))
    productList = productList.addFirst(Product("Milch", 1.20, 0))
    productList = productList.addFirst(Product("Stuhl", 19.99, 2))
    productList = productList.addFirst(Product("TV", 199.99, 7))

    var namenListe = map(productList, {it.productName})
    println(namenListe)

    var teureProdukte = map(filter(productList, {it.price<10}), {it.productName})
    println("Teure Produkte: $teureProdukte")

    var produkte19 = map(productList, {Product(it.productName, it.price * 1.19, it.rating)})
    map(produkte19, {print("${it.productName}: ${it.price}, ")})
    print("\n")

    var guteProdukte = filter(productList, {it.rating<3})
    println(map(guteProdukte, {it.productName}))

    var mitAngebot = replaceIf(productList, {Product("Angebot: ${it.productName}", it.price, it.rating)},
                                            {it.productName.startsWith("A")||it.productName.startsWith("B")})
    println(map(mitAngebot, {it.productName}))

    var teurer100 = any(productList, {it.price>100})
    println("Produkt teurer als 100: $teurer100")

    var summe = fold(productList, 0.0, {d1 : Double, p1 : Product -> p1.price+d1})
    println("Summe: $summe")

    var hochPreis = fold(productList, 0.0, {d1 : Double, p1 : Product -> if(d1>p1.price) return@fold d1 else return@fold p1.price})
    println("Höchstpreis: $hochPreis")

    val kaeseFactory = productFactory("Käse", 5)
    val teurerKaese = kaeseFactory(4.99)
    val billigerKaese = kaeseFactory(0.99)

    println("Teurer Käse: ${teurerKaese.price}, Billiger Käse: ${billigerKaese.price}")
}

fun <T, R> map (list : customList<T> , f : (T) -> R) : customList<R>{
    var newList = customList<R>()
    var tail = list

    while (tail !is customList.Nil){
        when(tail){
            is customList.Nil -> throw IllegalStateException()
            is customList.Node<T> -> newList = newList.addFirst(f(tail.head))
        }

        tail = tail.removeFirst()
    }

    return newList
}

fun <T> replaceIf (list : customList<T>, f : (T) -> T, p : (T) -> Boolean) : customList<T>{
    var newList = customList<T>()
    var tail = list

    while (tail !is customList.Nil){
        when(tail){
            is customList.Nil -> throw IllegalStateException()
            is customList.Node<T> -> if(p(tail.head))newList = newList.addFirst(f(tail.head))
        }

        tail = tail.removeFirst()
    }

    return newList
}

fun <T> filter (list : customList<T>, p : (T) -> Boolean) : customList<T>{
    var newList = customList<T>()
    var tail = list

    while (tail !is customList.Nil){
        when(tail){
            is customList.Nil -> throw IllegalStateException()
            is customList.Node<T> -> if(!p(tail.head)) newList = newList.addFirst(tail.head)
        }

        tail = tail.removeFirst()
    }

    return newList
}

fun <T> anyInterativ (list : customList<T>, p : (T) -> Boolean) : Boolean{
    var tail = list

    while (tail !is customList.Nil) {
        when(tail){
            is customList.Nil -> throw IllegalStateException()
            is customList.Node<T> -> if(p(tail.head)) return true
        }

        tail = tail.removeFirst()
    }

    return false
}


fun <T> any (list : customList<T>, p : (T) -> Boolean) : Boolean {
    when(list){
        is customList.Nil -> return false
        is customList.Node<T> -> if(p(list.head)) return true
    }

    return any(list.removeFirst(), p)
}

fun <T, R> fold(list : customList<T>, accumulated : R, f : (R, T) -> R) : R{
    when(list){
        is customList.Nil -> return accumulated
        is customList.Node<T> -> return fold(list.removeFirst(), f(accumulated, list.head), f)
    }
}

fun productFactory (produktName : String, rating : Int) : (Double) -> Product = {Product(produktName, it, rating)}