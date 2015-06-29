package com.sos.joe.globals.messages;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Cursor;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Group;

import com.sos.dialog.classes.SOSComposite;
import com.sos.dialog.swtdesigner.SWTResourceManager;
 
import com.sos.i18n.annotation.I18NMsg;

public class SOSJOEMessageCodes extends SOSComposite {

    @I18NMsg
    public static final SOSMsgJOE   JOE_B_Remove                            = new SOSMsgJOE("JOE_B_Remove");
    @I18NMsg
    public static final SOSMsgJOE   JOE_B_Add                               = new SOSMsgJOE("JOE_B_Add");
    @I18NMsg
    public static final SOSMsgJOE   JOE_B_Ok                                = new SOSMsgJOE("JOE_B_Ok");
    @I18NMsg
    public static final SOSMsgJOE   JOE_B_Apply                             = new SOSMsgJOE("JOE_B_Apply");
    @I18NMsg
    public static final SOSMsgJOE   JOE_B_New                               = new SOSMsgJOE("JOE_B_New");
    
    //Return Codes
    @I18NMsg
    public static final SOSMsgJOE   JOE_L_ReturnCodesForm_Add_Order         = new SOSMsgJOE("JOE_L_ReturnCodesForm_Add_Order");
    @I18NMsg
    public static final SOSMsgJOE   JOE_B_Add_Next_State                    = new SOSMsgJOE("JOE_B_Add_Next_State");
    @I18NMsg
    public static final SOSMsgJOE   JOE_B_ReturnCodesForm_Add_Next_State    = new SOSMsgJOE("JOE_B_ReturnCodesForm_Add_Next_State");
    @I18NMsg
    public static final SOSMsgJOE   JOE_B_ReturnCodesForm_Add_Order         = new SOSMsgJOE("JOE_B_ReturnCodesForm_Add_Order");
    @I18NMsg
    public static final SOSMsgJOE   JOE_B_ReturnCodesForm_Add_Param         = new SOSMsgJOE("JOE_B_ReturnCodesForm_Add_Param");
    @I18NMsg
    public static final SOSMsgJOE   JOE_B_ReturnCodesForm_New_Param         = new SOSMsgJOE("JOE_B_ReturnCodesForm_New_Param");
    @I18NMsg
    public static final SOSMsgJOE   JOE_B_ReturnCodesForm_Remove_Param      = new SOSMsgJOE("JOE_B_ReturnCodesForm_Remove_Param");
    
	// BaseForm
	@I18NMsg
	public static final SOSMsgJOE	JOE_G_BaseForm_BaseFiles							= new SOSMsgJOE("JOE_G_BaseForm_BaseFiles");
	@I18NMsg
	public static final SOSMsgJOE	JOE_L_BaseForm_BaseFile								= new SOSMsgJOE("JOE_L_BaseForm_BaseFile");
	@I18NMsg
	public static final SOSMsgJOE	JOE_T_BaseForm_BaseFile								= new SOSMsgJOE("JOE_T_BaseForm_BaseFile");
	@I18NMsg
	public static final SOSMsgJOE	JOE_B_BaseForm_Apply								= new SOSMsgJOE("JOE_B_BaseForm_Apply");
	@I18NMsg
	public static final SOSMsgJOE	JOE_L_BaseForm_BaseComment							= new SOSMsgJOE("JOE_L_BaseForm_BaseComment");
	@I18NMsg
	public static final SOSMsgJOE	JOE_T_BaseForm_BaseComment							= new SOSMsgJOE("JOE_T_BaseForm_BaseComment");
	@I18NMsg
	public static final SOSMsgJOE	JOE_Cmp_BaseForm_CommentOpen						= new SOSMsgJOE("JOE_Cmp_BaseForm_CommentOpen");
	@I18NMsg
	public static final SOSMsgJOE	JOE_B_BaseForm_Comment								= new SOSMsgJOE("JOE_B_BaseForm_Comment");
	@I18NMsg
	public static final SOSMsgJOE	JOE_B_BaseForm_OpenFileDialog						= new SOSMsgJOE("JOE_B_BaseForm_OpenFileDialog");
	@I18NMsg
	public static final SOSMsgJOE	JOE_Sep_BaseForm_S1									= new SOSMsgJOE("JOE_Sep_BaseForm_S1");
	@I18NMsg
	public static final SOSMsgJOE	JOE_B_BaseForm_NewBaseFile							= new SOSMsgJOE("JOE_B_BaseForm_NewBaseFile");
	@I18NMsg
	public static final SOSMsgJOE	JOE_B_BaseForm_OpenBaseFile							= new SOSMsgJOE("JOE_B_BaseForm_OpenBaseFile");
	@I18NMsg
	public static final SOSMsgJOE	JOE_Sep_BaseForm_S2									= new SOSMsgJOE("JOE_Sep_BaseForm_S2");
	@I18NMsg
	public static final SOSMsgJOE	JOE_B_BaseForm_RemoveBaseFile						= new SOSMsgJOE("JOE_B_BaseForm_RemoveBaseFile");
	@I18NMsg
	public static final SOSMsgJOE	JOE_Tbl_BaseForm_BaseTable							= new SOSMsgJOE("JOE_Tbl_BaseForm_BaseTable");
	@I18NMsg
	public static final SOSMsgJOE	JOE_TCl_BaseForm_BaseFiles							= new SOSMsgJOE("JOE_TCl_BaseForm_BaseFiles");
	@I18NMsg
	public static final SOSMsgJOE	JOE_TCl_BaseForm_BaseComment						= new SOSMsgJOE("JOE_TCl_BaseForm_BaseComment");
	@I18NMsg
	public static final SOSMsgJOE	JOE_FD_BaseForm_OpenBaseFile						= new SOSMsgJOE("JOE_FD_BaseForm_OpenBaseFile");

	// JobMainComposite
	@I18NMsg
	public static final SOSMsgJOE	JOE_G_JobMainComposite_MainOptions					= new SOSMsgJOE("JOE_G_JobMainComposite_MainOptions");
	@I18NMsg
	public static final SOSMsgJOE	JOE_L_JobMainComposite_JobName						= new SOSMsgJOE("JOE_L_JobMainComposite_JobName");
	@I18NMsg
	public static final SOSMsgJOE	JOE_T_JobMainComposite_JobName						= new SOSMsgJOE("JOE_T_JobMainComposite_JobName");
	@I18NMsg
	public static final SOSMsgJOE	JOE_L_JobMainComposite_JobTitle						= new SOSMsgJOE("JOE_L_JobMainComposite_JobTitle");
	@I18NMsg
	public static final SOSMsgJOE	JOE_T_JobMainComposite_JobTitle						= new SOSMsgJOE("JOE_T_JobMainComposite_JobTitle");
	@I18NMsg
	public static final SOSMsgJOE	JOE_L_JobMainComposite_ProcessClass					= new SOSMsgJOE("JOE_L_JobMainComposite_ProcessClass");
	@I18NMsg
	public static final SOSMsgJOE	JOE_B_JobMainComposite_ShowProcessClass				= new SOSMsgJOE("JOE_B_JobMainComposite_ShowProcessClass");
	@I18NMsg
	public static final SOSMsgJOE	JOE_Cbo_JobMainComposite_ProcessClass				= new SOSMsgJOE("JOE_Cbo_JobMainComposite_ProcessClass");
	@I18NMsg
	public static final SOSMsgJOE	JOE_B_JobMainComposite_BrowseProcessClass			= new SOSMsgJOE("JOE_B_JobMainComposite_BrowseProcessClass");

	// PreProcessingComposite
	@I18NMsg
	public static final SOSMsgJOE	JOE_G_PreProcessingComposite_Script					= new SOSMsgJOE("JOE_G_PreProcessingComposite_Script");
	@I18NMsg
	public static final SOSMsgJOE	JOE_Cmp_PreProcessingComposite_NameOrdering			= new SOSMsgJOE("JOE_Cmp_PreProcessingComposite_NameOrdering");
	@I18NMsg
	public static final SOSMsgJOE	JOE_L_Name											= new SOSMsgJOE("JOE_L_Name");
	@I18NMsg
	public static final SOSMsgJOE	JOE_T_PreProcessingComposite_PreProcessingName		= new SOSMsgJOE("JOE_T_PreProcessingComposite_PreProcessingName");
	@I18NMsg
	public static final SOSMsgJOE	JOE_L_PreProcessingComposite_Ordering				= new SOSMsgJOE("JOE_L_PreProcessingComposite_Ordering");
	@I18NMsg
	public static final SOSMsgJOE	JOE_Sp_PreProcessingComposite_Ordering				= new SOSMsgJOE("JOE_Sp_PreProcessingComposite_Ordering");
	@I18NMsg
	public static final SOSMsgJOE	JOE_B_PreProcessingComposite_Favourites				= new SOSMsgJOE("JOE_B_PreProcessingComposite_Favourites");
	@I18NMsg
	public static final SOSMsgJOE	JOE_Cbo_PreProcessingComposite_Favourites			= new SOSMsgJOE("JOE_Cbo_PreProcessingComposite_Favourites");

	// ProcessClassesForm
	@I18NMsg
    public static final SOSMsgJOE   JOE_B_ProcessClassesForm_NewRemotScheduler          = new SOSMsgJOE("JOE_B_ProcessClassesForm_NewRemotScheduler");
    @I18NMsg
    public static final SOSMsgJOE   JOE_B_ProcessClassesForm_RemoveRemotScheduler       = new SOSMsgJOE("JOE_B_ProcessClassesForm_RemoveRemotScheduler");
	@I18NMsg
	public static final SOSMsgJOE	JOE_G_ProcessClassesForm_ProcessClasses				= new SOSMsgJOE("JOE_G_ProcessClassesForm_ProcessClasses");
	@I18NMsg
	public static final SOSMsgJOE	JOE_L_ProcessClassesForm_ProcessClass				= new SOSMsgJOE("JOE_L_ProcessClassesForm_ProcessClass");
	@I18NMsg
	public static final SOSMsgJOE	JOE_T_ProcessClassesForm_ProcessClass				= new SOSMsgJOE("JOE_T_ProcessClassesForm_ProcessClass");
	@I18NMsg
	public static final SOSMsgJOE	JOE_B_ProcessClassesForm_Apply						= new SOSMsgJOE("JOE_B_ProcessClassesForm_Apply");
	@I18NMsg
	public static final SOSMsgJOE	JOE_L_ProcessClassesForm_MaxProcesses				= new SOSMsgJOE("JOE_L_ProcessClassesForm_MaxProcesses");
	@I18NMsg
	public static final SOSMsgJOE	JOE_T_ProcessClassesForm_MaxProcesses				= new SOSMsgJOE("JOE_T_ProcessClassesForm_MaxProcesses");
	@I18NMsg
	public static final SOSMsgJOE	JOE_L_ProcessClassesForm_remoteExecution			= new SOSMsgJOE("JOE_L_ProcessClassesForm_remoteExecution");
	@I18NMsg
	public static final SOSMsgJOE	JOE_T_ProcessClassesForm_remoteExecution			= new SOSMsgJOE("JOE_T_ProcessClassesForm_remoteExecution");
	@I18NMsg
	public static final SOSMsgJOE	JOE_L_ProcessClassesForm_Port						= new SOSMsgJOE("JOE_L_ProcessClassesForm_Port");
	@I18NMsg
	public static final SOSMsgJOE	JOE_T_ProcessClassesForm_Port						= new SOSMsgJOE("JOE_T_ProcessClassesForm_Port");
	// @I18NMsg
	// public static final SOSMsgJOE JOE_Sep_ProcessClassesForm_S1 = new SOSMsgJOE("JOE_Sep_ProcessClassesForm_S1");
	@I18NMsg
	public static final SOSMsgJOE	JOE_B_ProcessClassesForm_NewProcessClass			= new SOSMsgJOE("JOE_B_ProcessClassesForm_NewProcessClass");
	// @I18NMsg
	// public static final SOSMsgJOE JOE_Sep_ProcessClassesForm_S2 = new SOSMsgJOE("JOE_Sep_ProcessClassesForm_S2");
	@I18NMsg
	public static final SOSMsgJOE	JOE_B_ProcessClassesForm_RemoveProcessClass			= new SOSMsgJOE("JOE_B_ProcessClassesForm_RemoveProcessClass");
	@I18NMsg
	public static final SOSMsgJOE	JOE_Tbl_ProcessClassesForm_ProcessClasses			= new SOSMsgJOE("JOE_Tbl_ProcessClassesForm_ProcessClasses");
	@I18NMsg
	public static final SOSMsgJOE	JOE_TCl_ProcessClassesForm_ProcessClass				= new SOSMsgJOE("JOE_TCl_ProcessClassesForm_ProcessClass");
	@I18NMsg
	public static final SOSMsgJOE	JOE_TCl_ProcessClassesForm_MaxProcesses				= new SOSMsgJOE("JOE_TCl_ProcessClassesForm_MaxProcesses");
	@I18NMsg
	public static final SOSMsgJOE	JOE_TCl_ProcessClassesForm_RemoteExecution			= new SOSMsgJOE("JOE_TCl_ProcessClassesForm_RemoteExecution");
	@I18NMsg
	public static final SOSMsgJOE	JOE_M_ProcessClassesForm_MissingPort				= new SOSMsgJOE("JOE_M_ProcessClassesForm_MissingPort");
	@I18NMsg
	public static final SOSMsgJOE	JOE_M_ProcessClassesForm_MissingHost				= new SOSMsgJOE("JOE_M_ProcessClassesForm_MissingHost");

	// ClusterForm
	@I18NMsg
	public static final SOSMsgJOE	JOE_G_ClusterForm_Cluster							= new SOSMsgJOE("JOE_G_ClusterForm_Cluster");
	@I18NMsg
	public static final SOSMsgJOE	JOE_L_ClusterForm_HeartbeatTimeout					= new SOSMsgJOE("JOE_L_ClusterForm_HeartbeatTimeout");
	@I18NMsg
	public static final SOSMsgJOE	JOE_L_ClusterForm_HeartbeatOwnTimeout				= new SOSMsgJOE("JOE_L_ClusterForm_HeartbeatOwnTimeout");
	@I18NMsg
	public static final SOSMsgJOE	JOE_T_ClusterForm_HeartbeatOwnTimeout				= new SOSMsgJOE("JOE_T_ClusterForm_HeartbeatOwnTimeout");
	@I18NMsg
	public static final SOSMsgJOE	JOE_L_ClusterForm_HeartbeatWarnTimeout				= new SOSMsgJOE("JOE_L_ClusterForm_HeartbeatWarnTimeout");
	@I18NMsg
	public static final SOSMsgJOE	JOE_T_ClusterForm_HeartbeatWarnTimeout				= new SOSMsgJOE("JOE_T_ClusterForm_HeartbeatWarnTimeout");
	@I18NMsg
	public static final SOSMsgJOE	JOE_T_ClusterForm_HeartbeatTimeout					= new SOSMsgJOE("JOE_T_ClusterForm_HeartbeatTimeout");

	// CommandsForm
	@I18NMsg
	public static final SOSMsgJOE	JOE_G_CommandsForm_Commands							= new SOSMsgJOE("JOE_G_CommandsForm_Commands");
	@I18NMsg
	public static final SOSMsgJOE	JOE_T_CommandsForm_Commands							= new SOSMsgJOE("JOE_T_CommandsForm_Commands");
	@I18NMsg
	public static final SOSMsgJOE	JOE_B_CommandsForm_Apply							= new SOSMsgJOE("JOE_B_CommandsForm_Apply");

	// ConfigForm
	@I18NMsg
	public static final SOSMsgJOE	JOE_G_ConfigForm_Config								= new SOSMsgJOE("JOE_G_ConfigForm_Config");
	@I18NMsg
	public static final SOSMsgJOE	JOE_G_ConfigForm_Group1								= new SOSMsgJOE("JOE_G_ConfigForm_Group1");
	@I18NMsg
	public static final SOSMsgJOE	JOE_L_ConfigForm_SchedulerID						= new SOSMsgJOE("JOE_L_ConfigForm_SchedulerID");
	@I18NMsg
	public static final SOSMsgJOE	JOE_T_ConfigForm_SchedulerID						= new SOSMsgJOE("JOE_T_ConfigForm_SchedulerID");
	@I18NMsg
	public static final SOSMsgJOE	JOE_L_ConfigForm_Params								= new SOSMsgJOE("JOE_L_ConfigForm_Params");
	@I18NMsg
	public static final SOSMsgJOE	JOE_T_ConfigForm_Params								= new SOSMsgJOE("JOE_T_ConfigForm_Params");
	@I18NMsg
	public static final SOSMsgJOE	JOE_L_ConfigForm_IncludePath						= new SOSMsgJOE("JOE_L_ConfigForm_IncludePath");
	@I18NMsg
	public static final SOSMsgJOE	JOE_T_ConfigForm_IncludePath						= new SOSMsgJOE("JOE_T_ConfigForm_IncludePath");
	@I18NMsg
	public static final SOSMsgJOE	JOE_L_ConfigForm_IPAddress							= new SOSMsgJOE("JOE_L_ConfigForm_IPAddress");
	@I18NMsg
	public static final SOSMsgJOE	JOE_T_ConfigForm_IPAddress							= new SOSMsgJOE("JOE_T_ConfigForm_IPAddress");
	@I18NMsg
	public static final SOSMsgJOE	JOE_L_ConfigForm_LogDir								= new SOSMsgJOE("JOE_L_ConfigForm_LogDir");
	@I18NMsg
	public static final SOSMsgJOE	JOE_T_ConfigForm_LogDir								= new SOSMsgJOE("JOE_T_ConfigForm_LogDir");
	@I18NMsg
	public static final SOSMsgJOE	JOE_L_ConfigForm_MailXSLT							= new SOSMsgJOE("JOE_L_ConfigForm_MailXSLT");
	@I18NMsg
	public static final SOSMsgJOE	JOE_T_ConfigForm_MailXSLT							= new SOSMsgJOE("JOE_T_ConfigForm_MailXSLT");
	@I18NMsg
	public static final SOSMsgJOE	JOE_L_ConfigForm_CentralConfigDir					= new SOSMsgJOE("JOE_L_ConfigForm_CentralConfigDir");
	@I18NMsg
	public static final SOSMsgJOE	JOE_T_ConfigForm_CentralConfigDir					= new SOSMsgJOE("JOE_T_ConfigForm_CentralConfigDir");
	@I18NMsg
	public static final SOSMsgJOE	JOE_G_ConfigForm_Event								= new SOSMsgJOE("JOE_G_ConfigForm_Event");
	@I18NMsg
	public static final SOSMsgJOE	JOE_L_ConfigForm_ConfigAddEvent						= new SOSMsgJOE("JOE_L_ConfigForm_ConfigAddEvent");
	@I18NMsg
	public static final SOSMsgJOE	JOE_Cbo_ConfigForm_ConfigAddEvent					= new SOSMsgJOE("JOE_Cbo_ConfigForm_ConfigAddEvent");
	@I18NMsg
	public static final SOSMsgJOE	JOE_B_ConfigForm_Browse1							= new SOSMsgJOE("JOE_B_ConfigForm_Browse1");
	@I18NMsg
	public static final SOSMsgJOE	JOE_B_ConfigForm_Browse2							= new SOSMsgJOE("JOE_B_ConfigForm_Browse2");
	@I18NMsg
	public static final SOSMsgJOE	JOE_B_ConfigForm_Browse3							= new SOSMsgJOE("JOE_B_ConfigForm_Browse3");
	@I18NMsg
	public static final SOSMsgJOE	JOE_L_ConfigForm_ConfigModifyEvent					= new SOSMsgJOE("JOE_L_ConfigForm_ConfigModifyEvent");
	@I18NMsg
	public static final SOSMsgJOE	JOE_Cbo_ConfigForm_ConfigModifyEvent				= new SOSMsgJOE("JOE_Cbo_ConfigForm_ConfigModifyEvent");
	@I18NMsg
	public static final SOSMsgJOE	JOE_L_ConfigForm_ConfigDeleteEvent					= new SOSMsgJOE("JOE_L_ConfigForm_ConfigDeleteEvent");
	@I18NMsg
	public static final SOSMsgJOE	JOE_Cbo_ConfigForm_ConfigDeleteEvent				= new SOSMsgJOE("JOE_Cbo_ConfigForm_ConfigDeleteEvent");
	@I18NMsg
	public static final SOSMsgJOE	JOE_Cmp_ConfigForm_CmpPort							= new SOSMsgJOE("JOE_Cmp_ConfigForm_CmpPort");
	@I18NMsg
	public static final SOSMsgJOE	JOE_G_ConfigForm_Ports								= new SOSMsgJOE("JOE_G_ConfigForm_Ports");
	@I18NMsg
	public static final SOSMsgJOE	JOE_B_ConfigForm_SamePortsCheckBtn					= new SOSMsgJOE("JOE_B_ConfigForm_SamePortsCheckBtn");
	@I18NMsg
	public static final SOSMsgJOE	JOE_L_ConfigForm_SchedulerPort						= new SOSMsgJOE("JOE_L_ConfigForm_SchedulerPort");
	@I18NMsg
	public static final SOSMsgJOE	JOE_T_ConfigForm_SamePort							= new SOSMsgJOE("JOE_T_ConfigForm_SamePort");
	@I18NMsg
	public static final SOSMsgJOE	JOE_L_ConfigForm_TCP								= new SOSMsgJOE("JOE_L_ConfigForm_TCP");
	@I18NMsg
	public static final SOSMsgJOE	JOE_T_ConfigForm_TCP								= new SOSMsgJOE("JOE_T_ConfigForm_TCP");
	@I18NMsg
	public static final SOSMsgJOE	JOE_L_ConfigForm_UDP								= new SOSMsgJOE("JOE_L_ConfigForm_UDP");
	@I18NMsg
	public static final SOSMsgJOE	JOE_T_ConfigForm_UDP								= new SOSMsgJOE("JOE_T_ConfigForm_UDP");
	@I18NMsg
	public static final SOSMsgJOE	JOE_G_ConfigForm_Supervisor							= new SOSMsgJOE("JOE_G_ConfigForm_Supervisor");
	@I18NMsg
	public static final SOSMsgJOE	JOE_L_ConfigForm_Host								= new SOSMsgJOE("JOE_L_ConfigForm_Host");
	@I18NMsg
	public static final SOSMsgJOE	JOE_T_ConfigForm_Host								= new SOSMsgJOE("JOE_T_ConfigForm_Host");
	@I18NMsg
	public static final SOSMsgJOE	JOE_L_ConfigForm_SupervisorPort						= new SOSMsgJOE("JOE_L_ConfigForm_SupervisorPort");
	@I18NMsg
	public static final SOSMsgJOE	JOE_T_ConfigForm_SupervisorPort						= new SOSMsgJOE("JOE_T_ConfigForm_SupervisorPort");
	@I18NMsg
	public static final SOSMsgJOE	JOE_G_ConfigForm_JavaOptions						= new SOSMsgJOE("JOE_G_ConfigForm_JavaOptions");
	@I18NMsg
	public static final SOSMsgJOE	JOE_L_ConfigForm_ClassPath							= new SOSMsgJOE("JOE_L_ConfigForm_ClassPath");
	@I18NMsg
	public static final SOSMsgJOE	JOE_T_ConfigForm_ClassPath							= new SOSMsgJOE("JOE_T_ConfigForm_ClassPath");
	@I18NMsg
	public static final SOSMsgJOE	JOE_L_ConfigForm_Options							= new SOSMsgJOE("JOE_L_ConfigForm_Options");
	@I18NMsg
	public static final SOSMsgJOE	JOE_T_ConfigForm_Options							= new SOSMsgJOE("JOE_T_ConfigForm_Options");
	@I18NMsg
	public static final SOSMsgJOE	JOE_G_ConfigForm_Comment							= new SOSMsgJOE("JOE_G_ConfigForm_Comment");
	@I18NMsg
	public static final SOSMsgJOE	JOE_B_ConfigForm_Comment							= new SOSMsgJOE("JOE_B_ConfigForm_Comment");
	@I18NMsg
	public static final SOSMsgJOE	JOE_T_ConfigForm_Comment							= new SOSMsgJOE("JOE_T_ConfigForm_Comment");

	// DateForm
	@I18NMsg
	public static final SOSMsgJOE	JOE_G_DateForm_Holiday								= new SOSMsgJOE("JOE_G_DateForm_Holiday");
	@I18NMsg
	public static final SOSMsgJOE	JOE_G_DateForm_Specific								= new SOSMsgJOE("JOE_G_DateForm_Specific");
	@I18NMsg
	public static final SOSMsgJOE	JOE_G_DateForm_Dates								= new SOSMsgJOE("JOE_G_DateForm_Dates");
	@I18NMsg
	public static final SOSMsgJOE	JOE_L_DateForm_Year									= new SOSMsgJOE("JOE_L_DateForm_Year");
	@I18NMsg
	public static final SOSMsgJOE	JOE_Sp_DateForm_Year								= new SOSMsgJOE("JOE_Sp_DateForm_Year");
	@I18NMsg
	public static final SOSMsgJOE	JOE_L_DateForm_Month								= new SOSMsgJOE("JOE_L_DateForm_Month");
	@I18NMsg
	public static final SOSMsgJOE	JOE_Sp_DateForm_Month								= new SOSMsgJOE("JOE_Sp_DateForm_Month");
	@I18NMsg
	public static final SOSMsgJOE	JOE_L_DateForm_Day									= new SOSMsgJOE("JOE_L_DateForm_Day");
	@I18NMsg
	public static final SOSMsgJOE	JOE_Sp_DateForm_Day									= new SOSMsgJOE("JOE_Sp_DateForm_Day");
	@I18NMsg
	public static final SOSMsgJOE	JOE_B_DateForm_AddDate								= new SOSMsgJOE("JOE_B_DateForm_AddDate");
	@I18NMsg
	public static final SOSMsgJOE	JOE_M_0015											= new SOSMsgJOE("JOE_M_0015");
	@I18NMsg
	public static final SOSMsgJOE	JOE_M_0014											= new SOSMsgJOE("JOE_M_0014");
	@I18NMsg
	public static final SOSMsgJOE	JOE_Sep_DateForm_S1									= new SOSMsgJOE("JOE_Sep_DateForm_S1");
	@I18NMsg
	public static final SOSMsgJOE	JOE_Lst_DateForm_DatesList							= new SOSMsgJOE("JOE_Lst_DateForm_DatesList");
	@I18NMsg
	public static final SOSMsgJOE	JOE_B_DateForm_RemoveDate							= new SOSMsgJOE("JOE_B_DateForm_RemoveDate");
	@I18NMsg
	public static final SOSMsgJOE	JOE_G_DateForm_IncludeFiles							= new SOSMsgJOE("JOE_G_DateForm_IncludeFiles");
	@I18NMsg
	public static final SOSMsgJOE	JOE_B_DateForm_IsLifeFile							= new SOSMsgJOE("JOE_B_DateForm_IsLifeFile");
	@I18NMsg
	public static final SOSMsgJOE	JOE_Cbo_DateForm_Include							= new SOSMsgJOE("JOE_Cbo_DateForm_Include");
	@I18NMsg
	public static final SOSMsgJOE	JOE_B_DateForm_AddFile								= new SOSMsgJOE("JOE_B_DateForm_AddFile");
	@I18NMsg
	public static final SOSMsgJOE	JOE_Sep_DateForm_S2									= new SOSMsgJOE("JOE_Sep_DateForm_S2");
	@I18NMsg
	public static final SOSMsgJOE	JOE_Tbl_DateForm_Includes							= new SOSMsgJOE("JOE_Tbl_DateForm_Includes");
	@I18NMsg
	public static final SOSMsgJOE	JOE_TCl_DateForm_NameColumn							= new SOSMsgJOE("JOE_TCl_DateForm_NameColumn");
	@I18NMsg
	public static final SOSMsgJOE	JOE_TCl_DateForm_FileColumn							= new SOSMsgJOE("JOE_TCl_DateForm_FileColumn");
	@I18NMsg
	public static final SOSMsgJOE	JOE_TCl_DateForm_DescriptionColumn					= new SOSMsgJOE("JOE_TCl_DateForm_DescriptionColumn");
	@I18NMsg
	public static final SOSMsgJOE	JOE_B_DateForm_NewButton							= new SOSMsgJOE("JOE_B_DateForm_NewButton");
	@I18NMsg
	public static final SOSMsgJOE	JOE_B_DateForm_OpenButton							= new SOSMsgJOE("JOE_B_DateForm_OpenButton");
	@I18NMsg
	public static final SOSMsgJOE	JOE_B_DateForm_RemoveFile							= new SOSMsgJOE("JOE_B_DateForm_RemoveFile");
	@I18NMsg
	public static final SOSMsgJOE	JOE_E_0001											= new SOSMsgJOE("JOE_E_0001");

	// DaysForm
	@I18NMsg
	public static final SOSMsgJOE	JOE_G_DaysForm_WeekdaysGroup						= new SOSMsgJOE("JOE_G_DaysForm_WeekdaysGroup");
	@I18NMsg
	public static final SOSMsgJOE	JOE_G_DaysForm_MonthdaysGroup						= new SOSMsgJOE("JOE_G_DaysForm_MonthdaysGroup");
	@I18NMsg
	public static final SOSMsgJOE	JOE_G_DaysForm_UltimosGroup							= new SOSMsgJOE("JOE_G_DaysForm_UltimosGroup");
	@I18NMsg
	public static final SOSMsgJOE	JOE_G_DaysForm_MonthGroup							= new SOSMsgJOE("JOE_G_DaysForm_MonthGroup");
	@I18NMsg
	public static final SOSMsgJOE	JOE_L_DaysForm_Weekday								= new SOSMsgJOE("JOE_L_DaysForm_Weekday");
	@I18NMsg
	public static final SOSMsgJOE	JOE_L_DaysForm_Monthday								= new SOSMsgJOE("JOE_L_DaysForm_Monthday");
	@I18NMsg
	public static final SOSMsgJOE	JOE_L_DaysForm_Ultimo								= new SOSMsgJOE("JOE_L_DaysForm_Ultimo");
	@I18NMsg
	public static final SOSMsgJOE	JOE_L_DaysForm_Month								= new SOSMsgJOE("JOE_L_DaysForm_Month");
	@I18NMsg
	public static final SOSMsgJOE	JOE_B_DaysForm_AddWeekday							= new SOSMsgJOE("JOE_B_DaysForm_AddWeekday");
	@I18NMsg
	public static final SOSMsgJOE	JOE_B_DaysForm_AddMonthday							= new SOSMsgJOE("JOE_B_DaysForm_AddMonthday");
	@I18NMsg
	public static final SOSMsgJOE	JOE_B_DaysForm_AddUltimo							= new SOSMsgJOE("JOE_B_DaysForm_AddUltimo");
	@I18NMsg
	public static final SOSMsgJOE	JOE_B_DaysForm_AddMonth								= new SOSMsgJOE("JOE_B_DaysForm_AddMonth");
	@I18NMsg
	public static final SOSMsgJOE	JOE_Sep_DaysForm_S1									= new SOSMsgJOE("JOE_Sep_DaysForm_S1");
	@I18NMsg
	public static final SOSMsgJOE	JOE_Lst_DaysForm_UsedDays							= new SOSMsgJOE("JOE_Lst_DaysForm_UsedDays");
	@I18NMsg
	public static final SOSMsgJOE	JOE_B_DaysForm_Remove								= new SOSMsgJOE("JOE_B_DaysForm_Remove");
	@I18NMsg
	public static final SOSMsgJOE	JOE_Lst_DaysForm_DaysList							= new SOSMsgJOE("JOE_Lst_DaysForm_DaysList");
	@I18NMsg
	public static final SOSMsgJOE	JOE_Lst_DaysForm_GroupsList							= new SOSMsgJOE("JOE_Lst_DaysForm_GroupsList");
	@I18NMsg
	public static final SOSMsgJOE	JOE_B_DaysForm_NewGroup								= new SOSMsgJOE("JOE_B_DaysForm_NewGroup");
	@I18NMsg
	public static final SOSMsgJOE	JOE_B_DaysForm_Add									= new SOSMsgJOE("JOE_B_DaysForm_Add");
	@I18NMsg
	public static final SOSMsgJOE	JOE_B_DaysForm_ApplyGroup							= new SOSMsgJOE("JOE_B_DaysForm_ApplyGroup");
	@I18NMsg
	public static final SOSMsgJOE	JOE_B_DaysForm_RemoveDay							= new SOSMsgJOE("JOE_B_DaysForm_RemoveDay");
	@I18NMsg
	public static final SOSMsgJOE	JOE_L_Monthnames									= new SOSMsgJOE("JOE_L_Monthnames");
	@I18NMsg
	public static final SOSMsgJOE	JOE_Cbo_DaysForm_UnusedDays							= new SOSMsgJOE("JOE_Cbo_DaysForm_UnusedDays");
	@I18NMsg
	public static final SOSMsgJOE	JOE_E_0008											= new SOSMsgJOE("JOE_E_0008");

	// TODO DetailDialogForm
	@I18NMsg
	public static final SOSMsgJOE	JOE_M_0017											= new SOSMsgJOE("JOE_M_0017");
	@I18NMsg
	public static final SOSMsgJOE	JOE_M_0018											= new SOSMsgJOE("JOE_M_0018");
	@I18NMsg
	public static final SOSMsgJOE	JOE_M_0019											= new SOSMsgJOE("JOE_M_0019");

