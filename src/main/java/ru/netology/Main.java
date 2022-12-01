package ru.netology;

import org.apache.http.HttpHeaders;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.entity.ContentType;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileOutputStream;
import java.io.IOException;

public class Main {

    static final String[] columnMapping = {"copyright", "date", "explanation",
            "hdurl", "media_type",
            "service_version", "title", "url"};

    public static void main(String[] args) throws IOException, ParseException {

        final String REMOTE_JSON = "https://api.nasa.gov/planetary/apod?api_key";
        final String MY_KEY = "SsliaJND71yoKTus6UcQKBC9aGoY6KG5VWtYbFU3";
        final int CONNECT_TIMEOUT = 5000;
        final int SOCKET_TIMEOUT = 30_000;

        CloseableHttpResponse response = null;
        CloseableHttpClient httpClient = null;
        NasaCardMedia cardMedia = null;


        try {
            httpClient = HttpClientBuilder.create()
                    .setUserAgent("My Homework")
                    .setDefaultRequestConfig(RequestConfig.custom()
                            .setConnectTimeout(CONNECT_TIMEOUT)
                            .setSocketTimeout(SOCKET_TIMEOUT)

                            .setRedirectsEnabled(false)
                            .build())
                    .build();
            // формируем запрос: стартовую строку и заголовок
            HttpGet request = new HttpGet(REMOTE_JSON + "=" + MY_KEY);
            request.setHeader(HttpHeaders.ACCEPT, ContentType.APPLICATION_JSON.getMimeType());

            // посылаем запрос и получаем json
            response = httpClient.execute(request);
            String json = EntityUtils.toString(response.getEntity());

            // Преобразуем json в экземпляры класса CatFact
            cardMedia = jsonToClass(columnMapping, json);
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }


        // Определяем имя медиа-файла
        String[] prs = cardMedia.getUrl().split("/");
        String fileName = prs[prs.length - 1];

        // Делаем запрос на получение медиа-файла
        try {
            HttpGet requestMediaFile = new HttpGet(cardMedia.getUrl());
            requestMediaFile.setHeader(HttpHeaders.ACCEPT, ContentType.IMAGE_PNG.getMimeType());
            response = httpClient.execute(requestMediaFile);
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }

        // Сохраняем контент в файле fileName
        try (FileOutputStream file = new FileOutputStream(fileName)) {
            byte[] bytes = response.getEntity().getContent().readAllBytes();
            file.write(bytes);
        } catch (IOException exc) {
            System.out.println(exc.getMessage());
        }

        // Выводим сообщение о сохраненном файле
        System.out.println(cardMedia.toString(fileName));
    }

    public static NasaCardMedia jsonToClass(String[] column, String json) throws ParseException {

        String[] attr = new String[columnMapping.length];

        JSONParser pars = new JSONParser();
        JSONObject obj = (JSONObject) pars.parse(json);

        for (int i = 0; i < column.length; i++) {
            attr[i] = (obj.get(column[i]) != null) ?
                    obj.get(column[i]).toString() : "";
        }
        NasaCardMedia card = new NasaCardMedia(attr);
        return card;
    }
}