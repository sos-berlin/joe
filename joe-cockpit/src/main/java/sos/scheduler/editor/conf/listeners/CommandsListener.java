package sos.scheduler.editor.conf.listeners;
import java.io.ByteArrayInputStream;
import java.io.IOException;

import org.apache.log4j.Logger;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;

import com.sos.joe.globals.interfaces.ISchedulerUpdate;
import com.sos.joe.globals.messages.ErrorLog;
import com.sos.joe.xml.jobscheduler.SchedulerDom;
 
public class CommandsListener {
    private static final Logger LOGGER = Logger.getLogger(JOEListener.class);
    private SchedulerDom		_dom;
	private Element				_config;
	private ISchedulerUpdate	_main;
	private Element				_commands;

	public CommandsListener(SchedulerDom dom, ISchedulerUpdate update) {
		_dom = dom;
		_config = _dom.getRoot().getChild("config");
		_commands = _config.getChild("commands");
		_main = update;
	}

	public String readCommands() throws Exception {
		String xml = "";
		if (_commands != null) {
			try {
				@SuppressWarnings("unchecked")
                java.util.List<Element> l = _commands.getChildren();
				for (int i = 0; i < l.size(); i++) {
					Element e = l.get(i);
					String s = _dom.getXML(e);
					xml += s.substring(45);
				}
			}
			catch (JDOMException ex) {
				new ErrorLog("error in " + sos.util.SOSClassUtil.getMethodName(), ex);
				throw new Exception("error in " + sos.util.SOSClassUtil.getMethodName() + " : " + ex.getMessage());
			}
		}
		return xml;
	}

	public Element getCommands() {
		return _commands;
	}

	public void saveCommands(String commands) {
		_config.removeChild("commands");
		ByteArrayInputStream bai;
		try {
			String s = "<commands>" + commands + "</commands>";
			bai = new ByteArrayInputStream(s.getBytes("UTF-8"));
			SAXBuilder builder = new SAXBuilder(false);
			Document doc;
			doc = builder.build(bai);
			Element r = doc.getRootElement();
			r.detach();
			_config.addContent(r);
			_dom.setChanged(true);
			_main.updateOrders();
		}
		catch (JDOMException e1) {
			new ErrorLog("error in " + sos.util.SOSClassUtil.getMethodName(), e1);
			LOGGER.error(e1.getMessage(),e1);
		}
		catch (IOException e1) {
			new ErrorLog("error in " + sos.util.SOSClassUtil.getMethodName(), e1);
            LOGGER.error(e1.getMessage(),e1);
		}
	}
}
