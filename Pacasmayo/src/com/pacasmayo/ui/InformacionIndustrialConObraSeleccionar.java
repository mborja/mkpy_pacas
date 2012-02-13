
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
import net.rim.device.api.ui.component.EditField;
import net.rim.device.api.ui.component.LabelField;
import net.rim.device.api.ui.component.ListField;
import net.rim.device.api.ui.component.ListFieldCallback;
import net.rim.device.api.ui.container.HorizontalFieldManager;
import net.rim.device.api.ui.container.MainScreen;
import net.rim.device.api.ui.container.VerticalFieldManager;

import com.pacasmayo.dao.ObraDB;
import com.pacasmayo.dao.UsuarioDB;
import com.pacasmayo.entidades.CanalIndustrial;
import com.pacasmayo.entidades.Obra;

import com.pacasmayo.entidades.Usuario;
import com.pacasmayo.utilidades.Estilos;
import com.pacasmayo.utilidades.Fechas;
import com.pacasmayo.utilidades.Sistema;
import com.makipuray.ui.mkpyLabelEditField;
import com.makipuray.ui.mkpyLabelField;
import com.makipuray.ui.mkpyLabelLabelField;
import com.makipuray.ui.mkpyStatusProgress;

public class InformacionIndustrialConObraSeleccionar extends MainScreen implements ListFieldCallback, FieldChangeListener {        
	private mkpyLabelEditField txtBuscar = new mkpyLabelEditField("Filtrar:", "", 200, EditField.FIELD_LEFT | EditField.NO_NEWLINE | EditField.FILTER_DEFAULT, Color.BLACK, Color.WHITE);
	private ButtonField btnBuscar = new ButtonField("    Filtrar    ", ButtonField.FIELD_HCENTER | ButtonField.CONSUME_CLICK | ButtonField.USE_ALL_WIDTH);
    private ListField lstObras, lstTitulo;
    private mkpyStatusProgress progress = new mkpyStatusProgress("");   
	private ObraDB obras = new ObraDB();
	private Vector result;
	private Usuario usuario;
	private UsuarioDB usuarios = new UsuarioDB();
	private static final int NROLINEAS = 2;
	private String sTextoBusqueda="";
	private int liContObras =0;
    private CanalIndustrial canalIndustrial;

    protected boolean onSavePrompt() {
           return true;
    }
    
    private Obra seleccion() {
    	Obra obra = null;
    	int index = lstObras.getSelectedIndex(); 
    	if( index >= 0 ) {
    		obra = get(index);
    	}
    	return obra;
    }            
       
    public InformacionIndustrialConObraSeleccionar(CanalIndustrial ci) {
        add(new BitmapField(Bitmap.getBitmapResource("img/titulos/planCM.png"), BitmapField.FIELD_HCENTER));
        add(txtBuscar);
		btnBuscar.setChangeListener(this);
    	add(btnBuscar);
    	
    	usuario = usuarios.getUsuario();
    	canalIndustrial = ci;
    	buscar();
    	lstTitulo =  new ListField(1, ListField.FIELD_HCENTER | ListField.NON_FOCUSABLE);
    	lstTitulo.setCallback(this);
    	//lstObras = new ListField(result.size(), ListField.FIELD_HCENTER);
    	lstObras = new ListField(liContObras, ListField.FIELD_HCENTER);
    	lstObras.setCallback(this);
    	lstObras.setFont(this.getFont().derive(this.getFont().getStyle(), this.getFont().getHeight() - 3));
    	lstObras.setRowHeight( this.getFont().getHeight() * NROLINEAS);
        add(lstTitulo);
        add(lstObras);
        
        addMenuItem(mnGrabar);
        addMenuItem(mnSalir);
    }
 
