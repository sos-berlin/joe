package sos.scheduler.editor.actions.listeners;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;

import sos.scheduler.editor.app.Editor;
import sos.scheduler.editor.app.Utils;
import sos.scheduler.editor.actions.forms.ActionsForm;
import sos.scheduler.editor.actions.ActionsDom;
import sos.scheduler.editor.app.Options;

public class ParameterListener {


	private         ActionsForm      _main                    = null;

	private         ActionsDom          _dom                     = null;

	private         Element               _parent                  = null;

	private         List                  _params                  = null;

	private         List                  _environments            = null;

	private         List                  _includeParams           = null;

	private  static HashMap               parameterDescription     = new HashMap();

	private  static HashMap               parameterRequired        = new HashMap();
	//default ist config
	private         int                   type                     = Editor.CONFIG;


	public ParameterListener(ActionsDom dom, Element parent, ActionsForm update, int type_) {


		_dom = dom;
		_parent = parent;
		_main = update;
		type = type_;

		Element params = _parent.getChild("params");
		if (params != null) {			
			_params = params.getChildren();			
			_includeParams = params.getChildren("include");
		}

		Element environment = _parent.getChild("environment");
		if(environment != null)
			_environments = environment.getChildren("variable");

	}


	private void initParams() {
		Element params = _parent.getChild("params");
		if (params != null) {
			_params = params.getChildren();
			_includeParams = params.getChildren("include");
		} else {
			_parent.addContent(0, new Element("params"));
			_params = _parent.getChild("params").getChildren();
			_includeParams = _parent.getChild("params").getChildren("include");
		}
	}



	public void fillParams(Table table) {
		if (_params != null) {
			Iterator it = _params.iterator();
			while (it.hasNext()) {
				Object o = it.next();
				if (o instanceof Element) {	
					Element e = (Element) o;
					if (e.getName().equals("copy_params") && type == Editor.COMMANDS) {
						TableItem item = new TableItem(table, SWT.NONE);
						item.setText(0, "<from>");
						item.setText(1, ((Element) o).getAttributeValue("from"));
					} else if (e.getName().equals("param")){
						if(e.getAttributeValue("name") != null) {
							TableItem item = new TableItem(table, SWT.NONE);
							item.setText(0, ((Element) o).getAttributeValue("name"));
							item.setText(1, (((Element) o).getAttributeValue("value") != null ? ((Element) o).getAttributeValue("value") : ""));
							if(parameterDescription != null) {
								item.setData("parameter_description_de", parameterDescription.get("parameter_description_de_" + ((Element) o).getAttributeValue("name")));
								item.setData("parameter_description_en", parameterDescription.get("parameter_description_en_" + ((Element) o).getAttributeValue("name")));
							}
							if(parameterRequired != null && isParameterRequired(((Element) o).getAttributeValue("name")))
								item.setBackground(Options.getRequiredColor());
						} 
					}
				}
			}						
		}		
		
	} 

	public void fillParams(ArrayList listOfParams, Table table, boolean refreshTable) {	

		if(refreshTable) {
			if(_params!=null)
				_params.clear();		
			table.removeAll();
		}

		for (int i =0; i < listOfParams.size(); i++) {
			HashMap h = (HashMap)listOfParams.get(i);                
			if (h.get("name") != null) {
				TableItem item = existsParams(h.get("name").toString(), table, (h.get("default_value") != null? h.get("default_value").toString(): ""));
				if(!refreshTable && item != null) {					
					if(h.get("required") != null && h.get("required").equals("true"))
						item.setBackground(Options.getRequiredColor());

					//existParam = true;
				} else {
					String pname = h.get("name").toString();
					String pvalue = (h.get("default_value") != null ? h.get("default_value").toString() : "");
					String desc_de = (h.get("description_de") != null ? h.get("description_de").toString() : ""); 
					String desc_en = (h.get("description_en") != null ? h.get("description_en").toString() : "");
					saveParameter(table, pname, pvalue, desc_de, desc_en, (h.get("required")!=null ? h.get("required").equals("true"):false));					
				}
			}           
		}

	}

