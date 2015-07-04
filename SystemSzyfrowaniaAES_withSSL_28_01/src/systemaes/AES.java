package systemaes;


/**
 * Klasa zawierajaca algorytm szyfrowania/deszyfrowania AES
 */
 
import java.io.*;
import java.security.*;
import java.security.spec.*;
import java.util.*;

import javax.crypto.*;
import javax.crypto.spec.*;
import javax.swing.JOptionPane;
 
/**
 * Klasa ktora predstawia szyfrowanie i deszyfrowanie AES na podstawie hasla 
 * w trybie CBC.
 * Moze byc zrobione 128, 192, 256-bitowe szyfrowanie, ale na niektorych
 * platformach 192 i 256-bitowe szyfrowanie moze byc zabronione.
 * Metody encrypt i decrypt odpowiadaja szyfrowaniu i deszyfrowaniu.
 */

public class AES {
 
	/**
	 *  Specyfikacja AES, algorytm/tryb/odstep
	 */
	private static final String CIPHER_SPEC = "AES/CBC/PKCS5Padding";
 
	/**
	 *  Metod generacji klucza Password-Based Key Derivation Function
	 *  standart generacji klucza na podstawie hasla, korzystuje 
	 *  z pseudolosowej funkcji dla otrzymania klucza. Korzystuje z HMAC 
	 */
	private static final String KEYGEN_SPEC = "PBKDF2WithHmacSHA1";
	
	/**
	 *  Dlugosc modyfikatora (sol) w bajtach
	 */ 
	private static final int SALT_LENGTH = 16; 
	
	/**
	 *  Dlugosc klucza autoryzacji w bajtach
	 */
	private static final int AUTH_KEY_LENGTH = 8; 
	/**
	 * Ilosc iteracji
	 */
	private static final int ITERATIONS = 32768;
 
	/**
	 * Dla procesow input i output
	 */
	private static final int BUFFER_SIZE = 1024;
 
 
	/**
	 * Metoda genetujaca sol
	 * @return Zwraca pseudolosowa sol (modyfikator) oznaczonej dlugosci
	 */
	private static byte[] generateSalt(int length) {
		
		SecureRandom r = new SecureRandom();
		byte[] salt = new byte[length];
		r.nextBytes(salt);
		String salt_string = Arrays.toString(salt);
		
		System.out.println("Generuje Sol przez SecureRandom"); //wypisuje do consoli
		System.out.println("Sol: " + salt_string); //wypisuje do consoli
		return salt;
	
	}
 
	/**
	 * Generuje klucz szyfrowania AES i klucz autentyfikacji dlugoscia 64 bit 
	 * z wykorzystaniem podanego hasla i soli, za pomoca PBKDF2.
	 * @param keyLength
	 *   Dlugosc klucza AES 128/192/256
	 * @param password
	 *   podane haslo na podstawie ktorego sa generowane klucze
	 * @param salt
	 *   sol na podstawie ktorej sa generowane klucze
	 * @return objekt Keys zawiera dwa wygenerowane klucza
	 */
	private static Keys keygen(int keyLength, char[] password, byte[] salt) {
		SecretKeyFactory factory;
		try {
			factory = SecretKeyFactory.getInstance(KEYGEN_SPEC);
		} catch (NoSuchAlgorithmException impossible) { return null; }
		// derive a longer key, then split into AES key and authentication key
		KeySpec spec = new PBEKeySpec(password, salt, ITERATIONS, keyLength + AUTH_KEY_LENGTH * 8);
		SecretKey tmp = null;
		try {
			tmp = factory.generateSecret(spec);
		} catch (InvalidKeySpecException impossible) { }
		byte[] fullKey = tmp.getEncoded();
		SecretKey authKey = new SecretKeySpec( // key for password authentication
				Arrays.copyOfRange(fullKey, 0, AUTH_KEY_LENGTH), "AES");
		SecretKey encKey = new SecretKeySpec( // key for AES encryption
				Arrays.copyOfRange(fullKey, AUTH_KEY_LENGTH, fullKey.length), "AES");
		return new Keys(encKey, authKey);
	}
 
