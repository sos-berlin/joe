package com.sos.jobdoc;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.jdom.Element;

import com.sos.jobdoc.forms.NoteForm;

import sos.scheduler.editor.app.Options;
import sos.scheduler.editor.app.ResourceManager;
import sos.scheduler.editor.app.Utils;

public class NoteDialog extends Dialog {
    private Shell    _shell;

    private Image    _image;

    private int      _shellStyle = SWT.CLOSE | SWT.TITLE | SWT.APPLICATION_MODAL | SWT.RESIZE;

    private Point    _size       = new Point(450, 350);

    private NoteForm _fNote      = null;
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
            e.printStackTrace();
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
        // if (_image != null)
        // _image.dispose();
    }

    //Textfeld soll beim verlassen diese Dialogs aktualiert werden
    /*public void setUpdateText(org.eclipse.swt.widgets.Text txt) {
       refreshText;
    }*/

    public void setTooltip(String string) {
        _fNote.setToolTipText(string);
    }
}
