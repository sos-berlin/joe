package sos.scheduler.editor.conf.listeners;

import java.util.Iterator;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;
import org.jdom.Element;

import com.sos.joe.interfaces.ISchedulerUpdate;

import sos.scheduler.editor.app.Utils;
import sos.scheduler.editor.conf.SchedulerDom;

public class WebserviceListener {

	private SchedulerDom _dom           = null;

	private Element           _service       = null;

	private String[]           _chains       = new String[0];

	private ISchedulerUpdate   _main          = null;

	

	public WebserviceListener(SchedulerDom dom, Element webService, ISchedulerUpdate main) {

		_dom = dom;
		_service = webService;
		_main = main;

	}

	
	public boolean getDebug() {
		return Utils.isAttributeValue("debug", _service);
	}


	public String getForwardXSLT() {
		return Utils.getAttributeValue("forward_xslt_stylesheet", _service);
	}


	public String getRequestXSLT() {
		return Utils.getAttributeValue("request_xslt_stylesheet", _service);
	}


	public String getResponseXSLT() {
		return Utils.getAttributeValue("response_xslt_stylesheet", _service);
	}


	public String getJobChain() {
		return Utils.getAttributeValue("job_chain", _service);
	}


	public String getName() {
		return Utils.getAttributeValue("name", _service);
	}


	public String getTimeout() {
		return Utils.getAttributeValue("timeout", _service);
	}


	public String getURL() {
		return Utils.getAttributeValue("url_path", _service);
	}




	public void applyService(boolean debug, String chain, String name, String forward, String request, String response,
			String timeout, String url) {
		
		Utils.setAttribute("debug", debug, _service, _dom);
		Utils.setAttribute("job_chain", chain, _service, _dom);
		Utils.setAttribute("name", name, _service, _dom);
		Utils.setAttribute("forward_xslt_stylesheet", forward, _service, _dom);
		Utils.setAttribute("request_xslt_stylesheet", request, _service, _dom);
		Utils.setAttribute("response_xslt_stylesheet", response, _service, _dom);
		Utils.setAttribute("timeout", timeout, _service, _dom);
		Utils.setAttribute("url_path", url, _service, _dom);

		_main.updateTreeItem("Web Service: " +name);
		_dom.setChanged(true);
	}




	public String[] getJobChains() {
		if(_service == null || 
				_service.getParentElement() == null ||
				_service.getParentElement().getParentElement() == null	
		)
			return new String[0];
		
		Element _config = _service.getParentElement().getParentElement(); 
		Element element = _config.getChild("job_chains");
		if (element != null) {
			List chains = element.getChildren("job_chain");
			_chains = new String[chains.size()];
			int index = 0;
			Iterator it = chains.iterator();
			while (it.hasNext()) {
				String name = ((Element) it.next()).getAttributeValue("name");
				_chains[index++] = name != null ? name : "";
			}
		} else
			_chains = new String[0];

		return _chains;
	}


	public int getChainIndex(String jobChain) {
		for (int i = 0; i < _chains.length; i++) {
			if (_chains[i].equals(jobChain))
				return i;
		}
		return -1;
	}


	public void fillParams(Table table) {
		table.removeAll();
		if (_service != null) {
			Element params = _service.getChild("params");
			if (params != null) {
				for (Iterator it = params.getChildren("param").iterator(); it.hasNext();) {
					Element e = (Element) it.next();
					TableItem param = new TableItem(table, SWT.NONE);
					param.setText(0, Utils.getAttributeValue("name", e));
					param.setText(1, Utils.getAttributeValue("value", e));
				}
			}
		}
	}


	public boolean isValid(String name) {
		
		List _list = _service.getParentElement().getChildren("web_service");
		if (_list != null) {
			for (Iterator it = _list.iterator(); it.hasNext();) {
				Element e = (Element) it.next();
				if (Utils.getAttributeValue("name", e).equals(name))
					return false;
			}
		}
		return true;
	}
	
}