	// DetailForm
	@I18NMsg
	public static final SOSMsgJOE	JOE_G_DetailForm_MainGroup							= new SOSMsgJOE("JOE_G_DetailForm_MainGroup");
	@I18NMsg
	public static final SOSMsgJOE	JOE_G_DetailForm_ParameterGroup						= new SOSMsgJOE("JOE_G_DetailForm_ParameterGroup");
	// @I18NMsg
	// public static final SOSMsgJOE JOE_L_NameLabel = new SOSMsgJOE("JOE_L_NameLabel");
	@I18NMsg
	public static final SOSMsgJOE	JOE_T_DetailForm_Name								= new SOSMsgJOE("JOE_T_DetailForm_Name");
	@I18NMsg
	public static final SOSMsgJOE	JOE_L_Value											= new SOSMsgJOE("JOE_L_Value");
	@I18NMsg
	public static final SOSMsgJOE	JOE_T_DetailForm_Value								= new SOSMsgJOE("JOE_T_DetailForm_Value");
	@I18NMsg
	public static final SOSMsgJOE	JOE_B_DetailForm_Text								= new SOSMsgJOE("JOE_B_DetailForm_Text");
	@I18NMsg
	public static final SOSMsgJOE	JOE_B_DetailForm_ApplyParam							= new SOSMsgJOE("JOE_B_DetailForm_ApplyParam");
	@I18NMsg
	public static final SOSMsgJOE	JOE_Tbl_DetailForm_Params							= new SOSMsgJOE("JOE_Tbl_DetailForm_Params");
	@I18NMsg
	public static final SOSMsgJOE	JOE_TCl_DetailForm_NameColumn						= new SOSMsgJOE("JOE_TCl_DetailForm_NameColumn");
	@I18NMsg
	public static final SOSMsgJOE	JOE_TCl_DetailForm_ValueColumn						= new SOSMsgJOE("JOE_TCl_DetailForm_ValueColumn");
	@I18NMsg
	public static final SOSMsgJOE	JOE_TCl_DetailForm_TextColumn						= new SOSMsgJOE("JOE_TCl_DetailForm_TextColumn");
	@I18NMsg
	public static final SOSMsgJOE	JOE_B_DetailForm_New								= new SOSMsgJOE("JOE_B_DetailForm_New");
	@I18NMsg
	public static final SOSMsgJOE	JOE_B_DetailForm_Up									= new SOSMsgJOE("JOE_B_DetailForm_Up");
	@I18NMsg
	public static final SOSMsgJOE	JOE_B_DetailForm_Down								= new SOSMsgJOE("JOE_B_DetailForm_Down");
	@I18NMsg
	public static final SOSMsgJOE	JOE_B_DetailForm_Wizard								= new SOSMsgJOE("JOE_B_DetailForm_Wizard");
	@I18NMsg
	public static final SOSMsgJOE	JOE_B_DetailForm_Remove								= new SOSMsgJOE("JOE_B_DetailForm_Remove");
	@I18NMsg
	public static final SOSMsgJOE	JOE_B_DetailForm_TempDocumentation					= new SOSMsgJOE("JOE_B_DetailForm_TempDocumentation");
	@I18NMsg
	public static final SOSMsgJOE	JOE_B_DetailForm_ApplyDetails						= new SOSMsgJOE("JOE_B_DetailForm_ApplyDetails");
	@I18NMsg
	public static final SOSMsgJOE	JOE_B_Cancel								        = new SOSMsgJOE("JOE_B_Cancel");
	@I18NMsg
	public static final SOSMsgJOE	JOE_T_DetailForm_JobChainNote						= new SOSMsgJOE("JOE_T_DetailForm_Note");
	@I18NMsg
	public static final SOSMsgJOE	JOE_Cbo_DetailForm_Language							= new SOSMsgJOE("JOE_Cbo_DetailForm_Language");
	@I18NMsg
	public static final SOSMsgJOE	JOE_B_DetailForm_RefreshWizardNoteParam				= new SOSMsgJOE("JOE_B_DetailForm_RefreshWizardNoteParam");
	@I18NMsg
	public static final SOSMsgJOE	JOE_T_DetailForm_Param								= new SOSMsgJOE("JOE_T_DetailForm_Param");
	@I18NMsg
	public static final SOSMsgJOE	JOE_G_DetailForm_NoteGroup							= new SOSMsgJOE("JOE_G_DetailForm_NoteGroup");
	@I18NMsg
	public static final SOSMsgJOE	JOE_T_DetailForm_Note								= new SOSMsgJOE("JOE_T_DetailForm_JobChainNote");
	@I18NMsg
	public static final SOSMsgJOE	JOE_B_DetailForm_XML								= new SOSMsgJOE("JOE_B_DetailForm_XML");
	@I18NMsg
	public static final SOSMsgJOE	JOE_B_DetailForm_Documentation						= new SOSMsgJOE("JOE_B_DetailForm_Documentation");
	@I18NMsg
	public static final SOSMsgJOE	JOE_M_0013											= new SOSMsgJOE("JOE_M_0013");
	@I18NMsg
	public static final SOSMsgJOE	JOE_M_0012											= new SOSMsgJOE("JOE_M_0012");
	@I18NMsg
	public static final SOSMsgJOE	JOE_M_0011											= new SOSMsgJOE("JOE_M_0011");
	@I18NMsg
	public static final SOSMsgJOE	JOE_L_DetailForm_JobDocumentation					= new SOSMsgJOE("JOE_L_DetailForm_JobDocumentation");
	@I18NMsg
	public static final SOSMsgJOE	JOE_T_DetailForm_ParamsFile							= new SOSMsgJOE("JOE_T_DetailForm_ParamsFile");
	@I18NMsg
	public static final SOSMsgJOE	JOE_L_DetailForm_ConfigFile							= new SOSMsgJOE("JOE_L_DetailForm_ConfigFile");
	@I18NMsg
	public static final SOSMsgJOE	JOE_M_0010											= new SOSMsgJOE("JOE_M_0010");
	@I18NMsg
	public static final SOSMsgJOE	JOE_M_0020											= new SOSMsgJOE("JOE_M_0020");
	@I18NMsg
	public static final SOSMsgJOE	JOE_M_0021											= new SOSMsgJOE("JOE_M_0021");
	@I18NMsg
	public static final SOSMsgJOE	JOE_M_0022											= new SOSMsgJOE("JOE_M_0022");
	@I18NMsg
	public static final SOSMsgJOE	JOE_M_0023											= new SOSMsgJOE("JOE_M_0023");
	@I18NMsg
	public static final SOSMsgJOE	JOE_M_0024											= new SOSMsgJOE("JOE_M_0024");

	// DetailXMLEditorDialogForm
	@I18NMsg
	public static final SOSMsgJOE	JOE_M_0009											= new SOSMsgJOE("JOE_M_0009");
	@I18NMsg
	public static final SOSMsgJOE	JOE_G_DetailXMLEditorDialogForm_JobGroup			= new SOSMsgJOE("JOE_G_DetailXMLEditorDialogForm_JobGroup");
	@I18NMsg
	public static final SOSMsgJOE	JOE_T_DetailXMLEditorDialogForm_XML					= new SOSMsgJOE("JOE_T_DetailXMLEditorDialogForm_XML");
	@I18NMsg
	public static final SOSMsgJOE	JOE_B_DetailXMLEditorDialogForm_Apply				= new SOSMsgJOE("JOE_B_DetailXMLEditorDialogForm_Apply");
	@I18NMsg
	public static final SOSMsgJOE	JOE_B_DetailXMLEditorDialogForm_Close				= new SOSMsgJOE("JOE_B_DetailXMLEditorDialogForm_Close");
	@I18NMsg
	public static final SOSMsgJOE	JOE_E_0002											= new SOSMsgJOE("JOE_E_0002");
	@I18NMsg
	public static final SOSMsgJOE	JOE_M_0008											= new SOSMsgJOE("JOE_M_0008");

	// HotFolderDialog
	@I18NMsg
	public static final SOSMsgJOE	JOE_M_HotFolderDialog_SchedulerGroup				= new SOSMsgJOE("JOE_M_HotFolderDialog_SchedulerGroup");
	@I18NMsg
	public static final SOSMsgJOE	JOE_M_0006											= new SOSMsgJOE("JOE_M_0006");
	@I18NMsg
	public static final SOSMsgJOE	JOE_M_0007											= new SOSMsgJOE("JOE_M_0007");
	@I18NMsg
	public static final SOSMsgJOE	JOE_B_HotFolderDialog_Cancel						= new SOSMsgJOE("JOE_B_HotFolderDialog_Cancel");
	@I18NMsg
	public static final SOSMsgJOE	JOE_T_HotFolderDialog_Name							= new SOSMsgJOE("JOE_T_HotFolderDialog_Name");
	@I18NMsg
	public static final SOSMsgJOE	JOE_T_HotFolderDialog_Port							= new SOSMsgJOE("JOE_T_HotFolderDialog_Port");
	@I18NMsg
	public static final SOSMsgJOE	JOE_B_HotFolderDialog_Add							= new SOSMsgJOE("JOE_B_HotFolderDialog_Add");
	@I18NMsg
	public static final SOSMsgJOE	JOE_B_HotFolderDialog_Rename						= new SOSMsgJOE("JOE_B_HotFolderDialog_Rename");
	@I18NMsg
	public static final SOSMsgJOE	JOE_M_0005											= new SOSMsgJOE("JOE_M_0005");
	@I18NMsg
	public static final SOSMsgJOE	JOE_E_0007											= new SOSMsgJOE("JOE_E_0007");
	@I18NMsg
	public static final SOSMsgJOE	JOE_B_HotFolderDialog_Open							= new SOSMsgJOE("JOE_B_HotFolderDialog_Open");
	@I18NMsg
	public static final SOSMsgJOE	JOE_E_0003											= new SOSMsgJOE("JOE_E_0003");
	@I18NMsg
	public static final SOSMsgJOE	JOE_E_0004											= new SOSMsgJOE("JOE_E_0004");
	@I18NMsg
	public static final SOSMsgJOE	JOE_E_0005											= new SOSMsgJOE("JOE_E_0005");
	@I18NMsg
	public static final SOSMsgJOE	JOE_M_0001											= new SOSMsgJOE("JOE_M_0001");
	@I18NMsg
	public static final SOSMsgJOE	JOE_M_0002											= new SOSMsgJOE("JOE_M_0002");
	@I18NMsg
	public static final SOSMsgJOE	JOE_M_0003											= new SOSMsgJOE("JOE_M_0003");
	@I18NMsg
	public static final SOSMsgJOE	JOE_M_0004											= new SOSMsgJOE("JOE_M_0004");
	@I18NMsg
	public static final SOSMsgJOE	JOE_HotFolderDialog_Tree							= new SOSMsgJOE("JOE_HotFolderDialog_Tree");
	@I18NMsg
	public static final SOSMsgJOE	JOE_E_0006											= new SOSMsgJOE("JOE_E_0006");

	// HttpAuthenticationForm
	@I18NMsg
	public static final SOSMsgJOE	JOE_G_HttpAuthenticationForm_AuthGroup				= new SOSMsgJOE("JOE_G_HttpAuthenticationForm_AuthGroup");
	@I18NMsg
	public static final SOSMsgJOE	JOE_G_HttpAuthenticationForm_Group					= new SOSMsgJOE("JOE_G_HttpAuthenticationForm_Group");
	@I18NMsg
	public static final SOSMsgJOE	JOE_L_HttpAuthenticationForm_UserName				= new SOSMsgJOE("JOE_L_HttpAuthenticationForm_UserName");
	@I18NMsg
	public static final SOSMsgJOE	JOE_T_HttpAuthenticationForm_UserName				= new SOSMsgJOE("JOE_T_HttpAuthenticationForm_UserName");
	@I18NMsg
	public static final SOSMsgJOE	JOE_L_HttpAuthenticationForm_Password				= new SOSMsgJOE("JOE_L_HttpAuthenticationForm_Password");
	@I18NMsg
	public static final SOSMsgJOE	JOE_T_HttpAuthenticationForm_Password				= new SOSMsgJOE("JOE_T_HttpAuthenticationForm_Password");
	@I18NMsg
	public static final SOSMsgJOE	JOE_B_HttpAuthenticationForm_Encrypt				= new SOSMsgJOE("JOE_B_HttpAuthenticationForm_Encrypt");
	@I18NMsg
	public static final SOSMsgJOE	JOE_L_HttpAuthenticationForm_MD5PW					= new SOSMsgJOE("JOE_L_HttpAuthenticationForm_MD5PW");
	@I18NMsg
	public static final SOSMsgJOE	JOE_T_HttpAuthenticationForm_MD5PW					= new SOSMsgJOE("JOE_T_HttpAuthenticationForm_MD5PW");
	@I18NMsg
	public static final SOSMsgJOE	JOE_B_HttpAuthenticationForm_Apply					= new SOSMsgJOE("JOE_B_HttpAuthenticationForm_Apply");
	@I18NMsg
	public static final SOSMsgJOE	JOE_Tbl_HttpAuthenticationForm_Users				= new SOSMsgJOE("JOE_Tbl_HttpAuthenticationForm_Users");
	@I18NMsg
	public static final SOSMsgJOE	JOE_TCl_HttpAuthenticationForm_NameColumn			= new SOSMsgJOE("JOE_TCl_HttpAuthenticationForm_NameColumn");
	@I18NMsg
	public static final SOSMsgJOE	JOE_TCl_HttpAuthenticationForm_PWColumn				= new SOSMsgJOE("JOE_TCl_HttpAuthenticationForm_PWColumn");
	@I18NMsg
	public static final SOSMsgJOE	JOE_B_HttpAuthenticationForm_Remove					= new SOSMsgJOE("JOE_B_HttpAuthenticationForm_Remove");
	@I18NMsg
	public static final SOSMsgJOE	JOE_M_0016											= new SOSMsgJOE("JOE_M_0016");

	// HttpDirectoriesForm
	@I18NMsg
	public static final SOSMsgJOE	JOE_G_HttpDirectoriesForm_DirectoriesGroup			= new SOSMsgJOE("JOE_G_HttpDirectoriesForm_DirectoriesGroup");
	@I18NMsg
	public static final SOSMsgJOE	JOE_G_HttpDirectoriesForm_Group1					= new SOSMsgJOE("JOE_G_HttpDirectoriesForm_Group1");
	@I18NMsg
	public static final SOSMsgJOE	JOE_L_HttpDirectoriesForm_URLPath					= new SOSMsgJOE("JOE_L_HttpDirectoriesForm_URLPath");
	@I18NMsg
	public static final SOSMsgJOE	JOE_T_HttpDirectoriesForm_URLPath					= new SOSMsgJOE("JOE_T_HttpDirectoriesForm_URLPath");
	@I18NMsg
	public static final SOSMsgJOE	JOE_L_HttpDirectoriesForm_Path						= new SOSMsgJOE("JOE_L_HttpDirectoriesForm_Path");
	@I18NMsg
	public static final SOSMsgJOE	JOE_T_HttpDirectoriesForm_Path						= new SOSMsgJOE("JOE_T_HttpDirectoriesForm_Path");
	@I18NMsg
	public static final SOSMsgJOE	JOE_B_HttpDirectoriesForm_Apply						= new SOSMsgJOE("JOE_B_HttpDirectoriesForm_Apply");
	@I18NMsg
	public static final SOSMsgJOE	JOE_Tbl_HttpDirectoriesForm_DirectoriesTable		= new SOSMsgJOE("JOE_Tbl_HttpDirectoriesForm_DirectoriesTable");
	@I18NMsg
	public static final SOSMsgJOE	JOE_TCl_HttpDirectoriesForm_URLPath					= new SOSMsgJOE("JOE_TCl_HttpDirectoriesForm_URLPath");
	@I18NMsg
	public static final SOSMsgJOE	JOE_TCl_HttpDirectoriesForm_Path					= new SOSMsgJOE("JOE_TCl_HttpDirectoriesForm_Path");
	@I18NMsg
	public static final SOSMsgJOE	JOE_B_HttpDirectoriesForm_Remove					= new SOSMsgJOE("JOE_B_HttpDirectoriesForm_Remove");

	// JobAssistentDelayAfterErrorForm
	@I18NMsg
	public static final SOSMsgJOE	JOE_M_JobAssistent_Step8of9							= new SOSMsgJOE("JOE_M_JobAssistent_Step8of9");
	@I18NMsg
	public static final SOSMsgJOE	JOE_M_JobAssistent_Step8of8							= new SOSMsgJOE("JOE_M_JobAssistent_Step8of8");
	@I18NMsg
	public static final SOSMsgJOE	JOE_M_JobAssistent_DelayAfterError					= new SOSMsgJOE("JOE_M_JobAssistent_DelayAfterError");
	@I18NMsg
	public static final SOSMsgJOE	JOE_M_JobAssistent_JobGroup							= new SOSMsgJOE("JOE_M_JobAssistent_JobGroup");
	@I18NMsg
	public static final SOSMsgJOE	JOE_L_JobAssistent_JobWait							= new SOSMsgJOE("JOE_L_JobAssistent_JobWait");
	@I18NMsg
	public static final SOSMsgJOE	JOE_Cmp_JobAssistent_Time							= new SOSMsgJOE("JOE_Cmp_JobAssistent_Time");
	@I18NMsg
	public static final SOSMsgJOE	JOE_T_JobAssistent_Hour								= new SOSMsgJOE("JOE_T_JobAssistent_Hour");
	@I18NMsg
	public static final SOSMsgJOE	JOE_T_JobAssistent_Min								= new SOSMsgJOE("JOE_T_JobAssistent_Min");
	@I18NMsg
	public static final SOSMsgJOE	JOE_T_JobAssistent_Sec								= new SOSMsgJOE("JOE_T_JobAssistent_Sec");
	@I18NMsg
	public static final SOSMsgJOE	JOE_L_JobAssistent_TimeFormat						= new SOSMsgJOE("JOE_L_JobAssistent_TimeFormat");
	@I18NMsg
	public static final SOSMsgJOE	JOE_L_JobAssistent_OftenSetBack						= new SOSMsgJOE("JOE_L_JobAssistent_OftenSetBack");
	@I18NMsg
	public static final SOSMsgJOE	JOE_T_JobAssistent_ErrorCount						= new SOSMsgJOE("JOE_T_JobAssistent_ErrorCount");
	@I18NMsg
	public static final SOSMsgJOE	JOE_L_JobAssistent_MaxErrors						= new SOSMsgJOE("JOE_L_JobAssistent_MaxErrors");
	@I18NMsg
	public static final SOSMsgJOE	JOE_T_JobAssistent_MaxErrors						= new SOSMsgJOE("JOE_T_JobAssistent_MaxErrors");
	@I18NMsg
	public static final SOSMsgJOE	JOE_Cmp_JobAssistent_Cancel							= new SOSMsgJOE("JOE_Cmp_JobAssistent_Cancel");
	@I18NMsg
	public static final SOSMsgJOE	JOE_B_JobAssistent_Cancel							= new SOSMsgJOE("JOE_B_JobAssistent_Cancel");
	@I18NMsg
	public static final SOSMsgJOE	JOE_Cmp_JobAssistent_Show							= new SOSMsgJOE("JOE_Cmp_JobAssistent_Show");
	@I18NMsg
	public static final SOSMsgJOE	JOE_B_JobAssistent_Show								= new SOSMsgJOE("JOE_B_JobAssistent_Show");
	@I18NMsg
	public static final SOSMsgJOE	JOE_B_JobAssistent_Finish							= new SOSMsgJOE("JOE_B_JobAssistent_Finish");
	@I18NMsg
	public static final SOSMsgJOE	JOE_B_JobAssistent_Back								= new SOSMsgJOE("JOE_B_JobAssistent_Back");
	@I18NMsg
	public static final SOSMsgJOE	JOE_B_JobAssistent_Next								= new SOSMsgJOE("JOE_B_JobAssistent_Next");
	@I18NMsg
	public static final SOSMsgJOE	JOE_M_JobAssistent_NoNum							= new SOSMsgJOE("JOE_M_JobAssistent_NoNum");
	@I18NMsg
	public static final SOSMsgJOE	JOE_M_JobAssistent_Finish							= new SOSMsgJOE("JOE_M_JobAssistent_Finish");
	@I18NMsg
	public static final SOSMsgJOE	JOE_M_JobAssistent_EndWizard						= new SOSMsgJOE("JOE_M_JobAssistent_EndWizard");
	@I18NMsg
	public static final SOSMsgJOE	JOE_M_JobAssistent_TimeMissing						= new SOSMsgJOE("JOE_M_JobAssistent_TimeMissing");
	@I18NMsg
	public static final SOSMsgJOE	JOE_M_JobAssistent_ErrorCountMissing				= new SOSMsgJOE("JOE_M_JobAssistent_ErrorCountMissing");
	@I18NMsg
	public static final SOSMsgJOE	JOE_M_JobAssistent_CancelWizard						= new SOSMsgJOE("JOE_M_JobAssistent_CancelWizard");

	// JobAssistentDelayOrderAfterSetbackForm
	@I18NMsg
	public static final SOSMsgJOE	JOE_M_JobAssistent_Step9of9							= new SOSMsgJOE("JOE_M_JobAssistent_Step9of9");
	@I18NMsg
	public static final SOSMsgJOE	JOE_M_JobAssistent_OrderAfterSetback				= new SOSMsgJOE("JOE_M_JobAssistent_OrderAfterSetback");
	@I18NMsg
	public static final SOSMsgJOE	JOE_L_JobAssistent_OrderWait						= new SOSMsgJOE("JOE_L_JobAssistent_OrderWait");
	@I18NMsg
	public static final SOSMsgJOE	JOE_L_JobAssistent_OftenSetback						= new SOSMsgJOE("JOE_L_JobAssistent_OftenSetback");
	@I18NMsg
	public static final SOSMsgJOE	JOE_T_JobAssistent_SetBack							= new SOSMsgJOE("JOE_T_JobAssistent_SetBack");
	@I18NMsg
	public static final SOSMsgJOE	JOE_L_JobAssistent_NumOfSetback						= new SOSMsgJOE("JOE_L_JobAssistent_NumOfSetback");
	@I18NMsg
	public static final SOSMsgJOE	JOE_B_JobAssistent_No								= new SOSMsgJOE("JOE_B_JobAssistent_No");
	@I18NMsg
	public static final SOSMsgJOE	JOE_B_JobAssistent_Yes								= new SOSMsgJOE("JOE_B_JobAssistent_Yes");
	@I18NMsg
	public static final SOSMsgJOE	JOE_Composite1										= new SOSMsgJOE("JOE_Composite1");
	@I18NMsg
	public static final SOSMsgJOE	JOE_Composite2										= new SOSMsgJOE("JOE_Composite2");

	// JobAssistentExecuteForms
	@I18NMsg
	public static final SOSMsgJOE	JOE_M_JobAssistent_Execute							= new SOSMsgJOE("JOE_M_JobAssistent_Execute");
	@I18NMsg
	public static final SOSMsgJOE	JOE_B_JobAssistent_Process							= new SOSMsgJOE("JOE_B_JobAssistent_Process");
	@I18NMsg
	public static final SOSMsgJOE	JOE_B_JobAssistent_Script							= new SOSMsgJOE("JOE_B_JobAssistent_Script");
	@I18NMsg
	public static final SOSMsgJOE	JOE_L_JobAssistent_Language							= new SOSMsgJOE("JOE_L_JobAssistent_Language");
	@I18NMsg
	public static final SOSMsgJOE	JOE_Cbo_JobAssistent_Language						= new SOSMsgJOE("JOE_Cbo_JobAssistent_Language");
	@I18NMsg
	public static final SOSMsgJOE	JOE_T_JobAssistent_Include							= new SOSMsgJOE("JOE_T_JobAssistent_Include");
	@I18NMsg
	public static final SOSMsgJOE	JOE_L_JobAssistent_Include							= new SOSMsgJOE("JOE_L_JobAssistent_Include");

	// JobAssistentForm

	// JobAssistentImportJobParamsForm
	@I18NMsg
	public static final SOSMsgJOE	JOE_M_0025											= new SOSMsgJOE("JOE_M_0025");
	@I18NMsg
	public static final SOSMsgJOE	JOE_M_0026											= new SOSMsgJOE("JOE_M_0026");
	@I18NMsg
	public static final SOSMsgJOE	JOE_M_JobAssistent_Step3of9							= new SOSMsgJOE("JOE_M_JobAssistent_Step3of9");
	@I18NMsg
	public static final SOSMsgJOE	JOE_M_JobAssistent_Step3of8							= new SOSMsgJOE("JOE_M_JobAssistent_Step3of8");
	@I18NMsg
	public static final SOSMsgJOE	JOE_M_JobAssistent_JobParameter						= new SOSMsgJOE("JOE_M_JobAssistent_JobParameter");
	@I18NMsg
	public static final SOSMsgJOE	JOE_M_JobAssistent_JobParameter_Missing_Xulrunner	= new SOSMsgJOE("JOE_M_JobAssistent_JobParameter_Missing_Xulrunner");
	@I18NMsg
	public static final SOSMsgJOE	JOE_G_JobAssistent_ParamGroup						= new SOSMsgJOE("JOE_G_JobAssistent_ParamGroup");
	@I18NMsg
	public static final SOSMsgJOE	JOE_Composite3										= new SOSMsgJOE("JOE_Composite3");
	@I18NMsg
	public static final SOSMsgJOE	JOE_T_JobAssistent_Name								= new SOSMsgJOE("JOE_T_JobAssistent_Name");
	@I18NMsg
	public static final SOSMsgJOE	JOE_T_JobAssistent_Value							= new SOSMsgJOE("JOE_T_JobAssistent_Value");
	@I18NMsg
	public static final SOSMsgJOE	JOE_B_JobAssistent_Apply							= new SOSMsgJOE("JOE_B_JobAssistent_Apply");
	@I18NMsg
	public static final SOSMsgJOE	JOE_Tbl_JobAssistent_DescParams						= new SOSMsgJOE("JOE_Tbl_JobAssistent_DescParams");
	@I18NMsg
	public static final SOSMsgJOE	JOE_TCl_JobAssistent_NameColumn						= new SOSMsgJOE("JOE_TCl_JobAssistent_NameColumn");
	@I18NMsg
	public static final SOSMsgJOE	JOE_TCl_JobAssistent_ValueColumn					= new SOSMsgJOE("JOE_TCl_JobAssistent_ValueColumn");
	@I18NMsg
	public static final SOSMsgJOE	JOE_Composite4										= new SOSMsgJOE("JOE_Composite4");
	@I18NMsg
	public static final SOSMsgJOE	JOE_B_JobAssistent_Put								= new SOSMsgJOE("JOE_B_JobAssistent_Put");
	@I18NMsg
	public static final SOSMsgJOE	JOE_B_JobAssistent_PutAll							= new SOSMsgJOE("JOE_B_JobAssistent_PutAll");
	@I18NMsg
	public static final SOSMsgJOE	JOE_B_JobAssistent_Remove							= new SOSMsgJOE("JOE_B_JobAssistent_Remove");
	@I18NMsg
	public static final SOSMsgJOE	JOE_B_JobAssistent_RemoveAll						= new SOSMsgJOE("JOE_B_JobAssistent_RemoveAll");
	@I18NMsg
	public static final SOSMsgJOE	JOE_Tbl_JobAssistent_SelectedParams					= new SOSMsgJOE("JOE_Tbl_JobAssistent_SelectedParams");
	@I18NMsg
	public static final SOSMsgJOE	JOE_DescriptionBrowser								= new SOSMsgJOE("JOE_DescriptionBrowser");
	@I18NMsg
	public static final SOSMsgJOE	JOE_M_JobAssistent_NoParamName						= new SOSMsgJOE("JOE_M_JobAssistent_NoParamName");
	@I18NMsg
	public static final SOSMsgJOE	JOE_M_JobAssistent_ParamsExist						= new SOSMsgJOE("JOE_M_JobAssistent_ParamsExist");
	@I18NMsg
	public static final SOSMsgJOE	JOE_M_JobAssistent_NoParamsSelected					= new SOSMsgJOE("JOE_M_JobAssistent_NoParamsSelected");
	@I18NMsg
	public static final SOSMsgJOE	JOE_M_JobAssistent_NoRemParamsSelected				= new SOSMsgJOE("JOE_M_JobAssistent_NoRemParamsSelected");
	@I18NMsg
	public static final SOSMsgJOE	JOE_M_JobAssistent_ParamsRequired					= new SOSMsgJOE("JOE_M_JobAssistent_ParamsRequired");
	@I18NMsg
	public static final SOSMsgJOE	JOE_M_ApplyChanges									= new SOSMsgJOE("JOE_M_ApplyChanges");

	// JobAssistentImportJobsForm
	@I18NMsg
	public static final SOSMsgJOE	JOE_M_JobAssistent_MissingDirectory					= new SOSMsgJOE("JOE_M_JobAssistent_MissingDirectory");
	@I18NMsg
	public static final SOSMsgJOE	JOE_M_JobAssistent_Step2of9							= new SOSMsgJOE("JOE_M_JobAssistent_Step2of9");
	@I18NMsg
	public static final SOSMsgJOE	JOE_M_JobAssistent_Step2of8							= new SOSMsgJOE("JOE_M_JobAssistent_Step2of8");
	@I18NMsg
	public static final SOSMsgJOE	JOE_M_JobAssistent_ImportJobs						= new SOSMsgJOE("JOE_M_JobAssistent_ImportJobs");
	@I18NMsg
	public static final SOSMsgJOE	JOE_G_JobAssistent_JobGroup							= new SOSMsgJOE("JOE_G_JobAssistent_JobGroup");
	@I18NMsg
	public static final SOSMsgJOE	JOE_L_JobAssistent_JobName							= new SOSMsgJOE("JOE_L_JobAssistent_JobName");
	@I18NMsg
	public static final SOSMsgJOE	JOE_T_JobAssistent_JobName							= new SOSMsgJOE("JOE_T_JobAssistent_JobName");
	@I18NMsg
	public static final SOSMsgJOE	JOE_L_JobAssistent_Title							= new SOSMsgJOE("JOE_L_JobAssistent_Title");
	@I18NMsg
	public static final SOSMsgJOE	JOE_T_JobAssistent_Title							= new SOSMsgJOE("JOE_T_JobAssistent_Title");
	@I18NMsg
	public static final SOSMsgJOE	JOE_L_JobAssistent_PathLabel						= new SOSMsgJOE("JOE_L_JobAssistent_PathLabel");
	@I18NMsg
	public static final SOSMsgJOE	JOE_T_JobAssistent_Path								= new SOSMsgJOE("JOE_T_JobAssistent_Path");
	@I18NMsg
	public static final SOSMsgJOE	JOE_B_JobAssistent_Description						= new SOSMsgJOE("JOE_B_JobAssistent_Description");
	@I18NMsg
	public static final SOSMsgJOE	JOE_M_JobAssistent_NoJobDescription					= new SOSMsgJOE("JOE_M_JobAssistent_NoJobDescription");
	@I18NMsg
	public static final SOSMsgJOE	JOE_E_0009											= new SOSMsgJOE("JOE_E_0009");
	@I18NMsg
	public static final SOSMsgJOE	JOE_B_JobAssistent_Import							= new SOSMsgJOE("JOE_B_JobAssistent_Import");
	@I18NMsg
	public static final SOSMsgJOE	JOE_M_JobAssistent_JobNameExists					= new SOSMsgJOE("JOE_M_JobAssistent_JobNameExists");
	@I18NMsg
	public static final SOSMsgJOE	JOE_M_JobAssistent_DiscardChanges					= new SOSMsgJOE("JOE_M_JobAssistent_DiscardChanges");
	@I18NMsg
	public static final SOSMsgJOE	JOE_M_JobAssistent_ImportParams						= new SOSMsgJOE("JOE_M_JobAssistent_ImportParams");
	@I18NMsg
	public static final SOSMsgJOE	JOE_G_JobAssistent_JobsGroup						= new SOSMsgJOE("JOE_G_JobAssistent_JobsGroup");
	@I18NMsg
	public static final SOSMsgJOE	JOE_JobAssistent_JobTree							= new SOSMsgJOE("JOE_JobAssistent_JobTree");
	@I18NMsg
	public static final SOSMsgJOE	JOE_JobAssistent_NameTreeColumn						= new SOSMsgJOE("JOE_JobAssistent_NameTreeColumn");
	@I18NMsg
	public static final SOSMsgJOE	JOE_JobAssistent_TitleTreeColumn					= new SOSMsgJOE("JOE_JobAssistent_TitleTreeColumn");
	@I18NMsg
	public static final SOSMsgJOE	JOE_JobAssistent_FilenameTreeColumn					= new SOSMsgJOE("JOE_JobAssistent_FilenameTreeColumn");
	@I18NMsg
	public static final SOSMsgJOE	JOE_M_JobAssistent_NoJobDoc							= new SOSMsgJOE("JOE_M_JobAssistent_NoJobDoc");
	@I18NMsg
	public static final SOSMsgJOE	JOE_M_JobAssistent_NoJobSelected					= new SOSMsgJOE("JOE_M_JobAssistent_NoJobSelected");
	@I18NMsg
	public static final SOSMsgJOE	JOE_M_JobAssistent_NoJobName						= new SOSMsgJOE("JOE_M_JobAssistent_NoJobName");
	@I18NMsg
	public static final SOSMsgJOE	JOE_M_JobAssistent_EditJobName						= new SOSMsgJOE("JOE_M_JobAssistent_EditJobName");

	// JobAssistentInfoForms
	@I18NMsg
	public static final SOSMsgJOE	JOE_M_JobAssistent_JobWizard						= new SOSMsgJOE("JOE_M_JobAssistent_JobWizard");
	@I18NMsg
	public static final SOSMsgJOE	JOE_T_JobAssistent_InfoGlobal						= new SOSMsgJOE("JOE_T_JobAssistent_InfoGlobal");
	@I18NMsg
	public static final SOSMsgJOE	JOE_B_JobAssistent_ShowInFuture						= new SOSMsgJOE("JOE_B_JobAssistent_ShowInFuture");

