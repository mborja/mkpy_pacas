package com.pacasmayo.ui;

import java.util.Date;
import java.util.Vector;

import com.makipuray.ui.mkpyLabelChoiceField;
import com.makipuray.ui.mkpyLabelEditField;
import com.makipuray.ui.mkpyLabelLabelField;
import com.makipuray.ui.mkpyStatusProgress;
import com.pacasmayo.dao.InformacionCIDB;
import com.pacasmayo.dao.MarcaDBCI;
import com.pacasmayo.dao.UsuarioDB;
import com.pacasmayo.dao.TipoObraDB;
import com.pacasmayo.dao.ProductoDB;
import com.pacasmayo.dao.ProveedorDB;
import com.pacasmayo.dao.FrecuenciaDB;
import com.pacasmayo.dao.UnidadMedidaDB;

import com.pacasmayo.entidades.CanalIndustrial;
import com.pacasmayo.entidades.InformacionCI;
import com.pacasmayo.entidades.MarcaCI;
import com.pacasmayo.entidades.TipoObra;
import com.pacasmayo.entidades.Producto;
import com.pacasmayo.entidades.Frecuencia;
import com.pacasmayo.entidades.Proveedor;
import com.pacasmayo.entidades.UnidadMedida;
import com.pacasmayo.entidades.Usuario;
import com.pacasmayo.entidades.Obra;
import com.pacasmayo.utilidades.Fechas;

import net.rim.device.api.system.Bitmap;
import net.rim.device.api.ui.Color;
import net.rim.device.api.ui.Field;
import net.rim.device.api.ui.FieldChangeListener;
import net.rim.device.api.ui.FocusChangeListener;
import net.rim.device.api.ui.MenuItem;
import net.rim.device.api.ui.component.BitmapField;
import net.rim.device.api.ui.component.DateField;
import net.rim.device.api.ui.component.Dialog;
import net.rim.device.api.ui.component.EditField;
import net.rim.device.api.ui.component.ObjectChoiceField;
import net.rim.device.api.ui.container.MainScreen;

public class IngresoDatosCI extends MainScreen implements FieldChangeListener, FocusChangeListener {
    private mkpyStatusProgress progress = new mkpyStatusProgress("");
	private InformacionCI informacion;
	private CanalIndustrial canalIndustrial;
	private Obra obra;
	private InformacionCIDB informes = new InformacionCIDB();
	private Usuario usuario;
	private MarcaDBCI marcas = new MarcaDBCI();
	private TipoObraDB tipoObras = new TipoObraDB();
	private ProductoDB productos = new ProductoDB();
	private UnidadMedidaDB unidades = new UnidadMedidaDB();
	private ProveedorDB proveedores = new ProveedorDB();
	private FrecuenciaDB frecuencias = new FrecuenciaDB();
	
	private mkpyLabelChoiceField cboMarca;
	private mkpyLabelChoiceField cboTipoObra;
	private mkpyLabelChoiceField cboProducto;
	private mkpyLabelChoiceField cboProveedor;
	private mkpyLabelEditField txtCantidad = new mkpyLabelEditField("Cantidad:", "", 15, EditField.FIELD_LEFT | EditField.NO_NEWLINE | EditField.FILTER_REAL_NUMERIC, Color.BLACK, Color.WHITE);
	private mkpyLabelChoiceField cboUnidadMedida;
	private mkpyLabelChoiceField cboFrecuencia;
	private mkpyLabelEditField txtPrecioCompra = new mkpyLabelEditField("Precio de Compra:", "", 15, EditField.FIELD_LEFT | EditField.NO_NEWLINE | EditField.FILTER_NUMERIC, Color.BLACK, Color.WHITE);
	private mkpyLabelEditField txtEstatus = new mkpyLabelEditField("Estado:", "", 15, EditField.FIELD_LEFT | EditField.NO_NEWLINE | EditField.FILTER_NUMERIC, Color.BLACK, Color.WHITE);
	private mkpyLabelEditField txtResidente = new mkpyLabelEditField("Residente:", "", 15, EditField.FIELD_LEFT | EditField.NO_NEWLINE | EditField.FILTER_DEFAULT , Color.BLACK, Color.WHITE);
	private mkpyLabelEditField txtObs = new mkpyLabelEditField("Observacion:", "", 200, EditField.FIELD_LEFT | EditField.NO_NEWLINE | EditField.FILTER_DEFAULT, Color.BLACK, Color.WHITE);
	
