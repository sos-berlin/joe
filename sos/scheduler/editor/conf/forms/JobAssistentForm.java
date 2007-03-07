package sos.scheduler.editor.conf.forms;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator; 
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeColumn;
import org.eclipse.swt.widgets.TreeItem;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;
import org.eclipse.swt.layout.*;

import sos.scheduler.editor.app.MainWindow;
import sos.scheduler.editor.app.Messages;
import sos.scheduler.editor.conf.ISchedulerUpdate;
import sos.scheduler.editor.conf.SchedulerDom;
import sos.scheduler.editor.conf.listeners.JobsListener;
import sos.scheduler.editor.conf.listeners.JobListener;
import org.eclipse.swt.widgets.Table;
import org.jdom.Element;

public class JobAssistentForm {
	private Shell shell = null;			
	private JobsListener listener;
	private JobListener joblistener;
	private SchedulerDom dom = null;
	private ISchedulerUpdate update = null;
	
	/** Parameter: isStandaloneJob = true -> StandaloneJob, sonst Order Job*/
	private boolean isStandaloneJob = true;
	
	private Button radStandalonejob = null;
	
	private Button radOrderjob = null;
	
	public JobAssistentForm(SchedulerDom dom_, ISchedulerUpdate update_) {
		dom = dom_;
		update = update_;
		listener = new JobsListener(dom, update);
	}	
	
	
	
	
	public void startJobAssistant() {
		try {
			
			//int cont = MainWindow.message(shell, sos.scheduler.editor.app.Messages.getString("no_selected_tree"), SWT.ICON_WARNING | SWT.OK );
			//Form Order or standalone Job
			JobAssistentTypeForms typeForms = new JobAssistentTypeForms(dom, update);
			Element job = new Element("job");						
			typeForms.showTypeForms(job);									
									
		} catch (Exception e){
			System.out.println("error in JobAssistent.startJobAssistent: " + e.getMessage() );
		}
			
			
	}
	
	
	
	public void setToolTipText() {
		//butImport.setToolTipText(Messages.getTooltip("butImport"));					
	}
	
}
