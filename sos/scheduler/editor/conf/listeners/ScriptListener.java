package sos.scheduler.editor.conf.listeners;

import java.util.Iterator;
import java.util.List;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.SWT;
import org.jdom.CDATA;
import org.jdom.Element;
import sos.scheduler.editor.app.Editor;
import sos.scheduler.editor.app.MainWindow;
import sos.scheduler.editor.app.Utils;
import sos.scheduler.editor.conf.ISchedulerUpdate;
import sos.scheduler.editor.conf.SchedulerDom;

public class ScriptListener {
	
	
    public final static int      NONE        = 0;

    public final static int      JAVA        = 1;

    public final static int      JAVA_SCRIPT = 2;

    public final static int      PERL        = 3;

    public final static int      VB_SCRIPT   = 4;
    
    public final static int      SHELL       = 5;

    public final static int      COM         = 6;

    public final static String[] _languages  = { "", "java", "javascript", "perlScript", "VBScript", "shell","" };

    private SchedulerDom         _dom        = null;

    private Element              _parent     = null;

    private Element              _script     = null;

    private int                  _type       = -1;
    
    private ISchedulerUpdate     _update     = null;


    public ScriptListener(SchedulerDom dom, Element parent, int type, ISchedulerUpdate update) {
        _dom = dom;
        _parent = parent;
        _type = type;
        _update = update;
        setScript();
    }


    private void setScript() {
        if (_type == Editor.MONITOR) {
            //Element monitor = _parent.getChild("monitor");
        	Element monitor = _parent;
            if (monitor != null) {
                _script = monitor.getChild("script");
                //if (_script == null) monitor.detach();
            }
        } else
            _script = _parent.getChild("script");
    }


    public int languageAsInt(String language) {
        for (int i = 0; i < _languages.length; i++) {
            if (_languages[i].equalsIgnoreCase(language))
                return i;
        }

        if (_script != null && (_script.getAttribute("com_class") != null || _script.getAttribute("filename") != null))
            return COM;

        System.out.println("unknown language: " + language + " - set to java...");
        if (_script != null)
            _script.setAttribute("language", "java");
        return -1;
    }


    private String languageAsString(int language) {
        return _languages[language];
    }

    public String getLanguage(int language) {
        return _languages[language];
    }

    public int getLanguage() {
        if (_script != null)
            return languageAsInt(_script.getAttributeValue("language"));
        else
            return NONE;
    }


    public void setLanguage(int language) {
        setScript();
        if (_script == null && language != NONE) {
            // init script element
            _script = new Element("script");
            if (_type == Editor.MONITOR) {
                //Element monitor = _parent.getChild("monitor");
            	Element monitor = _parent;
                if (monitor == null) {
                    monitor = new Element("monitor");
                    _parent.addContent(monitor);
                }
                monitor.addContent(_script);
            } else
                _parent.addContent(_script);
        }

        if (_script != null) {
            switch (language) {
                case NONE: // remove script element
                    /*if (_type == Editor.MONITOR)
                        _parent.removeChild("monitor");
                    else*/
                        _parent.removeChildren("script");
                    _script = null;
                    break;
                case PERL:
                case JAVA_SCRIPT:
                case VB_SCRIPT:
                case SHELL:
                    _script.removeAttribute("com_class");
                    _script.removeAttribute("filename");
                    _script.removeAttribute("java_class");
                    break;
                case JAVA:
                    if (_script.getAttribute("java_class") == null)
                        _script.setAttribute("java_class", "");
                    _script.removeAttribute("com_class");
                    _script.removeAttribute("filename");
                    break;
                case COM:
                    if (_script.getAttribute("com_class") == null)
                        _script.setAttribute("com_class", "");
                    if (_script.getAttribute("filename") == null)
                        _script.setAttribute("filename", "");
                    _script.removeAttribute("java_class");
                    setSource("");
                    break;
            }

            if (language != NONE)
                Utils.setAttribute("language", languageAsString(language), _script, _dom);

            _dom.setChanged(true);            
            
            setChangedForDirectory();
        }
    }


    private void setAttributeValue(String element, String value, int language) {
        if (getLanguage() == language) {
            _script.setAttribute(element, value);
            _dom.setChanged(true);
            setChangedForDirectory();
        }
    }


    public String getJavaClass() {
        return Utils.getAttributeValue("java_class", _script);
    }


    public void setJavaClass(String javaClass) {
        setAttributeValue("java_class", javaClass.trim(), JAVA);
        setChangedForDirectory();
    }


    public String getComClass() {
        return Utils.getAttributeValue("com_class", _script);
    }


    public void setComClass(String comClass) {
        setAttributeValue("com_class", comClass.trim(), COM);
        setChangedForDirectory();
    }


    public String getFilename() {
        return Utils.getAttributeValue("filename", _script);
    }


    public void setFilename(String filename) {
        setAttributeValue("filename", filename, COM);
    }