	// JobAssistentMonitoringDirectoryForms
	@I18NMsg
	public static final SOSMsgJOE	JOE_M_JobAssistent_MonitoringDirectory				= new SOSMsgJOE("JOE_M_JobAssistent_MonitoringDirectory");
	@I18NMsg
	public static final SOSMsgJOE	JOE_L_JobAssistent_Directory						= new SOSMsgJOE("JOE_L_JobAssistent_Directory");
	@I18NMsg
	public static final SOSMsgJOE	JOE_T_JobAssistent_Directory						= new SOSMsgJOE("JOE_T_JobAssistent_Directory");
	@I18NMsg
	public static final SOSMsgJOE	JOE_B_JobAssistent_ApplyDir							= new SOSMsgJOE("JOE_B_JobAssistent_ApplyDir");
	@I18NMsg
	public static final SOSMsgJOE	JOE_B_JobAssistent_NewDirectory						= new SOSMsgJOE("JOE_B_JobAssistent_NewDirectory");
	@I18NMsg
	public static final SOSMsgJOE	JOE_L_JobAssistent_Regex							= new SOSMsgJOE("JOE_L_JobAssistent_Regex");
	@I18NMsg
	public static final SOSMsgJOE	JOE_T_JobAssistent_Regex							= new SOSMsgJOE("JOE_T_JobAssistent_Regex");
	@I18NMsg
	public static final SOSMsgJOE	JOE_Tbl_JobAssistent_WatchDirectory					= new SOSMsgJOE("JOE_Tbl_JobAssistent_WatchDirectory");
	@I18NMsg
	public static final SOSMsgJOE	JOE_TCl_JobAssistent_DirectoryColumn				= new SOSMsgJOE("JOE_TCl_JobAssistent_DirectoryColumn");
	@I18NMsg
	public static final SOSMsgJOE	JOE_TCl_JobAssistent_RegexColumn					= new SOSMsgJOE("JOE_TCl_JobAssistent_RegexColumn");
	@I18NMsg
	public static final SOSMsgJOE	JOE_B_JobAssistent_RemoveDirectory					= new SOSMsgJOE("JOE_B_JobAssistent_RemoveDirectory");
	@I18NMsg
	public static final SOSMsgJOE	JOE_B_JobAssistent_Close							= new SOSMsgJOE("JOE_B_JobAssistent_Close");
	@I18NMsg
	public static final SOSMsgJOE	JOE_B_JobAssistentMonitoringDirectoryForms_Apply	= new SOSMsgJOE("JOE_B_JobAssistentMonitoringDirectoryForms_Apply");
	@I18NMsg
	public static final SOSMsgJOE	JOE_M_JobAssistent_Close							= new SOSMsgJOE("JOE_M_JobAssistent_Close");

	// JobAssistentPeriodForm
	@I18NMsg
	public static final SOSMsgJOE	JOE_G_JobAssistent_Period							= new SOSMsgJOE("JOE_G_JobAssistent_Period");
	@I18NMsg
	public static final SOSMsgJOE	JOE_L_JobAssistent_BeginTime						= new SOSMsgJOE("JOE_L_JobAssistent_BeginTime");
	@I18NMsg
	public static final SOSMsgJOE	JOE_L_Colon											= new SOSMsgJOE("JOE_L_Colon");
	@I18NMsg
	public static final SOSMsgJOE	JOE_T_JobAssistent_BeginHours						= new SOSMsgJOE("JOE_T_JobAssistent_BeginHours");
	@I18NMsg
	public static final SOSMsgJOE	JOE_T_JobAssistent_BeginMinutes						= new SOSMsgJOE("JOE_T_JobAssistent_BeginMinutes");
	@I18NMsg
	public static final SOSMsgJOE	JOE_T_JobAssistent_BeginSeconds						= new SOSMsgJOE("JOE_T_JobAssistent_BeginSeconds");
	@I18NMsg
	public static final SOSMsgJOE	JOE_L_JobAssistent_EndTime							= new SOSMsgJOE("JOE_L_JobAssistent_EndTime");
	@I18NMsg
	public static final SOSMsgJOE	JOE_T_JobAssistent_EndHours							= new SOSMsgJOE("JOE_T_JobAssistent_EndHours");
	@I18NMsg
	public static final SOSMsgJOE	JOE_T_JobAssistent_EndMinutes						= new SOSMsgJOE("JOE_T_JobAssistent_EndMinutes");
	@I18NMsg
	public static final SOSMsgJOE	JOE_T_JobAssistent_EndSeconds						= new SOSMsgJOE("JOE_T_JobAssistent_EndSeconds");

	// JobAssistentPeriodForms
	@I18NMsg
	public static final SOSMsgJOE	JOE_M_JobAssistent_RunTimePeriods					= new SOSMsgJOE("JOE_M_JobAssistent_RunTimePeriods");
	@I18NMsg
	public static final SOSMsgJOE	JOE_JobAssistent_EveryDayTabItem					= new SOSMsgJOE("JOE_JobAssistent_EveryDayTabItem");
	@I18NMsg
	public static final SOSMsgJOE	JOE_Group											= new SOSMsgJOE("JOE_Group");
	@I18NMsg
	public static final SOSMsgJOE	JOE_B_JobAssistent_NewPeriod						= new SOSMsgJOE("JOE_B_JobAssistent_NewPeriod");
	@I18NMsg
	public static final SOSMsgJOE	JOE_JobAssistent_WeekdayTabItem						= new SOSMsgJOE("JOE_JobAssistent_WeekdayTabItem");
	@I18NMsg
	public static final SOSMsgJOE	JOE_Cbo_JobAssistent_Weekday						= new SOSMsgJOE("JOE_Cbo_JobAssistent_Weekday");
	@I18NMsg
	public static final SOSMsgJOE	JOE_JobAssistent_MonthDayTabItem					= new SOSMsgJOE("JOE_JobAssistent_MonthDayTabItem");
	@I18NMsg
	public static final SOSMsgJOE	JOE_Cbo_JobAssistent_Month							= new SOSMsgJOE("JOE_Cbo_JobAssistent_Month");
	@I18NMsg
	public static final SOSMsgJOE	JOE_JobAssistent_SpecificDayTabItem					= new SOSMsgJOE("JOE_JobAssistent_SpecificDayTabItem");
	@I18NMsg
	public static final SOSMsgJOE	JOE_B_JobAssistent_AddPeriod						= new SOSMsgJOE("JOE_B_JobAssistent_AddPeriod");
	@I18NMsg
	public static final SOSMsgJOE	JOE_JobAssistent_PeriodList							= new SOSMsgJOE("JOE_JobAssistent_PeriodList");
	@I18NMsg
	public static final SOSMsgJOE	JOE_B_JobAssistentPeriodForms_Remove				= new SOSMsgJOE("JOE_B_JobAssistentPeriodForms_Remove");
	@I18NMsg
	public static final SOSMsgJOE	JOE_B_JobAssistentPeriodForms_Apply					= new SOSMsgJOE("JOE_B_JobAssistentPeriodForms_Apply");
	@I18NMsg
	public static final SOSMsgJOE	JOE_M_JobAssistent_EveryDay							= new SOSMsgJOE("JOE_M_JobAssistent_EveryDay");
	@I18NMsg
	public static final SOSMsgJOE	JOE_M_0027											= new SOSMsgJOE("JOE_M_0027");
	@I18NMsg
	public static final SOSMsgJOE	JOE_M_JobAssistent_NoWeekdaySelected				= new SOSMsgJOE("JOE_M_JobAssistent_NoWeekdaySelected");
	@I18NMsg
	public static final SOSMsgJOE	JOE_M_0028											= new SOSMsgJOE("JOE_M_0028");
	@I18NMsg
	public static final SOSMsgJOE	JOE_M_JobAssistent_SpecificDay						= new SOSMsgJOE("JOE_M_JobAssistent_SpecificDay");
	@I18NMsg
	public static final SOSMsgJOE	JOE_M_JobAssistent_Weekday							= new SOSMsgJOE("JOE_M_JobAssistent_Weekday");
	@I18NMsg
	public static final SOSMsgJOE	JOE_M_JobAssistent_Monthday							= new SOSMsgJOE("JOE_M_JobAssistent_Monthday");
	@I18NMsg
	public static final SOSMsgJOE	JOE_M_JobAssistent_SpecificWeekday					= new SOSMsgJOE("JOE_M_JobAssistent_SpecificWeekday");
	@I18NMsg
	public static final SOSMsgJOE	JOE_M_0029											= new SOSMsgJOE("JOE_M_0029");
	@I18NMsg
	public static final SOSMsgJOE	JOE_M_0030											= new SOSMsgJOE("JOE_M_0030");
	@I18NMsg
	public static final SOSMsgJOE	JOE_M_JobAssistent_At								= new SOSMsgJOE("JOE_M_JobAssistent_At");

	// JobAssistentProcessForms
	@I18NMsg
	public static final SOSMsgJOE	JOE_M_JobAssistent_Step5of9							= new SOSMsgJOE("JOE_M_JobAssistent_Step5of9");
	@I18NMsg
	public static final SOSMsgJOE	JOE_M_JobAssistent_Step5of8							= new SOSMsgJOE("JOE_M_JobAssistent_Step5of8");
	@I18NMsg
	public static final SOSMsgJOE	JOE_JobAssistent_FileLabel							= new SOSMsgJOE("JOE_JobAssistent_FileLabel");
	@I18NMsg
	public static final SOSMsgJOE	JOE_JobAssistent_FileText							= new SOSMsgJOE("JOE_JobAssistent_FileText");
	@I18NMsg
	public static final SOSMsgJOE	JOE_JobAssistent_ParameterLabel						= new SOSMsgJOE("JOE_JobAssistent_ParameterLabel");
	@I18NMsg
	public static final SOSMsgJOE	JOE_JobAssistent_ParameterText						= new SOSMsgJOE("JOE_JobAssistent_ParameterText");
	@I18NMsg
	public static final SOSMsgJOE	JOE_JobAssistent_LogFileLabel						= new SOSMsgJOE("JOE_JobAssistent_LogFileLabel");
	@I18NMsg
	public static final SOSMsgJOE	JOE_JobAssistent_LogFileText						= new SOSMsgJOE("JOE_JobAssistent_LogFileText");
	@I18NMsg
	public static final SOSMsgJOE	JOE_M_JobAssistentProcessForms_Execute				= new SOSMsgJOE("JOE_M_JobAssistentProcessForms_Execute");

	// JobAssistentRunOptionsForm
	@I18NMsg
	public static final SOSMsgJOE	JOE_M_JobAssistent_Step7of9							= new SOSMsgJOE("JOE_M_JobAssistent_Step7of9");
	@I18NMsg
	public static final SOSMsgJOE	JOE_M_JobAssistent_Step7of8							= new SOSMsgJOE("JOE_M_JobAssistent_Step7of8");
	@I18NMsg
	public static final SOSMsgJOE	JOE_M_JobAssistent_RunOptions						= new SOSMsgJOE("JOE_M_JobAssistent_RunOptions");
	@I18NMsg
	public static final SOSMsgJOE	JOE_B_JobAssistent_Period							= new SOSMsgJOE("JOE_B_JobAssistent_Period");
	@I18NMsg
	public static final SOSMsgJOE	JOE_B_JobAssistent_RunTime							= new SOSMsgJOE("JOE_B_JobAssistent_RunTime");
	@I18NMsg
	public static final SOSMsgJOE	JOE_B_JobAssistent_DirectoryMonitoring				= new SOSMsgJOE("JOE_B_JobAssistent_DirectoryMonitoring");

	// JobAssistentRunTimeForms
	@I18NMsg
	public static final SOSMsgJOE	JOE_M_JobAssistent_RunTimeSingleStarts				= new SOSMsgJOE("JOE_M_JobAssistent_RunTimeSingleStarts");
	@I18NMsg
	public static final SOSMsgJOE	JOE_B_JobAssistent_EveryDay							= new SOSMsgJOE("JOE_B_JobAssistent_EveryDay");
	@I18NMsg
	public static final SOSMsgJOE	JOE_L_JobAssistent_At								= new SOSMsgJOE("JOE_L_JobAssistent_At");
	@I18NMsg
	public static final SOSMsgJOE	JOE_B_JobAssistent_SpecificDay						= new SOSMsgJOE("JOE_B_JobAssistent_SpecificDay");
	@I18NMsg
	public static final SOSMsgJOE	JOE_JobAssistent_SpecificDayDateTime				= new SOSMsgJOE("JOE_JobAssistent_SpecificDayDateTime");
	@I18NMsg
	public static final SOSMsgJOE	JOE_B_JobAssistent_WeekDay							= new SOSMsgJOE("JOE_B_JobAssistent_WeekDay");
	@I18NMsg
	public static final SOSMsgJOE	JOE_Cbo_JobAssistent_WeekDayCombo					= new SOSMsgJOE("JOE_Cbo_JobAssistent_WeekDayCombo");
	@I18NMsg
	public static final SOSMsgJOE	JOE_B_JobAssistent_MonthDay							= new SOSMsgJOE("JOE_B_JobAssistent_MonthDay");
	@I18NMsg
	public static final SOSMsgJOE	JOE_Cbo_JobAssistent_MonthCombo						= new SOSMsgJOE("JOE_Cbo_JobAssistent_MonthCombo");
	@I18NMsg
	public static final SOSMsgJOE	JOE_B_JobAssistentRunTimeForms_Add					= new SOSMsgJOE("JOE_B_JobAssistentRunTimeForms_Add");
	@I18NMsg
	public static final SOSMsgJOE	JOE_B_JobAssistentRunTimeForms_Remove				= new SOSMsgJOE("JOE_B_JobAssistentRunTimeForms_Remove");
	@I18NMsg
	public static final SOSMsgJOE	JOE_B_JobAssistentRunTimeForms_Apply				= new SOSMsgJOE("JOE_B_JobAssistentRunTimeForms_Apply");
	@I18NMsg
	public static final SOSMsgJOE	JOE_M_JobAssistent_PeriodExists						= new SOSMsgJOE("JOE_M_JobAssistent_PeriodExists");
	@I18NMsg
	public static final SOSMsgJOE	JOE_M_0031											= new SOSMsgJOE("JOE_M_0031");
	@I18NMsg
	public static final SOSMsgJOE	JOE_M_JobAssistent_NoTime							= new SOSMsgJOE("JOE_M_JobAssistent_NoTime");
	@I18NMsg
	public static final SOSMsgJOE	JOE_T_JobAssistent_AtHour							= new SOSMsgJOE("JOE_T_JobAssistent_AtHour");
	@I18NMsg
	public static final SOSMsgJOE	JOE_T_JobAssistent_AtMinute							= new SOSMsgJOE("JOE_T_JobAssistent_AtMinute");
	@I18NMsg
	public static final SOSMsgJOE	JOE_T_JobAssistent_AtSecond							= new SOSMsgJOE("JOE_T_JobAssistent_AtSecond");

	// JobAssistentScriptForms
	@I18NMsg
	public static final SOSMsgJOE	JOE_M_JobAssistent_Script							= new SOSMsgJOE("JOE_M_JobAssistent_Script");
	@I18NMsg
	public static final SOSMsgJOE	JOE_T_JobAssistent_Language							= new SOSMsgJOE("JOE_T_JobAssistent_Language");
	@I18NMsg
	public static final SOSMsgJOE	JOE_M_JobAssistent_UnknownLanguage					= new SOSMsgJOE("JOE_M_JobAssistent_UnknownLanguage");
	@I18NMsg
	public static final SOSMsgJOE	JOE_L_JobAssistent_ComClass							= new SOSMsgJOE("JOE_L_JobAssistent_ComClass");
	@I18NMsg
	public static final SOSMsgJOE	JOE_L_JobAssistent_JavaClass						= new SOSMsgJOE("JOE_L_JobAssistent_JavaClass");
	@I18NMsg
	public static final SOSMsgJOE	JOE_T_JobAssistent_JavaClass						= new SOSMsgJOE("JOE_T_JobAssistent_JavaClass");
	@I18NMsg
	public static final SOSMsgJOE	JOE_L_JobAssistent_FileName							= new SOSMsgJOE("JOE_L_JobAssistent_FileName");
	@I18NMsg
	public static final SOSMsgJOE	JOE_L_JobAssistent_Resource							= new SOSMsgJOE("JOE_L_JobAssistent_Resource");
	@I18NMsg
	public static final SOSMsgJOE	JOE_T_JobAssistent_Resource							= new SOSMsgJOE("JOE_T_JobAssistent_Resource");
	@I18NMsg
	public static final SOSMsgJOE	JOE_Tbl_JobAssistent_Include						= new SOSMsgJOE("JOE_Tbl_JobAssistent_Include");

	// JobAssistentTaskForms
	@I18NMsg
	public static final SOSMsgJOE	JOE_M_JobAssistent_Step4of9							= new SOSMsgJOE("JOE_M_JobAssistent_Step4of9");
	@I18NMsg
	public static final SOSMsgJOE	JOE_M_JobAssistent_Step4of8							= new SOSMsgJOE("JOE_M_JobAssistent_Step4of8");
	@I18NMsg
	public static final SOSMsgJOE	JOE_M_JobAssistent_Tasks							= new SOSMsgJOE("JOE_M_JobAssistent_Tasks");
	@I18NMsg
	public static final SOSMsgJOE	JOE_L_JobAssistent_Tasks							= new SOSMsgJOE("JOE_L_JobAssistent_Tasks");
	@I18NMsg
	public static final SOSMsgJOE	JOE_T_JobAssistent_Tasks							= new SOSMsgJOE("JOE_T_JobAssistent_Tasks");
	@I18NMsg
	public static final SOSMsgJOE	JOE_L_JobAssistent_MinimumTasks						= new SOSMsgJOE("JOE_L_JobAssistent_MinimumTasks");
	@I18NMsg
	public static final SOSMsgJOE	JOE_T_JobAssistent_MinimumTasks						= new SOSMsgJOE("JOE_T_JobAssistent_MinimumTasks");
	@I18NMsg
	public static final SOSMsgJOE	JOE_M_JobAssistent_MinTasksTooLarge					= new SOSMsgJOE("JOE_M_JobAssistent_MinTasksTooLarge");

	// JobAssistentTimeoutForms
	@I18NMsg
	public static final SOSMsgJOE	JOE_M_JobAssistent_Step6of9							= new SOSMsgJOE("JOE_M_JobAssistent_Step6of9");
	@I18NMsg
	public static final SOSMsgJOE	JOE_M_JobAssistent_Step6of8							= new SOSMsgJOE("JOE_M_JobAssistent_Step6of8");
	@I18NMsg
	public static final SOSMsgJOE	JOE_M_JobAssistent_Timeout							= new SOSMsgJOE("JOE_M_JobAssistent_Timeout");
	@I18NMsg
	public static final SOSMsgJOE	JOE_L_JobAssistent_Timeout							= new SOSMsgJOE("JOE_L_JobAssistent_Timeout");
	@I18NMsg
	public static final SOSMsgJOE	JOE_T_JobAssistent_Timeout							= new SOSMsgJOE("JOE_T_JobAssistent_Timeout");

	// JobAssistentTimeoutOrderForms
	@I18NMsg
	public static final SOSMsgJOE	JOE_L_JobAssistent_IdleTimeout						= new SOSMsgJOE("JOE_L_JobAssistent_IdleTimeout");
	@I18NMsg
	public static final SOSMsgJOE	JOE_T_JobAssistent_IdleTimeout						= new SOSMsgJOE("JOE_T_JobAssistent_IdleTimeout");
	@I18NMsg
	public static final SOSMsgJOE	JOE_L_JobAssistent_ForceIdleTimeout					= new SOSMsgJOE("JOE_L_JobAssistent_ForceIdleTimeout");
	@I18NMsg
	public static final SOSMsgJOE	JOE_B_JobAssistent_NoButton							= new SOSMsgJOE("JOE_B_JobAssistent_NoButton");
	@I18NMsg
	public static final SOSMsgJOE	JOE_B_JobAssistent_YesButton						= new SOSMsgJOE("JOE_B_JobAssistent_YesButton");

	// JobAssistentTypeForms
	@I18NMsg
	public static final SOSMsgJOE	JOE_M_JobAssistent_JobType							= new SOSMsgJOE("JOE_M_JobAssistent_JobType");
	@I18NMsg
	public static final SOSMsgJOE	JOE_M_JobAssistent_Step1							= new SOSMsgJOE("JOE_M_JobAssistent_Step1");
	@I18NMsg
	public static final SOSMsgJOE	JOE_B_JobAssistent_OrderJob							= new SOSMsgJOE("JOE_B_JobAssistent_OrderJob");
	@I18NMsg
	public static final SOSMsgJOE	JOE_B_JobAssistent_StandaloneJob					= new SOSMsgJOE("JOE_B_JobAssistent_StandaloneJob");

	// JobChainConfigurationForm
	@I18NMsg
	public static final SOSMsgJOE	JOE_G_JobAssistent_JobChainConfiguration			= new SOSMsgJOE("JOE_G_JobAssistent_JobChainConfiguration");
	@I18NMsg
	public static final SOSMsgJOE	JOE_M_JobAssistent_State							= new SOSMsgJOE("JOE_M_JobAssistent_State");
	@I18NMsg
	public static final SOSMsgJOE	JOE_JobAssistent_JobChain							= new SOSMsgJOE("JOE_JobAssistent_JobChain");
	@I18NMsg
	public static final SOSMsgJOE	JOE_FD_JobAssistent_OpenFile						= new SOSMsgJOE("JOE_FD_JobAssistent_OpenFile");
	@I18NMsg
	public static final SOSMsgJOE	JOE_M_JobAssistent_FileIsOpened						= new SOSMsgJOE("JOE_M_JobAssistent_FileIsOpened");
	@I18NMsg
	public static final SOSMsgJOE	JOE_M_JobAssistent_FileNotFound						= new SOSMsgJOE("JOE_M_JobAssistent_FileNotFound");
	@I18NMsg
	public static final SOSMsgJOE	JOE_M_JobAssistent_FileReadProtected				= new SOSMsgJOE("JOE_M_JobAssistent_FileReadProtected");
	@I18NMsg
	public static final SOSMsgJOE	JOE_M_JobAssistent_JobDetailsEditor					= new SOSMsgJOE("JOE_M_JobAssistent_JobDetailsEditor");

	// JobChainForm
	@I18NMsg
	public static final SOSMsgJOE	JOE_M_JobChainForm_JobChain							= new SOSMsgJOE("JOE_M_JobChainForm_JobChain");
	@I18NMsg
	public static final SOSMsgJOE	JOE_L_JobChainForm_ChainName						= new SOSMsgJOE("JOE_L_JobChainForm_ChainName");
	@I18NMsg
	public static final SOSMsgJOE	JOE_T_JobChainForm_ChainName						= new SOSMsgJOE("JOE_T_JobChainForm_ChainName");
	@I18NMsg
	public static final SOSMsgJOE	JOE_B_JobChainForm_Parameter						= new SOSMsgJOE("JOE_B_JobChainForm_Parameter");
	@I18NMsg
	public static final SOSMsgJOE	JOE_M_JobChainForm_SaveChain						= new SOSMsgJOE("JOE_M_JobChainForm_SaveChain");
	@I18NMsg
	public static final SOSMsgJOE	JOE_M_JobChainForm_ChainNameChanged					= new SOSMsgJOE("JOE_M_JobChainForm_ChainNameChanged");
	@I18NMsg
	public static final SOSMsgJOE	JOE_L_JobChainForm_Title							= new SOSMsgJOE("JOE_L_JobChainForm_Title");
	@I18NMsg
	public static final SOSMsgJOE	JOE_T_JobChainForm_Title							= new SOSMsgJOE("JOE_T_JobChainForm_Title");
    @I18NMsg
    public static final SOSMsgJOE   JOE_L_JobChainForm_MaxOrders                        = new SOSMsgJOE("JOE_L_JobChainForm_MaxOrders");
    @I18NMsg
    public static final SOSMsgJOE   JOE_T_JobChainForm_MaxOrders                        = new SOSMsgJOE("JOE_T_JobChainForm_MaxOrders");
    @I18NMsg
    public static final SOSMsgJOE   JOE_L_JobChainForm_ProcessClass                     = new SOSMsgJOE("JOE_L_JobChainForm_ProcessClass");
    @I18NMsg
    public static final SOSMsgJOE   JOE_T_JobChainForm_ProcessClass                     = new SOSMsgJOE("JOE_T_JobChainForm_ProcessClass");
	@I18NMsg
	public static final SOSMsgJOE	JOE_B_JobChainForm_Recoverable						= new SOSMsgJOE("JOE_B_JobChainForm_Recoverable");
	@I18NMsg
	public static final SOSMsgJOE	JOE_B_JobChainForm_Distributed						= new SOSMsgJOE("JOE_B_JobChainForm_Distributed");
	@I18NMsg
	public static final SOSMsgJOE	JOE_B_JobChainForm_Visible							= new SOSMsgJOE("JOE_B_JobChainForm_Visible");

	// JobChainNestedNodesForm
	@I18NMsg
	public static final SOSMsgJOE	JOE_M_JCNestedNodesForm_NestedNodes					= new SOSMsgJOE("JOE_M_JCNestedNodesForm_NestedNodes");
	@I18NMsg
	public static final SOSMsgJOE	JOE_L_JobChainNodes_State							= new SOSMsgJOE("JOE_L_JobChainNodes_State");
	@I18NMsg
	public static final SOSMsgJOE	JOE_T_JobChainNodes_State							= new SOSMsgJOE("JOE_T_JobChainNodes_State");
	@I18NMsg
	public static final SOSMsgJOE	JOE_B_JobChainNodes_ApplyNode						= new SOSMsgJOE("JOE_B_JobChainNodes_ApplyNode");
	@I18NMsg
	public static final SOSMsgJOE	JOE_L_JCNestedNodesForm_JobChain					= new SOSMsgJOE("JOE_L_JCNestedNodesForm_JobChain");
	@I18NMsg
	public static final SOSMsgJOE	JOE_B_JobChainNodes_Goto							= new SOSMsgJOE("JOE_B_JobChainNodes_Goto");
	@I18NMsg
	public static final SOSMsgJOE	JOE_Cbo_JCNestedNodesForm_JobChain					= new SOSMsgJOE("JOE_Cbo_JCNestedNodesForm_JobChain");
	@I18NMsg
	public static final SOSMsgJOE	JOE_B_JobChainNodes_Browse							= new SOSMsgJOE("JOE_B_JobChainNodes_Browse");
	@I18NMsg
	public static final SOSMsgJOE	JOE_L_JobChainNodes_NextState						= new SOSMsgJOE("JOE_L_JobChainNodes_NextState");
	@I18NMsg
	public static final SOSMsgJOE	JOE_Cbo_JobChainNodes_NextState						= new SOSMsgJOE("JOE_Cbo_JobChainNodes_NextState");
	@I18NMsg
	public static final SOSMsgJOE	JOE_B_JCNestedNodesForm_NewNode						= new SOSMsgJOE("JOE_B_JCNestedNodesForm_NewNode");
	@I18NMsg
	public static final SOSMsgJOE	JOE_L_JobChainNodes_ErrorState						= new SOSMsgJOE("JOE_L_JobChainNodes_ErrorState");
	@I18NMsg
	public static final SOSMsgJOE	JOE_Cbo_JobChainNodes_ErrorState					= new SOSMsgJOE("JOE_Cbo_JobChainNodes_ErrorState");
	@I18NMsg
	public static final SOSMsgJOE	JOE_B_JCNestedNodesForm_Insert						= new SOSMsgJOE("JOE_B_JCNestedNodesForm_Insert");
	@I18NMsg
	public static final SOSMsgJOE	JOE_B_JCNestedNodesForm_FullNode					= new SOSMsgJOE("JOE_B_JCNestedNodesForm_FullNode");
	@I18NMsg
	public static final SOSMsgJOE	JOE_B_JCNestedNodesForm_EndNode						= new SOSMsgJOE("JOE_B_JCNestedNodesForm_EndNode");
	@I18NMsg
	public static final SOSMsgJOE	JOE_Tbl_JCNestedNodesForm_Nodes						= new SOSMsgJOE("JOE_Tbl_JCNestedNodesForm_Nodes");
	@I18NMsg
	public static final SOSMsgJOE	JOE_TCl_JCNestedNodesForm_State						= new SOSMsgJOE("JOE_TCl_JCNestedNodesForm_State");
	@I18NMsg
	public static final SOSMsgJOE	JOE_TCl_JCNestedNodesForm_Node						= new SOSMsgJOE("JOE_TCl_JCNestedNodesForm_Node");
	@I18NMsg
	public static final SOSMsgJOE	JOE_TCl_JCNestedNodesForm_JobChain					= new SOSMsgJOE("JOE_TCl_JCNestedNodesForm_JobChain");
	@I18NMsg
	public static final SOSMsgJOE	JOE_TCl_JCNestedNodesForm_NextState					= new SOSMsgJOE("JOE_TCl_JCNestedNodesForm_NextState");
	@I18NMsg
	public static final SOSMsgJOE	JOE_TCl_JCNestedNodesForm_ErrorState				= new SOSMsgJOE("JOE_TCl_JCNestedNodesForm_ErrorState");
	@I18NMsg
	public static final SOSMsgJOE	JOE_TCl_JCNestedNodesForm_OnError					= new SOSMsgJOE("JOE_TCl_JCNestedNodesForm_OnError");
	@I18NMsg
	public static final SOSMsgJOE	JOE_B_Up											= new SOSMsgJOE("JOE_B_Up");
	@I18NMsg
	public static final SOSMsgJOE	JOE_B_Down											= new SOSMsgJOE("JOE_B_Down");
	@I18NMsg
	public static final SOSMsgJOE	JOE_B_JCNestedNodesForm_Reorder						= new SOSMsgJOE("JOE_B_JCNestedNodesForm_Reorder");
	@I18NMsg
	public static final SOSMsgJOE	JOE_B_JCNestedNodesForm_AddMissingNodes				= new SOSMsgJOE("JOE_B_JCNestedNodesForm_AddMissingNodes");
	@I18NMsg
	public static final SOSMsgJOE	JOE_B_JCNestedNodesForm_RemoveNode					= new SOSMsgJOE("JOE_B_JCNestedNodesForm_RemoveNode");
	@I18NMsg
	public static final SOSMsgJOE	JOE_M_JCNestedNodesForm_RemoveNode					= new SOSMsgJOE("JOE_M_JCNestedNodesForm_RemoveNode");
	@I18NMsg
	public static final SOSMsgJOE	JOE_M_JobChain_StateAlreadyDefined					= new SOSMsgJOE("JOE_M_JobChain_StateAlreadyDefined");

	// JobChainNodesForm
 	@I18NMsg
	public static final SOSMsgJOE	JOE_L_JCNodesForm_Max								= new SOSMsgJOE("JOE_L_JCNodesForm_Max");
	@I18NMsg
	public static final SOSMsgJOE	JOE_T_JCNodesForm_Max								= new SOSMsgJOE("JOE_T_JCNodesForm_Max");
	@I18NMsg
	public static final SOSMsgJOE	JOE_M_JCNodesForm_NodesGroup						= new SOSMsgJOE("JOE_M_JCNodesForm_NodesGroup");
	@I18NMsg
	public static final SOSMsgJOE	JOE_L_JCNodesForm_Job								= new SOSMsgJOE("JOE_L_JCNodesForm_Job");
	@I18NMsg
	public static final SOSMsgJOE	JOE_Cbo_JCNodesForm_Job								= new SOSMsgJOE("JOE_Cbo_JCNodesForm_Job");
	@I18NMsg
	public static final SOSMsgJOE	JOE_L_JCNodesForm_Delay								= new SOSMsgJOE("JOE_L_JCNodesForm_Delay");
	@I18NMsg
	public static final SOSMsgJOE	JOE_T_JCNodesForm_Delay								= new SOSMsgJOE("JOE_T_JCNodesForm_Delay");
	@I18NMsg
	public static final SOSMsgJOE	JOE_B_JCNodesForm_ImportJob							= new SOSMsgJOE("JOE_B_JCNodesForm_ImportJob");
	@I18NMsg
	public static final SOSMsgJOE	JOE_L_JCNodesForm_OnError							= new SOSMsgJOE("JOE_L_JCNodesForm_OnError");
	@I18NMsg
	public static final SOSMsgJOE	JOE_Cbo_JCNodesForm_OnError							= new SOSMsgJOE("JOE_Cbo_JCNodesForm_OnError");
	@I18NMsg
	public static final SOSMsgJOE	JOE_M_JCNodesForm_Setback							= new SOSMsgJOE("JOE_M_JCNodesForm_Setback");
	@I18NMsg
	public static final SOSMsgJOE	JOE_M_JCNodesForm_Suspend							= new SOSMsgJOE("JOE_M_JCNodesForm_Suspend");
	@I18NMsg
	public static final SOSMsgJOE	JOE_B_JCNodesForm_NewNode							= new SOSMsgJOE("JOE_B_JCNodesForm_NewNode");
	@I18NMsg
	public static final SOSMsgJOE	JOE_B_JCNodesForm_FullNode							= new SOSMsgJOE("JOE_B_JCNodesForm_FullNode");
	@I18NMsg
	public static final SOSMsgJOE	JOE_B_JCNodesForm_EndNode							= new SOSMsgJOE("JOE_B_JCNodesForm_EndNode");
	@I18NMsg
	public static final SOSMsgJOE	JOE_B_JCNodesForm_FileSink							= new SOSMsgJOE("JOE_B_JCNodesForm_FileSink");
	@I18NMsg
	public static final SOSMsgJOE	JOE_L_JCNodesForm_RemoveFile						= new SOSMsgJOE("JOE_L_JCNodesForm_RemoveFile");
	@I18NMsg
	public static final SOSMsgJOE	JOE_B_JCNodesForm_RemoveFile						= new SOSMsgJOE("JOE_B_JCNodesForm_RemoveFile");
	@I18NMsg
	public static final SOSMsgJOE	JOE_L_JCNodesForm_MoveTo							= new SOSMsgJOE("JOE_L_JCNodesForm_MoveTo");
	@I18NMsg
	public static final SOSMsgJOE	JOE_T_JCNodesForm_MoveTo							= new SOSMsgJOE("JOE_T_JCNodesForm_MoveTo");
	@I18NMsg
	public static final SOSMsgJOE	JOE_B_JCNodesForm_Insert							= new SOSMsgJOE("JOE_B_JCNodesForm_Insert");
	@I18NMsg
	public static final SOSMsgJOE	JOE_Tbl_JCNodesForm_Nodes							= new SOSMsgJOE("JOE_Tbl_JCNodesForm_Nodes");
	@I18NMsg
	public static final SOSMsgJOE	JOE_TCl_JCNodesForm_State							= new SOSMsgJOE("JOE_TCl_JCNodesForm_State");
	@I18NMsg
	public static final SOSMsgJOE	JOE_TCl_JCNodesForm_Node							= new SOSMsgJOE("JOE_TCl_JCNodesForm_Node");
	@I18NMsg
	public static final SOSMsgJOE	JOE_TCl_JCNodesForm_JobDir							= new SOSMsgJOE("JOE_TCl_JCNodesForm_JobDir");
	@I18NMsg
	public static final SOSMsgJOE	JOE_TCl_JCNodesForm_NextState						= new SOSMsgJOE("JOE_TCl_JCNodesForm_NextState");
	@I18NMsg
	public static final SOSMsgJOE	JOE_TCl_JCNodesForm_ErrorState						= new SOSMsgJOE("JOE_TCl_JCNodesForm_ErrorState");
	@I18NMsg
	public static final SOSMsgJOE	JOE_TCl_JCNodesForm_OnError							= new SOSMsgJOE("JOE_TCl_JCNodesForm_OnError");
	@I18NMsg
	public static final SOSMsgJOE	JOE_B_JCNodesForm_Reorder							= new SOSMsgJOE("JOE_B_JCNodesForm_Reorder");
	@I18NMsg
	public static final SOSMsgJOE	JOE_B_JCNodesForm_Details							= new SOSMsgJOE("JOE_B_JCNodesForm_Details");
	@I18NMsg
	public static final SOSMsgJOE	JOE_B_JCNodesForm_AddMissingNodes					= new SOSMsgJOE("JOE_B_JCNodesForm_AddMissingNodes");
	@I18NMsg
    public static final SOSMsgJOE   JOE_B_JCNodesForm_ReturnCodes                        = new SOSMsgJOE("JOE_B_JCNodesForm_ReturnCode");
    @I18NMsg
    public static final SOSMsgJOE   JOE_B_JCNodesForm_Remove                            = new SOSMsgJOE("JOE_B_JCNodesForm_Remove");
	@I18NMsg
	public static final SOSMsgJOE	JOE_M_JCNodesForm_Remove							= new SOSMsgJOE("JOE_M_JCNodesForm_Remove");
	@I18NMsg
	public static final SOSMsgJOE	JOE_G_JCNodesForm_FileOrderSources					= new SOSMsgJOE("JOE_G_JCNodesForm_FileOrderSources");
	@I18NMsg
	public static final SOSMsgJOE	JOE_L_JCNodesForm_Directory							= new SOSMsgJOE("JOE_L_JCNodesForm_Directory");
	@I18NMsg
	public static final SOSMsgJOE	JOE_T_JCNodesForm_Directory							= new SOSMsgJOE("JOE_T_JCNodesForm_Directory");
	@I18NMsg
	public static final SOSMsgJOE	JOE_L_JCNodesForm_DelayAfterError					= new SOSMsgJOE("JOE_L_JCNodesForm_DelayAfterError");
	@I18NMsg
	public static final SOSMsgJOE	JOE_T_JCNodesForm_DelayAfterError					= new SOSMsgJOE("JOE_T_JCNodesForm_DelayAfterError");
	@I18NMsg
	public static final SOSMsgJOE	JOE_B_JCNodesForm_ApplyFileOrderSource				= new SOSMsgJOE("JOE_B_JCNodesForm_ApplyFileOrderSource");
    @I18NMsg
    public static final SOSMsgJOE   JOE_L_JCNodesForm_Regex                             = new SOSMsgJOE("JOE_L_JCNodesForm_Regex");
    @I18NMsg
    public static final SOSMsgJOE   JOE_L_JCNodesForm_AlertWhenDirectoryMissing         = new SOSMsgJOE("JOE_L_JCNodesForm_AlertWhenDirectoryMissing");
	@I18NMsg
	public static final SOSMsgJOE	JOE_T_JCNodesForm_Regex								= new SOSMsgJOE("JOE_T_JCNodesForm_Regex");
	@I18NMsg
	public static final SOSMsgJOE	JOE_L_JCNodesForm_Repeat							= new SOSMsgJOE("JOE_L_JCNodesForm_Repeat");
	@I18NMsg
	public static final SOSMsgJOE	JOE_T_JCNodesForm_Repeat							= new SOSMsgJOE("JOE_T_JCNodesForm_Repeat");
	@I18NMsg
	public static final SOSMsgJOE	JOE_L_JCNodesForm_NextState							= new SOSMsgJOE("JOE_L_JCNodesForm_NextState");
	@I18NMsg
	public static final SOSMsgJOE	JOE_T_JCNodesForm_NextState							= new SOSMsgJOE("JOE_T_JCNodesForm_NextState");
	@I18NMsg
	public static final SOSMsgJOE	JOE_B_JCNodesForm_RemoveFileOrderSource				= new SOSMsgJOE("JOE_B_JCNodesForm_RemoveFileOrderSource");
	@I18NMsg
	public static final SOSMsgJOE	JOE_Tbl_JCNodesForm_FileOrderSource					= new SOSMsgJOE("JOE_Tbl_JCNodesForm_FileOrderSource");
	@I18NMsg
	public static final SOSMsgJOE	JOE_TCl_JCNodesForm_Directory						= new SOSMsgJOE("JOE_TCl_JCNodesForm_Directory");
	@I18NMsg
	public static final SOSMsgJOE	JOE_TCl_JCNodesForm_Regex							= new SOSMsgJOE("JOE_TCl_JCNodesForm_Regex");
	@I18NMsg
	public static final SOSMsgJOE	JOE_B_JCNodesForm_NewFileOrderSource				= new SOSMsgJOE("JOE_B_JCNodesForm_NewFileOrderSource");
	@I18NMsg
	public static final SOSMsgJOE	JOE_M_NoRegex										= new SOSMsgJOE("JOE_M_NoRegex");

