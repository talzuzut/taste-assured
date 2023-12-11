package com.rating.exception;

import java.util.Locale;
import java.util.ResourceBundle;

public class ErrorMessageProvider {
	private static final String BASE_NAME = "error-messages"; // The base name of your resource bundle

	public static String getErrorMessage(String key) {
		ResourceBundle bundle = ResourceBundle.getBundle(BASE_NAME, Locale.ENGLISH);
		return bundle.getString(key);
	}
}

