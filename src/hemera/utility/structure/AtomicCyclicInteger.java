package hemera.utility.structure;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * <code>AtomicCyclicInteger</code> defines a data
 * structure that is an <code>AtomicInteger</code>
 * but has a defined minimum and maximum value that
 * if the current value is at the minimum, a
 * decrement operation will set the value to the
 * defined maximum. And if the current value is at
 * the maximum, an increment will set the value to
 * the defined minimum.
 *
 * @author Yi Wang (Neakor)
 * @version 1.0.0
 */
public class AtomicCyclicInteger {
	/**
	 * The <code>AtomicInteger</code> instance.
	 */
	private final AtomicInteger value;
	/**
	 * The <code>int</code> minimum value.
	 */
	private final int min;
	/**
	 * The <code>int</code> maximum value.
	 */
	private final int max;
	
	/**
	 * Constructor of <code>AtomicCyclicInteger</code>.
	 * <p>
	 * This constructor sets the initial value to the
	 * specified minimum.
	 * @param min The <code>int</code> minimum value.
	 * @param max The <code>int</code> maximum value.
	 */
	public AtomicCyclicInteger(final int min, final int max) {
		this.value = new AtomicInteger(min);
		this.min = min;
		this.max = max;
	}
	
	/**
	 * Retrieve the current value and increment the
	 * value by 1 atomically. If the current value
	 * is the maximum, after this operation, the value
	 * will be at the minimum.
	 * @return The <code>int</code> current value
	 * before the increment.
	 */
	public int getAndIncrement() {
		final int oldvalue = this.value.getAndIncrement();
		if (oldvalue > this.max) {
			this.value.set(this.min);
			return this.getAndIncrement();
		} else {
			return oldvalue;
		}
	}
	
	/**
	 * Retrieve the current value and decrement the
	 * value by 1 atomically. If the current value
	 * is the minimum, after this operation, the value
	 * will be at the maximum.
	 * @return The <code>int</code> current value
	 * before the decrement.
	 */
	public int getAndDecrement() {
		final int oldvalue = this.value.getAndDecrement();
		if (oldvalue < this.min) {
			this.value.set(this.max);
			return this.getAndDecrement();
		} else {
			return oldvalue;
		}
	}
	
	/**
	 * Retrieve the current value.
	 * @return The <code>int</code> current value.
	 */
	public int get() {
		return this.value.get();
	}
}
