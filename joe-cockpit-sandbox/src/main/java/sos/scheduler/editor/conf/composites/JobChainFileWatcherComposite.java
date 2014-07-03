package sos.scheduler.editor.conf.composites;

import static com.sos.joe.globals.messages.SOSJOEMessageCodes.JOE_B_JCNodesForm_RemoveFile;
import static com.sos.joe.globals.messages.SOSJOEMessageCodes.JOE_L_JCNodesForm_DelayAfterError;
import static com.sos.joe.globals.messages.SOSJOEMessageCodes.JOE_L_JCNodesForm_Directory;
import static com.sos.joe.globals.messages.SOSJOEMessageCodes.JOE_L_JCNodesForm_Max;
import static com.sos.joe.globals.messages.SOSJOEMessageCodes.JOE_L_JCNodesForm_MoveTo;
import static com.sos.joe.globals.messages.SOSJOEMessageCodes.JOE_L_JCNodesForm_NextState;
import static com.sos.joe.globals.messages.SOSJOEMessageCodes.JOE_L_JCNodesForm_Regex;
import static com.sos.joe.globals.messages.SOSJOEMessageCodes.JOE_L_JCNodesForm_RemoveFile;
import static com.sos.joe.globals.messages.SOSJOEMessageCodes.JOE_L_JCNodesForm_Repeat;
import static com.sos.joe.globals.messages.SOSJOEMessageCodes.JOE_M_NoRegex;
import static com.sos.joe.globals.messages.SOSJOEMessageCodes.JOE_TCl_JCNodesForm_Directory;
import static com.sos.joe.globals.messages.SOSJOEMessageCodes.JOE_TCl_JCNodesForm_NextState;
import static com.sos.joe.globals.messages.SOSJOEMessageCodes.JOE_TCl_JCNodesForm_Regex;
import static com.sos.joe.globals.messages.SOSJOEMessageCodes.JOE_T_JCNodesForm_DelayAfterError;
import static com.sos.joe.globals.messages.SOSJOEMessageCodes.JOE_T_JCNodesForm_Directory;
import static com.sos.joe.globals.messages.SOSJOEMessageCodes.JOE_T_JCNodesForm_Max;
import static com.sos.joe.globals.messages.SOSJOEMessageCodes.JOE_T_JCNodesForm_MoveTo;
import static com.sos.joe.globals.messages.SOSJOEMessageCodes.JOE_T_JCNodesForm_NextState;
import static com.sos.joe.globals.messages.SOSJOEMessageCodes.JOE_T_JCNodesForm_Regex;
import static com.sos.joe.globals.messages.SOSJOEMessageCodes.JOE_T_JCNodesForm_Repeat;
import static com.sos.joe.globals.messages.SOSJOEMessageCodes.JOE_Tbl_JCNodesForm_FileOrderSource;

import org.apache.log4j.Logger;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;

import sos.scheduler.editor.app.MainWindow;
import sos.scheduler.editor.app.Utils;
import sos.scheduler.editor.classes.CompositeBaseClass;
import sos.scheduler.editor.classes.FolderNameSelector;
import sos.scheduler.editor.classes.ISOSTableMenueListeners;
import sos.scheduler.editor.classes.SOSComboBox;
import sos.scheduler.editor.classes.SOSTable;
import sos.scheduler.editor.conf.composites.CompositeBaseAbstract.enuOperationMode;

import com.sos.joe.globals.messages.ErrorLog;
import com.sos.joe.globals.messages.SOSJOEMessageCodes;
import com.sos.joe.globals.messages.SOSMsgJOE;
import com.sos.joe.objects.jobchain.JobChainListener;

//public class JobChainFileWatcherComposite extends FormBaseClass implements ISOSTableMenueListeners {
public class JobChainFileWatcherComposite extends CompositeBaseClass /* SOSJOEMessageCodes */ implements ISOSTableMenueListeners {
	
	public static final SOSMsgJOE	JOE_G_JCNodesForm_FileOrderSink					= new SOSMsgJOE("JOE_G_JCNodesForm_FileOrderSink");

	@SuppressWarnings("unused")
	private final String		conSVNVersion				= "$Id$";

