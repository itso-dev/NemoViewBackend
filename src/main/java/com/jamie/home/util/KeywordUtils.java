package com.jamie.home.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jamie.home.api.model.KEYWORD;
import com.jamie.home.api.model.Keywords;
import com.jamie.home.api.model.MEMBER;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

public class KeywordUtils {
    public static final String commonType = "1";        // 공통
    public static final String mandatoryType = "2";     // 필수
    public static final String inputType = "3";         // 직접입력

    private static final ObjectMapper mapper = new ObjectMapper();

    public static String getKeywordsValue(List<Keywords> common, List<Keywords> mandatory, List<Keywords> input) throws JsonProcessingException {
        List<Keywords> list = new ArrayList<>();
        if(common != null && common.size() != 0){
            list.addAll(common);
        }
        if(mandatory != null && mandatory.size() != 0){
            list.addAll(mandatory);
        }
        if(input != null && input.size() != 0){
            list.addAll(input);
        }

        return mapper.writeValueAsString(list);
    }

    public static List<Keywords> getCommonKeyword(MEMBER member) {
        List<Keywords> list = new ArrayList<>();
        // 1. 성별
        list.add(new Keywords(commonType, member.getGender()));
        // 2. 연령대 (회원 가입 과정에서 생년월일 입력 시 자동생성)
        list.add(new Keywords(commonType, getAges(member.getBirthday())));
        // 3. 직업
        list.add(new Keywords(commonType, member.getJob()));
        // 4. 한 달 지출 정도
        list.add(new Keywords(commonType, member.getMoney()));
        // 5. 거주 지역 ( “시/도” 분류)
        list.add(new Keywords(commonType, member.getLocation()));
        return list;
    }

    private static String getAges(String birthday){
        String result = "10세 이하";
        String year = birthday.substring(0,4);
        Calendar current = Calendar.getInstance();
        int currentYear  = current.get(Calendar.YEAR);
        int age = currentYear - Integer.parseInt(year);

        if(age >= 60){
            result = "60대 이상";
        } else if(age >= 10){
            if((age%10) >= 5){
                result = (age/10)*10 + "대 중후반";
            } else {
                result = (age/10)*10 + "대 초중반";
            }
        }
/*
        // 생일 안 지난 경우 -1
        String month = birthday.substring(4,6);
        String day = birthday.substring(6,8);
        int currentMonth = current.get(Calendar.MONTH) + 1;
        int currentDay   = current.get(Calendar.DAY_OF_MONTH);
        if (Integer.parseInt(month) * 100 + Integer.parseInt(day) > currentMonth * 100 + currentDay) age--;
*/
        return  result;
    }

    public static List<Keywords> getKeywordInTableType(String type, String keywords) throws JsonProcessingException {
        List<Keywords> result = new ArrayList<>();
        List<Keywords> keywordsList = new ArrayList<>();
        if(keywords != null){
            keywordsList = Arrays.asList(mapper.readValue(keywords, Keywords[].class));
        }
        for(Keywords k : keywordsList){
            if(type.equals(k.getType())){
                result.add(k);
            }
        }
        return result;
    }

    public static List<Keywords> getMandatoryKeyword(String keywordList) throws JsonProcessingException {
        List<KEYWORD> list = null;
        if(keywordList != null){
            list = Arrays.asList(mapper.readValue(keywordList, KEYWORD[].class));
        }
        List<Keywords> result = new ArrayList<>();

        if(list != null){
            for(KEYWORD k : list){
                result.add(new Keywords(mandatoryType, k.getName()));
            }
        }
        return result;
    }

    public static List<Keywords> getInputKeyword(String keywordInputList) throws JsonProcessingException {
        List<String> list = null;
        if(keywordInputList != null){
            list = Arrays.asList(mapper.readValue(keywordInputList, String[].class));
        }
        List<Keywords> result = new ArrayList<>();

        if(list != null){
            for(String k : list){
                result.add(new Keywords(inputType, k));
            }
        }
        return result;
    }

    public static List<String> getKeywordListFromSearch(String keywords) throws JsonProcessingException {
        List<String> result = Arrays.asList(mapper.readValue(keywords, String[].class));
        if(result.size() == 0){
            return null;
        }
        return result;
    }
}
