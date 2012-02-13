package com.pacasmayo.ui;

import net.rim.device.api.system.Bitmap;
import net.rim.device.api.ui.Color;
import net.rim.device.api.ui.Field;
import net.rim.device.api.ui.Graphics;
import net.rim.device.api.ui.MenuItem;
import net.rim.device.api.ui.component.BitmapField;
import net.rim.device.api.ui.component.Dialog;
import net.rim.device.api.ui.component.LabelField;
import net.rim.device.api.ui.component.ListField;
import net.rim.device.api.ui.component.ListFieldCallback;
import net.rim.device.api.ui.container.HorizontalFieldManager;
import net.rim.device.api.ui.container.MainScreen;
import net.rim.device.api.ui.container.VerticalFieldManager;

import com.pacasmayo.dao.UsuarioDB;
import com.pacasmayo.dao.VendedorDB;
import com.pacasmayo.entidades.Usuario;
import com.pacasmayo.entidades.Vendedor;
import com.pacasmayo.utilidades.Estilos;
import com.pacasmayo.utilidades.Sistema;
import com.makipuray.ui.mkpyLabelField;

public class MenuOpciones extends MainScreen implements ListFieldCallback {        
    private ListField menu;
    private String[] opciones = {"Plan de visitas", " Canal masivo", " Canal industrial", "Sincronización", " Envío de pendientes"} ;
    private Usuario usuario;
    private Vendedor vendedor;

    public boolean onClose() {
    	if(Dialog.ask(Dialog.D_YES_NO, "Desea salir del aplicativo?") != Dialog.NO)
    		System.exit(0);
    	return true;
    };
    
    private void seleccion() {
    	if( menu.getSelectedIndex() == 0 ) { // Plan de visitas
        	//
    	} else if( menu.getSelectedIndex() == 1  ) { // Canal Masivo
    		if(vendedor.getTipoVendedor().equals("1") || vendedor.getTipoVendedor().equals("3")){
    		   Estilos.pushScreen(new ClientesMasivo());
    		}else{
    			Dialog.inform("No tiene acceso a clientes Masivos");
    		}
    	} else if( menu.getSelectedIndex() == 2 ) { // Canal Industrial
    		if(vendedor.getTipoVendedor().equals("2") || vendedor.getTipoVendedor().equals("3") ){
    			Estilos.pushScreen(new ClientesIndustrial());
     		}else{
     			Dialog.inform("No tiene acceso a clientes Industriales");
     		}
    	} else if( menu.getSelectedIndex() == 3 ) { // Sincronización
        	//
    	} else if( menu.getSelectedIndex() == 4 ) { // Envío de pendientes
    		Estilos.pushScreen(new Sincronizacion());
    	}
    }            
       
    public MenuOpciones() {
        VerticalFieldManager vField = new VerticalFieldManager();
        UsuarioDB usuarios = new UsuarioDB();
    	usuario = usuarios.getUsuario();
    	usuarios = null;
    	VendedorDB vendedores = new VendedorDB();
    	vendedor = vendedores.getVendedor();
//    	setTitle("Menú de opciones");
        
        HorizontalFieldManager hField = new HorizontalFieldManager() {
    		protected void sublayout(int width, int height) {
    			super.sublayout(super.getScreen().getWidth(), height);
    			super.setExtent(super.getScreen().getWidth(), getField(1).getHeight());
    			Field field;

    			field = getField(0);
                layoutChild(field, width / 2, height);
    			//setPositionChild(field, (width / 4) - ( field.getPreferredWidth() / 2), 0);
    			setPositionChild(field, 5, 0);

    			field = getField(1);
                layoutChild(field, width, height);
    			setPositionChild(field, (width / 2) + (width / 4) - ( field.getPreferredWidth() / 2), 0);

        	}        	
        };
        
		vField.add(new LabelField("", LabelField.FIELD_LEFT));
		vField.add(new mkpyLabelField(usuario.getNombre(), LabelField.ELLIPSIS | LabelField.FIELD_LEFT, Color.BLACK, Color.WHITE, true));
		
		hField.add(vField);
		hField.add(new BitmapField(Bitmap.getBitmapResource("img/logo.png"), BitmapField.FIELD_LEFT | BitmapField.FIELD_VCENTER));

        menu = new ListField(opciones.length, ListField.FIELD_HCENTER);
        menu.setCallback(this);

        add(new BitmapField(Bitmap.getBitmapResource("img/titulos/menu.png"), BitmapField.FIELD_HCENTER));
        add(hField);
        add(menu);
        addMenuItem(mnAcerca);

    }
    
    MenuItem mnAcerca = new MenuItem ("Acerca de ...", 110, 10) {
        public void run() {
        	Dialog.inform("Pacasmyo Ver. " + Sistema.getVersion());
        }
    };

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
			seleccion();
			return true;
		}
		return super.navigationClick(status, time);
	}

    public void drawListRow(ListField list, Graphics g, int index, int y, int w) {        
    	if ( list.getSelectedIndex() == index ) {
        } else {
            if( index == 0 || index == 3 ) {
                g.setColor(Color.WHITE);
                g.setBackgroundColor(Color.GRAY);
                //g.setBackgroundColor(Estilos.getBGModulo());
            } else  {
	            g.setColor(Color.BLACK);
                g.setBackgroundColor(Estilos.getBGInterlinea(0));
                //g.setBackgroundColor(Estilos.getBGSubModulo());
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
}
