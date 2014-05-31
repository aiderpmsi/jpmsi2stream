package com.github.aiderpmsi.pims.treebrowser;

import java.util.Date;
import java.util.HashMap;
import java.util.concurrent.locks.ReentrantLock;

import org.apache.commons.jexl2.MapContext;

public class TreeBrowserFactory<T extends Config> implements Runnable {

	/** Config element */
	@SuppressWarnings("rawtypes")
	private static HashMap<Class<? extends ConfigBuilder>, Config> configs = new HashMap<>();
	
	/** Config builder */
	private ConfigBuilder<T> configBuilder;
	
	/** Lock when using config */
	private static ReentrantLock lock = new ReentrantLock();
	
	public TreeBrowserFactory(ConfigBuilder<T> configBuilder) {
		this.configBuilder = configBuilder;
	}
	
	public TreeBrowser build() throws TreeBrowserException {
		// GETS THE CONFIGURATION
		Config clonedConfig;
		lock.lock();
		try {
			Config config;
			if ((config = configs.get(configBuilder.getClass())) == null) {
				config = configBuilder.build();
				configs.put(configBuilder.getClass(), config);
				(new Thread(this)).start();
			}
			clonedConfig = (Config) config.clone();
		} finally {
			lock.unlock();
		}
		
		// USES THE CONFIGURATION IN TREEBROWSERCONFIG TO CREATE THE TREEBROWSER
		TreeBrowser tb = new TreeBrowser();
		tb.setJc(new MapContext(clonedConfig.getContext()));
		tb.setJexl(clonedConfig.getJexlEngine());
		tb.setTree(clonedConfig.getTree());

		return tb;
	}

	@Override
	public void run() {
		while (true) {
			if (removeConfig())
				break;
			
			// SLEEPS. IF INTERRUPTION EXCEPTION HAPPENS, LET THROW AN EXCEPTION
			try {
				Thread.sleep(10000);
			} catch (InterruptedException e) {
				// DO NOTHING
			}
			if (Thread.interrupted()) {
				removeConfig();
				break;
			}
		}
	}

	private boolean removeConfig() {
		lock.lock();
		try {
			Config config;
			if (Thread.interrupted() ||
					((config = configs.get(configBuilder.getClass())) != null &&
					config.getClonedTime() != null &&
					new Date().getTime() - config.getClonedTime() > 600000)) {
				configs.remove(configBuilder.getClass());
				config = null;
				return true;
			}
		} finally {
			lock.unlock();
		}
		return false;
	}
	
}
