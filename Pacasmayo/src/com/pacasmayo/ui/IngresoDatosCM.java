package com.pacasmayo.ui;

import java.util.Vector;

import com.makipuray.ui.mkpyLabelChoiceField;
import com.makipuray.ui.mkpyLabelEditField;
import com.makipuray.ui.mkpyStatusProgress;
import com.pacasmayo.dao.InformacionCMDB;
import com.pacasmayo.dao.MarcaDBCM;
import com.pacasmayo.dao.UsuarioDB;
import com.pacasmayo.entidades.CanalMasivo;
import com.pacasmayo.entidades.InformacionCM;
import com.pacasmayo.entidades.MarcaCM;
import com.pacasmayo.entidades.Usuario;
import com.pacasmayo.utilidades.Fechas;

import net.rim.device.api.system.Bitmap;
import net.rim.device.api.ui.Color;
import net.rim.device.api.ui.MenuItem;
import net.rim.device.api.ui.component.BitmapField;
import net.rim.device.api.ui.component.Dialog;
import net.rim.device.api.ui.component.EditField;
import net.rim.device.api.ui.container.MainScreen;

public class IngresoDatosCM extends MainScreen {
    private mkpyStatusProgress progress = new mkpyStatusProgress("");
	private InformacionCM informacion;
	private CanalMasivo canalMasivo;
	private InformacionCMDB informes = new InformacionCMDB();
	private Usuario usuario;
	private MarcaDBCM marcas = new MarcaDBCM();

	private mkpyLabelChoiceField cboMarca;
	private mkpyLabelEditField txtPromVentas = new mkpyLabelEditField("Promedio venta al mes:", "", 15, EditField.FIELD_LEFT | EditField.NO_NEWLINE | EditField.FILTER_REAL_NUMERIC, Color.BLACK, Color.WHITE);
	private mkpyLabelEditField txtPrecioCompra = new mkpyLabelEditField("Precio compra:", "", 15, EditField.FIELD_LEFT | EditField.NO_NEWLINE | EditField.FILTER_REAL_NUMERIC, Color.BLACK, Color.WHITE);
	private mkpyLabelEditField txtPrecioVF = new mkpyLabelEditField("Precio venta (ferreteria):", "", 15, EditField.FIELD_LEFT | EditField.NO_NEWLINE | EditField.FILTER_REAL_NUMERIC, Color.BLACK, Color.WHITE);
	private mkpyLabelEditField txtPrecioVC = new mkpyLabelEditField("Precio venta (construccion):", "", 15, EditField.FIELD_LEFT | EditField.NO_NEWLINE | EditField.FILTER_REAL_NUMERIC, Color.BLACK, Color.WHITE);
	private mkpyLabelEditField txtPrecioVAC = new mkpyLabelEditField("Precio venta (auto construccion):", "", 15, EditField.FIELD_LEFT | EditField.NO_NEWLINE | EditField.FILTER_REAL_NUMERIC, Color.BLACK, Color.WHITE);
	private mkpyLabelEditField txtStock = new mkpyLabelEditField("Stock:", "", 15, EditField.FIELD_LEFT | EditField.NO_NEWLINE | EditField.FILTER_NUMERIC, Color.BLACK, Color.WHITE);
	private mkpyLabelEditField txtProveedor = new mkpyLabelEditField("Proveedor:", "", 140, EditField.FIELD_LEFT | EditField.NO_NEWLINE | EditField.FILTER_DEFAULT, Color.BLACK, Color.WHITE);
	private mkpyLabelEditField txtCondicion = new mkpyLabelEditField("Condicion de compra:", "", 140, EditField.FIELD_LEFT | EditField.NO_NEWLINE | EditField.FILTER_DEFAULT, Color.BLACK, Color.WHITE);
	private mkpyLabelEditField txtCapacidadAlmacen = new mkpyLabelEditField("Capacidad almacen:", "", 15, EditField.FIELD_LEFT | EditField.NO_NEWLINE | EditField.FILTER_NUMERIC, Color.BLACK, Color.WHITE);
	private mkpyLabelEditField txtObs = new mkpyLabelEditField("Observacion:", "", 200, EditField.FIELD_LEFT | EditField.NO_NEWLINE | EditField.FILTER_DEFAULT, Color.BLACK, Color.WHITE);
	
