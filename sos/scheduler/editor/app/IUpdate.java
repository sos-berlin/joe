package sos.scheduler.editor.app;

public interface IUpdate {

	public void updateJobs();
	
	public void updateJob();
	public void expandJob(String job);
	public void updateJob(String s);
	public void updateCommand(String s);
	public void updateCommands();
	public void updateOrder(String s);
	public void updateOrders();
	
	public void updateDays(int type);
	
	public void dataChanged();
}
