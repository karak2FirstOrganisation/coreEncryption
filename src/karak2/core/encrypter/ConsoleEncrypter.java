package karak2.core.encrypter;

import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.InvalidParameterSpecException;
import java.util.HashMap;
import java.util.Map;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.management.InvalidAttributeValueException;

public class ConsoleEncrypter {
	public static void main(String[] args) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException, InvalidKeySpecException, InvalidAttributeValueException, InvalidParameterSpecException, UnsupportedEncodingException, InvalidAlgorithmParameterException
	{
		//System.out.print("hoy");
						
		String password = "qwe123YXC++"; 
		String message = "abcédééeffgéé így néz ki egy ABC! ŐŐÚÚŰŰÁ-.¤űá'§Mi a lóf¸s¨¨¨¨ẗ írok én itt??ÍÍ01232";
		
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
	}
}

