package sos.scheduler.editor.conf.composites;

import static sos.scheduler.editor.app.SOSJOEMessageCodes.JOE_Cbo_JCNodesForm_Job;
import static sos.scheduler.editor.app.SOSJOEMessageCodes.JOE_Cbo_JCNodesForm_OnError;
import static sos.scheduler.editor.app.SOSJOEMessageCodes.JOE_Cbo_JobChainNodes_ErrorState;
import static sos.scheduler.editor.app.SOSJOEMessageCodes.JOE_Cbo_JobChainNodes_NextState;
import static sos.scheduler.editor.app.SOSJOEMessageCodes.JOE_E_0002;
import static sos.scheduler.editor.app.SOSJOEMessageCodes.JOE_L_JCNodesForm_Delay;
import static sos.scheduler.editor.app.SOSJOEMessageCodes.JOE_L_JCNodesForm_Job;
import static sos.scheduler.editor.app.SOSJOEMessageCodes.JOE_L_JCNodesForm_OnError;
import static sos.scheduler.editor.app.SOSJOEMessageCodes.JOE_L_JobChainNodes_ErrorState;
import static sos.scheduler.editor.app.SOSJOEMessageCodes.JOE_L_JobChainNodes_NextState;
import static sos.scheduler.editor.app.SOSJOEMessageCodes.JOE_L_JobChainNodes_State;
import static sos.scheduler.editor.app.SOSJOEMessageCodes.JOE_M_JCNodesForm_Setback;
import static sos.scheduler.editor.app.SOSJOEMessageCodes.JOE_M_JCNodesForm_Suspend;
import static sos.scheduler.editor.app.SOSJOEMessageCodes.JOE_M_JobChain_StateAlreadyDefined;
import static sos.scheduler.editor.app.SOSJOEMessageCodes.JOE_T_JCNodesForm_Delay;
import static sos.scheduler.editor.app.SOSJOEMessageCodes.JOE_T_JobChainNodes_State;

import org.apache.log4j.Logger;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

import sos.scheduler.editor.app.ContextMenu;
import sos.scheduler.editor.app.Editor;
import sos.scheduler.editor.app.ErrorLog;
import sos.scheduler.editor.app.MainWindow;
import sos.scheduler.editor.classes.SOSComboBox;
import sos.scheduler.editor.conf.listeners.JobChainListener;
import sos.util.SOSClassUtil;

import com.sos.dialog.interfaces.ICompositeBaseAbstract;

public class JobChainNodeComposite extends CompositeBaseAbstract<JobChainListener> implements ICompositeBaseAbstract {
	@SuppressWarnings("unused")
	private final String		conClassName		= this.getClass().getSimpleName();

	@SuppressWarnings("unused")
	private static final String	conSVNVersion		= "$Id$";
	@SuppressWarnings("unused")
	private final Logger		logger				= Logger.getLogger(this.getClass());

	private SOSComboBox			cboErrorState		= null;
	private SOSComboBox			cboNextState		= null;
	private SOSComboBox			cboJob				= null;
	private Text				tbxState			= null;
	private Text				tDelay				= null;
	private boolean				refresh				= false;
	private SOSComboBox			cboOnError			= null;

	private boolean				flgInsertNewNode	= false;

	public JobChainNodeComposite(final Composite parent, final JobChainListener pobjDataProvider, final CompositeBaseAbstract.enuOperationMode enuMode) {
		super(parent, pobjDataProvider, enuMode);
	}

	public JobChainNodeComposite(final JobChainListener pobjDataProvider, final CompositeBaseAbstract.enuOperationMode enuMode) {
		super(pobjDataProvider, enuMode);
	}

