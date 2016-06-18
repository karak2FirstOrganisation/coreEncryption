package test;

import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.InvalidParameterSpecException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import karak2.core.encrypter.AesPasswordEncypter;
import karak2.core.encrypter.EncryptedData;
import karak2.core.encrypter.Secret;

public class AesPasswordEncypterTest {

	 private static Secret simpleSecret;
	 private static Secret complexSecret;
	 private static Secret emptySecret;
	
	 @BeforeClass
     public static void oneTimeSetUp() throws NoSuchAlgorithmException, InvalidKeySpecException, IllegalArgumentException {
		 simpleSecret = Secret.CreateForNewEncryption("pass");
		 complexSecret = Secret.CreateForNewEncryption("â‚¬Ã�â€žÅ§â€“áš â€�Ã¤Ä‘Ä�Ã·Ã—[0123456789abcdef");
		 emptySecret = Secret.CreateForNewEncryption("");
     }

	
	@Test
	public void testEncrypt_simple_pass_and_text() throws InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidParameterSpecException, IllegalBlockSizeException, BadPaddingException, UnsupportedEncodingException, InvalidKeySpecException, InvalidAlgorithmParameterException 
	{
		EncryptedData ed = AesPasswordEncypter.Encrypt(simpleSecret, "asdf");
		String result = AesPasswordEncypter.Decrypt(simpleSecret, ed);
		Assert.assertNotEquals("asdf", ed.getEncryptedBase64String());
		Assert.assertEquals("asdf", result);
	}
	
	@Test
	public void testEncrypt_long_complex_pass_and_text() throws InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidParameterSpecException, IllegalBlockSizeException, BadPaddingException, UnsupportedEncodingException, InvalidKeySpecException, InvalidAlgorithmParameterException
	{
		String complexPlainText = "0123456789abcdefÂ¬~Â¬~Ë‡^Ë˜Â°Ë›`Ë™'Ë�\"Â¸|Ã„Â¶Å§â€“áš â‚¬Ã�â€žâ€�Ã·Ã—Ã¤Ä‘Ä�[]Ä§Ã­Å‚ð��‰Å�$ÃŸÂ¤<>#&@{}<;>à¶«*ÃšÅ�Å°Ã�_:?Ã�YÃ“ÃœÃ–)â±—ð�ŒºÏŸà¨�";
		EncryptedData ed = AesPasswordEncypter.Encrypt(complexSecret, complexPlainText);
		String result = AesPasswordEncypter.Decrypt(complexSecret, ed);
		Assert.assertNotEquals(complexPlainText, ed.getEncryptedBase64String());
		Assert.assertEquals(complexPlainText, result);
	}
	
	@Test
	public void testEncrypt_empty_input_produces_valid_encription() throws InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidParameterSpecException, IllegalBlockSizeException, BadPaddingException, UnsupportedEncodingException, InvalidKeySpecException, InvalidAlgorithmParameterException
	{
		String complexPlainText = "";
		EncryptedData ed = AesPasswordEncypter.Encrypt(complexSecret, complexPlainText);
		String result = AesPasswordEncypter.Decrypt(complexSecret, ed);
		Assert.assertNotEquals(complexPlainText, ed.getEncryptedBase64String());
		Assert.assertEquals(complexPlainText, result);
	}
	
	@Test(expected=NullPointerException.class)
	public void testEncrypt_null_input_produces_exception() throws InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidParameterSpecException, IllegalBlockSizeException, BadPaddingException, UnsupportedEncodingException, InvalidKeySpecException, InvalidAlgorithmParameterException
	{
		String complexPlainText = null;
		AesPasswordEncypter.Encrypt(complexSecret, complexPlainText);
	}
	
	@Test(expected=NullPointerException.class)
	public void testEncrypt_null_pass_produces_exception() throws InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidParameterSpecException, IllegalBlockSizeException, BadPaddingException, UnsupportedEncodingException, InvalidKeySpecException, InvalidAlgorithmParameterException
	{
		Secret nullSecret = Secret.CreateForNewEncryption(null);
		String complexPlainText = "0123456789abcdefÂ¬~Â¬~Ë‡^Ë˜Â°Ë›`Ë™'Ë�\"Â¸|Ã„Â¶Å§â€“áš â‚¬Ã�â€žâ€�Ã·Ã—Ã¤Ä‘Ä�[]Ä§Ã­Å‚ð��‰Å�$ÃŸÂ¤<>#&@{}<;>à¶«*ÃšÅ�Å°Ã�_:?Ã�YÃ“ÃœÃ–)â±—ð�ŒºÏŸà¨�";
		AesPasswordEncypter.Encrypt(nullSecret, complexPlainText);
	}
	
