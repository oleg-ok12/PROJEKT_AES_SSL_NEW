package systemaes;


import javax.swing.JOptionPane;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

public class AESTest {


	/**
	 * Launch the application.
	 * @throws UnsupportedLookAndFeelException 
	 * @throws IllegalAccessException 
	 * @throws InstantiationException 
	 * @throws ClassNotFoundException 
	 */
	public static void main(String[] args) throws ClassNotFoundException, InstantiationException, IllegalAccessException, UnsupportedLookAndFeelException {
		
					UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
					
					
					String PortNumber = JOptionPane.showInputDialog(null, null, "SERVER: Podaj numer porta" , JOptionPane.INFORMATION_MESSAGE);
					Main frame = new Main();
					frame.setSize(530, 400);
					frame.setVisible(true);
					
					Server_SSL server = new Server_SSL(PortNumber);
					//new Thread(new Client()).start();
					//new Thread(new Server()).start();
					//new Thread(new SSL()).start();
					//new Thread(new Server_SSL()).start();
					//new Thread(new Client_SSL()).start();
					frame.wczytujdane(server.password_from_client, server.file_path, server.aes_key_length, server.decryptYes);
                
			
	
}
}
