package com.sos.joe.globals.interfaces;
import java.util.Collection;

public interface IEditor {
	public boolean hasChanges();

	public boolean close();

	public boolean saveAs();

	public boolean save();

	public boolean open(Collection files);

	public void openBlank();

	public boolean applyChanges();

	public void updateLanguage();

	public String getHelpKey();

	public String getFilename();
}
