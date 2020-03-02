package com.sos.joe.globals.messages;

import java.io.File;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CCombo;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.events.FocusAdapter;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.program.Program;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Spinner;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeColumn;

import com.sos.dialog.classes.SOSGroup;
import com.sos.dialog.classes.SOSMsgControl;
import com.sos.dialog.components.IntegerField;
import com.sos.dialog.components.SOSDateTime;
import com.sos.joe.globals.options.Options;

public class SOSMsgJOE extends SOSMsgControl {

    public SOSMsgJOE(String pstrMessageCode) {
        super(pstrMessageCode);
        if (getMessages() == null) {
            super.setMessageResource("JOEMessages");
        }
    }

    @Override
    public SOSMsgJOE newMsg(final String pstrMessageCode) {
        return new SOSMsgJOE(pstrMessageCode);
    }

    @Override
    public Text control(final Text pobjC) {
        pobjC.setToolTipText(tooltip());
        setKeyListener(pobjC);
        pobjC.addFocusListener(new FocusAdapter() {

            @Override
            public void focusGained(final FocusEvent e) {
                pobjC.selectAll();
            }

            @Override
            public void focusLost(FocusEvent e) {
                //
            }
        });
        return pobjC;
    }

    @Override
    public IntegerField integerField(final IntegerField integerField) {
        integerField.setToolTipText(tooltip());
        setKeyListener(integerField);
        integerField.addFocusListener(new FocusAdapter() {

            @Override
            public void focusGained(final FocusEvent e) {
                integerField.selectAll();
            }

            @Override
            public void focusLost(FocusEvent e) {
                //
            }
        });
        return integerField;
    }

    @Override
    public Label control(final Label pobjC) {
        pobjC.setText(label());
        pobjC.setToolTipText(this.tooltip());
        setKeyListener(pobjC);
        return pobjC;
    }

    public SOSGroup Control(final SOSGroup pobjC) {
        pobjC.setText(caption());
        pobjC.setToolTipText(tooltip());
        setKeyListener(pobjC);
        return pobjC;
    }

    @Override
    public Group control(final Group pobjC) {
        pobjC.setText(caption());
        pobjC.setToolTipText(tooltip());
        setKeyListener(pobjC);
        return pobjC;
    }

    @Override
    public Button control(final Button pobjC) {
        pobjC.setText(caption());
        pobjC.setToolTipText(tooltip());
        setKeyListener(pobjC);
        return pobjC;
    }

    @Override
    public Combo control(final Combo pobjC) {
        pobjC.setText(caption());
        pobjC.setToolTipText(tooltip());
        setKeyListener(pobjC);
        return pobjC;
    }

    @Override
    public Composite control(final Composite pobjC) {
        pobjC.setToolTipText(tooltip());
        setKeyListener(pobjC);
        return pobjC;
    }

    @Override
    public CCombo control(final CCombo pobjC) {
        pobjC.setToolTipText(tooltip());
        setKeyListener(pobjC);
        return pobjC;
    }

    @Override
    public TableColumn control(final TableColumn pobjC) {
        pobjC.setText(caption());
        pobjC.setToolTipText(tooltip());
        return pobjC;
    }

    @Override
    public Table control(final Table pobjC) {
        pobjC.setToolTipText(tooltip());
        setKeyListener(pobjC);
        return pobjC;
    }

    @Override
    public FileDialog control(final FileDialog pobjC) {
        pobjC.setText(caption());
        return pobjC;
    }

    @Override
    public Spinner control(final Spinner pobjC) {
        pobjC.setToolTipText(tooltip());
        setKeyListener(pobjC);
        return pobjC;
    }

    @Override
    public MessageBox control(final MessageBox pobjC) {
        pobjC.setMessage(this.caption());
        return pobjC;
    }

    @Override
    public List control(final List pobjC) {
        pobjC.setToolTipText(tooltip());
        setKeyListener(pobjC);
        return pobjC;
    }

    @Override
    public Tree control(final Tree pobjC) {
        pobjC.setToolTipText(this.tooltip());
        setKeyListener(pobjC);
        return pobjC;
    }

    @Override
    public TreeColumn control(final TreeColumn pobjC) {
        pobjC.setText(caption());
        pobjC.setToolTipText(tooltip());
        return pobjC;
    }

    @Override
    public TabItem control(final TabItem pobjC) {
        pobjC.setText(caption());
        pobjC.setToolTipText(this.tooltip());
        return pobjC;
    }

    @Override
    public CTabItem control(final CTabItem pobjC) {
        pobjC.setText(caption());
        pobjC.setToolTipText(this.tooltip());
        return pobjC;
    }

    @Override
    public SOSDateTime control(final SOSDateTime pobjC) {
        pobjC.setToolTipText(this.tooltip());
        return pobjC;
    }

    private void setKeyListener(final Control pobjC) {
        pobjC.addKeyListener(new KeyListener() {

            @Override
            public void keyPressed(KeyEvent event) {
                if (event.keyCode == SWT.F1) {
                    openHelp(getF1());
                }
            }

            @Override
            public void keyReleased(KeyEvent arg0) {
                //
            }
        });
    }

    @Override
    public void openHelp(String helpKey) {
        String lang = Options.getLanguage();
        String url = helpKey;
        try {
            if (url.contains("http:")) {
            } else {
                url = new File(url).toURL().toString();
            }
            Program prog = Program.findProgram("html");
            if (prog != null) {
                prog.execute(url);
            } else {
                Runtime.getRuntime().exec(Options.getBrowserExec(url, lang));
            }
        } catch (Exception e) {
            new ErrorLog("error in " + sos.util.SOSClassUtil.getMethodName() + "; " + com.sos.joe.globals.messages.Messages.getString(
                    "MainListener.cannot_open_help", new String[] { url, lang, e.getMessage() }), e);
            ErrorLog.message(com.sos.joe.globals.messages.Messages.getString("MainListener.cannot_open_help", new String[] { url, lang, e
                    .getMessage() }), SWT.ICON_ERROR | SWT.OK);
        }
    }

}