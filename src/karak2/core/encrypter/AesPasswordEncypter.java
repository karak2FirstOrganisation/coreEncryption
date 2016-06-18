package karak2.core.encrypter;

import java.io.UnsupportedEncodingException;
import java.security.AlgorithmParameters;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.InvalidParameterSpecException;
import java.util.UUID;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;


public class AesPasswordEncypter {
	
	private static final UUID versionId = UUID.fromString("6c928589-d710-4b05-b82f-45c7b1704891"); // change in case of breaking change
	
	public static String Decrypt(Secret secret, EncryptedData ed)
			throws NoSuchAlgorithmException,
			InvalidKeySpecException, UnsupportedEncodingException,
			InvalidKeyException, NoSuchPaddingException,
			InvalidAlgorithmParameterException, IllegalBlockSizeException,
			BadPaddingException {
		if (!ed.getEncryptorId().equals(versionId))
		{
			throw new IllegalArgumentException("Not data was encypted by unknown encryptor or unsupported version: " + versionId);
		}
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
			UnsupportedEncodingException,
			InvalidKeySpecException {

		Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
		cipher.init(Cipher.ENCRYPT_MODE, secret.getKey());
		AlgorithmParameters params = cipher.getParameters();
		byte[] iv = ((IvParameterSpec) params
				.getParameterSpec(IvParameterSpec.class)).getIV();
		byte[] ciphertext = cipher.doFinal(plaintext.getBytes("UTF-8"));
		EncryptedData et = new EncryptedData(secret.getSalt(), iv, ciphertext, versionId);
		return et;
	}
}