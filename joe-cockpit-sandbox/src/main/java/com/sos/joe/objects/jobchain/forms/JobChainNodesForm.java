package com.sos.joe.objects.jobchain.forms;
import static com.sos.joe.globals.messages.SOSJOEMessageCodes.JOE_E_0002;
import static com.sos.joe.globals.messages.SOSJOEMessageCodes.JOE_L_JCNodesForm_MoveTo;
import static com.sos.joe.globals.messages.SOSJOEMessageCodes.JOE_L_JCNodesForm_RemoveFile;
import static com.sos.joe.globals.messages.SOSJOEMessageCodes.JOE_M_JCNodesForm_Remove;
import static com.sos.joe.globals.messages.SOSJOEMessageCodes.JOE_M_ScriptForm_ItemIndex;
import static com.sos.joe.globals.messages.SOSJOEMessageCodes.JOE_TCl_JCNodesForm_ErrorState;
import static com.sos.joe.globals.messages.SOSJOEMessageCodes.JOE_TCl_JCNodesForm_JobDir;
import static com.sos.joe.globals.messages.SOSJOEMessageCodes.JOE_TCl_JCNodesForm_NextState;
import static com.sos.joe.globals.messages.SOSJOEMessageCodes.JOE_TCl_JCNodesForm_Node;
import static com.sos.joe.globals.messages.SOSJOEMessageCodes.JOE_TCl_JCNodesForm_OnError;
import static com.sos.joe.globals.messages.SOSJOEMessageCodes.JOE_TCl_JCNodesForm_State;
import static com.sos.joe.globals.messages.SOSJOEMessageCodes.JOE_TI_ScriptJobMainForm_Doc;
import static com.sos.joe.globals.messages.SOSJOEMessageCodes.JOE_TI_ScriptJobMainForm_FileWatcher;
import static com.sos.joe.globals.messages.SOSJOEMessageCodes.JOE_TI_ScriptJobMainForm_Options;
import static com.sos.joe.globals.messages.SOSJOEMessageCodes.JOE_TI_ScriptJobMainForm_XML;
import static com.sos.joe.globals.messages.SOSJOEMessageCodes.JOE_Tbl_JCNodesForm_Nodes;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

import org.apache.log4j.Logger;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.ScrollBar;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.jdom.Element;

import sos.scheduler.editor.app.MainWindow;
import sos.scheduler.editor.app.TreeData;
import sos.scheduler.editor.classes.CompositeBaseClass;
import sos.scheduler.editor.classes.FolderNameSelector;
import sos.scheduler.editor.classes.ISOSTableMenueListeners;
import sos.scheduler.editor.classes.SOSTabItemCreator;
import sos.scheduler.editor.classes.SOSTable;
import sos.scheduler.editor.conf.composites.CompositeBaseAbstract;
import sos.scheduler.editor.conf.composites.CompositeBaseAbstract.enuOperationMode;
import sos.scheduler.editor.conf.composites.JobChainFileSinkComposite;
import sos.scheduler.editor.conf.composites.JobChainFileWatcherComposite;
import sos.scheduler.editor.conf.composites.JobChainMainComposite;
import sos.scheduler.editor.conf.composites.JobChainNodeComposite;
import sos.scheduler.editor.conf.composites.JobChainOptionsComposite;
import sos.scheduler.editor.conf.composites.JobChainParameterComposite;
import sos.scheduler.editor.conf.composites.JobChainParameterNodesComposite;
import sos.scheduler.editor.conf.container.JobScript;
import sos.scheduler.editor.conf.container.JobSourceViewer;
import sos.util.SOSClassUtil;

import com.sos.dialog.classes.DialogAdapter;
import com.sos.joe.globals.JOEConstants;
import com.sos.joe.globals.messages.ErrorLog;
import com.sos.joe.globals.interfaces.ISchedulerUpdate;
import com.sos.joe.interfaces.IUpdateLanguage;
import com.sos.joe.objects.jobchain.JobChainListener;
import com.sos.joe.objects.jobchain.JobChainNodeWrapper;
import com.sos.scheduler.model.objects.JSObjJobChain;

public class JobChainNodesForm extends CompositeBaseClass /* SOSJOEMessageCodes */implements IUpdateLanguage, ISOSTableMenueListeners {
	private final String		conClassName			= this.getClass().getSimpleName();
	private final Logger		logger					= Logger.getLogger(this.getClass());
	/**
	 * TODO Rename via Menu-Function and special form
	 * TODO Edit Cell Content directly
	 * TODO Function to Create a zip file with chain, jobs, orders and (scheduler.log, order log, task log, ...)
	 * TODO Job-Title in die Tabelle übernehmen
	 * TODO Parameter (global/Nodes) als Tab einbauen
	 */
	@SuppressWarnings("unused")
	private final String		conSVNVersion			= "$Id$";
	private SOSTable			tblJobChainStates		= null;
	private JobChainListener	objDataProvider			= null;
	private ISchedulerUpdate	update					= null;
	private boolean				flgInsertFileSink		= false;
	private boolean				flgDoReorderStates		= false;
	private boolean				checkParameter			= false;
	private final Element				objJobChainDOMElement	= null;
	class HelpKeyListener extends KeyAdapter {
		@Override
		public void keyPressed(final KeyEvent e) {
			if (e.keyCode == SWT.F1) {
				logger.debug(conClassName);
			}
		}
	}
	private final HelpKeyListener	objHelpKeyListener	= new HelpKeyListener();
	private JSObjJobChain			objJSJobChain			= null;

