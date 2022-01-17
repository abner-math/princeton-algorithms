import java.util.Iterator;
import java.util.NoSuchElementException;

import edu.princeton.cs.algs4.StdRandom;

public class RandomizedQueue<Item> implements Iterable<Item> {

	private final Deque<Item> deque;
	
    // construct an empty randomized queue
    public RandomizedQueue() {
    	this.deque = new Deque<Item>();
    }

    // is the randomized queue empty?
    public boolean isEmpty() {
    	return this.deque.isEmpty();
    }

    // return the number of items on the randomized queue
    public int size() {
    	return this.deque.size();
    }

    // add the item
    public void enqueue(Item item) {
    	if (item == null) {
    		throw new IllegalArgumentException("Item cannot be null");
    	}
    	
    	if (StdRandom.uniform(2) == 0) {
    		this.deque.addFirst(item);
    	} else {
    		this.deque.addLast(item);
    	}
    }

    // remove and return a random item
    public Item dequeue() {
    	if (this.isEmpty()) {
    		throw new NoSuchElementException();
    	}
    	
    	if (StdRandom.uniform(2) == 0) {
    		return this.deque.removeFirst();
    	} else {
    		return this.deque.removeLast();
    	}
    }

    // return a random item (but do not remove it)
    public Item sample() {
    	if (this.isEmpty()) {
    		throw new NoSuchElementException();
    	}
    	
    	if (StdRandom.uniform(2) == 0) {
    		Item first = this.deque.removeFirst();
    		this.deque.addFirst(first);
    		return first;
    	} else {
    		Item last = this.deque.removeLast();
    		this.deque.addLast(last);
    		return last;
    	}
    }

    // return an independent iterator over items in random order
    public Iterator<Item> iterator() {
    	return new RandomizedQueueIterator<>(this.deque);
    }

    // unit testing (required)
    public static void main(String[] args) {
    	RandomizedQueue<Integer> queue = new RandomizedQueue<>();
    	for (var i = 0; i < 10; i++) {
    		queue.enqueue(i);
    	}
    	
    	for (var i = 0; i < 10; i++) {
    		System.out.println(queue.sample());
    	}
    	
    	for (int i : queue) {
    		System.out.println(i);
    	}
    }

}

class RandomizedQueueIterator<Item> implements Iterator<Item> {

	private final Item[] items;
	private int current;
	
	public RandomizedQueueIterator(Deque<Item> deque) {
		this.items = (Item[])new Object[deque.size()];
		this.current = 0;
		int count = 0;
		for (Item item : deque) {
			this.items[count++] = item;
		}
		
		// shuffle array
		for (int i = this.items.length - 1; i >= 0; i--) {
			int index = StdRandom.uniform(i + 1);
			Item it = this.items[index];
			this.items[index] = this.items[i];
			this.items[i] = it;
		}
	}
	
	@Override
	public boolean hasNext() {
		return this.current < this.items.length - 1;
	}

	@Override
	public Item next() {
		if (!this.hasNext()) {
			throw new NoSuchElementException();
		}
		
		return this.items[this.current++];
	}
	
	@Override
	public void remove() {
		throw new UnsupportedOperationException();
	}
}