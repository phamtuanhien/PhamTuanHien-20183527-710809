package utils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.logging.Logger;

import entity.payment.CreditCard;
import entity.payment.PaymentTransaction;

/**
 * @author tuanhien
 * @version 1.0
 */
public class API {
	/**
	 * Thuoc tinh giup log thong tin ra console
	 */
	private static Logger LOGGER = Utils.getLogger(Utils.class.getName());

	/**
	 * Phuong thuc goi cac api dang GET
	 * @param url duong dan toi server can request
	 * @param token doan ma bam can cung cap de xac thuc nguoi dung
	 * @return phan hoi tu server (dang string)
	 * @throws Exception
	 */
	public static String get(String url, String token) throws Exception {
		// Phan 1: setup
		HttpURLConnection conn = setupConnection(url, token, "GET");

		// Phan 2: doc du lieu tra ve tu server
		return readResponseString(conn);
	}
	/**
	 * Phuong thuc giup goi cac api dang POST (thanh toan, ...)
	 * @param url duong dan toi server can request
	 * @param data du lieu dua len server de xu ly (dang JSON)
	 * @param token doan ma bam can cung cap de xac thuc nguoi dung
	 * @return phan hoi tu server (dang string)
	 * @throws IOException
	 */
	public static String post(String url, String data, String token) throws IOException {
		allowMethods("PATCH");

		// Phan 1: setup
		HttpURLConnection conn = setupConnection(url, token, "PATCH");

		// Phan 2: gui du lieu
		Writer writer = new BufferedWriter(new OutputStreamWriter(conn.getOutputStream()));
		writer.write(data);
		writer.close();

		// Phan 3: doc du lieu gui ve tu server
		return readResponseString(conn);
	}

	private static HttpURLConnection setupConnection(String url, String token, String method) throws IOException {
		LOGGER.info("Request URL: " + url + "\n");
		URL line_api_url = new URL(url);
		HttpURLConnection conn = (HttpURLConnection) line_api_url.openConnection();
		conn.setDoInput(true);
		conn.setDoOutput(true);
		conn.setRequestMethod(method);
		conn.setRequestProperty("Content-Type", "application/json");
		conn.setRequestProperty("Authorization", "Bearer " + token);
		return conn;
	}

	private static String readResponseString(HttpURLConnection conn) throws IOException {
		BufferedReader in;
		String inputLine;
		if (conn.getResponseCode() / 100 == 2) {
			in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
		} else {
			in = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
		}
		StringBuilder response = new StringBuilder(); // su dung String Builder cho viec toi uu ve mat bo nho
		while ((inputLine = in.readLine()) != null)
			response.append(inputLine);
		in.close();
		LOGGER.info("Response Info: " + response);
		return response.toString();
	}

	/**
	 * Phuong thuc cho phep goi cac loai giao thuc API khac nhau nhu PATCH, PUT,... (chi hoat dong voi Java 11)
	 * @deprecated chi hoat dong voi Java <= 11
	 * @param methods giao thuc can cho cho phep (PATCH, PUT, ...)
	 */
	private static void allowMethods(String... methods) {
		try {
			Field methodsField = HttpURLConnection.class.getDeclaredField("methods");
			methodsField.setAccessible(true);

			Field modifiersField = Field.class.getDeclaredField("modifiers");
			modifiersField.setAccessible(true);
			modifiersField.setInt(methodsField, methodsField.getModifiers() & ~Modifier.FINAL);

			String[] oldMethods = (String[]) methodsField.get(null);
			Set<String> methodsSet = new LinkedHashSet<>(Arrays.asList(oldMethods));
			methodsSet.addAll(Arrays.asList(methods));
			String[] newMethods = methodsSet.toArray(new String[0]);

			methodsField.set(null/* static field */, newMethods);
		} catch (NoSuchFieldException | IllegalAccessException e) {
			throw new IllegalStateException(e);
		}
	}

}