	public JobChainNodesForm(final Composite parent, final int style, final TreeData pobjTreeData) {
		super(parent, style);
		objJSJobChain = (JSObjJobChain) pobjTreeData.getObject();
				objDataProvider = new JobChainListener(objJSJobChain);
		objDataProvider.setTreeData(pobjTreeData);
		objJSJobChain.setInit(true);
		initialize();
		setToolTipText();
		//		InitializeAllFormControls(false, false);
		objJSJobChain.setInit(false);
	}

//	@Deprecated
//	public JobChainNodesForm(final Composite objParentComposite, final int style, final SchedulerDom dom_, final Element jobChain) {
//		super(objParentComposite, style);
//		objParent = objParentComposite;
//		objDataProvider = new JobChainListener(dom_, jobChain);
//		objJobChainDOMElement = jobChain;
//		initialize();
//	}

	private void initialize() {
		try {
			objParent.setRedraw(false);
			logger.debug(conClassName);
			createGroup();
			boolean existChainNodes = objDataProvider.hasNodesOrChains();
			if (existChainNodes) {
				populateNodesTable(false, false);
			}
// TODO unclear			objParent.setEnabled(objDataProvider.getJSObject().isEnabled());
		}
		catch (Exception e) {
			new ErrorLog(JOE_E_0002.params(conClassName), e);
		}
		finally {
			objParent.setRedraw(true);
			objParent.layout(true, true);
		}
	}
	private CTabFolder				tabFolder								= null;
	private Composite				tabItemDocumentationComposite			= null;
	private Composite				tabItemFileWatcherComposite				= null;
	private Composite				tabItemOptionsComposite					= null;
	private Composite				tabItemJobChainParameterComposite		= null;
	private CTabItem				tabItemJobChainParameter				= null;
	private Composite				tabItemDiagramViewerComposite			= null;
	private Composite				tabItemJobChainNodeParameterComposite	= null;
	private CTabItem				tabItemJobChainNodeParameter			= null;
	private Composite				tabItemSourceViewerComposite			= null;
	private CTabItem				tabItemDiagramViewer					= null;
	private CTabItem				tabItemSourceViewer						= null;
	private CTabItem				tabItemDocumentation					= null;
	private CTabItem				tabItemOptions							= null;
	private CTabItem				tabItemFileWatcher						= null;
	//	private Group					objMainOptionsGroup					= null;
	private Composite				objMainOptionsComposite					= null;
	private JobChainMainComposite	jobMainComposite						= null;
	private Composite				tabItemNodesComposite					= null;
	private CTabItem				tabItemNodes							= null;
	protected JobScript				objJobScript							= null;

	protected void createGroup() {
		GridLayout gridLayoutMainOptionsGroup = new GridLayout();
		gridLayoutMainOptionsGroup.numColumns = 1;
		gridLayoutMainOptionsGroup.marginHeight = 0;
		gridLayoutMainOptionsGroup.marginWidth = 0;
		gridLayoutMainOptionsGroup.horizontalSpacing = 2;
		gridLayoutMainOptionsGroup.verticalSpacing = 3;
		objMainOptionsComposite = new Composite(objParent, SWT.None | SWT.RESIZE);
		objMainOptionsComposite.addKeyListener(objHelpKeyListener);
		setStatusLine(String.format("JobChain '%1$s' selected", objDataProvider.getChainName()));
		objMainOptionsComposite.setLayout(gridLayoutMainOptionsGroup);
		objMainOptionsComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
		jobMainComposite = new JobChainMainComposite(objMainOptionsComposite, objDataProvider);
		createScriptTabForm(objMainOptionsComposite);
	}

	private void createOptionsTab(final Composite pParentComposite) {
		if (pParentComposite == null) {
			return;
		}
		disposeChilds(pParentComposite);
		new JobChainOptionsComposite(pParentComposite, objDataProvider);
		pParentComposite.layout();
	}

	private void createFileWatcherTab(final Composite pParentComposite) {
		if (pParentComposite == null) {
			return;
		}
		disposeChilds(pParentComposite);
		new JobChainFileWatcherComposite(pParentComposite, objDataProvider);
		pParentComposite.layout();
	}

	private void createJobChainParameterTab(final Composite pParentComposite) {
		if (pParentComposite == null) {
			return;
		}
		disposeChilds(pParentComposite);
		new JobChainParameterComposite(pParentComposite, objDataProvider, JOEConstants.JOB_CHAIN);
		pParentComposite.layout();
	}

	private void createJobChainNodesParameterContent(final Composite pParentComposite) {
		if (pParentComposite == null) {
			return;
		}
		disposeChilds(pParentComposite);
		new JobChainParameterNodesComposite(pParentComposite, objDataProvider);
		pParentComposite.layout();
	}

