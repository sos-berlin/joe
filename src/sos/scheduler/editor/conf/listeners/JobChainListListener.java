package sos.scheduler.editor.conf.listeners;

import java.io.StringReader;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.TableItem;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;
import org.xml.sax.InputSource;

import sos.scheduler.editor.app.ErrorLog;
import sos.scheduler.editor.app.Utils;
import sos.scheduler.editor.classes.SOSTable;
import sos.scheduler.editor.conf.ISchedulerUpdate;
import sos.scheduler.editor.conf.SchedulerDom;

import com.sos.JSHelper.io.Files.JSFile;
import com.sos.JSHelper.io.Files.JSXMLFile;
import com.sos.scheduler.model.objects.JSObjJobChain;

public class JobChainListListener extends JOEJobChainDataProvider {

	@SuppressWarnings("unused")
	private final String		conClassName	= "JobChainListListener";
	@SuppressWarnings("unused")
	private final String		conSVNVersion	= "$Id: JobChainsListener.java 19107 2013-02-12 11:38:14Z ur $";
	private static final Logger	logger			= Logger.getLogger(JobChainListListener.class);

	private Element				lstJobChainList			= null;

	public JobChainListListener(final SchedulerDom dom, final Element config, final ISchedulerUpdate update_) {
		_dom = dom;
		_config = config;
		update = update_;
		lstJobChainList = _config.getChild("job_chains");
	}

	private List<Element> getJobChainList() {
		if (lstJobChainList == null) {
			lstJobChainList = _config.getChild("job_chains");
		}
		@SuppressWarnings("unchecked")
		List<Element> list = lstJobChainList.getChildren("job_chain");
		return list;
	}

	public void populateJobChainsTable(final SOSTable ptblJobChains) {
		ptblJobChains.removeAll();
		if (lstJobChainList != null) {
//			int index = 0;
			for (Element chain : getJobChainList()) {
				objJobChain = chain;
				String name = getName();
				String strTitle = getTitle();
				String strMaxOrders = Utils.getAttributeValue("max_orders", chain);

				TableItem item = new TableItem(ptblJobChains, SWT.NONE);
				item.setData(objJobChain);
				if (DetailsListener.existJobChainNodesParameter(null, name, null, get_dom(), update, true, null)) {
					item.setBackground(getColor4NodeParameter());
				}
				else {
					item.setBackground(null);
				}
				item.setText(0, name);
				item.setText(1, strTitle);
				item.setText(2, strMaxOrders);

				// TODO use a Checkbox
				// TODO i18n
				item.setText(3, Utils.isAttributeValue("orders_recoverable", chain) ? "Yes" : "No");
				item.setText(4, Utils.isAttributeValue("visible", chain) ? "Yes" : "No");

				if (!Utils.isElementEnabled("job_chain", get_dom(), chain)) {
					item.setForeground(getColor4DisabledElements());
				}
			}
		}
	}

	public int indexOf(final String jobChainName) {

		int i = -1;
		int index = -1;
		for (Element chain : getJobChainList()) {
			String name = Utils.getAttributeValue("name", chain);
			if (name.equalsIgnoreCase(jobChainName)) {
				i++;
				index = i;
				break;
			}
		}
		return index;
	}

	public void selectChain(final int index) {

		if (index >= 0)
			objJobChain = getJobChainList().get(index);
		else
			objJobChain = null;
	}

	public void newChain(final String pstrNewChainName) {
		try {
			JSXMLFile objTemplateFile = new JSXMLFile("R:/backup/sos/java/development/com.sos.scheduler.editor/src/sos/scheduler/editor/jobchain-template.xml");
			System.out.println(objTemplateFile.getPath());
			if (objTemplateFile.exists() == true) {
				String strContent = objTemplateFile.getContent();
				strContent = strContent.replaceAll("\\$\\{jobchainname\\}", pstrNewChainName);
				strContent = strContent.replaceAll("\\$\\{title\\}", "title for " + pstrNewChainName);
				SAXBuilder objP = new SAXBuilder();
				Document document = objP.build(new InputSource(new StringReader(strContent)));

				objJobChain = (Element) document.getRootElement().clone();
				String strFileName = get_dom().getFilename();
				String strJobChainName = getChainName();
				strFileName += "/" + strJobChainName + JSObjJobChain.fileNameExtension;
				JSFile objF = new JSFile(strFileName);
				objF.WriteLine(strContent);
				objF.close();
			}
			else {
				objJobChain = new Element("job_chain");
				setName("new job chain");
				setTitle("new job chain");
				setOrdersRecoverable(true);
				setVisible(true);
				setDistributed("false");
			}
		}
		catch (Exception e) {
			new ErrorLog (e.getLocalizedMessage(), e);
		}
		finally {
		}

	}

	public void applyChain(final String name, final boolean ordersRecoverable, final boolean pflgVisible) {
		String oldjobChainName = getName();
		if (oldjobChainName != null && oldjobChainName.length() > 0) {
			_dom.setChangedForDirectory("job_chain", oldjobChainName, SchedulerDom.DELETE);
		}
		setName(name);
		setOrdersRecoverable(ordersRecoverable);
		setVisible(pflgVisible);

		if (lstJobChainList == null) {
			lstJobChainList = new Element("job_chains");
			_config.addContent(lstJobChainList);
		}

		if (!lstJobChainList.getChildren("job_chain").contains(objJobChain))
			lstJobChainList.addContent(objJobChain);

		update.updateJobChains();
		setDirty();
		_dom.setChangedForDirectory("job_chain", name, SchedulerDom.NEW);
	}

	public void deleteChain(final int index) {
		List chains = lstJobChainList.getChildren("job_chain");
		String delname = Utils.getAttributeValue("name", (Element) chains.get(index));
		((Element) chains.get(index)).detach();
		if (chains.size() == 0) {
			_config.removeChild("job_chains");
			lstJobChainList = null;
		}

		int i = 0;
		Iterator it = chains.iterator();
		while (it.hasNext()) {
			Element chain = (Element) it.next();
			String name = Utils.getAttributeValue("name", chain);
		}
		setDirty();
		objJobChain = null;
		update.updateJobChains();
	}
}
