package sos.scheduler.editor.forms;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.Text;
import org.jdom.Element;

import sos.scheduler.editor.app.DomParser;
import sos.scheduler.editor.app.Editor;
import sos.scheduler.editor.app.IUnsaved;
import sos.scheduler.editor.app.IUpdateLanguage;
import sos.scheduler.editor.app.Messages;
import sos.scheduler.editor.listeners.ScriptListener;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.widgets.Display;

public class ScriptForm extends Composite implements IUnsaved, IUpdateLanguage {
	private Label label1_1;
	private Label label1_2;
	private ScriptListener listener;

	private String groupTitle = "Script";

	private int type;

	private Group gScript = null;

	private Label label1 = null;

	private Text tClass = null;

	private Label label3 = null;

	private Text tFilename = null;

	private SashForm sashForm = null;

	private Group gInclude = null;

	private List lInclude = null;

	private Button bRemove = null;

	private Label label4 = null;

	private Text tInclude = null;

	private Button bAdd = null;

	private Group gSource = null;

	private Text tSource = null;

	private Label label14 = null;

	private Composite cLanguage = null;

	private Button bJava = null;

	private Button bJavaScript = null;

	private Button bPerl = null;

	private Button bVBScript = null;

	private Button bNone = null;

	private Button bCom = null;

	private Label label = null;

	public ScriptForm(Composite parent, int style) {
		super(parent, style);
		initialize();
		setToolTipText();
		
		sashForm.setWeights(new int[] { 40, 60 });
	}

	public ScriptForm(Composite parent, int style, String title, DomParser dom,
			Element element, int type) {
		super(parent, style);
		groupTitle = title;
		initialize();
		setToolTipText();

		sashForm.setWeights(new int[] { 40, 60 });
		setAttributes(dom, element, type);
	}

	public void setAttributes(DomParser dom, Element element, int type) {
		this.type = type;
		listener = new ScriptListener(dom, element, type);
		fillForm();
	}

	public void apply() {
		if(isUnsaved())
			applyFile();
	}

	public boolean isUnsaved() {
		return bAdd.isEnabled();
	}

	private void initialize() {
		this.setLayout(new FillLayout());
		createGroup();
		setSize(new org.eclipse.swt.graphics.Point(604, 427));
	}

	/**
	 * This method initializes group
	 * 
	 */
	private void createGroup() {
		GridData gridData2 = new GridData(GridData.FILL, GridData.CENTER, false, false);
		gridData2.horizontalIndent = 7;
		gridData2.widthHint = 0;
		GridData gridData = new GridData();
		gridData.horizontalIndent = 7;
		gridData.horizontalAlignment = GridData.FILL;
		gridData.horizontalSpan = 1;
		gridData.verticalAlignment = GridData.CENTER;
		GridLayout gridLayout = new GridLayout();
		gridLayout.numColumns = 2;
		gScript = new Group(this, SWT.NONE);
		gScript.setText(groupTitle);
		gScript.setLayout(gridLayout);
		label14 = new Label(gScript, SWT.NONE);
		label14.setText("Language:");
		createComposite();
		label1 = new Label(gScript, SWT.NONE);
		label1.setText("Classname:");
		tClass = new Text(gScript, SWT.BORDER);
		tClass.setLayoutData(gridData);
		tClass.addModifyListener(new org.eclipse.swt.events.ModifyListener() {
			public void modifyText(org.eclipse.swt.events.ModifyEvent e) {
				if (bJava.getSelection())
					listener.setJavaClass(tClass.getText());
				else if (bCom.getSelection())
					listener.setComClass(tClass.getText());
			}
		});
		label3 = new Label(gScript, SWT.NONE);
		label3.setText("Filename:");
		tFilename = new Text(gScript, SWT.BORDER);
		tFilename.setLayoutData(gridData2);
		tFilename
				.addModifyListener(new org.eclipse.swt.events.ModifyListener() {
					public void modifyText(org.eclipse.swt.events.ModifyEvent e) {
						listener.setFilename(tFilename.getText());
					}
				});
		createSashForm();
	}

