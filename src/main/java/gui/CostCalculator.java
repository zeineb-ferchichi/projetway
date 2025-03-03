package gui;
import okhttp3.*;
import org.json.JSONObject;
import java.io.IOException;
import java.util.List;
import entities.trajet;
public class CostCalculator {

    private static final String API_KEY = "YOUR_API_KEY"; // Replace with your API key for the exchange rate service
    private static final String EXCHANGE_API_URL = "https://v6.exchangerate-api.com/v6/" + API_KEY + "/latest/";

    public double calculateTotalCost(List<trajet> trajets) {
        double totalCostInBaseCurrency = 0.0;

        for (trajet trajet : trajets) {
            double costInLocalCurrency = trajet.getCout(); // Get cost in local currency
            String destinationCurrency = trajet.getVille_depart(); // Assuming destination currency is available

            try {
                // Get conversion rate from destination currency to base currency (e.g., USD)
                double conversionRate = getConversionRate(destinationCurrency, "USD");
                double costInBaseCurrency = costInLocalCurrency * conversionRate;
                totalCostInBaseCurrency += costInBaseCurrency;
            } catch (IOException e) {
                showAlert("Error", "Error calculating cost: " + e.getMessage());
            }
        }

        return totalCostInBaseCurrency;
    }

    // Get the conversion rate from the destination currency to the base currency (e.g., USD)
    private double getConversionRate(String fromCurrency, String toCurrency) throws IOException {
        OkHttpClient client = new OkHttpClient();
        String url = EXCHANGE_API_URL + fromCurrency; // URL like: "https://v6.exchangerate-api.com/v6/YOUR_API_KEY/latest/USD"

        Request request = new Request.Builder()
                .url(url)
                .addHeader("Content-Type", "application/json")
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new IOException("Unexpected code " + response);
            }

            JSONObject jsonResponse = new JSONObject(response.body().string());
            JSONObject rates = jsonResponse.getJSONObject("rates");

            if (rates.has(toCurrency)) {
                return rates.getDouble(toCurrency);
            } else {
                throw new IOException("No exchange rate found for currency: " + toCurrency);
            }
        }
    }

    // Alert method for displaying errors
    private void showAlert(String title, String message) {
        // Displaying the alert to the user (use appropriate method depending on your UI framework)
        System.err.println(title + ": " + message);
    }
}
