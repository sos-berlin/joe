package sos.scheduler.editor.conf.listeners;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Iterator;

import org.eclipse.swt.SWT;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;

import ErrorLog;
import sos.scheduler.editor.app.MainWindow;
import sos.scheduler.editor.conf.DetailDom;
import sos.util.SOSClassUtil;

public class DetailXMLEditorListener {

    private DetailDom        _dom        = null;

    private Element          _settings   = null;

    private String           xmlFilename = null;


    public DetailXMLEditorListener(final String xmlFilename_) {

    	try {
    		xmlFilename = xmlFilename_;
    		_dom = new DetailDom();
    		_dom.read(xmlFilename);
    		_settings = _dom.getRoot();

    	} catch (Exception e) {
    		try {
				new ErrorLog("error in " + SOSClassUtil.getMethodName() , e);
			} catch(Exception ee) {

			}
    		System.out.println("error in DetailXMLEditorListener: " + e.getMessage());
    	}

    }

    public DetailXMLEditorListener(final DetailDom     dom) {

    	try {
    		_dom = dom;
    		xmlFilename = dom.getFilename();

    		_dom.read(xmlFilename);
    		_settings = _dom.getRoot();

    	} catch (Exception e) {
    		try {
				new ErrorLog("error in " + SOSClassUtil.getMethodName() , e);
			} catch(Exception ee) {

			}
    		System.out.println("error in DetailXMLEditorListener: " + e.getMessage());
    	}

    }

    public String readCommands() throws Exception {

        String xml = "";
        if (_settings != null) {
            try {

              Iterator it = _settings.getChildren().iterator();
                while (it.hasNext()) {
                    Element e = (Element) it.next();
                    String s = _dom.getXML(e);
                    xml += s.substring(45);

                }

            } catch (JDOMException ex) {
            	try {
    				new ErrorLog("error in " + SOSClassUtil.getMethodName() , ex);
    			} catch(Exception ee) {

    			}
                throw new Exception("Error: " + ex.getMessage());

            }
        }
        return xml;
    }


    public Element getCommands() {
        return _settings;
    }


    public void saveXML(final String sXML) {


        ByteArrayInputStream bai;
        try {
            bai = new ByteArrayInputStream(sXML.getBytes("UTF-8"));
            SAXBuilder builder = new SAXBuilder(false);
            Document doc;

            doc = builder.build(bai);
            Element r = doc.getRootElement();
            r.detach();

            _settings.removeContent();
            _settings.addContent(r);
            _dom.setChanged(true);
            _dom.write(xmlFilename);

        } catch (JDOMException e1) {
        	try {
				new ErrorLog("error in " + SOSClassUtil.getMethodName() + "  ; XML could not be saved ; [xml=" + sXML + "]", e1);
			} catch(Exception ee) {

			}
        	MainWindow.message("XML could not be saved because:\n" + e1.getMessage(), SWT.ICON_ERROR);
            e1.printStackTrace();
        } catch (IOException e1) {
        	try {
				new ErrorLog("error in " + SOSClassUtil.getMethodName() + "  ; XML could not be saved ; [xml=" + sXML + "]", e1);
			} catch(Exception ee) {

			}

        	MainWindow.message("XML could not be saved because:\n" + e1.getMessage(), SWT.ICON_ERROR);
            e1.printStackTrace();
        }

    }

}
