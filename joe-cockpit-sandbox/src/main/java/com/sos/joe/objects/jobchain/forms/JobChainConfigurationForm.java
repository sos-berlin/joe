package com.sos.joe.objects.jobchain.forms;

import java.io.File;
import java.util.Collection;
import java.util.Iterator;

import org.apache.log4j.Logger;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;

import sos.scheduler.editor.app.MainWindow;
import sos.scheduler.editor.app.TreeData;
import sos.scheduler.editor.app.Utils;
import sos.util.SOSClassUtil;

import com.sos.joe.globals.interfaces.IDetailUpdate;
import com.sos.joe.globals.interfaces.IEditor;
import com.sos.joe.globals.interfaces.IUpdateLanguage;
import com.sos.joe.globals.messages.ErrorLog;
import com.sos.joe.globals.messages.SOSJOEMessageCodes;
import com.sos.joe.globals.options.Options;
import com.sos.joe.interfaces.IContainer;
import com.sos.joe.objects.jobchain.JobChainConfigurationListener;
import com.sos.joe.xml.IOUtils;
import com.sos.joe.xml.jobscheduler.DetailDom;

public class JobChainConfigurationForm extends SOSJOEMessageCodes implements IDetailUpdate, IEditor {


	@SuppressWarnings("unused")
	private final String conClassName = this.getClass().getSimpleName();
	@SuppressWarnings("unused")
	private static final String conSVNVersion = "$Id$";
	@SuppressWarnings("unused")
	private final Logger logger = Logger.getLogger(this.getClass());

    private final JobChainConfigurationListener listener;

    private final IContainer        container;
    private TreeItem          selection;
    private SashForm          sashForm  = null;
    private Group             gTree     = null;
    private Tree              tree      = null;
    private Composite         cMainForm = null;
    private static String     filename  = "";
    private static DetailDom         dom       = null;


    public JobChainConfigurationForm(final IContainer container, final Composite parent, final int style) {
        super(parent, style);
        this.container = container;
        dom = new DetailDom();
        dom.setDataChangedListener(this);
        listener = new JobChainConfigurationListener(this, dom);
    }


    private void initialize() {
        FillLayout fillLayout = new FillLayout();
        fillLayout.spacing = 0;
        fillLayout.marginWidth = 5;
        fillLayout.marginHeight = 5;
        setLayout(fillLayout);
        createSashForm();
    }


    private void createSashForm() {
        sashForm = new SashForm(this, SWT.NONE);
        createGTree();
        createCMainForm();
        cMainForm = new Composite(sashForm, SWT.NONE);
        cMainForm.setLayout(new FillLayout());

        Options.loadSash("main", sashForm);

    }


