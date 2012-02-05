package com.pacasmayo.utilidades;

import java.util.Vector;

import javax.microedition.global.Formatter;

import net.rim.device.api.system.ApplicationDescriptor;
import net.rim.device.api.system.CodeModuleGroup;
import net.rim.device.api.system.CodeModuleGroupManager;

import net.rim.device.api.i18n.MessageFormat;
import net.rim.device.api.system.ApplicationDescriptor;
import net.rim.device.api.system.CodeModuleGroup;
import net.rim.device.api.system.CodeModuleGroupManager;
import net.rim.device.api.system.DeviceInfo;
import net.rim.device.api.system.Display;
import net.rim.device.api.ui.component.Dialog;

public final class Cadenas {
    public static final String TOKEN = "¬";
    public static final String URLBASE = "http://10.34.2.62";
    //public static final String URLBASE = "http://portal.dino.com.pe";
    public static final String URIServer = "";
    public static final String urlDownload = "";
    
    public static final String getJadUrl(String jadProperty){
    	String description;	
    	CodeModuleGroup[] allGroups = CodeModuleGroupManager.loadAll();
    	CodeModuleGroup myGroup = null;
    	String moduleName = ApplicationDescriptor
    	   .currentApplicationDescriptor().getModuleName();

    	for (int i = 0; i < allGroups.length; i++) {
    	   if (allGroups[i].containsModule(moduleName)) {
    	      myGroup = allGroups[i];
    	      break;
    	    }
    	}

    	// Get the property
    	try{
    		description = myGroup.getProperty(jadProperty);
    	}catch(Exception ex){
    		description = ex.getMessage();
    	}
    	
    	return description;
    	
    }

    public static String getBIS() {
    	if ( DeviceInfo.isSimulator() ) {
    		return ";DeviceSide=false";
    	} else {
    		return ";DeviceSide=false;ConnectionSetup=delayed;UsePipe=true;ConnectionTimeout=120000;EncryptRequired=true;ConnectionType=mds-public";
    	}
    }
    
    public static String[] splitSimple(String strCadena, String strSeparador) {
        int indexOfEnd = strCadena.length();
        String[] strCampos = null;
        Vector lista = new Vector();
        while (indexOfEnd > 0) {
           indexOfEnd = strCadena.indexOf(strSeparador);
           if (indexOfEnd >= 0) {
              lista.addElement(strCadena.substring(0, indexOfEnd));
              indexOfEnd += strSeparador.length();
              strCadena = strCadena.substring(indexOfEnd);
           } else {
              String cadena = strCadena.substring(0);
              if (cadena.length() > 0) {
                 lista.addElement(cadena);
              }
           }
        }
        strCampos = new String[lista.size()];
        lista.copyInto(strCampos);
        lista = null;
        return strCampos;
     }
    
}
