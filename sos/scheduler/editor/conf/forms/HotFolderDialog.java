package sos.scheduler.editor.conf.forms;

import java.io.File;
import java.util.HashMap;
import java.util.Iterator;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.TraverseEvent;
import org.eclipse.swt.events.TraverseListener;
import org.eclipse.swt.events.VerifyEvent;
import org.eclipse.swt.events.VerifyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;

import sos.scheduler.editor.app.ErrorLog;
import sos.scheduler.editor.app.MainWindow;
import sos.scheduler.editor.app.Messages;
import sos.scheduler.editor.app.Options;
import sos.scheduler.editor.app.ResourceManager;
import sos.scheduler.editor.app.Utils;
import sos.util.SOSString;

import com.swtdesigner.SWTResourceManager;

public class HotFolderDialog {


	public  static final int              SCHEDULER_CLUSTER             = 1;

	public  static final int              SCHEDULER_HOST                = 2;

	private              Button           butOK                         = null;

	private              Button           cancelButton                  = null;

	private              Group            schedulerGroup                = null;

	private              Tree             tree                          = null;

	private              int              type                          = -1;

	private              String           sType                         = "";

	private              MainWindow       mainwindow                    = null;

	private              SOSString        sosString                     = null;

	private              Shell            schedulerConfigurationShell   = null;

	private              Button           butRename                     = null;

	private              Text             txtName                       = null;

	private              Text             txtPort                       = null;

	private              Button           butAdd                        = null;


	public HotFolderDialog(MainWindow mainwindow_) {
		sosString = new SOSString();
		mainwindow = mainwindow_;
	}

