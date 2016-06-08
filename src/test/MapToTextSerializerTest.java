package test;
import java.util.HashMap;
import java.util.Map;

import karak2.core.encrypter.MapToTextSerializer;

import org.junit.Assert;
import org.junit.Test;

public class MapToTextSerializerTest {

	@Test
	public void serialize_simple_map() {
		HashMap<String, String> map = new HashMap<String, String>();
		map.put("k1", "v1");
		map.put("k2", "v2");
		map.put("k6", "v6");
		
		String serializedStr = MapToTextSerializer.Serialize(map);
		Map<String, String> mapResult = MapToTextSerializer.Deserialize(serializedStr);
		Assert.assertEquals(map, mapResult);
	}
	
	@Test
	public void serialize_with_special_characters() {
		String complexPlainText = "0123456789abcdef¬~¬~ˇ^˘°˛`˙'˝\"¸|Ä¶ŧ–ᚠ€Í„”÷×äđĐ[]ħíł𐏉Ł$ß¤<>#&@{}<>ණ*ÚŐŰÁ_:?ÍYÓÜÖ)ⱗ𐌺ϟਐ";
		HashMap<String, String> map = new HashMap<String, String>();
		map.put("k1", complexPlainText);
		map.put(complexPlainText, "v2");
		String serializedStr = MapToTextSerializer.Serialize(map);
		Map<String, String> mapResult = MapToTextSerializer.Deserialize(serializedStr);
		Assert.assertEquals(map, mapResult);
	}
	
	@Test
	public void serialize_empty_map() {
		HashMap<String, String> map = new HashMap<String, String>();
		
		String serializedStr = MapToTextSerializer.Serialize(map);
		Assert.assertEquals("", serializedStr);
	}
	
	@Test(expected=NullPointerException.class)
	public void serialize_null_map() {
		MapToTextSerializer.Serialize(null);
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void serialize_invalid_key_comma() {
		HashMap<String, String> map = new HashMap<String, String>();
		map.put("k,1", "v1");
		MapToTextSerializer.Serialize(map);
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void serialize_invalid_key_semicomma() {
		HashMap<String, String> map = new HashMap<String, String>();
		map.put("k;1", "v1");
		MapToTextSerializer.Serialize(map);
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void serialize_invalid_value_comma() {
		HashMap<String, String> map = new HashMap<String, String>();
		map.put("k1", "v,1");
		MapToTextSerializer.Serialize(map);
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void serialize_invalid_value_semicomma() {
		HashMap<String, String> map = new HashMap<String, String>();
		map.put("k;1", "v;1");
		MapToTextSerializer.Serialize(map);
	}
	
	@Test
	public void deserialize_simple_map() {
		HashMap<String, String> map = new HashMap<String, String>();
		map.put("k1", "v1");
		map.put("k2", "v2");
		map.put("k6", "v6");
		
		Map<String, String> mapResult = MapToTextSerializer.Deserialize(";k1,v1;k2,v2;k6,v6;");
		Assert.assertTrue(map.equals(mapResult));
	}
	
	@Test
	public void deserialize_empty_string() {
		HashMap<String, String> map = new HashMap<String, String>();
		Map<String, String> mapResult = MapToTextSerializer.Deserialize("");
		Assert.assertTrue(map.equals(mapResult));
	}
	
	@Test
	public void deserialize_only_separators_invalid_syntax_case1() {
		HashMap<String, String> map = new HashMap<String, String>();
		Map<String, String> mapResult = MapToTextSerializer.Deserialize(";;;;;;;;;;");
		Assert.assertTrue(map.equals(mapResult));
	}
	
	@Test
	public void deserialize_only_separators_invalid_syntax_case2() {
		HashMap<String, String> map = new HashMap<String, String>();
		Map<String, String> mapResult = MapToTextSerializer.Deserialize(",,,,,,,");
		Assert.assertTrue(map.equals(mapResult));
	}
	
	@Test
	public void deserialize_only_separators_valid_syntax() {
		HashMap<String, String> map = new HashMap<String, String>();
		map.put("", "");
		Map<String, String> mapResult = MapToTextSerializer.Deserialize(";,;,;,;,;,;");
		Assert.assertTrue(map.equals(mapResult));
	}
	
	@Test
	public void deserialize_invalid_data_can_lead_to_unexpected_deserialized_map() {
		HashMap<String, String> map = new HashMap<String, String>();
		Map<String, String> mapResult = MapToTextSerializer.Deserialize(";k;v;");
		Assert.assertTrue(map.equals(mapResult));
		mapResult = MapToTextSerializer.Deserialize(";k,v,f;"); // unexpected result
		Assert.assertFalse(map.equals(mapResult));
	}
	
	@Test(expected=NullPointerException.class)
	public void deserialize_null_string() {
		MapToTextSerializer.Deserialize(null);
	}
	
	

}
