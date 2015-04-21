package com.sos.joe.jobdoc.editor;
import com.sos.JSHelper.Basics.VersionInfo;
import com.sos.dialog.classes.WindowsSaver;
import com.sos.i18n.annotation.I18NMessage;
import com.sos.i18n.annotation.I18NMessages;
import com.sos.i18n.annotation.I18NResourceBundle;
import com.sos.joe.globals.interfaces.IEditor;
import com.sos.joe.globals.interfaces.IEditorAdapter;
import com.sos.joe.globals.messages.ErrorLog;
import com.sos.joe.globals.messages.Messages;
import com.sos.joe.globals.misc.ResourceManager;
import com.sos.joe.globals.options.Options;
import com.sos.joe.jobdoc.editor.forms.DocumentationForm;
import com.sos.joe.xml.Utils;

import org.apache.log4j.Logger;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabFolder2Adapter;
import org.eclipse.swt.custom.CTabFolderEvent;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.events.*;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.*;

import java.io.File;
import java.util.ArrayList;

import static sos.util.SOSClassUtil.getMethodName;

@I18NResourceBundle(baseName = "JOEMessages", defaultLocale = "en") public class MainWindow implements IEditorAdapter{
	private static final String	conStringEDITOR			= "editor";
	public static final String	conIconICON_OPEN_GIF	= "/sos/scheduler/editor/icon_open.gif";
	public static final String	conIconEDITOR_PNG		= "/sos/scheduler/editor/editor.png";
	private final String		conClassName			= "MainWindow";
	private final String		conSVNVersion			= "$Id: MainWindow.java 25898 2014-06-20 14:36:54Z kb $";
	private static final Logger	logger					= Logger.getLogger(MainWindow.class);
	private static Shell		sShell					= null;													// @jve:decl-index=0:visual-constraint="3,1"
	private Menu				menuBar					= null;
	private static Menu			mFile					= null;
	private final Menu			submenu					= null;
	private Menu				submenu1				= null;
	private MainWindow			main					= null;
	private Composite			groupmain				= null;
	private static ToolItem		butSave					= null;
	/**  */
	private final static String	EMPTY					= "";
	private static Label		StatusLine				= null;
	class TabData  {
		protected String	title	= "";
		protected String	caption	= "";
		protected int		cnt		= 0;

		public TabData(String title, String caption) {
			this.title = title;
			this.caption = caption;
		}
	}

	public MainWindow() {
		logger.debug(conSVNVersion);
	}
	private final ArrayList<String>	filelist				= new ArrayList<String>();
	private static final String		NEW_DOCUMENTATION_TITLE	= "Unknown";

	// public TabbedContainer(MainWindow window, Composite parent) {
	public CTabFolder TabbedContainer(Composite parent) {
		// this.window = window;
		Options.loadOptions(JobDocEditorMain.class);
		objCTabFolder = new CTabFolder(parent, SWT.TOP | SWT.CLOSE);
		objCTabFolder.setLayoutData(new GridData(GridData.FILL, GridData.FILL, true, true));
		initialize();
		//		if (Options.openLastFolder() == true) {
		//			OpenLastFolder();
		//		}
		//		else {
		newDocumentation();
		//		}
		return objCTabFolder;
	}
	IEditor	objDataHandler	= null;

	public DocumentationForm newDocumentation() {
		DocumentationForm doc = new DocumentationForm(this, objCTabFolder, SWT.NONE);
		objDataHandler = doc;
		doc.openBlank();
		newItem(doc, NEW_DOCUMENTATION_TITLE);
		return doc;
	}

	public DocumentationForm openDocumentation() {
		try {
			DocumentationForm doc = new DocumentationForm(this, objCTabFolder, SWT.NONE);
			if (doc.open(filelist)) {
				// CTabItem tab = newItem(doc, doc.getFilename());
				newItem(doc, doc.getFilename());
				return doc;
			}
			else
				return null;
		}
		catch (Exception e) {
			new ErrorLog("error in " + getMethodName(), e);
			System.out.println("error in TabbedContainer.openDocumentation()" + e.getMessage());
			return null;
		}
	}

	public DocumentationForm openDocumentation(String filename) {
		try {
			DocumentationForm doc = new DocumentationForm(this, objCTabFolder, SWT.NONE);
			if (doc.open(filename, filelist)) {
				// CTabItem tab = newItem(doc, doc.getFilename());
				newItem(doc, doc.getFilename());
				// tab.setImage(ResourceManager.getImageFromResource("/sos/scheduler/editor/editor-small.png"));
				return doc;
			}
			else
				return null;
		}
		catch (Exception e) {
			new ErrorLog("error in " + getMethodName(), e);
			System.out.println("error in TabbedContainer.openDocumentation()" + e.getMessage());
			return null;
		}
	}

