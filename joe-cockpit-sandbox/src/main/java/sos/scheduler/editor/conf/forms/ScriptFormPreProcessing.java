package sos.scheduler.editor.conf.forms;
//import org.eclipse.draw2d.*;
import java.util.HashMap;

import org.apache.log4j.Logger;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;

import sos.scheduler.editor.app.JOEMainWindow;
import sos.scheduler.editor.conf.composites.PreProcessingComposite;
import sos.util.SOSClassUtil;

import com.sos.joe.globals.messages.ErrorLog;
import com.sos.joe.globals.misc.TreeData;
import com.sos.joe.globals.options.Options;
import com.sos.joe.objects.job.JobListener;
import com.sos.joe.objects.job.forms.ScriptForm;
import com.sos.scheduler.model.LanguageDescriptor;
import com.sos.scheduler.model.LanguageDescriptorList;
import com.sos.scheduler.model.objects.JSObjJob;

public class ScriptFormPreProcessing extends ScriptForm {
	@SuppressWarnings("unused")
	private final String			conClassName		= this.getClass().getSimpleName();
	@SuppressWarnings("unused")
	private static final String		conSVNVersion		= "$Id$";
	@SuppressWarnings("unused")
	private final Logger			logger				= Logger.getLogger(this.getClass());
	private PreProcessingComposite	preProcessingHeader	= null;
	private HashMap<String, String>	favorites			= null;
	private TreeData				objTreeData			= null;

	public ScriptFormPreProcessing(final Composite parent, final TreeData pobjTreeData) {
		super(parent, pobjTreeData);
		objDataProvider = new JobListener(pobjTreeData);
		objDataProvider._languages = JSObjJob.ValidLanguages4Job;
		objTreeData = pobjTreeData;
		objDataProvider._languages = LanguageDescriptorList.getLanguages4Monitor();
		initialize();
	}

	@Override protected void createGroup() {
		GridLayout gridLayoutMainOptionsGroup = new GridLayout();
		gridLayoutMainOptionsGroup.numColumns = 1;
		objMainOptionsGroup = new Group(this, SWT.NONE);
		objMainOptionsGroup.setText(objDataProvider.getJobNameAndTitle());
		objMainOptionsGroup.setLayout(gridLayoutMainOptionsGroup);
		objMainOptionsGroup.setLayoutData(new GridData(GridData.FILL, GridData.FILL, true, true));
		GridLayout gridLayout = new GridLayout();
		gridLayout.marginHeight = 1;
		gridLayout.numColumns = 1;
		preProcessingHeader = new PreProcessingComposite(objMainOptionsGroup, SWT.NONE, objDataProvider);
		//		preProcessingHeader.setLayout(gridLayout);
		//		preProcessingHeader.setLayoutData(new GridData(GridData.FILL, GridData.FILL, true, false));
		createLanguageSelector(preProcessingHeader.getgMain());
		if (objDataProvider.getLanguageDescriptor() == null) {
			LanguageDescriptor objLD = LanguageDescriptorList.getDefaultLanguage4Monitor();
			objDataProvider.setLanguage(objLD);
			languageSelector.selectLanguageItem(objLD);
		}
		createScriptTabForm(objMainOptionsGroup);
		getFavoriteNames();
		preProcessingHeader.getButFavorite().addSelectionListener(new SelectionAdapter() {
			@Override public void widgetSelected(final SelectionEvent e) {
				Options.setProperty("monitor_favorite_" + objDataProvider.getLanguageAsString() + "_" + preProcessingHeader.getTxtName().getText(),
						getFavoriteValue());
				Options.saveProperties();
				preProcessingHeader.getCboFavorite().setItems(normalized(Options.getPropertiesWithPrefix("monitor_favorite_")));
			}
		});
		preProcessingHeader.getCboFavorite().addSelectionListener(new SelectionAdapter() {
			@Override public void widgetSelected(final SelectionEvent e) {
				getDataFromFavorite();
			}
		});
	}

	@Override protected String getPredefinedFunctionNames() {
		// TODO attribute of languageDescriptor
		return "spooler_task_before;spooler_task_after;spooler_process_before;spooler_process_after";
	}

	@Override protected String[] getScriptLanguages() {
		String[] strR = LanguageDescriptorList.getLanguages4Monitor();
		return strR;
	}

