package com.pacasmayo.ui;

import java.util.Vector;

import com.pacasmayo.dao.VendedorDB;
import com.pacasmayo.entidades.Vendedor;

import com.pacasmayo.dao.InformacionCMDB;
import com.pacasmayo.entidades.InformacionCM;

import com.pacasmayo.dao.InformacionCIDB;
import com.pacasmayo.entidades.InformacionCI;

import com.pacasmayo.utilidades.Estilos;
import com.makipuray.ui.mkpyStatusProgress;

import net.rim.device.api.system.Bitmap;
import net.rim.device.api.ui.Color;
import net.rim.device.api.ui.Field;
import net.rim.device.api.ui.Graphics;
import net.rim.device.api.ui.component.BitmapField;
import net.rim.device.api.ui.component.Dialog;
import net.rim.device.api.ui.component.ListField;
import net.rim.device.api.ui.component.ListFieldCallback;
import net.rim.device.api.ui.container.MainScreen;

public class Sincronizacion extends MainScreen implements ListFieldCallback {
    private ListField menu;
    private mkpyStatusProgress progress = new mkpyStatusProgress(""); 
    
    private Vendedor vendedor;

    private InformacionCMDB informesCM = new InformacionCMDB();
    private InformacionCIDB informesCI = new InformacionCIDB();
    
    private Vector infCMSinEnviar = new Vector();
    private Vector infCISinEnviar = new Vector();

    boolean cont = false;
	private String[] acciones = {" Canal masivo", " Canal industrial"}; 
	private String[] opciones = {"Pendientes de envío", "", ""};

	public Sincronizacion() {
		
		VendedorDB lista = new VendedorDB();
		vendedor = lista.getVendedor();
		
//		setTitle("Pendientes");
		informesCM = new InformacionCMDB();
		informesCI = new InformacionCIDB();
		
        add(new BitmapField(Bitmap.getBitmapResource("img/titulos/pendientes.png"), BitmapField.HCENTER));
		
        menu = new ListField(opciones.length, ListField.FIELD_HCENTER);
        menu.setCallback(this);
        cuentaPendientes();
        add(menu);
	}
	
	private void cuentaPendientes() {
		infCMSinEnviar = informesCM.getObjetosSinEnviar();
		infCISinEnviar = informesCI.getObjetosSinEnviar();
		
		
		opciones[1] = acciones[0] + " (" + infCMSinEnviar.size() + ")";
		opciones[2] = acciones[1] + " (" + infCISinEnviar.size() + ")";
		
		menu.invalidate();
	}

    protected boolean navigationMovement(int dx, int dy, int status, int time) {
        Field field = this.getFieldWithFocus();
        if(field == menu) {
            menu.invalidate(menu.getSelectedIndex() + dy);
            menu.invalidate(menu.getSelectedIndex());
        }
        return super.navigationMovement(dx, dy, status, time);
    }
    
	protected boolean navigationClick(int status, int time) {
		Field field = this.getFieldWithFocus();
		if(field == menu) {
			if ( menu.getSelectedIndex() == 1 ) {
				if(vendedor.getTipoVendedor().equals("1") || vendedor.getTipoVendedor().equals("3")){
					enviarMasivo();
				}else{
					Dialog.inform("No puede sincronizar visitas de productos masivos");
				}
			}
			if ( menu.getSelectedIndex() == 2 ) {
				if(vendedor.getTipoVendedor().equals("2") || vendedor.getTipoVendedor().equals("3")){
					enviarIndustrial();
				}else{
					Dialog.inform("No puede sincronizar visitas de productos masivos");
				}
			}
			return true;
		}
		return super.navigationClick(status, time);
	}

	private void enviarMasivo() {
		int n = infCMSinEnviar.size();
		progress.setTitle("Enviando...");
		progress.open();
		for (int i = 0; i < n; i++) {
			InformacionCM item = (InformacionCM) infCMSinEnviar.elementAt(i);
			progress.setTitle("Enviando " + (i + 1) + " de " + n + " ...");
			if ( informesCM.putRemote(item) ) {
	            //item.setEnviado(true);
	            informesCM.getObjetos().removeElement(item);
	            informesCM.commitChanges();
			} else {
				Dialog.inform("Error al enviar " + i + " de " + n + ". " + informesCM.getMsgError() + " Fecha: " + item.getFechaFormato() + ", idCliente: " + item.getIdCliente() + ", Marca: " + item.getMarca());
				//Dialog.inform("Error al enviar " + i + " de " + n + ". " + informes.getMsgError());
			}
		}
		cuentaPendientes();
		progress.close();
	}

	private void enviarIndustrial() {
		int n = infCISinEnviar.size();
		progress.setTitle("Enviando...");
		progress.open();
		for (int i = 0; i < n; i++) {
			InformacionCI item = (InformacionCI) infCISinEnviar.elementAt(i);
			progress.setTitle("Enviando " + (i + 1) + " de " + n + " ...");
			if ( informesCI.putRemote(item) ) {
	            //item.setEnviado(true);
				actualizaObrasWS(item);
	            informesCI.getObjetos().removeElement(item);
	            informesCI.commitChanges();
			} else {
				Dialog.inform("Error al enviar " + i + " de " + n + ". " + informesCI.getMsgError() + " Fecha: " + item.getFechaFormato() + ", idCliente: " + item.getIdCliente() + ", Marca: " + item.getMarca());
				//Dialog.inform("Error al enviar " + i + " de " + n + ". " + informes.getMsgError());
			}
		}
		cuentaPendientes();
		progress.close();
	}

	public void drawListRow(ListField list, Graphics g, int index, int y, int w) {
        if ( list.getSelectedIndex() == index ) {
        } else {
            g.setColor(Color.BLACK);
        	if ( index == 0 ) {
                g.setColor(Color.WHITE);
                g.setBackgroundColor(Color.GRAY);
            } else  {
	            g.setColor(Color.BLACK);
                g.setBackgroundColor(Estilos.getBGInterlinea(0));
            }
            g.clear();
        }
        g.drawText(get(index), 0, y, 0, w);
    }

    public String get(int index) {
        return opciones[index];
    }

    public int getPreferredWidth(ListField arg0) {
        return 0;
    }

    public int indexOfList(ListField arg0, String arg1, int arg2) {
        return 0;
    }

    public Object get(ListField listField, int index) {
        return null;
    }

    private void actualizaObrasWS(InformacionCI inf){
		int n = infCISinEnviar.size();
		progress.open();
		for (int i = 0; i < n; i++) {
			InformacionCI item = (InformacionCI) infCISinEnviar.elementAt(i);
			if ( item.getIdObra() == inf.getIdObra()  ) {
	            item.setIdObraWS(inf.getIdObraWS());   
			}
		}
    }
}
