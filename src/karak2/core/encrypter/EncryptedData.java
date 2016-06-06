package karak2.core.encrypter;

import java.io.UnsupportedEncodingException;
import java.util.Base64;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.sun.media.sound.InvalidDataException;

public class EncryptedData {
	
	final String serializedFormatVersionId = "30e8a4a6-c3c1-488f-978a-15374e9fa4c0"; // GUID, it changes in case of breaking change
	
	public EncryptedData(byte[] salt, byte[] iv, byte[] encyptedBytes) {
		this.salt = salt;
		this.iv = iv;
		this.encyptedBytes = encyptedBytes;
	}

	public EncryptedData(String saltBase64, String ivBase64,
			String encyptedBytesBase64) throws UnsupportedEncodingException {
		this.salt = fromBase64String(saltBase64);
		this.iv = fromBase64String(ivBase64);
		this.encyptedBytes = fromBase64String(encyptedBytesBase64);
	}

	private byte[] salt;
	private byte[] iv;
	private byte[] encyptedBytes;

	private static String toBase64String(byte[] originalBytes)
			throws UnsupportedEncodingException {

		byte[] byteBase64 = Base64.getEncoder().encode(originalBytes);
		return new String(byteBase64, "UTF-8");
	}

	private static byte[] fromBase64String(String str)
			throws UnsupportedEncodingException {

		byte[] originalBytes = Base64.getDecoder().decode(str);
		return originalBytes;
	}

	public byte[] getSalt() {
		return (byte[]) salt.clone();
	}

	public byte[] getIv() {
		return (byte[]) iv.clone();
	}

	public byte[] getEcyptedBytes() {
		return (byte[]) encyptedBytes.clone();
	}

	public String getSaltBase64String() throws UnsupportedEncodingException {
		return toBase64String(this.salt);
	}

	public String getEncryptedBase64String()
			throws UnsupportedEncodingException {
		return toBase64String(this.encyptedBytes);
	}

	public String getIvBase64String() throws UnsupportedEncodingException {
		return toBase64String(this.iv);
	}
	
	private static final String SER_SerializedFormatVersionId = "SerializedFormatVersionId";
	private static final String SER_SaltBase64 = "SaltBase64";
	private static final String SER_IvBase64 = "IvBase64";
	private static final String SER_EncryptedBase64 = "EncryptedBase64";
	private static final String SER_SeparatorToken = ";"; // not special character neither in Base64 nor in Regex nor in GUID nor in field names
	
	public String toSerializedString() throws UnsupportedEncodingException
	{
		return  SER_SeparatorToken + SER_SerializedFormatVersionId + SER_SeparatorToken + serializedFormatVersionId + 
				SER_SeparatorToken + SER_SaltBase64 + SER_SeparatorToken + getSaltBase64String() +
				SER_SeparatorToken + SER_IvBase64 + SER_SeparatorToken + getIvBase64String() +
				SER_SeparatorToken + SER_EncryptedBase64 + SER_SeparatorToken + getEncryptedBase64String() + 
				SER_SeparatorToken; // closing token is important as much as the opening
	}
	
	private static String getField(String fieldName, String input)
	{
		String patStr = SER_SeparatorToken + fieldName + SER_SeparatorToken + "([^" + SER_SeparatorToken  + "]*)" + SER_SeparatorToken; // regex: ;filedName;([^;]*); 
		Pattern p = Pattern.compile(patStr);
		Matcher m = p.matcher(input);
		if (m.find())
		{
			String result = m.group(1);
			return result;
		}
		return "";
	}
		
	public EncryptedData fromSerializedString(String serializedString) throws InvalidDataException, UnsupportedEncodingException
	{
		String versionId = getField(SER_SerializedFormatVersionId, serializedString);
		String saltBase64 = getField(SER_SaltBase64, serializedString);
		String ivBase64 = getField(SER_IvBase64, serializedString);
		String encyptedBytesBase64 = getField(SER_EncryptedBase64, serializedString);
		if (versionId != serializedFormatVersionId)
		{
			throw new InvalidDataException("Unknown version id. Parsing not possible. Received version id: " + versionId);
		}
		return new EncryptedData(saltBase64, ivBase64, encyptedBytesBase64);
	}

	public void print() throws UnsupportedEncodingException {
		System.out.println(this.getSaltBase64String());
		System.out.println(this.getIvBase64String());
		System.out.println(this.getEncryptedBase64String());
	}
}