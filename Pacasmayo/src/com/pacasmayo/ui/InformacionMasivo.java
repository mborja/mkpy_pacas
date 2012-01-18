package com.pacasmayo.ui;

import java.util.Date;
import java.util.Vector;

import com.makipuray.ui.mkpyLabelLabelField;
import com.makipuray.ui.mkpyStatusProgress;
import com.pacasmayo.dao.InformacionCMDB;
import com.pacasmayo.dao.UsuarioDB;
import com.pacasmayo.entidades.CanalMasivo;
import com.pacasmayo.entidades.InformacionCM;
import com.pacasmayo.entidades.Usuario;
import com.pacasmayo.utilidades.Estilos;
import com.pacasmayo.utilidades.Fechas;

import net.rim.device.api.system.Bitmap;
import net.rim.device.api.ui.Color;
import net.rim.device.api.ui.DrawStyle;
import net.rim.device.api.ui.Field;
import net.rim.device.api.ui.Graphics;
import net.rim.device.api.ui.MenuItem;
import net.rim.device.api.ui.component.BitmapField;
import net.rim.device.api.ui.component.Dialog;
import net.rim.device.api.ui.component.LabelField;
import net.rim.device.api.ui.component.ListField;
import net.rim.device.api.ui.component.ListFieldCallback;
import net.rim.device.api.ui.container.MainScreen;

public class InformacionMasivo extends MainScreen implements ListFieldCallback {
    private ListField lstTomas, lstTitulo;
    private mkpyStatusProgress progress = new mkpyStatusProgress("");
    private CanalMasivo canalMasivo;
    private Usuario usuario;
	private Vector result;
    private InformacionCMDB informaciones = new InformacionCMDB();
    private boolean refresca = false;
    private long fDesde, fHasta;
    
    public InformacionMasivo(CanalMasivo cm, long desde, long hasta) {
    	canalMasivo = cm;
    	fDesde = desde;
    	fHasta = hasta;
    	UsuarioDB usuarios = new UsuarioDB();
    	usuario = usuarios.getUsuario();
    	usuarios = null;
        add(new BitmapField(Bitmap.getBitmapResource("img/titulos/tomaInfCM.png"), BitmapField.FIELD_HCENTER));

    	add(new mkpyLabelLabelField("Vendedor: ", usuario.getNombre(), LabelField.NON_FOCUSABLE, Color.BLACK, Color.WHITE));
    	add(new mkpyLabelLabelField("Cliente: ", cm.getNombre(), LabelField.NON_FOCUSABLE, Color.BLACK, Color.WHITE));
    	add(new mkpyLabelLabelField("Fecha V.: ", cm.getFechaFormato(), LabelField.NON_FOCUSABLE, Color.BLACK, Color.WHITE));

    	buscar();
    	lstTitulo =  new ListField(1, ListField.FIELD_HCENTER | ListField.NON_FOCUSABLE);
    	lstTitulo.setCallback(this);

    	lstTomas = new ListField(result.size(), ListField.FIELD_HCENTER);
    	lstTomas.setCallback(this);
        add(lstTitulo);
        add(lstTomas);
    	
        addMenuItem(mnIngresar);
        addMenuItem(mnModificar);
        addMenuItem(mnEliminar);

    }

	private void buscar() {
		progress.open();
		progress.setTitle("Buscando...");    	
		//result = informaciones.getObjetosByIdClienteFechas(canalMasivo.getCodigo(), fDesde, fHasta);
		result = informaciones.getObjetosByIdClienteFechas(canalMasivo.getCodigo(), canalMasivo.getFecha());
    	if ( lstTomas != null ) {
    		lstTomas.setSize(result.size());
    		lstTomas.invalidate();
    	}
		progress.close();
	}
	
    private void seleccion() {
    	if( lstTomas.getSelectedIndex() == 0 ) { // Plan de visitas
    		
    	}
    }            
    
	protected void onExposed() {
		if ( refresca ) {
			buscar();
			refresca = false;
		}
	}
    
    MenuItem mnIngresar = new MenuItem("Ingresar", 110, 10) {
        public void run() {
    		long lngFecha = Long.parseLong(canalMasivo.getFecha());
    		long lngHoy = Long.parseLong(Fechas.dateToString("yyyyMMdd"));
    		if ( lngFecha + 1 < lngHoy ) {
    			Dialog.inform("No se pueden registrar visitas anteriores a ayer.");
    			return;
    		}
        	refresca = true;
    		Estilos.pushScreen(new IngresoDatosCM(canalMasivo, null));
        }
    };

    MenuItem mnModificar = new MenuItem("Modificar", 110, 10) {
        public void run() {
    		long lngFecha = Long.parseLong(canalMasivo.getFecha());
    		long lngHoy = Long.parseLong(Fechas.dateToString("yyyyMMdd"));
    		if ( lngFecha + 1 < lngHoy ) {
    			Dialog.inform("No se pueden registrar visitas anteriores a ayer.");
    			return;
    		}
        	refresca = false;
        	int index = lstTomas.getSelectedIndex();
        	if( index >= 0 ) { 
        		InformacionCM inf = get(index);
        		if ( inf.isEnviado() ) {
        			Dialog.inform("No se puede modificar, registro ya sincronizado");
        		} else {
            		Estilos.pushScreen(new IngresoDatosCM(canalMasivo, inf));
        		}
        	}
        }
    };
    
    MenuItem mnEliminar = new MenuItem("Eliminar", 110, 10) {
        public void run() {
        	refresca = false;
        	int index = lstTomas.getSelectedIndex(); 
        	if( index >= 0 ) { 
        		InformacionCM inf = get(index);
        		if ( inf.isEnviado() ) {
        			Dialog.inform("No se puede eliminar, registro ya sincronizado");
        		} else {
            		informaciones.getObjetos().removeElement(inf); // .removeElementAt(index);
            		informaciones.commitChanges();
            		buscar();
        		}
        	}
        }
    };

    protected boolean navigationMovement(int dx, int dy, int status, int time) {
        Field field = this.getFieldWithFocus();
        if(field == lstTomas) {
        	lstTomas.invalidate(lstTomas.getSelectedIndex() + dy);
        	lstTomas.invalidate(lstTomas.getSelectedIndex());
        }
        return super.navigationMovement(dx, dy, status, time);
    }
    
	protected boolean navigationClick(int status, int time) {
		Field field = this.getFieldWithFocus();
		if(field == lstTomas) {
			seleccion();
			return true;
		}
		return super.navigationClick(status, time);
	}

    public void drawListRow(ListField list, Graphics g, int index, int y, int w) {
    	if ( list == lstTitulo ) {
            g.setColor(Color.WHITE);
            g.setBackgroundColor(Color.GRAY);
            g.clear();
            g.drawText("MarcaCM  Observacion", 0, y, DrawStyle.LEFT, w);
    	}
    	if ( list == lstTomas ) {
            if ( list.getSelectedIndex() == index ) {
            } else {
                g.setColor(Color.BLACK);
                g.setBackgroundColor(Estilos.getBGInterlinea(index));
                g.clear();
            }
        	InformacionCM temp = get(index);
            g.drawText(temp.getMarca() + "  " + temp.getObservacion(), 0, y, DrawStyle.LEFT, w);
    	}
    }

    public InformacionCM get(int index) {
        return (InformacionCM) result.elementAt(index); // opciones[index];
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
    
    protected boolean onSavePrompt() {
        return true;
    }
    
}
