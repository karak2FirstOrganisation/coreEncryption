package karak2.core.encrypter;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;

import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;

public class Secret {
	
	private byte[] salt;
	private SecretKey key;
	
	public static Secret CreateForNewEncryption(String password) throws NoSuchAlgorithmException, InvalidKeySpecException, IllegalArgumentException
	{
		return new Secret(password);
	}
	
	public static Secret CreateDecryption(String password, EncryptedData ed) throws NoSuchAlgorithmException, InvalidKeySpecException, IllegalArgumentException
	{
		return new Secret(password, ed);
	}
	
	private Secret (String password) throws NoSuchAlgorithmException, InvalidKeySpecException, IllegalArgumentException
	{
		this.salt = new byte[8];
		SecureRandom.getInstanceStrong().nextBytes(salt);
		this.key = GetSecret(password, salt);
	}
		
	private Secret (String password, EncryptedData ed) throws NoSuchAlgorithmException, InvalidKeySpecException, IllegalArgumentException
	{
		this.salt = ed.getSalt();
		SecureRandom.getInstanceStrong().nextBytes(salt);
		this.key = GetSecret(password, salt);
	}
	
	public byte[] getSalt()
	{
		return this.salt.clone();
	}
	
	public SecretKey getKey()
	{
		return this.key; // TODO: check if this object is immutable. If it is not, it must be cloned before giving it out
	}
	
	private static SecretKey GetSecret(String password, byte[] salt)
			throws NoSuchAlgorithmException, InvalidKeySpecException,
			IllegalArgumentException {

		char passwordCharArray[] = password.toCharArray();
		if (salt.length != 8) {
			throw new IllegalArgumentException(
					"Salt shall be 8 byte long");
		}
		SecretKeyFactory factory = SecretKeyFactory
				.getInstance("PBKDF2WithHmacSHA256");

		KeySpec spec = new PBEKeySpec(passwordCharArray, salt, 1, 128);
		SecretKey tmp = factory.generateSecret(spec);
		SecretKey secret = new SecretKeySpec(tmp.getEncoded(), "AES");
		return secret;
	}
}
