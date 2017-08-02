package sos.scheduler.editor.classes;

import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;

/** @author Uwe Risse */
public class LanguageSelector extends Combo {

    public LanguageSelector(Composite pobjComposite, int arg1) {
        super(pobjComposite, arg1);
    }

    public boolean isScriptLanguage() {
        return !this.isShell() && !this.isJava();
    }

    public void selectLanguageItem(int languageId) {
        this.select(languageId);
    }

    public boolean isJava() {
        return "java".equalsIgnoreCase(this.getText());
    }

    public boolean isDotNet() {
        return "dotnet".equalsIgnoreCase(this.getText());
    }

    public boolean isShell() {
        return "shell".equalsIgnoreCase(this.getText());
    }

    protected void checkSubclass() {
        // Disable the check that prevents subclassing of SWT components
    }

}