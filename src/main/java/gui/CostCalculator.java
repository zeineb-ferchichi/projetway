package gui;

import okhttp3.*;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;
import entities.trajet;

public class CostCalculator {

    private static final String API_KEY = System.getenv("\n" +
            "3485c58e40mshb315bf3b9aa9ee8p1fb0e1jsn245bc097e9fb"); // Clé API sécurisée
    private static final String GEMINI_API_URL = "https://gemini-pro.p.rapidapi.com/generate";
    // Vérifiez l'URL correcte

    public double calculateTotalCostUsingAI(List<trajet> trajets) {
        if (API_KEY == null || API_KEY.isEmpty()) {
            showAlert("Error", "API Key is missing. Set GEMINI_API_KEY as an environment variable.");
            return 0;
        }

        String prompt = buildPrompt(trajets);

        try {
            String aiResponse = getAIResponse(prompt);
            return extractCostFromResponse(aiResponse);
        } catch (IOException e) {
            showAlert("Error", "AI Error: " + e.getMessage());
            return 0;
        }
    }

    private String buildPrompt(List<trajet> trajets) {
        StringBuilder prompt = new StringBuilder("Estimate the total travel cost based on these trips:\n");

        for (trajet t : trajets) {
            prompt.append("Trip Cost: ").append(t.getCout()).append(" USD\n");
        }

        prompt.append("Provide the estimated total cost in USD.");
        return prompt.toString();
    }

    private String getAIResponse(String prompt) throws IOException {
        OkHttpClient client = new OkHttpClient();

        JSONObject jsonBody = new JSONObject();
        jsonBody.put("model", "gemini-pro"); // Vérifiez le bon modèle
        jsonBody.put("prompt", prompt);
        jsonBody.put("temperature", 0.7);

        RequestBody body = RequestBody.create(jsonBody.toString(), MediaType.get("application/json; charset=utf-8"));

        Request request = new Request.Builder()
                .url(GEMINI_API_URL)
                .post(body)
                .addHeader("Content-Type", "application/json")
                .addHeader("X-RapidAPI-Key", API_KEY)
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

            if (!jsonResponse.has("candidates")) {
                throw new IOException("Invalid AI response format: " + response);
            }

            JSONArray candidates = jsonResponse.getJSONArray("candidates");
            if (candidates.length() == 0) return 0.0;

            String textResponse = candidates.getJSONObject(0)
                    .getJSONObject("content")
                    .getJSONArray("parts")
                    .getJSONObject(0)
                    .getString("text");

            return parseCostFromText(textResponse);
        } catch (Exception e) {
            showAlert("Error", "Failed to parse AI response: " + e.getMessage());
            return 0.0;
        }
    }

    private double parseCostFromText(String text) {
        text = text.replaceAll("[^0-9.]", "").trim(); // Garder uniquement les nombres et les points
        try {
            return text.isEmpty() ? 0.0 : Double.parseDouble(text);
        } catch (NumberFormatException e) {
            showAlert("Error", "Failed to parse cost value: " + text);
            return 0.0;
        }
    }

    private void showAlert(String title, String message) {
        System.err.println(title + ": " + message);
    }
}
