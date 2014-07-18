package sos.scheduler.editor.conf.composites;

import static com.sos.joe.globals.messages.SOSJOEMessageCodes.JOE_G_JobMainComposite_MainOptions;
import static com.sos.joe.globals.messages.SOSJOEMessageCodes.JOE_L_JobMainComposite_JobName;
import static com.sos.joe.globals.messages.SOSJOEMessageCodes.JOE_L_JobMainComposite_JobTitle;
import static com.sos.joe.globals.messages.SOSJOEMessageCodes.JOE_T_JobMainComposite_JobName;
import static com.sos.joe.globals.messages.SOSJOEMessageCodes.JOE_T_JobMainComposite_JobTitle;

import org.apache.log4j.Logger;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.VerifyEvent;
import org.eclipse.swt.events.VerifyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

import sos.scheduler.editor.classes.CompositeBaseClass;
import sos.scheduler.editor.classes.ProcessClassSelector;
import sos.scheduler.editor.conf.composites.CompositeBaseAbstract.enuOperationMode;

import com.sos.joe.globals.JOEConstants;
import com.sos.joe.globals.messages.ErrorLog;
import com.sos.joe.objects.job.JobListener;
import com.sos.joe.xml.Utils;

public class JobMainComposite extends CompositeBaseClass {
	@SuppressWarnings("unused")
	private final String conClassName = this.getClass().getSimpleName();
	@SuppressWarnings("unused")
	private static final String conSVNVersion = "$Id$";
	@SuppressWarnings("unused")
	private final Logger logger = Logger.getLogger(this.getClass());
	private final int		intNoOfLabelColumns	= 2;

	private JobListener		objDataProvider		= null;
	private Group			gMain				= null;

	private Text			tbxJobName			= null;
	private Label			lblJobTitlelabel1	= null;
	private Text			tbxJobTitle			= null;
	private Label			label				= null;
//	private final int		intComboBoxStyle	= SWT.NONE;
	private GridLayout		gridLayout			= null;
	private ProcessClassSelector objProcessClassSelector = null;
	
	/**
	 * Create the composite.
	 * @param parent
	 * @param style
	 */
	public JobMainComposite(final Group parent, final int style, final JobListener objDataProvider_) {
		super(parent, style);
		try {
			parent.setRedraw(false);
			objDataProvider = objDataProvider_;
			objParent = parent;
			createGroup(parent);
		}
		catch (Exception e) {
			new ErrorLog(e.getLocalizedMessage(), e);
		}
		finally {
			parent.setRedraw(true);
			parent.layout();
		}
	}

	private void createGroup(final Group parent) {

		gridLayout = new GridLayout();
		gridLayout.marginHeight = 1;
		gridLayout.numColumns = 6;

		gMain = JOE_G_JobMainComposite_MainOptions.Control(new Group(parent, SWT.NONE));
		gMain.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false));
		gMain.setLayout(gridLayout);

		label = JOE_L_JobMainComposite_JobName.Control(new Label(gMain, SWT.NONE));
		label.setLayoutData(new GridData(GridData.BEGINNING, GridData.END, false, false, intNoOfLabelColumns, 1));

		tbxJobName = JOE_T_JobMainComposite_JobName.Control(new Text(gMain, SWT.BORDER));
		tbxJobName.addVerifyListener(new VerifyListener() {
			@Override
			public void verifyText(final VerifyEvent e) {
				if (!init) {
					e.doit = Utils.checkElement(objDataProvider.getJobName(), objDataProvider.get_dom(), JOEConstants.JOB, null);
				}
			}
		});
		tbxJobName.setLayoutData(new GridData(GridData.FILL, GridData.CENTER, true, false, 4, 1));
		tbxJobName.addModifyListener(new ModifyListener() {
			@Override
			public void modifyText(final ModifyEvent e) {
				if (init) {
					return;
				}
				checkName();
				objDataProvider.setJobName(tbxJobName.getText(), true);
				parent.setText(objDataProvider.getJobNameAndTitle());
			}
		});

		lblJobTitlelabel1 = JOE_L_JobMainComposite_JobTitle.Control(new Label(gMain, SWT.NONE));
		lblJobTitlelabel1.setLayoutData(new GridData(GridData.BEGINNING, GridData.CENTER, false, false, intNoOfLabelColumns, 1));

		tbxJobTitle = JOE_T_JobMainComposite_JobTitle.Control(new Text(gMain, SWT.BORDER));
		tbxJobTitle.setLayoutData(new GridData(GridData.FILL, GridData.CENTER, false, false, 4, 1));
		tbxJobTitle.addModifyListener(new ModifyListener() {
			@Override
			public void modifyText(final ModifyEvent e) {
				if (init)
					return;
				objDataProvider.setTitle(tbxJobTitle.getText());
			}
		});
		
		objProcessClassSelector = new ProcessClassSelector (gMain, SWT.NONE, objDataProvider);
		
	}

	public void init() {
		init = true;
		tbxJobName.setText(objDataProvider.getJobName());
		tbxJobTitle.setText(objDataProvider.getTitle());
		objProcessClassSelector.init();
		init = false;
		tbxJobName.setFocus();
	}

	private void checkName() {
		// TODO exist name in model/hotfolder
//		if (Utils.existName(tbxJobName.getText(), objDataProvider.getJob(), "job")) {
//			tbxJobName.setBackground(Display.getCurrent().getSystemColor(SWT.COLOR_YELLOW));
//		}
//		else {
//			tbxJobName.setBackground(null);
//		}
	}

	public Group getgMain() {
		return gMain;
	}

	@Override
	protected void applyInputFields(final boolean flgT, final enuOperationMode OperationMode1) {
	}

}
