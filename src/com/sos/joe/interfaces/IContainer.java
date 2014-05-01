package com.sos.joe.interfaces;

import org.eclipse.swt.custom.CTabItem;

import com.sos.jobdoc.forms.DocumentationForm;
import com.sos.joe.objects.jobchain.forms.JobChainConfigurationForm;

import sos.scheduler.editor.actions.forms.ActionsForm;
import sos.scheduler.editor.conf.forms.SchedulerForm;

public interface IContainer {
	
    public SchedulerForm newScheduler();
    
    public SchedulerForm newScheduler(int type);

    public DocumentationForm newDocumentation();        

    public SchedulerForm openScheduler();
    
    public org.eclipse.swt.widgets.Composite openQuick();
    
    public org.eclipse.swt.widgets.Composite openQuick(String filename);
    
    public SchedulerForm openScheduler(String filename);
    
   // public SchedulerForm reOpenScheduler(String filename);

    public DocumentationForm openDocumentation();
    
    public DocumentationForm openDocumentation(String filename);
    
    public String openDocumentationName();

    public IEditor getCurrentEditor();

    public void setStatusInTitle();

    public void setNewFilename(String oldFilename);       

    public boolean closeAll();

    public void updateLanguages();
    
    public JobChainConfigurationForm newDetails();
    
    public JobChainConfigurationForm openDetails();
    
    //public SchedulerForm openDirectory();
    
    public SchedulerForm openDirectory(String filename);        
    
    public CTabItem getCurrentTab();
    
    public ActionsForm newActions();

	public void setTitleText(String strT);
}
