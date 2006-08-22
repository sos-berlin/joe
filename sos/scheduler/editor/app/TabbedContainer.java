package sos.scheduler.editor.app;

import java.util.ArrayList;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabFolder2Adapter;
import org.eclipse.swt.custom.CTabFolderEvent;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;

import sos.scheduler.editor.conf.forms.SchedulerForm;
import sos.scheduler.editor.doc.forms.DocumentationForm;

public class TabbedContainer implements IContainer {
    private static final String NEW_SCHEDULER_TITLE     = "Unknown";

    private static final String NEW_DOCUMENTATION_TITLE = "Unknown";

    private CTabFolder          folder                  = null;

    private MainWindow          window                  = null;

    private ArrayList           filelist                = new ArrayList();


    public TabbedContainer(MainWindow window, Composite parent) {
        this.window = window;
        folder = new CTabFolder(parent, SWT.TOP | SWT.CLOSE);
        initialize();
    }


    private void initialize() {
        folder.setSimple(false);
        folder.setSize(new Point(690, 478));
        folder.setLayout(new FillLayout());

        // on tab selection
        folder.addSelectionListener(new SelectionListener() {
            public void widgetSelected(SelectionEvent e) {
                setWindowTitle();
                window.setMenuStatus();
            }


            public void widgetDefaultSelected(SelectionEvent e) {
                widgetSelected(e);
            }
        });

        // on tab close
        folder.addCTabFolder2Listener(new CTabFolder2Adapter() {
            public void close(CTabFolderEvent event) {
                IEditor editor = getCurrentEditor();
                if (editor.hasChanges()) {
                    event.doit = editor.close();
                }
                if (event.doit)
                    filelist.remove(editor.getFilename());
            }
        });
    }


    public SchedulerForm newScheduler() {
        SchedulerForm scheduler = new SchedulerForm(this, folder, SWT.NONE);
        scheduler.openBlank();
        CTabItem tab = newItem(scheduler, NEW_SCHEDULER_TITLE);
        tab.setImage(new Image(tab.getDisplay(), getClass().getResourceAsStream(
                "/sos/scheduler/editor/editor-small.png")));
        return scheduler;
    }


    public SchedulerForm openScheduler() {
        SchedulerForm scheduler = new SchedulerForm(this, folder, SWT.NONE);
        if (scheduler.open(filelist)) {
            CTabItem tab = newItem(scheduler, scheduler.getFilename());
            tab.setImage(ResourceManager.getImageFromResource("/sos/scheduler/editor/editor-small.png"));
            return scheduler;
        } else
            return null;
    }


    public DocumentationForm newDocumentation() {
        DocumentationForm doc = new DocumentationForm(this, folder, SWT.NONE);
        doc.openBlank();
        newItem(doc, NEW_DOCUMENTATION_TITLE);
        return doc;
    }


    public DocumentationForm openDocumentation() {
        DocumentationForm doc = new DocumentationForm(this, folder, SWT.NONE);
        if (doc.open(filelist)) {
            CTabItem tab = newItem(doc, doc.getFilename());
            // tab.setImage(ResourceManager.getImageFromResource("/sos/scheduler/editor/editor-small.png"));
            return doc;
        } else
            return null;
    }


    private CTabItem newItem(Control control, String filename) {
        CTabItem tab = new CTabItem(folder, SWT.NONE);
        tab.setControl(control);
        folder.setSelection(folder.indexOf(tab));
        String title = setSuffix(Utils.getFileFromURL(filename));
        tab.setText(title);
        tab.setToolTipText(filename);
        tab.setData(title);

        if (!filename.equals(NEW_DOCUMENTATION_TITLE) && !filename.equals(NEW_SCHEDULER_TITLE))
            filelist.add(filename);

        return tab;
    }


    private CTabItem getCurrentTab() {
        if (folder.getItemCount() == 0)
            return null;
        else
            return folder.getItem(folder.getSelectionIndex());
    }


    public IEditor getCurrentEditor() {
        if (folder.getItemCount() == 0)
            return null;
        else
            return (IEditor) getCurrentTab().getControl();
    }


    public void setStatusInTitle() {
        if (folder.getItemCount() == 0)
            return;

        CTabItem tab = getCurrentTab();
        String title = (String) tab.getData();
        tab.setText(getCurrentEditor().hasChanges() == false ? title : "*" + title);
        setWindowTitle();
        window.setMenuStatus();
    }


    public void setNewFilename(String oldFilename) {
        if (folder.getItemCount() == 0)
            return;
        String filename = getCurrentEditor().getFilename();
        CTabItem tab = getCurrentTab();
        if (oldFilename != null) {
            filelist.remove(oldFilename);
            filelist.add(filename);
        }

        String title = setSuffix(Utils.getFileFromURL(filename));
        tab.setText(title);
        tab.setToolTipText(filename);
        tab.setData(title);
        setWindowTitle();
    }


    private void setWindowTitle() {
        Shell shell = folder.getShell();
        shell.setText((String) shell.getData() + " [" + getCurrentTab().getText() + "]");
    }


    private String setSuffix(String title) {
        int sameTitles = getSameTitles(title);
        if (sameTitles > 0)
            title = title + "(" + (sameTitles + 1) + ")";
        return title;
    }


    private int getSameTitles(String title) {
        int cnt = 0;
        CTabItem[] items = folder.getItems();
        for (int i = 0; i < items.length; i++) {
            if (items[i].getData() != null && items[i].getData().equals(title) && !items[i].equals(getCurrentTab()))
                cnt++;
        }
        return cnt;
    }


    public boolean closeAll() {
        boolean doit = true;
        for (int i = 0; i < folder.getItemCount(); i++) {
            CTabItem tab = folder.getItem(i);
            folder.setSelection(i);
            if (((IEditor) tab.getControl()).close()) {
                tab.dispose();
                i--;
            } else {
                doit = false;
            }
        }
        return doit;
    }


    public void updateLanguages() {
        for (int i = 0; i < folder.getItemCount(); i++) {
            CTabItem tab = folder.getItem(i);
            ((IEditor) tab.getControl()).updateLanguage();
        }
    }
}
