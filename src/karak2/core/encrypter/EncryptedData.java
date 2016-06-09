package karak2.core.encrypter;

import java.io.UnsupportedEncodingException;
import java.util.Base64;
import java.util.Map;
import java.util.UUID;

public class EncryptedData {
	
	final static String serializedFormatVersionId = "30e8a4a6-c3c1-488f-978a-15374e9fa4c0"; // GUID, it changes in case of breaking change
	
	public EncryptedData(byte[] salt, byte[] iv, byte[] encyptedBytes, UUID encryptorId) {
		this.salt = salt;
		this.iv = iv;
		this.encyptedBytes = encyptedBytes;
		this.encryptorId = encryptorId;
	}

	public EncryptedData(String saltBase64, String ivBase64,
			String encyptedBytesBase64, UUID encryptorId) throws UnsupportedEncodingException {
		this.salt = fromBase64String(saltBase64);
		this.iv = fromBase64String(ivBase64);
		this.encyptedBytes = fromBase64String(encyptedBytesBase64);
		this.encryptorId = encryptorId;
	}

	private byte[] salt;
	private byte[] iv;
	private byte[] encyptedBytes;
	private UUID encryptorId;

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
	
	public UUID getEncryptorId() {
		return encryptorId;
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
	
	private static final String SER_EncryptorId = "EncryptorId";
	private static final String SER_DataFormatVersionId = "DataFormatVersionId";
	private static final String SER_SaltBase64 = "EncryptedDataSaltBase64";
	private static final String SER_IvBase64 = "EncryptedDataIvBase64";
	private static final String SER_EncryptedBase64 = "EncryptedDataBytesBase64";
	
	public void toSerializedMap(Map<String, String> map) throws UnsupportedEncodingException
	{
		map.put(SER_DataFormatVersionId, serializedFormatVersionId);
		map.put(SER_SaltBase64, getSaltBase64String());
		map.put(SER_IvBase64 , getIvBase64String());
		map.put(SER_EncryptedBase64 , getEncryptedBase64String());
	}
		
	public static EncryptedData fromSerializedMap(Map<String, String> map) throws UnsupportedEncodingException
	{
		String versionId = map.get(SER_DataFormatVersionId);
		if (!serializedFormatVersionId.equals(versionId))
		{
			throw new IllegalArgumentException("Not data was encypted by unknown encryptor or unsupported version: " + versionId);
		}
		String saltBase64 = map.get(SER_SaltBase64);
		String ivBase64 = map.get(SER_IvBase64);
		String encyptedBytesBase64 = map.get(SER_EncryptedBase64);
		if (versionId != serializedFormatVersionId)
		{
			throw new IllegalArgumentException("Unknown data format or version. Parsing not possible. Received version id: " + versionId);
		}
		return new EncryptedData(saltBase64, ivBase64, encyptedBytesBase64, UUID.fromString(map.get(SER_EncryptorId)));
	}

	public void print() throws UnsupportedEncodingException {
		System.out.println(this.getSaltBase64String());
		System.out.println(this.getIvBase64String());
		System.out.println(this.getEncryptedBase64String());
	}
}