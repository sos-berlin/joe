package sos.scheduler.editor.app;

import org.eclipse.swt.custom.CTabItem;

import sos.scheduler.editor.conf.forms.JobChainConfigurationForm;
import sos.scheduler.editor.conf.forms.SchedulerForm;
import sos.scheduler.editor.doc.forms.DocumentationForm;

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
    
   
}
