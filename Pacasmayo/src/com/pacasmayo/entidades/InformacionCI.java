package com.pacasmayo.entidades;

import net.rim.device.api.util.Persistable;

public class InformacionCI implements Persistable {

	private String idCliente;
	private String idObra;
	private String idObraWS;
	private String Obra;
	private String ObraDescripcion;
	private String CreadoEnCelular;
	
	private String idTipoObra;
	private String TipoObra;
	
	private String idMarca;
	private String marca;
	
	private String idUnidadMedida;
	private String unidadMedida;
	
	private String idProducto;
	private String producto;
	
    private String idFrecuencia;
	private String frecuencia;
	
	private String idProveedor;
	private String proveedor;
	
	private String fecha;
	private String cantidad;
	
	private String preciocompra;
	private String fechafinobra;
	private String residente;
	private String estado;
	
	public String getEstado() {
		return estado;
	}
	public void setEstado(String estado) {
		this.estado = estado;
	}
	private String observacion;
	private boolean enviado;
	
	public boolean isEnviado() {
		return enviado;
	}
	public void setEnviado(boolean enviado) {
		this.enviado = enviado;
	}
	public String getMarca() {
		return marca;
	}
	public void setMarca(String marca) {
		this.marca = marca;
	}
	public String getFecha() {
		return fecha;
	}
	public String getFechaFormato() {
		String fechaFormato = "";
		//20110101
		//01234567
		if(getFecha()!=null){
		    fechaFormato = getFecha().substring(6, 8) + "/" + getFecha().substring(4, 6) + "/" + getFecha().substring(0, 4); 
		}
		return fechaFormato;
	}
	public void setFecha(String fecha) {
		this.fecha = fecha;
	}
	public String getIdCliente() {
		return idCliente;
	}
	public void setIdCliente(String idCliente) {
		this.idCliente = idCliente;
	}
	public String getIdMarca() {
		return idMarca;
	}
	public void setIdMarca(String idMarca) {
		this.idMarca = idMarca;
	}
	public String getProveedor() {
		return proveedor;
	}
	public void setProveedor(String proveedor) {
		this.proveedor = proveedor;
	}
	public String getObservacion() {
		return observacion;
	}
	public void setObservacion(String observacion) {
		this.observacion = observacion;
	}
	public String getIdObra() {
		return idObra;
	}
	public String getIdTipoObra() {
		return idTipoObra;
	}
	public String getCantidad() {
		return cantidad;
	}
	public String getIdUnidadMedida() {
		return idUnidadMedida;
	}
	public String getFrecuencia() {
		return frecuencia;
	}
	public String getPreciocompra() {
		return preciocompra;
	}
	public String getFechafinobra() {
		return fechafinobra;
	}
	public String getResidente() {
		return residente;
	}
	public void setIdObra(String idObra) {
		this.idObra = idObra;
	}
	public void setIdTipoObra(String idTipoObra) {
		this.idTipoObra = idTipoObra;
	}
	public void setCantidad(String cantidad) {
		this.cantidad = cantidad;
	}
	public void setIdUnidadMedida(String idUnidadMedida) {
		this.idUnidadMedida = idUnidadMedida;
	}
	public void setFrecuencia(String frecuencia) {
		this.frecuencia = frecuencia;
	}
	public void setPreciocompra(String preciocompra) {
		this.preciocompra = preciocompra;
	}
	public void setFechafinobra(String fechafinobra) {
		this.fechafinobra = fechafinobra;
	}
	public void setResidente(String residente) {
		this.residente = residente;
	}
	public String getProducto() {
		return producto;
	}
	public void setProducto(String producto) {
		this.producto = producto;
	}
	public String getTipoObra() {
		return TipoObra;
	}
	public String getUnidadMedida() {
		return unidadMedida;
	}
	public String getIdProducto() {
		return idProducto;
	}
	public void setTipoObra(String tipoObra) {
		TipoObra = tipoObra;
	}
	public void setUnidadMedida(String unidadMedida) {
		this.unidadMedida = unidadMedida;
	}
	public void setIdProducto(String idProducto) {
		this.idProducto = idProducto;
	}
	public String getIdFrecuencia() {
		return idFrecuencia;
	}
	public void setIdFrecuencia(String idFrecuencia) {
		this.idFrecuencia = idFrecuencia;
	}
	public String getIdProveedor() {
		return idProveedor;
	}
	public void setIdProveedor(String idProveedor) {
		this.idProveedor = idProveedor;
	}
	
	public String getObra() {
		return Obra;
	}
	public String getObraDescripcion() {
		return ObraDescripcion;
	}
	public void setObra(String obra) {
		Obra = obra;
	}
	public void setObraDescripcion(String obraDescripcion) {
		ObraDescripcion = obraDescripcion;
	}
	public String getCreadoEnCelular() {
		return CreadoEnCelular;
	}
	public void setCreadoEnCelular(String creadoEnCelular) {
		CreadoEnCelular = creadoEnCelular;
	}
	public String getIdObraWS() {
		return idObraWS;
	}
	public void setIdObraWS(String idObraWS) {
		this.idObraWS = idObraWS;
	}
	
	
}
