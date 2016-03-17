package com.sos.joe.xml.jobscheduler;

import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import com.sos.joe.globals.options.Options;

public class FormatDetailHandler extends DefaultHandler implements ContentHandler {

    private StringBuilder sb = new StringBuilder();
    private String encoding = "ISO-8859-1";
    private String indentStr = "    ";
    private String indent = "    ";
    private StringBuilder text = new StringBuilder();
    private int level = 0;
    private boolean _isOpen = false;

    public FormatDetailHandler(DetailDom dom) {
        //
    }

    public void setEnconding(String encoding) {
        this.encoding = encoding;
    }

    public String getXML() {
        return sb.toString();
    }

    public void startDocument() {
        sb.append("<?xml version=\"1.0\" encoding=\"").append(encoding).append("\"?>\n\n");
        if (Options.getDetailXSLT() != null && !Options.getDetailXSLT().isEmpty()) {
            sb.append("<?xml-stylesheet type=\"text/xsl\" href=\"").append(Options.getDetailXSLT()).append("\"?> ");
        }
    }

    public void characters(char[] ch, int start, int length) throws SAXException {
        text.append(new String(ch, start, length));
    }

    public void endElement(String namespaceURI, String localName, String qName) throws SAXException {
        level--;
        indent = strRepeat(indentStr, level);
        String text = this.text.toString().trim();
        this.text = new StringBuilder();
        boolean hasText = !text.isEmpty();
        if (_isOpen && !hasText) {
            sb.append("/>\n");
        } else if (_isOpen) {
            sb.append(">\n");
        }
        if (hasText) {
            sb.append(indent).append(indentStr).append("<![CDATA[\n");
            sb.append(text).append("\n");
            sb.append(indent).append(indentStr).append("]]>\n");
        }
        if (!_isOpen || hasText) {
            sb.append(indent).append("</").append(qName).append(">\n");
        }
        _isOpen = false;
    }

    public void startElement(String namespaceURI, String localName, String qName, Attributes atts) throws SAXException {
        if (_isOpen) {
            sb.append(">\n");
        } else {
            sb.append("\n");
        }
        indent = strRepeat(indentStr, level);
        StringBuilder attributes = new StringBuilder();
        String sep = " ";
        String sepStr = "\n" + indent + strRepeat(" ", new String("<" + qName).length() + 1);
        for (int i = 0; i < atts.getLength(); i++) {
            String name = atts.getQName(i);
            String value = atts.getValue(i);
            String uri = atts.getURI(i);
            if ("__comment__".equals(name)) {
                sb.append(indent).append("<!-- ").append(value).append(" -->\n");
            } else {
                if (!"".equals(uri)) {
                    attributes.append(sep);
                    attributes.append("xmlns:xsi").append("=\"").append(uri).append("\"");
                }
                attributes.append(sep);
                attributes.append(name).append("=\"").append(value).append("\"");
                sep = sepStr;
            }
        }
        String text = this.text.toString().trim();
        if (text != null && !text.isEmpty()) {
            sb.append(text).append(" \n");
            this.text = new StringBuilder();
        }
        String ns = "";
        if (namespaceURI != null && !namespaceURI.isEmpty()) {
            ns = " xmlns=\"" + namespaceURI + "\"";
        }
        sb.append(indent).append("<").append(qName).append(ns).append(attributes.toString());
        _isOpen = true;
        level++;
    }

    private String strRepeat(String str, int cnt) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < cnt; i++) {
            sb.append(str);
        }
        return sb.toString();
    }

}