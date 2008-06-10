package sos.scheduler.editor.app;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabFolder2Adapter;
import org.eclipse.swt.custom.CTabFolderEvent;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.events.TraverseEvent;
import org.eclipse.swt.events.TraverseListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Shell;
import org.jdom.input.SAXBuilder;
import sos.scheduler.editor.conf.SchedulerDom;
import sos.scheduler.editor.conf.forms.SchedulerForm;
import sos.scheduler.editor.doc.forms.DocumentationForm;
import sos.scheduler.editor.conf.forms.JobChainConfigurationForm;

public class TabbedContainer implements IContainer {


	private static final String NEW_SCHEDULER_TITLE     = "Unknown";

	private static final String NEW_DOCUMENTATION_TITLE = "Unknown";

	private static final String NEW_DETAIL_TITLE        = "Unknown";

	private CTabFolder          folder                  = null;

	private MainWindow          window                  = null;

	private ArrayList           filelist                = new ArrayList();

	private HashMap             ftpfilelist             = new HashMap();


	class TabData{
		protected String title="";
		protected String caption="";
		protected int cnt=0;

		public TabData (String title,String caption) {
			this.title = title;
			this.caption = caption;
		}
	}	    


	public TabbedContainer(MainWindow window, Composite parent) {
		this.window = window;		
		folder = new CTabFolder(parent, SWT.TOP | SWT.CLOSE);
		
		folder.setLayoutData(new GridData(GridData.FILL, GridData.FILL, true, true));
		initialize();
	}

