package sos.scheduler.editor.app;

import java.io.BufferedWriter;
import java.io.File;
import java.io.StringReader;

import org.eclipse.swt.SWT;
import org.eclipse.swt.dnd.Clipboard;
import org.eclipse.swt.dnd.TextTransfer;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.program.Program;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Tree;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;

import sos.scheduler.editor.conf.DetailDom;
import sos.scheduler.editor.conf.SchedulerDom;
import sos.scheduler.editor.conf.forms.SchedulerForm;
import sos.util.SOSFile;
import sos.util.SOSFileOperations;

public class TreeMenu {

    private DomParser               _dom                  = null;

    private Tree                    _tree                 = null;

    private Menu                    _menu                 = null;

    private Clipboard               _cb                   = null;

    private static Element          _copy                 = null;

    private SchedulerForm           _gui                  = null;

    private static final String EDIT_XML                  = "Edit XML";
    
    private static final String SHOW_XML                  = "Show XML";
    
    private static final String SHOW_INFO                 = "Show Info";
    
    private static final String COPY                      = "Copy";
    
    private static final String COPY_TO_CLIPBOARD         = "Copy to Clipboard";
    
    private static final String PASTE                     = "Paste";
    
    
    private boolean bEdit                                 = false;//hilfsvariable
    
   

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
    		TreeData data = (TreeData) _tree.getSelection()[0].getData();
    		if (data != null && data instanceof TreeData) {
    			if (data.getChild() != null) {
    				
    				if(data.getElement().getChild(data.getChild()) == null)                		
    					data.getElement().addContent(new Element(data.getChild()));
    				
    				return data.getElement().getChild(data.getChild());
    				
    			} else {
    				if(data.getElement().getName().equals("at") || data.getElement().getName().equals("date")) {
    					return data.getElement().getParentElement();
    				} else
    					return data.getElement();
    			}
    		} else
    			return null;
    	} else
    		return null;
    }
    

    private void createMenu() {
        _menu = new Menu(_tree.getShell(), SWT.POP_UP);
        
        MenuItem item = new MenuItem(_menu, SWT.PUSH);
        item.addListener(SWT.Selection, getXMLListener());
        item.setText(TreeMenu.EDIT_XML);
        
        item = new MenuItem(_menu, SWT.SEPARATOR);
        
        item = new MenuItem(_menu, SWT.PUSH);
        item.addListener(SWT.Selection, getXMLListener());
        item.setText(TreeMenu.SHOW_XML);

        item = new MenuItem(_menu, SWT.PUSH);
        item.addListener(SWT.Selection, getInfoListener());
        item.setText(TreeMenu.SHOW_INFO);
        
        item = new MenuItem(_menu, SWT.SEPARATOR);

        item = new MenuItem(_menu, SWT.PUSH);
        item.addListener(SWT.Selection, getCopyListener());
        item.setText(TreeMenu.COPY);

        item = new MenuItem(_menu, SWT.PUSH);
        item.addListener(SWT.Selection, getClipboardListener());
        item.setText(TreeMenu.COPY_TO_CLIPBOARD);

        item = new MenuItem(_menu, SWT.SEPARATOR);

        item = new MenuItem(_menu, SWT.PUSH);
        item.addListener(SWT.Selection, getPasteListener());
        item.setText(TreeMenu.PASTE);

        _menu.addListener(SWT.Show, new Listener() {
            public void handleEvent(Event e) {
                MenuItem[] items = _menu.getItems();
                if(_copy == null)
                	disableMenu();
                if (_tree.getSelectionCount() > 0) {
                    Element element = getElement();
                    if (element != null ) {
                        
                    	getItem(TreeMenu.EDIT_XML).setEnabled(true); 
                    	getItem(TreeMenu.SHOW_INFO).setEnabled(true); // show info
                    	getItem(TreeMenu.SHOW_XML).setEnabled(true); // show xml
                    	getItem(TreeMenu.COPY_TO_CLIPBOARD).setEnabled(true); // copy to clipboard
                    	

                        String name = element.getName();

                        if (name.equals("job") || name.equals("config") || name.equals("run_time"))
                        	getItem(TreeMenu.COPY).setEnabled(true); // copy
                        else
                        	getItem(TreeMenu.COPY).setEnabled(false); // copy
                       
                        if (_copy != null) {
                            String cName = _copy.getName();

                            MenuItem _paste = getItem(TreeMenu.PASTE);
                            if (name.equals("jobs") && cName.equals("job"))
                            	_paste.setEnabled(true); // paste
                            else if (name.equals("job") && cName.equals("run_time"))
                            	_paste.setEnabled(true); // paste
                            else if (name.equals("config") && cName.equals("config"))
                            	_paste.setEnabled(true); // paste
                            else
                            	_paste.setEnabled(false); // paste
                        }
                    }
                }
            }
        });
    }


    public Menu getMenu() {
        return _menu;
    }


    private void disableMenu() {
        MenuItem[] items = _menu.getItems();
        for (int i = 0; i < items.length; i++)
            items[i].setEnabled(false);
    }


    private Listener getInfoListener() {
        return new Listener() {
            public void handleEvent(Event e) {
                Element element = getElement();
                if (element != null) {
                    try {
                        String filename = _dom.transform(element);

                        Program prog = Program.findProgram("html");
                        if (prog != null)
                            prog.execute(filename);
                        else {
                            Runtime.getRuntime().exec(Options.getBrowserExec(filename, null));
                        }
                    } catch (Exception ex) {
                        ex.printStackTrace();
                        message("Error: " + ex.getMessage(), SWT.ICON_ERROR | SWT.OK);
                    }
                }
            }
        };
    }


    private String getXML() {
        Element element = getElement();
        String xml = "";
        if (element != null) {
            try {
                xml = _dom.getXML(element);
            } catch (JDOMException ex) {
                message("Error: " + ex.getMessage(), SWT.ICON_ERROR | SWT.OK);
                return null;
            }
        }
        return xml;
    }


