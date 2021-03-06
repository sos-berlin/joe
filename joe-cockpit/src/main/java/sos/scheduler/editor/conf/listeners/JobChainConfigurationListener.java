package sos.scheduler.editor.conf.listeners;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;
import sos.scheduler.editor.app.MainWindow;
import sos.scheduler.editor.app.Utils;
import sos.scheduler.editor.conf.forms.DetailForm;
import sos.scheduler.editor.conf.forms.JobChainConfigurationForm;
import com.sos.joe.globals.JOEConstants;
import com.sos.joe.globals.messages.ErrorLog;
import com.sos.joe.xml.jobscheduler.DetailDom;

public class JobChainConfigurationListener {

    private DetailDom _dom;
    private JobChainConfigurationForm _gui = null;
    private String filename = null;
    private String jobChainname = null;

    public JobChainConfigurationListener(JobChainConfigurationForm gui, DetailDom dom) {
        _gui = gui;
        _dom = dom;
    }

    public void treeFillMain(Tree tree, Composite c, String jobChainname_) {
        jobChainname = jobChainname_;
        treeFillMain(tree, c);
    }

    public void treeFillMain(Tree tree, Composite c) {
        tree.removeAll();
        if (_dom.getDoc() != null) {
            Element jobChain = _dom.getRoot().getChild("job_chain");
            if (jobChain == null) {
                jobChain = _dom.getRoot().getChild("application");
            }
            jobChainname = Utils.getAttributeValue("name", jobChain);
        }
        TreeItem item = new TreeItem(tree, SWT.NONE);
        treeFillState(item);
        item.setText(jobChainname != null ? jobChainname : "");
        item.setExpanded(true);
        tree.setSelection(new TreeItem[] { tree.getItem(0) });
        treeSelection(tree, c);
    }

    public void setFilename(String filename_) {
        filename = filename_;
    }

    public void treeFillState(TreeItem parent) {
        parent.removeAll();
        ArrayList listOfState = getAllDetailState();
        for (int i = 0; i < listOfState.size(); i++) {
            TreeItem item = new TreeItem(parent, SWT.NONE);
            item.setData(listOfState.get(i));
            item.setText("State: " + listOfState.get(i).toString());
        }
        parent.setExpanded(true);
    }

    private ArrayList getAllDetailState() {
        ArrayList list = new ArrayList();
        Element root = null;
        Element order = null;
        List process = null;
        try {
            SAXBuilder builder = new SAXBuilder();
            filename = _dom.getFilename();
            if (filename == null || filename.isEmpty()) {
                return list;
            }
            Document doc = builder.build(new File(filename));
            root = doc.getRootElement();
            Element application = root.getChild("job_chain");
            if (application == null) {
                application = root.getChild("application");
            }
            jobChainname = Utils.getAttributeValue("name", application);
            if (application != null) {
                order = application.getChild("order");
            }
            if (order != null) {
                process = order.getChildren("process");
            }
            for (int i = 0; i < process.size(); i++) {
                Element p = (Element) process.get(i);
                list.add(Utils.getAttributeValue("state", p));
            }
        } catch (Exception e) {
            new ErrorLog("error in " + sos.util.SOSClassUtil.getMethodName(), e);
        }
        return list;
    }

    public void treeExpandJob(TreeItem parent, String job) {
        if ("Jobs".equals(parent.getText())) {
            for (int i = 0; i < parent.getItemCount(); i++) {
                if (parent.getItem(i).getText().equals(job)) {
                    parent.getItem(i).setExpanded(true);
                }
            }
        }
    }

    public boolean treeSelection(Tree tree, Composite c) {
        try {
            if (tree.getSelectionCount() > 0) {
                Control[] children = c.getChildren();
                for (int i = 0; i < children.length; i++) {
                    if (!Utils.applyFormChanges(children[i])) {
                        return false;
                    }
                    children[i].dispose();
                }
                TreeItem item = tree.getSelection()[0];
                _dom.setInit(true);
                DetailForm df = null;
                try {
                    if (jobChainname == null) {
                        df = new DetailForm(c, SWT.NONE, JOEConstants.DETAILS, _dom, _gui, false, null);
                        df.setLayout(new org.eclipse.swt.layout.FillLayout());
                    } else {
                        Composite composite = new Composite(c.getShell(), SWT.NONE);
                        composite.setLayout(new org.eclipse.swt.layout.FillLayout());
                        df =
                                new DetailForm(composite, SWT.NONE, jobChainname, item.getData() != null
                                        && !(item.getData() instanceof com.sos.joe.globals.misc.TreeData) ? item.getData().toString() : null, null,
                                        JOEConstants.DETAILS, _dom, _gui, false, null);
                        df.setLayout(new org.eclipse.swt.layout.FillLayout());
                    }
                    df.setTree(tree);
                    df.setJobChainConfigurationListener(this);
                    df.open();
                    if (df.hasErrors()) {
                        df.dispose();
                        _gui.close();
                        return false;
                    }
                } catch (Exception e) {
                    new ErrorLog("error in " + sos.util.SOSClassUtil.getMethodName(), e);
                    MainWindow.message(e.getMessage(), SWT.ICON_ERROR);
                    df.dispose();
                    _gui.close();
                    return false;
                }
            }
            c.layout();
        } catch (Exception e) {
            c.layout();
            new ErrorLog("error in " + sos.util.SOSClassUtil.getMethodName(), e);
            MainWindow.message(e.getMessage(), SWT.ICON_ERROR);
        }
        _dom.setInit(false);
        return true;
    }

    public void setJobChainname(String jobChainname) {
        this.jobChainname = jobChainname;
    }

    public DetailDom getDom() {
        return _dom;
    }

    public void setDom(DetailDom _dom) {
        this._dom = _dom;
    }

    public void deleteState(String state, TreeItem parent) {
        parent.removeAll();
        Element jobChain = _dom.getRoot().getChild("job_chain");
        if (jobChain != null) {
            Element order = jobChain.getChild("order");
            List pList = order.getChildren("process");
            for (int i = 0; i < pList.size(); i++) {
                Object o = pList.get(i);
                if (o instanceof Element) {
                    Element element = (Element) o;
                    TreeItem item = new TreeItem(parent, SWT.NONE);
                    item.setText("State: " + Utils.getAttributeValue("state", element));
                    item.setData(Utils.getAttributeValue("state", element));
                }
            }
        }
        parent.setExpanded(true);
    }

}