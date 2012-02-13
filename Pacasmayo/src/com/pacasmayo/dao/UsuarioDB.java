package com.pacasmayo.dao;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.Calendar;
import java.util.Date;
import java.util.Vector;

import javax.microedition.io.Connector;
import javax.microedition.io.HttpConnection;

import net.rim.device.api.i18n.SimpleDateFormat;
import net.rim.device.api.io.transport.TransportInfo;
import net.rim.device.api.system.PersistentObject;
import net.rim.device.api.system.PersistentStore;
import net.rim.device.api.ui.component.Dialog;
import net.rim.device.api.ui.component.GaugeField;
import net.rim.device.api.ui.component.Status;
import net.rim.device.api.xml.parsers.DocumentBuilder;
import net.rim.device.api.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.pacasmayo.entidades.Usuario;
import com.pacasmayo.entidades.Vendedor;
import com.pacasmayo.utilidades.Cadenas;
import com.pacasmayo.utilidades.Fechas;
import com.pacasmayo.utilidades.Sistema;
import com.makipuray.ui.mkpyStatusProgress;


import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransport;

public class UsuarioDB {
	private static final String RESPONSE_OK = "1";
    private static final String metodoWeb = "authenticate";
    private static String URL = "http://intranet.cpsaa.com.pe/Aplicaciones/WS.nsf/SecurityService", DATA;
    private static PersistentObject persist;
    private static final long IDSTORE = 0x1dad7ef6fa646871L; // com.pacasmayo.entidades.Usuario    
    private Usuario usuario;
    private Vendedor vendedor;
    private String codigo;
    private String clave;
    private int tipoVendedor=0;
    private mkpyStatusProgress progress = new mkpyStatusProgress("");   
    
    /**
     * Constructor del DAO para el Usuario
     */
    public UsuarioDB() {
    	//setUrl();
        persist = PersistentStore.getPersistentObject( IDSTORE ); 
        try {
            usuario = (Usuario) persist.getContents();
        } catch (Exception e) {
            usuario = null;
        }
        try {
            if ( usuario == null) {
                //usuario = new Usuario();
                //persist.setContents(usuario);
                persist.commit();
            }
        } catch (Exception e) {
        }
    }    
    
