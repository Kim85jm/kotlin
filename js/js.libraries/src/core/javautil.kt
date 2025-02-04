package java.util

import js.*;


library("collectionsMax")
public fun max<T>(col : Collection<T>, comp : Comparator<T>) : T = js.noImpl

library
public trait Comparator<T> {
    fun compare(obj1 : T, obj2 : T) : Int;
}

library("comparator")
public fun comparator<T>(f : (T, T) -> Int) : Comparator<T> = js.noImpl


library
public open class Iterator<T>() {
    open fun next() : T = js.noImpl
    open fun hasNext() : Boolean = js.noImpl
}

library
val Collections = object {
    library("collectionsMax")
    public fun max<T>(col : Collection<T>, comp : Comparator<T>) : T = js.noImpl
}

library
public open class ArrayList<erased E>() : java.util.List<E> {
    override public fun size() : Int = js.noImpl
    override public fun isEmpty() : Boolean = js.noImpl
    override public fun contains(o : Any?) : Boolean = js.noImpl
    override public fun iterator() : Iterator<E> = js.noImpl
   // override public fun indexOf(o : Any?) : Int = js.noImpl
  //  override public fun lastIndexOf(o : Any?) : Int = js.noImpl
  //  override public fun toArray() : Array<Any?> = js.noImpl
  //  override public fun toArray<erased T>(a : Array<out T>) : Array<T> = js.noImpl
    override public fun get(index : Int) : E = js.noImpl
    override public fun set(index : Int, element : E) : E = js.noImpl
    override public fun add(e : E) : Boolean = js.noImpl
    override public fun add(index : Int, element : E) : Unit = js.noImpl
    override public fun remove(index : Int) : E = js.noImpl
    override public fun remove(o : Any?) : Boolean = js.noImpl
    override public fun clear() : Unit = js.noImpl
    override public fun addAll(c : java.util.Collection<out E>) : Boolean = js.noImpl
  //  override public fun addAll(index : Int, c : java.util.Collection<out E>) : Boolean = js.noImpl
}

library
public trait Collection<erased E> : java.lang.Iterable<E> {
    open fun size() : Int
    open fun isEmpty() : Boolean
    open fun contains(o : Any?) : Boolean
    override fun iterator() : java.util.Iterator<E>
   // open fun toArray() : Array<Any?>
   // open fun toArray<erased T>(a : Array<out T>) : Array<T>
    open fun add(e : E) : Boolean
    open fun remove(o : Any?) : Boolean
    //open fun containsAll(c : java.util.Collection<*>) : Boolean
    open fun addAll(c : java.util.Collection<out E>) : Boolean
    //open fun removeAll(c : java.util.Collection<*>) : Boolean
    //open fun retainAll(c : java.util.Collection<*>) : Boolean
    open fun clear() : Unit
}

library
public trait List<erased E> : Collection<E> {
    override fun size() : Int
    override fun isEmpty() : Boolean
    override fun contains(o : Any?) : Boolean
    override fun iterator() : java.util.Iterator<E>
  //  override fun toArray() : Array<Any?>
    // Simulate Java's array covariance
 //   override fun toArray<erased T>(a : Array<out T>) : Array<T>
    override fun add(e : E) : Boolean
    override fun remove(o : Any?) : Boolean
  //  override fun containsAll(c : java.util.Collection<*>) : Boolean
    override fun addAll(c : java.util.Collection<out E>) : Boolean
   // open fun addAll(index : Int, c : java.util.Collection<out E>) : Boolean
   // override fun removeAll(c : java.util.Collection<*>) : Boolean
   // override fun retainAll(c : java.util.Collection<*>) : Boolean
    override fun clear() : Unit
    open fun get(index : Int) : E
    open fun set(index : Int, element : E) : E
    open fun add(index : Int, element : E) : Unit
    open fun remove(index : Int) : E
   // open fun indexOf(o : Any?) : Int
   // open fun lastIndexOf(o : Any?) : Int
}