	@Override
	public void createGroup(final Composite parent) {

		try {
			parent.addKeyListener(new KeyListener() {
				@Override
				public void keyReleased(final KeyEvent e) {
					if ((e.stateMask & SWT.CTRL) == SWT.CTRL && e.keyCode == 'u') {
						System.out.println("Key up !!");
					}
				}

				@Override
				public void keyPressed(final KeyEvent e) {
					if ((e.stateMask & SWT.CTRL) == SWT.CTRL && e.keyCode == 'd') {
						System.out.println("Key down !!");
					}
				}
			});

			Group composite = new Group(parent, SWT.NONE);
			composite.setLayout(new GridLayout());
			composite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
			composite.addDisposeListener(new DisposeListener() {
				@Override
				public void widgetDisposed(final DisposeEvent e) {
					//					if (butApply.isEnabled()) {
					//						save();
					//					}
				}
			});

			Composite grpJobChainStates1 = new Composite(composite, SWT.NONE);
			final GridLayout gridLayout_3 = new GridLayout();
			gridLayout_3.numColumns = 6;
			gridLayout_3.makeColumnsEqualWidth = true;
			grpJobChainStates1.setLayout(gridLayout_3);
			GridData gd_gNodes = new GridData(SWT.FILL, SWT.FILL, true, true, 6, 6);
			grpJobChainStates1.setLayoutData(gd_gNodes);

			createControl4JobName(grpJobChainStates1);

			JOE_L_JobChainNodes_State.Control(new Label(grpJobChainStates1, SWT.NONE));
			tbxState = JOE_T_JobChainNodes_State.Control(new Text(grpJobChainStates1, gconFieldBorderConstant));
			final GridData gridData18 = new GridData(SWT.FILL, SWT.FILL, false, false, 2, 1);
			tbxState.setLayoutData(gridData18);
			tbxState.addModifyListener(new ModifyListener() {
				@Override
				public void modifyText(final ModifyEvent e) {
					boolean valid = objDataProvider.isValidState(tbxState.getText());
					if (!valid) {
						tbxState.setBackground(objDataProvider.getColor4InvalidValues());
					}
					else {
						tbxState.setBackground(null);
						setDirty();
					}
				}
			});

			tbxState.addKeyListener(objLocalKeyListener);
			tbxState.addKeyListener(new KeyAdapter() {
				@Override
				public void keyPressed(final KeyEvent e) {
					if (e.keyCode == SWT.F8) {
						applyAutomaticNode();
					}
				}
			});

			JOE_L_JobChainNodes_NextState.Control(new Label(grpJobChainStates1, SWT.NONE));
			cboNextState = new SOSComboBox(grpJobChainStates1, JOE_Cbo_JobChainNodes_NextState);
			cboNextState.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false, 2, 1));
			cboNextState.addModifyListener(objLocalModifyListener);
			cboNextState.addKeyListener(objLocalKeyListener);
			{
				JOE_L_JobChainNodes_ErrorState.Control(new Label(grpJobChainStates1, SWT.NONE));
				cboErrorState = new SOSComboBox(grpJobChainStates1, JOE_Cbo_JobChainNodes_ErrorState);
				cboErrorState.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false, 2, 1));
				cboErrorState.addModifyListener(objLocalModifyListener);
				cboErrorState.addKeyListener(objLocalKeyListener);
			}

			{
				JOE_L_JCNodesForm_Delay.Control(new Label(grpJobChainStates1, SWT.NONE));
				tDelay = JOE_T_JCNodesForm_Delay.Control(new Text(grpJobChainStates1, gconFieldBorderConstant));
				tDelay.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1));
				tDelay.addModifyListener(objLocalModifyListener);
				tDelay.addKeyListener(objLocalKeyListener);
			}

			new Label(grpJobChainStates1, SWT.NONE);

			{
				JOE_L_JCNodesForm_OnError.Control(new Label(grpJobChainStates1, SWT.NONE));
				cboOnError = new SOSComboBox(grpJobChainStates1, JOE_Cbo_JCNodesForm_OnError);
				// TODO remove I18N
				cboOnError.setItems(new String[] { "", JOE_M_JCNodesForm_Setback.label(), JOE_M_JCNodesForm_Suspend.label() });
				cboOnError.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 2, 1));
				cboOnError.addModifyListener(objLocalModifyListener);
				cboOnError.addKeyListener(objLocalKeyListener);
			}

			composite.layout();
			parent.layout();
		}
		catch (Exception e) {
			new ErrorLog(e.getLocalizedMessage(), e);
		}
		finally {
		}
	}

	@Override
	public void init() {
		init = true;
		flgInsertNewNode = false;
		switch (OperationMode) {
			case New:
				clearNodeFields();
				changeState4NodeFields(true);
				setCursorToFirstField();
				break;

			case Insert:
				flgInsertNewNode = true;
				clearNodeFields();
				changeState4NodeFields(true);
				setCursorToFirstField();
				break;

			case Edit:
				clearNodeFields();
				populateNodeFields();
				changeState4NodeFields(true);
				setCursorToFirstField();
				break;

			case Delete:
				clearNodeFields();
				populateNodeFields();
				changeState4NodeFields(false);
				break;

			case Browse:
				clearNodeFields();
				populateNodeFields();
				changeState4NodeFields(false);
				setCursorToFirstField();
				break;

			default:
				break;
		}

		init = false;
	}

	private void createControl4JobName(final Composite pobjParent) {
		JOE_L_JCNodesForm_Job.Control(new Label(pobjParent, SWT.NONE));
		cboJob = new SOSComboBox(pobjParent, JOE_Cbo_JCNodesForm_Job);
		cboJob.setVisibleItemCount(9);
		cboJob.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false, 2, 1));
		ContextMenu objM = new ContextMenu(cboJob, objDataProvider.get_dom(), Editor.JOB);
		if (objM != null) {
			cboJob.setMenu(objM.getMenu());
		}
		cboJob.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDoubleClick(final MouseEvent arg0) {
				ContextMenu.goTo(cboJob.getText(), objDataProvider.get_dom(), Editor.JOB);
				objParent.getShell().close();
			}

			@Override
			public void mouseDown(final MouseEvent e) {
				if (refresh) {
					if (objDataProvider.getJobs() != null) {
						cboJob.setItems(objDataProvider.getJobs());
						refresh = false;
					}
				}
			}
		});
		cboJob.addModifyListener(objLocalModifyListener);
		cboJob.addKeyListener(objLocalKeyListener);
	}

	int	intStepIncr	= 100;

	private void applyAutomaticNode() {
		String strState = "step" + intStepIncr;
		tbxState.setText(strState);
		intStepIncr += 100;
		cboNextState.setText("step" + intStepIncr);
		cboErrorState.setText("!" + strState);
		applyInputFields(false);
	}

	private void changeState4NodeFields(final boolean pflgEnabledState) {

		tbxState.setEnabled(pflgEnabledState);
		cboJob.setEnabled(pflgEnabledState);
		cboNextState.setEnabled(pflgEnabledState);
		cboErrorState.setEnabled(pflgEnabledState);
		cboOnError.setEnabled(pflgEnabledState);
		tDelay.setEnabled(pflgEnabledState);
	}

	private void clearNodeFields() {

		try {
			cboJob.setText("");
			cboNextState.setText("");
			cboErrorState.setText("");
			cboOnError.setText("");
			tbxState.setText("");
			tDelay.setText("");

			//		if (OperationMode == New) {  // hier völlig falsch
			//		objDataProvider.clearNode();
			//		}

			cboJob.setItems(objDataProvider.getJobs());
			cboNextState.setItems(objDataProvider.getStates());
//			cboErrorState.setItems(objDataProvider.getAllStates());

		}
		catch (Exception e) {

		}
		finally {
			changeState4NodeFields(true);
			setCursorToFirstField();
			flgIsDirty = false;
		}
	}

	private void setCursorToFirstField() {
		cboJob.setFocus();
	}

	private void populateNodeFields() {

		init = true;
		if (cboJob.getItemCount() <= 0) {
			cboJob.setItems(objDataProvider.getJobs());
		}

		cboJob.setText(objDataProvider.getJob());
		tbxState.setText(objDataProvider.getState());
		tDelay.setText(objDataProvider.getDelay());
		String strNextState = objDataProvider.getNextState();
		cboNextState.setText(strNextState);
		cboErrorState.setText(objDataProvider.getErrorState());
		cboOnError.setText(objDataProvider.getOnError());
		changeState4NodeFields(true);
		init = false;
		flgIsDirty = false;
	}

	@Override
	protected void applyInputFields(final boolean flgFromCopyStore) {
		try {
			//			cboNextState.setVisibleItemCount(20);
			// TODO use class JobChainNodesWrapper
			String strState = tbxState.getText();
			String strJob = cboJob.getText();
			String strDelay = tDelay.getText();
			String strNextState = cboNextState.getText();
			String strErrorState = cboErrorState.getText();
			String strOnError = cboOnError.getText();

			if (flgFromCopyStore == true) {
				//				strState = "copy_" + ++intCopyCnt + "_" + objCopyNodeStore.strState; //  tbxState.getText();
				//				strJob = objCopyNodeStore.strJob;
				//				strDelay = objCopyNodeStore.strDelay;
				//				strNextState = objCopyNodeStore.strNextState;
				//				strErrorState = objCopyNodeStore.strErrorState;
				//				strOnError = objCopyNodeStore.strOnError;
			}

			String msg = "";
			if (flgFromCopyStore == false) {
				if (!objDataProvider.isValidState(strState)) {
					msg = JOE_M_JobChain_StateAlreadyDefined.label();
				}
			}

			if (!msg.equals("")) {
				MainWindow.message(msg, SWT.ICON_INFORMATION);
			}
			else {
				if (flgInsertNewNode == true) {
					objDataProvider.applyInsertNode(true, strState, strJob, strDelay, strNextState, strErrorState, false, "", strOnError);
				}
				else {
					objDataProvider.applyNode(true, strState, strJob, strDelay, strNextState, strErrorState, false, "", strOnError);
				}
				//				DetailsListener.checkGlobalJobChainParameter(strState, objDataProvider.getChainName(), strJob, objDataProvider.get_dom(), update);
				objDataProvider.populateNodesTable();
				clearNodeFields();
			}
			flgInsertNewNode = false;
		}
		catch (Exception e) {
			new ErrorLog(JOE_E_0002.params(SOSClassUtil.getMethodName()), e);
		}
	}

	@Override
	public String getWindowTitle() {
		String strS = "Title is " + OperationMode;
		// TODO Auto-generated method stub
		return strS;
	}
}
