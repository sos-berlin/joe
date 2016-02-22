package com.sos.event.service.forms;
import java.io.File;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.TraverseEvent;
import org.eclipse.swt.events.TraverseListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.DirectoryDialog;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import com.sos.dialog.swtdesigner.SWTResourceManager;
import com.sos.joe.globals.messages.ErrorLog;
import com.sos.joe.globals.messages.SOSJOEMessageCodes;
import com.sos.joe.globals.misc.ResourceManager;
import com.sos.joe.globals.options.Options;
import com.sos.joe.xml.DomParser;

public class SaveEventsDialogForm {
	private Shell		_shell			= null;
	private Group		eventgroup		= null;
	private Text		txtName			= null;
	private Text		txtJobChain		= null;
	private Text		txtJob			= null;
	private Text		txtEventClass	= null;
	private Button		butDirectory	= null;
	private Label		lblDirectory	= null;
	private DomParser	dom				= null;
	private String		filename		= null;
	private Button		butApply		= null;

	public SaveEventsDialogForm(DomParser  dom_) {
		try {
			dom = dom_;
			showForm();
			init();
			while (!_shell.isDisposed()) {
				if (!_shell.getDisplay().readAndDispatch())
					_shell.getDisplay().sleep();
			}		
		}
		catch (Exception e) {
			new ErrorLog(SOSJOEMessageCodes.JOE_E_0002.params(sos.util.SOSClassUtil.getMethodName()), e);
		}
	}
	

	private void init() throws Exception {
		String filename = "";
		try {
			filename = dom.getFilename();
			if (filename == null || filename.length() == 0)
				return;
			File f = new File(filename);
			if (f.getParent() != null)
				lblDirectory.setText(normalized(f.getParent()));
			String[] split = f.getName().split("\\.");
			if (f.getName().endsWith("job_chain.actions.xml")) {
				txtJobChain.setText(split[0]);
				txtName.setText(f.getName().substring(split[0].length() + 1, f.getName().indexOf("job_chain.actions.xml") - 1));
				txtJobChain.setEnabled(true);
				txtJob.setEnabled(false);
				txtEventClass.setEnabled(false);
			}
			else
				if (f.getName().endsWith("job.actions.xml")) {
					txtJob.setText(split[0]);
					txtName.setText(f.getName().substring(split[0].length() + 1, f.getName().indexOf("job.actions.xml") - 1));
					txtJobChain.setEnabled(false);
					txtJob.setEnabled(true);
					txtEventClass.setEnabled(false);
				}
				else
					if (f.getName().endsWith("event_class.actions.xml")) {
						txtEventClass.setText(split[0]);
						txtName.setText(f.getName().substring(split[0].length() + 1, f.getName().indexOf("event_class.actions.xml") - 1));
						txtJobChain.setEnabled(false);
						txtJob.setEnabled(false);
						txtEventClass.setEnabled(true);
					}
					else
						if (f.getName().endsWith(".actions.xml")) {
							txtName.setText(f.getName().substring(0, f.getName().indexOf("actions.xml") - 1));
						}
		}
		catch (Exception e) {
			new ErrorLog(SOSJOEMessageCodes.JOE_E_0002.params(sos.util.SOSClassUtil.getMethodName()), e);
			throw e;
		}
	}

