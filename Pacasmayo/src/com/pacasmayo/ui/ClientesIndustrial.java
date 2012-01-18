package com.pacasmayo.ui;

import java.util.Date;
import java.util.Vector;

import net.rim.device.api.system.Bitmap;
import net.rim.device.api.ui.Color;
import net.rim.device.api.ui.DrawStyle;
import net.rim.device.api.ui.Field;
import net.rim.device.api.ui.FieldChangeListener;
import net.rim.device.api.ui.Graphics;
import net.rim.device.api.ui.MenuItem;
import net.rim.device.api.ui.component.BitmapField;
import net.rim.device.api.ui.component.ButtonField;
import net.rim.device.api.ui.component.DateField;
import net.rim.device.api.ui.component.Dialog;
import net.rim.device.api.ui.component.LabelField;
import net.rim.device.api.ui.component.ListField;
import net.rim.device.api.ui.component.ListFieldCallback;
import net.rim.device.api.ui.container.HorizontalFieldManager;
import net.rim.device.api.ui.container.MainScreen;
import net.rim.device.api.ui.container.VerticalFieldManager;

import com.pacasmayo.dao.CanalIndustrialDB;
import com.pacasmayo.dao.UsuarioDB;
import com.pacasmayo.entidades.CanalIndustrial;

import com.pacasmayo.entidades.Usuario;
import com.pacasmayo.utilidades.Estilos;
import com.pacasmayo.utilidades.Fechas;
import com.pacasmayo.utilidades.Sistema;
import com.makipuray.ui.mkpyLabelEditField;
import com.makipuray.ui.mkpyLabelField;
import com.makipuray.ui.mkpyStatusProgress;

public class ClientesIndustrial extends MainScreen implements ListFieldCallback, FieldChangeListener {        
	private DateField txtFechaDesde = new DateField("Fecha desde:", (new Date()).getTime(), DateField.DATE | DateField.USE_ALL_WIDTH);
	private DateField txtFechaHasta = new DateField("Fecha hasta:", (new Date()).getTime(), DateField.DATE | DateField.USE_ALL_WIDTH);
	private ButtonField btnBuscar = new ButtonField("    Filtrar    ", ButtonField.FIELD_HCENTER | ButtonField.CONSUME_CLICK | ButtonField.USE_ALL_WIDTH);
    private ListField lstClientes, lstTitulo;
    private mkpyStatusProgress progress = new mkpyStatusProgress("");   
	private CanalIndustrialDB canales = new CanalIndustrialDB();
	private Vector result;
	private Usuario usuario;
	private UsuarioDB usuarios = new UsuarioDB();
	private static final int NROLINEAS = 2;

    protected boolean onSavePrompt() {
           return true;
    }
    
    private CanalIndustrial seleccion() {
    	CanalIndustrial ci = null;
    	int index = lstClientes.getSelectedIndex(); 
    	if( index >= 0 ) {
    		ci = get(index);
    	}
    	return ci;
    }            
       
    public ClientesIndustrial() {
        add(new BitmapField(Bitmap.getBitmapResource("img/titulos/planCM.png"), BitmapField.FIELD_HCENTER));
    	add(txtFechaDesde);
        add(txtFechaHasta);
		btnBuscar.setChangeListener(this);
    	add(btnBuscar);
    	
    	usuario = usuarios.getUsuario();
    	if ( ! usuario.getFechaDesdeCM().equals("") ) {
    		Date tmp = Fechas.stringToDate(usuario.getFechaDesdeCM());
    		txtFechaDesde.setDate(tmp);
    	}
    	if ( ! usuario.getFechaHastaCM().equals("") ) {
    		Date tmp = Fechas.stringToDate(usuario.getFechaHastaCM());
    		txtFechaHasta.setDate(tmp);
    	}
    	
    	buscar();
    	lstTitulo =  new ListField(1, ListField.FIELD_HCENTER | ListField.NON_FOCUSABLE);
    	lstTitulo.setCallback(this);
    	lstClientes = new ListField(result.size(), ListField.FIELD_HCENTER);
    	lstClientes.setCallback(this);
    	lstClientes.setFont(this.getFont().derive(this.getFont().getStyle(), this.getFont().getHeight() - 3));
    	lstClientes.setRowHeight( this.getFont().getHeight() * NROLINEAS);
        add(lstTitulo);
        add(lstClientes);
        
        addMenuItem(mnVisitaConObra);
        addMenuItem(mnVisitaSinObra);
    }
    
