package sos.scheduler.editor.app;

import java.io.File;
import java.util.HashMap;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;
import org.jdom.Element;
import sos.scheduler.editor.app.MainListener;
import sos.scheduler.editor.app.IContainer;
import sos.scheduler.editor.app.TabbedContainer;
import sos.scheduler.editor.conf.DetailDom;
import sos.scheduler.editor.conf.SchedulerDom;
import sos.scheduler.editor.doc.DocumentationDom;
import sos.scheduler.editor.conf.forms.JobChainConfigurationForm;
import sos.scheduler.editor.conf.forms.HotFolderDialog;
import java.util.ArrayList;
import sos.scheduler.editor.conf.forms.SchedulerForm;
import sos.scheduler.editor.doc.forms.DocumentationForm;
import sos.util.SOSString;

 
public class MainWindow  {

	private static Shell sShell             = null; // @jve:decl-index=0:visual-constraint="3,1"

	private MainListener listener           = null;

	private static IContainer container     = null;

	private Menu         menuBar            = null;

	private Menu         mFile              = null;

	private Menu         submenu            = null;

	private Menu         menuLanguages      = null;

	private Menu         submenu1           = null;

	private MainWindow   main               = null;

	private Composite    groupmain          = null;

	private ToolItem     butSave            = null;

	private ToolItem     butShowAsSML       = null; 
	
	private SOSString    sosString          = new SOSString();
	

	public MainWindow() {
		super();	
	}


	/**
	 * This method initializes composite
	 */
	private void createContainer() {
		container = new TabbedContainer(this, sShell);
		sShell.setLayoutData(new GridData(GridData.FILL, GridData.FILL, true, true));

		main = this;
	}