	private void createDiagramViewer(final Composite pParentComposite) {
		if (pParentComposite == null) {
			return;
		}
		disposeChilds(pParentComposite);
		pParentComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		Image originalImage = null;
		try {
			String strDiagramFileName = objDataProvider.getDiagramFileName();
			originalImage = new Image(objParent.getDisplay(), new FileInputStream(strDiagramFileName));
		}
		catch (FileNotFoundException e1) {
			new ErrorLog(e1.getLocalizedMessage(), e1);
		}
		final Image image = originalImage;
		final Point origin = new Point(0, 0);
		final Canvas objCanvas = new Canvas(pParentComposite, SWT.NO_BACKGROUND | SWT.NO_REDRAW_RESIZE | SWT.V_SCROLL | SWT.H_SCROLL);
		objCanvas.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		final ScrollBar hBar = objCanvas.getHorizontalBar();
		hBar.addListener(SWT.Selection, new Listener() {
			@Override
			public void handleEvent(final Event e) {
				int hSelection = hBar.getSelection();
				int destX = -hSelection - origin.x;
				Rectangle rect = image.getBounds();
				objCanvas.scroll(destX, 0, 0, 0, rect.width, rect.height, false);
				origin.x = -hSelection;
			}
		});
		final ScrollBar vBar = objCanvas.getVerticalBar();
		vBar.addListener(SWT.Selection, new Listener() {
			@Override
			public void handleEvent(final Event e) {
				int vSelection = vBar.getSelection();
				int destY = -vSelection - origin.y;
				Rectangle rect = image.getBounds();
				objCanvas.scroll(0, destY, 0, 0, rect.width, rect.height, false);
				origin.y = -vSelection;
			}
		});
		objCanvas.addListener(SWT.Resize, new Listener() {
			@Override
			public void handleEvent(final Event e) {
				Rectangle rect = image.getBounds();
				Rectangle client = objCanvas.getClientArea();
				hBar.setMaximum(rect.width);
				vBar.setMaximum(rect.height);
				hBar.setThumb(Math.min(rect.width, client.width));
				vBar.setThumb(Math.min(rect.height, client.height));
				int hPage = rect.width - client.width;
				int vPage = rect.height - client.height;
				int hSelection = hBar.getSelection();
				int vSelection = vBar.getSelection();
				if (hSelection >= hPage) {
					if (hPage <= 0)
						hSelection = 0;
					origin.x = -hSelection;
				}
				if (vSelection >= vPage) {
					if (vPage <= 0)
						vSelection = 0;
					origin.y = -vSelection;
				}
				objCanvas.redraw();
			}
		});
		objCanvas.addListener(SWT.Paint, new Listener() {
			@Override
			public void handleEvent(final Event e) {
				GC gc = e.gc;
				gc.drawImage(image, origin.x, origin.y);
				Rectangle rect = image.getBounds();
				Rectangle client = objCanvas.getClientArea();
				int marginWidth = client.width - rect.width;
				if (marginWidth > 0) {
					gc.fillRectangle(rect.width, 0, marginWidth, client.height);
				}
				int marginHeight = client.height - rect.height;
				if (marginHeight > 0) {
					gc.fillRectangle(0, rect.height, client.width, marginHeight);
				}
			}
		});
		pParentComposite.layout();
	}

	private void createJobChainTabPages() {
		SOSTabItemCreator objTcr = new SOSTabItemCreator(tabFolder);
		tabItemNodes = objTcr.getItem(JOE_JobChain_TabItemNodes, 2);
		tabItemNodesComposite = objTcr.getComposite();
		tabItemOptions = objTcr.getItem(JOE_TI_ScriptJobMainForm_Options, 3);
		tabItemOptionsComposite = objTcr.getComposite();
		tabItemFileWatcher = objTcr.getItem(JOE_TI_ScriptJobMainForm_FileWatcher, 4);
		tabItemFileWatcherComposite = objTcr.getComposite();
		tabItemDocumentation = objTcr.getItem(JOE_TI_ScriptJobMainForm_Doc, 5);
		tabItemDocumentationComposite = objTcr.getComposite();
		tabItemDiagramViewer = objTcr.getItem(JOE_TI_DiagramViewer, 6);
		tabItemDiagramViewerComposite = objTcr.getComposite();
		tabItemSourceViewer = objTcr.getItem(JOE_TI_ScriptJobMainForm_XML, 7);
		tabItemSourceViewerComposite = objTcr.getComposite();
		tabItemJobChainParameter = objTcr.getItem(JOE_TI_JobChainParameter, 8);
		tabItemJobChainParameterComposite = objTcr.getComposite();
		tabItemJobChainNodeParameter = objTcr.getItem(JOE_TI_JobChainNodeParameter, 9);
		tabItemJobChainNodeParameterComposite = objTcr.getComposite();
		createGroup2();
		createOptionsTab(tabItemOptionsComposite);
		createDocumentationContent(tabItemDocumentationComposite);
	}

	private void createSourceViewerContent(final Composite pParentComposite) {
		if (pParentComposite == null) {
			return;
		}
		disposeChilds(pParentComposite);
		new JobSourceViewer(pParentComposite, objDataProvider);
		pParentComposite.layout();
	}

	private void disposeChilds(final Composite pParentComposite) {
		for (Control objCtrl : pParentComposite.getChildren()) {
			objCtrl.dispose();
		}
	}

	private void createDocumentationContent(final Composite pParentComposite) {
		if (pParentComposite == null) {
			return;
		}
		disposeChilds(pParentComposite);
		//		new JobDocumentation(pParentComposite, objDataProvider);
		pParentComposite.layout();
	}

