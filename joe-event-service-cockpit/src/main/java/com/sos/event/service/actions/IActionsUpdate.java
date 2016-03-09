package com.sos.event.service.actions;

import com.sos.joe.globals.interfaces.IDataChanged;

public interface IActionsUpdate extends IDataChanged {

    public void updateActions();

    public void updateAction(String name);

    public void updateCommands();

    public void updateCommand();

    public void updateTreeItem(String s);

    public void updateTree(String which);

}
