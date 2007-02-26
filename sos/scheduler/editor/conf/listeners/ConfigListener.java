package sos.scheduler.editor.conf.listeners;

import org.jdom.Element;

import sos.scheduler.editor.app.Utils;
import sos.scheduler.editor.conf.SchedulerDom;

public class ConfigListener {
    private SchedulerDom _dom;

    private Element      _config;


    public ConfigListener(SchedulerDom dom) {
        _dom = dom;
        _config = _dom.getRoot().getChild("config");
    }


    public SchedulerDom getDom() {
        return _dom;
    }


    public String getComment() {
        return Utils.getAttributeValue("__comment__", _config);
    }


    public void setComment(String comment) {
        Utils.setAttribute("__comment__", comment, _config, _dom);
    }


    public String getIncludePath() {
        return Utils.getAttributeValue("include_path", _config);
    }


    public void setIncludePath(String path) {
      Utils.setAttribute("include_path", path, _config, _dom);
  }

    public void setIpAddress(String ip) {
      Utils.setAttribute("ip_address", ip, _config, _dom);
      _dom.setChanged(true);
  }


    public String getJavaClasspath() {
        return Utils.getAttributeValue("java_class_path", _config);
    }


    public void setJavaClasspath(String classpath) {
        Utils.setAttribute("java_class_path", classpath, _config, _dom);
    }


    public String getJavaOptions() {
        return Utils.getAttributeValue("java_options", _config);
    }


    public void setJavaOptions(String options) {
        Utils.setAttribute("java_options", options, _config, _dom);
    }


    public String getLogDir() {
        return Utils.getAttributeValue("log_dir", _config);
    }


    public void setLogDir(String dir) {
        Utils.setAttribute("log_dir", dir, _config, _dom);
    }


    public String getMailXSLTStylesheet() {
        return Utils.getAttributeValue("mail_xslt_stylesheet", _config);
    }


    public void setMailXSLTStylesheet(String stylesheet) {
        Utils.setAttribute("mail_xslt_stylesheet", stylesheet, _config, _dom);
    }


    public String getMainSchedulerHost() {
        return Utils.getAttributeValue("main_scheduler", _config).split(":")[0];
    }


    public int getMainSchedulerPort() {
        String[] str = Utils.getAttributeValue("main_scheduler", _config).split(":");
        if (str.length > 1) {
            try {
                return new Integer(str[1]).intValue();
            } catch (Exception e) {
                Utils.setAttribute("main_scheduler", str[0] + ":0", _config, _dom);
            }
        }
        return 0;
    }


    public void setMainScheduler(String scheduler) {
        if (scheduler.startsWith(":"))
            scheduler = "";
        Utils.setAttribute("main_scheduler", scheduler, _config, _dom);
    }


    public boolean isMainScheduler() {
        return _config.getAttribute("main_scheduler") != null;
    }


    public String getParam() {
        return Utils.getAttributeValue("param", _config);
    }


    public void setParam(String param) {
        Utils.setAttribute("param", param, _config, _dom);
    }


    public int getPriorityMax() {
        if (_config.getAttributeValue("priority_max") == null)
            return 1000;
        else
            return Utils.getIntValue("priority_max", _config);
    }


    public void setPriorityMax(int max) {
        Utils.setAttribute("priority_max", new Integer(max).toString(), _config, _dom);
    }


    public String getSpoolerID() {
        return Utils.getAttributeValue("spooler_id", _config);
    }


    public void setSpoolerID(String spoolerid) {
        Utils.setAttribute("spooler_id", spoolerid, _config, _dom);
    }


    public String getTcpPort() {
      return Utils.getAttributeValue("tcp_port", _config);
  }

   public String getIpAddress() {
      return Utils.getAttributeValue("ip_address ", _config);
   }


    public void setTcpPort(String port) {
        Utils.setAttribute("tcp_port", Utils.getIntegerAsString(Utils.str2int(port)), _config, _dom);
        _config.removeAttribute("port");
        _dom.setChanged(true);
    }


    public String getUdpPort() {
        return Utils.getAttributeValue("udp_port", _config);
    }


    public void setUdpPort(String port) {
        Utils.setAttribute("udp_port", Utils.getIntegerAsString(Utils.str2int(port)), _config, _dom);
        _config.removeAttribute("port");
        _dom.setChanged(true);
    }


    public String getPort() {
        return Utils.getAttributeValue("port", _config);
    }


    public void setPort(String port) {
        Utils.setAttribute("port", Utils.getIntegerAsString(Utils.str2int(port)), _config, _dom);
        _config.removeAttribute("tcp_port");
        _config.removeAttribute("udp_port");
        _dom.setChanged(true);
    }


    public boolean isPort() {
        return _config.getAttribute("port") != null;
    }

}
