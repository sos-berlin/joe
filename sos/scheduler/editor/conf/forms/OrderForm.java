package sos.scheduler.editor.conf.forms;

import javax.xml.transform.TransformerException;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.VerifyEvent;
import org.eclipse.swt.events.VerifyListener;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;
import org.jdom.Element;
import org.jdom.JDOMException;

import sos.scheduler.editor.app.IUnsaved;
import sos.scheduler.editor.app.IUpdateLanguage;
import sos.scheduler.editor.app.MainWindow;
import sos.scheduler.editor.app.Messages;
import sos.scheduler.editor.app.Utils;
import sos.scheduler.editor.conf.ISchedulerUpdate;
import sos.scheduler.editor.conf.SchedulerDom;
import sos.scheduler.editor.conf.listeners.OrderListener;

public class OrderForm extends Composite implements IUnsaved, IUpdateLanguage {
    private OrderListener listener;

    private Group         group      = null;

    private SashForm      sashForm   = null;

    private Table         tParameter = null;

    private Button        bRemove    = null;

    private Label         label2     = null;

    private Text          tParaName  = null;

    private Label         label6     = null;

    private Text          tParaValue = null;

    private Button        bApply     = null;

    private Group         gOrder     = null;

    private Label         label10    = null;

    private Text          tTitle     = null;

    private Text          tState     = null;

    private Text          tPriority  = null;

    private Combo         cJobchain  = null;

    private Button        bReplace   = null;

    private Text          tOrderId   = null;

    private boolean       event      = false;


    public OrderForm(Composite parent, int style, SchedulerDom dom, Element order, ISchedulerUpdate main)
            throws JDOMException, TransformerException {
        super(parent, style);

        listener = new OrderListener(dom, order, main);
        initialize();
        setToolTipText();
        sashForm.setWeights(new int[] { 25, 75 });

        dom.setInit(true);

        cJobchain.setItems(listener.getJobChains());

        fillOrder();
        listener.fillParams(tParameter);
        dom.setInit(false);
        event = true;
    }


    public void apply() {
        if (isUnsaved())
            addParam();
    }


    public boolean isUnsaved() {
        return bApply.isEnabled();
    }


    private void initialize() {
        this.setLayout(new FillLayout());
        createGroup();
        setSize(new org.eclipse.swt.graphics.Point(723, 566));
    }


    /**
     * This method initializes group
     */
    private void createGroup() {
        GridLayout gridLayout2 = new GridLayout();
        gridLayout2.numColumns = 1;
        group = new Group(this, SWT.NONE);

        group.setLayout(gridLayout2);
        createSashForm();
    }


    /**
     * This method initializes group1
     */
    private void createGroup1() {
        GridLayout gridLayout3 = new GridLayout();
        gridLayout3.numColumns = 4;
        gOrder = new Group(sashForm, SWT.NONE);
        gOrder.setText("Order");
        gOrder.setLayout(gridLayout3);
        label10 = new Label(gOrder, SWT.NONE);
        label10.setLayoutData(new GridData());
        label10.setText("Order ID");

        tOrderId = new Text(gOrder, SWT.BORDER);
        tOrderId.addModifyListener(new org.eclipse.swt.events.ModifyListener() {
            public void modifyText(org.eclipse.swt.events.ModifyEvent e) {
                if (event) {
                    listener.setOrderId(tOrderId.getText(), true);
                    group.setText("Order: " + tOrderId.getText());
                }
            }
        });
        final GridData gridData_3 = new GridData(183, SWT.DEFAULT);
        tOrderId.setLayoutData(gridData_3);

        final Label jobchainLabel = new Label(gOrder, SWT.NONE);
        jobchainLabel.setLayoutData(new GridData(GridData.END, GridData.CENTER, false, false));
        jobchainLabel.setText("Job chain");

        cJobchain = new Combo(gOrder, SWT.NONE);
        cJobchain.addModifyListener(new org.eclipse.swt.events.ModifyListener() {
            public void modifyText(org.eclipse.swt.events.ModifyEvent e) {
                if (event)
                    listener.setCommandAttribute("job_chain", cJobchain.getText());
            }
        });

        final GridData gridData_1 = new GridData(GridData.FILL, GridData.CENTER, false, false);
        gridData_1.widthHint = 233;
        cJobchain.setLayoutData(gridData_1);

        final Label titleLabel = new Label(gOrder, SWT.NONE);
        titleLabel.setLayoutData(new GridData(GridData.END, GridData.BEGINNING, false, false));
        titleLabel.setText("Title");

        tTitle = new Text(gOrder, SWT.BORDER);
        tTitle.addModifyListener(new org.eclipse.swt.events.ModifyListener() {
            public void modifyText(org.eclipse.swt.events.ModifyEvent e) {
                if (event)
                    listener.setCommandAttribute("title", tTitle.getText());
            }
        });

        final GridData gridData_5 = new GridData(GridData.FILL, GridData.CENTER, false, false);
        gridData_5.widthHint = 150;
        tTitle.setLayoutData(gridData_5);

        final Label priorityLabel = new Label(gOrder, SWT.NONE);
        priorityLabel.setLayoutData(new GridData(GridData.END, GridData.CENTER, false, false));
        priorityLabel.setText("Priority");

        tPriority = new Text(gOrder, SWT.BORDER);
        tPriority.addVerifyListener(new VerifyListener() {
            public void verifyText(final VerifyEvent e) {
                e.doit = Utils.isOnlyDigits(e.text);
            }
        });
        tPriority.addModifyListener(new org.eclipse.swt.events.ModifyListener() {
            public void modifyText(org.eclipse.swt.events.ModifyEvent e) {
                if (event)
                    listener.setCommandAttribute("priority", tPriority.getText());
            }
        });
        tPriority.setLayoutData(new GridData(GridData.FILL, GridData.CENTER, false, false));

        final Label stateLabel = new Label(gOrder, SWT.NONE);
        stateLabel.setLayoutData(new GridData(GridData.END, GridData.BEGINNING, false, false));
        stateLabel.setText("State");

        tState = new Text(gOrder, SWT.BORDER);
        tState.addModifyListener(new org.eclipse.swt.events.ModifyListener() {
            public void modifyText(org.eclipse.swt.events.ModifyEvent e) {
                if (event)
                    listener.setCommandAttribute("state", tState.getText());
            }
        });
        final GridData gridData = new GridData(GridData.FILL, GridData.CENTER, false, false);
        gridData.widthHint = 150;
        tState.setLayoutData(gridData);

        final Label replaceLabel = new Label(gOrder, SWT.NONE);
        replaceLabel.setLayoutData(new GridData(GridData.END, GridData.CENTER, false, false));
        replaceLabel.setText("Replace");

        bReplace = new Button(gOrder, SWT.CHECK);
        bReplace.addSelectionListener(new SelectionAdapter() {
            public void widgetSelected(final SelectionEvent e) {
                String r = "no";
                if (bReplace.getSelection())
                    r = "yes";
                if (event)
                    listener.setCommandAttribute("replace", r);
            }
        });
        bReplace.setLayoutData(new GridData());

    }


