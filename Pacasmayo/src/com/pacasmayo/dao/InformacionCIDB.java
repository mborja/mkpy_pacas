package com.pacasmayo.dao;

import java.io.ByteArrayInputStream;
import java.util.Date;
import java.util.Vector;

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

import com.pacasmayo.entidades.InformacionCI;
import com.pacasmayo.entidades.MarcaCM;
import com.pacasmayo.entidades.Obra;
import com.pacasmayo.entidades.Usuario;
import com.pacasmayo.utilidades.Cadenas;
import com.pacasmayo.utilidades.Fechas;
import com.pacasmayo.utilidades.Sistema;

public class InformacionCIDB {
	private static final String RESPONSE_OK = "1";
    private static final String metodoWeb = "setEnvioRegistroCanalIndustrial";
    private static String URL = Cadenas.URLBASE + "/portal/clientes/diBlackBerry.nsf/WS-EnvioRegistroCanalIndustrial1", DATA;
    private static PersistentObject persist;
    private static final long IDSTORE = 0x678feecf6df5bd9aL; // com.pacasmayo.entidades.InformacionCI
    private Vector objetos;
    private Usuario usuario;
    //private InformacionCI informacionCI;
    private String msgError = "";

    public InformacionCIDB() {
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

    public void commitChanges() {
    	persist.commit();
    }
    
    private boolean checkResult(InformacionCI inf, String result) throws Exception {
    	boolean resultado = false;
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
            //objetos = new Vector();
            Node controlNode = node.item(0);
            registro = controlNode.getChildNodes().item(0).getNodeValue();
            fields = Cadenas.splitSimple(registro, Cadenas.TOKEN);
            //TODO: evaluar la respuesta del Ws
            int state = Integer.parseInt(fields[1]);
            msgError = fields[2];

            if ( state == 0 ) {
            	if(inf.getIdObraWS()==null){
            	   inf.setIdObraWS(msgError.substring(msgError.length()-10));
            	}
                inf.setEnviado(true);
                persist.commit();
                resultado = true;
            } else {
            	resultado = false;
            }
        }
        return resultado;
    }
    
    public boolean putRemote(InformacionCI inf) {
		try {
			SoapObject request = new SoapObject("http://tempuri.org", metodoWeb);
			request.addProperty("idBlackBerry", Sistema.getPin());
			request.addProperty("idUsuario", usuario.getCodigoTrabajador());
			request.addProperty("idCliente", inf.getIdCliente());
			request.addProperty("fechaVisita", inf.getFecha());
			if(inf.getCreadoEnCelular()=="X" ){
				if( inf.getIdObraWS() == null ){
					request.addProperty("idObra", "X");
				}else{
					request.addProperty("idObra", inf.getIdObraWS());
				}
			}else{
				if(inf.getIdObra()==null){
					request.addProperty("idObra", "NOB");
				}else{
					request.addProperty("idObra", inf.getIdObra());
				}
			}

			request.addProperty("obra", inf.getObra());
			request.addProperty("desObra", inf.getObraDescripcion());	
			
			//request.addProperty("idTipoObra", inf.getIdTipoObra());
			request.addProperty("idTipoObra", inf.getTipoObra());
			request.addProperty("idProducto", inf.getIdProducto());
			request.addProperty("idMarca", inf.getIdMarca());
			request.addProperty("cantidad", inf.getCantidad() );
			//request.addProperty("idUnidadM", inf.getIdUnidadMedida());
			request.addProperty("idUnidadM", inf.getUnidadMedida());
			//request.addProperty("idFrecuencia", inf.getIdFrecuencia());
			request.addProperty("idFrecuencia", inf.getFrecuencia());
			//request.addProperty("idProveedor", inf.getIdProveedor());
			request.addProperty("idProveedor", inf.getProveedor());
			request.addProperty("precioCompra", inf.getPreciocompra());
			request.addProperty("estatus", "1");
			request.addProperty("fechaFinObra", inf.getFechafinobra());
			request.addProperty("residente", inf.getResidente());
			request.addProperty("observacion", inf.getObservacion());
			
			SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
			envelope.bodyOut = request;
			HttpTransport ht = new HttpTransport(URL);
		
			envelope.encodingStyle = SoapSerializationEnvelope.ENC;
			
			ht.call("http://tempuri.org/" + metodoWeb, envelope);
			
			String result = (String) envelope.getResponse();
			if ( checkResult(inf, result) ) {
		        return true;
			}
		} catch(Exception e) {
			Dialog.inform("FF : " + e.getMessage());
			e.printStackTrace();
		}
        return false;
    }

