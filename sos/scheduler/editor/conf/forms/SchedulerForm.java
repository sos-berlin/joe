package sos.scheduler.editor.conf.forms;

import java.util.Collection;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;
import org.jdom.Element;

import sos.scheduler.editor.app.Editor;
import sos.scheduler.editor.app.IContainer;
import sos.scheduler.editor.app.IEditor;
import sos.scheduler.editor.app.IOUtils;
import sos.scheduler.editor.app.IUpdateLanguage;
import sos.scheduler.editor.app.Options;
import sos.scheduler.editor.app.TreeData;
import sos.scheduler.editor.app.TreeMenu;
import sos.scheduler.editor.app.Utils;
import sos.scheduler.editor.conf.ISchedulerUpdate;
import sos.scheduler.editor.conf.SchedulerDom;
import sos.scheduler.editor.conf.listeners.SchedulerListener;

public class SchedulerForm extends Composite implements ISchedulerUpdate, IEditor {
    private SchedulerDom      dom;

    private SchedulerListener listener;

    private IContainer        container;

    private TreeItem          selection;

    private SashForm          sashForm  = null;

    private Group             gTree     = null;

    private Tree              tree      = null;

    private Composite         cMainForm = null;


    public SchedulerForm(IContainer container, Composite parent, int style) {
        super(parent, style);
        this.container = container;

        // initialize();
        this.dom = new SchedulerDom();
        this.dom.setDataChangedListener(this);

        listener = new SchedulerListener(this, this.dom);

    }

    public SchedulerForm(IContainer container, Composite parent, int style, int type) {
        super(parent, style);
        this.container = container;

        // initialize();
        this.dom = new SchedulerDom(type);
        this.dom.setDataChangedListener(this);

        listener = new SchedulerListener(this, this.dom);

    }
    
    
    private void initialize() {
        FillLayout fillLayout = new FillLayout();
        fillLayout.spacing = 0;
        fillLayout.marginWidth = 5;
        fillLayout.marginHeight = 5;
        setSize(new Point(783, 450));
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
        sashForm.setWeights(new int[] { 176, 698 });
        Options.loadSash("main", sashForm);
    }


