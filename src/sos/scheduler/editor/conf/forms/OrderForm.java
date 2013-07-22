package sos.scheduler.editor.conf.forms;


import java.io.File;

import javax.xml.transform.TransformerException;

import org.apache.log4j.Logger;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.FocusAdapter;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.VerifyEvent;
import org.eclipse.swt.events.VerifyListener;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.jdom.Element;
import org.jdom.JDOMException;

import sos.scheduler.editor.app.ContextMenu;
import sos.scheduler.editor.app.Editor;
import sos.scheduler.editor.app.ErrorLog;
import sos.scheduler.editor.app.IUnsaved;
import sos.scheduler.editor.app.IUpdateLanguage;
import sos.scheduler.editor.app.MainWindow;
import sos.scheduler.editor.app.ResourceManager;
import sos.scheduler.editor.app.SOSJOEMessageCodes;
import sos.scheduler.editor.app.Utils;
import sos.scheduler.editor.classes.SOSComboBox;
import sos.scheduler.editor.conf.ISchedulerUpdate;
import sos.scheduler.editor.conf.SchedulerDom;
import sos.scheduler.editor.conf.listeners.OrderListener;
import sos.util.SOSClassUtil;

import com.sos.i18n.annotation.I18NMsg;
import com.swtdesigner.SWTResourceManager;

public class OrderForm extends SOSJOEMessageCodes implements IUnsaved, IUpdateLanguage {

	@SuppressWarnings("unused")
	private final Logger			logger			= Logger.getLogger(OrderForm.class);
	public final String				conSVNVersion	= "$Id$";

    private Button              butDetails                  = null;
    private OrderListener       listener                    = null;
    private Group               group                       = null;
    private Group               gOrder                      = null;
    private Label               label10                     = null;
    private Text                tTitle                      = null;
    private SOSComboBox               tState                      = null;
    private Text                tPriority                   = null;
    private SOSComboBox               cJobchain                   = null;
    private Text                tOrderId                    = null;
    private boolean             event                       = false;
    private SchedulerDom        dom                         = null;
    private ISchedulerUpdate    main                        = null;
    private Element             order                       = null;
    private Button              butGoto                     = null;
    private SOSComboBox               cboEndState                 = null;
    private SOSComboBox               cboStates                   = null;
    private String              xmlDetailsConfigFilename    = null;
    private Button              butRemove                   = null;

    @I18NMsg private final String JOE_L_JOB_CHAIN = "JOE_L_JOB_CHAIN"; // "Job chain";

    @I18NMsg private final String JOE_L_Title_order = "JOE_L_Title_order"; // "Title";
    @I18NMsg private final String JOE_L_Order = "JOE_L_Order"; // ""Order"";
    @I18NMsg private final String JOE_L_OrderId = "JOE_L_OrderId"; // "Order ID";

    public OrderForm(final Composite parent, final int style, final SchedulerDom _dom, final Element _order, final ISchedulerUpdate _main) throws JDOMException, TransformerException {

        super(parent, style);
        dom = _dom;
        main = _main;
        order = _order;
        listener = new OrderListener(dom, order, main);
        initialize();
        setToolTipText();
        dom.setInit(true);
        cJobchain.setItems(listener.getJobChains());
        fillOrder();
        dom.setInit(false);
        existDetailsConfigurationsFile();
        event = true;
        this.setEnabled(Utils.isElementEnabled("commands", dom, order));
    }

    @Override
	public void apply() {

        // if (isUnsaved())
        // addParam();
    }