	public String openDocumentationName() {
		try {
			DocumentationForm doc = new DocumentationForm(this, objCTabFolder, SWT.NONE);
			if (doc.open(filelist)) {
				// CTabItem tab = newItem(doc, doc.getFilename());
				// tab.setImage(ResourceManager.getImageFromResource("/sos/scheduler/editor/editor-small.png"));
				return doc.getFilename();
			}
			else
				return null;
		}
		catch (Exception e) {
			new ErrorLog("error in " + getMethodName(), e);
			System.out.println("error in TabbedContainer.openDocumentation()" + e.getMessage());
			return null;
		}
	}

	private CTabItem newItem(Control control, String filename) {
		CTabItem tab = new CTabItem(objCTabFolder, SWT.NONE);
		tab.addDisposeListener(new DisposeListener() {
			@Override public void widgetDisposed(final DisposeEvent e) {
//				MainWindow.getSShell().setText(strTitleText /* "Job Scheduler Editor" */);
//				MainWindow.setSaveStatus();
			}
		});
		tab.setControl(control);
		objCTabFolder.setSelection(objCTabFolder.indexOf(tab));
		String actFilename = Utils.getFileFromURL(filename);
		tab.setData(new TabData(actFilename, strTitleText));
		String title = setSuffix(tab, actFilename);
		TabData t = (TabData) tab.getData();
		t.caption = shortCaption(title);
		tab.setToolTipText(filename);
		tab.setText(title);
		filelist.add(filename);
		return tab;
	}

	private String shortCaption(String caption) {
		File f = new File(caption);
		if (caption.length() > 30 && f.getParentFile() != null && f.getParentFile().getParentFile() != null) {
			String s = "..." + f.getParentFile().getParentFile().getName() + "/" + f.getParentFile().getName() + "/" + f.getName();
			if (s.length() > 30) {
				return caption;
			}
			else {
				return s;
			}
		}
		else {
			return caption;
		}
	}
	private String	strTitleText	= "";

	public void setTitleText(final String pstrTitle) {
		strTitleText = pstrTitle;
	}

	private String setSuffix(CTabItem tab, String title) {
		int sameTitles = getSameTitles(title);
		TabData t = (TabData) tab.getData();
		if (t != null) {
			t.cnt = sameTitles;
			if (sameTitles > 0)
				title = title + "(" + (sameTitles + 1) + ")";
		}
		return title;
	}

	private boolean isFreeIndex(int index, String title) {
		boolean found = false;
		CTabItem[] items = objCTabFolder.getItems();
		int i = 0;
		while (i < items.length && !found) {
			TabData t = (TabData) items[i].getData();
			if (items[i].getData() != null && t.title.equals(title) && t.cnt == index && !items[i].equals(getCurrentTab())) {
				found = true;
			}
			i++;
		}
		return !found;
	}

	public CTabItem getCurrentTab() {
		if (objCTabFolder.getItemCount() == 0)
			return null;
		else
			return objCTabFolder.getItem(objCTabFolder.getSelectionIndex());
	}

	private int getSameTitles(String title) {
		int cnt = -1;
		int i = 0;
		// boolean found = false;
		while (cnt == -1) {
			if (isFreeIndex(i, title)) {
				// found = true;
				cnt = i;
			}
			i++;
		}
		return cnt;
	}
	private CTabFolder	objCTabFolder	= null;

	private void initialize() {
		objCTabFolder.setSimple(false);
		objCTabFolder.setSize(new Point(690, 478));
		objCTabFolder.setLayout(new FillLayout());
		// on tab selection
		objCTabFolder.addSelectionListener(new SelectionListener() {
			@Override public void widgetSelected(SelectionEvent e) {
				//				setWindowTitle();
				//				MainWindow.setMenuStatus();
				//				MainWindow.shellActivated_();
			}

			@Override public void widgetDefaultSelected(SelectionEvent e) {
				widgetSelected(e);
			}
		});
		// on tab close
		objCTabFolder.addCTabFolder2Listener(new CTabFolder2Adapter() {
			@Override public void close(CTabFolderEvent event) {
				// IEditor editor = getCurrentEditor();
				IEditor editor = (IEditor) ((CTabItem) (event.item)).getControl();
				if (editor.hasChanges()) {
					event.doit = editor.close();
				}
				//				if (event.doit)
				//					filelist.remove(editor.getFilename());
			}
		});
		objCTabFolder.addTraverseListener(new TraverseListener() {
			@Override public void keyTraversed(final TraverseEvent e) {
				/*if(e.detail == SWT.TRAVERSE_ESCAPE) {		
					System.out.println(folder.getChildren().length);
					IEditor editor = (IEditor)folder.getSelection().getControl();
					filelist.remove(editor.getFilename());
					editor.close();
					folder.getSelection().dispose();
					folder.removeControlListener(listener)
				}*/
			}
		});
	}

