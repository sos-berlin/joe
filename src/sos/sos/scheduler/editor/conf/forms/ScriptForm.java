package sos.scheduler.editor.conf.forms;

import java.util.HashMap;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.FocusAdapter;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.VerifyEvent;
import org.eclipse.swt.events.VerifyListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Spinner;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.Text;
import org.jdom.Element;

import sos.scheduler.editor.app.ContextMenu;
import sos.scheduler.editor.app.Editor;
import sos.scheduler.editor.app.ErrorLog;
import sos.scheduler.editor.app.IUnsaved;
import sos.scheduler.editor.app.IUpdateLanguage;
import sos.scheduler.editor.app.MainWindow;
import sos.scheduler.editor.app.Messages;
import sos.scheduler.editor.app.Options;
import sos.scheduler.editor.app.ResourceManager;
import sos.scheduler.editor.app.Utils;
import sos.scheduler.editor.classes.LanguageSelector;
import sos.scheduler.editor.conf.ISchedulerUpdate;
import sos.scheduler.editor.conf.SchedulerDom;
import sos.scheduler.editor.conf.listeners.ScriptListener;
import sos.util.SOSString;

public class ScriptForm extends Composite implements IUnsaved, IUpdateLanguage {

	private Button				butFavorite			= null;
	private ScriptListener		listener			= null;
	private String				groupTitle			= "Script";
	private int					type				= -1;
	private Group				gScript_1;
	private Text				tClass				= null;
	private Group				gInclude			= null;
	private Button				bRemove				= null;
	private Text				tInclude			= null;
	private Button				bAdd				= null;
	private Group				gSource				= null;
	private Text				tSource				= null;
	private Label				label14				= null;
	private Label				label				= null;
	private Text				txtName				= null;
	private Spinner				spinner				= null;
	private ISchedulerUpdate	update				= null;
	private Table				tableIncludes		= null;
	private Button				butIsLifeFile		= null;
	private boolean				init				= false;
	private Combo				cboFavorite			= null;
	private HashMap				favorites			= null;
	private SOSString			sosString			= null;
	private Label				lblPrefunction		= null;
	private Combo				cboPrefunction		= null;
	private Button				button				= null;
	private Text				tClasspath			= null;
	private LanguageSelector	languageSelector	= null;

	/**
	 * @wbp.parser.constructor
	 */
	public ScriptForm(Composite parent, int style, ISchedulerUpdate update_) {
		super(parent, style);
		init = true;
		update = update_;
		initialize();
		setToolTipText();
		init = false;

	}

	public ScriptForm(Composite parent, int style, String title, SchedulerDom dom, Element element, int type, ISchedulerUpdate update_) {

		super(parent, style);
		init = true;
		this.type = type;
		update = update_;
		groupTitle = title;
		initialize();
		setToolTipText();
		setAttributes(dom, element, type);
		

		gScript_1.setEnabled(Utils.isElementEnabled("job", dom, element));
		init = false;

	}

	public void setAttributes(SchedulerDom dom, Element element, int type_) {

		listener = new ScriptListener(dom, element, type_, update);
	
		cboFavorite.setData("favorites", favorites);
		if (type == Editor.MONITOR) {
			cboFavorite.setMenu(new ContextMenu(cboFavorite, listener.getDom(), Editor.SCRIPT).getMenu());
		}
		this.type = type_;
		fillForm();
	}

	public void apply() {
		if (isUnsaved())
			applyFile();
	}

	public boolean isUnsaved() {
		return bAdd.isEnabled();
	}

	private void initialize() {
		this.setLayout(new FillLayout());
		createGroup();
		setSize(new org.eclipse.swt.graphics.Point(604, 427));
		sosString = new SOSString();
		if (normalized(Options.getPropertiesWithPrefix("monitor_favorite_")) != null
				&& normalized(Options.getPropertiesWithPrefix("monitor_favorite_"))[0] != null) {
			cboFavorite.setItems(normalized(Options.getPropertiesWithPrefix("monitor_favorite_")));
		}
	}

