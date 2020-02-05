package sos.scheduler.editor.conf.composites;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.events.HelpEvent;
import org.eclipse.swt.events.HelpListener;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.VerifyEvent;
import org.eclipse.swt.events.VerifyListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import sos.scheduler.editor.app.MainWindow;
import sos.scheduler.editor.app.SchedulerEditorFontDialog;
import sos.scheduler.editor.app.Utils;
import sos.scheduler.editor.conf.listeners.JOEListener;

import com.sos.joe.globals.messages.Messages;

/** @author Uwe Risse */
public class TextArea extends StyledText {

    private static final Logger LOGGER = LoggerFactory.getLogger(TextArea.class);
    private String strTagName = "job";
    private String strAttributeName = "";
    private enuSourceTypes enuWhatSourceType = TextArea.enuSourceTypes.ScriptSource;
    boolean flgInit = false;
    private JOEListener objDataProvider = null;

    public static enum enuSourceTypes {
        ScriptSource, MonitorSource, xmlSource, xmlComment, JobDocu;
    }

    public StyledText getControl() {
        return this;
    }

    public void setSourceType(final enuSourceTypes penuWhatSourceType) {
        enuWhatSourceType = penuWhatSourceType;
    }

    public void setDataProvider(final JOEListener pobjDataProvider, final enuSourceTypes penuWhatSourceType) {
        setSourceType(penuWhatSourceType);
        setDataProvider(pobjDataProvider);
    }

    public void setDataProvider(final JOEListener pobjDataProvider) {
        objDataProvider = pobjDataProvider;
        refreshContent();
        createContextMenue();
    }

    private void createContextMenue() {
        Menu objContextMenu = getMenu();
        if (objContextMenu == null) {
            objContextMenu = new Menu(this.getControl());
        }
        MenuItem itemCopy = new MenuItem(objContextMenu, SWT.PUSH);
        itemCopy.addListener(SWT.Selection, getCopyListener());
        itemCopy.setText("Copy");
        MenuItem itemCut = new MenuItem(objContextMenu, SWT.PUSH);
        itemCut.addListener(SWT.Selection, getCutListener());
        itemCut.setText("Cut");
        MenuItem itemPaste = new MenuItem(objContextMenu, SWT.PUSH);
        itemPaste.addListener(SWT.Selection, getPasteListener());
        itemPaste.setText("Paste");
        MenuItem itemSelectAll = new MenuItem(objContextMenu, SWT.PUSH);
        itemSelectAll.addListener(SWT.Selection, getSelectAllListener());
        itemSelectAll.setText("Select all");
        new MenuItem(objContextMenu, SWT.SEPARATOR);
        MenuItem itemStartExternalEditor = new MenuItem(objContextMenu, SWT.PUSH);
        itemStartExternalEditor.addListener(SWT.Selection, getStartExternalEditorListener());
        itemStartExternalEditor.setText("Start external Editor");
        setMenu(objContextMenu);
        MenuItem itemSelectFont = new MenuItem(objContextMenu, SWT.PUSH);
        itemSelectFont.addListener(SWT.Selection, getSelectFontListener());
        itemSelectFont.setText("Select Font");
    }

    private Listener getStartExternalEditorListener() {
        return new Listener() {

            public void handleEvent(Event e) {
                LOGGER.info("'External Editor' was pressed....");
                startExternalEditor();
            }
        };
    }

    public void startExternalEditor() {
        String text = getText();
        if (enuWhatSourceType != enuSourceTypes.ScriptSource) {
            String strT = "job";
            if (enuWhatSourceType == enuSourceTypes.MonitorSource) {
                strT = "monitor";
            }
            String lan = "function_" + strT + "_" + objDataProvider.getLanguage(objDataProvider.getLanguage()) + "_";
            text = Utils.showClipboard(text, getShell(), true, "", true, lan, false);
        } else {
            text = Utils.showClipboard(text, getShell(), true, "");
        }
        if (text != null) {
            setText(text);
        }
    }

    private Listener getSelectFontListener() {
        return new Listener() {

            public void handleEvent(Event e) {
                LOGGER.info("'Select Font' was pressed....");
                changeFont();
            }
        };
    }

    private Listener getCopyListener() {
        return new Listener() {

            public void handleEvent(Event e) {
                LOGGER.info("ReadFile was pressed....");
                _copy();
            }
        };
    }

