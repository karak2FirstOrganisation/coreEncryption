package karak2.core.encrypter;

import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.InvalidParameterSpecException;

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
		
		AesPasswordEncypter aes = new AesPasswordEncypter();
		
		EncryptedData et = aes.Encrypt(password, message);
		System.out.println(et.toSerializedString());
		
		//password = "qwe123YXC+";
		//password = "wrong";
		
		String decrypted = aes.Decrypt(password, et);
		
		System.out.println(decrypted);
	}
}

