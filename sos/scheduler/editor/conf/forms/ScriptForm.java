package sos.scheduler.editor.conf.forms;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.FocusAdapter;
import org.eclipse.swt.events.FocusEvent;
import java.util.HashMap;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.TraverseEvent;
import org.eclipse.swt.events.TraverseListener;
import org.eclipse.swt.events.VerifyEvent;
import org.eclipse.swt.events.VerifyListener;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
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

import sos.scheduler.editor.app.ContextMenu;
import sos.scheduler.editor.app.Editor;
import sos.scheduler.editor.app.ErrorLog;
import sos.scheduler.editor.app.IUnsaved;
import sos.scheduler.editor.app.IUpdateLanguage;
import sos.scheduler.editor.app.MainWindow;
import sos.scheduler.editor.app.Messages;
import sos.scheduler.editor.app.ResourceManager;
import sos.scheduler.editor.app.Utils;
import sos.scheduler.editor.conf.ISchedulerUpdate;
import sos.scheduler.editor.conf.SchedulerDom;
import sos.scheduler.editor.conf.listeners.ScriptListener;
import sos.scheduler.editor.app.Options;
import sos.util.SOSString;


public class ScriptForm extends Composite implements IUnsaved, IUpdateLanguage {
	

    private Button           butFavorite         = null;
    
    private ScriptListener   listener            = null;

    private String           groupTitle          = "Script";

    private int              type                = -1;

    private Group            gScript             = null;

    private Label            label1              = null;

    private Text             tClass              = null;

    private Label            label3              = null;

    private Text             tFilename           = null;

    private Group            gInclude            = null;

    private Button           bRemove             = null;

    private Text             tInclude            = null;

    private Button           bAdd                = null;

    private Group            gSource             = null;

    private Text             tSource             = null;

    private Label            label14             = null;

    private Composite        cLanguage           = null;

    private Button           bJava               = null;

    private Button           bJavaScript         = null;

    private Button           bPerl               = null;

    private Button           bVBScript           = null;

    private Button           bNone               = null;
    
    private Button           bCom                = null;

    private Label            label               = null;
    
    private Button           bShell              = null;
    
    private Text             txtName             = null;
    
    private Spinner          spinner             = null;
    
    private ISchedulerUpdate update              = null;
    
    private Table            tableIncludes       = null; 
    
    private Button           butIsLifeFile       = null;
  
    private boolean          init                = false;
    
    private Combo            cboFavorite         = null;
    
    private HashMap          favorites           = null;
    
    private SOSString        sosString           = null;
    
    private Label            lblPrefunction      = null;

    private Combo            cboPrefunction      = null;   
    
    private Button           button              = null;

    
    public ScriptForm(Composite parent, int style, ISchedulerUpdate update_) {
        super(parent, style);
        init = true;        
        update = update_;
        initialize();
        setToolTipText();
        init = false;
    }


    public ScriptForm(Composite parent, int style, String title, SchedulerDom dom, Element element, int type, ISchedulerUpdate update_) {
    	
        super(parent, style);
        init = true;       
        this.type = type;
        update = update_;
        groupTitle = title;
        initialize();
        setToolTipText();
        setAttributes(dom, element, type);
        
        gScript.setEnabled(Utils.isElementEnabled("job", dom, element));        	
        init = false;
        
    }