	/**
	 * This method initializes sShell
	 */
	public void createSShell() {
		sShell = new Shell();
		final GridLayout gridLayout_1 = new GridLayout();
		sShell.setLayout(gridLayout_1);
		sShell.setText("Job Scheduler Editor");
		sShell.setData(sShell.getText());		
		sShell.setImage(ResourceManager.getImageFromResource("/sos/scheduler/editor/editor.png"));

		groupmain = new Composite(sShell, SWT.NONE);		
		final GridData gridData = new GridData(GridData.FILL, GridData.FILL, true, false);
		groupmain.setLayoutData(gridData);
		final GridLayout gridLayout = new GridLayout();
		gridLayout.verticalSpacing = 0;
		gridLayout.marginWidth = 0;
		gridLayout.marginHeight = 0;
		gridLayout.horizontalSpacing = 0;
		groupmain.setLayout(gridLayout);

		createToolBar();
		createContainer();

		listener = new MainListener(this, container);

		sShell.setSize(new org.eclipse.swt.graphics.Point(895, 625));
		sShell.setMinimumSize(890, 620);

		// load resources
		listener.loadOptions();
		listener.loadMessages();
		listener.loadJobTitels();
		listener.loadHolidaysTitel();
		Options.loadWindow(sShell, "editor");

		menuBar = new Menu(sShell, SWT.BAR);
		MenuItem submenuItem2 = new MenuItem(menuBar, SWT.CASCADE);
		submenuItem2.setText("&File");
		mFile = new Menu(submenuItem2);

		MenuItem open = new MenuItem(mFile, SWT.PUSH);
		open.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(final SelectionEvent e) {
				if (container.openQuick() != null)
					setSaveStatus();
			}
		});
		open.setText("Open                                  \tCtrl+O");
		open.setAccelerator(SWT.CTRL | 'O');

		MenuItem mNew = new MenuItem(mFile, SWT.CASCADE);				
		mNew.setText("New                                 \tCtrl+N");
		mNew.setAccelerator(SWT.CTRL | 'N');

		Menu pmNew = new Menu(mNew);
		MenuItem pNew = new MenuItem(pmNew, SWT.PUSH);
		pNew.setText("Configuration                  \tCtrl+I");
		pNew.setAccelerator(SWT.CTRL | 'I');
		pNew.addSelectionListener(new org.eclipse.swt.events.SelectionListener() {
			public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
				if (container.newScheduler() != null)
					setSaveStatus();
			}
			public void widgetDefaultSelected(org.eclipse.swt.events.SelectionEvent e) {
			}
		});
		mNew.setMenu(pmNew);

		MenuItem push1 = new MenuItem(pmNew, SWT.PUSH);
		push1.setText("Documentation            \tCtrl+P"); // Generated
		push1.addSelectionListener(new org.eclipse.swt.events.SelectionListener() {
			public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
				if (container.newDocumentation() != null)
					setSaveStatus();
			}


			public void widgetDefaultSelected(org.eclipse.swt.events.SelectionEvent e) {
			}
		});

		MenuItem pNewDetails = new MenuItem(pmNew, SWT.PUSH);
		pNewDetails.setText("Job Chain Details   \tCtrl+F");
		pNewDetails.setAccelerator(SWT.CTRL | 'F');
		pNewDetails.addSelectionListener(new org.eclipse.swt.events.SelectionListener() {
			public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
				if (container.newDetails() != null)
					setSaveStatus();
			}
			public void widgetDefaultSelected(org.eclipse.swt.events.SelectionEvent e) {
			}
		});

		//new event handler
		MenuItem pNewActions = new MenuItem(pmNew, SWT.PUSH);
		pNewActions.setText("Event Handler \tCTRL+X");
		pNewActions.setAccelerator(SWT.CTRL | 'X');
		pNewActions.addSelectionListener(new org.eclipse.swt.events.SelectionListener() {
			public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
				if (container.newActions() != null)
					setSaveStatus();
			}
			public void widgetDefaultSelected(org.eclipse.swt.events.SelectionEvent e) {
			}
		});

		
		MenuItem mpLife = new MenuItem(pmNew, SWT.CASCADE);				
		mpLife.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(final SelectionEvent e) {
			}
		});
		mpLife.setText("Hot Folder Element   \tCtrl+L");
		mpLife.setAccelerator(SWT.CTRL | 'L');

		Menu mLife = new Menu(mpLife);
		MenuItem mLifeJob = new MenuItem(mLife, SWT.PUSH);
		mLifeJob.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(final SelectionEvent e) {

				if (container.newScheduler(SchedulerDom.LIFE_JOB) != null)
					setSaveStatus();
			}
		});
		mLifeJob.setText("Job           \tCtrl+J");
		mLifeJob.setAccelerator(SWT.CTRL | 'J');
		mpLife.setMenu(mLife);

		MenuItem mLifeJobChain = new MenuItem(mLife, SWT.PUSH);
		mLifeJobChain.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(final SelectionEvent e) {
				if (container.newScheduler(SchedulerDom.LIFE_JOB_CHAIN) != null)
					setSaveStatus();
			}
		});
		mLifeJobChain.setText("Job Chain     \tCtrl+K");
		mLifeJobChain.setAccelerator(SWT.CTRL | 'K');

		MenuItem mLifeProcessClass = new MenuItem(mLife, SWT.PUSH);
		mLifeProcessClass.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(final SelectionEvent e) {
				if (container.newScheduler(SchedulerDom.LIFE_PROCESS_CLASS) != null)
					setSaveStatus();
			}
		});
		mLifeProcessClass.setText("Process Class \tCtrl+R");
		mLifeProcessClass.setAccelerator(SWT.CTRL | 'R');

		MenuItem mLifeLock = new MenuItem(mLife, SWT.PUSH);
		mLifeLock.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(final SelectionEvent e) {
				if (container.newScheduler(SchedulerDom.LIFE_LOCK) != null)
					setSaveStatus();
			}
		});
		mLifeLock.setText("Lock          \tCtrl+M");
		mLifeLock.setAccelerator(SWT.CTRL | 'M');

		MenuItem mLifeOrder= new MenuItem(mLife, SWT.PUSH);
		mLifeOrder.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(final SelectionEvent e) {
				if (container.newScheduler(SchedulerDom.LIFE_ORDER) != null)
					setSaveStatus();
			}
		});
		mLifeOrder.setText("Order         \tCtrl+W");
		mLifeOrder.setAccelerator(SWT.CTRL | 'W');

		MenuItem mLifeSchedule= new MenuItem(mLife, SWT.PUSH);
		mLifeSchedule.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(final SelectionEvent e) {
				if (container.newScheduler(SchedulerDom.LIFE_SCHEDULE) != null)
					setSaveStatus();
			}
		});
		mLifeSchedule.setText("Schedule      \tCtrl+K");
		mLifeSchedule.setAccelerator(SWT.CTRL | 'K');

		new MenuItem(mFile, SWT.SEPARATOR);

		MenuItem openDir = new MenuItem(mFile, SWT.PUSH);
		openDir.setText("Open Hot Folder               \tCtrl+D");		
		openDir.setAccelerator(SWT.CTRL | 'D');
		openDir.setEnabled(true);
		openDir.addSelectionListener(new org.eclipse.swt.events.SelectionListener() {
			public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
				if (container.openDirectory(null) != null)
					setSaveStatus();
			}
			public void widgetDefaultSelected(org.eclipse.swt.events.SelectionEvent e) {
			}
		});

		//open remote configuration
		MenuItem mORC = new MenuItem(mFile, SWT.CASCADE);
		mORC.setText("Open Remote Configuration\tCtrl+R");
		mORC.setAccelerator(SWT.CTRL | 'R');

		Menu pMOpenGlobalScheduler = new Menu(mORC);

		MenuItem pOpenGlobalScheduler = new MenuItem(pMOpenGlobalScheduler, SWT.PUSH);
		pOpenGlobalScheduler.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(final SelectionEvent e) {
				Utils.startCursor(getSShell());
				String globalSchedulerPath = Options.getSchedulerHome().endsWith("/") || Options.getSchedulerHome().endsWith("\\") ? Options.getSchedulerHome() : Options.getSchedulerHome() + "/";
				globalSchedulerPath = globalSchedulerPath + "config/remote/_all";
				File f = new java.io.File(globalSchedulerPath); 
				if(!f.exists()) {
					if(!f.mkdirs()) {						
						MainWindow.message("could not create Global Scheduler Configurations: " + globalSchedulerPath, SWT.ICON_WARNING);
						Utils.stopCursor(getSShell());
						return;
					}
				}

				if (container.openDirectory(globalSchedulerPath) != null)
					setSaveStatus();
				Utils.stopCursor(getSShell());
			}
		});
		pOpenGlobalScheduler.setText("Open Global Scheduler                          \tCtrl+T");
		pOpenGlobalScheduler.setAccelerator(SWT.CTRL | 'T');

		MenuItem pOpenSchedulerCluster = new MenuItem(pMOpenGlobalScheduler, SWT.PUSH);
		pOpenSchedulerCluster.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(final SelectionEvent e) {
				HotFolderDialog dialog = new HotFolderDialog(main);
				dialog.showForm(HotFolderDialog.SCHEDULER_CLUSTER);
			}
		});
		pOpenSchedulerCluster.setText("Open Cluster Configuration                    \tCtrl+U");
		pOpenSchedulerCluster.setAccelerator(SWT.CTRL | 'U');

		MenuItem pOpenSchedulerHost = new MenuItem(pMOpenGlobalScheduler, SWT.PUSH);
		pOpenSchedulerHost.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(final SelectionEvent e) {
				HotFolderDialog dialog = new HotFolderDialog(main);
				dialog.showForm(HotFolderDialog.SCHEDULER_HOST);
			}
		});
		pOpenSchedulerHost.setText("Open Remote Scheduler Configuration\tCtrl+U");
		pOpenSchedulerHost.setAccelerator(SWT.CTRL | 'U');

		mORC.setMenu(pMOpenGlobalScheduler);

		new MenuItem(mFile, SWT.SEPARATOR);

		MenuItem pSaveFile = new MenuItem(mFile, SWT.PUSH);
		pSaveFile.setText("Save                                    \tCtrl+S");
		pSaveFile.setAccelerator(SWT.CTRL | 'S');
		pSaveFile.setEnabled(false);
		pSaveFile.addSelectionListener(new org.eclipse.swt.events.SelectionListener() {
			public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
				save();				
			}
			public void widgetDefaultSelected(org.eclipse.swt.events.SelectionEvent e) {
			}
		});
		MenuItem pSaveAs = new MenuItem(mFile, SWT.PUSH);
		pSaveAs.setText("Save As                            ");		
		pSaveAs.setEnabled(false);
		pSaveAs.addSelectionListener(new org.eclipse.swt.events.SelectionListener() {
			public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
				if (container.getCurrentEditor() != null && container.getCurrentEditor().applyChanges()) {
					if(container.getCurrentTab().getData("ftp_title") != null) {
						container.getCurrentTab().setData("ftp_title", null);
						container.getCurrentTab().setData("ftp_profile_name", null);
						container.getCurrentTab().setData("ftp_remote_directory", null);
						container.getCurrentTab().setData("ftp_hot_folder_elements", null);
						container.getCurrentTab().setData("ftp_profile", null);
					}
					container.getCurrentEditor().saveAs();
					setSaveStatus();
				}
			}
			public void widgetDefaultSelected(org.eclipse.swt.events.SelectionEvent e) {
			}
		});

		MenuItem pSaveAsHotFolderElement = new MenuItem(mFile, SWT.PUSH);
		pSaveAsHotFolderElement.setText("Save As Hot Folder Elements   \tCtrl+B");		
		pSaveAsHotFolderElement.setAccelerator(SWT.CTRL | 'B');
		pSaveAsHotFolderElement.setEnabled(false);
		pSaveAsHotFolderElement.addSelectionListener(new org.eclipse.swt.events.SelectionListener() {
			public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {

				if (container.getCurrentEditor() != null && container.getCurrentEditor().applyChanges()) {
					SchedulerForm form =(SchedulerForm)container.getCurrentEditor();
					SchedulerDom currdom = (SchedulerDom)form.getDom();
					if(IOUtils.saveDirectory(currdom, true, SchedulerDom.DIRECTORY, null, container)) {
						Element root = currdom.getRoot();
						if(root != null) {
							Element config = root.getChild("config");
							if(config != null) {
								config.removeChildren("jobs");								
								config.removeChildren("job_chains");
								config.removeChildren("locks");
								Utils.removeChildrensWithName(config, "process_classes");
								config.removeChildren("schedules");
								config.removeChildren("commands");
								form.updateTree("main");
								form.update();
							}
						}
					}
					container.getCurrentEditor().save();
					setSaveStatus();
				}
			}

			public void widgetDefaultSelected(org.eclipse.swt.events.SelectionEvent e) {
			}
		});


		new MenuItem(mFile, SWT.SEPARATOR);

