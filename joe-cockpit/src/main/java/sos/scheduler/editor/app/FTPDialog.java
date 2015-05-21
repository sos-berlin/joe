package sos.scheduler.editor.app;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.TraverseEvent;
import org.eclipse.swt.events.TraverseListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;

import sos.ftp.profiles.FTPDialogListener;
import sos.ftp.profiles.FTPProfileJadeClient;
import sos.ftp.profiles.FTPProfilePicker;
import sos.scheduler.editor.conf.forms.SchedulerForm;
import sos.util.SOSString;

import com.sos.joe.globals.messages.ErrorLog;
import com.sos.joe.globals.messages.Messages;
import com.sos.joe.globals.misc.ResourceManager;
import com.sos.joe.globals.options.Options;
import com.sos.joe.xml.DomParser;
import com.sos.joe.xml.jobscheduler.SchedulerDom;
import com.sos.VirtualFileSystem.common.SOSFileEntry;
import com.sos.dialog.swtdesigner.SWTResourceManager;

public abstract class FTPDialog {
	protected Button		    butExecute			    	= null;
	private Group				schedulerGroup				= null;
	protected Shell				schedulerConfigurationShell	= null;
	protected FTPDialogListener	listener					= null;
	protected Table				directoryTable				= null;
	protected Text				txtDir						= null;
	protected SOSString			sosString					= new SOSString();
	protected Text				txtFilename					= null;
 	private Text				txtLog						= null;
	private Button				butChangeDir				= null;
	private Button				butRefresh					= null;
	private Button				butNewFolder				= null;
	private Button				butRemove					= null;
	private TableColumn			newColumnTableColumn_1		= null;
	private Button				butSite						= null;
	private Button				butClose					= null;
	private FTPProfilePicker	ftpProfilePicker			= null;
	private TableColumn			newColumnTableColumn_2		= null;
	protected FTPProfileJadeClient ftpProfileJadeClient       = null; 
		
    abstract String getTitle();
    abstract void setTxtFilenameText(Text txtFilename, TableItem tableItem);
    abstract String getFilenameLabel();
    abstract void fillTable(Table directoryTable, HashMap <String, SOSFileEntry> h);
    abstract void setTooltip();
    abstract void execute();
    
	public FTPDialog() {
 	}

