package com.pacasmayo.entidades;

import net.rim.device.api.util.Persistable;

public class TipoUsuario implements Persistable {
	private String rol;
	
	public String getRol() {
		return rol;
	}
	public void setRol(String lsrol) {
		this.rol = lsrol;
	}	

}
