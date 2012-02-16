package pacasmayo;

import com.pacasmayo.dao.UsuarioDB;
import com.pacasmayo.entidades.Usuario;
import com.pacasmayo.ui.Autenticar;
import com.pacasmayo.ui.MenuOpciones;
import com.pacasmayo.utilidades.Sistema;
//import com.pacasmayo.utilidades.Cadenas;

import net.rim.device.api.ui.UiApplication;
import net.rim.device.api.ui.component.Dialog;
import javax.microedition.io.*;
import javax.microedition.io.file.*;
import java.io.*;
import java.io.IOException;
import java.io.OutputStream;

public class Pacasmayo extends UiApplication {
    private static Pacasmayo myApp;

    public static void main(String[] args) {
        myApp = new Pacasmayo();
        myApp.enterEventDispatcher();
    }
    
    public static void crearCarpeta() 
    {
        try 
        {    // the final slash in the folder path is required
             FileConnection fc = (FileConnection)Connector.open("file:///SDCard/pacasmayo/");
             // If no exception is thrown, the URI is valid but the folder may not exist.
             if (!fc.exists())
             {
                 fc.mkdir();  // create the folder if it doesn't exist
             }
             fc.close();
         }
         catch (IOException ioe) 
         {
            System.out.println(ioe.getMessage() );
         }
    }
    
    public static void crearArchivo() 
    {
      try 
      {
        FileConnection fc = (FileConnection)Connector.open("file:///SDCard/pacasmayo/conf.txt");
        // If no exception is thrown, then the URI is valid, but the file may or may not exist.
        if (!fc.exists())
        {
            fc.create();  // create the file if it doesn't exist
	        OutputStream outStream = fc.openOutputStream(); 
	        outStream.write("seguridad-url-base: http://intranet.cpsaa.com.pe/Aplicaciones/WS.nsf/SecurityService\r\n".getBytes());
	        outStream.write("seguridad-metodo-1: authenticate\r\n".getBytes());
	        outStream.write("\r\n".getBytes());
	        outStream.write("marcas-url-base: http://portal.dino.com.pe/portal/clientes/diBlackBerry.nsf/WS-ListasMaestras1\r\n".getBytes());
	        outStream.write("marcas-metodo-1: getListadoMarcasCM\r\n".getBytes());
	        outStream.write("\r\n".getBytes());
	        outStream.write("informacion-masivo-url-base: http://portal.dino.com.pe/portal/clientes/diBlackBerry.nsf/WS-EnvioRegistroCanalMasivo1\r\n".getBytes());
	        outStream.write("informacion-masivo-metodo-1: setEnvioRegistroCanalMasivo\r\n".getBytes());
	        outStream.write("\r\n".getBytes());
	        outStream.write("informacion-industrial-url-base: http://portal.dino.com.pe/portal/clientes/diBlackBerry.nsf/WS-EnvioRegistroCanalIndustrial1\r\n".getBytes());
	        outStream.write("informacion-industrial-metodo-1: setEnvioRegistroCanalIndustrial\r\n".getBytes());
	        outStream.write("\r\n".getBytes());
	        outStream.write("clientes-masivo-url-base: http://portal.dino.com.pe/portal/clientes/diBlackBerry.nsf/WS-ListadoClientesCanalMasivo1\r\n".getBytes());
	        outStream.write("clientes-masivo-metodo-1: getListadoClientesCanalMasivo\r\n".getBytes());
	        outStream.write("\r\n".getBytes());
	        outStream.write("clientes-industrial-url-base: http://portal.dino.com.pe/portal/clientes/diBlackBerry.nsf/WS-ListadoClientesCanalIndustrial1\r\n".getBytes());
	        outStream.write("clientes-industrial-metodo-1: getListadoClientesCanalIndustrial\r\n".getBytes());
	        
	        outStream.close();
        }
        fc.close();
       }
       catch (IOException ioe) 
       {
          System.out.println(ioe.getMessage() );
       }
    }
    
    public static void leerArchivo() 
    {
      try 
      {
        FileConnection fc = (FileConnection)Connector.open("file:///SDCard/pacasmayo/conf.txt");
        // If no exception is thrown, then the URI is valid, but the file may or may not exist.
        if (!fc.exists())
        {
            fc.create();  // create the file if it doesn't exist
	        InputStream inputStream = fc.openInputStream();
	        
	        byte[] buffer = new byte[1024];
	        StringBuffer sb = new StringBuffer();
	        int readIn = 0;
	        while((readIn = inputStream.read(buffer)) > 0)
	        {
	             String temp = new String(buffer, 0, readIn);
	             sb.append(temp);  
	        }      
	        	        
	        inputStream.close();
	        
	        Dialog.inform(sb.toString());
	        
        }
        fc.close();
       }
       catch (IOException ioe) 
       {
          System.out.println(ioe.getMessage() );
       }
    }
    
    Pacasmayo() {
    	
        try {
            UiApplication.getUiApplication().invokeLater( new Runnable() {
            	public void run () {
            		UsuarioDB usuarios = new UsuarioDB();
        	        Usuario usuario = usuarios.getUsuario();
	        		//Si el usuario existe
	        		if(usuario != null && usuario.getCodigo() != null) { 
	        	        String versionApp = Sistema.getVersion();
	        			String versionImsi = Sistema.getImsi();
	        			String versionAppUsr = usuario.getVersion();
	        			String versionImsiUsr = usuario.getImsi();
        	        	// si la version del app son iguales
        	        	if (! versionApp.equals(versionAppUsr) ) {
        	        		Dialog.inform("La aplicación tiene una versión diferente " + versionApp + " " + versionAppUsr);
        	        		System.exit(0);
        	        	}
        	        	crearCarpeta();
        	        	crearArchivo();
        	        	leerArchivo();
        	        	//Dialog.inform(Cadenas.getJadUrl("Pacasmayo-url-general"));
        	        	
	          	      	//si las versiones del IMSI son iguales
	        	        if(versionImsi.equals(versionImsiUsr)) {
            	            if(usuario.isAutoValidar()) {
        	                    pushScreen(new MenuOpciones());
	        	        	} else {
                        		pushScreen(new Autenticar() );                            
        	            	}
            	            // si el IMSI no son iguales
		                } else {
		                	pushScreen(new Autenticar() );
		                }
        	        } else {  // si no existe usuario se muestra la opcion de login pero antes se tiene que cargar los mensajes
                		pushScreen(new Autenticar() );                            
        	        }
            	}
           });
            
        } catch(Exception e) {
        }
     }

}
