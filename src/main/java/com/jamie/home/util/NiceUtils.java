package com.jamie.home.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jamie.home.api.model.SEARCH;
import com.jamie.home.api.model.auth.Data;
import com.jamie.home.api.model.auth.Key;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import javax.crypto.*;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Date;
import java.util.UUID;

public class NiceUtils {
    private static final String access_token = "d4b29387-832b-4367-9503-c86f78868028";
    private static final String client_id = "b11648e2-e119-4c9c-985b-41bfd920a35b";
    private static final String client_secret = "d4eb778dd26b41d66f3b02ce7a76d9c9";
    private static final Logger logger = LoggerFactory.getLogger(NiceUtils.class);

    public static ResponseEntity<String> makeToken() throws Exception {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type","application/x-www-form-urlencoded");
        long current_timestamp = new Date().getTime()/1000;
        String encodedString = client_id+":"+client_secret;
        String Authorization = "Basic " +  Base64.getUrlEncoder().encodeToString(encodedString.getBytes());
        headers.add("Authorization",Authorization);
        headers.add("grant_type","client_credentials");
        headers.add("scope","default");

        // 파라미터 생성
        String requestJson = "grant_type=client_credentials&scope=default";
        HttpEntity<String> entity = new HttpEntity<String>(requestJson, headers);

        // 요청
        RestTemplate rt = new RestTemplate();
        String uri = "https://svc.niceapi.co.kr:22001/digital/niceid/oauth/oauth/token";
        ResponseEntity<String> response = rt.exchange(
                uri, //{요청할 서버 주소}
                HttpMethod.POST, //{요청할 방식}
                entity, // {요청할 때 보낼 데이터}
                String.class //{요청시 반환되는 데이터 타입}
        );

        return response;
    }

    public static Key requestEncryptionToken(SEARCH search) throws Exception {
        String req_dtim = new SimpleDateFormat("yyyyMMddhhmmss").format(new Timestamp(System.currentTimeMillis()));
        UUID uuid = UUID.randomUUID();
        String req_no = uuid.toString().substring(6);
        // 헤더 생성
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type","application/json");
        long current_timestamp = new Date().getTime()/1000;
        String encodedString = access_token+":"+current_timestamp+":"+client_id;
        String Authorization = "bearer " +  Base64.getUrlEncoder().encodeToString(encodedString.getBytes());
        headers.add("Authorization",Authorization);
        headers.add("client_id",client_id);
        headers.add("ProductID","2101979031");

        // 파라미터 생성
        String requestJson = "{\n" +
                "      \"dataHeader\":{\"CNTY_CD\":\"ko\"},\n" +
                "      \"dataBody\":{\"req_dtim\": \""+req_dtim+"\",\n" +
                "                       \"req_no\":\""+req_no+"\",\n" +
                "                       \"enc_mode\":\"1\"}\n" +
                "}";
        //logger.info("Request JSON ="+requestJson); // Construct your JSON
        HttpEntity<String> entity = new HttpEntity<String>(requestJson, headers);

        // 요청
        RestTemplate rt = new RestTemplate();
        String uri = "https://svc.niceapi.co.kr:22001/digital/niceid/api/v1.0/common/crypto/token";
        ResponseEntity<String> response = rt.exchange(
                uri, //{요청할 서버 주소}
                HttpMethod.POST, //{요청할 방식}
                entity, // {요청할 때 보낼 데이터}
                String.class //{요청시 반환되는 데이터 타입}
        );

        // 대칭키 생성
        ObjectMapper mapper = new ObjectMapper();
        Data resultData = mapper.readValue(response.getBody(), Data.class);
        String value = req_dtim.trim()+req_no.trim()+resultData.getDataBody().getToken_val().trim();
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        md.update(value.getBytes());
        byte[] arrHashValue = md.digest();
        String resultVal = new String(Base64.getEncoder().encode(arrHashValue));
        String key = resultVal.substring(0,16);
        String iv = resultVal.substring(resultVal.length()-16,resultVal.length());
        String hmac_key = resultVal.substring(0,32);

        // 요청데이터 암호화
        String reqData = "{\"requestno\":\""+req_no+"\""+
                ",\"returnurl\":\""+search.getReturnurl()+"\"" +
                ",\"sitecode\":\""+resultData.getDataBody().getSite_code()+"\"}";

        //logger.info("reqData JSON ="+reqData); // Construct your JSON

        SecretKey secureKey = new SecretKeySpec(key.getBytes(), "AES");
        Cipher c = Cipher.getInstance("AES/CBC/PKCS5Padding");
        c.init(Cipher.ENCRYPT_MODE, secureKey, new IvParameterSpec(iv.getBytes()));
        byte[] encrypted = c.doFinal(reqData.trim().getBytes());
        String enc_data = new String(Base64.getEncoder().encode(encrypted));

        byte[] hmacSha256 = hmac256(hmac_key.getBytes(), enc_data.getBytes());
        String integrity_value = Base64.getEncoder().encodeToString(hmacSha256);

        Key result = new Key(key, iv, hmac_key, req_dtim, req_no, enc_data, integrity_value, resultData.getDataBody());

        return result;
    }

    private static byte[] hmac256(byte[] secretKey,byte[] message){
        byte[] hmac256 = null;
        try{
            Mac mac = Mac.getInstance("HmacSHA256");
            SecretKeySpec sks = new SecretKeySpec(secretKey, "HmacSHA256");
            mac.init(sks);
            hmac256 = mac.doFinal(message);
            return hmac256;
        } catch(Exception e){
            throw new RuntimeException("Failed to generate HMACSHA256 encrypt");
        }
    }

    public static String decodeResult(SEARCH search) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidAlgorithmParameterException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException, UnsupportedEncodingException {
        String encData = search.getEnc_data();
        String key = search.getAuth_key(); //요청 시 암호화한 key와 동일
        String iv = search.getAuth_iv(); //요청 시 암호화한 iv와 동일

        // 복호화
        SecretKey secureKey = new SecretKeySpec(key.trim().getBytes(), "AES");
        Cipher c = Cipher.getInstance("AES/CBC/PKCS5Padding");
        c.init(Cipher.DECRYPT_MODE, secureKey, new IvParameterSpec(iv.trim().getBytes()));
        byte[] cipherEnc = Base64.getDecoder().decode(encData);
        String resData = new String(c.doFinal(cipherEnc), "euc-kr");

        return resData;
    }
}
