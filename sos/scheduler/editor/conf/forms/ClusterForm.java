package sos.scheduler.editor.conf.forms;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.jdom.Element;

import sos.scheduler.editor.app.IUnsaved;
import sos.scheduler.editor.app.IUpdateLanguage;
import sos.scheduler.editor.app.Messages;
import sos.scheduler.editor.conf.SchedulerDom;
import sos.scheduler.editor.conf.listeners.ClusterListener;


public class ClusterForm extends Composite implements IUnsaved, IUpdateLanguage {


	private ClusterListener listener;

	private int            type;

	private Group          gScript      = null;

	private Label          label1       = null;

	private Text           tWarnTimeout = null;

	private Text           tTimeout     = null;

	private Text           tOwnTimeout  = null;

	private Label          label3       = null;

	private Label          label14      = null;


	public ClusterForm(Composite parent, int style) {

		super(parent, style);
		initialize();
		setToolTipText();

	}


	public ClusterForm(Composite parent, int style, SchedulerDom dom, Element element) {

		super(parent, style);

		initialize();
		setToolTipText();
		setAttributes(dom, element, type);

	}


	public void setAttributes(SchedulerDom dom, Element element, int type) {

		listener = new ClusterListener(dom, element);
		tOwnTimeout.setText(listener.getHeartbeatOwnTimeout());
		tWarnTimeout.setText(listener.getHeartbeatWarnTimeout());
		tTimeout.setText(listener.getHeartbeatTimeout());

	}


	private void initialize() {

		this.setLayout(new FillLayout());
		createGroup();
		setSize(new org.eclipse.swt.graphics.Point(604, 427));

	}


	/**
	 * This method initializes group
	 */
	 private void createGroup() {

		GridData gridData = new GridData();
		gridData.minimumWidth = 60;
		GridLayout gridLayout = new GridLayout();
		gridLayout.numColumns = 2;
		gScript = new Group(this, SWT.NONE);
		gScript.setText("Cluster");
		gScript.setLayout(gridLayout);
		label14 = new Label(gScript, SWT.NONE);
		label14.setText("Heartbeat Timeout");
		createComposite();
		label1 = new Label(gScript, SWT.NONE);
		label1.setText("Heartbeat Own Timeout");
		tOwnTimeout = new Text(gScript, SWT.BORDER);
		tOwnTimeout.setLayoutData(gridData);
		tOwnTimeout.addModifyListener(new org.eclipse.swt.events.ModifyListener() {
			public void modifyText(org.eclipse.swt.events.ModifyEvent e) {
				listener.setHeartbeatOwnTimeout(tOwnTimeout.getText());
			}
		});
		label3 = new Label(gScript, SWT.NONE);
		label3.setText("Heartbeat Warn Timeout");

		tWarnTimeout = new Text(gScript, SWT.BORDER);
		tWarnTimeout.addModifyListener(new ModifyListener() {
			public void modifyText(final ModifyEvent e) {
				listener.setHeartbeatWarnTimeout(tWarnTimeout.getText());
			}
		});
		final GridData gridData_1 = new GridData(GridData.BEGINNING, GridData.CENTER, true, false);
		gridData_1.minimumWidth = 60;
		tWarnTimeout.setLayoutData(gridData_1);

	 }


	 /**
	  * This method initializes composite
	  */
	 private void createComposite() {

		 tTimeout = new Text(gScript, SWT.BORDER);
		 tTimeout.addModifyListener(new ModifyListener() {
			 public void modifyText(final ModifyEvent e) {
				 listener.setHeartbeatTimeout(tTimeout.getText());
			 }
		 });
		 final GridData gridData = new GridData(GridData.BEGINNING, GridData.CENTER, true, false);
		 gridData.minimumWidth = 60;
		 tTimeout.setLayoutData(gridData);

	 }


	 public void setToolTipText() {

		 tOwnTimeout.setToolTipText(Messages.getTooltip("cluster.heart_beat_own_timeout"));
		 tWarnTimeout.setToolTipText(Messages.getTooltip("cluster.heart_beat_warn_timeout"));
		 tTimeout.setToolTipText(Messages.getTooltip("cluster.heart_beat_timeout"));

	 }

	 public boolean isUnsaved() {
		 return false;
	 }

	 public void apply() {

	 }

} // @jve:decl-index=0:visual-constraint="10,10"
