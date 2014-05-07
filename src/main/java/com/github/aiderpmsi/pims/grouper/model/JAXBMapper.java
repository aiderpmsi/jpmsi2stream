package com.github.aiderpmsi.pims.grouper.model;


public interface JAXBMapper<U, T> {
	
	public abstract T transform(U model);
	
}