	// JobChainsForm
	@I18NMsg
	public static final SOSMsgJOE	JOE_G_JobChainsForm_JobChains						= new SOSMsgJOE("JOE_G_JobChainsForm_JobChains");
	@I18NMsg
	public static final SOSMsgJOE	JOE_Tbl_JobChainsForm_JobChains						= new SOSMsgJOE("JOE_Tbl_JobChainsForm_JobChains");
	@I18NMsg
	public static final SOSMsgJOE	JOE_TCl_JobChainsForm_Name							= new SOSMsgJOE("JOE_TCl_JobChainsForm_Name");
	@I18NMsg
	public static final SOSMsgJOE	JOE_TCl_JobChainsForm_OrdersRecoverable				= new SOSMsgJOE("JOE_TCl_JobChainsForm_OrdersRecoverable");
	@I18NMsg
	public static final SOSMsgJOE	JOE_TCl_JobChainsForm_Visible						= new SOSMsgJOE("JOE_TCl_JobChainsForm_Visible");
	@I18NMsg
	public static final SOSMsgJOE	JOE_B_JobChainsForm_NewChain						= new SOSMsgJOE("JOE_B_JobChainsForm_NewChain");
	@I18NMsg
	public static final SOSMsgJOE	JOE_B_JobChainsForm_RemoveChain						= new SOSMsgJOE("JOE_B_JobChainsForm_RemoveChain");
	@I18NMsg
	public static final SOSMsgJOE	JOE_M_JobChainsForm_RemoveChain						= new SOSMsgJOE("JOE_M_JobChainsForm_RemoveChain");
	@I18NMsg
	public static final SOSMsgJOE	JOE_B_JobChainsForm_Details							= new SOSMsgJOE("JOE_B_JobChainsForm_Details");
	@I18NMsg
	public static final SOSMsgJOE	JOE_M_JobChain										= new SOSMsgJOE("JOE_M_JobChain");

	// JobCommandExitCodesForm
	@I18NMsg
	public static final SOSMsgJOE	JOE_M_JobCommand_Disabled							= new SOSMsgJOE("JOE_M_JobCommand_Disabled");
	@I18NMsg
	public static final SOSMsgJOE	JOE_G_JobCommands_Commands							= new SOSMsgJOE("JOE_G_JobCommands_Commands");
	@I18NMsg
	public static final SOSMsgJOE	JOE_Cbo_JobCommands_Exitcode						= new SOSMsgJOE("JOE_Cbo_JobCommands_Exitcode");
	@I18NMsg
	public static final SOSMsgJOE	JOE_B_JobCommands_AddJob							= new SOSMsgJOE("JOE_B_JobCommands_AddJob");
	@I18NMsg
	public static final SOSMsgJOE	JOE_B_JobCommands_AddOrder							= new SOSMsgJOE("JOE_B_JobCommands_AddOrder");
	@I18NMsg
	public static final SOSMsgJOE	JOE_L_JobCommands_ExitCodes							= new SOSMsgJOE("JOE_L_JobCommands_ExitCodes");
	@I18NMsg
	public static final SOSMsgJOE	JOE_Tbl_JobCommands_Commands						= new SOSMsgJOE("JOE_Tbl_JobCommands_Commands");
	@I18NMsg
	public static final SOSMsgJOE	JOE_TCl_JobCommands_Command							= new SOSMsgJOE("JOE_TCl_JobCommands_Command");
	@I18NMsg
	public static final SOSMsgJOE	JOE_TCl_JobCommands_JobID							= new SOSMsgJOE("JOE_TCl_JobCommands_JobID");
	@I18NMsg
	public static final SOSMsgJOE	JOE_TCl_JobCommands_JobChain						= new SOSMsgJOE("JOE_TCl_JobCommands_JobChain");
	@I18NMsg
	public static final SOSMsgJOE	JOE_TCl_JobCommands_StartAt							= new SOSMsgJOE("JOE_TCl_JobCommands_StartAt");
	@I18NMsg
	public static final SOSMsgJOE	JOE_B_JobCommands_Remove							= new SOSMsgJOE("JOE_B_JobCommands_Remove");
	@I18NMsg
	public static final SOSMsgJOE	JOE_M_JobCommand_CommandsForJob						= new SOSMsgJOE("JOE_M_JobCommand_CommandsForJob");
	@I18NMsg
	public static final SOSMsgJOE	JOE_G_JobCommand_JobsAndOrders						= new SOSMsgJOE("JOE_G_JobCommand_JobsAndOrders");
	@I18NMsg
	public static final SOSMsgJOE	JOE_L_JobCommand_JobChain							= new SOSMsgJOE("JOE_L_JobCommand_JobChain");
	@I18NMsg
	public static final SOSMsgJOE	JOE_Cbo_JobCommand_JobChain							= new SOSMsgJOE("JOE_Cbo_JobCommand_JobChain");
	@I18NMsg
	public static final SOSMsgJOE	JOE_L_JobCommand_JobOrderID							= new SOSMsgJOE("JOE_L_JobCommand_JobOrderID");
	@I18NMsg
	public static final SOSMsgJOE	JOE_L_JobCommand_Job								= new SOSMsgJOE("JOE_L_JobCommand_Job");
	@I18NMsg
	public static final SOSMsgJOE	JOE_T_JobCommand_Job								= new SOSMsgJOE("JOE_T_JobCommand_Job");
	@I18NMsg
	public static final SOSMsgJOE	JOE_L_JobCommand_StartAt							= new SOSMsgJOE("JOE_L_JobCommand_StartAt");
	@I18NMsg
	public static final SOSMsgJOE	JOE_T_JobCommand_StartAt							= new SOSMsgJOE("JOE_T_JobCommand_StartAt");
	@I18NMsg
	public static final SOSMsgJOE	JOE_L_JobCommand_Priority							= new SOSMsgJOE("JOE_L_JobCommand_Priority");
	@I18NMsg
	public static final SOSMsgJOE	JOE_T_JobCommand_Priority							= new SOSMsgJOE("JOE_T_JobCommand_Priority");
	@I18NMsg
	public static final SOSMsgJOE	JOE_L_JobCommand_Title								= new SOSMsgJOE("JOE_L_JobCommand_Title");
	@I18NMsg
	public static final SOSMsgJOE	JOE_T_JobCommand_Title								= new SOSMsgJOE("JOE_T_JobCommand_Title");
	@I18NMsg
	public static final SOSMsgJOE	JOE_L_JobCommand_OrderID							= new SOSMsgJOE("JOE_L_JobCommand_OrderID");
	@I18NMsg
	public static final SOSMsgJOE	JOE_L_JobCommand_State								= new SOSMsgJOE("JOE_L_JobCommand_State");
	@I18NMsg
	public static final SOSMsgJOE	JOE_T_JobCommand_State								= new SOSMsgJOE("JOE_T_JobCommand_State");
	@I18NMsg
	public static final SOSMsgJOE	JOE_L_JobCommand_EndState							= new SOSMsgJOE("JOE_L_JobCommand_EndState");
	@I18NMsg
	public static final SOSMsgJOE	JOE_Cbo_JobCommand_EndState							= new SOSMsgJOE("JOE_Cbo_JobCommand_EndState");
	@I18NMsg
	public static final SOSMsgJOE	JOE_L_JobCommand_Replace							= new SOSMsgJOE("JOE_L_JobCommand_Replace");
	@I18NMsg
	public static final SOSMsgJOE	JOE_B_JobCommand_Replace							= new SOSMsgJOE("JOE_B_JobCommand_Replace");

	// JobCommandsForm
	@I18NMsg
	public static final SOSMsgJOE	JOE_G_JobCommand_Commands							= new SOSMsgJOE("JOE_G_JobCommand_Commands");
	@I18NMsg
	public static final SOSMsgJOE	JOE_B_JobCommand_NewCommand							= new SOSMsgJOE("JOE_B_JobCommand_NewCommand");
	@I18NMsg
	public static final SOSMsgJOE	JOE_B_JobCommand_RemoveCommand						= new SOSMsgJOE("JOE_B_JobCommand_RemoveCommand");
	@I18NMsg
	public static final SOSMsgJOE	JOE_Tbl_JobCommand_Table							= new SOSMsgJOE("JOE_Tbl_JobCommand_Table");
	@I18NMsg
	public static final SOSMsgJOE	JOE_TCl_JobCommand_Exitcode							= new SOSMsgJOE("JOE_TCl_JobCommand_Exitcode");

	// JobLockUseForm
	@I18NMsg
	public static final SOSMsgJOE	JOE_G_JobLockUseForm_Use							= new SOSMsgJOE("JOE_G_JobLockUseForm_Use");
	@I18NMsg
	public static final SOSMsgJOE	JOE_L_JobLockUseForm_Lock							= new SOSMsgJOE("JOE_L_JobLockUseForm_Lock");
	@I18NMsg
	public static final SOSMsgJOE	JOE_Cbo_JobLockUseForm_LockUse						= new SOSMsgJOE("JOE_Cbo_JobLockUseForm_LockUse");
	@I18NMsg
	public static final SOSMsgJOE	JOE_B_JobLockUseForm_Exclusive						= new SOSMsgJOE("JOE_B_JobLockUseForm_Exclusive");
	@I18NMsg
	public static final SOSMsgJOE	JOE_B_JobLockUseForm_ApplyLockUse					= new SOSMsgJOE("JOE_B_JobLockUseForm_ApplyLockUse");
	@I18NMsg
	public static final SOSMsgJOE	JOE_L_JobLockUseForm_Exclusive						= new SOSMsgJOE("JOE_L_JobLockUseForm_Exclusive");
	@I18NMsg
	public static final SOSMsgJOE	JOE_Tbl_JobLockUseForm_LockUseTable					= new SOSMsgJOE("JOE_Tbl_JobLockUseForm_LockUseTable");
	@I18NMsg
	public static final SOSMsgJOE	JOE_TCl_JobLockUseForm_Lock							= new SOSMsgJOE("JOE_TCl_JobLockUseForm_Lock");
	@I18NMsg
	public static final SOSMsgJOE	JOE_TCl_JobLockUseForm_Exclusive					= new SOSMsgJOE("JOE_TCl_JobLockUseForm_Exclusive");
	@I18NMsg
	public static final SOSMsgJOE	JOE_B_JobLockUseForm_NewLockUse						= new SOSMsgJOE("JOE_B_JobLockUseForm_NewLockUse");
	@I18NMsg
	public static final SOSMsgJOE	JOE_B_JobLockUseForm_RemoveLockUse					= new SOSMsgJOE("JOE_B_JobLockUseForm_RemoveLockUse");
	@I18NMsg
	public static final SOSMsgJOE	JOE_B_JobLockUseForm_Browse							= new SOSMsgJOE("JOE_B_JobLockUseForm_Browse");

	
    // JobMonitorUseForm
    @I18NMsg
    public static final SOSMsgJOE   JOE_G_JobMonitorUseForm_Use                         = new SOSMsgJOE("JOE_G_JobMonitorUseForm_Use");
    @I18NMsg
    public static final SOSMsgJOE   JOE_L_JobMonitorUseForm_Monitor                     = new SOSMsgJOE("JOE_L_JobMonitorUseForm_Monitor");
    @I18NMsg
    public static final SOSMsgJOE   JOE_Cbo_JobMonitorUseForm_MonitorUse                = new SOSMsgJOE("JOE_Cbo_JobMonitorUseForm_MonitorUse");
    @I18NMsg
    public static final SOSMsgJOE   JOE_T_JobMonitorUseForm_Ordering                    = new SOSMsgJOE("JOE_B_JobMonitorUseForm_Ordering");
    @I18NMsg
    public static final SOSMsgJOE   JOE_B_JobMonitorUseForm_ApplyMonitorUse             = new SOSMsgJOE("JOE_B_JobMonitorUseForm_ApplyMonitorUse");
    @I18NMsg
    public static final SOSMsgJOE   JOE_L_JobMonitorUseForm_Ordering                    = new SOSMsgJOE("JOE_L_JobMonitorUseForm_Ordering");
    @I18NMsg
    public static final SOSMsgJOE   JOE_Tbl_JobMonitorUseForm_MonitorUseTable           = new SOSMsgJOE("JOE_Tbl_JobMonitorUseForm_MonitorTable");
    @I18NMsg
    public static final SOSMsgJOE   JOE_TCl_JobMonitorUseForm_Monitor                   = new SOSMsgJOE("JOE_TCl_JobMonitorUseForm_Monitor");
    @I18NMsg
    public static final SOSMsgJOE   JOE_TCl_JobMonitorUseForm_Ordering                  = new SOSMsgJOE("JOE_TCl_JobMonitorUseForm_Ordering");
    @I18NMsg
    public static final SOSMsgJOE   JOE_B_JobMonitorUseForm_NewMonitorUse               = new SOSMsgJOE("JOE_B_JobMonitorUseForm_NewMonitorUse");
    @I18NMsg
    public static final SOSMsgJOE   JOE_B_JobMonitorUseForm_RemoveMonitorUse            = new SOSMsgJOE("JOE_B_JobMonitorUseForm_RemoveMonitorUse");
    @I18NMsg
    public static final SOSMsgJOE   JOE_B_JobMonitorUseForm_Browse                      = new SOSMsgJOE("JOE_B_JobMonitorUseForm_Browse");

	
	
	
	// JobMainOptionForm
	@I18NMsg
	public static final SOSMsgJOE	JOE_M_JobMainOptionForm_MainOptions					= new SOSMsgJOE("JOE_M_JobMainOptionForm_MainOptions");
	@I18NMsg
	public static final SOSMsgJOE	JOE_L_JobMainOptionForm_SchedulerID					= new SOSMsgJOE("JOE_L_JobMainOptionForm_SchedulerID");
	@I18NMsg
	public static final SOSMsgJOE	JOE_T_JobMainOptionForm_SchedulerID					= new SOSMsgJOE("JOE_T_JobMainOptionForm_SchedulerID");
	@I18NMsg
	public static final SOSMsgJOE	JOE_L_JobMainOptionForm_JavaOptions					= new SOSMsgJOE("JOE_L_JobMainOptionForm_JavaOptions");
	@I18NMsg
	public static final SOSMsgJOE	JOE_T_JobMainOptionForm_JavaOptions					= new SOSMsgJOE("JOE_T_JobMainOptionForm_JavaOptions");
	@I18NMsg
	public static final SOSMsgJOE	JOE_L_JobMainOptionForm_IgnoreSignals				= new SOSMsgJOE("JOE_L_JobMainOptionForm_IgnoreSignals");
	@I18NMsg
	public static final SOSMsgJOE	JOE_T_JobMainOptionForm_IgnoreSignals				= new SOSMsgJOE("JOE_T_JobMainOptionForm_IgnoreSignals");
	@I18NMsg
	public static final SOSMsgJOE	JOE_B_JobMainOptionForm_Add							= new SOSMsgJOE("JOE_B_JobMainOptionForm_Add");
	@I18NMsg
	public static final SOSMsgJOE	JOE_Cbo_JobMainOptionForm_Signals					= new SOSMsgJOE("JOE_Cbo_JobMainOptionForm_Signals");
	@I18NMsg
	public static final SOSMsgJOE	JOE_L_JobMainOptionForm_Priority					= new SOSMsgJOE("JOE_L_JobMainOptionForm_Priority");
	@I18NMsg
	public static final SOSMsgJOE	JOE_Cbo_JobMainOptionForm_Priority					= new SOSMsgJOE("JOE_Cbo_JobMainOptionForm_Priority");
	@I18NMsg
	public static final SOSMsgJOE	JOE_L_JobMainOptionForm_Visible						= new SOSMsgJOE("JOE_L_JobMainOptionForm_Visible");
	@I18NMsg
	public static final SOSMsgJOE	JOE_Cbo_JobMainOptionForm_Visible					= new SOSMsgJOE("JOE_Cbo_JobMainOptionForm_Visible");
	@I18NMsg
	public static final SOSMsgJOE	JOE_L_JobMainOptionForm_MinTasks					= new SOSMsgJOE("JOE_L_JobMainOptionForm_MinTasks");
	@I18NMsg
	public static final SOSMsgJOE	JOE_T_JobMainOptionForm_MinTasks					= new SOSMsgJOE("JOE_T_JobMainOptionForm_MinTasks");
	@I18NMsg
	public static final SOSMsgJOE	JOE_L_JobMainOptionForm_Tasks						= new SOSMsgJOE("JOE_L_JobMainOptionForm_Tasks");
	@I18NMsg
	public static final SOSMsgJOE	JOE_T_JobMainOptionForm_Tasks						= new SOSMsgJOE("JOE_T_JobMainOptionForm_Tasks");
	@I18NMsg
	public static final SOSMsgJOE	JOE_L_JobMainOptionForm_Timeout						= new SOSMsgJOE("JOE_L_JobMainOptionForm_Timeout");
	@I18NMsg
	public static final SOSMsgJOE	JOE_T_JobMainOptionForm_Timeout						= new SOSMsgJOE("JOE_T_JobMainOptionForm_Timeout");
	@I18NMsg
	public static final SOSMsgJOE	JOE_L_JobMainOptionForm_IdleTimeout					= new SOSMsgJOE("JOE_L_JobMainOptionForm_IdleTimeout");
	@I18NMsg
	public static final SOSMsgJOE	JOE_T_JobMainOptionForm_IdleTimeout					= new SOSMsgJOE("JOE_T_JobMainOptionForm_IdleTimeout");
	@I18NMsg
	public static final SOSMsgJOE	JOE_L_JobMainOptionForm_IdleTimeoutFormat			= new SOSMsgJOE("JOE_L_JobMainOptionForm_IdleTimeoutFormat");
	@I18NMsg
	public static final SOSMsgJOE	JOE_L_JobMainOptionForm_WarnIfLonger				= new SOSMsgJOE("JOE_L_JobMainOptionForm_WarnIfLonger");
	@I18NMsg
	public static final SOSMsgJOE	JOE_T_JobMainOptionForm_WarnIfLonger				= new SOSMsgJOE("JOE_T_JobMainOptionForm_WarnIfLonger");
	@I18NMsg
	public static final SOSMsgJOE	JOE_L_JobMainOptionForm_WarnIfLongerFormat			= new SOSMsgJOE("JOE_L_JobMainOptionForm_WarnIfLongerFormat");
	@I18NMsg
	public static final SOSMsgJOE	JOE_L_JobMainOptionForm_WarnIfShorter				= new SOSMsgJOE("JOE_L_JobMainOptionForm_WarnIfShorter");
	@I18NMsg
	public static final SOSMsgJOE	JOE_T_JobMainOptionForm_WarnIfShorter				= new SOSMsgJOE("JOE_T_JobMainOptionForm_WarnIfShorter");
	@I18NMsg
	public static final SOSMsgJOE	JOE_L_JobMainOptionForm_WarnIfShorterFormat			= new SOSMsgJOE("JOE_L_JobMainOptionForm_WarnIfShorterFormat");
	@I18NMsg
	public static final SOSMsgJOE	JOE_L_JobMainOptionForm_ForceIdleTimeout			= new SOSMsgJOE("JOE_L_JobMainOptionForm_ForceIdleTimeout");
	@I18NMsg
	public static final SOSMsgJOE	JOE_B_JobMainOptionForm_ForceIdleTimeout			= new SOSMsgJOE("JOE_B_JobMainOptionForm_ForceIdleTimeout");

	// JobOptionsForm
	@I18NMsg
	public static final SOSMsgJOE	JOE_G_JobOptionsForm_RunOptions						= new SOSMsgJOE("JOE_G_JobOptionsForm_RunOptions");
	@I18NMsg
	public static final SOSMsgJOE	JOE_G_JobOptionsForm_StartWhenDirectoryChanged		= new SOSMsgJOE("JOE_G_JobOptionsForm_StartWhenDirectoryChanged");
	@I18NMsg
	public static final SOSMsgJOE	JOE_L_JobOptionsForm_WatchDirectory					= new SOSMsgJOE("JOE_L_JobOptionsForm_WatchDirectory");
	@I18NMsg
	public static final SOSMsgJOE	JOE_T_JobOptionsForm_WatchDirectory					= new SOSMsgJOE("JOE_T_JobOptionsForm_WatchDirectory");
	@I18NMsg
	public static final SOSMsgJOE	JOE_L_JobOptionsForm_FileRegex						= new SOSMsgJOE("JOE_L_JobOptionsForm_FileRegex");
	@I18NMsg
	public static final SOSMsgJOE	JOE_T_JobOptionsForm_FileRegex						= new SOSMsgJOE("JOE_T_JobOptionsForm_FileRegex");
	@I18NMsg
	public static final SOSMsgJOE	JOE_B_JobOptionsForm_ApplyDir						= new SOSMsgJOE("JOE_B_JobOptionsForm_ApplyDir");
	@I18NMsg
	public static final SOSMsgJOE	JOE_B_JobOptionsForm_NewDir							= new SOSMsgJOE("JOE_B_JobOptionsForm_NewDir");
	@I18NMsg
	public static final SOSMsgJOE	JOE_B_JobOptionsForm_RemoveDir						= new SOSMsgJOE("JOE_B_JobOptionsForm_RemoveDir");
	@I18NMsg
	public static final SOSMsgJOE	JOE_G_JobOptionsForm_DelayAfterError				= new SOSMsgJOE("JOE_G_JobOptionsForm_DelayAfterError");
	@I18NMsg
	public static final SOSMsgJOE	JOE_L_JobOptionsForm_ErrorCount						= new SOSMsgJOE("JOE_L_JobOptionsForm_ErrorCount");
	@I18NMsg
	public static final SOSMsgJOE	JOE_T_JobOptionsForm_ErrorCount						= new SOSMsgJOE("JOE_T_JobOptionsForm_ErrorCount");
	@I18NMsg
	public static final SOSMsgJOE	JOE_B_JobOptionsForm_Stop							= new SOSMsgJOE("JOE_B_JobOptionsForm_Stop");
	@I18NMsg
	public static final SOSMsgJOE	JOE_B_JobOptionsForm_Delay							= new SOSMsgJOE("JOE_B_JobOptionsForm_Delay");
	@I18NMsg
	public static final SOSMsgJOE	JOE_T_JobOptionsForm_ErrorHours						= new SOSMsgJOE("JOE_T_JobOptionsForm_ErrorHours");
	@I18NMsg
	public static final SOSMsgJOE	JOE_T_JobOptionsForm_ErrorMinutes					= new SOSMsgJOE("JOE_T_JobOptionsForm_ErrorMinutes");
	@I18NMsg
	public static final SOSMsgJOE	JOE_T_JobOptionsForm_ErrorSeconds					= new SOSMsgJOE("JOE_T_JobOptionsForm_ErrorSeconds");
	@I18NMsg
	public static final SOSMsgJOE	JOE_L_JobOptionsForm_DelayFormat					= new SOSMsgJOE("JOE_L_JobOptionsForm_DelayFormat");
	@I18NMsg
	public static final SOSMsgJOE	JOE_B_JobOptionsForm_ApplyDelay						= new SOSMsgJOE("JOE_B_JobOptionsForm_ApplyDelay");
	@I18NMsg
	public static final SOSMsgJOE	JOE_B_JobOptionsForm_NewDelayAfterError				= new SOSMsgJOE("JOE_B_JobOptionsForm_NewDelayAfterError");
	@I18NMsg
	public static final SOSMsgJOE	JOE_B_JobOptionsForm_RemoveDelay					= new SOSMsgJOE("JOE_B_JobOptionsForm_RemoveDelay");
	@I18NMsg
	public static final SOSMsgJOE	JOE_G_JobOptionsForm_DelayOrderAfterSetBack			= new SOSMsgJOE("JOE_G_JobOptionsForm_DelayOrderAfterSetBack");
	@I18NMsg
	public static final SOSMsgJOE	JOE_L_JobOptionsForm_SetBackCount					= new SOSMsgJOE("JOE_L_JobOptionsForm_SetBackCount");
	@I18NMsg
	public static final SOSMsgJOE	JOE_T_JobOptionsForm_SetBackCount					= new SOSMsgJOE("JOE_T_JobOptionsForm_SetBackCount");
	@I18NMsg
	public static final SOSMsgJOE	JOE_B_JobOptionsForm_IsMax							= new SOSMsgJOE("JOE_B_JobOptionsForm_IsMax");
	@I18NMsg
	public static final SOSMsgJOE	JOE_L_JobOptionsForm_Delay							= new SOSMsgJOE("JOE_L_JobOptionsForm_Delay");
	@I18NMsg
	public static final SOSMsgJOE	JOE_T_JobOptionsForm_SetBackHours					= new SOSMsgJOE("JOE_T_JobOptionsForm_SetBackHours");
	@I18NMsg
	public static final SOSMsgJOE	JOE_T_JobOptionsForm_SetBackMinutes					= new SOSMsgJOE("JOE_T_JobOptionsForm_SetBackMinutes");
	@I18NMsg
	public static final SOSMsgJOE	JOE_T_JobOptionsForm_SetBackSeconds					= new SOSMsgJOE("JOE_T_JobOptionsForm_SetBackSeconds");
	@I18NMsg
	public static final SOSMsgJOE	JOE_B_JobOptionsForm_ApplySetBack					= new SOSMsgJOE("JOE_B_JobOptionsForm_ApplySetBack");
	@I18NMsg
	public static final SOSMsgJOE	JOE_B_JobOptionsForm_NewSetBack						= new SOSMsgJOE("JOE_B_JobOptionsForm_NewSetBack");
	@I18NMsg
	public static final SOSMsgJOE	JOE_B_JobOptionsForm_RemoveSetback					= new SOSMsgJOE("JOE_B_JobOptionsForm_RemoveSetback");
	@I18NMsg
	public static final SOSMsgJOE	JOE_Tbl_JobOptionsForm_ErrorDelay					= new SOSMsgJOE("JOE_Tbl_JobOptionsForm_ErrorDelay");
	@I18NMsg
	public static final SOSMsgJOE	JOE_TCl_JobOptionsForm_ErrorCount					= new SOSMsgJOE("JOE_TCl_JobOptionsForm_ErrorCount");
	@I18NMsg
	public static final SOSMsgJOE	JOE_TCl_JobOptionsForm_Delayhhmmss					= new SOSMsgJOE("JOE_TCl_JobOptionsForm_Delayhhmmss");
	@I18NMsg
	public static final SOSMsgJOE	JOE_Tbl_JobOptionsForm_SetBack						= new SOSMsgJOE("JOE_Tbl_JobOptionsForm_SetBack");
	@I18NMsg
	public static final SOSMsgJOE	JOE_TCl_JobOptionsForm_SetBackCount					= new SOSMsgJOE("JOE_TCl_JobOptionsForm_SetBackCount");
	@I18NMsg
	public static final SOSMsgJOE	JOE_TCl_JobOptionsForm_IsMax						= new SOSMsgJOE("JOE_TCl_JobOptionsForm_IsMax");
	@I18NMsg
	public static final SOSMsgJOE	JOE_Tbl_JobOptionsForm_Dirs							= new SOSMsgJOE("JOE_Tbl_JobOptionsForm_Dirs");
	@I18NMsg
	public static final SOSMsgJOE	JOE_TCl_JobOptionsForm_Dir							= new SOSMsgJOE("JOE_TCl_JobOptionsForm_Dir");
	@I18NMsg
	public static final SOSMsgJOE	JOE_TCl_JobOptionsForm_Regex						= new SOSMsgJOE("JOE_TCl_JobOptionsForm_Regex");
	@I18NMsg
	public static final SOSMsgJOE	JOE_M_ZeroNotAllowed								= new SOSMsgJOE("JOE_M_ZeroNotAllowed");
	@I18NMsg
	public static final SOSMsgJOE	JOE_M_0032											= new SOSMsgJOE("JOE_M_0032");
	@I18NMsg
	public static final SOSMsgJOE	JOE_M_0033											= new SOSMsgJOE("JOE_M_0033");
	@I18NMsg
	public static final SOSMsgJOE	JOE_M_0034											= new SOSMsgJOE("JOE_M_0034");
	@I18NMsg
	public static final SOSMsgJOE	JOE_M_0035											= new SOSMsgJOE("JOE_M_0035");
	@I18NMsg
	public static final SOSMsgJOE	JOE_M_Stop											= new SOSMsgJOE("JOE_M_Stop");
	@I18NMsg
	public static final SOSMsgJOE	JOE_M_0036											= new SOSMsgJOE("JOE_M_0036");
	@I18NMsg
	public static final SOSMsgJOE	JOE_M_0037											= new SOSMsgJOE("JOE_M_0037");
	@I18NMsg
	public static final SOSMsgJOE	JOE_M_0038											= new SOSMsgJOE("JOE_M_0038");
	@I18NMsg
	public static final SOSMsgJOE	JOE_M_0039											= new SOSMsgJOE("JOE_M_0039");
	@I18NMsg
	public static final SOSMsgJOE	JOE_M_Yes											= new SOSMsgJOE("JOE_M_Yes");