	public void showForm() {
		_shell = new Shell(ErrorLog.getSShell(), SWT.TITLE | SWT.CLOSE | SWT.APPLICATION_MODAL | SWT.BORDER | SWT.RESIZE);
		_shell.addTraverseListener(new TraverseListener() {
			public void keyTraversed(final TraverseEvent e) {
				if (e.detail == SWT.TRAVERSE_ESCAPE) {
					_shell.dispose();
				}
			}
		});
		_shell.setImage(ResourceManager.getImageFromResource("/sos/scheduler/editor/editor.png"));
		final GridLayout gridLayout = new GridLayout();
		gridLayout.marginTop = 5;
		gridLayout.marginRight = 5;
		gridLayout.marginLeft = 5;
		gridLayout.marginBottom = 5;
		_shell.setLayout(gridLayout);
		// _shell.setSize(425, 278);
		_shell.setSize(625, 325);
		_shell.setText(SOSJOEMessageCodes.JOE_M_SaveEventhandler.label());
		{
			eventgroup = SOSJOEMessageCodes.JOE_G_SaveEventsDialogForm_NameSpec.Control(new Group(_shell, SWT.NONE));
			final GridData gridData = new GridData(GridData.BEGINNING, GridData.FILL, true, true);
			gridData.widthHint = 581;
			gridData.heightHint = 77;
			eventgroup.setLayoutData(gridData);
			final GridLayout gridLayout_1 = new GridLayout();
			gridLayout_1.numColumns = 3;
			gridLayout_1.verticalSpacing = 10;
			gridLayout_1.horizontalSpacing = 10;
			gridLayout_1.marginWidth = 10;
			gridLayout_1.marginTop = 10;
			gridLayout_1.marginRight = 10;
			gridLayout_1.marginLeft = 10;
			gridLayout_1.marginHeight = 10;
			gridLayout_1.marginBottom = 10;
			eventgroup.setLayout(gridLayout_1);
			final Label nameLabel = SOSJOEMessageCodes.JOE_L_Name.Control(new Label(eventgroup, SWT.NONE));
			nameLabel.setLayoutData(new GridData());
			txtName = SOSJOEMessageCodes.JOE_T_SaveEventsDialogForm_Name.Control(new Text(eventgroup, SWT.BORDER));
			txtName.setBackground(SWTResourceManager.getColor(255, 255, 217));
			txtName.addModifyListener(new ModifyListener() {
				public void modifyText(final ModifyEvent e) {
					refresh();
				}
			});
			txtName.setLayoutData(new GridData(GridData.FILL, GridData.CENTER, true, false, 2, 1));
			final Label jobketteLabel = SOSJOEMessageCodes.JOE_L_SaveEventsDialogForm_JobChain.Control(new Label(eventgroup, SWT.NONE));
			jobketteLabel.setLayoutData(new GridData());
			txtJobChain = SOSJOEMessageCodes.JOE_T_SaveEventsDialogForm_JobChain.Control(new Text(eventgroup, SWT.BORDER));
			txtJobChain.addModifyListener(new ModifyListener() {
				public void modifyText(final ModifyEvent e) {
					refresh();
				}
			});
			txtJobChain.setLayoutData(new GridData(GridData.FILL, GridData.CENTER, true, false, 2, 1));
			final Label jobLabel = SOSJOEMessageCodes.JOE_L_SaveEventsDialogForm_Job.Control(new Label(eventgroup, SWT.NONE));
			jobLabel.setLayoutData(new GridData());
			txtJob = SOSJOEMessageCodes.JOE_T_SaveEventsDialogForm_Job.Control(new Text(eventgroup, SWT.BORDER));
			txtJob.addModifyListener(new ModifyListener() {
				public void modifyText(final ModifyEvent e) {
					refresh();
				}
			});
			txtJob.setLayoutData(new GridData(GridData.FILL, GridData.CENTER, true, false, 2, 1));
			final Label eventClassLabel = SOSJOEMessageCodes.JOE_L_SaveEventsDialogForm_EventClass.Control(new Label(eventgroup, SWT.NONE));
			eventClassLabel.setLayoutData(new GridData());
			txtEventClass = SOSJOEMessageCodes.JOE_T_SaveEventsDialogForm_EventClass.Control(new Text(eventgroup, SWT.BORDER));
			txtEventClass.addModifyListener(new ModifyListener() {
				public void modifyText(final ModifyEvent e) {
					refresh();
				}
			});
			txtEventClass.setLayoutData(new GridData(GridData.FILL, GridData.CENTER, true, false, 2, 1));
			lblDirectory = new Label(eventgroup, SWT.SHADOW_IN | SWT.CENTER | SWT.BORDER);
			lblDirectory.setAlignment(SWT.CENTER);
			try {
				lblDirectory.setText(new File(Options.getSchedulerData(), "config/events").getCanonicalPath());
			}
			catch (Exception e) {
			}
			final GridData gridData_1 = new GridData(GridData.FILL, GridData.FILL, true, false, 2, 1);
			lblDirectory.setLayoutData(gridData_1);
			butDirectory = SOSJOEMessageCodes.JOE_B_SaveEventsDialogForm_Directory.Control(new Button(eventgroup, SWT.NONE));
			butDirectory.addSelectionListener(new SelectionAdapter() {
				public void widgetSelected(final SelectionEvent e) {
					DirectoryDialog fdialog = new DirectoryDialog(ErrorLog.getSShell(), SWT.MULTI);
					fdialog.setFilterPath(Options.getLastDirectory());
					fdialog.setText(SOSJOEMessageCodes.JOE_M_SaveEventhandler.label() + "...");
					String path = fdialog.open();
					if (path != null)
						lblDirectory.setText(normalized(path));
				}
			});
			butDirectory.setLayoutData(new GridData(GridData.END, GridData.CENTER, false, false));
			butApply = SOSJOEMessageCodes.JOE_B_SaveEventsDialogForm_Save.Control(new Button(eventgroup, SWT.NONE));
			butApply.setLayoutData(new GridData(GridData.FILL, GridData.CENTER, false, false));
			butApply.setEnabled(false);
			butApply.addSelectionListener(new SelectionAdapter() {
				public void widgetSelected(final SelectionEvent e) {
					if (txtJob.getEnabled() && txtJobChain.getEnabled() && txtEventClass.getEnabled()
							&& (txtJob.getText().length() + txtJobChain.getText().length() + txtEventClass.getText().length()) == 0) {
						filename = txtName.getText() + ".actions.xml";
					}
					else
						if (txtJob.getEnabled()) {
							filename = txtJob.getText() + "." + txtName.getText() + ".job.actions.xml";
						}
						else
							if (txtJobChain.getEnabled()) {
								filename = txtJobChain.getText() + "." + txtName.getText() + ".job_chain.actions.xml";
							}
							else
								if (txtEventClass.getEnabled()) {
									filename = txtEventClass.getText() + "." + txtName.getText() + ".event_class.actions.xml";
								}
					filename = normalized(lblDirectory.getText()) + filename;
					File _file = new File(filename);
					boolean ok_ = true;
					if (_file.exists()) {
						int ok = ErrorLog.message(SOSJOEMessageCodes.JOE_M_OverwriteFile.label(), //$NON-NLS-1$
								SWT.ICON_QUESTION | SWT.YES | SWT.NO);
						if (ok == SWT.NO) {
							ok_ = false;
						}
					}
					if (ok_) {
						dom.setFilename(filename);
						_shell.dispose();
					}
				}
			});
			final Button cancelButton = SOSJOEMessageCodes.JOE_B_SaveEventsDialogForm_Cancel.Control(new Button(eventgroup, SWT.NONE));
			cancelButton.setLayoutData(new GridData(GridData.END, GridData.CENTER, true, false, 2, 1));
			cancelButton.addSelectionListener(new SelectionAdapter() {
				public void widgetSelected(final SelectionEvent e) {
					_shell.dispose();
				}
			});
		}
		setToolTipText();
		_shell.layout();
		_shell.open();
	}

	private void refresh() {
		butApply.setEnabled(txtName.getText().length() > 0);
		txtJobChain.setEnabled((txtJob.getText().length() + txtEventClass.getText().length()) == 0);
		txtJob.setEnabled((txtJobChain.getText().length() + txtEventClass.getText().length()) == 0);
		txtEventClass.setEnabled((txtJob.getText().length() + txtJobChain.getText().length()) == 0);
	}

	public void setToolTipText() {
		//
	}

	/**
	 * @return the filename
	 */
	public String getFilename() {
		return filename;
	}

	/**
	 * @param filename the filename to set
	 */
	public void setFilename(String filename) {
		this.filename = filename;
	}

	private String normalized(String path) {
		if (path == null)
			return "";
		path = path.replaceAll("\\\\", "/");
		if (path.endsWith("/") || path.endsWith("\\"))
			return path;
		else
			return path + "/";
	}
}
