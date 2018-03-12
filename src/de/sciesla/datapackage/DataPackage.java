package de.sciesla.datapackage;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import de.sciesla.annotations.Encoding;
import de.sciesla.encoding.AESEncoding;
import de.sciesla.sender.Sender;

public abstract class DataPackage {
	private String[] args;

	public DataPackage() {
	}

	public DataPackage(String... args) {
		this.args = args;
	}

	public abstract void onServer(Sender paramSender);

	public abstract void onClient(Sender paramSender);

	public String toString(AESEncoding aesEncoding) {
		GsonBuilder gson = new GsonBuilder();
		gson.registerTypeAdapter(DataPackage.class, new DatapackageSerializer());
		String json = gson.create().toJson(this, DataPackage.class);
		if (getClass().isAnnotationPresent(Encoding.class)) {
			json = aesEncoding.encrypt(json);
			json = "$" + json;
		}

		return json;
	}

	public static DataPackage fromString(String s, AESEncoding aesEncoding) {
		
		if (s.startsWith("$")) {
			s = s.substring(1);
			s = aesEncoding.decrypt(s);
		}
		GsonBuilder gson = new GsonBuilder();
		gson.registerTypeAdapter(DataPackage.class, new DatapackageDeserializer());
		return (DataPackage) gson.create().fromJson(s, DataPackage.class);
	}

	public int getLength() {
		return this.args.length;
	}

	public String getString(int i) {
		if ((i < this.args.length) && (i >= 0)) {
			return this.args[i];
		}
		return "";
	}

	public Integer getInt(int i) {
		if ((i < this.args.length) && (i >= 0)) {
			return Integer.valueOf(Integer.parseInt(this.args[i]));
		}
		return Integer.valueOf(0);
	}

	public Float getFloat(int i) {
		if ((i < this.args.length) && (i >= 0)) {
			return Float.valueOf(Float.parseFloat(this.args[i]));
		}
		return Float.valueOf(0.0F);
	}

	public Double getDouble(int i) {
		if ((i < this.args.length) && (i >= 0)) {
			return Double.valueOf(Double.parseDouble(this.args[i]));
		}
		return Double.valueOf(0.0D);
	}

	public Long getLong(int i) {
		if ((i < this.args.length) && (i >= 0)) {
			return Long.valueOf(Long.parseLong(this.args[i]));
		}
		return Long.valueOf(0L);
	}

	public Boolean getBoolean(int i) {
		if ((i < this.args.length) && (i >= 0)) {
			return Boolean.valueOf(Boolean.parseBoolean(this.args[i]));
		}
		return Boolean.valueOf(false);
	}

	public Object getObject(int i) {
		if ((i < this.args.length) && (i >= 0)) {
			return this.args[i];
		}
		return null;
	}

	public String[] getArgs() {
		return this.args;
	}

	public void setArgs(String[] args) {
		this.args = args;
	}
}
