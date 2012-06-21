package sos.scheduler.editor.app;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.program.Program;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;
import org.jdom.Element;
import org.jdom.JDOMException;

import sos.scheduler.editor.conf.SchedulerDom;
import sos.scheduler.editor.conf.forms.JobChainsForm;
import sos.scheduler.editor.conf.forms.SchedulerForm;
import sos.scheduler.editor.conf.listeners.SchedulerListener;

public class TreeMenu {

    private DomParser _dom = null;

    private Tree _tree = null;

    private Menu _menu = null;

    private static Element _copy = null;

    private static int _type = -1;

    private SchedulerForm _gui = null;

<<<<<<< .mine
    private static final String EDIT_XML = "Edit XML";
    private static final String SHOW_XML = "Show XML";
    //private static final String SHOW_INFO = "Show Info";
    private static final String COPY = "Copy";
    private static final String COPY_TO_CLIPBOARD = "XML to Clipboard";
    private static final String PASTE = "Paste";
    private static final String DELETE_HOT_HOLDER_FILE = "Delete Hot Folder File";
    private static final String NEW = "New";
    private static final String DELETE = "Delete";
=======
    private static final String EDIT_XML = "Edit XML";
    private static final String SHOW_XML = "Show XML";
    private static final String SHOW_INFO = "Show Info";
    private static final String COPY = "Copy";
    private static final String COPY_TO_CLIPBOARD = "Copy to Clipboard";
    private static final String PASTE = "Paste";
    private static final String DELETE_HOT_HOLDER_FILE = "Delete Hot Folder File";
    private static final String NEW = "New";
    private static final String DELETE = "Delete";
>>>>>>> .r17402

    public TreeMenu(Tree tree, DomParser dom, SchedulerForm gui) {

        _tree = tree;
        _dom = dom;
        _gui = gui;
        createMenu();

    }

    public int message(String message, int style) {
        MessageBox mb = new MessageBox(_tree.getShell(), style);
        mb.setMessage(message);
        return mb.open();
    }

    private Element getElement() {
        if (_tree.getSelectionCount() > 0) {
            Element retVal = (Element) _tree.getSelection()[0].getData("copy_element");
            if (retVal == null) {
                try {
                    new sos.scheduler.editor.app.ErrorLog(_tree.getSelection()[0].getText() + " need copy_element." + sos.util.SOSClassUtil.getMethodName());
                } catch (Exception ee) {
                    // tu nichts
                }
                return null;
            }
            if (((SchedulerDom) _dom).isDirectory()) {
                return (Element) Utils.getHotFolderParentElement(retVal).clone();
            }
            return (Element) retVal.clone();
        }

        return null;

    }

    /*
     * private Element getElement() { if (_tree.getSelectionCount() > 0) {
     * TreeData data = (TreeData) _tree.getSelection()[0].getData(); if (data !=
     * null && data instanceof TreeData) { if (data.getChild() != null) { if
     * (data.getChild().equalsIgnoreCase("orders")) { return
     * data.getElement().getChild("commands"); } else {
     * if(data.getElement().getChild(data.getChild()) == null) {
     * //data.getElement().addContent(new Element(data.getChild())); return
     * data.getElement(); } return data.getElement().getChild(data.getChild());
     * } } else { if(data.getElement().getName().equals("at") ||
     * data.getElement().getName().equals("date")) { return
     * data.getElement().getParentElement(); } else return data.getElement(); }
     * } else return null; } else return null; }
     */

    private void createMenu() {
        _menu = new Menu(_tree.getShell(), SWT.POP_UP);

        MenuItem item = new MenuItem(_menu, SWT.PUSH);
        item.addListener(SWT.Selection, getCopyListener());
        item.setText(TreeMenu.COPY);

<<<<<<< .mine
        item = new MenuItem(_menu, SWT.PUSH);
        item.addListener(SWT.Selection, getPasteListener());
        item.setText(TreeMenu.PASTE);
=======
        item = new MenuItem(_menu, SWT.PUSH);
        item.addListener(SWT.Selection, getNewItemSelection());
        item.setText(TreeMenu.NEW);
        item.setEnabled(false);
>>>>>>> .r17402

<<<<<<< .mine
        item = new MenuItem(_menu, SWT.SEPARATOR);
=======
        item = new MenuItem(_menu, SWT.PUSH);
        item.addListener(SWT.Selection, getDeleteSelection());
        item.setText(TreeMenu.DELETE);
        item.setEnabled(false);
>>>>>>> .r17402

<<<<<<< .mine
        item = new MenuItem(_menu, SWT.PUSH);
        item.addListener(SWT.Selection, getNewItemSelection());
        item.setText(TreeMenu.NEW);
        item.setEnabled(false);
=======
        if ((_dom instanceof sos.scheduler.editor.conf.SchedulerDom)) {
            if (((sos.scheduler.editor.conf.SchedulerDom) _dom).isLifeElement()) {
                item = new MenuItem(_menu, SWT.PUSH);
                item.addListener(SWT.Selection, getDeleteHoltFolderFileListener());
                item.setText(TreeMenu.DELETE_HOT_HOLDER_FILE);
>>>>>>> .r17402

<<<<<<< .mine
        item = new MenuItem(_menu, SWT.PUSH);
        item.addListener(SWT.Selection, getDeleteSelection());
        item.setText(TreeMenu.DELETE);
        item.setEnabled(false);
=======
            }
        }
>>>>>>> .r17402

<<<<<<< .mine
        if ((_dom instanceof sos.scheduler.editor.conf.SchedulerDom)) {
            if (((sos.scheduler.editor.conf.SchedulerDom) _dom).isLifeElement()) {
                item = new MenuItem(_menu, SWT.PUSH);
                item.addListener(SWT.Selection, getDeleteHoltFolderFileListener());
                item.setText(TreeMenu.DELETE_HOT_HOLDER_FILE);
=======
        item = new MenuItem(_menu, SWT.PUSH);
        item.addListener(SWT.Selection, getClipboardListener());
        item.setText(TreeMenu.COPY_TO_CLIPBOARD);
>>>>>>> .r17402

<<<<<<< .mine
            }
        }
=======
        item = new MenuItem(_menu, SWT.SEPARATOR);
>>>>>>> .r17402

<<<<<<< .mine
        item = new MenuItem(_menu, SWT.SEPARATOR);
        
        item = new MenuItem(_menu, SWT.PUSH);
        item.addListener(SWT.Selection, getClipboardListener());
        item.setText(TreeMenu.COPY_TO_CLIPBOARD);
=======
        item = new MenuItem(_menu, SWT.PUSH);
        item.addListener(SWT.Selection, getPasteListener());
        item.setText(TreeMenu.PASTE);
>>>>>>> .r17402

<<<<<<< .mine
=======
        _menu.addListener(SWT.Show, new Listener() {
            public void handleEvent(Event e) {
                // MenuItem[] items = _menu.getItems();
                if (_copy == null)
                    disableMenu();
                if (_tree.getSelectionCount() > 0) {
                    Element element = getElement();
                    if (element != null) {
                        // test element = _dom.getRoot().getChild("config");
>>>>>>> .r17402

<<<<<<< .mine
=======
                        getItem(TreeMenu.EDIT_XML).setEnabled(true);
                        getItem(TreeMenu.SHOW_INFO).setEnabled(true); // show
                                                                      // info
                        getItem(TreeMenu.SHOW_XML).setEnabled(true); // show xml
                        getItem(TreeMenu.COPY_TO_CLIPBOARD).setEnabled(true); // copy
                                                                              // to
                                                                              // clipboard
                        if (_dom instanceof SchedulerDom) {
                            SchedulerDom curDom = ((SchedulerDom) _dom);
                            if (curDom.isLifeElement()) {
                                getItem(TreeMenu.DELETE_HOT_HOLDER_FILE).setEnabled(true);
                            }
                            if (curDom.isDirectory()) {
                                String elemName = getElement().getName();
                                if (elemName.equals("config"))
                                    getItem(TreeMenu.EDIT_XML).setEnabled(false);
                            }
                        }
>>>>>>> .r17402

<<<<<<< .mine
        _menu.addListener(SWT.Show, new Listener() {
            public void handleEvent(Event e) {
                // MenuItem[] items = _menu.getItems();
                if (_copy == null)
                    disableMenu();
                if (_tree.getSelectionCount() > 0) {
                    Element element = getElement();
                    if (element != null) {
                        // test element = _dom.getRoot().getChild("config");
=======
                        // String name = element.getName();
                        // test
                        getItem(TreeMenu.COPY).setEnabled(true); // copy
                        /*
                         * if (name.equals("job") || name.equals("config") ||
                         * name.equals("run_time"))
                         * getItem(TreeMenu.COPY).setEnabled(true); // copy else
                         * getItem(TreeMenu.COPY).setEnabled(false); // copy
                         */
                        if (_copy != null) {
                            // String cName = _copy.getName();
>>>>>>> .r17402

<<<<<<< .mine
                        getItem(TreeMenu.EDIT_XML).setEnabled(true);
                       // getItem(TreeMenu.SHOW_INFO).setEnabled(true); // show
                                                                      // info
                        getItem(TreeMenu.SHOW_XML).setEnabled(true); // show xml
                        getItem(TreeMenu.COPY_TO_CLIPBOARD).setEnabled(true); // copy
                                                                              // to
                                                                              // clipboard
                        if (_dom instanceof SchedulerDom) {
                            SchedulerDom curDom = ((SchedulerDom) _dom);
                            if (curDom.isLifeElement()) {
                                getItem(TreeMenu.DELETE_HOT_HOLDER_FILE).setEnabled(true);
                            }
                            if (curDom.isDirectory()) {
                                String elemName = getElement().getName();
                                if (elemName.equals("config"))
                                    getItem(TreeMenu.EDIT_XML).setEnabled(false);
                            }
                        }
=======
                            MenuItem _paste = getItem(TreeMenu.PASTE);
>>>>>>> .r17402

<<<<<<< .mine
                        // String name = element.getName();
                        // test
                        getItem(TreeMenu.COPY).setEnabled(true); // copy
                        /*
                         * if (name.equals("job") || name.equals("config") ||
                         * name.equals("run_time"))
                         * getItem(TreeMenu.COPY).setEnabled(true); // copy else
                         * getItem(TreeMenu.COPY).setEnabled(false); // copy
                         */
                        if (_copy != null) {
                            // String cName = _copy.getName();
=======
                            /*
                             * if(_tree.getSelectionCount() > 0) {
                             * System.out.println
                             * (_tree.getSelection()[0].getData("key") + "   " +
                             * _tree.getSelection()[0].getText()); }
                             */
>>>>>>> .r17402

<<<<<<< .mine
                            MenuItem _paste = getItem(TreeMenu.PASTE);
=======
                            _paste.setEnabled(true); // paste
                            /*
                             * if (name.equals("jobs") && cName.equals("job"))
                             * _paste.setEnabled(true); // paste else if
                             * (name.equals("job") && cName.equals("run_time"))
                             * _paste.setEnabled(true); // paste else if
                             * (name.equals("config") && cName.equals("config"))
                             * _paste.setEnabled(true); // paste else
                             * _paste.setEnabled(false); // paste
                             */
                        }
>>>>>>> .r17402

<<<<<<< .mine
                            /*
                             * if(_tree.getSelectionCount() > 0) {
                             * System.out.println
                             * (_tree.getSelection()[0].getData("key") + "   " +
                             * _tree.getSelection()[0].getText()); }
                             */
=======
                        if (getParentItemName().length() > 0) {
                            getItem(TreeMenu.NEW).setEnabled(true);
                        }
>>>>>>> .r17402

<<<<<<< .mine
                            _paste.setEnabled(true); // paste
                            /*
                             * if (name.equals("jobs") && cName.equals("job"))
                             * _paste.setEnabled(true); // paste else if
                             * (name.equals("job") && cName.equals("run_time"))
                             * _paste.setEnabled(true); // paste else if
                             * (name.equals("config") && cName.equals("config"))
                             * _paste.setEnabled(true); // paste else
                             * _paste.setEnabled(false); // paste
                             */
                        }
=======
                        if ((_dom instanceof sos.scheduler.editor.conf.SchedulerDom) && !((SchedulerDom) _dom).isLifeElement()) {
                            if (getItemElement() != null) {
                                getItem(TreeMenu.DELETE).setEnabled(true);
                            }
                        }
>>>>>>> .r17402

<<<<<<< .mine
                        if (getParentItemName().length() > 0) {
                            getItem(TreeMenu.NEW).setEnabled(true);
                        }
=======
                    }
                }
            }
        });
>>>>>>> .r17402

<<<<<<< .mine
                        if ((_dom instanceof sos.scheduler.editor.conf.SchedulerDom) && !((SchedulerDom) _dom).isLifeElement()) {
                            if (getItemElement() != null) {
                                getItem(TreeMenu.DELETE).setEnabled(true);
                            }
                        }
=======
        item = new MenuItem(_menu, SWT.SEPARATOR);
        
        item = new MenuItem(_menu, SWT.PUSH);
        item.addListener(SWT.Selection, getXMLListener());
        item.setText(TreeMenu.EDIT_XML);
>>>>>>> .r17402

<<<<<<< .mine
                    }
                }
            }
        });
=======
        item = new MenuItem(_menu, SWT.SEPARATOR);
>>>>>>> .r17402

<<<<<<< .mine
        
        item = new MenuItem(_menu, SWT.PUSH);
        item.addListener(SWT.Selection, getXMLListener());
        item.setText(TreeMenu.EDIT_XML);
=======
        item = new MenuItem(_menu, SWT.PUSH);
        item.addListener(SWT.Selection, getXMLListener());
        item.setText(TreeMenu.SHOW_XML);
>>>>>>> .r17402

<<<<<<< .mine
=======
       /* item = new MenuItem(_menu, SWT.PUSH);
        item.addListener(SWT.Selection, getInfoListener());
        item.setText(TreeMenu.SHOW_INFO);
        */
>>>>>>> .r17402

<<<<<<< .mine
        item = new MenuItem(_menu, SWT.PUSH);
        item.addListener(SWT.Selection, getXMLListener());
        item.setText(TreeMenu.SHOW_XML);
=======
    }
>>>>>>> .r17402

<<<<<<< .mine
       /* item = new MenuItem(_menu, SWT.PUSH);
        item.addListener(SWT.Selection, getInfoListener());
        item.setText(TreeMenu.SHOW_INFO);
        */
=======
    public Menu getMenu() {
        return _menu;
    }
>>>>>>> .r17402

<<<<<<< .mine
    }
