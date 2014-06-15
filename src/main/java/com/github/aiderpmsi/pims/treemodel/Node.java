package com.github.aiderpmsi.pims.treemodel;

import java.util.Iterator;

public class Node<T> {

	private final Class<? extends T> type;

	private T content;
	
	private Node<?> parent = null;
	
	private LinkedNodes children = new LinkedNodes();
	
	public Node(Class<? extends T> type) {
		this.type = type;
	}
	
	public Node(Class<? extends T> type, T content) {
		this(type);
		this.setContent(content);
	}

	public Node<?> getParent() {
		return parent;
	}
	
	/**
	 * 
	 * @return current node
	 */
	public Node<T> setParent(Node<?> parent) {
		this.parent = parent;
		return this;
	}
	
	public Node<?> addChild(Node<?> child) {
		children.addLast(child);
		return this;
	}

	public Node<?> getFollowingChild(Node<?> child) {
		Iterator<Node<?>> it = children.iterator(true, child);
		if (it.hasNext()) {
			return it.next();
		} else {
			return null;
		}
	}

	public Node<?> getPreviousChild(Node<?> child) {
		Iterator<Node<?>> it = children.iterator(false, child);
		if (it.hasNext()) {
			return it.next();
		} else {
			return null;
		}
	}

	public Node<?> getFirstChild() {
		return children.getFirst();
	}
	
	public Node<?> getLastChild() {
		return children.getLast();
	}

	public T getContent() {
		return content;
	}

	public void setContent(T content) {
		this.content = content;
	}

	public Class<? extends T> getType() {
		return type;
	}
	
}
