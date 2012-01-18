package com.pacasmayo.entidades;

import net.rim.device.api.util.Persistable;

public class Vendedor implements Persistable {
	private String codigo;
	private String descripcion;
	private String tipoVendedor;
	
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
	public String getTipoVendedor() {
		return tipoVendedor;
	}
	public void setTipoVendedor(String tipoVendedor) {
		this.tipoVendedor = tipoVendedor;
	}	
	

}