library
public trait Set<erased E> : Collection<E> {
    override fun size() : Int
    override fun isEmpty() : Boolean
    override fun contains(o : Any?) : Boolean
    override fun iterator() : java.util.Iterator<E>
  //  override fun toArray() : Array<Any?>
  //  override fun toArray<erased T>(a : Array<out T>) : Array<T>
    override fun add(e : E) : Boolean
    override fun remove(o : Any?) : Boolean
    //override fun containsAll(c : java.util.Collection<*>) : Boolean
    override fun addAll(c : java.util.Collection<out E>) : Boolean
    //override fun retainAll(c : java.util.Collection<*>) : Boolean
    //override fun removeAll(c : java.util.Collection<*>) : Boolean
    override fun clear() : Unit
}

library
public open class HashSet<erased E>() : java.util.Set<E> {
    override public fun iterator() : java.util.Iterator<E> = js.noImpl
    override public fun size() : Int = js.noImpl
    override public fun isEmpty() : Boolean = js.noImpl
    override public fun contains(o : Any?) : Boolean = js.noImpl
    override public fun add(e : E) : Boolean = js.noImpl
    override public fun remove(o : Any?) : Boolean = js.noImpl
    override public fun clear() : Unit = js.noImpl
    override fun addAll(c : java.util.Collection<out E>) : Boolean = js.noImpl
}

library
public trait Map<erased K, erased V> {
    open fun size() : Int
    open fun isEmpty() : Boolean
    open fun containsKey(key : Any?) : Boolean
    open fun containsValue(value : Any?) : Boolean
    open fun get(key : Any?) : V?
    open fun put(key : K, value : V) : V?
    open fun remove(key : Any?) : V?
    open fun putAll(m : java.util.Map<out K, out V>) : Unit
    open fun clear() : Unit
    open fun keySet() : java.util.Set<K>
    open fun values() : java.util.Collection<V>
}

library
public open class HashMap<erased K, erased V>() : java.util.Map<K, V> {
    override public fun size() : Int = js.noImpl
    override public fun isEmpty() : Boolean = js.noImpl
    override public fun get(key : Any?) : V = js.noImpl
    override public fun containsKey(key : Any?) : Boolean = js.noImpl
    override public fun put(key : K, value : V) : V = js.noImpl
    override public fun putAll(m : java.util.Map<out K, out V>) : Unit = js.noImpl
    override public fun remove(key : Any?) : V? = js.noImpl
    override public fun clear() : Unit = js.noImpl
    override public fun containsValue(value : Any?) : Boolean = js.noImpl
    override public fun keySet() : java.util.Set<K> = js.noImpl
    override public fun values() : java.util.Collection<V> = js.noImpl
}

library
public open class LinkedList<erased E>() : List<E> {
    override public fun iterator() : java.util.Iterator<E> = js.noImpl
    override public fun isEmpty() : Boolean = js.noImpl
    override public fun contains(o : Any?) : Boolean = js.noImpl
    override public fun size() : Int = js.noImpl
    override public fun add(e : E) : Boolean = js.noImpl
    override public fun remove(o : Any?) : Boolean = js.noImpl
    override public fun addAll(c : java.util.Collection<out E>) : Boolean = js.noImpl
    override public fun clear() : Unit = js.noImpl
    override public fun get(index : Int) : E = js.noImpl
    override public fun set(index : Int, element : E) : E = js.noImpl
    override public fun add(index : Int, element : E) : Unit = js.noImpl
    override public fun remove(index : Int) : E = js.noImpl
    public fun poll() : E? = js.noImpl
    public fun peek() : E? = js.noImpl
    public fun offer(e : E) : Boolean = js.noImpl
}

library
public class StringBuilder() {
    public fun append(obj : Any) : StringBuilder = js.noImpl
    public fun toString() : String = js.noImpl
}