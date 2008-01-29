package sos.scheduler.editor.conf.listeners;

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
import sos.scheduler.editor.conf.ISchedulerUpdate;
import sos.scheduler.editor.conf.SchedulerDom;
import sos.scheduler.editor.app.Options;

public class ParameterListener {

	private ISchedulerUpdate _main;

	private SchedulerDom     _dom;

	private Element          _parent;

	private List             _params;

	private List             _environments;

	private List             _includeParams;

	//Hifsvariable
	//private static String library      = "";  

	private static HashMap parameterDescription = new HashMap();

	private static HashMap parameterRequired = new HashMap();

	//default ist config
	private int    type     = Editor.CONFIG;


	public ParameterListener(SchedulerDom dom, Element parent, ISchedulerUpdate update, int type_) {
		_dom = dom;
		_parent = parent;
		_main = update;
		type = type_;


		Element params = _parent.getChild("params");
		if (params != null) {
			_params = params.getChildren("param");
			_includeParams = params.getChildren("include");
		}

		Element environment = _parent.getChild("environment");
		if(environment != null)
			_environments = environment.getChildren("variable");



	}


	private void initParams() {
		Element params = _parent.getChild("params");
		if (params != null) {
			_params = params.getChildren("param");
			_includeParams = params.getChildren("include");
		} else {
			_parent.addContent(0, new Element("params"));
			_params = _parent.getChild("params").getChildren("param");
			_includeParams = _parent.getChild("params").getChildren("include");
		}
	}


	private void initEnvironment() {
		_parent.addContent(0, new Element("environment"));
		_environments = _parent.getChild("environment").getChildren();
	}


