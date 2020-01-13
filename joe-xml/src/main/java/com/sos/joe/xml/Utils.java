package com.sos.joe.xml;

import java.io.StringWriter;
import java.util.List;
import java.util.regex.Pattern;
import org.eclipse.swt.SWT;
import org.eclipse.swt.dnd.Clipboard;
import org.eclipse.swt.dnd.TextTransfer;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.Text;
import org.jdom.Element;
import org.jdom.Namespace;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;
import org.jdom.xpath.XPath;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import sos.scheduler.editor.app.SchedulerEditorFontDialog;
import com.sos.dialog.swtdesigner.SWTResourceManager;
import com.sos.joe.globals.JOEConstants;
import com.sos.joe.globals.interfaces.IDataChanged;
import com.sos.joe.globals.interfaces.IUnsaved;
import com.sos.joe.globals.messages.ErrorLog;
import com.sos.joe.globals.messages.Messages;
import com.sos.joe.globals.misc.ResourceManager;
import com.sos.joe.globals.misc.TextDialog;
import com.sos.joe.globals.options.Options;
import com.sos.joe.xml.Events.ActionsDom;
import com.sos.joe.xml.jobdoc.DocumentationDom;
import com.sos.joe.xml.jobscheduler.SchedulerDom;

public class Utils {