	private void createContainer(Composite objParent) {
		objCTabFolder = TabbedContainer(objParent);
		sShell.setLayoutData(new GridData(GridData.FILL, GridData.FILL, true, true));
		// TODO: Ausserhalb des Job Editors veränderte Files sollten mit Hilfe einer "Aktualisieren" Funktion neu eingelesen werden können.
		sShell.addShellListener(new ShellListener() {
			@Override public void shellActivated(ShellEvent event) {
				//				shellActivated_();
			}

			@Override public void shellClosed(ShellEvent arg0) {
				objPersistenceStore.saveWindowPosAndSize();
				logger.debug("shellClosed");
			}

			@Override public void shellDeactivated(ShellEvent arg0) {
				logger.debug("shellDeactivated");
			}

			@Override public void shellDeiconified(ShellEvent arg0) {
				logger.debug("shellDeiconified");
			}

			@Override public void shellIconified(ShellEvent arg0) {
				// System.out.println("icon");
			}
		});
		main = this;
	}

	private String getMenuText(final String pstrText, final String pstrAccelerator) {
		@SuppressWarnings("unused") final String conMethodName = conClassName + "::getMenuText";
		String strRet = pstrText;
		int intLen = pstrAccelerator.trim().length();
		if (intLen > 0) {
			if (intLen == 1) {
				strRet += "\tCtrl+" + pstrAccelerator;
			}
			else {
				strRet += "\t" + pstrAccelerator;
			}
		}
		return strRet;
	} // private String getMenuText

	public void OpenLastFolder() {
		if (Options.openLastFolder() == true) {
			String strF = Options.getLastFolderName();
			if (strF != null && strF.trim().length() > 0) {
				setStatusLine(String.format("Open last Folder '%1$s'", strF));
				openDocumentation();
			}
		}
	}

	public void setStatusLine(final String pstrText, final int pintMsgType) {
		StatusLine.setText(pstrText);
		// StatusLine.setForeground(SWT.COLOR_BLUE);
	}

	public void setStatusLine(final String pstrText) {
		StatusLine.setText(pstrText);
		// StatusLine.setForeground(SWT.COLOR_BLUE);
	}
	private WindowsSaver	objPersistenceStore;

	/**
	 * This method initializes sShell Will only be executed with JOE standalone
	 * @wbp.parser.entryPoint
	 */
	public void createSShell() {
		Shell shell = new Shell();
		final GridLayout gridLayout_1 = new GridLayout();
		shell.setLayout(gridLayout_1);
		shell.setMinimumSize(940, 600);
		shell.setImage(ResourceManager.getImageFromResource(conIconEDITOR_PNG));
		Composite groupmain = new Composite(shell, SWT.NONE);
		final GridLayout gridLayout = new GridLayout();
		gridLayout.verticalSpacing = 0;
		gridLayout.marginWidth = 0;
		gridLayout.marginHeight = 0;
		gridLayout.horizontalSpacing = 0;
		groupmain.setLayout(gridLayout);
		groupmain.setLayoutData(new GridData(GridData.FILL, GridData.FILL, true, false));
		createSShell(shell, groupmain);
		Options.loadWindow(sShell, conStringEDITOR);
		String strT = Messages.getLabel(JOE_I_0010) + " " + VersionInfo.JAR_VERSION;
		//		container.setTitleText(strT);
		sShell.setText(strT);
		logger.debug(strT);
		Options.conJOEGreeting = strT;
		sShell.setData(sShell.getText());
	}