    public void setAttributes(SchedulerDom dom, Element element, int type_) {
    	
        listener = new ScriptListener(dom, element, type_, update);
        cboFavorite.setData("favorites", favorites);
        cboFavorite.setMenu(new ContextMenu(cboFavorite, listener.getDom(), Editor.SCRIPT).getMenu());
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
        sosString = new SOSString();
        if(normalized(Options.getPropertiesWithPrefix("monitor_favorite_")) != null &&
        		normalized(Options.getPropertiesWithPrefix("monitor_favorite_"))[0] != null)
        	cboFavorite.setItems(normalized(Options.getPropertiesWithPrefix("monitor_favorite_")));
        
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
        tClass.addVerifyListener(new VerifyListener() {
        	public void verifyText(final VerifyEvent e) {
        		if(e.text.length() > 0 && bJava.getSelection() && tSource.getText().length() > 0) {
            		MainWindow.message("Please remove first Source Code.", SWT.ICON_WARNING);
            		e.doit = false;            		            	
            		return;
            	}
        	}
        });
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
        		Options.setProperty("monitor_favorite_" + ( bCom.getSelection() ? "com" : listener.getLanguage(listener.getLanguage())) +"_" + txtName.getText(), getFavoriteValue());
        		Options.saveProperties();
        		 cboFavorite.setItems(normalized(Options.getPropertiesWithPrefix("monitor_favorite_")));
        	}
        });
        butFavorite.setEnabled(false);
        butFavorite.setVisible(type == Editor.MONITOR);
        butFavorite.setText("Favorites");
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
        GridData gridData1 = new org.eclipse.swt.layout.GridData(GridData.FILL, GridData.FILL, false, false, 3, 1);
        GridData gridData7 = new GridData(GridData.FILL, GridData.CENTER, false, false);
        GridData gridData6 = new GridData(GridData.FILL, GridData.CENTER, true, false);
        GridLayout gridLayout1 = new GridLayout();
        gridLayout1.marginWidth = 0;
        gridLayout1.marginHeight = 0;
        gridLayout1.numColumns = 3;
        gInclude = new Group(gScript, SWT.NONE);
        final GridData gridData_4 = new GridData(GridData.FILL, GridData.BEGINNING, false, false, 4, 1);
        gInclude.setLayoutData(gridData_4);
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
        final GridData gridData_2 = new GridData(GridData.FILL, GridData.FILL, true, true, 2, 2);
        gridData_2.heightHint = 4;
        gridData_2.minimumHeight = 20;
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
        tInclude.addModifyListener(new org.eclipse.swt.events.ModifyListener() {
            public void modifyText(org.eclipse.swt.events.ModifyEvent e) {
                bAdd.setEnabled(!tInclude.getText().equals(""));
            }
        });
        tInclude.addKeyListener(new org.eclipse.swt.events.KeyAdapter() {
            public void keyPressed(org.eclipse.swt.events.KeyEvent e) {
                if (e.keyCode == SWT.CR && !tInclude.getText().equals("")) {                    
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
                    listener.fillTable(tableIncludes);
                    if (index >= tableIncludes.getItemCount())
                        index--;
                    if (tableIncludes.getItemCount() > 0)
                    	tableIncludes.setSelection(index);
                }
               
            }
        });
        gSource = new Group(gScript, SWT.NONE);
        final GridData gridData_5 = new GridData(GridData.FILL, GridData.FILL, true, true, 4, 1);
        gridData_5.minimumHeight = 30;
        gSource.setLayoutData(gridData_5);
        final GridLayout gridLayout_2 = new GridLayout();
        gridLayout_2.marginHeight = 0;
        gridLayout_2.numColumns = 3;
        gSource.setLayout(gridLayout_2);
        gSource.setText("Source Code");

        lblPrefunction = new Label(gSource, SWT.NONE);
        lblPrefunction.setLayoutData(new GridData());
        lblPrefunction.setText("Select predefined functions:");

        cboPrefunction = new Combo(gSource, SWT.READ_ONLY);
        cboPrefunction.setVisibleItemCount(7);
        cboPrefunction.addSelectionListener(new SelectionAdapter() {        	
        	public void widgetSelected(final SelectionEvent e) {
        		if(cboPrefunction.getText().length() > 0) {
        			String lan = "function_" + ((type == Editor.MONITOR)? "monitor" : "job") + "_" + listener.getLanguage(listener.getLanguage()) + "_";
					tSource.append(Options.getProperty(lan.toLowerCase() + cboPrefunction.getText()));
					cboPrefunction.setText("");
					//cboPrefunction.setText("-- Templates --");
				}
        	}
        });
        cboPrefunction.setLayoutData(new GridData(GridData.FILL, GridData.CENTER, true, false));

        button = new Button(gSource, SWT.NONE);        
        final GridData gridData_3 = new GridData(GridData.CENTER, GridData.BEGINNING, false, false);
        gridData_3.widthHint = 30;
        button.setLayoutData(gridData_3);
        button.addSelectionListener(new SelectionAdapter() {
        	public void widgetSelected(final SelectionEvent e) {
        		String text = "";    		
        		if(type!=Editor.SCRIPT) {        			        		
        			String lan = "function_" + ((type == Editor.MONITOR)? "monitor" : "job") + "_" + listener.getLanguage(listener.getLanguage()) + "_";
        			text = sos.scheduler.editor.app.Utils.showClipboard(tSource.getText(), getShell(), true, "", true, lan);
        		} else {
        			text = sos.scheduler.editor.app.Utils.showClipboard(tSource.getText(), getShell(), true, "");
        		}
        		if(text != null)
        			tSource.setText(text);
        		
        	}
        });
        button.setImage(ResourceManager.getImageFromResource("/sos/scheduler/editor/icon_edit.gif"));
        
        tSource = new Text(gSource, SWT.V_SCROLL | SWT.MULTI | SWT.BORDER | SWT.H_SCROLL);
        tSource.addVerifyListener(new VerifyListener() {
        	public void verifyText(final VerifyEvent e) {
        		if(e.text.length() > 0 && bJava.getSelection() && tClass.getText().length() > 0) {
            		MainWindow.message("Please remove first Classname.", SWT.ICON_WARNING);
            		e.doit = false;            		            	
            		return;
            	}
        	}
        });
        tSource.addKeyListener(new KeyAdapter() {
        	public void keyPressed(final KeyEvent e) {
        		if(e.keyCode==97 && e.stateMask == SWT.CTRL){
        			tSource.setSelection(0, tSource.getText().length());
				}
        	}
        });
        final GridData gridData_1 = new GridData(GridData.FILL, GridData.FILL, false, true, 2, 1);
        gridData_1.minimumHeight = 40;
        gridData_1.widthHint = 454;
        gridData_1.heightHint = 55;
        tSource.setLayoutData(gridData_1);
        tSource.setFont(ResourceManager.getFont("Courier New", 8, SWT.NONE));
        tSource.addModifyListener(new org.eclipse.swt.events.ModifyListener() {
            public void modifyText(org.eclipse.swt.events.ModifyEvent e) {
            	
            	if(!init)
            		listener.setSource(tSource.getText());
            }
        });
        new Label(gSource, SWT.NONE);
       
        
    }


    /**
     * This method initializes composite
     */
    private void createComposite() {
        new Label(gScript, SWT.NONE);
    	    		    	
        cLanguage = new Composite(gScript, SWT.NONE);
        final GridLayout gridLayout = new GridLayout();
        gridLayout.verticalSpacing = 0;
        gridLayout.marginWidth = 0;
        gridLayout.marginHeight = 0;
        gridLayout.numColumns = 8;
        cLanguage.setLayout(gridLayout);
        cLanguage.setLayoutData(new GridData(GridData.FILL, GridData.BEGINNING, true, false, 2, 1));
        bJava = new Button(cLanguage, SWT.RADIO);
        bJava.setLayoutData(new GridData(GridData.BEGINNING, GridData.CENTER, false, true));
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
        bCom.setLayoutData(new GridData(GridData.BEGINNING, GridData.CENTER, false, true));
        bCom.setText("Com");
        bCom.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
            public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
            	if(bCom.getSelection() && tSource.getText().length() > 0) {
            		MainWindow.message("Please remove first Source Code.", SWT.ICON_WARNING);
            		bCom.setSelection(false);
            		fillForm();            		
            		return;
            	}
                if (bCom.getSelection()) {
                	if(tSource.getText().length() > 0) {
                		MainWindow.message("Please remove first Source Code.", SWT.ICON_WARNING);
                		return;
                	}
                    listener.setLanguage(ScriptListener.COM);
                    fillForm();
                }
            }
        });
        bJavaScript = new Button(cLanguage, SWT.RADIO);
        bJavaScript.setLayoutData(new GridData(GridData.BEGINNING, GridData.CENTER, false, true));
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
        bPerl.setLayoutData(new GridData(GridData.BEGINNING, GridData.CENTER, false, true));
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
        bVBScript.setLayoutData(new GridData(GridData.BEGINNING, GridData.CENTER, false, true));
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
        bShell.setLayoutData(new GridData(GridData.BEGINNING, GridData.CENTER, false, true));
        bShell.addSelectionListener(new SelectionAdapter() {
        	
        	public void widgetSelected(final SelectionEvent e) {
        		if (bShell.getSelection()) {
        			listener.setLanguage(ScriptListener.SHELL);
        			fillForm();
        		}
        	}
        });
        bShell.setText("Shell");
        bNone = new Button(cLanguage, SWT.RADIO);
       
       
        
        bNone.setLayoutData(new GridData(GridData.BEGINNING, GridData.CENTER, false, true));
        bNone.setText("None");
        bNone.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
            public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
            	if(bNone.getSelection() && tSource.getText().length() > 0) {
            		MainWindow.message("Please remove first Source Code.", SWT.ICON_WARNING);
            		bNone.setSelection(false);
            		fillForm();            		
            		return;
            	}
        		
                if (bNone.getSelection()) {                	
                    listener.setLanguage(ScriptListener.NONE);
                    fillForm();
                }
            }
        });

        
        cboFavorite = new Combo(cLanguage, SWT.NONE);
        cboFavorite.setVisible(type == Editor.MONITOR);
        
        
        cboFavorite.setLayoutData(new GridData(GridData.FILL, GridData.CENTER, true, true));
        cboFavorite.addSelectionListener(new SelectionAdapter() {
        	public void widgetSelected(final SelectionEvent e) {
        		if(cboFavorite.getText().length() > 0) {
        			if(Options.getProperty(getPrefix() + cboFavorite.getText()) != null) {
        				
        				if( (tClass.isEnabled() && tClass.getText().length() > 0) || 
        				    (tableIncludes.isEnabled() && tableIncludes.getItemCount() > 0)) {
        					int c = MainWindow.message(getShell(), "Overwrite this Monitor?", SWT.ICON_QUESTION | SWT.YES | SWT.NO );
        					if(c != SWT.YES)
        						return;
        					else {
        						tClass.setText("");
        						tableIncludes.clearAll();
        						listener.removeIncludes();
        					}
        				}
        				
        					
        				
        				if (favorites != null && favorites.get(cboFavorite.getText()) != null && favorites.get(cboFavorite.getText()).toString().length() > 0) {
        					if(favorites.get(cboFavorite.getText()).equals("com")) {
        					   bCom.setSelection(true);
        					   listener.setLanguage(ScriptListener.COM);
        					} else
        						listener.setLanguage(listener.languageAsInt(favorites.get(cboFavorite.getText()).toString()));
        					
        					txtName.setText(cboFavorite.getText());
        					
        					boolean isInclude = false;
        			    	switch (listener.getLanguage()) {        			    	
        			    	case ScriptListener.COM:
        			    		tClass.setText(Options.getProperty(getPrefix() + cboFavorite.getText())) ;
        			    		break;
        			    	case ScriptListener.JAVA:
        			    		tClass.setText(Options.getProperty(getPrefix() + cboFavorite.getText())) ;
        			    		break;
        			    	case ScriptListener.JAVA_SCRIPT:   
        			    		isInclude = true;
        			    		break;
        			    	case ScriptListener.PERL:
        			    		isInclude = true;
        			    		break;
        			    	case ScriptListener.VB_SCRIPT:
        			    		isInclude = true;
        			    		break;
        			    	}

        			    	if(isInclude) {
        			    		String[] split = Options.getProperty(getPrefix() + cboFavorite.getText()).split(";");
        			    		for(int i = 0; i < split.length ; i++){
        			    			listener.addInclude(split[i]);
        			    		}        			    		
        			    	}
            				
        					
        					bNone.setSelection(false);        			
                			bCom.setSelection(false);
                			bJava.setSelection(false);
                			bJavaScript.setSelection(false);
                			bPerl.setSelection(false);
                			bShell.setSelection(false);
                			bVBScript.setSelection(false);
        					fillForm();
        				}
        			}
        			
        		}
        	}
        });
    }


    private void fillForm() {
    	init = true;
        int language = listener.getLanguage();
        cboPrefunction.removeAll();
        if(type == Editor.MONITOR) {
        	txtName.setText(listener.getName());
        	spinner.setSelection((listener.getOrdering().length() == 0 ? 0 : Integer.parseInt(listener.getOrdering())));
        }
        
        if (type == Editor.EXECUTE) {
        	bNone.setVisible(true);           
        }

        if (type == Editor.MONITOR) {
        	bNone.setVisible(false);
        	//hier
        	bShell.setVisible(false);
        }
        
        setEnabled(true);
        
        
        cboFavorite.setEnabled(true);
        butFavorite.setEnabled(true);
       
        switch (language) {
            case ScriptListener.NONE:
                bNone.setSelection(true);
                tSource.setEnabled(false);
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
                lblPrefunction.setEnabled(false);
                cboPrefunction.setEnabled(false);
                button.setEnabled(false);
                break;
            case ScriptListener.JAVA:
            	bJava.setSelection(true);
            	 //tSource.setEnabled(false);
            	 //tSource.setText("");
                tClass.setEnabled(true);
                tClass.setFocus();
                if (!tClass.getText().equals("") && listener.getJavaClass().equals(""))
                    listener.setJavaClass(tClass.getText());
                tClass.setText(listener.getJavaClass());
                //listener.setSource("");
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
        	if(listener.getSource().length() > 0)
        		tSource.setText(listener.getSource());
        	else if(tSource.getText().length() > 0)
        		listener.setSource(tSource.getText());
        }                
        
        if (language != ScriptListener.NONE) {
            listener.fillTable(tableIncludes);
        }
        
        //String lan = "function_" + ((type == Editor.MONITOR)? "monitor" : "job") + "_" + listener.getLanguage(listener.getLanguage()) + "_";
        //cboPrefunction.setItems(Options.getPropertiesWithPrefix(lan.toLowerCase()));
        String lan = "";
        if(listener.getLanguage() == ScriptListener.JAVA_SCRIPT || listener.getLanguage() == ScriptListener.PERL ||
        		listener.getLanguage() == ScriptListener.VB_SCRIPT) {
        	if(type == Editor.MONITOR) {
        		lan = "spooler_task_before;spooler_task_after;spooler_process_before;spooler_process_after";
        	} else {
        		lan = "spooler_init;spooler_open;spooler_process;spooler_close;spooler_exit;spooler_on_error;spooler_on_success";
        	}
        	cboPrefunction.setItems(lan.split(";"));
        }
        init = false;
    }


   
    
    public void setEnabled(boolean enabled) {    	
        tClass.setEnabled(false);
        tFilename.setEnabled(false);       
        tableIncludes.setEnabled(enabled);
        bRemove.setEnabled(false);
        tInclude.setEnabled(enabled);
        butIsLifeFile.setEnabled(enabled);        
        tableIncludes.deselectAll();        
        bAdd.setEnabled(false);        
        tSource.setEnabled(enabled);
        lblPrefunction.setEnabled(enabled && type!=Editor.SCRIPT);
        cboPrefunction.setEnabled(enabled && type!=Editor.SCRIPT);
        button.setEnabled(enabled);
        
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
    	bNone.setSelection(false);
        listener.setLanguage(language);
        fillForm();
    }


    private void applyFile() {
    	listener.addInclude(tableIncludes, tInclude.getText(), butIsLifeFile.getSelection());       
        tInclude.setText("");
        tInclude.setFocus();
    }


    public void setToolTipText() {
        tClass.setToolTipText(Messages.getTooltip("script.class"));
        tFilename.setToolTipText(Messages.getTooltip("script.filename"));
        tInclude.setToolTipText(Messages.getTooltip("script.include.file_entry"));
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
        
        if(cboPrefunction!= null) cboPrefunction.setToolTipText("-- Templates --");
    }

    private String getPrefix() {
    	if(favorites != null&& cboFavorite.getText().length() > 0 && favorites.get(cboFavorite.getText()) != null)
    		return "monitor_favorite_" + favorites.get(cboFavorite.getText()) +"_" ;
    	if(listener.getLanguage() == 0)
    		return "";
    	return "monitor_favorite_" + ( bCom.getSelection() ? "com" : listener.getLanguage(listener.getLanguage())) +"_" ;
    }
    
    private String[] normalized(String[] str) {
    	String[] retVal = new String[]{""};
    	try {
    		favorites = new HashMap();
    		if(str == null) 
    			return new String[0];

    		String newstr = "";
    		retVal = new String[str.length];
    		for(int i = 0; i < str.length; i++) {
    			String s = sosString.parseToString(str[i]);
    			int idx = s.indexOf("_");
    			if(idx > -1) {    		
    				String lan = s.substring(0, idx);
    				String name = s.substring(idx+1);
    				if(name == null || lan == null)
    					System.out.println(name);
    				else
    					favorites.put(name, lan);
    				newstr = name + ";" + newstr;
    			} 
    		}
    		retVal = newstr.split(";");
    		return retVal;
    	} catch (Exception e) {
    		System.out.println(e.toString());
    		try {
    			new ErrorLog("error in " + sos.util.SOSClassUtil.getMethodName() , e);
    		} catch(Exception ee) {
    			//tu nichts
    		}
    		return retVal;	
    	}
    }
    
    private String  getFavoriteValue() {
    	String retVal = "";
    	int lan =  listener.getLanguage();
    	switch (lan) {
    	case ScriptListener.COM:
    		retVal = tClass.getText();
    		break;
    	case ScriptListener.JAVA:
    		retVal = tClass.getText();
    		break;
    	case ScriptListener.JAVA_SCRIPT:    		
    		retVal = listener.getIncludesAsString();
    		break;
    	case ScriptListener.PERL:
    		retVal = listener.getIncludesAsString();
    		break;
    	case ScriptListener.VB_SCRIPT:
    		retVal = listener.getIncludesAsString();
    		break;
    	}
    	
    	return retVal;
    }


    public int getSelectionLanguageButton() {
    	if (listener.getLanguage() != ScriptListener.NONE)
    		return listener.getLanguage();

    	if(bJava.getSelection()) {
    		return ScriptListener.JAVA;
    	} else if(bJavaScript.getSelection()) {
    		return ScriptListener.JAVA_SCRIPT;
    	} else if(bPerl.getSelection()) {
    		return ScriptListener.PERL;
    	} else if(bVBScript.getSelection()) {
    		return ScriptListener.VB_SCRIPT;
    	} else if(bNone.getSelection()) {
    		return ScriptListener.NONE;
    	}   	 else if(bCom.getSelection()) {
    		return ScriptListener.COM;    	
    	}   	 else if(bShell.getSelection()) {
    		return ScriptListener.SHELL;
    	}   	 else {
    		return ScriptListener.NONE;
    	}
    
   
    }
} // @jve:decl-index=0:visual-constraint="10,10"