    public void fillTable(Table table) {
    	if (_script != null) {
    		table.removeAll();
    		List includeList = _script.getChildren("include");
    		for(int i = 0; i < includeList.size(); i++) {
    			Element include = (Element) includeList.get(i);
    			
    			if(include.getAttributeValue("file") != null) {
    				TableItem item = new TableItem(table, SWT.NONE); 
    				item.setText(0, Utils.getAttributeValue("file", include));
    				item.setText(1, "file");
    			} else {
    				TableItem item = new TableItem(table, SWT.NONE); 
    				item.setText(0, Utils.getAttributeValue("live_file", include));
    				item.setText(1, "live_file");
    			}    			
    		}
    	}
    }
    
    public String[] getIncludes() {
        if (_script != null) {
            List includeList = _script.getChildren("include");            
            String[] includes = new String[includeList.size()];
            Iterator it = includeList.iterator();
            int i = 0;
            while (it.hasNext()) {
                Element include = (Element) it.next();
                String file = "";
                if(include.getAttribute("live_file")!=null)
                	file = include.getAttributeValue("live_file");
                else 
                	file = include.getAttributeValue("file");
                
                includes[i++] = file == null ? "" : file;
            }
            return includes;
        } else
            return new String[0];
    }


    public void addInclude(Table table, String filename, boolean isLife) {
    	if (_script != null) {
            List includes = _script.getChildren("include");
            _script.addContent(includes.size(), new Element("include").setAttribute((isLife?"live_file":"file"), filename));            
            _dom.setChanged(true);
            fillTable(table);
            setChangedForDirectory();
            
            
        } else
            System.out.println("no script element defined!");
    }
    
    public void addInclude(String filename) {
        if (_script != null) {
            List includes = _script.getChildren("include");
            _script.addContent(includes.size(), new Element("include").setAttribute("file", filename));
            _dom.setChanged(true);
            setChangedForDirectory();
        } else
            System.out.println("no script element defined!");
    }

  /*  private void removeScriptSource() {
      String includes[] = getIncludes();

      _script.removeContent();

      for (int i = 0; i < includes.length; i++) {
        addInclude(includes[i]);
       }
    }
*/
    public void removeInclude(int index) {
        if (_script != null) {
            List includeList = _script.getChildren("include");
            if (index >= 0 && index < includeList.size()) {
                includeList.remove(index);
                _dom.setChanged(true);
                setChangedForDirectory();
            } else
                System.out.println("index " + index + " is out of range for include!");
        } else
            System.out.println("no script element defined!");
    }


    public String getSource() {
        if (_script != null) {
            return _script.getTextTrim();
        } else
            return "";
    }


    /*public void deleteScript() {
      //    if (_script != null) 	_script.removeContent();
      if (_script != null) 	removeScriptSource();
    }*/
    
    public void setSource(String source) {

    	try {
    		
    		if (_script != null) {
    			List l = _script.getContent();
				for(int i = 0; i < l.size(); i++) {
					if(l.get(i) instanceof CDATA)
						l.remove(i);
				}
    			if (!source.equals("")) {
    				
    				_script.addContent(new CDATA(source));
    			} 

    			_dom.setChanged(true);
    			setChangedForDirectory();
    		} else
    			System.out.println("no script element defined!");

    	}catch (org.jdom.IllegalDataException jdom) {
			try {
				new sos.scheduler.editor.app.ErrorLog("error in " + sos.util.SOSClassUtil.getMethodName() , jdom);
			} catch(Exception ee) {
				//tu nichts
			}

    		MainWindow.message(jdom.getMessage(), SWT.ICON_ERROR);
    	}
    	catch (Exception e) {
			try {
				new sos.scheduler.editor.app.ErrorLog("error in " + sos.util.SOSClassUtil.getMethodName() , e);
			} catch(Exception ee) {
				//tu nichts
			}

    		MainWindow.message(e.getMessage(), SWT.ICON_ERROR);
    		System.out.println(e);
    	}
    }


	public Element getParent() {
		return _parent;
	}
	
	public String getName() {
        return Utils.getAttributeValue("name", _parent);
    }

    public void setName(String name) {
        Utils.setAttribute("name", name, _parent);
        if(_update != null)
        	_update.updateTreeItem(name);
        
        _dom.setChanged(true);   
        
        setChangedForDirectory();
    }

    public String getOrdering() {
        return Utils.getAttributeValue("ordering", _parent);
    }

    public void setOrdering(String ordering) {
        Utils.setAttribute("ordering", ordering, "0", _parent);
        setChangedForDirectory();
    }

    private void setChangedForDirectory() {
    	if (_dom.isDirectory() || _dom.isLifeElement()) {
    		if(_parent != null) {
    			Element job = _parent;
    			if(!job.getName().equals(_parent))
    				job = Utils.getJobElement(_parent);	
    			_dom.setChangedForDirectory("job", Utils.getAttributeValue("name",job), SchedulerDom.MODIFY);
    		}
    	}
    }
}