=======
    private void disableMenu() {
        MenuItem[] items = _menu.getItems();
        for (int i = 0; i < items.length; i++)
            items[i].setEnabled(false);
    }
>>>>>>> .r17402

<<<<<<< .mine
    public Menu getMenu() {
        return _menu;
    }
=======
    private Listener getInfoListener() {
        return new Listener() {
            public void handleEvent(Event e) {
                Element element = getElement();
                if (element != null) {
                    try {
                        String filename = _dom.transform(element);
>>>>>>> .r17402

<<<<<<< .mine
    private void disableMenu() {
        MenuItem[] items = _menu.getItems();
        for (int i = 0; i < items.length; i++)
            items[i].setEnabled(false);
    }
=======
                        Program prog = Program.findProgram("html");
                        if (prog != null)
                            prog.execute(filename);
                        else {
                            filename = new File(filename).toURL().toString();
                            Runtime.getRuntime().exec(Options.getBrowserExec(filename, null));
                        }
                    } catch (Exception ex) {
                        try {
                            new ErrorLog("error in " + sos.util.SOSClassUtil.getMethodName(), ex);
                        } catch (Exception ee) {
                            // tu nichts
                        }
                        ex.printStackTrace();
                        message("Error: " + ex.getMessage(), SWT.ICON_ERROR | SWT.OK);
                    }
                }
            }
        };
    }
>>>>>>> .r17402