    @Override
	public boolean isUnsaved() {
        return false;
        // return bApply.isEnabled();
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
        group = new Group(this, SWT.NONE);
        group.setLayout(gridLayout2);
        GridLayout gridLayout3 = new GridLayout();
        gridLayout3.numColumns = 7;

        gOrder = JOE_G_OrderForm_Order.Control(new Group(group, SWT.NONE));
        gOrder.setLayoutData(new GridData(GridData.FILL, GridData.CENTER, true, false));
        gOrder.setLayout(gridLayout3);

        label10 = JOE_L_OrderForm_OrderID.Control(new Label(gOrder, SWT.NONE));
        label10.setLayoutData(new GridData(GridData.BEGINNING, GridData.CENTER, false, false, 2, 1));

        tOrderId = JOE_T_OrderForm_OrderID.Control(new Text(gOrder, SWT.BORDER));
        tOrderId.setBackground(Display.getCurrent().getSystemColor(SWT.COLOR_WHITE));
        tOrderId.addModifyListener(new org.eclipse.swt.events.ModifyListener() {
            @Override
			public void modifyText(final org.eclipse.swt.events.ModifyEvent e) {
                /*if (event) {
                    listener.setOrderId(tOrderId.getText(), true, !checkName());
                    group.setText("Order: " + tOrderId.getText());
                }
                 */
                if (event) {
                    if (checkName()) {
                        listener.setCommandAttribute("id", tOrderId.getText());
                        existDetailsConfigurationsFile();
                    }
                }
            }
        });
        tOrderId.setLayoutData(new GridData(GridData.FILL, GridData.FILL, true, false, 5, 1));

        @SuppressWarnings("unused")
        final Label jobchainLabel = JOE_L_OrderForm_JobChain.Control(new Label(gOrder, SWT.NONE));

        butGoto = JOE_B_JobChainNodes_Goto.Control(new Button(gOrder, SWT.ARROW | SWT.DOWN));
        butGoto.setVisible(dom != null && !dom.isLifeElement());
        butGoto.addSelectionListener(new SelectionAdapter() {
            @Override
			public void widgetSelected(final SelectionEvent e) {
                ContextMenu.goTo(cJobchain.getText(), dom, Editor.JOB_CHAIN);
            }
        });
        butGoto.setAlignment(SWT.RIGHT);

        final Composite composite = new Composite(gOrder, SWT.NONE);
        composite.setLayoutData(new GridData(GridData.FILL, GridData.FILL, true, false, 5, 1));
        final GridLayout gridLayout = new GridLayout();
        gridLayout.verticalSpacing = 0;
        gridLayout.marginWidth = 0;
        gridLayout.marginHeight = 0;
        gridLayout.horizontalSpacing = 0;
        composite.setLayout(gridLayout);

        cJobchain = new SOSComboBox(composite, JOE_Cbo_OrderForm_JobChain);
        cJobchain.setMenu(new ContextMenu(cJobchain, dom, Editor.JOB_CHAIN).getMenu());
        cJobchain.setLayoutData(new GridData(GridData.FILL, GridData.CENTER, true, false));
        cJobchain.addModifyListener(new org.eclipse.swt.events.ModifyListener() {
            @Override
			public void modifyText(final org.eclipse.swt.events.ModifyEvent e) {
                if (event)
                    if (checkName()) {
                        listener.setCommandAttribute("job_chain", cJobchain.getText());
                        String curstate = listener.getCommandAttribute("state");
                        tState.setItems(listener.getStates());
                        tState.setText(curstate);

                        cboStates.setItems(listener.getStates());
//                        cboStates.add("global");
//                        cboStates.setText("global");
                        cboStates.add(JOE_M_OrderForm_Global.label());
                        cboStates.setText(JOE_M_OrderForm_Global.label());

                        String curEndstate = listener.getCommandAttribute("end_state");
                        cboEndState.setItems(listener.getStates());
                        cboEndState.setText(curEndstate);
                        butDetails.setEnabled(cJobchain.getText().length() > 0);
                        cboStates.setEnabled(cJobchain.getText().length() > 0);
                        existDetailsConfigurationsFile();
                    }
            }
        });
        /*Button butBrowse = new Button(gOrder, SWT.NONE);
        butBrowse.setLayoutData(new GridData(GridData.END, GridData.CENTER, false, false));
        butBrowse.addSelectionListener(new SelectionAdapter() {
            public void widgetSelected(final SelectionEvent e) {
                String jobname = IOUtils.openDirectoryFile(MergeAllXMLinDirectory.MASK_JOB_CHAIN);
                if(jobname != null && jobname.length() > 0) {
                    cJobchain.setText(jobname);
                }
            }
        });
        butBrowse.setText("Browse");
         */

        final Label titleLabel = JOE_L_OrderForm_Title.Control(new Label(gOrder, SWT.NONE));
        final GridData gridData_6 = new GridData(GridData.BEGINNING, GridData.CENTER, false, false, 2, 1);
        gridData_6.widthHint = 47;
        titleLabel.setLayoutData(gridData_6);

        tTitle = JOE_T_OrderForm_Title.Control(new Text(gOrder, SWT.BORDER));
        tTitle.addModifyListener(new org.eclipse.swt.events.ModifyListener() {
            @Override
			public void modifyText(final org.eclipse.swt.events.ModifyEvent e) {
                if (event)
                    listener.setCommandAttribute("title", tTitle.getText());
            }
        });
        tTitle.setLayoutData(new GridData(GridData.FILL, GridData.CENTER, true, false, 5, 1));

        final Label priorityLabel = JOE_L_OrderForm_Priority.Control(new Label(gOrder, SWT.NONE));
        priorityLabel.setLayoutData(new GridData(GridData.BEGINNING, GridData.CENTER, false, false, 2, 1));

        tPriority = JOE_T_OrderForm_Priority.Control(new Text(gOrder, SWT.BORDER));
        tPriority.addVerifyListener(new VerifyListener() {
            @Override
			public void verifyText(final VerifyEvent e) {
                e.doit = Utils.isOnlyDigits(e.text);
            }
        });
        tPriority.addModifyListener(new org.eclipse.swt.events.ModifyListener() {
            @Override
			public void modifyText(final org.eclipse.swt.events.ModifyEvent e) {
                if (event)
                    listener.setCommandAttribute("priority", tPriority.getText());
            }
        });
        tPriority.setLayoutData(new GridData(GridData.FILL, GridData.CENTER, true, false, 5, 1));

        final Label stateLabel = JOE_L_OrderForm_State.Control(new Label(gOrder, SWT.NONE));
        stateLabel.setLayoutData(new GridData(GridData.BEGINNING, GridData.CENTER, false, false, 2, 1));

        tState = new SOSComboBox(gOrder, JOE_T_OrderForm_State);
        tState.addModifyListener(new org.eclipse.swt.events.ModifyListener() {
            @Override
			public void modifyText(final org.eclipse.swt.events.ModifyEvent e) {
                if (event)
                    listener.setCommandAttribute("state", tState.getText());
            }
        });
        tState.setLayoutData(new GridData(GridData.FILL, GridData.CENTER, true, false, 5, 1));

        final Label endStateLabel = JOE_L_OrderForm_EndState.Control(new Label(gOrder, SWT.NONE));
        endStateLabel.setLayoutData(new GridData(GridData.BEGINNING, GridData.CENTER, false, false, 2, 1));

        cboEndState = new SOSComboBox(gOrder, JOE_Cbo_OrderForm_EndState);
        cboEndState.addModifyListener(new ModifyListener() {
            @Override
			public void modifyText(final ModifyEvent e) {
                if (event)
                    listener.setCommandAttribute("end_state", cboEndState.getText());
            }
        });
        cboEndState.setLayoutData(new GridData(GridData.FILL, GridData.CENTER, true, false, 5, 1));

        new Label(gOrder, SWT.NONE);
        new Label(gOrder, SWT.NONE);

        @SuppressWarnings("unused")
        final Label stateLabel_1 = JOE_L_OrderForm_State.Control(new Label(gOrder, SWT.NONE));

        cboStates = new SOSComboBox(gOrder, JOE_Cbo_OrderForm_State2);
        cboStates.setLayoutData(new GridData(GridData.FILL, GridData.CENTER, true, false));

        butRemove = JOE_B_OrderForm_Remove.Control(new Button(gOrder, SWT.NONE));
        butRemove.addFocusListener(new FocusAdapter() {
            @Override
			public void focusGained(final FocusEvent e) {
                existDetailsConfigurationsFile();
            }
        });
        butRemove.addSelectionListener(new SelectionAdapter() {
            @Override
			public void widgetSelected(final SelectionEvent e) {
                if (xmlDetailsConfigFilename != null && xmlDetailsConfigFilename.length() > 0 && new File(xmlDetailsConfigFilename).exists()) {

//                  int ok = MainWindow.message(Messages.getString("detailform.remove_state"), SWT.ICON_QUESTION | SWT.YES | SWT.NO | SWT.CANCEL);
                    int ok = MainWindow.message(JOE_M_OrderForm_RemoveState.label(), SWT.ICON_QUESTION | SWT.YES | SWT.NO | SWT.CANCEL);
                    if (ok == SWT.YES) {
                        if (!new File(xmlDetailsConfigFilename).delete())
                            MainWindow.message(JOE_M_OrderForm_RemoveFailed.params(xmlDetailsConfigFilename), SWT.ICON_INFORMATION);
                    }
                    existDetailsConfigurationsFile();
                }
            }
        });
        butRemove.setImage(ResourceManager.getImageFromResource("/sos/scheduler/editor/icon_delete.gif"));

        butDetails = JOE_B_JobChainForm_Parameter.Control(new Button(gOrder, SWT.NONE));
        butDetails.addFocusListener(new FocusAdapter() {
            @Override
			public void focusGained(final FocusEvent e) {
                existDetailsConfigurationsFile();
            }
        });
        butDetails.addSelectionListener(new SelectionAdapter() {
            @Override
			public void widgetSelected(final SelectionEvent e) {
                // DetailForm dialogForm =new DetailForm(composite, SWT.NONE, cJobchain.getText(), tState.getText(), null,
                // Editor.JOB_CHAINS, null, null, dom.isLifeElement(), dom.getFilename());
                // DetailDialogForm detail = new DetailDialogForm(cJobchain.getText(), tState.getText(), tOrderId.getText(),
                // dom.isLifeElement() || dom.isDirectory(), dom.getFilename());
                String state = cboStates.getText().length() == 0 || cboStates.getText().equals("global") ? null : cboStates.getText();
                DetailDialogForm detail = new DetailDialogForm(cJobchain.getText(), state, tOrderId.getText(), dom.isLifeElement() || dom.isDirectory(),
                        dom.getFilename());
                detail.showDetails();
                detail.getDialogForm().setParamsForWizzard(dom, main);
            }
        });
        butDetails.setLayoutData(new GridData(GridData.END, GridData.CENTER, false, false));

        createSashForm();
    }

