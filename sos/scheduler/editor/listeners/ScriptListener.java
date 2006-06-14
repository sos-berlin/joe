package sos.scheduler.editor.listeners;

import java.util.Iterator;
import java.util.List;

import org.jdom.CDATA;
import org.jdom.Element;

import sos.scheduler.editor.app.DomParser;
import sos.scheduler.editor.app.Editor;
import sos.scheduler.editor.app.Utils;

public class ScriptListener {
	public final static int NONE = 0;

	public final static int JAVA = 1;

	public final static int JAVA_SCRIPT = 2;

	public final static int PERL = 3;

	public final static int VB_SCRIPT = 4;

	public final static int COM = 5;

	public final static String[] _languages = { "", "java", "javascript",
			"perlScript", "VBScript", "" };

	private DomParser _dom;

	private Element _parent;

	private Element _script;

	private int _type;

	public ScriptListener(DomParser dom, Element parent, int type) {
		_dom = dom;
		_parent = parent;
		_type = type;

		setScript();
	}

	private void setScript() {
		if (_type == Editor.MONITOR) {
			Element monitor = _parent.getChild("monitor");
			if (monitor != null) {
				_script = monitor.getChild("script");
				if (_script == null)
					monitor.detach();
			}
		} else
			_script = _parent.getChild("script");
	}

	private int languageAsInt(String language) {
		for (int i = 0; i < _languages.length; i++) {
			if (_languages[i].equalsIgnoreCase(language))
				return i;
		}

		if (_script != null
				&& (_script.getAttribute("com_class") != null || _script
						.getAttribute("filename") != null))
			return COM;

		System.out.println("unknown language: " + language
				+ " - set to java...");
		if (_script != null)
			_script.setAttribute("language", "java");
		return -1;
	}

	private String languageAsString(int language) {
		return _languages[language];
	}

	public int getLanguage() {
		if (_script != null)
			return languageAsInt(_script.getAttributeValue("language"));
		else
			return NONE;
	}

	public void setLanguage(int language) {
		setScript();
		if (_script == null && language != NONE) {
			// init script element
			_script = new Element("script");
			if (_type == Editor.MONITOR) {
				Element monitor = _parent.getChild("monitor");
				if (monitor == null) {
					monitor = new Element("monitor");
					_parent.addContent(monitor);
				}
				monitor.addContent(_script);
			} else
				_parent.addContent(_script);
		}

		if (_script != null) {
			switch (language) {
			case NONE: // remove script element
				if (_type == Editor.MONITOR)
					_parent.removeChild("monitor");
				else
					_parent.removeChildren("script");
				_script = null;
				break;
			case PERL:
			case JAVA_SCRIPT:
			case VB_SCRIPT:
				_script.removeAttribute("com_class");
				_script.removeAttribute("filename");
				_script.removeAttribute("java_class");
				break;
			case JAVA:
				if (_script.getAttribute("java_class") == null)
					_script.setAttribute("java_class", "");
				_script.removeAttribute("com_class");
				_script.removeAttribute("filename");
				break;
			case COM:
				if (_script.getAttribute("com_class") == null)
					_script.setAttribute("com_class", "");
				if (_script.getAttribute("filename") == null)
					_script.setAttribute("filename", "");
				_script.removeAttribute("java_class");
				setSource("");
				break;
			}

			if(language != NONE)
				Utils.setAttribute("language", languageAsString(language), _script,
					_dom);

			_dom.setChanged(true);
		}
	}

	private void setAttributeValue(String element, String value, int language) {
		if (getLanguage() == language) {
			_script.setAttribute(element, value);
			_dom.setChanged(true);
		}
	}

	public String getJavaClass() {
		return Utils.getAttributeValue("java_class", _script);
	}

	public void setJavaClass(String javaClass) {
		setAttributeValue("java_class", javaClass, JAVA);
	}

	public String getComClass() {
		return Utils.getAttributeValue("com_class", _script);
	}

	public void setComClass(String comClass) {
		setAttributeValue("com_class", comClass, COM);
	}

	public String getFilename() {
		return Utils.getAttributeValue("filename", _script);
	}

	public void setFilename(String filename) {
		setAttributeValue("filename", filename, COM);
	}

	public String[] getIncludes() {
		if (_script != null) {
			List includeList = _script.getChildren("include");
			String[] includes = new String[includeList.size()];
			Iterator it = includeList.iterator();
			int i = 0;
			while (it.hasNext()) {
				Element include = (Element) it.next();
				String file = include.getAttributeValue("file");
				includes[i++] = file == null ? "" : file;
			}
			return includes;
		} else
			return new String[0];
	}

	public void addInclude(String filename) {
		if (_script != null) {
			List includes = _script.getChildren("include");
			_script.addContent(includes.size(), new Element("include")
					.setAttribute("file", filename));
			_dom.setChanged(true);
		} else
			System.out.println("no script element defined!");
	}

	public void removeInclude(int index) {
		if (_script != null) {
			List includeList = _script.getChildren("include");
			if (index >= 0 && index < includeList.size()) {
				includeList.remove(index);
				_dom.setChanged(true);
			} else
				System.out.println("index " + index
						+ " is out of range for include!");
		} else
			System.out.println("no script element defined!");
	}

	public String getSource() {
		if (_script != null) {
			return _script.getTextTrim();
		} else
			return "";
	}

	public void setSource(String source) {

		if (_script != null) {
	//		List mixed = _script.getContent();
	//		Iterator it = mixed.iterator();
			boolean found = false;
			/*			
 
			while (it.hasNext() ) {
				Object o = it.next();
				if (o instanceof CDATA || o instanceof Text) {
					found = true;
					if (source == null || source.equals("")) {
						((Text) o).detach();
				   	break;
					} else {
						((Text) o).setText(source);
					}
				}
			}
*/

      String[] f = getIncludes();
			if (!found && !source.equals("")) {
			 
			  _script.removeContent();
				for (int i=0;i<f.length;i++) {
					addInclude(f[i]);
				}
				_script.addContent(new CDATA(source));
			}

			_dom.setChanged(true);
		} else
			System.out.println("no script element defined!");
	}
}
