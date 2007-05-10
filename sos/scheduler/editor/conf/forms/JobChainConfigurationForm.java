package sos.scheduler.editor.conf.forms;

import java.io.File;
import java.util.Collection;
import java.util.Iterator;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;
import com.swtdesigner.SWTResourceManager;

import sos.scheduler.editor.app.IContainer;
import sos.scheduler.editor.app.IEditor;
import sos.scheduler.editor.app.IOUtils;
import sos.scheduler.editor.app.IUpdateLanguage;
import sos.scheduler.editor.app.MainWindow;
import sos.scheduler.editor.app.Messages;
import sos.scheduler.editor.app.Options;
import sos.scheduler.editor.app.TreeData;
import sos.scheduler.editor.app.Utils;
import sos.scheduler.editor.conf.SchedulerDom;
import sos.scheduler.editor.conf.listeners.JobChainConfigurationListener;
import sos.scheduler.editor.conf.listeners.SchedulerListener;
import sos.scheduler.editor.conf.DetailDom;
import  sos.scheduler.editor.conf.IDetailUpdate;

public class JobChainConfigurationForm extends Composite implements IDetailUpdate, IEditor {  
    

    private JobChainConfigurationListener listener;

    private IContainer        container;

    private TreeItem          selection;

    private SashForm          sashForm  = null;

    private Group             gTree     = null;

    private Tree              tree      = null;

    private Composite         cMainForm = null;
    
    private static String     filename  = "";
    
    private DetailDom         dom       = null;


    public JobChainConfigurationForm(IContainer container, Composite parent, int style) {
        super(parent, style);
        this.container = container;

        //setDataChangedListener(this);
        this.dom = new DetailDom();
        this.dom.setDataChangedListener(this);

        //listener = new SchedulerListener(this, this.dom);
        listener = new JobChainConfigurationListener(this, dom);

    }


    private void initialize() {
        FillLayout fillLayout = new FillLayout();
        fillLayout.spacing = 0;
        fillLayout.marginWidth = 5;
        fillLayout.marginHeight = 5;
        //setSize(new Point(783, 450));
        setLayout(fillLayout);
        createSashForm();
    }


    /**
     * This method initializes sashForm
     */
    private void createSashForm() {
        sashForm = new SashForm(this, SWT.NONE);
        createGTree();
        createCMainForm();
        cMainForm = new Composite(sashForm, SWT.NONE);
        cMainForm.setLayout(new FillLayout());
               
        Options.loadSash("main", sashForm);
        
    }


