package com.pacasmayo.entidades;

import net.rim.device.api.util.Persistable;

public class InformacionCM implements Persistable {
	private String idCliente;
	private String idMarca;
	private String marca;
	private String fecha;
	private String promedioVentaMes;
	private String precioCompra;
	private String precioVentaFerreteria;
	private String precioVentaConstruccion;
	private String precioVentaAutoConstruccion;
	private String stock;
	private String proveedor;
	private String condicionCompra;
	private String capacidadAlmacen;
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
		fechaFormato = getFecha().substring(6, 8) + "/" + getFecha().substring(4, 6) + "/" + getFecha().substring(0, 4); 
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
	public String getPromedioVentaMes() {
		return promedioVentaMes;
	}
	public void setPromedioVentaMes(String promedioVentaMes) {
		this.promedioVentaMes = promedioVentaMes;
	}
	public String getPrecioCompra() {
		return precioCompra;
	}
	public void setPrecioCompra(String precioCompra) {
		this.precioCompra = precioCompra;
	}
	public String getPrecioVentaFerreteria() {
		return precioVentaFerreteria;
	}
	public void setPrecioVentaFerreteria(String precioVentaFerreteria) {
		this.precioVentaFerreteria = precioVentaFerreteria;
	}
	public String getPrecioVentaConstruccion() {
		return precioVentaConstruccion;
	}
	public void setPrecioVentaConstruccion(String precioVentaConstruccion) {
		this.precioVentaConstruccion = precioVentaConstruccion;
	}
	public String getPrecioVentaAutoConstruccion() {
		return precioVentaAutoConstruccion;
	}
	public void setPrecioVentaAutoConstruccion(String precioVentaAutoConstruccion) {
		this.precioVentaAutoConstruccion = precioVentaAutoConstruccion;
	}
	public String getStock() {
		return stock;
	}
	public void setStock(String stock) {
		this.stock = stock;
	}
	public String getProveedor() {
		return proveedor;
	}
	public void setProveedor(String proveedor) {
		this.proveedor = proveedor;
	}
	public String getCondicionCompra() {
		return condicionCompra;
	}
	public void setCondicionCompra(String condicionCompra) {
		this.condicionCompra = condicionCompra;
	}
	public String getCapacidadAlmacen() {
		return capacidadAlmacen;
	}
	public void setCapacidadAlmacen(String capacidadAlmacen) {
		this.capacidadAlmacen = capacidadAlmacen;
	}
	public String getObservacion() {
		return observacion;
	}
	public void setObservacion(String observacion) {
		this.observacion = observacion;
	}
	
	
}
