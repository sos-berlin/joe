/**
 * 
 */
package sos.scheduler.editor.conf.forms;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.FocusAdapter;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.Text;
import org.jdom.JDOMException;

import sos.scheduler.editor.app.IUnsaved;
import sos.scheduler.editor.app.IUpdateLanguage;
import sos.scheduler.editor.app.MainWindow;
import sos.scheduler.editor.app.Messages;
import sos.scheduler.editor.app.Options;
import sos.scheduler.editor.app.ResourceManager;
import sos.scheduler.editor.conf.SchedulerDom;
import sos.scheduler.editor.conf.listeners.BaseListener;

public class BaseForm extends Composite implements IUnsaved, IUpdateLanguage {
    
	private static final String	conImageICON_OPEN_GIF	= "/sos/scheduler/editor/icon_open.gif";
	private static final String	conImageICON_EDIT_GIF	= "/sos/scheduler/editor/icon_edit.gif";
	private final String conClassName = "BaseForm";
	final String conMethodName = conClassName + "::enclosing_method";
	@SuppressWarnings("unused")
	private final String conSVNVersion = "$Id$";

	private BaseListener listener = null;
    private Group        group    = null;
    private Label        label1   = null;
    private Text         tFile    = null;
    private Button       bApply   = null;
    private Button       bNew     = null;
    private Button       bRemove  = null;
    private Label        label    = null;
    private Label        label2   = null;
    private Label        label3   = null;
    private Text         tComment = null;
    private Table        table    = null;
    private Button       butOpen  = null; 
    private Button       button   = null;
    private Button       butOpenFileDialog = null; 

    /**
     * @param parent
     * @param style
     * @throws JDOMException
     */
    public BaseForm(Composite parent, int style, SchedulerDom dom) throws JDOMException {
        super(parent, style);
        listener = new BaseListener(dom);

        initialize();
        setToolTipText();
        listener.fillTable(table);
    }

    public boolean isUnsaved() {
        return bApply.isEnabled();
    }

    public void apply() {
        if (isUnsaved())
            applyFile();
    }

    private void initialize() {
        this.setLayout(new FillLayout());
        createGroup();
        setSize(new org.eclipse.swt.graphics.Point(657, 329));
    }

