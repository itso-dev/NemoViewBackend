package com.jamie.home.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.database.annotations.NotNull;
import com.jamie.home.api.controller.AdminController;
import com.jamie.home.api.model.FCMMessage;
import com.jamie.home.api.model.FCMMessageAll;
import okhttp3.*;
import org.apache.http.HttpHeaders;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;
import java.util.List;

public class FirebaseUtils {
    private static final Logger logger = LoggerFactory.getLogger(FirebaseUtils.class);
    public static final String FCM_TOPIC = "nemoview";
    public static final String API_URL = "https://fcm.googleapis.com/v1/projects/fcm-push-1cab1/messages:send";
    public static final ObjectMapper objectMapper = new ObjectMapper();

    public static void sendMessageTo(String targetToken, String title, String body) throws IOException {
        String message = "";
        if(targetToken == null){ // 전체 푸시
            message = makeMessageTopic(title, body);
        } else {
            message = makeMessageTarget(targetToken, title, body);
        }

        OkHttpClient client = new OkHttpClient();
        RequestBody requestBody = RequestBody.create(MediaType.get("application/json; charset=utf-8"), message);
        Request request = new Request.Builder()
                .url(API_URL)
                .post(requestBody)
                .addHeader(HttpHeaders.AUTHORIZATION, "Bearer " + getAccessToken())
                .addHeader(HttpHeaders.CONTENT_TYPE, "application/json; UTF-8")
                .build();

        //Response response = client.newCall(request).execute();
        //비동기 처리 (enqueue 사용)
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                logger.error(e.getLocalizedMessage());
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
            }
        });

        //log.info(response.body().string());
    }

    // 파라미터를 FCM이 요구하는 body 형태로 만들어준다.
    private static String makeMessageTarget(String targetToken, String title, String body) throws JsonProcessingException {
        FCMMessage fcmMessage = FCMMessage.builder()
                .message(FCMMessage.Message.builder()
                        .token(targetToken)
                        .notification(FCMMessage.Notification.builder()
                                .title(title)
                                .body(body)
                                .image(null)
                                .build()
                        )
                        .build()
                )
                .validate_only(false)
                .build();
        return objectMapper.writeValueAsString(fcmMessage);
    }

    private static String makeMessageTopic(String title, String body) throws JsonProcessingException {
        FCMMessageAll fcmMessage = FCMMessageAll.builder()
                .message(FCMMessageAll.Message.builder()
                        .topic(FCM_TOPIC)
                        .notification(FCMMessageAll.Notification.builder()
                                .title(title)
                                .body(body)
                                .image(null)
                                .build()
                        )
                        .build()
                )
                .validate_only(false)
                .build();
        return objectMapper.writeValueAsString(fcmMessage);
    }

    private static String getAccessToken() throws IOException {
        String firebaseConfigPath = "firebase_service_key.json";
        GoogleCredentials googleCredentials = GoogleCredentials
                .fromStream(new ClassPathResource(firebaseConfigPath).getInputStream())
                .createScoped(List.of("https://www.googleapis.com/auth/cloud-platform"));
        googleCredentials.refreshIfExpired();
        return googleCredentials.getAccessToken().getTokenValue();
    }
}
