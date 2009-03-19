package sos.scheduler.editor.conf.forms;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Spinner;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.Text;
import org.jdom.Element;
import sos.scheduler.editor.app.Editor;
import sos.scheduler.editor.app.IUnsaved;
import sos.scheduler.editor.app.IUpdateLanguage;
import sos.scheduler.editor.app.Messages;
import sos.scheduler.editor.app.ResourceManager;
import sos.scheduler.editor.app.Utils;
import sos.scheduler.editor.conf.ISchedulerUpdate;
import sos.scheduler.editor.conf.SchedulerDom;
import sos.scheduler.editor.conf.listeners.ScriptListener;
import sos.scheduler.editor.app.Options;

public class ScriptForm extends Composite implements IUnsaved, IUpdateLanguage {
	
   
    //private Label          label1_2    = null;

    private Button butFavorite;
    private ScriptListener listener    = null;

    private String         groupTitle  = "Script";

    private int            type        = -1;

    private Group          gScript     = null;

    private Label          label1      = null;

    private Text           tClass      = null;

    private Label          label3      = null;

    private Text           tFilename   = null;

    //private SashForm       sashForm    = null;
    private Composite       sashForm    = null;

    private Group          gInclude    = null;

    //private List           lInclude    = null;

    private Button         bRemove     = null;

    private Text           tInclude    = null;

    private Button         bAdd        = null;

    private Group          gSource     = null;

    private Text           tSource     = null;

    private Label          label14     = null;

    private Composite      cLanguage   = null;

    private Button         bJava       = null;

    private Button         bJavaScript = null;

    private Button         bPerl       = null;

    private Button         bVBScript   = null;

    private Button         bNone       = null;

    private Button         bCom        = null;

    private Label          label       = null;
    
    private Button         bShell      = null;
    
    private Text           txtName     = null;
    
    private Spinner        spinner     = null;
    
    private ISchedulerUpdate update    = null;
    
    private Table          tableIncludes = null; 
    
    private Button         butIsLifeFile = null;
    
    //private Label       label1_1      = null;

    private boolean       init          = false;
    
    private Combo         cboFavorite   = null;
    
    

    public ScriptForm(Composite parent, int style, ISchedulerUpdate update_) {
        super(parent, style);
        init = true;        
        update = update_;
        initialize();
        setToolTipText();
        init = false;
        //sashForm.setWeights(new int[] { 30, 70 });
    }


    public ScriptForm(Composite parent, int style, String title, SchedulerDom dom, Element element, int type, ISchedulerUpdate update_) {
        super(parent, style);
        init = true;       
        this.type = type;
        update = update_;
        groupTitle = title;
        initialize();
        setToolTipText();
        //sashForm.setWeights(new int[] { 30, 70 });
        setAttributes(dom, element, type);
        
        gScript.setEnabled(Utils.isElementEnabled("job", dom, element));        	
        init = false;
        
    }


    public void setAttributes(SchedulerDom dom, Element element, int type_) {
    	
        listener = new ScriptListener(dom, element, type_, update);
        this.type = type_ ;
        fillForm();
    }