    private static final Logger LOGGER = LoggerFactory.getLogger(Utils.class);
    private static final String STR_DEFAULT = "";
    private static final String INT_DEFAULT = null;
    private static final String BOOLEAN_DEFAULT = null;
    private static Clipboard _cb = null;
    private static Element resetElement = null;
    final static String JOE_L_Object_In_Use = "JOE_L_Object_In_Use";
    final static String JOE_L_Process_Class = "processclass";
    final static String JOE_L_Job = "job";
    final static String JOE_L_Job_chain = "job_chain";
    final static String JOE_L_Lock = "lock";
    final static String JOE_L_Schedule = "schedule";

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
        } else {
            return "";
        }
    }

    public static int getIntValue(String attribute, Element element) {
        return getIntValue(attribute, 0, element);
    }

    public static int getIntValue(String attribute, int defaultValue, Element element) {
        if (element == null) {
            return defaultValue;
        }
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
        return "yes".equalsIgnoreCase(getAttributeValue(attribute, element));
    }

    public static boolean getBooleanValue(String attribute, Element element) {
        return "true".equalsIgnoreCase(getAttributeValue(attribute, element));
    }

    public static void setAttribute(String attribute, String value, String defaultValue, Element element, DomParser dom) {
        if (value == null || value.trim().equals(defaultValue)) {
            element.removeAttribute(attribute);
            if (dom != null) {
                dom.setChanged(true);
            }
        } else if (!value.trim().equals(element.getAttributeValue(attribute))) {
            element.setAttribute(attribute, value.trim());
            if (dom != null) {
                dom.setChanged(true);
                if (dom instanceof SchedulerDom) {
                    if ("order".equals(element.getName())) {
                        ((SchedulerDom) dom).setChangedForDirectory("order",
                                Utils.getAttributeValue("job_chain", element) + "," + Utils.getAttributeValue("id", element), SchedulerDom.MODIFY);
                    } else {
                        ((SchedulerDom) dom).setChangedForDirectory("job", Utils.getAttributeValue("name", element), SchedulerDom.MODIFY);
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

    public static void setAttribute(String attribute, boolean value, boolean defaultValue, Element element, DomParser dom) {
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
            if (element != null) {
                return element.getTextTrim();
            } else {
                return "";
            }
        } else {
            return "";
        }
    }

    public static void setElement(String name, String value, boolean optional, Element parent, Namespace ns, DomParser dom) {
        if (parent != null) {
            Element element = parent.getChild(name, ns);
            if (element == null && ((value != null && !value.isEmpty()) || !optional)) {
                element = new Element(name, ns).setText(value);
                parent.addContent(element);
                if (dom != null) {
                    dom.setChanged(true);
                }
            } else if (element != null && (value == null || value.isEmpty()) && optional) {
                element.detach();
                if (dom != null) {
                    dom.setChanged(true);
                }
            } else if (element.getTextTrim() != null && !element.getTextTrim().equals(value)) {
                element.setText(value);
                if (dom != null) {
                    dom.setChanged(true);
                }
            }
        }
    }

    public static int getHours(String time, int defaultValue) {
        if (time == null || "".equals(time)) {
            return defaultValue;
        }
        String[] str = time.split(":");
        if (str.length > 1) {
            return new Integer(str[0]).intValue();
        } else {
            return defaultValue;
        }
    }

    public static int getMinutes(String time, int defaultValue) {
        if (time == null || "".equals(time)) {
            return defaultValue;
        }
        String[] str = time.split(":");
        if (str.length > 1) {
            return new Integer(str[1]).intValue();
        } else {
            return defaultValue;
        }
    }

    public static int getSeconds(String time, int defaultValue) {
        try {
            if (time == null || "".equals(time)) {
                return defaultValue;
            }
            String[] str = time.split(":");
            if (str.length > 2) {
                return new Integer(str[2]).intValue();
            } else if (str.length == 1) {
                return new Integer(str[0]).intValue();
            } else {
                return defaultValue;
            }
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
        } else {
            return asStr(hours) + ":" + asStr(minutes) + ":" + asStr(seconds);
        }
    }

    public static String getTime(int maxHour, String hours, String minutes, String seconds, boolean onlySeconds) {
        int h = Utils.str2int(hours, maxHour);
        int m = 0;
        int s = 0;
        if (h != 24) {
            m = Utils.str2int(minutes, 59);
            if (h > 0 || m > 0) {
                s = Utils.str2int(seconds, 59);
            } else {
                s = Utils.str2int(seconds);
            }
        }
        if (h < 0 && m < 0 && s < 0) {
            return "";
        } else {
            if (h < 0) {
                h = 0;
            }
            if (m < 0) {
                m = 0;
            }
            if (s < 0) {
                s = 0;
            }
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
            if ("".equals(s) || "-".equals(s)) {
                return true;
            }
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
        if (i > maxValue) {
            i = maxValue;
        }
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
        if (t.getText().length() == 0 && colorIt) {
            t.setBackground(Options.getRequiredColor());
        } else {
            t.setBackground(null);
        }
    }

    public static void setBackground(Table t, boolean colorIt) {
        if (t.getItemCount() == 0 && colorIt) {
            t.setBackground(Options.getRequiredColor());
        } else {
            t.setBackground(null);
        }
    }

    public static void setBackground(Control c, boolean toColor) {
        if (toColor) {
            c.setBackground(Options.getRequiredColor());
        } else {
            c.setBackground(null);
        }
    }

    public static String getFileFromURL(String url) {
        if (url != null && new java.io.File(url).isDirectory()) {
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
            IUnsaved unsaved = (IUnsaved) c;
            if (unsaved.isUnsaved()) {
                int ok = ErrorLog.message(Messages.getString("MainListener.apply_changes"), SWT.ICON_QUESTION | SWT.YES | SWT.NO | SWT.CANCEL);
                if (ok == SWT.CANCEL) {
                    return false;
                } else if (ok == SWT.NO) {
                    return true;
                } else if (ok == SWT.YES) {
                    unsaved.apply();
                }
            }
        }
        return true;
    }

    public static String getElementAsString(Element job) {
        org.jdom.output.XMLOutputter output = new org.jdom.output.XMLOutputter(org.jdom.output.Format.getPrettyFormat());
        String retVal = "";
        try {
            retVal = output.outputString(job);
        } catch (Exception e) {
            new ErrorLog("error in " + sos.util.SOSClassUtil.getMethodName(), e);
        }
        return retVal;
    }

    public static String noteAsStr(Element element) {
        if (element == null) {
            return "";
        }
        StringWriter stream = new StringWriter();
        XMLOutputter out = new XMLOutputter(Format.getPrettyFormat());
        try {
            out.output(element.getContent(), stream);
            stream.close();
        } catch (Exception e) {
            new ErrorLog("error in " + sos.util.SOSClassUtil.getMethodName(), e);
        }
        String str = stream.toString().trim();
        if (str.startsWith("<div")) {
            str = str.replaceFirst("\\A<\\s*div\\s*xmlns\\s*=\\s*\"[a-zA-Z0-9/:\\.]*\"\\s*>\\s*", "");
            str = str.replaceFirst("\\s*<\\s*/\\s*div\\s*>\\Z", "");
        }
        str = str.replaceFirst("\\<\\!\\[CDATA\\[", "");
        str = str.replaceFirst("]]>", "");
        str = str.replaceAll("<pre space=\"preserve\">", "<pre>");
        return str;
    }

    public static String getElementAsString(org.jdom.Text job) {
        org.jdom.output.XMLOutputter output = new org.jdom.output.XMLOutputter(org.jdom.output.Format.getPrettyFormat());
        String retVal = "";
        try {
            retVal = output.outputString(job);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
        }
        return retVal;
    }

    public static boolean isNumeric(String str) {
        boolean retVal = true;
        char[] c = null;
        if (str == null || str.isEmpty()) {
            return false;
        }
        c = str.toCharArray();
        for (int i = 0; i < str.length(); i++) {
            if (!Character.isDigit(c[i])) {
                return false;
            }
        }
        return retVal;
    }

    public static String showClipboard(String xml, Shell shell, boolean bApply, String selectStr) {
        return showClipboard(xml, shell, bApply, selectStr, false, null, false);
    }

    public static String showClipboard(String xml, Shell shell, boolean bApply, String selectStr, boolean showFunction, String scriptLanguage,
            boolean showWizzardInfo) {
        TextDialog dialog =
                new TextDialog(shell, SWT.CLOSE | SWT.TITLE | SWT.APPLICATION_MODAL | SWT.RESIZE, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL);
        dialog.setSize(new Point(500, 400));
        if (selectStr != null && !selectStr.trim().isEmpty()) {
            dialog.setContent(xml, selectStr);
        } else {
            dialog.setContent(xml);
        }
        dialog.setClipBoard(true);
        SchedulerEditorFontDialog fd = new SchedulerEditorFontDialog(shell);
        fd.readFontData();
        dialog.getStyledText().setFont(new Font(null, fd.getFontData()));
        dialog.getStyledText().setForeground(new Color(null, fd.getForeGround()));
        dialog.getStyledText().setEditable(bApply);
        dialog.setVisibleApplyButton(bApply);
        dialog.setBSaveWindow(true);
        dialog.setShowFunctions(showFunction);
        dialog.setScriptLanguage(scriptLanguage);
        dialog.setShowWizzardInfo(showWizzardInfo);
        String s = dialog.open(false);
        if (dialog.isClipBoardClick()) {
            copyClipboard(s, shell.getDisplay());
            return null;
        }
        if (!dialog.isApplyBoardClick()) {
            s = null;
        }
        return s;
    }

    public static void copyClipboard(String content, Display display) {
        if (_cb == null) {
            _cb = new Clipboard(display);
        }
        TextTransfer transfer = TextTransfer.getInstance();
        _cb.setContents(new Object[] { content }, new Transfer[] { transfer });
    }

    public static boolean isElementEnabled(String which, SchedulerDom dom, Element elem) {
        if ("job".equals(which) && elem != null && !"job".equals(elem.getName())) {
            elem = getJobElement(elem);
        }
        java.util.ArrayList listOfReadOnly = dom.getListOfReadOnlyFiles();
        if ("commands".equalsIgnoreCase(which)) {
            if (listOfReadOnly != null
                    && listOfReadOnly.contains(which + "_" + Utils.getAttributeValue("job_chain", elem) + "," + Utils.getAttributeValue("id", elem))) {
                return false;
            }
        } else {
            if (listOfReadOnly != null && listOfReadOnly.contains(which + "_" + Utils.getAttributeValue("name", elem))) {
                return false;
            }
        }
        return true;
    }

    public static Element getJobElement(Element elem) {
        boolean loop = true;
        int counter = 0;
        while (loop) {
            if (elem != null && elem.getParentElement() != null && !"spooler".equalsIgnoreCase(elem.getParentElement().getName())) {
                if ("job".equalsIgnoreCase(elem.getName())) {
                    return elem;
                } else if ("job".equalsIgnoreCase(elem.getParentElement().getName())) {
                    return elem.getParentElement();
                } else {
                    elem = elem.getParentElement();
                }
                ++counter;
                if (counter == 5) {
                    loop = false;
                }
            } else {
                return elem;
            }
        }
        return elem;
    }

    public static Element getHotFolderParentElement(Element elem) {
        boolean loop = true;
        int counter = 0;
        while (loop) {
            if (elem != null && elem.getParentElement() != null && !"spooler".equalsIgnoreCase(elem.getParentElement().getName())) {
                if ("job".equalsIgnoreCase(elem.getName()) || "job_chain".equalsIgnoreCase(elem.getName())
                        || "add_order".equalsIgnoreCase(elem.getName()) || "order".equalsIgnoreCase(elem.getName())
                        || "process_class".equalsIgnoreCase(elem.getName()) || "schedule".equalsIgnoreCase(elem.getName())
                        || "lock".equalsIgnoreCase(elem.getName())) {
                    return elem;
                } else if ("job".equalsIgnoreCase(elem.getParentElement().getName())
                        || "job_chain".equalsIgnoreCase(elem.getParentElement().getName())
                        || "add_order".equalsIgnoreCase(elem.getParentElement().getName())
                        || "order".equalsIgnoreCase(elem.getParentElement().getName())
                        || "process_class".equalsIgnoreCase(elem.getParentElement().getName())
                        || "schedule".equalsIgnoreCase(elem.getParentElement().getName())
                        || "lock".equalsIgnoreCase(elem.getParentElement().getName())) {
                    return elem.getParentElement();
                } else {
                    elem = elem.getParentElement();
                }
                ++counter;
                if (counter == 5) {
                    loop = false;
                }
            } else {
                return elem;
            }
        }
        return elem;
    }

    public static Element getRunTimeParentElement(Element elem) {
        boolean loop = true;
        int counter = 0;
        while (loop) {
            if (elem != null && elem.getParentElement() != null) {
                if ("job".equalsIgnoreCase(elem.getName()) || "schedule".equalsIgnoreCase(elem.getName()) || "order".equalsIgnoreCase(elem.getName())
                        || "add_order".equalsIgnoreCase(elem.getName())) {
                    return elem;
                } else if ("job".equalsIgnoreCase(elem.getParentElement().getName())) {
                    return elem.getParentElement();
                } else {
                    elem = elem.getParentElement();
                }
                ++counter;
                if (counter == 5) {
                    loop = false;
                }
            } else {
                return elem;
            }
        }
        return elem;
    }

    public static String escape(String s) {
        if (s == null) {
            return s;
        }
        int len = s.length();
        StringBuilder str = new StringBuilder(len);
        for (int i = 0; i < len; i++) {
            char ch = s.charAt(i);
            switch (ch) {
            case '<':
                str.append("&lt;");
                break;
            case '>':
                str.append("&gt;");
                break;
            case '"':
                str.append("&quot;");
                break;
            case '&':
                if (!(s.substring(i).startsWith("&quot;") || s.substring(i).startsWith("&lt;") || s.substring(i).startsWith("&gt;") || s.substring(i).startsWith(
                        "&amp;"))) {
                    str.append("&amp;");
                } else {
                    str.append(ch);
                }
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

    public static void removeChildrensWithName(Element elem, String name) {
        Element child = elem.getChild(name);
        java.util.ArrayList nl = new java.util.ArrayList();
        if (child != null) {
            java.util.List l = child.getChildren();
            for (int i = 0; i < l.size(); i++) {
                Element e = (Element) l.get(i);
                if (!Utils.getAttributeValue("name", e).isEmpty()) {
                    nl.add(e);
                }
            }
            l.removeAll(nl);
            if (l.isEmpty()) {
                elem.removeChildren(name);
            }
        }
    }

    public static boolean existName(String name, Element elem, String childname) {
        if (elem.getParentElement() != null) {
            List l = elem.getParentElement().getChildren(childname);
            for (int i = 0; i < l.size(); i++) {
                Element e = (Element) l.get(i);
                if (!e.equals(elem) && Utils.getAttributeValue("name", e).equalsIgnoreCase(name)) {
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
    }

    public static boolean checkElement(String name, SchedulerDom _dom, int type, String which) {
        boolean onlyWarning = false;
        try {
            if (which == null) {
                which = "";
            }
            if (type == JOEConstants.JOB_CHAIN) {
                String strObject = Messages.getLabel(JOE_L_Job_chain);
                String strM = Messages.getLabel(JOE_L_Object_In_Use);
                String strException = String.format(strM, strObject, name);
                XPath x3 = XPath.newInstance("//order[@job_chain='" + name + "']");
                List<Element> listOfElement_3 = x3.selectNodes(_dom.getDoc());
                if (!listOfElement_3.isEmpty()) {
                    throw new Exception(strException);
                }
                XPath x4 = XPath.newInstance("//add_order[@job_chain='" + name + "']");
                List<Element> listOfElement_4 = x4.selectNodes(_dom.getDoc());
                if (!listOfElement_4.isEmpty()) {
                    throw new Exception(strException);
                }
            } else if (type == JOEConstants.JOB_CHAINS) {
                String strObject = Messages.getLabel(JOE_L_Job_chain);
                String strM = Messages.getLabel(JOE_L_Object_In_Use);
                String strException = String.format(strM, strObject, name);
                XPath x3 = XPath.newInstance("//order[@job_chain='" + name + "']");
                List listOfElement_3 = x3.selectNodes(_dom.getDoc());
                if (!listOfElement_3.isEmpty()) {
                    throw new Exception(strException);
                }
                XPath x4 = XPath.newInstance("//add_order[@job_chain='" + name + "']");
                List listOfElement_4 = x4.selectNodes(_dom.getDoc());
                if (!listOfElement_4.isEmpty()) {
                    throw new Exception(strException);
                }
            } else if (type == JOEConstants.JOB) {
                String strObject = Messages.getLabel(JOE_L_Job);
                String strM = Messages.getLabel(JOE_L_Object_In_Use);
                String strException = String.format(strM, strObject, name);
                if (which != null && "close".equalsIgnoreCase(which)) {
                    onlyWarning = true;
                    XPath x0 = XPath.newInstance("//job[@name='" + name + "']");
                    Element e = (Element) x0.selectSingleNode(_dom.getDoc());
                    boolean isOrder = "yes".equalsIgnoreCase(Utils.getAttributeValue("order", e));
                    if (!isOrder) {
                        XPath x3 = XPath.newInstance("//job_chain_node[@job='" + name + "']");
                        List listOfElement_3 = x3.selectNodes(_dom.getDoc());
                        if (!listOfElement_3.isEmpty()) {
                            throw new Exception(strException);
                        }
                    }
                } else {
                    if (name.isEmpty()) {
                        return true;
                    }
                    XPath x0 = XPath.newInstance("//job[@name='" + name + "']");
                    Element e = (Element) x0.selectSingleNode(_dom.getDoc());
                    boolean isOrder = "yes".equalsIgnoreCase(Utils.getAttributeValue("order", e));
                    if (isOrder) {
                        XPath x = XPath.newInstance("//job[@name='" + name + "']/run_time[@let_run='yes' or @once='yes' or @single_start]");
                        List listOfElement = x.selectNodes(_dom.getDoc());
                        if (!listOfElement.isEmpty()) {
                            throw new Exception("An order job [name=" + name + "] may not use single_start-, start_once- and "
                                    + "let_run attributes in Runtime Elements. Should these attributes be deleted?");
                        }
                        XPath x2 = XPath.newInstance("//job[@name='" + name + "']/run_time//period[@let_run='yes' or @single_start]");
                        List listOfElement_2 = x2.selectNodes(_dom.getDoc());
                        if (!listOfElement_2.isEmpty()) {
                            throw new Exception("An order job [name=" + name + "] may not use single_start-, start_once- and "
                                    + "let_run attributes in Runtime Elements. Should these attributes be deleted?");
                        }
                    }
                    XPath x3 = XPath.newInstance("//job_chain_node[@job='" + name + "']");
                    List listOfElement_3 = x3.selectNodes(_dom.getDoc());
                    if (!listOfElement_3.isEmpty()) {
                        if ("change_order".equalsIgnoreCase(which)) {
                            throw new Exception(strException);
                        } else {
                            throw new Exception(strException);
                        }
                    }
                }
            } else if (type == JOEConstants.JOBS) {
                String strObject = Messages.getLabel(JOE_L_Job);
                String strM = Messages.getLabel(JOE_L_Object_In_Use);
                String strException = String.format(strM, strObject, name);
                XPath x3 = XPath.newInstance("//job_chain_node[@job='" + name + "']");
                List listOfElement_3 = x3.selectNodes(_dom.getDoc());
                if (!listOfElement_3.isEmpty()) {
                    throw new Exception(strException);
                }
            } else if (type == JOEConstants.LOCKS) {
                String strObject = Messages.getLabel(JOE_L_Lock);
                String strM = Messages.getLabel(JOE_L_Object_In_Use);
                String strException = String.format(strM, strObject, name);
                XPath x3 = XPath.newInstance("//lock.use[@lock='" + name + "']");
                List listOfElement_3 = x3.selectNodes(_dom.getDoc());
                if (!listOfElement_3.isEmpty()) {
                    throw new Exception(strException);
                }
            } else if (type == JOEConstants.PROCESS_CLASSES) {
                String strObject = Messages.getLabel(JOE_L_Process_Class);
                String strM = Messages.getLabel(JOE_L_Object_In_Use);
                String strException = String.format(strM, strObject, name);
                XPath x3 = XPath.newInstance("//job[@process_class='" + name + "']");
                List listOfElement_3 = x3.selectNodes(_dom.getDoc());
                if (!listOfElement_3.isEmpty()) {
                    throw new Exception(strException);
                }
            } else if (type == JOEConstants.SCHEDULES || type == JOEConstants.SCHEDULE) {
                String strObject = Messages.getLabel(JOE_L_Schedule);
                String strM = Messages.getLabel(JOE_L_Object_In_Use);
                String strException = String.format(strM, strObject, name);
                XPath x3 = XPath.newInstance("//run_time[@schedule='" + name + "']");
                List listOfElement_3 = x3.selectNodes(_dom.getDoc());
                if (!listOfElement_3.isEmpty()) {
                    throw new Exception(strException);
                }
            }
        } catch (Exception e) {
            if (onlyWarning) {
                ErrorLog.message(e.getMessage(), SWT.ICON_WARNING);
            } else {
                int c = ErrorLog.message(e.getMessage(), SWT.YES | SWT.NO | SWT.ICON_WARNING);
                if (c != SWT.YES) {
                    return false;
                }
            }
        }
        return true;
    }

    public static void setChangedForDirectory(Element elem, SchedulerDom dom) {
        if (dom.isDirectory() || dom.isLifeElement()) {
            Element e = Utils.getHotFolderParentElement(elem);
            if ("order".equals(e.getName()) || "add_order".equals(e.getName())) {
                if ("job".equals(getJobElement(e).getName())) {
                    dom.setChangedForDirectory(e.getName(), Utils.getAttributeValue("name", Utils.getJobElement(e)), SchedulerDom.MODIFY);
                } else {
                    dom.setChangedForDirectory("order", Utils.getAttributeValue("job_chain", e) + "," + Utils.getAttributeValue("id", e),
                            SchedulerDom.MODIFY);
                }
            } else {
                dom.setChangedForDirectory(e.getName(), Utils.getAttributeValue("name", e), SchedulerDom.MODIFY);
            }
        }
    }

    public static boolean hasSchedulesElement(SchedulerDom dom, Element element) {
        try {
            return !Utils.getAttributeValue("schedule", element).isEmpty();
        } catch (Exception e) {
            new ErrorLog("error in " + sos.util.SOSClassUtil.getMethodName(), e);
        }
        return false;
    }

    public static void startCursor(Shell shell) {
        if (!shell.isDisposed()) {
            shell.setCursor(SWTResourceManager.getCursor(SWT.CURSOR_WAIT));
        }
    }

    public static void stopCursor(Shell shell) {
        if (!shell.isDisposed()) {
            shell.setCursor(SWTResourceManager.getCursor(SWT.CURSOR_ARROW));
        }
    }

    public static void setResetElement(Element elem) {
        resetElement = (Element) elem.clone();
    }

    public static void reset(Element elem, IDataChanged update, DomParser currdom) {
        try {
            elem.getAttributes().removeAll(elem.getAttributes());
            List l = resetElement.getAttributes();
            for (int i = 0; i < l.size(); i++) {
                org.jdom.Attribute attr = (org.jdom.Attribute) l.get(i);
                elem.setAttribute(attr.getName(), attr.getValue());
            }
            elem.setContent(resetElement.cloneContent());
            if (currdom instanceof SchedulerDom) {
                update.updateTree("main");
            } else if (currdom instanceof ActionsDom) {
                update.updateTree("main");
            } else if (currdom instanceof DocumentationDom) {
                update.updateTree("main");
            }
        } catch (Exception e) {
            new ErrorLog("error in " + sos.util.SOSClassUtil.getMethodName(), e);
        }
    }

    public static Button createHelpButton(Composite composite, String text, Shell shell) {
        Button butHelp = new Button(composite, SWT.NONE);
        final Shell shell_ = shell;
        final String text_ = text;
        butHelp.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(final SelectionEvent e) {
                Utils.showClipboard(Messages.getString(text_), shell_, false, null);
            }
        });
        butHelp.setImage(ResourceManager.getImageFromResource("/sos/scheduler/editor/icon_help_small.gif"));
        return butHelp;
    }

}