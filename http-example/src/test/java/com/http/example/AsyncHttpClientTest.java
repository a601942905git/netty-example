package com.http.example;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.asynchttpclient.*;
import org.junit.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

import java.io.IOException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

/**
 * com.http.example.AsyncHttpClientTest
 *
 * @author lipeng
 * @date 2019-08-16 15:34
 */
public class AsyncHttpClientTest {

    @Test
    public void testGetSync() throws ExecutionException, InterruptedException, IOException {
        String requestUrl = "http://localhost:8080/hello?name=asynchttpclient";
        AsyncHttpClient asyncHttpClient = init();
        ListenableFuture<Response> listenableFuture = asyncHttpClient.prepareGet(requestUrl).execute();
        Response response = listenableFuture.get();
        System.err.println("response from server：" + response.getResponseBody());
        System.err.println("main thread execute finish......");
        close(asyncHttpClient);
    }

    @Test
    public void testGetSyncWithRetry() throws ExecutionException, InterruptedException, IOException {
        String requestUrl = "http://localhost:8080/retry";
        DefaultAsyncHttpClientConfig.Builder config = Dsl.config();
        // 设置最大重试次数
        config.setMaxRequestRetry(3);
        AsyncHttpClient asyncHttpClient = Dsl.asyncHttpClient(config);
        ListenableFuture<Response> listenableFuture = asyncHttpClient.prepareGet(requestUrl).execute();
        Response response = listenableFuture.get();
        System.err.println("response from server：" + response.getResponseBody());
        System.err.println("main thread execute finish......");
        close(asyncHttpClient);
    }

    @Test
    public void testPostSync() throws ExecutionException, InterruptedException, IOException {
        String requestUrl = "http://localhost:8080/students/";
        AsyncHttpClient asyncHttpClient = init();
        Student student = new Student();
        student.setId(10002);
        student.setName("test");

        ListenableFuture<Response> listenableFuture = asyncHttpClient.preparePost(requestUrl)
                .setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_UTF8_VALUE)
                .setBody(new ObjectMapper().writeValueAsString(student))
                .execute();
        System.err.println(listenableFuture.get().getResponseBody());

        String responseBody = asyncHttpClient.prepareGet(requestUrl).execute().get().getResponseBody();
        System.err.println("response from server：" + responseBody);
        close(asyncHttpClient);
    }

    @Test
    public void testGetASync() throws InterruptedException, IOException {
        String requestUrl = "http://localhost:8080/hello?name=asynchttpclient";
        AsyncHttpClient asyncHttpClient = init();
        ListenableFuture<Response> listenableFuture = asyncHttpClient.prepareGet(requestUrl).execute();
        CompletableFuture<Response> completableFuture = listenableFuture.toCompletableFuture();
        completableFuture.whenComplete((response, ex) ->
                System.err.println("response from server：" + response.getResponseBody()));
        System.err.println("main thread execute finish......");
        TimeUnit.SECONDS.sleep(4);
        close(asyncHttpClient);
    }

    /**
     * 初始化AsyncHttpClient
     *
     * @return
     */
    private AsyncHttpClient init() {
        return Dsl.asyncHttpClient();
    }

    /**
     * 关闭AsyncHttpClient
     *
     * @param asyncHttpClient AsyncHttpClient对象
     * @throws IOException
     */
    private void close(AsyncHttpClient asyncHttpClient) throws IOException {
        asyncHttpClient.close();
    }
}
