package com.sos.joe.xml.jobscheduler;
import java.util.ArrayList;

import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class FormatHandler extends DefaultHandler implements ContentHandler {
	private SchedulerDom			_dom						= null;
	private StringBuffer			_sb							= new StringBuffer();
	private String					_encoding					= "ISO-8859-1";
	private String					_indentStr					= "    ";
	private String					_indent						= "    ";
	private StringBuffer			_text						= new StringBuffer();
	private int						_level						= 0;
	private boolean					_isOpen						= false;
	private String					_stylesheet					= "";
	private ArrayList				_noIndentInCDATAElements	= null;
	/** NO_CDATA_ELEMENTS dürfen nicht in CDATA geschrieben werden. Es geht hier um "Yes_no" XML Typen, die nur als Text yes oder no (ohne leerzeichen) haben dürfen */
	private static final String[]	NO_CDATA_ELEMENTS			= { "log_mail_cc", "log_mail_bcc", "log_mail_to", "mail_on_error", "mail_on_warning",
			"mail_on_success", "mail_on_process", "mail_on_delay_after_error", "history", "history_on_process", "history_with_log", "log_level" };

	public FormatHandler(SchedulerDom dom) {
		_dom = dom;
		init();
	}

	private void init() {
		_noIndentInCDATAElements = new ArrayList();
		String[] se = NO_CDATA_ELEMENTS;
		for (int i = 0; i < se.length; i++) {
			_noIndentInCDATAElements.add(se[i]);
		}
	}

	public void setEnconding(String encoding) {
		_encoding = encoding;
	}

	public String getXML() {
		return _sb.toString();
	}

	public void startDocument() {
		_sb.append("<?xml version=\"1.0\" encoding=\"" + _encoding + "\"?>\n\n");
		if (_stylesheet != null && _stylesheet.length() > 0)
			_sb.append(_stylesheet + "\n");
	}

	public void characters(char[] ch, int start, int length) throws SAXException {
		_text.append(new String(ch, start, length));
	}

	public void endElement(String namespaceURI, String localName, String qName) throws SAXException {
		_level--;
		_indent = strRepeat(_indentStr, _level);
		String text = _text.toString().trim();
		_text = new StringBuffer();
		boolean hasText = text.length() > 0;
		if (_isOpen && !hasText)
			_sb.append("/>\n");
		else
			if (_isOpen)
				_sb.append(">" + (_noIndentInCDATAElements.contains(localName) ? "" : "\n"));
		if (hasText && _noIndentInCDATAElements.contains(localName)) {
			_sb.append("<![CDATA[" + text + "]]>");
		}
		else
			if (hasText) {
				_sb.append(_indent + _indentStr + "<![CDATA[\n");
				_sb.append(text + "\n");
				_sb.append(_indent + _indentStr + "]]>\n");
			}
		if (!_isOpen || hasText) {
			if (_noIndentInCDATAElements.contains(localName))
				_sb.append("</" + qName + ">\n");// test
			else
				_sb.append(_indent + "</" + qName + ">\n");
		}
		_isOpen = false;
	}

	public void startElement(String namespaceURI, String localName, String qName, Attributes atts) throws SAXException {
		if (_isOpen)
			_sb.append(">\n");
		else
			if (!qName.equals("base") && !qName.equals("holiday"))
				_sb.append("\n"); // nl());
			else
				_sb.append("\n");
		_indent = strRepeat(_indentStr, _level);
		StringBuffer attributes = new StringBuffer();
		String sep = " ";
		// String sepStr = "\n" + _indent + strRepeat(" ", new String("<" + qName).length() + 1);
		String attrName = "";
		String strComment = "";
		for (int i = 0; i < atts.getLength(); i++) {
			String name = atts.getQName(i);
			String value = atts.getValue(i);
			if (name.equals("__comment__")) { // build element comment  
				if (qName.trim().equalsIgnoreCase("job")) {
					strComment = _indent + "<!-- " + value + " -->\n";
				}
				else {
					_sb.append(_indent + "<!-- " + value + " -->\n");
				}
			}
			else { // add attribute
				String uri = atts.getURI(i);
				if (!uri.equals("")) {
					attributes.append(sep + "xmlns:xsi" + "=\"" + uri + "\"");
				}
				attributes.append(sep + name + "=\"" + value + "\"");
				// sep = sepStr;
				if (name.equals("name")) {
					attrName = value;
				}
			}
		}
		_sb.append(_indent + "<" + qName + " " + attributes.toString());
		_isOpen = true;
		if (qName.trim().equalsIgnoreCase("job")) {
			if (strComment.length() > 0) {
				_sb.append(">\n");
				_isOpen = false;
				_sb.append(strComment);
			}
		}
		_level++;
	}

	private String strRepeat(String str, int cnt) {
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < cnt; i++)
			sb.append(str);
		return sb.toString();
	}

	public void setStyleSheet(String stylesheet_) {
		_stylesheet = stylesheet_;
	}

	private String nl() {
		switch (_level) {
			case 2:
				return strRepeat("\n", 2);
			case 3:
				return strRepeat("\n", 1);
			default:
				return "";
		}
	}
}