    /**
     * Metodo que permite actualizar el objeto de usuario en la base de datos persistente
     * @return
     */
    private boolean actualizar() {
    	try {
            persist.commit();
    		return true;
    	} catch(Exception e) {
    		return false;
    	}
    }
    
    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
            this.codigo = codigo;
    }

    public String getClave() {
            return clave;
    }

    public void setClave(String clave) {
            this.clave = clave;
    }
    
	/**
	 * Metodo que permite validar si los datos ingresados son de un usuario valido
	 * @return si es que el usuario es valido 
	 */
	public boolean validar() {
		SoapObject request = new SoapObject("http://tempuri.org", metodoWeb);
		request.addProperty("in0", codigo);
		request.addProperty("in1", clave);
		
		SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
		envelope.bodyOut = request;
		HttpTransport ht = new HttpTransport(URL);
	
		envelope.encodingStyle = SoapSerializationEnvelope.ENC;
		try {
			ht.call("http://tempuri.org/" + metodoWeb, envelope);
			SoapObject result = (SoapObject) envelope.getResponse();

	        usuario = new Usuario();
	        usuario.setCodigo(getCodigo());
	        usuario.setCodigoTrabajador(result.getProperty("codigoTrabajador").toString());
	        usuario.setClave(getClave());
	        usuario.setNombre(result.getProperty("apellidoPaterno").toString() + " " + result.getProperty("nombre").toString());
	        usuario.setAutoValidar(false);
	        usuario.setVersion(Sistema.getVersion());
	        usuario.setImsi(Sistema.getImsi());
	        usuario.setFechaDesdeCM("");
	        usuario.setFechaHastaCM("");
	        usuario.setFechaValidacion(Fechas.dateToString("yyyyMMdd"));
	        persist.setContents(usuario);
	        persist.commit();
	        return true;
		
		} catch(Exception e) {
			usuario = null;
			e.printStackTrace();
		}
        return false;
    }
    
    public Usuario getUsuario() {
        return usuario;
    }
    
    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
        actualizar();
    }

    /**
     * Metodo de llamada a la sincronizacion de la informacion general de la aplicación
     */
    public boolean sincronizar() {
    	mkpyStatusProgress progressSync = new mkpyStatusProgress("Sincronizando...", 0, 100, GaugeField.PERCENT);
    	if ( ! Sistema.isCoverage() ) {
    		Dialog.inform("No se puede inicar la sincronización error en la conexión a internet, intentelo más tarde.");
        	progressSync.close();
    		return false;
    	}
    	
    	usuario.setSincronizado(false);
    	this.actualizar();
    	progressSync.open();
    	
    	//Tipo de vendedor
    	//0: No autorizado
    	//1: Vendedor Masivo
    	//2: Vendedor Industrial
    	//3: Vendedor de Ambos
    	
    	VendedorDB vendedores = new VendedorDB();
    	
    	if ( ! vendedores.getRemote() ) {
        	Dialog.inform("Error. Acceso denegado");
        	progressSync.close();
    		return false;
    	}else{
    		Vector lista = (Vector) vendedores.getObjetos();
    		Vendedor vendedor = (Vendedor) lista.elementAt(0) ;
    		tipoVendedor = Integer.parseInt(vendedor.getTipoVendedor()) ;
    		if(tipoVendedor==0){
    			Dialog.inform("Error. Acceso denegado" + vendedores.getMsgError());
            	progressSync.close();
        		return false;
    		}
    	}
    	
    	if(tipoVendedor==1 || tipoVendedor==3){
        //Vendedor Comercial
	    	MarcaDBCM marcascm = new MarcaDBCM();
	    	if ( ! marcascm.getRemote() ) {
	        	Dialog.inform("Error. Marcas Masivas" + marcascm.getMsgError());
	        	progressSync.close();
	    		return false;
	    	}
	    	marcascm = null;
	    	progressSync.setProgress(20);
    	
    	}
    	
    	if(tipoVendedor==2 || tipoVendedor==3){
    		//Vendedor Industrial
    		MarcaDBCI marcasci = new MarcaDBCI();
        	if ( ! marcasci.getRemote() ) {
            	Dialog.inform("Error. Marcas Industriales " + marcasci.getMsgError());
            	progressSync.close();
        		return false;
        	}
        	marcasci = null;
        	progressSync.setProgress(30);
        	
        	/*
        	TipoObraDB tipoObra = new TipoObraDB();
        	if ( ! tipoObra.getRemote() ) {
            	Dialog.inform("Error. Tipos de obras " + tipoObra.getMsgError());
            	progressSync.close();
        		return false;
        	}
        	tipoObra = null;
        	progressSync.setProgress(40);
        	*/
        	ObraDB Obra = new ObraDB();
        	if ( ! Obra.getRemote() ) {
            	Dialog.inform("Error. Obras " + Obra.getMsgError());
            	progressSync.close();
        		return false;
        	}
        	Obra = null;
        	progressSync.setProgress(40);
        	
        	
        	ProductoDB producto = new ProductoDB();
        	if ( ! producto.getRemote() ) {
            	Dialog.inform("Error. Productos " + producto.getMsgError());
            	progressSync.close();
        		return false;
        	}
        	producto = null;
        	progressSync.setProgress(50);
        	
        	UnidadMedidaDB unidadMedida = new UnidadMedidaDB();
        	if ( ! unidadMedida.getRemote() ) {
            	Dialog.inform("Error. Unidades de medida " + unidadMedida.getMsgError());
            	progressSync.close();
        		return false;
        	}
        	producto = null;
        	progressSync.setProgress(60);    	
        	
        	ProveedorDB proveedor = new ProveedorDB();
        	if ( ! proveedor.getRemote() ) {
            	Dialog.inform("Error. Proveedores " + proveedor.getMsgError());
            	progressSync.close();
        		return false;
        	}
        	proveedor = null;
        	progressSync.setProgress(70);  
        	
        	FrecuenciaDB frecuencia = new FrecuenciaDB();
        	if ( ! frecuencia.getRemote() ) {
            	Dialog.inform("Error. Frecuencias " + frecuencia.getMsgError());
            	progressSync.close();
        		return false;
        	}
        	frecuencia = null;
        	progressSync.setProgress(80);
    		
    	}
    	
    	usuario.setSincronizado(true);
    	this.actualizar();
        progressSync.setProgress(100);
    	progressSync.close();
		return true;

    }
    
	
}