	protected void createScriptTabForm(final Composite pobjParentGroup) {
		tabFolder = new CTabFolder(pobjParentGroup, SWT.None | SWT.Resize | SWT.V_SCROLL | SWT.H_SCROLL); // SWT.Bottom
		GridLayout objTabFolderGridLayout = new GridLayout();
		objTabFolderGridLayout.marginBottom = 0;
		objTabFolderGridLayout.marginLeft = 0;
		objTabFolderGridLayout.marginRight = 0;
		objTabFolderGridLayout.marginTop = 0;
		objTabFolderGridLayout.marginHeight = 0;
		objTabFolderGridLayout.horizontalSpacing = 3;
		objTabFolderGridLayout.verticalSpacing = 3;
		tabFolder.setBorderVisible(false);
		tabFolder.setLayout(objTabFolderGridLayout);
		tabFolder.setSimple(false);
		tabFolder.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		tabFolder.addSelectionListener(new SelectionListener() {
			@Override
			public void widgetSelected(final SelectionEvent e) {
				int intIndex = tabFolder.getSelectionIndex();
				// TODO use FormsHelper
				Options.setLastTabItemIndex(intIndex);
				logger.debug(JOE_M_ScriptForm_ItemIndex.params(tabFolder.getSelectionIndex()));
				CTabItem objSelectedTab = tabFolder.getItem(intIndex);
				int intTabKey = (Integer) objSelectedTab.getData("TabKeyIndex");
				switch (intTabKey) {
					case 4: //
						createFileWatcherTab(tabItemFileWatcherComposite);
						break;
					case 6:
						createDiagramViewer(tabItemDiagramViewerComposite);
						break;
					case 7: //XML
						createSourceViewerContent(tabItemSourceViewerComposite);
						break;
					case 8: // JobChainParameter
						createJobChainParameterTab(tabItemJobChainParameterComposite);
						break;
					case 9: // JobChainNodeParameter
						createJobChainNodesParameterContent(tabItemJobChainNodeParameterComposite);
						break;
					default:
						break;
				}
			}

			@Override
			public void widgetDefaultSelected(final SelectionEvent e) {
				widgetSelected(e);
			}
		});
		tabFolder.addSelectionListener(new SelectionListener() {
			@Override
			public void widgetSelected(final SelectionEvent e) {
				int index = tabFolder.getSelectionIndex();
				//				logger.debug("Selected item index = " + index);
				//				logger.debug("Selected item = " + (tabFolder.getSelection() == null ? "null" : tabFolder.getItem(index).getText()));
			}

			@Override
			public void widgetDefaultSelected(final SelectionEvent e) {
				widgetSelected(e);
			}
		});
		createJobChainTabPages();
		tabFolder.setSelection(0);
	}
	private Composite					grpJobChainStates1		= null;
	private final Button				bFileSinkRemoveFile		= null;
	private final Button				bFileSinkActive			= null;
	private final FolderNameSelector	tbxFileSinkMoveFileTo	= null;

