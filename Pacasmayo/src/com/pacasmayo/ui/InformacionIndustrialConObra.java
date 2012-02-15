package com.pacasmayo.ui;

import java.util.Date;
import java.util.Vector;

import com.makipuray.ui.mkpyLabelLabelField;
import com.makipuray.ui.mkpyStatusProgress;
import com.pacasmayo.dao.CanalIndustrialDB;
import com.pacasmayo.dao.InformacionCIDB;

import com.pacasmayo.dao.UsuarioDB;
import com.pacasmayo.entidades.CanalIndustrial;
import com.pacasmayo.entidades.InformacionCI;
import com.pacasmayo.entidades.Obra;
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

public class InformacionIndustrialConObra extends MainScreen implements ListFieldCallback {
    private ListField lstObras, lstTitulo;
    private mkpyStatusProgress progress = new mkpyStatusProgress("");
    private CanalIndustrial canalIndustrial;
    private Usuario usuario;
	private Vector result;
    private InformacionCIDB informaciones = new InformacionCIDB();
    private boolean refresca = false;
    private long fDesde, fHasta;
    private static final int NROLINEAS = 2;
    private int nVisitas=0;
    
    public InformacionIndustrialConObra(CanalIndustrial ci) {
    	UsuarioDB usuarios = new UsuarioDB();
    	usuario = usuarios.getUsuario();
    	usuarios = null;
    	
    	add(new BitmapField(Bitmap.getBitmapResource("img/titulos/planCM.png"), BitmapField.FIELD_HCENTER));		
    	add(new mkpyLabelLabelField("Vendedor: ", usuario.getNombre(), LabelField.NON_FOCUSABLE, Color.BLACK, Color.WHITE));
    	add(new mkpyLabelLabelField("Cliente: ", ci.getNombre(), LabelField.NON_FOCUSABLE, Color.BLACK, Color.WHITE));
    	add(new mkpyLabelLabelField("Fecha V.: ", ci.getFechaFormato(), LabelField.NON_FOCUSABLE, Color.BLACK, Color.WHITE));
		
		canalIndustrial = ci;
		result = ci.getObrasByFechaCI();
		lstTitulo =  new ListField(1, ListField.FIELD_HCENTER | ListField.NON_FOCUSABLE);
    	lstTitulo.setCallback(this);
    	lstObras = new ListField(result.size(), ListField.FIELD_HCENTER);
    	lstObras.setCallback(this);
    	lstObras.setFont(this.getFont().derive(this.getFont().getStyle(), this.getFont().getHeight() - 3));
    	lstObras.setRowHeight( this.getFont().getHeight() * NROLINEAS);
    	
    	add(lstTitulo);
        add(lstObras);
    	
        nVisitas = result.size();
        
        //if(nVisitas==0){
        	addMenuItem(mnSeleccionar);
        	addMenuItem(mnVisita);
        //}

        addMenuItem(mnEliminar);
        lstObras.select(true);

    }
    
    MenuItem mnIngresar = new MenuItem("Ingresar Obra", 110, 10) {
        public void run() {
        	Estilos.pushScreen(new ObraMantenimiento(canalIndustrial,"NUEVO"));
        }
    };

    MenuItem mnModificar = new MenuItem("Modificar Obra", 110, 10) {
        public void run() {
        	Estilos.pushScreen(new ObraMantenimiento(canalIndustrial,"MODIFICAR",lstObras.getSelectedIndex()));
        }
    };
    
    MenuItem mnEliminar = new MenuItem("Eliminar Obra", 110, 10) {
        public void run() {
        	if(Dialog.ask(Dialog.D_YES_NO, "¿Está seguro de borrar la obra?") == Dialog.YES ){
        		Vector v = canalIndustrial.getObras();
        		v.removeElementAt(lstObras.getSelectedIndex());
        		canalIndustrial.setObras(v);
        		Dialog.inform("La obra fue borrada");
        	};
        	
        }
    };
    
    MenuItem mnVisita = new MenuItem("Visita", 110, 10) {
        public void run() {
        	if(lstObras.getSelectedIndex()>=0){
	    		long lngFecha = Long.parseLong(canalIndustrial.getFecha());
	    		long lngHoy = Long.parseLong(Fechas.dateToString("yyyyMMdd"));
	    		if ( lngFecha + 1 < lngHoy ) {
	    			Dialog.inform("No se pueden registrar visitas anteriores a ayer.");
	    			return; ///MB descomentar cuando se terminen las pruebas
	    		}
	        	refresca = true;
	        	Obra obra = seleccion();
	        	Estilos.pushScreen(new InformacionIndustrialPorObra(canalIndustrial,lngFecha,lngHoy,obra));
	    		//Estilos.pushScreen(new IngresoDatosCI(canalIndustrial, null));
        	}else{
        		Dialog.inform("Debe seleccionar una obra");
        	}
        }
    };
    
