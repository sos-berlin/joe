package sos.scheduler.editor.conf.forms;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.FocusAdapter;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.VerifyEvent;
import org.eclipse.swt.events.VerifyListener;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

import sos.scheduler.editor.app.IUpdateLanguage;
import sos.scheduler.editor.app.Messages;
import sos.scheduler.editor.conf.ISchedulerUpdate;
import sos.scheduler.editor.conf.SchedulerDom;
import sos.scheduler.editor.conf.listeners.ScheduleListener;
import org.jdom.Element;

import com.sos.dialog.components.SOSDateTime;

import sos.scheduler.editor.app.Utils;

public class ScheduleForm extends Composite implements IUpdateLanguage {

    private ScheduleListener listener = null;
    private Group scheduleGroup = null;
    private SchedulerDom dom = null;
    private Text txtName = null;
    private boolean init = true;
    private Text txtTitle = null;
    private SOSDateTime validFromDate = null;
    private SOSDateTime validToDate = null;
    private SOSDateTime validFromTime = null;
    private SOSDateTime validToTime = null;
    private Combo cboCombo = null;

    public ScheduleForm(Composite parent, int style, SchedulerDom dom, org.jdom.Element schedule_, ISchedulerUpdate update) {
        super(parent, style);
        try {
            init = true;
            this.dom = dom;
            listener = new ScheduleListener(dom, update, schedule_);
            initialize();
            setToolTipText();
            init = false;

        } catch (Exception e) {
            try {
                new sos.scheduler.editor.app.ErrorLog("error in " + sos.util.SOSClassUtil.getMethodName(), e);
            } catch (Exception ee) {
                // tu nichts
            }
            System.err.println("..error in ScheduleForm.init() " + e.getMessage());
        }
    }

    private void initialize() {
        try {
            this.setLayout(new FillLayout());
            createGroup();

            txtName.setText(listener.getName());
            txtTitle.setText(listener.getTitle());

            String d = listener.getValidFrom();
            validFromDate.setDate(d);
            validFromTime.setTime(d);

            d = listener.getValidTo();
            validToDate.setDate(d);
            validToTime.setTime(d);

            String[] s = listener.getAllSchedules();
            if (s != null)
                cboCombo.setItems(s);

            cboCombo.setText(listener.getSubstitute());

            setSize(new org.eclipse.swt.graphics.Point(656, 400));
            txtName.setFocus();
        } catch (Exception e) {
            try {
                new sos.scheduler.editor.app.ErrorLog("error in " + sos.util.SOSClassUtil.getMethodName(), e);
            } catch (Exception ee) {
                // tu nichts
            }
            System.err.println("..error in ScheduleForm.initialize() " + e.getMessage());
        }
    }

