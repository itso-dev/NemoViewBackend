package com.jamie.home.util;

import com.jamie.home.api.model.FILE;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

public class CodeUtils {
    public static String getCodeValue() {
        String uuid = UUID.randomUUID().toString();
        return uuid.substring(0,uuid.indexOf('-')).toUpperCase();
    }
}
