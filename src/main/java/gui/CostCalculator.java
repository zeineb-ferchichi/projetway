package gui;

import okhttp3.*;
import org.json.JSONObject;
import java.io.IOException;
import java.util.List;
import entities.trajet;

public class CostCalculator {

    private static final String API_KEY = "3485c58e40mshb315bf3b9aa9ee8p1fb0e1jsn245bc097e9fb"; // Correct API Key format
    private static final String GEMINI_API_URL = "\n" +
            "https://gemini-pro-ai.p.rapidapi.com/"; // Correct API URL

    public double calculateTotalCostUsingAI(List<trajet> trajets) {
        String prompt = buildPrompt(trajets);

        try {
            String aiResponse = getAIResponse(prompt);
            return extractCostFromResponse(aiResponse);
        } catch (IOException e) {
            showAlert("Error", "AI Error: " + e.getMessage());
            return 0.0;
        }
    }

    private String buildPrompt(List<trajet> trajets) {
        StringBuilder prompt = new StringBuilder("Estimate the total travel cost based on these trips:\n");

        for (trajet t : trajets) {
            prompt.append("Departure: ").append(t.getVille_depart())
                    .append(", Destination: ").append(t.getVille_arrivee())
                    .append(", Cost: ").append(t.getCout())
                    .append("\n");
        }

        prompt.append("Provide the estimated total cost in USD.");

        return prompt.toString();
    }

    private String getAIResponse(String prompt) throws IOException {
        OkHttpClient client = new OkHttpClient();
        JSONObject jsonBody = new JSONObject();
        jsonBody.put("contents", new JSONObject().put("parts", new JSONObject().put("text", prompt)));

        RequestBody body = RequestBody.create(jsonBody.toString(), MediaType.get("application/json; charset=utf-8"));
        Request request = new Request.Builder()
                .url(GEMINI_API_URL)
                .post(body)
                .addHeader("Content-Type", "application/json")
                .addHeader("X-RapidAPI-Key", API_KEY) // 🔥 Correction ici
                .addHeader("X-RapidAPI-Host", "gemini-pro-ai.p.rapidapi.com")
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new IOException("Unexpected response: " + response.body().string());
            }
            return response.body().string();
        }
    }

    private double extractCostFromResponse(String response) {
        try {
            JSONObject jsonResponse = new JSONObject(response);
            String textResponse = jsonResponse.getJSONArray("candidates")
                    .getJSONObject(0)
                    .getJSONObject("content")
                    .getJSONArray("parts")
                    .getJSONObject(0)
                    .getString("text");

            return parseCostFromText(textResponse);
        } catch (Exception e) {
            return 0.0;
        }
    }

    private double parseCostFromText(String text) {
        text = text.replaceAll("[^0-9.]", ""); // Remove non-numeric characters
        return text.isEmpty() ? 0.0 : Double.parseDouble(text);
    }

    private void showAlert(String title, String message) {
        System.err.println(title + ": " + message);
    }
}