	private static Logger		logger						= Logger.getLogger(JobChainFileWatcherComposite.class);
	private final String		conClassName				= "JobChainFileWatcherComposite";

	private JobChainListener	objDataProvider				= null;

	private FolderNameSelector	tbxFileOrderSourceDirectory	= null;
	private Text				tDelayAfterError			= null;
	private Text				tMax						= null;
	private SOSComboBox				cbxNextState					= null;
	private Text				tRegex						= null;
	private Text				tRepeat						= null;

	private SOSTable			tFileOrderSource			= null;
	@SuppressWarnings("unused")
	private boolean				flgIsInitActive						= true;


	public JobChainFileWatcherComposite(final Composite parent, final JobChainListener pobjDataProvider) {

		super(parent, SWT.NONE);
		objParent = parent;
		objDataProvider = pobjDataProvider;
		createGroup(parent);
	}

	public JobChainFileWatcherComposite(final Group parent, final int style, final JobChainListener pobjDataProvider) {
		super(parent, style);
		objDataProvider = pobjDataProvider;
		objParent = parent;
		createGroup(parent);
	}

	private void createGroup(final Composite parent) {
		try {
			parent.setRedraw(false);
			Composite objMainControl = new Composite(parent, SWT.NONE);
			final GridLayout gridLayout = new GridLayout();
			gridLayout.numColumns = 1;
			objMainControl.setLayout(gridLayout);
			objMainControl.setLayoutData(new GridData(SWT.TOP, SWT.FILL, true, false));
			createControls(objMainControl);
			init();
		}
		catch (Exception e) {
			new ErrorLog(SOSJOEMessageCodes.JOE_E_0002.params(conClassName, e), e);
		}
		finally {
			parent.setRedraw(true);
			parent.layout();
		}
	}

	Composite	gFileOrderSource	= null;

