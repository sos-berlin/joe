package sos.scheduler.editor.conf.listeners;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Iterator;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;

import sos.scheduler.editor.conf.ISchedulerUpdate;
import sos.scheduler.editor.conf.SchedulerDom;

public class CommandsListener {

    private SchedulerDom     _dom;

    private Element          _config;

    private ISchedulerUpdate _main;

    private Element          _commands;


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
                Iterator it = _commands.getChildren().iterator();
                while (it.hasNext()) {
                    Element e = (Element) it.next();
                    String s = _dom.getXML(e);
                    xml += s.substring(45);
                }
            } catch (JDOMException ex) {
                throw new Exception("Error: " + ex.getMessage());

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
        } catch (JDOMException e1) {
            e1.printStackTrace();
        } catch (IOException e1) {
            e1.printStackTrace();
        }

    }

}