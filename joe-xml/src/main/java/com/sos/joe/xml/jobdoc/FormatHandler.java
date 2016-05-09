package com.sos.joe.xml.jobdoc;

import java.util.Iterator;
import java.util.List;

import org.jdom.Namespace;
import org.jdom.ProcessingInstruction;
import org.jdom.filter.ContentFilter;
import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class FormatHandler extends DefaultHandler implements ContentHandler {

    private static final String[] NO_LINEBREAKS = { "code", "strong" };
    private String _encoding = "utf-8";
    private DocumentationDom _dom = null;
    private StringBuilder _sb = new StringBuilder();
    private StringBuilder _text = new StringBuilder();
    private int _level = 0;
    private boolean _isOpen = false;
    private String _indentStr = "    ";
    private String _indent = "    ";
    private int _xhtml = -1;
    private String _root = null;
    private String _ns = null;
    private String _xhtmlNs = null;
    private boolean _doBreak = true;

    public FormatHandler(DocumentationDom dom) {
        _dom = dom;
        _root = _dom.getRoot().getName();
        _ns = _dom.getNamespace().getURI();
        _xhtmlNs = _dom.getNamespace("xhtml").getURI();
    }

    public void setEnconding(String encoding) {
        _encoding = encoding;
    }

    public String getXML() {
        return _sb.toString();
    }

    public void startDocument() {
        _sb.append("<?xml version=\"1.0\" encoding=\"").append(_encoding).append("\"?>\n");
        List pis = _dom.getDoc().getContent(new ContentFilter(ContentFilter.PI));
        for (Iterator it = pis.iterator(); it.hasNext();) {
            ProcessingInstruction pi = (ProcessingInstruction) it.next();
            _sb.append("<?").append(pi.getTarget()).append(" ").append(pi.getValue()).append("?>\n");
        }
    }

    public void characters(char[] ch, int start, int length) throws SAXException {
        _text.append(new String(ch, start, length));
    }

    public void endElement(String namespaceURI, String localName, String qName) throws SAXException {
        _doBreak = doLinebreak(qName);
        _level--;
        _indent = strRepeat(_indentStr, _level);
        if (_xhtml == _level) {
            _xhtml = -1;
        }
        String text = _text.toString().trim();
        _text = new StringBuilder();
        boolean hasText = !text.isEmpty();
        if (_isOpen && !hasText) {
            _sb.append("/>").append(_doBreak ? "\n" + _indent : "");
        } else if (_isOpen) {
            _sb.append(">").append(_doBreak ? "\n" + _indent : "");
        }
        if (hasText) {
            _sb.append(formatText(text)).append(_doBreak ? "\n" : "");
        }
        if (!_isOpen) {
            _sb.append("</").append(qName).append(">").append(_doBreak ? "\n" + _indent : "");
        } else if (hasText) {
            _sb.append(_doBreak ? _indent : "").append("</").append(qName).append(">").append(
                    _doBreak ? "\n" + strRepeat(_indentStr, _level - 1) : "");
        }
        if ("div".equalsIgnoreCase(qName)) {
            _xhtml--;
        }
        _isOpen = false;
    }

    public void startElement(String namespaceURI, String localName, String qName, Attributes atts) throws SAXException {
        _indent = strRepeat(_indentStr, _level);
        if (_isOpen) {
            _sb.append(">");
            _sb.append(breakline(_doBreak)).append(_indent);
        }
        _doBreak = doLinebreak(qName);
        StringBuilder attributes = new StringBuilder();
        String sep = " ";
        String sepStr = "\n" + _indent + strRepeat(" ", new String("<" + qName).length() + 1);
        for (int i = 0; i < atts.getLength(); i++) {
            String name = atts.getQName(i);
            String value = atts.getValue(i);
            String uri = atts.getURI(i);
            if (!"".equals(uri)) {
                attributes.append(sep);
                attributes.append("xmlns:xsi").append("=\"").append(uri).append("\"");
                sep = sepStr;
            }
            attributes.append(sep);
            attributes.append(name).append("=\"").append(value).append("\"");
            sep = sepStr;
        }
        if (namespaceURI != null && !"".equals(namespaceURI)) {
            if (_xhtml <= 0 && namespaceURI.equals(_xhtmlNs)) {
                _xhtml = _level;
                attributes.append(sep);
                attributes.append("xmlns").append("=\"").append(namespaceURI).append("\"");
            } else if (!namespaceURI.equals(_ns) && !namespaceURI.equals(_xhtmlNs)) {
                attributes.append(sep);
                attributes.append("xmlns").append("=\"").append(namespaceURI).append("\"");
            }
        }
        if (qName.equals(_root)) {
            if (_ns != null && !"".equals(_ns)) {
                attributes.append(sep);
                attributes.append("xmlns").append("=\"").append(_ns).append("\"");
            }
            for (Iterator it = _dom.getAdditinalNamespaces().iterator(); it.hasNext();) {
                Namespace ns = (Namespace) it.next();
                if (!"xsi".equals(ns.getPrefix())) {
                    attributes.append(sep);
                    attributes.append("xmlns:").append(ns.getPrefix()).append("=\"").append(ns.getURI()).append("\"");
                }
            }
        }
        _sb.append("<").append(qName).append(attributes.toString());
        _isOpen = true;
        _level++;
    }

    private String strRepeat(String str, int cnt) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < cnt; i++) {
            sb.append(str);
        }
        return sb.toString();
    }

    private String breakline(boolean doBreak) {
        if (doBreak) {
            switch (_level) {
            case 0:
                return strRepeat("\n", 1);
            case 1:
                return strRepeat("\n", 2);
            case 2:
                return strRepeat("\n", 1);
            default:
                return "\n";
            }
        } else {
            return "";
        }
    }

    private boolean doLinebreak(String name) {
        for (int i = 0; i < NO_LINEBREAKS.length; i++) {
            if (NO_LINEBREAKS[i].equals(name)) {
                return false;
            }
        }
        return true;
    }

    private StringBuilder formatText(String text) {
        StringBuilder sb = new StringBuilder();
        sb.append(_indentStr);
        text = text.replaceAll("\\s{1,}", " ").trim();
        sb.append(text);
        return sb;
    }

}