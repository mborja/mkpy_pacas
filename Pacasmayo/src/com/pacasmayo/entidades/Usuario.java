package com.pacasmayo.entidades;

import net.rim.device.api.util.Persistable;

public final class Usuario implements Persistable {
	private String codigo;
	private String clave;
	private String nombre;
	private String estado;
	private boolean autoValidar;
	private String version;
	private String imsi;
	private boolean sincronizado;
	private String fechaDesdeCM;
	private String fechaHastaCM;
	private String fechaValidacion;
	private String codigoTrabajador;
	
	public String getCodigoTrabajador() {
		return codigoTrabajador;
	}
	public void setCodigoTrabajador(String codigoTrabajador) {
		this.codigoTrabajador = codigoTrabajador;
	}
	public String getFechaValidacion() {
		return fechaValidacion;
	}
	public void setFechaValidacion(String fechaValidacion) {
		this.fechaValidacion = fechaValidacion;
	}
	public String getFechaDesdeCM() {
		return fechaDesdeCM;
	}
	public void setFechaDesdeCM(String fechaDesdeCM) {
		this.fechaDesdeCM = fechaDesdeCM;
	}
	public String getFechaHastaCM() {
		return fechaHastaCM;
	}
	public void setFechaHastaCM(String fechaHastaCM) {
		this.fechaHastaCM = fechaHastaCM;
	}
	public boolean isSincronizado() {
		return sincronizado;
	}
	public void setSincronizado(boolean sincronizado) {
		this.sincronizado = sincronizado;
	}
	public String getImsi() {
		return imsi;
	}
	public void setImsi(String imsi) {
		this.imsi = imsi;
	}
	public String getVersion() {
		return version;
	}
	public void setVersion(String version) {
		this.version = version;
	}
	public String getCodigo() {
		return codigo;
	}
	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}
	public String getClave() {
		return clave;
	}
	public void setClave(String clave) {
		this.clave = clave;
	}
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	public String getEstado() {
		return estado;
	}
	public void setEstado(String estado) {
		this.estado = estado;
	}
	public boolean isAutoValidar() {
		return autoValidar;
	}
	public void setAutoValidar(boolean autiValidar) {
		this.autoValidar = autiValidar;
	}

	
}