	private void createMenu() {
		menuBar = new Menu(sShell, SWT.BAR);
		menuBar.setVisible(true);
		MenuItem submenuItem2 = new MenuItem(menuBar, SWT.CASCADE);
		submenuItem2.setEnabled(true);
		submenuItem2.setText("&" + getMenuText(Messages.getLabel(MENU_File), EMPTY));
		mFile = new Menu(submenuItem2);
		MenuItem open = new MenuItem(mFile, SWT.PUSH);
		open.addSelectionListener(new SelectionAdapter() {
			@Override public void widgetSelected(final SelectionEvent e) {
				openDocumentation();
				//				if (container.openQuick() != null) {
				//					setSaveStatus();
				//				}
			}
		});
		open.setText(getMenuText(Messages.getLabel(MENU_OPEN), "O"));
		open.setAccelerator(SWT.CTRL | 'O');
		MenuItem push1 = new MenuItem(mFile, SWT.PUSH);
		push1.setText(getMenuText(Messages.getLabel(MENU_Documentation), "P")); // Generated
		// push1.setText(getMenuText("Modify Documentation", "P")); // Generated
		push1.setAccelerator(SWT.CTRL | 'P');
		push1.addSelectionListener(new SelectionListener() {
			@Override public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
				setSaveStatus();
			}

			@Override public void widgetDefaultSelected(org.eclipse.swt.events.SelectionEvent e) {
			}
		});
		//
		MenuItem pSaveAs = new MenuItem(mFile, SWT.PUSH);
		pSaveAs.setText("Save As                            ");
		pSaveAs.setEnabled(false);
		pSaveAs.addSelectionListener(new SelectionListener() {
			@Override public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
				//				if (container.getCurrentEditor() != null && container.getCurrentEditor().applyChanges()) {
				//					if (container.getCurrentTab().getData("ftp_title") != null) {
				//						container.getCurrentTab().setData("ftp_title", null);
				//						container.getCurrentTab().setData("ftp_profile_name", null);
				//						container.getCurrentTab().setData("ftp_remote_directory", null);
				//						container.getCurrentTab().setData("ftp_hot_folder_elements", null);
				//						container.getCurrentTab().setData("ftp_profile", null);
				//					}
				//					container.getCurrentEditor().saveAs();
				//					setSaveStatus();
				//				}
			}

			@Override public void widgetDefaultSelected(org.eclipse.swt.events.SelectionEvent e) {
			}
		});
		new MenuItem(mFile, SWT.SEPARATOR);
		submenuItem2.setMenu(mFile);
		MenuItem pExit = new MenuItem(mFile, SWT.PUSH);
		// pExit.setText("Exit\tCtrl+E");
		pExit.setText(getMenuText(Messages.getLabel("MENU_Exit"), "E"));
		pExit.setAccelerator(SWT.CTRL | 'E');
		pExit.addSelectionListener(new SelectionListener() {
			@Override public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
				try {
					objPersistenceStore.saveWindowPosAndSize();
					sShell.close();
				}
				catch (Exception es) {
					new ErrorLog("error: " + getMethodName(), es);
				}
			}

			@Override public void widgetDefaultSelected(SelectionEvent e) {
			}
		});
		MenuItem submenuItem = new MenuItem(menuBar, SWT.CASCADE);
		submenuItem.setText(getMenuText(Messages.getLabel(MENU_Options), EMPTY));
		MenuItem submenuItem3 = new MenuItem(menuBar, SWT.CASCADE);
		submenuItem3.setText("&" + getMenuText(Messages.getLabel(MENU_Help), EMPTY));
		submenu1 = new Menu(submenuItem3);
		MenuItem pHelS = new MenuItem(submenu1, SWT.PUSH);
		pHelS.setText("JOE " + getMenuText(Messages.getLabel(MENU_Help), EMPTY));
		pHelS.addSelectionListener(new SelectionListener() {
			@Override public void widgetSelected(SelectionEvent e) {
				//				listener.openHelp(Options.getHelpURL("index"));
			}

			@Override public void widgetDefaultSelected(org.eclipse.swt.events.SelectionEvent e) {
			}
		});
		MenuItem pHelp = new MenuItem(submenu1, SWT.PUSH);
		pHelp.setText(getMenuText(Messages.getLabel(MENU_Help), "F1"));
		// pHelp.setAccelerator(SWT.F1);
		pHelp.addSelectionListener(new SelectionListener() {
			@Override public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
				//				if (container.getCurrentEditor() != null) {
				//					listener.openHelp(container.getCurrentEditor().getHelpKey());
				//				}
				//				else {
				//					// String msg = "Help is available after documentation or configuration is opened";
				//					String msg = Messages.getString("help.info");
				//					MainWindow.message(msg, SWT.ICON_INFORMATION);
				//				}
			}

			@Override public void widgetDefaultSelected(org.eclipse.swt.events.SelectionEvent e) {
			}
		});
		// TODO FAQ, JIRA, Ticket-System, .... als Menu-Items
		MenuItem pAbout = new MenuItem(submenu1, SWT.PUSH);
		pAbout.setText(getMenuText(Messages.getLabel(MENU_About), EMPTY) + " JOE");
		pAbout.addSelectionListener(new SelectionListener() {
			@Override public void widgetSelected(SelectionEvent e) {
				//				listener.showAbout();
			}

			@Override public void widgetDefaultSelected(SelectionEvent e) {
			}
		});
		submenuItem3.setMenu(submenu1);
		sShell.setMenuBar(menuBar);
	}

	/**
	 * This method initializes sShell Will only be executed with JOE using in Dashboard
	 * @wbp.parser.entryPoint
	 */
	public void createSShell(Composite containerParent, Composite objParent) {
		sShell = objParent.getShell();
		groupmain = objParent;
		groupmain.setLayoutData(new GridData(GridData.FILL, GridData.FILL, true, false));
		createMenu();
		createToolBar();
		createContainer(containerParent);
		StatusLine = new Label(groupmain, SWT.BOTTOM);
		GridData gridStatuslineLayout = new GridData();
		gridStatuslineLayout.horizontalAlignment = GridData.FILL;
		gridStatuslineLayout.grabExcessHorizontalSpace = true;
		StatusLine.setLayoutData(gridStatuslineLayout);
		//		listener = new MainListener(this, container);
		objPersistenceStore = new WindowsSaver(this.getClass(), sShell, 940, 600);
		objPersistenceStore.restoreWindowLocation();
		objPersistenceStore.restoreWindowSize();
	}