	//	private Group				gFileOrderSink			= null;
	//	private void createFileSinkControls(final Composite pobjMainControl) {
	//
	//		gFileOrderSink = new Group(pobjMainControl, SWT.NONE);
	//		final GridData gridData_10 = new GridData(SWT.FILL, SWT.FILL, true, false);
	//		gFileOrderSink.setLayoutData(gridData_10);
	//		gFileOrderSink.setText(JOE_G_JCNodesForm_FileOrderSink.params(objDataProvider.getChainName()));
	//		final GridLayout gridLayout_1 = new GridLayout();
	//		gridLayout_1.numColumns = 4;
	//		gFileOrderSink.setLayout(gridLayout_1);
	//
	//		JOE_L_JCNodesForm_RemoveFile.Control(new Label(gFileOrderSink, SWT.NONE));
	//		bFileSinkActive = JOE_B_JCNodesForm_RemoveFile.Control(new Button(gFileOrderSink, SWT.RADIO));
	//		bFileSinkActive.setText("");
	//		final GridData gridData_1 = new GridData();
	//		bFileSinkActive.setLayoutData(gridData_1);
	//		bFileSinkActive.setEnabled(true);
	//
	//		new Label(gFileOrderSink, SWT.NONE);
	//		new Label(gFileOrderSink, SWT.None);
	//
	//		JOE_L_JCNodesForm_RemoveFile.Control(new Label(gFileOrderSink, SWT.NONE));
	//		bFileSinkRemoveFile = JOE_B_JCNodesForm_RemoveFile.Control(new Button(gFileOrderSink, SWT.CHECK));
	//		bFileSinkRemoveFile.setLayoutData(gridData_1);
	//		bFileSinkRemoveFile.addSelectionListener(new SelectionAdapter() {
	//			@Override
	//			public void widgetSelected(final SelectionEvent e) {
	//				if (bFileSinkRemoveFile.getSelection()) {
	//					tbxFileSinkMoveFileTo.setText("");
	//					tbxFileSinkMoveFileTo.setEnabled(true);
	//				}
	//			}
	//		});
	//		bFileSinkRemoveFile.addKeyListener(objLocalKeyListener);
	//
	//		bFileSinkRemoveFile.setEnabled(true);
	//
	//		//		new Label(gFileOrderSink, SWT.NONE);
	//		//		new Label(gFileOrderSink, SWT.None);
	//
	//		JOE_L_JCNodesForm_MoveTo.Control(new Label(gFileOrderSink, SWT.NONE));
	//		tbxFileSinkMoveFileTo = (FolderNameSelector) JOE_T_JCNodesForm_MoveTo.Control(new FolderNameSelector(gFileOrderSink, gconFieldBorderConstant));
	//		tbxFileSinkMoveFileTo.setParentForm(this);
	//
	//		tbxFileSinkMoveFileTo.addModifyListener(new ModifyListener() {
	//			@Override
	//			public void modifyText(final ModifyEvent e) {
	//				if (!tbxFileSinkMoveFileTo.getText().equals(""))
	//					bFileSinkRemoveFile.setSelection(false);
	//			}
	//		});
	//		tbxFileSinkMoveFileTo.addKeyListener(objLocalKeyListener);
	//	}
	//
	@SuppressWarnings("unused")
	private void createGroup2() {
		try {
			grpJobChainStates1 = new Composite(tabItemNodesComposite, SWT.NONE);
			final GridLayout gridLayout_3 = new GridLayout();
			gridLayout_3.numColumns = 6;
			gridLayout_3.makeColumnsEqualWidth = true;
			grpJobChainStates1.setLayout(gridLayout_3);
			GridData gd_gNodes = new GridData(SWT.FILL, SWT.FILL, true, true, 6, 6);
			grpJobChainStates1.setLayoutData(gd_gNodes);
			//			createControl4JobName(grpJobChainStates1);
			//
			//			label6 = JOE_L_JobChainNodes_State.Control(new Label(grpJobChainStates1, SWT.NONE));
			//			label6.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false));
			//
			//			tbxState = JOE_T_JobChainNodes_State.Control(new Text(grpJobChainStates1, gconFieldBorderConstant));
			//			final GridData gridData18 = new GridData(SWT.FILL, SWT.FILL, false, false, 2, 1);
			//			tbxState.setLayoutData(gridData18);
			//			tbxState.addModifyListener(new ModifyListener() {
			//				@Override
			//				public void modifyText(final ModifyEvent e) {
			//					boolean valid = objDataProvider.isValidState(tbxState.getText());
			//					if (!valid) {
			//						tbxState.setBackground(objDataProvider.getColor4InvalidValues());
			//					}
			//					else {
			//						tbxState.setBackground(null);
			//						setDirty();
			//					}
			//				}
			//			});
			//			tbxState.addKeyListener(objLocalKeyListener);
			//			tbxState.addKeyListener(new KeyAdapter() {
			//				@Override
			//				public void keyPressed(final KeyEvent e) {
			//					if (e.keyCode == SWT.F8) {
			//						applyAutomaticNode();
			//					}
			//				}
			//			});
			//
			//			JOE_L_JobChainNodes_NextState.Control(new Label(grpJobChainStates1, SWT.NONE));
			//
			//			cboNextState = new SOSComboBox(grpJobChainStates1, JOE_Cbo_JobChainNodes_NextState);
			//			cboNextState.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false, 2, 1));
			//			cboNextState.addModifyListener(objLocalModifyListener);
			//			cboNextState.addKeyListener(objLocalKeyListener);
			//
			//			JOE_L_JobChainNodes_ErrorState.Control(new Label(grpJobChainStates1, SWT.NONE));
			//			cboErrorState = new SOSComboBox(grpJobChainStates1, JOE_Cbo_JobChainNodes_ErrorState);
			//			cboErrorState.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false, 2, 1));
			//			cboErrorState.addModifyListener(objLocalModifyListener);
			//			cboErrorState.addKeyListener(objLocalKeyListener);
			//
			//			final Label delayLabel = JOE_L_JCNodesForm_Delay.Control(new Label(grpJobChainStates1, SWT.NONE));
			//			tDelay = JOE_T_JCNodesForm_Delay.Control(new Text(grpJobChainStates1, gconFieldBorderConstant));
			//			tDelay.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1));
			//			tDelay.addModifyListener(objLocalModifyListener);
			//			tDelay.addKeyListener(objLocalKeyListener);
			//
			//			new Label(grpJobChainStates1, SWT.NONE);
			//
			//			final Label onErrorLabel = JOE_L_JCNodesForm_OnError.Control(new Label(grpJobChainStates1, SWT.NONE));
			//			cboOnError = new SOSComboBox(grpJobChainStates1, JOE_Cbo_JCNodesForm_OnError);
			//			// TODO remove I18N
			//			cboOnError.setItems(new String[] { "", JOE_M_JCNodesForm_Setback.label(), JOE_M_JCNodesForm_Suspend.label() });
			//			cboOnError.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 2, 1));
			//			cboOnError.addModifyListener(objLocalModifyListener);
			//			cboOnError.addKeyListener(objLocalKeyListener);
			/**
			 * Table with all nodes/states/steps
			 */
			tblJobChainStates = JOE_Tbl_JCNodesForm_Nodes.Control(new SOSTable(grpJobChainStates1, SWT.MULTI | SWT.FULL_SELECTION | SWT.BORDER | SWT.RESIZE,
					this));
			tblJobChainStates.initialize();
			tblJobChainStates.setData("caption", "tblJobChainStates");
			tblJobChainStates.addSelectionListener(new SelectionAdapter() {
				@Override
				public void widgetSelected(final SelectionEvent e) {
					selectNodes();
				}
			});
			final GridData gridData4 = new GridData(SWT.FILL, SWT.FILL, true, true, 6, 6);
			tblJobChainStates.setLayoutData(gridData4);
			tblJobChainStates.strTableName = conClassName + "." + "tblJobchainStates";
			final TableColumn tcolStateName = JOE_TCl_JCNodesForm_State.Control(tblJobChainStates.newTableColumn("tcolStateName", 90));
			final TableColumn tcolStateType = JOE_TCl_JCNodesForm_Node.Control(tblJobChainStates.newTableColumn("tcolStateType", 100));
			final TableColumn tcolJobName = JOE_TCl_JCNodesForm_JobDir.Control(tblJobChainStates.newTableColumn("tcolJobName", 200));
			final TableColumn tcolNextStateName = JOE_TCl_JCNodesForm_NextState.Control(tblJobChainStates.newTableColumn("tcolNextStateName", 90));
			final TableColumn tcolErrorStateName = JOE_TCl_JCNodesForm_ErrorState.Control(tblJobChainStates.newTableColumn("tcolErrorStateName", 90));
			final TableColumn tcolOnErrorDirective = JOE_TCl_JCNodesForm_OnError.Control(tblJobChainStates.newTableColumn("tcolOnErrorDirective", 100));
			final TableColumn tcolDelay = JOE_TCl_JCNodesForm_Delay.Control(tblJobChainStates.newTableColumn("tcolDelay", 100));
			final TableColumn tcolHasParams = JOE_TCl_JCNodesForm_HasParams.Control(tblJobChainStates.newTableColumn("tcolHasParams", 100));
			final TableColumn tcolRemoveFile = JOE_L_JCNodesForm_RemoveFile.Control(tblJobChainStates.newTableColumn("tcolRemoveFile", 100));
			final TableColumn tcolMoveFileTo = JOE_L_JCNodesForm_MoveTo.Control(tblJobChainStates.newTableColumn("tcolMoveFileTo", 250));
			Menu objContextMenuJobChainStates = tblJobChainStates.objContextMenu;
			MenuItem itemUp = new MenuItem(objContextMenuJobChainStates, SWT.PUSH);
			tblJobChainStates.setMenuItemText(itemUp, "Up", "F5", SWT.F5, false);
			itemUp.addListener(SWT.Selection, getMoveNodeUpListener());
			MenuItem itemDown = new MenuItem(objContextMenuJobChainStates, SWT.PUSH);
			tblJobChainStates.setMenuItemText(itemDown, "Down", "F6", SWT.F6, false);
			itemDown.addListener(SWT.Selection, getMoveNodeDownListener());
			MenuItem itemDoReorderStates = new MenuItem(objContextMenuJobChainStates, SWT.CHECK);
			itemDoReorderStates.setText("Reorder");
			itemDoReorderStates.setSelection(flgDoReorderStates);
			itemDoReorderStates.addListener(SWT.Selection, getDoReorderStatesListener());
			tblJobChainStates.addMenueSeparator();
			MenuItem itemJobChainParameter = new MenuItem(objContextMenuJobChainStates, SWT.PUSH);
			itemJobChainParameter.setText("JobChain Parameter");
			itemJobChainParameter.addListener(SWT.Selection, getJobChainParameterListener());
			MenuItem itemNodeParameter = new MenuItem(objContextMenuJobChainStates, SWT.PUSH);
			itemNodeParameter.setText("Node Parameter");
			itemNodeParameter.addListener(SWT.Selection, getNodeParameterListener());
			tblJobChainStates.addMenueSeparator();
			MenuItem itemInsertFileSink = new MenuItem(objContextMenuJobChainStates, SWT.PUSH);
			itemInsertFileSink.setText("insert FileSink");
			itemInsertFileSink.addListener(SWT.Selection, getInsertFileSinkListener());
			MenuItem itemCompleteNodes = new MenuItem(objContextMenuJobChainStates, SWT.PUSH);
			itemCompleteNodes.setText("complete Nodes");
			itemCompleteNodes.addListener(SWT.Selection, getCompleteNodesListener());
			tblJobChainStates.addMenueSeparator();
			MenuItem itemCreateMissingJobs = new MenuItem(objContextMenuJobChainStates, SWT.PUSH);
			itemCreateMissingJobs.setText("Create missing Jobs");
			itemCreateMissingJobs.addListener(SWT.Selection, getCreateMissingJobsListener());
			tblJobChainStates.addKeyListener(new KeyAdapter() {
				@Override
				public void keyPressed(final KeyEvent e) {
					if (e.keyCode == (SWT.MOD1 | 'I')) {
						insertNode();
					}
					if (e.keyCode == SWT.INSERT) {
						insertNode();
					}
					if (e.keyCode == SWT.F5) {
						MoveNodeUp();
					}
					if (e.keyCode == SWT.F6) {
						MoveNodeDown();
					}
					if (e.keyCode == SWT.DEL) {
						deleteNode();
					}
				}
			});
			tblJobChainStates.Restore();
			objDataProvider.objJobChainNodesTable = tblJobChainStates;
			//			createFileSinkControls(tabItemNodesComposite);
			//			gFileOrderSink.setVisible(false);
		}
		catch (Exception e) {
			new ErrorLog(JOE_E_0002.params(SOSClassUtil.getMethodName()), e);
		}
	}

