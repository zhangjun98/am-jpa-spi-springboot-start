package com.aming.orm.spi.json;

import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson.JSON;
import com.aming.orm.spi.log.ALoger;
import com.aming.orm.spi.log.ALogerFactory;
import com.aming.orm.spi.utils.AClassUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JavaType;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.google.gson.Gson;


import java.lang.reflect.Type;


public class AJsonProvider {


    private static ALoger logger = ALogerFactory.getLoger(AJsonProvider.class);

    public enum JsonUtilSupport {FASTJSON, GSON, JACKSON, HUTOOLS}

    ;


    private static JsonUtilSupport jsonUtil;

    static {
        if (AClassUtil.isPresent("com.alibaba.fastjson.JSON", FastJson.class.getClassLoader())) {
            AJsonProvider.setJsonUtilType(JsonUtilSupport.FASTJSON);
        } else if (AClassUtil.isPresent("com.fasterxml.jackson.databind.ObjectMapper", JacksonJson.class.getClassLoader())) {
            AJsonProvider.setJsonUtilType(JsonUtilSupport.JACKSON);
        } else if (AClassUtil.isPresent("cn.hutool.json.JSON", HutoolJson.class.getClassLoader())) {
            AJsonProvider.setJsonUtilType(JsonUtilSupport.HUTOOLS);
        } else if (AClassUtil.isPresent("com.google.gson.Gson", GsonJson.class.getClassLoader())) {
            AJsonProvider.setJsonUtilType(JsonUtilSupport.GSON);
        } else {
            logger.warn("未找到合适的json转换工具");
        }
    }


    public static void setJsonUtilType(JsonUtilSupport jsonUtilSupport) {
        jsonUtil = jsonUtilSupport;
    }



    public static AiJSON parseJson(String jsonStr) {
        switch (jsonUtil) {
            case FASTJSON:
                return FastJson.parse(jsonStr);
            case JACKSON:
                return JacksonJson.parse(jsonStr);
            case GSON:
                return GsonJson.parse(jsonStr);
            case HUTOOLS:
                return HutoolJson.parse(jsonStr);
            default:
                return null;
        }
    }


    public static AiJSON toJson(Object object) {
        switch (jsonUtil) {
            case FASTJSON:
                return FastJson.toJson(object);
            case JACKSON:
                return JacksonJson.toJson(object);
            case GSON:
                return GsonJson.toJson(object);
            case HUTOOLS:
                return HutoolJson.toJson(object);
            default:
                return null;
        }
    }


    private static class FastJson implements AiJSON {

        /**
         *
         */
        private static final long serialVersionUID = 1L;


        private String jsonString = null;
        private Object jsonTarget = null;

        private FastJson(String json) {
            this.jsonString = json;
        }

        private FastJson(Object object) {
            this.jsonTarget = object;
        }

        public static AiJSON parse(String jsonStr) {
            return new FastJson(jsonStr);
        }

        public static AiJSON toJson(Object object) {
            return new FastJson(object);
        }


        @Override
        public <T> T toBean(Type type, boolean ignoreError) {
            return JSON.parseObject(toJSONString(), type);
        }

        @Override
        public <T> T getByPath(String expression, Class<T> resultType) {
            // todo 暂时不支持
            return  null;
        }

        @Override
        public String toJSONString() {
            if (jsonString == null) {
                jsonString = JSON.toJSONString(jsonTarget);
            }
            return jsonString;
        }

    }

    private static class HutoolJson implements AiJSON {


        /**
         *
         */
        private static final long serialVersionUID = 1L;

        private String jsonString = null;
        private Object jsonTarget = null;

        private HutoolJson(String json) {
            this.jsonString = json;
        }

        private HutoolJson(Object object) {
            this.jsonTarget = object;
        }


        public static AiJSON parse(String jsonStr) {
            return new HutoolJson(jsonStr);
        }

        public static AiJSON toJson(Object object) {
            return new HutoolJson(object);
        }


        //@Override
        //public Object getByPath(String expression) {

        //	return JSONUtil.parse(toJSONString()).getByPath(expression);
        //}

        @Override
        public <T> T getByPath(String expression, Class<T> resultType) {

            return JSONUtil.parse(toJSONString()).getByPath(expression, resultType);
        }

        @Override
        public <T> T toBean(Type type, boolean ignoreError) {
            return JSONUtil.parse(toJSONString()).toBean(type, ignoreError);
        }

        @Override
        public String toJSONString() {
            if (jsonString == null) {
                jsonString = JSONUtil.toJsonStr(jsonTarget);
            }
            return jsonString;
        }


    }

    private static class GsonJson implements AiJSON {

        /**
         *
         */
        private static final long serialVersionUID = 1L;


        private String jsonString = null;
        private Object jsonTarget = null;

        private GsonJson(String json) {
            this.jsonString = json;
        }

        private GsonJson(Object object) {
            this.jsonTarget = object;
        }

        public static AiJSON parse(String jsonStr) {
            return new GsonJson(jsonStr);
        }

        public static AiJSON toJson(Object object) {
            return new GsonJson(object);
        }

		/*
		@Override
		public Object getByPath(String expression) {
			
			Gson gson = new Gson();
			gson.fromJson(json, classOfT);
			gson.toJson(src)
			return GwBeanPath.create(expression).get(JSON.parse(toJSONString()));
		}
		*/

        @Override
        public <T> T getByPath(String expression, Class<T> resultType) {
            // todo 暂时不支持
            return  null;
        }

        @Override
        public <T> T toBean(Type type, boolean ignoreError) {
            Gson gson = new Gson();
            return gson.fromJson(toJSONString(), type);
        }

        @Override
        public String toJSONString() {
            if (jsonString == null) {
                Gson gson = new Gson();
                jsonString = gson.toJson(jsonTarget);
            }
            return jsonString;
        }

    }

    private static class JacksonJson implements AiJSON {

        /**
         *
         */
        private static final long serialVersionUID = 1L;


        private String jsonString = null;
        private Object jsonTarget = null;

        private JacksonJson(String json) {
            this.jsonString = json;
        }

        private JacksonJson(Object object) {
            this.jsonTarget = object;
        }

        public static AiJSON parse(String jsonStr) {
            return new JacksonJson(jsonStr);
        }

        public static AiJSON toJson(Object object) {
            return new JacksonJson(object);
        }

        @Override
        public <T> T getByPath(String expression, Class<T> resultType) {
            // todo 暂时不支持
            return  null;
        }

        @Override
        public <T> T toBean(Type type, boolean ignoreError) {
            ObjectMapper mapper = new ObjectMapper();
            JavaType javaType = TypeFactory.defaultInstance().constructType(type);
            try {
                return mapper.readValue(toJSONString(), javaType);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        public String toJSONString() {
            if (jsonString == null) {
                ObjectMapper mapper = new ObjectMapper();
                try {
                    jsonString = mapper.writeValueAsString(jsonTarget);
                } catch (JsonProcessingException e) {
                    e.printStackTrace();
                }
            }
            return jsonString;
        }

    }

}