	private mkpyLabelLabelField txtProducto;
	private mkpyLabelLabelField txtMarca;
	
	
	private DateField txtFechaObra = new DateField("Fecha :", (new Date()).getTime(), DateField.DATE | DateField.USE_ALL_WIDTH);
	
    public IngresoDatosCI(CanalIndustrial ci, InformacionCI inf, Obra obra) {
    	this.canalIndustrial = ci;
    	this.informacion = inf;
    	this.obra = obra;
    	UsuarioDB usuarios = new UsuarioDB();
    	this.usuario = usuarios.getUsuario();
    	usuarios = null;
        add(new BitmapField(Bitmap.getBitmapResource("img/titulos/IngresoCM.png"), BitmapField.FIELD_HCENTER));
     	        
   		cboTipoObra = new mkpyLabelChoiceField("Tipo Obra: ", mostrarTiposObra(), 0, ObjectChoiceField.FIELD_RIGHT | ObjectChoiceField.FORCE_SINGLE_LINE, Color.BLACK, Color.WHITE);    	
   		cboTipoObra.setChangeListener(this);
   		
   		if(inf!=null){
   			txtProducto = new mkpyLabelLabelField("Producto:", inf.getProducto() , 200, Color.BLACK, Color.WHITE);
   			txtMarca = new mkpyLabelLabelField("Marca:", inf.getMarca(), 200, Color.BLACK, Color.WHITE);
   		}else{
	   		cboProducto = new mkpyLabelChoiceField("Producto: ", mostrarProductos(), 0, ObjectChoiceField.FIELD_RIGHT | ObjectChoiceField.FORCE_SINGLE_LINE, Color.BLACK, Color.WHITE);   		
	   		cboProducto.setChangeListener(this);
	   		cboProducto.getOpciones().setChangeListener(this);
	   		cboMarca = new mkpyLabelChoiceField("Marca: ", mostrarMarcas((Producto) productos.getObjetos().elementAt(0)), 0, ObjectChoiceField.FIELD_RIGHT | ObjectChoiceField.FORCE_SINGLE_LINE, Color.BLACK, Color.WHITE);
	   		cboMarca.setChangeListener(this);
   		}
   		cboUnidadMedida = new mkpyLabelChoiceField("Unidad de Medida: ", mostrarUnidadesMedida(), 0, ObjectChoiceField.FIELD_RIGHT | ObjectChoiceField.FORCE_SINGLE_LINE, Color.BLACK, Color.WHITE);
   		cboUnidadMedida.setChangeListener(this);
   		cboProveedor = new mkpyLabelChoiceField("Proveedor: ", mostrarProveedor(), 0, ObjectChoiceField.FIELD_RIGHT | ObjectChoiceField.FORCE_SINGLE_LINE, Color.BLACK, Color.WHITE);
   		cboProveedor.setChangeListener(this);
   		cboFrecuencia = new mkpyLabelChoiceField("Frecuencia: ", mostrarFrecuencia(), 0, ObjectChoiceField.FIELD_RIGHT | ObjectChoiceField.FORCE_SINGLE_LINE, Color.BLACK, Color.WHITE);
   		cboFrecuencia.setChangeListener(this);
    	
   		add(cboTipoObra);
   		if(inf!=null){
   			add(txtProducto);
	   		add(txtMarca);
   		}else{
	   		add(cboProducto);
	   		add(cboMarca);
   		}
     	add(txtCantidad);
    	add(cboUnidadMedida);
    	add(cboFrecuencia);
    	add(cboProveedor);
    	add(txtPrecioCompra);
    	add(txtEstatus);
    	add(txtFechaObra);
    	add(txtResidente);
    	add(txtObs);

    	addMenuItem(mnGrabar);

    	if ( informacion != null ) {
    		cboTipoObra.setSelectedIndex(tipoObras.getIndexById( informacion.getIdTipoObra().toString() )); 		
    		//cboMarca.setSelectedIndex(marcas.getIndexById( informacion.getIdMarca().toString() ))   ;
    		//cboProducto.setSelectedIndex(productos.getIndexById( informacion.getIdProducto().toString() ));
    		cboUnidadMedida.setSelectedIndex(unidades.getIndexById( informacion.getIdUnidadMedida().toString() ));
       		
    		if(informacion.getIdFrecuencia()==" ") {
    			cboFrecuencia.setSelectedIndex(0);
    		}
    		else
    		{	
    			int contador = cboFrecuencia.getOpciones().getSize();
    			for(int i=1;i<contador;i++){
    				if(cboFrecuencia.getOpciones().getChoice(i).equals(informacion.getFrecuencia() ) ){
    					cboFrecuencia.setSelectedIndex(i);
    				}
    			}

    		}
    		
    		if(informacion.getIdProveedor()==" ") {
    			cboProveedor.setSelectedIndex(0);
    		}
    		else
    		{	
    			int contador = cboProveedor.getOpciones().getSize();
    			for(int i=1;i<contador;i++){
    				if(cboProveedor.getOpciones().getChoice(i).equals(informacion.getProveedor() ) ){
    					cboProveedor.setSelectedIndex(i);
    				}
    			}

    		}
    		       		
    		txtCantidad.getText().setText(informacion.getCantidad());
      		txtPrecioCompra.getText().setText(informacion.getPreciocompra());
    		txtEstatus.getText().setText(informacion.getEstado());
    		
    		//txtFechaObra.setDate(informacion.getFechafinobra());
    		
    		txtResidente.getText().setText(informacion.getResidente());
    		txtObs.getText().setText(informacion.getObservacion());
    		
    		Date fechafinobra = new Date();
    		
    		if(obra!=null){
	    		fechafinobra = Fechas.stringToDate(informacion.getFechafinobra());
	    		txtFechaObra.setDate(fechafinobra);
    		}
    	}
    	
    }