<<<<<<< .mine
    private Listener getInfoListener() {
        return new Listener() {
            public void handleEvent(Event e) {
                Element element = getElement();
                if (element != null) {
                    try {
                        String filename = _dom.transform(element);
=======
    private String getXML() {
        Element element = getElement();
        String xml = "";
        if (element != null) {
            try {
>>>>>>> .r17402

<<<<<<< .mine
                        Program prog = Program.findProgram("html");
                        if (prog != null)
                            prog.execute(filename);
                        else {
                            filename = new File(filename).toURL().toString();
                            Runtime.getRuntime().exec(Options.getBrowserExec(filename, null));
                        }
                    } catch (Exception ex) {
                        try {
                            new ErrorLog("error in " + sos.util.SOSClassUtil.getMethodName(), ex);
                        } catch (Exception ee) {
                            // tu nichts
                        }
                        ex.printStackTrace();
                        message("Error: " + ex.getMessage(), SWT.ICON_ERROR | SWT.OK);
                    }
                }
            }
        };
    }
=======
                xml = _dom.getXML(element);
>>>>>>> .r17402

<<<<<<< .mine
    private String getXML() {
        Element element = getElement();
        String xml = "";
        if (element != null) {
            try {
=======
            } catch (JDOMException ex) {
                try {
                    new ErrorLog("error in " + sos.util.SOSClassUtil.getMethodName(), ex);
                } catch (Exception ee) {
                    // tu nichts
                }
                message("Error: " + ex.getMessage(), SWT.ICON_ERROR | SWT.OK);
                return null;
            }
>>>>>>> .r17402

<<<<<<< .mine
                xml = _dom.getXML(element);
=======
        }
        return xml;
    }
>>>>>>> .r17402

<<<<<<< .mine
            } catch (JDOMException ex) {
                try {
                    new ErrorLog("error in " + sos.util.SOSClassUtil.getMethodName(), ex);
                } catch (Exception ee) {
                    // tu nichts
                }
                message("Error: " + ex.getMessage(), SWT.ICON_ERROR | SWT.OK);
                return null;
            }
=======
    private String getXML(Element element) {
>>>>>>> .r17402

<<<<<<< .mine
        }
        return xml;
    }
=======
        String xml = "";
        if (element != null) {
            try {
                if (_dom instanceof SchedulerDom && ((SchedulerDom) _dom).isDirectory()) {
>>>>>>> .r17402

<<<<<<< .mine
    private String getXML(Element element) {
=======
                    xml = _dom.getXML(Utils.getHotFolderParentElement(element));
                } else {
                    xml = _dom.getXML(element);
                }
>>>>>>> .r17402

<<<<<<< .mine
        String xml = "";
        if (element != null) {
            try {
                if (_dom instanceof SchedulerDom && ((SchedulerDom) _dom).isDirectory()) {
=======
            } catch (JDOMException ex) {
                try {
                    new ErrorLog("error in " + sos.util.SOSClassUtil.getMethodName(), ex);
                } catch (Exception ee) {
                    // tu nichts
                }
                message("Error: " + ex.getMessage(), SWT.ICON_ERROR | SWT.OK);
                return null;
            }
>>>>>>> .r17402

<<<<<<< .mine
                    xml = _dom.getXML(Utils.getHotFolderParentElement(element));
                } else {
                    xml = _dom.getXML(element);
                }
=======
        }
        return xml;
    }
>>>>>>> .r17402

<<<<<<< .mine
            } catch (JDOMException ex) {
                try {
                    new ErrorLog("error in " + sos.util.SOSClassUtil.getMethodName(), ex);
                } catch (Exception ee) {
                    // tu nichts
                }
                message("Error: " + ex.getMessage(), SWT.ICON_ERROR | SWT.OK);
                return null;
            }
=======
    private Listener getXMLListener() {
>>>>>>> .r17402

<<<<<<< .mine
        }
        return xml;
    }
=======
        return new Listener() {
            public void handleEvent(Event e) {
>>>>>>> .r17402

<<<<<<< .mine
    private Listener getXMLListener() {
=======
                MenuItem i = (MenuItem) e.widget;
                Element element = null;
                String xml = null;
                if (i.getText().equalsIgnoreCase(TreeMenu.EDIT_XML)) {
                    if (_dom instanceof sos.scheduler.editor.conf.SchedulerDom && (((sos.scheduler.editor.conf.SchedulerDom) _dom).isLifeElement() || ((sos.scheduler.editor.conf.SchedulerDom) _dom).isDirectory())) {
                        element = getElement();
>>>>>>> .r17402

<<<<<<< .mine
        return new Listener() {
            public void handleEvent(Event e) {
=======
                    } else {
                        element = _dom.getRoot().getChild("config");
                    }
                    if (element != null) {
                        xml = getXML(element);
                    }
>>>>>>> .r17402

<<<<<<< .mine
                MenuItem i = (MenuItem) e.widget;
                Element element = null;
                String xml = null;
                if (i.getText().equalsIgnoreCase(TreeMenu.EDIT_XML)) {
                    if (_dom instanceof sos.scheduler.editor.conf.SchedulerDom && (((sos.scheduler.editor.conf.SchedulerDom) _dom).isLifeElement() || ((sos.scheduler.editor.conf.SchedulerDom) _dom).isDirectory())) {
                        element = getElement();
=======
                } else {
                    element = getElement();
                    if (element != null) {
                        xml = getXML(element);
>>>>>>> .r17402

<<<<<<< .mine
                    } else {
                        element = _dom.getRoot().getChild("config");
                    }
                    if (element != null) {
                        xml = getXML(element);
                    }
=======
                    }
>>>>>>> .r17402

<<<<<<< .mine
                } else {
                    element = getElement();
                    if (element != null) {
                        xml = getXML(element);
=======
                }
>>>>>>> .r17402

<<<<<<< .mine
                    }
=======
                if (element != null) {
                    if (xml == null) // error
                        return;
>>>>>>> .r17402

<<<<<<< .mine
                }
=======
                    String selectStr = Utils.getAttributeValue("name", getElement());
                    selectStr = selectStr == null || selectStr.length() == 0 ? getElement().getName() : selectStr;
>>>>>>> .r17402

<<<<<<< .mine
                if (element != null) {
                    if (xml == null) // error
                        return;
=======
                    String newXML = Utils.showClipboard(xml, _tree.getShell(), i.getText().equalsIgnoreCase(TreeMenu.EDIT_XML), selectStr);
>>>>>>> .r17402

<<<<<<< .mine
                    String selectStr = Utils.getAttributeValue("name", getElement());
                    selectStr = selectStr == null || selectStr.length() == 0 ? getElement().getName() : selectStr;
=======
                    // newXML ist null, wenn Änderungen nicht übernommen werden
                    // sollen
                    if (newXML != null)
                        applyXMLChange(newXML);
>>>>>>> .r17402

<<<<<<< .mine
                    String newXML = Utils.showClipboard(xml, _tree.getShell(), i.getText().equalsIgnoreCase(TreeMenu.EDIT_XML), selectStr);
=======
                }
            }
>>>>>>> .r17402

<<<<<<< .mine
                    // newXML ist null, wenn Änderungen nicht übernommen werden
                    // sollen
                    if (newXML != null)
                        applyXMLChange(newXML);
=======
        };
    }
>>>>>>> .r17402

<<<<<<< .mine
                }
            }
=======
    private void applyXMLChange(String newXML) {
        String newName = "";
        String oldname = "";
        try {
            if (_dom instanceof SchedulerDom) {
>>>>>>> .r17402

<<<<<<< .mine
        };
    }
=======
                newName = getHotFolderName(newXML);
>>>>>>> .r17402

<<<<<<< .mine
    private void applyXMLChange(String newXML) {
        String newName = "";
        String oldname = "";
        try {
            if (_dom instanceof SchedulerDom) {
=======
                if (((sos.scheduler.editor.conf.SchedulerDom) _dom).isDirectory() || ((SchedulerDom) _dom).isLifeElement()) {
                    String enco = " ";
                    if (newXML.indexOf("?>") > -1) {
                        enco = newXML.substring(0, newXML.indexOf("?>") + "?>".length());
                        newXML = newXML.substring(newXML.indexOf("?>") + "?>".length());
                    }
                    String xml = "";
                    if (((SchedulerDom) _dom).isDirectory())
                        xml = Utils.getElementAsString(_dom.getRoot().getChild("config"));
                    else
                        xml = Utils.getElementAsString(_dom.getRoot());
>>>>>>> .r17402

<<<<<<< .mine
                newName = getHotFolderName(newXML);
=======
                    String oldxml = Utils.getElementAsString(getElement());
                    oldname = getHotFolderName(oldxml);
>>>>>>> .r17402

<<<<<<< .mine
                if (((sos.scheduler.editor.conf.SchedulerDom) _dom).isDirectory() || ((SchedulerDom) _dom).isLifeElement()) {
                    String enco = " ";
                    if (newXML.indexOf("?>") > -1) {
                        enco = newXML.substring(0, newXML.indexOf("?>") + "?>".length());
                        newXML = newXML.substring(newXML.indexOf("?>") + "?>".length());
                    }
                    String xml = "";
                    if (((SchedulerDom) _dom).isDirectory())
                        xml = Utils.getElementAsString(_dom.getRoot().getChild("config"));
                    else
                        xml = Utils.getElementAsString(_dom.getRoot());
=======
                    int iPosBegin = 0;
                    if (oldxml.indexOf("\r\n") > -1)
                        iPosBegin = xml.indexOf(oldxml.substring(0, oldxml.indexOf("\r\n")));
                    else
                        iPosBegin = xml.indexOf(oldxml);
>>>>>>> .r17402

<<<<<<< .mine
                    String oldxml = Utils.getElementAsString(getElement());
                    oldname = getHotFolderName(oldxml);
=======
                    if (iPosBegin == -1)
                        iPosBegin = 0;
>>>>>>> .r17402

<<<<<<< .mine
                    int iPosBegin = 0;
                    if (oldxml.indexOf("\r\n") > -1)
                        iPosBegin = xml.indexOf(oldxml.substring(0, oldxml.indexOf("\r\n")));
                    else
                        iPosBegin = xml.indexOf(oldxml);
=======
                    // int iPosEnd = xml.indexOf("</"+ getElement().getName() +
                    // ">", iPosBegin) + (("</"+ getElement().getName() +
                    // ">").length()) ;
                    int iPosEnd = xml.indexOf("</" + getElement().getName() + ">", iPosBegin);
                    if (iPosEnd == -1) {
                        // hat keinen Kindknoten
                        iPosEnd = xml.indexOf("/>", iPosBegin) + "/>".length();
                    } else {
                        iPosEnd = iPosEnd + ("</" + getElement().getName() + ">").length();
                    }
                    /*
                     * System.out.println("****************************************"
                     * ); System.out.println("Begin : "); System.out.println(
                     * xml.substring(0, iPosBegin));
                     * System.out.println("End   : "); System.out.println(
                     * xml.substring(iPosEnd));
                     * System.out.println("****************************************"
                     * );
                     */
>>>>>>> .r17402

<<<<<<< .mine
                    if (iPosBegin == -1)
                        iPosBegin = 0;
=======
                    // int iPosEnd = iPosBegin + oldxml.length();
                    newXML = xml.substring(0, iPosBegin) + newXML + xml.substring(iPosEnd);
                    // snewXML = snewXML + newXML + xml.substring(iPosEnd);
>>>>>>> .r17402

<<<<<<< .mine
                    // int iPosEnd = xml.indexOf("</"+ getElement().getName() +
                    // ">", iPosBegin) + (("</"+ getElement().getName() +
                    // ">").length()) ;
                    int iPosEnd = xml.indexOf("</" + getElement().getName() + ">", iPosBegin);
                    if (iPosEnd == -1) {
                        // hat keinen Kindknoten
                        iPosEnd = xml.indexOf("/>", iPosBegin) + "/>".length();
                    } else {
                        iPosEnd = iPosEnd + ("</" + getElement().getName() + ">").length();
                    }
                    /*
                     * System.out.println("****************************************"
                     * ); System.out.println("Begin : "); System.out.println(
                     * xml.substring(0, iPosBegin));
                     * System.out.println("End   : "); System.out.println(
                     * xml.substring(iPosEnd));
                     * System.out.println("****************************************"
                     * );
                     */
=======
                    if (((SchedulerDom) _dom).isLifeElement())
                        newXML = enco + newXML;
                    else
                        newXML = enco + "<spooler>" + newXML + "</spooler>";
                    /*
                     * if(!oldname.equals(newName))
                     * ((sos.scheduler.editor.conf.SchedulerDom
                     * )_dom).setChangedForDirectory("job", oldname,
                     * SchedulerDom.DELETE);
                     * 
                     * ((sos.scheduler.editor.conf.SchedulerDom)_dom).
                     * setChangedForDirectory("job", newName,
                     * SchedulerDom.MODIFY);
                     */
                    // System.out.println(newXML);
                } else if (!((sos.scheduler.editor.conf.SchedulerDom) _dom).isLifeElement()) {
                    newXML = newXML.replaceAll("\\?>", "?><spooler>") + "</spooler>";
                }
>>>>>>> .r17402

<<<<<<< .mine
                    // int iPosEnd = iPosBegin + oldxml.length();
                    newXML = xml.substring(0, iPosBegin) + newXML + xml.substring(iPosEnd);
                    // snewXML = snewXML + newXML + xml.substring(iPosEnd);
=======
            }
>>>>>>> .r17402

<<<<<<< .mine
                    if (((SchedulerDom) _dom).isLifeElement())
                        newXML = enco + newXML;
                    else
                        newXML = enco + "<spooler>" + newXML + "</spooler>";
                    /*
                     * if(!oldname.equals(newName))
                     * ((sos.scheduler.editor.conf.SchedulerDom
                     * )_dom).setChangedForDirectory("job", oldname,
                     * SchedulerDom.DELETE);
                     * 
                     * ((sos.scheduler.editor.conf.SchedulerDom)_dom).
                     * setChangedForDirectory("job", newName,
                     * SchedulerDom.MODIFY);
                     */
                    // System.out.println(newXML);
                } else if (!((sos.scheduler.editor.conf.SchedulerDom) _dom).isLifeElement()) {
                    newXML = newXML.replaceAll("\\?>", "?><spooler>") + "</spooler>";
                }
=======
            // System.out.println("debug: \n" + newXML);
>>>>>>> .r17402

<<<<<<< .mine
            }
=======
            _dom.readString(newXML, true);
>>>>>>> .r17402

<<<<<<< .mine
            // System.out.println("debug: \n" + newXML);
=======
            _gui.update();
            _dom.setChanged(true);
            Element elem = null;
            if (_dom instanceof SchedulerDom && (((SchedulerDom) _dom).isDirectory() || ((SchedulerDom) _dom).isLifeElement())) {
                elem = getElement();
                if (!newName.equals("") && !Utils.getAttributeValue("name", elem).equals(newName) && (elem.getName().equals("order") || elem.getName().equals("add_order"))) {
                    ((SchedulerDom) _dom).setChangedForDirectory(elem.getName(), Utils.getAttributeValue("job_chain", elem) + "," + Utils.getAttributeValue("id", elem), SchedulerDom.DELETE);
                    ((SchedulerDom) _dom).setChangedForDirectory(elem.getName(), newName, SchedulerDom.NEW);
                } else if (!newName.equals("") && !Utils.getAttributeValue("name", elem).equals(newName)) {
                    ((SchedulerDom) _dom).setChangedForDirectory(elem.getName(), Utils.getAttributeValue("name", elem), SchedulerDom.DELETE);
                    ((SchedulerDom) _dom).setChangedForDirectory(elem.getName(), newName, SchedulerDom.NEW);
                } else
                    ((SchedulerDom) _dom).setChangedForDirectory(elem, SchedulerDom.NEW);
            }
>>>>>>> .r17402

<<<<<<< .mine
            _dom.readString(newXML, true);
=======
            if (_dom instanceof SchedulerDom && ((SchedulerDom) (_dom)).isLifeElement() && oldname != null && newName != null && !oldname.equals(newName) && _dom.getFilename() != null) {
>>>>>>> .r17402

<<<<<<< .mine
            _gui.update();
            _dom.setChanged(true);
            Element elem = null;
            if (_dom instanceof SchedulerDom && (((SchedulerDom) _dom).isDirectory() || ((SchedulerDom) _dom).isLifeElement())) {
                elem = getElement();
                if (!newName.equals("") && !Utils.getAttributeValue("name", elem).equals(newName) && (elem.getName().equals("order") || elem.getName().equals("add_order"))) {
                    ((SchedulerDom) _dom).setChangedForDirectory(elem.getName(), Utils.getAttributeValue("job_chain", elem) + "," + Utils.getAttributeValue("id", elem), SchedulerDom.DELETE);
                    ((SchedulerDom) _dom).setChangedForDirectory(elem.getName(), newName, SchedulerDom.NEW);
                } else if (!newName.equals("") && !Utils.getAttributeValue("name", elem).equals(newName)) {
                    ((SchedulerDom) _dom).setChangedForDirectory(elem.getName(), Utils.getAttributeValue("name", elem), SchedulerDom.DELETE);
                    ((SchedulerDom) _dom).setChangedForDirectory(elem.getName(), newName, SchedulerDom.NEW);
                } else
                    ((SchedulerDom) _dom).setChangedForDirectory(elem, SchedulerDom.NEW);
            }
=======
                String parent = "";
                if (_dom.getFilename() != null && new File(_dom.getFilename()).getParent() != null)
                    parent = new File(_dom.getFilename()).getParent();
>>>>>>> .r17402

<<<<<<< .mine
            if (_dom instanceof SchedulerDom && ((SchedulerDom) (_dom)).isLifeElement() && oldname != null && newName != null && !oldname.equals(newName) && _dom.getFilename() != null) {
=======
                File oldFilename = new File(parent, oldname + "." + getElement().getName() + ".xml");
>>>>>>> .r17402

<<<<<<< .mine
                String parent = "";
                if (_dom.getFilename() != null && new File(_dom.getFilename()).getParent() != null)
                    parent = new File(_dom.getFilename()).getParent();
=======
                File newFilename = null;
                if (oldFilename != null && oldFilename.getParent() != null)
                    newFilename = new File(oldFilename.getParent(), newName + "." + getElement().getName() + ".xml");
                else
                    newFilename = new File(parent, newName + "." + getElement().getName() + ".xml");
>>>>>>> .r17402

<<<<<<< .mine
                File oldFilename = new File(parent, oldname + "." + getElement().getName() + ".xml");
=======
                int c = MainWindow.message(MainWindow.getSShell(), "Do you want really rename Hot Folder File from " + oldFilename + " to " + newFilename + "?", SWT.ICON_WARNING | SWT.YES | SWT.NO);
                if (c == SWT.YES) {
                    _gui.updateJob(newName);
                    if (_dom.getFilename() != null) {
                        oldFilename.renameTo(newFilename);
                        // _dom.setFilename(_dom.getFilename().replaceAll(new
                        // File(_dom.getFilename()).getName(), (newName + "." +
                        // getElement().getName() + ".xml")));
                        _dom.setFilename(newFilename.getCanonicalPath());
                    }
                    // _gui.updateTree("main");
                    // _dom.setChanged(false);
                    if (MainWindow.getContainer().getCurrentEditor().applyChanges()) {
                        MainWindow.getContainer().getCurrentEditor().save();
                        MainWindow.setSaveStatus();
                    }
>>>>>>> .r17402

<<<<<<< .mine
                File newFilename = null;
                if (oldFilename != null && oldFilename.getParent() != null)
                    newFilename = new File(oldFilename.getParent(), newName + "." + getElement().getName() + ".xml");
                else
                    newFilename = new File(parent, newName + "." + getElement().getName() + ".xml");
=======
                } else {
                    return;
                }
>>>>>>> .r17402

<<<<<<< .mine
                int c = MainWindow.message(MainWindow.getSShell(), "Do you want really rename Hot Folder File from " + oldFilename + " to " + newFilename + "?", SWT.ICON_WARNING | SWT.YES | SWT.NO);
                if (c == SWT.YES) {
                    _gui.updateJob(newName);
                    if (_dom.getFilename() != null) {
                        oldFilename.renameTo(newFilename);
                        // _dom.setFilename(_dom.getFilename().replaceAll(new
                        // File(_dom.getFilename()).getName(), (newName + "." +
                        // getElement().getName() + ".xml")));
                        _dom.setFilename(newFilename.getCanonicalPath());
                    }
                    // _gui.updateTree("main");
                    // _dom.setChanged(false);
                    if (MainWindow.getContainer().getCurrentEditor().applyChanges()) {
                        MainWindow.getContainer().getCurrentEditor().save();
                        MainWindow.setSaveStatus();
                    }
=======
            }
>>>>>>> .r17402

<<<<<<< .mine
                } else {
                    return;
                }
=======
            _gui.update();
            _gui.updateTree("main");
>>>>>>> .r17402

<<<<<<< .mine
            }
=======
            refreshTree("main");
>>>>>>> .r17402

<<<<<<< .mine
            _gui.update();
            _gui.updateTree("main");
=======
        } catch (Exception de) {
            try {
                new ErrorLog("error in " + sos.util.SOSClassUtil.getMethodName(), de);
            } catch (Exception ee) {
                // tu nichts
            }
            MainWindow.message(MainWindow.getSShell(), "..error while update XML: " + de.getMessage(), SWT.ICON_WARNING);
        }
    }
>>>>>>> .r17402

<<<<<<< .mine
            refreshTree("main");
=======
    private Listener getCopyListener() {
        return new Listener() {
            public void handleEvent(Event e) {
                Element element = getElement();
                if (element != null) {
                    _copy = (Element) element.clone();
                    _type = ((TreeData) _tree.getSelection()[0].getData()).getType();
                }
            }
        };
    }
>>>>>>> .r17402

<<<<<<< .mine
        } catch (Exception de) {
            try {
                new ErrorLog("error in " + sos.util.SOSClassUtil.getMethodName(), de);
            } catch (Exception ee) {
                // tu nichts
            }
            MainWindow.message(MainWindow.getSShell(), "..error while update XML: " + de.getMessage(), SWT.ICON_WARNING);
        }
    }
=======
    // TreeData data = (TreeData)_tree.getSelection()[0].getData();
    // sos.scheduler.editor.conf.ISchedulerUpdate update = _gui;
>>>>>>> .r17402

<<<<<<< .mine
    private Listener getCopyListener() {
        return new Listener() {
            public void handleEvent(Event e) {
                Element element = getElement();
                if (element != null) {
                    _copy = (Element) element.clone();
                    _type = ((TreeData) _tree.getSelection()[0].getData()).getType();
                }
            }
        };
    }
