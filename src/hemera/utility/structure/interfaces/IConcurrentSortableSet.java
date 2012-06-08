package hemera.utility.structure.interfaces;

/**
 * <code>IConcurrentSortableSet</code> defines a data
 * structure implementation that maintains its contents
 * in their natural order, with concurrency support.
 * All operations are thread-safe, while all read-only
 * methods only hold read-lock, thus fully concurrent,
 * and write methods hold write-lock.
 * <p>
 * The <code>T</code> node implementation must define
 * its comparison logic such that when a node is
 * compared against itself, the method must return 0,
 * and when a node is compared against a different node
 * with the same compared value, the method must then
 * return -1 instead. The following is an example of
 * how the <code>compareTo</code> method should be
 * implemented.
 * <p>
 * <code>
 * public int compareTo(final Node o) { <p>
 * 		// If it is the same node, return 0. <p>
 *		if (this.equals(o)) return 0; <p>
 *		// Compare node values. <p>
 *		final int result = this.value - o.value; <p>
 *		// If values are the same, do not return 0, but -1. <p>
 *		if (result == 0) return -1; <p>
 *		else return result; <p>
 * }
 * </code>
 * @param K The key object type.
 * @param T The node <code>Comparable</code> type.
 * @param V The attachment for the node.
 *
 * @author Yi Wang (Neakor)
 * @version 1.0.0
 */
public interface IConcurrentSortableSet<K, T extends Comparable<T>, V> {

	/**
	 * Add the given node to the set if the set does
	 * not already contain a node that equals given
	 * one. Use the given key for node removal, and
	 * associate given attachment to the node.
	 * <p>
	 * Two nodes are equal if <code>n1.equals(n2)</code>
	 * or <code>n1.compareTo(n2) == 0</code>, where
	 * n1 and n2 are two instances of node.
	 * <p>
	 * This method does not compare using the key but
	 * rather the equals comparison is done using the
	 * node, since sorting is based on the node.
	 * <p>
	 * This method only holds a read-lock thus allowing
	 * concurrent invocations of addition.
	 * @param key The <code>K</code> key can be used to
	 * remove the node.
	 * @param node The <code>T</code> to be added.
	 * @param attachment The <code>V</code> attachment.
	 * @return <code>true</code> if node is successfully
	 * added to the set. <code>false</code> if the set
	 * already contains a node that equals the given one.
	 */
	public boolean add(final K key, final T node, final V attachment);
	
	/**
	 * Remove the node associated with given key.
	 * <p>
	 * The node trying to remove must define its logic
	 * of comparison in a way such that when the same
	 * node is compared against itself, the result is
	 * <code>0</code>.
	 * <p>
	 * This method only holds a read-lock thus allowing
	 * concurrent invocations of removal.
	 * @param key The <code>K</code> key of the node
	 * to remove.
	 * @return <code>true</code> if the set contains
	 * a node associated with given key and the node
	 * is removed. <code>false</code> otherwise.
	 */
	public boolean remove(final K key);
	
	/**
	 * Sort the entire set based on the current ordering
	 * state of the contained nodes.
	 * <p>
	 * This method should be used when a portion of the
	 * nodes added to the set have changed their order-
	 * state, thus making the set order no longer valid.
	 * <p>
	 * This method holds the write-lock, thus concurrent
	 * invocations are not supported. While invoking this
	 * method, all other read-operations are suspended.
	 * This operation is relatively expensive thus should
	 * only be done when a portion of contained nodes
	 * have changed their sorting state.
	 */
	public void sort();
	
	/**
	 * Retrieve the first (lowest) node in the set.
	 * <p>
	 * This method only holds a read-lock thus allowing
	 * concurrent invocations of retrieval.
	 * @return The <code>T</code> lowest node.
	 * <code>null</code> if no entries in the set.
	 */
	public T firstNode();
	
	/**
	 * Retrieve the last (highest) node in the set.
	 * <p>
	 * This method only holds a read-lock thus allowing
	 * concurrent invocations of retrieval.
	 * @return The <code>T</code> highest node.
	 * <code>null</code> if no entries in the set.
	 */
	public T lastNode();
	
	/**
	 * Retrieve the attachment of the first (lowest)
	 * node in the set.
	 * <p>
	 * This method only holds a read-lock thus allowing
	 * concurrent invocations of retrieval.
	 * @return The <code>V</code> attachment of the
	 * lowest node. <code>null</code> if no entries
	 * in the set.
	 */
	public V firstAttachment();

	/**
	 * Retrieve the attachment of the last (highest)
	 * node in the set.
	 * <p>
	 * This method only holds a read-lock thus allowing
	 * concurrent invocations of retrieval.
	 * @return The <code>V</code> attachment of the
	 * highest node. <code>null</code> if no entries
	 * in the set.
	 */
	public V lastAttachment();
	
	/**
	 * Retrieve the number of nodes contained in set.
	 * <p>
	 * This is a constant time operation but may not
	 * reflect the most up-to-date value, though the
	 * memory consistency of the returned value is
	 * guaranteed.
	 * @return The <code>int</code> number of nodes.
	 */
	public int size();

	/**
	 * Retrieve the node associated with given key.
	 * <p>
	 * This method only holds a read-lock thus allowing
	 * concurrent invocations of removal.
	 * @param key The <code>K</code> key of the node
	 * to retrieve.
	 * @return The <code>T</code> node associated with
	 * given key, <code>null</code> if no such key.
	 */
	public T getNode(final K key);

	/**
	 * Retrieve the attachment of the node associated
	 * with given key.
	 * <p>
	 * This method only holds a read-lock thus allowing
	 * concurrent invocations of removal.
	 * @param key The <code>K</code> key of the node
	 * to check.
	 * @return The <code>V</code> attachment of node.
	 */
	public V getAttachment(final K key);
	
	/**
	 * Retrieve all the contained keys.
	 * @return The <code>Iterable</code> of all the
	 * <code>K</code> keys.
	 */
	public Iterable<K> getAllKeys();
}
