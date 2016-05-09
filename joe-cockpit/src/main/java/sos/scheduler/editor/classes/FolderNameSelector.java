package sos.scheduler.editor.classes;

import java.io.File;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.FocusAdapter;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.DirectoryDialog;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Text;

import sos.scheduler.editor.app.MainWindow;
import sos.scheduler.editor.conf.listeners.JobListener;
import com.sos.joe.globals.messages.Messages;
import com.sos.joe.globals.options.Options;
import com.sos.joe.xml.IOUtils;

public class FolderNameSelector extends Text {

    private JobListener objDataProvider = null;
    private String strFolderName = "";
    private Menu objContextMenu = null;
    private FormBaseClass objParentForm = null;
    private String strI18NKey = "";
    public boolean flgIsFileFromLiveFolder = false;

    public FolderNameSelector(Composite pobjComposite, int arg1) {
        super(pobjComposite, arg1);
        addFocusListener(getFocusAdapter());
        setLayoutData(new GridData(GridData.FILL, GridData.CENTER, true, false));
        addMouseListener(getMouseListener());
        addContextMenue();
    }

    public void setParentForm(final FormBaseClass pobjParentForm) {
        objParentForm = pobjParentForm;
    }

    public void setLayoutData(Object pobjLayoutData) {
        super.setLayoutData(pobjLayoutData);
    }

    public String getFolderName() {
        return strFolderName;
    }

    private void addContextMenue() {
        if (objContextMenu == null) {
            objContextMenu = getMenu();
            if (objContextMenu == null) {
                objContextMenu = new Menu(getShell(), SWT.POP_UP);
            }
            MenuItem item = null;
            item = new MenuItem(objContextMenu, SWT.PUSH);
            item.addListener(SWT.Selection, CopyToClipboardListener());
            item.setText("Copy to Clipboard");
            item = new MenuItem(objContextMenu, SWT.PUSH);
            item.addListener(SWT.Selection, OpenListener());
            item.setText("Open");
            setMenu(objContextMenu);
        }
    }

    public void setI18NKey(final String pstrI18NKey) {
        strI18NKey = pstrI18NKey;
        setToolTipText(Messages.getTooltip(strI18NKey));
    }

    public String getI18NKey() {
        return strI18NKey;
    }

    public void setDataProvider(final JobListener pobjDataProvider) {
        objDataProvider = pobjDataProvider;
        refreshContent();
    }

    private Listener CopyToClipboardListener() {
        return new Listener() {

            public void handleEvent(Event e) {
                //
            }
        };
    }

    private Listener OpenListener() {
        return new Listener() {

            public void handleEvent(Event e) {
                objParentForm.showWaitCursor();
                String strLastFolderName = getText();
                if (strLastFolderName.trim().isEmpty()) {
                    strLastFolderName = Options.getLastIncludeFolderName();
                }
                String strT = openDirectory(strLastFolderName);
                objParentForm.restoreCursor();
                if (!strT.trim().isEmpty()) {
                    setText(strT);
                } else {
                    e.doit = false;
                }
                objParentForm.restoreCursor();
            }
        };
    }

    private FocusAdapter getFocusAdapter() {
        return new FocusAdapter() {

            @Override
            public void focusGained(final FocusEvent e) {
                selectAll();
                String strT = Messages.getTooltip(strI18NKey);
                objParentForm.setStatusLine(strT);
            }

            @Override
            public void focusLost(final FocusEvent e) {
                //
            }
        };
    }

    private MouseListener getMouseListener() {
        return new MouseListener() {

            @Override
            public void mouseUp(MouseEvent arg0) {
                // TO DO Auto-generated method stub
            }

            @Override
            public void mouseDown(MouseEvent arg0) {
                // TO DO Auto-generated method stub
            }

            @Override
            public void mouseDoubleClick(MouseEvent arg0) {
                objParentForm.showWaitCursor();
                String strT = "";
                strFolderName = strT;
                if (flgIsFileFromLiveFolder) {
                    String strLiveFolderName = Options.getSchedulerHotFolder();
                    strT = IOUtils.openDirectoryFile("*.*", strLiveFolderName);
                    if (objDataProvider.isNotEmpty(strT)) {
                        File objFile = new File(strLiveFolderName, strT);
                        setText(objFile.getName());
                    }
                } else {
                    String strLastFolderName = Options.getLastPropertyValue(strI18NKey);
                    strT = openDirectory(strLastFolderName);

                    if (objDataProvider.isNotEmpty(strT)) {
                        File objFile = new File(strT);
                        if (objFile.canRead()) {
                            setText(objFile.getAbsoluteFile().toString());
                            if (flgIsFileFromLiveFolder == false) {
                                Options.setLastPropertyValue(strI18NKey, strLastFolderName);
                            }
                            strFolderName = strT;
                            setText(objFile.getName());
                            objParentForm.setStatusLine(String.format("Directory '%1$s' selected", strT));
                        } else {
                            objParentForm.MsgWarning(String.format("Directory '%1$s' is not readable", strT));
                        }
                    }
                }
                objParentForm.restoreCursor();
            }
        };
    }

    public String openDirectory(final String pstrDirectoryName) {
        String filename = "";
        DirectoryDialog fdialog = new DirectoryDialog(MainWindow.getSShell(), SWT.OPEN);
        fdialog.setFilterPath(pstrDirectoryName);
        filename = fdialog.open();
        if (filename == null || filename.trim().isEmpty()) {
            return filename;
        }
        return filename.replaceAll("\\\\", "/");
    }

    public void refreshContent() {
        //
    }

    protected void checkSubclass() {
        // Disable the check that prevents subclassing of SWT components
    }

}