    /**
     * This method initializes group1
     */
    private void createGroup1() {

        // listener.setCommandAttribute("replace", "yes");

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

        if (!dom.isLifeElement()) {
        }

        // new ParameterForm(dom, order, main, group, Editor.ORDER);

        createGroup1();
        createGroup2();

    }

    /**
     * This method initializes group2
     */
    private void createGroup2() {
        GridLayout gridLayout2 = new GridLayout();
        gridLayout2.numColumns = 1;
    }

    private void clearFields() {
        tOrderId.setText("");
        tPriority.setText("");
        cJobchain.setText("");
        tTitle.setText("");
        tState.setText("");
        tState.removeAll();
        cboEndState.setText("");
        cboEndState.removeAll();

    }

    public void fillOrder() {
        clearFields();

        tOrderId.setText(listener.getCommandAttribute("id"));
        tTitle.setText(listener.getCommandAttribute("title"));
        tState.setItems(listener.getStates());
        tState.setText(listener.getCommandAttribute("state"));
        cboEndState.setItems(listener.getStates());
        cboEndState.setText(listener.getCommandAttribute("end_state"));
        cJobchain.setText(listener.getCommandAttribute("job_chain"));
        tPriority.setText(listener.getCommandAttribute("priority"));

        cboStates.setItems(listener.getStates());

//        cboStates.add("global");
//        cboStates.setText("global");
        cboStates.add(JOE_M_OrderForm_Global.label());
        cboStates.setText(JOE_M_OrderForm_Global.label());

        butDetails.setEnabled(cJobchain.getText().length() > 0);
        cboStates.setEnabled(cJobchain.getText().length() > 0);
        checkName();

    }