    MenuItem mnGrabar = new MenuItem("Grabar", 110, 10) {
        public void run() {
        	
        	if(txtCantidad.getText().getText().length()==0){
        		txtCantidad.setFocus();
        		Dialog.inform("Debe ingresar una cantidad");
        		return;
        	}
        			
        	if(txtPrecioCompra.getText().getText().length()==0){
        		txtPrecioCompra.setFocus();
        		Dialog.inform("Debe ingresar precio de compra");
        		return;
        	}
        	
        	if(txtEstatus.getText().getText().length()==0){
        		txtEstatus.setFocus();
        		Dialog.inform("Debe ingresar estatus");
        		return;
        	}
        	
        	if(txtResidente.getText().getText().length()==0){
        		txtResidente.setFocus();
        		Dialog.inform("Debe ingresar Residente");
        		return;
        	}
        	
        	if(txtObs.getText().getText().length()==0){
        		txtObs.setFocus();
        		Dialog.inform("Debe ingresar una observacion");
        		return;
        	}
        	
        	boolean nuevo = false;
    		String resultado = "";
    		TipoObra tipoObra = (TipoObra) tipoObras.getObjetos().elementAt(cboTipoObra.getSelectedIndex());
    		UnidadMedida unidadMedida = (UnidadMedida) unidades.getObjetos().elementAt(cboUnidadMedida.getSelectedIndex());
    		Proveedor proveedor ;
    		Frecuencia frecuencia ;
    		if(cboProveedor.getSelectedIndex()>0){
    			proveedor = (Proveedor) proveedores.getObjetos().elementAt(cboProveedor.getSelectedIndex()-1);
    		}else{
    			proveedor = new Proveedor();
    			proveedor.setCodigo(" ");
    			proveedor.setDescripcion("No seleccionado");
    		}
    		if(cboFrecuencia.getSelectedIndex()>0){
    			frecuencia = (Frecuencia) frecuencias.getObjetos().elementAt(cboFrecuencia.getSelectedIndex()-1);
    		}else{
    			frecuencia = new Frecuencia();
    			frecuencia.setCodigo(" ");
    			frecuencia.setDescripcion("No seleccionado");
    		}
    		//TODO: validar los ingresos
    		long lngFecha = Long.parseLong(canalIndustrial.getFecha());
    		long lngHoy = Long.parseLong(Fechas.dateToString("yyyyMMdd"));
    		if ( lngFecha + 1 < lngHoy ) {
    			Dialog.inform("No se pueden registrar visitas anteriores de ayer.");
    			//return; MB descomentar cuando se terminen las pruebas
    		}
    		
//    		String fecha = "";
//    		if ( informacion != null ) {
//    			fecha = informacion.getFecha();
//    		} else {
//    			fecha = canalIndustrial.getFecha();
//    		}
        	
    		progress.open();
    		progress.setTitle("Grabando...");
    		if ( informacion == null ) {
    			nuevo = true;
    			informacion = new InformacionCI();
    		}
    		informacion.setIdTipoObra(tipoObra.getCodigo());
    		informacion.setTipoObra(tipoObra.getDescripcion());
    		
    		if(nuevo){

    			Producto producto;	
	    		for(int i=0;i<productos.getObjetos().size();i++){
	    			producto = (Producto) productos.getObjetos().elementAt(i);
	    			if(producto.getDescripcion() == cboProducto.getOpciones().getChoice(cboProducto.getSelectedIndex())){
	    	    		informacion.setIdProducto(producto.getCodigo());
	    	    		informacion.setProducto(producto.getDescripcion());	
	    			}
	    		}
    			
    			MarcaCI marca;	
	    		for(int i=0;i<marcas.getObjetos().size();i++){
	    			marca = (MarcaCI) marcas.getObjetos().elementAt(i);
	    			if(marca.getDescripcion() == cboMarca.getOpciones().getChoice(cboMarca.getSelectedIndex())){
	    	    		informacion.setIdMarca(marca.getCodigo());
	    	    		informacion.setMarca(marca.getDescripcion());	
	    			}
	    		}
    		}
    		informacion.setIdUnidadMedida(unidadMedida.getCodigo());
    		informacion.setUnidadMedida(unidadMedida.getDescripcion());
    		
    		informacion.setIdProveedor(proveedor.getCodigo());
    		informacion.setProveedor(proveedor.getDescripcion());
    		
    		informacion.setIdFrecuencia(frecuencia.getCodigo());
    		informacion.setFrecuencia(frecuencia.getDescripcion());
    		
    		informacion.setIdCliente(canalIndustrial.getCodigo());    		
    		informacion.setCantidad(txtCantidad.getText().getText());
    		informacion.setPreciocompra(txtPrecioCompra.getText().getText()) ;
    		informacion.setEstado(txtEstatus.getText().getText()) ;
    		informacion.setResidente(txtResidente.getText().getText());
    		informacion.setObservacion(txtObs.getText().getText());
    		
    		if (obra!=null){
    			informacion.setIdObra(obra.getCodigo());
    			informacion.setObra(obra.getNombre());
    			informacion.setObraDescripcion(obra.getDescripcion());
    			informacion.setCreadoEnCelular(obra.getGeneradoEnCelular());
    			Date fechaObra = Fechas.stringToDate(obra.getFecha()) ;
        		Date fechafinobra = new Date( txtFechaObra.getDate());
        		informacion.setFechafinobra( Fechas.dateToString(fechafinobra,"yyyyMMdd") );
        		informacion.setFecha(Fechas.dateToString(fechaObra,"yyyyMMdd"));
        		    			
    		}else{
    			informacion.setIdObra(null);
    			Date fechafinobra = new Date( txtFechaObra.getDate());
        		informacion.setFechafinobra( Fechas.dateToString(fechafinobra,"yyyyMMdd") );
        		Date fechaObra = Fechas.stringToDate(canalIndustrial.getFecha()) ;
        		informacion.setFecha(Fechas.dateToString(fechaObra,"yyyyMMdd"));
    		}
    		
    		//999326702
    		
    		informacion.setEnviado(false);
    		
    		if ( nuevo == true ) {
            	informes.getObjetos().addElement(informacion);
    		}
        	informes.commitChanges();
			resultado = "Se grabó con éxito";
//    		progress.setTitle("Enviando...");
//    		if ( informes.putRemote(informacion) ) {
//    			resultado = "Envío exitoso";
//    		} else {
//    			resultado = "Envío NO exitoso, reintente más tarde. " + informes.getMsgError();
//    		}
    		progress.close();
    		Dialog.inform(resultado);
    		close();
        }
    };
    
