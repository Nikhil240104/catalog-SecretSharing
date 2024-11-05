import org.json.JSONObject;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class SecretSharing {
    public static void main(String[] args) throws Exception {
        // Step 1: Read the JSON input
        String content = new String(Files.readAllBytes(Paths.get("input.json")));
        JSONObject json = new JSONObject(content);
        
        int n = json.getJSONObject("keys").getInt("n");
        int k = json.getJSONObject("keys").getInt("k");
        
        List<Integer> xValues = new ArrayList<>();
        List<Integer> yValues = new ArrayList<>();
        
        // Step 2: Decode the Y values
        for (int i = 1; i <= n; i++) {
            JSONObject root = json.getJSONObject(String.valueOf(i));
            int x = i; // x is the key
            int y = decodeValue(root.getString("base"), root.getString("value"));
            xValues.add(x);
            yValues.add(y);
        }
        
        // Step 3: Find the constant term c using Lagrange interpolation
        int secret = calculateSecret(xValues, yValues, k);
        System.out.println("Secret (c): " + secret);
    }

    private static int decodeValue(String base, String value) {
        int baseValue = Integer.parseInt(base);
        return Integer.parseInt(value, baseValue);
    }

    private static int calculateSecret(List<Integer> xValues, List<Integer> yValues, int k) {
        int secret = 0;
        for (int i = 0; i < k; i++) {
            int xi = xValues.get(i);
            int yi = yValues.get(i);
            int li = 1;

            for (int j = 0; j < k; j++) {
                if (i != j) {
                    li *= (0 - xValues.get(j)) * modInverse(xi - xValues.get(j));
                }
            }
            secret += yi * li;
        }
        return secret % 256; 
    }

    