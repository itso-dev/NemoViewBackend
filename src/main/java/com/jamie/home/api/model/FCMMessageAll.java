package com.jamie.home.api.model;

import lombok.*;

@Getter
@Setter
@ToString
@Builder
public class FCMMessageAll {
    private boolean validate_only;
    private Message message;

    @Builder
    @AllArgsConstructor
    @Getter
    public static class Message {
        private Notification notification; // 모든 mobile os를 아우를수 있는 Notification
        private String topic; // 특정 topic에 알림을 보내기위해 사용 (여기선 전체 푸시)
    }

    @Builder
    @AllArgsConstructor
    @Getter
    public static class Notification {
        private String title;
        private String body;
        private String image;
    }
}
