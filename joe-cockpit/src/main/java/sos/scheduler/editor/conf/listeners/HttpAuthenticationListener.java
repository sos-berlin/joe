package sos.scheduler.editor.conf.listeners;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;
import org.jdom.Element;

import sos.scheduler.editor.app.Utils;

import com.sos.joe.xml.jobscheduler.SchedulerDom;

public class HttpAuthenticationListener {

    private SchedulerDom _dom = null;
    private Element _config = null;
    private Element _http_server = null;
    private List _httpUsers = new ArrayList();

    public HttpAuthenticationListener(SchedulerDom dom, Element config) {
        _dom = dom;
        _config = config;
        initAuthentication();
    }

    private void initAuthentication() {
        _http_server = _config.getChild("http_server");
        if (_http_server != null) {
            Element httpAuthentication = _http_server.getChild("http.authentication");
            if (httpAuthentication != null) {
                Element httpUsers = httpAuthentication.getChild("http.users");
                if (httpUsers != null) {
                    _httpUsers = httpUsers.getChildren("http.user");
                }
            }
        }
    }

    public void fillAuthentication(Table table) {
        table.removeAll();
        for (Iterator it = _httpUsers.iterator(); it.hasNext();) {
            Element http_user = (Element) it.next();
            TableItem item = new TableItem(table, SWT.NONE);
            item.setText(0, Utils.getAttributeValue("url_path", http_user));
            item.setText(1, Utils.getAttributeValue("path", http_user));
        }
    }

    public void applyHttpUser(TableItem[] httpUser) {
        Element auth = null;
        Element users = null;
        if (_http_server != null) {
            if (_http_server.getChild("http.authentication") == null) {
                auth = new Element("http.authentication");
                _http_server.addContent(auth);
            } else {
                auth = _http_server.getChild("http.authentication");
            }
            if (auth != null) {
                if (auth.getChild("http.users") != null) {
                    users = auth.getChild("http.users");
                } else {
                    users = new Element("http.users");
                    auth.addContent(users);
                }
            }
            users.removeChildren("http.user");
        }
        if (httpUser.length > 0) {
            if (_http_server == null && _config.getAttribute("http_server") == null) {
                _http_server = new Element("http_server");
                _config.addContent(_http_server);
                auth = new Element("http.authentication");
                _http_server.addContent(auth);
                users = new Element("http.users");
                auth.addContent(users);
            }
            for (int i = 0; i < httpUser.length; i++) {
                Element user = new Element("http.user");
                Utils.setAttribute("name", httpUser[i].getText(0), user);
                Utils.setAttribute("password_md5", httpUser[i].getText(1), user);
                if (!"".equalsIgnoreCase(httpUser[i].getText(0))) {
                    users.addContent(user);
                }
            }
            _dom.setChanged(true);
        } else {
            _config.removeChild(_http_server.getName());
            _dom.setChanged(true);
        }
    }

    public void fillHttpAuthenticationTable(Table table) {
        table.removeAll();
        if (_httpUsers != null) {
            for (Iterator it = _httpUsers.iterator(); it.hasNext();) {
                Element e = (Element) it.next();
                TableItem user = new TableItem(table, SWT.NONE);
                user.setText(0, Utils.getAttributeValue("name", e));
                user.setText(1, Utils.getAttributeValue("password_md5", e));
            }
        }
    }

}