	public void fillEnvironment(Table table) {
		if (_environments != null) {
			Iterator it = _environments.iterator();
			while (it.hasNext()) {
				Object o = it.next();
				if (o instanceof Element) {					
					TableItem item = new TableItem(table, SWT.NONE);
					item.setText(0, ((Element) o).getAttributeValue("name"));
					item.setText(1, (((Element) o).getAttributeValue("value") != null ? ((Element) o).getAttributeValue("value") : ""));					
				}
			}						
		}				

	} 

	public void fillIncludeParams(Table table) {
		if (_includeParams != null) {
			Iterator it = _includeParams.iterator();
			while (it.hasNext()) {
				Object o = it.next();
				if (o instanceof Element) {					
					TableItem item = new TableItem(table, SWT.NONE);
					Element elem = (Element) o; 
					if(elem.getAttribute("file") != null) {
						item.setText(0, Utils.getAttributeValue("file", elem));
						item.setText(2, "file");
					} else { 
						item.setText(0, Utils.getAttributeValue("live_file", elem));
						item.setText(2, "live_file");
					}
					item.setText(1, (((Element) o).getAttributeValue("node") != null ? ((Element) o).getAttributeValue("node") : ""));

				}
			}						
		}				
	} 


	public TableItem existsParams(String name, Table table, String replaceValue) {

		try {
			for (int i =0; i < table.getItemCount(); i++) {
				if(table.getItem(i).getText(0).equals(name)) {					
					return table.getItem(i);
				}
			}
		} catch (Exception e) {
			try {
				new sos.scheduler.editor.app.ErrorLog("error in " + sos.util.SOSClassUtil.getMethodName() , e);
			} catch(Exception ee) {
				//tu nichts
			}

			System.out.println("error in ParameterListener.existsParams " + e.getMessage());
		}
		return null;
	}

	public void deleteParameter(Table table, int index) {


		if (_params != null) {
			_params.remove(index);
			_dom.setChanged(true);
			//if(type == Editor.JOB) _dom.setChangedForDirectory("job", Utils.getAttributeValue("name",_parent), SchedulerDom.MODIFY);
			//Utils.setChangedForDirectory(_parent, _dom);
		}
		if(_params.size() == 0) {
			_parent.removeChild("params");
		}
		table.remove(index);

	}					

	/*
	public void deleteEnvironment(Table table, int index) {


		if (_environments != null) {
			_environments.remove(index);
			_dom.setChanged(true);
			//_dom.setChangedForDirectory("job", Utils.getAttributeValue("name",_parent), SchedulerDom.MODIFY);
			//Utils.setChangedForDirectory(_parent, _dom);
			//if(type == Editor.JOB) _dom.setChangedForDirectory("job", Utils.getAttributeValue("name",_parent), SchedulerDom.MODIFY);
		}
		table.remove(index);

	}	
*/

	public void deleteIncludeParams(Table table, int index) {


		if (_includeParams != null) {
			_includeParams.remove(index);
			_dom.setChanged(true);						
			//if(type == Editor.JOB) _dom.setChangedForDirectory("job", Utils.getAttributeValue("name",_parent), SchedulerDom.MODIFY);
			//Utils.setChangedForDirectory(_parent, _dom);
		}
		table.remove(index);

	}	