    public void apply() {
        if (isUnsaved())
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
     */
    private void createGroup() {
        GridData gridData2 = new GridData(GridData.FILL, GridData.CENTER, true, false, 2, 1);
        gridData2.widthHint = 0;
        GridData gridData = new GridData(GridData.FILL, GridData.CENTER, true, false);
        GridLayout gridLayout = new GridLayout();
        gridLayout.numColumns = 4;
        gScript = new Group(this, SWT.NONE);
        gScript.setText(groupTitle);
        gScript.setLayout(gridLayout);

        if(type == Editor.MONITOR) {

        final Label nameLabel = new Label(gScript, SWT.NONE);
        nameLabel.setLayoutData(new GridData());
        nameLabel.setText("Name: ");
        new Label(gScript, SWT.NONE);
        	final Composite scriptcom = new Composite(gScript, SWT.NONE);
        	scriptcom.setLayoutData(new GridData(GridData.FILL, GridData.CENTER, true, false, 2, 1));
        	final GridLayout gridLayout_1 = new GridLayout();
        	gridLayout_1.verticalSpacing = 0;
        	gridLayout_1.marginWidth = 0;
        	gridLayout_1.marginHeight = 0;
        	gridLayout_1.horizontalSpacing = 0;
        	gridLayout_1.numColumns = 3;
        	scriptcom.setLayout(gridLayout_1);

        	txtName = new Text(scriptcom, SWT.BORDER);
        	txtName.setLayoutData(new GridData(GridData.FILL, GridData.CENTER, true, false));
        	txtName.addModifyListener(new ModifyListener() {
        		public void modifyText(final ModifyEvent e) {
        			if(!init)
        			listener.setName(txtName.getText());
        		}
        	});

        	final Label orderingLabel = new Label(scriptcom, SWT.NONE);
        	orderingLabel.setLayoutData(new GridData());
        	orderingLabel.setText("  Ordering: ");

        	spinner = new Spinner(scriptcom, SWT.BORDER);
        	spinner.addSelectionListener(new SelectionAdapter() {
        		public void widgetSelected(final SelectionEvent e) {
        			if(!init)
        			listener.setOrdering(String.valueOf(spinner.getSelection()));
        		}
        	});
        	spinner.setSelection(-1);
        	spinner.setMaximum(999);
        }
        label14 = new Label(gScript, SWT.NONE);
        label14.setLayoutData(new GridData());
        label14.setText("Language:");
        createComposite();
        label1 = new Label(gScript, SWT.NONE);
        label1.setLayoutData(new GridData());
        label1.setText("Classname:");
        new Label(gScript, SWT.NONE);
        tClass = new Text(gScript, SWT.BORDER);
        tClass.setLayoutData(gridData);
        tClass.addModifyListener(new org.eclipse.swt.events.ModifyListener() {
            public void modifyText(org.eclipse.swt.events.ModifyEvent e) {
            	if(!init) {
            		if (bJava.getSelection())
            			listener.setJavaClass(tClass.getText());
            		else if (bCom.getSelection())
            			listener.setComClass(tClass.getText());
            	}
            }
        });

        butFavorite = new Button(gScript, SWT.NONE);
        butFavorite.addSelectionListener(new SelectionAdapter() {
        	public void widgetSelected(final SelectionEvent e) {
        		Options.setProperty(getPrefix() + txtName.getText(), tClass.getText());
        		Options.saveProperties();
        		if(listener.getLanguage() != 0)
        			cboFavorite.setItems(Options.getPropertiesWithPrefix(getPrefix()));
        	}
        });
        butFavorite.setEnabled(false);
        butFavorite.setVisible(type == Editor.MONITOR);
        butFavorite.setText("Favoriten");
        label3 = new Label(gScript, SWT.NONE);
        label3.setLayoutData(new GridData());
        label3.setText("Filename:");
        new Label(gScript, SWT.NONE);
        tFilename = new Text(gScript, SWT.BORDER);
        tFilename.setLayoutData(gridData2);
        tFilename.addModifyListener(new org.eclipse.swt.events.ModifyListener() {
            public void modifyText(org.eclipse.swt.events.ModifyEvent e) {
            	if(!init)
            		listener.setFilename(tFilename.getText());                
            }
        });
        new GridData(GridData.FILL, GridData.FILL, false, true, 3, 1);

        sashForm = new Composite(gScript, SWT.NONE);
        //sashForm.setText("Include Files");
        sashForm.setLayoutData(new GridData(GridData.FILL, GridData.FILL, true, true, 4, 1));
        final GridLayout gridLayout_1 = new GridLayout();
        gridLayout_1.marginWidth = 0;
        gridLayout_1.marginHeight = 0;
        gridLayout_1.horizontalSpacing = 0;
        sashForm.setLayout(gridLayout_1);
        //sashForm = new SashForm(gScript, SWT.VERTICAL);
        //sashForm.setOrientation(org.eclipse.swt.SWT.VERTICAL);
        //sashForm.setLayoutData(gridData3);
        createSashForm();
    }


    /**
     * This method initializes sashForm
     */
    private void createSashForm() {
        createGroup1();
        createGroup2();
    }


    /**
     * This method initializes group1
     */
    private void createGroup1() {
    }


    /**
     * This method initializes group2
     */
    private void createGroup2() {
        GridData gridData1 = new org.eclipse.swt.layout.GridData(GridData.FILL, GridData.FILL, false, false, 3, 1);
        GridData gridData7 = new GridData(GridData.FILL, GridData.CENTER, false, false);
        GridData gridData6 = new GridData(GridData.FILL, GridData.CENTER, true, false);
        GridLayout gridLayout1 = new GridLayout();
        gridLayout1.numColumns = 3;
        gInclude = new Group(sashForm, SWT.NONE);
        gInclude.setLayoutData(new GridData(GridData.FILL, GridData.BEGINNING, true, false));
        gInclude.setText("Include Files");
        gInclude.setLayout(gridLayout1);

        butIsLifeFile = new Button(gInclude, SWT.CHECK);
        butIsLifeFile.setLayoutData(new GridData());
        butIsLifeFile.setText("from Hot Folder");
        tInclude = new Text(gInclude, SWT.BORDER);
        tInclude.setLayoutData(gridData6);
        bAdd = new Button(gInclude, SWT.NONE);
        label = new Label(gInclude, SWT.SEPARATOR | SWT.HORIZONTAL);
        label.setText("Label");
        label.setLayoutData(gridData1);

        /*label1_2 = new Label(gInclude, SWT.NONE);
        label1_2.setLayoutData(new GridData(GridData.BEGINNING, GridData.BEGINNING, false, false));
        label1_2.setVisible(false);
        label1_2.setText("Classname:");*/
        
        tableIncludes = new Table(gInclude, SWT.FULL_SELECTION | SWT.BORDER);
        tableIncludes.addSelectionListener(new SelectionAdapter() {
        	public void widgetSelected(final SelectionEvent e) {
        		if(tableIncludes.getSelectionCount() > 0) {
        			
        			tInclude.setText(tableIncludes.getSelection()[0].getText(0));
        			butIsLifeFile.setSelection(tableIncludes.getSelection()[0].getText(1) != null && tableIncludes.getSelection()[0].getText(1).equals("live_file"));
        			bRemove.setEnabled(tableIncludes.getSelectionCount() > 0);
        			bAdd.setEnabled(false);
        		}
        		
        	}
        });
        tableIncludes.setLinesVisible(true);
        tableIncludes.setHeaderVisible(true);
        final GridData gridData_2 = new GridData(GridData.FILL, GridData.BEGINNING, true, true, 2, 2);
        gridData_2.minimumHeight = 60;
        tableIncludes.setLayoutData(gridData_2);

        final TableColumn newColumnTableColumn = new TableColumn(tableIncludes, SWT.NONE);
        newColumnTableColumn.setWidth(272);
        newColumnTableColumn.setText("Name");

        final TableColumn newColumnTableColumn_1 = new TableColumn(tableIncludes, SWT.NONE);
        newColumnTableColumn_1.setWidth(81);
        newColumnTableColumn_1.setText("File/Life File");

        final Button butNew = new Button(gInclude, SWT.NONE);
        butNew.addSelectionListener(new SelectionAdapter() {
        	public void widgetSelected(final SelectionEvent e) {
        		tableIncludes.deselectAll();
        		tInclude.setText("");
        		butIsLifeFile.setSelection(false);
        	}
        });
        butNew.setLayoutData(new GridData(GridData.FILL, GridData.CENTER, false, false));
        butNew.setText("New");
        /*lInclude = new List(gInclude, SWT.BORDER | SWT.H_SCROLL);
        lInclude.setLayoutData(gridData4);
        lInclude.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
            public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
                bRemove.setEnabled(lInclude.getSelectionCount() > 0);
                
            }
        });
        */
        tInclude.addModifyListener(new org.eclipse.swt.events.ModifyListener() {
            public void modifyText(org.eclipse.swt.events.ModifyEvent e) {
                bAdd.setEnabled(!tInclude.getText().equals(""));
            }
        });
        tInclude.addKeyListener(new org.eclipse.swt.events.KeyAdapter() {
            public void keyPressed(org.eclipse.swt.events.KeyEvent e) {
                if (e.keyCode == SWT.CR && !tInclude.getText().equals("")) {
                    /*
                    listener.addInclude(tInclude.getText());
                    lInclude.setItems(listener.getIncludes());
                    */
                	listener.addInclude(tableIncludes, tInclude.getText(), butIsLifeFile.getSelection());
                    listener.fillTable(tableIncludes);
                    tInclude.setText("");
                }
            }
        });
        bAdd.setText("&Add File");
        bAdd.setLayoutData(gridData7);
        bAdd.setEnabled(false);
        bAdd.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
            public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
                applyFile();
            }
        });
        GridData gridData5 = new GridData(GridData.FILL, GridData.BEGINNING, false, false);
        bRemove = new Button(gInclude, SWT.NONE);
        bRemove.setText("Remove File");
        bRemove.setEnabled(false);
        bRemove.setLayoutData(gridData5);
        bRemove.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
            public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
            	if (tableIncludes.getSelectionCount() > 0) {
                    int index = tableIncludes.getSelectionIndex();
                    listener.removeInclude(index);
                    //lInclude.setItems(listener.getIncludes());
                    listener.fillTable(tableIncludes);
                    if (index >= tableIncludes.getItemCount())
                        index--;
                    if (tableIncludes.getItemCount() > 0)
                    	tableIncludes.setSelection(index);
                }
                /*if (lInclude.getSelectionCount() > 0) {
                    int index = lInclude.getSelectionIndex();
                    listener.removeInclude(index);
                    lInclude.setItems(listener.getIncludes());
                    if (index >= lInclude.getItemCount())
                        index--;
                    if (lInclude.getItemCount() > 0)
                        lInclude.setSelection(index);
                }
                */
            }
        });
        gSource = new Group(sashForm, SWT.NONE);
        gSource.setLayoutData(new GridData(GridData.FILL, GridData.FILL, true, true));
        final GridLayout gridLayout = new GridLayout();
        gridLayout.numColumns = 2;
        gSource.setLayout(gridLayout);
        gSource.setText("Source Code");
        
        tSource = new Text(gSource, SWT.MULTI | SWT.V_SCROLL | SWT.BORDER | SWT.H_SCROLL);
        tSource.addKeyListener(new KeyAdapter() {
        	public void keyPressed(final KeyEvent e) {
        		if(e.keyCode==97 && e.stateMask == SWT.CTRL){
        			tSource.setSelection(0, tSource.getText().length());
				}
        	}
        });
        final GridData gridData_1 = new GridData(GridData.FILL, GridData.FILL, true, true);
        gridData_1.widthHint = 454;
        gridData_1.heightHint = 55;
        tSource.setLayoutData(gridData_1);
        tSource.setFont(ResourceManager.getFont("Courier New", 8, SWT.NONE));
        tSource.addModifyListener(new org.eclipse.swt.events.ModifyListener() {
            public void modifyText(org.eclipse.swt.events.ModifyEvent e) {
            	/*if (tSource.getText().trim().equals("")) {
            	  listener.deleteScript();
            	}else {
            	  listener.setSource(tSource.getText());
            	}*/
            	if(!init)
            		listener.setSource(tSource.getText());
            }
        });

        final Button button = new Button(gSource, SWT.NONE);        
        final GridData gridData = new GridData(GridData.BEGINNING, GridData.BEGINNING, false, true);
        gridData.widthHint = 30;
        button.setLayoutData(gridData);
		button.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(final SelectionEvent e) {
				
				String text = sos.scheduler.editor.app.Utils.showClipboard(tSource.getText(), getShell(), true, "");
				if(text != null)
					tSource.setText(text);
				
			}
		});
		button.setImage(ResourceManager.getImageFromResource("/sos/scheduler/editor/icon_edit.gif"));

        /*label1_1 = new Label(gSource, SWT.NONE);
        label1_1.setLayoutData(new GridData(GridData.BEGINNING, GridData.BEGINNING, false, true));
        label1_1.setVisible(false);
        label1_1.setText("Classname:");
        */
    }


    /**
     * This method initializes composite
     */
    private void createComposite() {
        new Label(gScript, SWT.NONE);
    	
    		
    	
        cLanguage = new Composite(gScript, SWT.NONE);
        final GridLayout gridLayout = new GridLayout();
        gridLayout.numColumns = 8;
        cLanguage.setLayout(gridLayout);
        cLanguage.setLayoutData(new GridData(GridData.FILL, GridData.CENTER, true, false, 2, 1));
        bJava = new Button(cLanguage, SWT.RADIO);
        bJava.setText("Java");
        bJava.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
            public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
                if (bJava.getSelection()) {
                    listener.setLanguage(ScriptListener.JAVA);
                    fillForm();
                }
            }
        });
        bCom = new Button(cLanguage, SWT.RADIO);
        bCom.setText("Com");
        bCom.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
            public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
                if (bCom.getSelection()) {
                    listener.setLanguage(ScriptListener.COM);
                    fillForm();
                }
            }
        });
        bJavaScript = new Button(cLanguage, SWT.RADIO);
        bJavaScript.setText("Javascript");
        bJavaScript.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
            public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
                if (bJavaScript.getSelection()) {
                    listener.setLanguage(ScriptListener.JAVA_SCRIPT);
                    fillForm();
                }
            }
        });
        bPerl = new Button(cLanguage, SWT.RADIO);
        bPerl.setText("PerlScript");
        bPerl.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
            public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
                if (bPerl.getSelection()) {
                    listener.setLanguage(ScriptListener.PERL);
                    fillForm();
                }
            }
        });
        bVBScript = new Button(cLanguage, SWT.RADIO);
        bVBScript.setText("VBScript");
        bVBScript.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
            public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
                if (bVBScript.getSelection()) {
                    listener.setLanguage(ScriptListener.VB_SCRIPT);
                    fillForm();
                }
            }
        });

        bShell = new Button(cLanguage, SWT.RADIO);
        bShell.addSelectionListener(new SelectionAdapter() {
        	public void widgetDefaultSelected(final SelectionEvent e) {
        		
        	}
        	public void widgetSelected(final SelectionEvent e) {
        		if (bShell.getSelection()) {
              listener.setLanguage(ScriptListener.SHELL);
              fillForm();
          }
        	}
        });
        bShell.setText("Shell");
        bNone = new Button(cLanguage, SWT.RADIO);
        bNone.setText("None");
        bNone.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
            public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
                if (bNone.getSelection()) {
                    listener.setLanguage(ScriptListener.NONE);
                    fillForm();
                }
            }
        });

        
        cboFavorite = new Combo(cLanguage, SWT.NONE);
        cboFavorite.setVisible(type == Editor.MONITOR);
        
        cboFavorite.setLayoutData(new GridData(GridData.FILL, GridData.CENTER, true, false));
        cboFavorite.addSelectionListener(new SelectionAdapter() {
        	public void widgetSelected(final SelectionEvent e) {
        		if(cboFavorite.getText().length() > 0) {
        			if(Options.getProperty(getPrefix() + cboFavorite.getText()) != null) {
        				tClass.setText(Options.getProperty(getPrefix() + cboFavorite.getText())) ;
        				txtName.setText(cboFavorite.getText());
        			}
        		}
        	}
        });
    }


    private void fillForm() {
    	init = true;
        int language = listener.getLanguage();
        if(type == Editor.MONITOR) {
        	txtName.setText(listener.getName());
        	spinner.setSelection((listener.getOrdering().length() == 0 ? 0 : Integer.parseInt(listener.getOrdering())));
        }
        
        if (type == Editor.EXECUTE) {
            bNone.setVisible(false);
            //hier
            
        }

        if (type == Editor.MONITOR) {
        	bNone.setVisible(false);
        	//hier
        	bShell.setVisible(false);
        }
        
        boolean enabled = language != ScriptListener.NONE;
        setEnabled(enabled);
        
        
        cboFavorite.setEnabled(true);
        butFavorite.setEnabled(true);
        if(Options.getPropertiesWithPrefix(getPrefix())!= null && language != 0)
        	cboFavorite.setItems(Options.getPropertiesWithPrefix(getPrefix()));

        switch (language) {
            case ScriptListener.NONE:
                bNone.setSelection(true);
                break;
            case ScriptListener.COM:
                bCom.setSelection(true);
                tClass.setEnabled(true);
                tClass.setFocus();
                if (!tClass.getText().equals("") && listener.getComClass().equals(""))
                    listener.setComClass(tClass.getText());
                tClass.setText(listener.getComClass());
                tFilename.setEnabled(true);
                if (!tFilename.getText().equals("") && listener.getFilename().equals(""))
                    listener.setFilename(tFilename.getText());
                tFilename.setText(listener.getFilename());
                tSource.setEnabled(false);
                break;
            case ScriptListener.JAVA:
            	//cboFavorite.setEnabled(true);
            	//butFavorite.setEnabled(true);            	
                bJava.setSelection(true);
                tClass.setEnabled(true);
                tClass.setFocus();
                if (!tClass.getText().equals("") && listener.getJavaClass().equals(""))
                    listener.setJavaClass(tClass.getText());
                tClass.setText(listener.getJavaClass());
                if (!tSource.getText().equals("") && listener.getSource().equals(""))
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
            case ScriptListener.SHELL:
                bShell.setSelection(true);
                tSource.setFocus();
                break;
        }

        if (language != ScriptListener.NONE && language != ScriptListener.COM) {
            tSource.setText(listener.getSource());
        }
        if (language != ScriptListener.NONE) {
            //lInclude.setItems(listener.getIncludes());
            listener.fillTable(tableIncludes);
        }
        init = false;
    }


    public void setEnabled(boolean enabled) {
        tClass.setEnabled(false);
        tFilename.setEnabled(false);
        //lInclude.setEnabled(enabled);
        tableIncludes.setEnabled(enabled);
        bRemove.setEnabled(false);
        tInclude.setEnabled(enabled);
        butIsLifeFile.setEnabled(enabled);
        //lInclude.deselectAll();
        tableIncludes.deselectAll();
        
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
        bShell.setEnabled(enabled);
        bCom.setEnabled(enabled);
        fillForm();
    }


    public void setLanguage(int language) {
        listener.setLanguage(language);
        fillForm();
    }


    private void applyFile() {
    	listener.addInclude(tableIncludes, tInclude.getText(), butIsLifeFile.getSelection());
        //listener.addInclude(tInclude.getText());
        //lInclude.setItems(listener.getIncludes());
        tInclude.setText("");
        tInclude.setFocus();
    }


    public void setToolTipText() {
        tClass.setToolTipText(Messages.getTooltip("script.class"));
        tFilename.setToolTipText(Messages.getTooltip("script.filename"));
        tInclude.setToolTipText(Messages.getTooltip("script.include.file_entry"));
        //lInclude.setToolTipText(Messages.getTooltip("script.include.file_list"));
        tableIncludes.setToolTipText(Messages.getTooltip("script.include.file_list"));
        bRemove.setToolTipText(Messages.getTooltip("script.include.btn_remove"));
        bAdd.setToolTipText(Messages.getTooltip("script.include.btn_add"));
        tSource.setToolTipText(Messages.getTooltip("script.source_entry"));
        bJava.setToolTipText(Messages.getTooltip("script.language.java"));
        bCom.setToolTipText(Messages.getTooltip("script.language.com"));
        bJavaScript.setToolTipText(Messages.getTooltip("script.language.javascript"));
        bPerl.setToolTipText(Messages.getTooltip("script.language.perl"));
        bVBScript.setToolTipText(Messages.getTooltip("script.language.vb_script"));
        bShell.setToolTipText(Messages.getTooltip("script.language.shell"));
        bNone.setToolTipText(Messages.getTooltip("script.language.none"));

        if(txtName != null) txtName.setToolTipText(Messages.getTooltip("script.name"));
        if(spinner != null) spinner.setToolTipText(Messages.getTooltip("script.ordering"));
        
        if(butIsLifeFile!= null) butIsLifeFile.setToolTipText(Messages.getTooltip("is_live_file"));
    }

    private String getPrefix() {
    	if(listener.getLanguage() == 0)
    		return "";
    	return "monitor_favorite_" + ( bCom.getSelection() ? "com" : listener.getLanguage(listener.getLanguage())) +"_" ;
    }
} // @jve:decl-index=0:visual-constraint="10,10"
