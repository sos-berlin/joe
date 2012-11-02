package sos.scheduler.editor.conf.container;

import java.util.HashMap;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.events.FocusAdapter;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Spinner;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.Text;

import sos.scheduler.editor.app.ContextMenu;
import sos.scheduler.editor.app.Editor;
import sos.scheduler.editor.app.ErrorLog;
import sos.scheduler.editor.app.MainWindow;
import sos.scheduler.editor.app.Messages;
import sos.scheduler.editor.app.Options;
import sos.scheduler.editor.app.SOSJOEMessageCodes;
import sos.scheduler.editor.classes.FormBaseClass;
import sos.scheduler.editor.classes.LanguageSelector;
import sos.scheduler.editor.classes.TextArea;
import sos.scheduler.editor.classes.TextArea.enuSourceTypes;
import sos.scheduler.editor.conf.listeners.JOEListener;
//import sos.scheduler.editor.conf.listeners.JobListener;
import sos.scheduler.editor.conf.listeners.ScriptListener;
import sos.util.SOSString;

public class PrePostProcessingForm extends FormBaseClass {

	private static final String		conMONITOR_FAVORITE				= "monitor_favorite_";

	private boolean					init							= true;

	private Composite				tabItemJavaAPIComposite			= null;
	private Composite				tabItemIncludedFilesComposite	= null;
	private Composite				objParentComposite				= null;
	private Combo					cboPrefunction					= null;

	private HashMap<String, String>	favorites						= null;

	private Text					tbxClassName					= null;
	private Table					tableIncludes					= null;
	private LanguageSelector		languageSelector4Monitor		= null;

	private ScriptListener			objScriptDataProvider			= null;
	private Combo				    cboFavorite						= null;
	private Text txtMonitorName = null;
	private Spinner spinner = null;
	private TextArea txtPrePostProcessingScriptCode = null;
	private StyledText tSource = null;
	
	public PrePostProcessingForm(Composite pParentComposite, JOEListener pobjDataProvider,PrePostProcessingForm that) {
		super(pParentComposite, pobjDataProvider);
		objParentComposite = pParentComposite;
		objJobDataProvider = pobjDataProvider;

		objScriptDataProvider = (ScriptListener) pobjDataProvider;
		createGroup();
        getValues(that);
		FillForm();
	}

	public void apply() {
		// if (isUnsaved())
		// addParam();
	}

	public boolean isUnsaved() {
		// return bApply.isEnabled();
		return false;
	}

	private void createGroup() {
//		 objParent.setLayout(new FillLayout());
		showWaitCursor();
		
		Group gMonitorGroup = new Group(objParentComposite, SWT.NONE); // TODO i18n
		final GridData gridData_5 = new GridData(GridData.FILL, GridData.FILL, true, true, 13, 1);
		gridData_5.heightHint = 100;
		gridData_5.minimumHeight = 30;
		gMonitorGroup.setLayoutData(gridData_5);
		final GridLayout gridLayout_2 = new GridLayout();
		gridLayout_2.marginHeight = 0;
		gridLayout_2.numColumns = 4;
		gridLayout_2.marginWidth = 0;
		gridLayout_2.marginHeight = 0;
		gMonitorGroup.setLayout(gridLayout_2);
		gMonitorGroup.setText(Messages.getLabel("job.executable.label"));

		GridData gd_Label = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);

		final Label nameLabel = new Label(gMonitorGroup, SWT.NONE);
		nameLabel.setLayoutData(gd_Label);
		nameLabel.setText("Name");

		txtMonitorName = new Text(gMonitorGroup, SWT.BORDER);
		txtMonitorName.addFocusListener(new FocusAdapter() {
			public void focusGained(final FocusEvent e) {
				txtMonitorName.selectAll();
			}
		});
		GridData gd_txtName = new GridData(GridData.FILL, GridData.CENTER, true, false);
		gd_txtName.widthHint = 135;
		txtMonitorName.setLayoutData(gd_txtName);
		txtMonitorName.addModifyListener(new ModifyListener() {
			public void modifyText(final ModifyEvent e) {
				if (!init) {
					objScriptDataProvider.setMonitorName(txtMonitorName.getText());
				}
			}
		});

		final Label orderingLabel = new Label(gMonitorGroup, SWT.NONE);
		orderingLabel.setText("Ordering");
		orderingLabel.setLayoutData(gd_Label);

