package com.sos.joe.jobdoc.editor.listeners;
import org.jdom.Element;

import com.sos.joe.xml.jobdoc.DocumentationDom;

public class JobScriptListener extends JobDocBaseListener<DocumentationDom> {
	Element				_job;
	Element				_script;

	public JobScriptListener(DocumentationDom dom, Element job) {
		_dom = dom;
		_job = job;
	}

	public boolean isScript() {
		boolean process = _job.getChild("process", _dom.getNamespace()) != null;
		_script = _job.getChild("script", _dom.getNamespace());
		if (!process && _script == null)
			setScript();
		return _script != null;
	}

	public void setScript() {
		_job.removeChild("process", _dom.getNamespace());
		_script = new Element("script", _dom.getNamespace());
		_job.addContent(_script);
		_dom.setChanged(true);
	}
}
