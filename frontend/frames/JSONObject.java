public class JSONObject {
    private String jsonString;

    public JSONObject(String json) {
        this.jsonString = json.trim();
    }

    public int getInt(String key) {
        String value = getValue(key);
        return Integer.parseInt(value);
    }

    public long getLong(String key) {
        String value = getValue(key);
        return Long.parseLong(value);
    }

    public String getString(String key) {
        String value = getValue(key);
        if (value.startsWith("\"") && value.endsWith("\"")) {
            return value.substring(1, value.length() - 1);
        }
        return value;
    }

    public JSONObject getJSONObject(String key) {
        String value = getValue(key);
        return new JSONObject(value);
    }

    public JSONArray getJSONArray(String key) {
        String value = getValue(key);
        return new JSONArray(value);
    }

    private String getValue(String key) {
        String searchKey = "\"" + key + "\":";
        int keyIndex = jsonString.indexOf(searchKey);
        if (keyIndex == -1) {
            throw new RuntimeException("Key not found: " + key);
        }

        int valueStart = keyIndex + searchKey.length();
        int valueEnd;
        char firstChar = jsonString.charAt(valueStart);

        if (firstChar == '"') {
            valueEnd = jsonString.indexOf('"', valueStart + 1) + 1;
        } else if (firstChar == '{') {
            valueEnd = findMatchingBrace(valueStart);
        } else if (firstChar == '[') {
            valueEnd = findMatchingBracket(valueStart);
        } else {
            valueEnd = Math.min(
                jsonString.indexOf(',', valueStart),
                jsonString.indexOf('}', valueStart)
            );
            if (valueEnd == -1) {
                valueEnd = jsonString.indexOf('}', valueStart);
            }
        }

        return jsonString.substring(valueStart, valueEnd).trim();
    }

    private int findMatchingBrace(int start) {
        int count = 0;
        for (int i = start; i < jsonString.length(); i++) {
            if (jsonString.charAt(i) == '{') count++;
            if (jsonString.charAt(i) == '}') count--;
            if (count == 0) return i + 1;
        }
        return jsonString.length();
    }

    private int findMatchingBracket(int start) {
        int count = 0;
        for (int i = start; i < jsonString.length(); i++) {
            if (jsonString.charAt(i) == '[') count++;
            if (jsonString.charAt(i) == ']') count--;
            if (count == 0) return i + 1;
        }
        return jsonString.length();
    }
}
