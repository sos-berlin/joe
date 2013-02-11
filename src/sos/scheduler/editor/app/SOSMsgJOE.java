package sos.scheduler.editor.app;

import java.io.File;

import org.eclipse.swt.SWT;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.custom.CCombo;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.events.FocusAdapter;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.program.Program;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Spinner;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeColumn;

import com.sos.dialog.components.SOSDateTime;
import com.sos.localization.SOSMsg;
import org.eclipse.swt.widgets.Text;

public class SOSMsgJOE extends SOSMsg {

	private static String	conClassName	= "SOSMsgJOE";

	public SOSMsgJOE(String pstrMessageCode) {
		super(pstrMessageCode);
		if (this.Messages == null) {
			super.setMessageResource("JOEMessages");
			this.Messages = super.Messages;
		}
		else {
			super.Messages = this.Messages;
		}
	} // public SOSMsgJOE

	public Text Control(final Text pobjC) {
		@SuppressWarnings("unused")
		final String conMethodName = conClassName + "::Control";
	// Text kommt im Normalfall nicht aus einer Propertie-Datei. Deswegen ergibt es keinen Sinn	
	//	pobjC.setText(label());
		
		pobjC.setToolTipText(tooltip());
		setKeyListener(pobjC);
		pobjC.addFocusListener(new FocusAdapter() {
			public void focusGained(final FocusEvent e) {
				pobjC.selectAll();
				// pobjC.setBackground(new Color(SWT.BLACK));
			}

			@Override
			public void focusLost(FocusEvent e) {
			}
		});
		return pobjC;
	} // public Text Control

	public Label Control(final Label pobjC) {
		@SuppressWarnings("unused")
		final String conMethodName = conClassName + "::Control";
		pobjC.setText(label());
		pobjC.setToolTipText(this.tooltip());
		setKeyListener(pobjC);
		return pobjC;
	} // public Label Control

	public Group Control(final Group pobjC) {
		@SuppressWarnings("unused")
		final String conMethodName = conClassName + "::Control";
		pobjC.setText(label());
		pobjC.setToolTipText(tooltip());
		setKeyListener(pobjC);
		return pobjC;
	} // public Group Control

	public Button Control(final Button pobjC) {
		@SuppressWarnings("unused")
		final String conMethodName = conClassName + "::Control";
		// Es gibt Buttons ohne Text. Die sollen keine Vorbelegung haben.

		//pobjC.setText(label());
		pobjC.setToolTipText(tooltip());
		setKeyListener(pobjC);
		return pobjC;
	} // public Button Control

	public Combo Control(final Combo pobjC) {
		@SuppressWarnings("unused")
		final String conMethodName = conClassName + "::Control";
		//pobjC.setText(label());
		pobjC.setToolTipText(tooltip());
		setKeyListener(pobjC);
		return pobjC;
	} // public Combo Control

	public Composite Control(final Composite pobjC) {
		@SuppressWarnings("unused")
		final String conMethodName = conClassName + "::Control";
		pobjC.setToolTipText(tooltip());
		setKeyListener(pobjC);
		return pobjC;
	} // public Composite Control
	
	public CCombo Control(final CCombo pobjC) {
		@SuppressWarnings("unused")
		final String conMethodName = conClassName + "::Control";
		pobjC.setToolTipText(tooltip());
		setKeyListener(pobjC);
		return pobjC;
	} // public CCombo Control

	public TableColumn Control(final TableColumn pobjC) {
		@SuppressWarnings("unused")
		final String conMethodName = conClassName + "::Control";
		pobjC.setText(label());
		pobjC.setToolTipText(tooltip());
		return pobjC;
	} // public TableColumn Control

	public Table Control(final Table pobjC) {
		@SuppressWarnings("unused")
		final String conMethodName = conClassName + "::Control";
		pobjC.setToolTipText(tooltip());
		setKeyListener(pobjC);
		return pobjC;
	} // public Table Control

	public FileDialog Control(final FileDialog pobjC) {
		@SuppressWarnings("unused")
		final String conMethodName = conClassName + "::Control";
		pobjC.setText(label());
		return pobjC;
	} // public FileDialog Control

