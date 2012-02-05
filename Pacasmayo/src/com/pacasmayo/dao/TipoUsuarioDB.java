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
import com.pacasmayo.entidades.Frecuencia;
import com.pacasmayo.entidades.TipoUsuario;
import com.pacasmayo.entidades.Usuario;
import com.pacasmayo.utilidades.Cadenas;
import com.pacasmayo.utilidades.Fechas;
import com.pacasmayo.utilidades.Sistema;

public class TipoUsuarioDB {
	private static final String RESPONSE_OK = "1";
    private static final String metodoWeb = "getTipoVendedor";
    private static String URL = Cadenas.URLBASE + "/portal/clientes/diBlackBerry.nsf/WS-ListasMaestras1", DATA;
    private static PersistentObject persist;
    private static final long IDSTORE = 0x71f1fbc702e93ed9L; // com.pacasmayo.entidades.TipoUsuarioDB    
    private Vector objetos;
    private Usuario usuario;
    private TipoUsuario tipousuario;
    private int state = 0;
    private String msgError = "";
    
    
    public TipoUsuarioDB() {
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

    private boolean fillObjectos(String result) throws Exception {
    	String registro = "";
    	String[] fields;
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        ByteArrayInputStream bis = new ByteArrayInputStream(result.getBytes("UTF-8"));
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
            state = Integer.parseInt(fields[1]);
            msgError = fields[2];
            if ( state == 0 ) {
            	return false;
            }
            for (int i = 1; i < n; i++) {
                Node contactNode = node.item(i);
                registro = contactNode.getChildNodes().item(0).getNodeValue();
                fields = Cadenas.splitSimple(registro, Cadenas.TOKEN);
                TipoUsuario item = new TipoUsuario();
                item.setRol(fields[0]);
                objetos.addElement(item);
            }
            persist.setContents(objetos);
            persist.commit();
        }
        return true;
    }
    
    public boolean getRemote() {
    	SoapObject request = new SoapObject("http://tempuri.org", metodoWeb);
    	request.addProperty("idBlackBerry", Sistema.getPin());
		request.addProperty("idUsuario", usuario.getCodigoTrabajador());
		
		SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
		envelope.bodyOut = request;
		HttpTransport ht = new HttpTransport(URL);
	
		envelope.encodingStyle = SoapSerializationEnvelope.ENC;
		try {
			ht.call("http://tempuri.org/" + metodoWeb, envelope);
			SoapObject result = (SoapObject) envelope.getResponse();

	        tipousuario = new TipoUsuario();
	        tipousuario.setRol(result.getProperty("tipovendedor").toString());
	        
	        //persist.setContents(usuario);
	        //persist.commit();
	        return true;
		
		} catch(Exception e) {
			tipousuario = null;
			e.printStackTrace();
		}
        return false;
    }
    
    public int getIndexById(String id) {
    	TipoUsuario item = null;
        int i, n;
        n = objetos.size();
        for (i = 0; i < n; i++) {
        	item = (TipoUsuario) objetos.elementAt(i);
            if(id.equals(item.getRol())){
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

	  public TipoUsuario getTipoUsuario() {
	        return tipousuario;
	    }
    
}