		spinner = new Spinner(gMonitorGroup, SWT.BORDER);
		GridData gd_spinner = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_spinner.widthHint = 106;
		spinner.setLayoutData(gd_spinner);
		spinner.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(final SelectionEvent e) {
				if (!init)
					objScriptDataProvider.setOrdering(String.valueOf(spinner.getSelection()));
			}
		});
		spinner.setSelection(-1);
		spinner.setMaximum(999);

		Label labelLanguageSelector = new Label(gMonitorGroup, SWT.NONE);
		labelLanguageSelector.setLayoutData(new GridData(SWT.BEGINNING, SWT.CENTER, false, false, 1, 1));
		labelLanguageSelector.setText(Messages.getLabel("Language.Monitor"));

		languageSelector4Monitor = new LanguageSelector(gMonitorGroup, SWT.NONE);
		languageSelector4Monitor.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent arg0) {
				if (objScriptDataProvider != null && init == false) {
					String strT = languageSelector4Monitor.getText();
					objScriptDataProvider.setLanguage(strT);
					FillForm();
				}
			}
		});

		new Label(gMonitorGroup, SWT.NONE);
		new Label(gMonitorGroup, SWT.NONE);

		GridData gd_combo = new GridData(SWT.LEFT, SWT.CENTER, true, false, 1, 1);
		gd_combo.minimumWidth = 100;
		languageSelector4Monitor.setLayoutData(gd_combo);
		languageSelector4Monitor.select(0);
		languageSelector4Monitor.setItems(ScriptListener._languagesMonitor);

		Button butFavorite = new Button(gMonitorGroup, SWT.NONE);

		butFavorite.setEnabled(true);
		butFavorite.setVisible(true);
		butFavorite.setText("Favorites"); // TODO lang "Favorites"

		cboFavorite = new Combo(gMonitorGroup, intComboBoxStyle);
		GridData gd_cboFavorite = new GridData(SWT.LEFT, SWT.CENTER, true, false);
		gd_cboFavorite.widthHint = 153;
		cboFavorite.setLayoutData(gd_cboFavorite);
		cboFavorite.setVisible(true);

		butFavorite.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(final SelectionEvent e) {
				Options.setProperty(
						conMONITOR_FAVORITE + objScriptDataProvider.getLanguage(objScriptDataProvider.getLanguage()) + "_" + txtMonitorName.getText(),
						getFavoriteValue());
				Options.saveProperties();
				cboFavorite.setItems(normalized(Options.getPropertiesWithPrefix(conMONITOR_FAVORITE)));
			}
		});

		cboFavorite.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(final SelectionEvent e) {
				String strFavText = cboFavorite.getText();
				if (strFavText.length() > 0) {
					if (Options.getProperty(getPrefix(strFavText) + cboFavorite.getText()) != null) {
						if ((tbxClassName.isEnabled() && tbxClassName.getText().length() > 0)
								|| (tableIncludes.isEnabled() && tableIncludes.getItemCount() > 0)) {
							int c = MainWindow.message(getShell(), "Overwrite this Monitor?", SWT.ICON_QUESTION | SWT.YES | SWT.NO);
							if (c != SWT.YES)
								return;
							else {
								tbxClassName.setText("");
								tableIncludes.clearAll();
								objScriptDataProvider.removeIncludes();
							}
						}

						if (favorites != null && favorites.get(strFavText) != null && favorites.get(strFavText).toString().length() > 0) {
							objScriptDataProvider.setLanguage(objScriptDataProvider.languageAsInt(favorites.get(strFavText).toString()));
							txtMonitorName.setText(strFavText);
							if (objScriptDataProvider.isJava()) {
								tbxClassName.setText(Options.getProperty(getPrefix(strFavText) + strFavText));
							}
							else {
								String[] split = Options.getProperty(getPrefix(strFavText) + cboFavorite.getText()).split(";");
								for (int i = 0; i < split.length; i++) {
									objScriptDataProvider.addInclude(split[i]);
								}
							}
						}
					}
				}
			}
		});

		if (normalized(Options.getPropertiesWithPrefix(conMONITOR_FAVORITE)) != null
				&& normalized(Options.getPropertiesWithPrefix(conMONITOR_FAVORITE))[0] != null) {
			cboFavorite.setItems(normalized(Options.getPropertiesWithPrefix(conMONITOR_FAVORITE)));
		}

		createScriptTab2(gMonitorGroup, enuSourceTypes.MonitorSource);
		setResizableV(gMonitorGroup);
		objParentComposite.layout();

		restoreCursor();
		
	}
	
	private void getValues(PrePostProcessingForm that){
	    
	}

	private void FillForm() {
		
     	txtMonitorName.setText(objScriptDataProvider.getName());
		spinner.setSelection((objScriptDataProvider.getOrdering().length() == 0 ? 0 : Integer.parseInt(objScriptDataProvider.getOrdering())));

		int language = objScriptDataProvider.getLanguage();

		if (language != ScriptListener.NONE) {
			if (objScriptDataProvider.getSource().length() > 0)
				tSource.setText(objScriptDataProvider.getSource());
			else {
				String strT = tSource.getText();
				if (strT.length() > 0)
					objScriptDataProvider.setSource(strT);
			}
		}

		languageSelector4Monitor.selectLanguageItem(language);

		if (!languageSelector4Monitor.isShell() && !languageSelector4Monitor.isJava()) {
			// TODO mußß aus dem Dataprovider kommen ...
			String[] lan = new String[] { "spooler_task_before", "spooler_task_after", "spooler_process_before", "spooler_process_after" };
			cboPrefunction.setItems(lan);
			cboPrefunction.setEnabled(true);
		}
		else {
			cboPrefunction.setItems(new String [] {""});
			cboPrefunction.setEnabled(false);
		}
		cboFavorite.setData("favorites", favorites);
		cboFavorite.setMenu(new ContextMenu(cboFavorite, objScriptDataProvider.getDom(), Editor.SCRIPT).getMenu());
	}

	private void createScriptTab2(Composite pParentComposite, final enuSourceTypes penuSourceType) {
		init = true;

		Label lblPrefunction1 = new Label(pParentComposite, SWT.NONE);
		lblPrefunction1.setText(Messages.getLabel("job.selectpredefinedfunctions"));
		cboPrefunction = new Combo(pParentComposite, intComboBoxStyle);
		cboPrefunction.setVisibleItemCount(7);

		cboPrefunction.setLayoutData(new GridData(GridData.FILL, GridData.CENTER, true, false));

		// Button btnFont = new Button(pParentComposite, SWT.NONE);
		// btnFont.setText(Messages.getLabel("job.font"));
		//
		// btnEditButton = new Button(pParentComposite, SWT.NONE);
		// final GridData gridData_3 = new GridData(GridData.CENTER, GridData.BEGINNING, false, false);
		// gridData_3.widthHint = 30;
		// btnEditButton.setLayoutData(gridData_3);
		// btnEditButton.setImage(objScriptDataProvider.getImage("icon_edit.gif"));

		txtPrePostProcessingScriptCode = new TextArea(pParentComposite, SWT.V_SCROLL | SWT.MULTI | SWT.BORDER | SWT.H_SCROLL);
		txtPrePostProcessingScriptCode.setDataProvider(objScriptDataProvider, penuSourceType);
		tSource = txtPrePostProcessingScriptCode.getControl();

		// btnFont.addSelectionListener(new SelectionAdapter() {
		// @Override
		// public void widgetSelected(SelectionEvent e) {
		// txtArea.changeFont();
		// }
		// });
		//
		// btnEditButton.addSelectionListener(new SelectionAdapter() {
		// public void widgetSelected(final SelectionEvent e) {
		// txtArea.StartExternalEditor();
		// }
		// });
		//

		cboPrefunction.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(final SelectionEvent e) {
				if (cboPrefunction.getText().length() > 0) {
					String lan = "function_" + "monitor" + "_" + objScriptDataProvider.getLanguage(objScriptDataProvider.getLanguage()) + "_";
					String sourceTemplate = Options.getProperty(lan.toLowerCase() + cboPrefunction.getText());
					if (sourceTemplate != null) {
						tSource.append(Options.getProperty(lan.toLowerCase() + cboPrefunction.getText()));
						cboPrefunction.setText("");
						tSource.setFocus();
					}
				}
			}
		});
		// int language = objScriptDataProvider.getLanguage();
		// if (language != JobListener.NONE) {
		// String strSource = objScriptDataProvider.getSource();
		// if (strSource.length() > 0)
		// tSource.setText(strSource);
		// // else {
		// // if (tSource.getText().length() > 0)
		// // objScriptDataProvider.setSource(tSource.getText());
		// // }
		// }

		init = false;
		pParentComposite.layout();
	}

	private String getPrefix(final String pstrFavoriteName) {
		if (favorites != null && pstrFavoriteName.length() > 0 && favorites.get(pstrFavoriteName) != null) {
			return conMONITOR_FAVORITE + favorites.get(pstrFavoriteName) + "_";
		}
		if (objScriptDataProvider.getLanguage() == 0) {
			return "";
		}
		return conMONITOR_FAVORITE + objScriptDataProvider.getLanguage(objScriptDataProvider.getLanguage()) + "_";
	}

	public String getData(String filename) {

		String data = ".";
		return data;
	}

	private String getFavoriteValue() {
		if (objScriptDataProvider.isJava()) {
			return tbxClassName.getText();
		}
		else {
			return objScriptDataProvider.getIncludesAsString();
		}
	}

	private String[] normalized(String[] str) {
		String[] retVal = new String[] { "" };
		SOSString sosString = new SOSString();
		try {
			favorites = new HashMap<String, String>();
			if (str == null)
				return new String[0];

			String newstr = "";
			retVal = new String[str.length];
			for (int i = 0; i < str.length; i++) {
				String s = sosString.parseToString(str[i]);
				int idx = s.indexOf("_");
				if (idx > -1) {
					String lan = s.substring(0, idx);
					String name = s.substring(idx + 1);
					if (name == null || lan == null) {

					}
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
				new ErrorLog("error in " + sos.util.SOSClassUtil.getMethodName(), e);
			}
			catch (Exception ee) {
				// tu nichts
			}
			return retVal;
		}
	}

} // @jve:decl-index=0:visual-constraint="10,10"