    private void createGTree() {
        gTree = JOE_G_JobAssistent_JobChainConfiguration.Control(new Group(sashForm, SWT.NONE));
        gTree.setLayout(new FillLayout());
//        gTree.setText("Job Chain Configuration");
        tree = new Tree(gTree, SWT.BORDER);
        //tree.setMenu(new TreeMenu(tree, dom, this).getMenu());
        tree.addListener(SWT.MenuDetect, new Listener() {
            @Override
			public void handleEvent(final Event e) {
                e.doit = tree.getSelectionCount() > 0;
            }
        });
        tree.addListener(SWT.Selection, new Listener() {
            @Override
			public void handleEvent(final Event e) {
                if (tree.getSelectionCount() > 0) {

                	selection = tree.getSelection()[0];
                	//if (selection == null)  selection = tree.getItem(0);

                    e.doit = listener.treeSelection(tree, cMainForm);

                    if (!e.doit) {
                        tree.setSelection(new TreeItem[] { selection });
                    } else {
                        selection = tree.getSelection()[0];
                    }
                } else
                	selection = tree.getItem(0);
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


    @Override
	public void updateLanguage() {
        if (cMainForm.getChildren().length > 0) {
            if (cMainForm.getChildren()[0] instanceof IUpdateLanguage) {
                ((IUpdateLanguage) cMainForm.getChildren()[0]).setToolTipText();
            }
        }
    }


    @Override
	public void dataChanged() {
        container.setStatusInTitle();
    }

    @Override
	public void updateState(final String state){
    	TreeItem item = tree.getSelection()[0];
//        item.setText("State: " + state);
    	item.setText(JOE_M_JobAssistent_State.params(state));
        dom.setChanged(true);
    }

    @Override
	public void updateJobChainname(final String name){
    	TreeItem item = tree.getItem(0);
//    	item.setText("Job Chain: " + name);
    	item.setText(JOE_JobAssistent_JobChain.params(name));
    	dom.setChanged(true);
    }

    @Override
	public void updateNote() {
    	dom.setChanged(true);
    }

	@Override
	public void updateParamNote(){
		dom.setChanged(true);
	}

	@Override
	public void updateParam(){
		dom.setChanged(true);
	}

    @Override
	public boolean applyChanges() {
        Control[] c = cMainForm.getChildren();
        return c.length == 0 || Utils.applyFormChanges(c[0]);
    }


    @Override
	public void openBlank() {
        initialize();
        listener.treeFillMain(tree, cMainForm);
    }


    @Override
	public boolean open(final Collection files) {
    	assert false;
        boolean res = false; // IOUtils.openFile(files, dom);
        if (res) {
            initialize();
            listener.setFilename(filename);
            listener.treeFillMain(tree, cMainForm);
        }

        return res;
    }

    public boolean open(final String filename, final Collection files) {
    	assert false;
        boolean res = false; // IOUtils.openFile(filename, files, dom);
        if (res) {
            initialize();
            listener.setFilename(filename);
            listener.treeFillMain(tree, cMainForm);
        }

        return res;
    }

    public static String getFile(final Collection filenames) {

    	 try {
             // open file dialog
             if (filename == null || filename.equals("")) {
                 FileDialog fdialog = JOE_FD_JobAssistent_OpenFile.Control(new FileDialog(MainWindow.getSShell(), SWT.OPEN));
                 fdialog.setFilterPath(Options.getLastDirectory());
//                 fdialog.setText("Open File");
                 filename = fdialog.open();
             }

             // check for opened file
             if (filenames != null) {
                 for (Iterator it = filenames.iterator(); it.hasNext();) {
                     if (((String) it.next()).equals(filename)) {
//                         MainWindow.message(Messages.getString("MainListener.fileOpened"), SWT.ICON_INFORMATION | SWT.OK);
                    	 MainWindow.message(JOE_M_JobAssistent_FileIsOpened.label(), SWT.ICON_INFORMATION | SWT.OK);
                         return "";
                     }
                 }
             }

             if (filename != null && !filename.equals("")) { //$NON-NLS-1$
                 File file = new File(filename);
//System.out.println("~~~~~~~~~~~~~~~~~filename: " + filename);
                 // check the file
                 if (!file.exists()) {
                	 //System.out.println("~~~~~~~~~~~~~~~~~not exist filename: " + filename);
//                     MainWindow.message(Messages.getString("MainListener.fileNotFound"), //$NON-NLS-1$
//                             SWT.ICON_WARNING | SWT.OK);
                     MainWindow.message(JOE_M_JobAssistent_FileNotFound.label(),
                             SWT.ICON_WARNING | SWT.OK);
                 } else if (!file.canRead())
//                     MainWindow.message(Messages.getString("MainListener.fileReadProtected"), //$NON-NLS-1$
//                             SWT.ICON_WARNING | SWT.OK);
                 MainWindow.message(JOE_M_JobAssistent_FileReadProtected.label(),
                         SWT.ICON_WARNING | SWT.OK);
             } else
                 return filename;

//             MainWindow.getSShell().setText("Job Details Editor [" + filename + "]");
             MainWindow.getSShell().setText(JOE_M_JobAssistent_JobDetailsEditor.params(filename));

             Options.setLastDirectory(new File(filename), dom);
             return filename;
         } catch (Exception e) {
        	 try {
// 				new ErrorLog("error in " + SOSClassUtil.getMethodName() , e);
        		 new ErrorLog(JOE_E_0002.params(SOSClassUtil.getMethodName()) , e);
 			} catch(Exception ee) {
 			}
             e.printStackTrace();
             MainWindow.message(e.getMessage(), SWT.ICON_ERROR | SWT.OK);
         }

         return filename;
    }

    @Override
	public boolean save() {
    	boolean res = IOUtils.saveFile(dom, false);
        if (res)
            container.setNewFilename(null);
        return res;
    }


    @Override
	public boolean saveAs() {
    	String old = dom.getFilename();
        boolean res = IOUtils.saveFile(dom, true);
        if (res)
            container.setNewFilename(old);
        return res;
    }


    @Override
	public boolean close() {
        return applyChanges() && IOUtils.continueAnyway(dom);
    }


    @Override
	public boolean hasChanges() {
        Options.saveSash("main", sashForm.getWeights());
        return dom.isChanged();
    }


    @Override
	public String getHelpKey() {
        if (tree.getSelectionCount() > 0) {
            TreeItem item = tree.getSelection()[0];
            TreeData data = (TreeData) item.getData();
            if (data != null && data.getHelpKey() != null)
                return data.getHelpKey();
        }
        return null;
    }


    @Override
	public String getFilename() {
        return dom.getFilename();
    }


	public Composite getCMainForm() {
		return cMainForm;
	}


	public DetailDom getDom() {
		return dom;
	}


}
