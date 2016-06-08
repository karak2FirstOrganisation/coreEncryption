package karak2.core.encrypter;

import java.io.UnsupportedEncodingException;
import java.util.Base64;
import java.util.Map;

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
	
	private static final String SER_SerializedFormatVersionId = "EncryptedDataVersionId";
	private static final String SER_SaltBase64 = "EncryptedDataSaltBase64";
	private static final String SER_IvBase64 = "EncryptedDataIvBase64";
	private static final String SER_EncryptedBase64 = "EncryptedDataBytesBase64";
	
	public void toSerializedMap(Map<String, String> map) throws UnsupportedEncodingException
	{
		map.put(SER_SerializedFormatVersionId, serializedFormatVersionId);
		map.put(SER_SaltBase64, getSaltBase64String());
		map.put(SER_IvBase64 , getIvBase64String());
		map.put(SER_EncryptedBase64 , getEncryptedBase64String());
	}
		
	public EncryptedData fromSerializedMap(Map<String, String> map) throws UnsupportedEncodingException
	{
		String versionId = map.get(SER_SerializedFormatVersionId);
		String saltBase64 = map.get(SER_SaltBase64);
		String ivBase64 = map.get(SER_IvBase64);
		String encyptedBytesBase64 = map.get(SER_EncryptedBase64);
		if (versionId != serializedFormatVersionId)
		{
			throw new IllegalArgumentException("Unknown version id. Parsing not possible. Received version id: " + versionId);
		}
		return new EncryptedData(saltBase64, ivBase64, encyptedBytesBase64);
	}

	public void print() throws UnsupportedEncodingException {
		System.out.println(this.getSaltBase64String());
		System.out.println(this.getIvBase64String());
		System.out.println(this.getEncryptedBase64String());
	}
}