	// JobsForm
	@I18NMsg
	public static final SOSMsgJOE	JOE_G_JobsForm_Jobs									= new SOSMsgJOE("JOE_G_JobsForm_Jobs");
	@I18NMsg
	public static final SOSMsgJOE	JOE_B_JobsForm_NewStandaloneJob						= new SOSMsgJOE("JOE_B_JobsForm_NewStandaloneJob");
	@I18NMsg
	public static final SOSMsgJOE	JOE_B_JobsForm_NewOrderJob							= new SOSMsgJOE("JOE_B_JobsForm_NewOrderJob");
	@I18NMsg
	public static final SOSMsgJOE	JOE_B_JobsForm_JobWizard							= new SOSMsgJOE("JOE_B_JobsForm_JobWizard");
	@I18NMsg
	public static final SOSMsgJOE	JOE_M_0040											= new SOSMsgJOE("JOE_M_0040");
	@I18NMsg
	public static final SOSMsgJOE	JOE_B_JobsForm_RemoveJob							= new SOSMsgJOE("JOE_B_JobsForm_RemoveJob");
	@I18NMsg
	public static final SOSMsgJOE	JOE_M_RemoveJob										= new SOSMsgJOE("JOE_M_RemoveJob");
	@I18NMsg
	public static final SOSMsgJOE	JOE_Tbl_JobsForm_Table								= new SOSMsgJOE("JOE_Tbl_JobsForm_Table");
	@I18NMsg
	public static final SOSMsgJOE	JOE_TCl_JobsForm_Disabled							= new SOSMsgJOE("JOE_TCl_JobsForm_Disabled");
	@I18NMsg
	public static final SOSMsgJOE	JOE_TCl_JobsForm_Name								= new SOSMsgJOE("JOE_TCl_JobsForm_Name");
	@I18NMsg
	public static final SOSMsgJOE	JOE_TCl_JobsForm_Title								= new SOSMsgJOE("JOE_TCl_JobsForm_Title");
	@I18NMsg
	public static final SOSMsgJOE	JOE_TCl_JobsForm_SchedulerID						= new SOSMsgJOE("JOE_TCl_JobsForm_SchedulerID");
	@I18NMsg
	public static final SOSMsgJOE	JOE_TCl_JobsForm_ProcessClass						= new SOSMsgJOE("JOE_TCl_JobsForm_ProcessClass");
	@I18NMsg
	public static final SOSMsgJOE	JOE_TCl_JobsForm_Order								= new SOSMsgJOE("JOE_TCl_JobsForm_Order");

	// LocksForm
	@I18NMsg
	public static final SOSMsgJOE	JOE_G_LocksForm_Locks								= new SOSMsgJOE("JOE_G_LocksForm_Locks");
	@I18NMsg
	public static final SOSMsgJOE	JOE_L_LocksForm_Lock								= new SOSMsgJOE("JOE_L_LocksForm_Lock");
	@I18NMsg
	public static final SOSMsgJOE	JOE_T_LocksForm_Lock								= new SOSMsgJOE("JOE_T_LocksForm_Lock");
	@I18NMsg
	public static final SOSMsgJOE	JOE_B_LocksForm_Apply								= new SOSMsgJOE("JOE_B_LocksForm_Apply");
	@I18NMsg
	public static final SOSMsgJOE	JOE_L_LocksForm_MaxNonExclusive						= new SOSMsgJOE("JOE_L_LocksForm_MaxNonExclusive");
	@I18NMsg
	public static final SOSMsgJOE	JOE_B_LocksForm_UnlimitedNonExclusive				= new SOSMsgJOE("JOE_B_LocksForm_UnlimitedNonExclusive");
	@I18NMsg
	public static final SOSMsgJOE	JOE_Sp_LocksForm_MaxNonExclusive					= new SOSMsgJOE("JOE_Sp_LocksForm_MaxNonExclusive");
	@I18NMsg
	public static final SOSMsgJOE	JOE_B_LocksForm_NewLock								= new SOSMsgJOE("JOE_B_LocksForm_NewLock");
	@I18NMsg
	public static final SOSMsgJOE	JOE_B_LocksForm_RemoveLock							= new SOSMsgJOE("JOE_B_LocksForm_RemoveLock");
	@I18NMsg
	public static final SOSMsgJOE	JOE_Tbl_LocksForm_Table								= new SOSMsgJOE("JOE_Tbl_LocksForm_Table");
	@I18NMsg
	public static final SOSMsgJOE	JOE_TCl_LocksForm_Lock								= new SOSMsgJOE("JOE_TCl_LocksForm_Lock");
	@I18NMsg
	public static final SOSMsgJOE	JOE_TCl_LocksForm_MaxNonExclusive					= new SOSMsgJOE("JOE_TCl_LocksForm_MaxNonExclusive");

	// MailForm
	@I18NMsg
	public static final SOSMsgJOE	JOE_G_MailForm_Mail									= new SOSMsgJOE("JOE_G_MailForm_Mail");
	@I18NMsg
	public static final SOSMsgJOE	JOE_L_MailForm_MailOnError							= new SOSMsgJOE("JOE_L_MailForm_MailOnError");
	@I18NMsg
	public static final SOSMsgJOE	JOE_Cbo_MailForm_MailOnError						= new SOSMsgJOE("JOE_Cbo_MailForm_MailOnError");
	@I18NMsg
	public static final SOSMsgJOE	JOE_L_MailForm_MailOnWarning						= new SOSMsgJOE("JOE_L_MailForm_MailOnWarning");
	@I18NMsg
	public static final SOSMsgJOE	JOE_Cbo_MailForm_MailOnWarning						= new SOSMsgJOE("JOE_Cbo_MailForm_MailOnWarning");
	@I18NMsg
	public static final SOSMsgJOE	JOE_L_MailForm_MailOnSuccess						= new SOSMsgJOE("JOE_L_MailForm_MailOnSuccess");
	@I18NMsg
	public static final SOSMsgJOE	JOE_Cbo_MailForm_MailOnSuccess						= new SOSMsgJOE("JOE_Cbo_MailForm_MailOnSuccess");
	@I18NMsg
	public static final SOSMsgJOE	JOE_L_MailForm_MailOnProcess						= new SOSMsgJOE("JOE_L_MailForm_MailOnProcess");
	@I18NMsg
	public static final SOSMsgJOE	JOE_Cbo_MailForm_MailOnProcess						= new SOSMsgJOE("JOE_Cbo_MailForm_MailOnProcess");
	@I18NMsg
	public static final SOSMsgJOE	JOE_L_MailForm_MailOnDelayAfterError				= new SOSMsgJOE("JOE_L_MailForm_MailOnDelayAfterError");
	@I18NMsg
	public static final SOSMsgJOE	JOE_Cbo_MailForm_MailOnDelayAfterError				= new SOSMsgJOE("JOE_Cbo_MailForm_MailOnDelayAfterError");
	@I18NMsg
	public static final SOSMsgJOE	JOE_L_MailForm_MailTo								= new SOSMsgJOE("JOE_L_MailForm_MailTo");
	@I18NMsg
	public static final SOSMsgJOE	JOE_T_MailForm_MailTo								= new SOSMsgJOE("JOE_T_MailForm_MailTo");
	@I18NMsg
	public static final SOSMsgJOE	JOE_L_MailForm_MailCC								= new SOSMsgJOE("JOE_L_MailForm_MailCC");
	@I18NMsg
	public static final SOSMsgJOE	JOE_T_MailForm_MailCC								= new SOSMsgJOE("JOE_T_MailForm_MailCC");
	@I18NMsg
	public static final SOSMsgJOE	JOE_L_MailForm_MailBCC								= new SOSMsgJOE("JOE_L_MailForm_MailBCC");
	@I18NMsg
	public static final SOSMsgJOE	JOE_T_MailForm_MailBCC								= new SOSMsgJOE("JOE_T_MailForm_MailBCC");
	@I18NMsg
	public static final SOSMsgJOE	JOE_L_MailForm_LogLevel								= new SOSMsgJOE("JOE_L_MailForm_LogLevel");
	@I18NMsg
	public static final SOSMsgJOE	JOE_Cbo_MailForm_LogLevel							= new SOSMsgJOE("JOE_Cbo_MailForm_LogLevel");
	@I18NMsg
	public static final SOSMsgJOE	JOE_L_MailForm_History								= new SOSMsgJOE("JOE_L_MailForm_History");
	@I18NMsg
	public static final SOSMsgJOE	JOE_Cbo_MailForm_History							= new SOSMsgJOE("JOE_Cbo_MailForm_History");
	@I18NMsg
	public static final SOSMsgJOE	JOE_L_MailForm_HistoryOnProcess						= new SOSMsgJOE("JOE_L_MailForm_HistoryOnProcess");
	@I18NMsg
	public static final SOSMsgJOE	JOE_Cbo_MailForm_HistoryOnProcess					= new SOSMsgJOE("JOE_Cbo_MailForm_HistoryOnProcess");
	@I18NMsg
	public static final SOSMsgJOE	JOE_L_MailForm_HistoryWithLog						= new SOSMsgJOE("JOE_L_MailForm_HistoryWithLog");
	@I18NMsg
	public static final SOSMsgJOE	JOE_Cbo_MailForm_HistoryWithLog						= new SOSMsgJOE("JOE_Cbo_MailForm_HistoryWithLog");

	// OrderForm
	@I18NMsg
	public static final SOSMsgJOE	JOE_G_OrderForm_Order								= new SOSMsgJOE("JOE_G_OrderForm_Order");
	@I18NMsg
	public static final SOSMsgJOE	JOE_L_OrderForm_OrderID								= new SOSMsgJOE("JOE_L_OrderForm_OrderID");
	@I18NMsg
	public static final SOSMsgJOE	JOE_T_OrderForm_OrderID								= new SOSMsgJOE("JOE_T_OrderForm_OrderID");
	@I18NMsg
	public static final SOSMsgJOE	JOE_L_OrderForm_JobChain							= new SOSMsgJOE("JOE_L_OrderForm_JobChain");
	@I18NMsg
	public static final SOSMsgJOE	JOE_Cbo_OrderForm_JobChain							= new SOSMsgJOE("JOE_Cbo_OrderForm_JobChain");
	@I18NMsg
	public static final SOSMsgJOE	JOE_L_OrderForm_Title								= new SOSMsgJOE("JOE_L_OrderForm_Title");
	@I18NMsg
	public static final SOSMsgJOE	JOE_T_OrderForm_Title								= new SOSMsgJOE("JOE_T_OrderForm_Title");
	@I18NMsg
	public static final SOSMsgJOE	JOE_L_OrderForm_Priority							= new SOSMsgJOE("JOE_L_OrderForm_Priority");
	@I18NMsg
	public static final SOSMsgJOE	JOE_T_OrderForm_Priority							= new SOSMsgJOE("JOE_T_OrderForm_Priority");
	@I18NMsg
	public static final SOSMsgJOE	JOE_L_OrderForm_State								= new SOSMsgJOE("JOE_L_OrderForm_State");
	@I18NMsg
	public static final SOSMsgJOE	JOE_T_OrderForm_State								= new SOSMsgJOE("JOE_T_OrderForm_State");
	@I18NMsg
	public static final SOSMsgJOE	JOE_L_OrderForm_EndState							= new SOSMsgJOE("JOE_L_OrderForm_EndState");
	@I18NMsg
	public static final SOSMsgJOE	JOE_Cbo_OrderForm_EndState							= new SOSMsgJOE("JOE_Cbo_OrderForm_EndState");
	@I18NMsg
	public static final SOSMsgJOE	JOE_Cbo_OrderForm_State2							= new SOSMsgJOE("JOE_Cbo_OrderForm_State2");
	@I18NMsg
	public static final SOSMsgJOE	JOE_B_OrderForm_Remove								= new SOSMsgJOE("JOE_B_OrderForm_Remove");
	@I18NMsg
	public static final SOSMsgJOE	JOE_M_OrderForm_RemoveState							= new SOSMsgJOE("JOE_M_OrderForm_RemoveState");
	@I18NMsg
	public static final SOSMsgJOE	JOE_M_OrderForm_RemoveFailed						= new SOSMsgJOE("JOE_M_OrderForm_RemoveFailed");
	@I18NMsg
	public static final SOSMsgJOE	JOE_M_OrderForm_Global								= new SOSMsgJOE("JOE_M_OrderForm_Global");

	// OrdersForm
	@I18NMsg
	public static final SOSMsgJOE	JOE_G_OrdersForm_Orders								= new SOSMsgJOE("JOE_G_OrdersForm_Orders");
	@I18NMsg
	public static final SOSMsgJOE	JOE_B_OrdersForm_NewOrder							= new SOSMsgJOE("JOE_B_OrdersForm_NewOrder");
	@I18NMsg
	public static final SOSMsgJOE	JOE_B_OrdersForm_RemoveOrder						= new SOSMsgJOE("JOE_B_OrdersForm_RemoveOrder");
	@I18NMsg
	public static final SOSMsgJOE	JOE_Tbl_OrdersForm_Table							= new SOSMsgJOE("JOE_Tbl_OrdersForm_Table");
	@I18NMsg
	public static final SOSMsgJOE	JOE_TCl_OrdersForm_OrderNameID						= new SOSMsgJOE("JOE_TCl_OrdersForm_OrderNameID");
	@I18NMsg
	public static final SOSMsgJOE	JOE_M_OrdersForm_RemoveOrder						= new SOSMsgJOE("JOE_M_OrdersForm_RemoveOrder");

	// ParameterForm
	@I18NMsg
	public static final SOSMsgJOE	JOE_G_ParameterForm_JobParameter					= new SOSMsgJOE("JOE_G_ParameterForm_JobParameter");
	@I18NMsg
	public static final SOSMsgJOE	JOE_M_ParameterForm_FileNotExist					= new SOSMsgJOE("JOE_M_ParameterForm_FileNotExist");
	@I18NMsg
	public static final SOSMsgJOE	JOE_M_ParameterForm_NoDistinctParam					= new SOSMsgJOE("JOE_M_ParameterForm_NoDistinctParam");
	@I18NMsg
	public static final SOSMsgJOE	JOE_L_ParameterForm_Value							= new SOSMsgJOE("JOE_L_ParameterForm_Value");
	@I18NMsg
	public static final SOSMsgJOE	JOE_B_ParameterForm_IncludeSave						= new SOSMsgJOE("JOE_B_ParameterForm_IncludeSave");
	@I18NMsg
	public static final SOSMsgJOE	JOE_TCl_ParameterForm_Name							= new SOSMsgJOE("JOE_TCl_ParameterForm_Name");
	@I18NMsg
	public static final SOSMsgJOE	JOE_TCl_ParameterForm_Value							= new SOSMsgJOE("JOE_TCl_ParameterForm_Value");
	@I18NMsg
	public static final SOSMsgJOE	JOE_B_ParameterForm_New								= new SOSMsgJOE("JOE_B_ParameterForm_New");
	@I18NMsg
	public static final SOSMsgJOE	JOE_B_ParameterForm_IncludeRemove					= new SOSMsgJOE("JOE_B_ParameterForm_IncludeRemove");
	@I18NMsg
	public static final SOSMsgJOE	JOE_B_ParameterForm_Wizard							= new SOSMsgJOE("JOE_B_ParameterForm_Wizard");
	@I18NMsg
	public static final SOSMsgJOE	JOE_E_0010											= new SOSMsgJOE("JOE_E_0010");
	@I18NMsg
	public static final SOSMsgJOE	JOE_TI_ParameterForm_TabItemParameter				= new SOSMsgJOE("JOE_TI_ParameterForm_TabItemParameter");
	
	public static final SOSMsgJOE	JOE_TI_DiagramViewer						= new SOSMsgJOE("JOE_TI_DiagramViewer");
	public static final SOSMsgJOE	JOE_TI_JobChainParameter						= new SOSMsgJOE("JOE_TI_JobChainParameter");

	public static final SOSMsgJOE	JOE_TI_JobChainNodeParameter						= new SOSMsgJOE("JOE_TI_JobChainNodeParameter");


	@I18NMsg
	public static final SOSMsgJOE	JOE_T_ParameterForm_ParamName						= new SOSMsgJOE("JOE_T_ParameterForm_ParamName");
	@I18NMsg
	public static final SOSMsgJOE	JOE_T_ParameterForm_ParamValue						= new SOSMsgJOE("JOE_T_ParameterForm_ParamValue");
	@I18NMsg
	public static final SOSMsgJOE	JOE_B_ParameterForm_Comment							= new SOSMsgJOE("JOE_B_ParameterForm_Comment");
	@I18NMsg
	public static final SOSMsgJOE	JOE_B_ParameterForm_Apply							= new SOSMsgJOE("JOE_B_ParameterForm_Apply");
	@I18NMsg
	public static final SOSMsgJOE	JOE_Tbl_ParameterForm_Parameter						= new SOSMsgJOE("JOE_Tbl_ParameterForm_Parameter");
	@I18NMsg
	public static final SOSMsgJOE	JOE_B_ParameterForm_NewParam						= new SOSMsgJOE("JOE_B_ParameterForm_NewParam");
	@I18NMsg
	public static final SOSMsgJOE	JOE_B_ParameterForm_Remove							= new SOSMsgJOE("JOE_B_ParameterForm_Remove");
	@I18NMsg
	public static final SOSMsgJOE	JOE_T_ParameterForm_ParamDescription				= new SOSMsgJOE("JOE_T_ParameterForm_ParamDescription");
	@I18NMsg
	public static final SOSMsgJOE	JOE_TI_ParameterForm_TabItemEnvironment				= new SOSMsgJOE("JOE_TI_ParameterForm_TabItemEnvironment");
	@I18NMsg
	public static final SOSMsgJOE	JOE_T_ParameterForm_EnvName							= new SOSMsgJOE("JOE_T_ParameterForm_EnvName");
	@I18NMsg
	public static final SOSMsgJOE	JOE_T_ParameterForm_EnvValue						= new SOSMsgJOE("JOE_T_ParameterForm_EnvValue");
	@I18NMsg
	public static final SOSMsgJOE	JOE_B_ParameterForm_EnvApply						= new SOSMsgJOE("JOE_B_ParameterForm_EnvApply");
	@I18NMsg
	public static final SOSMsgJOE	JOE_B_ParameterForm_NewEnv							= new SOSMsgJOE("JOE_B_ParameterForm_NewEnv");
	@I18NMsg
	public static final SOSMsgJOE	JOE_B_ParameterForm_RemoveEnv						= new SOSMsgJOE("JOE_B_ParameterForm_RemoveEnv");
	@I18NMsg
	public static final SOSMsgJOE	JOE_TI_ParameterForm_TabItemIncludes				= new SOSMsgJOE("JOE_TI_ParameterForm_TabItemIncludes");
	@I18NMsg
	public static final SOSMsgJOE	JOE_B_ParameterForm_LifeFile						= new SOSMsgJOE("JOE_B_ParameterForm_LifeFile");
	@I18NMsg
	public static final SOSMsgJOE	JOE_L_ParameterForm_File							= new SOSMsgJOE("JOE_L_ParameterForm_File");
	@I18NMsg
	public static final SOSMsgJOE	JOE_T_ParameterForm_IncludeFilename					= new SOSMsgJOE("JOE_T_ParameterForm_IncludeFilename");
	@I18NMsg
	public static final SOSMsgJOE	JOE_L_ParameterForm_Node							= new SOSMsgJOE("JOE_L_ParameterForm_Node");
	@I18NMsg
	public static final SOSMsgJOE	JOE_T_ParameterForm_IncludeNode						= new SOSMsgJOE("JOE_T_ParameterForm_IncludeNode");
	@I18NMsg
	public static final SOSMsgJOE	JOE_B_ParameterForm_IncludesApply					= new SOSMsgJOE("JOE_B_ParameterForm_IncludesApply");
	@I18NMsg
	public static final SOSMsgJOE	JOE_Tbl_ParameterForm_IncludeParams					= new SOSMsgJOE("JOE_Tbl_ParameterForm_IncludeParams");
	@I18NMsg
	public static final SOSMsgJOE	JOE_TCl_ParameterForm_File							= new SOSMsgJOE("JOE_TCl_ParameterForm_File");
	@I18NMsg
	public static final SOSMsgJOE	JOE_TCl_ParameterForm_Node							= new SOSMsgJOE("JOE_TCl_ParameterForm_Node");
	@I18NMsg
	public static final SOSMsgJOE	JOE_TCl_ParameterForm_LiveFile						= new SOSMsgJOE("JOE_TCl_ParameterForm_LiveFile");
	@I18NMsg
	public static final SOSMsgJOE	JOE_B_ParameterForm_NewIncludes						= new SOSMsgJOE("JOE_B_ParameterForm_NewIncludes");
	@I18NMsg
	public static final SOSMsgJOE	JOE_B_ParameterForm_OpenInclude						= new SOSMsgJOE("JOE_B_ParameterForm_OpenInclude");
	@I18NMsg
	public static final SOSMsgJOE	JOE_B_ParameterForm_RemoveInclude					= new SOSMsgJOE("JOE_B_ParameterForm_RemoveInclude");
	@I18NMsg
	public static final SOSMsgJOE	JOE_TI_ParameterForm_Parameter						= new SOSMsgJOE("JOE_TI_ParameterForm_Parameter");
	@I18NMsg
	public static final SOSMsgJOE	JOE_T_ParameterForm_ParaName						= new SOSMsgJOE("JOE_T_ParameterForm_ParaName");
	@I18NMsg
	public static final SOSMsgJOE	JOE_T_ParameterForm_ParaValue						= new SOSMsgJOE("JOE_T_ParameterForm_ParaValue");
	@I18NMsg
	public static final SOSMsgJOE	JOE_B_ParameterForm_Param							= new SOSMsgJOE("JOE_B_ParameterForm_Param");
	@I18NMsg
	public static final SOSMsgJOE	JOE_B_ParameterForm_FromTask						= new SOSMsgJOE("JOE_B_ParameterForm_FromTask");
	@I18NMsg
	public static final SOSMsgJOE	JOE_B_ParameterForm_FromOrder						= new SOSMsgJOE("JOE_B_ParameterForm_FromOrder");
	@I18NMsg
	public static final SOSMsgJOE	JOE_M_ParameterForm_From							= new SOSMsgJOE("JOE_M_ParameterForm_From");
	@I18NMsg
	public static final SOSMsgJOE	JOE_M_ParameterForm_Task							= new SOSMsgJOE("JOE_M_ParameterForm_Task");
	@I18NMsg
	public static final SOSMsgJOE	JOE_M_ParameterForm_Order							= new SOSMsgJOE("JOE_M_ParameterForm_Order");
	@I18NMsg
	public static final SOSMsgJOE	JOE_Tbl_ParameterForm_Environment					= new SOSMsgJOE("JOE_Tbl_ParameterForm_Environment");

	// PeriodForm
	@I18NMsg
	public static final SOSMsgJOE	JOE_M_PeriodForm_SingleStart						= new SOSMsgJOE("JOE_M_PeriodForm_SingleStart");
	@I18NMsg
	public static final SOSMsgJOE	JOE_M_PeriodForm_RepeatTime							= new SOSMsgJOE("JOE_M_PeriodForm_RepeatTime");
	@I18NMsg
	public static final SOSMsgJOE	JOE_M_PeriodForm_AbsoluteTime						= new SOSMsgJOE("JOE_M_PeriodForm_AbsoluteTime");
	@I18NMsg
	public static final SOSMsgJOE	JOE_M_PeriodForm_StartTimeNA						= new SOSMsgJOE("JOE_M_PeriodForm_StartTimeNA");
	@I18NMsg
	public static final SOSMsgJOE	JOE_G_PeriodForm_TimeSlot							= new SOSMsgJOE("JOE_G_PeriodForm_TimeSlot");
	@I18NMsg
	public static final SOSMsgJOE	JOE_L_PeriodForm_LetRun								= new SOSMsgJOE("JOE_L_PeriodForm_LetRun");
	@I18NMsg
	public static final SOSMsgJOE	JOE_B_PeriodForm_LetRun								= new SOSMsgJOE("JOE_B_PeriodForm_LetRun");
	@I18NMsg
	public static final SOSMsgJOE	JOE_L_PeriodForm_RunOnce							= new SOSMsgJOE("JOE_L_PeriodForm_RunOnce");
	@I18NMsg
	public static final SOSMsgJOE	JOE_B_PeriodForm_RunOnce							= new SOSMsgJOE("JOE_B_PeriodForm_RunOnce");
	@I18NMsg
	public static final SOSMsgJOE	JOE_L_PeriodForm_BeginTime							= new SOSMsgJOE("JOE_L_PeriodForm_BeginTime");
	@I18NMsg
	public static final SOSMsgJOE	JOE_T_PeriodForm_BeginHours							= new SOSMsgJOE("JOE_T_PeriodForm_BeginHours");
	@I18NMsg
	public static final SOSMsgJOE	JOE_T_PeriodForm_BeginMinutes						= new SOSMsgJOE("JOE_T_PeriodForm_BeginMinutes");
	@I18NMsg
	public static final SOSMsgJOE	JOE_T_PeriodForm_BeginSeconds						= new SOSMsgJOE("JOE_T_PeriodForm_BeginSeconds");
	@I18NMsg
	public static final SOSMsgJOE	JOE_L_PeriodForm_EndTime							= new SOSMsgJOE("JOE_L_PeriodForm_EndTime");
	@I18NMsg
	public static final SOSMsgJOE	JOE_T_PeriodForm_EndHours							= new SOSMsgJOE("JOE_T_PeriodForm_EndHours");
	@I18NMsg
	public static final SOSMsgJOE	JOE_T_PeriodForm_EndMinutes							= new SOSMsgJOE("JOE_T_PeriodForm_EndMinutes");
	@I18NMsg
	public static final SOSMsgJOE	JOE_T_PeriodForm_EndSeconds							= new SOSMsgJOE("JOE_T_PeriodForm_EndSeconds");
	@I18NMsg
	public static final SOSMsgJOE	JOE_G_PeriodForm_StartTime							= new SOSMsgJOE("JOE_G_PeriodForm_StartTime");
	@I18NMsg
	public static final SOSMsgJOE	JOE_Tooltip_PeriodForm_SingleStartHours				= new SOSMsgJOE("JOE_Tooltip_PeriodForm_SingleStartHours");
	@I18NMsg
	public static final SOSMsgJOE	JOE_Tooltip_PeriodForm_SingleStartMinutes			= new SOSMsgJOE("JOE_Tooltip_PeriodForm_SingleStartMinutes");
	@I18NMsg
	public static final SOSMsgJOE	JOE_Tooltip_PeriodForm_SingleStartSeconds			= new SOSMsgJOE("JOE_Tooltip_PeriodForm_SingleStartSeconds");
	@I18NMsg
	public static final SOSMsgJOE	JOE_Tooltip_PeriodForm_RepeatHours					= new SOSMsgJOE("JOE_Tooltip_PeriodForm_RepeatHours");
	@I18NMsg
	public static final SOSMsgJOE	JOE_Tooltip_PeriodForm_RepeatMinutes				= new SOSMsgJOE("JOE_Tooltip_PeriodForm_RepeatMinutes");
	@I18NMsg
	public static final SOSMsgJOE	JOE_Tooltip_PeriodForm_RepeatSeconds				= new SOSMsgJOE("JOE_Tooltip_PeriodForm_RepeatSeconds");
	@I18NMsg
	public static final SOSMsgJOE	JOE_Cbo_PeriodForm_StartTime						= new SOSMsgJOE("JOE_Cbo_PeriodForm_StartTime");
	@I18NMsg
	public static final SOSMsgJOE	JOE_L_PeriodForm_OrSS								= new SOSMsgJOE("JOE_L_PeriodForm_OrSS");
	@I18NMsg
	public static final SOSMsgJOE	JOE_G_PeriodForm_WhenHoliday						= new SOSMsgJOE("JOE_G_PeriodForm_WhenHoliday");
	@I18NMsg
	public static final SOSMsgJOE	JOE_Cbo_PeriodForm_WhenHoliday						= new SOSMsgJOE("JOE_Cbo_PeriodForm_WhenHoliday");

	// PeriodsForm
	@I18NMsg
	public static final SOSMsgJOE	JOE_G_PeriodsForm_Periods							= new SOSMsgJOE("JOE_G_PeriodsForm_Periods");
	@I18NMsg
	public static final SOSMsgJOE	JOE_B_PeriodsForm_ApplyPeriod						= new SOSMsgJOE("JOE_B_PeriodsForm_ApplyPeriod");
	@I18NMsg
	public static final SOSMsgJOE	JOE_B_PeriodsForm_NewPeriod							= new SOSMsgJOE("JOE_B_PeriodsForm_NewPeriod");
	@I18NMsg
	public static final SOSMsgJOE	JOE_B_PeriodsForm_RemovePeriod						= new SOSMsgJOE("JOE_B_PeriodsForm_RemovePeriod");
	@I18NMsg
	public static final SOSMsgJOE	JOE_Tbl_PeriodsForm_Periods							= new SOSMsgJOE("JOE_Tbl_PeriodsForm_Periods");
	@I18NMsg
	public static final SOSMsgJOE	JOE_TCl_PeriodsForm_LetRun							= new SOSMsgJOE("JOE_TCl_PeriodsForm_LetRun");
	@I18NMsg
	public static final SOSMsgJOE	JOE_TCl_PeriodsForm_Begin							= new SOSMsgJOE("JOE_TCl_PeriodsForm_Begin");
	@I18NMsg
	public static final SOSMsgJOE	JOE_TCl_PeriodsForm_End								= new SOSMsgJOE("JOE_TCl_PeriodsForm_End");
	@I18NMsg
	public static final SOSMsgJOE	JOE_TCl_PeriodsForm_WhenHoliday						= new SOSMsgJOE("JOE_TCl_PeriodsForm_WhenHoliday");

	// RunTimeForm
	@I18NMsg
	public static final SOSMsgJOE	JOE_G_RunTimeForm_RunTime							= new SOSMsgJOE("JOE_G_RunTimeForm_RunTime");
	@I18NMsg
	public static final SOSMsgJOE	JOE_G_RunTimeForm_StartTimeFunction					= new SOSMsgJOE("JOE_G_RunTimeForm_StartTimeFunction");
	@I18NMsg
	public static final SOSMsgJOE	JOE_T_RunTimeForm_StartTimeFunction					= new SOSMsgJOE("JOE_T_RunTimeForm_StartTimeFunction");
	@I18NMsg
	public static final SOSMsgJOE	JOE_G_RunTimeForm_Schedule							= new SOSMsgJOE("JOE_G_RunTimeForm_Schedule");
	@I18NMsg
	public static final SOSMsgJOE	JOE_Cbo_RunTimeForm_Schedule						= new SOSMsgJOE("JOE_Cbo_RunTimeForm_Schedule");
	@I18NMsg
	public static final SOSMsgJOE	JOE_B_RunTimeForm_Browse							= new SOSMsgJOE("JOE_B_RunTimeForm_Browse");
	@I18NMsg
	public static final SOSMsgJOE	JOE_G_RunTimeForm_Comment							= new SOSMsgJOE("JOE_G_RunTimeForm_Comment");
	@I18NMsg
	public static final SOSMsgJOE	JOE_T_RunTimeForm_Comment							= new SOSMsgJOE("JOE_T_RunTimeForm_Comment");
	@I18NMsg
	public static final SOSMsgJOE	JOE_B_RunTimeForm_Comment							= new SOSMsgJOE("JOE_B_RunTimeForm_Comment");

	// ScheduleForm
	@I18NMsg
	public static final SOSMsgJOE	JOE_G_ScheduleForm_Schedule							= new SOSMsgJOE("JOE_G_ScheduleForm_Schedule");
	@I18NMsg
	public static final SOSMsgJOE	JOE_T_ScheduleForm_Name								= new SOSMsgJOE("JOE_T_ScheduleForm_Name");
	@I18NMsg
	public static final SOSMsgJOE	JOE_L_ScheduleForm_Title							= new SOSMsgJOE("JOE_L_ScheduleForm_Title");
	@I18NMsg
	public static final SOSMsgJOE	JOE_T_ScheduleForm_Title							= new SOSMsgJOE("JOE_T_ScheduleForm_Title");
	@I18NMsg
	public static final SOSMsgJOE	JOE_L_ScheduleForm_Substitute						= new SOSMsgJOE("JOE_L_ScheduleForm_Substitute");
	@I18NMsg
	public static final SOSMsgJOE	JOE_Cbo_ScheduleForm_Substitute						= new SOSMsgJOE("JOE_Cbo_ScheduleForm_Substitute");
	@I18NMsg
	public static final SOSMsgJOE	JOE_L_ScheduleForm_ValidFrom						= new SOSMsgJOE("JOE_L_ScheduleForm_ValidFrom");
	@I18NMsg
	public static final SOSMsgJOE	JOE_ScheduleForm_ValidFromDate						= new SOSMsgJOE("JOE_ScheduleForm_ValidFromDate");
	@I18NMsg
	public static final SOSMsgJOE	JOE_ScheduleForm_ValidFromTime						= new SOSMsgJOE("JOE_ScheduleForm_ValidFromTime");
	@I18NMsg
	public static final SOSMsgJOE	JOE_L_ScheduleForm_ValidTo							= new SOSMsgJOE("JOE_L_ScheduleForm_ValidTo");
	@I18NMsg
	public static final SOSMsgJOE	JOE_ScheduleForm_ValidToDate						= new SOSMsgJOE("JOE_ScheduleForm_ValidToDate");
	@I18NMsg
	public static final SOSMsgJOE	JOE_ScheduleForm_ValidToTime						= new SOSMsgJOE("JOE_ScheduleForm_ValidToTime");

	// SchedulerEditorFontDialog
	@I18NMsg
	public static final SOSMsgJOE	JOE_M_WrongColour									= new SOSMsgJOE("JOE_M_WrongColour");
	@I18NMsg
	public static final SOSMsgJOE	JOE_M_FontDialog									= new SOSMsgJOE("JOE_M_FontDialog");
	@I18NMsg
	public static final SOSMsgJOE	JOE_M_Blindtext										= new SOSMsgJOE("JOE_M_Blindtext");
	@I18NMsg
	public static final SOSMsgJOE	JOE_B_FontDialog_Change								= new SOSMsgJOE("JOE_B_FontDialog_Change");
	@I18NMsg
	public static final SOSMsgJOE	JOE_M_SelectFont									= new SOSMsgJOE("JOE_M_SelectFont");
	@I18NMsg
	public static final SOSMsgJOE	JOE_B_FontDialog_Save								= new SOSMsgJOE("JOE_B_FontDialog_Save");
	@I18NMsg
	public static final SOSMsgJOE	JOE_B_FontDialog_Reset								= new SOSMsgJOE("JOE_B_FontDialog_Reset");
	@I18NMsg
	public static final SOSMsgJOE	JOE_B_FontDialog_Cancel								= new SOSMsgJOE("JOE_B_FontDialog_Cancel");