    /**
     * This method initializes gTree
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
     */
    private void createCMainForm() {
        cMainForm = new Composite(sashForm, SWT.NONE);
        cMainForm.setLayout(new FillLayout());
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


    public void updateCommand(String s) {
        TreeItem item = tree.getSelection()[0];
        item.setText(s);
    }


    public void updateCommands() {
        if (tree.getSelectionCount() > 0) {
            TreeItem item = tree.getSelection()[0];
            TreeData data = (TreeData) item.getData();
            listener.treeFillCommands(tree.getSelection()[0], data.getElement(), true);
        }
    }


    public void updateDays(int type) {
    	updateDays(type, null);
        /*if (tree.getSelectionCount() > 0) {
            TreeItem item = tree.getSelection()[0];
            TreeData data = (TreeData) item.getData();
            listener.treeFillDays(item, data.getElement(), type, true);
        }*/
    }

    public void updateDays(int type, String name) {
        if (tree.getSelectionCount() > 0) {
            TreeItem item = tree.getSelection()[0];
            TreeData data = (TreeData) item.getData();            
            listener.treeFillDays(item, data.getElement(), type, true, name);
            item.setExpanded(true);
        }
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


    public void updateJobs() {
    	if(tree.getSelection()[0].getText().startsWith("Job Chain")) {
        	//Assistent: Der Aufruf erfolgte über den Assistenten. Hier ist nicht das Element "Jobs" im Tree selektiert
    		  //sondern das Element "Job Chains".
    		updateJobs_();
    	} else 
        if (tree.getSelectionCount() > 0)
            listener.treeFillJobs(tree.getSelection()[0]);                
    }


    public void expandJob(String job) {
        listener.treeExpandJob(tree.getSelection()[0],job);                
    }

    private void updateJobs_() {
    	if (tree.getSelectionCount() > 0) {
    		for (int i =0; i < tree.getItemCount(); i++) {
    			TreeItem ti = tree.getItem(i);
    			if(ti.getText().equalsIgnoreCase("Jobs")) {
    				//System.out.println("itemText "+ ti.getText());
    				listener.treeFillJobs(ti);
    			}        		        		
    		}
    		    		
    	}
    }

    public void updateOrder(String s) {
        TreeItem item = tree.getSelection()[0];
        String order = "Order: " + s;
        item.setText(order);
    }


    public void updateOrders() {
        if (tree.getSelectionCount() > 0) {
            listener.treeFillOrders(tree.getSelection()[0], true);
        }
    }


    public boolean applyChanges() {
        Control[] c = cMainForm.getChildren();
        return c.length == 0 || Utils.applyFormChanges(c[0]);
    }


    public void openBlank() {
        initialize();
        // dom.initScheduler();
        listener.treeFillMain(tree, cMainForm);
    }

    public void openBlank(int type) {
        initialize();
        // dom.initScheduler();
        listener.treeFillMain(tree, cMainForm, type);
    }
    
    public boolean openDirectory(Collection files) {
    	
    	boolean res = IOUtils.openFile("#xml#", files, dom);
    	if (res) {
    		if(dom.getListOfChangeElementNames() != null && dom.getListOfChangeElementNames().size() > 0 ) {
    			dom.setChanged(true);
    		}
    		initialize();
    		listener.treeFillMain(tree, cMainForm, SchedulerDom.DIRECTORY);
    	}
    	
    	return res;
    }
    
    
    public boolean open(Collection files) {
        boolean res = IOUtils.openFile(files, dom);
        if (res) {
            initialize();
            listener.treeFillMain(tree, cMainForm);
        }

        return res;
    }

    public boolean open(String filename, Collection files) {
        boolean res = IOUtils.openFile(filename, files, dom);
        if (res) {
            initialize();
            listener.treeFillMain(tree, cMainForm);
        }

        return res;
    }
    
    public boolean open(String filename, Collection files, int type) {
        boolean res = IOUtils.openFile(filename, files, dom);
        if (res) {
            initialize();
            listener.treeFillMain(tree, cMainForm, type);
        }

        return res;
    }

   /* public boolean reOpen(String filename, Collection files) {
    	try {
    		dom.read(filename);
    		
    		initialize();
    		listener.treeFillMain(tree, cMainForm);
    	} catch (Exception e) {
    		return false;
    	}
    	return true;
    }*/
    
    
    public boolean save() {
    	boolean res = true;
    	if(dom.getFilename() != null && new java.io.File(dom.getFilename()).getName().startsWith("#xml#.config.") && dom.getFilename().endsWith(".xml~")) {    		
    		res = IOUtils.saveDirectory(dom, false, SchedulerDom.DIRECTORY, null, container);
    		
    	} else if(dom.isLifeElement()) {
    		
    		int type = -1;
    		if(dom.getRoot().getName().equals("job")) type=SchedulerDom.LIFE_JOB;
    		if(dom.getRoot().getName().equals("job_chain")) type=SchedulerDom.LIFE_JOB_CHAIN;
    		if(dom.getRoot().getName().equals("process_class")) type=SchedulerDom.LIFE_PROCESS_CLASS;
    		if(dom.getRoot().getName().equals("lock")) type=SchedulerDom.LIFE_LOCK;
    		if(dom.getRoot().getName().equals("order")) type=SchedulerDom.LIFE_ORDER;
    		if(dom.getRoot().getName().equals("add_order")) type=SchedulerDom.LIFE_ADD_ORDER;
    		res = IOUtils.saveDirectory(dom, false,type, dom.getRoot().getName(), container);
    		
    	} else {
    		res = IOUtils.saveFile(dom, false);
    	}
        if (res)
            container.setNewFilename(null);
        return res;
    }


    public boolean saveAs() {
        String old = dom.getFilename();
        
        boolean res = IOUtils.saveFile(dom, true);
        
        if(dom.isLifeElement()) {             	
        	updateLifeElement();        	        	
        }
        
        if (res)
            container.setNewFilename(old);
        return res;
    }


    public boolean close() {
        return applyChanges() && IOUtils.continueAnyway(dom);
    }


    public boolean hasChanges() {
        Options.saveSash("main", sashForm.getWeights());

        return dom.isChanged();
    }


    public String getHelpKey() {
    	if (tree.getSelectionCount() > 0) {
    		TreeItem item = tree.getSelection()[0];            
    		if(item.getData("key") != null) {
    			return Options.getHelpURL(item.getData("key").toString());
    		} else {
    			TreeData data = (TreeData) item.getData(); 
    			if (data != null && data.getHelpKey() != null)
    				return data.getHelpKey();
    		}
    	}
    	return null;
    }
    

    public String getFilename() {
        return dom.getFilename();
    }

  	
    public void updateTree(String which) {
    	if(which.equalsIgnoreCase("main"))
    		listener.treeFillMain(tree, cMainForm);	
    	else if(which.equalsIgnoreCase("jobs"))
    		listener.treeSelection(tree, cMainForm);
    }
	
	public String getTreeSelection() {
		return tree.getSelection()[0].getText();
	}
	
	public void updateJobChains() {
		listener.treeFillJobChains(tree.getSelection()[0]);
	}
	public void updateJobChain(String newName, String oldName) {
		//listener.treeFillJobChains(tree.getSelection()[0]);
		TreeItem item = tree.getSelection()[0];
		if(item.getText().equals("Job Chains")) {
			TreeItem[] items = item.getItems();
			for (int i = 0; i < items.length; i++) {
				TreeItem it = items[i]; 
				if(it.getText().equals("Job Chain: " + oldName))
				    it.setText("Job Chain: " + newName);
			}
		} else {
			String jobChain = "Job Chain: " + newName;
			item.setText(jobChain);
		}
	}
	
	public void updateLifeElement() {
		TreeItem item = tree.getSelection()[0];		
		TreeData data = (TreeData) item.getData();
		org.jdom.Element elem = data.getElement();
		if(elem.getName().equals("job")) {	
			updateJob(Utils.getAttributeValue("name", elem));
			updateJob();	    	
		} else if(elem.getName().equals("job_chain")) {
			updateJobChain(item.getText(0).substring("Job Chain: ".length()), Utils.getAttributeValue("name", elem));
		} else if(elem.getName().equals("config")) {
			
			if(elem.getChild("process_classes") != null) {
				Element process_classes = elem.getChild("process_classes");
				Element pc = process_classes.getChild("process_class");
				pc.setAttribute("name", dom.getRoot().getAttributeValue("name"));
				
			}
			
			if(elem.getChild("locks") != null) {
				Element locks = elem.getChild("locks");
				Element pc = locks.getChild("lock");
				pc.setAttribute("name", dom.getRoot().getAttributeValue("name"));
				
			}
			
		} else if(elem.getName().equals("add_order") || elem.getName().equals("order")) {
			updateOrder(Utils.getAttributeValue("id", elem));
		}
	    	
	    listener.treeSelection(tree, cMainForm);
	}

    public void updateSpecificWeekdays() {
        if (tree.getSelectionCount() > 0) {
            TreeItem item = tree.getSelection()[0];
            TreeData data = (TreeData) item.getData();
            listener.treeFillSpecificWeekdays(item, data.getElement(), true);
        }
    }

	public Tree getTree() {
		return tree;
	}

	public SchedulerDom getDom() {
		return dom;
	}

    /*public void updateMonths(int type) {
    	
        if (tree.getSelectionCount() > 0 && type==3) {
            TreeItem item = tree.getSelection()[0];
            TreeData data = (TreeData) item.getData();
            listener.treeFillRunTimes(item, data.getElement(), true, "month");
        }
    }
*/
}
