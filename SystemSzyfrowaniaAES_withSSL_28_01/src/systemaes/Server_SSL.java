package systemaes;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLServerSocket;
import javax.net.ssl.SSLServerSocketFactory;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.TrustManagerFactory;
import javax.swing.JOptionPane;

@SuppressWarnings("unused")
public class Server_SSL  {
	//parametry do ladowania magazynu kluczy
		static  String KSType = "JKS", KSProvider = "SUN", KSFilename = "keystore.jks", KSPassword = "keystorepassword";
		
		//parametry do stworzenia managerow
		static  String KeyPassword="keypassword", KMFAlgorithm="SunX509", KMFProvider="SunJSSE", TMFAlgorithm="SunX509", TMFProvider="SunJSSE";
		
		//parametry dla sesji SSL
		static  String SSLCProtocol="TLSv1", SSLCProvider="SunJSSE";

		//zmienne pomocnicze
		
		//parametry do stworzenia server socket'a , jedny port na ktorym bedziemy nasluchiwac
	    //static private int serverport=4911;
	    //parametry do klienta
	    public String password_from_client;
	    public String file_path;
	    public String aes_key_length;
	    public String decryptYes;
	    
	    String host="localhost";
	    int port = 4911;

		
    
    public Server_SSL(String portNumber) {
    	
    	try
		{
			
			int serverport = Integer.parseInt(portNumber);
			
			KeyStore ks = KeyStore.getInstance(KSType,KSProvider);
			ks.load(new BufferedInputStream(new FileInputStream(KSFilename)), KSPassword.toCharArray());
			
			
			KeyManagerFactory kmf = KeyManagerFactory.getInstance(KMFAlgorithm, KMFProvider);
			kmf.init(ks, KeyPassword.toCharArray());
			TrustManagerFactory tmf = TrustManagerFactory.getInstance(TMFAlgorithm, TMFProvider);
			tmf.init(ks);
			
			SSLContext sslc = SSLContext.getInstance(SSLCProtocol, SSLCProvider);
			sslc.init(kmf.getKeyManagers(), tmf.getTrustManagers(), null);
			
			
			//tworzymy server 
			SSLServerSocketFactory ssf	=	sslc.getServerSocketFactory();
		    SSLServerSocket ss=(SSLServerSocket)ssf.createServerSocket(serverport);
		    ss.setNeedClientAuth(true);//wymagamy uwierzytelniania klientow
		    
		    SSLSocket sslSocket = (SSLSocket)ss.accept();
		    InputStream sslserverIS = sslSocket.getInputStream();
		    
		    //do wyslania z powrotem do klienta
		    OutputStream sslserverOS = sslSocket.getOutputStream();
		    
		    //czytanie od klienta
		    BufferedReader bufferedreader = new BufferedReader(new InputStreamReader(sslserverIS));
		    
		    String string = null;
		    while ((string = bufferedreader.readLine())!=null)
		    {
		    	
		    	
		    	String[] dane = string.split(",", 4);
		    	
		    	password_from_client=dane[0];
		    	file_path=dane[1];
		    	aes_key_length=dane[2];
		    	decryptYes=dane[3];
		    	JOptionPane.showMessageDialog(null, "Otrzymane dane od klienta", "Komunikat od servera", JOptionPane.INFORMATION_MESSAGE);
		    	System.out.println(string);
		    	System.out.flush();
		    }
		    
		    ss.close();
		    
		    
		   			
		}catch (NoSuchProviderException e){
			JOptionPane.showMessageDialog(null, "ERROR : NoSuchProviderException");
			e.printStackTrace();
		}catch (KeyStoreException e) {
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (CertificateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			JOptionPane.showMessageDialog(null, "Port na ktorym uruchamiasz server jest zajety");
			System.exit(0);
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnrecoverableKeyException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (KeyManagementException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
}
}
