package systemaes;

import java.awt.Font;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.SecureRandom;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.border.EmptyBorder;

import systemaes.AES.InvalidAESStreamException;
import systemaes.AES.InvalidKeyLengthException;
import systemaes.AES.InvalidPasswordException;
import systemaes.AES.StrongEncryptionNotAvailableException;


@SuppressWarnings("serial")
public class Main extends JFrame {

	private JPanel contentPane;
	private JTextField textField;
	private JTextField textField_1;
	private JTextField textField_2;
	private JTextField textField_3;
	protected int BitLengthOfKey;
	private String haslo;
	private String sciezka_do_pliku;
	private String tryb;
	private String deszyfruj;
	

	

	/**
	 * Utworzenie okna
	 */
	public Main() {
		setTitle("Program szyfrujacy z generatorem hasel");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 501, 389);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		
		JRadioButton rdbtnBitAes = new JRadioButton("128 bit AES");
		rdbtnBitAes.setBounds(20, 171, 109, 23);
		contentPane.add(rdbtnBitAes);
		rdbtnBitAes.addActionListener(new ActionListener(){
		    public void actionPerformed(ActionEvent e) {
		    	BitLengthOfKey = 128;
		    }
		});
		JRadioButton rdbtnBitAes_1 = new JRadioButton("192 bit AES");
		rdbtnBitAes_1.setBounds(20, 197, 109, 23);
		contentPane.add(rdbtnBitAes_1);
		rdbtnBitAes_1.addActionListener(new ActionListener(){
		    public void actionPerformed(ActionEvent e) {
		    	BitLengthOfKey = 192;
		    }
		});
		JRadioButton rdbtnBitAes_2 = new JRadioButton("256 bit AES");
		rdbtnBitAes_2.setBounds(20, 223, 109, 23);
		contentPane.add(rdbtnBitAes_2);
		rdbtnBitAes_2.addActionListener(new ActionListener(){
		    public void actionPerformed(ActionEvent e) {
		    	BitLengthOfKey = 256;
		    }
		});
		
		
		
		
		textField = new JTextField();
		textField.setBounds(217, 122, 200, 20);
		contentPane.add(textField);
		textField.setColumns(10);
		
		JLabel lblHaslo = new JLabel("Haslo");
		lblHaslo.setFont(new Font("Tahoma", Font.BOLD, 14));
		lblHaslo.setBounds(217, 96, 46, 21);
		contentPane.add(lblHaslo);
		
		textField_1 = new JTextField();
		textField_1.setBounds(217, 65, 200, 20);
		contentPane.add(textField_1);
		textField_1.setColumns(10);
		
		
		JLabel lblHasloDeszyfr = new JLabel("Haslo dla Deszyfrowania");
		lblHasloDeszyfr.setFont(new Font("Tahoma", Font.BOLD, 14));
		lblHasloDeszyfr.setBounds(217, 210, 200, 21);
		contentPane.add(lblHasloDeszyfr);
		textField_2 = new JTextField();
		textField_2.setBounds(217, 240, 200, 20);
		contentPane.add(textField_2);
		textField_2.setColumns(10);
		
		
		