	// SchedulerForm
	@I18NMsg
	public static final SOSMsgJOE	JOE_G_SchedulerForm_SchedulerElements				= new SOSMsgJOE("JOE_G_SchedulerForm_SchedulerElements");

	// SchedulesForm
	@I18NMsg
	public static final SOSMsgJOE	JOE_G_SchedulesForm_Schedules						= new SOSMsgJOE("JOE_G_SchedulesForm_Schedules");
	@I18NMsg
	public static final SOSMsgJOE	JOE_B_SchedulesForm_NewSchedule						= new SOSMsgJOE("JOE_B_SchedulesForm_NewSchedule");
	@I18NMsg
	public static final SOSMsgJOE	JOE_B_SchedulesForm_Remove							= new SOSMsgJOE("JOE_B_SchedulesForm_Remove");
	@I18NMsg
	public static final SOSMsgJOE	JOE_M_SchedulesForm_RemoveSchedule					= new SOSMsgJOE("JOE_M_SchedulesForm_RemoveSchedule");
	@I18NMsg
	public static final SOSMsgJOE	JOE_Tbl_SchedulesForm_Schedules						= new SOSMsgJOE("JOE_Tbl_SchedulesForm_Schedules");
	@I18NMsg
	public static final SOSMsgJOE	JOE_TCl_SchedulesForm_Name							= new SOSMsgJOE("JOE_TCl_SchedulesForm_Name");

	// ScriptForm
	@I18NMsg
	public static final SOSMsgJOE	JOE_M_ScriptForm_ItemIndex							= new SOSMsgJOE("JOE_M_ScriptForm_ItemIndex");
	@I18NMsg
	public static final SOSMsgJOE	JOE_ScriptForm_TabItemScript						= new SOSMsgJOE("JOE_ScriptForm_TabItemScript");
	@I18NMsg
	public static final SOSMsgJOE	JOE_ScriptForm_TabItemJavaAPI						= new SOSMsgJOE("JOE_ScriptForm_TabItemJavaAPI");
	@I18NMsg
	public static final SOSMsgJOE	JOE_ScriptForm_TabItemIncludes						= new SOSMsgJOE("JOE_ScriptForm_TabItemIncludes");
	@I18NMsg
	public static final SOSMsgJOE	JOE_L_ScriptForm_Language							= new SOSMsgJOE("JOE_L_ScriptForm_Language");

	// ScriptFormPreProcessing
	@I18NMsg
	public static final SOSMsgJOE	JOE_M_ScriptFormPreProcessing_OverwriteMonitor		= new SOSMsgJOE("JOE_M_ScriptFormPreProcessing_OverwriteMonitor");

	// ScriptJobMainForm
	@I18NMsg
	public static final SOSMsgJOE	JOE_TI_ScriptJobMainForm_ProcessFile				= new SOSMsgJOE("JOE_TI_ScriptJobMainForm_ProcessFile");
	@I18NMsg
	public static final SOSMsgJOE	JOE_TI_ScriptJobMainForm_Options					= new SOSMsgJOE("JOE_TI_ScriptJobMainForm_Options");
	@I18NMsg
	public static final SOSMsgJOE	JOE_TI_ScriptJobMainForm_Executable					= new SOSMsgJOE("JOE_TI_ScriptJobMainForm_Executable");
	@I18NMsg
	public static final SOSMsgJOE	JOE_TI_ScriptJobMainForm_EMail						= new SOSMsgJOE("JOE_TI_ScriptJobMainForm_EMail");
	@I18NMsg
	public static final SOSMsgJOE	JOE_TI_ScriptJobMainForm_SetBack					= new SOSMsgJOE("JOE_TI_ScriptJobMainForm_SetBack");
	@I18NMsg
	public static final SOSMsgJOE	JOE_TI_ScriptJobMainForm_OnError					= new SOSMsgJOE("JOE_TI_ScriptJobMainForm_OnError");
	@I18NMsg
	public static final SOSMsgJOE	JOE_TI_ScriptJobMainForm_FileWatcher				= new SOSMsgJOE("JOE_TI_ScriptJobMainForm_FileWatcher");
	@I18NMsg
	public static final SOSMsgJOE	JOE_TI_ScriptJobMainForm_Doc						= new SOSMsgJOE("JOE_TI_ScriptJobMainForm_Doc");
	@I18NMsg
	public static final SOSMsgJOE	JOE_TI_ScriptJobMainForm_XML						= new SOSMsgJOE("JOE_TI_ScriptJobMainForm_XML");

	// ScriptsForm
	@I18NMsg
	public static final SOSMsgJOE	JOE_B_ScriptsForm_Remove							= new SOSMsgJOE("JOE_B_ScriptsForm_Remove");
	@I18NMsg
	public static final SOSMsgJOE	JOE_M_ScriptsForm_RemoveMonitor						= new SOSMsgJOE("JOE_M_ScriptsForm_RemoveMonitor");
	@I18NMsg
	public static final SOSMsgJOE	JOE_Tbl_ScriptsForm_PrePostProcessing				= new SOSMsgJOE("JOE_Tbl_ScriptsForm_PrePostProcessing");
	@I18NMsg
	public static final SOSMsgJOE	JOE_TCl_ScriptsForm_Name							= new SOSMsgJOE("JOE_TCl_ScriptsForm_Name");
	@I18NMsg
	public static final SOSMsgJOE	JOE_TCl_ScriptsForm_Ordering						= new SOSMsgJOE("JOE_TCl_ScriptsForm_Ordering");
	@I18NMsg
	public static final SOSMsgJOE	JOE_B_ScriptsForm_New								= new SOSMsgJOE("JOE_B_ScriptsForm_New");
	@I18NMsg
	public static final SOSMsgJOE	JOE_M_ScriptsForm_Monitor							= new SOSMsgJOE("JOE_M_ScriptsForm_Monitor");

	// SecurityForm
	@I18NMsg
	public static final SOSMsgJOE	JOE_G_SecurityForm_Security							= new SOSMsgJOE("JOE_G_SecurityForm_Security");
	@I18NMsg
	public static final SOSMsgJOE	JOE_B_SecurityForm_IgnoreUnkownHosts				= new SOSMsgJOE("JOE_B_SecurityForm_IgnoreUnkownHosts");
	@I18NMsg
	public static final SOSMsgJOE	JOE_L_SecurityForm_AccessLevel						= new SOSMsgJOE("JOE_L_SecurityForm_AccessLevel");
	@I18NMsg
	public static final SOSMsgJOE	JOE_L_SecurityForm_Host								= new SOSMsgJOE("JOE_L_SecurityForm_Host");
	@I18NMsg
	public static final SOSMsgJOE	JOE_T_SecurityForm_Host								= new SOSMsgJOE("JOE_T_SecurityForm_Host");
	@I18NMsg
	public static final SOSMsgJOE	JOE_Cbo_SecurityForm_AccessLevel					= new SOSMsgJOE("JOE_Cbo_SecurityForm_AccessLevel");
	@I18NMsg
	public static final SOSMsgJOE	JOE_B_SecurityForm_ApplyHost						= new SOSMsgJOE("JOE_B_SecurityForm_ApplyHost");
	@I18NMsg
	public static final SOSMsgJOE	JOE_B_SecurityForm_NewHost							= new SOSMsgJOE("JOE_B_SecurityForm_NewHost");
	@I18NMsg
	public static final SOSMsgJOE	JOE_B_SecurityForm_RemoveHost						= new SOSMsgJOE("JOE_B_SecurityForm_RemoveHost");
	@I18NMsg
	public static final SOSMsgJOE	JOE_Tbl_SecurityForm_Hosts							= new SOSMsgJOE("JOE_Tbl_SecurityForm_Hosts");
	@I18NMsg
	public static final SOSMsgJOE	JOE_TCl_SecurityForm_Host							= new SOSMsgJOE("JOE_TCl_SecurityForm_Host");
	@I18NMsg
	public static final SOSMsgJOE	JOE_TCl_SecurityForm_AccessLevel					= new SOSMsgJOE("JOE_TCl_SecurityForm_AccessLevel");

	// SpecificWeekdaysForm
	@I18NMsg
	public static final SOSMsgJOE	JOE_G_SpecificWeekdaysForm_Monthdays				= new SOSMsgJOE("JOE_G_SpecificWeekdaysForm_Monthdays");
	@I18NMsg
	public static final SOSMsgJOE	JOE_B_SpecificWeekdaysForm_AddWeekday				= new SOSMsgJOE("JOE_B_SpecificWeekdaysForm_AddWeekday");
	@I18NMsg
	public static final SOSMsgJOE	JOE_Lst_SpecificWeekdaysForm_UsedDays				= new SOSMsgJOE("JOE_Lst_SpecificWeekdaysForm_UsedDays");
	@I18NMsg
	public static final SOSMsgJOE	JOE_B_SpecificWeekdaysForm_RemoveWeekday			= new SOSMsgJOE("JOE_B_SpecificWeekdaysForm_RemoveWeekday");
	@I18NMsg
	public static final SOSMsgJOE	JOE_Cbo_SpecificWeekdaysForm_Daynames				= new SOSMsgJOE("JOE_Cbo_SpecificWeekdaysForm_Daynames");
	@I18NMsg
	public static final SOSMsgJOE	JOE_M_SpecificWeekdaysForm_Weekdays					= new SOSMsgJOE("JOE_M_SpecificWeekdaysForm_Weekdays");
	@I18NMsg
	public static final SOSMsgJOE	JOE_Cbo_SpecificWeekdaysForm_Weekdays				= new SOSMsgJOE("JOE_Cbo_SpecificWeekdaysForm_Weekdays");

	// WebserviceForm
	@I18NMsg
	public static final SOSMsgJOE	JOE_G_WeberviceForm_WebServices						= new SOSMsgJOE("JOE_G_WeberviceForm_WebServices");
	@I18NMsg
	public static final SOSMsgJOE	JOE_B_WebserviceForm_Apply							= new SOSMsgJOE("JOE_B_WebserviceForm_Apply");
	@I18NMsg
	public static final SOSMsgJOE	JOE_T_WebserviceForm_Name							= new SOSMsgJOE("JOE_T_WebserviceForm_Name");
	@I18NMsg
	public static final SOSMsgJOE	JOE_L_WebserviceForm_URL							= new SOSMsgJOE("JOE_L_WebserviceForm_URL");
	@I18NMsg
	public static final SOSMsgJOE	JOE_T_WebserviceForm_URL							= new SOSMsgJOE("JOE_T_WebserviceForm_URL");
	@I18NMsg
	public static final SOSMsgJOE	JOE_L_WebserviceForm_JobChain						= new SOSMsgJOE("JOE_L_WebserviceForm_JobChain");
	@I18NMsg
	public static final SOSMsgJOE	JOE_Cbo_WebserviceForm_JobChain						= new SOSMsgJOE("JOE_Cbo_WebserviceForm_JobChain");
	@I18NMsg
	public static final SOSMsgJOE	JOE_L_WebserviceForm_Timeout						= new SOSMsgJOE("JOE_L_WebserviceForm_Timeout");
	@I18NMsg
	public static final SOSMsgJOE	JOE_T_WebserviceForm_Timeout						= new SOSMsgJOE("JOE_T_WebserviceForm_Timeout");
	@I18NMsg
	public static final SOSMsgJOE	JOE_L_WebserviceForm_Debug							= new SOSMsgJOE("JOE_L_WebserviceForm_Debug");
	@I18NMsg
	public static final SOSMsgJOE	JOE_B_WebserviceForm_Debug							= new SOSMsgJOE("JOE_B_WebserviceForm_Debug");
	@I18NMsg
	public static final SOSMsgJOE	JOE_L_WebserviceForm_RequestXSLT					= new SOSMsgJOE("JOE_L_WebserviceForm_RequestXSLT");
	@I18NMsg
	public static final SOSMsgJOE	JOE_T_WebserviceForm_RequestXSLT					= new SOSMsgJOE("JOE_T_WebserviceForm_RequestXSLT");
	@I18NMsg
	public static final SOSMsgJOE	JOE_L_WebserviceForm_ResponseXSLT					= new SOSMsgJOE("JOE_L_WebserviceForm_ResponseXSLT");
	@I18NMsg
	public static final SOSMsgJOE	JOE_T_WebserviceForm_ResponseXSLT					= new SOSMsgJOE("JOE_T_WebserviceForm_ResponseXSLT");
	@I18NMsg
	public static final SOSMsgJOE	JOE_L_WebserviceForm_ForwardXSLT					= new SOSMsgJOE("JOE_L_WebserviceForm_ForwardXSLT");
	@I18NMsg
	public static final SOSMsgJOE	JOE_T_WebserviceForm_ForwardXSLT					= new SOSMsgJOE("JOE_T_WebserviceForm_ForwardXSLT");
	@I18NMsg
	public static final SOSMsgJOE	JOE_M_0041											= new SOSMsgJOE("JOE_M_0041");
	@I18NMsg
	public static final SOSMsgJOE	JOE_M_0042											= new SOSMsgJOE("JOE_M_0042");
	@I18NMsg
	public static final SOSMsgJOE	JOE_M_0043											= new SOSMsgJOE("JOE_M_0043");
	@I18NMsg
	public static final SOSMsgJOE	JOE_M_0044											= new SOSMsgJOE("JOE_M_0044");
	@I18NMsg
	public static final SOSMsgJOE	JOE_M_0045											= new SOSMsgJOE("JOE_M_0045");
	@I18NMsg
	public static final SOSMsgJOE	JOE_M_0046											= new SOSMsgJOE("JOE_M_0046");

	// WebservicesForm
	@I18NMsg
	public static final SOSMsgJOE	JOE_G_WebservicesForm_WebServices					= new SOSMsgJOE("JOE_G_WebservicesForm_WebServices");
	@I18NMsg
	public static final SOSMsgJOE	JOE_Tbl_WebservicesForm_Services					= new SOSMsgJOE("JOE_Tbl_WebservicesForm_Services");
	@I18NMsg
	public static final SOSMsgJOE	JOE_TCl_WebservicesForm_Name						= new SOSMsgJOE("JOE_TCl_WebservicesForm_Name");
	@I18NMsg
	public static final SOSMsgJOE	JOE_TCl_WebservicesForm_URL							= new SOSMsgJOE("JOE_TCl_WebservicesForm_URL");
	@I18NMsg
	public static final SOSMsgJOE	JOE_TCl_WebservicesForm_JobChain					= new SOSMsgJOE("JOE_TCl_WebservicesForm_JobChain");
	@I18NMsg
	public static final SOSMsgJOE	JOE_B_WebservicesForm_New							= new SOSMsgJOE("JOE_B_WebservicesForm_New");
	@I18NMsg
	public static final SOSMsgJOE	JOE_B_WebservicesForm_Remove						= new SOSMsgJOE("JOE_B_WebservicesForm_Remove");

	// ActionForm
	@I18NMsg
	public static final SOSMsgJOE	JOE_G_ActionForm_Action								= new SOSMsgJOE("JOE_G_ActionForm_Action");
	@I18NMsg
	public static final SOSMsgJOE	JOE_T_ActionForm_Name								= new SOSMsgJOE("JOE_T_ActionForm_Name");

	// ActionsForm
	@I18NMsg
	public static final SOSMsgJOE	JOE_G_ActionsForm_ActionsElements					= new SOSMsgJOE("JOE_G_ActionsForm_ActionsElements");

	// ActionsListForm
	@I18NMsg
	public static final SOSMsgJOE	JOE_G_ActionsListForm_Actions						= new SOSMsgJOE("JOE_G_ActionsListForm_Actions");
	@I18NMsg
	public static final SOSMsgJOE	JOE_Tbl_ActionsListForm_ActionsList					= new SOSMsgJOE("JOE_Tbl_ActionsListForm_ActionsList");
	@I18NMsg
	public static final SOSMsgJOE	JOE_B_ActionsListForm_New							= new SOSMsgJOE("JOE_B_ActionsListForm_New");
	@I18NMsg
	public static final SOSMsgJOE	JOE_M_ActionsListForm_NewAction						= new SOSMsgJOE("JOE_M_ActionsListForm_NewAction");
	@I18NMsg
	public static final SOSMsgJOE	JOE_B_ActionsListForm_Remove						= new SOSMsgJOE("JOE_B_ActionsListForm_Remove");

	// EventForm
	@I18NMsg
	public static final SOSMsgJOE	JOE_G_EventForm_ActionGroup							= new SOSMsgJOE("JOE_G_EventForm_ActionGroup");
	@I18NMsg
	public static final SOSMsgJOE	JOE_G_EventForm_ActionRemoveEvent					= new SOSMsgJOE("JOE_G_EventForm_ActionRemoveEvent");
	@I18NMsg
	public static final SOSMsgJOE	JOE_G_EventForm_ActionAddEvent						= new SOSMsgJOE("JOE_G_EventForm_ActionAddEvent");
	@I18NMsg
	public static final SOSMsgJOE	JOE_L_EventForm_EventName							= new SOSMsgJOE("JOE_L_EventForm_EventName");
	@I18NMsg
	public static final SOSMsgJOE	JOE_T_EventForm_EventName							= new SOSMsgJOE("JOE_T_EventForm_EventName");
	@I18NMsg
	public static final SOSMsgJOE	JOE_B_EventForm_Apply								= new SOSMsgJOE("JOE_B_EventForm_Apply");
	@I18NMsg
	public static final SOSMsgJOE	JOE_L_EventForm_EventTitle							= new SOSMsgJOE("JOE_L_EventForm_EventTitle");
	@I18NMsg
	public static final SOSMsgJOE	JOE_T_EventForm_EventTitle							= new SOSMsgJOE("JOE_T_EventForm_EventTitle");
	@I18NMsg
	public static final SOSMsgJOE	JOE_B_EventForm_New									= new SOSMsgJOE("JOE_B_EventForm_New");
	@I18NMsg
	public static final SOSMsgJOE	JOE_G_EventForm_MatchingAttributes					= new SOSMsgJOE("JOE_G_EventForm_MatchingAttributes");
	@I18NMsg
	public static final SOSMsgJOE	JOE_L_EventForm_EventClass							= new SOSMsgJOE("JOE_L_EventForm_EventClass");
	@I18NMsg
	public static final SOSMsgJOE	JOE_Cbo_EventForm_EventClass						= new SOSMsgJOE("JOE_Cbo_EventForm_EventClass");
	@I18NMsg
	public static final SOSMsgJOE	JOE_L_EventForm_EventID								= new SOSMsgJOE("JOE_L_EventForm_EventID");
	@I18NMsg
	public static final SOSMsgJOE	JOE_T_EventForm_EventID								= new SOSMsgJOE("JOE_T_EventForm_EventID");
	@I18NMsg
	public static final SOSMsgJOE	JOE_L_EventForm_JobName								= new SOSMsgJOE("JOE_L_EventForm_JobName");
	@I18NMsg
	public static final SOSMsgJOE	JOE_T_EventForm_JobName								= new SOSMsgJOE("JOE_T_EventForm_JobName");
	@I18NMsg
	public static final SOSMsgJOE	JOE_L_EventForm_JobChain							= new SOSMsgJOE("JOE_L_EventForm_JobChain");
	@I18NMsg
	public static final SOSMsgJOE	JOE_T_EventForm_JobChain							= new SOSMsgJOE("JOE_T_EventForm_JobChain");
	@I18NMsg
	public static final SOSMsgJOE	JOE_L_EventForm_OrderID								= new SOSMsgJOE("JOE_L_EventForm_OrderID");
	@I18NMsg
	public static final SOSMsgJOE	JOE_T_EventForm_OrderID								= new SOSMsgJOE("JOE_T_EventForm_OrderID");
	@I18NMsg
	public static final SOSMsgJOE	JOE_L_EventForm_ExitCode							= new SOSMsgJOE("JOE_L_EventForm_ExitCode");
	@I18NMsg
	public static final SOSMsgJOE	JOE_T_EventForm_ExitCode							= new SOSMsgJOE("JOE_T_EventForm_ExitCode");
	@I18NMsg
	public static final SOSMsgJOE	JOE_L_EventForm_Comment								= new SOSMsgJOE("JOE_L_EventForm_Comment");
	@I18NMsg
	public static final SOSMsgJOE	JOE_T_EventForm_Comment								= new SOSMsgJOE("JOE_T_EventForm_Comment");
	@I18NMsg
	public static final SOSMsgJOE	JOE_Tbl_EventForm_Events							= new SOSMsgJOE("JOE_Tbl_EventForm_Events");
	@I18NMsg
	public static final SOSMsgJOE	JOE_L_EventForm_ExpirationPeriod					= new SOSMsgJOE("JOE_L_EventForm_ExpirationPeriod");
	@I18NMsg
	public static final SOSMsgJOE	JOE_L_EventForm_ExpirationCycle						= new SOSMsgJOE("JOE_L_EventForm_ExpirationCycle");
	@I18NMsg
	public static final SOSMsgJOE	JOE_B_EventForm_Remove								= new SOSMsgJOE("JOE_B_EventForm_Remove");
	@I18NMsg
	public static final SOSMsgJOE	JOE_M_EventForm_RemoveGroup							= new SOSMsgJOE("JOE_M_EventForm_RemoveGroup");
	@I18NMsg
	public static final SOSMsgJOE	JOE_M_EventForm_RemoveCommand						= new SOSMsgJOE("JOE_M_EventForm_RemoveCommand");
	@I18NMsg
	public static final SOSMsgJOE	JOE_T_EventForm_HourExpirationPeriod				= new SOSMsgJOE("JOE_T_EventForm_HourExpirationPeriod");
	@I18NMsg
	public static final SOSMsgJOE	JOE_T_EventForm_MinExpirationPeriod					= new SOSMsgJOE("JOE_T_EventForm_MinExpirationPeriod");
	@I18NMsg
	public static final SOSMsgJOE	JOE_T_EventForm_SecExpirationPeriod					= new SOSMsgJOE("JOE_T_EventForm_SecExpirationPeriod");
	@I18NMsg
	public static final SOSMsgJOE	JOE_T_EventForm_HourExpirationCycle					= new SOSMsgJOE("JOE_T_EventForm_HourExpirationCycle");
	@I18NMsg
	public static final SOSMsgJOE	JOE_T_EventForm_MinExpirationCycle					= new SOSMsgJOE("JOE_T_EventForm_MinExpirationCycle");
	@I18NMsg
	public static final SOSMsgJOE	JOE_T_EventForm_SecExpirationCycle					= new SOSMsgJOE("JOE_T_EventForm_SecExpirationCycle");

	// EventsForm
	@I18NMsg
	public static final SOSMsgJOE	JOE_G_EventsForm_Action								= new SOSMsgJOE("JOE_G_EventsForm_Action");
	@I18NMsg
	public static final SOSMsgJOE	JOE_L_EventsForm_Logic								= new SOSMsgJOE("JOE_L_EventsForm_Logic");
	@I18NMsg
	public static final SOSMsgJOE	JOE_T_EventsForm_Logic								= new SOSMsgJOE("JOE_T_EventsForm_Logic");
	@I18NMsg
	public static final SOSMsgJOE	JOE_B_EventsForm_Operation							= new SOSMsgJOE("JOE_B_EventsForm_Operation");
	@I18NMsg
	public static final SOSMsgJOE	JOE_G_EventsForm_EventsGroup						= new SOSMsgJOE("JOE_G_EventsForm_EventsGroup");
	@I18NMsg
	public static final SOSMsgJOE	JOE_L_EventsForm_Group								= new SOSMsgJOE("JOE_L_EventsForm_Group");
	@I18NMsg
	public static final SOSMsgJOE	JOE_T_EventsForm_Group								= new SOSMsgJOE("JOE_T_EventsForm_Group");
	@I18NMsg
	public static final SOSMsgJOE	JOE_B_EventsForm_Apply								= new SOSMsgJOE("JOE_B_EventsForm_Apply");
	@I18NMsg
	public static final SOSMsgJOE	JOE_T_EventsForm_LogicGroup							= new SOSMsgJOE("JOE_T_EventsForm_LogicGroup");
	@I18NMsg
	public static final SOSMsgJOE	JOE_B_EventsForm_New								= new SOSMsgJOE("JOE_B_EventsForm_New");
	@I18NMsg
	public static final SOSMsgJOE	JOE_L_EventsForm_EventClass							= new SOSMsgJOE("JOE_L_EventsForm_EventClass");
	@I18NMsg
	public static final SOSMsgJOE	JOE_Cbo_EventsForm_EventClass						= new SOSMsgJOE("JOE_Cbo_EventsForm_EventClass");
	@I18NMsg
	public static final SOSMsgJOE	JOE_B_EventsForm_Remove								= new SOSMsgJOE("JOE_B_EventsForm_Remove");
	@I18NMsg
	public static final SOSMsgJOE	JOE_M_EventsForm_RemoveGroup						= new SOSMsgJOE("JOE_M_EventsForm_RemoveGroup");
	@I18NMsg
	public static final SOSMsgJOE	JOE_Tbl_EventsForm_Groups							= new SOSMsgJOE("JOE_Tbl_EventsForm_Groups");
	@I18NMsg
	public static final SOSMsgJOE	JOE_TCl_EventsForm_Group							= new SOSMsgJOE("JOE_TCl_EventsForm_Group");
	@I18NMsg
	public static final SOSMsgJOE	JOE_TCl_EventsForm_Logic							= new SOSMsgJOE("JOE_TCl_EventsForm_Logic");
	@I18NMsg
	public static final SOSMsgJOE	JOE_TCl_EventsForm_EventClass						= new SOSMsgJOE("JOE_TCl_EventsForm_EventClass");

	// Actions JobCommandForm
	@I18NMsg
	public static final SOSMsgJOE	JOE_G_ActionsJobCommandForm_JobsOrders				= new SOSMsgJOE("JOE_G_ActionsJobCommandForm_JobsOrders");
	@I18NMsg
	public static final SOSMsgJOE	JOE_L_ActionsJobCommandForm_JobChain				= new SOSMsgJOE("JOE_L_ActionsJobCommandForm_JobChain");
	@I18NMsg
	public static final SOSMsgJOE	JOE_Cbo_ActionsJobCommandForm_JobChain				= new SOSMsgJOE("JOE_Cbo_ActionsJobCommandForm_JobChain");
	@I18NMsg
	public static final SOSMsgJOE	JOE_T_ActionsJobCommandForm_Job						= new SOSMsgJOE("JOE_T_ActionsJobCommandForm_Job");
	@I18NMsg
	public static final SOSMsgJOE	JOE_L_ActionsJobCommandForm_StartAt					= new SOSMsgJOE("JOE_L_ActionsJobCommandForm_StartAt");
	@I18NMsg
	public static final SOSMsgJOE	JOE_T_ActionsJobCommandForm_Year					= new SOSMsgJOE("JOE_T_ActionsJobCommandForm_Year");
	@I18NMsg
	public static final SOSMsgJOE	JOE_L_Hyphen										= new SOSMsgJOE("JOE_L_Hyphen");
	@I18NMsg
	public static final SOSMsgJOE	JOE_T_ActionsJobCommandForm_Month					= new SOSMsgJOE("JOE_T_ActionsJobCommandForm_Month");
	@I18NMsg
	public static final SOSMsgJOE	JOE_T_ActionsJobCommandForm_Day						= new SOSMsgJOE("JOE_T_ActionsJobCommandForm_Day");
	@I18NMsg
	public static final SOSMsgJOE	JOE_T_ActionsJobCommandForm_Hour					= new SOSMsgJOE("JOE_T_ActionsJobCommandForm_Hour");
	@I18NMsg
	public static final SOSMsgJOE	JOE_T_ActionsJobCommandForm_Min						= new SOSMsgJOE("JOE_T_ActionsJobCommandForm_Min");
	@I18NMsg
	public static final SOSMsgJOE	JOE_T_ActionsJobCommandForm_Sec						= new SOSMsgJOE("JOE_T_ActionsJobCommandForm_Sec");
	@I18NMsg
	public static final SOSMsgJOE	JOE_Cbo_ActionsJobCommandForm_Times					= new SOSMsgJOE("JOE_Cbo_ActionsJobCommandForm_Times");
	@I18NMsg
	public static final SOSMsgJOE	JOE_L_ActionsJobCommandForm_Priority				= new SOSMsgJOE("JOE_L_ActionsJobCommandForm_Priority");
	@I18NMsg
	public static final SOSMsgJOE	JOE_T_ActionsJobCommandForm_Priority				= new SOSMsgJOE("JOE_T_ActionsJobCommandForm_Priority");
	@I18NMsg
	public static final SOSMsgJOE	JOE_L_ActionsJobCommandForm_Title					= new SOSMsgJOE("JOE_L_ActionsJobCommandForm_Title");
	@I18NMsg
	public static final SOSMsgJOE	JOE_T_ActionsJobCommandForm_Title					= new SOSMsgJOE("JOE_T_ActionsJobCommandForm_Title");
	@I18NMsg
	public static final SOSMsgJOE	JOE_L_ActionsJobCommandForm_State					= new SOSMsgJOE("JOE_L_ActionsJobCommandForm_State");
	@I18NMsg
	public static final SOSMsgJOE	JOE_T_ActionsJobCommandForm_State					= new SOSMsgJOE("JOE_T_ActionsJobCommandForm_State");
	@I18NMsg
	public static final SOSMsgJOE	JOE_L_ActionsJobCommandForm_EndState				= new SOSMsgJOE("JOE_L_ActionsJobCommandForm_EndState");
	@I18NMsg
	public static final SOSMsgJOE	JOE_Cbo_ActionsJobCommandForm_EndState				= new SOSMsgJOE("JOE_Cbo_ActionsJobCommandForm_EndState");
	@I18NMsg
	public static final SOSMsgJOE	JOE_L_ActionsJobCommandForm_Replace					= new SOSMsgJOE("JOE_L_ActionsJobCommandForm_Replace");
	@I18NMsg
	public static final SOSMsgJOE	JOE_B_ActionsJobCommandForm_Replace					= new SOSMsgJOE("JOE_B_ActionsJobCommandForm_Replace");
	@I18NMsg
	public static final SOSMsgJOE	JOE_L_ActionsJobCommandForm_Job						= new SOSMsgJOE("JOE_L_ActionsJobCommandForm_Job");
	@I18NMsg
	public static final SOSMsgJOE	JOE_L_ActionsJobCommandForm_OrderID					= new SOSMsgJOE("JOE_L_ActionsJobCommandForm_OrderID");

	// JobCommandNamesForm
	@I18NMsg
	public static final SOSMsgJOE	JOE_G_JobCommandNamesForm_Command					= new SOSMsgJOE("JOE_G_JobCommandNamesForm_Command");
	@I18NMsg
	public static final SOSMsgJOE	JOE_L_JobCommandNamesForm_Name						= new SOSMsgJOE("JOE_L_JobCommandNamesForm_Name");
	@I18NMsg
	public static final SOSMsgJOE	JOE_T_JobCommandNamesForm_Name						= new SOSMsgJOE("JOE_T_JobCommandNamesForm_Name");
	@I18NMsg
	public static final SOSMsgJOE	JOE_B_JobCommandNamesForm_AddJob					= new SOSMsgJOE("JOE_B_JobCommandNamesForm_AddJob");
	@I18NMsg
	public static final SOSMsgJOE	JOE_L_JobCommandNamesForm_SchedulerHost				= new SOSMsgJOE("JOE_L_JobCommandNamesForm_SchedulerHost");
	@I18NMsg
	public static final SOSMsgJOE	JOE_T_JobCommandNamesForm_SchedulerHost				= new SOSMsgJOE("JOE_T_JobCommandNamesForm_SchedulerHost");
	@I18NMsg
	public static final SOSMsgJOE	JOE_B_JobCommandNamesForm_AddOrder					= new SOSMsgJOE("JOE_B_JobCommandNamesForm_AddOrder");
	@I18NMsg
	public static final SOSMsgJOE	JOE_L_JobCommandNamesForm_SchedulerPort				= new SOSMsgJOE("JOE_L_JobCommandNamesForm_SchedulerPort");
	@I18NMsg
	public static final SOSMsgJOE	JOE_T_JobCommandNamesForm_SchedulerPort				= new SOSMsgJOE("JOE_T_JobCommandNamesForm_SchedulerPort");
	@I18NMsg
	public static final SOSMsgJOE	JOE_Tbl_JobCommandNamesForm_Commands				= new SOSMsgJOE("JOE_Tbl_JobCommandNamesForm_Commands");
	@I18NMsg
	public static final SOSMsgJOE	JOE_TCl_JobCommandNamesForm_Command					= new SOSMsgJOE("JOE_TCl_JobCommandNamesForm_Command");
	@I18NMsg
	public static final SOSMsgJOE	JOE_TCl_JobCommandNamesForm_JobID					= new SOSMsgJOE("JOE_TCl_JobCommandNamesForm_JobID");
	@I18NMsg
	public static final SOSMsgJOE	JOE_TCl_JobCommandNamesForm_JobChain				= new SOSMsgJOE("JOE_TCl_JobCommandNamesForm_JobChain");
	@I18NMsg
	public static final SOSMsgJOE	JOE_TCl_JobCommandNamesForm_StartAt					= new SOSMsgJOE("JOE_TCl_JobCommandNamesForm_StartAt");
	@I18NMsg
	public static final SOSMsgJOE	JOE_B_JobCommandNamesForm_RemoveExitCode			= new SOSMsgJOE("JOE_B_JobCommandNamesForm_RemoveExitCode");

	// ActionsJobCommandsForm
	@I18NMsg
	public static final SOSMsgJOE	JOE_G_ActionsJobCommandsForm_Commands				= new SOSMsgJOE("JOE_G_ActionsJobCommandsForm_Commands");
	@I18NMsg
	public static final SOSMsgJOE	JOE_B_ActionsJobCommandsForm_NewCommand				= new SOSMsgJOE("JOE_B_ActionsJobCommandsForm_NewCommand");
	@I18NMsg
	public static final SOSMsgJOE	JOE_B_ActionsJobCommandsForm_RemoveCommand			= new SOSMsgJOE("JOE_B_ActionsJobCommandsForm_RemoveCommand");
	@I18NMsg
	public static final SOSMsgJOE	JOE_Tbl_ActionsJobCommandsForm_Commands				= new SOSMsgJOE("JOE_Tbl_ActionsJobCommandsForm_Commands");
	@I18NMsg
	public static final SOSMsgJOE	JOE_TCl_ActionsJobCommandsForm_Command				= new SOSMsgJOE("JOE_TCl_ActionsJobCommandsForm_Command");
	@I18NMsg
	public static final SOSMsgJOE	JOE_TCl_ActionsJobCommandsForm_Host					= new SOSMsgJOE("JOE_TCl_ActionsJobCommandsForm_Host");
	@I18NMsg
	public static final SOSMsgJOE	JOE_TCl_ActionsJobCommandsForm_Port					= new SOSMsgJOE("JOE_TCl_ActionsJobCommandsForm_Port");