=======
    private Listener getNewItemSelection() {
        return new Listener() {
            public void handleEvent(Event e) {
>>>>>>> .r17402

<<<<<<< .mine
    // TreeData data = (TreeData)_tree.getSelection()[0].getData();
    // sos.scheduler.editor.conf.ISchedulerUpdate update = _gui;
=======
                String name = getParentItemName();
>>>>>>> .r17402

<<<<<<< .mine
    private Listener getNewItemSelection() {
        return new Listener() {
            public void handleEvent(Event e) {
=======
                if (name.equals(SchedulerListener.JOBS)) {
>>>>>>> .r17402

<<<<<<< .mine
                String name = getParentItemName();
=======
                    sos.scheduler.editor.conf.listeners.JobsListener listeners = new sos.scheduler.editor.conf.listeners.JobsListener((SchedulerDom) _dom, _gui);
                    listeners.newJob(sos.scheduler.editor.conf.forms.JobsForm.getTable(), false);
>>>>>>> .r17402

<<<<<<< .mine
                if (name.equals(SchedulerListener.JOBS)) {
=======
                } else if (name.equals(SchedulerListener.MONITOR)) {
>>>>>>> .r17402

<<<<<<< .mine
                    sos.scheduler.editor.conf.listeners.JobsListener listeners = new sos.scheduler.editor.conf.listeners.JobsListener((SchedulerDom) _dom, _gui);
                    listeners.newJob(sos.scheduler.editor.conf.forms.JobsForm.getTable(), false);
=======
                    TreeData data = (TreeData) _tree.getSelection()[0].getData();
                    org.eclipse.swt.widgets.Table table = sos.scheduler.editor.conf.forms.ScriptsForm.getTable();
                    sos.scheduler.editor.conf.listeners.ScriptsListener listener = new sos.scheduler.editor.conf.listeners.ScriptsListener((SchedulerDom) _dom, _gui, data.getElement());
                    listener.save(table, "monitor" + table.getItemCount(), String.valueOf(table.getItemCount()), null);
>>>>>>> .r17402

<<<<<<< .mine
                } else if (name.equals(SchedulerListener.MONITOR)) {
=======
                } else if (name.equals(SchedulerListener.JOB_CHAINS)) {
>>>>>>> .r17402

<<<<<<< .mine
                    TreeData data = (TreeData) _tree.getSelection()[0].getData();
                    org.eclipse.swt.widgets.Table table = sos.scheduler.editor.conf.forms.ScriptsForm.getTable();
                    sos.scheduler.editor.conf.listeners.ScriptsListener listener = new sos.scheduler.editor.conf.listeners.ScriptsListener((SchedulerDom) _dom, _gui, data.getElement());
                    listener.save(table, "monitor" + table.getItemCount(), String.valueOf(table.getItemCount()), null);
=======
                    TreeData data = (TreeData) _tree.getSelection()[0].getData();
                    sos.scheduler.editor.conf.listeners.JobChainsListener listeners = new sos.scheduler.editor.conf.listeners.JobChainsListener((SchedulerDom) _dom, data.getElement(), _gui);
                    listeners.newChain();
                    int i = 1;
                    if (data.getElement().getChild("job_chains") != null)
                        i = data.getElement().getChild("job_chains").getChildren("job_chain").size() + 1;
                    listeners.applyChain("job_chain" + i, true, true);
                    listeners.fillChains(JobChainsForm.getTableChains());
>>>>>>> .r17402

<<<<<<< .mine
                } else if (name.equals(SchedulerListener.JOB_CHAINS)) {
=======
                } else if (name.equals(SchedulerListener.SCHEDULES)) {
>>>>>>> .r17402

<<<<<<< .mine
                    TreeData data = (TreeData) _tree.getSelection()[0].getData();
                    sos.scheduler.editor.conf.listeners.JobChainsListener listeners = new sos.scheduler.editor.conf.listeners.JobChainsListener((SchedulerDom) _dom, data.getElement(), _gui);
                    listeners.newChain();
                    int i = 1;
                    if (data.getElement().getChild("job_chains") != null)
                        i = data.getElement().getChild("job_chains").getChildren("job_chain").size() + 1;
                    listeners.applyChain("job_chain" + i, true, true);
                    listeners.fillChains(JobChainsForm.getTableChains());
=======
                    sos.scheduler.editor.conf.listeners.SchedulesListener listener = new sos.scheduler.editor.conf.listeners.SchedulesListener((SchedulerDom) _dom, _gui);
                    listener.newScheduler(sos.scheduler.editor.conf.forms.SchedulesForm.getTable());
>>>>>>> .r17402

<<<<<<< .mine
                } else if (name.equals(SchedulerListener.SCHEDULES)) {
=======
                } else if (name.equals(SchedulerListener.ORDERS)) {
>>>>>>> .r17402

<<<<<<< .mine
                    sos.scheduler.editor.conf.listeners.SchedulesListener listener = new sos.scheduler.editor.conf.listeners.SchedulesListener((SchedulerDom) _dom, _gui);
                    listener.newScheduler(sos.scheduler.editor.conf.forms.SchedulesForm.getTable());
=======
                    sos.scheduler.editor.conf.listeners.OrdersListener listener = new sos.scheduler.editor.conf.listeners.OrdersListener((SchedulerDom) _dom, _gui);
                    listener.newCommands(sos.scheduler.editor.conf.forms.OrdersForm.getTable());
>>>>>>> .r17402

<<<<<<< .mine
                } else if (name.equals(SchedulerListener.ORDERS)) {
=======
                } else if (name.equals(SchedulerListener.WEB_SERVICES)) {
                    TreeData data = (TreeData) _tree.getSelection()[0].getData();
                    sos.scheduler.editor.conf.listeners.WebservicesListener listener = new sos.scheduler.editor.conf.listeners.WebservicesListener((SchedulerDom) _dom, data.getElement(), _gui);
                    listener.newService(sos.scheduler.editor.conf.forms.WebservicesForm.getTable());
>>>>>>> .r17402

<<<<<<< .mine
                    sos.scheduler.editor.conf.listeners.OrdersListener listener = new sos.scheduler.editor.conf.listeners.OrdersListener((SchedulerDom) _dom, _gui);
                    listener.newCommands(sos.scheduler.editor.conf.forms.OrdersForm.getTable());
=======
                } else if (name.equals(SchedulerListener.LOCKS)) {
                    try {
                        TreeData data = (TreeData) _tree.getSelection()[0].getData();
                        sos.scheduler.editor.conf.listeners.LocksListener listener = new sos.scheduler.editor.conf.listeners.LocksListener((SchedulerDom) _dom, data.getElement());
                        listener.newLock();
                        int i = 1;
                        if (data.getElement().getChild("locks") != null)
                            i = data.getElement().getChild("locks").getChildren("lock").size() + 1;
                        listener.applyLock("lock_" + i, 0, true);
                        listener.fillTable(sos.scheduler.editor.conf.forms.LocksForm.getTable());
                        listener.selectLock(i - 1);
                    } catch (Exception es) {
                        try {
                            new sos.scheduler.editor.app.ErrorLog("error in " + sos.util.SOSClassUtil.getMethodName(), es);
                        } catch (Exception ee) {
                            // tu nichts
                        }
                    }
>>>>>>> .r17402

<<<<<<< .mine
                } else if (name.equals(SchedulerListener.WEB_SERVICES)) {
                    TreeData data = (TreeData) _tree.getSelection()[0].getData();
                    sos.scheduler.editor.conf.listeners.WebservicesListener listener = new sos.scheduler.editor.conf.listeners.WebservicesListener((SchedulerDom) _dom, data.getElement(), _gui);
                    listener.newService(sos.scheduler.editor.conf.forms.WebservicesForm.getTable());
=======
                } else if (name.equals(SchedulerListener.PROCESS_CLASSES)) {
                    TreeData data = (TreeData) _tree.getSelection()[0].getData();
                    try {
                        sos.scheduler.editor.conf.listeners.ProcessClassesListener listener = new sos.scheduler.editor.conf.listeners.ProcessClassesListener((SchedulerDom) _dom, data.getElement());
                        listener.newProcessClass();
                        int i = 1;
                        if (data.getElement().getChild("process_classes") != null)
                            i = data.getElement().getChild("process_classes").getChildren("process_class").size() + 1;
                        listener.applyProcessClass("processClass_" + i, "", "", 0);
                        listener.fillTable(sos.scheduler.editor.conf.forms.ProcessClassesForm.getTable());
                        listener.selectProcessClass(i - 1);
>>>>>>> .r17402

<<<<<<< .mine
                } else if (name.equals(SchedulerListener.LOCKS)) {
                    try {
                        TreeData data = (TreeData) _tree.getSelection()[0].getData();
                        sos.scheduler.editor.conf.listeners.LocksListener listener = new sos.scheduler.editor.conf.listeners.LocksListener((SchedulerDom) _dom, data.getElement());
                        listener.newLock();
                        int i = 1;
                        if (data.getElement().getChild("locks") != null)
                            i = data.getElement().getChild("locks").getChildren("lock").size() + 1;
                        listener.applyLock("lock_" + i, 0, true);
                        listener.fillTable(sos.scheduler.editor.conf.forms.LocksForm.getTable());
                        listener.selectLock(i - 1);
                    } catch (Exception es) {
                        try {
                            new sos.scheduler.editor.app.ErrorLog("error in " + sos.util.SOSClassUtil.getMethodName(), es);
                        } catch (Exception ee) {
                            // tu nichts
                        }
                    }
=======
                    } catch (Exception es) {
                        try {
                            new sos.scheduler.editor.app.ErrorLog("error in " + sos.util.SOSClassUtil.getMethodName(), es);
                        } catch (Exception ee) {
                            // tu nichts
                        }
                    }
>>>>>>> .r17402

<<<<<<< .mine
                } else if (name.equals(SchedulerListener.PROCESS_CLASSES)) {
                    TreeData data = (TreeData) _tree.getSelection()[0].getData();
                    try {
                        sos.scheduler.editor.conf.listeners.ProcessClassesListener listener = new sos.scheduler.editor.conf.listeners.ProcessClassesListener((SchedulerDom) _dom, data.getElement());
                        listener.newProcessClass();
                        int i = 1;
                        if (data.getElement().getChild("process_classes") != null)
                            i = data.getElement().getChild("process_classes").getChildren("process_class").size() + 1;
                        listener.applyProcessClass("processClass_" + i, "", "", 0);
                        listener.fillTable(sos.scheduler.editor.conf.forms.ProcessClassesForm.getTable());
                        listener.selectProcessClass(i - 1);
=======
                }
>>>>>>> .r17402

<<<<<<< .mine
                    } catch (Exception es) {
                        try {
                            new sos.scheduler.editor.app.ErrorLog("error in " + sos.util.SOSClassUtil.getMethodName(), es);
                        } catch (Exception ee) {
                            // tu nichts
                        }
                    }
=======
            }
        };
    }
>>>>>>> .r17402

<<<<<<< .mine
                }
=======
    private Listener getDeleteSelection() {
        return new Listener() {
            public void handleEvent(Event e) {
                Element elem = getItemElement();
                String name = elem.getName();
>>>>>>> .r17402

<<<<<<< .mine
            }
        };
    }
=======
                int c = MainWindow.message("Do you want remove the " + name + "?", SWT.ICON_QUESTION | SWT.YES | SWT.NO);
                if (c != SWT.YES)
                    return;
>>>>>>> .r17402

<<<<<<< .mine
    private Listener getDeleteSelection() {
        return new Listener() {
            public void handleEvent(Event e) {
                Element elem = getItemElement();
                String name = elem.getName();
=======
                if (name.equals("job")) {
                    /*
                     * if (_tree.getSelection()[0] != null &&
                     * !_tree.getSelection
                     * ()[0].getText().equals(SchedulerListener.JOB)) { TreeData
                     * data = (TreeData)_tree.getSelection()[0].getData();
                     * ((SchedulerDom)_dom).setChangedForDirectory("job",
                     * Utils.getAttributeValue("name", elem)
                     * ,SchedulerDom.MODIFY); elem.detach(); //List el =
                     * elem.getChildren
                     * (_tree.getSelection()[0].getText().toLowerCase());
                     * //if(el != null) //el.clear();
                     * 
                     * //getElement().detach(); } //TreeData data =
                     * (TreeData)_tree.getSelection()[0].getData(); else {
                     */
                    _dom.setChanged(true);
                    ((SchedulerDom) _dom).setChangedForDirectory("job", Utils.getAttributeValue("name", elem), SchedulerDom.DELETE);
                    elem.detach();
>>>>>>> .r17402

<<<<<<< .mine
                int c = MainWindow.message("Do you want remove the " + name + "?", SWT.ICON_QUESTION | SWT.YES | SWT.NO);
                if (c != SWT.YES)
                    return;
=======
                    // }
>>>>>>> .r17402

<<<<<<< .mine
                if (name.equals("job")) {
                    /*
                     * if (_tree.getSelection()[0] != null &&
                     * !_tree.getSelection
                     * ()[0].getText().equals(SchedulerListener.JOB)) { TreeData
                     * data = (TreeData)_tree.getSelection()[0].getData();
                     * ((SchedulerDom)_dom).setChangedForDirectory("job",
                     * Utils.getAttributeValue("name", elem)
                     * ,SchedulerDom.MODIFY); elem.detach(); //List el =
                     * elem.getChildren
                     * (_tree.getSelection()[0].getText().toLowerCase());
                     * //if(el != null) //el.clear();
                     * 
                     * //getElement().detach(); } //TreeData data =
                     * (TreeData)_tree.getSelection()[0].getData(); else {
                     */
                    _dom.setChanged(true);
                    ((SchedulerDom) _dom).setChangedForDirectory("job", Utils.getAttributeValue("name", elem), SchedulerDom.DELETE);
                    elem.detach();
=======
                    _gui.updateJobs();
>>>>>>> .r17402

<<<<<<< .mine
                    // }
=======
                } else if (name.equals("monitor")) {
                    _dom.setChanged(true);
                    ((SchedulerDom) _dom).setChangedForDirectory("job", Utils.getAttributeValue("name", elem.getParentElement()), SchedulerDom.MODIFY);
                    elem.detach();
>>>>>>> .r17402

                    _gui.updateJobs();

<<<<<<< .mine
                } else if (name.equals("monitor")) {
                    _dom.setChanged(true);
                    ((SchedulerDom) _dom).setChangedForDirectory("job", Utils.getAttributeValue("name", elem.getParentElement()), SchedulerDom.MODIFY);
                    elem.detach();
=======
                } else if (name.equals("job_chain")) {
>>>>>>> .r17402

<<<<<<< .mine
                    _gui.updateJobs();
=======
                    // TreeData data =
                    // (TreeData)_tree.getSelection()[0].getData();
                    // data.getElement().detach();
                    _dom.setChanged(true);
                    ((SchedulerDom) _dom).setChangedForDirectory("job_chain", Utils.getAttributeValue("name", elem), SchedulerDom.DELETE);
                    elem.detach();
                    TreeItem parentItem = _tree.getSelection()[0].getParentItem();
                    _tree.setSelection(new TreeItem[] { parentItem });
                    if (parentItem.getItemCount() == 1)// job_chains Element hat
                                                       // keine weiteren
                                                       // Kindelemente
                        ((TreeData) parentItem.getData()).getElement().getChild("job_chains").detach();
                    _gui.updateJobChains();
                    _gui.updateCMainForm();
>>>>>>> .r17402

<<<<<<< .mine
                } else if (name.equals("job_chain")) {
=======
                } else if (name.equals("schedule")) {
>>>>>>> .r17402

<<<<<<< .mine
                    // TreeData data =
                    // (TreeData)_tree.getSelection()[0].getData();
                    // data.getElement().detach();
                    _dom.setChanged(true);
                    ((SchedulerDom) _dom).setChangedForDirectory("job_chain", Utils.getAttributeValue("name", elem), SchedulerDom.DELETE);
                    elem.detach();
                    TreeItem parentItem = _tree.getSelection()[0].getParentItem();
                    _tree.setSelection(new TreeItem[] { parentItem });
                    if (parentItem.getItemCount() == 1)// job_chains Element hat
                                                       // keine weiteren
                                                       // Kindelemente
                        ((TreeData) parentItem.getData()).getElement().getChild("job_chains").detach();
                    _gui.updateJobChains();
                    _gui.updateCMainForm();
=======
                    _dom.setChanged(true);
                    ((SchedulerDom) _dom).setChangedForDirectory("schedule", Utils.getAttributeValue("name", elem), SchedulerDom.DELETE);
                    elem.detach();
                    TreeItem parentItem = _tree.getSelection()[0].getParentItem();
                    _tree.setSelection(new TreeItem[] { parentItem });
                    if (parentItem.getItemCount() == 1)// job_chains Element hat
                                                       // keine weiteren
                                                       // Kindelemente
                        ((TreeData) parentItem.getData()).getElement().getChild("schedules").detach();
                    _gui.updateSchedules();
                    _gui.updateCMainForm();
>>>>>>> .r17402

<<<<<<< .mine
                } else if (name.equals("schedule")) {
=======
                } else if (name.equals("order") || name.equals("add_order")) {
>>>>>>> .r17402

<<<<<<< .mine
                    _dom.setChanged(true);
                    ((SchedulerDom) _dom).setChangedForDirectory("schedule", Utils.getAttributeValue("name", elem), SchedulerDom.DELETE);
                    elem.detach();
                    TreeItem parentItem = _tree.getSelection()[0].getParentItem();
                    _tree.setSelection(new TreeItem[] { parentItem });
                    if (parentItem.getItemCount() == 1)// job_chains Element hat
                                                       // keine weiteren
                                                       // Kindelemente
                        ((TreeData) parentItem.getData()).getElement().getChild("schedules").detach();
                    _gui.updateSchedules();
                    _gui.updateCMainForm();
=======
                    _dom.setChanged(true);
                    ((SchedulerDom) _dom).setChangedForDirectory("order", Utils.getAttributeValue("job_chain", elem) + "," + Utils.getAttributeValue("id", elem), SchedulerDom.DELETE);
                    elem.detach();
>>>>>>> .r17402

<<<<<<< .mine
                } else if (name.equals("order") || name.equals("add_order")) {
=======
                    TreeItem parentItem = _tree.getSelection()[0].getParentItem();
                    _tree.setSelection(new TreeItem[] { parentItem });
>>>>>>> .r17402

<<<<<<< .mine
                    _dom.setChanged(true);
                    ((SchedulerDom) _dom).setChangedForDirectory("order", Utils.getAttributeValue("job_chain", elem) + "," + Utils.getAttributeValue("id", elem), SchedulerDom.DELETE);
                    elem.detach();
=======
                    if (parentItem.getItemCount() == 1)// job_chains Element hat
                                                       // keine weiteren
                                                       // Kindelemente
                        ((TreeData) parentItem.getData()).getElement().getChild("commands").detach();
>>>>>>> .r17402

<<<<<<< .mine
                    TreeItem parentItem = _tree.getSelection()[0].getParentItem();
                    _tree.setSelection(new TreeItem[] { parentItem });
=======
                    _gui.updateOrders();
                    _gui.updateCMainForm();
>>>>>>> .r17402

<<<<<<< .mine
                    if (parentItem.getItemCount() == 1)// job_chains Element hat
                                                       // keine weiteren
                                                       // Kindelemente
                        ((TreeData) parentItem.getData()).getElement().getChild("commands").detach();
=======
                } else if (name.equals("web_service")) {
>>>>>>> .r17402

<<<<<<< .mine
                    _gui.updateOrders();
                    _gui.updateCMainForm();
=======
                    // TreeData data =
                    // (TreeData)_tree.getSelection()[0].getData();
                    _dom.setChanged(true);
                    elem.detach();
>>>>>>> .r17402

<<<<<<< .mine
                } else if (name.equals("web_service")) {
=======
                    TreeItem parentItem = _tree.getSelection()[0].getParentItem();
                    _tree.setSelection(new TreeItem[] { parentItem });
>>>>>>> .r17402

<<<<<<< .mine
                    // TreeData data =
                    // (TreeData)_tree.getSelection()[0].getData();
                    _dom.setChanged(true);
                    elem.detach();
=======
                    // if(parentItem.getItemCount() == 1)//job_chains Element
                    // hat keine weiteren Kindelemente
                    // ((TreeData)parentItem.getData()).getElement().getChild("Web Services").detach();
>>>>>>> .r17402

<<<<<<< .mine
                    TreeItem parentItem = _tree.getSelection()[0].getParentItem();
                    _tree.setSelection(new TreeItem[] { parentItem });
=======
                    _gui.updateWebServices();
                    _gui.updateCMainForm();
>>>>>>> .r17402

<<<<<<< .mine
                    // if(parentItem.getItemCount() == 1)//job_chains Element
                    // hat keine weiteren Kindelemente
                    // ((TreeData)parentItem.getData()).getElement().getChild("Web Services").detach();
=======
                }
>>>>>>> .r17402

<<<<<<< .mine
                    _gui.updateWebServices();
                    _gui.updateCMainForm();
=======
            }
        };
    }
>>>>>>> .r17402

<<<<<<< .mine
                }
=======
    private Listener getDeleteHoltFolderFileListener() {
        return new Listener() {
            public void handleEvent(Event e) {
>>>>>>> .r17402

<<<<<<< .mine
            }
        };
    }
=======
                String filename = _dom.getFilename();
>>>>>>> .r17402

<<<<<<< .mine
    private Listener getDeleteHoltFolderFileListener() {
        return new Listener() {
            public void handleEvent(Event e) {
=======
                if (filename == null) {
                    MainWindow.message("file name is null. Could not remove file.", SWT.ICON_WARNING | SWT.OK);
                    return;
                }
>>>>>>> .r17402

<<<<<<< .mine
                String filename = _dom.getFilename();
=======
                int ok = MainWindow.message("Do you want really remove live file: " + filename, //$NON-NLS-1$
                        SWT.ICON_QUESTION | SWT.YES | SWT.NO | SWT.CANCEL);
>>>>>>> .r17402

<<<<<<< .mine
                if (filename == null) {
                    MainWindow.message("file name is null. Could not remove file.", SWT.ICON_WARNING | SWT.OK);
                    return;
                }
=======
                if (ok == SWT.CANCEL || ok == SWT.NO)
                    return;
>>>>>>> .r17402

<<<<<<< .mine
                int ok = MainWindow.message("Do you want really remove live file: " + filename, //$NON-NLS-1$
                        SWT.ICON_QUESTION | SWT.YES | SWT.NO | SWT.CANCEL);
=======
                if (!new java.io.File(filename).delete()) {
                    MainWindow.message("could not remove live file", SWT.ICON_WARNING | SWT.OK);
                }
                if (_dom instanceof SchedulerDom && ((SchedulerDom) _dom).isLifeElement()) {
                    sos.scheduler.editor.app.IContainer con = MainWindow.getContainer();
                    con.getCurrentTab().dispose();
                }
>>>>>>> .r17402

<<<<<<< .mine
                if (ok == SWT.CANCEL || ok == SWT.NO)
                    return;
=======
            }
        };
    }
>>>>>>> .r17402

<<<<<<< .mine
                if (!new java.io.File(filename).delete()) {
                    MainWindow.message("could not remove live file", SWT.ICON_WARNING | SWT.OK);
                }
                if (_dom instanceof SchedulerDom && ((SchedulerDom) _dom).isLifeElement()) {
                    sos.scheduler.editor.app.IContainer con = MainWindow.getContainer();
                    con.getCurrentTab().dispose();
                }
=======
    private Listener getClipboardListener() {
        return new Listener() {
            public void handleEvent(Event e) {
                Element element = getElement();
>>>>>>> .r17402

<<<<<<< .mine
            }
        };
    }
=======
                if (element != null) {
                    String xml = getXML();
                    if (xml == null) // error
                        return;
>>>>>>> .r17402

<<<<<<< .mine
    private Listener getClipboardListener() {
        return new Listener() {
            public void handleEvent(Event e) {
                Element element = getElement();
=======
                    if (!element.getName().equals("config") && xml.indexOf("<?xml") > -1) {
                        xml = xml.substring(xml.indexOf("?>") + 2);
                    }
                    Utils.copyClipboard(xml, _tree.getDisplay());
>>>>>>> .r17402

<<<<<<< .mine
                if (element != null) {
                    String xml = getXML();
                    if (xml == null) // error
                        return;
=======
                }
            }
        };
    }
>>>>>>> .r17402

<<<<<<< .mine
                    if (!element.getName().equals("config") && xml.indexOf("<?xml") > -1) {
                        xml = xml.substring(xml.indexOf("?>") + 2);
                    }
                    Utils.copyClipboard(xml, _tree.getDisplay());
=======
    private Listener getPasteListener() {
        return new Listener() {
>>>>>>> .r17402

<<<<<<< .mine
                }
            }
        };
    }
=======
            public void handleEvent(Event e) {
>>>>>>> .r17402

<<<<<<< .mine
    private Listener getPasteListener() {
        return new Listener() {
=======
                if (_tree.getSelectionCount() == 0)
                    return;
>>>>>>> .r17402

<<<<<<< .mine
            public void handleEvent(Event e) {
=======
                TreeData data = (TreeData) _tree.getSelection()[0].getData();
>>>>>>> .r17402

<<<<<<< .mine
                if (_tree.getSelectionCount() == 0)
                    return;
=======
                boolean override = false;
>>>>>>> .r17402

<<<<<<< .mine
                TreeData data = (TreeData) _tree.getSelection()[0].getData();
=======
                if (_tree.getSelection()[0].getData("override_attributes") != null)
                    override = _tree.getSelection()[0].getData("override_attributes").equals("true");
>>>>>>> .r17402

<<<<<<< .mine
                boolean override = false;
=======
                if (_tree.getSelection()[0].getData("key") instanceof String) {
                    // ein Formular hat nur einen Kindknoten Element zum
                    // Kopieren, z.B. job Formular
                    String key = _tree.getSelection()[0].getData("key").toString();
                    paste(key, data, override);
                } else if (_tree.getSelection()[0].getData("key") instanceof ArrayList) {
                    // ein Formular hat mehreren Kindknoten Element zum
                    // Kopieren, z.B. job Run Option ->
                    // start_when_directory_changed; delay_after_error;
                    // delay_order_after_setback
                    ArrayList keys = (ArrayList) _tree.getSelection()[0].getData("key");
                    for (int i = 0; i < keys.size(); i++) {
                        paste(keys.get(i).toString(), data, override);
                    }
                }
>>>>>>> .r17402

<<<<<<< .mine
                if (_tree.getSelection()[0].getData("override_attributes") != null)
                    override = _tree.getSelection()[0].getData("override_attributes").equals("true");
=======
                if (_dom instanceof SchedulerDom && (((SchedulerDom) _dom).isDirectory() || ((SchedulerDom) _dom).isLifeElement())) {
                    Utils.setChangedForDirectory(data.getElement(), ((SchedulerDom) _dom));
                }
>>>>>>> .r17402

<<<<<<< .mine
                if (_tree.getSelection()[0].getData("key") instanceof String) {
                    // ein Formular hat nur einen Kindknoten Element zum
                    // Kopieren, z.B. job Formular
                    String key = _tree.getSelection()[0].getData("key").toString();
                    paste(key, data, override);
                } else if (_tree.getSelection()[0].getData("key") instanceof ArrayList) {
                    // ein Formular hat mehreren Kindknoten Element zum
                    // Kopieren, z.B. job Run Option ->
                    // start_when_directory_changed; delay_after_error;
                    // delay_order_after_setback
                    ArrayList keys = (ArrayList) _tree.getSelection()[0].getData("key");
                    for (int i = 0; i < keys.size(); i++) {
                        paste(keys.get(i).toString(), data, override);
                    }
                }
=======
            }
>>>>>>> .r17402

<<<<<<< .mine
                if (_dom instanceof SchedulerDom && (((SchedulerDom) _dom).isDirectory() || ((SchedulerDom) _dom).isLifeElement())) {
                    Utils.setChangedForDirectory(data.getElement(), ((SchedulerDom) _dom));
                }
=======
        };
    }
>>>>>>> .r17402

<<<<<<< .mine
            }
=======
    /*
     * private Listener getPasteListener() { return new Listener() { public void
     * handleEvent(Event e) { Element target = getElement();
     * 
     * if ((target != null && _copy != null)) { String tName = target.getName();
     * String cName = _copy.getName();
     * 
     * if(_dom instanceof SchedulerDom && ((SchedulerDom)_dom).isLifeElement())
     * {
     * 
     * //if(cName.equals("job")) { target = (Element)_copy.clone(); TreeData
     * data = (TreeData) _tree.getSelection()[0].getData();
     * data.setElement(target); _gui.update(); _gui.updateLifeElement();
     * //_gui.updateJob(target.getName()); return; //} }
     * 
     * if (tName.equals("jobs") && cName.equals("job")) { // copy job
     * 
     * String append = "copy(" + (target.getChildren("job").size() + 1); Element
     * currCopy = (Element)_copy.clone();
     * 
     * if(existJobname(target, Utils.getAttributeValue("name", _copy)))
     * currCopy.setAttribute("name", append + ")of_" +
     * Utils.getAttributeValue("name", _copy));
     * 
     * target.addContent(currCopy);
     * 
     * refreshTree("jobs"); _gui.update(); if(_dom instanceof SchedulerDom &&
     * !((SchedulerDom)_dom).isLifeElement()) _gui.updateJobs();
     * _dom.setChanged(true);
     * 
     * } else if (tName.equals("job") && cName.equals("run_time")) { // copy //
     * run_time target.removeChildren("run_time"); target.addContent(_copy);
     * _gui.updateJob(); _dom.setChanged(true); } else if
     * (tName.equals("config") && cName.equals("config")) { // copy // run_time
     * //target.getParentElement().removeContent(); Element spooler =
     * target.getParentElement(); spooler.removeChildren("config");
     * spooler.addContent((Element)_copy.clone());
     * 
     * refreshTree("main"); _dom.setChanged(true);
     * 
     * _gui.update(); } else if (tName.equals("commands") &&
     * cName.equals("order")) { // copy job
     * 
     * String append = "copy(" + (target.getChildren("order").size() + 1);
     * Element currCopy = (Element)_copy.clone();
     * 
     * 
     * currCopy.setAttribute("id", append + ")of_" +
     * Utils.getAttributeValue("id", _copy));
     * 
     * target.addContent(currCopy);
     * 
     * refreshTree("main"); _gui.updateCommands(); _gui.updateOrders();
     * _gui.update(); _dom.setChanged(true);
     * 
     * } else if (tName.equals("job_chains") && cName.equals("job_chain")) { //
     * copy job
     * 
     * String append = "copy(" + (target.getChildren("job_chain").size() + 1);
     * Element currCopy = (Element)_copy.clone();
     * 
     * if(existJobname(target, Utils.getAttributeValue("name", _copy)))
     * currCopy.setAttribute("name", append + ")of_" +
     * Utils.getAttributeValue("name", _copy));
     * 
     * target.addContent(currCopy);
     * 
     * _gui.updateJobChains(); refreshTree("main"); _gui.update();
     * _dom.setChanged(true);
     * 
     * } } } }; }
     */
>>>>>>> .r17402

<<<<<<< .mine
        };
    }
