package sos.scheduler.editor.conf.composites;

import org.apache.log4j.Logger;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;

import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.SWT;

import sos.scheduler.editor.app.ContextMenu;
import sos.scheduler.editor.app.IProcessClassDataProvider;

import com.sos.joe.globals.JOEConstants;
import com.sos.joe.globals.messages.SOSJOEMessageCodes;
import com.sos.joe.xml.IOUtils;
import com.sos.joe.xml.jobscheduler.MergeAllXMLinDirectory;

public class ProcessClassSelector extends SOSJOEMessageCodes {

    @SuppressWarnings("unused")
    private static Logger logger = Logger.getLogger(JobMainComposite.class);
    @SuppressWarnings("unused")
    private final String conClassName = "JobMainForm";
    private IProcessClassDataProvider processClassDataProvider = null;
    @SuppressWarnings("unused")
    private Label lblProcessClass = null;
    private Combo cProcessClass = null;
    private Button butBrowse = null;
    private boolean init = true;
    private int horizontalSpan = 3;
    private Button butShowProcessClass = null;
    private int intComboBoxStyle = SWT.NONE;

    /** Create the composite.
     * 
     * @param parent
     * @param style */
    public ProcessClassSelector(IProcessClassDataProvider processClassDataProvider_, Composite parent, int style) {
        super(parent, style);
        processClassDataProvider = processClassDataProvider_;
        createComposite(parent);
        init();
    }

    /** Create the composite.
     * 
     * @param parent
     * @param style */
    public ProcessClassSelector(IProcessClassDataProvider processClassDataProvider_, Composite parent, int horizontalSpan_, int style) {
        super(parent, style);
        processClassDataProvider = processClassDataProvider_;
        horizontalSpan = horizontalSpan_;
        createComposite(parent);
        init();
    }

    private void init() {
        String process_class = processClassDataProvider.getProcessClass();
        cProcessClass.setItems(processClassDataProvider.getProcessClasses());
        cProcessClass.setText(process_class);
        init = false;

    }

    private void createComposite(Composite parent) {

        lblProcessClass = JOE_L_JobMainComposite_ProcessClass.Control(new Label(parent, SWT.NONE));
        butShowProcessClass = JOE_B_JobMainComposite_ShowProcessClass.Control(new Button(parent, SWT.ARROW | SWT.DOWN));
        butShowProcessClass.setVisible(processClassDataProvider.get_dom() != null && !processClassDataProvider.get_dom().isLifeElement());
        butShowProcessClass.addSelectionListener(new SelectionAdapter() {

            public void widgetSelected(final SelectionEvent e) {
                String strT = cProcessClass.getText();
                if (strT.length() > 0) {
                    ContextMenu.goTo(strT, processClassDataProvider.get_dom(), JOEConstants.PROCESS_CLASSES);
                }
            }
        });
        butShowProcessClass.setAlignment(SWT.RIGHT);
        butShowProcessClass.setVisible(true);
        cProcessClass = JOE_Cbo_JobMainComposite_ProcessClass.Control(new Combo(parent, intComboBoxStyle));
        cProcessClass.setMenu(new ContextMenu(cProcessClass, processClassDataProvider.get_dom(), JOEConstants.PROCESS_CLASSES).getMenu());
        cProcessClass.addModifyListener(new ModifyListener() {

            public void modifyText(final ModifyEvent e) {
                if (init) {
                    return;
                }
                processClassDataProvider.setProcessClass(cProcessClass.getText());
                butShowProcessClass.setVisible(true);
            }
        });
        cProcessClass.setLayoutData(new GridData(GridData.FILL, GridData.CENTER, false, false, horizontalSpan, 1));
        cProcessClass.addSelectionListener(new SelectionAdapter() {

            public void widgetSelected(SelectionEvent e) {
                if (init) {
                    return;
                }
                processClassDataProvider.setProcessClass(cProcessClass.getText());
            }
        });
        cProcessClass.addKeyListener(new KeyListener() {

            @Override
            public void keyPressed(KeyEvent event) {
                if (event.keyCode == SWT.F1) {
                    processClassDataProvider.openXMLAttributeDoc("job", "process_class");
                }
                if (event.keyCode == SWT.F10) {
                    processClassDataProvider.openXMLDoc("job");
                }
            }

            @Override
            public void keyReleased(KeyEvent arg0) {
            }
        });
        cProcessClass.addMouseListener(new MouseListener() {

            @Override
            public void mouseUp(MouseEvent arg0) {
            }

            @Override
            public void mouseDown(MouseEvent arg0) {
            }

            @Override
            public void mouseDoubleClick(MouseEvent arg0) {
                String strT = cProcessClass.getText();
                if (strT.length() > 0) {
                    ContextMenu.goTo(strT, processClassDataProvider.get_dom(), JOEConstants.PROCESS_CLASSES);
                }
            }
        });
        butBrowse = JOE_B_JobMainComposite_BrowseProcessClass.Control(new Button(parent, SWT.NONE));
        butBrowse.addSelectionListener(new SelectionAdapter() {

            public void widgetSelected(final SelectionEvent e) {
                String name = IOUtils.getJobschedulerObjectPathName(MergeAllXMLinDirectory.MASK_PROCESS_CLASS);
                if (name != null && name.length() > 0)
                    cProcessClass.setText(name);
            }
        });
    }

    public void setProcessClass(String processClass) {
        cProcessClass.setText(processClass);
    }

    @Override
    public void setEnabled(boolean enabled) {
        cProcessClass.setEnabled(enabled);
        butBrowse.setEnabled(enabled);
    }

    public Combo getcProcessClass() {
        return cProcessClass;
    }

    public void setLabel(String caption) {
        lblProcessClass.setText(caption);
    }

    protected void checkSubclass() {
        // Disable the check that prevents subclassing of SWT components
    }

}
