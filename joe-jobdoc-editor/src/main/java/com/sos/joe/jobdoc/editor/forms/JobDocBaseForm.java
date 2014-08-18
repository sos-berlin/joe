package com.sos.joe.jobdoc.editor.forms;
import java.util.Collection;

import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;

import com.sos.joe.globals.interfaces.IEditor;
import com.sos.joe.globals.interfaces.IUnsaved;
import com.sos.joe.globals.interfaces.IUpdateLanguage;
import com.sos.joe.xml.IOUtils;
import com.sos.joe.xml.Utils;
import com.sos.joe.xml.jobdoc.DocumentationDom;

public abstract class JobDocBaseForm<T>  extends Composite /* SOSJOEMessageCodes */ implements IEditor, IUnsaved, IUpdateLanguage {
	protected T					listener	= null;
	protected DocumentationDom	dom			= null;
	protected Button			bApply		= null;

	public JobDocBaseForm(Composite parent, int style) {
		super(parent, style);
	}

	public T getDataProvider () {
		return listener;
	}

	public DocumentationDom getDom() {
		return dom;
	}


	@Override public void apply() {
		if (isUnsaved())
			applySetting();
	}
 
	@Override public boolean isUnsaved() {
		boolean flgR = false;
		if (bApply != null) {
			flgR = bApply.isEnabled();
		}
		return flgR;
	}

	@Override public void setToolTipText() {
		//
	}
	
	protected abstract void applySetting();
	@Override public abstract boolean applyChanges();
//	@Override public abstract boolean open(Collection files);


	@Override public boolean close() {
		return applyChanges() && IOUtils.continueAnyway(dom);
	}

	@Override public boolean hasChanges() {
		return dom.isChanged();
	}

	@Override public boolean save() {
		boolean res = IOUtils.saveFile(dom, false);
//		if (res)
//			container.setNewFilename(null);
		Utils.setResetElement(dom.getRoot());
		return res;
	}

	@Override public boolean saveAs() {
		String old = dom.getFilename();
		boolean res = IOUtils.saveFile(dom, true);
//		if (res)
//			container.setNewFilename(old);
		return res;
	}

	@Override public String getHelpKey() {
		return null;
	}

	@Override public String getFilename() {
		return dom.getFilename();
	}

	@Override public void updateLanguage() {
		
	}

	@Override public boolean open(Collection files) {
		return false;
	}


}