    MenuItem mnGrabar = new MenuItem("Asociar Obra a Cliente", 110, 10) {
        public void run() {
//        	if(validar()){
	        	Date fecha = Fechas.stringToDate(canalIndustrial.getFecha());
	        	String sFecha = Fechas.dateToString(fecha,"yyyyMMdd");
	        	Obra obra = new Obra();
	        	Obra obraSeleccion = seleccion();

		        obra.setGeneradoEnCelular("X");
		        obra.setCodigo(obraSeleccion.getCodigo());
	        	obra.setCodigoCliente(canalIndustrial.getCodigo());
	        	obra.setFecha(sFecha);
	        	obra.setNombre(obraSeleccion.getNombre());
	        	obra.setDescripcion(obraSeleccion.getDescripcion());
	        	Vector listaObrasCliente = canalIndustrial.getObras();
	        	
	        	listaObrasCliente.addElement(obra);
	        		canalIndustrial.setObras(listaObrasCliente );
	        		Dialog.inform("La obra se asoció correctamente");      	
	        	close();
//        	}
        }
    };

    MenuItem mnSalir = new MenuItem("Salir sin grabar", 110, 10) {
        public void run() {
        	
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
		
		liContObras=0;
		sTextoBusqueda= txtBuscar.getText().getText().toUpperCase();
		
		if(sTextoBusqueda.equals("")==false){
		    	
    	int n = obras.getObjetos().size();
    	result = new Vector();
    	//result.addElement(new CanalMasivo());
    	Obra lsObra = new Obra();
		String lsNombre="";
		//int liCont =0;
		
    	for ( int i = 0; i < n; i++) {
    		lsObra = (Obra)obras.getObjetos().elementAt(i);
    		lsNombre = lsObra.getNombre();
    		if(lsNombre.startsWith(sTextoBusqueda))
    		{
    		result.addElement(obras.getObjetos().elementAt(i));
    		liContObras++;
    		}
    	}
    	if ( lstObras != null ) {
    		//lstObras.setSize(result.size());
    		lstObras.setSize(liContObras);
    		lstObras.invalidate();
    	}
		}
		progress.close();
	}

    protected boolean navigationMovement(int dx, int dy, int status, int time) {
        Field field = this.getFieldWithFocus();
        if(field == lstObras) {
        	lstObras.invalidate(lstObras.getSelectedIndex() + dy);
        	lstObras.invalidate(lstObras.getSelectedIndex());
        }
        return super.navigationMovement(dx, dy, status, time);
    }
    
	protected boolean navigationClick(int status, int time) {
		Field field = this.getFieldWithFocus();
		if(field == lstObras) {
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
	            g.drawText("Codigo", 0, y, DrawStyle.LEFT, w);
	            g.drawText("Nombre Obra", 0, y, DrawStyle.RIGHT, w);
	        }
		}
		if ( list == lstObras ) {
			
			

        		Obra temp = get(index);
        		
        			  if ( list.getSelectedIndex() == index && field == lstObras ) {
              			
        		      }
        			  else {
        		            g.setColor(Color.BLACK);
        	                g.setBackgroundColor(Estilos.getBGInterlinea(index));
        		            g.clear();
        			  }
        			  
        			  g.drawText(temp.getCodigo(), 0, y, DrawStyle.LEFT, w);
            		  g.drawText(temp.getNombre().substring(0, 20), 0, y, DrawStyle.RIGHT, w);
        		
        		
        	
//        	if(sTextoBusqueda.trim().equals("")==false){
//        		
//        		Obra temp = get(index);
//        		String sNombre = temp.getNombre();
//        		
//        		if(sNombre.startsWith(sTextoBusqueda)){
//        			  if ( list.getSelectedIndex() == index && field == lstObras ) {
//        					
//              			g.drawText(temp.getCodigo(), 0, y, DrawStyle.LEFT, w);
//              			sNombre = sNombre.substring(0,20);
//              			g.drawText(sNombre, 0, y, DrawStyle.LEFT, w);
//        		      }
//        			  else {
//        		            g.setColor(Color.BLACK);
//        	                g.setBackgroundColor(Estilos.getBGInterlinea(index));
//        		            g.clear();
//        		            
//        		            
//        			  }
//        			  
//        		
//        		}
//        	}
        	
			
	      
//	        if( index == 0 ) {
//	        } else {
	        
	        
		}
    }

    public Obra get(int index) {
        return (Obra) obras.getObjetos().elementAt(index); // opciones[index];
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