=======
    private boolean existJobname(Element jobs, String jobname) {
        boolean retVal = false;
        java.util.List list = jobs.getChildren();
        for (int i = 0; i < list.size(); i++) {
            Element job = (Element) list.get(i);
            if (Utils.getAttributeValue("name", job).equalsIgnoreCase(jobname)) {
                retVal = true;
                break;
            }
        }
        return retVal;
    }
>>>>>>> .r17402

<<<<<<< .mine
    /*
     * private Listener getPasteListener() { return new Listener() { public void
     * handleEvent(Event e) { Element target = getElement();
     * 
     * if ((target != null && _copy != null)) { String tName = target.getName();
     * String cName = _copy.getName();
     * 
     * if(_dom instanceof SchedulerDom && ((SchedulerDom)_dom).isLifeElement())
     * {
     * 
     * //if(cName.equals("job")) { target = (Element)_copy.clone(); TreeData
     * data = (TreeData) _tree.getSelection()[0].getData();
     * data.setElement(target); _gui.update(); _gui.updateLifeElement();
     * //_gui.updateJob(target.getName()); return; //} }
     * 
     * if (tName.equals("jobs") && cName.equals("job")) { // copy job
     * 
     * String append = "copy(" + (target.getChildren("job").size() + 1); Element
     * currCopy = (Element)_copy.clone();
     * 
     * if(existJobname(target, Utils.getAttributeValue("name", _copy)))
     * currCopy.setAttribute("name", append + ")of_" +
     * Utils.getAttributeValue("name", _copy));
     * 
     * target.addContent(currCopy);
     * 
     * refreshTree("jobs"); _gui.update(); if(_dom instanceof SchedulerDom &&
     * !((SchedulerDom)_dom).isLifeElement()) _gui.updateJobs();
     * _dom.setChanged(true);
     * 
     * } else if (tName.equals("job") && cName.equals("run_time")) { // copy //
     * run_time target.removeChildren("run_time"); target.addContent(_copy);
     * _gui.updateJob(); _dom.setChanged(true); } else if
     * (tName.equals("config") && cName.equals("config")) { // copy // run_time
     * //target.getParentElement().removeContent(); Element spooler =
     * target.getParentElement(); spooler.removeChildren("config");
     * spooler.addContent((Element)_copy.clone());
     * 
     * refreshTree("main"); _dom.setChanged(true);
     * 
     * _gui.update(); } else if (tName.equals("commands") &&
     * cName.equals("order")) { // copy job
     * 
     * String append = "copy(" + (target.getChildren("order").size() + 1);
     * Element currCopy = (Element)_copy.clone();
     * 
     * 
     * currCopy.setAttribute("id", append + ")of_" +
     * Utils.getAttributeValue("id", _copy));
     * 
     * target.addContent(currCopy);
     * 
     * refreshTree("main"); _gui.updateCommands(); _gui.updateOrders();
     * _gui.update(); _dom.setChanged(true);
     * 
     * } else if (tName.equals("job_chains") && cName.equals("job_chain")) { //
     * copy job
     * 
     * String append = "copy(" + (target.getChildren("job_chain").size() + 1);
     * Element currCopy = (Element)_copy.clone();
     * 
     * if(existJobname(target, Utils.getAttributeValue("name", _copy)))
     * currCopy.setAttribute("name", append + ")of_" +
     * Utils.getAttributeValue("name", _copy));
     * 
     * target.addContent(currCopy);
     * 
     * _gui.updateJobChains(); refreshTree("main"); _gui.update();
     * _dom.setChanged(true);
     * 
     * } } } }; }
     */
