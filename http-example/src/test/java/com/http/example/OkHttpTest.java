package com.http.example;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.*;
import org.junit.Test;

import java.io.IOException;

/**
 * com.http.example.OkHttpTest
 *
 * @author lipeng
 * @date 2019-08-16 18:45
 */
public class OkHttpTest {

    @Test
    public void testGetSync() throws IOException {
        String url = "https://raw.github.com/square/okhttp/master/README.md";
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(url)
                .build();
        Response response = client.newCall(request).execute();
        System.err.println(response.body().string());
    }

    @Test
    public void testPostSync() throws IOException {
        String url = "http://127.0.0.1:8080/students/";
        OkHttpClient client = new OkHttpClient();
        MediaType mediaType = MediaType.get(org.springframework.http.MediaType.APPLICATION_JSON_UTF8_VALUE);
        RequestBody requestBody = RequestBody.create(mediaType, getBody());
        Request request = new Request.Builder()
                .url(url)
                .post(requestBody)
                .build();
        Response response = client.newCall(request).execute();
        System.err.println("新增student======>" + response.body().string());

        request = new Request.Builder()
                .url(url)
                .build();
        response = client.newCall(request).execute();
        System.err.println("student list======>" + response.body().string());
    }

    public String getBody() throws JsonProcessingException {
        Student student = new Student();
        student.setId(10001);
        student.setName("smile");
        return new ObjectMapper().writeValueAsString(student);
    }
}
