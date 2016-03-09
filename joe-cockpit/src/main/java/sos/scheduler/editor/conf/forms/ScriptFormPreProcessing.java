package sos.scheduler.editor.conf.forms;

// import org.eclipse.draw2d.*;
import java.util.HashMap;

import org.apache.log4j.Logger;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.jdom.Element;

import sos.scheduler.editor.app.ContextMenu;
import sos.scheduler.editor.app.MainWindow;
import sos.scheduler.editor.conf.composites.PreProcessingComposite;

import com.sos.joe.globals.JOEConstants;
import com.sos.joe.globals.interfaces.ISchedulerUpdate;
import com.sos.joe.globals.messages.ErrorLog;
import com.sos.joe.globals.options.Options;
import com.sos.joe.xml.jobscheduler.SchedulerDom;

public class ScriptFormPreProcessing extends ScriptForm {

    @SuppressWarnings("unused")
    private final String conSVNVersion = "$Id$";
    @SuppressWarnings("unused")
    private static Logger logger = Logger.getLogger(ScriptJobMainForm.class);
    @SuppressWarnings("unused")
    private final String conClassName = "PreProcessingForm";
    private PreProcessingComposite preProcessingHeader = null;
    private HashMap<String, String> favorites = null;

    public ScriptFormPreProcessing(Composite parent, int style, SchedulerDom dom, Element job, ISchedulerUpdate main) {
        super(parent, style, dom, job, main);
        objDataProvider._languages = objDataProvider._languagesMonitor;

        initialize();
    }

    protected void createGroup() {
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
        preProcessingHeader.setLayout(gridLayout);
        preProcessingHeader.setLayoutData(new GridData(GridData.FILL, GridData.FILL, true, false));
        createLanguageSelector(preProcessingHeader.getgMain());
        if (objDataProvider.getLanguage() < 0) {
            objDataProvider.setLanguage(0);
            languageSelector.selectLanguageItem(0);
        }
        createScriptTabForm(objMainOptionsGroup);
        getFavoriteNames();
        preProcessingHeader.getButFavorite().addSelectionListener(new SelectionAdapter() {

            public void widgetSelected(final SelectionEvent e) {
                Options.setProperty("monitor_favorite_" + objDataProvider.getLanguage(objDataProvider.getLanguage()) + "_"
                        + preProcessingHeader.getTxtName().getText(), getFavoriteValue());
                Options.saveProperties();
                preProcessingHeader.getCboFavorite().setItems(normalized(Options.getPropertiesWithPrefix("monitor_favorite_")));
            }
        });
        preProcessingHeader.getCboFavorite().addSelectionListener(new SelectionAdapter() {

            public void widgetSelected(final SelectionEvent e) {
                getDataFromFavorite();
            }
        });
    }

    protected String getPredefinedFunctionNames() {
        return "spooler_task_before;spooler_task_after;spooler_process_before;spooler_process_after";
    }

    protected String[] getScriptLanguages() {
        return objDataProvider._languagesMonitor;
    }

    private void getFavoriteNames() {
        preProcessingHeader.getCboFavorite().setData("favorites", favorites);
        preProcessingHeader.getCboFavorite().setMenu(new ContextMenu(preProcessingHeader.getCboFavorite(), objDataProvider.getDom(), JOEConstants.SCRIPT).getMenu());
    }

    private String getFavoriteValue() {
        if (objDataProvider.isJava()) {
            return this.getObjJobJAPI().getTbxClassName().getText();
        } else {
            return objDataProvider.getIncludesAsString();
        }
    }

    private String getPrefix() {
        if (favorites != null && preProcessingHeader.getCboFavorite().getText().length() > 0
                && favorites.get(preProcessingHeader.getCboFavorite().getText()) != null)
            return "monitor_favorite_" + favorites.get(preProcessingHeader.getCboFavorite().getText()) + "_";
        if (objDataProvider.getLanguage() == 0)
            return "";
        return "monitor_favorite_" + objDataProvider.getLanguage(objDataProvider.getLanguage()) + "_";
    }

    private String[] normalized(String[] str) {
        String[] retVal = new String[] { "" };
        try {
            favorites = new HashMap<String, String>();
            if (str == null)
                return new String[0];
            String newstr = "";
            retVal = new String[str.length];
            for (int i = 0; i < str.length; i++) {
                String s = str[i];
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
        } catch (Exception e) {
            System.out.println(e.toString());
            try {
                new ErrorLog(JOE_E_0002.params(sos.util.SOSClassUtil.getMethodName()), e);
            } catch (Exception ee) {
            }
            return retVal;
        }
    }

    private void getDataFromFavorite() {
        if (preProcessingHeader.getCboFavorite().getText().length() > 0) {
            if (Options.getProperty(getPrefix() + preProcessingHeader.getCboFavorite().getText()) != null) {
                if (this.getObjJobJAPI() != null && this.getObjJobJAPI().getTbxClassName().getText().length() > 0 || this.getObjJobIncludeFile() != null
                        && this.getObjJobIncludeFile().getTableIncludes().isEnabled() && this.getObjJobIncludeFile().getTableIncludes().getItemCount() > 0) {
                    int c = MainWindow.message(getShell(), JOE_M_ScriptFormPreProcessing_OverwriteMonitor.label(), SWT.ICON_QUESTION | SWT.YES | SWT.NO);
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
                    preProcessingHeader.getTxtName().setText(preProcessingHeader.getCboFavorite().getText());
                    objDataProvider.setMonitorName(preProcessingHeader.getCboFavorite().getText());
                    objDataProvider.setLanguage(objDataProvider.languageAsInt(favorites.get(preProcessingHeader.getCboFavorite().getText()).toString()));
                    languageSelector.setText(objDataProvider.getLanguageAsString(objDataProvider.getLanguage()));
                    if (objDataProvider.isJava()) {
                        this.getObjJobJAPI().getTbxClassName().setText(Options.getProperty(getPrefix() + preProcessingHeader.getCboFavorite().getText()));
                    } else {
                        tabFolder.setSelection(this.getTabItemIncludedFiles());
                        String[] split = Options.getProperty(getPrefix() + preProcessingHeader.getCboFavorite().getText()).split(";");
                        for (int i = 0; i < split.length; i++) {
                            objDataProvider.addInclude(split[i]);
                        }
                    }
                    fillForm();
                }
            }
        }
    }

    @Override
    protected void initForm() {
        preProcessingHeader.init();
        if (normalized(Options.getPropertiesWithPrefix("monitor_favorite_")) != null
                && normalized(Options.getPropertiesWithPrefix("monitor_favorite_"))[0] != null) {
            preProcessingHeader.getCboFavorite().setItems(normalized(Options.getPropertiesWithPrefix("monitor_favorite_")));
        }
    }
}
