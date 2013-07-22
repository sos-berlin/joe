package sos.scheduler.editor.conf.composites;

import static sos.scheduler.editor.app.SOSJOEMessageCodes.JOE_B_JobMainComposite_BrowseProcessClass;
import static sos.scheduler.editor.app.SOSJOEMessageCodes.JOE_B_JobMainComposite_ShowProcessClass;
import static sos.scheduler.editor.app.SOSJOEMessageCodes.JOE_Cbo_JobMainComposite_ProcessClass;
import static sos.scheduler.editor.app.SOSJOEMessageCodes.JOE_G_JobMainComposite_MainOptions;
import static sos.scheduler.editor.app.SOSJOEMessageCodes.JOE_L_JobMainComposite_JobName;
import static sos.scheduler.editor.app.SOSJOEMessageCodes.JOE_L_JobMainComposite_JobTitle;
import static sos.scheduler.editor.app.SOSJOEMessageCodes.JOE_L_JobMainComposite_ProcessClass;
import static sos.scheduler.editor.app.SOSJOEMessageCodes.JOE_T_JobMainComposite_JobName;
import static sos.scheduler.editor.app.SOSJOEMessageCodes.JOE_T_JobMainComposite_JobTitle;

import org.apache.log4j.Logger;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.VerifyEvent;
import org.eclipse.swt.events.VerifyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

import sos.scheduler.editor.app.ContextMenu;
import sos.scheduler.editor.app.Editor;
import sos.scheduler.editor.app.ErrorLog;
import sos.scheduler.editor.app.IOUtils;
import sos.scheduler.editor.app.MergeAllXMLinDirectory;
import sos.scheduler.editor.app.Utils;
import sos.scheduler.editor.classes.CompositeBaseClass;
import sos.scheduler.editor.classes.SOSComboBox;
import sos.scheduler.editor.conf.listeners.JobListener;

public class JobMainComposite extends CompositeBaseClass {
	@SuppressWarnings("unused")
	private final String	conSVNVersion		= "$Id$";

	private final int		intNoOfLabelColumns	= 2;
	@SuppressWarnings("unused")
	private static Logger	logger				= Logger.getLogger(JobMainComposite.class);
	@SuppressWarnings("unused")
	private final String	conClassName		= "JobMainForm";

	private JobListener		objDataProvider		= null;
	private Group			gMain				= null;

	private Text			tbxJobName			= null;
	private Label			lblJobTitlelabel1	= null;
	@SuppressWarnings("unused")
	private Label			lblProcessClass		= null;
	private Text			tbxJobTitle			= null;
	private SOSComboBox		cProcessClass		= null;
	private Button			butBrowse			= null;
	private Button			butShowProcessClass	= null;
	private Label			label				= null;
//	private final int		intComboBoxStyle	= SWT.NONE;
	private GridLayout		gridLayout			= null;

	/**
	 * Create the composite.
	 * @param parent
	 * @param style
	 */
	public JobMainComposite(final Group parent, final int style, final JobListener objDataProvider_) {
		super(parent, style);
		try {
			parent.setRedraw(false);
			objDataProvider = objDataProvider_;
			objParent = parent;
			createGroup(parent);
		}
		catch (Exception e) {
			new ErrorLog(e.getLocalizedMessage(), e);
		}
		finally {
			parent.setRedraw(true);
			parent.layout();
		}
	}