    public boolean existeMarcaRegistrada(String idCliente, String idMarca, String fecha) {
    	int j = 0;
    	int n = objetos.size();
    	for (int i = 0; i < n; i++) {
    		InformacionCI info = (InformacionCI) objetos.elementAt(i);
    		if ( info.getIdCliente().equals(idCliente)) // && info.getIdMarca().equals(idMarca) && info.getFecha().equals(fecha) ) 
    		{
    			j++;
    		}
    	}
    	if ( j >= 1 ) {
    		return true;
    	} else {
        	return false;
    	}
    }
    
    public Vector getObjetosByIdCliente(String idCliente) {
    	Vector result = new Vector();
    	int n = objetos.size();
    	for (int i = 0; i < n; i++) {
    		InformacionCI info = (InformacionCI) objetos.elementAt(i);
    		if ( info.getIdCliente().equals(idCliente) ) {
        		result.addElement(info);
    		}
    	}
    	return result;
    }
    
    public Vector getObjetosByIdClienteFechas(String idCliente, long desde, long hasta) {
    	Vector result = new Vector();
    	int n = objetos.size();
    	for (int i = 0; i < n; i++) {
    		InformacionCI info = (InformacionCI) objetos.elementAt(i);
    		long sDesde = Long.parseLong(Fechas.dateToString(new Date(desde), "yyyyMMdd"));
    		long sHasta = Long.parseLong(Fechas.dateToString(new Date(hasta), "yyyyMMdd"));
    		if ( info.getIdCliente().equals(idCliente) && ( Long.parseLong(info.getFecha()) >= sDesde && Long.parseLong(info.getFecha()) <= sHasta )) {
        		result.addElement(info);
    		}
    	}
    	return result;
    }

    public Vector getObjetosByIdClienteFechas(String idCliente, String fechaV) {
    	Vector result = new Vector();
    	int n = objetos.size();
    	for (int i = 0; i < n; i++) {
    		InformacionCI info = (InformacionCI) objetos.elementAt(i);
    		if ( info.getIdCliente().equals(idCliente) ){
    				// && info.getFecha().equals(fechaV)) {
    				result.addElement(info);
    		}
    	}
    	return result;
    }

    public Vector getObjetosByIdClienteFechasObra(String idCliente, String fechaV, Obra obra) {
    	Vector result = new Vector();
    	int n = objetos.size();
    	for (int i = 0; i < n; i++) {
    		InformacionCI info = (InformacionCI) objetos.elementAt(i);
    		if ( info.getIdCliente().equals(obra.getCodigoCliente()) && obra.getCodigo().equals(info.getIdObra()) ){
    				//&& info.getFecha().equals(fechaV)) {
        		result.addElement(info);
    		}
    	}
    	return result;
    }
    
    public Vector getObjetosByIdClienteFechasSinObra(String idCliente, String fechaV) {
    	Vector result = new Vector();
    	int n = objetos.size();
    	for (int i = 0; i < n; i++) {
    		InformacionCI info = (InformacionCI) objetos.elementAt(i);
    		if ( info.getIdCliente().equals(idCliente) && info.getIdObra()==null ){
    				// && info.getFecha().equals(fechaV)) {
    				result.addElement(info);
    		}
    	}
    	return result;
    }
    
    public Vector getObjetosSinEnviar() {
    	Vector result = new Vector();
    	int n = objetos.size();
    	for (int i = 0; i < n; i++) {
    		InformacionCI info = (InformacionCI) objetos.elementAt(i);
    		if ( info.isEnviado() == false ) {
        		result.addElement(info);
    		}
    	}
    	return result;
    }

    public Vector getObjetos() {
        return objetos;
    }

	public String getMsgError() {
		return msgError;
	}

    
}

