package com.eddie.utils.util;

public class ArrayUtils {
	/*
	public static List<Categoria> arrayCategoria(String [] arrayParameter){
		
			String[] categoria = arrayParameter;	
			List<Categoria> categorias = new ArrayList<Categoria>();
			for (String s: categoria) {
				Categoria c = new Categoria ();
				c.setIdCategria(Integer.valueOf(s));
				categorias.add(c);
			}
			return categorias;
		}
	*/
	
	public static final int[] asInteger(String ss[]) {
		int[] ii = new int[ss.length];
		for (int i = 0; i<ss.length; i++) {
			ii[i] = Integer.valueOf(ss[i]);
		}
		return ii;
	}
	
	/*
	public static List<Idioma> arrayIdioma(String [] arrayParameter){
			
			String[] idioma = arrayParameter;	
			List<Idioma> idiomas = new ArrayList<Idioma>();
			for (String c: idioma) {
				Idioma i = new Idioma ();
				i.setIdIdioma(c);
				idiomas.add(i);
			}
			return idiomas;
		}
	*/
	/*
	public static List<Plataforma> arrayPlataforma(String [] arrayParameter){
		
		String[] plataforma = arrayParameter;		
		List<Plataforma> lista = new ArrayList<Plataforma>();
		for (String c: plataforma) {
			Plataforma p = new Plataforma ();
			p.setIdPlatadorma(Integer.valueOf(c));
			lista.add(p);
		}
		return lista;
	}
	*/
	
}
