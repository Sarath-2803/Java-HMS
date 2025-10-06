package util;

import java.util.Map;

public class HelperUtils{
	public static String extractJsonValue(String json, String key){
	// Match "key": "value" OR "key": value (number, boolean)
	String pattern = "\"" + key + "\"\\s*:\\s*\"?([^\"]+?)\"?(,|})";
	java.util.regex.Pattern r = java.util.regex.Pattern.compile(pattern);
	java.util.regex.Matcher m = r.matcher(json);
	if (m.find()){
		return m.group(1).trim();
	}
    		return null;
	}

	public static Map<String, String> parseQuery(String query) {
    		Map<String, String> map = new java.util.HashMap<>();
		if (query == null || query.isEmpty()) return map;

		String[] pairs = query.split("&");
		for (String pair : pairs) {
			String[] kv = pair.split("=");
			if (kv.length == 2) {
        			map.put(kv[0], kv[1]);
        		}
    		}
    		return map;
	}
}