    private void createGroup() {
        GridData gridData21 = new org.eclipse.swt.layout.GridData();
        gridData21.horizontalAlignment = org.eclipse.swt.layout.GridData.FILL;
        gridData21.verticalAlignment = org.eclipse.swt.layout.GridData.CENTER;
        GridData gridData11 = new org.eclipse.swt.layout.GridData(GridData.FILL, GridData.CENTER, false, false, 3, 1);
        gridData11.heightHint = 10;
        GridData gridData4 = new org.eclipse.swt.layout.GridData();
        gridData4.horizontalAlignment = org.eclipse.swt.layout.GridData.FILL;
        gridData4.verticalAlignment = org.eclipse.swt.layout.GridData.CENTER;
        GridData gridData3 = new org.eclipse.swt.layout.GridData();
        gridData3.horizontalAlignment = org.eclipse.swt.layout.GridData.FILL;
        gridData3.verticalAlignment = org.eclipse.swt.layout.GridData.CENTER;
        GridData gridData2 = new org.eclipse.swt.layout.GridData();
        gridData2.horizontalAlignment = org.eclipse.swt.layout.GridData.FILL;
        gridData2.verticalSpan = 1;
        gridData2.verticalAlignment = org.eclipse.swt.layout.GridData.BEGINNING;
        GridData gridData1 = new org.eclipse.swt.layout.GridData(GridData.FILL, GridData.BEGINNING, false, false);
        GridLayout gridLayout = new GridLayout();
        gridLayout.numColumns = 3;
        gridLayout.verticalSpacing = 5;
        gridLayout.horizontalSpacing = 5;
        group = new Group(this, SWT.NONE);
        group.setText(Messages.getLabel("BaseFiles"));
        group.setLayout(gridLayout);
        label1 = new Label(group, SWT.NONE);
        label1.setText(Messages.getLabel("BaseFile"));
        tFile = new Text(group, SWT.BORDER);
        tFile.addFocusListener(new FocusAdapter() {
        	public void focusGained(final FocusEvent e) {
        		tFile.selectAll();
        	}
        });
        bApply = new Button(group, SWT.NONE);
        GridData gridData8 = new org.eclipse.swt.layout.GridData(GridData.BEGINNING, GridData.BEGINNING, false, false);
        label3 = new Label(group, SWT.NONE);
        label3.setText(Messages.getLabel("Comment"));
        label3.setLayoutData(gridData8);
        GridData gridData9 = new org.eclipse.swt.layout.GridData(GridData.FILL, GridData.FILL, false, false);
        gridData9.heightHint = 80;
        
        tComment = new Text(group, SWT.MULTI | SWT.WRAP | SWT.V_SCROLL | SWT.BORDER | SWT.H_SCROLL);
        tComment.addKeyListener(new KeyAdapter() {
        	public void keyPressed(final KeyEvent e) {
        		if(e.keyCode==97 && e.stateMask == SWT.CTRL){
					tComment.setSelection(0, tComment.getText().length());
				}
        	}
        });
        tComment.setLayoutData(gridData9);
        tComment.setFont(ResourceManager.getFont("Courier New", 8, SWT.NONE));
        tComment.setEnabled(false);
        tComment.addModifyListener(new org.eclipse.swt.events.ModifyListener() {
            public void modifyText(org.eclipse.swt.events.ModifyEvent e) {
                bApply.setEnabled(!tFile.getText().equals(""));
                button.setEnabled(!tFile.getText().equals(""));
            }
        });

        final Composite composite = new Composite(group, SWT.NONE);
        composite.setLayoutData(new GridData(GridData.FILL, GridData.CENTER, false, false));
        final GridLayout gridLayout_1 = new GridLayout();
        gridLayout_1.marginWidth = 0;
        gridLayout_1.marginHeight = 0;
        gridLayout_1.horizontalSpacing = 0;
        composite.setLayout(gridLayout_1);

        button = new Button(composite, SWT.NONE);
        final GridData gridData = new GridData(GridData.BEGINNING, GridData.CENTER, true, true);
        button.setLayoutData(gridData);
        button.setEnabled(false);
        button.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(final SelectionEvent e) {
				String text = sos.scheduler.editor.app.Utils.showClipboard(tComment.getText(), getShell(), true, "");
				if(text != null)
					tComment.setText(text);
			}
		});
        button.setImage(ResourceManager.getImageFromResource(conImageICON_EDIT_GIF));

        butOpenFileDialog = new Button(composite, SWT.NONE);
        butOpenFileDialog.setLayoutData(new GridData(GridData.BEGINNING, GridData.BEGINNING, true, true));
        butOpenFileDialog.setEnabled(false);
        butOpenFileDialog.addSelectionListener(new SelectionAdapter() {
        	public void widgetSelected(final SelectionEvent e) {
        		openFileDialog();
        	}
        	
        });
        butOpenFileDialog.setImage(ResourceManager.getImageFromResource(conImageICON_OPEN_GIF));
        
        label = new Label(group, SWT.SEPARATOR | SWT.HORIZONTAL | SWT.SHADOW_OUT);
        label.setText(Messages.getLabel("separator"));
        label.setLayoutData(gridData11);
        createTable();
        bNew = new Button(group, SWT.NONE);
        bNew.setLayoutData(gridData3);
        bNew.setText(Messages.getLabel("NewBaseFile"));
        getShell().setDefaultButton(bNew);

        butOpen = new Button(group, SWT.NONE);
        butOpen.addSelectionListener(new SelectionAdapter() {
        	public void widgetSelected(final SelectionEvent e) {
        		openBaseElement();
        		
        	}
        });
        butOpen.setLayoutData(new GridData(GridData.FILL, GridData.CENTER, false, false));
        butOpen.setText(Messages.getLabel("OpenBaseFile"));

        label2 = new Label(group, SWT.SEPARATOR | SWT.HORIZONTAL);
        label2.setText(Messages.getLabel("Label"));
        label2.setLayoutData(gridData21);
        bNew.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
            public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
                listener.newBaseFile();
                setInput(true);
            }
        });
        bRemove = new Button(group, SWT.NONE);
        bRemove.setEnabled(false);
        bRemove.setText(Messages.getLabel("RemoveBaseFile"));
        bRemove.setLayoutData(gridData2);
        bRemove.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
            public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
                if (table.getSelectionCount() > 0) {
                    int index = table.getSelectionIndex();
                    listener.removeBaseFile(index);
                    table.remove(index);
                    if (index >= table.getItemCount())
                        index--;
                    if (table.getItemCount() > 0) {
                        table.select(index);
                        listener.selectBaseFile(index);
                        setInput(true);
                    } else
                        setInput(false);
                }
                bRemove.setEnabled(table.getSelectionCount() > 0);
            }
        });
        tFile.setEnabled(false);
        tFile.setLayoutData(gridData4);
        tFile.addModifyListener(new org.eclipse.swt.events.ModifyListener() {
            public void modifyText(org.eclipse.swt.events.ModifyEvent e) {
                getShell().setDefaultButton(bApply);
                bApply.setEnabled(!tFile.getText().equals(""));
                button.setEnabled(!tFile.getText().equals(""));
            }
        });
        tFile.addKeyListener(new org.eclipse.swt.events.KeyAdapter() {
            public void keyReleased(org.eclipse.swt.events.KeyEvent e) {
                if (e.keyCode == SWT.CR && !tFile.getText().equals(""))
                    applyFile();
            }
        });
        bApply.setLayoutData(gridData1);
        bApply.setText(Messages.getLabel("ApplyBaseFile"));
        bApply.setEnabled(false);
        bApply.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
            public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
                applyFile();
            }
        });
    }


    /**
     * This method initializes table
     */
    private void createTable() {
        GridData gridData = new org.eclipse.swt.layout.GridData(GridData.FILL, GridData.FILL, true, true, 2, 4);
        table = new Table(group, SWT.BORDER | SWT.FULL_SELECTION);
        table.addMouseListener(new MouseAdapter() {
        	public void mouseDoubleClick(final MouseEvent e) {
        		openBaseElement();
        	}
        });
        table.setHeaderVisible(true);
        table.setLayoutData(gridData);
        table.setLinesVisible(true);
        table.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
            public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
                if (table.getSelectionCount() > 0) {
                    listener.selectBaseFile(table.getSelectionIndex());
                    setInput(true);
                }
            }
        });
        TableColumn tableColumn = new TableColumn(table, SWT.NONE);
        tableColumn.setWidth(300);
        tableColumn.setText(Messages.getLabel("BaseFile"));
        TableColumn tableColumn1 = new TableColumn(table, SWT.NONE);
        table.setSortColumn(tableColumn1);
        tableColumn1.setWidth(300);
        tableColumn1.setText(Messages.getLabel("Comment"));
    }


    private void applyFile() {
        listener.applyBaseFile(tFile.getText(), tComment.getText());
        listener.fillTable(table);
        setInput(false);
        getShell().setDefaultButton(bNew);
    }


    private void setInput(boolean enabled) {
        tFile.setEnabled(enabled);
        tComment.setEnabled(enabled);
        butOpenFileDialog.setEnabled(enabled);
        if (enabled) {
            tFile.setText(listener.getFile());
            tComment.setText(listener.getComment());
            tFile.setFocus();
        } else {
            tFile.setText("");
            tComment.setText("");
        }
        bApply.setEnabled(false);
        button.setEnabled(false);
        bRemove.setEnabled(table.getSelectionCount() > 0);
    }


    public void setToolTipText() {
        bNew.setToolTipText(Messages.getTooltip("base.btn_new_file"));
        tComment.setToolTipText(Messages.getTooltip("base.comment"));
        bRemove.setToolTipText(Messages.getTooltip("base.btn_remove_file"));
        bApply.setToolTipText(Messages.getTooltip("base.btn_apply"));
        table.setToolTipText(Messages.getTooltip("base.table"));
        tFile.setToolTipText(Messages.getTooltip("base.file_input"));
        butOpen.setToolTipText(Messages.getTooltip("base.file_open"));
        button.setToolTipText(Messages.getTooltip("button.comment"));

    }
    
    //öffnet das File Dialog um ein Basefile auszuwählen    
    private void openFileDialog() {
    	sos.scheduler.editor.app.IContainer con = MainWindow.getContainer();
    	String currPath = "";
    	String sep = System.getProperty("file.separator");
    	if(con.getCurrentEditor().getFilename() != null && con.getCurrentEditor().getFilename().length() > 0) {
    		currPath = new java.io.File(con.getCurrentEditor().getFilename()).getParent();    			
    	} else {
    		currPath = Options.getSchedulerData() + sep + "config";
    	}
    	
    	currPath = currPath.replace("/".toCharArray()[0], sep.toCharArray()[0]);
    	currPath = currPath.replace("\\".toCharArray()[0], sep.toCharArray()[0]);


    	FileDialog fdialog = new FileDialog(MainWindow.getSShell(), SWT.OPEN);
    	fdialog.setFilterPath(currPath);
    	fdialog.setFilterExtensions(new String[]{"*.xml", "*.sosdoc", "*.*"});
    	fdialog.setText(Messages.getLabel("OpenBaseFile"));

    	String fname = fdialog.open();
    	if (fname == null)
    		return;

    	//fname = fname.substring(currPath.length()+1);
    	fname = fname.replaceAll("/", sep);
    	//fname = fname.replaceAll("\\\\", sep);
    	
    	if(fname.toLowerCase().startsWith(currPath.toLowerCase()))
    		fname = fname.substring(currPath.length()+1);
    	
    	tFile.setText(fname);
    	
    	//path = fdialog.getFilterPath();
    }	

    
    //öffnet den BaseFile im seperaten Tabitem im Editor
    private void openBaseElement() {
    	String currPath = "";
    	String sep = System.getProperty("file.separator");
    	if(tFile.getText() != null && tFile.getText().length() > 0) {
    		
    		sos.scheduler.editor.app.IContainer con = MainWindow.getContainer();
    		
    		if(con.getCurrentEditor().getFilename() != null && con.getCurrentEditor().getFilename().length() > 0) {
    			currPath = new java.io.File(con.getCurrentEditor().getFilename()).getParent();    			
    		} else {
    			currPath = sos.scheduler.editor.app.Options.getSchedulerData() + sep + "config";
    		}
    		if(!(currPath.endsWith("/") || currPath.endsWith("\\")))
				currPath = currPath.concat(sep);
			
    		if(tFile.getText().toLowerCase().startsWith(currPath.toLowerCase()))
    			currPath = tFile.getText();
    		else
    			currPath = currPath.concat(tFile.getText());
    		
    		con.openScheduler(currPath);
    		
    		con.setStatusInTitle();
    		
    	} else {        			
    		MainWindow.message("There is no Basefile defined.", SWT.ICON_WARNING | SWT.OK);
    	}
    }
} // @jve:decl-index=0:visual-constraint="10,10"
