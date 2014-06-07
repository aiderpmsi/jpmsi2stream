package com.github.aiderpmsi.grouper.tests;

import java.util.EnumMap;
import java.util.List;

public class EnumMapBuilder<T extends Enum<T>, V> {

	public class Entry {
		public T key;
		public V value;
	}
	
	private Class<T> clazz;
	
	public EnumMapBuilder(Class<T> clazz) {
		this.clazz = clazz;
	}
	
	public EnumMap<T, V> build() {
		return new EnumMap<T, V>(clazz);
	}
	
	public EnumMap<T, V> buildAndfill(List<Entry> entries) {
		EnumMap<T, V> enumMap = build();
		
		for (Entry entry : entries) {
			enumMap.put(entry.key, entry.value);
		}
		
		return enumMap;
	}
	
	public Entry buildEntry(T key, V value) {
		Entry entry = new Entry();
		entry.key = key;
		entry.value = value;
		return entry;
	}

}
