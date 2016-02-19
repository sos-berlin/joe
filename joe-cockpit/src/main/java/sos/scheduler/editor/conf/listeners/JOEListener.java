package sos.scheduler.editor.conf.listeners;
import java.io.File;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.program.Program;
import org.jdom.Element;
import org.jdom.JDOMException;
import sos.scheduler.editor.app.MainWindow;
import sos.scheduler.editor.app.Utils;
import com.sos.JSHelper.Basics.JSToolBox;
import com.sos.joe.globals.interfaces.ISchedulerUpdate;
import com.sos.joe.globals.messages.ErrorLog;
import com.sos.joe.globals.misc.ResourceManager;
import com.sos.joe.globals.options.Options;
import com.sos.joe.xml.jobscheduler.SchedulerDom;

public class JOEListener extends JSToolBox {
	@SuppressWarnings("unused") private final String	conClassName	= "JOEListener";
	protected com.sos.joe.xml.jobscheduler.SchedulerDom								_dom			= null;
	protected ISchedulerUpdate							_main			= null;
	protected Element									_job			= null;
	protected Element									_parent			= null;
	protected Element									objElement		= null;
	public final static int								NONE			= -1;

	public JOEListener() {
		//
	}

	public int getLanguage() {
		return NONE;
	}

	public String getLanguageAsString(int language) {
		return "";
	}

	public String getLanguage(int language) {
		return "";
	}

	public String getComment() {
		return "";
	}

	public String getDescription() {
		return "";
	}

	public String getSource() {
		return "";
	}

	public String getPrePostProcessingScriptSource() {
		String strT = "";
		return strT;
	}

	public void setSource(final String pstrS) {
	}

	public void setComment(final String pstrS) {
	}

	public void setDescription(final String pstrD) {
	}

	public void setLanguage(final String pstrLanguage) {
	}

	public String getJobName() {
		return "???";
	}

	public boolean isDisabled() {
		return false;
	}

	public SchedulerDom get_dom() {
		return _dom;
	}

	public Image getImage(final String pstrImageFileName) {
		return ResourceManager.getImageFromResource("/sos/scheduler/editor/" + pstrImageFileName);
	}

	public void openXMLDoc(final String pstrTagName) {
		String lang = Options.getLanguage();
		String strHelpUrl = "http://www.sos-berlin.com/doc/" + lang + "/scheduler.doc/xml/" + pstrTagName + ".xml";
		openHelp(strHelpUrl);
	}

	public void openXMLAttributeDoc(final String pstrTagName, final String pstrAttributeName) {
		String lang = Options.getLanguage();
		String strHelpUrl = "http://www.sos-berlin.com/doc/" + lang + "/scheduler.doc/xml/" + pstrTagName + ".xml#attribute_" + pstrAttributeName;
		openHelp(strHelpUrl);
	}

	public boolean Check4HelpKey(final int pintKeyCode, final String pstrTagName, final String pstrAttribute) {
		if (isHelpKey(pintKeyCode)) {
			openXMLAttributeDoc(pstrTagName, pstrAttribute);
			return true;
		}
		if (isGlobalHelpKey(pintKeyCode)) {
			openXMLDoc(pstrTagName);
			return true;
		}
		return false;
	}

	public boolean isHelpKey(final int pintKeyCode) {
		boolean flgRet = (pintKeyCode == SWT.F1);
		return flgRet;
	}

	public boolean isGlobalHelpKey(final int pintKeyCode) {
		boolean flgRet = (pintKeyCode == SWT.F10);
		return flgRet;
	}

	public void openHelp(String helpKey) {
		String lang = Options.getLanguage();
		String url = helpKey;
		try {
			// TODO: überprüfen, ob Datei wirklich existiert
			if (url.contains("http:")) {
			}
			else {
				url = new File(url).toURL().toString();
			}
			Program prog = Program.findProgram("html");
			if (prog != null)
				prog.execute(url);
			else {
				Runtime.getRuntime().exec(Options.getBrowserExec(url, lang));
			}
		}
		catch (Exception e) {
			new ErrorLog("error in " + getMethodName() + "; "
						+ com.sos.joe.globals.messages.Messages.getString("MainListener.cannot_open_help", new String[] { url, lang, e.getMessage() }), e);
			MainWindow.message(com.sos.joe.globals.messages.Messages.getString("MainListener.cannot_open_help", new String[] { url, lang, e.getMessage() }),
					SWT.ICON_ERROR | SWT.OK);
		}
	}

	public String getXML() {
		String strXmlText = "";
		if (objElement != null) {
			strXmlText = getXML(objElement);
 
		}
		return strXmlText;
	}

	private String getXML(Element element) {
		String xml = "";
		if (element != null) {
			try {
				if (_dom instanceof SchedulerDom && _dom.isDirectory()) {
					xml = _dom.getXML(Utils.getHotFolderParentElement(element));
				}
				else {
					xml = _dom.getXML(element);
				}
			}
			catch (JDOMException ex) {
				new ErrorLog("error in " + getMethodName(), ex);
				return null;
			}
		}
		return xml;
	}
	
}