	@Test
	public void testEncrypt_pass_can_be_empty() throws InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidParameterSpecException, IllegalBlockSizeException, BadPaddingException, UnsupportedEncodingException, InvalidKeySpecException, InvalidAlgorithmParameterException
	{
		String complexPlainText = "0123456789abcdefÂ¬~Â¬~Ë‡^Ë˜Â°Ë›`Ë™'Ë�\"Â¸|Ã„Â¶Å§â€“áš â‚¬Ã�â€žâ€�Ã·Ã—Ã¤Ä‘Ä�[]Ä§Ã­Å‚ð��‰Å�$ÃŸÂ¤<>#&@{}<;>à¶«*ÃšÅ�Å°Ã�_:?Ã�YÃ“ÃœÃ–)â±—ð�ŒºÏŸà¨�";
		EncryptedData ed = AesPasswordEncypter.Encrypt(emptySecret, complexPlainText);
		String result = AesPasswordEncypter.Decrypt(emptySecret, ed);
		Assert.assertNotEquals(complexPlainText, ed.getEncryptedBase64String());
		Assert.assertEquals(complexPlainText, result);
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testDecrypted_data_salt_cannot_be_less_than_8_byte() throws InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidParameterSpecException, IllegalBlockSizeException, BadPaddingException, UnsupportedEncodingException, InvalidKeySpecException, InvalidAlgorithmParameterException
	{
		EncryptedData ed = AesPasswordEncypter.Encrypt(simpleSecret, "asdf");
		EncryptedData ed2 = new EncryptedData(new byte[]{11}, ed.getIv(), ed.getEcyptedBytes(), ed.getEncryptorId());
		Secret decryptorSecret = Secret.CreateDecryption("pass", ed2);
		String result = AesPasswordEncypter.Decrypt(decryptorSecret, ed2);
		Assert.assertNotEquals("asdf", ed2.getEncryptedBase64String());
		Assert.assertEquals("asdf", result);
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testDecrypted_data_salt_cannot_be_more_than_8_byte() throws InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidParameterSpecException, IllegalBlockSizeException, BadPaddingException, UnsupportedEncodingException, InvalidKeySpecException, InvalidAlgorithmParameterException
	{
		EncryptedData ed = AesPasswordEncypter.Encrypt(simpleSecret, "asdf");
		EncryptedData ed2 = new EncryptedData(new byte[]{1,2,3,4,5,6,7,8,9}, ed.getIv(), ed.getEcyptedBytes(), ed.getEncryptorId());
		Secret decryptorSecret = Secret.CreateDecryption("pass", ed2);
		String result = AesPasswordEncypter.Decrypt(decryptorSecret, ed2);
		Assert.assertNotEquals("asdf", ed2.getEncryptedBase64String());
		Assert.assertEquals("asdf", result);
	}
	
	@Test
	public void testDecrypted_data_empty_encrypted_bytes_result_in_empty_string() throws InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidParameterSpecException, IllegalBlockSizeException, BadPaddingException, UnsupportedEncodingException, InvalidKeySpecException, InvalidAlgorithmParameterException
	{
		EncryptedData ed = AesPasswordEncypter.Encrypt(simpleSecret, "asdf");
		EncryptedData ed2 = new EncryptedData(ed.getSalt(), ed.getIv(), new byte[0], ed.getEncryptorId()); 
		String result = AesPasswordEncypter.Decrypt(simpleSecret, ed2);
		Assert.assertEquals("", result);
	}
	
	@Test(expected=NullPointerException.class)
	public void testDecrypted_data_null_encrypted_bytes_does_not_work() throws InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidParameterSpecException, IllegalBlockSizeException, BadPaddingException, UnsupportedEncodingException, InvalidKeySpecException, InvalidAlgorithmParameterException
	{
		EncryptedData ed = AesPasswordEncypter.Encrypt(simpleSecret, "asdf");
		EncryptedData ed2 = new EncryptedData(ed.getSalt(), ed.getIv(), null, ed.getEncryptorId()); 
		String result = AesPasswordEncypter.Decrypt(simpleSecret, ed2);
		Assert.assertEquals("", result);
	}
	
	@Test(expected=NullPointerException.class)
	public void testDecrypted_data_null_salt_does_not_work() throws InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidParameterSpecException, IllegalBlockSizeException, BadPaddingException, UnsupportedEncodingException, InvalidKeySpecException, InvalidAlgorithmParameterException
	{
		EncryptedData ed = AesPasswordEncypter.Encrypt(simpleSecret, "asdf");
		EncryptedData ed2 = new EncryptedData(null, ed.getIv(), ed.getEcyptedBytes(), ed.getEncryptorId());
		Secret decryptorSecret = Secret.CreateDecryption("pass", ed2);
		String result = AesPasswordEncypter.Decrypt(decryptorSecret, ed2);
		Assert.assertEquals("asdf", result);
	}
	
	@Test(expected=NullPointerException.class)
	public void testDecrypted_data_null_iv_does_not_work() throws InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidParameterSpecException, IllegalBlockSizeException, BadPaddingException, UnsupportedEncodingException, InvalidKeySpecException, InvalidAlgorithmParameterException
	{
		EncryptedData ed = AesPasswordEncypter.Encrypt(simpleSecret, "asdf");
		EncryptedData ed2 = new EncryptedData(ed.getSalt(), null, ed.getEcyptedBytes(), ed.getEncryptorId()); 
		String result = AesPasswordEncypter.Decrypt(simpleSecret, ed2);
		Assert.assertEquals("asdf", result);
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testDecrypted_data_with_wrong_encryptor_does_not_work() throws InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidParameterSpecException, IllegalBlockSizeException, BadPaddingException, UnsupportedEncodingException, InvalidKeySpecException, InvalidAlgorithmParameterException
	{
		EncryptedData ed = AesPasswordEncypter.Encrypt(simpleSecret, "asdf");
		EncryptedData ed2 = new EncryptedData(ed.getSalt(), ed.getIv(), ed.getEcyptedBytes(), UUID.randomUUID()); 
		AesPasswordEncypter.Decrypt(simpleSecret, ed2);
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testDecrypted_data_with_wrong_format_does_not_work() throws InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidParameterSpecException, IllegalBlockSizeException, BadPaddingException, UnsupportedEncodingException, InvalidKeySpecException, InvalidAlgorithmParameterException
	{
		EncryptedData ed = AesPasswordEncypter.Encrypt(simpleSecret, "asdf");
		Map<String, String> map = new HashMap<String, String>();
		ed.toSerializedMap(map);
		map.put("DataFormatVersionId", UUID.randomUUID().toString());
		EncryptedData ed2 = EncryptedData.fromSerializedMap(map);
		AesPasswordEncypter.Decrypt(simpleSecret, ed2);
	}

}