	/**
	 * @wbp.parser.entryPoint
	 */
	public void showForm() {
		try {
 			schedulerConfigurationShell = new Shell(MainWindow.getSShell(), SWT.CLOSE | SWT.TITLE | SWT.APPLICATION_MODAL | SWT.BORDER | SWT.RESIZE);
			schedulerConfigurationShell.setImage(ResourceManager.getImageFromResource("/sos/scheduler/editor/editor.png"));
			schedulerConfigurationShell.addTraverseListener(new TraverseListener() {
				public void keyTraversed(final TraverseEvent e) {
					if (e.detail == SWT.TRAVERSE_ESCAPE) {
						try {
							schedulerConfigurationShell.dispose();
						}
						catch (Exception r) {
							try {
								new ErrorLog("error in " + sos.util.SOSClassUtil.getMethodName(), r);
							}
							catch (Exception ee) {
								// tu nichts
							}
						}
					}
				}
			});
			final GridLayout gridLayout = new GridLayout();
			gridLayout.numColumns = 2;
			gridLayout.marginTop = 5;
			gridLayout.marginRight = 5;
			gridLayout.marginLeft = 5;
			gridLayout.marginBottom = 5;
			schedulerConfigurationShell.setLayout(gridLayout);
			schedulerConfigurationShell.setSize(625, 486);
			schedulerConfigurationShell.setText(getTitle());
			{
				schedulerGroup = new Group(schedulerConfigurationShell, SWT.NONE);
				schedulerGroup.setText("Open");
				final GridData gridData = new GridData(GridData.FILL, GridData.FILL, true, true, 2, 1);
				gridData.widthHint = 581;
				gridData.heightHint = 329;
				schedulerGroup.setLayoutData(gridData);
				final GridLayout gridLayout_1 = new GridLayout();
				gridLayout_1.numColumns = 3;
				gridLayout_1.marginTop = 5;
				gridLayout_1.marginRight = 5;
				gridLayout_1.marginLeft = 5;
				gridLayout_1.marginBottom = 5;
				schedulerGroup.setLayout(gridLayout_1);
				ftpProfilePicker = new FTPProfilePicker(schedulerGroup, SWT.NONE, new File(Options.getSchedulerData(), "config/factory.ini"));

				ftpProfilePicker.setLayoutData(new GridData(GridData.FILL, GridData.CENTER, true, false, 2, 1));
				listener = ftpProfilePicker.getListener();
				if (ftpProfilePicker.getSelectedProfilename() != null && ftpProfilePicker.getSelectedProfilename().length() > 0) {
					ftpProfilePicker.getProfileByName(ftpProfilePicker.getSelectedProfilename());
					listener = ftpProfilePicker.getListener();
				}
				ftpProfilePicker.addSelectionListener((new SelectionAdapter() {
					public void widgetSelected(final SelectionEvent e) {
						try {
							txtDir.setText("");
							directoryTable.removeAll();
							txtFilename.setText("");
							listener.setCurrProfileName(ftpProfilePicker.getSelectedProfilename());
							ftpProfileJadeClient = new FTPProfileJadeClient(listener.getCurrProfile());
							initForm();
							butExecute.setEnabled( txtFilename.getText().length() > 0);
							_setEnabled(true);
						}
						catch (Exception r) {
							MainWindow.message("error while choice Profilename: " + e.toString(), SWT.ICON_WARNING);
							try {
								new ErrorLog("error in " + sos.util.SOSClassUtil.getMethodName(), r);
							}
							catch (Exception ee) {
								// tu nichts
							}
						}
					}
				}));
				butSite = new Button(schedulerGroup, SWT.NONE);
				butSite.setLayoutData(new GridData(GridData.FILL, GridData.CENTER, false, false));
				butSite.addSelectionListener(new SelectionAdapter() {
					public void widgetSelected(final SelectionEvent e) {
						Utils.startCursor(schedulerConfigurationShell);
						try {
							if (listener.getProfileNames().length == 0) {
								MainWindow.message("Please first define a Profile", SWT.ICON_WARNING);
								return;
							}
	                        
						
						    disconnect();
					        txtDir.setText(listener.getCurrProfile().getRoot());

	                        ftpProfileJadeClient = new FTPProfileJadeClient(listener.getCurrProfile());
                            fillTable(ftpProfileJadeClient.getDirectoryContent(listener.getCurrProfile().getRoot()));
                            _setEnabled(true);
						}
						catch (Exception ex) {
							try {
								MainWindow.message("error while connecting: " + ex.toString(), SWT.ICON_WARNING);
								new ErrorLog("error in " + sos.util.SOSClassUtil.getMethodName(), ex);
							}
							catch (Exception ee) {
								// tu nichts
							}
						}
						Utils.stopCursor(schedulerConfigurationShell);
					}
				});
				butSite.setText("Connect");
				 
				txtDir = new Text(schedulerGroup, SWT.BORDER);
				txtDir.addKeyListener(new KeyAdapter() {
					public void keyPressed(final KeyEvent e) {
						try {
							if (e.keyCode == SWT.CR) {
								fillTable(ftpProfileJadeClient.getDirectoryContent(txtDir.getText()));
							}
						}
						catch (Exception r) {
							MainWindow.message("error while reading directory: " + e.toString(), SWT.ICON_WARNING);
							try {
								new ErrorLog("error in " + sos.util.SOSClassUtil.getMethodName(), r);
							}
							catch (Exception ee) {
								// tu nichts
							}
						}
					}
				});
				txtDir.setLayoutData(new GridData(GridData.FILL, GridData.CENTER, true, false, 2, 1));
				butChangeDir = new Button(schedulerGroup, SWT.NONE);
				butChangeDir.addSelectionListener(new SelectionAdapter() {
					public void widgetSelected(final SelectionEvent e) {
						try {
				     	    fillTable(ftpProfileJadeClient.getDirectoryContent(txtDir.getText()+"/"+txtFilename.getText()));
				     	    txtDir.setText(txtDir.getText()+"/"+txtFilename.getText());
				     	    txtFilename.setText("");
						}
						catch (Exception r) {
							MainWindow.message("error: " + e.toString(), SWT.ICON_WARNING);
							try {
								new ErrorLog("error in " + sos.util.SOSClassUtil.getMethodName(), r);
							}
							catch (Exception ee) {
								// tu nichts
							}
						}
					}
				});
				butChangeDir.setLayoutData(new GridData(GridData.FILL, GridData.CENTER, false, false));
				butChangeDir.setText("Change Directory ");
				directoryTable = new Table(schedulerGroup, SWT.FULL_SELECTION | SWT.BORDER);
				directoryTable.setSortDirection(SWT.DOWN);
				directoryTable.addSelectionListener(new SelectionAdapter() {
					public void widgetSelected(final SelectionEvent e) {
						try {
							if (directoryTable.getSelectionCount() > 0) {
								TableItem item = directoryTable.getSelection()[0];
								setTxtFilenameText(txtFilename, item);
							}
							butExecute.setEnabled(true);
						}
						catch (Exception ex) {
							System.err.println(ex.toString());
						}
					}
				});
				directoryTable.addMouseListener(new MouseAdapter() {
					public void mouseDoubleClick(final MouseEvent e) {
						try {
							if (directoryTable.getSelectionCount() > 0) {
								TableItem item = directoryTable.getSelection()[0];
								SOSFileEntry sosFileEntry = (SOSFileEntry) item.getData();
								if (sosFileEntry.isDirectory() && ! sosFileEntry.isDirUp()) {
									txtDir.setText(sosFileEntry.getFullPath());
									txtFilename.setText("");
									fillTable(ftpProfileJadeClient.getDirectoryContent(txtDir.getText()));
								}
								else
									if (sosFileEntry.isDirUp()) {
										String parentPath = sosFileEntry.getParentPath();
	                                    txtDir.setText(parentPath);
	                                    txtFilename.setText("");
	                                    fillTable(ftpProfileJadeClient.getDirectoryContent(parentPath));
									}
									else {
                                        execute();
                                        disconnect();
									}
							}
						}
						catch (Exception r) {
							try {
								new ErrorLog("error in " + sos.util.SOSClassUtil.getMethodName(), r);
							}
							catch (Exception ee) {
								// tu nichts
							}
						}
					}
				});
				directoryTable.setHeaderVisible(true);
				directoryTable.setLayoutData(new GridData(GridData.FILL, GridData.FILL, true, true, 2, 3));
				newColumnTableColumn_2 = new TableColumn(directoryTable, SWT.NONE);
				newColumnTableColumn_2.addSelectionListener(new SelectionAdapter() {
					public void widgetSelected(final SelectionEvent e) {
						sort(newColumnTableColumn_2);
					}
				});
				directoryTable.setSortColumn(newColumnTableColumn_2);
				newColumnTableColumn_2.setMoveable(true);
				newColumnTableColumn_2.setWidth(176);
				newColumnTableColumn_2.setText("Name");
				final TableColumn newColumnTableColumn = new TableColumn(directoryTable, SWT.NONE);
				newColumnTableColumn.addSelectionListener(new SelectionAdapter() {
					public void widgetSelected(final SelectionEvent e) {
						sort(newColumnTableColumn);
					}
				});
				newColumnTableColumn.setWidth(117);
				newColumnTableColumn.setText("Size");
				newColumnTableColumn_1 = new TableColumn(directoryTable, SWT.NONE);
				newColumnTableColumn_1.addSelectionListener(new SelectionAdapter() {
					public void widgetSelected(final SelectionEvent e) {
						sort(newColumnTableColumn_1);
					}
				});
				newColumnTableColumn_1.setWidth(100);
				newColumnTableColumn_1.setText("Type");
				butRefresh = new Button(schedulerGroup, SWT.NONE);
				butRefresh.addSelectionListener(new SelectionAdapter() {
					public void widgetSelected(final SelectionEvent e) {
						refresh();
					}
				});
				butRefresh.setLayoutData(new GridData(GridData.FILL, GridData.BEGINNING, false, false));
				butRefresh.setText("Refresh");
				butNewFolder = new Button(schedulerGroup, SWT.NONE);
				butNewFolder.addSelectionListener(new SelectionAdapter() {
					public void widgetSelected(final SelectionEvent e) {
						openDialog();
					}
				});
				butNewFolder.setLayoutData(new GridData(GridData.FILL, GridData.CENTER, false, false));
				butNewFolder.setText("New Folder");
				butRemove = new Button(schedulerGroup, SWT.NONE);
				butRemove.addSelectionListener(new SelectionAdapter() {
					public void widgetSelected(final SelectionEvent e) {
						if (txtFilename.getText() != null) {
							Utils.startCursor(schedulerConfigurationShell);
							try {
							    if (directoryTable.getSelection().length > 0){
	                                ftpProfileJadeClient.removeFile((SOSFileEntry) directoryTable.getSelection()[0].getData());
	                                fillTable(ftpProfileJadeClient.getDirectoryContent(txtDir.getText()));
							    }
							}
							catch (Exception r) {
								try {
									new ErrorLog("error in " + sos.util.SOSClassUtil.getMethodName(), r);
								}
								catch (Exception ee) {
									// tu nichts
								}
							}
							Utils.stopCursor(schedulerConfigurationShell);
						}
					}
				});
				butRemove.setLayoutData(new GridData(GridData.FILL, GridData.BEGINNING, false, false));
				butRemove.setText("Remove");
				final Label filenameLabel = new Label(schedulerGroup, SWT.NONE);
				filenameLabel.setText(getFilenameLabel());
				txtFilename = new Text(schedulerGroup, SWT.BORDER);
				txtFilename.addModifyListener(new ModifyListener() {
					public void modifyText(final ModifyEvent e) {
						if (listener == null)
							listener = ftpProfilePicker.getListener();
						butExecute.setEnabled(true);
					}
				});
				txtFilename.setLayoutData(new GridData(GridData.FILL, GridData.CENTER, true, false));
				{
					butExecute = new Button(schedulerGroup, SWT.NONE);
					butExecute.setEnabled(false);
					butExecute.setLayoutData(new GridData(GridData.FILL, GridData.CENTER, false, false));
					butExecute.addSelectionListener(new SelectionAdapter() {
						public void widgetSelected(final SelectionEvent e) {
							execute();
							try {
								disconnect();
							} catch (Exception e1) {
								e1.printStackTrace();
							}
						}
					});
					butExecute.setFont(SWTResourceManager.getFont("", 8, SWT.BOLD));
					butExecute.setText(getTitle());
				}
				new Label(schedulerGroup, SWT.NONE);
				new Label(schedulerGroup, SWT.NONE);
				butClose = new Button(schedulerGroup, SWT.NONE);
				butClose.addSelectionListener(new SelectionAdapter() {
					public void widgetSelected(final SelectionEvent e) {
						try {
						    disconnect();
						    schedulerConfigurationShell.dispose();
						}
						catch (Exception r) {
							try {
								new ErrorLog("error in " + sos.util.SOSClassUtil.getMethodName(), r);
							}
							catch (Exception ee) {
								// tu nichts
							}
						}
					}
				});
				butClose.setLayoutData(new GridData(GridData.FILL, GridData.CENTER, false, false));
				butClose.setText("Close");
			}
			txtLog = new Text(schedulerConfigurationShell, SWT.NONE);
			txtLog.setEditable(false);
			txtLog.setLayoutData(new GridData(GridData.FILL, GridData.FILL, true, false));
			ftpProfilePicker.setLogText(txtLog);
			 
			initForm();
			schedulerConfigurationShell.layout();
			schedulerConfigurationShell.open();
		}
		catch (Exception e) {
			try {
				new ErrorLog("error in " + sos.util.SOSClassUtil.getMethodName(), e);
			}
			catch (Exception ee) {
				// tu nichts
			}
			MainWindow.message("could not int FTP Profiles:" + e.getMessage(), SWT.ICON_WARNING);
		}
	}
	