    public IngresoDatosCM(CanalMasivo cm, InformacionCM inf) {
    	canalMasivo = cm;
    	informacion = inf;
    	UsuarioDB usuarios = new UsuarioDB();
    	usuario = usuarios.getUsuario();
    	usuarios = null;
        add(new BitmapField(Bitmap.getBitmapResource("img/titulos/IngresoCM.png"), BitmapField.FIELD_HCENTER));

    	if ( informacion != null ) {
        	cboMarca = new mkpyLabelChoiceField("MarcaCM: ", mostrarMarcas(), 0, EditField.NON_FOCUSABLE, Color.BLACK, Color.WHITE);
    	} else {
        	cboMarca = new mkpyLabelChoiceField("MarcaCM: ", mostrarMarcas(), 0, EditField.FILTER_DEFAULT, Color.BLACK, Color.WHITE);
    	}
    	add(cboMarca);

    	add(txtPromVentas);
    	add(txtPrecioCompra);
    	add(txtPrecioVF);
    	add(txtPrecioVC);
    	add(txtPrecioVAC);
    	add(txtStock);
    	add(txtProveedor);
    	add(txtCondicion);
    	add(txtCapacidadAlmacen);
    	add(txtObs);

    	addMenuItem(mnGrabar);

    	if ( informacion != null ) {
    		int index = marcas.getIndexById(informacion.getIdMarca());
    		cboMarca.setSelectedIndex(index);
    		txtPromVentas.getText().setText(informacion.getPromedioVentaMes());
    		txtPrecioCompra.getText().setText(informacion.getPrecioCompra());    		
    		txtPrecioVF.getText().setText(informacion.getPrecioVentaFerreteria());
    		txtPrecioVC.getText().setText(informacion.getPrecioVentaConstruccion());
    		txtPrecioVAC.getText().setText(informacion.getPrecioVentaAutoConstruccion());
    		txtStock.getText().setText(informacion.getStock());
    		txtProveedor.getText().setText(informacion.getProveedor());
    		txtCondicion.getText().setText(informacion.getCondicionCompra());
    		txtCapacidadAlmacen.getText().setText(informacion.getCapacidadAlmacen());
    		txtObs.getText().setText(informacion.getObservacion());
    	}
    	
    }

    MenuItem mnGrabar = new MenuItem("Grabar", 110, 10) {
        public void run() {
        	boolean nuevo = false;
    		String resultado = "";
    		MarcaCM marca = (MarcaCM) marcas.getObjetos().elementAt(cboMarca.getSelectedIndex());
        	//TODO: validar los ingresos
    		long lngFecha = Long.parseLong(canalMasivo.getFecha());
    		long lngHoy = Long.parseLong(Fechas.dateToString("yyyyMMdd"));
    		if ( lngFecha + 1 < lngHoy ) {
    			Dialog.inform("No se pueden registrar visitas anteriores de ayer.");
    			return;
    		}
    		
    		String fecha = "";
//    		if ( informacion != null ) {
//    			fecha = informacion.getFecha();
//    		} else {
//    			fecha = canalMasivo.getFecha();
//    		}
        	if ( informacion == null ) {
        		if ( informes.existeMarcaRegistrada(canalMasivo.getCodigo(), marca.getCodigo(), canalMasivo.getFecha()) ) {
        			Dialog.inform("No puede registrar, debe cambiar la marca.");
        			return;
        		}
        	}
    		progress.open();
    		progress.setTitle("Grabando...");
    		if ( informacion == null ) {
    			nuevo = true;
    			informacion = new InformacionCM();
    		}
    		informacion.setIdCliente(canalMasivo.getCodigo());
    		informacion.setIdMarca(marca.getCodigo());
    		informacion.setMarca(marca.getDescripcion());
    		//informacion.setFecha(Fechas.dateToString("yyyyMMdd"));
    		informacion.setFecha(canalMasivo.getFecha());
    		informacion.setPromedioVentaMes(txtPromVentas.getText().getText());
    		informacion.setPrecioCompra(txtPrecioCompra.getText().getText());
    		informacion.setPrecioVentaFerreteria(txtPrecioVF.getText().getText());
    		informacion.setPrecioVentaConstruccion(txtPrecioVC.getText().getText());
    		informacion.setPrecioVentaAutoConstruccion(txtPrecioVAC.getText().getText());
    		informacion.setStock(txtStock.getText().getText());
    		informacion.setProveedor(txtProveedor.getText().getText());
    		informacion.setCondicionCompra(txtCondicion.getText().getText());
    		informacion.setCapacidadAlmacen(txtCapacidadAlmacen.getText().getText());
    		informacion.setObservacion(txtObs.getText().getText());
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
    
    public String[] mostrarMarcas() {
        Vector vMarcas = marcas.getObjetos();
        String[] sMarcas = new String[vMarcas.size()];
        int n = vMarcas.size();
        for (int i = 0; i < n; i++) {
        	MarcaCM marca = (MarcaCM) vMarcas.elementAt(i);
            sMarcas[i] = marca.getDescripcion();
        }
        return sMarcas;
    }

    protected boolean onSavePrompt() {
    	if ( Dialog.ask(Dialog.D_YES_NO, "Descartar los cambios?") == Dialog.YES ) 
    		return true;
		else
			return false;
    } 
    
}
