package sos.scheduler.editor.app;

import org.jdom.Element;

public class Utils {

	// saving a default value results in removing the tag
	private static final String STR_DEFAULT = "";

	private static final String INT_DEFAULT = null;

	private static final String BOOLEAN_DEFAULT = null;

	public static String getAttributeValue(String attribute, Element element) {
		if (element != null) {
			String val = element.getAttributeValue(attribute);
			return val == null ? "" : val;
		} else
			return "";
	}

	public static int getIntValue(String attribute, Element element) {
		return getIntValue(attribute, 0, element);
	}

	public static int getIntValue(String attribute, int defaultValue,
			Element element) {
		if (element == null)
			return defaultValue;

		String val = element.getAttributeValue(attribute);
		if (val != null) {
			try {
				return new Integer(val).intValue();
			} catch (Exception e) {
				element.setAttribute(attribute, "" + defaultValue);
			}
		}
		return defaultValue;
	}

	public static boolean isAttributeValue(String attribute, Element element) {
		return getAttributeValue(attribute, element).equalsIgnoreCase("yes");
	}

	public static void setAttribute(String attribute, String value,
			String defaultValue, Element element, DomParser dom) {
		if (value == null || value.equals(defaultValue)) {
			element.removeAttribute(attribute);
			if (dom != null)
				dom.setChanged(true);
		} else if (!value.equals(element.getAttributeValue(attribute))) {
			element.setAttribute(attribute, value);
			if (dom != null)
				dom.setChanged(true);
		}
	}

	public static void setAttribute(String attribute, String value,
			Element element, DomParser dom) {
		setAttribute(attribute, value, STR_DEFAULT, element, dom);
	}

	public static void setAttribute(String attribute, String value,
			String defaultValue, Element element) {
		setAttribute(attribute, value, defaultValue, element, null);
	}

	public static void setAttribute(String attribute, String value,
			Element element) {
		setAttribute(attribute, value, STR_DEFAULT, element, null);
	}

	public static void setAttribute(String attribute, int value,
			int defaultValue, Element element, DomParser dom) {
		setAttribute(attribute, "" + value, "" + defaultValue, element, dom);
	}

	public static void setAttribute(String attribute, int value,
			Element element, DomParser dom) {
		setAttribute(attribute, "" + value, INT_DEFAULT, element, dom);
	}

	public static void setAttribute(String attribute, int value,
			int defaultValue, Element element) {
		setAttribute(attribute, "" + value, "" + defaultValue, element, null);
	}

	public static void setAttribute(String attribute, int value, Element element) {
		setAttribute(attribute, "" + value, INT_DEFAULT, element, null);
	}

	public static void setAttribute(String attribute, boolean value,
			boolean defaultValue, Element element, DomParser dom) {
		setAttribute(attribute, value ? "yes" : "no", defaultValue ? "yes"
				: "no", element, dom);
	}

	public static void setAttribute(String attribute, boolean value,
			Element element, DomParser dom) {
		setAttribute(attribute, value ? "yes" : "no", BOOLEAN_DEFAULT, element,
				dom);
	}

	public static void setAttribute(String attribute, boolean value,
			boolean defaultValue, Element element) {
		setAttribute(attribute, value ? "yes" : "no", defaultValue ? "yes"
				: "no", element, null);
	}

	public static void setAttribute(String attribute, boolean value,
			Element element) {
		setAttribute(attribute, value ? "yes" : "no", BOOLEAN_DEFAULT, element,
				null);
	}

	public static int getHours(String time, int defaultValue) {
		if (time == null || time.equals(""))
			return defaultValue;

		String[] str = time.split(":");
		if (str.length > 1)
			return new Integer(str[0]).intValue();
		else
			return defaultValue;
	}

	public static int getMinutes(String time, int defaultValue) {
		if (time == null || time.equals(""))
			return defaultValue;

		String[] str = time.split(":");
		if (str.length > 1)
			return new Integer(str[1]).intValue();
		else
			return defaultValue;
	}

	public static int getSeconds(String time, int defaultValue) {
		if (time == null || time.equals(""))
			return defaultValue;

		String[] str = time.split(":");
		if (str.length > 2)
			return new Integer(str[2]).intValue();
		else if (str.length == 1)
			return new Integer(str[0]).intValue();
		else
			return defaultValue;
	}

	public static String getTime(int hours, int minutes, int seconds,
			boolean onlySeconds) {
		if (!onlySeconds && seconds == 0) {
			return asStr(hours) + ":" + asStr(minutes);
		} else if (onlySeconds && hours == 0 && minutes == 0) {
			return asStr(seconds);
		} else if (onlySeconds && seconds == 0) {
			return asStr(hours) + ":" + asStr(minutes);
		} else
			return asStr(hours) + ":" + asStr(minutes) + ":" + asStr(seconds);

	}

	public static String asStr(int value) {
		return value < 10 ? "0" + value : "" + value;
	}

}
