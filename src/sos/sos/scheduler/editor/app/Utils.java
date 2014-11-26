package sos.scheduler.editor.app;

import java.io.StringWriter;
import java.util.List;
import org.eclipse.swt.SWT;
import org.eclipse.swt.dnd.Clipboard;
import org.eclipse.swt.dnd.TextTransfer;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Cursor;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.Text;
import org.jdom.Element;
import org.jdom.Namespace;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;
import org.jdom.xpath.XPath;
import org.eclipse.swt.widgets.Shell;
import sos.scheduler.editor.conf.SchedulerDom;
import sos.scheduler.editor.conf.forms.SchedulerEditorFontDialog;

import java.util.regex.Pattern;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.events.*;

public class Utils {

	// saving a default value results in removing the tag
	private static final String       STR_DEFAULT     = "";

	private static final String       INT_DEFAULT     = null;

	private static final String       BOOLEAN_DEFAULT = null;

	private static       Clipboard    _cb             = null;

	//all root elements for undo 
	//private static       List         undo            = null;

	//private static       int          UNDO_SIZE       = 10;

	private static       Element      resetElement    = null;


	public static String getIntegerAsString(int i) {
		String s;
		if (i == -999) {
			s = "";
		} else {
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


	public static int getIntValue(String attribute, int defaultValue, Element element) {
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


	public static boolean getBooleanValue(String attribute, Element element) {
		return getAttributeValue(attribute, element).equalsIgnoreCase("true");
	}


	public static void setAttribute(String attribute, String value, String defaultValue, Element element, DomParser dom) {
		value = value.trim();

		//value = escape(value);

		//System.out.println("attribute[" + attribute + " = " + value + "]  default: " + defaultValue);

		if (value == null || value.equals(defaultValue) ) {
			//if(element.getName().equals("param") && attribute.equals("value")) {
			element.removeAttribute(attribute);
			if (dom != null)
				dom.setChanged(true);
			//}
		} else if (!value.equals(element.getAttributeValue(attribute))) {
			element.setAttribute(attribute, value);
			if (dom != null) {
				dom.setChanged(true);
				if(dom instanceof SchedulerDom) {
					if(element.getName().equals("order")) {
						((SchedulerDom)dom).setChangedForDirectory("order", Utils.getAttributeValue("job_chain",element) + "," + Utils.getAttributeValue("id",element), SchedulerDom.MODIFY);
					} else {
						((SchedulerDom)dom).setChangedForDirectory("job", Utils.getAttributeValue("name",element), SchedulerDom.MODIFY);
					}
				}
			}
		}
	}


	public static void setAttribute(String attribute, String value, Element element, DomParser dom) {
		setAttribute(attribute, value, STR_DEFAULT, element, dom);
	}


	public static void setAttribute(String attribute, String value, String defaultValue, Element element) {
		setAttribute(attribute, value, defaultValue, element, null);
	}


	public static void setAttribute(String attribute, String value, Element element) {
		setAttribute(attribute, value, STR_DEFAULT, element, null);
	}


	public static void setAttribute(String attribute, int value, int defaultValue, Element element, DomParser dom) {
		setAttribute(attribute, "" + value, "" + defaultValue, element, dom);
	}


	public static void setAttribute(String attribute, int value, Element element, DomParser dom) {
		setAttribute(attribute, "" + value, INT_DEFAULT, element, dom);
	}


	public static void setAttribute(String attribute, int value, int defaultValue, Element element) {
		setAttribute(attribute, "" + value, "" + defaultValue, element, null);
	}


	public static void setAttribute(String attribute, int value, Element element) {
		setAttribute(attribute, "" + value, INT_DEFAULT, element, null);
	}


	public static void setAttribute(String attribute, boolean value, boolean defaultValue, Element element,
			DomParser dom) {
		setAttribute(attribute, value ? "yes" : "no", defaultValue ? "yes" : "no", element, dom);
	}


	public static void setAttribute(String attribute, boolean value, Element element, DomParser dom) {
		setAttribute(attribute, value ? "yes" : "no", BOOLEAN_DEFAULT, element, dom);
	}


	public static void setAttribute(String attribute, boolean value, boolean defaultValue, Element element) {
		setAttribute(attribute, value ? "yes" : "no", defaultValue ? "yes" : "no", element, null);
	}


	public static void setAttribute(String attribute, boolean value, Element element) {
		setAttribute(attribute, value ? "yes" : "no", BOOLEAN_DEFAULT, element, null);
	}


	public static void setBoolean(String attribute, boolean value, Element element) {
		setAttribute(attribute, value ? "true" : "false", element, null);
	}


	public static void setBoolean(String attribute, boolean value, Element element, DomParser dom) {
		setAttribute(attribute, value ? "true" : "false", element, dom);
	}


	public static String getElement(String name, Element parent, Namespace ns) {
		if (parent != null) {
			Element element = parent.getChild(name, ns);
			if (element != null)
				return element.getTextTrim();
			else
				return "";
		} else
			return "";
	}


	public static void setElement(String name, String value, boolean optional, Element parent, Namespace ns,
			DomParser dom) {
		if (parent != null) {
			Element element = parent.getChild(name, ns);
			if (element == null && ((value != null && value.length() > 0) || !optional)) {
				element = new Element(name, ns).setText(value);
				parent.addContent(element);
				if (dom != null)
					dom.setChanged(true);
			} else if (element != null && (value == null || value.length() == 0) && optional) {
				element.detach();
				if (dom != null)
					dom.setChanged(true);
			} else if (element.getTextTrim() != null && !element.getTextTrim().equals(value)) {
				element.setText(value);
				if (dom != null)
					dom.setChanged(true);
			}
		}
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
		} catch (NumberFormatException e) {
			return defaultValue;
		}
	}


	public static String getTime(int hours, int minutes, int seconds, boolean onlySeconds) {
		if (!onlySeconds && seconds == 0) {
			return asStr(hours) + ":" + asStr(minutes);
		} else if (onlySeconds && hours == 0 && minutes == 0) {
			return asStr(seconds);
		} else if (onlySeconds && seconds == 0) {
			return asStr(hours) + ":" + asStr(minutes);
		} else
			return asStr(hours) + ":" + asStr(minutes) + ":" + asStr(seconds);

	}


	public static String getTime(int maxHour, String hours, String minutes, String seconds, boolean onlySeconds) {
		int h = Utils.str2int(hours, maxHour);
		int m = 0;
		int s = 0;
		if (h != 24) {
			m = Utils.str2int(minutes, 59);
			if ( h > 0 || m > 0) {
				s = Utils.str2int(seconds,59);
			}else {
				s = Utils.str2int(seconds);
			}
		}
		if (h < 0 && m < 0 && s < 0) {
			return "";
		} else {
			if (h < 0)
				h = 0;
			if (m < 0)
				m = 0;
			if (s < 0)
				s = 0;
			return getTime(h, m, s, onlySeconds);
		}
	}


	public static String getTime(String hours, String minutes, String seconds, boolean onlySeconds) {
		return getTime(23, hours, minutes, seconds, onlySeconds);

	}


	public static String asStr(int value) {
		return value < 10 ? "0" + value : "" + value;
	}


	public static String fill(int l, String s) {
		String n = "00000000000000000000000000000";
		//if ((s.length() < l) && (!s.trim().equals(""))) {
		if (s.length() < l) {
			s = n.substring(0, l - s.length()) + s;
		}
		return s;
	}


	public static String onlyDigits(String s) {
		String erg = "";
		int i = 0;
		for (i = 0; i < s.length(); i++) {
			try {
				Integer.parseInt(String.valueOf(s.charAt(i)));
				erg = erg + s.charAt(i);
			} catch (Exception ee) {
			}
		}

		return erg;
	}


	public static boolean isOnlyDigits(String s) {
		try {
			if (s.equals("")) {
				return true;
			}
			if (s.equals("-"))
				return true;
			Integer.parseInt(s);
			return true;
		} catch (Exception ee) {
			return false;
		}

	}


	public static int str2int(String s) {
		int i = 0;
		try {
			i = Integer.parseInt(s);
		} catch (Exception e) {
			s = onlyDigits(s);
			try {
				i = Integer.parseInt(s);
			} catch (Exception ee) {
				i = -999;
			}
		}
		return i;
	}


	public static int str2int(String s, int maxValue) {
		int i = 0;
		try {
			i = Integer.parseInt(s);
		} catch (Exception e) {
			s = onlyDigits(s);
			try {
				i = Integer.parseInt(s);
			} catch (Exception ee) {
				i = -999;
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
		} catch (Exception e) {
			s = onlyDigits(s);
			try {
				i = Integer.parseInt(s);
			} catch (Exception ee) {
				i = default_value;
			}
		}
		return i;
	}


	public static void setBackground(int min, int max, Text t) {
		if ((str2int(t.getText()) > max || str2int(t.getText()) < min) && str2int(t.getText()) != -999) {
			t.setBackground(Options.getRequiredColor());
		} else {
			t.setBackground(null);
		}

	}


	public static void setBackground(int min, int max, Combo c) {
		if ((str2int(c.getText()) > max || str2int(c.getText()) < min) && str2int(c.getText()) != -999) {
			c.setBackground(Options.getRequiredColor());
		} else {
			c.setBackground(null);
		}

	}


	public static void setBackground(Text t, boolean colorIt) {
		if (t.getText().length() == 0 && colorIt)
			t.setBackground(Options.getRequiredColor());
		else
			t.setBackground(null);
	}


	public static void setBackground(Table t, boolean colorIt) {
		if (t.getItemCount() == 0 && colorIt)
			t.setBackground(Options.getRequiredColor());
		else
			t.setBackground(null);
	}


	public static void setBackground(Control c, boolean toColor) {
		if (toColor)
			c.setBackground(Options.getRequiredColor());
		else
			c.setBackground(null);
	}


	public static String getFileFromURL(String url) {
		/*String separator = System.getProperty("file.separator");
        int index = url.lastIndexOf(separator);
        if(url != null && new java.io.File(url).getName().startsWith("#xml#.config.")) {
        	return new java.io.File(url).getParent();
        }
        if (index < 0)
            return url;
        else
            return url.substring(index + 1);
		 */
		/*if(url != null && new java.io.File(url).getName().startsWith("#xml#.config.")) {
    	return new java.io.File(url).getParent();
    }*/
		if(url != null && new java.io.File(url).isDirectory()) {
			return new java.io.File(url).getPath();
		}
		java.io.File file = new java.io.File(url);
		if (file.isFile()) {
			return file.getName();
		} else {
			return url;
		}
	}


	public static boolean applyFormChanges(Control c) {
		if (c instanceof IUnsaved) {
			// looking for unsaved changes...
			IUnsaved unsaved = (IUnsaved) c;
			if (unsaved.isUnsaved()) {
				int ok = MainWindow.message(Messages.getString("MainListener.apply_changes"), //$NON-NLS-1$
						SWT.ICON_QUESTION | SWT.YES | SWT.NO | SWT.CANCEL);
				if (ok == SWT.CANCEL)
					return false;
				if (ok == SWT.NO)
					return true;//return false;
				else if (ok == SWT.YES) {
					unsaved.apply();
					//return false;
				}
			}
		}
		return true;
	}

	public static String getElementAsString(Element job) {
		org.jdom.output.XMLOutputter output = new org.jdom.output.XMLOutputter( org.jdom.output.Format.getPrettyFormat() );
		String retVal = "";
		try {    		    		
			retVal = output.outputString(job);
		}

		catch (Exception e) {
			try {
				new ErrorLog("error in " + sos.util.SOSClassUtil.getMethodName() , e);
			} catch(Exception ee) {
				//tu nichts
			}
			System.out.println(e);
		}
		return retVal;
	}

	public static String noteAsStr(Element element) {
		if(element == null) {
			return "";
		}
		StringWriter stream = new StringWriter();
		XMLOutputter out = new XMLOutputter(Format.getPrettyFormat());
		try {
			out.output(element.getContent(), stream);
			stream.close();
		} catch (Exception e) {
			try {
				new ErrorLog("error in " + sos.util.SOSClassUtil.getMethodName() , e);
			} catch(Exception ee) {
				//tu nichts
			}
			e.printStackTrace();
		}

		String str = stream.toString().trim();
		if(str.startsWith("<div")) {
			str = str.replaceFirst("\\A<\\s*div\\s*xmlns\\s*=\\s*\"[a-zA-Z0-9/:\\.]*\"\\s*>\\s*", "");
			str = str.replaceFirst("\\s*<\\s*/\\s*div\\s*>\\Z", "");

		}
		str = str.replaceFirst("\\<\\!\\[CDATA\\[", "");						
		str = str.replaceFirst("]]>", "");
		str = str.replaceAll("<pre space=\"preserve\">","<pre>");
		return str;

	}

	public static String getElementAsString(org.jdom.Text job) {
		org.jdom.output.XMLOutputter output = new org.jdom.output.XMLOutputter( org.jdom.output.Format.getPrettyFormat() );
		String retVal = "";
		try {    		    		
			retVal = output.outputString(job);
		}

		catch (Exception e) {
			System.out.println(e);
		}
		return retVal;
	}

	public static boolean isNumeric(String str) {
		boolean retVal = true;
		char[] c = null;
		if(str==null)
			return false;

		if(str.length()==0)
			return false;

		c = str.toCharArray();
		for(int i = 0; i < str.length(); i++) {
			if(!Character.isDigit(c[i])){
				return false;	
			}

		}
		return retVal;
	}

	/*public static String showClipboard(String xml, Shell shell) {
		return showClipboard(xml, shell, false, null, false, null, true);    	
	}*/

	public static String showClipboard(String xml, Shell shell, boolean bApply, String selectStr) {
		return showClipboard(xml, shell, bApply, selectStr, false, null, false);
	}


	/**
	 * 
	 * @param xml
	 * @param shell
	 * @param bApply
	 * @param selectStr
	 * @param showFunction -> wird in Pre Function in Job Execute verwendet
	 * @param scriptLanguage -> wird in Pre Function in Job Execute verwendet
	 * @param dontShowWizzardInfo -> wird in Jobs Wizzard verwendet
	 * @return
	 */
	public static String showClipboard(String xml, 
			Shell shell, 
			boolean bApply, 
			String selectStr, 
			boolean showFunction, 
			String scriptLanguage,
			boolean showWizzardInfo) {

		/*System.out.println("test 1 XXX xml " + xml);
		System.out.println("test 2  XXX shell " + shell);
		System.out.println("test 3 XXX bApply " + bApply);
		System.out.println("test 4 XXX selectStr " + selectStr);
		System.out.println("test 5 XXX showFunction " + showFunction);
		System.out.println("test 6 XXX scriptLanguage " + scriptLanguage);
		System.out.println("test 7 XXX showWizzardInfo " + showWizzardInfo);
		 */

		//kommt jetzt aus den Options Font font = new Font(Display.getDefault(), "Courier New", 8, SWT.NORMAL);
		TextDialog dialog = new TextDialog(shell, SWT.CLOSE | SWT.TITLE | SWT.APPLICATION_MODAL
				| SWT.RESIZE, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL);
		dialog.setSize(new Point(500, 400));


		if(selectStr != null && selectStr.trim().length() > 0) {
			dialog.setContent(xml, selectStr);
		} else { 

			dialog.setContent(xml);
		}

		dialog.setClipBoard(true);
		
		SchedulerEditorFontDialog fd = new SchedulerEditorFontDialog();
	    fd.readFontData();
 	    dialog.getStyledText().setFont(new Font(null, fd.getFontData()));
	    dialog.getStyledText().setForeground(new Color(null, fd.getForeGround())); 


		dialog.getStyledText().setEditable(bApply);		
		dialog.setVisibleApplyButton(bApply);		
		dialog.setBSaveWindow(true);		
		dialog.setShowFunctions(showFunction);
		dialog.setScriptLanguage(scriptLanguage);
		dialog.setShowWizzardInfo(showWizzardInfo);
		//String s = dialog.open(true);
		String s = dialog.open(false);

		if (dialog.isClipBoardClick()) {
			copyClipboard(s, shell.getDisplay());
			return null;
		}

		if(!dialog.isApplyBoardClick()) {
			s = null;
		}

		/*if (font != null)
			font.dispose();
			*/
		return s;
	}


	public static void copyClipboard(String content, Display display) {

		if (_cb == null)
			_cb = new Clipboard(display);

		TextTransfer transfer = TextTransfer.getInstance();
		_cb.setContents(new Object[] { content }, new Transfer[] { transfer });

	}

	/**
	 * Element ist schreibgeschützt
	 * @param dom
	 * @param elem
	 * @return
	 */
	public static boolean isElementEnabled(String which, sos.scheduler.editor.conf.SchedulerDom dom, Element elem) {

		if(which.equals("job") && elem != null && !elem.getName().equals("job")) {
			elem = getJobElement(elem);
		}

		java.util.ArrayList listOfReadOnly = dom.getListOfReadOnlyFiles();

		if(which.equalsIgnoreCase("commands")) {
			if (listOfReadOnly != null && listOfReadOnly.contains(which + "_" + Utils.getAttributeValue("job_chain", elem) + "," + Utils.getAttributeValue("id", elem))) {        	

				//this.setEnabled(false);
				return false;
			}
		} else {
			if (listOfReadOnly != null && listOfReadOnly.contains(which + "_" + Utils.getAttributeValue("name", elem))) {        	

				//this.setEnabled(false);
				return false;
			}
		}
		return true;
	}

	/*
	 * liefert den Vaterknoten Job
	 */
	public static Element getJobElement(Element elem) {

		boolean loop = true;
		int counter = 0;
		while(loop) {
			if(elem != null && elem.getParentElement()!= null && !elem.getParentElement().getName().equalsIgnoreCase("spooler")) {
				if(elem.getName().equalsIgnoreCase("job")) {
					return elem;					
				} else if(elem.getParentElement().getName().equalsIgnoreCase("job")) {
					return elem.getParentElement();					
				} else {
					elem = elem.getParentElement();					
				}
				++counter;
				if(counter == 5) {
					loop = false;
				}
			} else {
				return elem;
			}
		}
		return elem;
	}


	/*
	 * liefert den Hotfolder Vaterknoten 
	 * mögliche Vaterknoten
	 * "job", "job_chain", "add_order", "order", process_class, schedule, lock
	 */
	public static Element getHotFolderParentElement(Element elem) {

		boolean loop = true;
		int counter = 0;
		while(loop) {
			if(elem != null && elem.getParentElement()!= null && !elem.getParentElement().getName().equalsIgnoreCase("spooler")) {
				if( elem.getName().equalsIgnoreCase("job") ||
						elem.getName().equalsIgnoreCase("job_chain") ||
						elem.getName().equalsIgnoreCase("add_order") ||
						elem.getName().equalsIgnoreCase("order") ||
						elem.getName().equalsIgnoreCase("process_class") || 
						elem.getName().equalsIgnoreCase("schedule") ||
						elem.getName().equalsIgnoreCase("lock")
				) {					
					return elem;					
				} else if( elem.getParentElement().getName().equalsIgnoreCase("job") ||
						elem.getParentElement().getName().equalsIgnoreCase("job_chain") ||
						elem.getParentElement().getName().equalsIgnoreCase("add_order") ||
						elem.getParentElement().getName().equalsIgnoreCase("order") ||
						elem.getParentElement().getName().equalsIgnoreCase("process_class") ||
						elem.getParentElement().getName().equalsIgnoreCase("schedule") ||
						elem.getParentElement().getName().equalsIgnoreCase("lock")
				) {			
					return elem.getParentElement();					
				} else {
					elem = elem.getParentElement();					
				}
				++counter;
				if(counter == 5) {
					loop = false;
				}
			} else {
				return elem;
			}
		}
		return elem;
	}

	/*
	 * liefert den Vaterknoten der Runtime
	 * 
	 * folgende Vaterknoten sind gesucht: job, order, schedule
	 */
	public static Element getRunTimeParentElement(Element elem) {

		boolean loop = true;
		int counter = 0;
		while(loop) {
			if(elem != null && elem.getParentElement()!= null) {
				if(elem.getName().equalsIgnoreCase("job") 
						|| elem.getName().equalsIgnoreCase("schedule")
						|| elem.getName().equalsIgnoreCase("order")|| elem.getName().equalsIgnoreCase("add_order")) {
					return elem;					
				} else if(elem.getParentElement().getName().equalsIgnoreCase("job")) {
					return elem.getParentElement();					
				} else {
					elem = elem.getParentElement();					
				}
				++counter;
				if(counter == 5) {
					loop = false;
				}
			} else {
				return elem;
			}
		}
		return elem;
	}
	/**
	 * Normalizes the given string
	 */
	public static String escape(String s) {
		if (s == null){
			return s;
		}

		int len = s.length();
		StringBuffer str = new StringBuffer(len);

		for (int i = 0; i < len; i++){
			char ch = s.charAt(i);
			switch (ch) {
			case '<':
				str.append("&lt;"); //$NON-NLS-1$
				break;

			case '>':
				str.append("&gt;"); //$NON-NLS-1$
				break;    			    		

			case '"':
				str.append("&quot;"); //$NON-NLS-1$
				break;

			case '&': 
				if( !(s.substring(i).startsWith("&quot;") ||
						s.substring(i).startsWith("&lt;") ||
						s.substring(i).startsWith("&gt;") ||
						s.substring(i).startsWith("&amp;")))
					str.append("&amp;"); //$NON-NLS-1$
				else
					str.append(ch);
				break;
			default:
				str.append(ch);
			}
		}

		return str.toString();
	}

	public static String deEscape(String s) { 
		String newValue = s;
		newValue = newValue.replaceAll("&quot;", "\"");
		newValue = newValue.replaceAll("&lt;", "<");
		newValue = newValue.replaceAll("&gt;", ">");
		newValue = newValue.replaceAll("&amp;", "&");
		return newValue;
	}


	//löscht alle Kinder der Element elem, wenn diese einen Attribut name haben  
	public static void removeChildrensWithName(Element elem, String name) {    	    	
		Element child = elem.getChild(name);
		java.util.ArrayList nl = new java.util.ArrayList();
		if(child != null) {    		
			java.util.List l = child.getChildren();    		
			for(int i = 0; i < l.size(); i++) {
				Element e = (Element)l.get(i);
				if(Utils.getAttributeValue("name", e).length() > 0){
					nl.add(e);    				    				
				}
			}
			l.removeAll(nl);
			if(l.size() == 0)
				elem.removeChildren(name);

		}
	}

	public static boolean existName(String name, Element elem, String childname) {
		if(elem.getParentElement() != null) {
			List l = elem.getParentElement().getChildren(childname);
			for (int i = 0; i < l.size(); i++) {
				Element e = (Element)l.get(i);
				if(!e.equals(elem) && Utils.getAttributeValue("name", e).equalsIgnoreCase(name)) {
					return true;
				} 

			}
		}
		return false;		
	}

	public static boolean isRegExpressions(String regex) {
		try {
			Pattern.compile(regex);
			return true;
		} catch (Exception e) {
			return false;
		}
		//Matcher matcher = pattern.matcher(filename);


	}

	/**
	 * Überprüft die Abhängigkeiten der Elementen 
	 * @param name -> Names des Element, der gelöscht bzw. geändert wurde
	 * @param _dom
	 * @param type -> Im welchen Formular wurde geändert
	 * @param which -> Wenn type nicht ausreicht:  z.B. im Job Formular (type=JOB) wird einmal beim Schliessen und einmal beim Ändern der
	 * Name des Jobs überprüft.
	 * 
	 * @return boolean true alles im grünen Bereich. 
	 */
	public static boolean checkElement(String name, SchedulerDom _dom, int type, String which ) {
		boolean onlyWarning = false;//-> true: Gibt nur eine Warnung aus. Sonst Warnung mit Yes- und No- Button um ggf. die Änderungen zurückzunehmen

		try {
			if(which == null) which ="";				
			if(type == Editor.JOB_CHAIN) {

				XPath x3 = XPath.newInstance("//order[@job_chain='"+ name + "']");				 
				List<Element> listOfElement_3 = x3.selectNodes(_dom.getDoc());
				if(!listOfElement_3.isEmpty())
					//throw new Exception ("Die Jobkette [job_chain=" + name + "] wird in einem Auftrag verwendet. " +
					//"Soll die Jobkette trotzdem umbennant werden");
					throw new Exception ("The Jobchain [job_chain=" + name + "] is currently being used by an order. " +
					"Do you want really rename the jobchain");				

				XPath x4 = XPath.newInstance("//add_order[@job_chain='"+ name + "']");				 
				List<Element> listOfElement_4 = x4.selectNodes(_dom.getDoc());
				if(!listOfElement_4.isEmpty())
					throw new Exception ("The Jobchain [job_chain=" + name + "] is currently being used by an order. " +
					"Do you want really rename the jobchain");				

			} else if(type == Editor.JOB_CHAINS) {

				XPath x3 = XPath.newInstance("//order[@job_chain='"+ name + "']");				 
				List listOfElement_3 = x3.selectNodes(_dom.getDoc());
				if(!listOfElement_3.isEmpty())
					//throw new Exception ("Die Jobkette [job_chain=" + name + "] wird in einem Auftrag verwendet. " +
					//"Soll die Jobkette trotzdem gelöscht werden");
					throw new Exception ("The Jobchain [job_chain=" + name + "] is currently being used by an order. " +
					"Do you want to delete the jobchain");

				XPath x4 = XPath.newInstance("//add_order[@job_chain='"+ name + "']");				 
				List listOfElement_4 = x4.selectNodes(_dom.getDoc());
				if(!listOfElement_4.isEmpty())
					//throw new Exception ("Die Jobkette [job_chain=" + name + "] wird in einem Auftrag verwendet. " +
					//"Soll die Jobkette trotzdem gelöscht werden");
					throw new Exception ("The Jobchain [job_chain=" + name + "] is currently being used by an order. " +
					"Do you want to delete the jobchain");

			} else if(type==Editor.JOB) {

				if(which != null && which.equalsIgnoreCase("close")) {
					onlyWarning = true;
					XPath x0 = XPath.newInstance("//job[@name='"+ name + "']");			 
					Element e = (Element)x0.selectSingleNode(_dom.getDoc());
					boolean isOrder = Utils.getAttributeValue("order", e).equalsIgnoreCase("yes");

					if(!isOrder) {
						XPath x3 = XPath.newInstance("//job_chain_node[@job='"+ name + "']");				 
						List listOfElement_3 = x3.selectNodes(_dom.getDoc());
						if(!listOfElement_3.isEmpty())
							//throw new Exception ("Der Standalone Job " + name + " ist in einer Jobkette definiert.");
							throw new Exception   ("The Standalone Job " + name + " is currently being used by a job chain");
					}
				} else {

					if(name.length() == 0)
						return true;

					//
					XPath x0 = XPath.newInstance("//job[@name='"+ name + "']");			 
					Element e = (Element)x0.selectSingleNode(_dom.getDoc());
					boolean isOrder = Utils.getAttributeValue("order", e).equalsIgnoreCase("yes");
					if( isOrder) {						

						XPath x = XPath.newInstance("//job[@name='"+ name + "']/run_time[@let_run='yes' or @once='yes' or @single_start]");			 

						List listOfElement = x.selectNodes(_dom.getDoc());
						if(!listOfElement.isEmpty())
							throw new Exception ("An order job [name=" + name+ "] may not use single_start-, start_once- and " +
							"let_run attributes in Runtime Elements. Should these attributes be deleted?");

						XPath x2 = XPath.newInstance("//job[@name='"+ name + "']/run_time//period[@let_run='yes' or @single_start]");				 
						List listOfElement_2 = x2.selectNodes(_dom.getDoc());
						if(!listOfElement_2.isEmpty())
							throw new Exception ("An order job [name=" + name+ "] may not use single_start-, start_once- and " +
							"let_run attributes in Runtime Elements. Should these attributes be deleted?");

					}


					XPath x3 = XPath.newInstance("//job_chain_node[@job='"+ name + "']");				 
					List listOfElement_3 = x3.selectNodes(_dom.getDoc());
					if(!listOfElement_3.isEmpty()) {
						if(which.equalsIgnoreCase("change_order")) {
							throw new Exception   ("The Job " + name + " is currently being used by a job chain. Do you want really change to a standalone job?");
						} else {
							throw new Exception   ("The Job " + name + " is currently being used by a job chain. Do you want really rename the name?");
						}
					}

				}

			} else if(type == Editor.JOBS) {

				XPath x3 = XPath.newInstance("//job_chain_node[@job='"+ name + "']");				 
				List listOfElement_3 = x3.selectNodes(_dom.getDoc());
				if(!listOfElement_3.isEmpty())					
					throw new Exception ("The Job [name=" + name + "] is currently being used by a job chain. Do you want to delete?");

			} else if(type == Editor.LOCKS) {

				XPath x3 = XPath.newInstance("//lock.use[@lock='"+ name + "']");				 
				List listOfElement_3 = x3.selectNodes(_dom.getDoc());
				if(!listOfElement_3.isEmpty())					
					throw new Exception ("The lock [lock=" + name + "] is currently	being used by a job. Do you want to continue?");				


			} else if(type == Editor.PROCESS_CLASSES) {

				XPath x3 = XPath.newInstance("//job[@process_class='"+ name + "']");				 
				List listOfElement_3 = x3.selectNodes(_dom.getDoc());
				if(!listOfElement_3.isEmpty())					
					throw new Exception ("The process class [process_class=" + name + "] is currently being used by a job. Do you want to continue?");

			} else if(type == Editor.SCHEDULES || type == Editor.SCHEDULE) {

				XPath x3 = XPath.newInstance("//run_time[@schedule='"+ name + "']");				 
				List listOfElement_3 = x3.selectNodes(_dom.getDoc());
				if(!listOfElement_3.isEmpty())					
					throw new Exception ("The Schedule [name=" + name + "] is currently being used by a Runtime. Do you want to continue?");

			}


		} catch (Exception e) {

			if(onlyWarning) {
				MainWindow.message(e.getMessage(), SWT.ICON_WARNING);	
			} else {				
				int c = MainWindow.message(e.getMessage(), SWT.YES | SWT.NO | SWT.ICON_WARNING);
				if(c != SWT.YES) {					
					return false;
				}
			}
		}
		return true;
	}

	/*public static void setUndoElement(Element elem) {
		if(undo == null)
			undo = new java.util.ArrayList();

		undo.set(0, elem);
		if(undo.size() > UNDO_SIZE)
			undo.remove(undo.size()-1);
	}

	public static Element getUndoElement() {
		if(undo != null) {    		    
			Element retval = (Element)((Element)undo.get(0)).clone();
			undo.remove(0);    		
			return retval;    		
		}
		return null;
	}
*/


	public static void setChangedForDirectory(Element elem, SchedulerDom dom) {

		if(dom.isDirectory() || dom.isLifeElement()) {
			//mögliche hot folder element
			Element e = Utils.getHotFolderParentElement(elem);

			if(e.getName().equals("order") || e.getName().equals("add_order")) {
				if(getJobElement(e).getName().equals("job"))			
					dom.setChangedForDirectory(e.getName(), Utils.getAttributeValue("name",Utils.getJobElement(e)), SchedulerDom.MODIFY);
				else 
					dom.setChangedForDirectory("order", Utils.getAttributeValue("job_chain", e) + "," + Utils.getAttributeValue("id", e), SchedulerDom.MODIFY);			
			} else {
				dom.setChangedForDirectory(e.getName(), Utils.getAttributeValue("name",e), SchedulerDom.MODIFY);
			}
		}
	}

	public static boolean hasSchedulesElement(SchedulerDom dom, Element element) {
		try {			
			if(Utils.getAttributeValue("schedule", element).length() > 0)
				return true;
			else 
				return false;
		} catch (Exception e) {
			try{
				new ErrorLog("error in " + sos.util.SOSClassUtil.getMethodName() , e);
			} catch(Exception ee){}

		}
		return false;
	}

	//setzt den Maus auf SandUhr
	public static void startCursor(Shell shell){
		if(!shell.isDisposed())
			shell.setCursor(new Cursor(shell.getDisplay(), SWT.CURSOR_WAIT));
	}

	//setzt den Maus auf Pfeil
	public static void stopCursor(Shell shell){
		if(!shell.isDisposed())
			shell.setCursor(new Cursor(shell.getDisplay(), SWT.CURSOR_ARROW));
	}


	public static void setResetElement(Element elem) {
		resetElement = (Element)elem.clone();
	}

	//public static void reset(Element elem, ISchedulerUpdate update, SchedulerDom currdom) {
	public static void reset(Element elem, IDataChanged update, DomParser currdom) {
		try {
			elem.getAttributes().removeAll(elem.getAttributes());
			List l = resetElement.getAttributes();
			for(int i = 0; i < l.size(); i++) {
				org.jdom.Attribute attr = (org.jdom.Attribute)l.get(i);
				elem.setAttribute(attr.getName(), attr.getValue());
			}

			elem.setContent(resetElement.cloneContent());

			if(currdom instanceof SchedulerDom)
				((sos.scheduler.editor.conf.forms.SchedulerForm)update).updateTree("main");
			else if(currdom instanceof sos.scheduler.editor.actions.ActionsDom)
				((sos.scheduler.editor.actions.forms.ActionsForm)update).updateTree("main");
			else if(currdom instanceof sos.scheduler.editor.doc.DocumentationDom)
				((sos.scheduler.editor.doc.forms.DocumentationForm)update).updateTree("main");



		} catch (Exception e) {
			try {
				new ErrorLog("error in " + sos.util.SOSClassUtil.getMethodName() , e);
			} catch(Exception ee) {
				//tu nichts
			}
		}
	}

	/**
	 * Generiert einen Help Button. 
	 * Beim Klicken wird ein Fenster geöffnet, indem der text steht 
	 */
	public static Button createHelpButton(Composite composite, String text, Shell shell) {

		Button butHelp = new Button(composite, SWT.NONE);
		//butHelp.setLayoutData(new GridData());
		final Shell shell_ = shell;
		final String text_ = text;
		butHelp.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(final SelectionEvent e) {
				Utils.showClipboard(Messages.getString(text_), shell_, false, null);
			}
		});

		butHelp.setImage(ResourceManager
				.getImageFromResource("/sos/scheduler/editor/icon_help_small.gif"));

		return butHelp;
	}

}
