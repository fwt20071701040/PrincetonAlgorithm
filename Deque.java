import java.util.Iterator;
import java.util.NoSuchElementException;

public class Deque<Item> implements Iterable<Item> {

	private Node first;
	private Node last;
	private int count;
	public Deque() {

		first = null;
		last = null;
		count = 0;
	} // construct an empty deque
	private class Node {

		Item item;
		Node next;
		Node previous;
		private Node() {

			item = null;
			next = null;
			previous = null;
		}
	}
	public boolean isEmpty() {

		return first == null || last == null;
	} // is the deque empty?
	public int size() {

		return count;
	} // return the number of items on the deque
	private void checkAddValid(Item item) {

		if (null == item) {

			throw new IllegalArgumentException();
		}
	}
	private void checkRemoveValid() {

		if (isEmpty()) {

			throw new NoSuchElementException();
		}
	}
	private void addToEmptyQueue(Item item) {

		first = new Node();
		first.item = item;
		last = first;
	}
	public void addFirst(Item item) {

		checkAddValid(item);
		if (count == 0) {

			addToEmptyQueue(item);
		} else {

			Node oldFirst = first;
			first = new Node();
			first.item = item;
			first.next = oldFirst;
			oldFirst.previous = first;
		}
		count++;
	} // add the item to the front
	public void addLast(Item item) {

		checkAddValid(item);
		if (count == 0) {

			addToEmptyQueue(item);
		} else {

			Node oldLast = last;
			last = new Node();
			last.item = item;
			oldLast.next = last;
			last.previous = oldLast;
		}
		count++;
	} // add the item to the end

	public Item removeFirst() {

		checkRemoveValid();
		Item item = first.item;
		if (count > 1) {

			Node oldFirst = first;
			first = first.next;
			first.previous = null;
			oldFirst = null;
		} else {

			first = null;
			last = null;
		}
		count--;
		return item;
	} // remove and return the item from the front
	public Item removeLast() {

		checkRemoveValid();
		Item item = last.item;
		if (count > 1) {

			Node oldLast = last;
			last = last.previous;
			last.next = null;
			oldLast = null;
		} else {

			first = null;
			last = null;
		}
		count--;
		return item;
	} // remove and return the item from the end
	@Override
	public Iterator<Item> iterator() {

		return new ListIterator();
	} // return an iterator over items in order from front to end
	private class ListIterator implements Iterator<Item> {

		private Node current = first;
		@Override
		public boolean hasNext() {

			return current != null;
		}

		@Override
		public Item next() {

			if (!hasNext()) {

				throw new NoSuchElementException(); 
			}
			Item item = current.item;
			current = current.next;
			return item;
		}

		@Override
		public void remove() {

			throw new UnsupportedOperationException();
		}
	}
	public static void main(String[] args) {

		Deque<String> deque = new Deque<String>();
		deque.addFirst("Hello");
		deque.addFirst("World");
		deque.addFirst("good");
		deque.addLast("bye");
		//		deque.addFirst(null);
		deque.removeFirst();
		deque.removeLast();
		deque.removeFirst();
		deque.removeLast();
		//		deque.iterator().remove();
		Iterator<String> iterator = deque.iterator();
		while (iterator.hasNext()) {

			System.out.println(iterator.next());
		};
		//		iterator.next();
	} // unit testing (optional)
}