	/**
	 * This method initializes group
	 */
	private void createGroup() {
		final Display display = this.getDisplay();
		GridLayout gridLayout = new GridLayout();
		gridLayout.numColumns = 13;
		gScript_1 = new Group(this, SWT.NONE);
		gScript_1.setText(groupTitle);
		gScript_1.setLayout(gridLayout);

		if (type == Editor.MONITOR) {

			final Composite scriptcom = new Composite(gScript_1, SWT.NONE);
			scriptcom.setLayoutData(new GridData(GridData.FILL, GridData.CENTER, true, false, 13, 1));
			final GridLayout gridLayout_1 = new GridLayout();
			gridLayout_1.verticalSpacing = 0;
			gridLayout_1.marginWidth = 0;
			gridLayout_1.marginHeight = 0;
			gridLayout_1.horizontalSpacing = 0;
			gridLayout_1.numColumns = 13;
			scriptcom.setLayout(gridLayout_1);

			final Label nameLabel = new Label(scriptcom, SWT.NONE);
			GridData gd_nameLabel = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
			gd_nameLabel.widthHint = 60;
			nameLabel.setLayoutData(gd_nameLabel);
			nameLabel.setText("Name: ");

			txtName = new Text(scriptcom, SWT.BORDER);
			txtName.addFocusListener(new FocusAdapter() {
				public void focusGained(final FocusEvent e) {
					txtName.selectAll();
				}
			});
			GridData gd_txtName = new GridData(GridData.FILL, GridData.CENTER, true, false);
			gd_txtName.widthHint = 135;
			txtName.setLayoutData(gd_txtName);
			txtName.addModifyListener(new ModifyListener() {
				public void modifyText(final ModifyEvent e) {
					if (!init)
						listener.setName(txtName.getText());
				}
			});

			final Label orderingLabel = new Label(scriptcom, SWT.NONE);
			orderingLabel.setText("  Ordering: ");

			spinner = new Spinner(scriptcom, SWT.BORDER);
			GridData gd_spinner = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
			gd_spinner.widthHint = 106;
			spinner.setLayoutData(gd_spinner);
			spinner.addSelectionListener(new SelectionAdapter() {
				public void widgetSelected(final SelectionEvent e) {
					if (!init)
						listener.setOrdering(String.valueOf(spinner.getSelection()));
				}
			});
			spinner.setSelection(-1);
			spinner.setMaximum(999);

		}
		label14 = new Label(gScript_1, SWT.NONE);
		label14.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		label14.setText("Language:");
		createComposite();
		new Label(gScript_1, SWT.NONE);

		butFavorite = new Button(gScript_1, SWT.NONE);

		butFavorite.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(final SelectionEvent e) {
				Options.setProperty("monitor_favorite_" + listener.getLanguage(listener.getLanguage()) + "_" + txtName.getText(), getFavoriteValue());
				Options.saveProperties();
				cboFavorite.setItems(normalized(Options.getPropertiesWithPrefix("monitor_favorite_")));
			}
		});
		butFavorite.setEnabled(false);
		butFavorite.setVisible(type == Editor.MONITOR);
		butFavorite.setText("Favorites");

		cboFavorite = new Combo(gScript_1, SWT.NONE);
		GridData gd_cboFavorite = new GridData(SWT.LEFT, SWT.CENTER, false, false, 3, 1);
		gd_cboFavorite.widthHint = 153;
		cboFavorite.setLayoutData(gd_cboFavorite);
		cboFavorite.setVisible(type == Editor.MONITOR);
		cboFavorite.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(final SelectionEvent e) {
				if (cboFavorite.getText().length() > 0) {
					if (Options.getProperty(getPrefix() + cboFavorite.getText()) != null) {

						if ((tClass.isEnabled() && tClass.getText().length() > 0) || (tableIncludes.isEnabled() && tableIncludes.getItemCount() > 0)) {
							int c = MainWindow.message(getShell(), "Overwrite this Monitor?", SWT.ICON_QUESTION | SWT.YES | SWT.NO);
							if (c != SWT.YES)
								return;
							else {
								tClass.setText("");
								tableIncludes.clearAll();
								listener.removeIncludes();
							}
						}

						if (favorites != null && favorites.get(cboFavorite.getText()) != null && favorites.get(cboFavorite.getText()).toString().length() > 0) {

							listener.setLanguage(listener.languageAsInt(favorites.get(cboFavorite.getText()).toString()));

							txtName.setText(cboFavorite.getText());

							if (listener.isJava()) {
								tClass.setText(Options.getProperty(getPrefix() + cboFavorite.getText()));
							}
							else {
								String[] split = Options.getProperty(getPrefix() + cboFavorite.getText()).split(";");
								for (int i = 0; i < split.length; i++) {
									listener.addInclude(split[i]);
								}
							}

							fillForm();
						}
					}

				}
			}
		});
		new Label(gScript_1, SWT.NONE);
		new Label(gScript_1, SWT.NONE);
		new Label(gScript_1, SWT.NONE);
		new Label(gScript_1, SWT.NONE);
		new Label(gScript_1, SWT.NONE);
		new Label(gScript_1, SWT.NONE);
		

		gSource = new Group(gScript_1, SWT.NONE);
		final GridData gridData_5 = new GridData(GridData.FILL, GridData.FILL, true, true, 13, 1);
		gridData_5.heightHint = 100;
		gridData_5.minimumHeight = 30;
		gSource.setLayoutData(gridData_5);
		final GridLayout gridLayout_2 = new GridLayout();
		gridLayout_2.marginHeight = 0;
		gridLayout_2.numColumns = 4;
		gSource.setLayout(gridLayout_2);
		gSource.setText("Source Code");
		
				lblPrefunction = new Label(gSource, SWT.NONE);
				lblPrefunction.setText("Select predefined functions:");
				
						cboPrefunction = new Combo(gSource, SWT.READ_ONLY);
						cboPrefunction.setVisibleItemCount(7);
						cboPrefunction.addSelectionListener(new SelectionAdapter() {
							public void widgetSelected(final SelectionEvent e) {
								if (cboPrefunction.getText().length() > 0) {
									String lan = "function_" + ((type == Editor.MONITOR) ? "monitor" : "job") + "_" + listener.getLanguage(listener.getLanguage()) + "_";
									String sourceTemplate = Options.getProperty(lan.toLowerCase() + cboPrefunction.getText());
									if (sourceTemplate != null) {
									   tSource.append(Options.getProperty(lan.toLowerCase() + cboPrefunction.getText()));
									   cboPrefunction.setText("");
									   tSource.setFocus();
									}
								}
							}
						});
						cboPrefunction.setLayoutData(new GridData(GridData.FILL, GridData.CENTER, true, false));
						
								Button btnFont = new Button(gSource, SWT.NONE);
								btnFont.addSelectionListener(new SelectionAdapter() {
									@Override
									public void widgetSelected(SelectionEvent e) {
										SchedulerEditorFontDialog fd = new SchedulerEditorFontDialog(tSource.getFont().getFontData()[0], tSource.getForeground().getRGB());
										fd.show(display);
										setFont(fd.getFontData(), fd.getForeGround());
									}
								});
								btnFont.setText("Font");
								
										button = new Button(gSource, SWT.NONE);
										final GridData gridData_3 = new GridData(GridData.CENTER, GridData.BEGINNING, false, false);
										gridData_3.widthHint = 30;
										button.setLayoutData(gridData_3);
										button.addSelectionListener(new SelectionAdapter() {
											public void widgetSelected(final SelectionEvent e) {
												String text = "";
												if (type != Editor.SCRIPT) {
													String lan = "function_" + ((type == Editor.MONITOR) ? "monitor" : "job") + "_" + listener.getLanguage(listener.getLanguage()) + "_";
													text = sos.scheduler.editor.app.Utils.showClipboard(tSource.getText(), getShell(), true, "", true, lan, false);
												}
												else {
													text = sos.scheduler.editor.app.Utils.showClipboard(tSource.getText(), getShell(), true, "");
												}
												if (text != null)
													tSource.setText(text);

											}
										});
										button.setImage(ResourceManager.getImageFromResource("/sos/scheduler/editor/icon_edit.gif"));
										
												tSource = new Text(gSource, SWT.V_SCROLL | SWT.MULTI | SWT.BORDER | SWT.H_SCROLL);
												
														tSource.addVerifyListener(new VerifyListener() {
															public void verifyText(final VerifyEvent e) {
																if (e.text.length() > 0 && e.text.trim().length() > 0 && languageSelector.isJava() && tClass.getText().length() > 0) {
																	MainWindow.message("Please remove first Classname.", SWT.ICON_WARNING);
																	e.doit = false;
																	return;
																}
															}
														});
														tSource.addKeyListener(new KeyAdapter() {
															public void keyPressed(final KeyEvent e) {
																if (e.keyCode == 97 && e.stateMask == SWT.CTRL) {
																	tSource.setSelection(0, tSource.getText().length());
																}
															}
														});
														final GridData gridData_1 = new GridData(GridData.FILL, GridData.FILL, true, true, 4, 1);
														gridData_1.minimumHeight = 40;
														gridData_1.widthHint = 454;
														gridData_1.heightHint = 139;
														tSource.setLayoutData(gridData_1);
														
																SchedulerEditorFontDialog fd = new SchedulerEditorFontDialog(tSource.getFont().getFontData()[0], tSource.getForeground().getRGB());
																
																		tSource.addModifyListener(new org.eclipse.swt.events.ModifyListener() {
																			public void modifyText(org.eclipse.swt.events.ModifyEvent e) {
																
																				if (!init)
																					listener.setSource(tSource.getText());
																			}
																		});
		new Label(gScript_1, SWT.NONE);
		new Label(gScript_1, SWT.NONE);
		new Label(gScript_1, SWT.NONE);
		new Label(gScript_1, SWT.NONE);
		new Label(gScript_1, SWT.NONE);
		new Label(gScript_1, SWT.NONE);
		new Label(gScript_1, SWT.NONE);
		new Label(gScript_1, SWT.NONE);
		new Label(gScript_1, SWT.NONE);
		new Label(gScript_1, SWT.NONE);
		new Label(gScript_1, SWT.NONE);
		new Label(gScript_1, SWT.NONE);
		new Label(gScript_1, SWT.NONE);
		
		Label lblNewLabel = new Label(gScript_1, SWT.NONE);
		lblNewLabel.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblNewLabel.setText("Clasname");

		 
		tClass = new Text(gScript_1, SWT.BORDER);
		tClass.setEnabled(false);
		tClass.addFocusListener(new FocusAdapter() {
			public void focusGained(final FocusEvent e) {
				tClass.selectAll();
			}
		});
		tClass.addVerifyListener(new VerifyListener() {
			public void verifyText(final VerifyEvent e) {
				if (e.text.length() > 0 && languageSelector.isJava() && tSource.getText().length() > 0) {
					MainWindow.message("Please remove first Source Code.", SWT.ICON_WARNING);
					e.doit = false;
					return;
				}
			}
		});
		GridData gd_tClass = new GridData(GridData.FILL, GridData.CENTER, true, false);
		gd_tClass.horizontalSpan = 8;
		tClass.setLayoutData(gd_tClass);
		tClass.addModifyListener(new org.eclipse.swt.events.ModifyListener() {
			public void modifyText(org.eclipse.swt.events.ModifyEvent e) {
				if (!init) {
					if (languageSelector.isJava())
						listener.setJavaClass(tClass.getText());
				}
			}
		});
		new Label(gScript_1, SWT.NONE);
		new Label(gScript_1, SWT.NONE);
		new Label(gScript_1, SWT.NONE);
		new Label(gScript_1, SWT.NONE);
		
		Label lblNewLabel_1 = new Label(gScript_1, SWT.NONE);
		lblNewLabel_1.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblNewLabel_1.setText("Classpath");
		
			  
		GridData gd_tClasspath = new GridData(GridData.FILL, GridData.CENTER, true, false);
		gd_tClasspath.horizontalSpan = 8;
		tClasspath = new Text(gScript_1, SWT.BORDER);
		tClasspath.setLayoutData(gd_tClasspath);
		tClasspath.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent arg0) {
				listener.setClasspath(tClasspath.getText());
			}
		});
		tClasspath.setEnabled(false);
		new Label(gScript_1, SWT.NONE);
		new Label(gScript_1, SWT.NONE);
		new Label(gScript_1, SWT.NONE);
		new Label(gScript_1, SWT.NONE);
		
		 

		GridLayout gridLayout1 = new GridLayout();
		gridLayout1.marginWidth = 0;
		gridLayout1.marginHeight = 0;
		gridLayout1.numColumns = 3;
		gInclude = new Group(gScript_1, SWT.NONE);
		gInclude.setLayoutData(new GridData(GridData.FILL, GridData.BEGINNING, false, true, 13, 1));
		gInclude.setText("Include Files");
		gInclude.setLayout(gridLayout1);
		
				butIsLifeFile = new Button(gInclude, SWT.CHECK);
				butIsLifeFile.setText("in live Folder");
				tInclude = new Text(gInclude, SWT.BORDER);
				tInclude.addFocusListener(new FocusAdapter() {
					public void focusGained(final FocusEvent e) {
						tInclude.selectAll();
					}
				});
				tInclude.setLayoutData(new GridData(GridData.FILL, GridData.CENTER, true, false));
				bAdd = new Button(gInclude, SWT.NONE);
				label = new Label(gInclude, SWT.SEPARATOR | SWT.HORIZONTAL);
				label.setText("Label");
				label.setLayoutData(new org.eclipse.swt.layout.GridData(GridData.FILL, GridData.FILL, false, false, 3, 1));
				
						tableIncludes = new Table(gInclude, SWT.FULL_SELECTION | SWT.BORDER);
						tableIncludes.addSelectionListener(new SelectionAdapter() {
							public void widgetSelected(final SelectionEvent e) {
								if (tableIncludes.getSelectionCount() > 0) {

									tInclude.setText(tableIncludes.getSelection()[0].getText(0));
									tInclude.setEnabled(true);
									butIsLifeFile.setSelection(tableIncludes.getSelection()[0].getText(1) != null
											&& tableIncludes.getSelection()[0].getText(1).equals("live_file"));
									bRemove.setEnabled(tableIncludes.getSelectionCount() > 0);
									bAdd.setEnabled(false);
								}

							}
						});
						tableIncludes.setLinesVisible(true);
						tableIncludes.setHeaderVisible(true);
						final GridData gridData_2 = new GridData(GridData.FILL, GridData.FILL, true, true, 2, 2);
						gridData_2.heightHint = 4;
						gridData_2.minimumHeight = 20;
						tableIncludes.setLayoutData(gridData_2);
						
								final TableColumn newColumnTableColumn = new TableColumn(tableIncludes, SWT.NONE);
								newColumnTableColumn.setWidth(272);
								newColumnTableColumn.setText("Name");
								
										final TableColumn newColumnTableColumn_1 = new TableColumn(tableIncludes, SWT.NONE);
										newColumnTableColumn_1.setWidth(81);
										newColumnTableColumn_1.setText("File/Life File");
										
												final Button butNew = new Button(gInclude, SWT.NONE);
												butNew.addSelectionListener(new SelectionAdapter() {
													public void widgetSelected(final SelectionEvent e) {
														tableIncludes.deselectAll();
														tInclude.setText("");
														tInclude.setEnabled(true);
														butIsLifeFile.setSelection(false);
														tInclude.setFocus();
													}
												});
												butNew.setLayoutData(new GridData(GridData.FILL, GridData.CENTER, false, false));
												butNew.setText("New");
												tInclude.addModifyListener(new org.eclipse.swt.events.ModifyListener() {
													public void modifyText(org.eclipse.swt.events.ModifyEvent e) {
														bAdd.setEnabled(!tInclude.getText().equals(""));
													}
												});
												tInclude.addKeyListener(new org.eclipse.swt.events.KeyAdapter() {
													public void keyPressed(org.eclipse.swt.events.KeyEvent e) {
														if (e.keyCode == SWT.CR && !tInclude.getText().equals("")) {
															listener.addInclude(tableIncludes, tInclude.getText(), butIsLifeFile.getSelection());
															listener.fillTable(tableIncludes);
															tInclude.setText("");
														}
													}
												});
												bAdd.setText("&Add File");
												bAdd.setLayoutData(new GridData(GridData.FILL, GridData.CENTER, false, false));
												bAdd.setEnabled(false);
												bAdd.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
													public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
														applyFile();
													}
												});
												bRemove = new Button(gInclude, SWT.NONE);
												bRemove.setText("Remove File");
												bRemove.setEnabled(false);
												bRemove.setLayoutData(new GridData(GridData.FILL, GridData.BEGINNING, false, false));
												bRemove.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
													public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
														if (tableIncludes.getSelectionCount() > 0) {
															int index = tableIncludes.getSelectionIndex();
															listener.removeInclude(index);
															listener.fillTable(tableIncludes);
															if (index >= tableIncludes.getItemCount())
																index--;
															//if (tableIncludes.getItemCount() > 0)
															//	tableIncludes.setSelection(index);
															tableIncludes.deselectAll();
															tInclude.setText("");
															tInclude.setEnabled(false);
														}

													}
												});

		fd.readFontData();
		setFont(fd.getFontData(), fd.getForeGround());

	}

	/**
	 * This method initializes composite
	 */
	private void createComposite() {
		
				languageSelector = new LanguageSelector(gScript_1, SWT.NONE);
				languageSelector.addModifyListener(new ModifyListener() {
					public void modifyText(ModifyEvent arg0) {
						if (listener != null) {
							listener.setLanguage(listener.languageAsInt(languageSelector.getText().toLowerCase()));
							fillForm();
						}
					}
				});
				GridData gd_combo = new GridData(SWT.LEFT, SWT.CENTER, true, false, 1, 1);
				gd_combo.minimumWidth = 100;
				languageSelector.setLayoutData(gd_combo);
				languageSelector.select(0);
		if (type == Editor.MONITOR) {
			languageSelector.setItems(new String[] { "Java", "Javascript", "VBScript", "PerlScript" });
		}
		else {
			languageSelector.setItems(new String[] { "Shell", "Java", "Javascript", "VBScript", "PerlScript" });
		}

	}

	private void setFont(FontData f, RGB foreGround) {
		tSource.setFont(new Font(this.getDisplay(), f));
		tSource.setForeground(new Color(this.getDisplay(), foreGround));
	}

	private void fillForm() {
		init = true;
		int language = listener.getLanguage();
		if (language < 0){
			language = 0;
		}
		cboPrefunction.removeAll();
		if (type == Editor.MONITOR) {
			txtName.setText(listener.getName());
			spinner.setSelection((listener.getOrdering().length() == 0 ? 0 : Integer.parseInt(listener.getOrdering())));
			//bShell.setVisible(false);
		}

		setEnabled(true);

		cboFavorite.setEnabled(true);
		butFavorite.setEnabled(true);
		languageSelector.selectLanguageItem(language);
		tClasspath.setEnabled(false);
		if (languageSelector.isJava()) {
			tClass.setEnabled(true);
			tClass.setFocus();
			tClasspath.setEnabled(true);
			tClasspath.setText(listener.getClasspath());

			if (!tClass.getText().equals("") && listener.getJavaClass().equals(""))
				listener.setJavaClass(tClass.getText());
			tClass.setText(listener.getJavaClass());

		}
		else {
			tSource.setFocus();
		}

		if (language != ScriptListener.NONE) {
			if (listener.getSource().length() > 0)
				tSource.setText(listener.getSource());
			else
				if (tSource.getText().length() > 0)
					listener.setSource(tSource.getText());
		}

		if (language != ScriptListener.NONE) {
			listener.fillTable(tableIncludes);
		}

		String lan = "";
		if (listener.getLanguageAsString(listener.getLanguage()) != "shell") {
			if (type == Editor.MONITOR) {
				lan = "spooler_task_before;spooler_task_after;spooler_process_before;spooler_process_after";
			}
			else {
				lan = "spooler_init;spooler_open;spooler_process;spooler_close;spooler_exit;spooler_on_error;spooler_on_success";
			}
			cboPrefunction.setItems(lan.split(";"));
		}
		init = false;
	}

	public void setEnabled(boolean enabled) {
		tClass.setEnabled(false);
		tableIncludes.setEnabled(enabled);
		bRemove.setEnabled(false);
		tInclude.setEnabled(enabled);
		butIsLifeFile.setEnabled(enabled);
		tableIncludes.deselectAll();
		bAdd.setEnabled(false);
		tSource.setEnabled(enabled);
		lblPrefunction.setEnabled(enabled && type != Editor.SCRIPT);
		cboPrefunction.setEnabled(enabled && type != Editor.SCRIPT);
		button.setEnabled(enabled);

	}

	public void setFullEnabled(boolean enabled) {

		setEnabled(enabled);
		fillForm();

	}

	public void setLanguage(int language) {
		listener.setLanguage(language);
		fillForm();
	}

	private void applyFile() {
		listener.addInclude(tableIncludes, tInclude.getText(), butIsLifeFile.getSelection());
		tInclude.setText("");
		tInclude.setEnabled(false);
		tInclude.setFocus();
		tableIncludes.deselectAll();
	}

	public void setToolTipText() {
		tClass.setToolTipText(Messages.getTooltip("script.class"));
		tInclude.setToolTipText(Messages.getTooltip("script.include.file_entry"));
		tableIncludes.setToolTipText(Messages.getTooltip("script.include.file_list"));
		bRemove.setToolTipText(Messages.getTooltip("script.include.btn_remove"));
		bAdd.setToolTipText(Messages.getTooltip("script.include.btn_add"));
		tSource.setToolTipText(Messages.getTooltip("script.source_entry"));

		if (txtName != null)
			txtName.setToolTipText(Messages.getTooltip("script.name"));
		if (spinner != null)
			spinner.setToolTipText(Messages.getTooltip("script.ordering"));

		if (butIsLifeFile != null)
			butIsLifeFile.setToolTipText(Messages.getTooltip("is_live_file"));

		if (cboPrefunction != null)
			cboPrefunction.setToolTipText("-- Templates --");
	}

	private String getPrefix() {
		if (favorites != null && cboFavorite.getText().length() > 0 && favorites.get(cboFavorite.getText()) != null)
			return "monitor_favorite_" + favorites.get(cboFavorite.getText()) + "_";
		if (listener.getLanguage() == 0)
			return "";
		return "monitor_favorite_" + listener.getLanguage(listener.getLanguage()) + "_";
	}

	private String[] normalized(String[] str) {
		String[] retVal = new String[] { "" };
		try {
			favorites = new HashMap();
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
					if (name == null || lan == null)
						System.out.println(name);
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
				//tu nichts
			}
			return retVal;
		}
	}

	private String getFavoriteValue() {
		if (listener.isJava()) {
			return tClass.getText();
		}
		else {
			return listener.getIncludesAsString();
		}
	}

	public int getSelectionLanguageButton() {

		return listener.getLanguage();

	}

} // @jve:decl-index=0:visual-constraint="10,10"
