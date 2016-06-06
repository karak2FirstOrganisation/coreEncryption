package karak2.core.encrypter;

import java.io.UnsupportedEncodingException;
import java.security.AlgorithmParameters;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.InvalidParameterSpecException;
import java.security.spec.KeySpec;
import java.util.Base64;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import javax.management.InvalidAttributeValueException;

public class AesPasswordEncypter {
	
	public static String Decrypt(String password, EncryptedData ed)
			throws InvalidAttributeValueException, NoSuchAlgorithmException,
			InvalidKeySpecException, UnsupportedEncodingException,
			InvalidKeyException, NoSuchPaddingException,
			InvalidAlgorithmParameterException, IllegalBlockSizeException,
			BadPaddingException {

		SecretKey sk = GetSecret(password, ed.getSalt());
		byte[] iv = ed.getIv();
		byte[] ciphertext = ed.getEcyptedBytes();
		Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
		cipher.init(Cipher.DECRYPT_MODE, sk, new IvParameterSpec(iv));
		String plaintext = new String(cipher.doFinal(ciphertext), "UTF-8");
		return plaintext;
	}

	public static EncryptedData Encrypt(String password, String plaintext)
			throws NoSuchAlgorithmException, NoSuchPaddingException,
			InvalidKeyException, InvalidParameterSpecException,
			IllegalBlockSizeException, BadPaddingException,
			UnsupportedEncodingException, InvalidAttributeValueException,
			InvalidKeySpecException {

		byte[] salt = new byte[8];
		SecureRandom.getInstanceStrong().nextBytes(salt);
		SecretKey sk = GetSecret(password, salt);
		Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
		cipher.init(Cipher.ENCRYPT_MODE, sk);
		AlgorithmParameters params = cipher.getParameters();
		byte[] iv = ((IvParameterSpec) params
				.getParameterSpec(IvParameterSpec.class)).getIV();
		byte[] ciphertext = cipher.doFinal(plaintext.getBytes("UTF-8"));
		EncryptedData et = new EncryptedData(salt, iv, ciphertext);
		return et;
	}

	private static SecretKey GetSecret(String password, byte[] salt)
			throws NoSuchAlgorithmException, InvalidKeySpecException,
			InvalidAttributeValueException {

		char passwordCharArray[] = password.toCharArray();
		if (salt.length != 8) {
			throw new InvalidAttributeValueException(
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