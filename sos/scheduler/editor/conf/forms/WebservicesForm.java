package sos.scheduler.editor.conf.forms;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CCombo;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.VerifyEvent;
import org.eclipse.swt.events.VerifyListener;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;
import org.jdom.Element;

import sos.scheduler.editor.app.IUnsaved;
import sos.scheduler.editor.app.IUpdateLanguage;
import sos.scheduler.editor.app.MainWindow;
import sos.scheduler.editor.app.Messages;
import sos.scheduler.editor.app.Utils;
import sos.scheduler.editor.conf.SchedulerDom;
import sos.scheduler.editor.conf.listeners.WebservicesListener;

public class WebservicesForm extends Composite implements IUnsaved, IUpdateLanguage {
    private static final String GROUP_WEB_SERVICE = "Web Service";

    private WebservicesListener listener;

    private Group               group             = null;

    private Table               tServices         = null;

    private Group               gWebService       = null;

    private Button              bRemove           = null;

    private Button              bApply            = null;

    private Button              bNew              = null;

    private Label               label             = null;

    private Text                tName             = null;

    private Label               label1            = null;

    private Text                tURL              = null;

    private Label               label2            = null;

    private CCombo              cChain            = null;

    private Label               label3            = null;

    private Text                sTimeout          = null;

    private Label               label5            = null;

    private Button              bDebug            = null;

    private Label               label7            = null;

    private Label               label13           = null;

    private Label               label19           = null;

    private Text                tRequest          = null;

    private Text                tForward          = null;

    private Text                tResponse         = null;

    private Group               group1            = null;

    private Table               tParams           = null;

    private Label               label4            = null;

    private Text                tParaName         = null;

    private Label               label9            = null;

    private Text                tParaValue        = null;

    private Button              bApplyPara        = null;

    private Button              bRemovePara       = null;

    private Label               label6            = null;

    private Label               label8            = null;

    private Label               label10           = null;


    public WebservicesForm(Composite parent, int style, SchedulerDom dom, Element config) {
        super(parent, style);
        listener = new WebservicesListener(dom, config);
        initialize();
        setToolTipText();

        listener.fillTable(tServices);
        cChain.setItems(listener.getJobChains());
    }


    public void apply() {
        if (isUnsaved())

            applyService();

    }


    public boolean isUnsaved() {
        return bApply.isEnabled();
    }


    private void initialize() {
        this.setLayout(new FillLayout());
        createGroup();
        setSize(new org.eclipse.swt.graphics.Point(653, 468));
    }


