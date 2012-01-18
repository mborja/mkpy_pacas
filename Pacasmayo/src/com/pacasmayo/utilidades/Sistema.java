package com.pacasmayo.utilidades;

import net.rim.device.api.system.ApplicationDescriptor;
import net.rim.device.api.system.CodeModuleGroup;
import net.rim.device.api.system.CodeModuleGroupManager;
import net.rim.device.api.system.DeviceInfo;
import net.rim.device.api.system.GPRSInfo;
import net.rim.device.api.system.SIMCardInfo;
import net.rim.device.api.ui.component.Dialog;

public final class Sistema {
	private final static String idApp = "0xb5f5e3a2f562f41L"; // pe.com.pacasmayo.one

    /**
     * Obtiene el estado de la red celular
     * @return boolean si es que hay cobertura o no
     */
    public static boolean isCoverage() {
		try {
	    	if ( GPRSInfo.getGPRSState() == GPRSInfo.GPRS_STATE_STANDBY || GPRSInfo.getGPRSState() == GPRSInfo.GPRS_STATE_READY ) {
	    		return true;
	    	}
	    	return false;
		} catch (Exception e) {
			return true;
		}
    }
    
    /**
     * Obtiene el número del chip del la línea celular
     * @return String el número del chip
     */
    public static String getImsi() {
		try {
			return GPRSInfo.imeiToString(SIMCardInfo.getIMSI(), false );
		} catch (Exception e) {
			return "ND";
		}
	}

    /**
     * Retorna el número redondeado  
     * @param n
     * @return
     */
	public static long round(double n){
	    return (long)(n + 0.5D);
	}
	
	public static String round(double n, int d){
		String num = String.valueOf(n);
		int index = num.indexOf(".");
	    return num.substring(0, index + d);
	}
	
	/**
	 * Obtiene el IMEI número de serie del equipo celular
	 * @return String el número de serie del equipo móvil
	 */
	public static String getImei() {
		try {
			return GPRSInfo.imeiToString(GPRSInfo.getIMEI(), false);	
		} catch (Exception e) {
			return "ND";
		}
	}
	
	/**
	 * Obtiene el número de PIN del BlackBerry
	 * @return String el número PIN del BlackBerry
	 */
	public static String getPin() {
		String valor = "";
		valor = Integer.toHexString(DeviceInfo.getDeviceId());
		return valor;
	}
	
	/**
	 * Obtiene el número de versión de la aplicación indicado en el JAD
	 * @return
	 */
	public static String getVersion(){
	    String version = ApplicationDescriptor.currentApplicationDescriptor().getVersion();
		return version;
	}

	/**
	 * Obtiene un valor del JAD dependiendo de la clave
	 * @param Name la clave indicada en el archivo JAD
	 * @return String el valor indicado en el archivo JAD para la Clave en @Name
	 */
	public static String getJADProperty(String Name){
	    CodeModuleGroup[] allGroups = CodeModuleGroupManager.loadAll();
	    CodeModuleGroup myGroup = null;
	    String moduleName = ApplicationDescriptor.currentApplicationDescriptor().getModuleName();
	    for (int i = 0; i < allGroups.length; i++) {
		    if (allGroups[i].containsModule(moduleName)) {
			    myGroup = allGroups[i];
			    break;
		    }
	    }
	 
	    // Get the property
	    String prop = myGroup.getProperty(Name);
	    return prop;
	}

	/**
	 * Obtiene el valor ID de la aplicación
	 * @return String el id de la aplicación
	 */
	public static String getIdapp() {
		return idApp;
	}

}
