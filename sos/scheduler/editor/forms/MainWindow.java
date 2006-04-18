/**
 * 
 */
package sos.scheduler.editor.forms;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;

import sos.scheduler.editor.app.DomParser;
import sos.scheduler.editor.app.IUpdate;
import sos.scheduler.editor.app.IUpdateLanguage;
import sos.scheduler.editor.app.Options;
import sos.scheduler.editor.app.TreeData;
import sos.scheduler.editor.app.TreeMenu;
import sos.scheduler.editor.app.Utils;
import sos.scheduler.editor.listeners.MainListener;

/**
 * @author sky2000
 * 
 */
public class MainWindow implements IUpdate {
	private static final String GROUP_TREE = "Scheduler Elements";

	private DomParser dom;

	private MainListener listener;

	private TreeItem selection;

	private Shell sShell = null; // @jve:decl-index=0:visual-constraint="3,1"

	private Menu menuBar = null;

	private Composite composite = null;

	private SashForm sashForm = null;

	private Group gTree = null;

	private Composite cMainForm = null;

	private Tree tree = null;

	private Menu mFile = null;

	private Menu submenu = null;

	private Menu menuLanguages = null;

	private Menu submenu1 = null;

	/**
	 * 
	 */
	public MainWindow(DomParser dom) {
		super();
		listener = new MainListener(this, dom);
		this.dom = dom;
	}

	/**
	 * This method initializes composite
	 * 
	 */
	private void createComposite() {
		FillLayout fillLayout = new FillLayout();
		fillLayout.spacing = 0;
		fillLayout.marginWidth = 5;
		fillLayout.marginHeight = 5;
		composite = new Composite(sShell, SWT.NONE);
		createSashForm();
		composite.setLayout(fillLayout);
	}

	/**
	 * This method initializes sashForm
	 * 
	 */
	private void createSashForm() {
		sashForm = new SashForm(composite, SWT.NONE);
		createGTree();
		createCMainForm();
		sashForm.setWeights(new int[] { 20, 80 });
	}

