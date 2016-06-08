package karak2.core.encrypter;

import java.io.UnsupportedEncodingException;
import java.security.AlgorithmParameters;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.InvalidParameterSpecException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.management.InvalidAttributeValueException;


public class AesPasswordEncypter {
	
	public static String Decrypt(Secret secret, EncryptedData ed)
			throws InvalidAttributeValueException, NoSuchAlgorithmException,
			InvalidKeySpecException, UnsupportedEncodingException,
			InvalidKeyException, NoSuchPaddingException,
			InvalidAlgorithmParameterException, IllegalBlockSizeException,
			BadPaddingException {

		byte[] iv = ed.getIv();
		byte[] ciphertext = ed.getEcyptedBytes();
		Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
		cipher.init(Cipher.DECRYPT_MODE, secret.getKey(), new IvParameterSpec(iv));
		String plaintext = new String(cipher.doFinal(ciphertext), "UTF-8");
		return plaintext;
	}

	public static EncryptedData Encrypt(Secret secret, String plaintext)
			throws NoSuchAlgorithmException, NoSuchPaddingException,
			InvalidKeyException, InvalidParameterSpecException,
			IllegalBlockSizeException, BadPaddingException,
			UnsupportedEncodingException, InvalidAttributeValueException,
			InvalidKeySpecException {

		Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
		cipher.init(Cipher.ENCRYPT_MODE, secret.getKey());
		AlgorithmParameters params = cipher.getParameters();
		byte[] iv = ((IvParameterSpec) params
				.getParameterSpec(IvParameterSpec.class)).getIV();
		byte[] ciphertext = cipher.doFinal(plaintext.getBytes("UTF-8"));
		EncryptedData et = new EncryptedData(secret.getSalt(), iv, ciphertext);
		return et;
	}
}