    @Override
	public void setToolTipText() {
//
    }

    private boolean checkName() {
        if (listener.existName(tOrderId.getText() + "," + cJobchain.getText())) {
            tOrderId.setBackground(Display.getCurrent().getSystemColor(SWT.COLOR_YELLOW));
            cJobchain.setBackground(Display.getCurrent().getSystemColor(SWT.COLOR_YELLOW));
            return false;

        }
        else
            if (tOrderId.getText() == null || tOrderId.getText().length() == 0) {
                tOrderId.setBackground(Display.getCurrent().getSystemColor(SWT.COLOR_YELLOW));
                return false;
            }
            else {
                tOrderId.setBackground(null);
                cJobchain.setBackground(null);
                return true;
            }

    }

    private void existDetailsConfigurationsFile() {

        /*sos.scheduler.editor.conf.listeners.DetailsListener detailListener =
            new sos.scheduler.editor.conf.listeners.DetailsListener(cJobchain.getText(),
                    cboStates.getText(),
                    tOrderId.getText(),
                    Editor.JOB_CHAINS,
                    null,
                    dom.isLifeElement() || dom.isDirectory(),
                    dom.getFilename());
        xmlDetailsConfigFilename = detailListener.getConfigurationFilename();
         */
        try {
            String path = dom.getFilename();
            String xmlPaths = "";
            String orderId = tOrderId.getText();
            if (path != null && path.length() > 0) {
                File f = new File(path);
                if (f.isFile())
                    xmlPaths = f.getParent();
                else
                    xmlPaths = path;
            }
            else {
                xmlPaths = sos.scheduler.editor.app.Options.getSchedulerData();
                xmlPaths = xmlPaths.endsWith("/") || xmlPaths.endsWith("\\") ? xmlPaths + "config/" : xmlPaths.concat("/config/");
            }

            String _currOrderId = orderId != null && orderId.length() > 0 ? "," + orderId : "";
            xmlDetailsConfigFilename = new File(xmlPaths, cJobchain.getText() + _currOrderId + ".config.xml").getCanonicalPath();

            if (xmlDetailsConfigFilename != null && xmlDetailsConfigFilename.trim().length() > 0 && new File(xmlDetailsConfigFilename).exists()) {
                FontData fontDatas[] = this.getFont().getFontData();
                FontData fdata = fontDatas[0];
//                Font font = new Fo nt(Display.getCurrent(), fdata.getName(), fdata.getHeight(), SWT.BOLD);
                Font font = SWTResourceManager.getFont(fdata.getName(), fdata.getHeight(), SWT.BOLD);
                butDetails.setFont(font);
                butRemove.setEnabled(true);
            }
            else {
                FontData fontDatas[] = this.getFont().getFontData();
                FontData fdata = fontDatas[0];
//                Font font = new Fo nt(Display.getCurrent(), fdata.getName(), fdata.getHeight(), SWT.NORMAL);
                Font font = SWTResourceManager.getFont(fdata.getName(), fdata.getHeight(), SWT.NORMAL);
                butDetails.setFont(font);
                butRemove.setEnabled(false);
            }

        }
        catch (Exception e) {
            try {
//              System.out.println("..error in " + SOSClassUtil.getMethodName() + ": " + e.getMessage());
                System.out.println(JOE_E_0002.params(SOSClassUtil.getMethodName()) + ": " + e.getMessage());
//              new ErrorLog("error in " + SOSClassUtil.getMethodName(), e);
                new ErrorLog(JOE_E_0002.params(SOSClassUtil.getMethodName()), e);
            }
            catch (Exception ee) {
            }
        }
    }
} // @jve:decl-index=0:visual-constraint="10,10"