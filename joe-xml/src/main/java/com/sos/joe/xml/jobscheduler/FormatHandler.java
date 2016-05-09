package com.sos.joe.xml.jobscheduler;

import java.util.ArrayList;

import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class FormatHandler extends DefaultHandler implements ContentHandler {

    private SchedulerDom _dom = null;
    private StringBuilder _sb = new StringBuilder();
    private String _encoding = "ISO-8859-1";
    private String _indentStr = "    ";
    private String _indent = "    ";
    private StringBuilder _text = new StringBuilder();
    private int _level = 0;
    private boolean _isOpen = false;
    private String _stylesheet = "";
    private String aktNamepace = "___";
    private String aktElementName = "___";
    private ArrayList _noIndentInCDATAElements = null;
    private static final String[] NO_CDATA_ELEMENTS = { "log_mail_cc", "log_mail_bcc", "log_mail_to", "mail_on_error", "mail_on_warning",
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
        _sb.append("<?xml version=\"1.0\" encoding=\"").append(_encoding).append("\"?>\n\n");
        if (_stylesheet != null && !_stylesheet.isEmpty()) {
            _sb.append(_stylesheet).append("\n");
        }
    }

    public void characters(char[] ch, int start, int length) throws SAXException {
        _text.append(new String(ch, start, length));
    }

    public void endElement(String namespaceURI, String localName, String qName) throws SAXException {
        if (aktElementName.equals(qName)) {
            aktNamepace = "__";
        }
        _level--;
        _indent = strRepeat(_indentStr, _level);
        String text = _text.toString().trim();
        _text = new StringBuilder();
        boolean hasText = !text.isEmpty();
        if (_isOpen && !hasText) {
            _sb.append("/>\n");
        } else if (_isOpen) {
            _sb.append(">").append(_noIndentInCDATAElements.contains(localName) ? "" : "\n");
        }
        if (hasText && _noIndentInCDATAElements.contains(localName)) {
            _sb.append("<![CDATA[").append(text).append("]]>");
        } else if (hasText) {
            _sb.append(_indent).append(_indentStr).append("<![CDATA[\n");
            _sb.append(text).append("\n");
            _sb.append(_indent).append(_indentStr).append("]]>\n");
        }
        if (!_isOpen || hasText) {
            if (_noIndentInCDATAElements.contains(localName)) {
                _sb.append("</").append(qName).append(">\n");
            } else {
                _sb.append(_indent).append("</").append(qName).append(">\n");
            }
        }
        _isOpen = false;
    }

    public void startElement(String namespaceURI, String localName, String qName, Attributes atts) throws SAXException {
        if (_isOpen) {
            _sb.append(">\n");
        } else if (!qName.equals("base") && !qName.equals("holiday")) {
            _sb.append("\n");
        } else {
            _sb.append("\n");
        }
        _indent = strRepeat(_indentStr, _level);
        StringBuilder attributes = new StringBuilder();
        String sep = " ";
        String attrName = "";
        String strComment = "";
        for (int i = 0; i < atts.getLength(); i++) {
            String name = atts.getQName(i);
            String value = atts.getValue(i);
            if ("__comment__".equals(name)) {
                if ("job".equalsIgnoreCase(qName.trim())) {
                    strComment = _indent + "<!-- " + value + " -->\n";
                } else {
                    _sb.append(_indent).append("<!-- ").append(value).append(" -->\n");
                }
            } else {
                String uri = atts.getURI(i);
                if (!"".equals(uri)) {
                    attributes.append(sep).append("xmlns:xsi").append("=\"").append(uri).append("\"");
                }
                if (!"".equals(namespaceURI) && !aktNamepace.equals(namespaceURI)) {
                    attributes.append(sep).append("xmlns").append("=\"").append(namespaceURI).append("\"");
                    aktNamepace = namespaceURI;
                    aktElementName = qName;
                }
                attributes.append(sep).append(name).append("=\"").append(value).append("\"");
                if ("name".equals(name)) {
                    attrName = value;
                }
            }
        }
        _sb.append(_indent).append("<").append(qName).append(" ").append(attributes.toString());
        _isOpen = true;
        if ("job".equalsIgnoreCase(qName.trim()) && !strComment.isEmpty()) {
            _sb.append(">\n");
            _isOpen = false;
            _sb.append(strComment);
        }
        _level++;
    }

    private String strRepeat(String str, int cnt) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < cnt; i++) {
            sb.append(str);
        }
        return sb.toString();
    }

    public void setStyleSheet(String stylesheet_) {
        _stylesheet = stylesheet_;
    }

}