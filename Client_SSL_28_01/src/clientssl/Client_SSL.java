package clientssl;


import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
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
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManagerFactory;
import javax.swing.JOptionPane;

public class Client_SSL {
	
	//public SSL() throws NoSuchProviderException {}
	
		//parametry do ladowania magazynu kluczy
		static  String KSType = "JKS", KSProvider = "SUN", KSFilename = "keystore.jks", KSPassword = "keystorepassword";
		
		//parametry do stworzenia managerow
		static  String KeyPassword="keypassword", KMFAlgorithm="SunX509", KMFProvider="SunJSSE", TMFAlgorithm="SunX509", TMFProvider="SunJSSE";
		
		//parametry dla sesji SSL
		static  String SSLCProtocol="TLSv1", SSLCProvider="SunJSSE";

		//zmienne pomocnicze
		
		//parametry do stworzenia server socket'a , jedny port na ktorym bedziemy nasluchiwac
	    @SuppressWarnings("unused")
		static private int serverport=4911;
	    //parametry do klienta
	    static String host="localhost";
	    static int port=4911;

		private String flag = ",";

	    
		
		public Client_SSL(String key, String plik, String tryb, String deszyfruj, String portnumber) {
			try
			{
				
				int port = Integer.parseInt(portnumber);
				KeyStore ks = KeyStore.getInstance(KSType,KSProvider);
				ks.load(new BufferedInputStream(new FileInputStream(KSFilename)), KSPassword.toCharArray());
				
				
				KeyManagerFactory kmf = KeyManagerFactory.getInstance(KMFAlgorithm, KMFProvider);
				kmf.init(ks, KeyPassword.toCharArray());
				TrustManagerFactory tmf = TrustManagerFactory.getInstance(TMFAlgorithm, TMFProvider);
				tmf.init(ks);
				
				SSLContext sslc = SSLContext.getInstance(SSLCProtocol, SSLCProvider);
				sslc.init(kmf.getKeyManagers(), tmf.getTrustManagers(), null);
				
				
			    
			    //tworzymy klienta
			    SSLSocketFactory sf=sslc.getSocketFactory();
		        SSLSocket s=(SSLSocket)sf.createSocket(host,port);
		        s.setWantClientAuth(true);//klient zada procedury uwierzytelnienia
		        
		        OutputStream sslOS = s.getOutputStream();
		        sslOS.write(key.getBytes());//write to the server
		        sslOS.write(flag .getBytes());
		        sslOS.write(plik.getBytes());//write to the server
		        sslOS.write(flag .getBytes());
		        sslOS.write(tryb.getBytes());
		        sslOS.write(flag .getBytes());
		        sslOS.write(deszyfruj.getBytes());
		        sslOS.flush();
		        sslOS.close();
				
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