	public void showForm(int type_) {

		type = type_;
		schedulerConfigurationShell = new Shell(SWT.CLOSE | SWT.TITLE
				| SWT.APPLICATION_MODAL | SWT.BORDER | SWT.RESIZE);
		
		schedulerConfigurationShell.addTraverseListener(new TraverseListener() {
			public void keyTraversed(final TraverseEvent e) {				
				if(e.detail == SWT.TRAVERSE_ESCAPE) {					
					schedulerConfigurationShell.dispose();
				}
			}
		});
		
		schedulerConfigurationShell.setImage(ResourceManager
				.getImageFromResource("/sos/scheduler/editor/editor.png"));
		final GridLayout gridLayout = new GridLayout();
		gridLayout.marginTop = 5;
		gridLayout.marginRight = 5;
		gridLayout.marginLeft = 5;
		gridLayout.marginBottom = 5;
		gridLayout.numColumns = 6;
		schedulerConfigurationShell.setLayout(gridLayout);
		schedulerConfigurationShell.setSize(425, 486);

		sType = "";
		if (type == SCHEDULER_HOST)
			sType = "Host";
		else if (type == SCHEDULER_CLUSTER)
			sType = "Cluster";

		schedulerConfigurationShell.setText("Open Scheduler " + sType
				+ " Configuration");

		{
			schedulerGroup = new Group(schedulerConfigurationShell, SWT.NONE);
			schedulerGroup
			.setText("Open Scheduler " + sType + " Configuration");
			final GridData gridData = new GridData(GridData.FILL,
					GridData.FILL, true, true, 6, 1);
			gridData.widthHint = 581;
			gridData.heightHint = 329;
			schedulerGroup.setLayoutData(gridData);

			final GridLayout gridLayout_1 = new GridLayout();
			gridLayout_1.verticalSpacing = 10;
			gridLayout_1.horizontalSpacing = 10;
			gridLayout_1.marginWidth = 10;
			gridLayout_1.marginTop = 10;
			gridLayout_1.marginRight = 10;
			gridLayout_1.marginLeft = 10;
			gridLayout_1.marginHeight = 10;
			gridLayout_1.marginBottom = 10;
			schedulerGroup.setLayout(gridLayout_1);

			createTree();

			// final Tree tree = new Tree(schedulerGroup, SWT.BORDER);

			final GridData gridData_1 = new GridData(GridData.FILL,
					GridData.FILL, true, true);
			tree.setLayoutData(gridData_1);

		}

		cancelButton = new Button(schedulerConfigurationShell, SWT.NONE);
		cancelButton.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(final SelectionEvent e) {
				schedulerConfigurationShell.dispose();
			}
		});
		cancelButton.setText("Cancel");

		txtName = new Text(schedulerConfigurationShell, SWT.BORDER);
		txtName.addVerifyListener(new VerifyListener() {
			public void verifyText(final VerifyEvent e) {
				e.doit = (e.text.indexOf("#") == -1);
			}
		});
		txtName.addModifyListener(new ModifyListener() {
			public void modifyText(final ModifyEvent e) {
				setButtonRenameEnable();

			}
		});
		txtName.setLayoutData(new GridData(GridData.FILL, GridData.CENTER, true, false));

		if(type == SCHEDULER_HOST) {
			txtPort = new Text(schedulerConfigurationShell, SWT.BORDER);
			txtPort.addVerifyListener(new VerifyListener() {
				public void verifyText(final VerifyEvent e) {
					if(type == SCHEDULER_CLUSTER)
						e.doit = e.text.indexOf("#") == -1;
					else
						e.doit = (e.text.indexOf("#") == -1) && Utils.isOnlyDigits(e.text);;
				}
			});
			txtPort.addModifyListener(new ModifyListener() {
				public void modifyText(final ModifyEvent e) {
					setButtonRenameEnable();
				}
			});
			txtPort.setLayoutData(new GridData(GridData.FILL, GridData.CENTER, true, false));
		}

		butAdd = new Button(schedulerConfigurationShell, SWT.NONE);
		butAdd.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(final SelectionEvent e) {
				addItem();				
			}
		});
		butAdd.setLayoutData(new GridData(GridData.END, GridData.CENTER, false, false));
		butAdd.setText("Add");



		butRename = new Button(schedulerConfigurationShell, SWT.NONE);
		butRename.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(final SelectionEvent e) {
				try {
					if(tree.getSelectionCount() > 0 
							&& !tree.getSelection()[0].getText().equals(sType) 
							&& txtName.getText().trim().length() > 0) {

						File f = new File( sosString.parseToString(tree.getSelection()[0].getData()));
						String path = f.getParent().endsWith("/") || f.getParent().endsWith("\\")  ?  f.getParent() : f.getParent() + "/";

						if(type == SCHEDULER_HOST && txtName.getText().length() > 0 && txtPort.getText().length() == 0) {
//							host ändern
							changeHost(path);														
							return;	
						} else if(type == SCHEDULER_HOST && txtName.getText().length() > 0 && txtPort.getText().length() > 0 
								&& tree.getSelection()[0].getParentItem().getText().equals(sType) ) {
							//ein host wurde selektiert und ein neuer port wurde eingegeben
							//dann soll diese neu hinzugefügt werden
							addItem();
							return;
						} else {
							path = path + txtName.getText();
						}

						if(type == SCHEDULER_HOST)
							path = path + "#" + txtPort.getText();




						if(f.renameTo(new File(path))) {	
							if(type == SCHEDULER_HOST) {
								//port ändern
								tree.getSelection()[0].getParentItem().setText(txtName.getText());
								tree.getSelection()[0].setText(txtPort.getText());											
								tree.getSelection()[0].setData( path);
							} else {
								//scheduler id ändern
								tree.getSelection()[0].setText(txtName.getText());											
								tree.getSelection()[0].setData( path);
							}
						} else{
							MainWindow.message("could not rename configuration: ", SWT.ICON_INFORMATION);
							schedulerConfigurationShell.setFocus();
						}


					}
				} catch (Exception ex) {
					try {
		    			new ErrorLog("error in " + sos.util.SOSClassUtil.getMethodName() + " ; could not rename configuration.", ex);
		    		} catch(Exception ee) {
		    			//tu nichts
		    		}
					MainWindow.message("could not rename configuration: " + ex.getMessage(), SWT.ICON_ERROR);
					schedulerConfigurationShell.setFocus();
				}
			}
		});
		butRename.setEnabled(false);
		butRename.setLayoutData(new GridData(GridData.BEGINNING, GridData.CENTER, true, false));
		butRename.setText("Rename");
		{
			butOK = new Button(schedulerConfigurationShell, SWT.NONE);
			butOK.addSelectionListener(new SelectionAdapter() {
				public void widgetSelected(final SelectionEvent e) {					
					openDirectory();
				}
			});
			butOK.setLayoutData(new GridData(GridData.END, GridData.CENTER,
					false, false));
			butOK.setFont(SWTResourceManager.getFont("", 8, SWT.BOLD));
			butOK.setText("Open");
		}
		setToolTipText();

		schedulerConfigurationShell.layout();
		schedulerConfigurationShell.open();
	}

	

	private void createTree() {

		String mask = "";
		try {

			tree = new Tree(schedulerGroup, SWT.BORDER);

			tree.addSelectionListener(new SelectionAdapter() {
				public void widgetSelected(final SelectionEvent e) {
					if (tree.getSelectionCount() > 0 && !tree.getSelection()[0].getText().equals(sType)) {
						if(type ==SCHEDULER_CLUSTER) {
							txtName.setText(tree.getSelection()[0].getText());

						} else if(type ==SCHEDULER_HOST) {
							if(tree.getSelection()[0].getParentItem().getText().equalsIgnoreCase(sType)) {
								//host wurde selektiert
								txtName.setText(tree.getSelection()[0].getText());
								txtPort.setText("");

							} else {
								//port ist selektiert
								txtName.setText(tree.getSelection()[0].getParentItem().getText());
								txtPort.setText(tree.getSelection()[0].getText());

							}
						}
					} else {
						txtName.setText("");
						if(txtPort != null)
							txtPort.setText("");
						butRename.setEnabled(false);

					}
				}
			});
			tree.addMouseListener(new MouseAdapter() {
				public void mouseDoubleClick(final MouseEvent e) {
					openDirectory();
				}
			});

			tree.setLayoutData(new GridData(GridData.FILL, GridData.FILL, true,true));
			if (type == SCHEDULER_CLUSTER) {				
				mask = "^[^#]+$";
			} else if (type == SCHEDULER_HOST) {
				mask = "^[^#]+#\\d{1,5}$";
			}

			String path = Options.getSchedulerHome().endsWith("/") || Options.getSchedulerHome().endsWith("\\") ? Options.getSchedulerHome() : Options.getSchedulerHome() + "/";
			path = path + "config/remote";

			File p = new File(path); 
			if(!p.exists()) {
				p.mkdirs();
			}
			java.util.Vector filelist = sos.util.SOSFile.getFolderlist(path,
					mask, java.util.regex.Pattern.CASE_INSENSITIVE, false);

			TreeItem rItem = new TreeItem(tree, SWT.NONE);
			rItem.setText(sType);
			rItem.setImage(ResourceManager.getImageFromResource("/sos/scheduler/editor/folder.png"));

			createTreeItem(rItem, filelist, false);

			rItem.setExpanded(true);

		} catch (Exception e) {
			try {
    			new ErrorLog("error in " + sos.util.SOSClassUtil.getMethodName(), e);
    		} catch(Exception ee) {
    			//tu nichts
    		}
			MainWindow.message("..error in create tree for Open Scheduler Cluster/Host " + e.getMessage(), SWT.ICON_ERROR);
			schedulerConfigurationShell.setFocus();
		}

	}

	private void createTreeItem(TreeItem parentItem, java.util.Vector filelist,
			boolean sub) {
		try {
			Iterator fileIterator = filelist.iterator();
			String filename = "";

			while (fileIterator.hasNext()) {
				filename = sosString.parseToString(fileIterator.next());

				File f = new File(filename);
				if(!f.isDirectory())
					continue;

				if(f.getName().equals("_all")) 
					continue;

				String name = f.getName();

				if (type == SCHEDULER_CLUSTER) {
					TreeItem item = new TreeItem(parentItem, SWT.NONE);

					name = f.getName(); 
					item.setText(name);
					item.setData(filename);
					item.setImage(ResourceManager.getImageFromResource("/sos/scheduler/editor/folder.png"));
				} else {

					if (sub) {
						// ports von hast bestimmen
						TreeItem item = new TreeItem(parentItem, SWT.NONE);

						name = f.getName().substring(f.getName().indexOf("#") + 1);
						item.setText(name);
						item.setData(filename);
						item.setImage(ResourceManager.getImageFromResource("/sos/scheduler/editor/folder.png"));
					} else {
						// host bestimmen und ports bilden

						HashMap names = new HashMap();// alle Hostname aufschreiben
						if(sosString.parseToString(name).length() > 0) {
							String sname = name.substring(0, f.getName().indexOf("#"));
							names.put(sname, null);
						}
						while (fileIterator.hasNext()) {
							filename = sosString.parseToString(fileIterator.next());
							f = new File(filename);
							String sname = new File(filename).getName().substring(0, f.getName().indexOf("#"));
							names.put(sname, null);
						}

						String path = Options.getSchedulerHome().endsWith("/")|| Options.getSchedulerHome().endsWith("\\") ? Options.getSchedulerHome(): Options.getSchedulerHome() + "/";
						path = path + "config/remote";
						Iterator hostIterator = names.keySet().iterator();
						while (hostIterator.hasNext()) {
							String sname = sosString.parseToString(hostIterator.next());
							TreeItem newItem = new TreeItem(parentItem,SWT.NONE);
							newItem.setText(sname);
							//newItem.setData(sname);
							newItem.setData(path + "/" + sname);//test
							newItem.setImage(ResourceManager.getImageFromResource("/sos/scheduler/editor/folder.png"));
							//String mask = "^" + sname + ".*\\.scheduler$";
							String mask = "^" + sname + "#";
							java.util.Vector subFilelist = sos.util.SOSFile.getFolderlist(path,
									mask,
									java.util.regex.Pattern.CASE_INSENSITIVE,
									true);
							createTreeItem(newItem, subFilelist, true);
							newItem.setExpanded(true);
						}
						// break;
					}
				}

			}
		} catch (Exception e) {
			try {
    			new ErrorLog("error in " + sos.util.SOSClassUtil.getMethodName(), e);
    		} catch(Exception ee) {
    			//tu nichts
    		}
			MainWindow.message(
					"..error in create tree for Open Scheduler Cluster/Host "
					+ e.getMessage(), SWT.ICON_ERROR);
			schedulerConfigurationShell.setFocus();
		}

	}

	private void openDirectory() {
		try {
			if (tree.getSelectionCount() > 0) {
				String path = sosString.parseToString(tree.getSelection()[0].getData());
				if((tree.getSelection()[0].getItemCount() > 0 && type == SCHEDULER_HOST && !tree.getSelection()[0].getText().equals(sType))
						|| type == SCHEDULER_CLUSTER && tree.getSelection()[0].getText().equals(sType)) {
					//host wurde ausgewählt -> enzsprechende Ports öffnen
					for (int i = 0; i < tree.getSelection()[0].getItemCount(); i++) {
						path = sosString.parseToString(tree.getSelection()[0].getItem(i).getData());
						if (MainWindow.getContainer().openDirectory(path) != null)
							mainwindow.setSaveStatus();
					}

				} else if(!tree.getSelection()[0].getText().equals(sType)){
					if (MainWindow.getContainer().openDirectory(path) != null)
						mainwindow.setSaveStatus();
				}
				schedulerConfigurationShell.close();
			}
		} catch (Exception e) {
			try {
    			new ErrorLog("error in " + sos.util.SOSClassUtil.getMethodName(), e);
    		} catch(Exception ee) {
    			//tu nichts
    		}
			MainWindow.message(
					"..error in create tree for Open Scheduler Cluster/Host "
					+ e.getMessage(), SWT.ICON_ERROR);
			schedulerConfigurationShell.setFocus();
		}

	}

	private void setButtonRenameEnable() {		
		if(tree.getSelectionCount() > 0 && !tree.getSelection()[0].getText().equals(sType)) {
			if(type == SCHEDULER_HOST) {
				butRename.setEnabled(true);
				if(txtName.getText().length() > 0 && txtPort.getText().length() > 0) {
					txtName.setEditable(false);
				} else {
					txtName.setEditable(true);
				}	
			} else {
				if(txtName.getText().length() > 0) {
					butRename.setEnabled(true);
				} else {
					butRename.setEnabled(false);
				}
			}
		} else {
			txtName.setEditable(true);
		}
	}

	private void changeHost(String path) {
		//host ändern
		try {

			String filename = "";

			tree.getSelection()[0].setText(txtName.getText());
			tree.getSelection()[0].setData(path + txtName.getText());

			for(int i = 0; i < tree.getSelection()[0].getItemCount(); i++) {
				TreeItem item = tree.getSelection()[0].getItem(i);
				filename = item.getData().toString();
				File _f = new File(filename);
				String newFilename = path + txtName.getText() + _f.getName().substring(_f.getName().indexOf("#"));
				File _newF = new File(newFilename);
				//System.out.println("rename: " + filename + " in " + newFilename);
				if(!_f.renameTo(_newF)) {
					MainWindow.message("could not rename configuration: " + filename + " in " + newFilename, SWT.ICON_INFORMATION);
				}				
				item.setData(newFilename);
			}

		} catch (Exception e) {
			
			try {
    			new ErrorLog("error in " + sos.util.SOSClassUtil.getMethodName() + " could not change host.", e);
    		} catch(Exception ee) {
    			//tu nichts
    		}
			MainWindow.message(
					"..could not Change Host "
					+ e.getMessage(), SWT.ICON_ERROR);
		}
	}

	private void addItem() {
		try {


			String name = "";

			if(type == SCHEDULER_HOST && (txtName.getText().length() == 0 || txtPort.getText().length() == 0)) {
				MainWindow.message("missing host and port", SWT.NONE);
				schedulerConfigurationShell.setFocus();
				return;
			}



			if(type == SCHEDULER_CLUSTER) {
				name = txtName.getText();
			} else {
				name = txtName.getText() + "#" +txtPort.getText();
			}


			String path = (Options.getSchedulerHome().endsWith("/") || Options.getSchedulerHome().endsWith("\\") ? Options.getSchedulerHome() : Options.getSchedulerHome() + "/") + "config/remote/";
			path = path  + name;

			File newFile = new File(path);
			if(newFile.exists()) {
				MainWindow.message("could not create Remote Directory, cause Directory exist", SWT.NONE);
				schedulerConfigurationShell.setFocus();
				return;						
			} 

			TreeItem item =null;						

			if(type == SCHEDULER_CLUSTER) {						
				item = new TreeItem(tree.getItems()[0], SWT.NONE);
				item.setData(path);
			} 

			if (type == SCHEDULER_HOST ) {
				//herausfinden, ob bereits ein host mit der selben Namen existiert				
				for(int i = 0; i < tree.getItem(0).getItemCount(); i++) {
					if(tree.getItem(0).getItem(i).getText().equalsIgnoreCase(txtName.getText())) {
						item = tree.getItem(0).getItem(i);
						break;
					}
				}
				if(item == null) {
					//ein Host Item existiert nicht, also einen neuen erstellen
					item = new TreeItem(tree.getItems()[0], SWT.NONE);
				}


				TreeItem itemPort = new TreeItem(item, SWT.NONE);
				itemPort.setData(path);
				itemPort.setText(txtPort.getText());
				itemPort.setImage(ResourceManager.getImageFromResource("/sos/scheduler/editor/folder.png"));
			}
			item.setText(txtName.getText());

			item.setImage(ResourceManager.getImageFromResource("/sos/scheduler/editor/folder.png"));
			item.setExpanded(true);								

			txtName.setFocus();
			txtName.setSelection(0, txtName.getText().length());

			newFile = new File(path);
			if(!newFile.exists() ) {
				if(!new File(path).mkdirs()) {
					MainWindow.message("could not crate new remote Directory " , SWT.ICON_ERROR);
					schedulerConfigurationShell.setFocus();
				}
			}
			tree.setSelection(new TreeItem[] {item});

		} catch(Exception ex) {
			try {
    			new ErrorLog("error in " + sos.util.SOSClassUtil.getMethodName() +  " ; Error while creating new " + sType + " Configuration. ", ex);
    		} catch(Exception ee) {
    			//tu nichts
    		}
			MainWindow.message("Error while creating new " + sType + " Configuration: " + ex.getMessage(), SWT.ICON_ERROR);
			schedulerConfigurationShell.setFocus();
		}
	}

	public void setToolTipText() {
		butOK.setToolTipText(Messages.getTooltip("detail.param.open_configuration_file"));
		cancelButton.setToolTipText(Messages.getTooltip("detail.cancel"));
		tree.setToolTipText(Messages.getTooltip("hotfolder.tree." + sType.toLowerCase()));

		if(txtName != null ){
			if(type == SCHEDULER_HOST)
				txtName.setToolTipText(Messages.getTooltip("hotfolder.host"));
			else
				txtName.setToolTipText(Messages.getTooltip("hotfolder.scheduler"));
				
		}
		
		if(txtPort != null)   txtPort.setToolTipText(Messages.getTooltip(""));
		if(butAdd != null)    butAdd.setToolTipText(Messages.getTooltip(""));
		if(butRename != null) butRename.setToolTipText(Messages.getTooltip(""));
	}
}
