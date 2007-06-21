package sos.scheduler.editor.app;

import java.io.File;
import java.io.StringReader;
import java.util.ArrayList;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabFolder2Adapter;
import org.eclipse.swt.custom.CTabFolderEvent;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.FillLayout;
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
                IEditor editor = getCurrentEditor();
                if (editor.hasChanges()) {
                    event.doit = editor.close();
                }
                if (event.doit)
                    filelist.remove(editor.getFilename());
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
    			CTabItem tab = newItem(doc, doc.getFilename());
    			// tab.setImage(ResourceManager.getImageFromResource("/sos/scheduler/editor/editor-small.png"));
    			return doc;
    		} else 
    			return null;
    	} catch (Exception e) {    		
    		System.out.println("error in TabbedContainer.openDocumentation()" + e.getMessage() );
    		return null;
    	}
    }

    public DocumentationForm openDocumentation(String filename){
    	try {
    		DocumentationForm doc = new DocumentationForm(this, folder, SWT.NONE);
    		if (doc.open(filename, filelist)) {
    			CTabItem tab = newItem(doc, doc.getFilename());
    			// tab.setImage(ResourceManager.getImageFromResource("/sos/scheduler/editor/editor-small.png"));
    			return doc;
    		} else 
    			return null;
    	} catch (Exception e) {    		
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
    		System.out.println("error in TabbedContainer.openDocumentation()" + e.getMessage() );
    		return null;
    	}
    }

    private CTabItem newItem(Control control, String filename) {
        CTabItem tab = new CTabItem(folder, SWT.NONE);
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


    private CTabItem getCurrentTab() {
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
        tab.setText(title);
        tab.setToolTipText(filename);
        tab.setData(new TabData(Utils.getFileFromURL(filename),title));
        setWindowTitle();
    }


    private void setWindowTitle() {
        Shell shell = folder.getShell();
        shell.setText((String) shell.getData() + " [" + getCurrentTab().getText() + "]");
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
        boolean found = false;

        while (cnt==-1) {
        	if (isFreeIndex(i, title)){
        		found = true;
        		cnt=i;
        	}
        	i++;
        }
       /* for (int i = 0; i < items.length; i++) {
          TabData t = (TabData) items[i].getData();          
          if (items[i].getData() != null && t.title.equals(title) && !items[i].equals(getCurrentTab()))
                cnt++;
        }
*/
        
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


    public void updateLanguages() {
        for (int i = 0; i < folder.getItemCount(); i++) {
            CTabItem tab = folder.getItem(i);
            ((IEditor) tab.getControl()).updateLanguage();
        }
    }
    
    public SchedulerForm openDirectory() {
        SchedulerForm scheduler = new SchedulerForm(this, folder, SWT.NONE, SchedulerDom.DIRECTORY);
        
        if (scheduler.openDirectory(filelist)) {
            CTabItem tab = newItem(scheduler, scheduler.getFilename());
            tab.setImage(ResourceManager.getImageFromResource("/sos/scheduler/editor/editor-small.png"));
            return scheduler;
        } else
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
    			} else {
    				MainWindow.message("Unknows root Element: " + root.getName() + " from filename " + xmlFilename, SWT.NONE);
    			}
    		}
    	} catch (Exception e) {
    		MainWindow.message("could not open file cause" + e.getMessage(), SWT.NONE);
    	}
    	return null; 
    }
    
}