//		FTP
		MenuItem mFTP = new MenuItem(mFile, SWT.CASCADE);				
		mFTP.setText("FTP");
		mFTP.setAccelerator(SWT.CTRL | 'N');

		Menu pmFTP = new Menu(mNew);

		MenuItem pOpenFTP = new MenuItem(pmFTP, SWT.PUSH);
		pOpenFTP.setText("Open By FTP");

		pOpenFTP.addSelectionListener(new org.eclipse.swt.events.SelectionListener() {
			public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {	
				FTPDialog ftp = new FTPDialog(main);
				ftp.showForm(FTPDialog.OPEN);
			}
			public void widgetDefaultSelected(org.eclipse.swt.events.SelectionEvent e) {
			}
		});

		MenuItem pOpenHotFolderFTP = new MenuItem(pmFTP, SWT.PUSH);
		pOpenHotFolderFTP.setText("Open Hot Folder By FTP");
		pOpenHotFolderFTP.addSelectionListener(new org.eclipse.swt.events.SelectionListener() {
			public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {	
				FTPDialog ftp = new FTPDialog(main);
				ftp.showForm(FTPDialog.OPEN_HOT_FOLDER);
			}
			public void widgetDefaultSelected(org.eclipse.swt.events.SelectionEvent e) {
			}
		});

		new MenuItem(pmFTP, SWT.SEPARATOR);

		MenuItem pSaveFTP = new MenuItem(pmFTP, SWT.PUSH);
		pSaveFTP.setText("Save By FTP");

		pSaveFTP.addSelectionListener(new org.eclipse.swt.events.SelectionListener() {
			public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {				
				saveByFTP();				
			}

			public void widgetDefaultSelected(org.eclipse.swt.events.SelectionEvent e) {
			}
		});

		mFTP.setMenu(pmFTP);
		new MenuItem(mFile, SWT.SEPARATOR);

