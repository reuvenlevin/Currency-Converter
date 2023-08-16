package com.example.currencyconverter.lib;

import com.example.currencyconverter.BuildConfig;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.io.InputStream;

public class ConvertUtils {
    String currencyFrom = "USD";
    String currencyTo = "ILS";
    double currencyFromAmount = 1; // $1

    public ConvertUtils(String currencyFrom, String currencyTo, double currencyFromAmount) {
        this.currencyFrom = currencyFrom;
        this.currencyTo = currencyTo;
        this.currencyFromAmount = currencyFromAmount;
    }

    public ConvertUtils(){}

    public String getCurrencyFrom() {
        return currencyFrom;
    }

    public void setCurrencyFrom(String currencyFrom) {
        this.currencyFrom = currencyFrom;
    }

    public String getCurrencyTo() {
        return currencyTo;
    }

    public void setCurrencyTo(String currencyTo) {
        this.currencyTo = currencyTo;
    }

    public double getCurrencyFromAmount() {
        return currencyFromAmount;
    }

    public void setCurrencyFromAmount(double currencyFromAmount) {
        this.currencyFromAmount = currencyFromAmount;
    }

    public double convertAndGetAmount() throws IOException {
        //
        // Setting URL
        String url_str = "https://v6.exchangerate-api.com/v6/" + BuildConfig.EXCHANGE_RATE_API_KEY + "/latest/" + this.currencyFrom;

        // Making Request
        URL url = new URL(url_str);
        HttpURLConnection request = (HttpURLConnection) url.openConnection();
        request.connect();

        // Convert to JSON
        JsonParser jp = new JsonParser();
        JsonElement root = jp.parse(new InputStreamReader((InputStream) request.getContent()));
        JsonObject jsonObject = root.getAsJsonObject();

        // Accessing object
        String req_result = jsonObject.get("result").getAsString();

        System.out.println(jsonObject.getAsJsonObject());

        // get conversion rate
        String currencyToConversionRate =
                String.valueOf(
                        jsonObject
                                .get("conversion_rates")
                                .getAsJsonObject()
                                .get(currencyTo.toUpperCase()));

        // convert fromCurrency to toCurrency
        double converted = currencyFromAmount * Double.parseDouble(currencyToConversionRate);

        System.out.println("Entered: " + currencyFromAmount + "(" + currencyFrom + ")\nConvert to (" + currencyTo + "): " + converted);

        return  converted;
    }
}