    /**
     * This method initializes gTree
     */
    private void createGTree() {
        gTree = new Group(sashForm, SWT.NONE);
        gTree.setLayout(new FillLayout());
        gTree.setText("Job Chain Configuration");
        tree = new Tree(gTree, SWT.BORDER);
        //tree.setMenu(new TreeMenu(tree, dom, this).getMenu());
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
     */
    private void createCMainForm() {
        
    }


    public Shell getSShell() {
        return this.getShell();
    }


    public void updateLanguage() {
        if (cMainForm.getChildren().length > 0) {
            if (cMainForm.getChildren()[0] instanceof IUpdateLanguage) {
                ((IUpdateLanguage) cMainForm.getChildren()[0]).setToolTipText();
            }
        }
    }


    public void dataChanged() {
        container.setStatusInTitle();
    }

/*
    public void updateCommand(String s) {
        TreeItem item = tree.getSelection()[0];
        item.setText(s);
    }


   
    

    public void updateJob() {
        if (tree.getSelectionCount() > 0) {
            TreeItem item = tree.getSelection()[0];
            TreeData data = (TreeData) item.getData();
            //listener.treeFillJob(item, data.getElement(), true);
        }
    }


    public void updateJob(String s) {
        TreeItem item = tree.getSelection()[0];
        String job = "Job: " + s;
        item.setText(job);
    }
   
*/
    
    public void updateState(String state){
    	TreeItem item = tree.getSelection()[0];        
        item.setText("State: " + state);
        dom.setChanged(true);
    }
    
    public void updateJobChainname(String name){    	
    	TreeItem item = tree.getItem(0);
    	item.setText("Job Chain: " + name);
    	dom.setChanged(true);
    }

    public void updateNote() {
    	dom.setChanged(true);
    }
	
	public void updateParamNote(){
		dom.setChanged(true);
	}
	
    public boolean applyChanges() {
        Control[] c = cMainForm.getChildren();
        return c.length == 0 || Utils.applyFormChanges(c[0]);
    }


    public void openBlank() {
        initialize();
        //dom.initScheduler();
        listener.treeFillMain(tree, cMainForm);
    }


    public boolean open(Collection files) {
        boolean res = IOUtils.openFile(files, dom);
        if (res) {
            initialize();
            listener.setFilename(filename);
            listener.treeFillMain(tree, cMainForm);
        }

        return res;
    }
    
    /*public boolean open(Collection files) {
        String filename = getFile(files); //IOUtils.openFile(files, dom);
        MainWindow.getSShell().setText("Job Details Editor [" + filename + "]");
        initialize();
        listener.setFilename(filename);
        listener.treeFillMain(tree, cMainForm);
        return true;
    }
*/
    public static String getFile(Collection filenames) {
    	//String filename = "";
    	 try {    		 
             // open file dialog
             if (filename == null || filename.equals("")) {
                 FileDialog fdialog = new FileDialog(MainWindow.getSShell(), SWT.OPEN);
                 fdialog.setFilterPath(Options.getLastDirectory());
                 fdialog.setText("Open File");
                 filename = fdialog.open();
             }

             // check for opened file
             if (filenames != null) {
                 for (Iterator it = filenames.iterator(); it.hasNext();) {
                     if (((String) it.next()).equals(filename)) {
                         MainWindow
                                 .message(Messages.getString("MainListener.fileOpened"), SWT.ICON_INFORMATION | SWT.OK);
                         return "";
                     }
                 }
             }

             if (filename != null && !filename.equals("")) { //$NON-NLS-1$
                 File file = new File(filename);

                 // check the file
                 if (!file.exists())
                     MainWindow.message(Messages.getString("MainListener.fileNotFound"), //$NON-NLS-1$
                             SWT.ICON_WARNING | SWT.OK);
                 else if (!file.canRead())
                     MainWindow.message(Messages.getString("MainListener.fileReadProtected"), //$NON-NLS-1$
                             SWT.ICON_WARNING | SWT.OK);                 
             } else
                 return filename;

             MainWindow.getSShell().setText("Job Details Editor [" + filename + "]");

             Options.setLastDirectory(new File(filename));
             return filename;
         } catch (Exception e) {
             e.printStackTrace();
             MainWindow.message(e.getMessage(), SWT.ICON_ERROR | SWT.OK);
         }
          
         return filename;
    }

    public boolean save() {
        //boolean res = IOUtils.saveFile(null, false); 
    	boolean res = IOUtils.saveFile(dom, false);
        if (res)
            container.setNewFilename(null);
        return res;
    }


    public boolean saveAs() {
        //String old = null; //
    	String old = dom.getFilename();
        
        //boolean res = IOUtils.saveFile(null, true); 
        boolean res = IOUtils.saveFile(dom, true);
        if (res)
            container.setNewFilename(old);
        return res;
    }


    public boolean close() {
        return applyChanges() && IOUtils.continueAnyway(dom);
    }


    public boolean hasChanges() {
        Options.saveSash("main", sashForm.getWeights());
        //return true;
        return dom.isChanged();
    }


    public String getHelpKey() {
        if (tree.getSelectionCount() > 0) {
            TreeItem item = tree.getSelection()[0];
            TreeData data = (TreeData) item.getData();
            if (data != null && data.getHelpKey() != null)
                return data.getHelpKey();
        }
        return null;
    }


    public String getFilename() {    	
        return dom.getFilename();
        
    	//return filename;
    }


	public Composite getCMainForm() {
		return cMainForm;
	}


}
