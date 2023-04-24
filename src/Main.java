

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
// Press Shift twice to open the Search Everywhere dialog and type `show whitespaces`,
// then press Enter. You can now see whitespace characters in your code.
public class Main {
    private static final String API_URL = "https://api.openai.com/v1/audio/transcriptions";
    private static final String API_KEY = "Bearer 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";
    private static final String BOUNDARY = "---------------------------" + System.currentTimeMillis();


    public static void main(String[] args) {




                String filePath = "/home/ayoub/Desktop/2023.mp3";
                String model = "whisper-1";
                String response = null;
                try {
                    URL url = new URL(API_URL);
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("POST");
                    connection.setDoInput(true);
                    connection.setDoOutput(true);
                    connection.setUseCaches(false);
                    connection.setRequestProperty("Authorization", API_KEY);
                    connection.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + BOUNDARY);

                    // build the request body
                    OutputStream outputStream = connection.getOutputStream();
                    PrintWriter writer = new PrintWriter(new OutputStreamWriter(outputStream), true);
                    writer.append("--" + BOUNDARY).append("\r\n");
                    writer.append("Content-Disposition: form-data; name=\"model\"").append("\r\n\r\n");
                    writer.append(model).append("\r\n");

                    writer.append("--" + BOUNDARY).append("\r\n");
                    writer.append("Content-Disposition: form-data; name=\"file\"; filename=\"").append(new File(filePath).getName()).append("\"").append("\r\n");
                    writer.append("Content-Type: ").append(HttpURLConnection.guessContentTypeFromName(new File(filePath).getName())).append("\r\n\r\n");
                    writer.flush();

                    FileInputStream inputStream = new FileInputStream(new File(filePath));
                    byte[] buffer = new byte[4096];
                    int bytesRead;
                    while ((bytesRead = inputStream.read(buffer)) != -1) {
                        outputStream.write(buffer, 0, bytesRead);
                    }
                    outputStream.flush();
                    inputStream.close();

                    writer.append("\r\n").flush();
                    writer.append("--" + BOUNDARY + "--").append("\r\n");
                    writer.close();

                    // get the response
                    int statusCode = connection.getResponseCode();
                    if (statusCode == HttpURLConnection.HTTP_OK) {
                        BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                        StringBuilder responseBuilder = new StringBuilder();
                        String line;
                        while ((line = in.readLine()) != null) {
                            responseBuilder.append(line);
                        }
                        in.close();
                        response = responseBuilder.toString();
                    } else {
                        // handle error
                        response = "Failed to make API call. Response code: " + statusCode;
                    }
                    connection.disconnect();
                } catch (Exception e) {
                    e.printStackTrace();
                    response = "Failed to make API call. Exception: " + e.getMessage();
                }
               // System.out.println("Response: " + response);

        JsonParser jsonParser = new JsonParser();
        JsonObject jsonObject = jsonParser.parse(response).getAsJsonObject();
        String transcribedText = jsonObject.get("text").getAsString();
        System.out.println(transcribedText);
            }
        }





