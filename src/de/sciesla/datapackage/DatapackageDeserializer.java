package de.sciesla.datapackage;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import java.lang.reflect.Type;

public class DatapackageDeserializer implements JsonDeserializer<DataPackage> {
	
	public DataPackage deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
		
		JsonObject jsonObject = json.getAsJsonObject();
		String type = jsonObject.get("type").getAsString();
		JsonElement element = jsonObject.get("properties");
		
		try {
			
			return (DataPackage) context.deserialize(element, Class.forName(type));
		} catch (ClassNotFoundException cnfe) {
			
			throw new JsonParseException("Unknown element type: " + type, cnfe);
		}
	}
}
