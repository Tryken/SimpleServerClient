package de.sciesla.datapackage;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import java.lang.reflect.Type;

public class DatapackageSerializer implements JsonSerializer<DataPackage> {
	
	public JsonElement serialize(DataPackage src, Type type, JsonSerializationContext context) {
		
		JsonObject result = new JsonObject();
		result.add("type", new JsonPrimitive(src.getClass().getName()));
		result.add("properties", context.serialize(src, src.getClass()));
		return result;
	}
}
