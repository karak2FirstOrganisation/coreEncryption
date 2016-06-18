package karak2.core.encrypter;

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

public class ConsoleEncrypter {
	public static void main(String[] args) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException, InvalidKeySpecException, InvalidParameterSpecException, UnsupportedEncodingException, InvalidAlgorithmParameterException
	{
		//System.out.print("hoy");
						
		String password = "qwe123YXC++"; 
		String message = "abcÃ©dÃ©Ã©effgÃ©Ã© Ã­gy nÃ©z ki\n\n egy ABC! Å�Å�ÃšÃšÅ°Å°Ã�-.Â¤Å±Ã¡'Â§Mi a lÃ³fÂ¸sÂ¨Â¨Â¨Â¨áº— Ã­rok Ã©n itt??Ã�Ã�01232";
		
		Secret secret = Secret.CreateForNewEncryption(password);
		AesPasswordEncypter aes = new AesPasswordEncypter();
		
		EncryptedData et = aes.Encrypt(secret, message);
		Map<String, String> map = new HashMap<String,String>();
		et.toSerializedMap(map);
		map.put("PasswordReminder", "kisatol2ig");
		System.out.println(MapToTextSerializer.Serialize(map));
		
		//password = "qwe123YXC+";
		//password = "wrong";
		
		String decrypted = aes.Decrypt(secret, et);
		
		System.out.println(decrypted);
		System.out.println(UUID.randomUUID());
	}
}

