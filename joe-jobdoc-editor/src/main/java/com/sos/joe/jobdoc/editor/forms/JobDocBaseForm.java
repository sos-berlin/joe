package com.sos.joe.jobdoc.editor.forms;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;

import com.sos.joe.globals.interfaces.IUnsaved;
import com.sos.joe.globals.interfaces.IUpdateLanguage;
import com.sos.joe.globals.messages.SOSJOEMessageCodes;
import com.sos.joe.xml.jobdoc.DocumentationDom;

public abstract class JobDocBaseForm<T> extends SOSJOEMessageCodes implements IUnsaved, IUpdateLanguage {
	protected T					listener	= null;
	protected DocumentationDom	dom			= null;
	protected Button			bApply		= null;

	public JobDocBaseForm(Composite parent, int style) {
		super(parent, style);
	}

	protected abstract void applySetting();

	public void apply() {
		if (isUnsaved())
			applySetting();
	}

	public boolean isUnsaved() {
		return bApply.isEnabled();
	}

	public void setToolTipText() {
		//
	}
}
