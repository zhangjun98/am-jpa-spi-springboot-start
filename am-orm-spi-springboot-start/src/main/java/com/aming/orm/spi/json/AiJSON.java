package com.aming.orm.spi.json;



import java.io.Serializable;
import java.lang.reflect.Type;

public interface AiJSON extends Cloneable, Serializable {

	<T> T getByPath(String expression, Class<T> resultType);

	String toJSONString();

	<T> T toBean(Type type, boolean ignoreError);
}