	public void saveParameter(Table table, String name, String value, String parameterDescription_de, String parameterDescription_en, boolean required) {

		Element e = new Element("param");				
		e.setAttribute("name", name);
		e.setAttribute("value", value);


		/*if((_dom.isLifeElement() || _dom.isDirectory()) && _params == null) {
			Element params = _parent.getChild("params");
			if (params != null)				
				_params = params.getChildren();
		}*/

		if (_params == null)
			initParams();
		_params.add(e);

		TableItem item = new TableItem(table, SWT.NONE);
		item.setText(new String[] { name, value });

		if(parameterDescription_de!=null && parameterDescription_de.trim().length()>0) {
			item.setData("parameter_description_de", parameterDescription_de);
			parameterDescription.put( "parameter_description_de_"+ name, parameterDescription_de);
		}
		if(parameterDescription_en!=null && parameterDescription_en.trim().length()>0) {
			item.setData("parameter_description_en", parameterDescription_en);
			parameterDescription.put( "parameter_description_en_"+ name, parameterDescription_de);
		}


		if(required) {
			item.setBackground(Options.getRequiredColor());
		}
		_dom.setChanged(true);
		
		//if(type == Editor.JOB) _dom.setChangedForDirectory("job", Utils.getAttributeValue("name",_parent), SchedulerDom.MODIFY);
		//Utils.setChangedForDirectory(_parent, _dom);
	}


	public void saveIncludeParams(Table table, String file, String node, boolean isLive) {

		boolean found = false;

		if (_includeParams != null) {
			int index = 0;
			Iterator it = _includeParams.iterator();
			while (it.hasNext()) {
				Object o = it.next();
				if (o instanceof Element) {
					Element e = (Element) o;
					if ((file.equals(e.getAttributeValue("live_file")) || file.equals(e.getAttributeValue("file"))) && (node.equals(e.getAttributeValue("node")) || table.getSelectionCount() > 0 )) {
						found = true;																								
						e.removeAttribute("live_file");
						e.removeAttribute("file");
						if(isLive)
							e.setAttribute("live_file", file);
						else
							e.setAttribute("file", file);
						Utils.setAttribute("node", node, e);
						_dom.setChanged(true);						
						//if(type == Editor.JOB) _dom.setChangedForDirectory("job", Utils.getAttributeValue("name",_parent), SchedulerDom.MODIFY);
						//Utils.setChangedForDirectory(_parent, _dom);
						table.getItem(index).setText(1, node);
						table.getItem(index).setText(2, (isLive ? "live_file" : "file"));
						break;
					}
					index++;
				}
			}
		}
		if (!found) {
			Element e = new Element("include");
			if(isLive)
				e.setAttribute("live_file", file);
			else
				e.setAttribute("file", file);

			e.setAttribute("node", node);
			_dom.setChanged(true);			
			//if(type == Editor.JOB) _dom.setChangedForDirectory("job", Utils.getAttributeValue("name",_parent), SchedulerDom.MODIFY);
			//Utils.setChangedForDirectory(_parent, _dom);

			if (_includeParams == null)
				initParams();

			_includeParams.add(e);

			TableItem item = new TableItem(table, SWT.NONE);
			item.setText(new String[] { file, node, (isLive ? "live_file" : "file")});


		}			
	}

