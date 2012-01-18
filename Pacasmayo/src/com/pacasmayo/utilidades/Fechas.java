package com.pacasmayo.utilidades;

import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import net.rim.device.api.i18n.SimpleDateFormat;
import net.rim.device.api.ui.component.Dialog;

public final class Fechas {
	
	public static int getGMT() {
		return TimeZone.getTimeZone("GMT").getRawOffset();
	}
	
    public static boolean validarFechas(String fechaComparar, String fechaSistema) {
        long fecha1, fecha2;
        fecha1 = Long.parseLong(fechaComparar);
        fecha2 = Long.parseLong(fechaSistema);
        //Dialog.inform("fecha1 " + fecha1 + " fecha2 " + fecha2 );
        if ( fecha1 < fecha2 ) {
            return true;
        } else {
            return false;               
        }
    }

	/**
	 * Parsea un string en format YYYYMMDD a un objeto Date
	 * @param fecha
	 * @return
	 */
	public static Date stringToDate(String fecha) {
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.YEAR, Integer.parseInt(fecha.substring(0, 4)));
		cal.set(Calendar.MONTH, Integer.parseInt(fecha.substring(4, 6))-1);
		cal.set(Calendar.DATE, Integer.parseInt(fecha.substring(6, 8)));
		return cal.getTime();
	}
    
	/**
	 * Parsea un string en format YYYYMMDD HH24MM a un objeto Date
	 * @param fecha
	 * @return
	 */
	public static Date stringToDate(String fecha, String horas) {
		Calendar cal = Calendar.getInstance();
		int horaSuma = Integer.parseInt(fecha.substring(8, 10)) + Integer.parseInt(horas);
		cal.set(Calendar.YEAR, Integer.parseInt(fecha.substring(0, 4)));
		cal.set(Calendar.MONTH, Integer.parseInt(fecha.substring(4, 6))-1);
		cal.set(Calendar.DATE, Integer.parseInt(fecha.substring(6, 8)));
		if(horas != null)
			cal.set(Calendar.HOUR_OF_DAY, horaSuma );
		else
			cal.set(Calendar.HOUR_OF_DAY, Integer.parseInt(fecha.substring(8, 10)));

		cal.set(Calendar.MINUTE, Integer.parseInt(fecha.substring(10)));
		return cal.getTime();
	}
	
	/**
	 * Parsea un string en format YYYYMMDD HH24MM a un objeto Date
	 * @param fecha
	 * @return
	 */
	public static String stringToFechaAValidacion(String fecha, String horas) {
		Calendar cal = Calendar.getInstance();
		int horaSuma = Integer.parseInt(fecha.substring(8, 10)) + Integer.parseInt(horas);
		cal.set(Calendar.YEAR, Integer.parseInt(fecha.substring(0, 4)));
		cal.set(Calendar.MONTH, Integer.parseInt(fecha.substring(4, 6))-1);
		cal.set(Calendar.DATE, Integer.parseInt(fecha.substring(6, 8)));
		if(horas != null)
			cal.set(Calendar.HOUR_OF_DAY, horaSuma );
		else
			cal.set(Calendar.HOUR_OF_DAY, Integer.parseInt(fecha.substring(8, 10)));

		cal.set(Calendar.MINUTE, Integer.parseInt(fecha.substring(10)));
		
		return dateToString(cal.getTime());
	}

    public static String dateToString(Date fecha) {
    	SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmm");
    	return sdf.format(fecha);
    }
    
    public static String dateToString(Date fecha, String format) {
    	SimpleDateFormat sdf = new SimpleDateFormat(format);
    	return sdf.format(fecha);
    }

    public static String dateToString() {
    	  Calendar oCal = Calendar.getInstance();
          Date fecha = oCal.getTime();
    	SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmm");
    	return sdf.format(fecha);
    }

    // "yyyyMMddhhmm"
    public static String dateToString(String format) {
        Calendar oCal = Calendar.getInstance();
        Date fecha = oCal.getTime();
    	SimpleDateFormat sdf = new SimpleDateFormat(format);
    	return sdf.format(fecha);
    }

	
}
