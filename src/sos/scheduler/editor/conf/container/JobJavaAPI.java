package sos.scheduler.editor.conf.container;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.FocusAdapter;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.VerifyEvent;
import org.eclipse.swt.events.VerifyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

import sos.scheduler.editor.app.Messages;
import sos.scheduler.editor.classes.FormBaseClass;
import sos.scheduler.editor.conf.listeners.JobListener;

public class JobJavaAPI extends FormBaseClass {

	private boolean init = true;

	public JobJavaAPI(Composite pParentComposite, JobListener pobjDataProvider) {
		super(pParentComposite, pobjDataProvider);
		init = true;
		createGroup();
		init = false;
	}

	public void apply() {
		// if (isUnsaved())
		// addParam();
	}

	public boolean isUnsaved() {
		return false;
	}

	public void refreshContent () {
	}
	
	private void createGroup() {
		showWaitCursor();

		Group gScript_2 = new Group(objParent, SWT.NONE);
		GridLayout lgridLayout = new GridLayout();
		lgridLayout.numColumns = 13;
		gScript_2.setLayout(lgridLayout);
		setResizableV(gScript_2);

		Label lblClassNameLabel = new Label(gScript_2, SWT.NONE);
		GridData labelGridData = new GridData(SWT.BEGINNING, SWT.CENTER, false, false, 1, 1);
		lblClassNameLabel.setLayoutData(labelGridData);
		lblClassNameLabel.setText(Messages.getLabel("Classname"));

		final Text tbxClassName = new Text(gScript_2, SWT.BORDER);
		tbxClassName.setEnabled(true);
		tbxClassName.setText(objDataProvider.getJavaClass());

		tbxClassName.addFocusListener(new FocusAdapter() {
			public void focusGained(final FocusEvent e) {
				tbxClassName.selectAll();
			}
		});

		tbxClassName.addVerifyListener(new VerifyListener() {
			public void verifyText(final VerifyEvent e) {
				if (e.text.length() > 0 && objDataProvider.isJava() && objDataProvider.getSource().length() > 0) {
					MsgWarning("Please remove Script-Code first.");
					e.doit = false;
					return;
				}
			}
		});

		GridData gd_tClass = new GridData(GridData.FILL, GridData.CENTER, true, false);
		gd_tClass.horizontalSpan = 8;
		tbxClassName.setLayoutData(gd_tClass);
		tbxClassName.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				if (!init) {
					if (objDataProvider.isJava())
						objDataProvider.setJavaClass(tbxClassName.getText());
				}
			}
		});

		Group gScript_1 = gScript_2;
		new Label(gScript_1, SWT.NONE);
		new Label(gScript_1, SWT.NONE);
		new Label(gScript_1, SWT.NONE);
		new Label(gScript_1, SWT.NONE);

		Label lblNewLabel_1 = new Label(gScript_2, SWT.NONE);
		lblNewLabel_1.setLayoutData(labelGridData);
		lblNewLabel_1.setText(Messages.getLabel("job.classpath"));

		// GridData gd_tClasspath = new GridData(GridData.FILL, GridData.CENTER, true, false);
		// gd_tClasspath.horizontalSpan = 8;
		final Text tClasspath = new Text(gScript_2, SWT.BORDER);
		tClasspath.setLayoutData(gd_tClass);
		tClasspath.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent arg0) {
				objDataProvider.setClasspath(tClasspath.getText());
			}
		});

		tClasspath.setText(objDataProvider.getClasspath());
		tClasspath.setEnabled(true);

		new Label(gScript_2, SWT.NONE);
		new Label(gScript_2, SWT.NONE);
		new Label(gScript_2, SWT.NONE);
		new Label(gScript_2, SWT.NONE);

		final Label java_optionsLabel = new Label(gScript_2, SWT.NONE);
		java_optionsLabel.setLayoutData(labelGridData);
		java_optionsLabel.setText("Java Options:");

		final Text txtJavaOptions = new Text(gScript_2, SWT.BORDER);
		txtJavaOptions.setText(objDataProvider.getJavaOptions());

		txtJavaOptions.setToolTipText(Messages.getTooltip("job.java_options"));

		txtJavaOptions.addFocusListener(new FocusAdapter() {
			public void focusGained(final FocusEvent e) {
				txtJavaOptions.selectAll();
			}
		});
		txtJavaOptions.addModifyListener(new ModifyListener() {
			public void modifyText(final ModifyEvent e) {
				if (init) {
					return;
				}
				objDataProvider.setJavaOptions(txtJavaOptions.getText());
			}
		});
		txtJavaOptions.setLayoutData(gd_tClass);

		gScript_1.setVisible(true);
		gScript_1.redraw();
		RestoreCursor();
		init = false;
	}

}
