package com.sos.joe.objects.jobchain;

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

import com.sos.joe.objects.jobchain.forms.DetailForm;
import com.sos.joe.objects.jobchain.forms.JobChainConfigurationForm;

import sos.scheduler.editor.app.Editor;
import sos.scheduler.editor.app.ErrorLog;
import sos.scheduler.editor.app.MainWindow;
import sos.scheduler.editor.app.Utils;
import sos.scheduler.editor.conf.DetailDom;
import sos.util.SOSClassUtil;


public class JobChainConfigurationListener{

	private DetailDom  _dom;

	private JobChainConfigurationForm _gui            = null;

	private String                    filename        = null;

	private String                    jobChainname    = null;

	public JobChainConfigurationListener(final JobChainConfigurationForm gui, final DetailDom dom) {
		_gui = gui;
		_dom = dom;
	}


	public void treeFillMain(final Tree tree, final Composite c, final String jobChainname_) {
		jobChainname = jobChainname_;
		treeFillMain(tree, c);
	}

	public void treeFillMain(final Tree tree, final Composite c) {
		tree.removeAll();

		if(_dom.getDoc() != null) {
			Element jobChain = _dom.getRoot().getChild("job_chain");

			if(jobChain == null) {
				jobChain = _dom.getRoot().getChild("application");
			}

			jobChainname = Utils.getAttributeValue("name", jobChain);
		}
		TreeItem item = new TreeItem(tree, SWT.NONE);

		treeFillState(item);
		item.setText(jobChainname != null? jobChainname:"");
		item.setExpanded(true);

		tree.setSelection(new TreeItem[] { tree.getItem(0) });
		treeSelection(tree, c);

	}

	public void setFilename(final String filename_) {
		filename = filename_;
	}

	public void treeFillState(final TreeItem parent) {
		parent.removeAll();
		ArrayList listOfState = getAllDetailState();

		for (int i =0; i < listOfState.size(); i++) {
			TreeItem item = new TreeItem(parent, SWT.NONE);
			item.setData(listOfState.get(i));
			item.setText("State: " + listOfState.get(i).toString());
		}

		parent.setExpanded(true);
	}

	private ArrayList getAllDetailState() {
		ArrayList list = new ArrayList();


		Element root        = null;
		Element order       = null;
		//Element params_     = null;
		List process     = null;

		try {
			SAXBuilder builder = new SAXBuilder();
			filename = _dom.getFilename();
			if(filename == null || filename.length() == 0)
				return list;

			Document doc = builder.build( new File( filename ) );

			root = doc.getRootElement();

			Element application = root.getChild("job_chain");
			if(application == null) {
				application = root.getChild("application");
			}

			jobChainname = Utils.getAttributeValue("name", application);

			if(application != null)
				order =   application.getChild("order");


			if(order != null) {
				process = order.getChildren("process");
			}

			for (int i = 0; i < process.size(); i++) {
				Element p = (Element)process.get(i);
				list.add(Utils.getAttributeValue("state", p));
			}

		} catch(Exception e) {
			try {
				new ErrorLog("error in " + SOSClassUtil.getMethodName(), e);
			} catch(Exception ee) {

			}

			System.err.println("..error im JobChainConfigurationListener.getAllDetailState(): " + e.getMessage());
		}

		return list;
	}

	public void treeExpandJob(final TreeItem parent, final String job) {

		if (parent.getText().equals("Jobs")) {

			for (int i = 0; i < parent.getItemCount(); i++)
				if (parent.getItem(i).getText().equals(job)) {
					parent.getItem(i).setExpanded(true);
				}

		}

	}

	public boolean treeSelection(final Tree tree, final Composite c) {
		try {
			if (tree.getSelectionCount() > 0) {

				// dispose the old form
				Control[] children = c.getChildren();
				for (int i = 0; i < children.length; i++) {
					if (!Utils.applyFormChanges(children[i]))
						return false;
					children[i].dispose();
				}

				TreeItem item = tree.getSelection()[0];
				_dom.setInit(true);
				DetailForm df = null;
				try {
					if(jobChainname == null) {

						df = new DetailForm(c, SWT.NONE, Editor.DETAILS, _dom, _gui, false, null);
						df.setLayout(new org.eclipse.swt.layout.FillLayout());

					} else {
						Composite composite = new Composite(c.getShell(), SWT.NONE);
						composite.setLayout(new org.eclipse.swt.layout.FillLayout());
						df = new DetailForm(composite, SWT.NONE, jobChainname, item.getData() != null && !(item.getData() instanceof sos.scheduler.editor.app.TreeData)? item.getData().toString(): null, null, Editor.DETAILS, _dom, _gui, false, null);
						df.setLayout(new org.eclipse.swt.layout.FillLayout());
					}
					df.setTree(tree);
					df.setJobChainConfigurationListener(this);

					df.open();

					if(df.hasErrors()) {
						df.dispose();

						_gui.close();
						return false;
					}

				} catch (Exception e) {

					try {
						new ErrorLog("error in " + SOSClassUtil.getMethodName(), e);
					} catch(Exception ee) {

					}

					MainWindow.message(e.getMessage(), SWT.ICON_ERROR);
					df.dispose();

					_gui.close();
					return false;
				}

			}

			c.layout();

		} catch (Exception e) {
			c.layout();
			try {
				new ErrorLog("error in " + SOSClassUtil.getMethodName(), e);
			} catch(Exception ee) {

			}

			e.printStackTrace();
			MainWindow.message(e.getMessage(), SWT.ICON_ERROR);
		}
		_dom.setInit(false);
		return true;
	}

	public void setJobChainname(final String jobChainname) {
		this.jobChainname = jobChainname;
	}


	public DetailDom getDom() {
		return _dom;
	}


	public void setDom(final DetailDom _dom) {
		this._dom = _dom;
	}

	public void deleteState(final String state, final TreeItem parent) {
		parent.removeAll();
		Element jobChain = _dom.getRoot().getChild("job_chain");
		if (jobChain != null) {
			//Iterator it = jobChain.getChildren().iterator();
			Element order = jobChain.getChild("order");
			List  pList = order.getChildren("process");
			for(int i = 0; i< pList.size(); i++) {
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
