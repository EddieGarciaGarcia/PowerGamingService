package com.eddie.model;

import java.util.List;


public class Resultados<T> {
	
	private List<T> resultados= null;
	private Integer startIndex=0;
	private Integer total=0;
	
	public Resultados(List<T> r, int startIndex, int total) {
		setResultados(r);
		setStartIndex(startIndex);
		setTotal(total);
	}

	public List<T> getResultados() {
		return resultados;
	}

	public void setResultados(List<T> resultados) {
		this.resultados = resultados;
	}

	public Integer getTotal() {
		return total;
	}

	public void setTotal(Integer total) {
		this.total = total;
	}

	public Integer getStartIndex() {
		return startIndex;
	}

	public void setStartIndex(Integer startIndex) {
		this.startIndex = startIndex;
	}
	
	
}
