package com.sos.joe.jobdoc.editor.forms;

import java.util.Collection;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;

import com.sos.dialog.classes.SOSComposite;
import com.sos.joe.globals.interfaces.IEditor;
import com.sos.joe.globals.interfaces.IUnsaved;
import com.sos.joe.xml.IOUtils;
import com.sos.joe.xml.Utils;
import com.sos.joe.xml.jobdoc.DocumentationDom;

public abstract class JobDocBaseForm<T> extends SOSComposite implements IEditor, IUnsaved {

    protected T listener = null;
    protected DocumentationDom dom = null;
    protected Button bApply = null;
    protected final modifyTextEvent modifyTextListener = new modifyTextEvent();
    protected Composite objParent = null;

    private class modifyTextEvent implements ModifyListener {

        @Override
        public void modifyText(ModifyEvent e) {
            setApplyStatus();
        }
    }

    protected void setApplyStatus() {

    }

    public JobDocBaseForm(Composite parent, int style) {
        super(parent, style);
        objParent = parent;
        setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
    }

    public T getDataProvider() {
        return listener;
    }

    public DocumentationDom getDom() {
        return dom;
    }

    @Override
    public void apply() {
        if (isUnsaved()) {
            applySetting();
        }
    }

    @Override
    public boolean isUnsaved() {
        boolean flgR = false;
        if (bApply != null) {
            flgR = bApply.isEnabled();
        }
        return flgR;
    }

    protected abstract void applySetting();

    @Override
    public abstract boolean applyChanges();

    @Override
    public boolean close() {
        return applyChanges() && IOUtils.continueAnyway(dom);
    }

    @Override
    public boolean hasChanges() {
        return dom.isChanged();
    }

    @Override
    public boolean save() {
        boolean res = IOUtils.saveFile(dom, false);
        Utils.setResetElement(dom.getRoot());
        return res;
    }

    @Override
    public boolean saveAs() {
        String old = dom.getFilename();
        boolean res = IOUtils.saveFile(dom, true);
        return res;
    }

    @Override
    public String getHelpKey() {
        return null;
    }

    @Override
    public String getFilename() {
        return dom.getFilename();
    }

    @Override
    public boolean open(Collection files) {
        return false;
    }

}