	private void disconnect() throws Exception{
	    if (ftpProfileJadeClient != null){
	       ftpProfileJadeClient.disconnect();
        }
	}


	private void initForm() {
		try {
			setToolTipText();
			if (listener == null) {
				ftpProfilePicker.getProfileByName(ftpProfilePicker.getSelectedProfilename());
				listener = ftpProfilePicker.getListener();
			}
			listener.setRemoteDirectory(txtDir);
			txtDir.setText(listener.getCurrProfile() != null ? listener.getCurrProfile().getRoot() : "");
			_setEnabled(false);
		}
		catch (Exception e) {
			try {
				new ErrorLog("error in " + sos.util.SOSClassUtil.getMethodName(), e);
			}
			catch (Exception ee) {
				// tu nichts
			}
			MainWindow.message("could not int FTP Profiles:" + e.getMessage(), SWT.ICON_WARNING);
		}
	}

	protected void fillTable(HashMap<String, SOSFileEntry> h) {
            Utils.startCursor(schedulerConfigurationShell);
			directoryTable.removeAll();
			fillTable(directoryTable, h);
            directoryTable.setSortDirection(SWT.UP);
			sort(newColumnTableColumn_2);
            Utils.stopCursor(schedulerConfigurationShell);

	} 

	public FTPDialogListener getListener() {
		return listener;
	}

