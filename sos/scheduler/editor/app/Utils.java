package sos.scheduler.editor.app;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.jdom.Element;

public class Utils {

	// saving a default value results in removing the tag
	private static final String STR_DEFAULT = "";

	private static final String INT_DEFAULT = null;

	private static final String BOOLEAN_DEFAULT = null;

	 
	public static String getIntegerAsString(int i) {
		String s;
		if (i == -999) {
			 s = "";
		}else {
			s = String.valueOf(i);
		}
		return s;
		}
	
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
		value = value.trim();
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
		try {
		if (time == null || time.equals(""))
			return defaultValue;

		String[] str = time.split(":");
		if (str.length > 2)
			return new Integer(str[2]).intValue();
		else if (str.length == 1)
			return new Integer(str[0]).intValue();
		else
			return defaultValue;
		}catch (NumberFormatException e) {
			return defaultValue;
		}
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
	
	public static String getTime(int maxHour,String hours, String minutes, String seconds,
			boolean onlySeconds) {
		int h = Utils.str2int(hours,maxHour);
	  int m = 0;
	  int s = 0;
		if (h!=24) {
			 m = Utils.str2int(minutes,59);
			 s = Utils.str2int(seconds,59);
	  }
		if (h<0 && m<0 && s<0) {
     return "";
  		}else {
  		if (h<0) h=0;
  		if (m<0) m=0;
  		if (s<0) s=0;
		  return getTime(h,m,s,onlySeconds);
		}
	}

	public static String getTime(String hours, String minutes, String seconds,
			boolean onlySeconds) {
	  return getTime(23,hours,minutes,seconds,onlySeconds);

	}

	public static String asStr(int value) {
		return value < 10 ? "0" + value : "" + value;
	}
	
	public static String fill(int l,String s) {
		String n="00000000000000000000000000000";
		if ((s.length() < l) && (!s.trim().equals(""))) {
			s = n.substring(0, l-s.length())+ s;
		}
		return s;
	}

	public static String onlyDigits(String s) {
		String erg="";
		int i=0;
		for (i=0;i < s.length();i++) {
			try {
			  Integer.parseInt(String.valueOf(s.charAt(i)));
			  erg = erg + s.charAt(i);
  	  }catch (Exception ee) {}		
		}
	
		return erg;
	}
	
	public static boolean isOnlyDigits(String s) {
		try {
			if (s.equals("")) {
				return true;
			}
			if (s.equals("-")) return true;
			Integer.parseInt(s);
			return true;
		}catch (Exception ee){
			return false;
		}

	}
	
	public static  int str2int(String s) {
		int i = 0;
		try {
			  i = Integer.parseInt(s);
	  	}catch (Exception e) {
		  	s = onlyDigits(s);
				try {
  			  i = Integer.parseInt(s);
		 	  }catch (Exception ee) {
		  		  i =-999;
	 	   }
		  }
		return i;
	}
 
	public static int str2int(String s,int maxValue) {
		int i = 0;
		try {
			  i = Integer.parseInt(s);
	  	}catch (Exception e) {
		  	s = onlyDigits(s);
				try {
  			  i = Integer.parseInt(s);
		  	  }catch (Exception ee) {
		  		  i=-999;
		  	   }
		  }
	  	
	  if (i > maxValue) 
	  	i = maxValue;
		
	 return i;
	}
		
	public static int str2int(int default_value, String s) {
		int i = 0;
		try {
			  i = Integer.parseInt(s);
	  	}catch (Exception e) {
		  	s = onlyDigits(s);
				try {
  			  i = Integer.parseInt(s);
		  	  }catch (Exception ee) {
		  		  i=default_value;
		  	   }
		  }
	 return i;
	}
		
	public static int message(Shell shell, String message, int style) {
		MessageBox mb = new MessageBox(shell, style);
		mb.setMessage(message);
		return mb.open();
	}
	
	public static void setBackground(int min,int max, Text t) {
		if ((str2int(t.getText()) > max || str2int(t.getText()) < min ) && str2int(t.getText()) != -999) {
			t.setBackground(Display.getCurrent().getSystemColor(SWT.COLOR_YELLOW));
    }else {
			t.setBackground(null);
	  }

	}
	public static void setBackground(int min,int max, Combo c) {
		if ((str2int(c.getText()) > max || str2int(c.getText()) < min) && str2int(c.getText()) != -999) {
			c.setBackground(Display.getCurrent().getSystemColor(SWT.COLOR_YELLOW));
    }else {
			c.setBackground(null);
	  }

	}
}