    MenuItem mnVisitaConObra = new MenuItem("Mant. de visita con obra", 110, 10) {
        public void run() {
   			//Dialog.inform("Pantalla de mantenimiento con obra.");
   			CanalIndustrial ci;
   			ci = seleccion();
   			if(ci!=null){
   	   			if(ci!=null){
   	   				Estilos.pushScreen(new InformacionIndustrialConObra(ci));
   	   			}
   			}
        }
    };
    
    MenuItem mnVisitaSinObra = new MenuItem("Mant. de visita sin obra", 110, 10) {
        public void run() {
   			//Dialog.inform("Pantalla de mantenimiento sin obra.");
   			CanalIndustrial ci;
   			ci = seleccion();
   			if(ci!=null){
   				Estilos.pushScreen(new InformacionIndustrialSinObra(ci, txtFechaDesde.getDate(), txtFechaHasta.getDate()));
   			}
   			
        }
    };
    
	public void fieldChanged(Field field, int context) {
        if ( field == btnBuscar ) {
        	buscar();
        }
    }

	private void buscar() {
		progress.open();
		progress.setTitle("Buscando...");
    	if ( ! canales.getRemote(Fechas.dateToString(new Date(txtFechaDesde.getDate()), "yyyyMMdd"), 
    			Fechas.dateToString(new Date(txtFechaHasta.getDate()), "yyyyMMdd")) ) {
        	Dialog.inform("Error. Canal masivo, se usará la información anterior. " + canales.getMsgError());
    	}
    	usuario.setFechaDesdeCM(Fechas.dateToString(new Date(txtFechaDesde.getDate()), "yyyyMMdd"));
    	usuario.setFechaHastaCM(Fechas.dateToString(new Date(txtFechaHasta.getDate()), "yyyyMMdd"));
    	usuarios.setUsuario(usuario);
    	
    	int n = canales.getObjetos().size();
    	result = new Vector();
    	//result.addElement(new CanalMasivo());
    	for ( int i = 0; i < n; i++) {
    		result.addElement(canales.getObjetos().elementAt(i));
    	}
    	if ( lstClientes != null ) {
        	lstClientes.setSize(result.size());
        	lstClientes.invalidate();
    	}
		progress.close();
	}

    protected boolean navigationMovement(int dx, int dy, int status, int time) {
        Field field = this.getFieldWithFocus();
        if(field == lstClientes) {
        	lstClientes.invalidate(lstClientes.getSelectedIndex() + dy);
        	lstClientes.invalidate(lstClientes.getSelectedIndex());
        }
        return super.navigationMovement(dx, dy, status, time);
    }
    
	protected boolean navigationClick(int status, int time) {
		Field field = this.getFieldWithFocus();
		if(field == lstClientes) {
			seleccion();
			return true;
		}
		return super.navigationClick(status, time);
	}

    public void drawListRow(ListField list, Graphics g, int index, int y, int w) {
		Field field = this.getFieldWithFocus();
		if ( list == lstTitulo ) {
            g.setColor(Color.WHITE);
            g.setBackgroundColor(Color.GRAY);
            g.clear();
	        if( index == 0 ) {
	            g.drawText("Fecha Cliente", 0, y, DrawStyle.LEFT, w);
	            g.drawText("Estado", 0, y, DrawStyle.RIGHT, w);
	        }
		}
		if ( list == lstClientes ) {
	        if ( list.getSelectedIndex() == index && field == lstClientes ) {
	        } else {
	            g.setColor(Color.BLACK);
                g.setBackgroundColor(Estilos.getBGInterlinea(index));
	            g.clear();
	        }
//	        if( index == 0 ) {
//	        } else {
	            CanalIndustrial temp = get(index);
	            g.drawText(temp.getFechaFormato() + " " + temp.getCodigo(), 0, y, DrawStyle.LEFT, w);
	            g.drawText(temp.getEstado(), 0, y, DrawStyle.RIGHT, w);
	            g.drawText(temp.getNombre(), 0, y + this.getFont().getHeight(), DrawStyle.LEFT, w);        
//	        }
		}
    }

    public CanalIndustrial get(int index) {
        return (CanalIndustrial) canales.getObjetos().elementAt(index); // opciones[index];
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
}