//		WebDav		
		boolean existwebDavLib = existLibraries();
		MenuItem mWebDav = new MenuItem(mFile, SWT.CASCADE);				
		mWebDav.setText("WebDav");
		mWebDav.setAccelerator(SWT.CTRL | 'N');
        mWebDav.setEnabled(existwebDavLib);
		Menu pmWebDav = new Menu(mNew);

		MenuItem pOpenWebDav = new MenuItem(pmWebDav, SWT.PUSH);
		pOpenWebDav.setText("Open By WebDav");
		pOpenWebDav.setEnabled(existwebDavLib);
		pOpenWebDav.addSelectionListener(new org.eclipse.swt.events.SelectionListener() {
			public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
				try {
					if(existLibraries()) {
						WebDavDialog webdav = new WebDavDialog(main);
						webdav.showForm(WebDavDialog.OPEN);
					}
				} catch(Exception ex) {
					try {
						new sos.scheduler.editor.app.ErrorLog("error in " + sos.util.SOSClassUtil.getMethodName() + " ; could not open file on Webdav Server", ex);
					} catch(Exception ee) {
						//tu nichts
					}
					MainWindow.message("could not open file on Webdav Server, cause: "  + ex.getMessage(), SWT.ICON_WARNING);

				}
			}
			public void widgetDefaultSelected(org.eclipse.swt.events.SelectionEvent e) {
			}
		});

		MenuItem pOpenHotFolderWebDav = new MenuItem(pmWebDav, SWT.PUSH);
		pOpenHotFolderWebDav.setText("Open Hot Folder By WebDav");
		pOpenHotFolderWebDav.setEnabled(existwebDavLib);
		pOpenHotFolderWebDav.addSelectionListener(new org.eclipse.swt.events.SelectionListener() {
			public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
				if(existLibraries()) {
					WebDavDialog webdav = new WebDavDialog(main);
					webdav.showForm(WebDavDialog.OPEN_HOT_FOLDER);
				}
			}
			public void widgetDefaultSelected(org.eclipse.swt.events.SelectionEvent e) {
			}
		});

		new MenuItem(pmWebDav, SWT.SEPARATOR);

		MenuItem pSaveWebDav = new MenuItem(pmWebDav, SWT.PUSH);
		pSaveWebDav.setText("Save By WebDav");
		pSaveWebDav.setEnabled(existwebDavLib);
		pSaveWebDav.addSelectionListener(new org.eclipse.swt.events.SelectionListener() {
			public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {	
				if(existLibraries()) {

					WebDavDialog webdav = new WebDavDialog(main);
					DomParser currdom = null;
					if(MainWindow.getContainer().getCurrentEditor() instanceof SchedulerForm) {
						SchedulerForm form =(SchedulerForm)MainWindow.getContainer().getCurrentEditor();			
						currdom = (SchedulerDom)form.getDom();
					} else if(MainWindow.getContainer().getCurrentEditor() instanceof DocumentationForm) {
						DocumentationForm form =(DocumentationForm)MainWindow.getContainer().getCurrentEditor();			
						currdom = (DocumentationDom)form.getDom();
					} else if(MainWindow.getContainer() instanceof 	JobChainConfigurationForm) {
						JobChainConfigurationForm form =(JobChainConfigurationForm)MainWindow.getContainer().getCurrentEditor();
						currdom = form.getDom();
					}

					if( currdom instanceof SchedulerDom && ((SchedulerDom)currdom).isDirectory()) {				
						webdav.showForm(WebDavDialog.SAVE_AS_HOT_FOLDER);
					} else
						webdav.showForm(WebDavDialog.SAVE_AS);
				}
			}
			public void widgetDefaultSelected(org.eclipse.swt.events.SelectionEvent e) {
			}
		});

		mWebDav.setMenu(pmWebDav);
		new MenuItem(mFile, SWT.SEPARATOR);

		submenuItem2.setMenu(mFile);
		MenuItem pExit = new MenuItem(mFile, SWT.PUSH);
		pExit.setText("Exit\tCtrl+E");
		pExit.setAccelerator(SWT.CTRL | 'E');
		pExit.addSelectionListener(new org.eclipse.swt.events.SelectionListener() {
			public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
				try {
					sShell.close();
				} catch(Exception es) {
					try {
						new ErrorLog("error: " + sos.util.SOSClassUtil.getMethodName(), es);
					} catch (Exception ee){
						//tu nichts
					}
				}
				
			}
			public void widgetDefaultSelected(org.eclipse.swt.events.SelectionEvent e) {
			}
		});

		MenuItem submenuItem = new MenuItem(menuBar, SWT.CASCADE);
		submenuItem.setText("Options");
		MenuItem submenuItem3 = new MenuItem(menuBar, SWT.CASCADE);
		submenuItem3.setText("&Help");
		submenu1 = new Menu(submenuItem3);

		MenuItem pHelS = new MenuItem(submenu1, SWT.PUSH);
		pHelS.setText("Scheduler Editor Help");		
		pHelS.addSelectionListener(new org.eclipse.swt.events.SelectionListener() {
			public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {				
				listener.openHelp(Options.getHelpURL("index"));				
			}
			public void widgetDefaultSelected(org.eclipse.swt.events.SelectionEvent e) {
			}
		});

		MenuItem pHelp = new MenuItem(submenu1, SWT.PUSH);
		pHelp.setText("Help\tF1");		
		pHelp.setAccelerator(SWT.F1);
		pHelp.addSelectionListener(new org.eclipse.swt.events.SelectionListener() {
			public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
				if (container.getCurrentEditor() != null) {
					listener.openHelp(container.getCurrentEditor().getHelpKey());					
				}else {
					String msg = "Help is available after documentation or configuration is opened";
					MainWindow.message(msg, SWT.ICON_INFORMATION);
				}
			}
			public void widgetDefaultSelected(org.eclipse.swt.events.SelectionEvent e) {
			}
		});

		MenuItem pAbout = new MenuItem(submenu1, SWT.PUSH);
		pAbout.setText("About");
		pAbout.addSelectionListener(new org.eclipse.swt.events.SelectionListener() {
			public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
				listener.showAbout();
			}
			public void widgetDefaultSelected(org.eclipse.swt.events.SelectionEvent e) {
			}
		});
		submenuItem3.setMenu(submenu1);
		submenu = new Menu(submenuItem);
		MenuItem submenuItem1 = new MenuItem(submenu, SWT.CASCADE);
		submenuItem1.setText("Help Language");
		menuLanguages = new Menu(submenuItem1);

		// create languages menu
		listener.setLanguages(menuLanguages);

		submenuItem1.setMenu(menuLanguages);
		submenuItem.setMenu(submenu);

		MenuItem submenuItemInfo = new MenuItem(submenu, SWT.PUSH);
		submenuItemInfo.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(final SelectionEvent e) {
				listener.resetInfoDialog();				
			}
		});
		submenuItemInfo.setText("Reset Dialog");
		sShell.setMenuBar(menuBar);
		sShell.addShellListener(new org.eclipse.swt.events.ShellAdapter() {
			public void shellClosed(org.eclipse.swt.events.ShellEvent e) {
				e.doit = container.closeAll();
				setSaveStatus();
				Options.saveWindow(sShell, "editor");
				listener.saveOptions();
				ResourceManager.dispose();
			}
			public void shellActivated(org.eclipse.swt.events.ShellEvent e) {
				setSaveStatus();
			}
		});
	}


	public static Shell getSShell() {
		return sShell;
	}


	public void setSaveStatus() {
		setMenuStatus();
		container.setStatusInTitle();
	}


	public boolean setMenuStatus() {
		boolean saved = true;
		if (container.getCurrentEditor() != null) {
			saved = !container.getCurrentEditor().hasChanges();
			butShowAsSML.setEnabled(true);	
			butSave.setEnabled(container.getCurrentEditor().hasChanges());

		} else {
			butShowAsSML.setEnabled(false);
			butSave.setEnabled(false);
		}

		MenuItem[] items = mFile.getItems();
		int index = 0;
		for (int i =0; i < items.length; i++){
			MenuItem item = items[i];
			if(item.getText().startsWith("Save")) {
				index = i;
				break;
			} 
		}

		items[index].setEnabled(container.getCurrentEditor() != null);
		items[index+1].setEnabled(container.getCurrentEditor() != null);

		if(container.getCurrentEditor() instanceof sos.scheduler.editor.conf.forms.SchedulerForm)  {
			sos.scheduler.editor.conf.forms.SchedulerForm form =(sos.scheduler.editor.conf.forms.SchedulerForm)container.getCurrentEditor();
			SchedulerDom dom = (SchedulerDom)form.getDom(); 
			if(dom.isDirectory()) {
				items[index+1].setEnabled(false);			
			} 
			if(!dom.isLifeElement() && !dom.isDirectory()) {
				items[index+2].setEnabled(true);
				butSave.setEnabled(true);
			} else {
				items[index+2].setEnabled(false);			
			}

		} else {
			items[index+2].setEnabled(false);

		}
		return saved;
	}


	public static int message(String message, int style) {
		return message(getSShell(), message, style);		
	}

	
	public static int message(Shell shell, String message, int style) {
		MessageBox mb = new MessageBox(shell, style);
		mb.setMessage(message);

		String title = "Message";
		if ((style & SWT.ICON_ERROR) != 0)
			title = "Error";
		else if ((style & SWT.ICON_INFORMATION) != 0)
			title = "Information";
		else if ((style & SWT.ICON_QUESTION) != 0)
			title = "Question";
		else if ((style & SWT.ICON_WARNING) != 0)
			title = "Warning";
		mb.setText(title);

		return mb.open();
	}

	
	public static IContainer getContainer() {
		return container;
	}

	
	private void save() {
		Utils.startCursor(getSShell());
		HashMap changes = new HashMap();
		if(container.getCurrentEditor() instanceof sos.scheduler.editor.conf.forms.SchedulerForm) {
			sos.scheduler.editor.conf.forms.SchedulerForm form =(sos.scheduler.editor.conf.forms.SchedulerForm)container.getCurrentEditor();
			SchedulerDom currdom = (SchedulerDom)form.getDom();
			changes = (java.util.HashMap)((SchedulerDom)currdom).getChangedJob().clone()	;
		}


		/*if(container.getCurrentEditor() instanceof sos.scheduler.editor.actions.forms.ActionsForm) {
			sos.scheduler.editor.actions.forms.ActionsForm form =(sos.scheduler.editor.actions.forms.ActionsForm)container.getCurrentEditor();
			sos.scheduler.editor.actions.ActionsDom currdom = (sos.scheduler.editor.actions.ActionsDom)form.getDom();
			if(currdom.getFilename() == null || currdom.getFilename().length() == 0) {
				sos.scheduler.editor.actions.forms.SaveEventsDialogForm d= new sos.scheduler.editor.actions.forms.SaveEventsDialogForm();

			}
		} */

		if (container.getCurrentEditor().applyChanges()) {
			container.getCurrentEditor().save();
			saveFTP(changes);
			saveWebDav(changes);
			setSaveStatus();
		}		
		Utils.stopCursor(getSShell());
	}

	private void createToolBar() {

		final ToolBar toolBar = new ToolBar(groupmain, SWT.NONE);
		final GridData gridData = new GridData(GridData.BEGINNING, GridData.FILL, true, false);
		toolBar.setLayoutData(gridData);
		final ToolItem butNew = new ToolItem(toolBar, SWT.NONE);
		butNew.setImage(ResourceManager.getImageFromResource("/sos/scheduler/editor/icon_new.gif"));	
		final Menu menu = new Menu(toolBar);
		butNew.setToolTipText("New Confuguration");
		MenuItem itemConfig = new MenuItem(menu, SWT.PUSH);
		itemConfig.setText("Configuration");
		itemConfig.addSelectionListener(new org.eclipse.swt.events.SelectionListener() {
			public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
				if (container.newScheduler() != null)
					setSaveStatus();
			}
			public void widgetDefaultSelected(org.eclipse.swt.events.SelectionEvent e) {
			}
		});
		MenuItem itemDoc = new MenuItem(menu, SWT.PUSH);
		itemDoc.setText("Documentation");
		itemDoc.addSelectionListener(new org.eclipse.swt.events.SelectionListener() {
			public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
				if (container.newDocumentation() != null)
					setSaveStatus();
			}
			public void widgetDefaultSelected(org.eclipse.swt.events.SelectionEvent e) {
			}
		});

		MenuItem itemDetails = new MenuItem(menu, SWT.PUSH);
		itemDetails.setText("Details");
		itemDetails.addSelectionListener(new org.eclipse.swt.events.SelectionListener() {
			public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
				if (container.newDetails() != null)
					setSaveStatus();
			}
			public void widgetDefaultSelected(org.eclipse.swt.events.SelectionEvent e) {
			}
		});

		MenuItem itemActions = new MenuItem(menu, SWT.PUSH);
		itemActions.setText("Event Handler");
		itemActions.addSelectionListener(new org.eclipse.swt.events.SelectionListener() {
			public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
				if (container.newActions() != null)
					setSaveStatus();
			}
			public void widgetDefaultSelected(org.eclipse.swt.events.SelectionEvent e) {
			}
		});
		
		MenuItem itemHFEJob = new MenuItem(menu, SWT.PUSH);
		itemHFEJob.setText("Hot Folder Element - Job");
		itemHFEJob.addSelectionListener(new org.eclipse.swt.events.SelectionListener() {
			public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
				if (container.newScheduler(SchedulerDom.LIFE_JOB) != null)
					setSaveStatus();
			}
			public void widgetDefaultSelected(org.eclipse.swt.events.SelectionEvent e) {
			}
		});

		MenuItem itemHFEJobChain = new MenuItem(menu, SWT.PUSH);
		itemHFEJobChain.setText("Hot Folder Element - Job Chain");
		itemHFEJobChain.addSelectionListener(new org.eclipse.swt.events.SelectionListener() {
			public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
				if (container.newScheduler(SchedulerDom.LIFE_JOB_CHAIN) != null)
					setSaveStatus();
			}
			public void widgetDefaultSelected(org.eclipse.swt.events.SelectionEvent e) {
			}
		});

		MenuItem itemHFEProcessClass = new MenuItem(menu, SWT.PUSH);
		itemHFEProcessClass.setText("Hot Folder Element - Process Class");
		itemHFEProcessClass.addSelectionListener(new org.eclipse.swt.events.SelectionListener() {
			public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
				if (container.newScheduler(SchedulerDom.LIFE_PROCESS_CLASS) != null)
					setSaveStatus();
			}
			public void widgetDefaultSelected(org.eclipse.swt.events.SelectionEvent e) {
			}
		});

		MenuItem itemHFELock = new MenuItem(menu, SWT.PUSH);
		itemHFELock.setText("Hot Folder Element - Lock");
		itemHFELock.addSelectionListener(new org.eclipse.swt.events.SelectionListener() {
			public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
				if (container.newScheduler(SchedulerDom.LIFE_LOCK) != null)
					setSaveStatus();
			}
			public void widgetDefaultSelected(org.eclipse.swt.events.SelectionEvent e) {
			}
		});

		MenuItem itemHFEOrder = new MenuItem(menu, SWT.PUSH);
		itemHFEOrder.setText("Hot Folder Element - Order");
		itemHFEOrder.addSelectionListener(new org.eclipse.swt.events.SelectionListener() {
			public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
				if (container.newScheduler(SchedulerDom.LIFE_ORDER) != null)
					setSaveStatus();
			}
			public void widgetDefaultSelected(org.eclipse.swt.events.SelectionEvent e) {
			}
		});

		MenuItem itemHFEScheduler = new MenuItem(menu, SWT.PUSH);
		itemHFEScheduler.setText("Hot Folder Element - Schedule");
		itemHFEScheduler.addSelectionListener(new org.eclipse.swt.events.SelectionListener() {
			public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
				if (container.newScheduler(SchedulerDom.LIFE_SCHEDULE) != null)
					setSaveStatus();
			}
			public void widgetDefaultSelected(org.eclipse.swt.events.SelectionEvent e) {
			}
		});
		addDropDown(butNew, menu);

		final ToolItem butOpen = new ToolItem(toolBar, SWT.PUSH);
		butOpen.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(final SelectionEvent e) {
				if (container.openQuick() != null)
					setSaveStatus();
			}
		});
		butOpen.setImage(ResourceManager
				.getImageFromResource("/sos/scheduler/editor/icon_open.gif"));
		butOpen.setToolTipText("Open Configuration File");


		final ToolItem butOpenHotFolder = new ToolItem(toolBar, SWT.PUSH);
		butOpenHotFolder.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(final SelectionEvent e) {
				if (container.openDirectory(null) != null)
					setSaveStatus();
			}
		});
		butOpenHotFolder.setImage(ResourceManager
				.getImageFromResource("/sos/scheduler/editor/icon_open_hot_folder.gif"));
		butOpenHotFolder.setToolTipText("Open Hot Folder");


		butSave = new ToolItem(toolBar, SWT.PUSH);
		butSave.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(final SelectionEvent e) {
				save();
			}
		});
		butSave.setImage(ResourceManager
				.getImageFromResource("/sos/scheduler/editor/save.gif"));	
		butSave.setToolTipText("Save Configuration");

		butShowAsSML = new ToolItem(toolBar, SWT.PUSH);
		butShowAsSML.setEnabled(container != null && container.getCurrentEditor() instanceof SchedulerForm);
		butShowAsSML.addSelectionListener(new SelectionAdapter() {			
			public void widgetSelected(final SelectionEvent e) {
				if(container.getCurrentEditor()== null)
					return;
				sos.scheduler.editor.conf.forms.SchedulerForm form =(sos.scheduler.editor.conf.forms.SchedulerForm)container.getCurrentEditor();
				SchedulerDom currdom = (SchedulerDom)form.getDom();

				Utils.showClipboard(Utils.getElementAsString(currdom.getRoot()), getSShell());
			}
		});
		butShowAsSML.setImage(ResourceManager
				.getImageFromResource("/sos/scheduler/editor/icon_view_as_xml.gif"));	
		butShowAsSML.setToolTipText("Show Configuration as XML");
		
		final ToolItem butFTP = new ToolItem(toolBar, SWT.NONE);

		final Menu menuFTP = new Menu(toolBar);
		addDropDown(butFTP, menuFTP);

		butFTP.setImage(ResourceManager.getImageFromResource("/sos/scheduler/editor/icon_open_ftp.gif"));	
		butFTP.setToolTipText("FTP");

		MenuItem itemFTPOpen = new MenuItem(menuFTP, SWT.PUSH);
		itemFTPOpen.setText("Open By FTP");
		itemFTPOpen.addSelectionListener(new org.eclipse.swt.events.SelectionListener() {
			public void widgetSelected(final SelectionEvent e) {
				FTPDialog ftp = new FTPDialog(main);
				ftp.showForm(FTPDialog.OPEN);
			}
			public void widgetDefaultSelected(org.eclipse.swt.events.SelectionEvent e) {
			}
		});


		MenuItem itemFTPOpenHotFolder = new MenuItem(menuFTP, SWT.PUSH);
		itemFTPOpenHotFolder.setText("Open Hot Folder By FTP");
		itemFTPOpenHotFolder.addSelectionListener(new org.eclipse.swt.events.SelectionListener() {
			public void widgetSelected(final SelectionEvent e) {
				FTPDialog ftp = new FTPDialog(main);
				ftp.showForm(FTPDialog.OPEN_HOT_FOLDER);
			}
			public void widgetDefaultSelected(org.eclipse.swt.events.SelectionEvent e) {
			}
		});


		MenuItem itemFTPSave = new MenuItem(menuFTP, SWT.PUSH);
		itemFTPSave.setText("Save As By FTP");
		itemFTPSave.addSelectionListener(new org.eclipse.swt.events.SelectionListener() {
			public void widgetSelected(final SelectionEvent e) {
				saveByFTP();				
			}
			public void widgetDefaultSelected(org.eclipse.swt.events.SelectionEvent e) {
			}
		});
				
		final ToolItem itemReset = new ToolItem(toolBar, SWT.PUSH);
		//itemReset.setEnabled(container != null && (container.getCurrentEditor() instanceof sos.scheduler.editor.actions.forms.ActionsForm || container.getCurrentEditor() instanceof SchedulerForm ));
		itemReset.setImage(ResourceManager
				.getImageFromResource("/sos/scheduler/editor/icon_reset.gif"));
		
		itemReset.addSelectionListener(new org.eclipse.swt.events.SelectionListener() {
			public void widgetSelected(final SelectionEvent e) {
				if(container.getCurrentEditor() instanceof SchedulerForm) {
					SchedulerForm form =(SchedulerForm)container.getCurrentEditor();
					SchedulerDom currdom = (SchedulerDom)form.getDom();
					if(currdom.isLifeElement())
						sos.scheduler.editor.app.Utils.reset( currdom.getRoot(), form, currdom);
					else
						sos.scheduler.editor.app.Utils.reset( currdom.getRoot().getChild("config"), form, currdom);
					
				} else if(container.getCurrentEditor() instanceof sos.scheduler.editor.actions.forms.ActionsForm) {
					 sos.scheduler.editor.actions.forms.ActionsForm form =( sos.scheduler.editor.actions.forms.ActionsForm)container.getCurrentEditor();
					 sos.scheduler.editor.actions.ActionsDom currdom = ( sos.scheduler.editor.actions.ActionsDom)form.getDom();
					 sos.scheduler.editor.app.Utils.reset( currdom.getRoot(),form, currdom);
				} else if(container.getCurrentEditor() instanceof sos.scheduler.editor.doc.forms.DocumentationForm) {
					 sos.scheduler.editor.doc.forms.DocumentationForm form =( sos.scheduler.editor.doc.forms.DocumentationForm)container.getCurrentEditor();
					 sos.scheduler.editor.doc.DocumentationDom currdom = ( sos.scheduler.editor.doc.DocumentationDom)form.getDom();
					 sos.scheduler.editor.app.Utils.reset( currdom.getRoot(),form, currdom);
				}

			}
			public void widgetDefaultSelected(org.eclipse.swt.events.SelectionEvent e) {
			}
		});

		
		final ToolItem butHelp = new ToolItem(toolBar, SWT.PUSH);
		butHelp.setImage(ResourceManager
				.getImageFromResource("/sos/scheduler/editor/icon_help.gif"));
		butHelp.addSelectionListener(new org.eclipse.swt.events.SelectionListener() {
			public void widgetSelected(final SelectionEvent e) {
				if (container.getCurrentEditor() != null) {
					listener.openHelp(container.getCurrentEditor().getHelpKey());					
				}else {
					String msg = "Help is available after documentation or configuration is opened";
					MainWindow.message(msg, SWT.ICON_INFORMATION);
				}
			}
			public void widgetDefaultSelected(org.eclipse.swt.events.SelectionEvent e) {
			}
		});

		
	}

	
	private static void addDropDown(final ToolItem item, final Menu menu) {
		item.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(final SelectionEvent e) {
				Rectangle rect = item.getBounds();
				Point pt = new Point(rect.x, rect.y + rect.height);
				pt = item.getParent().toDisplay(pt);
				menu.setLocation(pt.x, pt.y);
				menu.setVisible(true);
			}
		});	
	}


	private void saveFTP(java.util.HashMap changes) {

		if(container.getCurrentTab().getData("ftp_title") != null && 
				container.getCurrentTab().getData("ftp_title").toString().length()>0) {

			DomParser currdom = null;
			if(container.getCurrentEditor() instanceof SchedulerForm) {
				SchedulerForm form =(SchedulerForm)container.getCurrentEditor();			
				currdom = (SchedulerDom)form.getDom();
			} else if(container.getCurrentEditor() instanceof DocumentationForm) {
				DocumentationForm form =(DocumentationForm)container.getCurrentEditor();			
				currdom = (DocumentationDom)form.getDom();
			}else if(container.getCurrentEditor() instanceof JobChainConfigurationForm) {
				JobChainConfigurationForm form =(JobChainConfigurationForm)MainWindow.getContainer().getCurrentEditor();
				currdom = (DetailDom)form.getDom();
			}

			String profilename = container.getCurrentTab().getData("ftp_profile_name").toString();
			String remoteDir = container.getCurrentTab().getData("ftp_remote_directory").toString();
			ArrayList ftpHotFolderElements = new ArrayList();
			if(container.getCurrentTab().getData("ftp_hot_folder_elements") != null)
				ftpHotFolderElements = (ArrayList)container.getCurrentTab().getData("ftp_hot_folder_elements");

			java.util.Properties profile = (java.util.Properties)container.getCurrentTab().getData("ftp_profile");

			Text txtLog = new Text(getSShell(), SWT.NONE);
			final GridData gridData = new GridData(GridData.BEGINNING, GridData.BEGINNING, false, false);
			gridData.widthHint = 0;
			gridData.heightHint = 0;
			txtLog.setLayoutData(gridData);
			txtLog.setSize(0, 0);
			FTPDialogListener ftpListener = new FTPDialogListener(profile, profilename);
			ftpListener.setLogText(txtLog);
			ftpListener.connect(profilename);
			if(ftpListener.isLoggedIn()) {
				
				if( currdom instanceof SchedulerDom && ((SchedulerDom)currdom).isLifeElement()) {
					String filename = container.getCurrentEditor().getFilename();
					if(!new File(remoteDir).getName().equalsIgnoreCase(new File(filename).getName())){
						//Attribute "name" wurde geändert: Das bedeutet auch Änderungen der life Datei namen.
						ftpListener.removeFile(remoteDir);
						try {
							String newName = sosString.parseToString(new File(remoteDir).getParent()) + "/" + new File(filename).getName();
							newName = newName.replaceAll("\\\\", "/");
							container.getCurrentTab().setData("ftp_remote_directory", newName);
						} catch(Exception e) {
							System.out.println("test: " + e.toString());
							
						} //tu nichts 
					}
					remoteDir = new File(remoteDir).getParent() + "/" + new File(filename).getName();
					ftpListener.saveAs( filename, remoteDir);
					
				} else if( currdom instanceof SchedulerDom && ((SchedulerDom)currdom).isDirectory()) {

					ftpListener.saveHotFolderAs(container.getCurrentEditor().getFilename(), remoteDir, ftpHotFolderElements, changes);

				} else {

					ftpListener.saveAs( container.getCurrentEditor().getFilename(), remoteDir );

				}
				ftpListener.disconnect();
				
			} else {
				MainWindow.message("could not save file on ftp Server", SWT.ICON_WARNING);
			}

			if(ftpListener.hasError()) {
				String text = sos.scheduler.editor.app.Utils.showClipboard(txtLog.getText(), getSShell(), false, "");
				if(text != null)
					txtLog.setText(text);
			} 
		}
	}

	
	private void saveWebDav(java.util.HashMap changes) {
	
		WebDavDialogListener webdavListener = null;
		Text txtLog = null;

		if(container.getCurrentTab().getData("webdav_title") != null && 
				container.getCurrentTab().getData("webdav_title").toString().length()>0) {

			DomParser currdom = null;
			if(container.getCurrentEditor() instanceof SchedulerForm) {
				SchedulerForm form =(SchedulerForm)container.getCurrentEditor();			
				currdom = (SchedulerDom)form.getDom();
			} else if(container.getCurrentEditor() instanceof DocumentationForm) {
				DocumentationForm form =(DocumentationForm)container.getCurrentEditor();			
				currdom = (DocumentationDom)form.getDom();
			}else if(container.getCurrentEditor() instanceof JobChainConfigurationForm) {
				JobChainConfigurationForm form =(JobChainConfigurationForm)MainWindow.getContainer().getCurrentEditor();
				currdom = (DetailDom)form.getDom();
			}

			String profilename = container.getCurrentTab().getData("webdav_profile_name").toString();
			String remoteDir = container.getCurrentTab().getData("webdav_remote_directory").toString();
			ArrayList webdavHotFolderElements = new ArrayList();
			if(container.getCurrentTab().getData("webdav_hot_folder_elements") != null)
				webdavHotFolderElements = (ArrayList)container.getCurrentTab().getData("webdav_hot_folder_elements");

			java.util.Properties profile = (java.util.Properties)container.getCurrentTab().getData("webdav_profile");

			txtLog = new Text(getSShell(), SWT.NONE);
			final GridData gridData = new GridData(GridData.BEGINNING, GridData.BEGINNING, false, false);
			gridData.widthHint = 0;
			gridData.heightHint = 0;
			txtLog.setLayoutData(gridData);
			txtLog.setSize(0, 0);
			webdavListener = new WebDavDialogListener(profile, profilename);
			webdavListener.setLogText(txtLog);
			
			if( currdom instanceof SchedulerDom && ((SchedulerDom)currdom).isLifeElement()) {

				String filename = container.getCurrentEditor().getFilename();
				if(!new File(remoteDir).getName().equalsIgnoreCase(new File(filename).getName())){
					//Attribute "name" wurde geändert: Das bedeutet auch Änderungen der life Datei namen.
					webdavListener.removeFile(remoteDir);
				}

				remoteDir = remoteDir.substring(0, remoteDir.lastIndexOf("/"))+ "/" + new File(filename).getName();


				webdavListener.saveAs( filename, remoteDir);

			} else if( currdom instanceof SchedulerDom && ((SchedulerDom)currdom).isDirectory()) {

				webdavListener.saveHotFolderAs(container.getCurrentEditor().getFilename(), remoteDir, webdavHotFolderElements, changes);

			} else {

				webdavListener.saveAs( container.getCurrentEditor().getFilename(), remoteDir );

			}
		
			if(webdavListener.hasError()) {
				String text = sos.scheduler.editor.app.Utils.showClipboard(txtLog.getText(), getSShell(), false, "");
				if(text != null)
					txtLog.setText(text);
			} 

		} 
	}


	private void saveByFTP() {

		FTPDialog ftp = new FTPDialog(main);

		DomParser currdom = null;
		if(MainWindow.getContainer().getCurrentEditor() instanceof SchedulerForm) {
			SchedulerForm form =(SchedulerForm)MainWindow.getContainer().getCurrentEditor();			
			currdom = (SchedulerDom)form.getDom();
		} else if(MainWindow.getContainer().getCurrentEditor() instanceof DocumentationForm) {
			DocumentationForm form =(DocumentationForm)MainWindow.getContainer().getCurrentEditor();			
			currdom = (DocumentationDom)form.getDom();
		} else if(MainWindow.getContainer().getCurrentEditor() instanceof JobChainConfigurationForm) {
			JobChainConfigurationForm form =(JobChainConfigurationForm)MainWindow.getContainer().getCurrentEditor();
			currdom = (DetailDom)form.getDom();
		}
		 
		if( currdom instanceof SchedulerDom && ((SchedulerDom)currdom).isDirectory()) {
			ftp.showForm(FTPDialog.SAVE_AS_HOT_FOLDER);
		} else
			ftp.showForm(FTPDialog.SAVE_AS);
		
	}
	
	private boolean existLibraries() { 
		boolean libExist = false;
		try{
			try {
				Class.forName("org.apache.commons.logging.LogFactory");
			} catch (Exception e) {
				throw e;
			}
			try {
				Class.forName("org.apache.commons.httpclient.HttpState");
			} catch (Exception e) {
				throw e;
			}
			try {
				Class.forName("org.apache.commons.codec.DecoderException");
			} catch (Exception e) {
				throw e;
			}
			try {
				Class.forName("org.apache.webdav.lib.WebdavResource");
			} catch (Exception e) {
				throw e;
			}


			libExist = true;
		} catch (Exception e){
			/*
			String msg = "Missing libraries to open connection to Webdav Server. \n\n " +
			"\t- webdavclient4j-core-0.92.jar\n" +
			"\t- commons-logging.jar\n" +								
			"\t- commons-codec-1.3.jar\n" +
			"\t- commons-httpclient-3.0.1.jar\n\n" +
			"for more information see https://sourceforge.net/projects/webdavclient4j/";
			
			MainWindow.message(msg, SWT.ICON_WARNING);
			
			try {
				new ErrorLog("error in " + sos.util.SOSClassUtil.getMethodName() + " cause: " + msg, e);
			} catch(Exception ee) {
				//tu nichts
			}
			*/
		}
		return libExist;
		
	}
	
}
