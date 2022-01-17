import java.util.Iterator;
import java.util.NoSuchElementException;

public class Deque<Item> implements Iterable<Item> {

	private LinkedListNode<Item> head;
	private LinkedListNode<Item> tail;
	private int count;
	
    // construct an empty deque
    public Deque() {
    }

    // is the deque empty?
    public boolean isEmpty() {
    	return this.count == 0;
    }

    // return the number of items on the deque
    public int size() {
    	return this.count;
    }

    // add the item to the front
    public void addFirst(Item item) {
    	if (item == null) {
    		throw new IllegalArgumentException("Item cannot be null");
    	}
    	
    	LinkedListNode<Item> previousHead = this.head;
    	this.head = new LinkedListNode<>(item);
    	this.head.SetNext(previousHead);
    	if (previousHead != null) {
        	previousHead.SetPrevious(this.head);
    	}
    	
    	++this.count;
    	if (this.count == 1) {
    		this.tail = this.head;
    	}
    }

    // add the item to the back
    public void addLast(Item item) {
    	if (item == null) {
    		throw new IllegalArgumentException("Item cannot be null");
    	}
    	
    	LinkedListNode<Item> previousTail = this.tail;
    	this.tail = new LinkedListNode<>(item);
    	this.tail.SetPrevious(previousTail);
    	if (previousTail != null) {
    		previousTail.SetNext(this.tail);
    	}
    	
    	++this.count;
    	if (this.count == 1) {
    		this.head = this.tail;
    	}
    }

    // remove and return the item from the front
    public Item removeFirst() {
    	if (this.count == 0) {
    		throw new NoSuchElementException();
    	}
    	
    	Item item = this.head.GetData();
    	this.head = this.head.GetNext();
    	if (this.head != null) {
        	this.head.SetPrevious(null);
    	}
    	
    	--this.count;
    	if (this.count <= 1) {
    		this.tail = this.head;
    	}
    	
    	return item;
    }

    // remove and return the item from the back
    public Item removeLast() {
    	if (this.count == 0) {
    		throw new NoSuchElementException();
    	}
    	
    	Item item = this.tail.GetData();
    	this.tail = this.tail.GetPrevious();
    	if (this.tail != null) {
        	this.tail.SetNext(null);
    	}
    	
    	--this.count;
    	if (this.count <= 1) {
    		this.head = this.tail;
    	}
    	
    	return item;
    }

    // return an iterator over items in order from front to back
    public Iterator<Item> iterator() {
    	return new LinkedListNodeIterator<>(this.head);
    }
    
    // unit testing (required)
    public static void main(String[] args) {
    	var queue = new Deque<Integer>();
    	queue.addFirst(1);
    	queue.addFirst(2);
    	queue.addFirst(3);
    	queue.addLast(4);
    	queue.addLast(5);
    	queue.addLast(6);
    	queue.removeFirst();
    	queue.removeLast();
    	for (int value : queue) {
    		System.out.println(value);
    	}
    }

}

class LinkedListNode<Item> {

	private final Item data;
	private LinkedListNode<Item> next;
	private LinkedListNode<Item> previous;
	
	public LinkedListNode(Item data) {
		this.data = data;
	}
	
	public LinkedListNode<Item> GetNext() {
		return this.next;
	}
	
	public void SetNext(LinkedListNode<Item> next) {
		this.next = next;
	}
	
	public LinkedListNode<Item> GetPrevious() {
		return this.previous;
	}
	
	public void SetPrevious(LinkedListNode<Item> previous) {
		this.previous = previous;
	}
	
	public Item GetData() {
		return this.data;
	}
}

class LinkedListNodeIterator<Item> implements Iterator<Item> {

	private LinkedListNode<Item> current;
	
	public LinkedListNodeIterator(LinkedListNode<Item> head) {
		this.current = head;
	}
	
	@Override
	public boolean hasNext() {
		return this.current != null;
	}

	@Override
	public Item next() {
		if (!this.hasNext()) {
			throw new NoSuchElementException();
		}
		
		Item item = this.current.GetData();
		this.current = this.current.GetNext();
		return item;
	}
	
	@Override
	public void remove() {
		throw new UnsupportedOperationException();
	}
	
}