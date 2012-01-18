package com.pacasmayo.entidades;

import net.rim.device.api.ui.component.Dialog;
import net.rim.device.api.util.Persistable;

public final class CanalMasivo implements Persistable {
	private String codigo;
	private String nombre;
	private String fecha;
	private String estado;
	
	public String getCodigo() {
		return codigo;
	}
	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	public String getFecha() {
		return fecha;
	}
	public String getFechaFormato() {
		String fechaFormato = "";
		//20110101
		//01234567
		fechaFormato = getFecha().substring(6, 8) + "/" + getFecha().substring(4, 6) + "/" + getFecha().substring(0, 4); 
		return fechaFormato;
	}
	
	public void setFecha(String fecha) {
		this.fecha = fecha;
	}
	public String getEstado() {
		return estado;
	}
	public void setEstado(String estado) {
		this.estado = estado;
	}
	
	
}