    public String[] mostrarMarcas(Producto producto ) {
        Vector vMarcas = marcas.getObjetos();
        
        int n = vMarcas.size();
        int indice = 0;
        int contador=0;
        for (int i = 0; i < n; i++) {
        	MarcaCI marca = (MarcaCI) vMarcas.elementAt(i);
        	if(producto.getCodigo().equals(marca.getCodigoProducto())){
            	contador++;
        	}
        }
        
        String[] sMarcas = new String[contador];
        for (int i = 0; i < n; i++) {
        	MarcaCI marca = (MarcaCI) vMarcas.elementAt(i);
        	if(producto.getCodigo().equals(marca.getCodigoProducto())){
        		sMarcas[indice] = marca.getDescripcion();
        		indice++;
        	}
        }
        return sMarcas;
    }
    
    public String[] mostrarTiposObra() {
        Vector vTipoObra = tipoObras.getObjetos();
        String[] sTipoObras = new String[vTipoObra.size()];
        int n = vTipoObra.size();
        for (int i = 0; i < n; i++) {
        	TipoObra tipoObra = (TipoObra) vTipoObra.elementAt(i);
        	sTipoObras[i] = tipoObra.getDescripcion();
        }
        return sTipoObras;
    }

    public String[] mostrarProductos() {
        Vector vProducto = productos.getObjetos();
        String[] sProducto = new String[vProducto.size()];
        int n = vProducto.size();

        for (int i = 0; i < n; i++) {
        	Producto producto = (Producto) vProducto.elementAt(i);
        	sProducto[i] = producto.getDescripcion();
        }

        return sProducto;
    }

