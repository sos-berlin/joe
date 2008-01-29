package sos.scheduler.editor.app;

import java.io.File;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseTrackAdapter;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.jdom.Element;

import sos.scheduler.editor.app.MainListener;
import sos.scheduler.editor.app.IContainer;
import sos.scheduler.editor.app.TabbedContainer;
import sos.scheduler.editor.conf.SchedulerDom;
import sos.scheduler.editor.conf.forms.HotFolderDialog;

public class MainWindow  {
	
	private static Shell sShell             = null; // @jve:decl-index=0:visual-constraint="3,1"
	
	private MainListener listener           = null;
	
	private static IContainer container     = null;
	
	private Menu         menuBar            = null;
	
	private Menu         mFile              = null;
	
	private Menu         submenu            = null;
	
	private Menu         menuLanguages      = null;
	
	private Menu         submenu1           = null;
	
	private MainWindow  main                = null;
	
	public MainWindow() {
		super();
		
	}
	
	
	/**
	 * This method initializes composite
	 */
	private void createContainer() {
		container = new TabbedContainer(this, sShell);
		main = this;
	}
	
	
	/**
	 * This method initializes sShell
	 */
	public void createSShell() {
		sShell = new Shell();
		sShell.setText("Job Scheduler Editor");
		sShell.setData(sShell.getText());
		
		sShell.setImage(ResourceManager.getImageFromResource("/sos/scheduler/editor/editor.png"));
		sShell.setLayout(new FillLayout());
		createContainer();
		
		listener = new MainListener(this, container);
		
		sShell.setSize(new org.eclipse.swt.graphics.Point(895, 625));
		sShell.setMinimumSize(890, 620);
		
		// load resources
		listener.loadOptions();
		listener.loadMessages();
		// Options.loadSash("main", sashForm);
		Options.loadWindow(sShell, "editor");
		
		menuBar = new Menu(sShell, SWT.BAR);
		MenuItem submenuItem2 = new MenuItem(menuBar, SWT.CASCADE);
		submenuItem2.setText("&File");
		mFile = new Menu(submenuItem2);
		
		//neu
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
		
		
		 
		//pmNew.setMenu();
		//
		
		//ok
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
				/*if (container.openDirectory(SchedulerDom.LIFE_JOB) != null)
					setSaveStatus();*/
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
		mLifeJobChain.setText("Job Chain     \tCtrl+A");
		mLifeJobChain.setAccelerator(SWT.CTRL | 'A');
		
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
		
		
		
		//////////
		
		
		//MenuItem separator = new MenuItem(mFile, SWT.SEPARATOR);
		new MenuItem(mFile, SWT.SEPARATOR);
		//ende neu
		//submenu = new Menu(mNew);
		//MenuItem submenuItem1 = new MenuItem(submenu, SWT.CASCADE);
		//submenuItem1.setText("New ffff");
		
		/*MenuItem pNew = new MenuItem(mFile, SWT.PUSH);
		pNew.setText("New Configuration\tCtrl+N");
		pNew.setAccelerator(SWT.CTRL | 'N');
		pNew.addSelectionListener(new org.eclipse.swt.events.SelectionListener() {
			public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
				if (container.newScheduler() != null)
					setSaveStatus();
			}
			
			
			public void widgetDefaultSelected(org.eclipse.swt.events.SelectionEvent e) {
			}
		});
		
		
		MenuItem pOpenFile = new MenuItem(mFile, SWT.PUSH);
		pOpenFile.setText("Open Configuration...\tCtrl+O");
		pOpenFile.setAccelerator(SWT.CTRL | 'O');
		
		MenuItem separator2 = new MenuItem(mFile, SWT.SEPARATOR);
		
		
		MenuItem push = new MenuItem(mFile, SWT.PUSH);
		push.setText("New Documentation\tCtrl+M"); // Generated
		push.addSelectionListener(new org.eclipse.swt.events.SelectionListener() {
			public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
				if (container.newDocumentation() != null)
					setSaveStatus();
			}
			
			
			public void widgetDefaultSelected(org.eclipse.swt.events.SelectionEvent e) {
			}
		});
		MenuItem push1 = new MenuItem(mFile, SWT.PUSH);
		push1.setText("Open Documentation...\tCtrl+P"); // Generated
		push1.addSelectionListener(new org.eclipse.swt.events.SelectionListener() {
			public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
				if (container.openDocumentation() != null)
					setSaveStatus();
			}
			
			
			public void widgetDefaultSelected(org.eclipse.swt.events.SelectionEvent e) {
			}
		});
		MenuItem separator = new MenuItem(mFile, SWT.SEPARATOR);
		
		
		MenuItem pNewDetails = new MenuItem(mFile, SWT.PUSH);
		pNewDetails.setText("New Job Chain Details\tCtrl+F");
		pNewDetails.setAccelerator(SWT.CTRL | 'F');
		pNewDetails.addSelectionListener(new org.eclipse.swt.events.SelectionListener() {
			public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
				
				if (container.newDetails() != null)
					setSaveStatus();
			}
			
			
			public void widgetDefaultSelected(org.eclipse.swt.events.SelectionEvent e) {
			}
		});
		
		MenuItem pOpenDetails = new MenuItem(mFile, SWT.PUSH);
		pOpenDetails.setText("Open Job Chain Details\tCtrl+E");
		pOpenDetails.setEnabled(true);
		pOpenDetails.setAccelerator(SWT.CTRL | 'E');
		pOpenDetails.addSelectionListener(new org.eclipse.swt.events.SelectionListener() {
			public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
				if (container.openDetails() != null)
					setSaveStatus();
			}
			
			
			public void widgetDefaultSelected(org.eclipse.swt.events.SelectionEvent e) {
			}
		});
		
		MenuItem separatorDetails = new MenuItem(mFile, SWT.SEPARATOR);
		
		*/
		
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
		//MenuItem mNew = new MenuItem(mFile, SWT.CASCADE);
		MenuItem mORC = new MenuItem(mFile, SWT.CASCADE);
		mORC.setText("Open Remote Configuration\tCtrl+R");
		mORC.setAccelerator(SWT.CTRL | 'R');
		
		Menu pMOpenGlobalScheduler = new Menu(mORC);
		
		MenuItem pOpenGlobalScheduler = new MenuItem(pMOpenGlobalScheduler, SWT.PUSH);
		pOpenGlobalScheduler.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(final SelectionEvent e) {

				//String globalSchedulerPath = Options.getSchedulerHotFolder().endsWith("/") || Options.getSchedulerHotFolder().endsWith("\\") ? Options.getSchedulerHotFolder() : Options.getSchedulerHotFolder() + "/";
				//globalSchedulerPath = globalSchedulerPath + "#.scheduler";
				String globalSchedulerPath = Options.getSchedulerHome().endsWith("/") || Options.getSchedulerHome().endsWith("\\") ? Options.getSchedulerHome() : Options.getSchedulerHome() + "/";
				globalSchedulerPath = globalSchedulerPath + "config/remote/_all";
				File f = new java.io.File(globalSchedulerPath); 
				if(!f.exists()) {
					if(!f.mkdirs()) {
						MainWindow.message("could not create Global Scheduler Configurations: " + globalSchedulerPath, SWT.ICON_WARNING);
						return;
					}
				}
				
				if (container.openDirectory(globalSchedulerPath) != null)
					setSaveStatus();
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
		//
		
		//MenuItem separatorDetails1 = new MenuItem(mFile, SWT.SEPARATOR);
		new MenuItem(mFile, SWT.SEPARATOR);
		
		/*
		
		pOpenFile.addSelectionListener(new org.eclipse.swt.events.SelectionListener() {
			public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
				if (container.openScheduler() != null)
					setSaveStatus();
			}
			
			
			public void widgetDefaultSelected(org.eclipse.swt.events.SelectionEvent e) {
			}
		});
		*/
		MenuItem pSaveFile = new MenuItem(mFile, SWT.PUSH);
		pSaveFile.setText("Save                                    \tCtrl+S");
		pSaveFile.setAccelerator(SWT.CTRL | 'S');
		pSaveFile.setEnabled(false);
		pSaveFile.addSelectionListener(new org.eclipse.swt.events.SelectionListener() {
			public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
				if (container.getCurrentEditor().applyChanges()) {
					container.getCurrentEditor().save();
					setSaveStatus();
					
//					test
					/*try {
						sos.scheduler.editor.conf.forms.SchedulerForm form =(sos.scheduler.editor.conf.forms.SchedulerForm)container.getCurrentEditor();
						
						SchedulerDom currdom = (SchedulerDom)form.getDom();
						currdom.read(currdom.getFilename());
					} catch (Exception ex) {
						System.out.println("test" + ex.getMessage());
					}
					*/
					//ende test
				}
			}
			
			
			
			public void widgetDefaultSelected(org.eclipse.swt.events.SelectionEvent e) {
			}
		});
		MenuItem pSaveAs = new MenuItem(mFile, SWT.PUSH);
		pSaveAs.setText("Save As                            \tCtrl+A");
		pSaveAs.setAccelerator(SWT.CTRL | 'A');
		pSaveAs.setEnabled(false);
		pSaveAs.addSelectionListener(new org.eclipse.swt.events.SelectionListener() {
			public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
				if (container.getCurrentEditor() != null && container.getCurrentEditor().applyChanges()) {
					
					container.getCurrentEditor().saveAs();
					setSaveStatus();
				}
			}
			
			
			public void widgetDefaultSelected(org.eclipse.swt.events.SelectionEvent e) {
			}
		});
		
		
		//test begin
		MenuItem pSaveAsHotFolderElement = new MenuItem(mFile, SWT.PUSH);
		pSaveAsHotFolderElement.setText("Save As Hot Folder Elements   \tCtrl+B");		
		pSaveAsHotFolderElement.setAccelerator(SWT.CTRL | 'B');
		pSaveAsHotFolderElement.setEnabled(false);
		pSaveAsHotFolderElement.addSelectionListener(new org.eclipse.swt.events.SelectionListener() {
			public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
				if (container.getCurrentEditor() != null && container.getCurrentEditor().applyChanges()) {
					sos.scheduler.editor.conf.forms.SchedulerForm form =(sos.scheduler.editor.conf.forms.SchedulerForm)container.getCurrentEditor();
					
					SchedulerDom currdom = (SchedulerDom)form.getDom();
					
					if(IOUtils.saveDirectory(currdom, true, SchedulerDom.DIRECTORY, null, container)) {
						Element root = currdom.getRoot();
						if(root != null) {
							Element config = root.getChild("config");
							if(config != null) {
								config.removeChildren("jobs");								
								config.removeChildren("job_chains");
								config.removeChildren("locks");
								config.removeChildren("process_classes");
								config.removeChildren("commands");
								
								//IOUtils.saveFile(currdom, false);
								if (container.getCurrentEditor().applyChanges()) {
									container.getCurrentEditor().save();
									setSaveStatus();
								}
								form.updateTree("main");
								form.update();
								
							}
						}
					}
					//container.getCurrentEditor().saveAs();
					setSaveStatus();
				}
			}
			
			
			public void widgetDefaultSelected(org.eclipse.swt.events.SelectionEvent e) {
			}
		});
		
		//test ende
		
		//MenuItem separator1 = new MenuItem(mFile, SWT.SEPARATOR);
		new MenuItem(mFile, SWT.SEPARATOR);
		
		submenuItem2.setMenu(mFile);
		MenuItem pExit = new MenuItem(mFile, SWT.PUSH);
		pExit.setText("Exit\tCtrl+E");
		pExit.setAccelerator(SWT.CTRL | 'E');
		
		/*MenuItem submenuItemOpen = new MenuItem(menuBar, SWT.CASCADE);
		submenuItemOpen.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(final SelectionEvent e) {
				//parsiere die XML Datei und erkenne, ob du eine Konfiguration oder Dokumentation bis
				if (container.openQuick() != null)
					setSaveStatus();
			}
		});
		submenuItemOpen.setText("&Quick Open");
		*/
		
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
		
		pExit.addSelectionListener(new org.eclipse.swt.events.SelectionListener() {
			public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
				sShell.close();
			}
			
			
			public void widgetDefaultSelected(org.eclipse.swt.events.SelectionEvent e) {
			}
		});
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
		if (container.getCurrentEditor() != null)
			saved = !container.getCurrentEditor().hasChanges();
		
		MenuItem[] items = mFile.getItems();
		int index = 0;
		for (int i =0; i < items.length; i++){
			MenuItem item = items[i];
			if(item.getText().startsWith("Save")) {
				index = i;
				break;
			} 
		}
		
		//items[index].setEnabled(!saved);
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
			} else 
				items[index+2].setEnabled(false);
			
		} else 
			items[index+2].setEnabled(false);
		
		return saved;
	}
	
	
	public static int message(String message, int style) {
		return message(getSShell(), message, style);
		/* MessageBox mb = new MessageBox(getSShell(), style);
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
		 */
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
		
}
