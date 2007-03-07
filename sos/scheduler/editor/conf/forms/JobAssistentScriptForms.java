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
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.jdom.Element;

import sos.scheduler.editor.app.MainWindow;
import sos.scheduler.editor.app.Messages;
import sos.scheduler.editor.app.Utils;
import sos.scheduler.editor.conf.ISchedulerUpdate;
import sos.scheduler.editor.conf.SchedulerDom;
import sos.scheduler.editor.conf.listeners.JobsListener;

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
	
	
	public JobAssistentScriptForms(SchedulerDom dom_, ISchedulerUpdate update_) {
		dom = dom_;
		update = update_;
		listener = new JobsListener(dom, update);			
	}



	public void showScriptForm(Element job_) {
		job = job_;
		final Shell scriptShell = new Shell(SWT.CLOSE | SWT.TITLE | SWT.APPLICATION_MODAL | SWT.BORDER);
		final GridLayout gridLayout = new GridLayout();
		gridLayout.numColumns = 4;
		scriptShell.setLayout(gridLayout);
		scriptShell.setSize(400, 236);
		scriptShell.setText("Script");

		{
			final Text txtScript = new Text(scriptShell, SWT.MULTI);
			txtScript.setEditable(false);
			final GridData gridData = new GridData(GridData.FILL, GridData.CENTER, true, false, 4, 1);
			gridData.heightHint = 103;
			gridData.widthHint = 384;
			txtScript.setLayoutData(gridData);
			txtScript.setText(Messages.getString("assistent.script"));
		}

		{
			final Label lblLanguage = new Label(scriptShell, SWT.NONE);
			lblLanguage.setLayoutData(new GridData());
			lblLanguage.setText("Language");
		}

		{
			final Text txtLanguage = new Text(scriptShell, SWT.NONE);
			txtLanguage.setFont(SWTResourceManager.getFont("", 10, SWT.BOLD));
			txtLanguage.setEditable(false);
			final GridData gridData = new GridData(GridData.FILL, GridData.CENTER, true, false, 3, 1);
			gridData.widthHint = 327;
			txtLanguage.setLayoutData(gridData);
			Element script = job.getChild("script");
			if(script != null && Utils.getAttributeValue("language", script) != null &&
					Utils.getAttributeValue("language", script).trim().length() > 0	){
				txtLanguage.setText(Utils.getAttributeValue("language", script));
			}
		}

		{
			final Label lblClass = new Label(scriptShell, SWT.NONE);
			lblClass.setLayoutData(new GridData());
			lblClass.setText("Class");
		}

		{
			final Text txtClass = new Text(scriptShell, SWT.NONE);
			txtClass.setFont(SWTResourceManager.getFont("", 10, SWT.BOLD));
			txtClass.setEditable(false);
			txtClass.setLayoutData(new GridData(GridData.FILL, GridData.CENTER, true, false, 3, 1));
			Element script = job.getChild("script");
			if(script != null && Utils.getAttributeValue("java_class", script) != null &&
					Utils.getAttributeValue("java_class", script).trim().length() > 0	){
				txtClass.setText(Utils.getAttributeValue("java_class", script));
			}
		}

		{
			final Label lblRessources = new Label(scriptShell, SWT.NONE);
			lblRessources.setLayoutData(new GridData());
			lblRessources.setText("Ressource");
			
		}

		{
			final Text txtResource = new Text(scriptShell, SWT.NONE);
			txtResource.setFont(SWTResourceManager.getFont("", 10, SWT.BOLD));
			txtResource.setEditable(false);
			txtResource.setLayoutData(new GridData(GridData.FILL, GridData.CENTER, true, false, 3, 1));
			Element script = job.getChild("script");
			if(script != null && Utils.getAttributeValue("filename", script) != null &&
					Utils.getAttributeValue("filename", script).trim().length() > 0	){
				txtResource.setText(Utils.getAttributeValue("filename", script));
			}
		}

		{
			butFinish = new Button(scriptShell, SWT.NONE);
			butFinish.addSelectionListener(new SelectionAdapter() {
				public void widgetSelected(final SelectionEvent e) {
					listener.newImportJob(job);
					scriptShell.dispose();
				}
			});
			butFinish.setLayoutData(new GridData(GridData.FILL, GridData.CENTER, false, false));
			butFinish.setText("Finish");
		}
		{
			butCancel = new Button(scriptShell, SWT.NONE);
			butCancel.addSelectionListener(new SelectionAdapter() {
				public void widgetSelected(final SelectionEvent e) {
					scriptShell.dispose();
				}
			});
			butCancel.setText("Cancel");
		}
		{
			butNext = new Button(scriptShell, SWT.NONE);
			butNext.addSelectionListener(new SelectionAdapter() {
				public void widgetSelected(final SelectionEvent e) {
					MainWindow.message(scriptShell, "offen???????", SWT.OK );					
				}
			});
			butNext.setText("Next");
		}
		
		scriptShell.open();

		{
			butShow = new Button(scriptShell, SWT.NONE);
			butShow.addSelectionListener(new SelectionAdapter() {
				public void widgetSelected(final SelectionEvent e) {					
					MainWindow.message(scriptShell, Utils.getElementAsString(job), SWT.OK );
				}
			});
			butShow.setLayoutData(new GridData());
			butShow.setText("Show");
		}
		setToolTipText();
		scriptShell.layout();		
	}

	public void setToolTipText() {
		butCancel.setToolTipText(Messages.getTooltip("assistent.cancel"));
		butNext.setToolTipText(Messages.getTooltip("assistent.next"));
		butShow.setToolTipText(Messages.getTooltip("assistent.show"));
		butFinish.setToolTipText(Messages.getTooltip("assistent.finish"));				
	}
}