=======
    private void refreshTree(String whichItem) {
>>>>>>> .r17402

<<<<<<< .mine
    private boolean existJobname(Element jobs, String jobname) {
        boolean retVal = false;
        java.util.List list = jobs.getChildren();
        for (int i = 0; i < list.size(); i++) {
            Element job = (Element) list.get(i);
            if (Utils.getAttributeValue("name", job).equalsIgnoreCase(jobname)) {
                retVal = true;
                break;
            }
        }
        return retVal;
    }
=======
        sos.scheduler.editor.app.IContainer con = MainWindow.getContainer();
        SchedulerForm sf = (SchedulerForm) (con.getCurrentEditor());
        sf.updateTree(whichItem);
>>>>>>> .r17402

<<<<<<< .mine
    private void refreshTree(String whichItem) {
=======
    }
>>>>>>> .r17402

<<<<<<< .mine
        sos.scheduler.editor.app.IContainer con = MainWindow.getContainer();
        SchedulerForm sf = (SchedulerForm) (con.getCurrentEditor());
        sf.updateTree(whichItem);
=======
    private MenuItem getItem(String name) {
        MenuItem[] items = _menu.getItems();
        for (int i = 0; i < items.length; i++) {
            MenuItem item = items[i];
            if (item.getText().equalsIgnoreCase(name)) {
                return item;
            }
        }
        return null;
    }
>>>>>>> .r17402

<<<<<<< .mine
    }