	// LogicOperationDialog
	@I18NMsg
	public static final SOSMsgJOE	JOE_M_LogicOperationDialog_LogicalOperation			= new SOSMsgJOE("JOE_M_LogicOperationDialog_LogicalOperation");
	@I18NMsg
	public static final SOSMsgJOE	JOE_T_LogicOperationDialog_Expression				= new SOSMsgJOE("JOE_T_LogicOperationDialog_Expression");
	@I18NMsg
	public static final SOSMsgJOE	JOE_Lst_LogicOperationDialog_Operators				= new SOSMsgJOE("JOE_Lst_LogicOperationDialog_Operators");
	@I18NMsg
	public static final SOSMsgJOE	JOE_Lst_LogicOperationDialog_Group					= new SOSMsgJOE("JOE_Lst_LogicOperationDialog_Group");
	@I18NMsg
	public static final SOSMsgJOE	JOE_B_LogicOperationDialog_Cancel					= new SOSMsgJOE("JOE_B_LogicOperationDialog_Cancel");
	@I18NMsg
	public static final SOSMsgJOE	JOE_B_LogicOperationDialog_Restore					= new SOSMsgJOE("JOE_B_LogicOperationDialog_Restore");
	@I18NMsg
	public static final SOSMsgJOE	JOE_B_LogicOperationDialog_Clear					= new SOSMsgJOE("JOE_B_LogicOperationDialog_Clear");
	@I18NMsg
	public static final SOSMsgJOE	JOE_B_LogicOperationDialog_Apply					= new SOSMsgJOE("JOE_B_LogicOperationDialog_Apply");

	// ActionsParameterForm
	@I18NMsg
	public static final SOSMsgJOE	JOE_G_ActionsParameterForm_Parameter				= new SOSMsgJOE("JOE_G_ActionsParameterForm_Parameter");
	@I18NMsg
	public static final SOSMsgJOE	JOE_TI_ActionsParameterForm_Includes				= new SOSMsgJOE("JOE_TI_ActionsParameterForm_Includes");
	@I18NMsg
	public static final SOSMsgJOE	JOE_B_ActionsParameterForm_IsLifeFile				= new SOSMsgJOE("JOE_B_ActionsParameterForm_IsLifeFile");
	@I18NMsg
	public static final SOSMsgJOE	JOE_L_ActionsParameterForm_File						= new SOSMsgJOE("JOE_L_ActionsParameterForm_File");
	@I18NMsg
	public static final SOSMsgJOE	JOE_T_ActionsParameterForm_IncludeFilename			= new SOSMsgJOE("JOE_T_ActionsParameterForm_IncludeFilename");
	@I18NMsg
	public static final SOSMsgJOE	JOE_L_ActionsParameterForm_Node						= new SOSMsgJOE("JOE_L_ActionsParameterForm_Node");
	@I18NMsg
	public static final SOSMsgJOE	JOE_T_ActionsParameterForm_IncludeNode				= new SOSMsgJOE("JOE_T_ActionsParameterForm_IncludeNode");
	@I18NMsg
	public static final SOSMsgJOE	JOE_B_ActionsParameterForm_IncludesApply			= new SOSMsgJOE("JOE_B_ActionsParameterForm_IncludesApply");
	@I18NMsg
	public static final SOSMsgJOE	JOE_Tbl_ActionsParameterForm_IncludeParams			= new SOSMsgJOE("JOE_Tbl_ActionsParameterForm_IncludeParams");
	@I18NMsg
	public static final SOSMsgJOE	JOE_TCl_ActionsParameterForm_File					= new SOSMsgJOE("JOE_TCl_ActionsParameterForm_File");
	@I18NMsg
	public static final SOSMsgJOE	JOE_TCl_ActionsParameterForm_Node					= new SOSMsgJOE("JOE_TCl_ActionsParameterForm_Node");
	@I18NMsg
	public static final SOSMsgJOE	JOE_TCl_ActionsParameterForm_FileLifeFile			= new SOSMsgJOE("JOE_TCl_ActionsParameterForm_FileLifeFile");
	@I18NMsg
	public static final SOSMsgJOE	JOE_B_ActionsParameterForm_NewIncludes				= new SOSMsgJOE("JOE_B_ActionsParameterForm_NewIncludes");
	@I18NMsg
	public static final SOSMsgJOE	JOE_B_ActionsParameterForm_RemoveInclude			= new SOSMsgJOE("JOE_B_ActionsParameterForm_RemoveInclude");
	@I18NMsg
	public static final SOSMsgJOE	JOE_TI_ActionsParameterForm_Parameter				= new SOSMsgJOE("JOE_TI_ActionsParameterForm_Parameter");
	@I18NMsg
	public static final SOSMsgJOE	JOE_L_ActionsParameterForm_Name						= new SOSMsgJOE("JOE_L_ActionsParameterForm_Name");
	@I18NMsg
	public static final SOSMsgJOE	JOE_T_ActionsParameterForm_Name						= new SOSMsgJOE("JOE_T_ActionsParameterForm_Name");
	@I18NMsg
	public static final SOSMsgJOE	JOE_L_ActionsParameterForm_Value					= new SOSMsgJOE("JOE_L_ActionsParameterForm_Value");
	@I18NMsg
	public static final SOSMsgJOE	JOE_Cbo_ActionsParameterForm_Source					= new SOSMsgJOE("JOE_Cbo_ActionsParameterForm_Source");
	@I18NMsg
	public static final SOSMsgJOE	JOE_T_ActionsParameterForm_Value					= new SOSMsgJOE("JOE_T_ActionsParameterForm_Value");
	@I18NMsg
	public static final SOSMsgJOE	JOE_B_ActionsParameterForm_Apply					= new SOSMsgJOE("JOE_B_ActionsParameterForm_Apply");
	@I18NMsg
	public static final SOSMsgJOE	JOE_Tbl_ActionsParameterForm_Parameter				= new SOSMsgJOE("JOE_Tbl_ActionsParameterForm_Parameter");
	@I18NMsg
	public static final SOSMsgJOE	JOE_TCl_ActionsParameterForm_Name					= new SOSMsgJOE("JOE_TCl_ActionsParameterForm_Name");
	@I18NMsg
	public static final SOSMsgJOE	JOE_TCl_ActionsParameterForm_Value					= new SOSMsgJOE("JOE_TCl_ActionsParameterForm_Value");
	@I18NMsg
	public static final SOSMsgJOE	JOE_B_ActionsParameterForm_NewParam					= new SOSMsgJOE("JOE_B_ActionsParameterForm_NewParam");
	@I18NMsg
	public static final SOSMsgJOE	JOE_B_ActionsParameterForm_Remove					= new SOSMsgJOE("JOE_B_ActionsParameterForm_Remove");
	@I18NMsg
	public static final SOSMsgJOE	JOE_B_ActionsParameterForm_Parameter				= new SOSMsgJOE("JOE_B_ActionsParameterForm_Parameter");
	@I18NMsg
	public static final SOSMsgJOE	JOE_B_ActionsParameterForm_FromTask					= new SOSMsgJOE("JOE_B_ActionsParameterForm_FromTask");
	@I18NMsg
	public static final SOSMsgJOE	JOE_B_ActionsParameterForm_FromOrder				= new SOSMsgJOE("JOE_B_ActionsParameterForm_FromOrder");

	// SaveEventsDialogForm
	@I18NMsg
	public static final SOSMsgJOE	JOE_M_SaveEventhandler								= new SOSMsgJOE("JOE_M_SaveEventhandler");
	@I18NMsg
	public static final SOSMsgJOE	JOE_G_SaveEventsDialogForm_NameSpec					= new SOSMsgJOE("JOE_G_SaveEventsDialogForm_NameSpec");
	@I18NMsg
	public static final SOSMsgJOE	JOE_T_SaveEventsDialogForm_Name						= new SOSMsgJOE("JOE_T_SaveEventsDialogForm_Name");
	@I18NMsg
	public static final SOSMsgJOE	JOE_L_SaveEventsDialogForm_JobChain					= new SOSMsgJOE("JOE_L_SaveEventsDialogForm_JobChain");
	@I18NMsg
	public static final SOSMsgJOE	JOE_T_SaveEventsDialogForm_JobChain					= new SOSMsgJOE("JOE_T_SaveEventsDialogForm_JobChain");
	@I18NMsg
	public static final SOSMsgJOE	JOE_L_SaveEventsDialogForm_Job						= new SOSMsgJOE("JOE_L_SaveEventsDialogForm_Job");
	@I18NMsg
	public static final SOSMsgJOE	JOE_T_SaveEventsDialogForm_Job						= new SOSMsgJOE("JOE_T_SaveEventsDialogForm_Job");
	@I18NMsg
	public static final SOSMsgJOE	JOE_L_SaveEventsDialogForm_EventClass				= new SOSMsgJOE("JOE_L_SaveEventsDialogForm_EventClass");
	@I18NMsg
	public static final SOSMsgJOE	JOE_T_SaveEventsDialogForm_EventClass				= new SOSMsgJOE("JOE_T_SaveEventsDialogForm_EventClass");
	@I18NMsg
	public static final SOSMsgJOE	JOE_B_SaveEventsDialogForm_Directory				= new SOSMsgJOE("JOE_B_SaveEventsDialogForm_Directory");
	@I18NMsg
	public static final SOSMsgJOE	JOE_B_SaveEventsDialogForm_Save						= new SOSMsgJOE("JOE_B_SaveEventsDialogForm_Save");
	@I18NMsg
	public static final SOSMsgJOE	JOE_M_OverwriteFile									= new SOSMsgJOE("JOE_M_OverwriteFile");
	@I18NMsg
	public static final SOSMsgJOE	JOE_B_SaveEventsDialogForm_Cancel					= new SOSMsgJOE("JOE_B_SaveEventsDialogForm_Cancel");

	// JobDocumentation
	@I18NMsg
	public static final SOSMsgJOE	JOE_G_JobDocumentation_JobDescription				= new SOSMsgJOE("JOE_G_JobDocumentation_JobDescription");
	@I18NMsg
	public static final SOSMsgJOE	JOE_T_JobDocumentation_FileName						= new SOSMsgJOE("JOE_T_JobDocumentation_FileName");
	@I18NMsg
	public static final SOSMsgJOE	JOE_B_JobDocumentation_IsLiveFile					= new SOSMsgJOE("JOE_B_JobDocumentation_IsLiveFile");
	@I18NMsg
	public static final SOSMsgJOE	JOE_M_0050											= new SOSMsgJOE("JOE_M_0050");
	@I18NMsg
	public static final SOSMsgJOE	JOE_M_0051											= new SOSMsgJOE("JOE_M_0051");
	@I18NMsg
	public static final SOSMsgJOE	JOE_B_JobDocumentation_Show							= new SOSMsgJOE("JOE_B_JobDocumentation_Show");
	@I18NMsg
	public static final SOSMsgJOE	JOE_B_JobDocumentation_Open							= new SOSMsgJOE("JOE_B_JobDocumentation_Open");
	@I18NMsg
	public static final SOSMsgJOE	JOE_M_JobDocumentation_NoDoc						= new SOSMsgJOE("JOE_M_JobDocumentation_NoDoc");

	// JobEmailSettings
	@I18NMsg
	public static final SOSMsgJOE	JOE_G_JobEmailSettings_Notifications				= new SOSMsgJOE("JOE_G_JobEmailSettings_Notifications");

	// JobIncludeFile
	@I18NMsg
	public static final SOSMsgJOE	JOE_G_JobIncludeFile_IncludeFiles					= new SOSMsgJOE("JOE_G_JobIncludeFile_IncludeFiles");
	@I18NMsg
	public static final SOSMsgJOE	JOE_B_JobIncludeFile_Add							= new SOSMsgJOE("JOE_B_JobIncludeFile_Add");
	@I18NMsg
	public static final SOSMsgJOE	JOE_Tbl_JobIncludeFile_Includes						= new SOSMsgJOE("JOE_Tbl_JobIncludeFile_Includes");
	@I18NMsg
	public static final SOSMsgJOE	JOE_TCl_JobIncludeFile_Name							= new SOSMsgJOE("JOE_TCl_JobIncludeFile_Name");
	@I18NMsg
	public static final SOSMsgJOE	JOE_TCl_JobIncludeFile_FileLiveFile					= new SOSMsgJOE("JOE_TCl_JobIncludeFile_FileLiveFile");
	@I18NMsg
	public static final SOSMsgJOE	JOE_B_JobIncludeFile_New							= new SOSMsgJOE("JOE_B_JobIncludeFile_New");
	@I18NMsg
	public static final SOSMsgJOE	JOE_B_JobIncludeFile_Remove							= new SOSMsgJOE("JOE_B_JobIncludeFile_Remove");

	// JobJavaAPI
	@I18NMsg
	public static final SOSMsgJOE	JOE_L_JobJavaAPI_Classname							= new SOSMsgJOE("JOE_L_JobJavaAPI_Classname");
	@I18NMsg
	public static final SOSMsgJOE	JOE_T_JobJavaAPI_Classname							= new SOSMsgJOE("JOE_T_JobJavaAPI_Classname");
	@I18NMsg
	public static final SOSMsgJOE	JOE_L_JobJavaAPI_Classpath							= new SOSMsgJOE("JOE_L_JobJavaAPI_Classpath");
	@I18NMsg
	public static final SOSMsgJOE	JOE_T_JobJavaAPI_Classpath							= new SOSMsgJOE("JOE_T_JobJavaAPI_Classpath");
	@I18NMsg
	public static final SOSMsgJOE	JOE_L_JobJavaAPI_Options							= new SOSMsgJOE("JOE_L_JobJavaAPI_Options");
	@I18NMsg
	public static final SOSMsgJOE	JOE_T_JobJavaAPI_Options							= new SOSMsgJOE("JOE_T_JobJavaAPI_Options");

	// JobOptions
	@I18NMsg
	public static final SOSMsgJOE	JOE_G_JobOptions_Options							= new SOSMsgJOE("JOE_G_JobOptions_Options");
	@I18NMsg
	public static final SOSMsgJOE	JOE_L_JobOptions_JobChainJob						= new SOSMsgJOE("JOE_L_JobOptions_JobChainJob");
	@I18NMsg
	public static final SOSMsgJOE	JOE_B_JobOptions_OrderYes							= new SOSMsgJOE("JOE_B_JobOptions_OrderYes");
	@I18NMsg
	public static final SOSMsgJOE	JOE_B_JobOptions_OrderNo							= new SOSMsgJOE("JOE_B_JobOptions_OrderNo");
	@I18NMsg
	public static final SOSMsgJOE	JOE_L_JobOptions_StopOnError						= new SOSMsgJOE("JOE_L_JobOptions_StopOnError");
	@I18NMsg
	public static final SOSMsgJOE	JOE_B_JobOptions_StopOnError						= new SOSMsgJOE("JOE_B_JobOptions_StopOnError");

	// JobProcessFile
	@I18NMsg
	public static final SOSMsgJOE	JOE_G_JobProcessFile_RunExecutable					= new SOSMsgJOE("JOE_G_JobProcessFile_RunExecutable");
	@I18NMsg
	public static final SOSMsgJOE	JOE_L_JobProcessFile_File							= new SOSMsgJOE("JOE_L_JobProcessFile_File");
	@I18NMsg
	public static final SOSMsgJOE	JOE_T_JobProcessFile_File							= new SOSMsgJOE("JOE_T_JobProcessFile_File");
	@I18NMsg
	public static final SOSMsgJOE	JOE_L_JobProcessFile_Parameter						= new SOSMsgJOE("JOE_L_JobProcessFile_Parameter");
	@I18NMsg
	public static final SOSMsgJOE	JOE_T_JobProcessFile_Parameter						= new SOSMsgJOE("JOE_T_JobProcessFile_Parameter");
	@I18NMsg
	public static final SOSMsgJOE	JOE_L_JobProcessFile_LogFile						= new SOSMsgJOE("JOE_L_JobProcessFile_LogFile");
	@I18NMsg
	public static final SOSMsgJOE	JOE_T_JobProcessFile_LogFile						= new SOSMsgJOE("JOE_T_JobProcessFile_LogFile");
	@I18NMsg
	public static final SOSMsgJOE	JOE_L_JobProcessFile_Ignore							= new SOSMsgJOE("JOE_L_JobProcessFile_Ignore");
	@I18NMsg
	public static final SOSMsgJOE	JOE_B_JobProcessFile_IgnoreSignal					= new SOSMsgJOE("JOE_B_JobProcessFile_IgnoreSignal");
	@I18NMsg
	public static final SOSMsgJOE	JOE_B_JobProcessFile_IgnoreError					= new SOSMsgJOE("JOE_B_JobProcessFile_IgnoreError");

	// JobScript
	@I18NMsg
	public static final SOSMsgJOE	JOE_G_JobScript_Executable							= new SOSMsgJOE("JOE_G_JobScript_Executable");
	@I18NMsg
	public static final SOSMsgJOE	JOE_L_JobScript_PredefinedFunctions					= new SOSMsgJOE("JOE_L_JobScript_PredefinedFunctions");
	@I18NMsg
	public static final SOSMsgJOE	JOE_Cbo_JobScript_PredefinedFunctions				= new SOSMsgJOE("JOE_Cbo_JobScript_PredefinedFunctions");
	@I18NMsg
	public static final SOSMsgJOE	JOE_L_JobSetback_TimeFormat							= new SOSMsgJOE("JOE_L_JobSetback_TimeFormat");

	// JobSourceViewer
	@I18NMsg
	public static final SOSMsgJOE	JOE_G_JobSourceViewer_SourceViewer					= new SOSMsgJOE("JOE_G_JobSourceViewer_SourceViewer");

	// PrePostProcessingForm
	@I18NMsg
	public static final SOSMsgJOE	JOE_G_PrePostProcessingForm_Executable				= new SOSMsgJOE("JOE_G_PrePostProcessingForm_Executable");
	@I18NMsg
	public static final SOSMsgJOE	JOE_L_PrePostProcessingForm_Language				= new SOSMsgJOE("JOE_L_PrePostProcessingForm_Language");
	// ApplicationsForm
	@I18NMsg
	public static final SOSMsgJOE	JOE_G_ApplicationsForm_Applications					= new SOSMsgJOE("JOE_G_ApplicationsForm_Applications");
	@I18NMsg
	public static final SOSMsgJOE	JOE_T_ApplicationsForm_Name							= new SOSMsgJOE("JOE_T_ApplicationsForm_Name");
	@I18NMsg
	public static final SOSMsgJOE	JOE_B_ApplicationsForm_ApplyApplication				= new SOSMsgJOE("JOE_B_ApplicationsForm_ApplyApplication");
	@I18NMsg
	public static final SOSMsgJOE	JOE_L_ApplicationsForm_ID							= new SOSMsgJOE("JOE_L_ApplicationsForm_ID");
	@I18NMsg
	public static final SOSMsgJOE	JOE_T_ApplicationsForm_ID							= new SOSMsgJOE("JOE_T_ApplicationsForm_ID");
	@I18NMsg
	public static final SOSMsgJOE	JOE_L_ApplicationsForm_Reference					= new SOSMsgJOE("JOE_L_ApplicationsForm_Reference");
	@I18NMsg
	public static final SOSMsgJOE	JOE_Cbo_ApplicationsForm_Reference					= new SOSMsgJOE("JOE_Cbo_ApplicationsForm_Reference");
	@I18NMsg
	public static final SOSMsgJOE	JOE_Tbl_ApplicationsForm_Applications				= new SOSMsgJOE("JOE_Tbl_ApplicationsForm_Applications");
	@I18NMsg
	public static final SOSMsgJOE	JOE_TCl_ApplicationsForm_Name						= new SOSMsgJOE("JOE_TCl_ApplicationsForm_Name");
	@I18NMsg
	public static final SOSMsgJOE	JOE_TCl_ApplicationsForm_ID							= new SOSMsgJOE("JOE_TCl_ApplicationsForm_ID");
	@I18NMsg
	public static final SOSMsgJOE	JOE_TCl_ApplicationsForm_Reference					= new SOSMsgJOE("JOE_TCl_ApplicationsForm_Reference");
	@I18NMsg
	public static final SOSMsgJOE	JOE_B_ApplicationsForm_NewApplication				= new SOSMsgJOE("JOE_B_ApplicationsForm_NewApplication");
	@I18NMsg
	public static final SOSMsgJOE	JOE_B_ApplicationsForm_RemoveApplication			= new SOSMsgJOE("JOE_B_ApplicationsForm_RemoveApplication");

	// AuthorsForm
	@I18NMsg
	public static final SOSMsgJOE	JOE_G_AuthorsForm_Authors							= new SOSMsgJOE("JOE_G_AuthorsForm_Authors");
	@I18NMsg
	public static final SOSMsgJOE	JOE_T_AuthorsForm_Name								= new SOSMsgJOE("JOE_T_AuthorsForm_Name");
	@I18NMsg
	public static final SOSMsgJOE	JOE_L_AuthorsForm_EMail								= new SOSMsgJOE("JOE_L_AuthorsForm_EMail");
	@I18NMsg
	public static final SOSMsgJOE	JOE_T_AuthorsForm_EMail								= new SOSMsgJOE("JOE_T_AuthorsForm_EMail");
	@I18NMsg
	public static final SOSMsgJOE	JOE_B_AuthorsForm_Apply								= new SOSMsgJOE("JOE_B_AuthorsForm_Apply");
	@I18NMsg
	public static final SOSMsgJOE	JOE_Tbl_AuthorsForm_Authors							= new SOSMsgJOE("JOE_Tbl_AuthorsForm_Authors");
	@I18NMsg
	public static final SOSMsgJOE	JOE_TCl_AuthorsForm_Name							= new SOSMsgJOE("JOE_TCl_AuthorsForm_Name");
	@I18NMsg
	public static final SOSMsgJOE	JOE_TCl_AuthorsForm_EMail							= new SOSMsgJOE("JOE_TCl_AuthorsForm_EMail");
	@I18NMsg
	public static final SOSMsgJOE	JOE_B_AuthorsForm_Remove							= new SOSMsgJOE("JOE_B_AuthorsForm_Remove");

	// ConnectionsForm
	@I18NMsg
	public static final SOSMsgJOE	JOE_G_ConnectionsForm_Connections					= new SOSMsgJOE("JOE_G_ConnectionsForm_Connections");
	@I18NMsg
	public static final SOSMsgJOE	JOE_T_ConnectionsForm_Name							= new SOSMsgJOE("JOE_T_ConnectionsForm_Name");
	@I18NMsg
	public static final SOSMsgJOE	JOE_B_ConnectionsForm_Notes							= new SOSMsgJOE("JOE_B_ConnectionsForm_Notes");
	@I18NMsg
	public static final SOSMsgJOE	JOE_B_ConnectionsForm_Apply							= new SOSMsgJOE("JOE_B_ConnectionsForm_Apply");
	@I18NMsg
	public static final SOSMsgJOE	JOE_Tbl_ConnectionsForm_Connections					= new SOSMsgJOE("JOE_Tbl_ConnectionsForm_Connections");
	@I18NMsg
	public static final SOSMsgJOE	JOE_TCl_ConnectionsForm_Name						= new SOSMsgJOE("JOE_TCl_ConnectionsForm_Name");
	@I18NMsg
	public static final SOSMsgJOE	JOE_B_ConnectionsForm_New							= new SOSMsgJOE("JOE_B_ConnectionsForm_New");
	@I18NMsg
	public static final SOSMsgJOE	JOE_B_ConnectionsForm_Remove						= new SOSMsgJOE("JOE_B_ConnectionsForm_Remove");

	// DatabaseResourcesForm
	@I18NMsg
	public static final SOSMsgJOE	JOE_G_DBResources_Resources							= new SOSMsgJOE("JOE_G_DBResources_Resources");
	@I18NMsg
	public static final SOSMsgJOE	JOE_T_DBResources_Name								= new SOSMsgJOE("JOE_T_DBResources_Name");
	@I18NMsg
	public static final SOSMsgJOE	JOE_B_DBResources_Apply								= new SOSMsgJOE("JOE_B_DBResources_Apply");
	@I18NMsg
	public static final SOSMsgJOE	JOE_Tbl_DBResources_Resources						= new SOSMsgJOE("JOE_Tbl_DBResources_Resources");
	@I18NMsg
	public static final SOSMsgJOE	JOE_TCl_DBResources_Name							= new SOSMsgJOE("JOE_TCl_DBResources_Name");
	@I18NMsg
	public static final SOSMsgJOE	JOE_TCl_DBResources_Type							= new SOSMsgJOE("JOE_TCl_DBResources_Type");
	@I18NMsg
	public static final SOSMsgJOE	JOE_B_DBResources_New								= new SOSMsgJOE("JOE_B_DBResources_New");
	@I18NMsg
	public static final SOSMsgJOE	JOE_B_DBResources_Remove							= new SOSMsgJOE("JOE_B_DBResources_Remove");
	@I18NMsg
	public static final SOSMsgJOE	JOE_B_DBResources_Notes								= new SOSMsgJOE("JOE_B_DBResources_Notes");
	@I18NMsg
	public static final SOSMsgJOE	JOE_L_DBResources_Type								= new SOSMsgJOE("JOE_L_DBResources_Type");
	@I18NMsg
	public static final SOSMsgJOE	JOE_Cbo_DBResources_Type							= new SOSMsgJOE("JOE_L_DBResources_Type");

	// DatabasesForm
	@I18NMsg
	public static final SOSMsgJOE	JOE_G_DBForm_Databases								= new SOSMsgJOE("JOE_G_DBForm_Databases");
	@I18NMsg
	public static final SOSMsgJOE	JOE_T_DBForm_Database								= new SOSMsgJOE("JOE_T_DBForm_Database");
	@I18NMsg
	public static final SOSMsgJOE	JOE_B_DBForm_Required								= new SOSMsgJOE("JOE_B_DBForm_Required");
	@I18NMsg
	public static final SOSMsgJOE	JOE_B_DBForm_Apply									= new SOSMsgJOE("JOE_B_DBForm_Apply");
	@I18NMsg
	public static final SOSMsgJOE	JOE_Tbl_DBForm_Databases							= new SOSMsgJOE("JOE_Tbl_DBForm_Databases");
	@I18NMsg
	public static final SOSMsgJOE	JOE_TCl_DBForm_Name									= new SOSMsgJOE("JOE_TCl_DBForm_Name");
	@I18NMsg
	public static final SOSMsgJOE	JOE_TCl_DBForm_Required								= new SOSMsgJOE("JOE_TCl_DBForm_Required");
	@I18NMsg
	public static final SOSMsgJOE	JOE_B_DBForm_New									= new SOSMsgJOE("JOE_B_DBForm_New");
	@I18NMsg
	public static final SOSMsgJOE	JOE_B_DBForm_Remove									= new SOSMsgJOE("JOE_B_DBForm_Remove");

	// DocumentationForm
	@I18NMsg
	public static final SOSMsgJOE	JOE_G_DocumentationForm_DocElements					= new SOSMsgJOE("JOE_G_DocumentationForm_DocElements");

	// FilesForm
	@I18NMsg
	public static final SOSMsgJOE	JOE_G_FilesForm_Files								= new SOSMsgJOE("JOE_G_FilesForm_Files");
	@I18NMsg
	public static final SOSMsgJOE	JOE_L_FilesForm_File								= new SOSMsgJOE("JOE_L_FilesForm_File");
	@I18NMsg
	public static final SOSMsgJOE	JOE_T_FilesForm_File								= new SOSMsgJOE("JOE_T_FilesForm_File");
	@I18NMsg
	public static final SOSMsgJOE	JOE_B_FilesForm_Apply								= new SOSMsgJOE("JOE_B_FilesForm_Apply");
	@I18NMsg
	public static final SOSMsgJOE	JOE_L_FilesForm_OS									= new SOSMsgJOE("JOE_L_FilesForm_OS");
	@I18NMsg
	public static final SOSMsgJOE	JOE_L_FilesForm_Type								= new SOSMsgJOE("JOE_L_FilesForm_Type");
	@I18NMsg
	public static final SOSMsgJOE	JOE_B_FilesForm_Notes								= new SOSMsgJOE("JOE_B_FilesForm_Notes");
	@I18NMsg
	public static final SOSMsgJOE	JOE_L_FilesForm_ID									= new SOSMsgJOE("JOE_L_FilesForm_ID");
	@I18NMsg
	public static final SOSMsgJOE	JOE_T_FilesForm_ID									= new SOSMsgJOE("JOE_T_FilesForm_ID");
	@I18NMsg
	public static final SOSMsgJOE	JOE_Tbl_FilesForm_Files								= new SOSMsgJOE("JOE_Tbl_FilesForm_Files");
	@I18NMsg
	public static final SOSMsgJOE	JOE_TCl_FilesForm_ID								= new SOSMsgJOE("JOE_TCl_FilesForm_ID");
	@I18NMsg
	public static final SOSMsgJOE	JOE_TCl_FilesForm_Type								= new SOSMsgJOE("JOE_TCl_FilesForm_Type");
	@I18NMsg
	public static final SOSMsgJOE	JOE_TCl_FilesForm_OS								= new SOSMsgJOE("JOE_TCl_FilesForm_OS");
	@I18NMsg
	public static final SOSMsgJOE	JOE_TCl_FilesForm_File								= new SOSMsgJOE("JOE_TCl_FilesForm_File");
	@I18NMsg
	public static final SOSMsgJOE	JOE_B_FilesForm_New									= new SOSMsgJOE("JOE_B_FilesForm_New");
	@I18NMsg
	public static final SOSMsgJOE	JOE_B_FilesForm_Remove								= new SOSMsgJOE("JOE_B_FilesForm_Remove");
	@I18NMsg
	public static final SOSMsgJOE	JOE_Cbo_FilesForm_OS								= new SOSMsgJOE("JOE_Cbo_FilesForm_OS");
	@I18NMsg
	public static final SOSMsgJOE	JOE_Cbo_FilesForm_Type								= new SOSMsgJOE("JOE_Cbo_FilesForm_Type");

	// IncludeFilesForm
	@I18NMsg
	public static final SOSMsgJOE	JOE_G_IncludeFilesForm_IncludeFiles					= new SOSMsgJOE("JOE_G_IncludeFilesForm_IncludeFiles");
	@I18NMsg
	public static final SOSMsgJOE	JOE_L_IncludeFilesForm_File							= new SOSMsgJOE("JOE_L_IncludeFilesForm_File");
	@I18NMsg
	public static final SOSMsgJOE	JOE_T_IncludeFilesForm_File							= new SOSMsgJOE("JOE_T_IncludeFilesForm_File");
	@I18NMsg
	public static final SOSMsgJOE	JOE_B_IncludeFilesForm_Add							= new SOSMsgJOE("JOE_B_IncludeFilesForm_Add");
	@I18NMsg
	public static final SOSMsgJOE	JOE_L_IncludeFilesForm_Parameter					= new SOSMsgJOE("JOE_L_IncludeFilesForm_Parameter");
	@I18NMsg
	public static final SOSMsgJOE	JOE_Lst_IncludeFilesForm_Files						= new SOSMsgJOE("JOE_Lst_IncludeFilesForm_Files");
	@I18NMsg
	public static final SOSMsgJOE	JOE_B_IncludeFilesForm_Remove						= new SOSMsgJOE("JOE_B_IncludeFilesForm_Remove");

	// JobForm
	@I18NMsg
	public static final SOSMsgJOE	JOE_G_JobForm_Job									= new SOSMsgJOE("JOE_G_JobForm_Job");
	@I18NMsg
	public static final SOSMsgJOE	JOE_T_JobForm_Name									= new SOSMsgJOE("JOE_T_JobForm_Name");
	@I18NMsg
	public static final SOSMsgJOE	JOE_L_JobForm_Title									= new SOSMsgJOE("JOE_L_JobForm_Title");
	@I18NMsg
	public static final SOSMsgJOE	JOE_T_JobForm_Title									= new SOSMsgJOE("JOE_T_JobForm_Title");
	@I18NMsg
	public static final SOSMsgJOE	JOE_L_JobForm_Order									= new SOSMsgJOE("JOE_L_JobForm_Order");
	@I18NMsg
	public static final SOSMsgJOE	JOE_L_JobForm_Tasks									= new SOSMsgJOE("JOE_L_JobForm_Tasks");
	@I18NMsg
	public static final SOSMsgJOE	JOE_Cbo_JobForm_Tasks								= new SOSMsgJOE("JOE_Cbo_JobForm_Tasks");
	@I18NMsg
	public static final SOSMsgJOE	JOE_B_JobForm_Preview								= new SOSMsgJOE("JOE_B_JobForm_Preview");
	@I18NMsg
	public static final SOSMsgJOE	JOE_L_JobForm_OutputPath							= new SOSMsgJOE("JOE_L_JobForm_OutputPath");
	@I18NMsg
	public static final SOSMsgJOE	JOE_T_JobForm_OutputPath							= new SOSMsgJOE("JOE_T_JobForm_OutputPath");
	@I18NMsg
	public static final SOSMsgJOE	JOE_L_JobForm_PackageName							= new SOSMsgJOE("JOE_L_JobForm_PackageName");
	@I18NMsg
	public static final SOSMsgJOE	JOE_T_JobForm_PackageName							= new SOSMsgJOE("JOE_T_JobForm_PackageName");
	@I18NMsg
	public static final SOSMsgJOE	JOE_L_JobForm_JobType								= new SOSMsgJOE("JOE_L_JobForm_JobType");
	@I18NMsg
	public static final SOSMsgJOE	JOE_Cbo_JobForm_JobType								= new SOSMsgJOE("JOE_Cbo_JobForm_JobType");
	@I18NMsg
	public static final SOSMsgJOE	JOE_B_JobForm_GenerateSource						= new SOSMsgJOE("JOE_B_JobForm_GenerateSource");
	@I18NMsg
	public static final SOSMsgJOE	JOE_M_FileNotFound									= new SOSMsgJOE("JOE_M_FileNotFound");
	@I18NMsg
	public static final SOSMsgJOE	JOE_Cbo_JobForm_Order								= new SOSMsgJOE("JOE_Cbo_JobForm_Order");