	@SuppressWarnings("unused")
	private void createControls(final Composite pobjMainControl) {
		pobjMainControl.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		/**
		 * from here: file_order_source
		 */
//		gFileOrderSource = new Group(pobjMainControl, SWT.NONE);
//		gFileOrderSource.setText(JOE_G_JCNodesForm_FileOrderSources.params(objDataProvider.getChainName()));
		gFileOrderSource = getComposite(pobjMainControl, 4);
//		gFileOrderSource = new Composite(pobjMainControl, SWT.NONE);
//		gFileOrderSource.setLayout(new GridLayout(4, false));
//		gFileOrderSource.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));

		/**
		 * tbxFileOrderSourceDirectory
		 */
		final Label directoryLabel = JOE_L_JCNodesForm_Directory.Control(new Label(gFileOrderSource, SWT.NONE));

		tbxFileOrderSourceDirectory = (FolderNameSelector) JOE_T_JCNodesForm_Directory.Control(new FolderNameSelector(gFileOrderSource, gconFieldBorderConstant));
		tbxFileOrderSourceDirectory.setParentForm(this);
		tbxFileOrderSourceDirectory.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1));
		tbxFileOrderSourceDirectory.addModifyListener(objLocalModifyListener);
		tbxFileOrderSourceDirectory.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(final SelectionEvent e) {
			}
		});
		tbxFileOrderSourceDirectory.addKeyListener(objLocalKeyListener);

		createControlRegExp();
		createControlStartNode();
		createDelayAfterControl();

		final Label repeatLabel = JOE_L_JCNodesForm_Repeat.Control(new Label(gFileOrderSource, SWT.NONE));

		tRepeat = JOE_T_JCNodesForm_Repeat.Control(new Text(gFileOrderSource, gconFieldBorderConstant));
		tRepeat.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1));
		tRepeat.addModifyListener(objLocalModifyListener);
		tRepeat.addKeyListener(objLocalKeyListener);

		final Label maxLabel = JOE_L_JCNodesForm_Max.Control(new Label(gFileOrderSource, SWT.NONE));
		tMax = JOE_T_JCNodesForm_Max.Control(new Text(gFileOrderSource, gconFieldBorderConstant));
		tMax.addModifyListener(objLocalModifyListener);
		tMax.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));

		//		Composite cmpTableComposite = new Composite(gFileOrderSource, SWT.None);  // need a separate composite for TableLayout
		final GridData gridData_9 = new GridData(SWT.FILL, SWT.FILL, true, true);

		//		cmpTableComposite.setLayoutData(gridData_9);
		//		cmpTableComposite.setLayout(new GridLayout(4, false));
		//		TableColumnLayout layTableColumnLayout = new TableColumnLayout();
		//		cmpTableComposite.setLayout(layTableColumnLayout);

		tFileOrderSource = (SOSTable) JOE_Tbl_JCNodesForm_FileOrderSource.Control(new SOSTable(gFileOrderSource, SWT.MULTI | SWT.FULL_SELECTION | SWT.BORDER, this));
		tFileOrderSource.initialize();
		tFileOrderSource.setData("caption", "tFileOrderSource");

		final GridData gridData4 = new GridData(SWT.FILL, SWT.FILL, true, true, 4, 1);
		tFileOrderSource.setLayoutData(gridData4);
		tFileOrderSource.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(final SelectionEvent e) {
				if (tFileOrderSource.getSelectionCount() > 0) {
					objDataProvider.objJobChainNodesTable = tFileOrderSource;
					objDataProvider.selectFileOrderSource();
					fillInputFields();
					enableInputFields(true);
				}
			}
		});

		tFileOrderSource.addKeyListener(objLocalKeyListener);

		tFileOrderSource.strTableName = conClassName + "." + "tFileOrderSource";
		final TableColumn tcolDirectoryName = JOE_TCl_JCNodesForm_Directory.Control(tFileOrderSource.newTableColumn("tcolDirectoryName", 300));
		final TableColumn tcolRegEx = JOE_TCl_JCNodesForm_Regex.Control(tFileOrderSource.newTableColumn("tcolRegEx", 200));
		final TableColumn tcolNextState = JOE_TCl_JCNodesForm_NextState.Control(tFileOrderSource.newTableColumn("tcolNextState", 100));
		final TableColumn tcolDelayAfterError = JOE_L_JCNodesForm_DelayAfterError.Control(tFileOrderSource.newTableColumn("tcolNextState", 100));
		final TableColumn tcolRepeat = JOE_L_JCNodesForm_Repeat.Control(tFileOrderSource.newTableColumn("tcolNextState", 100));
		final TableColumn tcolMaxFiles = JOE_L_JCNodesForm_Max.Control(tFileOrderSource.newTableColumn("tcolNextState", 100));

		//		layTableColumnLayout.setColumnData(tcolDirectoryName, new ColumnWeightData(50));
		//		layTableColumnLayout.setColumnData(tcolRegEx, new ColumnWeightData(30));
		//		layTableColumnLayout.setColumnData(tcolNextState, new ColumnWeightData(20));


		createFileSinkControls(pobjMainControl);

	}