	private void createGroup(final Group parent) {

		gridLayout = new GridLayout();
		gridLayout.marginHeight = 1;
		gridLayout.numColumns = 6;

		gMain = JOE_G_JobMainComposite_MainOptions.Control(new Group(parent, SWT.NONE));
		gMain.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false));
		gMain.setLayout(gridLayout);

		label = JOE_L_JobMainComposite_JobName.Control(new Label(gMain, SWT.NONE));
		label.setLayoutData(new GridData(GridData.BEGINNING, GridData.END, false, false, intNoOfLabelColumns, 1));

		tbxJobName = JOE_T_JobMainComposite_JobName.Control(new Text(gMain, SWT.BORDER));
		tbxJobName.addVerifyListener(new VerifyListener() {
			@Override
			public void verifyText(final VerifyEvent e) {
				if (!init) {
					e.doit = Utils.checkElement(objDataProvider.getJobName(), objDataProvider.get_dom(), Editor.JOB, null);
				}
			}
		});
		tbxJobName.setLayoutData(new GridData(GridData.FILL, GridData.CENTER, true, false, 4, 1));
		tbxJobName.addModifyListener(new ModifyListener() {
			@Override
			public void modifyText(final ModifyEvent e) {
				if (init) {
					return;
				}
				checkName();
				objDataProvider.setJobName(tbxJobName.getText(), true);
				parent.setText(objDataProvider.getJobNameAndTitle());
			}
		});

		lblJobTitlelabel1 = JOE_L_JobMainComposite_JobTitle.Control(new Label(gMain, SWT.NONE));
		lblJobTitlelabel1.setLayoutData(new GridData(GridData.BEGINNING, GridData.CENTER, false, false, intNoOfLabelColumns, 1));

		tbxJobTitle = JOE_T_JobMainComposite_JobTitle.Control(new Text(gMain, SWT.BORDER));
		tbxJobTitle.setLayoutData(new GridData(GridData.FILL, GridData.CENTER, false, false, 4, 1));
		tbxJobTitle.addModifyListener(new ModifyListener() {
			@Override
			public void modifyText(final ModifyEvent e) {
				if (init)
					return;
				objDataProvider.setTitle(tbxJobTitle.getText());
			}
		});

		// tbxJobTitle.setItems(Options.getJobTitleList());

		lblProcessClass = JOE_L_JobMainComposite_ProcessClass.Control(new Label(gMain, SWT.NONE));

		// butShowProcessClass = JOE_goto.Control(new Button(gMain, SWT.ARROW | SWT.DOWN));
		butShowProcessClass = JOE_B_JobMainComposite_ShowProcessClass.Control(new Button(gMain, SWT.ARROW | SWT.DOWN));
		butShowProcessClass.setVisible(objDataProvider.get_dom() != null && !objDataProvider.get_dom().isLifeElement());
		butShowProcessClass.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(final SelectionEvent e) {
				String strT = cProcessClass.getText();
				if (strT.length() > 0) {
					ContextMenu.goTo(strT, objDataProvider.get_dom(), Editor.PROCESS_CLASSES);
				}
			}
		});
		butShowProcessClass.setAlignment(SWT.RIGHT);
		butShowProcessClass.setVisible(true);

		cProcessClass = new SOSComboBox(gMain, JOE_Cbo_JobMainComposite_ProcessClass);
		cProcessClass.setEditable(false);
		cProcessClass.setMenu(new ContextMenu(cProcessClass, objDataProvider.get_dom(), Editor.PROCESS_CLASSES).getMenu());

		cProcessClass.addModifyListener(new ModifyListener() {
			@Override
			public void modifyText(final ModifyEvent e) {

				if (init) {
					return;
				}
				objDataProvider.setProcessClass(cProcessClass.getText());
				butShowProcessClass.setVisible(true);
			}
		});
		cProcessClass.setLayoutData(new GridData(GridData.FILL, GridData.CENTER, false, false, 3, 1));
		cProcessClass.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(final SelectionEvent e) {

				if (init) {
					return;
				}
				objDataProvider.setProcessClass(cProcessClass.getText());
			}
		});

		cProcessClass.addKeyListener(new KeyListener() {
			@Override
			public void keyPressed(final KeyEvent event) {
				if (event.keyCode == SWT.F1) {
					objDataProvider.openXMLAttributeDoc("job", "process_class");
				}
				if (event.keyCode == SWT.F10) {
					objDataProvider.openXMLDoc("job");
				}
			}

			@Override
			public void keyReleased(final KeyEvent arg0) {
			}
		});

		cProcessClass.addMouseListener(new MouseListener() {

			@Override
			public void mouseUp(final MouseEvent arg0) {
			}

			@Override
			public void mouseDown(final MouseEvent arg0) {
			}

			@Override
			public void mouseDoubleClick(final MouseEvent arg0) {
				String strT = cProcessClass.getText();
				if (strT.length() > 0) {
					ContextMenu.goTo(strT, objDataProvider.get_dom(), Editor.PROCESS_CLASSES);
				}
			}
		});

		butBrowse = JOE_B_JobMainComposite_BrowseProcessClass.Control(new Button(gMain, SWT.NONE));
		butBrowse.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(final SelectionEvent e) {
				String name = IOUtils.getJobschedulerObjectPathName(MergeAllXMLinDirectory.MASK_PROCESS_CLASS);
				if (name != null && name.length() > 0)
					cProcessClass.setText(name);
			}
		});
	}

	public void init() {
		init = true;
		tbxJobName.setText(objDataProvider.getJobName());
		tbxJobTitle.setText(objDataProvider.getTitle());
		cProcessClass.setItems(objDataProvider.getProcessClasses());
		String process_class = objDataProvider.getProcessClass();
		cProcessClass.setText(process_class);
		init = false;
		tbxJobName.setFocus();
	}

	private void checkName() {
		if (Utils.existName(tbxJobName.getText(), objDataProvider.getJob(), "job")) {
			tbxJobName.setBackground(Display.getCurrent().getSystemColor(SWT.COLOR_YELLOW));
		}
		else {
			tbxJobName.setBackground(null);
		}
	}

	public Group getgMain() {
		return gMain;
	}

	@Override
	protected void applyInputFields(final boolean flgT) {
	}

}