    private Listener getPasteListener() {
        return new Listener() {

            public void handleEvent(Event e) {
                LOGGER.info("ReadFile was pressed....");
                _paste();
            }
        };
    }

    private Listener getCutListener() {
        return new Listener() {

            public void handleEvent(Event e) {
                LOGGER.info("ReadFile was pressed....");
                _cut();
            }
        };
    }

    private Listener getSelectAllListener() {
        return new Listener() {

            public void handleEvent(Event e) {
                LOGGER.info("ReadFile was pressed....");
                _selectAll();
            }
        };
    }

    private void _copy() {
        this.copy();
    }

    private void _paste() {
        this.paste();
    }

    private void _cut() {
        this.cut();
    }

    private void _selectAll() {
        this.selectAll();
    }

    public void refreshContent() {
        flgInit = true;
        switch (enuWhatSourceType) {
        case ScriptSource:
            setText(objDataProvider.getSource());
            strTagName = "job";
            strAttributeName = "script";
            break;
        case xmlSource:
            setText(objDataProvider.getXML());
            strTagName = "job";
            strAttributeName = "script";
            break;
        case MonitorSource:
            setText(objDataProvider.getSource());
            strTagName = "job";
            strAttributeName = "Monitor";
            break;
        case xmlComment:
            setText(objDataProvider.getComment());
            strTagName = "job";
            strAttributeName = "comment";
            break;
        case JobDocu:
            setText(objDataProvider.getDescription());
            strTagName = "job";
            strAttributeName = "documentation";
            break;
        default:
            break;
        }
        SchedulerEditorFontDialog objFontDialog = new SchedulerEditorFontDialog(getFont().getFontData()[0], getForeground().getRGB());
        objFontDialog.readFontData();
        setFont(objFontDialog.getFontData(), objFontDialog.getForeGround());
        flgInit = false;
    }

    public TextArea(Composite pobjComposite, int arg1) {
        super(pobjComposite, arg1);
        addVerifyListener(new VerifyListener() {

            public void verifyText(final VerifyEvent e) {
                //
            }
        });
        addMouseListener(new MouseListener() {

            @Override
            public void mouseUp(MouseEvent event) {
                if (event.button == 3) {
                    LOGGER.info("button2");
                }
            }

            @Override
            public void mouseDown(MouseEvent arg0) {
                //
            }

            @Override
            public void mouseDoubleClick(MouseEvent arg0) {
                //
            }
        });
        addHelpListener(new HelpListener() {

            @Override
            public void helpRequested(HelpEvent objHelpEvent) {
                MainWindow.message(Messages.getString("OrderJob.Help"), SWT.ICON_INFORMATION);
            }
        });
        addKeyListener(new KeyAdapter() {

            public void keyPressed(final KeyEvent e) {
                if (objDataProvider.Check4HelpKey(e.keyCode, strTagName, strAttributeName)) {
                    e.doit = false;
                    return;
                }
                e.doit = true;
                return;
            }
        });
        final GridData gridData_1 = new GridData(GridData.FILL, GridData.FILL, true, true, 4, 1);
        gridData_1.minimumHeight = 40;
        gridData_1.widthHint = 454;
        gridData_1.heightHint = 139;
        setLayoutData(gridData_1);
        addModifyListener(new ModifyListener() {

            public void modifyText(ModifyEvent e) {
                if (!flgInit) {
                    switch (enuWhatSourceType) {
                    case ScriptSource:
                        objDataProvider.setSource(getText());
                        break;
                    case MonitorSource:
                        objDataProvider.setSource(getText());
                        break;
                    case xmlSource:
                        break;
                    case xmlComment:
                        objDataProvider.setComment(getText());
                        break;
                    case JobDocu:
                        objDataProvider.setDescription(getText());
                        break;
                    default:
                        break;
                    }
                }
            }
        });
    }

    public void setFont(FontData f, RGB foreGround) {
        setFont(new Font(this.getDisplay(), f));
        setForeground(new Color(this.getDisplay(), foreGround));
    }

    public void changeFont() {
        SchedulerEditorFontDialog fd = new SchedulerEditorFontDialog(getFont().getFontData()[0], getForeground().getRGB());
        fd.setParent(getShell());
        fd.show(getDisplay());
        setFont(fd.getFontData(), fd.getForeGround());
    }

    protected void checkSubclass() {
        // Disable the check that prevents subclassing of SWT components
    }

}