//	private boolean flgIsDirty = false;
//
//	@Override
//	protected void setDirty () {
//		if (flgIsInitActive == false			) {
//			flgIsDirty = true;
//		}
//	}
	public void init() {
		flgIsInitActive = true;
		objDataProvider.objJobChainNodesTable = tFileOrderSource;
		objDataProvider.populateTable4FileOrderSource();
		enableInputFields(false);
		flgIsInitActive = false;
	}

	@Override
	protected void applyInputFields(final boolean flgT, final enuOperationMode OperationMode) {
		if (Utils.isValidRegExpression(tRegex.getText())) {
			objDataProvider.applyFileOrderSource(tbxFileOrderSourceDirectory.getText(), tRegex.getText(), cbxNextState.getText(), tMax.getText(),
					tRepeat.getText(), tDelayAfterError.getText());

			objDataProvider.objJobChainNodesTable = tFileOrderSource;
			objDataProvider.populateTable4FileOrderSource();
			// TODO state must be overwritten, or if exists ignore this two statements
			objDataProvider.setFileOrderSink("success", true, "");
			objDataProvider.setFileOrderSink("!error", false, tbxFileSinkMoveFileTo.getText());
			clearInputFields();
			enableInputFields(false);
		}
		else {
			MainWindow.message(JOE_M_NoRegex.params(tRegex.getText()), SWT.ICON_INFORMATION);
		}
	}

	private void fillInputFields() {
		flgIsInitActive = true;
		tbxFileOrderSourceDirectory.setText(objDataProvider.getFileOrderSource("directory"));
		tRegex.setText(objDataProvider.getFileOrderSource("regex"));
		tMax.setText(objDataProvider.getFileOrderSource("max"));
		tDelayAfterError.setText(objDataProvider.getFileOrderSource("delay_after_error"));
		tRepeat.setText(objDataProvider.getFileOrderSource("repeat"));
		cbxNextState.setText(objDataProvider.getFileOrderSource("next_state"));

		tbxFileSinkMoveFileTo.setText(objDataProvider.getMoveTo());
		bFileSinkRemoveFile.setSelection(objDataProvider.getRemoveFile());
		flgIsInitActive = false;
		flgIsDirty = false;
	}

	private void clearInputFields() {
		flgIsInitActive = true;
		tbxFileOrderSourceDirectory.setText("");
		tRegex.setText("");
		tMax.setText("");
		tDelayAfterError.setText("");
		tRepeat.setText("");
		cbxNextState.setText("");

		objDataProvider.clearFileOrderSource();
		tbxFileOrderSourceDirectory.setFocus();
		flgIsInitActive = false;
		flgIsDirty = false;
	}

	private void enableInputFields(final boolean enable) {
		tbxFileOrderSourceDirectory.setEnabled(enable);
		tMax.setEnabled(enable);
		tRepeat.setEnabled(enable);
		tDelayAfterError.setEnabled(enable);
		cbxNextState.setEnabled(enable);
		tRegex.setEnabled(enable);
		tbxFileOrderSourceDirectory.setFocus();

		tbxFileSinkMoveFileTo.setEnabled(enable);
		bFileSinkRemoveFile.setEnabled(enable);
	}

	private void createDelayAfterControl() {

		/**
		 *
		 */
		@SuppressWarnings("unused")
		final Label delay_after_errorLabel = JOE_L_JCNodesForm_DelayAfterError.Control(new Label(gFileOrderSource, SWT.NONE));
		tDelayAfterError = JOE_T_JCNodesForm_DelayAfterError.Control(new Text(gFileOrderSource, gconFieldBorderConstant));
		tDelayAfterError.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
		tDelayAfterError.addModifyListener(objLocalModifyListener);
		tDelayAfterError.addKeyListener(objLocalKeyListener);
	}

	private void createControlRegExp() {
		JOE_L_JCNodesForm_Regex.Control(new Label(gFileOrderSource, SWT.NONE));
		tRegex = JOE_T_JCNodesForm_Regex.Control(new Text(gFileOrderSource, gconFieldBorderConstant));
		tRegex.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
		tRegex.addModifyListener(objLocalModifyListener);
		tRegex.addKeyListener(objLocalKeyListener);
	}

	private void createControlStartNode() { // State / NodeName / step
		@SuppressWarnings("unused")
		final Label stateLabel = JOE_L_JCNodesForm_NextState.Control(new Label(gFileOrderSource, SWT.NONE));

		cbxNextState = new SOSComboBox(gFileOrderSource, JOE_T_JCNodesForm_NextState);
		cbxNextState.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
		cbxNextState.addModifyListener(objLocalModifyListener);
		cbxNextState.addKeyListener(objLocalKeyListener);
		objDataProvider.clearNode();
		if (cbxNextState.getItemCount() <= 0) {
			cbxNextState.setItems(objDataProvider.getStates());
		}

	}

	private Button				bFileSinkRemoveFile		= null;
	private Button				bFileSinkActive		= null;
	private FolderNameSelector	tbxFileSinkMoveFileTo	= null;

	private void createFileSinkControls(final Composite pobjMainControl) {

		Group gFileOrderSink = new Group(pobjMainControl, SWT.NONE);
		final GridData gridData_10 = new GridData(SWT.FILL, SWT.CENTER, true, true);
		gFileOrderSink.setLayoutData(gridData_10);
		gFileOrderSink.setText(JOE_G_JCNodesForm_FileOrderSink.params(objDataProvider.getChainName()));
		final GridLayout gridLayout_1 = new GridLayout();
		gridLayout_1.numColumns = 4;
		gFileOrderSink.setLayout(gridLayout_1);

		JOE_L_JCNodesForm_RemoveFile.Control(new Label(gFileOrderSink, SWT.NONE));
		bFileSinkActive = JOE_B_JCNodesForm_RemoveFile.Control(new Button(gFileOrderSink, SWT.RADIO));
		bFileSinkActive.setText("");
		final GridData gridData_1 = new GridData();
		bFileSinkActive.setLayoutData(gridData_1);
		bFileSinkActive.setEnabled(true);

		new Label(gFileOrderSink, SWT.NONE);
		new Label(gFileOrderSink, SWT.None);

		JOE_L_JCNodesForm_RemoveFile.Control(new Label(gFileOrderSink, SWT.NONE));
		bFileSinkRemoveFile = JOE_B_JCNodesForm_RemoveFile.Control(new Button(gFileOrderSink, SWT.CHECK));
		bFileSinkRemoveFile.setLayoutData(gridData_1);
		bFileSinkRemoveFile.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(final SelectionEvent e) {
				if (bFileSinkRemoveFile.getSelection()) {
					tbxFileSinkMoveFileTo.setText("");
					tbxFileSinkMoveFileTo.setEnabled(true);
				}
			}
		});
		bFileSinkRemoveFile.addKeyListener(objLocalKeyListener);

		bFileSinkRemoveFile.setEnabled(true);

		new Label(gFileOrderSink, SWT.NONE);
		new Label(gFileOrderSink, SWT.None);

		JOE_L_JCNodesForm_MoveTo.Control(new Label(gFileOrderSink, SWT.NONE));
		tbxFileSinkMoveFileTo = (FolderNameSelector) JOE_T_JCNodesForm_MoveTo.Control(new FolderNameSelector(gFileOrderSink, gconFieldBorderConstant));