    /**
     * This method initializes group
     */
    private void createGroup() {
        GridData gridData19 = new org.eclipse.swt.layout.GridData();
        gridData19.horizontalAlignment = org.eclipse.swt.layout.GridData.FILL;
        gridData19.verticalAlignment = org.eclipse.swt.layout.GridData.CENTER;
        GridData gridData18 = new org.eclipse.swt.layout.GridData();
        gridData18.horizontalSpan = 2;
        gridData18.verticalAlignment = org.eclipse.swt.layout.GridData.CENTER;
        gridData18.heightHint = 10;
        gridData18.horizontalAlignment = org.eclipse.swt.layout.GridData.FILL;
        GridData gridData5 = new org.eclipse.swt.layout.GridData();
        gridData5.horizontalAlignment = org.eclipse.swt.layout.GridData.FILL;
        gridData5.verticalAlignment = org.eclipse.swt.layout.GridData.BEGINNING;
        GridData gridData4 = new org.eclipse.swt.layout.GridData(GridData.FILL, GridData.BEGINNING, false, false, 1, 2);
        GridData gridData3 = new org.eclipse.swt.layout.GridData();
        gridData3.horizontalAlignment = org.eclipse.swt.layout.GridData.FILL;
        gridData3.verticalSpan = 1;
        gridData3.verticalAlignment = org.eclipse.swt.layout.GridData.BEGINNING;
        GridLayout gridLayout = new GridLayout();
        gridLayout.numColumns = 2;
        group = new Group(this, SWT.NONE);
        group.setText("Web Services");
        createGroup1();
        bApply = new Button(group, SWT.NONE);

        // final Label PPPP = new Label(group, SWT.NONE);
        // PPPP.setText("label");
        createGroup12();
        new Label(group, SWT.NONE);
        new Label(group, SWT.NONE);
        label6 = new Label(group, SWT.SEPARATOR | SWT.HORIZONTAL);
        label6.setText("Label");
        label6.setLayoutData(gridData18);
        createTable();
        group.setLayout(gridLayout);
        bNew = new Button(group, SWT.NONE);
        label8 = new Label(group, SWT.SEPARATOR | SWT.HORIZONTAL);
        label8.setText("Label");
        label8.setLayoutData(gridData19);
        bRemove = new Button(group, SWT.NONE);
        bRemove.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
            public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
                if (tServices.getSelectionCount() > 0) {
                    int index = tServices.getSelectionIndex();
                    listener.removeService(index);
                    tServices.remove(index);
                    if (index >= tServices.getItemCount())
                        index--;
                    if (tServices.getItemCount() > 0) {
                        tServices.select(index);
                        listener.selectService(index);
                        setInput(true);
                    } else
                        setInput(false);
                }
                bRemove.setEnabled(tServices.getSelectionCount() > 0);
            }
        });
        bApply.setText("&Apply Web Service");
        bApply.setLayoutData(gridData4);
        bApply.setEnabled(false);
        bRemove.setEnabled(false);
        bRemove.setLayoutData(gridData3);
        bRemove.setText("Remove Web Service");
        bApply.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
            public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {

                applyService();

            }
        });
        bNew.setText("&New Web Service");
        bNew.setLayoutData(gridData5);
        getShell().setDefaultButton(bNew);

        bNew.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
            public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
                listener.newService();
                setInput(true);
                tName.setFocus();

            }
        });
    }


    /**
     * This method initializes table
     */
    private void createTable() {
        GridData gridData1 = new org.eclipse.swt.layout.GridData();
        gridData1.verticalSpan = 3;
        gridData1.verticalAlignment = org.eclipse.swt.layout.GridData.FILL;
        gridData1.grabExcessHorizontalSpace = true;
        gridData1.grabExcessVerticalSpace = true;
        gridData1.horizontalAlignment = org.eclipse.swt.layout.GridData.FILL;
        tServices = new Table(group, SWT.BORDER | SWT.FULL_SELECTION);
        tServices.setHeaderVisible(true);
        tServices.setLayoutData(gridData1);
        tServices.setLinesVisible(true);
        tServices.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
            public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
                boolean selection = tServices.getSelectionCount() > 0;
                bRemove.setEnabled(selection);
                if (selection) {
                    listener.selectService(tServices.getSelectionIndex());
                    setInput(true);
                    sTimeout.setEnabled(!cChain.getText().equals(""));
                    tRequest.setEnabled(!sTimeout.getEnabled());
                    tResponse.setEnabled(!sTimeout.getEnabled());
                    tForward.setEnabled(!sTimeout.getEnabled());
                }
            }
        });
        TableColumn tableColumn = new TableColumn(tServices, SWT.NONE);
        tableColumn.setWidth(150);
        tableColumn.setText("Name");
        TableColumn tableColumn1 = new TableColumn(tServices, SWT.NONE);
        tableColumn1.setWidth(150);
        tableColumn1.setText("URL");
        TableColumn tableColumn2 = new TableColumn(tServices, SWT.NONE);
        tableColumn2.setWidth(100);
        tableColumn2.setText("Job Chain");
    }


    /**
     * This method initializes group1
     */
    private void createGroup1() {
        GridData gridData12 = new org.eclipse.swt.layout.GridData();
        gridData12.horizontalAlignment = org.eclipse.swt.layout.GridData.FILL;
        gridData12.verticalAlignment = org.eclipse.swt.layout.GridData.CENTER;
        GridData gridData11 = new org.eclipse.swt.layout.GridData();
        gridData11.widthHint = 50;
        GridData gridData10 = new org.eclipse.swt.layout.GridData();
        gridData10.horizontalAlignment = org.eclipse.swt.layout.GridData.FILL;
        gridData10.grabExcessHorizontalSpace = true;
        gridData10.horizontalSpan = 3;
        gridData10.verticalAlignment = org.eclipse.swt.layout.GridData.CENTER;
        GridData gridData9 = new org.eclipse.swt.layout.GridData();
        gridData9.horizontalAlignment = org.eclipse.swt.layout.GridData.FILL;
        gridData9.grabExcessHorizontalSpace = true;
        gridData9.verticalAlignment = org.eclipse.swt.layout.GridData.CENTER;
        GridData gridData8 = new org.eclipse.swt.layout.GridData();
        gridData8.horizontalSpan = 5;
        gridData8.verticalAlignment = org.eclipse.swt.layout.GridData.CENTER;
        gridData8.grabExcessHorizontalSpace = true;
        gridData8.horizontalAlignment = org.eclipse.swt.layout.GridData.FILL;
        GridData gridData6 = new org.eclipse.swt.layout.GridData();
        gridData6.horizontalSpan = 5;
        gridData6.verticalAlignment = org.eclipse.swt.layout.GridData.CENTER;
        gridData6.grabExcessHorizontalSpace = true;
        gridData6.horizontalAlignment = org.eclipse.swt.layout.GridData.FILL;
        GridLayout gridLayout1 = new GridLayout();
        gridLayout1.numColumns = 6;
        GridData gridData = new org.eclipse.swt.layout.GridData(GridData.FILL, GridData.CENTER, true, false);
        gWebService = new Group(group, SWT.NONE);
        gWebService.setText("Web Service");
        gWebService.setLayout(gridLayout1);
        gWebService.setLayoutData(gridData);
        label = new Label(gWebService, SWT.NONE);
        label.setText("Name:");
        tName = new Text(gWebService, SWT.BORDER);
        tName.setEnabled(false);
        tName.setLayoutData(gridData9);
        tName.addModifyListener(new org.eclipse.swt.events.ModifyListener() {
            public void modifyText(org.eclipse.swt.events.ModifyEvent e) {
                getShell().setDefaultButton(null);
                boolean valid = listener.isValid(tName.getText());
                if (valid)
                    tName.setBackground(null);
                else
                    tName.setBackground(Display.getCurrent().getSystemColor(SWT.COLOR_YELLOW));

                valid = (valid && !tName.getText().equals("") && !tURL.getText().equals(""));
                if (valid) {
                    getShell().setDefaultButton(bApply);
                }
                bApply.setEnabled(valid);
                gWebService.setText(GROUP_WEB_SERVICE + ": " + tName.getText());
            }
        });
        label1 = new Label(gWebService, SWT.NONE);
        label1.setText("URL:");
        tURL = new Text(gWebService, SWT.BORDER);
        tURL.addSelectionListener(new SelectionAdapter() {
            public void widgetDefaultSelected(final SelectionEvent e) {
            }


            public void widgetSelected(final SelectionEvent e) {
            }
        });
        tURL.setEnabled(false);
        tURL.setLayoutData(gridData10);
        tURL.addModifyListener(new org.eclipse.swt.events.ModifyListener() {
            public void modifyText(org.eclipse.swt.events.ModifyEvent e) {
                if ((tURL.getText().length() > 0) && (tURL.getText().charAt(0) != '/')) {
                    tURL.setText("/" + tURL.getText());
                    tURL.setSelection(2);
                }
                boolean valid = (!tName.getText().equals("") && !tURL.getText().equals(""));
                if (valid) {
                    getShell().setDefaultButton(bApply);
                }
                bApply.setEnabled(valid);
            }
        });
        label2 = new Label(gWebService, SWT.NONE);
        label2.setText("Job Chain:");
        cChain = new CCombo(gWebService, SWT.BORDER);
        cChain.addSelectionListener(new SelectionAdapter() {
            public void widgetSelected(final SelectionEvent e) {
            }
        });
        cChain.setEditable(true);
        cChain.setEnabled(false);
        cChain.setLayoutData(gridData12);
        cChain.addModifyListener(new org.eclipse.swt.events.ModifyListener() {
            public void modifyText(org.eclipse.swt.events.ModifyEvent e) {

                boolean valid = (!tName.getText().equals(""));
                bApply.setEnabled(valid);
                if (valid) {
                    getShell().setDefaultButton(bApply);
                }
                sTimeout.setEnabled(!cChain.getText().equals(""));
                tRequest.setEnabled(!sTimeout.getEnabled());
                tResponse.setEnabled(!sTimeout.getEnabled());
                tForward.setEnabled(!sTimeout.getEnabled());
            }
        });
        label3 = new Label(gWebService, SWT.NONE);
        label3.setText("Timeout:");
        sTimeout = new Text(gWebService, SWT.BORDER);
        sTimeout.addVerifyListener(new VerifyListener() {
            public void verifyText(final VerifyEvent e) {
                e.doit = Utils.isOnlyDigits(e.text);
            }
        });
        sTimeout.setEnabled(false);
        sTimeout.setLayoutData(gridData11);
        sTimeout.addModifyListener(new org.eclipse.swt.events.ModifyListener() {
            public void modifyText(org.eclipse.swt.events.ModifyEvent e) {
                boolean valid = (!tName.getText().equals(""));
                if (valid) {
                    getShell().setDefaultButton(bApply);
                }
                bApply.setEnabled(valid);
            }
        });
        label5 = new Label(gWebService, SWT.NONE);
        label5.setText("Debug:");
        bDebug = new Button(gWebService, SWT.CHECK);
        bDebug.setEnabled(false);
        bDebug.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
            public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
                boolean valid = (!tName.getText().equals(""));
                if (valid) {
                    getShell().setDefaultButton(bApply);
                }
                bApply.setEnabled(valid);
            }
        });
        label7 = new Label(gWebService, SWT.NONE);
        label7.setText("Request XSLT:");
        tRequest = new Text(gWebService, SWT.BORDER);
        tRequest.setEnabled(false);
        tRequest.setLayoutData(gridData8);
        tRequest.addModifyListener(new org.eclipse.swt.events.ModifyListener() {
            public void modifyText(org.eclipse.swt.events.ModifyEvent e) {
                boolean valid = (!tName.getText().equals(""));
                if (valid) {
                    getShell().setDefaultButton(bApply);
                }
                bApply.setEnabled(valid);
                cChain.setEnabled(tRequest.getText().equals(""));
            }
        });
        label19 = new Label(gWebService, SWT.NONE);
        label19.setText("Response XSLT:");
        tResponse = new Text(gWebService, SWT.BORDER);
        tResponse.setEnabled(false);
        tResponse.setLayoutData(gridData6);
        tResponse.addModifyListener(new org.eclipse.swt.events.ModifyListener() {
            public void modifyText(org.eclipse.swt.events.ModifyEvent e) {
                boolean valid = (!tName.getText().equals(""));
                if (valid) {
                    getShell().setDefaultButton(bApply);
                }
                bApply.setEnabled(valid);
                cChain.setEnabled(tResponse.getText().equals(""));
            }
        });
        label13 = new Label(gWebService, SWT.NONE);
        label13.setLayoutData(new GridData());
        label13.setText("Forward XSLT:");
        GridData gridData7 = new org.eclipse.swt.layout.GridData(GridData.FILL, GridData.CENTER, true, false, 5, 1);
        tForward = new Text(gWebService, SWT.BORDER);
        tForward.setEnabled(false);
        tForward.setLayoutData(gridData7);
        tForward.addModifyListener(new org.eclipse.swt.events.ModifyListener() {
            public void modifyText(org.eclipse.swt.events.ModifyEvent e) {
                boolean valid = (!tName.getText().equals(""));
                if (valid) {
                    getShell().setDefaultButton(bApply);
                }
                bApply.setEnabled(valid);
            }
        });
    }


    /**
     * This method initializes group1
     */
    private void createGroup12() {
        GridData gridData20 = new org.eclipse.swt.layout.GridData();
        gridData20.horizontalSpan = 5;
        gridData20.verticalAlignment = org.eclipse.swt.layout.GridData.CENTER;
        gridData20.horizontalAlignment = org.eclipse.swt.layout.GridData.FILL;
        GridData gridData17 = new org.eclipse.swt.layout.GridData();
        gridData17.horizontalAlignment = org.eclipse.swt.layout.GridData.FILL;
        gridData17.grabExcessHorizontalSpace = true;
        gridData17.verticalAlignment = org.eclipse.swt.layout.GridData.CENTER;
        GridData gridData16 = new org.eclipse.swt.layout.GridData();
        gridData16.horizontalIndent = 45;
        gridData16.horizontalAlignment = org.eclipse.swt.layout.GridData.FILL;
        gridData16.grabExcessHorizontalSpace = true;
        gridData16.verticalAlignment = org.eclipse.swt.layout.GridData.CENTER;
        GridData gridData15 = new org.eclipse.swt.layout.GridData();
        gridData15.horizontalAlignment = org.eclipse.swt.layout.GridData.FILL;
        gridData15.verticalAlignment = org.eclipse.swt.layout.GridData.CENTER;
        GridData gridData14 = new org.eclipse.swt.layout.GridData();
        gridData14.horizontalAlignment = org.eclipse.swt.layout.GridData.FILL;
        gridData14.verticalAlignment = org.eclipse.swt.layout.GridData.BEGINNING;
        GridLayout gridLayout2 = new GridLayout();
        gridLayout2.numColumns = 5;
        GridData gridData2 = new org.eclipse.swt.layout.GridData(GridData.FILL, GridData.FILL, true, true, 1, 4);
        group1 = new Group(group, SWT.NONE);
        group1.setText("Parameters");
        label4 = new Label(group1, SWT.NONE);
        label4.setText("Name:");
        tParaName = new Text(group1, SWT.BORDER);
        label9 = new Label(group1, SWT.NONE);
        label9.setText("Value:");
        tParaValue = new Text(group1, SWT.BORDER);
        bApplyPara = new Button(group1, SWT.NONE);
        label10 = new Label(group1, SWT.SEPARATOR | SWT.HORIZONTAL);
        label10.setText("Label");
        label10.setLayoutData(gridData20);
        createTable2();
        group1.setLayout(gridLayout2);
        group1.setLayoutData(gridData2);
        bRemovePara = new Button(group1, SWT.NONE);
        bRemovePara.setText("Remove");
        bRemovePara.setEnabled(false);
        bRemovePara.setLayoutData(gridData14);
        bRemovePara.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
            public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
                if (tParams.getSelectionCount() > 0) {
                    tParams.remove(tParams.getSelectionIndex());
                    tParams.deselectAll();
                    tParaName.setText("");
                    tParaValue.setText("");
                    bApplyPara.setEnabled(false);
                }
                bRemovePara.setEnabled(false);
                boolean valid = (!tName.getText().equals(""));
                if (valid) {
                    getShell().setDefaultButton(bApply);
                }
                bApply.setEnabled(valid);
            }
        });
        tParaName.setEnabled(false);
        tParaName.setLayoutData(gridData16);
        tParaName.setText("");
        tParaName.addModifyListener(new org.eclipse.swt.events.ModifyListener() {
            public void modifyText(org.eclipse.swt.events.ModifyEvent e) {
                bApplyPara.setEnabled(!tParaName.getText().equals(""));
            }
        });
        tParaName.addKeyListener(new org.eclipse.swt.events.KeyAdapter() {
            public void keyPressed(org.eclipse.swt.events.KeyEvent e) {
                if (e.keyCode == SWT.CR && !tParaName.getText().equals(""))
                    applyParam();
            }
        });
        tParaValue.setEnabled(false);
        tParaValue.setLayoutData(gridData17);
        tParaValue.addModifyListener(new org.eclipse.swt.events.ModifyListener() {
            public void modifyText(org.eclipse.swt.events.ModifyEvent e) {
                bApplyPara.setEnabled(!tParaName.getText().equals(""));
            }
        });
        tParaValue.addKeyListener(new org.eclipse.swt.events.KeyAdapter() {
            public void keyPressed(org.eclipse.swt.events.KeyEvent e) {
                if (e.keyCode == SWT.CR && !tParaName.getText().equals(""))
                    applyParam();
            }
        });
        bApplyPara.setText("A&pply");
        bApplyPara.setLayoutData(gridData15);
        bApplyPara.setEnabled(false);
        bApplyPara.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
            public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
                applyParam();
            }
        });
    }


    /**
     * This method initializes table
     */
    private void createTable2() {
        GridData gridData13 = new org.eclipse.swt.layout.GridData(GridData.FILL, GridData.FILL, true, true, 3, 1);
        gridData13.horizontalIndent = 45;
        new Label(group1, SWT.NONE);
        tParams = new Table(group1, SWT.BORDER | SWT.FULL_SELECTION);
        tParams.setHeaderVisible(true);
        tParams.setEnabled(false);
        tParams.setLayoutData(gridData13);
        tParams.setLinesVisible(true);
        tParams.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
            public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
                if (tParams.getSelectionCount() > 0) {
                    TableItem item = tParams.getItem(tParams.getSelectionIndex());
                    tParaName.setText(item.getText(0));
                    tParaValue.setText(item.getText(1));
                    bApplyPara.setEnabled(false);
                    tParaName.setFocus();
                }
                bRemovePara.setEnabled(tParams.getSelectionCount() > 0);
            }
        });
        TableColumn tableColumn3 = new TableColumn(tParams, SWT.NONE);
        tableColumn3.setWidth(125);
        tableColumn3.setText("Name");
        TableColumn tableColumn4 = new TableColumn(tParams, SWT.NONE);
        tableColumn4.setWidth(200);
        tableColumn4.setText("Value");
    }


    private void applyService() {
        boolean found = false;
        boolean exist = false;
        TableItem[] services = tServices.getItems();
        int sel = tServices.getSelectionIndex();
        for (int i = 0; i < services.length; i++) {
            String url = services[i].getText(1);
            String name = services[i].getText(0);

            if (url.equals(tURL.getText()) && sel != i) {
                found = true;
            }
            if (name.equals(tName.getText()) && sel != i) {
                exist = true;
            }
        }

        if (found) {
            MainWindow.message("URL-path already defined", SWT.ICON_INFORMATION);
            tURL.setFocus();
        } else {

            if (!tRequest.getText().equals("") && tResponse.getText().equals("")) {
                MainWindow.message("Please set value for Response XSLT", SWT.ICON_INFORMATION);
                tResponse.setFocus();
            } else {
                if (tRequest.getText().equals("") && !tResponse.getText().equals("")) {
                    MainWindow.message("Please set value for Request XSLT", SWT.ICON_INFORMATION);
                    tRequest.setFocus();
                } else {
                    if (exist) {
                        MainWindow.message(tName.getText() + " already exists", SWT.ICON_INFORMATION);
                        tName.setFocus();
                    } else {
                        if (tURL.getText().equals("")) {
                            MainWindow.message("Url-Path must not be empty", SWT.ICON_INFORMATION);
                        } else {
                            if (Utils.str2int(sTimeout.getText()) == 0 && sTimeout.getText().length() > 0) {
                                MainWindow.message("Timeout must not be 0", SWT.ICON_INFORMATION);

                            } else {
                                listener.applyService(bDebug.getSelection(), cChain.getText(), tName.getText(),
                                        tForward.getText(), tRequest.getText(), tResponse.getText(),
                                        sTimeout.getText(), tURL.getText(), tParams.getItems());
                                listener.fillTable(tServices);
                                setInput(false);
                                getShell().setDefaultButton(bNew);
                            }
                        }
                    }
                }
            }
        }
    }


    private void applyParam() {
        String name = tParaName.getText();
        String value = tParaValue.getText();
        TableItem[] items = tParams.getItems();
        boolean found = false;
        for (int i = 0; i < items.length; i++) {
            if (items[i].getText(0).equals(name)) {
                items[i].setText(1, value);
                found = true;
            }
        }
        if (!found) {
            TableItem item = new TableItem(tParams, SWT.NONE);
            item.setText(new String[] { name, value });
        }

        tParams.deselectAll();
        tParaName.setText("");
        tParaValue.setText("");
        bRemovePara.setEnabled(false);
        bApplyPara.setEnabled(false);
        boolean valid = (!tName.getText().equals(""));
        if (valid) {
            getShell().setDefaultButton(bApply);
        }
        bApply.setEnabled(valid);
        tParaName.setFocus();

    }


    private void setInput(boolean enabled) {

        tParaName.setText("");
        tParaValue.setText("");
        listener.fillParams(tParams);

        if (enabled) {
            bDebug.setSelection(listener.getDebug());
            int index = listener.getChainIndex(listener.getJobChain());
            if (index != -1) {
                cChain.select(index);
            }
            tName.setText(listener.getName());
            cChain.setText(listener.getJobChain());
            tForward.setText(listener.getForwardXSLT());
            tRequest.setText(listener.getRequestXSLT());
            tResponse.setText(listener.getResponseXSLT());
            sTimeout.setText(Utils.getIntegerAsString(Utils.str2int(listener.getTimeout())));
            tURL.setText(listener.getURL());
            gWebService.setText(GROUP_WEB_SERVICE + ": " + listener.getName());
            tName.setFocus();
        } else {
            tName.setText("");
            cChain.select(-1);
            tForward.setText("");
            tRequest.setText("");
            tResponse.setText("");
            sTimeout.setText("");
            tURL.setText("");
            gWebService.setText(GROUP_WEB_SERVICE);
        }
        bDebug.setEnabled(enabled);
        cChain.setEnabled(enabled);
        tName.setEnabled(enabled);
        tForward.setEnabled(enabled);
        tRequest.setEnabled(enabled);
        tResponse.setEnabled(enabled);
        sTimeout.setEnabled(enabled);
        tURL.setEnabled(enabled);

        tParams.setEnabled(enabled);
        tParaName.setEnabled(enabled);
        tParaValue.setEnabled(enabled);
        bApply.setEnabled(false);
        bRemove.setEnabled(tServices.getSelectionCount() > 0);

        bApplyPara.setEnabled(false);
        bRemovePara.setEnabled(tParams.getSelectionCount() > 0);

        tName.setBackground(null);
    }


    public void setToolTipText() {
        bApply.setToolTipText(Messages.getTooltip("web_services.btn_apply"));
        bRemove.setToolTipText(Messages.getTooltip("web_services.btn_remove"));
        bNew.setToolTipText(Messages.getTooltip("web_services.btn_new"));
        tServices.setToolTipText(Messages.getTooltip("web_services.table"));
        tName.setToolTipText(Messages.getTooltip("web_services.name"));
        tURL.setToolTipText(Messages.getTooltip("web_services.url"));
        cChain.setToolTipText(Messages.getTooltip("web_services.job_chain"));
        sTimeout.setToolTipText(Messages.getTooltip("web_services.timeout"));
        bDebug.setToolTipText(Messages.getTooltip("web_services.debug"));
        tRequest.setToolTipText(Messages.getTooltip("web_services.request_xslt"));
        tForward.setToolTipText(Messages.getTooltip("web_services.forward_xslt"));
        tResponse.setToolTipText(Messages.getTooltip("web_services.response_xslt"));
        bRemovePara.setToolTipText(Messages.getTooltip("web_services.param.btn_remove"));
        tParaName.setToolTipText(Messages.getTooltip("web_services.param.name_entry"));
        tParaValue.setToolTipText(Messages.getTooltip("web_services.param.value_entry"));
        bApplyPara.setToolTipText(Messages.getTooltip("web_services.param.btn_apply"));
        tParams.setToolTipText(Messages.getTooltip("web_services.param.table"));

    }
} // @jve:decl-index=0:visual-constraint="10,10"
