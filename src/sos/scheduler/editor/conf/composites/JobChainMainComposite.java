package sos.scheduler.editor.conf.composites;

import static sos.scheduler.editor.app.SOSJOEMessageCodes.JOE_L_JobChainForm_ChainName;
import static sos.scheduler.editor.app.SOSJOEMessageCodes.JOE_L_JobChainForm_Title;
import static sos.scheduler.editor.app.SOSJOEMessageCodes.JOE_M_JobChainForm_JobChain;
import static sos.scheduler.editor.app.SOSJOEMessageCodes.JOE_T_JobChainForm_ChainName;
import static sos.scheduler.editor.app.SOSJOEMessageCodes.JOE_T_JobChainForm_Title;

import org.apache.log4j.Logger;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.VerifyEvent;
import org.eclipse.swt.events.VerifyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

import sos.scheduler.editor.app.Editor;
import sos.scheduler.editor.app.Utils;
import sos.scheduler.editor.classes.CompositeBaseClass;
import sos.scheduler.editor.conf.listeners.JobChainListener;

public class JobChainMainComposite extends CompositeBaseClass {
	@SuppressWarnings("unused")
	private final String		conSVNVersion		= "$Id$";

//	private final int			intNoOfLabelColumns	= 2;
	@SuppressWarnings("unused")
	private static Logger		logger				= Logger.getLogger(JobChainMainComposite.class);
	@SuppressWarnings("unused")
	private final String		conClassName		= "JobChainMainComposite";

	private JobChainListener	objDataProvider		= null;
	private final Group			gMain				= null;

	private Text			tbxName				= null;
	private Text				tbxTitle			= null;

	private Group				jobChainGroup;

	/**
	 * Create the composite.
	 * @param parent
	 * @param style
	 */
	public JobChainMainComposite(final Composite parent, final JobChainListener pobjDataProvider) {
		super(parent, SWT.NONE | SWT.RESIZE);
		objDataProvider = pobjDataProvider;
		objParent = parent;
		init = true;
		createGroup(parent);
		init();
	}

	public JobChainMainComposite(final Group parent, final int style, final JobChainListener pobjDataProvider) {
		super(parent, style);
		objDataProvider = pobjDataProvider;
		objParent = parent;
		init= true;
		createGroup(parent);
		init();
	}

	private void createGroup(final Composite parent) {
		jobChainGroup = new Group(parent, SWT.NONE);

		final GridLayout gridLayout = new GridLayout();
		gridLayout.numColumns = 6;
		jobChainGroup.setLayout(gridLayout);
		jobChainGroup.setLayoutData(new GridData(GridData.FILL, GridData.FILL, true, false));

		Label chainNameLabel = JOE_L_JobChainForm_ChainName.Control(new Label(jobChainGroup, SWT.NONE));
		chainNameLabel.setLayoutData(new GridData(GridData.BEGINNING, GridData.BEGINNING, false, false));

		tbxName = JOE_T_JobChainForm_ChainName.Control(new Text(jobChainGroup, SWT.BORDER));
		tbxName.addVerifyListener(new VerifyListener() {
			@Override
			public void verifyText(final VerifyEvent e) {
				if (!init) {
					e.doit = Utils.checkElement(objDataProvider.getChainName(), objDataProvider.get_dom(), Editor.JOB_CHAIN, null);
					/*System.out.println(e.doit);
					if(e.doit) {
						init = true;
						name = name.substring(0, e.start) + e.text + name.substring(e.start,  e.end);
						tName.setText(name);
						objDataProvider.setChainName(name);
						init = false;
					}
					*/
				}
			}
		});

		final GridData gridData_4 = new GridData(GridData.FILL, GridData.BEGINNING, true, false, 2, 1);

		tbxName.setLayoutData(gridData_4);
		tbxName.setText(objDataProvider.getChainName());
		tbxName.setBackground(objDataProvider.getColor4Background());
		tbxName.addModifyListener(new org.eclipse.swt.events.ModifyListener() {
			@Override
			public void modifyText(final ModifyEvent e) {
				if (init) {
					return;
				}
				String newName = tbxName.getText().trim();
				boolean existname = Utils.existName(newName, objDataProvider.getChain(), "job_chain");
				if (existname)
					tbxName.setBackground(objDataProvider.getColor4InvalidValues());
				else {
					tbxName.setBackground(null);
				}
				// TODO use a callback to update the Tree or something else
				//                if (update != null)
				////                  update.updateTreeItem("Job Chain: " + newName);
				//                	update.updateTreeItem(JOE_M_JobChainForm_JobChain.params(newName));
				objDataProvider.setChainName(newName);
				String strJobChainName = objDataProvider.getChainName();
				jobChainGroup.setText(JOE_M_JobChainForm_JobChain.params(strJobChainName));
//				boolean changeJobChainName = true;
			}
		});

		@SuppressWarnings("unused")
		final Label titleLabel = JOE_L_JobChainForm_Title.Control(new Label(jobChainGroup, SWT.NONE));

		tbxTitle = JOE_T_JobChainForm_Title.Control(new Text(jobChainGroup, SWT.BORDER));
		tbxTitle.setText(objDataProvider.getTitle());
		tbxTitle.addModifyListener(new ModifyListener() {
			@Override
			public void modifyText(final ModifyEvent e) {
				if (init)
					return;
				objDataProvider.setTitle(tbxTitle.getText());
			}
		});
		tbxTitle.setLayoutData(gridData_4);
	}

	public void init() {
		init = true;
		tbxName.setText(objDataProvider.getJobChainName());
		tbxTitle.setText(objDataProvider.getTitle());
		tbxName.setFocus();
		init = false;
	}

	public Group getgMain() {
		return gMain;
	}

	@Override
	protected void applyInputFields(final boolean flgT) {
		// TODO Auto-generated method stub

	}
}
