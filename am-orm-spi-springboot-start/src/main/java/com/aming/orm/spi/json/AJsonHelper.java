package com.aming.orm.spi.json;


public class AJsonHelper {


    public static AiJSON parseJson(String json) {
        Object o = AJsonProvider.parseJson(json);
        return (AiJSON) o;
    }


    public static AiJSON toJson(Object object) {
        Object o = AJsonProvider.toJson(object);
        return (AiJSON) o;
    }


}