	/**
	 * Szyfruje strumien danych, zaszyfrowany strumien sklada sie z naglowka i danych AES. 
	 *   Naglowek jest zrobiony w nastepujacy sposob:
	 *   keyLength: AES dlugosc klucza w bajtach (16, 24, 32)
	 *   salt: pseudorandom salt used to derive keys from password (16 bytes)
	 *   Klucz autentyfikacji: stworzony na podstawie hasla i soli 8 bajtow, stosowany
	 *   po to zeby sprawdzic prawidlowosc hasla przy deszyfrowaniu
	 *   IV: pseudolosowy vektor inicjalizacji AES 16 bajtow
	 * 
	 * 
	 * @param keyLength
	 *   dlugosc klucza ktora wykorzystuje szyfrowanie AES moze byc 128, 192, 256 bit
	 * @param password
	 *   haslo dla szyfrowania
	 * @param input
	 *   strumien bajtow dla szyfrowania
	 * @param output
	 *   strumien w ktory sa zapisywane zaszyfrowane dane
	 * @throws AES.InvalidKeyLengthException
	 *   jezeli dlugosc klucz nie jest rowna 128, 192, or 256
	 * @throws AES.StrongEncryptionNotAvailableException
	 *   jezeli nie ma mozliwosci korzystania z 192/256 bitowego szyfrowania
	 * @throws IOException
	 */
	public static void encrypt(int keyLength, char[] password, InputStream input, OutputStream output)
			throws InvalidKeyLengthException, StrongEncryptionNotAvailableException, IOException {
		// Sprawdzedie poprawnosci dlugosci klucza
		if (keyLength != 128 && keyLength != 192 && keyLength != 256) {
			throw new InvalidKeyLengthException(keyLength);
		}
		
		// generujemy sol i tworzymy klucze 
		byte[] salt = generateSalt(SALT_LENGTH);
		Keys keys = keygen(keyLength, password, salt);
		
		// zaczynamy szyfrowanie AES
		Cipher encrypt = null;
		try {
			encrypt = Cipher.getInstance(CIPHER_SPEC);
			encrypt.init(Cipher.ENCRYPT_MODE, keys.encryption);
		} catch (NoSuchAlgorithmException | NoSuchPaddingException impossible) { }
		  catch (InvalidKeyException e) {
			throw new StrongEncryptionNotAvailableException(keyLength);
		}
		
		// Otrzymujemy wektor inicjujacy
		byte[] iv = null;
		try {
			iv = encrypt.getParameters().getParameterSpec(IvParameterSpec.class).getIV();
		} catch (InvalidParameterSpecException impossible) { }
		
		// inicjalizacja AES
		output.write(keyLength / 8);
		output.write(salt);
		output.write(keys.authentication.getEncoded());
		output.write(iv);
 
		// wczytuje dane z wejscia do bufera, szyfryje i zapisuje do wyjscia
		byte[] buffer = new byte[BUFFER_SIZE];
		int numRead;
		byte[] encrypted = null;
		while ((numRead = input.read(buffer)) > 0) {
			encrypted = encrypt.update(buffer, 0, numRead);
			if (encrypted != null) {
				output.write(encrypted);
			}
		}
		try { 
			encrypted = encrypt.doFinal(); // finalizacja szyfrowania
		} catch (IllegalBlockSizeException | BadPaddingException impossible) { }
		if (encrypted != null) {
			output.write(encrypted);
		}
	}
 
