public class JSONArray {
    private String jsonString;

    public JSONArray(String json) {
        this.jsonString = json.trim();
        if (jsonString.startsWith("[")) {
            jsonString = jsonString.substring(1);
        }
        if (jsonString.endsWith("]")) {
            jsonString = jsonString.substring(0, jsonString.length() - 1);
        }
    }

    public int length() {
        if (jsonString.trim().isEmpty()) return 0;
        int count = 1;
        int braceCount = 0;
        int bracketCount = 0;
        boolean inString = false;

        for (char c : jsonString.toCharArray()) {
            if (c == '"') inString = !inString;
            if (!inString) {
                if (c == '{') braceCount++;
                if (c == '}') braceCount--;
                if (c == '[') bracketCount++;
                if (c == ']') bracketCount--;
                if (c == ',' && braceCount == 0 && bracketCount == 0) count++;
            }
        }
        return count;
    }

    public JSONObject getJSONObject(int index) {
        String value = getElement(index);
        return new JSONObject(value);
    }

    public String getString(int index) {
        String value = getElement(index);
        if (value.startsWith("\"") && value.endsWith("\"")) {
            return value.substring(1, value.length() - 1);
        }
        return value;
    }

    private String getElement(int index) {
        int currentIndex = 0;
        int start = 0;
        int braceCount = 0;
        int bracketCount = 0;
        boolean inString = false;

        for (int i = 0; i < jsonString.length(); i++) {
            char c = jsonString.charAt(i);
            if (c == '"') inString = !inString;
            if (!inString) {
                if (c == '{') braceCount++;
                if (c == '}') braceCount--;
                if (c == '[') bracketCount++;
                if (c == ']') bracketCount--;
                if (c == ',' && braceCount == 0 && bracketCount == 0) {
                    if (currentIndex == index) {
                        return jsonString.substring(start, i).trim();
                    }
                    currentIndex++;
                    start = i + 1;
                }
            }
        }
        if (currentIndex == index) {
            return jsonString.substring(start).trim();
        }
        throw new RuntimeException("Index out of bounds: " + index);
    }
}