	/*public void saveEnvironment(Table table, String name, String value) {

		boolean found = false;

		if (_environments != null) {
			int index = 0;
			Iterator it = _environments.iterator();
			while (it.hasNext()) {
				Object o = it.next();
				if (o instanceof Element) {
					Element e = (Element) o;
					if (name.equals(e.getAttributeValue("name"))) {
						found = true;												
						//Utils.setAttribute("value", value, e);
						e.setAttribute("value", value);
						_dom.setChanged(true);
						if(type == Editor.JOB) _dom.setChangedForDirectory("job", Utils.getAttributeValue("name",_parent), SchedulerDom.MODIFY);
						//Utils.setChangedForDirectory(_parent, _dom);
						table.getItem(index).setText(1, value);
						break;
					}
					index++;
				}
			}
		}
		if (!found) {
			Element e = new Element("variable");
			e.setAttribute("name", name);
			e.setAttribute("value", value);
			_dom.setChanged(true);
			//if(type == Editor.JOB) _dom.setChangedForDirectory("job", Utils.getAttributeValue("name",_parent), SchedulerDom.MODIFY);
			//Utils.setChangedForDirectory(_parent, _dom);

			if (_environments == null)
				initEnvironment();
			_environments.add(e);

			TableItem item = new TableItem(table, SWT.NONE);
			item.setText(new String[] { name, value });

		}			
	}
*/
	public void saveParameter(Table table, String name, String value) {
		boolean found = false;
		if (_params != null) {

			if (name.equals("<from>") && type == Editor.COMMANDS) {
				found = (table.getSelectionIndex() > -1);
			} else {
				int index = 0;
				Iterator it = _params.iterator();
				while (it.hasNext()) {
					Object o = it.next();
					if (o instanceof Element) {
						Element e = (Element) o;

						if (e.getName().equals("param")) {
							if (name.equals(e.getAttributeValue("name"))) {
								found = true;
								e.setAttribute("value", value);
								_dom.setChanged(true);
								//if(type == Editor.JOB) _dom.setChangedForDirectory("job", Utils.getAttributeValue("name",_parent), SchedulerDom.MODIFY);
								//Utils.setChangedForDirectory(_parent, _dom);
								table.getItem(index).setText(1, value);
							}

							index++;
						}
					}
				}
			}

			if (name.equals("<from>") && found && type == Editor.COMMANDS) {
				int index = table.getSelectionIndex();
				table.getItem(index).setText(0, name);
				table.getItem(index).setText(1, value);
				Element e = (Element) _params.get(index);
				e.setName("copy_params");
				e.setAttribute("from", value);
				e.removeAttribute("name");
				e.removeAttribute("value");
				_dom.setChanged(true);				
				//if(type == Editor.JOB) _dom.setChangedForDirectory("job", Utils.getAttributeValue("name",_parent), SchedulerDom.MODIFY);
			}
			
		}

		if (!found) {
			Element e = new Element("param");
			if (!name.equals("<from>")) {
				e.setAttribute("name", name);
				e.setAttribute("value", value);
			} else {
				e.setName("copy_params");
				e.setAttribute("from", value);
			}

			_dom.setChanged(true);	
			
			
			
			//if(type == Editor.JOB) _dom.setChangedForDirectory("job", Utils.getAttributeValue("name",_parent), SchedulerDom.MODIFY);
			if (_params == null)
				initParams();
			if (_params != null)
				_params.add(e);

			TableItem item = new TableItem(table, SWT.NONE);
			item.setText(new String[] { name, value });
			
			
		}
		////Utils.setChangedForDirectory(_parent, _dom);
		
	}
	

	public ActionsDom get_dom() {
		return _dom;
	}


	public ActionsForm get_main() {
		return _main;
	}


	public Element getParent() {
		return _parent;
	}    

	public void getAllParameterDescription() {
		String xmlPaths = sos.scheduler.editor.app.Options.getSchedulerHome() ;
		String include = "";
		Element desc = _parent.getChild("description");
		if (desc != null) {
			Element inc = desc.getChild("include");
			if (inc != null)
				include = inc.getAttributeValue("file");
		}

		xmlPaths = (xmlPaths.endsWith("/") || xmlPaths.endsWith("\\") ? xmlPaths.concat(include) : xmlPaths.concat("/").concat(include));		

		try {

			SAXBuilder builder = new SAXBuilder();
			Document doc = builder.build( new File( xmlPaths ) );
			Element root = doc.getRootElement();				
			Element config = root.getChild("configuration", root.getNamespace());
			Element params = config.getChild("params", config.getNamespace());
			if(params == null)
				return;
			List listMainElements = params.getChildren("param", params.getNamespace());
			for( int i=0; i<listMainElements.size(); i++ ){					
				Element elMain  = (Element)(listMainElements.get( i ));					
				if(elMain.getName().equalsIgnoreCase("param")) { 
					List noteList = elMain.getChildren("note", elMain.getNamespace());
					for (int k = 0; k < noteList.size(); k++ ) {												
						Element note = (Element)noteList.get(k);
						String language = Utils.getAttributeValue("language", note);

						if(note != null) {
							List notelist = note.getChildren();
							for (int j = 0; j < notelist.size(); j++) {
								Element elNote  = (Element)(notelist.get( j ));							
								parameterDescription.put( "parameter_description_" + language + "_" + elMain.getAttributeValue("name"), elNote.getText());
								if(elMain.getAttributeValue("required") != null)
									parameterRequired.put( elMain.getAttributeValue("name"), elMain.getAttributeValue("required"));
							}
						}																
					}				
				}
			}

		} catch( Exception ex ) {	
			try {
				new sos.scheduler.editor.app.ErrorLog("error in " + sos.util.SOSClassUtil.getMethodName() , ex);
			} catch(Exception ee) {
				//tu nichts
			}

			ex.printStackTrace();
		}		
	}