	/**
	 * Deszyfruje dane zaszyfrowane za pomoca algorytmu Encrypt
	 * Parametry:
	 * @param password
	 *   haslo dla deszyfrowania (takie jak bylo podane przy szyfrowaniu)
	 * @param input
	 *   strumien zaszyfrowanych danych
	 * @param output
	 *   strumien w ktory beda zapisane odszyfrowane dane
	 *   
	 * @return zwraca dlugosc klucza dla deszyfrowania 128/196/256
	 * 
	 * @throws AES.InvalidPasswordException
	 *   nieprawidlowe haslo
	 * @throws AES.InvalidAESStreamException
	 *   jezeli podany strumien nie byl zaszyfrowany przez AES
	 * @throws AES.StrongEncryptionNotAvailableException
	 *   jezeli nie sa dostepne tryby 192/256 bitowego szyfrowania
	 * @throws IOException
	 */
	public static int decrypt(char[] password, InputStream input, OutputStream output)
			throws InvalidPasswordException, InvalidAESStreamException, IOException,
			StrongEncryptionNotAvailableException {
		int keyLength = input.read() * 8;
		// Sprawdzedie poprawnosci dlugosci klucza
		if (keyLength != 128 && keyLength != 192 && keyLength != 256) {
			throw new InvalidAESStreamException();
		}
		
		// odczytuje sol, generuje klucze i sprawdza haslo
		byte[] salt = new byte[SALT_LENGTH];
		input.read(salt);
		Keys keys = keygen(keyLength, password, salt);
		byte[] authRead = new byte[AUTH_KEY_LENGTH];
		input.read(authRead);
		if (!Arrays.equals(keys.authentication.getEncoded(), authRead)) {
			
			JOptionPane.showMessageDialog(null, "Nieprawidłowe hasło!"); // komunikat ze haslo nieprawidlowe
			throw new InvalidPasswordException();
			
		}
		
		// inicjalizacja deszyfrowania AES
		byte[] iv = new byte[16]; // 16-byte, nie zalezy od rozmiaru klucza
		input.read(iv);
		Cipher decrypt = null;
		try {
			decrypt = Cipher.getInstance(CIPHER_SPEC);
			decrypt.init(Cipher.DECRYPT_MODE, keys.encryption, new IvParameterSpec(iv));
		} catch (NoSuchAlgorithmException | NoSuchPaddingException
				| InvalidAlgorithmParameterException impossible) { }
		  catch (InvalidKeyException e) { 
			throw new StrongEncryptionNotAvailableException(keyLength);
		}
		
		// wczytuje dane z wejscia do bufera deszyfruje i zapisuje do wyjscia
		byte[] buffer = new byte[BUFFER_SIZE];
		int numRead;
		byte[] decrypted;
		while ((numRead = input.read(buffer)) > 0) {
			decrypted = decrypt.update(buffer, 0, numRead);
			if (decrypted != null) {
				output.write(decrypted);
			}
		}
		try { // finalizacja deszyfrowania
			decrypted = decrypt.doFinal();
		} catch (IllegalBlockSizeException | BadPaddingException e) {
			throw new InvalidAESStreamException(e);
		}
		if (decrypted != null) {
			output.write(decrypted);
		}
 
		return keyLength;
	}
 
	/**
	 * Klucze szyfrowania i uwierzetylnienia zwrocone przez keygen
	 */
	private static class Keys {
		public final SecretKey encryption, authentication;
		public Keys(SecretKey encryption, SecretKey authentication) {
			this.encryption = encryption;
			this.authentication = authentication;
		}
	}
	
	
	// --------Wyjatki-----------
	
	/**
	 * Nieprawidlowe haslo.
	 */
	@SuppressWarnings("serial")
	public static class InvalidPasswordException extends Exception { }
	
	/**
	 * Nieprawidlowa dlugosc klucza AES.
	 */
	@SuppressWarnings("serial")
	public static class InvalidKeyLengthException extends Exception {
		InvalidKeyLengthException(int length) {
			super("Invalid AES key length: " + length);
		}
	}
	
	/**
	 * Tryb zabroniony
	 */
	@SuppressWarnings("serial")
	public static class StrongEncryptionNotAvailableException extends Exception {
		public StrongEncryptionNotAvailableException(int keySize) {
			
			
			super(keySize + "-bit AES encryption is not available on this Java platform.");
			JOptionPane.showMessageDialog(null, "Tryb zabroniony na twojej platwormie!");
			System.exit(0);
		}
	}
	
	/**
	 * Jezeli podany strumien nie byl zaszyfrowany przez AES.
	 */
	@SuppressWarnings("serial")
	public static class InvalidAESStreamException extends Exception {
		public InvalidAESStreamException() { super(); };
		public InvalidAESStreamException(Exception e) { super(e); }
	}
 
}