	/**
	 * This method initializes gTree
	 * 
	 */
	private void createGTree() {
		gTree = new Group(sashForm, SWT.NONE);
		gTree.setLayout(new FillLayout());
		gTree.setText("Scheduler Elements");
		tree = new Tree(gTree, SWT.BORDER);
		tree.setMenu(new TreeMenu(tree, dom, this).getMenu());
		tree.addListener(SWT.MenuDetect, new Listener() {
			public void handleEvent(Event e) {
				e.doit = tree.getSelectionCount() > 0;
			}
		});
		tree.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event e) {
				if (tree.getSelectionCount() > 0) {
					if (selection == null)
						selection = tree.getItem(0);

					e.doit = listener.treeSelection(tree, cMainForm);

					if (!e.doit) {
						tree.setSelection(new TreeItem[] { selection });
					} else {
						selection = tree.getSelection()[0];
					}
				}
			}
		});
	}

	/**
	 * This method initializes cMainForm
	 * 
	 */
	private void createCMainForm() {
		cMainForm = new Composite(sashForm, SWT.NONE);
		cMainForm.setLayout(new FillLayout());
	}

	/**
	 * This method initializes sShell
	 */
	public void createSShell() {
		sShell = new Shell();
		sShell.setText("Job Scheduler Editor");

		try {
			sShell.setImage(new Image(sShell.getDisplay(), getClass()
					.getResourceAsStream("/sos/scheduler/editor/editor.png")));
		} catch (Exception e) {
			e.printStackTrace();
		}

		sShell.setLayout(new FillLayout());
		createComposite();
  	sShell.setSize(new org.eclipse.swt.graphics.Point(895, 625));
  	sShell.setMinimumSize(890, 620);


		// load resources
		listener.loadOptions();
		listener.loadMessages();
		Options.loadWindow(sShell);
		Options.loadSash("main", sashForm);


		menuBar = new Menu(sShell, SWT.BAR);
		MenuItem submenuItem2 = new MenuItem(menuBar, SWT.CASCADE);
		submenuItem2.setText("&File");
		mFile = new Menu(submenuItem2);
		MenuItem pNew = new MenuItem(mFile, SWT.PUSH);
		pNew.setText("New\tCtrl+N");
		pNew.setAccelerator(SWT.CTRL | 'N');
		pNew
				.addSelectionListener(new org.eclipse.swt.events.SelectionListener() {
					public void widgetSelected(
							org.eclipse.swt.events.SelectionEvent e) {
						if (applyChanges()) {
							setMenuSaveStatus(!listener.newScheduler(tree,
									cMainForm));
						}
					}

					public void widgetDefaultSelected(
							org.eclipse.swt.events.SelectionEvent e) {
					}
				});
		MenuItem pOpenFile = new MenuItem(mFile, SWT.PUSH);
		pOpenFile.setText("Open File...\tCtrl+O");
		pOpenFile.setAccelerator(SWT.CTRL | 'O');
		MenuItem separator2 = new MenuItem(mFile, SWT.SEPARATOR);
		pOpenFile
				.addSelectionListener(new org.eclipse.swt.events.SelectionListener() {
					public void widgetSelected(
							org.eclipse.swt.events.SelectionEvent e) {
						if (applyChanges()) {
							listener.openFile(tree, cMainForm);
							setMenuSaveStatus(false);
						}
					}

					public void widgetDefaultSelected(
							org.eclipse.swt.events.SelectionEvent e) {
					}
				});
		MenuItem pSaveFile = new MenuItem(mFile, SWT.PUSH);
		pSaveFile.setText("Save\tCtrl+S");
		pSaveFile.setAccelerator(SWT.CTRL | 'S');
		pSaveFile.setEnabled(false);
		pSaveFile
				.addSelectionListener(new org.eclipse.swt.events.SelectionListener() {
					public void widgetSelected(
							org.eclipse.swt.events.SelectionEvent e) {
						if (applyChanges())
							setMenuSaveStatus(!listener.saveFile(false));
					}

					public void widgetDefaultSelected(
							org.eclipse.swt.events.SelectionEvent e) {
					}
				});
		MenuItem pSaveAs = new MenuItem(mFile, SWT.PUSH);
		pSaveAs.setText("Save As...\tCtrl+A");
		pSaveAs.setAccelerator(SWT.CTRL | 'A');
		pSaveAs.setEnabled(false);
		pSaveAs
				.addSelectionListener(new org.eclipse.swt.events.SelectionListener() {
					public void widgetSelected(
							org.eclipse.swt.events.SelectionEvent e) {
						if (applyChanges())
							setMenuSaveStatus(!listener.saveFile(true));
					}

					public void widgetDefaultSelected(
							org.eclipse.swt.events.SelectionEvent e) {
					}
				});
		MenuItem separator1 = new MenuItem(mFile, SWT.SEPARATOR);
		submenuItem2.setMenu(mFile);
		MenuItem pExit = new MenuItem(mFile, SWT.PUSH);
		pExit.setText("Exit\tCtrl+E");
		pExit.setAccelerator(SWT.CTRL | 'E');
		MenuItem submenuItem = new MenuItem(menuBar, SWT.CASCADE);
		submenuItem.setText("Options");
		MenuItem submenuItem3 = new MenuItem(menuBar, SWT.CASCADE);
		submenuItem3.setText("&Help");
		submenu1 = new Menu(submenuItem3);
		MenuItem pHelp = new MenuItem(submenu1, SWT.PUSH);
		pHelp.setText("Help\tF1");
		pHelp.setAccelerator(SWT.F1);
		pHelp
				.addSelectionListener(new org.eclipse.swt.events.SelectionListener() {
					public void widgetSelected(
							org.eclipse.swt.events.SelectionEvent e) {
						listener.openHelp(tree);
					}

					public void widgetDefaultSelected(
							org.eclipse.swt.events.SelectionEvent e) {
					}
				});
		MenuItem pAbout = new MenuItem(submenu1, SWT.PUSH);
		pAbout.setText("About");
		pAbout
				.addSelectionListener(new org.eclipse.swt.events.SelectionListener() {
					public void widgetSelected(
							org.eclipse.swt.events.SelectionEvent e) {
						listener.showAbout();
					}

					public void widgetDefaultSelected(
							org.eclipse.swt.events.SelectionEvent e) {
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
		pExit
				.addSelectionListener(new org.eclipse.swt.events.SelectionListener() {
					public void widgetSelected(
							org.eclipse.swt.events.SelectionEvent e) {
						sShell.close();
					}

					public void widgetDefaultSelected(
							org.eclipse.swt.events.SelectionEvent e) {
					}
				});
		sShell.setMenuBar(menuBar);
		sShell.addShellListener(new org.eclipse.swt.events.ShellAdapter() {
			public void shellClosed(org.eclipse.swt.events.ShellEvent e) {
				e.doit = applyChanges() && listener.close();
				setMenuSaveStatus(dom.isChanged());
				Options.saveWindow(sShell);
				Options.saveSash("main",sashForm.getWeights());
				//Options.saveGrid( )
				listener.saveOptions();
			}

			public void shellActivated(org.eclipse.swt.events.ShellEvent e) {
				// setMenuSaveStatus(false);
			}
		});
	}

	public Shell getSShell() {
		return sShell;
	}

	private void setMenuSaveStatus(boolean enabled) {
		MenuItem[] items = mFile.getItems();
		items[3].setEnabled(enabled);
		items[4].setEnabled(true);
		String title = (enabled ? "*" : "") + GROUP_TREE;
		gTree.setText(title);
	}

	public void updateJobs() {
		if (tree.getSelectionCount() > 0)
			listener.treeFillJobs(tree.getSelection()[0]);
	}

 
	
	public void updateJob() {
		if (tree.getSelectionCount() > 0) {
			TreeItem item = tree.getSelection()[0];
			TreeData data = (TreeData) item.getData();
			listener.treeFillJob(item, data.getElement(), true);
		}
	}
	
	public void updateJob(String s) {
		TreeItem item = tree.getSelection()[0];
    String job = "Job: " + s;
		item.setText(job);  
	}

	public void updateDays(int type) {
		if (tree.getSelectionCount() > 0) {
			TreeItem item = tree.getSelection()[0];
			TreeData data = (TreeData) item.getData();
			listener.treeFillDays(item, data.getElement(), type, true);
		}
	}

	public void dataChanged() {
		setMenuSaveStatus(true);
	}

	public void openFile(String filename) {
		if (listener.openFile(tree, cMainForm, filename))
			setMenuSaveStatus(true);
	}

	private boolean applyChanges() {
		Control[] c = cMainForm.getChildren();
		return c.length == 0 || listener.applyChanges(c[0]);
	}
	
	public IUpdateLanguage getForm(){
		if (cMainForm.getChildren().length > 0){
			if (cMainForm.getChildren()[0] instanceof IUpdateLanguage ){
				   return (IUpdateLanguage) cMainForm.getChildren()[0];
			}
				
  	}
		return null;
	}
}
