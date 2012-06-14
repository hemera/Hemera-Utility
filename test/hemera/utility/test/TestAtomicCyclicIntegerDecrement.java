package hemera.utility.test;

import hemera.utility.structure.AtomicCyclicInteger;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;

public class TestAtomicCyclicIntegerDecrement {

	public static void main(String[] args) throws InterruptedException {
		final AtomicCyclicInteger value = new AtomicCyclicInteger(0, 5);
		final AtomicInteger count0 = new AtomicInteger();
		final AtomicInteger count1 = new AtomicInteger();
		final AtomicInteger count2 = new AtomicInteger();
		final AtomicInteger count3 = new AtomicInteger();
		final AtomicInteger count4 = new AtomicInteger();
		final AtomicInteger count5 = new AtomicInteger();
		
		final int threadcount = 1000;
		final CountDownLatch startlatch = new CountDownLatch(1);
		final CountDownLatch finishlatch = new CountDownLatch(threadcount);
		for (int i = 0; i < threadcount; i++) {
			final Thread thread = new Thread(new Runnable() {
				@Override
				public void run() {
					try {
						startlatch.await();
						final int v = value.getAndDecrement();
						if (v == 0) count0.incrementAndGet();
						else if (v == 1) count1.incrementAndGet();
						else if (v == 2) count2.incrementAndGet();
						else if (v == 3) count3.incrementAndGet();
						else if (v == 4) count4.incrementAndGet();
						else if (v == 5) count5.incrementAndGet();
						else {
							System.err.println("Value out of range: " + v);
						}
						finishlatch.countDown();
					} catch (final Exception e) {
						e.printStackTrace();
					}
				}
			});
			thread.start();
		}
		System.out.println("Start!");
		startlatch.countDown();
		
		finishlatch.await();
		System.out.println("0 count: " + count0.get());
		System.out.println("1 count: " + count1.get());
		System.out.println("2 count: " + count2.get());
		System.out.println("3 count: " + count3.get());
		System.out.println("4 count: " + count4.get());
		System.out.println("5 count: " + count5.get());
	}
}