		JLabel TrybOdKlienta = new JLabel("Dlugosc klucza od klienta (bit):");
		TrybOdKlienta.setBounds(20, 250, 170, 20);
		contentPane.add(TrybOdKlienta);
		textField_3 = new JTextField();
		textField_3.setBounds(20, 270, 170, 20);
		contentPane.add(textField_3);
		textField_3.setColumns(10);
		
		
		
		
		
		
		JLabel lblNazwaPlikuDla = new JLabel("Nazwa pliku dla szyfrowania");
		lblNazwaPlikuDla.setFont(new Font("Tahoma", Font.BOLD, 14));
		lblNazwaPlikuDla.setBounds(217, 34, 206, 23);
		contentPane.add(lblNazwaPlikuDla);
		
		
		JLabel lblServerV = new JLabel("Server v1.1");
		lblServerV.setBounds(430, 330, 200, 23);
		contentPane.add(lblServerV);
		
		
		JButton btnNewButton = new JButton("Zaszyfruj plik");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
				
					
					String key = textField.getText();
		    		String nazwa_dokumentu = textField_1.getText();
		    	
		 
			char[] key_arr = key.toCharArray();
			
			
			InputStream inputStream = null;
			try {
				inputStream = new FileInputStream(nazwa_dokumentu);
				} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			OutputStream outputStream_encrypt = null;
			try {
				outputStream_encrypt = new FileOutputStream("zaszyfrowany_plik.txt");
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			
			
			
			
			try {
				
				AES.encrypt(BitLengthOfKey, key_arr, inputStream, outputStream_encrypt);
				System.out.print("Bit length of AES key: ");
				System.out.println(BitLengthOfKey);
			} catch (InvalidKeyLengthException
					| StrongEncryptionNotAvailableException | IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			
			/*
			 * Sprawdzenie czy plik zostal zaszyfrowany
			 */
			if (inputStream == null || key_arr == null || BitLengthOfKey == 0)
			{
				JOptionPane.showMessageDialog(null, "Podany plik NIE zostal zaszyfrowany, nie bylo podano haslo lub nazwa pliku lub nie wybrany tryb");
			}
			else {
			JOptionPane.showMessageDialog(null, "SUKCES! Podany plik zostal zaszyfrowany"); 
			}
			
				
			}
			
			
			
			
		});
		btnNewButton.setBounds(217, 171, 200, 23);
		contentPane.add(btnNewButton);
		
		JButton btnDeszyfrujZaszyfrowanyPlik = new JButton("Deszyfruj zaszyfrowany plik");
		btnDeszyfrujZaszyfrowanyPlik.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
			
				
				String key_deszyf = textField_2.getText();
				char[] key_deszyf_arr = key_deszyf.toCharArray();
				
				InputStream inputStream_encrypt = null;
				try {
					inputStream_encrypt = new FileInputStream("zaszyfrowany_plik.txt");
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				OutputStream outputStream_decrypt = null;
				try {
					outputStream_decrypt = new FileOutputStream("deszyfrowany_plik.txt");
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				
				
				try {
					AES.decrypt(key_deszyf_arr, inputStream_encrypt, outputStream_decrypt);
					JOptionPane.showMessageDialog(null, "SUKCES! Podany plik zostal ODSZYFROWANY");
					
					
				} catch (InvalidPasswordException | InvalidAESStreamException
						| IOException | StrongEncryptionNotAvailableException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				
			}
		});
		btnDeszyfrujZaszyfrowanyPlik.setBounds(217, 273, 200, 23);
		contentPane.add(btnDeszyfrujZaszyfrowanyPlik);
		
		
		
		
		
		
		JLabel lblWybierzTryb = new JLabel("Wybierz tryb:");
		lblWybierzTryb.setFont(new Font("Tahoma", Font.BOLD, 14));
		lblWybierzTryb.setBounds(20, 148, 109, 20);
		contentPane.add(lblWybierzTryb);
		
		JLabel lblGeneratorHasel = new JLabel("Generator hasel");
		lblGeneratorHasel.setFont(new Font("Tahoma", Font.BOLD, 14));
		lblGeneratorHasel.setBounds(20, 36, 130, 20);
		contentPane.add(lblGeneratorHasel);
		
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
		
		
		JButton btnOtworz = new JButton("Otworz");
		btnOtworz.setBounds(430, 64, 70, 22);
		contentPane.add(btnOtworz);
		btnOtworz.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
				FileChooseWindow.run(new FileChooseWindow(), 270, 150);
				
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




public void wczytujdane(String password_from_client, String file_path, String aes_key_length, String deszyfruj_dane ) {
	// TODO Auto-generated method stub
	
	String No = "Tryb SZYFROWANIA";
	deszyfruj = deszyfruj_dane;
	textField_2.setText(deszyfruj);

	boolean booleanStrings = No.equals(deszyfruj); 
	
    if (booleanStrings == true) {
    	
    	
    	haslo = password_from_client;
    	sciezka_do_pliku = file_path;
    	tryb = aes_key_length;
    	
    	
    	
    	textField.setText(haslo);
    	textField_1.setText(sciezka_do_pliku);
    	textField_3.setText(tryb);
    	int BitLengthOfKey_from_string = Integer.parseInt(tryb);
    	BitLengthOfKey = BitLengthOfKey_from_string;
    	
    	
    	String key = textField.getText();
    	String nazwa_dokumentu = textField_1.getText();


    	char[] key_arr = key.toCharArray();
    	
    	
	InputStream inputStream = null;
	try {
		inputStream = new FileInputStream(nazwa_dokumentu);
	} catch (FileNotFoundException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	
		OutputStream outputStream_encrypt = null;
		try {
			outputStream_encrypt = new FileOutputStream("zaszyfrowany_plik.txt");
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}





		try {

	AES.encrypt(BitLengthOfKey, key_arr, inputStream, outputStream_encrypt);
	System.out.print("Bit length of AES key: ");
	System.out.println(BitLengthOfKey);
	} catch (InvalidKeyLengthException
	| StrongEncryptionNotAvailableException | IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}


	/*
	 * Sprawdzenie czy plik zostal zaszyfrowany
	*/
		if (inputStream == null || key_arr == null || BitLengthOfKey == 0)
		{
			JOptionPane.showMessageDialog(null, "Podany plik NIE zostal zaszyfrowany, nie bylo podano haslo lub nazwa pliku lub nie wybrany tryb");
		}
	else {
	JOptionPane.showMessageDialog(null, "SUKCES! Podany plik zostal zaszyfrowany"); 
	}
	
    	}///////////////// "if" close/////////////////////////////////
    
    else  {
    	
    	
    	String key_deszyf = textField_2.getText();
		char[] key_deszyf_arr = key_deszyf.toCharArray();
	
		InputStream inputStream_encrypt = null;
		try {
			inputStream_encrypt = new FileInputStream("zaszyfrowany_plik.txt");
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		OutputStream outputStream_decrypt = null;
		try {
			outputStream_decrypt = new FileOutputStream("deszyfrowany_plik.txt");
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
		try {
			AES.decrypt(key_deszyf_arr, inputStream_encrypt, outputStream_decrypt);
			JOptionPane.showMessageDialog(null, "SUKCES! Podany plik zostal ODSZYFROWANY");
			
			
		} catch (InvalidPasswordException | InvalidAESStreamException
				| IOException | StrongEncryptionNotAvailableException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		} ////////////// "else" close//////////////////////////////////////
		
    
	}


		}

