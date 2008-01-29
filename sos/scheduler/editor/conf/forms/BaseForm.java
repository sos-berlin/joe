/**
 * 
 */
package sos.scheduler.editor.conf.forms;

import org.eclipse.swt.SWT;
//import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
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
import sos.scheduler.editor.app.ResourceManager;
import sos.scheduler.editor.conf.SchedulerDom;
import sos.scheduler.editor.conf.listeners.BaseListener;

/**
 * @author sky2000
 */
public class BaseForm extends Composite implements IUnsaved, IUpdateLanguage {
    private BaseListener listener;

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


    /**
     * This method initializes group
     */
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
        group.setText("Base Files");
        group.setLayout(gridLayout);
        label1 = new Label(group, SWT.NONE);
        label1.setText("Base File:");
        tFile = new Text(group, SWT.BORDER);
        bApply = new Button(group, SWT.NONE);
        GridData gridData8 = new org.eclipse.swt.layout.GridData(GridData.BEGINNING, GridData.BEGINNING, false, false);
        label3 = new Label(group, SWT.NONE);
        label3.setText("Comment:");
        label3.setLayoutData(gridData8);
        GridData gridData9 = new org.eclipse.swt.layout.GridData(GridData.FILL, GridData.FILL, false, false);
        gridData9.heightHint = 80;
        
        tComment = new Text(group, SWT.MULTI | SWT.WRAP | SWT.V_SCROLL | SWT.BORDER | SWT.H_SCROLL);
        tComment.setLayoutData(gridData9);
        tComment.setFont(ResourceManager.getFont("Courier New", 8, SWT.NONE));
        tComment.setEnabled(false);
        tComment.addModifyListener(new org.eclipse.swt.events.ModifyListener() {
            public void modifyText(org.eclipse.swt.events.ModifyEvent e) {
                bApply.setEnabled(!tFile.getText().equals(""));
            }
        });

        final Button button = new Button(group, SWT.NONE);
        final GridData gridData = new GridData(GridData.BEGINNING, GridData.BEGINNING, false, false);
        gridData.widthHint = 23;
        button.setLayoutData(gridData);
        button.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(final SelectionEvent e) {
				String text = sos.scheduler.editor.app.Utils.showClipboard(tComment.getText(), getShell(), true, "");
				if(text != null)
					tComment.setText(text);
			}
		});
        button.setImage(ResourceManager.getImageFromResource("/sos/scheduler/editor/icon_edit.gif"));
        label = new Label(group, SWT.SEPARATOR | SWT.HORIZONTAL | SWT.SHADOW_OUT);
        label.setText("separator");
        label.setLayoutData(gridData11);
        createTable();
        bNew = new Button(group, SWT.NONE);
        bNew.setLayoutData(gridData3);
        bNew.setText("&New Base File");
        getShell().setDefaultButton(bNew);

        butOpen = new Button(group, SWT.NONE);
        butOpen.addSelectionListener(new SelectionAdapter() {
        	public void widgetSelected(final SelectionEvent e) {
        		openBaseElement();
        		
        	}
        });
        butOpen.setLayoutData(new GridData(GridData.FILL, GridData.CENTER, false, false));
        butOpen.setText("Open Base File");

        label2 = new Label(group, SWT.SEPARATOR | SWT.HORIZONTAL);
        label2.setText("Label");
        label2.setLayoutData(gridData21);
        bNew.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
            public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
                listener.newBaseFile();
                setInput(true);
            }
        });
        bRemove = new Button(group, SWT.NONE);
        bRemove.setEnabled(false);
        bRemove.setText("Remove Base File");
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
            }
        });
        tFile.addKeyListener(new org.eclipse.swt.events.KeyAdapter() {
            public void keyReleased(org.eclipse.swt.events.KeyEvent e) {
                if (e.keyCode == SWT.CR && !tFile.getText().equals(""))
                    applyFile();
            }
        });
        bApply.setLayoutData(gridData1);
        bApply.setText("&Apply Base File");
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
        tableColumn.setText("Base File");
        TableColumn tableColumn1 = new TableColumn(table, SWT.NONE);
        table.setSortColumn(tableColumn1);
        tableColumn1.setWidth(300);
        tableColumn1.setText("Comment");
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
        if (enabled) {
            tFile.setText(listener.getFile());
            tComment.setText(listener.getComment());
            tFile.setFocus();
        } else {
            tFile.setText("");
            tComment.setText("");
        }
        bApply.setEnabled(false);
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

    }
    
    private void openBaseElement() {
    	String currPath = "";
    	
    	if(tFile.getText() != null && tFile.getText().length() > 0) {
    		//String xmlPath = sos.scheduler.editor.app.Options.getSchedulerHome() ;
    		//xmlPath = (xmlPath.endsWith("/") || xmlPath.endsWith("\\") ? xmlPath.concat("config") : xmlPath.concat("/config/"));
    		//xmlPath = xmlPath.concat(tFile.getText());
    		
    		sos.scheduler.editor.app.IContainer con = MainWindow.getContainer();
    		
    		if(con.getCurrentEditor().getFilename() != null && con.getCurrentEditor().getFilename().length() > 0) {
    			currPath = new java.io.File(con.getCurrentEditor().getFilename()).getParent();
    			if(!(currPath.endsWith("/") || currPath.endsWith("\\")))
    				currPath = currPath.concat(System.getProperty("file.separator"));
    				
    		}
    		currPath = currPath.concat(tFile.getText());
    		con.openScheduler(currPath);
    		//con.openScheduler(xmlPath);
    		con.setStatusInTitle();
    		
    	} else {        			
    		MainWindow.message("There is no Basefile defined.", SWT.ICON_WARNING | SWT.OK);
    	}
    }
} // @jve:decl-index=0:visual-constraint="10,10"