/*    private void copyClipboard(String content) {

        if (_cb == null)
            _cb = new Clipboard(_tree.getDisplay());

        TextTransfer transfer = TextTransfer.getInstance();
        _cb.setContents(new Object[] { content }, new Transfer[] { transfer });

    }
*/
 
    private Listener getXMLListener() {
    	
        return new Listener() {
            public void handleEvent(Event e) {
            	MenuItem i = (MenuItem)e.widget;
            	Element element = null;
            	if(i.getText().equalsIgnoreCase(TreeMenu.EDIT_XML)) {
            		element = _dom.getRoot().getChild("config");
            	} else {
            		element = getElement();
            	}
            		if (element != null) {
            			String xml = getXML();
            			
            			if (xml == null) // error
            				return;
            		
            			String newXML = Utils.showClipboard(xml, _tree.getShell(), i.getText().equalsIgnoreCase(TreeMenu.EDIT_XML));
            			
            			//newXML ist null, wenn Änderungen nicht übernommen werden sollen
            			if(newXML != null)
            				applyXMLChange(newXML);
            			
            		}
            	}
            
        };
    }

    private void applyXMLChange(String newXML){
    	
    	newXML = newXML.replaceAll("\\?>", "?><spooler>" )+ "</spooler>";
    	
    	//System.out.println("debug: \n" + newXML);
    	    	
    	try {
    		    		
    		_dom.readString(newXML, true);
    		_gui.update();
    		
    		refreshTree("main");
    		
    		
    	} catch (Exception de) {    		    		
    		MainWindow.message(MainWindow.getSShell(), "..error while update XML: " + de.getMessage(), SWT.ICON_WARNING );    		
    	} 
    }

    private Listener getCopyListener() {
        return new Listener() {
            public void handleEvent(Event e) {
                Element element = getElement();
                if (element != null)
                    _copy = (Element) element.clone();
            }
        };
    }


    private Listener getClipboardListener() {
        return new Listener() {
            public void handleEvent(Event e) {
                Element element = getElement();
                if (element != null) {
                    String xml = getXML();
                    if (xml == null) // error
                        return;

                    Utils.copyClipboard(xml, _tree.getDisplay());
                }
            }
        };
    }
 

    private Listener getPasteListener() {
        return new Listener() {
            public void handleEvent(Event e) {
                Element target = getElement();
                if (target != null && _copy != null) {
                    String tName = target.getName();
                    String cName = _copy.getName();

                    if (tName.equals("jobs") && cName.equals("job")) { // copy job
                        
                        String append = "copy(" + (target.getChildren("job").size() + 1);
                        Element currCopy = (Element)_copy.clone();
                        
                        if(existJobname(target, Utils.getAttributeValue("name", _copy)))
                        	currCopy.setAttribute("name", append + ")of_" + Utils.getAttributeValue("name", _copy));
                        
                        target.addContent(currCopy);
                        
                         refreshTree("jobs");
                         _gui.update();
                         _gui.updateJobs();
                        
                    } else if (tName.equals("job") && cName.equals("run_time")) { // copy
                        // run_time
                        target.removeChildren("run_time");
                        target.addContent(_copy);
                        _gui.updateJob();
                    } else if (tName.equals("config") && cName.equals("config")) { // copy
                        // run_time
                    	  //target.getParentElement().removeContent();
                    	  Element spooler = target.getParentElement();
                    	  spooler.removeChildren("config");
                    	  spooler.addContent((Element)_copy.clone());
                    	  
                    	  refreshTree("main");
                    	  _dom.setChanged(true);
                    	  
                    	  _gui.update();
                    }
                }
            }
        };
    }

    private boolean existJobname(Element jobs, String jobname) {
    	boolean retVal = false;
    	java.util.List list = jobs.getChildren();
    	for (int i = 0; i < list.size(); i++) {
    		Element job = (Element)list.get(i);
    		if(Utils.getAttributeValue("name", job).equalsIgnoreCase(jobname)) {
    			retVal = true;
    			break;
    		}
    	}
    	return retVal;
    }
    
    private void refreshTree(String whichItem) {
    	sos.scheduler.editor.app.IContainer con = MainWindow.getContainer(); 
		SchedulerForm sf = (SchedulerForm)(con.getCurrentEditor());
		sf.updateTree(whichItem);
		
    }
    
    private MenuItem getItem(String name) {
    	MenuItem[] items = _menu.getItems();
    	for (int i = 0; i < items.length; i++) {
    		MenuItem item = items[i];
    		if(item.getText().equalsIgnoreCase(name)) {
    			return item;
    		}
    	}
    	return null;
    }
    
}
