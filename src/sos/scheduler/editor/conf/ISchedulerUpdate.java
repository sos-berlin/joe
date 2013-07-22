package sos.scheduler.editor.conf;

import org.eclipse.swt.widgets.TreeItem;

import sos.scheduler.editor.app.IDataChanged;

public interface ISchedulerUpdate extends IDataChanged {


	public void updateJobs();

  	public void updateJob();

  	public void updateJob(org.jdom.Element elem);

  	public void expandItem(String name);

    public void updateJob(String s);

    public void updateCommands();

    public void updateExitCodesCommand();

    public void updateOrder(String s);

    public void updateOrders();

    public void updateDays(int type);

    public void updateDays(int type, String name);

    public void updateSpecificWeekdays();

    public void updateJobChains();

    public void updateJobChain(String newName, String oldName);

    public void updateSchedules();

    public void updateScripts();

    public void updateTreeItem(String s);

    public void updateTree(String which);

    public void updateFont();

    public void updateFont(TreeItem item);

    public void updateWebServices();

    public void updateRunTime();

}
