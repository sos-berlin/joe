package sos.scheduler.editor.classes;

import java.io.File;

import com.sos.joe.globals.options.Options;

import com.sos.joe.xml.IOUtils;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.FocusAdapter;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Text;

import sos.scheduler.editor.app.MainWindow;
import sos.scheduler.editor.conf.listeners.JobListener;

public class FileNameSelector extends Text {

    private JobListener objDataProvider = null;
    private boolean flgInit = false;
    public boolean flgIsFileFromLiveFolder = false;
    private String strFileName = "";

    public String getFileName() {
        return strFileName;
    }

    public void setDataProvider(final JobListener pobjDataProvider) {
        objDataProvider = pobjDataProvider;
        refreshContent();
        Menu objContextMenu = getMenu();
        if (objContextMenu == null) {
            objContextMenu = new Menu(getShell(), SWT.POP_UP);
        }
        MenuItem item = new MenuItem(objContextMenu, SWT.PUSH);
        item.addListener(SWT.Selection, getSaveAsListener());
        item.setText("Save as ...");
    }

    private Listener getSaveAsListener() {
        return new Listener() {

            public void handleEvent(Event e) {
            }
        };
    }

    private FocusAdapter getFocusAdapter() {
        return new FocusAdapter() {

            @Override
            public void focusGained(final FocusEvent e) {
                selectAll();
            }

            @Override
            public void focusLost(final FocusEvent e) {
            }
        };
    }

    private MouseListener getMouseListener() {
        return (new MouseListener() {

            @Override
            public void mouseUp(MouseEvent arg0) {
                // TODO Auto-generated method stub
            }

            @Override
            public void mouseDown(MouseEvent arg0) {
                // TODO Auto-generated method stub
            }

            @Override
            public void mouseDoubleClick(MouseEvent arg0) {
                String strT = "";
                strFileName = strT;
                if (flgIsFileFromLiveFolder) {
                    String strLiveFolderName = Options.getSchedulerHotFolder();
                    strT = IOUtils.openDirectoryFile("*.*", strLiveFolderName);
                    if (objDataProvider.isNotEmpty(strT)) {
                        File objFile = new File(strLiveFolderName, strT);
                        setText(objFile.getName());
                    }
                } else {
                    String strLastFolderName = Options.getLastIncludeFolderName();
                    strT = IOUtils.openDirectoryFile("*.*", strLastFolderName);
                    if (objDataProvider.isNotEmpty(strT)) {
                        File objFile = new File(strT);
                        if (objFile.canRead()) {
                            setText(objFile.getAbsoluteFile().toString());
                            if (flgIsFileFromLiveFolder == false) {
                                Options.setLastIncludeFolderName(objFile.getParent());
                            }
                            strFileName = strT;
                            setText(objFile.getName());
                            // evtl. ein CallBack einbauen ...
                            // applyFile2Include();
                        } else {
                            MainWindow.ErrMsg(String.format("File '%1$s' not found or is not readable", strT));
                        }
                    }
                }
            }
        });
    }

    public void refreshContent() {
        flgInit = true;
        // switch (enuWhatSourceType) {
        // case ScriptSource:
        // setText(objDataProvider.getSource());
        // break;
        //
        // case xmlSource:
        // setText(objDataProvider.getXML());
        // break;
        //
        // default:
        // break;
        // }
        flgInit = false;
    }

    public FileNameSelector(Composite pobjComposite, int arg1) {
        super(pobjComposite, arg1);
        addFocusListener(getFocusAdapter());
        setLayoutData(new GridData(GridData.FILL, GridData.CENTER, true, false));
        addMouseListener(getMouseListener());
    }

    protected void checkSubclass() {
        // Disable the check that prevents subclassing of SWT components
    }
}
