package com.github.aiderpmsi.pims.treebrowser;

public abstract interface ConfigBuilder<T extends Config> {

	public T build() throws TreeBrowserException;

}