    /**
     * This method initializes group
     */
    private void createGroup() {
        try {
            GridLayout gridLayout = new GridLayout();
            gridLayout.numColumns = 3;
            scheduleGroup = new Group(this, SWT.NONE);
            scheduleGroup.setText("Schedule");
            scheduleGroup.setLayout(gridLayout);

            final Label nameLabel = new Label(scheduleGroup, SWT.NONE);
            nameLabel.setText("Name");

            txtName = new Text(scheduleGroup, SWT.BORDER);
            txtName.addFocusListener(new FocusAdapter() {
                public void focusGained(final FocusEvent e) {
                    txtName.selectAll();
                }
            });
            txtName.addVerifyListener(new VerifyListener() {
                public void verifyText(final VerifyEvent e) {
                    if (!init)// während der initialiserung sollen keine
                              // überprüfungen stattfinden
                        e.doit = Utils.checkElement(txtName.getText(), dom, sos.scheduler.editor.app.Editor.SCHEDULE, null);
                }
            });
            txtName.addModifyListener(new ModifyListener() {
                public void modifyText(final ModifyEvent e) {
                    if (!init && !existScheduleName())
                        listener.setName(txtName.getText());
                }
            });
            txtName.setLayoutData(new GridData(GridData.FILL, GridData.CENTER, false, false, 2, 1));

            final Label titleLabel = new Label(scheduleGroup, SWT.NONE);
            titleLabel.setLayoutData(new GridData(GridData.BEGINNING, GridData.BEGINNING, false, false));
            titleLabel.setText("Title");

            txtTitle = new Text(scheduleGroup, SWT.BORDER);
            txtTitle.addFocusListener(new FocusAdapter() {
                public void focusGained(final FocusEvent e) {
                    txtTitle.selectAll();
                }
            });
            txtTitle.addModifyListener(new ModifyListener() {
                public void modifyText(final ModifyEvent e) {
                    if (!init)
                        listener.setTitle(txtTitle.getText());
                }
            });
            txtTitle.setLayoutData(new GridData(GridData.FILL, GridData.CENTER, false, false, 2, 1));

            final Label substitueLabel = new Label(scheduleGroup, SWT.NONE);
            substitueLabel.setText("Substitute");

            cboCombo = new Combo(scheduleGroup, SWT.NONE);
            cboCombo.addModifyListener(new ModifyListener() {
                public void modifyText(final ModifyEvent e) {
                    if (!init)
                        listener.setSubstitut(cboCombo.getText());
                }
            });
            cboCombo.setLayoutData(new GridData(GridData.FILL, GridData.CENTER, false, false, 2, 1));

            Label validFromLabel = new Label(scheduleGroup, SWT.NONE);
            validFromLabel.setLayoutData(new GridData(GridData.FILL, GridData.CENTER, false, false));
            validFromLabel.setText("Valid From");

            validFromDate = new SOSDateTime(scheduleGroup, SWT.BORDER | SWT.DATE | SWT.DROP_DOWN);

            validFromDate.addSelectionListener(new SelectionAdapter() {
                public void widgetSelected(SelectionEvent e) {
                    setValidDateFrom();
                }
            });

            validFromTime = new SOSDateTime(scheduleGroup, SWT.BORDER | SWT.TIME | SWT.DROP_DOWN);

            validFromTime.addSelectionListener(new SelectionAdapter() {
                public void widgetSelected(SelectionEvent e) {
                    setValidDateFrom();
                }
            });

            
            

            final Label validToLabel = new Label(scheduleGroup, SWT.NONE);
            validToLabel.setText("Valid To");

            validToDate = new SOSDateTime(scheduleGroup, SWT.BORDER | SWT.DATE | SWT.DROP_DOWN);
            validToDate.addSelectionListener(new SelectionAdapter() {
                public void widgetSelected(SelectionEvent e) {
                    setValidDateTo();
                }
            });
            
            validToTime = new SOSDateTime(scheduleGroup, SWT.BORDER | SWT.TIME | SWT.DROP_DOWN);
            validToTime.addSelectionListener(new SelectionAdapter() {
                public void widgetSelected(SelectionEvent e) {
                    setValidDateTo();
                }
            });

            
        } catch (Exception e) {
            try {
                new sos.scheduler.editor.app.ErrorLog("error in " + sos.util.SOSClassUtil.getMethodName(), e);
            } catch (Exception ee) {
                // tu nichts
            }
            System.err.println("..error in ScheduleForm.createGroup() " + e.getMessage());
        }
    }

    public void setToolTipText() {
        txtName.setToolTipText(Messages.getTooltip("schedule.name"));
        txtTitle.setToolTipText(Messages.getTooltip("schedule.title"));
        validFromDate.setToolTipText(Messages.getTooltip("schedule.valid_from"));
        validToDate.setToolTipText(Messages.getTooltip("schedule.valid_to"));
        cboCombo.setToolTipText(Messages.getTooltip("schedule.subtitute"));
    }

    private boolean existScheduleName() {
        boolean retVal = false;
        if (!dom.isLifeElement()) {
            if (listener.getSchedule() != null && listener.getSchedule().getParentElement() != null) {
                Element parent = listener.getSchedule().getParentElement();
                java.util.List l = parent.getChildren("schedule");
                for (int i = 0; i < l.size(); i++) {
                    Element el = (Element) l.get(i);
                    if (Utils.getAttributeValue("name", el).equals(txtName.getText())) {
                        retVal = true;
                        break;
                    }
                }
            }
        }
        if (retVal)
            txtName.setBackground(Display.getCurrent().getSystemColor(SWT.COLOR_YELLOW));
        else
            txtName.setBackground(null);
        return retVal;
    }

    private void setValidDateTo() {
        if (!init) {
            try {
                listener.setValidTo(validToDate.getISODate() + " " + validToTime.getISOTime());
            } catch (Exception es) {
                es.printStackTrace();
            }
        }
    }

    private void setValidDateFrom() {
        if (!init) {
            try {
                listener.setValidFrom(validFromDate.getISODate()  + " " + validFromTime.getISOTime());
            } catch (Exception es) {
                es.printStackTrace();
            }
        }
    }

} // @jve:decl-index=0:visual-constraint="10,10"
