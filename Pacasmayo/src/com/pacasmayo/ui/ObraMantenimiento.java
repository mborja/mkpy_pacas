package com.pacasmayo.ui;

import java.util.Date;
import java.util.Vector;

import com.makipuray.ui.mkpyLabelEditField;
import com.makipuray.ui.mkpyLabelLabelField;
import com.makipuray.ui.mkpyStatusProgress;
import com.pacasmayo.dao.CanalIndustrialDB;
import com.pacasmayo.entidades.CanalIndustrial;
import com.pacasmayo.entidades.Obra;
import com.pacasmayo.utilidades.Estilos;
import com.pacasmayo.utilidades.Fechas;

import net.rim.device.api.system.Bitmap;
import net.rim.device.api.ui.Color;
import net.rim.device.api.ui.DrawStyle;
import net.rim.device.api.ui.Field;
import net.rim.device.api.ui.FieldChangeListener;
import net.rim.device.api.ui.Graphics;
import net.rim.device.api.ui.MenuItem;
import net.rim.device.api.ui.component.BitmapField;
import net.rim.device.api.ui.component.DateField;
import net.rim.device.api.ui.component.Dialog;
import net.rim.device.api.ui.component.EditField;
import net.rim.device.api.ui.component.ListField;
import net.rim.device.api.ui.component.ListFieldCallback;
import net.rim.device.api.ui.container.MainScreen;

public class ObraMantenimiento extends MainScreen implements ListFieldCallback, FieldChangeListener {

	private ListField lstTitulo;
  	private Vector result;
	private CanalIndustrial canalIndustrial;
	private static final int NROLINEAS = 2;
	private mkpyLabelEditField txtNombreObra = new mkpyLabelEditField("Nombre : ", "", 20, EditField.FIELD_LEFT | EditField.NO_NEWLINE | EditField.FILTER_DEFAULT, Color.BLACK, Color.WHITE);
	private mkpyLabelEditField txtDescripcionObra = new mkpyLabelEditField("Descripción : ", "", 200, EditField.FIELD_LEFT | EditField.NO_NEWLINE | EditField.FILTER_DEFAULT, Color.BLACK, Color.WHITE);
	private mkpyLabelLabelField txtFechaObra;
	private String operacion;
	private Obra obraSeleccionada;
	private int IndiceObraSeleccionada;
	
	public ObraMantenimiento(CanalIndustrial ci, String operacion) {
		this.operacion = operacion;
		this.canalIndustrial = ci;
		
		Date fecha = Fechas.stringToDate(ci.getFecha());
		txtFechaObra = new mkpyLabelLabelField("Fecha   : ", Fechas.dateToString(fecha,"dd/MM/yyyy") , 200, Color.BLACK, Color.WHITE);
					
		add(new BitmapField(Bitmap.getBitmapResource("img/titulos/planCM.png"), BitmapField.FIELD_HCENTER));
		
		result = ci.getObras();

		lstTitulo =  new ListField(1, ListField.FIELD_HCENTER | ListField.NON_FOCUSABLE);
    	lstTitulo.setCallback(this);

    	add(lstTitulo);
    	add(txtFechaObra);
    	add(txtNombreObra);
        add(txtDescripcionObra);
        
        addMenuItem(mnGrabar);
        addMenuItem(mnSalir);
	}
	
	public ObraMantenimiento(CanalIndustrial ci, String operacion, int index ) {
		this.operacion = operacion;
		this.obraSeleccionada = ci.getObraByIndex(index);
		this.canalIndustrial = ci;
		this.IndiceObraSeleccionada = index;
		
		add(new BitmapField(Bitmap.getBitmapResource("img/titulos/planCM.png"), BitmapField.FIELD_HCENTER));
		
		result = ci.getObras();

		lstTitulo =  new ListField(1, ListField.FIELD_HCENTER | ListField.NON_FOCUSABLE);
    	lstTitulo.setCallback(this);
    	
    	Date fecha = Fechas.stringToDate(ci.getFecha());
    	txtFechaObra = new mkpyLabelLabelField("Fecha :", Fechas.dateToString(fecha,"dd/MM/yyyy") , 200, Color.BLACK, Color.WHITE);
		
    	txtNombreObra.getText().setText(obraSeleccionada.getNombre());
    	txtDescripcionObra.getText().setText(obraSeleccionada.getDescripcion());

    	add(lstTitulo);
    	add(txtFechaObra);
    	add(txtNombreObra);
        add(txtDescripcionObra);
        
        addMenuItem(mnGrabar);
        addMenuItem(mnSalir);
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
	}
	
    MenuItem mnGrabar = new MenuItem("Grabar", 110, 10) {
        public void run() {
        	if(validar()){
	        	Date fecha = Fechas.stringToDate(canalIndustrial.getFecha());
	        	String sFecha = Fechas.dateToString(fecha,"yyyyMMdd");
	        	Obra obra = getObra();
	        	if(operacion=="NUEVO"){
		        	obra.setGeneradoEnCelular("X");
		        	obra.setAutocodigo();
	        	}
	        	obra.setCodigoCliente(canalIndustrial.getCodigo());
	        	obra.setFecha(sFecha);
	        	obra.setNombre(txtNombreObra.getText().getText());
	        	obra.setDescripcion(txtDescripcionObra.getText().getText());
	        	Vector listaObras = canalIndustrial.getObras();
	        	if(operacion=="NUEVO"){
	        		listaObras.addElement(obra);
	        		Dialog.inform("La obra se agregó correctamente");
	        	}else if(operacion=="MODIFICAR"){
	        		listaObras.setElementAt(obra, IndiceObraSeleccionada);
	        		Dialog.inform("La obra se modificó correctamente");
	        	}
	        	
	        	close();
        	}
        }
    };
    
    private Obra getObra(){
    	Obra obra=null;
    	if(operacion=="NUEVO"){
    		obra = new Obra();
    	}else if(operacion=="MODIFICAR"){
    		obra = obraSeleccionada;
    	}
    	return obra;
    }
    
    private boolean validar(){

    	if(txtNombreObra.getText().getText().equals("")){
    		Dialog.inform("Debe ingresar un nombre de obra");
    		txtNombreObra.setFocus();
    		return false;
    	};
    	if(txtDescripcionObra.getText().getText().equals("")){
    		Dialog.inform("Debe ingresar descripción");
    		txtDescripcionObra.setFocus();
    		return false;
    	};
    	
    	return true;
    }

    MenuItem mnSalir = new MenuItem("Salir sin grabar", 110, 10) {
        public void run() {
        	
        }
    };   
    
    protected boolean navigationMovement(int dx, int dy, int status, int time) {
        Field field = this.getFieldWithFocus();
        return super.navigationMovement(dx, dy, status, time);
    }
    
	protected boolean navigationClick(int status, int time) {
		Field field = this.getFieldWithFocus();
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

		Estilos.pushScreen(new IngresoDatosCI(canalIndustrial, null,obraSeleccionada));	
	}
	
    MenuItem mnIngresar = new MenuItem("Ingresar", 110, 10) {
        public void run() {
        	ingresoDatosCI();
        }
    };
    
    private void seleccion() {
    	
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
	public CanalIndustrial getCanalIndusstrial() {
		return canalIndustrial;
	}
	public void setCanalIndusstrial(CanalIndustrial canalIndusstrial) {
		this.canalIndustrial = canalIndusstrial;
	}	
}