	/**
	 * Note/Beschreibung der Parameter
	 * @param name
	 * @return
	 */
	public String getParameterDescription(String name) {
		return (parameterDescription.get("parameter_description_" + Options.getLanguage() + "_" + name) != null ? parameterDescription.get("parameter_description_" + Options.getLanguage() + "_" + name).toString() : "");
	}

	/**
	 * Note/Beschreibung der Parameter
	 * @param name
	 * @return
	 */
	public String getParameterDescription(String name, String language) {
		return (parameterDescription.get("parameter_description_" + language + "_" + name) != null ? parameterDescription.get("parameter_description_" + language + "_" + name).toString() : "");
	}

	private boolean isParameterRequired(String name) {
		String _isIt = (parameterRequired.get(name) != null ? parameterRequired.get(name).toString() : "");
		if(_isIt.equals("true")) { 
			return ( true);
		} else {
			return false;
		}
	}

	//selektierte Datensatz wird eine Zeile nach oben verschoben
	public void changeUp(Table table) {		
		int index = table.getSelectionIndex();
		if(index < 0)//nichts ist selektiert
			return;

		if(index == 0)//ist bereits ganz oben
			return;

		if(_params == null)
			initParams();
		
		_dom.reorderDOM();
		Element params = _parent.getChild("params");
		if (params != null) {			
			_params = params.getChildren();			
			_includeParams = params.getChildren("include");
		}
		
		/*String paramname = table.getSelection()[0].getText(0);		
		for(int i = 0; i < _params.size();++i) {
			Element elem = (Element)_params.get(i);
			if(Utils.getAttributeValue("name", elem).equals(paramname)) {
				Object obj =   elem.clone();
				_params.remove(i);
				_params.add(i-1, obj);
				table.removeAll();
				fillParams(table);
				table.select(i-1);
				//Utils.setChangedForDirectory(_parent, _dom);
				_dom.setChanged(true);
			}
				
				
		}
		*/
		
		
		Element elem = (Element)(_params.get(index));
		Object obj =   elem.clone();
		_params.remove(elem);
		_params.add(index-1, obj);

		
		table.removeAll();
		fillParams(table);
		table.select(index-1);
		//Utils.setChangedForDirectory(_parent, _dom);
		_dom.setChanged(true);
		
	}

	//selektierte Datensatz wird eine Zeile unten oben verschoben
	public void changeDown(Table table) {
		int index = table.getSelectionIndex();
		if(index < 0)//nichts ist selektiert
			return;

		if(index == table.getItemCount()-1)//ist bereits ganz oben
			return;

		if(_params == null)
			initParams();


		Element elem = (Element)(_params.get(index));
		Object obj =   elem.clone();
		_params.remove(elem);
		_params.add(index+1, obj);

		table.removeAll();
		fillParams(table);
		table.select(index+1);
		//Utils.setChangedForDirectory(_parent, _dom);
		_dom.setChanged(true);
	}
	
	
	
}
