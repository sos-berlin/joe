package sos.scheduler.editor.conf.container;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;

import sos.scheduler.editor.app.Editor;
import sos.scheduler.editor.app.Messages;
import sos.scheduler.editor.app.Options;
import sos.scheduler.editor.classes.FormBaseClass;
import sos.scheduler.editor.classes.TextArea;
import sos.scheduler.editor.classes.TextArea.enuSourceTypes;
import sos.scheduler.editor.conf.listeners.JobListener;

public class JobScript extends FormBaseClass {

    @SuppressWarnings("unused")
    private final String conClassName       = "JobScript";
    @SuppressWarnings("unused")
    private final String conSVNVersion      = "$Id$";
    private Group        group              = null;
    private Combo        cboPrefunction     = null;
    private boolean      init               = true;
    private JobListener  objJobDataProvider = null;
    private StyledText   tSource            = null;

    public JobScript(Composite pParentComposite, JobListener pobjJobDataProvider) {
        super(pParentComposite, pobjJobDataProvider);
        objJobDataProvider = pobjJobDataProvider;
        init = true;
        createGroup();
        init = false;
    }

    private void createGroup() {
        group = new Group(objParent, SWT.NONE);
        final GridData gridData_5 = new GridData(GridData.FILL, GridData.FILL, true, true, 13, 20);

        gridData_5.heightHint = 500;
        gridData_5.minimumHeight = 100;
        group.setLayoutData(gridData_5);
        final GridLayout gridLayout_2 = new GridLayout();
        gridLayout_2.marginHeight = 0;
        gridLayout_2.numColumns = 4;
        group.setLayout(gridLayout_2);
        group.setText(Messages.getLabel("job.executable.label"));

        Label lblPrefunction1 = new Label(group, SWT.NONE);
        lblPrefunction1.setText(Messages.getLabel("job.selectpredefinedfunctions"));
        cboPrefunction = new Combo(group, intComboBoxStyle);
        cboPrefunction.setVisibleItemCount(7);

        cboPrefunction.addSelectionListener(new SelectionAdapter() {
            public void widgetSelected(final SelectionEvent e) {
                if (cboPrefunction.getText().length() > 0) {
                    String lan = "function_" + objJobDataProvider.getLanguage(objJobDataProvider.getLanguage()) + "_";
                    String sourceTemplate = Options.getProperty(lan.toLowerCase() + cboPrefunction.getText());
                    if (sourceTemplate != null) {
                        tSource.append(Options.getProperty(lan.toLowerCase() + cboPrefunction.getText()));
                        cboPrefunction.setText("");
                        tSource.setFocus();
                    }
                }
            }
        });
        cboPrefunction.setLayoutData(new GridData(GridData.FILL, GridData.CENTER, true, false));
        final TextArea txtPrePostProcessingScriptCode = new TextArea(group, SWT.V_SCROLL | SWT.MULTI | SWT.BORDER | SWT.H_SCROLL);
        txtPrePostProcessingScriptCode.setDataProvider(objJobDataProvider, enuSourceTypes.ScriptSource);
        tSource = txtPrePostProcessingScriptCode.getControl();

        init = false;

        objParent.layout();
    }

    public StyledText gettSource() {
        return tSource;
    }

    public Combo getCboPrefunction() {
        return cboPrefunction;
    }

}
