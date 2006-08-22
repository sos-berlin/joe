package sos.scheduler.editor.doc.listeners;

import java.util.List;

import org.jdom.Attribute;
import org.jdom.Element;

import sos.scheduler.editor.app.Editor;
import sos.scheduler.editor.app.Utils;
import sos.scheduler.editor.doc.DocumentationDom;

public class ScriptListener {
    private DocumentationDom     _dom;

    private Element              _parent;

    private Element              _script;

    private int                  _type       = Editor.DOC_SCRIPT;

    public final static int      NONE        = 0;

    public final static int      JAVA        = 1;

    public final static int      JAVA_SCRIPT = 2;

    public final static int      PERL        = 3;

    public final static int      VB_SCRIPT   = 4;

    public final static int      COM         = 5;

    public final static String[] _languages  = { "", "java", "javascript", "perlScript", "vbscript", "" };


    public ScriptListener(DocumentationDom dom, Element parent, int type) {
        _dom = dom;
        _parent = parent;
        _type = type;
        setScript();
    }


    private Element setScript() {
        if (_type == Editor.DOC_MONITOR) {
            Element monitor = _parent.getChild("monitor", _dom.getNamespace());
            if (monitor == null) {
                monitor = new Element("monitor", _dom.getNamespace());
                _parent.addContent(monitor);
            }
            _script = monitor.getChild("script", _dom.getNamespace());
            if (_script == null) {
                _script = new Element("script", _dom.getNamespace());
                monitor.addContent(_script);
            }
        } else {
            _script = _parent.getChild("script", _dom.getNamespace());
            if (_script == null) {
                _script = new Element("script", _dom.getNamespace());
                _parent.addContent(_script);
            }
        }

        return _script;
    }


    public Element getScript() {
        return _script;
    }


    public void checkScript() {
        if (_script != null && _type == Editor.DOC_MONITOR) {
            Attribute language = _script.getAttribute("language");
            Attribute javaClass = _script.getAttribute("java_class");
            Attribute comClass = _script.getAttribute("com_class");
            Attribute resource = _script.getAttribute("resource");
            List includes = _script.getChildren("include");

            if (language == null && javaClass == null && comClass == null && resource == null && includes.size() == 0) {
                _parent.removeChild("monitor", _dom.getNamespace());
                // _dom.setChanged(true);
            }
        }
    }


    public int getLanguage() {
        if (_script != null)
            return languageAsInt(_script.getAttributeValue("language"));
        else
            return NONE;
    }


    private int languageAsInt(String language) {
        for (int i = 0; i < _languages.length; i++) {
            if (_languages[i].equalsIgnoreCase(language))
                return i;
        }

        if (_script != null && _script.getAttributeValue("com_class") != null)
            return COM;

        return NONE;
    }


    private String languageAsString(int language) {
        return _languages[language];
    }


    public void setLanguage(int language) {
        setScript();

        if (_script != null) {
            switch (language) {
                case NONE:
                    _script.removeAttribute("com_class");
                    _script.removeAttribute("java_class");
                    break;
                case PERL:
                case JAVA_SCRIPT:
                case VB_SCRIPT:
                    _script.removeAttribute("com_class");
                    _script.removeAttribute("java_class");
                    break;
                case JAVA:
                    _script.removeAttribute("com_class");
                    break;
                case COM:
                    if (_script.getAttributeValue("com_class") == null)
                        _script.setAttribute("com_class", "");
                    _script.removeAttribute("java_class");
                    break;
            }

            // if (language != NONE)
            Utils.setAttribute("language", languageAsString(language), _script, _dom);

            _dom.setChanged(true);
        }
    }


    public String getComClass() {
        return Utils.getAttributeValue("com_class", _script);
    }


    public void setComClass(String com) {
        setScript();
        Utils.setAttribute("com_class", com, "", _script, _dom);
    }


    public String getJavaClass() {
        return Utils.getAttributeValue("java_class", _script);
    }


    public void setJavaClass(String java) {
        setScript();
        Utils.setAttribute("java_class", java, "", _script, _dom);
    }


    public String[] getResources(String ownID) {
        return DocumentationListener.getIDs(_dom, ownID);
    }


    public String getResource() {
        return (Utils.getAttributeValue("resource", _script));
    }


    public void setResource(String resource) {
        setScript();
        Utils.setAttribute("resource", DocumentationListener.getID(resource), _script, _dom);
    }

}