    /**
     * This method initializes sashForm
     */
    private void createSashForm() {
        GridData gridData18 = new org.eclipse.swt.layout.GridData();
        gridData18.horizontalSpan = 1;
        gridData18.verticalAlignment = org.eclipse.swt.layout.GridData.FILL;
        gridData18.grabExcessHorizontalSpace = true;
        gridData18.grabExcessVerticalSpace = true;
        gridData18.horizontalAlignment = org.eclipse.swt.layout.GridData.FILL;
        sashForm = new SashForm(group, SWT.NONE);
        sashForm.setOrientation(org.eclipse.swt.SWT.VERTICAL);
        sashForm.setLayoutData(gridData18);
        createGroup1();
        createGroup2();

    }


    /**
     * This method initializes group2
     */
    private void createGroup2() {
        GridLayout gridLayout2 = new GridLayout();
        gridLayout2.numColumns = 1;

        final Group parameterGroup = new Group(sashForm, SWT.NONE);
        parameterGroup.setLayoutData(new GridData());
        parameterGroup.setText("Parameter");
        final GridLayout gridLayout = new GridLayout();
        gridLayout.numColumns = 6;
        parameterGroup.setLayout(gridLayout);

        final Label joborderIdLabel = new Label(parameterGroup, SWT.NONE);
        joborderIdLabel.setVisible(false);
        joborderIdLabel.setText("Order ID");
        label2 = new Label(parameterGroup, SWT.NONE);
        label2.setText("Name");
        tParaName = new Text(parameterGroup, SWT.BORDER);
        tParaName.setLayoutData(new GridData(103, SWT.DEFAULT));
        tParaName.addKeyListener(new org.eclipse.swt.events.KeyAdapter() {
            public void keyPressed(org.eclipse.swt.events.KeyEvent e) {
                if (e.keyCode == SWT.CR && !tParaName.equals(""))
                    addParam();
            }
        });
        tParaName.addModifyListener(new org.eclipse.swt.events.ModifyListener() {
            public void modifyText(org.eclipse.swt.events.ModifyEvent e) {
                bApply.setEnabled(!tParaName.getText().trim().equals(""));
            }
        });
        label6 = new Label(parameterGroup, SWT.NONE);
        label6.setText("Value");
        tParaValue = new Text(parameterGroup, SWT.BORDER);
        tParaValue.setLayoutData(new GridData(358, SWT.DEFAULT));
        tParaValue.addKeyListener(new org.eclipse.swt.events.KeyAdapter() {
            public void keyPressed(org.eclipse.swt.events.KeyEvent e) {
                if (e.keyCode == SWT.CR && !tParaName.getText().trim().equals(""))
                    addParam();
            }
        });
        tParaValue.addModifyListener(new org.eclipse.swt.events.ModifyListener() {
            public void modifyText(org.eclipse.swt.events.ModifyEvent e) {
                bApply.setEnabled(!tParaName.getText().equals(""));
            }
        });
        bApply = new Button(parameterGroup, SWT.NONE);
        bApply.setLayoutData(new GridData(GridData.FILL, GridData.CENTER, false, false));
        bApply.setText("&Apply");
        bApply.setEnabled(false);
        bApply.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
            public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
                addParam();
            }
        });
        new Label(parameterGroup, SWT.NONE);
        tParameter = new Table(parameterGroup, SWT.BORDER | SWT.FULL_SELECTION);
        final GridData gridData = new GridData(GridData.BEGINNING, GridData.FILL, false, true, 4, 1);
        gridData.widthHint = 522;
        tParameter.setLayoutData(gridData);
        tParameter.addPaintListener(new PaintListener() {
            public void paintControl(final PaintEvent e) {
            }
        });
        tParameter.setHeaderVisible(true);
        tParameter.setLinesVisible(true);
        tParameter.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
            public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
                TableItem item = (TableItem) e.item;
                if (item == null)
                    return;
                tParaName.setText(item.getText(0));
                tParaValue.setText(item.getText(1));
                bRemove.setEnabled(tParameter.getSelectionCount() > 0);
                bApply.setEnabled(false);
            }
        });
        TableColumn tcName = new TableColumn(tParameter, SWT.NONE);
        tcName.setWidth(100);
        tcName.setText("Name");
        TableColumn tcValue = new TableColumn(tParameter, SWT.NONE);
        tcValue.setWidth(420);
        tcValue.setText("Value");
        bRemove = new Button(parameterGroup, SWT.NONE);
        bRemove.setLayoutData(new GridData(GridData.BEGINNING, GridData.BEGINNING, false, true));
        bRemove.setText("Remove");
        bRemove.setEnabled(false);
        bRemove.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
            public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
                listener.deleteParameter(tParameter, tParameter.getSelectionIndex());
                tParaName.setText("");
                tParaValue.setText("");
                tParameter.deselectAll();
                bRemove.setEnabled(false);
                bApply.setEnabled(false);
            }
        });
    }


    private void addParam() {
        listener.saveParameter(tParameter, tParaName.getText().trim(), tParaValue.getText());

        tParaName.setText("");
        tParaValue.setText("");
        bRemove.setEnabled(false);
        bApply.setEnabled(false);
        tParameter.deselectAll();
        tParaName.setFocus();
    }


    // TODO addOrder() not used - remove it!!?
  /* private void addOrder() {
        String msg = "";
        if (cJobchain.getText().trim().equals("")) {
            msg = "A jobchain must be given for an order";
            cJobchain.setFocus();
        }

        if (!msg.equals("")) {
            MainWindow.message(msg, SWT.ICON_INFORMATION);
        } else {

            listener.setCommandAttribute("id", tOrderId.getText());
            if (bReplace.getSelection()) {
                listener.setCommandAttribute("replace", "yes");
            } else {
                listener.setCommandAttribute("replace", "no");
            }
            listener.setCommandAttribute("state", tState.getText());
            listener.setCommandAttribute("title", tTitle.getText());
            listener.setCommandAttribute("priority", tPriority.getText());
            listener.setCommandAttribute("job_chain", cJobchain.getText());
        }
    }
*/

    private void clearFields() {
        tOrderId.setText("");
        tPriority.setText("");
        cJobchain.setText("");
        tTitle.setText("");
        tState.setText("");
        bReplace.setSelection(true);
    }


    public void fillOrder() {
        clearFields();

        tOrderId.setText(listener.getCommandAttribute("id"));
        tTitle.setText(listener.getCommandAttribute("title"));
        tState.setText(listener.getCommandAttribute("state"));
        cJobchain.setText(listener.getCommandAttribute("job_chain"));
        tPriority.setText(listener.getCommandAttribute("priority"));
        bReplace.setSelection(listener.getCommandReplace());

    }


    public void setToolTipText() {
        tTitle.setToolTipText(Messages.getTooltip("jobcommand.title"));
        tPriority.setToolTipText(Messages.getTooltip("jobcommand.priority"));
        tState.setToolTipText(Messages.getTooltip("jobcommand.state"));
        bReplace.setToolTipText(Messages.getTooltip("jobcommand.replaceorder"));
        cJobchain.setToolTipText(Messages.getTooltip("jobcommand.jobchain"));
        tOrderId.setToolTipText(Messages.getTooltip("order.order_id"));
        tParaName.setToolTipText(Messages.getTooltip("job.param.name"));
        tParaValue.setToolTipText(Messages.getTooltip("job.param.value"));
        bRemove.setToolTipText(Messages.getTooltip("job.param.btn_remove"));
        bApply.setToolTipText(Messages.getTooltip("job.param.btn_add"));
        tParameter.setToolTipText(Messages.getTooltip("jobcommand.param.table"));

    }
} // @jve:decl-index=0:visual-constraint="10,10"
