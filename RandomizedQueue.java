import java.util.Iterator;
import java.util.NoSuchElementException;

import edu.princeton.cs.algs4.StdRandom;

public class RandomizedQueue<Item> implements Iterable<Item> {

	private Object[] items;
	private int N;
	public RandomizedQueue() {

		items = new Object[1];
	} // construct an empty randomized queue
	public boolean isEmpty() {

		return N == 0;
	} // is the randomized queue empty?
	public int size() {

		return N;
	} // return the number of items on the randomized queue
	private void checkEnqueueValid(Item item) {

		if (null == item)
			throw new IllegalArgumentException();
	}

	public void enqueue(Item item) {

		checkEnqueueValid(item);
		if (N == items.length) {

			if (N == 0) {

				items = new Object[1];
			}
			resize(items.length * 2);
		}
		items[N++] = item;
	} // add the item
	private void resize(int capacity) {

		Object[] copy = new Object[capacity];
		for(int i = 0; i < N; i++) {

			copy[i] = items[i];
		}
		items = copy;
	}
	private void emptyQueueValid() {

		if (isEmpty()) {

			throw new NoSuchElementException();
		}
	}
	public Item dequeue() {

		emptyQueueValid();
		int idx = StdRandom.uniform(N);
		Object item = items[idx];
		items[idx] = items[--N];
		items[N] = null;
		if (N == items.length / 4) {

			resize(items.length / 2);
		}
		Item chg = (Item) item;
		return chg;
	} // remove and return a random item
	public Item sample() {

		emptyQueueValid();
		Item chg = (Item) items[StdRandom.uniform(N)];
		return chg;
	} // return a random item (but do not remove it)
	@Override
	public Iterator<Item> iterator() {

		return new ListIterator();
	} // return an independent iterator over items in random order
	private class ListIterator implements Iterator<Item> {

		private int idx;
		private final int[] permu = StdRandom.permutation(N);
		@Override
		public boolean hasNext() {

			return idx < N;
		}

		@Override
		public Item next() {

			if (!hasNext()) {

				throw new NoSuchElementException(); 
			}
			Item chg = (Item) items[permu[idx++]];
			return chg;
		}

		@Override
		public void remove() {

			throw new UnsupportedOperationException(); 
		}
	}

	public static void main(String[] args) {

		RandomizedQueue<String> rq = new RandomizedQueue<String>();
		rq.enqueue("dfdfdf");
		rq.enqueue("iiiii");
		rq.dequeue();
		rq.dequeue();
		Iterator<String> iterator = rq.iterator();
		System.out.println(rq.size());
		while(iterator.hasNext()) {

			System.out.println(iterator.next());
		}
	} // unit testing (optional)
}
