package com.eddie.cache;

import java.util.HashMap;
import java.util.Map;

public class CacheImpl<K,V> implements Cache<K,V>{
		
	private Map<K,V> cache=null;
	private String nombre;
	
	public CacheImpl(String nombre) {
		this.nombre = nombre;
		cache=new HashMap<K,V>();
	}
	

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}


	@Override
	public void put(K k, V v) {
		cache.put(k, v);
		
	}

	@Override
	public V get(K k) {
		return cache.get(k);
	}


	@Override
	public void clear() {
		cache.clear();
		
	}
	
	

}