	private void populateNodesTable(final boolean enable, final boolean isNew) {
		objDataProvider.populateNodesTable(tblJobChainStates);
	}
	int					intStepIncr		= 100;
	private final int	intCopyCnt		= 0;
	//	private void applyAutomaticNode() {
	//	}
	//
	int					intDialogStyle	= SWT.DIALOG_TRIM | SWT.APPLICATION_MODAL | SWT.RESIZE;

	private void buttonNewNodePressed() {
		tblJobChainStates.deselectAll();
		showJobChainNodeComposite(CompositeBaseAbstract.enuOperationMode.New);
	}

	private void showJobChainNodeComposite(final CompositeBaseAbstract.enuOperationMode enuMode) {
		DialogAdapter objDA = new DialogAdapter(new Shell(MainWindow.getSShell()), intDialogStyle);
		JobChainNodeComposite objNodeC = new JobChainNodeComposite(objDataProvider, enuMode);
		// TODO callback for new and edit: Interface IEditCallback
		//		objDA.setEditCallback(this);
		objDA.open(objNodeC);
	}

	private void showJobChainFileSinkComposite(final CompositeBaseAbstract.enuOperationMode enuMode) {
		DialogAdapter objDA = new DialogAdapter(new Shell(MainWindow.getSShell()), intDialogStyle);
		JobChainFileSinkComposite objNodeC = new JobChainFileSinkComposite(objDataProvider, enuMode);
		objDA.open(objNodeC);
	}