	public void fillParams(Table table) {
		if (_params != null) {
			Iterator it = _params.iterator();
			while (it.hasNext()) {
				Object o = it.next();
				if (o instanceof Element) {					
					if(((Element) o).getAttributeValue("name") != null) {
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
		if(type == Editor.JOB)
			_main.updateJob();;
	} 

	public void fillParams(ArrayList listOfParams, Table table, boolean refreshTable) {
		//boolean existParam = false;

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
		if(type == Editor.JOB)
			_main.updateJob();

	} 

	public void fillIncludeParams(Table table) {
		if (_includeParams != null) {
			Iterator it = _includeParams.iterator();
			while (it.hasNext()) {
				Object o = it.next();
				if (o instanceof Element) {					
					TableItem item = new TableItem(table, SWT.NONE);
					item.setText(0, ((Element) o).getAttributeValue("file"));
					item.setText(1, (((Element) o).getAttributeValue("node") != null ? ((Element) o).getAttributeValue("node") : ""));					
				}
			}						
		}		
		if(type == Editor.JOB)
			_main.updateJob();
	} 


	public TableItem existsParams(String name, Table table, String replaceValue) {

		try {
			for (int i =0; i < table.getItemCount(); i++) {
				if(table.getItem(i).getText(0).equals(name)) {					
					return table.getItem(i);
				}
			}
		} catch (Exception e) {
			System.out.println("error in ParameterListener.existsParams " + e.getMessage());
		}
		return null;
	}

	public void deleteParameter(Table table, int index) {


		if (_params != null) {
			_params.remove(index);
			_dom.setChanged(true);
			if(type == Editor.JOB) _dom.setChangedForDirectory(_parent.getParentElement().getName(), Utils.getAttributeValue("name",_parent), SchedulerDom.MODIFY);			
		}
		table.remove(index);

	}					

	public void deleteEnvironment(Table table, int index) {


		if (_environments != null) {
			_environments.remove(index);
			_dom.setChanged(true);
			_dom.setChangedForDirectory("job", Utils.getAttributeValue("name",_parent), SchedulerDom.MODIFY);			
			if(type == Editor.JOB) _dom.setChangedForDirectory(_parent.getParentElement().getName(), Utils.getAttributeValue("name",_parent), SchedulerDom.MODIFY);
		}
		table.remove(index);

	}	


	public void deleteIncludeParams(Table table, int index) {


		if (_includeParams != null) {
			_includeParams.remove(index);
			_dom.setChanged(true);						
			if(type == Editor.JOB) _dom.setChangedForDirectory(_parent.getParentElement().getName(), Utils.getAttributeValue("name",_parent), SchedulerDom.MODIFY);
		}
		table.remove(index);

	}	

	public void saveParameter(Table table, String name, String value, String parameterDescription_de, String parameterDescription_en, boolean required) {

		Element e = new Element("param");		
		Utils.setAttribute("name", name, e);
		Utils.setAttribute("value", value, e);


		if((_dom.isLifeElement() || _dom.isDirectory()) && _params == null) {
			Element params = _parent.getChild("params");
			if (params != null)
				_params = params.getChildren("param");
		}

		if (_params == null)
			initParams();
		_params.add(e);

		TableItem item = new TableItem(table, SWT.NONE);
		item.setText(new String[] { name, value });

		if(parameterDescription_de!=null && parameterDescription_de.trim().length()>0) {
			item.setData("parameter_description_de", parameterDescription_de);
		}
		if(parameterDescription_en!=null && parameterDescription_en.trim().length()>0) {
			item.setData("parameter_description_en", parameterDescription_en);
		}


		if(required) {
			item.setBackground(Options.getRequiredColor());
		}
		_dom.setChanged(true);

		if(type == Editor.JOB) _dom.setChangedForDirectory(_parent.getParentElement().getName(), Utils.getAttributeValue("name",_parent), SchedulerDom.MODIFY);
	}


	public void saveIncludeParams(Table table, String file, String node) {

		boolean found = false;

		if (_includeParams != null) {
			int index = 0;
			Iterator it = _includeParams.iterator();
			while (it.hasNext()) {
				Object o = it.next();
				if (o instanceof Element) {
					Element e = (Element) o;
					if (file.equals(e.getAttributeValue("file")) && (node.equals(e.getAttributeValue("node")) || table.getSelectionCount() > 0 )) {
						found = true;																		
						Utils.setAttribute("node", node, e);
						_dom.setChanged(true);						
						if(type == Editor.JOB) _dom.setChangedForDirectory(_parent.getParentElement().getName(), Utils.getAttributeValue("name",_parent), SchedulerDom.MODIFY);
						table.getItem(index).setText(1, node);
						break;
					}
					index++;
				}
			}
		}
		if (!found) {
			Element e = new Element("include");
			e.setAttribute("file", file);
			e.setAttribute("node", node);
			_dom.setChanged(true);			
			if(type == Editor.JOB) _dom.setChangedForDirectory(_parent.getParentElement().getName(), Utils.getAttributeValue("name",_parent), SchedulerDom.MODIFY);

			if (_includeParams == null)
				initParams();

			_includeParams.add(e);

			TableItem item = new TableItem(table, SWT.NONE);
			item.setText(new String[] { file, node});

		}			
	}

	public void saveEnvironment(Table table, String name, String value) {

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
						Utils.setAttribute("value", value, e);
						_dom.setChanged(true);
						if(type == Editor.JOB) _dom.setChangedForDirectory(_parent.getParentElement().getName(), Utils.getAttributeValue("name",_parent), SchedulerDom.MODIFY);						
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
			if(type == Editor.JOB) _dom.setChangedForDirectory(_parent.getParentElement().getName(), Utils.getAttributeValue("name",_parent), SchedulerDom.MODIFY);

			if (_environments == null)
				initEnvironment();
			_environments.add(e);

			TableItem item = new TableItem(table, SWT.NONE);
			item.setText(new String[] { name, value });

		}			
	}

	public void saveParameter(Table table, String name, String value) {

		boolean found = false;
		//String value2 = value;

		if (_params != null) {
			int index = 0;
			Iterator it = _params.iterator();
			while (it.hasNext()) {
				Object o = it.next();
				if (o instanceof Element) {
					Element e = (Element) o;
					if (name.equals(e.getAttributeValue("name"))) {
						found = true;
						Utils.setAttribute("value", value, e);											
						_dom.setChanged(true);
						if(type == Editor.JOB) _dom.setChangedForDirectory(_parent.getParentElement().getName(), Utils.getAttributeValue("name",_parent), SchedulerDom.MODIFY);						
						table.getItem(index).setText(1, value);
						break;
					}
					index++;
				}
			}
		}
		if (!found) {
			Element e = new Element("param");
			Utils.setAttribute("name", name, e);
			Utils.setAttribute("value", value, e);	
			_dom.setChanged(true);			
			if(type == Editor.JOB) _dom.setChangedForDirectory(_parent.getParentElement().getName(), Utils.getAttributeValue("name",_parent), SchedulerDom.MODIFY);
			if (_params == null)
				initParams();
			_params.add(e);

			TableItem item = new TableItem(table, SWT.NONE);
			item.setText(new String[] { name, value });
			if(parameterRequired != null && isParameterRequired(name))
				item.setBackground(Options.getRequiredColor());
		}		

	}

	public SchedulerDom get_dom() {
		return _dom;
	}


	public ISchedulerUpdate get_main() {
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

}