	private void initialize() {
		folder.setSimple(false);
		folder.setSize(new Point(690, 478));
		folder.setLayout(new FillLayout());

		// on tab selection
		folder.addSelectionListener(new SelectionListener() {
			public void widgetSelected(SelectionEvent e) {
				setWindowTitle();
				window.setMenuStatus();
			}


			public void widgetDefaultSelected(SelectionEvent e) {
				widgetSelected(e);
			}
		});

		
		
		// on tab close
		folder.addCTabFolder2Listener(new CTabFolder2Adapter() {
			public void close(CTabFolderEvent event) {

				//IEditor editor = getCurrentEditor();
				IEditor editor = (IEditor)((CTabItem)(event.item)).getControl();
				if (editor.hasChanges()) {
					event.doit = editor.close();
				}
				if (event.doit)
					filelist.remove(editor.getFilename());
			}
		});
		
		folder.addTraverseListener(new TraverseListener() {
			public void keyTraversed(final TraverseEvent e) {
				
				
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


	public SchedulerForm newScheduler() {
		SchedulerForm scheduler = new SchedulerForm(this, folder, SWT.NONE);
		scheduler.openBlank();
		CTabItem tab = newItem(scheduler, NEW_SCHEDULER_TITLE);
		tab.setImage(new Image(tab.getDisplay(), getClass().getResourceAsStream(
				"/sos/scheduler/editor/editor-small.png")));
		return scheduler;
	}

	public SchedulerForm newScheduler(int type) {
		SchedulerForm scheduler = new SchedulerForm(this, folder, SWT.NONE, type);
		scheduler.openBlank(type);
		CTabItem tab = newItem(scheduler, NEW_SCHEDULER_TITLE);
		tab.setImage(new Image(tab.getDisplay(), getClass().getResourceAsStream(
				"/sos/scheduler/editor/editor-small.png")));
		return scheduler;
	}
	public JobChainConfigurationForm newDetails() {
		JobChainConfigurationForm detailForm = new JobChainConfigurationForm(this, folder, SWT.NONE);
		detailForm.openBlank();
		CTabItem tab = newItem(detailForm, NEW_DETAIL_TITLE);
		tab.setImage(new Image(tab.getDisplay(), getClass().getResourceAsStream(
				"/sos/scheduler/editor/editor-small.png")));
		return detailForm;
	}


	public JobChainConfigurationForm openDetails() {
		JobChainConfigurationForm detailForm = new JobChainConfigurationForm(this, folder, SWT.NONE);

		if(detailForm.open(filelist)) {
			CTabItem tab = newItem(detailForm, detailForm.getFilename());
			tab.setImage(new Image(tab.getDisplay(), getClass().getResourceAsStream(
					"/sos/scheduler/editor/editor-small.png")));
			return detailForm;
		} else 
			return null;
	}

	public JobChainConfigurationForm openDetails(String filename) {
		JobChainConfigurationForm detailForm = new JobChainConfigurationForm(this, folder, SWT.NONE);

		if(detailForm.open(filename, filelist)) {
			CTabItem tab = newItem(detailForm, detailForm.getFilename());
			tab.setImage(new Image(tab.getDisplay(), getClass().getResourceAsStream(
					"/sos/scheduler/editor/editor-small.png")));
			return detailForm;
		} else 
			return null;
	}

	public SchedulerForm openScheduler() {
		SchedulerForm scheduler = new SchedulerForm(this, folder, SWT.NONE);
		if (scheduler.open(filelist)) {
			CTabItem tab = newItem(scheduler, scheduler.getFilename());
			tab.setImage(ResourceManager.getImageFromResource("/sos/scheduler/editor/editor-small.png"));
			return scheduler;
		} else
			return null;
	}



	public SchedulerForm openScheduler(String filename) {
		SchedulerForm scheduler = new SchedulerForm(this, folder, SWT.NONE);
		if (scheduler.open(filename, filelist)) {
			CTabItem tab = newItem(scheduler, scheduler.getFilename());
			tab.setImage(ResourceManager.getImageFromResource("/sos/scheduler/editor/editor-small.png"));
			return scheduler;
		} else
			return null;
	}


	public DocumentationForm newDocumentation() {
		DocumentationForm doc = new DocumentationForm(this, folder, SWT.NONE);
		doc.openBlank();
		newItem(doc, NEW_DOCUMENTATION_TITLE);
		return doc;
	}


	public DocumentationForm openDocumentation(){
		try {
			DocumentationForm doc = new DocumentationForm(this, folder, SWT.NONE);
			if (doc.open(filelist)) {
				//CTabItem tab = newItem(doc, doc.getFilename());
				newItem(doc, doc.getFilename());
				return doc;
			} else 
				return null;
		} catch (Exception e) {
			try {
				new ErrorLog("error in " + sos.util.SOSClassUtil.getMethodName() , e);
			} catch(Exception ee) {
				//tu nichts
			}
			System.out.println("error in TabbedContainer.openDocumentation()" + e.getMessage() );
			return null;
		}
	}

	public DocumentationForm openDocumentation(String filename){
		try {
			DocumentationForm doc = new DocumentationForm(this, folder, SWT.NONE);
			if (doc.open(filename, filelist)) {
				//CTabItem tab = newItem(doc, doc.getFilename());
				newItem(doc, doc.getFilename());
				// tab.setImage(ResourceManager.getImageFromResource("/sos/scheduler/editor/editor-small.png"));
				return doc;
			} else 
				return null;
		} catch (Exception e) {    	
			try {
				new ErrorLog("error in " + sos.util.SOSClassUtil.getMethodName() , e);
			} catch(Exception ee) {
				//tu nichts
			}
			System.out.println("error in TabbedContainer.openDocumentation()" + e.getMessage() );
			return null;
		}
	}

	public String openDocumentationName(){
		try {
			DocumentationForm doc = new DocumentationForm(this, folder, SWT.NONE);
			if (doc.open(filelist)) { 
				//CTabItem tab = newItem(doc, doc.getFilename());
				// tab.setImage(ResourceManager.getImageFromResource("/sos/scheduler/editor/editor-small.png"));
				return doc.getFilename();
			} else 
				return null;
		} catch (Exception e) {    
			try {
				new ErrorLog("error in " + sos.util.SOSClassUtil.getMethodName() , e);
			} catch(Exception ee) {
				//tu nichts
			}
			System.out.println("error in TabbedContainer.openDocumentation()" + e.getMessage() );
			return null;
		}
	}

	private CTabItem newItem(Control control, String filename) {
		CTabItem tab = new CTabItem(folder, SWT.NONE);
		tab.addDisposeListener(new DisposeListener() {
			public void widgetDisposed(final DisposeEvent e) {        		        		
				MainWindow.getSShell().setText("Job Scheduler Editor");
				window.setSaveStatus();

			}
		});
		tab.setControl(control);
		folder.setSelection(folder.indexOf(tab));        
		tab.setData(new TabData(Utils.getFileFromURL(filename),""));
		String title = setSuffix(tab,Utils.getFileFromURL(filename));
		TabData t = (TabData)tab.getData();
		t.caption = title;
		tab.setToolTipText(filename);
		tab.setText(title);

		if (!filename.equals(NEW_DOCUMENTATION_TITLE) && !filename.equals(NEW_SCHEDULER_TITLE))
			filelist.add(filename);

		return tab;
	}


	public CTabItem getCurrentTab() {
		if (folder.getItemCount() == 0)
			return null;
		else
			return folder.getItem(folder.getSelectionIndex());
	}


	public IEditor getCurrentEditor() {
		if (folder.getItemCount() == 0)
			return null;
		else
			return (IEditor) getCurrentTab().getControl();
	}


	public void setStatusInTitle() {
		if (folder.getItemCount() == 0)
			return;

		CTabItem tab = getCurrentTab();
		TabData t = (TabData) tab.getData();
		String title = t.caption;

		if (tab.getData("ftp_profile_name") != null && tab.getData("ftp_profile_name").toString().length() > 0 && 
				tab.getData("ftp_remote_directory") != null && tab.getData("ftp_remote_directory").toString().length() > 0)
			title = tab.getData("ftp_remote_directory").toString();

		if (tab.getData("webdav_profile_name") != null && tab.getData("webdav_profile_name").toString().length() > 0 && 
				tab.getData("webdav_remote_directory") != null && tab.getData("webdav_remote_directory").toString().length() > 0)
			title = tab.getData("webdav_remote_directory").toString();

		tab.setText(getCurrentEditor().hasChanges() == false ? title : "*" + title);        
		setWindowTitle();
		window.setMenuStatus();
	}


	public void setNewFilename(String oldFilename) {
		if (folder.getItemCount() == 0)
			return;
		String filename = getCurrentEditor().getFilename();
		CTabItem tab = getCurrentTab();
		if (oldFilename != null) {
			filelist.remove(oldFilename);
			filelist.add(filename);
		}

		String title = setSuffix(tab,Utils.getFileFromURL(filename));
		if(tab.getData("ftp_remote_directory") != null && tab.getData("ftp_remote_directory").toString().length() > 0 
				&& tab.getData("ftp_profile_name") != null && tab.getData("ftp_profile_name").toString().length() > 0)
			title = tab.getData("ftp_remote_directory").toString();
		tab.setText(title);
		tab.setToolTipText(filename);
		tab.setData(new TabData(Utils.getFileFromURL(filename),title));
		setWindowTitle();
	}


	public void setNewFilename(String oldFilename, String newFilename) {
		if (folder.getItemCount() == 0)
			return;

		CTabItem tab = getCurrentTab();
		if (oldFilename != null) {
			filelist.remove(oldFilename);
			filelist.add(newFilename);
		}

		String title = setSuffix(tab,Utils.getFileFromURL(newFilename));
		tab.setText(title);
		tab.setToolTipText(newFilename);
		tab.setData(new TabData(Utils.getFileFromURL(newFilename),title));
		setWindowTitle();
	}


	private void setWindowTitle() {
		Shell shell = folder.getShell();
		String ftp = getCurrentTab().getData("ftp_title") != null ? getCurrentTab().getData("ftp_title").toString() + "\\": "";	
		
		String webdav = getCurrentTab().getData("webdav_title") != null ? getCurrentTab().getData("webdav_title").toString() + "\\": "";
		/*if(ftp != null && ftp.length() > 0  ) {
        	String f = new File(getCurrentTab().getText()).getName();
        }*/
		shell.setText((String) shell.getData() + webdav + ftp + " " +getCurrentTab().getText());
		//shell.setText((String) shell.getData() + " [" + getCurrentTab().getText() + "]");
	}


	private String setSuffix(CTabItem tab,String title) {
		int sameTitles = getSameTitles(title);
		TabData t = (TabData) tab.getData();
		if (t != null){
			t.cnt=sameTitles;
			if (sameTitles > 0)
				title = title + "(" + (sameTitles + 1) + ")";
		}
		return title;
	}


	private boolean isFreeIndex(int index,String title) {
		boolean found=false;
		CTabItem[] items = folder.getItems();
		int i=0;
		while (i < items.length && !found) {
			TabData t = (TabData) items[i].getData();          
			if (items[i].getData() != null && t.title.equals(title) && t.cnt==index && !items[i].equals(getCurrentTab())) {
				found = true;
			}
			i++;
		}

		return !found;
	}

	private int getSameTitles(String title) {
		int cnt = -1;
		int i=0;
		//boolean found = false;

		while (cnt==-1) {
			if (isFreeIndex(i, title)){
				//found = true;
				cnt=i;
			}
			i++;
		}
		return cnt;
	}


	public boolean closeAll() {
		boolean doit = true;
		for (int i = 0; i < folder.getItemCount(); i++) {
			CTabItem tab = folder.getItem(i);
			folder.setSelection(i);
			if (((IEditor) tab.getControl()).close()) {
				tab.dispose();
				i--;
			} else {
				doit = false;
			}
		}
		return doit;
	}

	public void closeCurrentTab() {
		if(folder.getSelectionIndex() > -1) {
			CTabItem tab = folder.getSelection();
			tab.dispose();       
		} else {
			getCurrentTab().dispose();
		}
	}

	public void updateLanguages() {
		for (int i = 0; i < folder.getItemCount(); i++) {
			CTabItem tab = folder.getItem(i);
			((IEditor) tab.getControl()).updateLanguage();
		}
	}

	/* public SchedulerForm openDirectory() {
        SchedulerForm scheduler = new SchedulerForm(this, folder, SWT.NONE, SchedulerDom.DIRECTORY);

        if (scheduler.openDirectory(null, filelist)) {
            CTabItem tab = newItem(scheduler, scheduler.getFilename());
            tab.setImage(ResourceManager.getImageFromResource("/sos/scheduler/editor/editor-small.png"));
            return scheduler;
        } else
            return null;
    }*/

	public SchedulerForm openDirectory(String filename) {

		SchedulerForm scheduler = new SchedulerForm(this, folder, SWT.NONE, SchedulerDom.DIRECTORY);

		if (scheduler.openDirectory(filename, filelist)) {
			CTabItem tab = newItem(scheduler, scheduler.getFilename());
			tab.setImage(ResourceManager.getImageFromResource("/sos/scheduler/editor/editor-small.png"));
			return scheduler;
		} else
			return null;
	}

	public SchedulerForm openLifeElement(String filename, int type) {
		SchedulerForm scheduler = new SchedulerForm(this, folder, SWT.NONE, type);
		if (scheduler.open(filename, filelist, type)) {
			CTabItem tab = newItem(scheduler, scheduler.getFilename());
			tab.setImage(ResourceManager.getImageFromResource("/sos/scheduler/editor/editor-small.png"));
			return scheduler;
		} else
			return null;
	}


	public org.eclipse.swt.widgets.Composite openQuick(String xmlFilename) {

		try {    		    	
			if (xmlFilename != null && xmlFilename.length() > 0) {
				SAXBuilder builder = new SAXBuilder();   		
				org.jdom.Document doc = builder.build( new File( xmlFilename ) );    		
				org.jdom.Element root = doc.getRootElement();
				if(root.getName().equalsIgnoreCase("description")) {
					return openDocumentation(xmlFilename);
				} else if(root.getName().equalsIgnoreCase("spooler")) {
					return openScheduler(xmlFilename);
				} else if(root.getName().equalsIgnoreCase("settings")) {
					return openDetails(xmlFilename);
				} else if(root.getName().equalsIgnoreCase("job")) {
					return openLifeElement(xmlFilename, SchedulerDom.LIFE_JOB);
				} else if(root.getName().equalsIgnoreCase("job_chain")) {
					return openLifeElement(xmlFilename, SchedulerDom.LIFE_JOB_CHAIN);
				} else if(root.getName().equalsIgnoreCase("process_class")) {
					return openLifeElement(xmlFilename, SchedulerDom.LIFE_PROCESS_CLASS);
				} else if(root.getName().equalsIgnoreCase("lock")) {
					return openLifeElement(xmlFilename, SchedulerDom.LIFE_LOCK);
				} else if(root.getName().equalsIgnoreCase("order") || root.getName().equalsIgnoreCase("add_order")) {
					return openLifeElement(xmlFilename, SchedulerDom.LIFE_ORDER);
				} else if(root.getName().equalsIgnoreCase("schedule")) {
					return openLifeElement(xmlFilename, SchedulerDom.LIFE_SCHEDULE);
				} else {
					MainWindow.message("Unknows root Element: " + root.getName() + " from filename " + xmlFilename, SWT.NONE);
				}
			}
		} catch (Exception e) {
			try {
				new ErrorLog("error in " + sos.util.SOSClassUtil.getMethodName() + " ; could not open file " + xmlFilename , e);
			} catch(Exception ee) {
				//tu nichts
			}
			MainWindow.message("could not open file cause" + e.getMessage(), SWT.NONE);
		}
		return null; 
	}

	public org.eclipse.swt.widgets.Composite openQuick() {
		String xmlFilename = "";
		try {

			FileDialog fdialog = new FileDialog(MainWindow.getSShell(), SWT.OPEN);            	            	
			fdialog.setFilterPath(Options.getLastDirectory());
			fdialog.setText("Open");
			fdialog.setFilterExtensions(new String[] {"*.xml"});
			xmlFilename = fdialog.open(); 
			return openQuick(xmlFilename);

		} catch (Exception e) {
			try {
				new ErrorLog("error in " + sos.util.SOSClassUtil.getMethodName() + " ; could not open File ", e);
			} catch(Exception ee) {
				//tu nichts
			}
			MainWindow.message("could not open file cause" + e.getMessage(), SWT.NONE);
		}
		return null; 
	}  

}
