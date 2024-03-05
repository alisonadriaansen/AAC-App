package com.alison.aac_app.sentence_logic;

import android.util.Log;

import com.alison.aac_app.BuildConfig;
import okhttp3.*;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;


public class AACOpenAIIntegration {

    private static final String OPENAI_API_KEY = BuildConfig.myAPI_KEY;
    private static final String OPENAI_ENDPOINT = "https://api.openai.com/v1/chat/completions";
    private static final String MODEL_ID = "gpt-3.5-turbo-0125";

    public AACOpenAIIntegration() {
    }

    public static String generateResponse(String words) throws IOException, JSONException {
        String[] words_split = words.split(" ");
        System.out.println("Enter up to 3 words (separated by spaces):");
        StringBuilder promptBuilder = new StringBuilder();
        promptBuilder.append("As a child using an AAC app, you might want to say: ");
        for (String word : words_split) {
            promptBuilder.append(word).append(" ");
        }

        // Create JSON request body
        JSONObject requestBody = new JSONObject()
                .put("model", MODEL_ID)
                .put("messages", new JSONArray()
                        .put(new JSONObject()
                                .put("role", "user")
                                .put("content", promptBuilder.toString())
                        )
                )
                .put("max_tokens", 200)
                .put("temperature", 0.7);

        // Create OkHttp client
        OkHttpClient client = new OkHttpClient();

        System.out.println(OPENAI_API_KEY);

        // Create request object
        Request request = new Request.Builder()
                .url(OPENAI_ENDPOINT)
                .post(RequestBody.create(MediaType.parse("application/json"), requestBody.toString()))
                .addHeader("Authorization", "Bearer " + OPENAI_API_KEY)
                .build();

        Headers headers = request.headers();
        for (String name : headers.names()) {
            Log.d("RequestHeader", name + ": " + headers.get(name));
        }

        // Execute the request
        Response response = client.newCall(request).execute();

        // Parse and process the response
        if (response.isSuccessful()) {
            String responseBody = response.body().string();
            JSONObject jsonResponse = new JSONObject(responseBody);
            JSONArray choices = jsonResponse.getJSONArray("choices");
            if (choices.length() > 0) {
                JSONObject firstChoice = choices.getJSONObject(0);
                JSONObject gptResponse = (JSONObject) firstChoice.get("message");
                System.out.println(gptResponse.get("content"));
                return gptResponse.get("content").toString();
            }
        } else {
            throw new IOException("Unexpected response code: " + response.code());
        }
        response.close();

        return "No response generated.";
    }
}