//		tbxFileSinkMoveFileTo.setDataProvider();
		tbxFileSinkMoveFileTo.setParentForm(this);

		tbxFileSinkMoveFileTo.addModifyListener(new ModifyListener() {
			@Override
			public void modifyText(final ModifyEvent e) {
				if (!tbxFileSinkMoveFileTo.getText().equals(""))
					bFileSinkRemoveFile.setSelection(false);
			}
		});
		tbxFileSinkMoveFileTo.addKeyListener(objLocalKeyListener);
	}

//	@Override
//	protected void checkSubclass() {
//	}

	@Override
	public Listener getNewListener() {
		return new Listener() {
			@Override
			public void handleEvent(final Event e) {
				logger.debug("getNewListener");
				buttonNewNodePressed();
			}
		};
	}

	private void buttonNewNodePressed() {
		tFileOrderSource.deselectAll();
		objDataProvider.clearFileOrderSource();
		clearInputFields();
		enableInputFields(true);
	}

	@Override
	public Listener getDeleteListener() {
		return new Listener() {
			@Override
			public void handleEvent(final Event e) {
				logger.debug("getDeleteListener");
				deleteFOS();
			}
		};
	}

	private void deleteFOS() {

		if (tFileOrderSource.getSelectionCount() > 0) {
			tFileOrderSource.deselectAll();
			for (TableItem tableItem : tFileOrderSource.getSelection()) {
				tFileOrderSource.setSelection(tableItem);
				objDataProvider.deleteNode();
				int index = tFileOrderSource.getSelectionIndex();
				tFileOrderSource.remove(index);
			}
		}

		boolean empty = tFileOrderSource.getItemCount() == 0;

		clearInputFields();
		enableInputFields(!empty);

		objDataProvider.clearFileOrderSource();
		tbxFileSinkMoveFileTo.setEnabled(! empty);
		bFileSinkRemoveFile.setEnabled(! empty);

	}

	@Override
	public Listener getCopyListener() {
		return tFileOrderSource.getDummyListener();
	}

	@Override
	public Listener getPasteListener() {
		return tFileOrderSource.getDummyListener();
	}

	@Override
	public Listener getInsertListener() {
		return tFileOrderSource.getDummyListener();
	}

	@Override
	public Listener getCutListener() {
		return tFileOrderSource.getDummyListener();
	}

	@Override
	public Listener getEditListener() {
		// TODO Auto-generated method stub
		return null;
	}

}
