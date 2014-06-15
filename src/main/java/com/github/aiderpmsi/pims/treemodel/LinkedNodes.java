package com.github.aiderpmsi.pims.treemodel;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.concurrent.locks.ReentrantLock;

public class LinkedNodes {

	private HashMap<Node<?>, LinkedNode> linkedNodes = new HashMap<>();

	private LinkedNode first = null;

	private LinkedNode last = null;

	private ReentrantLock sharedLock = new ReentrantLock();

	public void addLast(Node<?> element) {
		try {
			sharedLock.lock();
			if (linkedNodes.containsKey(element)) {
				throw new IllegalArgumentException("Inserted element "
						+ element.toString() + " already exists");
			} else {
				// CREATES THE LINKEDNODE
				LinkedNode linkedNode = new LinkedNode();
				linkedNode.current = element;

				// ADDS THIS LINKED NODE TO THE HASHSET
				linkedNodes.put(element, linkedNode);

				// CREATES THE LINKS
				if (first == null) {
					first = last = linkedNode;
				} else {
					linkedNode.previous = last;
					last.next = linkedNode;
					last = linkedNode;
				}

			}
		} finally {
			sharedLock.unlock();
		}
	}

	public void addFirst(Node<?> element) {
		try {
			sharedLock.lock();
			if (linkedNodes.containsKey(element)) {
				throw new IllegalArgumentException("Inserted element "
						+ element.toString() + " already exists");
			} else {
				// CREATES THE LINKEDNODE
				LinkedNode linkedNode = new LinkedNode();
				linkedNode.current = element;

				// ADDS THIS LINKED NODE TO THE HASHSET
				linkedNodes.put(element, linkedNode);

				// CREATES THE LINKS
				if (first == null) {
					first = last = linkedNode;
				} else {
					linkedNode.next = first;
					first.previous = linkedNode;
					first = linkedNode;
				}

			}
		} finally {
			sharedLock.unlock();
		}
	}

	public void clear() {
		try {
			sharedLock.lock();
			// DETACH EACH ELEMENT
			LinkedNode current = first;
			LinkedNode next;
			// REMOVE EVERYTHING OF NODE
			linkedNodes.clear();
			while (current != null) {
				// STORE POSITION OF NEXT ELEMENT
				next = current.next;
				current.previous = null;
				current.next = null;
				current.current = null;
				// DO IT FOR NEXT ELEMENT
				current = next;
			}
			first = last = null;
		} finally {
			sharedLock.unlock();
		}
	}

	public boolean contains(Object o) {
		try {
			sharedLock.lock();
			return linkedNodes.containsKey(o);
		} finally {
			sharedLock.unlock();
		}
	}

	public boolean containsAll(Collection<?> c) {
		try {
			sharedLock.lock();
			for (Object element : c) {
				if (!linkedNodes.containsKey(element)) {
					return false;
				}
			}
			return true;
		} finally {
			sharedLock.unlock();
		}
	}

	public final Node<?> get(int index) {
		try {
			sharedLock.lock();
			if (index < 0 || index >= linkedNodes.size()) {
				throw new IndexOutOfBoundsException("Index " + index
						+ " is greater than nodes list size of " + linkedNodes.size());
			} else {
				LinkedNode current = first;
				for (int i = 0; i < index; i++) {
					current = current.next;
				}
				return current.current;
			}
		} finally {
			sharedLock.unlock();
		}
	}

	public int indexOf(Object o) {
		try {
			sharedLock.lock();
			if (!linkedNodes.containsKey(o)) {
				return -1;
			} else {
				LinkedNode current = first;
				for (int i = 0; i < linkedNodes.size(); i++) {
					if (current.current.equals(o)) {
						return i;
					} else {
						current = current.next;
					}
				}
			}
			return -1;
		} finally {
			sharedLock.unlock();
		}
	}

	public boolean isEmpty() {
		try {
			sharedLock.lock();
			return linkedNodes.size() == 0 ? true : false;
		} finally {
			sharedLock.unlock();
		}
	}

	public Iterator<Node<?>> iterator(boolean asc) {
		try {
			sharedLock.lock();
			return asc ? new LinkedNodesIterator(asc, first, linkedNodes, sharedLock)
					: new LinkedNodesIterator(asc, last, linkedNodes, sharedLock);
		} finally {
			sharedLock.unlock();
		}
	}

	public Iterator<Node<?>> iterator(boolean asc, Node<?> from) {
		try {
			sharedLock.lock();
			if (!linkedNodes.containsKey(from)) {
				throw new IllegalArgumentException("Node " + from.toString()
						+ " doesn't exists");
			} else {
				return new LinkedNodesIterator(asc, linkedNodes.get(from),
						linkedNodes, sharedLock);
			}
		} finally {
			sharedLock.unlock();
		}
	}

	public int lastIndexOf(Object o) {
		try {
			sharedLock.lock();
			if (!linkedNodes.containsKey(o)) {
				return -1;
			} else {
				LinkedNode current = last;
				for (int i = linkedNodes.size() - 1; i >= 0; i--) {
					if (current.current.equals(o)) {
						return i;
					} else {
						current = current.previous;
					}
				}
			}
			return -1;
		} finally {
			sharedLock.unlock();
		}
	}
	
	public final Node<?> getFirst() {
		try {
			sharedLock.lock();
			if (linkedNodes.size() == 0) {
				return null;
			} else {
				return first.current;
			}
		} finally {
			sharedLock.unlock();
		}
	}

	public final Node<?> getLast() {
		try {
			sharedLock.lock();
			if (linkedNodes.size() == 0) {
				return null;
			} else {
				return last.current;
			}
		} finally {
			sharedLock.unlock();
		}
	}

	public int size() {
		return linkedNodes.size();
	}
	
}
