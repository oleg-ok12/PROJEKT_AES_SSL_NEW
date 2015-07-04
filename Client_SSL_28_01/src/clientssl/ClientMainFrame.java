package clientssl;

import java.awt.Font;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.security.SecureRandom;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.border.EmptyBorder;

@SuppressWarnings("serial")
public class ClientMainFrame extends JFrame {
	
	
	
	private JPanel contentPane;
	private JTextField textField;
	private JTextField textField_1;
	private JTextField textField_2;
	private JTextField textField_3;
	private String key;
	private String plik;
	private String tryb;
	private String decrypt;
	

	/**
	 * Create the frame.
	 */
	public  ClientMainFrame() {
		
		
		
		setTitle("Program klienta z generatorem hasel szyfrowanie AES");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		textField = new JTextField();
		textField.setBounds(190, 107, 180, 20);
		contentPane.add(textField);
		textField.setColumns(10);
		
		
		
		JLabel lblHasloDlaSzyfrowania = new JLabel("Haslo dla szyfrowania");
		lblHasloDlaSzyfrowania.setFont(new Font("Tahoma", Font.BOLD, 14));
		lblHasloDlaSzyfrowania.setBounds(190, 79, 180, 27);
		contentPane.add(lblHasloDlaSzyfrowania);
		textField.setText(key);
		
		JLabel lblNazwaPliku = new JLabel("Nazwa pliku");
		lblNazwaPliku.setFont(new Font("Tahoma", Font.BOLD, 14));
		lblNazwaPliku.setBounds(190, 11, 180, 27);
		contentPane.add(lblNazwaPliku);
		
		textField_1 = new JTextField();
		textField_1.setBounds(190, 38, 180, 20);
		contentPane.add(textField_1);
		textField_1.setColumns(10);
		textField_1.setText(plik);
		
		JLabel lblTryb = new JLabel("Dlugosc klucza AES");
		JLabel lblTrybOptions = new JLabel("128/192/256 bit");
		lblTryb.setFont(new Font("Tahoma", Font.BOLD, 14));
		lblTrybOptions.setBounds(190, 165, 180, 27);
		contentPane.add(lblTrybOptions);
		lblTryb.setBounds(190, 140, 180, 27);
		contentPane.add(lblTryb);
		
		textField_2 = new JTextField();
		textField_2.setBounds(190, 190, 180, 20);
		contentPane.add(textField_2);
		textField_2.setColumns(10);
		textField_2.setText(tryb);
		
		
		
		JLabel lblDeszyfruj = new JLabel("Haslo Deszyfrowania");
		lblDeszyfruj.setFont(new Font("Tahoma", Font.BOLD, 14));
		lblDeszyfruj.setBounds(5, 140, 180, 27);
		contentPane.add(lblDeszyfruj);
		
		textField_3 = new JTextField();
		textField_3.setBounds(5, 190, 180, 20);
		contentPane.add(textField_3);
		textField_3.setColumns(10);
		
		
		
		JLabel lblGeneratorHasel = new JLabel("Generator hasel");
		lblGeneratorHasel.setFont(new Font("Tahoma", Font.BOLD, 14));
		lblGeneratorHasel.setBounds(20, 36, 130, 20);
		contentPane.add(lblGeneratorHasel);
		
		
		JLabel lblServerV = new JLabel("Client v1.1");
		lblServerV.setBounds(370, 240, 120, 23);
		contentPane.add(lblServerV);
		
		
		JButton btnNewButton_1 = new JButton("Wygeneruj i skopiuj");
		btnNewButton_1.setBounds(20, 95, 130, 23);
		contentPane.add(btnNewButton_1);
		btnNewButton_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
				
				String generatedpassword = generate (7,3);
				JTextPane txtpnSdfr = new JTextPane();
				txtpnSdfr.setEditable(false);
				txtpnSdfr.selectAll();
				txtpnSdfr.setBounds(20, 65, 130, 20);
				contentPane.add(txtpnSdfr);
				txtpnSdfr.setText(generatedpassword);
				String myString = generatedpassword;
				StringSelection stringSelection = new StringSelection (myString);
				Clipboard clpbrd = Toolkit.getDefaultToolkit ().getSystemClipboard ();
				clpbrd.setContents (stringSelection, null);
				
			}}
			);
		
		
		
		
		
		
		JButton btnWyslijDane = new JButton("Wyslij dane i zaszyfruj plik");
		btnWyslijDane.setBounds(190, 220, 180, 23);
		contentPane.add(btnWyslijDane);
		btnWyslijDane.addActionListener(new ActionListener() {
			

			

			public void actionPerformed(ActionEvent arg0) {
				
				
				key = textField.getText();
				plik = textField_1.getText();
				tryb = textField_2.getText();
				decrypt = "Tryb SZYFROWANIA";
				
				
				String numerporta = JOptionPane.showInputDialog(null, null, "CLIENT: Podaj numer porta" , JOptionPane.INFORMATION_MESSAGE);
				@SuppressWarnings("unused")
				Client_SSL client = new Client_SSL(key, plik, tryb, decrypt, numerporta);
				
					
				
			}}
			);
		
		JButton btnWyslijDaneDeszyfruj = new JButton("Wyslij dane i deszyfruj plik");
		btnWyslijDaneDeszyfruj .setBounds(5, 220, 180, 23);
		contentPane.add(btnWyslijDaneDeszyfruj );
		btnWyslijDaneDeszyfruj.addActionListener(new ActionListener() {
			

			

			public void actionPerformed(ActionEvent arg0) {
				
				
				key = "Tryb DESZYFROWANIA";
				plik = "Tryb DESZYFROWANIA";
				tryb = "Tryb DESZYFROWANIA";
				decrypt = textField_3.getText();
				
				String numerporta = JOptionPane.showInputDialog(null, null, "CLIENT: Podaj numer porta" , JOptionPane.INFORMATION_MESSAGE);
				
				@SuppressWarnings("unused")
				Client_SSL client = new Client_SSL(key, plik, tryb, decrypt, numerporta);
				
					
				
			}}
			);
		
	

}
	
	/**
	 * Generator hasel
	 */
	
	public String generate(int length, int digitGoAfterNumOfChars) {
        SecureRandom r = new SecureRandom();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; i++) {
                char[] charset = 0 == (i + 1) % (digitGoAfterNumOfChars + 1) ? DIGITS
                                : CHARS;

                sb.append(charset[r.nextInt(charset.length)]);
        }
        return sb.toString();
}

/**
 * Litery dla generatora hasel bez "l" i "o"
 */
public static final char[] CHARS = { 'a', 'b', 'c', 'd', 'e', 'f', 'g',
                'h', 'i', 'j', 'k', 'm', 'n', 'p', 'q', 'r', 's', 't', 'u', 'v',
                'w', 'x', 'y', 'z', 'A', 'Z', 'F', 'C','X' };

/**
 * Cyfry dla generatora hasel bez "0" i "1" i znaki specjalne
 */
public static final char[] DIGITS = { '=','?', '@', '!', '2', '3', '4', '5', '6', '7', '8', '9', };


	
}