/*
	private void saveWindowPosAndSize() {
		objPersistenceStore.saveWindow();
		objPersistenceStore.restoreWindowSize();
	}
*/
	//	private void saveWindowPosAndSize() {
	//		objPersistenceStore.saveWindow();
	//	}
	//
	public static Shell getSShell() {
		return sShell;
	}

	@SuppressWarnings("unused") private String getMsg(final String pstrKey) {
		@SuppressWarnings("unused") final String conMethodName = conClassName + "::getMsg";
		// super.setLocale(Options.getLanguage());
		String strT = Messages.getString(pstrKey, EMPTY);
		return strT;
	} // private String getMsg

	public void setSaveStatus() {
		setMenuStatus();
		//		container.setStatusInTitle();
	}

	public static boolean setMenuStatus() {
		boolean saved = true;
		MenuItem[] items = mFile.getItems();
		int index = 0;
		for (int i = 0; i < items.length; i++) {
			items[i].setEnabled(true);
			MenuItem item = items[i];
		}
		return saved;
	}

	public static int ErrMsg(final String pstrMessage) {
		return message(pstrMessage, SWT.ICON_ERROR);
	}

	public static int message(String message, int style) {
		return message(getSShell(), message, style);
	}

	public static int message(String application, String message, int style) {
		return message(getSShell(), application, message, style);
	}

	// /**
	// * Erzeugt einen Confirm-Dialog, wenn der Button zum schließen des Fensters
	// * betätigt wird.
	// *
	// * @see org.eclipse.jface.window.Window#handleShellCloseEvent()
	// */
	// @Override
	// protected void handleShellCloseEvent () {
	// if (MessageDialog.openConfirm(null, "Bestätigung",
	// "Wollen Sie das Programm beenden?")) {
	// super.handleShellCloseEvent();
	// }
	// }
	public static int message(Shell shell, String pstrMessage, int style) {
		MessageBox mb = new MessageBox(shell, style);
		if (mb == null) {
			return -1;
		}
		if (pstrMessage == null) {
			pstrMessage = "??????";
		}
		mb.setMessage(pstrMessage);
		String title = Messages.getLabel("message");
		if ((style & SWT.ICON_ERROR) != 0)
			title = Messages.getLabel("error");
		else {
			if ((style & SWT.ICON_INFORMATION) != 0)
				title = Messages.getLabel("information");
			else
				if ((style & SWT.ICON_QUESTION) != 0)
					title = Messages.getLabel("question");
				else
					if ((style & SWT.ICON_WARNING) != 0)
						title = Messages.getLabel("warning");
		}
		mb.setText("JOE: " + title);
		return mb.open();
	}

	public static int message(Shell shell, String application, String pstrMessage, int style) {
		MessageBox mb = new MessageBox(shell, style);
		if (mb == null) {
			return -1;
		}
		if (pstrMessage == null) {
			pstrMessage = "??????";
		}
		mb.setMessage(pstrMessage);
		String title = Messages.getLabel("message");
		if ((style & SWT.ICON_ERROR) != 0)
			title = Messages.getLabel("error");
		else {
			if ((style & SWT.ICON_INFORMATION) != 0)
				title = Messages.getLabel("information");
			else
				if ((style & SWT.ICON_QUESTION) != 0)
					title = Messages.getLabel("question");
				else
					if ((style & SWT.ICON_WARNING) != 0)
						title = Messages.getLabel("warning");
		}
		mb.setText(application + ": " + title);
		return mb.open();
	}

	private void createToolBar() {
		final ToolBar toolBar = new ToolBar(groupmain, SWT.NONE);
		toolBar.setLayoutData(new GridData(GridData.BEGINNING, GridData.FILL, true, false));
		final ToolItem butNew = new ToolItem(toolBar, SWT.NONE);
		butNew.setImage(ResourceManager.getImageFromResource("/sos/scheduler/editor/icon_new.gif"));
		final Menu menu = new Menu(toolBar);
		butNew.setToolTipText("New Documentation");
		MenuItem itemDoc = new MenuItem(menu, SWT.PUSH);
		itemDoc.setText("Documentation");
		itemDoc.addSelectionListener(new SelectionListener() {
			@Override public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
				//				if (container.newDocumentation() != null)
				//					setSaveStatus();
			}

			@Override public void widgetDefaultSelected(org.eclipse.swt.events.SelectionEvent e) {
			}
		});
		/*MenuItem itemDetails = new MenuItem(menu, SWT.PUSH);
		itemDetails.setText("Details");
		itemDetails.addSelectionListener(new SelectionListener() {
			public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
				if (container.newDetails() != null)
					setSaveStatus();
			}
			public void widgetDefaultSelected(org.eclipse.swt.events.SelectionEvent e) {
			}
		});
		 */
		addDropDown(butNew, menu);
		butSave = new ToolItem(toolBar, SWT.PUSH);
		butSave.addSelectionListener(new SelectionAdapter() {
			@Override public void widgetSelected(final SelectionEvent e) {
				objDataHandler.save();
			}
		});
		butSave.setImage(ResourceManager.getImageFromResource("/sos/scheduler/editor/save.gif"));
		butSave.setToolTipText("Save Configuration");
		final ToolItem butHelp = new ToolItem(toolBar, SWT.PUSH);
		butHelp.setImage(ResourceManager.getImageFromResource("/sos/scheduler/editor/icon_help.gif"));
		butHelp.addSelectionListener(new SelectionListener() {
			@Override public void widgetSelected(final SelectionEvent e) {
				//				if (container.getCurrentEditor() != null) {
				//					listener.openHelp(container.getCurrentEditor().getHelpKey());
				//				}
				//				else {
				// String msg = "Help is available after documentation or configuration is opened";
				String msg = Messages.getString("help.info");
				MainWindow.message(msg, SWT.ICON_INFORMATION);
				//				}
			}

			@Override public void widgetDefaultSelected(org.eclipse.swt.events.SelectionEvent e) {
			}
		});
	}

	private static void addDropDown(final ToolItem item, final Menu menu) {
		item.addSelectionListener(new SelectionAdapter() {
			@Override public void widgetSelected(final SelectionEvent e) {
				Rectangle rect = item.getBounds();
				Point pt = new Point(rect.x, rect.y + rect.height);
				pt = item.getParent().toDisplay(pt);
				menu.setLocation(pt.x, pt.y);
				menu.setVisible(true);
			}
		});
	}
	public static final String	JOE_I_0010				= "JOE_I_0010";
	@I18NMessages(value = { @I18NMessage("Open"), //
			@I18NMessage(value = "Open", //
			locale = "en_UK", //
			explanation = "start the open dialog box ..." //
			), //
			@I18NMessage(value = "Öffnen", //
			locale = "de", //
			explanation = "Den Dialog für das Öffnen einer Datei starten" //
			), //
			@I18NMessage(value = "Open", locale = "es", //
			explanation = "Open" //
			), //
			@I18NMessage(value = "Open", locale = "fr", //
			explanation = "Open" //
			), //
			@I18NMessage(value = "Open", locale = "it", //
			explanation = "Open" //
			) //
	}, msgnum = "MENU_OPEN", msgurl = "Menu-Open")/*!
													 * \var MENU_OPEN
													 * \brief Open
													 */
	public static final String	MENU_OPEN				= "MENU_OPEN";
	@I18NMessages(value = { @I18NMessage("Configuration"), //
			@I18NMessage(value = "Configuration", //
			locale = "en_UK", //
			explanation = "Configuration" //
			), //
			@I18NMessage(value = "Konfiguration", //
			locale = "de", //
			explanation = "Configuration" //
			), //
			@I18NMessage(value = "Configuration", locale = "es", //
			explanation = "Configuration" //
			), //
			@I18NMessage(value = "Configuration", locale = "fr", //
			explanation = "Configuration" //
			), //
			@I18NMessage(value = "Configuration", locale = "it", //
			explanation = "Configuration" //
			) //
	}, msgnum = "MENU_Configuration", msgurl = "Menu-Configuration")/*!
																	 * \var MENU_Configuration
																	 * \brief Configuration
																	 */
	public static final String	MENU_Configuration		= "MENU_Configuration";
	@I18NMessages(value = { @I18NMessage("New"), //
			@I18NMessage(value = "New Object", //
			locale = "en_UK", //
			explanation = "New" //
			), //
			@I18NMessage(value = "Neues Objekt erstellen", //
			locale = "de", //
			explanation = "New" //
			), //
			@I18NMessage(value = "New", locale = "es", //
			explanation = "New" //
			), //
			@I18NMessage(value = "New", locale = "fr", //
			explanation = "New" //
			), //
			@I18NMessage(value = "New", locale = "it", //
			explanation = "New" //
			) //
	}, msgnum = "MENU_New", msgurl = "Menu-New")/*!
												 * \var MENU_New
												 * \brief New
												 */
	public static final String	MENU_New				= "MENU_New";
	@I18NMessages(value = { @I18NMessage("Documentation"), //
			@I18NMessage(value = "Modify Documentation", //
			locale = "en_UK", //
			explanation = "Modify Documentation of a job template" //
			), //
			@I18NMessage(value = "Dokumentation bearbeiten", //
			locale = "de", //
			explanation = "Dokumentation eines Job Templates bearbeiten" //
			), //
			@I18NMessage(value = "Modify Documentation", locale = "es", //
			explanation = "Modify Documentation of a job template" //
			), //
			@I18NMessage(value = "Modify Documentation", locale = "fr", //
			explanation = "Modify Documentation of a job template" //
			), //
			@I18NMessage(value = "Modify Documentation", locale = "it", //
			explanation = "Modify Documentation of a job template" //
			) //
	}, msgnum = "MENU_Documentation", msgurl = "Menu-Documentation")/*!
																	 * \var MENU_Documentation
																	 * \brief Documentation
																	 */
	public static final String	MENU_Documentation		= "MENU_Documentation";
	@I18NMessages(value = { @I18NMessage("EventHandler"), //
			@I18NMessage(value = "xEventHandler", //
			locale = "en_UK", //
			explanation = "EventHandler" //
			), //
			@I18NMessage(value = "Ereignis-Behandlung", //
			locale = "de", //
			explanation = "EventHandler" //
			), //
			@I18NMessage(value = "EventHandler", locale = "es", //
			explanation = "EventHandler" //
			), //
			@I18NMessage(value = "EventHandler", locale = "fr", //
			explanation = "EventHandler" //
			), //
			@I18NMessage(value = "EventHandler", locale = "it", //
			explanation = "EventHandler" //
			) //
	}, msgnum = "MENU_EventHandler", msgurl = "Menu-EventHandler")/*!
																	 * \var MENU_EventHandler
																	 * \brief EventHandler
																	 */
	public static final String	MENU_EventHandler		= "MENU_EventHandler";
	@I18NMessages(value = { @I18NMessage("HotFolderObject"), //
			@I18NMessage(value = "HotFolderObject", //
			locale = "en_UK", //
			explanation = "HotFolderObject" //
			), //
			@I18NMessage(value = "HotFolder-Objekt", //
			locale = "de", //
			explanation = "HotFolderObject" //
			), //
			@I18NMessage(value = "HotFolderObject", locale = "es", //
			explanation = "HotFolderObject" //
			), //
			@I18NMessage(value = "HotFolderObject", locale = "fr", //
			explanation = "HotFolderObject" //
			), //
			@I18NMessage(value = "HotFolderObject", locale = "it", //
			explanation = "HotFolderObject" //
			) //
	}, msgnum = "MENU_HotFolderObject", msgurl = "Menu-HotFolderObject")/*!
																		 * \var MENU_HotFolderObject
																		 * \brief HotFolderObject
																		 */
	public static final String	MENU_HotFolderObject	= "MENU_HotFolderObject";
	@I18NMessages(value = { @I18NMessage("Job"), //
			@I18NMessage(value = "Job", //
			locale = "en_UK", //
			explanation = "Job" //
			), //
			@I18NMessage(value = "Job", //
			locale = "de", //
			explanation = "Job" //
			), //
			@I18NMessage(value = "Job", locale = "es", //
			explanation = "Job" //
			), //
			@I18NMessage(value = "Job", locale = "fr", //
			explanation = "Job" //
			), //
			@I18NMessage(value = "Job", locale = "it", //
			explanation = "Job" //
			) //
	}, msgnum = "MENU_Job", msgurl = "Menu-Job")/*!
												 * \var MENU_Job
												 * \brief Job
												 */
	public static final String	MENU_Job				= "MENU_Job";
	@I18NMessages(value = { @I18NMessage("JobChain"), //
			@I18NMessage(value = "JobChain", //
			locale = "en_UK", //
			explanation = "JobChain" //
			), //
			@I18NMessage(value = "Job-Kette", //
			locale = "de", //
			explanation = "Job-Kette" //
			), //
			@I18NMessage(value = "JobChain", locale = "es", //
			explanation = "JobChain" //
			), //
			@I18NMessage(value = "JobChain", locale = "fr", //
			explanation = "JobChain" //
			), //
			@I18NMessage(value = "JobChain", locale = "it", //
			explanation = "JobChain" //
			) //
	}, msgnum = "MENU_JobChain", msgurl = "Menu-JobChain")/*!
															 * \var MENU_JobChain
															 * \brief JobChain
															 */
	public static final String	MENU_JobChain			= "MENU_JobChain";
	@I18NMessages(value = { @I18NMessage("ProcessClass"), //
			@I18NMessage(value = "ProcessClass", //
			locale = "en_UK", //
			explanation = "ProcessClass" //
			), //
			@I18NMessage(value = "Prozess Klasse", //
			locale = "de", //
			explanation = "ProcessClass" //
			), //
			@I18NMessage(value = "ProcessClass", locale = "es", //
			explanation = "ProcessClass" //
			), //
			@I18NMessage(value = "ProcessClass", locale = "fr", //
			explanation = "ProcessClass" //
			), //
			@I18NMessage(value = "ProcessClass", locale = "it", //
			explanation = "ProcessClass" //
			) //
	}, msgnum = "MENU_ProcessClass", msgurl = "Menu-ProcessClass")/*!
																	 * \var MENU_ProcessClass
																	 * \brief ProcessClass
																	 */
	public static final String	MENU_ProcessClass		= "MENU_ProcessClass";
	@I18NMessages(value = { @I18NMessage("Lock"), //
			@I18NMessage(value = "Lock", //
			locale = "en_UK", //
			explanation = "Lock" //
			), //
			@I18NMessage(value = "Sperre", //
			locale = "de", //
			explanation = "Sperren verwalten bzw anlegen" //
			), //
			@I18NMessage(value = "Lock", locale = "es", //
			explanation = "Lock" //
			), //
			@I18NMessage(value = "Lock", locale = "fr", //
			explanation = "Lock" //
			), //
			@I18NMessage(value = "Lock", locale = "it", //
			explanation = "Lock" //
			) //
	}, msgnum = "MENU_Lock", msgurl = "Menu-Lock")/*!
													 * \var MENU_Lock
													 * \brief Lock
													 */
	public static final String	MENU_Lock				= "MENU_Lock";
	@I18NMessages(value = { @I18NMessage("File"), //
			@I18NMessage(value = "File", //
			locale = "en_UK", //
			explanation = "File" //
			), //
			@I18NMessage(value = "Datei", //
			locale = "de", //
			explanation = "Menü der Dateioperationen" //
			), //
			@I18NMessage(value = "File", locale = "es", //
			explanation = "File" //
			), //
			@I18NMessage(value = "File", locale = "fr", //
			explanation = "File" //
			), //
			@I18NMessage(value = "File", locale = "it", //
			explanation = "File" //
			) //
	}, msgnum = "MENU_File", msgurl = "Menu-File")/*!
													 * \var MENU_File
													 * \brief File
													 */
	public static final String	MENU_File				= "MENU_File";
	@I18NMessages(value = { @I18NMessage("Options"), //
			@I18NMessage(value = "Options", //
			locale = "en_UK", //
			explanation = "Options" //
			), //
			@I18NMessage(value = "Einstellungen", //
			locale = "de", //
			explanation = "Einstellungen für JOE " //
			), //
			@I18NMessage(value = "Options", locale = "es", //
			explanation = "Options" //
			), //
			@I18NMessage(value = "Options", locale = "fr", //
			explanation = "Options" //
			), //
			@I18NMessage(value = "Options", locale = "it", //
			explanation = "Options" //
			) //
	}, msgnum = "MENU_Options", msgurl = "Menu-Options")/*!
														 * \var MENU_Options
														 * \brief Options
														 */
	public static final String	MENU_Options			= "MENU_Options";
	@I18NMessages(value = { @I18NMessage("Help"), //
			@I18NMessage(value = "Help", //
			locale = "en_UK", //
			explanation = "Help" //
			), //
			@I18NMessage(value = "Hilfe", //
			locale = "de", //
			explanation = "Hilfe zu JOE" //
			), //
			@I18NMessage(value = "Help", locale = "es", //
			explanation = "Help" //
			), //
			@I18NMessage(value = "Help", locale = "fr", //
			explanation = "Help" //
			), //
			@I18NMessage(value = "Help", locale = "it", //
			explanation = "Help" //
			) //
	}, msgnum = "MENU_Help", msgurl = "Menu-Help")/*!
													 * \var MENU_Help
													 * \brief Help
													 */
	public static final String	MENU_Help				= "MENU_Help";
	@I18NMessages(value = { @I18NMessage("About"), //
			@I18NMessage(value = "About", //
			locale = "en_UK", //
			explanation = "About" //
			), //
			@I18NMessage(value = "Über", //
			locale = "de", //
			explanation = "Liefert Informationen über ..." //
			), //
			@I18NMessage(value = "About", locale = "es", //
			explanation = "About" //
			), //
			@I18NMessage(value = "About", locale = "fr", //
			explanation = "About" //
			), //
			@I18NMessage(value = "About", locale = "it", //
			explanation = "About" //
			) //
	}, msgnum = "MENU_About", msgurl = "Menu-About")/*!
													 * \var MENU_About
													 * \brief About
													 */
	public static final String	MENU_About				= "MENU_About";
	// @I18NMessages(value = { @I18NMessage("Reset Dialog"), //
	// @I18NMessage(value = "Reset Dialog", //
	// locale = "en_UK", //
	// explanation = "Reset Dialog" //
	// ), //
	// @I18NMessage(value = "Einstellung zurücksetzen", //
	// locale = "de", //
	// explanation = "JOE wird neu initialisiert. Die Einstellungen werden neu geladen" //
	// ), //
	// @I18NMessage(value = "Reset Dialog", locale = "es", //
	// explanation = "Reset Dialog" //
	// ), //
	// @I18NMessage(value = "Reset Dialog", locale = "fr", //
	// explanation = "Reset Dialog" //
	// ), //
	// @I18NMessage(value = "Reset Dialog", locale = "it", //
	// explanation = "Reset Dialog" //
	// ) //
	// }, msgnum = "MENU_ResetDialog", msgurl = "Menu-ResetDialog")
	// /*!
	// * \var MENU_Reset Dialog
	// * \brief Reset Dialog
	// */
	public static final String	MENU_Reset_Dialog		= "MENU_ResetDialog";
	// /*!
	// * \var MENU_Order
	// * \brief Order
	// */
	public static final String	MENU_Order				= "MENU_Order";
}
