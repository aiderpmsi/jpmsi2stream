package com.github.aiderpmsi.pims.treemodel;


public class Node<T> {

	private final Class<? extends T> type;

	private T content;
	
	public Node<?> firstChild = null;
	
	public Node<?> nextSibling = null;
	
	public Node(Class<? extends T> type) {
		this.type = type;
	}
	
	public Node(Class<? extends T> type, T content) {
		this(type);
		this.setContent(content);
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
