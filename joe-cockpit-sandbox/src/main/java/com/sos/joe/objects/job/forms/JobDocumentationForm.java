package com.sos.joe.objects.job.forms;

import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;

import com.sos.joe.globals.misc.TreeData;
import sos.scheduler.editor.conf.container.JobDocumentation;

import com.sos.joe.globals.interfaces.ISchedulerUpdate;
import com.sos.joe.globals.messages.SOSJOEMessageCodes;
import com.sos.joe.objects.job.JobListener;

public class JobDocumentationForm extends SOSJOEMessageCodes /* implements IUpdateLanguage */ {

  private JobListener 	objDataProvider		= null;

// 	public JobDocumentationForm(final Composite parent, final int style, final SchedulerDom dom, final Element job, final ISchedulerUpdate main) {
//		super(parent, style);
// 		dom.setInit(true);
//		this.setEnabled(Utils.isElementEnabled("job", dom, job));
//		objDataProvider = new JobListener(dom, job, main);
//		initialize();
//		dom.setInit(false);
// 	}
//
    public JobDocumentationForm(final Composite parent, final int style, final TreeData pobjTreeData, final ISchedulerUpdate main) {
        super(parent, style);

//        showWaitCursor();

        objDataProvider = new JobListener(pobjTreeData, main);
        initialize();
//        restoreCursor();
    }


	public void apply() {
	}

	public boolean isUnsaved() {
		return false;
	}

	private void initialize() {
		this.setLayout(new FillLayout());
	    new JobDocumentation(this, objDataProvider);
//	    setSize(new org.eclipse.swt.graphics.Point(723, 566));
	}

    public void initForm(){

    }


} // @jve:decl-index=0:visual-constraint="10,10"