=======
    private boolean isParent(String key, String elemname) {
>>>>>>> .r17402

<<<<<<< .mine
    private MenuItem getItem(String name) {
        MenuItem[] items = _menu.getItems();
        for (int i = 0; i < items.length; i++) {
            MenuItem item = items[i];
            if (item.getText().equalsIgnoreCase(name)) {
                return item;
            }
        }
        return null;
    }
=======
        // String[] split = key.split("\\.");
        String[] split = key.split("_@_");
        if (split.length > 1)
            key = split[0];
>>>>>>> .r17402

<<<<<<< .mine
    private boolean isParent(String key, String elemname) {
=======
        String[] s = null;
        if (_dom.getDomOrders().get(key) != null)
            s = (String[]) _dom.getDomOrders().get(key);
>>>>>>> .r17402

<<<<<<< .mine
        // String[] split = key.split("\\.");
        String[] split = key.split("_@_");
        if (split.length > 1)
            key = split[0];
=======
        else if (sos.scheduler.editor.conf.listeners.DaysListener.getDays().get(key) != null)
            // s =
            // (String[])sos.scheduler.editor.conf.listeners.DaysListener.getDays().get(key);
            return true; // group sind nicht definiert
        else
            s = getPossibleParent(key);
        for (int i = 0; s != null && i < s.length; i++) {
            if (s[i].equalsIgnoreCase(elemname)) {
                return true;
            }
        }
        return false;
    }
>>>>>>> .r17402

<<<<<<< .mine
        String[] s = null;
        if (_dom.getDomOrders().get(key) != null)
            s = (String[]) _dom.getDomOrders().get(key);
=======
    // liefert mögliche Vaterknoten der key, falls diese nicht in dom.orders
    // steht
    private String[] getPossibleParent(String key) {
        if (key.equals("jobs") || key.equals("monitor"))
            return new String[] { "job" };
        if (key.equals("schedules"))
            return new String[] { "schedule" };
        if (key.equals("job_chains"))
            return new String[] { "job_chain" };
>>>>>>> .r17402

<<<<<<< .mine
        else if (sos.scheduler.editor.conf.listeners.DaysListener.getDays().get(key) != null)
            // s =
            // (String[])sos.scheduler.editor.conf.listeners.DaysListener.getDays().get(key);
            return true; // group sind nicht definiert
        else
            s = getPossibleParent(key);
        for (int i = 0; s != null && i < s.length; i++) {
            if (s[i].equalsIgnoreCase(elemname)) {
                return true;
            }
        }
        return false;
    }
=======
        // weekdays monthdays ultimos
>>>>>>> .r17402

<<<<<<< .mine
    // liefert mögliche Vaterknoten der key, falls diese nicht in dom.orders
    // steht
    private String[] getPossibleParent(String key) {
        if (key.equals("jobs") || key.equals("monitor"))
            return new String[] { "job" };
        if (key.equals("schedules"))
            return new String[] { "schedule" };
        if (key.equals("job_chains"))
            return new String[] { "job_chain" };
=======
        else
            return new String[] {};
    }
>>>>>>> .r17402

<<<<<<< .mine
        // weekdays monthdays ultimos
=======
    private void paste(String key, TreeData data, boolean overrideAttributes) {
>>>>>>> .r17402

<<<<<<< .mine
        else
            return new String[] {};
    }
=======
        try {
            // ungleiche Typen, überprüfen, ob das pastelement ein möglicher
            // Vaterknoten von _copy element ist, z.B. _copy Element ist job und
            // paste Element ist jobs
            if (_type != data.getType()) {
                // System.out.println("*****************************************");
                pasteChild(key, data);
                return;
            }
>>>>>>> .r17402

<<<<<<< .mine
    private void paste(String key, TreeData data, boolean overrideAttributes) {
=======
            // ab hier gleiche typen
            Element target = _copy;
>>>>>>> .r17402

<<<<<<< .mine
        try {
            // ungleiche Typen, überprüfen, ob das pastelement ein möglicher
            // Vaterknoten von _copy element ist, z.B. _copy Element ist job und
            // paste Element ist jobs
            if (_type != data.getType()) {
                // System.out.println("*****************************************");
                pasteChild(key, data);
                return;
            }
=======
            boolean isLifeElement = _dom instanceof SchedulerDom && ((SchedulerDom) _dom).isLifeElement();
>>>>>>> .r17402

<<<<<<< .mine
            // ab hier gleiche typen
            Element target = _copy;
=======
            if (key.equalsIgnoreCase(target.getName()) && !isLifeElement) {
                // Kopieren nur von Attributen und nicht der Kindelement, z.B.
                // Config Formular
                Element currElem = data.getElement();
                removeAttributes(currElem);
                copyAttr(currElem, _copy.getAttributes());
>>>>>>> .r17402

<<<<<<< .mine
            boolean isLifeElement = _dom instanceof SchedulerDom && ((SchedulerDom) _dom).isLifeElement();
=======
            } else {
>>>>>>> .r17402

<<<<<<< .mine
            if (key.equalsIgnoreCase(target.getName()) && !isLifeElement) {
                // Kopieren nur von Attributen und nicht der Kindelement, z.B.
                // Config Formular
                Element currElem = data.getElement();
                removeAttributes(currElem);
                copyAttr(currElem, _copy.getAttributes());
=======
                // gleiche Typen und und gleiche Elementname -> alle vorhandenen
                // Kindelement kopieren
                Element currElem = data.getElement();
>>>>>>> .r17402

<<<<<<< .mine
            } else {
=======
                Element copyElement = _copy;
>>>>>>> .r17402

<<<<<<< .mine
                // gleiche Typen und und gleiche Elementname -> alle vorhandenen
                // Kindelement kopieren
                Element currElem = data.getElement();
=======
                String[] split = null;
>>>>>>> .r17402

<<<<<<< .mine
                Element copyElement = _copy;
=======
                // split = key.split("\\.");
                split = key.split("_@_");
>>>>>>> .r17402

<<<<<<< .mine
                String[] split = null;
=======
                // System.out.println("*****************************************");
>>>>>>> .r17402

<<<<<<< .mine
                // split = key.split("\\.");
                split = key.split("_@_");
=======
                if (split.length > 1)
                    key = split[split.length - 1];
                java.util.List ce = null;
                if (key.equals(copyElement.getName())) {
                    // überschreiben: z.B. copy ist job element und paste ist
                    // auch Job element
                    removeAttributes(currElem);
                    currElem.removeContent();
                    copyAttr(currElem, copyElement.getAttributes());
                    ce = copyElement.getChildren();
                } else {
>>>>>>> .r17402

<<<<<<< .mine
                // System.out.println("*****************************************");
=======
                    for (int i = 0; i < split.length - 1; i++) {
                        // key ist der Pfad ab data.getelement.
                        copyElement = copyElement.getChild(split[i]);
                        // System.out.println(Utils.getElementAsString(_dom.getRoot()));
                        if (currElem.getChild(split[i]) == null) {
                            currElem = currElem.addContent(new Element(split[i]));
                        }
                        currElem = data.getElement().getChild(split[i]);
                        // System.out.println(Utils.getElementAsString(_dom.getRoot()));
                    }
                    ce = copyElement.getChildren(key);
                }
>>>>>>> .r17402

<<<<<<< .mine
                if (split.length > 1)
                    key = split[split.length - 1];
                java.util.List ce = null;
                if (key.equals(copyElement.getName())) {
                    // überschreiben: z.B. copy ist job element und paste ist
                    // auch Job element
                    removeAttributes(currElem);
                    currElem.removeContent();
                    copyAttr(currElem, copyElement.getAttributes());
                    ce = copyElement.getChildren();
                } else {
=======
                for (int i = 0; i < ce.size(); i++) {
                    Element a = (Element) ce.get(i);
                    Element cloneElement = (Element) a.clone();
                    java.util.List currElemContent = null;
                    if (_tree.getSelection()[0].getData("max_occur") != null && _tree.getSelection()[0].getData("max_occur").equals("1")) {
                        // es darf nur einen currElem.get(key) Kindknoten geben.
                        // Also attribute löschen aber die Kinder mitnehmen
                        if (currElem.getChild(key) != null)
                            currElemContent = currElem.getChild(key).cloneContent();
                        currElem.removeChild(key);
                    }
>>>>>>> .r17402

<<<<<<< .mine
                    for (int i = 0; i < split.length - 1; i++) {
                        // key ist der Pfad ab data.getelement.
                        copyElement = copyElement.getChild(split[i]);
                        // System.out.println(Utils.getElementAsString(_dom.getRoot()));
                        if (currElem.getChild(split[i]) == null) {
                            currElem = currElem.addContent(new Element(split[i]));
                        }
                        currElem = data.getElement().getChild(split[i]);
                        // System.out.println(Utils.getElementAsString(_dom.getRoot()));
                    }
                    ce = copyElement.getChildren(key);
                }
=======
                    //
                    // Element copyClone = (Element)_copy.clone();
>>>>>>> .r17402

<<<<<<< .mine
                for (int i = 0; i < ce.size(); i++) {
                    Element a = (Element) ce.get(i);
                    Element cloneElement = (Element) a.clone();
                    java.util.List currElemContent = null;
                    if (_tree.getSelection()[0].getData("max_occur") != null && _tree.getSelection()[0].getData("max_occur").equals("1")) {
                        // es darf nur einen currElem.get(key) Kindknoten geben.
                        // Also attribute löschen aber die Kinder mitnehmen
                        if (currElem.getChild(key) != null)
                            currElemContent = currElem.getChild(key).cloneContent();
                        currElem.removeChild(key);
                    }
=======
                    if (!Utils.getAttributeValue("name", cloneElement).equals("") && existJobname(currElem, Utils.getAttributeValue("name", cloneElement))) {
                        // System.out.println(Utils.getAttributeValue("name",
                        // cloneElement) + " existiert berteits");
                        String append = "copy(" + (cloneElement.getChildren("job").size() + currElem.getChildren().size() + 1) + ")of_" + Utils.getAttributeValue("name", cloneElement);
                        cloneElement.setAttribute("name", append);
                    }
                    //
>>>>>>> .r17402

<<<<<<< .mine
                    //
                    // Element copyClone = (Element)_copy.clone();
=======
                    currElem.addContent(cloneElement);
                    if (currElemContent != null)
                        cloneElement.addContent(currElemContent);
>>>>>>> .r17402

<<<<<<< .mine
                    if (!Utils.getAttributeValue("name", cloneElement).equals("") && existJobname(currElem, Utils.getAttributeValue("name", cloneElement))) {
                        // System.out.println(Utils.getAttributeValue("name",
                        // cloneElement) + " existiert berteits");
                        String append = "copy(" + (cloneElement.getChildren("job").size() + currElem.getChildren().size() + 1) + ")of_" + Utils.getAttributeValue("name", cloneElement);
                        cloneElement.setAttribute("name", append);
                    }
                    //
=======
                    if (overrideAttributes) {
                        copyElement = cloneElement;
                        currElem = currElem.getChild(key);
                    }
>>>>>>> .r17402

<<<<<<< .mine
                    currElem.addContent(cloneElement);
                    if (currElemContent != null)
                        cloneElement.addContent(currElemContent);
=======
                    // System.out.println(Utils.getElementAsString(_dom.getRoot()));
                }
>>>>>>> .r17402

<<<<<<< .mine
                    if (overrideAttributes) {
                        copyElement = cloneElement;
                        currElem = currElem.getChild(key);
                    }
=======
                if (overrideAttributes) {
                    removeAttributes(currElem);
                    copyAttr(currElem, copyElement.getAttributes());
                }
>>>>>>> .r17402

<<<<<<< .mine
                    // System.out.println(Utils.getElementAsString(_dom.getRoot()));
                }
=======
            }
>>>>>>> .r17402

<<<<<<< .mine
                if (overrideAttributes) {
                    removeAttributes(currElem);
                    copyAttr(currElem, copyElement.getAttributes());
                }
=======
            updateTreeView(data);
        } catch (Exception e) {
            try {
                new sos.scheduler.editor.app.ErrorLog("error in " + sos.util.SOSClassUtil.getMethodName(), e);
            } catch (Exception ee) {
                // tu nichts
            }
        }
    }
>>>>>>> .r17402

<<<<<<< .mine
            }
=======
    // Ein Kindelement hinzufügen, z.B. jobs einen job element oder job_chains
    // einen job_chain element einfügen
    private void pasteChild(String key, TreeData data) {
        if (key.equalsIgnoreCase("monitor") && _type != data.getType()) {
            // ausnahme
            data.getElement().addContent((Element) _copy.clone());
        } else if (!isParent(key, _copy.getName())) {
            return;
        } else {
>>>>>>> .r17402

<<<<<<< .mine
            updateTreeView(data);
        } catch (Exception e) {
            try {
                new sos.scheduler.editor.app.ErrorLog("error in " + sos.util.SOSClassUtil.getMethodName(), e);
            } catch (Exception ee) {
                // tu nichts
            }
        }
    }
=======
            String[] split = key.split("_@_");
            Element elem = data.getElement();
            for (int i = 0; i < split.length - 1; i++) {
                if (data.getElement().getChild(split[i]) == null)
                    data.getElement().addContent(new Element(split[i]));
                elem = data.getElement().getChild(split[i]);
                // System.out.println(Utils.getElementAsString(_dom.getRoot()));
            }
>>>>>>> .r17402

<<<<<<< .mine
    // Ein Kindelement hinzufügen, z.B. jobs einen job element oder job_chains
    // einen job_chain element einfügen
    private void pasteChild(String key, TreeData data) {
        if (key.equalsIgnoreCase("monitor") && _type != data.getType()) {
            // ausnahme
            data.getElement().addContent((Element) _copy.clone());
        } else if (!isParent(key, _copy.getName())) {
            return;
        } else {
=======
            Element copyClone = (Element) _copy.clone();
>>>>>>> .r17402

<<<<<<< .mine
            String[] split = key.split("_@_");
            Element elem = data.getElement();
            for (int i = 0; i < split.length - 1; i++) {
                if (data.getElement().getChild(split[i]) == null)
                    data.getElement().addContent(new Element(split[i]));
                elem = data.getElement().getChild(split[i]);
                // System.out.println(Utils.getElementAsString(_dom.getRoot()));
            }
=======
            if (!Utils.getAttributeValue("name", _copy).equals("") && existJobname(elem, Utils.getAttributeValue("name", _copy))) {
                // System.out.println(Utils.getAttributeValue("name", _copy) +
                // " existiert berteits");
                String append = "copy(" + (copyClone.getChildren("job").size() + elem.getChildren().size() + 1) + ")of_" + Utils.getAttributeValue("name", copyClone);
                copyClone.setAttribute("name", append);
            } else if (!Utils.getAttributeValue("id", _copy).equals("")) {
                // System.out.println(Utils.getAttributeValue("name", _copy) +
                // " existiert berteits");
                String append = "copy(" + (elem.getChildren().size() + 1) + ")of_" + Utils.getAttributeValue("id", copyClone);
                copyClone.setAttribute("id", append);
            }
>>>>>>> .r17402

<<<<<<< .mine
            Element copyClone = (Element) _copy.clone();
=======
            elem.addContent(copyClone);
>>>>>>> .r17402

<<<<<<< .mine
            if (!Utils.getAttributeValue("name", _copy).equals("") && existJobname(elem, Utils.getAttributeValue("name", _copy))) {
                // System.out.println(Utils.getAttributeValue("name", _copy) +
                // " existiert berteits");
                String append = "copy(" + (copyClone.getChildren("job").size() + elem.getChildren().size() + 1) + ")of_" + Utils.getAttributeValue("name", copyClone);
                copyClone.setAttribute("name", append);
            } else if (!Utils.getAttributeValue("id", _copy).equals("")) {
                // System.out.println(Utils.getAttributeValue("name", _copy) +
                // " existiert berteits");
                String append = "copy(" + (elem.getChildren().size() + 1) + ")of_" + Utils.getAttributeValue("id", copyClone);
                copyClone.setAttribute("id", append);
            }
=======
        }
>>>>>>> .r17402

<<<<<<< .mine
            elem.addContent(copyClone);
=======
        updateTreeView(data);
>>>>>>> .r17402

<<<<<<< .mine
        }
=======
    }
>>>>>>> .r17402

<<<<<<< .mine
        updateTreeView(data);
=======
    /*
     * private TreeItem getCurrentItem(String text) { TreeItem retVal = null;
     * for(int i = 0; i < _tree.getItemCount(); i++ ){
     * 
     * TreeItem item = getCurrentItem(_tree.getItem(i), text); if(item != null
     * && item.getText().equals(text)) return item; }
     * 
     * return retVal; }
     */
>>>>>>> .r17402

<<<<<<< .mine
    }
