package com.pacasmayo.entidades;

import java.util.Random;

import net.rim.device.api.util.Persistable;


public class Obra  implements Persistable {

	private String codigo;
	private String codigows;
	private String codigoCliente;
	private String nombre;
	private String descripcion;
	private String fecha;
	private String generadoEnCelular;
	
	public String getGeneradoEnCelular() {
		return generadoEnCelular;
	}
	public void setGeneradoEnCelular(String generadoEnCelular) {
		this.generadoEnCelular = generadoEnCelular;
	}
	public String getCodigo() {
		return codigo;
	}
	public String getCodigoCliente() {
		return codigoCliente;
	}
	public String getNombre() {
		return nombre;
	}
	public String getDescripcion() {
		return descripcion;
	}
	public String getFecha() {
		return fecha;
	}
	public String getFechaVisitaFormato() {
		String fechaFormato = "";
		//20110101
		//01234567
		fechaFormato = getFecha().substring(6, 8) + "/" + getFecha().substring(4, 6) + "/" + getFecha().substring(0, 4); 
		return fechaFormato;
	}
	
	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}
	
	public void setAutocodigo(){
		Random rand = new Random(System.currentTimeMillis());
		this.codigo = String.valueOf(rand.nextInt());
	}
	
	public void setCodigoCliente(String codigoCliente) {
		this.codigoCliente = codigoCliente;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}
	public void setFecha(String fechaVisita) {
		this.fecha = fechaVisita;
	}
	public String getCodigows() {
		return codigows;
	}
	public void setCodigows(String codigows) {
		this.codigows = codigows;
	}
}
