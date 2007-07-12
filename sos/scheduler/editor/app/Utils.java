package sos.scheduler.editor.app;

import java.io.StringWriter;

import org.eclipse.swt.SWT;
import org.eclipse.swt.dnd.Clipboard;
import org.eclipse.swt.dnd.TextTransfer;
import org.eclipse.swt.dnd.Transfer;
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
import org.eclipse.swt.widgets.Shell;

import sos.scheduler.editor.conf.SchedulerDom;

public class Utils {

    // saving a default value results in removing the tag
    private static final String STR_DEFAULT     = "";

    private static final String INT_DEFAULT     = null;

    private static final String BOOLEAN_DEFAULT = null;

    private static Clipboard    _cb             = null;
    

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
        if (value == null || value.equals(defaultValue)) {
            element.removeAttribute(attribute);
            if (dom != null)
                dom.setChanged(true);
        } else if (!value.equals(element.getAttributeValue(attribute))) {
            element.setAttribute(attribute, value);
            if (dom != null) {
                dom.setChanged(true);
                if(dom instanceof SchedulerDom) {
                	((SchedulerDom)dom).setChangedForDirectory("job", Utils.getAttributeValue("name",element), SchedulerDom.MODIFY);
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
            s = Utils.str2int(seconds, 59);
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
        if ((s.length() < l) && (!s.trim().equals(""))) {
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
    	if(url != null && new java.io.File(url).getName().startsWith("#xml#.config.")) {
        	return new java.io.File(url).getParent();
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
                else if (ok == SWT.YES)
                    unsaved.apply();
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
            e.printStackTrace();
        }
        
        String str = stream.toString().trim();
        if(str.startsWith("<div")) {
            str = str.replaceFirst("\\A<\\s*div\\s*xmlns\\s*=\\s*\"[a-zA-Z0-9/:\\.]*\"\\s*>\\s*", "");
            str = str.replaceFirst("\\s*<\\s*/\\s*div\\s*>\\Z", "");
        }
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

    public static String showClipboard(String xml, Shell shell, boolean bApply, String selectStr) {
    	Font font = new Font(Display.getDefault(), "Courier New", 8, SWT.NORMAL);
    	TextDialog dialog = new TextDialog(shell, SWT.CLOSE | SWT.TITLE | SWT.APPLICATION_MODAL
    			| SWT.RESIZE, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL);
    	dialog.setSize(new Point(500, 400));
    	if(selectStr != null && selectStr.trim().length() > 0)
    		dialog.setContent(xml, selectStr);
    	else
    		dialog.setContent(xml);
    	dialog.setClipBoard(true);
    	dialog.getStyledText().setFont(font);
    	dialog.getStyledText().setEditable(bApply);
    	dialog.setVisibleApplyButton(bApply);
    	dialog.setBSaveWindow(true);
    	
    	String s = dialog.open(true);
    	
    	
    	if (dialog.isClipBoardClick()) {
    		copyClipboard(s, shell.getDisplay());
    		return null;
    	}
    	
    	if(!dialog.isApplyBoardClick()) {
    		s = null;
    	}
    	    	
    	if (font != null)
    		font.dispose();
    	
    	return s;
    }
         
    public static String showClipboard(String xml, Shell shell) {
    	return showClipboard(xml, shell, false, null);    	
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
        if (listOfReadOnly != null && listOfReadOnly.contains(which + "_" + Utils.getAttributeValue("name", elem))) {        	
        	//this.setEnabled(false);
        	return false;
        } 
        return true;
    }
    
    /*
     * liefert den Vaterknoten Job
     */
    private static Element getJobElement(Element elem) {
		
		boolean loop = true;
		int counter = 0;
		while(loop) {
			if(elem != null && elem.getParentElement()!= null) {
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
	
}
