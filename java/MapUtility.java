public class MapUtility {
  public static String mapToJson(Map<String, String> map) {
        StringBuilder json = new StringBuilder("{");
        for (Map.Entry<String, String> entry : map.entrySet()) {
            json.append("\"").append(entry.getKey()).append("\":\"").append(entry.getValue()).append("\",");
        }
        json.deleteCharAt(json.length() - 1);
        json.append("}");
        return json.toString();
    }

    public static String mapToParams(Map<String, String> maps) {
        StringBuilder paramString = new StringBuilder();
        if (!maps.isEmpty()) {
            paramString.append("?");
        }
        for (Map.Entry<String, String> entry : maps.entrySet()) {
            paramString.append(entry.getKey()).append("=").append(entry.getValue()).append("&");
        }

        return paramString.toString();
    }

}
