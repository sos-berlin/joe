/*
 * Created on 06.03.2007
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
package sos.scheduler.editor.conf.forms;

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
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;
import org.jdom.Element;

import sos.scheduler.editor.app.MainWindow;
import sos.scheduler.editor.app.Messages;
import sos.scheduler.editor.app.Utils;
import sos.scheduler.editor.conf.ISchedulerUpdate;
import sos.scheduler.editor.conf.SchedulerDom;
import sos.scheduler.editor.conf.listeners.JobsListener;
import sos.util.SOSString;

import com.swtdesigner.SWTResourceManager;

public class JobAssistentScriptForms {
	
	private Element           job          = null;
	
	private SchedulerDom      dom          = null;
	
	private ISchedulerUpdate  update       = null;
	
	private JobsListener      listener     = null;
	
	private Button            butFinish    = null;
	
	private Button            butCancel    = null;
	
	private Button            butNext      = null;
	
	private Button            butShow      = null;		
	
	private Table             tableInclude = null;
	
	private SOSString         sosString    = new SOSString();
	
	private Text              txtScript    = null; 
	
	private Text              txtLanguage  = null;  
	
	private Text              txtJavaClass = null; 
	
	private String            libraryName  = "";
	
	private Label             lblClass     = null;
	
	/** Wer hat ihn aufgerufen, der Job assistent oder job_chain assistent*/
	private int assistentType = -1; 
	
	public JobAssistentScriptForms(SchedulerDom dom_, ISchedulerUpdate update_) {
		dom = dom_;
		update = update_;
		listener = new JobsListener(dom, update);			
	}



	public void showScriptForm(Element job_, int assistentType_) {
		assistentType = assistentType_;
		job = job_;
		final Shell scriptShell = new Shell(SWT.CLOSE | SWT.TITLE | SWT.APPLICATION_MODAL | SWT.BORDER);
		final GridLayout gridLayout = new GridLayout();
		scriptShell.setLayout(gridLayout);
		scriptShell.setSize(511, 388);
		scriptShell.setText("Script");

		{
			final Group jobGroup = new Group(scriptShell, SWT.NONE);
			jobGroup.setText("Job");
			final GridData gridData = new GridData(GridData.BEGINNING, GridData.BEGINNING, false, false);
			gridData.widthHint = 490;
			gridData.heightHint = 331;
			jobGroup.setLayoutData(gridData);
			final GridLayout gridLayout_1 = new GridLayout();
			gridLayout_1.marginWidth = 10;
			gridLayout_1.marginTop = 10;
			gridLayout_1.marginRight = 10;
			gridLayout_1.marginLeft = 10;
			gridLayout_1.marginHeight = 10;
			gridLayout_1.marginBottom = 10;
			gridLayout_1.numColumns = 2;
			jobGroup.setLayout(gridLayout_1);

			{
				Text txtScript = new Text(jobGroup, SWT.MULTI);
				final GridData gridData_1 = new GridData(GridData.FILL, GridData.FILL, false, false, 2, 1);
				gridData_1.heightHint = 120;
				gridData_1.widthHint = 441;
				txtScript.setLayoutData(gridData_1);
				txtScript.setEditable(false);
				txtScript.setText(Messages.getString("assistent.script"));
			}

			{
				final Label lblLanguage = new Label(jobGroup, SWT.NONE);
				lblLanguage.setText("Language");
			}
			txtLanguage = new Text(jobGroup, SWT.NONE);
			txtLanguage.setLayoutData(new GridData(GridData.FILL, GridData.FILL, false, false));
			txtLanguage.setFont(SWTResourceManager.getFont("", 8, SWT.BOLD));
			txtLanguage.setEditable(false);
			if(job != null) {
				Element script = job.getChild("script");
				txtLanguage.setText(Utils.getAttributeValue("language", script));
			}


			/*if(this.txtLanguage.getText() != null && txtLanguage.getText().equalsIgnoreCase("com_class")){
				{
					if(job != null){
						Element script = job.getChild("script");				 
						if(script!= null) {
							java.util.List includes = script.getChildren("include");
							
						}
						
					}
				}
			}*/

			{
				lblClass = new Label(jobGroup, SWT.NONE);
				lblClass.setText("Java Class");
				if(job != null){
					Element script = job.getChild("script");
					String language = Utils.getAttributeValue("language", script); 
					if(language != null && language.equalsIgnoreCase("com_class")) {
						lblClass.setText("Com Class");
					}
				}
				
			}
			txtJavaClass = new Text(jobGroup, SWT.NONE);
			txtJavaClass.setLayoutData(new GridData(GridData.FILL, GridData.CENTER, false, false));
			txtJavaClass.setFont(SWTResourceManager.getFont("", 8, SWT.BOLD));
			txtJavaClass.setEditable(false);
			if(job != null) {
				Element script = job.getChild("script");
				txtJavaClass.setText(Utils.getAttributeValue("java_class", script));
			}

			{				
				final Label lblRessources = new Label(jobGroup, SWT.NONE);
				if(lblClass != null && lblClass.getText().equals("Com Class")) {
					lblRessources.setText("Filename");
				} else {
					lblRessources.setText("Ressource");
				}
				
				
			}

			final Text txtResource = new Text(jobGroup, SWT.NONE);
			txtResource.setEditable(false);
			txtResource.setFont(SWTResourceManager.getFont("", 8, SWT.BOLD));
			txtResource.setLayoutData(new GridData(GridData.FILL, GridData.CENTER, true, false));
			if(lblClass != null && lblClass.getText().equals("Java Class")) {
				if(libraryName != null) {
					txtResource.setText(this.libraryName);
				}
			} else {
				if(job != null) {
					Element script = job.getChild("script");				
					if(script != null && Utils.getAttributeValue("filename", script) != null &&
							Utils.getAttributeValue("filename", script).trim().length() > 0	){
						txtResource.setText(Utils.getAttributeValue("filename", script));
					}
				}
			}
			
					
			
			if(job != null){
				Element script = job.getChild("script");				 
				if(script!= null) {
					java.util.List includes = script.getChildren("include");
					if(includes.size() > 0) {
						{
							final Label lblInclude = new Label(jobGroup, SWT.NONE);
							lblInclude.setLayoutData(new GridData(GridData.BEGINNING, GridData.BEGINNING, false, false));
							lblInclude.setText("Include");
						}
						tableInclude = new Table(jobGroup, SWT.BORDER);
						final GridData gridData_1 = new GridData(GridData.FILL, GridData.FILL, false, false);
						gridData_1.widthHint = 322;
						gridData_1.heightHint = 55;
						tableInclude.setLayoutData(gridData_1);
						tableInclude.setLinesVisible(true);
						tableInclude.setHeaderVisible(true);			
					}
					for (int i = 0; i < includes.size(); i++) {
						Element include = (Element)includes.get(i);				
						if( Utils.getAttributeValue("file", include) != null &&
								Utils.getAttributeValue("file", include).length() > 0) {							
							TableItem item = new TableItem(tableInclude, SWT.NONE);
							item.setText(Utils.getAttributeValue("file", include));							
						}
					}
				}
			}

			{
				final Composite composite = new Composite(jobGroup, SWT.NONE);
				final GridData gridData_2 = new GridData(GridData.FILL, GridData.FILL, false, false);
				gridData_2.heightHint = 23;
				composite.setLayoutData(gridData_2);
				final GridLayout gridLayout_2 = new GridLayout();
				gridLayout_2.numColumns = 2;
				composite.setLayout(gridLayout_2);

				{
					butFinish = new Button(composite, SWT.NONE);
					butFinish.addSelectionListener(new SelectionAdapter() {
						public void widgetSelected(final SelectionEvent e) {
							listener.newImportJob(job, assistentType);
							scriptShell.dispose();
						}
					});
					butFinish.setText("Finish");
				}
				{
					butCancel = new Button(composite, SWT.NONE);
					butCancel.addSelectionListener(new SelectionAdapter() {
						public void widgetSelected(final SelectionEvent e) {
							scriptShell.dispose();
						}
					});
					butCancel.setText("Cancel");
				}
			}

			{
				final Composite composite = new Composite(jobGroup, SWT.NONE);
				composite.setLayoutData(new GridData(GridData.END, GridData.FILL, false, false));
				final GridLayout gridLayout_2 = new GridLayout();
				gridLayout_2.numColumns = 2;
				composite.setLayout(gridLayout_2);

				{
					butShow = new Button(composite, SWT.NONE);
					butShow.addSelectionListener(new SelectionAdapter() {
						public void widgetSelected(final SelectionEvent e) {					
							MainWindow.message(scriptShell, Utils.getElementAsString(job), SWT.OK );
						}
					});
					butShow.setText("Show");
				}
				{
					butNext = new Button(composite, SWT.NONE);
					butNext.addSelectionListener(new SelectionAdapter() {
						public void widgetSelected(final SelectionEvent e) {
							JobAssistentTimeoutForms timeout = new JobAssistentTimeoutForms(dom, update);
							timeout.showTimeOutForm(job, assistentType);
							//MainWindow.message(scriptShell, "offen???????", SWT.OK );
							scriptShell.dispose();
						}
					});
					butNext.setText("Next");
				}
			}
		}

		{
			Element script = job.getChild("script");
			if(script != null && Utils.getAttributeValue("language", script) != null &&
					Utils.getAttributeValue("language", script).trim().length() > 0	){
			}
		}

		{
			Element script = job.getChild("script");
			if(script != null && Utils.getAttributeValue("java_class", script) != null &&
					Utils.getAttributeValue("java_class", script).trim().length() > 0	){
			}
		}

		{
			Element script = job.getChild("script");
			if(script != null && Utils.getAttributeValue("filename", script) != null &&
					Utils.getAttributeValue("filename", script).trim().length() > 0	){
			}
		}
		
		scriptShell.open();
		setToolTipText();
		scriptShell.layout();		
	}

	public void setToolTipText() {
		butCancel.setToolTipText(Messages.getTooltip("assistent.cancel"));
		butNext.setToolTipText(Messages.getTooltip("assistent.next"));
		butShow.setToolTipText(Messages.getTooltip("assistent.show"));
		butFinish.setToolTipText(Messages.getTooltip("assistent.finish"));
		txtJavaClass.setToolTipText(Messages.getTooltip("assistent.java_class"));
		txtLanguage.setToolTipText(Messages.getTooltip("assistent.language"));
		if(tableInclude != null ) tableInclude.setToolTipText(Messages.getTooltip("assistent.script_include"));
	}
	
	public void setLibraryName(String libraryName) {
		this.libraryName = libraryName;
	}
}
