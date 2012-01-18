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

import net.rim.device.api.system.PersistentObject;
import net.rim.device.api.system.PersistentStore;
import net.rim.device.api.ui.component.Dialog;
import net.rim.device.api.xml.parsers.DocumentBuilder;
import net.rim.device.api.xml.parsers.DocumentBuilderFactory;

import com.pacasmayo.utilidades.Cadenas;
import com.pacasmayo.utilidades.Fechas;
import com.pacasmayo.utilidades.Sistema;
import com.makipuray.ui.mkpyStatusProgress;
import com.pacasmayo.entidades.CanalIndustrial;
import com.pacasmayo.entidades.MarcaCM;
import com.pacasmayo.entidades.Usuario;
import com.pacasmayo.entidades.Obra;

public class CanalIndustrialDB {
	private static final String RESPONSE_OK = "1";
    private static final String metodoWeb = "getListadoClientesCanalIndustrial";
    private static String URL = Cadenas.URLBASE + "/portal/clientes/diBlackBerry.nsf/WS-ListadoClientesCanalIndustrial1", DATA;
    private static PersistentObject persist;
    private static final long IDSTORE = 0xe71402654086f9a4L; // com.pacasmayo.entidades.CanalIndustrial    
    private Vector objetos;
    private Usuario usuario;
    private int state = 0;
    private String msgError = "";

    /**
     * Constructor del DAO para el CanalMasivo
     */
    public CanalIndustrialDB() {
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
        result = result.replace('&', ' ');
        ByteArrayInputStream bis = new ByteArrayInputStream(result.getBytes("UTF-8"));
        Document document = builder.parse(bis);
        Element rootElement = document.getDocumentElement();
        rootElement.normalize();

        NodeList node = rootElement.getChildNodes();

        int n = node.getLength();
        if ( n > 0 ){
            Node controlNode = node.item(0);
            registro = controlNode.getChildNodes().item(0).getNodeValue();
            fields = Cadenas.splitSimple(registro, Cadenas.TOKEN);
            //int n = Integer.parseInt(fields[3]);
            state = Integer.parseInt(fields[1]);
            msgError = fields[2];
            if ( state == 0 ) {
            	return false;
            }
            objetos = new Vector();
            for (int i = 1; i < n; i++) {
	            Node contactNode = node.item(i);
	            registro = contactNode.getChildNodes().item(0).getNodeValue();
	            fields = Cadenas.splitSimple(registro, Cadenas.TOKEN);
	            CanalIndustrial item = new CanalIndustrial();
	            item.setCodigo(fields[0]);
	            item.setFecha(fields[1]);
	            item.setNombre(fields[2]);
	            item.setEstado(fields[3]);
	            item.setNumeroObras(fields[4]);
            	Vector obras = new Vector();
	            if(!item.getNumeroObras().equals("0")){
	            	int numernObras = Integer.parseInt(item.getNumeroObras()) ;
	            	int indice;
	            	for(int j=1;j<=numernObras;j++){
	            		Obra obra = new Obra();
	            		indice = (j*5);
	            		obra.setCodigo(fields[indice]);
	            		obra.setCodigoCliente(fields[indice+1]);
	            		obra.setNombre(fields[indice+2]);
	            		obra.setDescripcion(fields[indice+3]);
	            		obra.setFecha(fields[indice+4]);	
	            		obras.addElement(obra);
	            	}
	            }
	            item.setObras(obras);
	            objetos.addElement(item);
	        }
	        persist.setContents(objetos);
	        persist.commit();
        }
        return true;
    }
    
    public boolean getRemote(String fechaDesde, String fechaHasta) {
		try {
			SoapObject request = new SoapObject("http://tempuri.org", metodoWeb);
			request.addProperty("idBlackBerry", Sistema.getPin());
			request.addProperty("idUsuario", usuario.getCodigoTrabajador()); //codigoTrabajador
			request.addProperty("fechaInicio", fechaDesde);
			request.addProperty("fechaFin", fechaHasta);
			
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
			msgError = "Error HTTP: " + e.getMessage();
			//Dialog.inform("FF " + e.getMessage());
			e.printStackTrace();
		}
        return false;
    }
    
    public int getIndexById(String id) {
    	CanalIndustrial item = null;
        int i, n;
        n = objetos.size();
        for (i = 0; i < n; i++) {
        	item = (CanalIndustrial) objetos.elementAt(i);
            if(id.equals(item.getCodigo())){
                return i;
            }
        }
        return -1;
    }    
    
    public CanalIndustrial getByIndex(int index){
    	return (CanalIndustrial) objetos.elementAt(index);
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

