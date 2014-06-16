package com.github.aiderpmsi.pims.treemodel;

import java.util.HashMap;


public class Node<T> {

	private final Class<? extends T> type;

	private T content;
	
	public Node<?> firstChild = null;
	
	public Node<?> nextSibling = null;
	
	public HashMap<String, Node<?>> index;
	
	public Node(Class<? extends T> type, HashMap<String, Node<?>> index) {
		this.type = type;
		this.index = index;
	}
	
	public Node(Class<? extends T> type, HashMap<String, Node<?>> index, T content) {
		this(type, index);
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
