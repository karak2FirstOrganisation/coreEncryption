package karak2.core.encrypter;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MapToTextSerializer {

	// example: ;key1,value1;key2,valu2;
	// pattern: ;([^,;]*),([^,;]*);

	private static final String pattern = "([^,;]*),([^,;]*);";
	private static final String SER_ItemSeparator = ";"; // not valid character
															// in Base64 or in
															// GUID, not special
															// in Regex
	private static final String SER_KeyValueSeparator = ","; // not valid
																// character in
																// Base64 or in
																// GUID, not
																// special in
																// Regex

	public static String Serialize(Map<String, String> map) {
		ContractRequiresHasNoSeparatorCharacter(map);
		if (map.size() == 0) {
			return "";
		}
		StringBuilder sb = new StringBuilder();
		for (String key : map.keySet()) {
			sb.append(SER_ItemSeparator);
			sb.append(key);
			sb.append(SER_KeyValueSeparator);
			sb.append(map.get(key));
		}
		sb.append(SER_ItemSeparator);
		return sb.toString();
	}

	private static void ContractRequiresHasNoSeparatorCharacter(
			Map<String, String> map) {
		for (String key : map.keySet()) {
			if (key.contains(SER_ItemSeparator)
					|| key.contains(SER_KeyValueSeparator)
					|| map.get(key).contains(SER_ItemSeparator)
					|| map.get(key).contains(SER_KeyValueSeparator)) {
				throw new IllegalArgumentException("Nor key nor value shall contain separator charters (, or ;)");
			}
		}
	}

	public static Map<String, String> Deserialize(String input) {
		HashMap<String, String> map = new HashMap<String, String>();
		Pattern p = Pattern.compile(pattern);
		Matcher m = p.matcher(input);
		while (m.find()) {
			String key = m.group(1);
			String value = m.group(2);
			if (key == null) {
				continue;
			}
			map.put(key, value);
		}
		return map;
	}
}