	public void refresh() {
		try {
			Utils.startCursor(schedulerConfigurationShell);
			HashMap <String, SOSFileEntry> h = ftpProfileJadeClient.getDirectoryContent(txtDir.getText());
			fillTable(h);
			Utils.stopCursor(schedulerConfigurationShell);
		}
		catch (Exception r) {
			try {
				MainWindow.message("could not refersh Table, cause: " + r.toString(), SWT.ICON_WARNING);
				new ErrorLog("error in " + sos.util.SOSClassUtil.getMethodName(), r);
			}
			catch (Exception ee) {
				// tu nichts
			}
		}
	}

	public void openDialog() {
		final Shell shell = new Shell();
		shell.pack();
		Dialog dialog = new Dialog(shell);
		dialog.setText("Create New Folder");
		dialog.open(this);
	}

	private void _setEnabled(boolean enabled) {
		txtDir.setEnabled(enabled);
		butChangeDir.setEnabled(enabled);
		butRefresh.setEnabled(enabled);
		butNewFolder.setEnabled(enabled);
		butRemove.setEnabled(enabled);
	}

	private void sort(TableColumn col) {
		try {
			if (directoryTable.getSortDirection() == SWT.DOWN){
                directoryTable.setSortDirection(SWT.UP);
			}else{
				directoryTable.setSortDirection(SWT.DOWN);
			}
			
			directoryTable.setSortColumn(col);
			ArrayList<HashMap<String, String>> listOfSortData = new ArrayList<HashMap<String, String>>();
            HashMap <String,SOSFileEntry>sosFileEntries = new HashMap<String, SOSFileEntry>();
			for (int i = 0; i < directoryTable.getItemCount(); i++) {
				TableItem item = directoryTable.getItem(i);
                HashMap <String,String>hash = new HashMap<String, String>();

                for (int j = 0; j < directoryTable.getColumnCount(); j++) {
					hash.put(directoryTable.getColumn(j).getText(), item.getText(j));
				}
				
                SOSFileEntry sosFileEntry = (SOSFileEntry) item.getData();
                if (!sosFileEntry.isDirUp()){
                   sosFileEntries.put(item.getText(0), sosFileEntry);
				   listOfSortData.add(hash);
                }
				 
			}
			listOfSortData = sos.util.SOSSort.sortArrayList(listOfSortData, col.getText());
			directoryTable.removeAll();
			
			
			SOSFileEntry sosFileEntryDirup = new SOSFileEntry();
			sosFileEntryDirup.setDirectory(true);
			sosFileEntryDirup.setFilename("..");
			sosFileEntryDirup.setFilesize(0);
			sosFileEntryDirup.setParentPath(new File(txtDir.getText()).getParent());
            
			
			TableItem item_ = new TableItem(directoryTable, SWT.NONE);
			item_.setData(sosFileEntryDirup);
			item_.setImage(ResourceManager.getImageFromResource("/sos/scheduler/editor/icon_directory_up.gif"));

			TableItem item = null;
			if (directoryTable.getSortDirection() == SWT.DOWN) {
				// Verzeichnis
				for (int i = 0; i < listOfSortData.size(); i++) {
				    HashMap <String, String> hash = listOfSortData.get(i);
					SOSFileEntry sosFileEntry = sosFileEntries.get(hash.get(directoryTable.getColumn(0).getText()));
					if  (sosFileEntry.isDirectory()) {
						item = new TableItem(directoryTable, SWT.NONE);
						item.setData(sosFileEntry);
                        item.setImage(ResourceManager.getImageFromResource("/sos/scheduler/editor/icon_directory.gif"));
						for (int j = 0; j < directoryTable.getColumnCount(); j++) {
							item.setText(j, hash.get(directoryTable.getColumn(j).getText()));
						}
					}
				}
				// Datei
				for (int i = 0; i < listOfSortData.size(); i++) {
					HashMap <String,String> hash = listOfSortData.get(i);
	                SOSFileEntry sosFileEntry = sosFileEntries.get(hash.get(directoryTable.getColumn(0).getText()));

					if (!sosFileEntry.isDirectory()) {
						item = new TableItem(directoryTable, SWT.NONE);
						item.setData(sosFileEntry);
						item.setImage(ResourceManager.getImageFromResource("/sos/scheduler/editor/icon_file.gif"));
						for (int j = 0; j < directoryTable.getColumnCount(); j++) {
							item.setText(j, hash.get(directoryTable.getColumn(j).getText()));
						}
					}
				}
			}
			else {
				for (int i = listOfSortData.size() - 1; i >= 0; i--) {
					HashMap <String,String>hash = listOfSortData.get(i);
                    SOSFileEntry sosFileEntry = sosFileEntries.get(hash.get(directoryTable.getColumn(0).getText()));
					// Datei
					if (!sosFileEntry.isDirectory()) {
						item = new TableItem(directoryTable, SWT.NONE);
						item.setData(sosFileEntry);
						if (!sosFileEntry.isDirectory())
							item.setImage(ResourceManager.getImageFromResource("/sos/scheduler/editor/icon_file.gif"));
						for (int j = 0; j < directoryTable.getColumnCount(); j++) {
							item.setText(j, hash.get(directoryTable.getColumn(j).getText()));
						}
					}
				}
				// Verzeichnis
				for (int i = listOfSortData.size() - 1; i >= 0; i--) {
					HashMap <String,String> hash = listOfSortData.get(i);
                    SOSFileEntry sosFileEntry = sosFileEntries.get(hash.get(directoryTable.getColumn(0).getText()));

					if (sosFileEntry.isDirectory()) {
						item = new TableItem(directoryTable, SWT.NONE);
						item.setData(sosFileEntry);
						item.setImage(ResourceManager.getImageFromResource("/sos/scheduler/editor/icon_directory.gif"));
						for (int j = 0; j < directoryTable.getColumnCount(); j++) {
							item.setText(j, hash.get(directoryTable.getColumn(j).getText()));
						}
					}
				}
			}
		}
		catch (Exception e) {
			try {
				new ErrorLog("error in " + sos.util.SOSClassUtil.getMethodName(), e);
			}
			catch (Exception ee) {
				// tu nichts
			}
		}
	}

