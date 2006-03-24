package sos.scheduler.editor.app;

import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class FormatHandler extends DefaultHandler implements ContentHandler {
	private DomParser _dom;
	private StringBuffer _sb = new StringBuffer();

	private String _encoding = "ISO-8859-1";
	
	private boolean _disableJobs = false;
	
	private String _indentStr = "    ";

	private String _indent = "    ";

	private StringBuffer _text = new StringBuffer();
	
	private int _level = 0;

	private boolean _isOpen = false;

	private String _disabled = "";

	public FormatHandler(DomParser dom) {
		_dom = dom;
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
	}

	
	public void characters(char[] ch, int start, int length)
			throws SAXException {
		_text.append(new String(ch, start, length));
	}

	public void endElement(String namespaceURI, String localName, String qName)
			throws SAXException {
		_level--;
		_indent = strRepeat(_indentStr, _level);
		
		String text = _text.toString().trim();
		_text = new StringBuffer();

		boolean disableJobs = _disableJobs && qName.equals("jobs");
		boolean hasText = text.length() > 0;
		
		if (_isOpen && !hasText)
			_sb.append("/>\n");
		else if(_isOpen)
			_sb.append(">\n");
			
		if(disableJobs)
			_sb.append("<!-- disabled\n");
		
		if (hasText) {
			_sb.append(_indent + _indentStr + "<![CDATA[\n");
			_sb.append(text + "\n");
			_sb.append(_indent + _indentStr + "]]>\n");
		}

		if(!_isOpen || hasText)
			_sb.append(_indent + "</" + qName + ">\n");

		if(disableJobs)
			_sb.append("-->\n");
		
		if (_disabled.equals(qName)) {
			_sb.append("-->\n");
			_disabled = "";
		}

		_isOpen = false;
	}

	public void startElement(String namespaceURI, String localName,
			String qName, Attributes atts) throws SAXException {
		if (_isOpen)
			_sb.append(">\n");
		else if (!qName.equals("base") && !qName.equals("holiday"))
			_sb.append(nl());
		else
			_sb.append("\n");

		_indent = strRepeat(_indentStr, _level);

		StringBuffer attributes = new StringBuffer();
		String sep = " ";
		String sepStr = "\n" + _indent
				+ strRepeat(" ", new String("<" + qName).length() + 1);

		String attrName = "";
		
		// iterate atributes
		for (int i = 0; i < atts.getLength(); i++) {
			String name = atts.getQName(i);
			String value = atts.getValue(i);

			if (name.equals("__comment__")) { // build element comment
				_sb.append(_indent + "<!-- " + value + " -->\n");
			}
			else { // add attribute
				attributes.append(sep);
				attributes.append(name + "=\"" + value + "\"");
				sep = sepStr;
				
				if(name.equals("name"))
					attrName = value;
			}
		}
		
		boolean disableJobs = false;
		if(_dom.isJobDisabled(attrName)) { // disable job
			_sb.append("<!-- disabled=\"" + attrName + "\"\n");
			_disabled = qName;
		} else if(_disableJobs && qName.equals("jobs")) { // disable jobs
			disableJobs = true;
		}
		
		if(disableJobs)
			_sb.append("<!-- disabled\n");
		
		_sb.append(_indent + "<" + qName + attributes.toString());

		_isOpen = true;
		_level++;

		if(disableJobs) {
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
