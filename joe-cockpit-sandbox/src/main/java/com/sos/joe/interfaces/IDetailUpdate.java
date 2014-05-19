package com.sos.joe.interfaces;


public interface IDetailUpdate extends IDataChanged {
	
	public void updateState(String state);
	
	public void updateJobChainname(String name);
	
	public void updateNote();
	
	public void updateParamNote();
	
	public void updateParam();
  
}