=======
    /*
     * private TreeItem getCurrentItem(TreeItem parent, String text) { TreeItem
     * retVal = null; if(parent.getText().equals(text)) return parent; for(int i
     * = 0; i < parent.getItemCount(); i++ ){ return
     * getCurrentItem(parent.getItem(i), text); }
     * 
     * return retVal; }
     */
    private void removeAttributes(Element elem) {
        List l = elem.getAttributes();
        for (int i = 0; i < l.size(); i++)
            elem.removeAttribute((org.jdom.Attribute) l.get(i));
    }
>>>>>>> .r17402

<<<<<<< .mine
    /*
     * private TreeItem getCurrentItem(String text) { TreeItem retVal = null;
     * for(int i = 0; i < _tree.getItemCount(); i++ ){
     * 
     * TreeItem item = getCurrentItem(_tree.getItem(i), text); if(item != null
     * && item.getText().equals(text)) return item; }
     * 
     * return retVal; }
     */
=======
    private void copyAttr(Element elem, java.util.List attr) {
        for (int i = 0; i < attr.size(); i++) {
            org.jdom.Attribute a = (org.jdom.Attribute) attr.get(i);
            elem.setAttribute(a.getName(), a.getValue());
        }
    }
>>>>>>> .r17402

<<<<<<< .mine
    /*
     * private TreeItem getCurrentItem(TreeItem parent, String text) { TreeItem
     * retVal = null; if(parent.getText().equals(text)) return parent; for(int i
     * = 0; i < parent.getItemCount(); i++ ){ return
     * getCurrentItem(parent.getItem(i), text); }
     * 
     * return retVal; }
     */
    private void removeAttributes(Element elem) {
        List l = elem.getAttributes();
        for (int i = 0; i < l.size(); i++)
            elem.removeAttribute((org.jdom.Attribute) l.get(i));
    }
=======
    private void updateTreeView(TreeData data) {
>>>>>>> .r17402

<<<<<<< .mine
    private void copyAttr(Element elem, java.util.List attr) {
        for (int i = 0; i < attr.size(); i++) {
            org.jdom.Attribute a = (org.jdom.Attribute) attr.get(i);
            elem.setAttribute(a.getName(), a.getValue());
        }
    }
=======
        // unpdate der Formular
        // String currItemString = _tree.getSelection()[0].getText();
>>>>>>> .r17402

<<<<<<< .mine
    private void updateTreeView(TreeData data) {
=======
        if (_type == Editor.SPECIFIC_WEEKDAYS)
            _gui.updateSpecificWeekdays();
>>>>>>> .r17402

<<<<<<< .mine
        // unpdate der Formular
        // String currItemString = _tree.getSelection()[0].getText();
=======
        // if (_dom instanceof SchedulerDom &&
        // ((SchedulerDom)_dom).isLifeElement())
        // return;
>>>>>>> .r17402

<<<<<<< .mine
        if (_type == Editor.SPECIFIC_WEEKDAYS)
            _gui.updateSpecificWeekdays();
=======
        // sucht das Treeitem mit der currItemString um die gleiche Parent zu
        // selektieren.
        // TreeItem item = getCurrentItem(currItemString);
        // if(item != null)
        // _tree.setSelection(new TreeItem [] {item});
        _gui.update();
>>>>>>> .r17402

<<<<<<< .mine
        // if (_dom instanceof SchedulerDom &&
        // ((SchedulerDom)_dom).isLifeElement())
        // return;
=======
        if (_tree.getSelection()[0].getText().equals("Jobs"))
            _gui.updateJobs();
>>>>>>> .r17402

<<<<<<< .mine
        // sucht das Treeitem mit der currItemString um die gleiche Parent zu
        // selektieren.
        // TreeItem item = getCurrentItem(currItemString);
        // if(item != null)
        // _tree.setSelection(new TreeItem [] {item});
        _gui.update();
=======
        // if(_copy.getName().equals("job"))
        if (_type == Editor.JOB && !_tree.getSelection()[0].getText().endsWith("Jobs"))
            _gui.updateJob();
>>>>>>> .r17402

<<<<<<< .mine
        if (_tree.getSelection()[0].getText().equals("Jobs"))
            _gui.updateJobs();
=======
        if (_type == Editor.SCHEDULES)
            _gui.updateSchedules();
>>>>>>> .r17402

<<<<<<< .mine
        // if(_copy.getName().equals("job"))
        if (_type == Editor.JOB && !_tree.getSelection()[0].getText().endsWith("Jobs"))
            _gui.updateJob();
=======
        if (_type == Editor.ORDERS)
            _gui.updateOrders();
>>>>>>> .r17402

<<<<<<< .mine
        if (_type == Editor.SCHEDULES)
            _gui.updateSchedules();
=======
        if (_type == Editor.JOB_CHAINS || _type == Editor.JOB_CHAIN)
            _gui.updateJobChains();
>>>>>>> .r17402

<<<<<<< .mine
        if (_type == Editor.ORDERS)
            _gui.updateOrders();
=======
        _gui.expandItem(_tree.getSelection()[0].getText());
        _gui.updateTreeItem(_tree.getSelection()[0].getText());
>>>>>>> .r17402

<<<<<<< .mine
        if (_type == Editor.JOB_CHAINS || _type == Editor.JOB_CHAIN)
            _gui.updateJobChains();
=======
        _gui.updateTree("");
>>>>>>> .r17402

<<<<<<< .mine
        _gui.expandItem(_tree.getSelection()[0].getText());
        _gui.updateTreeItem(_tree.getSelection()[0].getText());
=======
        refreshTree("main");
>>>>>>> .r17402

<<<<<<< .mine
        _gui.updateTree("");
=======
        if (_dom instanceof SchedulerDom && ((SchedulerDom) _dom).isDirectory()) {
            ((SchedulerDom) _dom).setChangedForDirectory(data.getElement(), SchedulerDom.NEW);
        }
>>>>>>> .r17402

<<<<<<< .mine
        refreshTree("main");
=======
        _dom.setChanged(true);
    }
>>>>>>> .r17402

<<<<<<< .mine
        if (_dom instanceof SchedulerDom && ((SchedulerDom) _dom).isDirectory()) {
            ((SchedulerDom) _dom).setChangedForDirectory(data.getElement(), SchedulerDom.NEW);
        }
=======
    // liefert den Namen des selektierten Treeitems, wenn diese Jobs, Job_chains
    // orders, schedules ist
    private String getParentItemName() {
        if (_tree.getSelectionCount() > 0) {
            String name = _tree.getSelection()[0].getText();
            if (name.equals(SchedulerListener.JOBS) || name.equals(SchedulerListener.JOB_CHAINS) || name.equals(SchedulerListener.MONITOR) || name.equals(SchedulerListener.ORDERS) || name.equals(SchedulerListener.WEB_SERVICES)
                    || name.equals(SchedulerListener.SCHEDULES)) {
                return name;
            }
>>>>>>> .r17402

<<<<<<< .mine
        _dom.setChanged(true);
    }
=======
            if (_dom instanceof sos.scheduler.editor.conf.SchedulerDom && !((SchedulerDom) _dom).isLifeElement() && (name.equals(SchedulerListener.LOCKS) || name.equals(SchedulerListener.PROCESS_CLASSES)))
                return name;
        }
>>>>>>> .r17402

<<<<<<< .mine
    // liefert den Namen des selektierten Treeitems, wenn diese Jobs, Job_chains
    // orders, schedules ist
    private String getParentItemName() {
        if (_tree.getSelectionCount() > 0) {
            String name = _tree.getSelection()[0].getText();
            if (name.equals(SchedulerListener.JOBS) || name.equals(SchedulerListener.JOB_CHAINS) || name.equals(SchedulerListener.MONITOR) || name.equals(SchedulerListener.ORDERS) || name.equals(SchedulerListener.WEB_SERVICES)
                    || name.equals(SchedulerListener.SCHEDULES)) {
                return name;
            }
=======
        return "";
    }
>>>>>>> .r17402

<<<<<<< .mine
            if (_dom instanceof sos.scheduler.editor.conf.SchedulerDom && !((SchedulerDom) _dom).isLifeElement() && (name.equals(SchedulerListener.LOCKS) || name.equals(SchedulerListener.PROCESS_CLASSES)))
                return name;
        }
=======
    // liefert den Namen des selektierten Treeitems, wenn diese Job, Job chain
    // order, add_order, schedule ist
    private Element getItemElement() {
        if (_tree.getSelectionCount() > 0) {
            Element elem = ((TreeData) (_tree.getSelection()[0].getData())).getElement();
            String name = elem.getName();
            if (name.equals("job") || name.equals("job_chain") || name.equals("order") || name.equals("add_order") || name.equals("web_service") || name.equals("monitor") || name.equals("schedule")) {
                return elem;
            }
        }
        return null;
    }
>>>>>>> .r17402

<<<<<<< .mine
        return "";
    }
=======
    // liefert den namen des Hot Folders
    private String getHotFolderName(String newXML) {
        String newName = "";
>>>>>>> .r17402

<<<<<<< .mine
    // liefert den Namen des selektierten Treeitems, wenn diese Job, Job chain
    // order, add_order, schedule ist
    private Element getItemElement() {
        if (_tree.getSelectionCount() > 0) {
            Element elem = ((TreeData) (_tree.getSelection()[0].getData())).getElement();
            String name = elem.getName();
            if (name.equals("job") || name.equals("job_chain") || name.equals("order") || name.equals("add_order") || name.equals("web_service") || name.equals("monitor") || name.equals("schedule")) {
                return elem;
            }
        }
        return null;
    }
=======
        if (_dom instanceof SchedulerDom) {
>>>>>>> .r17402

<<<<<<< .mine
    // liefert den namen des Hot Folders
    private String getHotFolderName(String newXML) {
        String newName = "";
=======
            if (((SchedulerDom) _dom).isDirectory() || ((SchedulerDom) _dom).isLifeElement()) {
>>>>>>> .r17402

<<<<<<< .mine
        if (_dom instanceof SchedulerDom) {
=======
                if (getElement().getName().equals("order") || getElement().getName().equals("add_order")) {
>>>>>>> .r17402

<<<<<<< .mine
            if (((SchedulerDom) _dom).isDirectory() || ((SchedulerDom) _dom).isLifeElement()) {
=======
                    int i1 = -1;
                    int i2 = -1;
>>>>>>> .r17402

<<<<<<< .mine
                if (getElement().getName().equals("order") || getElement().getName().equals("add_order")) {
=======
                    i1 = newXML.indexOf("id=\"");
                    i2 = newXML.indexOf("\"", i1 + "id=\"".length());
                    if (i1 > +"id=\"".length() && i2 > i1)
                        newName = newXML.substring(i1 + "id=\"".length(), i2) + ",";
>>>>>>> .r17402

<<<<<<< .mine
                    int i1 = -1;
                    int i2 = -1;
=======
                    i1 = -1;
                    i2 = -1;
                    i1 = newXML.indexOf("job_chain=\"");
                    i2 = newXML.indexOf("\"", i1 + "job_chain=\"".length());
                    if (i1 > +"job_chain=\"".length() && i2 > i1)
                        newName = newName + newXML.substring(i1 + +"job_chain=\"".length(), i2);
>>>>>>> .r17402

<<<<<<< .mine
                    i1 = newXML.indexOf("id=\"");
                    i2 = newXML.indexOf("\"", i1 + "id=\"".length());
                    if (i1 > +"id=\"".length() && i2 > i1)
                        newName = newXML.substring(i1 + "id=\"".length(), i2) + ",";

                    i1 = -1;
                    i2 = -1;
                    i1 = newXML.indexOf("job_chain=\"");
                    i2 = newXML.indexOf("\"", i1 + "job_chain=\"".length());
                    if (i1 > +"job_chain=\"".length() && i2 > i1)
                        newName = newName + newXML.substring(i1 + +"job_chain=\"".length(), i2);

                } else {
                    int i1 = newXML.indexOf("name=\"") + "name=\"".length();
                    int i2 = newXML.indexOf("\"", i1);
                    newName = newXML.substring(i1, i2);
                }
            }
        }
        return newName;
    }
=======
                } else {
                    int i1 = newXML.indexOf("name=\"") + "name=\"".length();
                    int i2 = newXML.indexOf("\"", i1);
                    newName = newXML.substring(i1, i2);
                }
            }
        }
        return newName;
    }
>>>>>>> .r17402
}