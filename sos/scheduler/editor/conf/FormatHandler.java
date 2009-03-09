package sos.scheduler.editor.conf;

import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;
import java.util.*;

public class FormatHandler extends DefaultHandler implements ContentHandler {


	private    SchedulerDom    _dom           = null;

	private    StringBuffer    _sb            = new StringBuffer();

	private    String          _encoding      = "ISO-8859-1";

	private    boolean         _disableJobs   = false;

	private    String          _indentStr     = "    ";

	private    String          _indent        = "    ";

	private    StringBuffer    _text          = new StringBuffer();

	private    int             _level         = 0;

	private    boolean         _isOpen        = false;

	private    String          _disabled      = "";

	private    String          _stylesheet    = "";
	
	private    ArrayList       _noIndentInCDATAElements = null; 

	/** NO_CDATA_ELEMENTS dürfen nicht in CDATA geschrieben werden. Es geht hier um "Yes_no" XML Typen, die nur als Text yes oder no (ohne leerzeichen) haben dürfen */
	private   static final String[]   NO_CDATA_ELEMENTS   = { "mail_on_error", "mail_on_warning", "mail_on_success", "mail_on_process", "mail_on_delay_after_error", "history", "history_on_process", "history_with_log", "log_level"};

	public FormatHandler(SchedulerDom dom) {
		_dom = dom;
		init();
	}
	
	private void init()  {
		_noIndentInCDATAElements = new ArrayList();
		String[] se = NO_CDATA_ELEMENTS;
		for(int i = 0; i < se.length; i++) {
			_noIndentInCDATAElements.add(se[i]);
		}
	}

	public void setEnconding(String encoding) {
		_encoding = encoding;
	}


	public void setDisableJobs(boolean disable) {
		_disableJobs = disable;
	}


	public String getXML() {
		return _sb.toString();
	}


	public void startDocument() {
		_sb.append("<?xml version=\"1.0\" encoding=\"" + _encoding + "\"?>\n\n");
		if(_stylesheet != null && _stylesheet.length() > 0)
			_sb.append(_stylesheet+"\n");
	}



	public void characters(char[] ch, int start, int length) throws SAXException {
		_text.append(new String(ch, start, length));
	}


	public void endElement(String namespaceURI, String localName, String qName) throws SAXException {
		_level--;
		_indent = strRepeat(_indentStr, _level);

		String text = _text.toString().trim();
		_text = new StringBuffer();

		boolean disableJobs = _disableJobs && qName.equals("jobs");
		boolean hasText = text.length() > 0;

		if (_isOpen && !hasText)
			_sb.append("/>\n");
		else if (_isOpen )
			_sb.append(">" + (_noIndentInCDATAElements.contains(localName) ? "" : "\n"));

		if (disableJobs)
			_sb.append("<!-- disabled\n");
		
		if(hasText && _noIndentInCDATAElements.contains(localName)) {
			_sb.append("<![CDATA[" + text+ "]]>");
		} else if (hasText) {
			_sb.append(_indent + _indentStr + "<![CDATA[\n");
			_sb.append(text + "\n");
			_sb.append(_indent + _indentStr + "]]>\n");
		}

		if (!_isOpen || hasText) {
			if(_noIndentInCDATAElements.contains(localName))
				_sb.append("</" + qName + ">\n");//test
			else
				_sb.append(_indent + "</" + qName + ">\n");
			
		}
		if (disableJobs)
			_sb.append("-->\n");

		if (_disabled.equals(qName)) {
			_sb.append("-->\n");
			_disabled = "";
		}

		_isOpen = false;
	}


	public void startElement(String namespaceURI, String localName, String qName, Attributes atts) throws SAXException {
		if (_isOpen)
			_sb.append(">\n");
		else if (!qName.equals("base") && !qName.equals("holiday"))
			_sb.append(nl());
		else
			_sb.append("\n");

		_indent = strRepeat(_indentStr, _level);

		StringBuffer attributes = new StringBuffer();
		String sep = " ";

		 
		
			
		String sepStr = "\n" + _indent + strRepeat(" ", new String("<" + qName).length() + 1);

		String attrName = "";

		// iterate atributes
		for (int i = 0; i < atts.getLength(); i++) {
			String name = atts.getQName(i);
			String value = atts.getValue(i);
			String uri = atts.getURI(i);

			if (name.equals("__comment__")) { // build element comment
				_sb.append(_indent + "<!-- " + value + " -->\n");
			} else { // add attribute
				if (!uri.equals("")) {
					attributes.append(sep);
					attributes.append("xmlns:xsi" + "=\"" + uri + "\"");
				}
				attributes.append(sep);
				attributes.append(name + "=\"" + value + "\"");
				sep = sepStr;

				if (name.equals("name"))
					attrName = value;
			}
		}

		boolean disableJobs = false;
		if (_dom.isJobDisabled(attrName)) { // disable job
			_sb.append("<!-- disabled=\"" + attrName + "\"\n");
			_disabled = qName;
		} else if (_disableJobs && qName.equals("jobs")) { // disable jobs
			disableJobs = true;
		}

		if (disableJobs)
			_sb.append("<!-- disabled\n");

		//_sb.append((_noIndentInCDATAElements.contains(localName) ? "" : _indent ) + "<" + qName + attributes.toString());
		_sb.append(_indent + "<" + qName + attributes.toString());

		_isOpen = true;
		_level++;

		if (disableJobs) {
			_sb.append(">\n-->\n");
			_isOpen = false;
		}
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