	/**
	 * This method initializes sashForm
	 * 
	 */
	private void createSashForm() {
		GridData gridData3 = new GridData();
		gridData3.horizontalAlignment = GridData.FILL;
		gridData3.grabExcessHorizontalSpace = true;
		gridData3.grabExcessVerticalSpace = true;
		gridData3.horizontalSpan = 2;
		gridData3.verticalAlignment = GridData.FILL;
		sashForm = new SashForm(gScript, SWT.HORIZONTAL);
		sashForm.setOrientation(org.eclipse.swt.SWT.VERTICAL);
		sashForm.setLayoutData(gridData3);
		createGroup1();
		createGroup2();
	}

	/**
	 * This method initializes group1
	 * 
	 */
	private void createGroup1() {
	}

	/**
	 * This method initializes group2
	 * 
	 */
	private void createGroup2() {
		GridData gridData1 = new org.eclipse.swt.layout.GridData();
		gridData1.horizontalSpan = 3;
		gridData1.verticalAlignment = org.eclipse.swt.layout.GridData.CENTER;
		gridData1.horizontalAlignment = org.eclipse.swt.layout.GridData.FILL;
		GridData gridData7 = new GridData();
		gridData7.horizontalAlignment = GridData.FILL;
		gridData7.verticalAlignment = GridData.CENTER;
		GridData gridData6 = new GridData(GridData.FILL, GridData.CENTER, false, false);
		gridData6.widthHint = 50;
		GridData gridData5 = new GridData();
		gridData5.horizontalAlignment = GridData.FILL;
		gridData5.verticalAlignment = GridData.BEGINNING;
		GridData gridData4 = new GridData(GridData.FILL, GridData.FILL, true, true);
		GridLayout gridLayout1 = new GridLayout();
		gridLayout1.numColumns = 3;
		gInclude = new Group(sashForm, SWT.NONE);
		gInclude.setText("Include Files");
		gInclude.setLayout(gridLayout1);
		label4 = new Label(gInclude, SWT.NONE);
		label4.setText("File:");
		tInclude = new Text(gInclude, SWT.BORDER);
		tInclude.setLayoutData(gridData6);
		bAdd = new Button(gInclude, SWT.NONE);
		label = new Label(gInclude, SWT.SEPARATOR | SWT.HORIZONTAL);
		label.setText("Label");
		label.setLayoutData(gridData1);

		label1_2 = new Label(gInclude, SWT.NONE);
		label1_2.setLayoutData(new GridData());
		label1_2.setVisible(false);
		label1_2.setText("Classname:");
		lInclude = new List(gInclude, SWT.BORDER | SWT.H_SCROLL);
		lInclude.setLayoutData(gridData4);
		lInclude
				.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
					public void widgetSelected(
							org.eclipse.swt.events.SelectionEvent e) {
						bRemove.setEnabled(lInclude.getSelectionCount() > 0);
					}
				});
		bRemove = new Button(gInclude, SWT.NONE);
		bRemove.setText("Remove File");
		bRemove.setEnabled(false);
		bRemove.setLayoutData(gridData5);
		bRemove
				.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
					public void widgetSelected(
							org.eclipse.swt.events.SelectionEvent e) {
						if (lInclude.getSelectionCount() > 0) {
							int index = lInclude.getSelectionIndex();
							listener.removeInclude(index);
							lInclude.setItems(listener.getIncludes());
							if (index >= lInclude.getItemCount())
								index--;
							if (lInclude.getItemCount() > 0)
								lInclude.setSelection(index);
						}
					}
				});
		tInclude.addModifyListener(new org.eclipse.swt.events.ModifyListener() {
			public void modifyText(org.eclipse.swt.events.ModifyEvent e) {
				bAdd.setEnabled(!tInclude.getText().equals(""));
			}
		});
		tInclude.addKeyListener(new org.eclipse.swt.events.KeyAdapter() {
			public void keyPressed(org.eclipse.swt.events.KeyEvent e) {
				if (e.keyCode == SWT.CR && !tInclude.getText().equals("")) {
					listener.addInclude(tInclude.getText());
					lInclude.setItems(listener.getIncludes());
					tInclude.setText("");
				}
			}
		});
		bAdd.setText("&Add File");
		bAdd.setLayoutData(gridData7);
		bAdd.setEnabled(false);
		bAdd
				.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
					public void widgetSelected(
							org.eclipse.swt.events.SelectionEvent e) {
						applyFile();
					}
				});
		gSource = new Group(sashForm, SWT.NONE);
		final GridLayout gridLayout = new GridLayout();
		gridLayout.numColumns = 2;
		gSource.setLayout(gridLayout);
		gSource.setText("Source Code");

		label1_1 = new Label(gSource, SWT.NONE);
		label1_1.setLayoutData(new GridData());
		label1_1.setVisible(false);
		label1_1.setText("Classname:");
		tSource = new Text(gSource, SWT.MULTI | SWT.V_SCROLL | SWT.BORDER
				| SWT.H_SCROLL);
		tSource.setLayoutData(new GridData(GridData.FILL, GridData.FILL, true, true));
		tSource.setFont(new Font(Display.getDefault(), "Courier New", 8,
				SWT.NORMAL));
		tSource.addModifyListener(new org.eclipse.swt.events.ModifyListener() {
			public void modifyText(org.eclipse.swt.events.ModifyEvent e) {
				listener.setSource(tSource.getText());
			}
		});
	}

	/**
	 * This method initializes composite
	 * 
	 */
	private void createComposite() {
		cLanguage = new Composite(gScript, SWT.NONE);
		cLanguage.setLayout(new RowLayout());
		bJava = new Button(cLanguage, SWT.RADIO);
		bJava.setText("Java");
		bCom = new Button(cLanguage, SWT.RADIO);
		bCom.setText("Com");
		bCom
				.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
					public void widgetSelected(
							org.eclipse.swt.events.SelectionEvent e) {
						if (bCom.getSelection()) {
							listener.setLanguage(ScriptListener.COM);
							fillForm();
						}
					}
				});
		bJava
				.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
					public void widgetSelected(
							org.eclipse.swt.events.SelectionEvent e) {
						if (bJava.getSelection()) {
							listener.setLanguage(ScriptListener.JAVA);
							fillForm();
						}
					}
				});
		bJavaScript = new Button(cLanguage, SWT.RADIO);
		bJavaScript.setText("Javascript");
		bJavaScript
				.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
					public void widgetSelected(
							org.eclipse.swt.events.SelectionEvent e) {
						if (bJavaScript.getSelection()) {
							listener.setLanguage(ScriptListener.JAVA_SCRIPT);
							fillForm();
						}
					}
				});
		bPerl = new Button(cLanguage, SWT.RADIO);
		bPerl.setText("PerlScript");
		bPerl
				.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
					public void widgetSelected(
							org.eclipse.swt.events.SelectionEvent e) {
						if (bPerl.getSelection()) {
							listener.setLanguage(ScriptListener.PERL);
							fillForm();
						}
					}
				});
		bVBScript = new Button(cLanguage, SWT.RADIO);
		bVBScript.setText("VBScript");
		bVBScript
				.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
					public void widgetSelected(
							org.eclipse.swt.events.SelectionEvent e) {
						if (bVBScript.getSelection()) {
							listener.setLanguage(ScriptListener.VB_SCRIPT);
							fillForm();
						}
					}
				});
		bNone = new Button(cLanguage, SWT.RADIO);
		bNone.setText("None");
		bNone
				.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
					public void widgetSelected(
							org.eclipse.swt.events.SelectionEvent e) {
						if (bNone.getSelection()) {
							listener.setLanguage(ScriptListener.NONE);
							fillForm();
						}
					}
				});
	}

	private void fillForm() {
		int language = listener.getLanguage();

		if (type == Editor.EXECUTE) {
			bNone.setVisible(false);
			//if (language == ScriptListener.NONE) {
			//	language = ScriptListener.JAVA;
			//	listener.setLanguage(language);
			//}
		}

		boolean enabled = language != ScriptListener.NONE;
		setEnabled(enabled);

		switch (language) {
		case ScriptListener.NONE:
			bNone.setSelection(true);
			break;
		case ScriptListener.COM:
			bCom.setSelection(true);
			tClass.setEnabled(true);
			tClass.setFocus();
			if (!tClass.getText().equals("")
					&& listener.getComClass().equals(""))
				listener.setComClass(tClass.getText());
			tClass.setText(listener.getComClass());
			tFilename.setEnabled(true);
			if (!tFilename.getText().equals("")
					&& listener.getFilename().equals(""))
				listener.setFilename(tFilename.getText());
			tFilename.setText(listener.getFilename());
			tSource.setEnabled(false);
			break;
		case ScriptListener.JAVA:
			bJava.setSelection(true);
			tClass.setEnabled(true);
			tClass.setFocus();
			if (!tClass.getText().equals("")
					&& listener.getJavaClass().equals(""))
				listener.setJavaClass(tClass.getText());
			tClass.setText(listener.getJavaClass());
			if(!tSource.getText().equals("") && listener.getSource().equals(""))
				listener.setSource(tSource.getText());
			break;
		case ScriptListener.JAVA_SCRIPT:
			bJavaScript.setSelection(true);
			tSource.setFocus();
			break;
		case ScriptListener.PERL:
			bPerl.setSelection(true);
			tSource.setFocus();
			break;
		case ScriptListener.VB_SCRIPT:
			bVBScript.setSelection(true);
			tSource.setFocus();
			break;
		}

		if (language != ScriptListener.NONE && language != ScriptListener.COM) {
			tSource.setText(listener.getSource());
		}
		if (language != ScriptListener.NONE) {
			lInclude.setItems(listener.getIncludes());
		}
	}

	public void setEnabled(boolean enabled) {
		tClass.setEnabled(false);
		tFilename.setEnabled(false);
		lInclude.setEnabled(enabled);
		bRemove.setEnabled(false);
		tInclude.setEnabled(enabled);
		lInclude.deselectAll();
		bAdd.setEnabled(false);
		tSource.setEnabled(enabled);
	}

	public void setFullEnabled(boolean enabled) {
		setEnabled(enabled);
		bJava.setEnabled(enabled);
		bJavaScript.setEnabled(enabled);
		bNone.setEnabled(enabled);
		bPerl.setEnabled(enabled);
		bVBScript.setEnabled(enabled);
		bCom.setEnabled(enabled);
		fillForm();
	}

	public void setLanguage(int language) {
		listener.setLanguage(language);
		fillForm();
	}
	
	private void applyFile() {
		listener.addInclude(tInclude.getText());
		lInclude.setItems(listener.getIncludes());
		tInclude.setText("");
		tInclude.setFocus();
	}
	public void setToolTipText(){
		tClass.setToolTipText(Messages.getTooltip("script.class"));
		tFilename.setToolTipText(Messages.getTooltip("script.filename"));
		tInclude.setToolTipText(Messages
				.getTooltip("script.include.file_entry"));
		lInclude
				.setToolTipText(Messages.getTooltip("script.include.file_list"));
		bRemove
				.setToolTipText(Messages
						.getTooltip("script.include.btn_remove"));
		bAdd.setToolTipText(Messages.getTooltip("script.include.btn_add"));
		tSource.setToolTipText(Messages.getTooltip("script.source_entry"));
		bJava.setToolTipText(Messages.getTooltip("script.language.java"));
		bCom.setToolTipText(Messages.getTooltip("script.language.com"));
		bJavaScript.setToolTipText(Messages
				.getTooltip("script.language.javascript"));
		bPerl.setToolTipText(Messages.getTooltip("script.language.perl"));
		bVBScript.setToolTipText(Messages
				.getTooltip("script.language.vb_script"));
		bNone.setToolTipText(Messages.getTooltip("script.language.none"));
	 
	 
  }	

} // @jve:decl-index=0:visual-constraint="10,10"