	public Spinner Control(final Spinner pobjC) {
		@SuppressWarnings("unused")
		final String conMethodName = conClassName + "::Control";
		pobjC.setToolTipText(tooltip());
		setKeyListener(pobjC);
		return pobjC;
	} // public Spinner Control
	
	public MessageBox Control(final MessageBox pobjC) {
		@SuppressWarnings("unused")
		final String conMethodName = conClassName + "::Control";
		pobjC.setMessage(this.label());
		return pobjC;
	} // public MessageBox Control
	
	public List Control(final List pobjC) {
		@SuppressWarnings("unused")
		final String conMethodName = conClassName + "::Control";
		pobjC.setToolTipText(tooltip());
		setKeyListener(pobjC);
		return pobjC;
	} // public List Control
	
	public Tree Control(final Tree pobjC) {
		@SuppressWarnings("unused")
		final String conMethodName = conClassName + "::Control";
		pobjC.setToolTipText(this.tooltip());
		setKeyListener(pobjC);
		return pobjC;
	} // public Tree Control
	
	public Browser Control(final Browser pobjC) {
		@SuppressWarnings("unused")
		final String conMethodName = conClassName + "::Control";
		pobjC.setText(label());
		pobjC.setToolTipText(tooltip());
		setKeyListener(pobjC);
		return pobjC;
	} // public Browser Control
	
	public TreeColumn Control(final TreeColumn pobjC) {
		@SuppressWarnings("unused")
		final String conMethodName = conClassName + "::Control";
		pobjC.setText(label());
		pobjC.setToolTipText(tooltip());
		return pobjC;
	} // public TreeColumn Control
	
	public TabItem Control(final TabItem pobjC) {
		@SuppressWarnings("unused")
		final String conMethodName = conClassName + "::Control";
		pobjC.setText(label());
		pobjC.setToolTipText(this.tooltip());
		return pobjC;
	} // public TabItem Control
	
	public CTabItem Control(final CTabItem pobjC) {
		@SuppressWarnings("unused")
		final String conMethodName = conClassName + "::Control";
		pobjC.setText(label());
		pobjC.setToolTipText(this.tooltip());
		return pobjC;
	} // public CTabItem Control
	
	public SOSDateTime Control(final SOSDateTime pobjC) {
		@SuppressWarnings("unused")
		final String conMethodName = conClassName + "::Control";
		pobjC.setToolTipText(this.tooltip());
		return pobjC;
	} // public SOSDateTime Control
	
	

	private void setKeyListener(final Control pobjC) {
//		strControlName = pobjC.
		pobjC.addKeyListener(new KeyListener() {
			@Override
			public void keyPressed(KeyEvent event) {
				if (event.keyCode == SWT.F1) {
					// MainWindow.message("F1 gedrückt", SWT.ICON_INFORMATION);
					openHelp(getF1()); // "http:www.sos-berlin.com/doc/en/scheduler.doc/xml/job.xml");
				}
			}

			@Override
			public void keyReleased(KeyEvent arg0) {
			}
		});

	} // private void setKeyListener

	public void openHelp(String helpKey) {
		String lang = Options.getLanguage();
		String url = helpKey;
		try {
			// TODO: überprüfen, ob Datei wirklich existiert
			if (url.contains("http:")) {
			}
			else {
				url = new File(url).toURL().toString();
			}
			Program prog = Program.findProgram("html");
			if (prog != null)
				prog.execute(url);
			else {
				Runtime.getRuntime().exec(Options.getBrowserExec(url, lang));
			}
		}
		catch (Exception e) {
			try {
				new ErrorLog("error in " + sos.util.SOSClassUtil.getMethodName() + "; "
						+ sos.scheduler.editor.app.Messages.getString("MainListener.cannot_open_help", new String[] { url, lang, e.getMessage() }), e);
			}
			catch (Exception ee) {
				// tu nichts
			}
			e.printStackTrace();
			MainWindow.message(sos.scheduler.editor.app.Messages.getString("MainListener.cannot_open_help", new String[] { url, lang, e.getMessage() }),
					SWT.ICON_ERROR | SWT.OK);
		}
	} // public void openHelp

}
