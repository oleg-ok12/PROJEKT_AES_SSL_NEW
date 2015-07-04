package clientssl;

import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;


public class ClientTest {

	public static void main(String[] args) throws ClassNotFoundException, InstantiationException, IllegalAccessException, UnsupportedLookAndFeelException{
		
		
		
		UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		
		
		try {
			ClientMainFrame frame = new ClientMainFrame();
			frame.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
		
	}
	
	
	
	
	
	
}
