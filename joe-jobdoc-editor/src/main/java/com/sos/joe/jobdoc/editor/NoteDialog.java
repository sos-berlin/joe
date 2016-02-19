package com.sos.joe.jobdoc.editor;

import org.apache.log4j.Logger;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.jdom.Element;
import com.sos.joe.xml.Utils;
import com.sos.joe.globals.misc.ResourceManager;
import com.sos.joe.globals.options.Options;
import com.sos.joe.jobdoc.editor.forms.NoteForm;
import com.sos.joe.xml.jobdoc.DocumentationDom;

public class NoteDialog extends Dialog {

    private static final Logger LOGGER = Logger.getLogger(NoteDialog.class);
    private Shell _shell;
    private Image _image;
    private int _shellStyle = SWT.CLOSE | SWT.TITLE | SWT.APPLICATION_MODAL | SWT.RESIZE;
    private Point _size = new Point(450, 350);
    private NoteForm _fNote = null;
    org.eclipse.swt.widgets.Text refreshText = null;

    public NoteDialog(Shell parent, String title) {
        super(parent);
        init(title);
    }

    private void init(String title) {
        Shell parent = getParent();
        _shell = new Shell(parent, _shellStyle);
        _shell.setVisible(false);
        _shell.setText(getText());
        _shell.setSize(_size);
        Options.loadWindow(_shell, "note");
        _shell.addShellListener(new org.eclipse.swt.events.ShellAdapter() {

            public void shellClosed(org.eclipse.swt.events.ShellEvent e) {
                Options.saveWindow(_shell, "note");
                e.doit = Utils.applyFormChanges(_fNote);
            }
        });
        try {
            _image = ResourceManager.getImageFromResource("/sos/scheduler/editor/editor.png");
            _shell.setImage(_image);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            return;
        }
        setDialog(title);
    }

    private void setDialog(String title) {
        final FillLayout fillLayout = new FillLayout();
        _shell.setLayout(fillLayout);
        _fNote = new NoteForm(_shell, SWT.NONE);
        _fNote.setTitle(title);
    }

    public void setParams(DocumentationDom dom, Element parent, String name, boolean optional, boolean changeStatus) {
        _fNote.setParams(dom, parent, name, optional, changeStatus);
    }

    public void open() {
        _shell.open();
        Display display = _shell.getDisplay();
        while (!_shell.isDisposed()) {
            if (!display.readAndDispatch())
                display.sleep();
        }
        if (_fNote != null)
            _fNote.dispose();
    }

    public void setTooltip(String string) {
        _fNote.setToolTipText(string);
    }
}
