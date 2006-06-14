package sos.scheduler.editor.app;

public interface IUpdate {

	public void updateJobs();
	
	public void updateJob();
	public void updateJob(String s);
	public void updateCommand(String s);
	public void updateCommands();
	
	public void updateDays(int type);
	
	public void dataChanged();
}
