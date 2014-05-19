package sos.scheduler.editor.conf.container;

import org.apache.log4j.Logger;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;

import sos.scheduler.editor.app.Options;
import sos.scheduler.editor.app.SOSJOEMessageCodes;
import sos.scheduler.editor.classes.FormBaseClass;
import sos.scheduler.editor.classes.SOSComboBox;
import sos.scheduler.editor.classes.TextArea;
import sos.scheduler.editor.classes.TextArea.enuSourceTypes;

import com.sos.dialog.classes.WindowsSaver;
import com.sos.joe.objects.job.JobListener;

public class JobScript extends FormBaseClass<JobListener>  {

	private final String		conClassName	= this.getClass().getSimpleName();
	private static final String	conSVNVersion	= "$Id$";
	private final Logger		logger			= Logger.getLogger(this.getClass());
	private Group				group			= null;
	private SOSComboBox			cboPrefunction	= null;
	@SuppressWarnings("unused")
	private boolean				init			= true;
	private StyledText			tSource			= null;

	public JobScript(final Composite pParentComposite, final JobListener pobjJobDataProvider) {
		super(pParentComposite, pobjJobDataProvider);
		objJobDataProvider = pobjJobDataProvider;
		objFormPosSizeHandler = new WindowsSaver(this.getClass(), getShell(), 643, 600);
		objFormPosSizeHandler.setKey(conClassName);
		init = true;
		createGroup();

		logger.debug(conClassName + "\n" + conSVNVersion);
		init = false;
	}

	@Override
	public void createGroup() {
		group = SOSJOEMessageCodes.JOE_G_JobScript_Executable.Control(new Group(objParent, SWT.NONE));
		final GridData gridData_5 = new GridData(GridData.FILL, GridData.FILL, true, true, 13, 20);
		gridData_5.heightHint = 500;
		gridData_5.minimumHeight = 100;
		group.setLayoutData(gridData_5);
		final GridLayout gridLayout_2 = new GridLayout();
		gridLayout_2.marginHeight = 0;
		gridLayout_2.numColumns = 4;
		group.setLayout(gridLayout_2);

		@SuppressWarnings("unused")
		Label lblPrefunction1 = SOSJOEMessageCodes.JOE_L_JobScript_PredefinedFunctions.Control(new Label(group, SWT.NONE));

		cboPrefunction = new SOSComboBox(group, SOSJOEMessageCodes.JOE_Cbo_JobScript_PredefinedFunctions);
		cboPrefunction.setVisibleItemCount(7);
		cboPrefunction.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(final SelectionEvent e) {
				if (cboPrefunction.getText().length() > 0) {
					String lan = "function_" + objJobDataProvider.getLanguageDescriptor().getLanguageName() + "_";
					lan = lan.replaceAll(":", "_");
					String sourceTemplate = Options.getProperty(lan.toLowerCase() + cboPrefunction.getText());
					if (sourceTemplate != null) {
						tSource.append(Options.getProperty(lan.toLowerCase() + cboPrefunction.getText()));
						cboPrefunction.setText("");
						tSource.setFocus();
					}
					else {
						tSource.append(String.format("Template '%1$s' not found. check customizing", sourceTemplate));
					}
				}
			}
		});
		cboPrefunction.setLayoutData(new GridData(GridData.FILL, GridData.CENTER, true, false));

		final TextArea txtPrePostProcessingScriptCode = new TextArea(group, SWT.V_SCROLL | SWT.MULTI | SWT.BORDER | SWT.H_SCROLL);
		txtPrePostProcessingScriptCode.setDataProvider(objJobDataProvider, enuSourceTypes.ScriptSource);
		txtPrePostProcessingScriptCode.setFormHandler(objFormPosSizeHandler);
		tSource = txtPrePostProcessingScriptCode.getControl();

		init = false;

		objParent.layout();
	}

	public StyledText gettSource() {
		return tSource;
	}

	public SOSComboBox getCboPrefunction() {
		return cboPrefunction;
	}

}