	protected SOSFileEntry getSosFileEntryFromTable() {
    	SOSFileEntry sosFileEntry = null;
        for (int i=1;i<directoryTable.getItems().length;i++){
            TableItem item = directoryTable.getItem(i);
            if (item.getText().equals(txtFilename.getText())){
               sosFileEntry = (SOSFileEntry) item.getData();
            }
        }
        return sosFileEntry;
    }
 
	public void setToolTipText() {
	    
	    setTooltip();
	 
		directoryTable.setToolTipText(Messages.getTooltip("ftpdialog.table"));
		txtDir.setToolTipText(Messages.getTooltip("ftpdialog.directory"));
		txtLog.setToolTipText(Messages.getTooltip("ftpdialog.log"));
		butChangeDir.setToolTipText(Messages.getTooltip("ftpdialog.change_directory"));
		butRefresh.setToolTipText(Messages.getTooltip("ftpdialog.refresh"));
		butNewFolder.setToolTipText(Messages.getTooltip("ftpdialog.new_folder"));
		butRemove.setToolTipText(Messages.getTooltip("ftpdialog.remove"));
		butSite.setToolTipText(Messages.getTooltip("ftpdialog.connect"));
		butClose.setToolTipText(Messages.getTooltip("ftpdialog.close"));
	}

	 
 
	

	
}
