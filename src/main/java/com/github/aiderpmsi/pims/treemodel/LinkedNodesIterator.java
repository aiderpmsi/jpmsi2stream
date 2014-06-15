package com.github.aiderpmsi.pims.treemodel;

import java.util.HashMap;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.concurrent.locks.ReentrantLock;

public class LinkedNodesIterator implements Iterator<Node<?>> {

	private LinkedNode current;
	
	private LinkedNode next = null;
	
	private boolean asc;
	
	private HashMap<Node<?>, LinkedNode> nodes;
	
	private ReentrantLock sharedLock;
	
	public LinkedNodesIterator(boolean asc, LinkedNode start, HashMap<Node<?>, LinkedNode> nodes, ReentrantLock sharedLock) {
		this.current = start;
		this.asc = asc;
		this.nodes = nodes;
		this.sharedLock = sharedLock;
	}

	@Override
	public boolean hasNext() {
		try {
			sharedLock.lock();
			if (asc && current != null && current.next != null)
				return true;
			else if (!asc && current != null && current.previous != null)
				return true;
			else {
				return false;
			}
		} finally {
			sharedLock.unlock();
		}
	}

	@Override
	public final Node<?> next() {
		try {
			sharedLock.lock();
			if (current == null ||
					(asc && current.next == null) ||
					(!asc && current.previous == null)) {
				throw new NoSuchElementException();
			} else {
				current = asc ? current.next : current.previous;
				next = current;
				return current.current;
			}
		} finally {
			sharedLock.unlock();
		}
	}

	@Override
	public void remove() {
		try {
			sharedLock.lock();
			if (next == null) {
				throw new IllegalStateException();
			} else {
				nodes.remove(next.current);
				next.previous.next = next.next;
				next.next.previous = next.previous;
				next.next = null;
				next.previous = null;
				next = null;
			}
		} finally {
			sharedLock.unlock();
		}
	}

}