    public String[] mostrarFrecuencia() {
        Vector vFrecuencia = frecuencias.getObjetos();
        String[] sFrecuencia = new String[vFrecuencia.size()+1];
        int n = vFrecuencia.size();
        sFrecuencia[0] = "No seleccionado";
        for (int i = 0; i < n; i++) {
        	Frecuencia frecuencia = (Frecuencia) vFrecuencia.elementAt(i);
        	sFrecuencia[i+1] = frecuencia.getDescripcion();
        }
        return sFrecuencia;
    }    
    
    public String[] mostrarUnidadesMedida() {
        Vector vUnidad = unidades.getObjetos();
        String[] sUnidad = new String[vUnidad.size()];
        
        int n = vUnidad.size();
        for (int i = 0; i < n; i++) {
        	UnidadMedida unidad = (UnidadMedida) vUnidad.elementAt(i);
        	sUnidad[i] = unidad.getDescripcion();
        } 
    	
    	return sUnidad;
    }
    
    public String[] mostrarProveedor() {
        Vector vProveedor = proveedores.getObjetos();
        String[] sProveedor = new String[vProveedor.size()+1];
        sProveedor[0] = "No seleccionado";
        int n = vProveedor.size();
        for (int i = 0; i < n; i++) {
        	Proveedor proveedor = (Proveedor) vProveedor.elementAt(i);
        	sProveedor[i+1] = proveedor.getDescripcion();
        }
        
        return sProveedor;
    }
    
    protected boolean onSavePrompt() {
    	if ( Dialog.ask(Dialog.D_YES_NO, "Descartar los cambios?") == Dialog.YES ) 
    		return true;
		else
			return false;
    }

	public void fieldChanged(Field field, int context) {
        //if ( field == cboProducto ) {
        	//cboMarca.deleteAll();
        	//cboMarca = new mkpyLabelChoiceField("Marca: ", , 0, EditField.FILTER_DEFAULT, Color.BLACK, Color.WHITE);
        	ObjectChoiceField opc = cboMarca.getOpciones();
        	opc.setChoices(mostrarMarcas((Producto) productos.getObjetos().elementAt(cboProducto.getSelectedIndex())));
        	//cboMarca.setDirty(false);
        	
        //}		
	}

	public void focusChanged(Field field, int eventType) {
		// TODO Auto-generated method stub
		
	} 
    
}