	public void setISchedulerUpdate(final ISchedulerUpdate update_) {
		update = update_;
		objDataProvider.setISchedulerUpdate(update_);
	}

	private void editJobChainOrNodeParameter(final String state, final String jobname) {
		boolean isLifeFolderElement = objDataProvider.isLiveFolderElement();
		try {
			DetailDialogForm detail = null;
			if (state == null) {
				detail = new DetailDialogForm(objDataProvider.getChainName(), isLifeFolderElement, objDataProvider.get_dom().getFilename());
			}
			else {
				detail = new DetailDialogForm(objDataProvider.getChainName(), state, null, isLifeFolderElement, objDataProvider.get_dom().getFilename());
			}
			detail.showDetails();
			detail.getDialogForm().setParamsForWizzard(objDataProvider.get_dom(), update, jobname);
		}
		catch (Exception e) {
			new ErrorLog(JOE_E_0002.params(conClassName, e));
		}
	}

	private void selectNodes() {
		if (tblJobChainStates.getSelectionCount() > 0) {
			objDataProvider.selectNode();
		}
	}

	@Override
	// Interface routine
	public void setToolTipText() {
		//
	}

	private void MoveNodeUp() {
//	TODO	if (tblJobChainStates.getSelectionCount() > 0) {
//			int index = tblJobChainStates.getSelectionIndex();
//			if (index > 0) {
//				String strState = objDataProvider.changeNodeSequence(true, true, index, flgDoReorderStates);
//				if (strState.isEmpty() == false) {
//					tblJobChainStates.setSelection(index - 1);
//					selectNodes();
//					MainWindow.setStatusLine(String.format("moved Node '%1$s' one line up", strState));
//				}
//			}
//		}
	}

	private void MoveNodeDown() {
//	TODO	if (tblJobChainStates.getSelectionCount() > 0) {
//			int index = tblJobChainStates.getSelectionIndex();
//			if (index == tblJobChainStates.getItemCount() - 1) {
//				// System.out.println("Datensatz ist bereits ganz unten.");
//			}
//			else
//				if (index >= 0) {
//					String strState = objDataProvider.changeNodeSequence(false, true, index, flgDoReorderStates);
//					if (strState.isEmpty() == false) {
//						tblJobChainStates.setSelection(index + 1);
//						selectNodes();
//						MainWindow.setStatusLine(String.format("moved Node '%1$s' one line down", strState));
//					}
//				}
//		}
	}

	private Listener getMoveNodeUpListener() {
		return new Listener() {
			@Override
			public void handleEvent(final Event e) {
				logger.debug("MoveNodeUpListener");
				MoveNodeUp();
			}
		};
	}

	private Listener getMoveNodeDownListener() {
		return new Listener() {
			@Override
			public void handleEvent(final Event e) {
				logger.debug("MoveNodeDownListener");
				MoveNodeDown();
			}
		};
	}

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

	@Override
	public Listener getDeleteListener() {
		return new Listener() {
			@Override
			public void handleEvent(final Event e) {
				logger.debug("getDeleteListener");
				deleteNode();
			}
		};
	}

	private void deleteNode() {
		if (tblJobChainStates.getSelectionCount() > 0) {
			int c = MainWindow.message(getShell(), JOE_M_JCNodesForm_Remove.label(), SWT.ICON_QUESTION | SWT.YES | SWT.NO);
			if (c != SWT.YES) {
				return;
			}
			TableItem[] objSelectedItems = tblJobChainStates.getSelection();
			tblJobChainStates.deselectAll();
			for (TableItem tableItem : objSelectedItems) {
				tblJobChainStates.setSelection(tableItem);
				objDataProvider.deleteNode();
				int index = tblJobChainStates.getSelectionIndex();
				tblJobChainStates.remove(index);
			}
			//			boolean empty = tblJobChainStates.getItemCount() == 0;
		}
	}

	@Override
	public Listener getCopyListener() {
		return new Listener() {
			@Override
			public void handleEvent(final Event e) {
				logger.debug("getCopyListener");
				copyNode();
			}
		};
	}
	class NodeStore {
		public String	strState		= "";
		public String	strJob			= "";
		public String	strDelay		= "";
		public String	strNextState	= "";
		public String	strErrorState	= "";
		public String	strOnError		= "";
	}
	// TODO use class JobChainNodesWrapper
	private final NodeStore	objCopyNodeStore	= null;

