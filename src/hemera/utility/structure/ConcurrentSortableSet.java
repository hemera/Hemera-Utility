package hemera.utility.structure;

import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock.ReadLock;
import java.util.concurrent.locks.ReentrantReadWriteLock.WriteLock;

import hemera.utility.structure.interfaces.IConcurrentSortableSet;

/**
 * <code>ConcurrentSortableSet</code> defines a data
 * structure implementation of a sortable set that
 * supports the read-write lock based concurrency,
 * while fully conforming to its interface definition.
 *
 * @author Yi Wang (Neakor)
 * @version 1.0.0
 */
public class ConcurrentSortableSet<K, T extends Comparable<T>, V> implements IConcurrentSortableSet<K, T, V> {
	/**
	 * The <code>ConcurrentMap</code> of <code>K</code>
	 * key to <code>T</code> node.
	 * <p>
	 * This map is used to allow removal of nodes based
	 * on their associated keys.
	 */
	private final ConcurrentMap<K, T> keymap;
	/**
	 * The <code>ConcurrentSkipListMap</code> of the
	 * <code>T</code> nodes and its <code>V</code>
	 * attachment.
	 * <p>
	 * This set contains all the added nodes sorted
	 * in their natural ordering.
	 */
	private final ConcurrentSkipListMap<T, V> nodemap;
	/**
	 * The <code>ReadLock</code> used to guard all the
	 * read operations from <code>nodemap</code>.
	 */
	private final ReadLock readlock;
	/**
	 * The <code>WriteLock</code> used to guard the
	 * <code>sort</code> operation since the remove-
	 * add operation needs to be atomic, so no other
	 * concurrent read operation can occur to return
	 * invalid values.
	 */
	private final WriteLock writelock;
	/**
	 * The <code>AtomicInteger</code> used to count
	 * the number of nodes in this set.
	 * <p>
	 * Using this field to allow a constant time of
	 * size counting instead of using <code>size</code>
	 * method on either of the maps.
	 * <p>
	 * This field should be guarded by the same sort
	 * read-write lock to allow <code>sort</code> to
	 * obtain a most up-to-date value.
	 */
	private final AtomicInteger count;
	
	/**
	 * Constructor of <code>ConcurrentSortableSet</code>.
	 */
	public ConcurrentSortableSet() {
		this.keymap = new ConcurrentHashMap<K, T>();
		this.nodemap = new ConcurrentSkipListMap<T, V>();
		final ReentrantReadWriteLock lock = new ReentrantReadWriteLock();
		this.readlock = lock.readLock();
		this.writelock = lock.writeLock();
		this.count = new AtomicInteger();
	}

	@Override
	public boolean add(final K key, final T node, final V attachment) {
		// Early check using key map.
		final T prev = this.keymap.putIfAbsent(key, node);
		if (prev != null) return false;
		// Read-lock to prevent concurrent addition and sorting.
		this.readlock.lock();
		try {
			final V prevattachment = this.nodemap.putIfAbsent(node, attachment);
			// If comparison failed, remove from keymap.
			if (prevattachment != null) {
				this.keymap.remove(key);
				return false;
			}
			this.count.incrementAndGet();
			return true;
		} finally {
			this.readlock.unlock();
		}
	}
	
	@Override
	public boolean remove(final K key) {
		// Early check using key map.
		final T node = this.keymap.remove(key);
		if (node == null) return false;
		// Read-lock to prevent concurrent removal and sorting.
		this.readlock.lock();
		try {
			final V attachment = this.nodemap.remove(node);
			// If comparison failed, add back to keymap.
			if (attachment == null) {
				this.keymap.put(key, node);
				return false;
			}
			this.count.decrementAndGet();
			return true;
		} finally {
			this.readlock.unlock();
		}
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public void sort() {
		// Write lock to provide mutual exclusion, and
		// prevent concurrent addition and removal.
		this.writelock.lock();
		try {
			// Using count to determine when to stop,
			// and since count is guarded by the same
			// read-write lock for add and remove, the
			// value is guaranteed to be up-to-date.
			final int size = this.count.get();
			// Dump all nodes into an array, then add
			// all back into nodemap for sorting. This
			// guarantees that all nodes sorted.
			final Object[] array = new Object[size];
			for (int i = 0; i < size; i++) {
				final Entry<T, V> entry = this.nodemap.pollFirstEntry();
				array[i] = entry;
			}
			for (int i = 0; i < size; i++) {
				final Entry<T, V> entry = (Entry<T, V>)array[i];
				this.nodemap.put(entry.getKey(), entry.getValue());
			}
		} finally {
			this.writelock.unlock();
		}
	}
	
	@Override
	public T firstNode() {
		this.readlock.lock();
		try {
			return this.nodemap.firstKey();
		} finally {
			this.readlock.unlock();
		}
	}
	
	@Override
	public T lastNode() {
		this.readlock.lock();
		try {
			return this.nodemap.lastKey();
		} finally {
			this.readlock.unlock();
		}
	}
	
	@Override
	public V firstAttachment() {
		this.readlock.lock();
		try {
			final Entry<T, V> entry = this.nodemap.firstEntry();
			if (entry == null) return null;
			return entry.getValue();
		} finally {
			this.readlock.unlock();
		}
	}

	@Override
	public V lastAttachment() {
		this.readlock.lock();
		try {
			final Entry<T, V> entry = this.nodemap.lastEntry();
			if (entry == null) return null;
			return entry.getValue();
		} finally {
			this.readlock.unlock();
		}
	}

	@Override
	public int size() {
		return this.count.get();
	}

	@Override
	public T getNode(final K key) {
		this.readlock.lock();
		try {
			return this.keymap.get(key);
		} finally {
			this.readlock.unlock();
		}
	}

	@Override
	public V getAttachment(final K key) {
		this.readlock.lock();
		try {
			final T node = this.keymap.get(key);
			return this.nodemap.get(node);
		} finally {
			this.readlock.unlock();
		}
	}

	@Override
	public Iterable<K> getAllKeys() {
		return this.keymap.keySet();
	}
}