    MenuItem mnSeleccionar = new MenuItem("Asociar Obra", 110, 10) {
        public void run() {
        	Estilos.pushScreen(new InformacionIndustrialConObraSeleccionar(canalIndustrial));
        }
    };
    
    public void onExposed(){
    	
    	int indice = lstObras.getSelectedIndex();
    
        lstObras.setSize(0);
        delete(lstObras);
        lstObras.invalidate();
        
        //CanalIndustrialDB canalI = new CanalIndustrialDB();
        //CanalIndustrial ci = canalI.getByIndex(canalI.getIndexById(canalIndustrial.getCodigo()));
        //canalIndustrial = ci ; 
		result = canalIndustrial.getObrasByFechaCI();
		
    	lstObras = new ListField(result.size(), ListField.FIELD_HCENTER);
    	lstObras.setCallback(this);
    	lstObras.setFont(this.getFont().derive(this.getFont().getStyle(), this.getFont().getHeight() - 3));
    	lstObras.setRowHeight( this.getFont().getHeight() * NROLINEAS);
        add(lstObras);
        lstObras.setSelectedIndex(indice);
    }

    public void drawListRow(ListField list, Graphics g, int index, int y, int w) {
		Field field = this.getFieldWithFocus();
		if ( list == lstTitulo ) {
            g.setColor(Color.WHITE);
            g.setBackgroundColor(Color.GRAY);
            g.clear();
	        if( index == 0 ) {
	            g.drawText("Fecha Nombre", 0, y, DrawStyle.LEFT, w);
	            g.drawText("Descripcion", 0, y, DrawStyle.RIGHT, w);
	        }
		}
		if ( list == lstObras ) {
	        if ( list.getSelectedIndex() == index && field == lstObras ) {
	        } else {
	            g.setColor(Color.BLACK);
                g.setBackgroundColor(Estilos.getBGInterlinea(index));
	            g.clear();
	        }
            Obra obra = get(index);
            Date obraFechaDate = Fechas.stringToDate(obra.getFecha());
            String obraFechaString = Fechas.dateToString(obraFechaDate,"dd/MM/yyyy");	
          
            g.drawText( obraFechaString + " " +obra.getNombre(), 0, y, DrawStyle.LEFT, w); 
            g.drawText(obra.getDescripcion(), 0, y + this.getFont().getHeight(), DrawStyle.LEFT, w);        
		}
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
	
	private void ingresoDatosCI()
	{
		long lngFecha = Long.parseLong(canalIndustrial.getFecha());
		long lngHoy = Long.parseLong(Fechas.dateToString("yyyyMMdd"));
		if ( lngFecha + 1 < lngHoy ) {
			Dialog.inform("No se pueden registrar visitas anteriores a ayer.");
			//return; MB descomentar cuando se terminen las pruebas
		}
        Obra obraEnvio = (Obra)result.elementAt(lstObras.getSelectedIndex());
		Estilos.pushScreen(new IngresoDatosCI(canalIndustrial, null, obraEnvio));	
	}
	    
    private Obra seleccion() {
    	Obra obra = null;
    	int index = lstObras.getSelectedIndex(); 
    	if( index >= 0 ) {
    		obra = get(index);
    	}
    	return obra;
    	
    }
    
	public Obra get(int index) {
		 return (Obra) canalIndustrial.getObras().elementAt(index);
	}
	public int getPreferredWidth(ListField listField) {
		// TODO Auto-generated method stub
		return 0;
	}
	public int indexOfList(ListField listField, String prefix, int start) {
		// TODO Auto-generated method stub
		return 0;
	}
	public void fieldChanged(Field field, int context) {
		// TODO Auto-generated method stub
		
	}
    public Object get(ListField listField, int index) {
        return null;
    }
	public CanalIndustrial getCanalIndustrial() {
		return canalIndustrial;
	}
	public void setCanalIndusstrial(CanalIndustrial canalIndusstrial) {
		this.canalIndustrial = canalIndusstrial;
	}	    
}