	private void copyNode() {
		//		objCopyNodeStore = new NodeStore();
		//		objCopyNodeStore.strState = tbxState.getText();
		//		objCopyNodeStore.strJob = cboJob.getText();
		//		objCopyNodeStore.strDelay = tDelay.getText();
		//		objCopyNodeStore.strNextState = cboNextState.getText();
		//		objCopyNodeStore.strErrorState = cboErrorState.getText();
		//		objCopyNodeStore.strOnError = cboOnError.getText();
	}

	@Override
	public Listener getPasteListener() {
		return new Listener() {
			@Override
			public void handleEvent(final Event e) {
				logger.debug("getPasteListener");
				pasteNode();
			}
		};
	}

	private void pasteNode() {
		if (objCopyNodeStore != null) {
			//			flgInsertNewNode = true;
			applyInputFields(true, enuOperationMode.Insert);
			//			flgInsertNewNode = false;
		}
	}

	public Listener getNodeParameterListener() {
		return new Listener() {
			@Override
			public void handleEvent(final Event e) {
				logger.debug("getNodeParameterListener");
				MaintainNodeParameter();
			}
		};
	}

	private void MaintainNodeParameter() {
		int intSelectionIndex = tblJobChainStates.getSelectionIndex();
		if (intSelectionIndex > 0) {
			JobChainNodeWrapper objN = (JobChainNodeWrapper) tblJobChainStates.getItem(intSelectionIndex).getData();
			editJobChainOrNodeParameter(objN.getState(), objN.getJobName());
			checkParameter = true;
		}
	}

	@Override
	public Listener getInsertListener() {
		return new Listener() {
			@Override
			public void handleEvent(final Event e) {
				logger.debug("getInsertListener");
				insertNode();
			}
		};
	}

	private void insertNode() {
		showJobChainNodeComposite(CompositeBaseAbstract.enuOperationMode.Insert);
		//		flgInsertNewNode = true;
		//		String state = tbxState.getText();
		//		tbxState.setText("");
		//		tDelay.setText("");
		//		cboErrorState.setText("");
		//		cboOnError.setText("");
		//		cboJob.setText("");
		//		changeState4NodeFields(true);
		//		cboNextState.setText(state);
		//		setCursorToFirstField();
		flgIsDirty = false;
	}

	@Override
	public Listener getEditListener() {
		return new Listener() {
			@Override
			public void handleEvent(final Event e) {
				logger.debug("getEditListener");
				editNode();
			}
		};
	}

	private void editNode() {
		try {
			objDataProvider.selectNode();
			showJobChainNodeComposite(CompositeBaseAbstract.enuOperationMode.Edit);
		}
		catch (Exception e) {
			new ErrorLog(JOE_E_0002.params(conClassName, e));
		}
		finally {
			flgIsDirty = false;
		}
	}

	private Listener getInsertFileSinkListener() {
		return new Listener() {
			@Override
			public void handleEvent(final Event e) {
				logger.debug("getInsertFileSinkListener");
				insertFileSink();
			}
		};
	}

	private void insertFileSink() {
		//		flgInsertNewNode = true;
		flgInsertFileSink = true;
		showJobChainFileSinkComposite(CompositeBaseAbstract.enuOperationMode.New);
		//		gFileOrderSink.setVisible(true);
		//		String state = tbxState.getText();
		//		tbxState.setText("");
		//		tDelay.setText("");
		//		cboErrorState.setText("");
		//		cboOnError.setText("");
		//		cboJob.setText("");
		//		changeState4NodeFields(true);
		//		cboNextState.setText(state);
		//		setCursorToFirstField();
		flgIsDirty = false;
	}

	private Listener getCreateMissingJobsListener() {
		return new Listener() {
			@Override
			public void handleEvent(final Event e) {
				logger.debug("getCreateMissingJobsListener");
				objDataProvider.createMissingJobs();
			}
		};
	}

	private Listener getCompleteNodesListener() {
		return new Listener() {
			@Override
			public void handleEvent(final Event e) {
				logger.debug("getCompleteNodesListener");
				completeNodes();
			}
		};
	}

	private void completeNodes() {
		try {
			objDataProvider.completeNodes();
		}
		catch (Exception ex) {
			new ErrorLog(JOE_E_0002.params(SOSClassUtil.getMethodName()), ex);
		}
	}

	private Listener getJobChainParameterListener() {
		return new Listener() {
			@Override
			public void handleEvent(final Event e) {
				logger.debug("getInsertListener");
				MaintainJobChainParameter();
			}
		};
	}

	private void MaintainJobChainParameter() {
		editJobChainOrNodeParameter(null, null);
	}

	@Override
	public Listener getCutListener() {
		return new Listener() {
			@Override
			public void handleEvent(final Event e) {
				logger.debug("getInsertListener");
				cutNode();
			}
		};
	}

	private void cutNode() {
		try {
			copyNode();
			deleteNode();
		}
		catch (Exception ex) {
			new ErrorLog(JOE_E_0002.params(SOSClassUtil.getMethodName()), ex);
		}
	}

	private Listener getDoReorderStatesListener() {
		return new Listener() {
			@Override
			public void handleEvent(final Event e) {
				flgDoReorderStates = !flgDoReorderStates;
				MenuItem objMI = (MenuItem) e.widget;
				objMI.setSelection(flgDoReorderStates);
			}
		};
	}

	@Override
	protected void applyInputFields(final boolean flgT, final enuOperationMode OperationMode) {
		// TODO Auto-generated method stub
	}
} // @jve:decl-index=0:visual-constraint="10,10"