	// JobScriptForm
	@I18NMsg
	public static final SOSMsgJOE	JOE_B_JobScriptForm_UseScript						= new SOSMsgJOE("JOE_B_JobScriptForm_UseScript");

	// NoteForm
	@I18NMsg
	public static final SOSMsgJOE	JOE_G_NoteForm_Documentation						= new SOSMsgJOE("JOE_G_NoteForm_Documentation");
	@I18NMsg
	public static final SOSMsgJOE	JOE_L_NoteForm_Language								= new SOSMsgJOE("JOE_L_NoteForm_Language");
	@I18NMsg
	public static final SOSMsgJOE	JOE_B_NoteForm_Clear								= new SOSMsgJOE("JOE_B_NoteForm_Clear");
	@I18NMsg
	public static final SOSMsgJOE	JOE_B_NoteForm_Apply								= new SOSMsgJOE("JOE_B_NoteForm_Apply");
	@I18NMsg
	public static final SOSMsgJOE	JOE_Cbo_NoteForm_Language							= new SOSMsgJOE("JOE_Cbo_NoteForm_Language");
	@I18NMsg
	public static final SOSMsgJOE	JOE_M_NoteForm_Config								= new SOSMsgJOE("JOE_M_NoteForm_Config");
	@I18NMsg
	public static final SOSMsgJOE	JOE_M_NoteForm_Settings								= new SOSMsgJOE("JOE_M_NoteForm_Settings");
	@I18NMsg
	public static final SOSMsgJOE	JOE_M_NoteForm_Doc									= new SOSMsgJOE("JOE_M_NoteForm_Doc");

	// ParamsForm
	@I18NMsg
	public static final SOSMsgJOE	JOE_G_ParamsForm_Parameter							= new SOSMsgJOE("JOE_G_ParamsForm_Parameter");
	@I18NMsg
	public static final SOSMsgJOE	JOE_L_ParamsForm_ID									= new SOSMsgJOE("JOE_L_ParamsForm_ID");
	@I18NMsg
	public static final SOSMsgJOE	JOE_T_ParamsForm_ID									= new SOSMsgJOE("JOE_T_ParamsForm_ID");
	@I18NMsg
	public static final SOSMsgJOE	JOE_L_ParamsForm_Reference							= new SOSMsgJOE("JOE_L_ParamsForm_Reference");
	@I18NMsg
	public static final SOSMsgJOE	JOE_B_ParamsForm_ParamsNote							= new SOSMsgJOE("JOE_B_ParamsForm_ParamsNote");
	@I18NMsg
	public static final SOSMsgJOE	JOE_Cbo_ParamsForm_Reference						= new SOSMsgJOE("JOE_Cbo_ParamsForm_Reference");
	@I18NMsg
	public static final SOSMsgJOE	JOE_G_ParamsForm_ParamValues						= new SOSMsgJOE("JOE_G_ParamsForm_ParamValues");
	@I18NMsg
	public static final SOSMsgJOE	JOE_T_ParamsForm_Name								= new SOSMsgJOE("JOE_T_ParamsForm_Name");
	@I18NMsg
	public static final SOSMsgJOE	JOE_B_ParamsForm_Apply								= new SOSMsgJOE("JOE_B_ParamsForm_Apply");
	@I18NMsg
	public static final SOSMsgJOE	JOE_L_ParamsForm_DefaultValue						= new SOSMsgJOE("JOE_L_ParamsForm_DefaultValue");
	@I18NMsg
	public static final SOSMsgJOE	JOE_T_ParamsForm_DefaultValue						= new SOSMsgJOE("JOE_T_ParamsForm_DefaultValue");
	@I18NMsg
	public static final SOSMsgJOE	JOE_L_ParamsForm_Required							= new SOSMsgJOE("JOE_L_ParamsForm_Required");
	@I18NMsg
	public static final SOSMsgJOE	JOE_B_ParamsForm_Required							= new SOSMsgJOE("JOE_B_ParamsForm_Required");
	@I18NMsg
	public static final SOSMsgJOE	JOE_T_ParamsForm_ID2								= new SOSMsgJOE("JOE_T_ParamsForm_ID2");
	@I18NMsg
	public static final SOSMsgJOE	JOE_B_ParamsForm_Notes								= new SOSMsgJOE("JOE_B_ParamsForm_Notes");
	@I18NMsg
	public static final SOSMsgJOE	JOE_Tbl_ParamsForm_Params							= new SOSMsgJOE("JOE_Tbl_ParamsForm_Params");
	@I18NMsg
	public static final SOSMsgJOE	JOE_TCl_ParamsForm_Name								= new SOSMsgJOE("JOE_TCl_ParamsForm_Name");
	@I18NMsg
	public static final SOSMsgJOE	JOE_TCl_ParamsForm_Default							= new SOSMsgJOE("JOE_TCl_ParamsForm_Default");
	@I18NMsg
	public static final SOSMsgJOE	JOE_TCl_ParamsForm_Required							= new SOSMsgJOE("JOE_TCl_ParamsForm_Required");
	@I18NMsg
	public static final SOSMsgJOE	JOE_TCl_ParamsForm_Reference						= new SOSMsgJOE("JOE_TCl_ParamsForm_Reference");
	@I18NMsg
	public static final SOSMsgJOE	JOE_TCl_ParamsForm_ID								= new SOSMsgJOE("JOE_TCl_ParamsForm_ID");
	@I18NMsg
	public static final SOSMsgJOE	JOE_B_ParamsForm_NewParam							= new SOSMsgJOE("JOE_B_ParamsForm_NewParam");
	@I18NMsg
	public static final SOSMsgJOE	JOE_B_ParamsForm_RemoveParam						= new SOSMsgJOE("JOE_B_ParamsForm_RemoveParam");
	@I18NMsg
	public static final SOSMsgJOE	JOE_Cbo_ParamsForm_Reference2						= new SOSMsgJOE("JOE_Cbo_ParamsForm_Reference2");

	// PayloadForm
	@I18NMsg
	public static final SOSMsgJOE	JOE_G_PayloadForm_Payload							= new SOSMsgJOE("JOE_G_PayloadForm_Payload");
	@I18NMsg
	public static final SOSMsgJOE	JOE_B_PayloadForm_PayloadNote						= new SOSMsgJOE("JOE_B_PayloadForm_PayloadNote");
	@I18NMsg
	public static final SOSMsgJOE	JOE_B_PayloadForm_DocNote							= new SOSMsgJOE("JOE_B_PayloadForm_DocNote");

	// ProcessForm
	@I18NMsg
	public static final SOSMsgJOE	JOE_B_ProcessForm_UseProcess						= new SOSMsgJOE("JOE_B_ProcessForm_UseProcess");
	@I18NMsg
	public static final SOSMsgJOE	JOE_G_ProcessForm_Process							= new SOSMsgJOE("JOE_G_ProcessForm_Process");
	@I18NMsg
	public static final SOSMsgJOE	JOE_L_ProcessForm_File								= new SOSMsgJOE("JOE_L_ProcessForm_File");
	@I18NMsg
	public static final SOSMsgJOE	JOE_T_ProcessForm_File								= new SOSMsgJOE("JOE_T_ProcessForm_File");
	@I18NMsg
	public static final SOSMsgJOE	JOE_L_ProcessForm_Parameter							= new SOSMsgJOE("JOE_L_ProcessForm_Parameter");
	@I18NMsg
	public static final SOSMsgJOE	JOE_T_ProcessForm_Parameter							= new SOSMsgJOE("JOE_T_ProcessForm_Parameter");
	@I18NMsg
	public static final SOSMsgJOE	JOE_L_ProcessForm_Log								= new SOSMsgJOE("JOE_L_ProcessForm_Log");
	@I18NMsg
	public static final SOSMsgJOE	JOE_T_ProcessForm_Log								= new SOSMsgJOE("JOE_T_ProcessForm_Log");
	@I18NMsg
	public static final SOSMsgJOE	JOE_G_ProcessForm_EnvironmentVariables				= new SOSMsgJOE("JOE_G_ProcessForm_EnvironmentVariables");
	@I18NMsg
	public static final SOSMsgJOE	JOE_T_ProcessForm_Name								= new SOSMsgJOE("JOE_T_ProcessForm_Name");
	@I18NMsg
	public static final SOSMsgJOE	JOE_L_ProcessForm_Value								= new SOSMsgJOE("JOE_L_ProcessForm_Value");
	@I18NMsg
	public static final SOSMsgJOE	JOE_T_ProcessForm_Value								= new SOSMsgJOE("JOE_T_ProcessForm_Value");
	@I18NMsg
	public static final SOSMsgJOE	JOE_B_ProcessForm_Apply								= new SOSMsgJOE("JOE_B_ProcessForm_Apply");
	@I18NMsg
	public static final SOSMsgJOE	JOE_Tbl_ProcessForm_Variables						= new SOSMsgJOE("JOE_Tbl_ProcessForm_Variables");
	@I18NMsg
	public static final SOSMsgJOE	JOE_B_ProcessForm_Remove							= new SOSMsgJOE("JOE_B_ProcessForm_Remove");
	@I18NMsg
	public static final SOSMsgJOE	JOE_TCl_ProcessForm_Name							= new SOSMsgJOE("JOE_TCl_ProcessForm_Name");
	@I18NMsg
	public static final SOSMsgJOE	JOE_TCl_ProcessForm_Value							= new SOSMsgJOE("JOE_TCl_ProcessForm_Value");

	// ProfilesForm
	@I18NMsg
	public static final SOSMsgJOE	JOE_G_ProfilesForm_Profiles							= new SOSMsgJOE("JOE_G_ProfilesForm_Profiles");
	@I18NMsg
	public static final SOSMsgJOE	JOE_T_ProfilesForm_Name								= new SOSMsgJOE("JOE_T_ProfilesForm_Name");
	@I18NMsg
	public static final SOSMsgJOE	JOE_B_ProfilesForm_ProfileNotes						= new SOSMsgJOE("JOE_B_ProfilesForm_ProfileNotes");
	@I18NMsg
	public static final SOSMsgJOE	JOE_B_ProfilesForm_ApplyProfile						= new SOSMsgJOE("JOE_B_ProfilesForm_ApplyProfile");
	@I18NMsg
	public static final SOSMsgJOE	JOE_Tbl_ProfilesForm_Profiles						= new SOSMsgJOE("JOE_Tbl_ProfilesForm_Profiles");
	@I18NMsg
	public static final SOSMsgJOE	JOE_TCl_ProfilesForm_Name							= new SOSMsgJOE("JOE_TCl_ProfilesForm_Name");
	@I18NMsg
	public static final SOSMsgJOE	JOE_B_ProfilesForm_NewProfile						= new SOSMsgJOE("JOE_B_ProfilesForm_NewProfile");
	@I18NMsg
	public static final SOSMsgJOE	JOE_B_ProfilesForm_RemoveProfile					= new SOSMsgJOE("JOE_B_ProfilesForm_RemoveProfile");

	// ReleaseForm
	@I18NMsg
	public static final SOSMsgJOE	JOE_M_ReleaseForm_Changes							= new SOSMsgJOE("JOE_M_ReleaseForm_Changes");
	@I18NMsg
	public static final SOSMsgJOE	JOE_G_ReleaseForm_Releases							= new SOSMsgJOE("JOE_G_ReleaseForm_Releases");
	@I18NMsg
	public static final SOSMsgJOE	JOE_L_ReleaseForm_ID								= new SOSMsgJOE("JOE_L_ReleaseForm_ID");
	@I18NMsg
	public static final SOSMsgJOE	JOE_T_ReleaseForm_ID								= new SOSMsgJOE("JOE_T_ReleaseForm_ID");
	@I18NMsg
	public static final SOSMsgJOE	JOE_L_ReleaseForm_Created							= new SOSMsgJOE("JOE_L_ReleaseForm_Created");
	@I18NMsg
	public static final SOSMsgJOE	JOE_L_ReleaseForm_Title								= new SOSMsgJOE("JOE_L_ReleaseForm_Title");
	@I18NMsg
	public static final SOSMsgJOE	JOE_T_ReleaseForm_Title								= new SOSMsgJOE("JOE_T_ReleaseForm_Title");
	@I18NMsg
	public static final SOSMsgJOE	JOE_L_ReleaseForm_Modified							= new SOSMsgJOE("JOE_L_ReleaseForm_Modified");
	@I18NMsg
	public static final SOSMsgJOE	JOE_ReleaseForm_Created								= new SOSMsgJOE("JOE_ReleaseForm_Created");
	@I18NMsg
	public static final SOSMsgJOE	JOE_ReleaseForm_Modified							= new SOSMsgJOE("JOE_ReleaseForm_Modified");

	// ReleasesForm
	@I18NMsg
	public static final SOSMsgJOE	JOE_G_ReleasesForm_Releases							= new SOSMsgJOE("JOE_G_ReleasesForm_Releases");
	@I18NMsg
	public static final SOSMsgJOE	JOE_Tbl_ReleasesForm_Releases							= new SOSMsgJOE("JOE_Tbl_ReleasesForm_Releases");
	@I18NMsg
	public static final SOSMsgJOE	JOE_TCl_ReleasesForm_ID							= new SOSMsgJOE("JOE_TCl_ReleasesForm_ID");
	@I18NMsg
	public static final SOSMsgJOE	JOE_TCl_ReleasesForm_Title							= new SOSMsgJOE("JOE_TCl_ReleasesForm_Title");
	@I18NMsg
	public static final SOSMsgJOE	JOE_TCl_ReleasesForm_Created							= new SOSMsgJOE("JOE_TCl_ReleasesForm_Created");
	@I18NMsg
	public static final SOSMsgJOE	JOE_TCl_ReleasesForm_Modified							= new SOSMsgJOE("JOE_TCl_ReleasesForm_Modified");
	@I18NMsg
	public static final SOSMsgJOE	JOE_B_ReleasesForm_NewRelease							= new SOSMsgJOE("JOE_B_ReleasesForm_NewRelease");
	@I18NMsg
	public static final SOSMsgJOE	JOE_B_ReleasesForm_RemoveRelease							= new SOSMsgJOE("JOE_B_ReleasesForm_RemoveRelease");
	
//	ResourcesForm
	@I18NMsg
	public static final SOSMsgJOE	JOE_G_ResourcesForm_Resources							= new SOSMsgJOE("JOE_G_ResourcesForm_Resources");
	@I18NMsg
	public static final SOSMsgJOE	JOE_B_ResourcesForm_Memory							= new SOSMsgJOE("JOE_B_ResourcesForm_Memory");
	@I18NMsg
	public static final SOSMsgJOE	JOE_B_ResourcesForm_Space							= new SOSMsgJOE("JOE_B_ResourcesForm_Space");
	@I18NMsg
	public static final SOSMsgJOE	JOE_G_ResourcesForm_Memory							= new SOSMsgJOE("JOE_G_ResourcesForm_Memory");
	@I18NMsg
	public static final SOSMsgJOE	JOE_L_ResourcesForm_Minimum							= new SOSMsgJOE("JOE_L_ResourcesForm_Minimum");
	@I18NMsg
	public static final SOSMsgJOE	JOE_T_ResourcesForm_Memory							= new SOSMsgJOE("JOE_T_ResourcesForm_Memory");
	@I18NMsg
	public static final SOSMsgJOE	JOE_L_ResourcesForm_Unit							= new SOSMsgJOE("JOE_L_ResourcesForm_Unit");
	@I18NMsg
	public static final SOSMsgJOE	JOE_B_ResourcesForm_MemoryNotes							= new SOSMsgJOE("JOE_B_ResourcesForm_MemoryNotes");
	@I18NMsg
	public static final SOSMsgJOE	JOE_G_ResourcesForm_Space							= new SOSMsgJOE("JOE_G_ResourcesForm_Space");
	@I18NMsg
	public static final SOSMsgJOE	JOE_T_ResourcesForm_Space							= new SOSMsgJOE("JOE_T_ResourcesForm_Space");
	@I18NMsg
	public static final SOSMsgJOE	JOE_B_ResourcesForm_SpaceNotes							= new SOSMsgJOE("JOE_B_ResourcesForm_SpaceNotes");
	@I18NMsg
	public static final SOSMsgJOE	JOE_Cbo_ResourcesForm_Memory							= new SOSMsgJOE("JOE_Cbo_ResourcesForm_Memory");
	@I18NMsg
	public static final SOSMsgJOE	JOE_Cbo_ResourcesForm_Space							= new SOSMsgJOE("JOE_Cbo_ResourcesForm_Space");

//	ScriptForm
	@I18NMsg
	public static final SOSMsgJOE	JOE_G_ScriptForm_Script							= new SOSMsgJOE("JOE_G_ScriptForm_Script");
	@I18NMsg
	public static final SOSMsgJOE	JOE_L_ScriptForm_JavaClass							= new SOSMsgJOE("JOE_L_ScriptForm_JavaClass");
	@I18NMsg
	public static final SOSMsgJOE	JOE_T_ScriptForm_JavaClass							= new SOSMsgJOE("JOE_T_ScriptForm_JavaClass");
	@I18NMsg
	public static final SOSMsgJOE	JOE_L_ScriptForm_ResourceID							= new SOSMsgJOE("JOE_L_ScriptForm_ResourceID");
	@I18NMsg
	public static final SOSMsgJOE	JOE_B_ScriptForm_JavaRB							= new SOSMsgJOE("JOE_B_ScriptForm_JavaRB");
	@I18NMsg
	public static final SOSMsgJOE	JOE_B_ScriptForm_JavaScriptRB							= new SOSMsgJOE("JOE_B_ScriptForm_JavaScriptRB");
	@I18NMsg
	public static final SOSMsgJOE	JOE_B_ScriptForm_PerlScriptRB							= new SOSMsgJOE("JOE_B_ScriptForm_PerlScriptRB");
	@I18NMsg
	public static final SOSMsgJOE	JOE_B_ScriptForm_VBScriptRB							= new SOSMsgJOE("JOE_B_ScriptForm_VBScriptRB");
	@I18NMsg
	public static final SOSMsgJOE	JOE_B_ScriptForm_ShellRB							= new SOSMsgJOE("JOE_B_ScriptForm_ShellRB");
	@I18NMsg
	public static final SOSMsgJOE	JOE_B_ScriptForm_NoneRB							= new SOSMsgJOE("JOE_B_ScriptForm_NoneRB");
	@I18NMsg
	public static final SOSMsgJOE	JOE_Cbo_ScriptForm_Resource							= new SOSMsgJOE("JOE_Cbo_ScriptForm_Resource");
	
//	SectionsForm
	@I18NMsg
	public static final SOSMsgJOE	JOE_G_SectionsForm_Sections							= new SOSMsgJOE("JOE_G_SectionsForm_Sections");
	@I18NMsg
	public static final SOSMsgJOE	JOE_T_SectionsForm_Name							= new SOSMsgJOE("JOE_T_SectionsForm_Name");
	@I18NMsg
	public static final SOSMsgJOE	JOE_B_SectionsForm_Apply							= new SOSMsgJOE("JOE_B_SectionsForm_Apply");
	@I18NMsg
	public static final SOSMsgJOE	JOE_L_SectionsForm_Reference							= new SOSMsgJOE("JOE_L_SectionsForm_Reference");
	@I18NMsg
	public static final SOSMsgJOE	JOE_L_SectionsForm_ID							= new SOSMsgJOE("JOE_L_SectionsForm_ID");
	@I18NMsg
	public static final SOSMsgJOE	JOE_T_SectionsForm_ID							= new SOSMsgJOE("JOE_T_SectionsForm_ID");
	@I18NMsg
	public static final SOSMsgJOE	JOE_Tbl_SectionsForm_Sections							= new SOSMsgJOE("JOE_Tbl_SectionsForm_Sections");
	@I18NMsg
	public static final SOSMsgJOE	JOE_B_SectionsForm_New							= new SOSMsgJOE("JOE_B_SectionsForm_New");
	@I18NMsg
	public static final SOSMsgJOE	JOE_B_SectionsForm_Remove							= new SOSMsgJOE("JOE_B_SectionsForm_Remove");
	@I18NMsg
	public static final SOSMsgJOE	JOE_TCl_SectionsForm_Name							= new SOSMsgJOE("JOE_TCl_SectionsForm_Name");
	@I18NMsg
	public static final SOSMsgJOE	JOE_TCl_SectionsForm_Reference							= new SOSMsgJOE("JOE_TCl_SectionsForm_Reference");
	@I18NMsg
	public static final SOSMsgJOE	JOE_TCl_SectionsForm_ID							= new SOSMsgJOE("JOE_TCl_SectionsForm_ID");
	@I18NMsg
	public static final SOSMsgJOE	JOE_Cbo_SectionsForm_Reference							= new SOSMsgJOE("JOE_Cbo_SectionsForm_Reference");
	
//	SettingForm
	@I18NMsg
	public static final SOSMsgJOE	JOE_G_SettingForm_Settings							= new SOSMsgJOE("JOE_G_SettingForm_Settings");
	@I18NMsg
	public static final SOSMsgJOE	JOE_T_SettingForm_Name							= new SOSMsgJOE("JOE_T_SettingForm_Name");
	@I18NMsg
	public static final SOSMsgJOE	JOE_B_SettingForm_Apply							= new SOSMsgJOE("JOE_B_SettingForm_Apply");
	@I18NMsg
	public static final SOSMsgJOE	JOE_L_SettingForm_DefaultValue							= new SOSMsgJOE("JOE_L_SettingForm_DefaultValue");
	@I18NMsg
	public static final SOSMsgJOE	JOE_T_SettingForm_DefaultValue							= new SOSMsgJOE("JOE_T_SettingForm_DefaultValue");
	@I18NMsg
	public static final SOSMsgJOE	JOE_L_SettingForm_Type							= new SOSMsgJOE("JOE_L_SettingForm_Type");
	@I18NMsg
	public static final SOSMsgJOE	JOE_L_SettingForm_Reference							= new SOSMsgJOE("JOE_L_SettingForm_Reference");
	@I18NMsg
	public static final SOSMsgJOE	JOE_L_SettingForm_ID							= new SOSMsgJOE("JOE_L_SettingForm_ID");
	@I18NMsg
	public static final SOSMsgJOE	JOE_T_SettingForm_ID							= new SOSMsgJOE("JOE_T_SettingForm_ID");
	@I18NMsg
	public static final SOSMsgJOE	JOE_L_SettingForm_Required							= new SOSMsgJOE("JOE_L_SettingForm_Required");
	@I18NMsg
	public static final SOSMsgJOE	JOE_B_SettingForm_Required							= new SOSMsgJOE("JOE_B_SettingForm_Required");
	@I18NMsg
	public static final SOSMsgJOE	JOE_B_SettingForm_Notes							= new SOSMsgJOE("JOE_B_SettingForm_Notes");
	@I18NMsg
	public static final SOSMsgJOE	JOE_Tbl_SettingForm_Settings							= new SOSMsgJOE("JOE_Tbl_SettingForm_Settings");
	@I18NMsg
	public static final SOSMsgJOE	JOE_TCl_SettingForm_Name							= new SOSMsgJOE("JOE_TCl_SettingForm_Name");
	@I18NMsg
	public static final SOSMsgJOE	JOE_TCl_SettingForm_Default							= new SOSMsgJOE("JOE_TCl_SettingForm_Default");
	@I18NMsg
	public static final SOSMsgJOE	JOE_TCl_SettingForm_Type							= new SOSMsgJOE("JOE_TCl_SettingForm_Type");
	@I18NMsg
	public static final SOSMsgJOE	JOE_TCl_SettingForm_Required							= new SOSMsgJOE("JOE_TCl_SettingForm_Required");
	@I18NMsg
	public static final SOSMsgJOE	JOE_TCl_SettingForm_Reference							= new SOSMsgJOE("JOE_TCl_SettingForm_Reference");
	@I18NMsg
	public static final SOSMsgJOE	JOE_TCl_SettingForm_ID							= new SOSMsgJOE("JOE_TCl_SettingForm_ID");
	@I18NMsg
	public static final SOSMsgJOE	JOE_B_SettingForm_New							= new SOSMsgJOE("JOE_B_SettingForm_New");
	@I18NMsg
	public static final SOSMsgJOE	JOE_B_SettingForm_Remove							= new SOSMsgJOE("JOE_B_SettingForm_Remove");
	@I18NMsg
	public static final SOSMsgJOE	JOE_Cbo_SettingForm_Reference							= new SOSMsgJOE("JOE_Cbo_SettingForm_Reference");
	@I18NMsg
	public static final SOSMsgJOE	JOE_Cbo_SettingForm_Type							= new SOSMsgJOE("JOE_Cbo_SettingForm_Type");
	
	
	// SchedulerListener
	@I18NMsg
	public static final SOSMsgJOE	JOE_M_SchedulerListener_RunTime						= new SOSMsgJOE("JOE_M_SchedulerListener_RunTime");
	@I18NMsg
	public static final SOSMsgJOE	JOE_M_SchedulerListener_PrePostProcessing			= new SOSMsgJOE("JOE_M_SchedulerListener_PrePostProcessing");
	@I18NMsg
	public static final SOSMsgJOE	JOE_L_SchedulerListener_JobChains					= new SOSMsgJOE("JOE_L_SchedulerListener_JobChains");
	@I18NMsg
	public static final SOSMsgJOE	JOE_L_SchedulerListener_ProcessClasses				= new SOSMsgJOE("JOE_L_SchedulerListener_ProcessClasses");
	@I18NMsg
	public static final SOSMsgJOE	JOE_L_SchedulerListener_Jobs						= new SOSMsgJOE("JOE_L_SchedulerListener_Jobs");
	@I18NMsg
	public static final SOSMsgJOE	JOE_L_SchedulerListener_HTTPServer					= new SOSMsgJOE("JOE_L_SchedulerListener_HTTPServer");
	@I18NMsg
	public static final SOSMsgJOE	JOE_L_SchedulerListener_Webservices					= new SOSMsgJOE("JOE_L_SchedulerListener_Webservices");
	@I18NMsg
	public static final SOSMsgJOE	JOE_L_SchedulerListener_Schedules					= new SOSMsgJOE("JOE_L_SchedulerListener_Schedules");
	@I18NMsg
	public static final SOSMsgJOE	JOE_L_SchedulerListener_JobChainOrders				= new SOSMsgJOE("JOE_L_SchedulerListener_JobChainOrders");
    @I18NMsg
    public static final SOSMsgJOE   JOE_L_SchedulerListener_Locks                       = new SOSMsgJOE("JOE_L_SchedulerListener_Locks");
    @I18NMsg
    public static final SOSMsgJOE   JOE_L_SchedulerListener_Locks_used                  = new SOSMsgJOE("JOE_L_SchedulerListener_Locks_used");
    @I18NMsg
    public static final SOSMsgJOE   JOE_L_SchedulerListener_Monitors_used               = new SOSMsgJOE("JOE_L_SchedulerListener_Monitors_used");
	@I18NMsg
	public static final SOSMsgJOE	JOE_M_0047											= new SOSMsgJOE("JOE_M_0047");
	@I18NMsg
	public static final SOSMsgJOE	JOE_L_SchedulerListener_StepsNodes					= new SOSMsgJOE("JOE_L_SchedulerListener_StepsNodes");
	@I18NMsg
	public static final SOSMsgJOE	JOE_L_SchedulerListener_NestedJobChains				= new SOSMsgJOE("JOE_L_SchedulerListener_NestedJobChains");
	@I18NMsg
	public static final SOSMsgJOE	JOE_L_SchedulerListener_BaseConfig					= new SOSMsgJOE("JOE_L_SchedulerListener_BaseConfig");
	@I18NMsg
	public static final SOSMsgJOE	JOE_L_SchedulerListener_BaseFiles					= new SOSMsgJOE("JOE_L_SchedulerListener_BaseFiles");
	@I18NMsg
	public static final SOSMsgJOE	JOE_L_SchedulerListener_Parameter					= new SOSMsgJOE("JOE_L_SchedulerListener_Parameter");
	@I18NMsg
	public static final SOSMsgJOE	JOE_L_SchedulerListener_AccessControl				= new SOSMsgJOE("JOE_L_SchedulerListener_AccessControl");
	@I18NMsg
	public static final SOSMsgJOE	JOE_L_SchedulerListener_ClusterConfig				= new SOSMsgJOE("JOE_L_SchedulerListener_ClusterConfig");
	@I18NMsg
	public static final SOSMsgJOE	JOE_L_SchedulerListener_StartScript					= new SOSMsgJOE("JOE_L_SchedulerListener_StartScript");
	@I18NMsg
	public static final SOSMsgJOE	JOE_L_SchedulerListener_HTTPAuthentication			= new SOSMsgJOE("JOE_L_SchedulerListener_HTTPAuthentication");
	@I18NMsg
	public static final SOSMsgJOE	JOE_L_SchedulerListener_HTTPDirectories				= new SOSMsgJOE("JOE_L_SchedulerListener_HTTPDirectories");
	@I18NMsg
	public static final SOSMsgJOE	JOE_L_SchedulerListener_Holidays					= new SOSMsgJOE("JOE_L_SchedulerListener_Holidays");
	@I18NMsg
	public static final SOSMsgJOE	JOE_L_SchedulerListener_Commands					= new SOSMsgJOE("JOE_L_SchedulerListener_Commands");
	@I18NMsg
	public static final SOSMsgJOE	JOE_L_SchedulerListener_Options						= new SOSMsgJOE("JOE_L_SchedulerListener_Options");
	@I18NMsg
	public static final SOSMsgJOE	JOE_L_SchedulerListener_JobSettings					= new SOSMsgJOE("JOE_L_SchedulerListener_JobSettings");
	@I18NMsg
	public static final SOSMsgJOE	JOE_L_SchedulerListener_RunOptions					= new SOSMsgJOE("JOE_L_SchedulerListener_RunOptions");
	@I18NMsg
	public static final SOSMsgJOE	JOE_L_SchedulerListener_Documentation				= new SOSMsgJOE("JOE_L_SchedulerListener_Documentation");
	@I18NMsg
	public static final SOSMsgJOE	JOE_L_SchedulerListener_Weekdays					= new SOSMsgJOE("JOE_L_SchedulerListener_Weekdays");
	@I18NMsg
	public static final SOSMsgJOE	JOE_M_0048											= new SOSMsgJOE("JOE_M_0048");
	@I18NMsg
	public static final SOSMsgJOE	JOE_M_0049											= new SOSMsgJOE("JOE_M_0049");
	@I18NMsg
	public static final SOSMsgJOE	JOE_M_SchedulerListener_EveryDay					= new SOSMsgJOE("JOE_M_SchedulerListener_EveryDay");
	@I18NMsg
	public static final SOSMsgJOE	JOE_M_SchedulerListener_Weekdays					= new SOSMsgJOE("JOE_M_SchedulerListener_Weekdays");
	@I18NMsg
	public static final SOSMsgJOE	JOE_M_SchedulerListener_Monthdays					= new SOSMsgJOE("JOE_M_SchedulerListener_Monthdays");
	@I18NMsg
	public static final SOSMsgJOE	JOE_M_SchedulerListener_Ultimos						= new SOSMsgJOE("JOE_M_SchedulerListener_Ultimos");
	@I18NMsg
	public static final SOSMsgJOE	JOE_M_SchedulerListener_SpecificWeekdays			= new SOSMsgJOE("JOE_M_SchedulerListener_SpecificWeekdays");
	@I18NMsg
	public static final SOSMsgJOE	JOE_M_SchedulerListener_SpecificDays				= new SOSMsgJOE("JOE_M_SchedulerListener_SpecificDays");
	@I18NMsg
	public static final SOSMsgJOE	JOE_M_SchedulerListener_SpecificMonth				= new SOSMsgJOE("JOE_M_SchedulerListener_SpecificMonth");
	@I18NMsg
	public static final SOSMsgJOE	JOE_M_SchedulerListener_Holidays					= new SOSMsgJOE("JOE_M_SchedulerListener_Holidays");
	@I18NMsg
	public static final SOSMsgJOE	JOE_M_SchedulerListener_Webservice					= new SOSMsgJOE("JOE_M_SchedulerListener_Webservice");
	@I18NMsg
	public static final SOSMsgJOE	JOE_M_SchedulerListener_Parameter					= new SOSMsgJOE("JOE_M_SchedulerListener_Parameter");
    @I18NMsg
    public static final SOSMsgJOE   JOE_M_SchedulerListener_Empty                       = new SOSMsgJOE("JOE_M_SchedulerListener_Empty");
    @I18NMsg
    public static final SOSMsgJOE   JOE_E_ScheduleForm_ValidFromTo_001                  = new SOSMsgJOE("JOE_E_ScheduleForm_ValidFromTo_001");
    @I18NMsg
    public static final SOSMsgJOE   JOE_E_ScheduleForm_ValidFromTo_002                  = new SOSMsgJOE("JOE_E_ScheduleForm_ValidFromTo_002");
    
    @I18NMsg
    public static final SOSMsgJOE   JOE_G_ReturnCodesForm_ReturnCodes                   = new SOSMsgJOE("JOE_G_ReturnCodesForm_ReturnCodes");

    

	// public SOSJOEMessageCodes() {
	// // TODO Auto-generated constructor stub
	// }

	public SOSJOEMessageCodes(final Group parent, final int style) {
		super(parent, style);
	}

	public SOSJOEMessageCodes(final Composite parent, final int style) {
		super(parent, style);
	}
	
	Cursor objLastCursor = null;
	
	protected void showWaitCursor() {
		if (!getShell().isDisposed()) {
			objLastCursor = getShell().getCursor();
		}
		
		getShell().setCursor(SWTResourceManager.getCursor(SWT.CURSOR_WAIT));
	}

	protected void restoreCursor() {
		if (!getShell().isDisposed())
			if (objLastCursor == null) {
//				getShell().setCursor(new Cursor(getShell().getDisplay(), SWT.CURSOR_ARROW));
				getShell().setCursor(SWTResourceManager.getCursor(SWT.CURSOR_ARROW));
			}
			else {
				getShell().setCursor(objLastCursor);
				objLastCursor = null;
			}
	}

	protected void setResizableV(final Control objControl) {
		boolean flgGrapVerticalspace = true;
		objControl.setLayoutData(new GridData(GridData.FILL, GridData.FILL, true, flgGrapVerticalspace));
	}


}