	private void getFavoriteNames() {
		preProcessingHeader.getCboFavorite().setData("favorites", favorites);
//	 TODO	preProcessingHeader.getCboFavorite().setMenu(new ContextMenu(preProcessingHeader.getCboFavorite(), objDataProvider.getDom(), JOEConstants.SCRIPT).getMenu());
	}

	private String getFavoriteValue() {
		if (objDataProvider.isJava()) {
			return this.getObjJobJAPI().getTbxClassName().getText();
		}
		else {
			return objDataProvider.getIncludesAsString();
		}
	}

	private String getPrefix() {
		if (favorites != null && preProcessingHeader.getCboFavorite().getText().length() > 0
				&& favorites.get(preProcessingHeader.getCboFavorite().getText()) != null)
			return "monitor_favorite_" + favorites.get(preProcessingHeader.getCboFavorite().getText()) + "_";
		if (objDataProvider.getLanguage() == 0)
			return "";
		return "monitor_favorite_" + objDataProvider.getLanguageAsString() + "_";
	}

	private String[] normalized(final String[] str) {
		String[] retVal = new String[] { "" };
		try {
			favorites = new HashMap<String, String>();
			if (str == null)
				return new String[0];
			String newstr = "";
			retVal = new String[str.length];
			for (String s : str) {
				int idx = s.indexOf("_");
				if (idx > -1) {
					String lan = s.substring(0, idx);
					String name = s.substring(idx + 1);
					if (name == null || lan == null)
						System.out.println(name);
					else
						favorites.put(name, lan);
					newstr = name + ";" + newstr;
				}
			}
			retVal = newstr.split(";");
			return retVal;
		}
		catch (Exception e) {
			System.out.println(e.toString());
			try {
				new ErrorLog(JOE_E_0002.params(SOSClassUtil.getMethodName()), e);
			}
			catch (Exception ee) {
			}
			return retVal;
		}
	}

	private void getDataFromFavorite() {
		if (preProcessingHeader.getCboFavorite().getText().length() > 0) {
			if (Options.getProperty(getPrefix() + preProcessingHeader.getCboFavorite().getText()) != null) {
				if (this.getObjJobJAPI() != null && this.getObjJobJAPI().getTbxClassName().getText().length() > 0 || this.getObjJobIncludeFile() != null
						&& this.getObjJobIncludeFile().getTableIncludes().isEnabled() && this.getObjJobIncludeFile().getTableIncludes().getItemCount() > 0) {
					int c = JOEMainWindow.message(getShell(), JOE_M_ScriptFormPreProcessing_OverwriteMonitor.label(), SWT.ICON_QUESTION | SWT.YES | SWT.NO);
					if (c != SWT.YES)
						return;
					else {
						if (this.getObjJobJAPI() != null) {
							this.getObjJobJAPI().getTbxClassName().setText("");
						}
						if (this.getObjJobIncludeFile() != null) {
							this.getObjJobIncludeFile().getTableIncludes().clearAll();
						}
						objDataProvider.removeIncludes();
					}
				}
				if (favorites != null && favorites.get(preProcessingHeader.getCboFavorite().getText()) != null
						&& favorites.get(preProcessingHeader.getCboFavorite().getText()).toString().length() > 0) {
					String strPPH = preProcessingHeader.getCboFavorite().getText();
					preProcessingHeader.getTxtName().setText(strPPH);
					objDataProvider.setMonitorName(strPPH);
					// was ein kuddelmuddel!
					objDataProvider.setLanguage(favorites.get(preProcessingHeader.getCboFavorite().getText()).toString());
					languageSelector.setText(objDataProvider.getLanguageDescriptor().getLanguageName());
					if (objDataProvider.isJava()) {
						this.getObjJobJAPI().getTbxClassName().setText(Options.getProperty(getPrefix() + preProcessingHeader.getCboFavorite().getText()));
					}
					else {
						tabFolder.setSelection(this.getTabItemIncludedFiles());
						String[] split = Options.getProperty(getPrefix() + preProcessingHeader.getCboFavorite().getText()).split(";");
						for (String element : split) {
							objDataProvider.addInclude(element, false);
						}
					}
					fillForm();
				}
			}
		}
	}

	@Override protected void initForm() {
		preProcessingHeader.init();
		String[] strFavorites = Options.getPropertiesWithPrefix("monitor_favorite_");
		if (normalized(strFavorites) != null && normalized(strFavorites)[0] != null) {
			preProcessingHeader.getCboFavorite().setItems(normalized(strFavorites));
		}
	}
}
