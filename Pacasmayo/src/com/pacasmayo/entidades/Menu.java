package com.pacasmayo.entidades;

import net.rim.device.api.util.Persistable;

public class Menu implements Persistable {
	private String codigo;
	private String descripcion;
	
	public Menu(String cod1, String cod2) {
		// TODO Auto-generated constructor stub
		setCodigo(cod1);
		setDescripcion(cod2);
	}
	public String getCodigo() {
		return codigo;
	}
	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}
	public String getDescripcion() {
		return descripcion;
	}
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

}
