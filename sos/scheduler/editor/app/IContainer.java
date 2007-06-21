package sos.scheduler.editor.app;

import sos.scheduler.editor.conf.forms.JobChainConfigurationForm;
import sos.scheduler.editor.conf.forms.SchedulerForm;
import sos.scheduler.editor.doc.forms.DocumentationForm;

public interface IContainer {
	
    public SchedulerForm newScheduler();

    public DocumentationForm newDocumentation();

    public SchedulerForm openScheduler();
    
    public org.eclipse.swt.widgets.Composite openQuick();
    
    public SchedulerForm openScheduler(String filename);

    public DocumentationForm openDocumentation();
    
    public String openDocumentationName();

    public IEditor getCurrentEditor();

    public void setStatusInTitle();

    public void setNewFilename(String oldFilename);

    public boolean closeAll();

    public void updateLanguages();
    
    public JobChainConfigurationForm newDetails();
    
    public JobChainConfigurationForm openDetails();
    
    public SchedulerForm openDirectory();
    
    
}
