package sos.scheduler.editor.conf.container;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;

import sos.scheduler.editor.classes.FormBaseClass;
import sos.scheduler.editor.conf.composites.TextArea;
import sos.scheduler.editor.conf.composites.TextArea.enuSourceTypes;
import sos.scheduler.editor.conf.listeners.JobListener;

import com.sos.joe.globals.messages.SOSJOEMessageCodes;
import com.sos.joe.globals.messages.ErrorLog;
import com.sos.joe.globals.messages.Messages;
import com.sos.joe.globals.messages.SOSJOEMessageCodes;
import com.sos.joe.globals.options.Options;

import sos.scheduler.editor.app.Editor;
import sos.scheduler.editor.app.IContainer;
import sos.scheduler.editor.app.MainWindow;

public class JobSourceViewer extends FormBaseClass {

    @SuppressWarnings("unused")
    private final String conSVNVersion = "$Id$";
    private TextArea txtArea4XMLSource = null;

    public JobSourceViewer(Composite pParentComposite, JobListener pobjDataProvider) {
        super(pParentComposite, pobjDataProvider);
        createGroup();
    }

    public void apply() {
        // if (isUnsaved())
        // addParam();
    }

    public boolean isUnsaved() {
        return false;
    }

    public void refreshContent() {
        txtArea4XMLSource.refreshContent();
    }

    private void createGroup() {
        showWaitCursor();
        Group gSourceViewer = SOSJOEMessageCodes.JOE_G_JobSourceViewer_SourceViewer.Control(new Group(objParent, SWT.NONE));
        final GridData gridData_5 = new GridData(GridData.FILL, GridData.FILL, true, true, 13, 1);
        gridData_5.heightHint = 100;
        gridData_5.minimumHeight = 30;
        gSourceViewer.setLayoutData(gridData_5);
        final GridLayout gridLayout_2 = new GridLayout();
        gridLayout_2.marginHeight = 0;
        gridLayout_2.numColumns = 4;
        gSourceViewer.setLayout(gridLayout_2);
        txtArea4XMLSource = new TextArea(gSourceViewer, SWT.V_SCROLL | SWT.MULTI | SWT.BORDER | SWT.H_SCROLL);
        txtArea4XMLSource.setEditable(false);
        txtArea4XMLSource.setDataProvider(objJobDataProvider, enuSourceTypes.xmlSource);
        restoreCursor();
    }
}
