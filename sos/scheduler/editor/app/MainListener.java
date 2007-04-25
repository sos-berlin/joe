package sos.scheduler.editor.app;

import java.io.File;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyleRange;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.program.Program;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;

public class MainListener {
    MainWindow _gui       = null;

    IContainer _container = null;

 
    public MainListener(MainWindow gui, IContainer container) {
        _gui = gui;
        _container = container;
    }


    public void openHelp(String helpKey) {
        String lang = Options.getLanguage();
        String url = helpKey;

        try {

            url = new File(url).toURL().toString();

            Program prog = Program.findProgram("html");
            if (prog != null)
                prog.execute(url);
            else {
                Runtime.getRuntime().exec(Options.getBrowserExec(url, lang));
            }
        } catch (Exception e) {
            e.printStackTrace();
            MainWindow.message(Messages.getString("MainListener.cannot_open_help", new String[] { url, lang,
                    e.getMessage() }), SWT.ICON_ERROR | SWT.OK);
        }
    }


    public void showAbout() {
        TextDialog dialog = new TextDialog(MainWindow.getSShell());
        dialog.setText("About Job Scheduler Editor");
        String message = Messages.getString("MainListener.aboutText", Options.getVersion() + "\nSchema-Version:"
                + Options.getSchemaVersion() + "\n");
        dialog.setContent(message, SWT.CENTER);
        dialog.getStyledText().setEnabled(false);
        StyleRange bold = new StyleRange();
        bold.start = 0;
        bold.length = message.indexOf("\n");
        bold.fontStyle = SWT.BOLD;
        dialog.getStyledText().setStyleRange(bold);
        dialog.open();
    }


    public void setLanguages(Menu menu) {
        boolean found = false;
        MenuItem defaultItem = null;
        HashMap langs = Languages.getLanguages();
        Iterator it = langs.keySet().iterator();

        while (it.hasNext()) {
            String key = (String) it.next();
            String val = (String) langs.get(key);

            MenuItem item = new MenuItem(menu, SWT.RADIO);
            item.setText(key);
            item.setData(val);
            if (Options.getLanguage().equals(val)) {
                found = true;
                item.setSelection(true);
            }

            if (Options.getDefault("editor.language").equals(val))
                defaultItem = item;

            item.addSelectionListener(new SelectionListener() {
                public void widgetSelected(SelectionEvent e) {
                    MenuItem item = (MenuItem) e.widget;
                    if (item.getSelection()) {
                        String lang = (String) item.getData();
                        Options.setLanguage(lang);
                        loadMessages();
                    }
                }


                public void widgetDefaultSelected(SelectionEvent e) {

                }
            });
        }

        if (!found) {
            String def = Options.getDefault("editor.language");
            MainWindow.message("The language " + Options.getLanguage() + " was not found - setting to " + def,
                    SWT.ICON_WARNING | SWT.OK);
            Options.setLanguage(def);
            if (defaultItem != null)
                defaultItem.setSelection(true);
        }
    }


    public void loadMessages() {
        if (!Messages.setResource(new Locale(Options.getLanguage()))) {
            MainWindow.message("The resource bundle " + Messages.getBundle() + " for the language "
                    + Options.getLanguage() + " was not found!", SWT.ICON_ERROR | SWT.OK);
        }
        _container.updateLanguages();
    }

    public void resetInfoDialog() {
    	Options.setShowWizardInfo(true);	
    }

    public void loadOptions() {
        String msg = Options.loadOptions(getClass());
        if (msg != null)
            MainWindow.message("No options file " + Options.getDefaultOptionFilename() + " found - using defaults!\n"
                    + msg, SWT.ICON_ERROR | SWT.OK);
    }


    public void saveOptions() {
        String msg = Options.saveProperties();
        if (msg != null)
            MainWindow.message("Options cannot be saved!\n" + msg, SWT.ICON_ERROR | SWT.OK);
    }

}
