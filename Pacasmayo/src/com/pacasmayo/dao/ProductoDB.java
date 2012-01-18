package com.pacasmayo.dao;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Vector;

import javax.microedition.io.Connector;
import javax.microedition.io.HttpConnection;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransport;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import net.rim.device.api.input.InputHelper;
import net.rim.device.api.system.PersistentObject;
import net.rim.device.api.system.PersistentStore;
import net.rim.device.api.ui.component.Dialog;
import net.rim.device.api.xml.parsers.DocumentBuilder;
import net.rim.device.api.xml.parsers.DocumentBuilderFactory;

import com.pacasmayo.dao.UsuarioDB;
import com.makipuray.ui.mkpyStatusProgress;
import com.pacasmayo.entidades.Producto;
import com.pacasmayo.entidades.Usuario;
import com.pacasmayo.utilidades.Cadenas;
import com.pacasmayo.utilidades.Sistema;

public class ProductoDB {
	private static final String RESPONSE_OK = "1";
    private static final String metodoWeb = "getListadoProductoD";
    private static String URL = Cadenas.URLBASE + "/portal/clientes/diBlackBerry.nsf/WS-ListasMaestras1", DATA;
    private static PersistentObject persist;
    private static final long IDSTORE = 0x321af6df9c11ba2aL; // com.pacasmayo.entidades.ProductoDB    
    private Vector objetos;
    private Usuario usuario;
    private int state = 0;
    private String msgError = "";
    
    /**
     * Constructor del DAO para el CanalMasivo
     */
    public ProductoDB() {
    	UsuarioDB usuarios = new UsuarioDB();
    	usuario = usuarios.getUsuario();
    	usuarios = null;
        persist = PersistentStore.getPersistentObject( IDSTORE ); 
        try {
                objetos = (Vector) persist.getContents();
        } catch (Exception e) {
                objetos = null;
        }
        try {
            if ( objetos == null) {
                objetos = new Vector();
                persist.setContents(objetos);
                persist.commit();
            }
        } catch (Exception e) {
        }
    }

//    private void setUrl() {
//        URL = Cadenas.URLBASE + "/" + metodoWeb;
//        DATA = "PIN=" + Sistema.getPin() + "&IMSI=" + Sistema.getImsi();
//    }
    
    private boolean fillObjectos(String result) throws Exception {
    	String registro = "";
    	String[] fields;
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        ByteArrayInputStream bis = new ByteArrayInputStream(result.getBytes("UTF-8"));
		//Dialog.inform("-1 " + result);
        Document document = builder.parse(bis);
        Element rootElement = document.getDocumentElement();
        rootElement.normalize();
        NodeList node = rootElement.getChildNodes();
        int n = node.getLength();
        if ( n > 0 ){
            objetos = new Vector();
            Node controlNode = node.item(0);
            registro = controlNode.getChildNodes().item(0).getNodeValue();
            fields = Cadenas.splitSimple(registro, Cadenas.TOKEN);
            //int n = Integer.parseInt(fields[3]);
            state = Integer.parseInt(fields[1]);
            msgError = fields[2];
            if ( state == 0 ) {
            	return false;
            }
            for (int i = 1; i < n; i++) {
                Node contactNode = node.item(i);
                registro = contactNode.getChildNodes().item(0).getNodeValue();
                fields = Cadenas.splitSimple(registro, Cadenas.TOKEN);
                Producto item = new Producto();
                item.setCodigo(fields[0]);
                item.setDescripcion(fields[1]);
                objetos.addElement(item);
            }
            persist.setContents(objetos);
            persist.commit();
        }
        return true;
    }
    
    public boolean getRemote() {
		try {
			SoapObject request = new SoapObject("http://tempuri.org", metodoWeb);
			//Dialog.inform("" + Sistema.getPin());
			request.addProperty("idBlackBerry", Sistema.getPin());
			request.addProperty("idUsuario", usuario.getCodigoTrabajador());
			
			SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
			envelope.bodyOut = request;
			HttpTransport ht = new HttpTransport(URL);
		
			envelope.encodingStyle = SoapSerializationEnvelope.ENC;
			
			ht.call("http://tempuri.org/" + metodoWeb, envelope);
			
			String result = (String) envelope.getResponse();
			if ( fillObjectos(result) ) {
		        return true;
			}
		} catch(Exception e) {
			//Dialog.inform("FF " + e.getMessage());
			msgError = "Error HTTP: " + e.getMessage();
			e.printStackTrace();
		}
        return false;
    }
    
    public int getIndexById(String id) {
    	Producto item = null;
        int i, n;
        n = objetos.size();
        for (i = 0; i < n; i++) {
        	item = (Producto) objetos.elementAt(i);
            if(id.equals(item.getCodigo())){
                return i;
            }
        }
        return -1;
    }    
    
    public Vector getObjetos() {
        return objetos;
    }

	public int getState() {
		return state;
	}

	public void setState(int state) {
		this.state = state;
	}

	public String getMsgError() {
		return msgError;
	}

	public void setMsgError(String msgError) {
		this.msgError = msgError;
	}

    
}
