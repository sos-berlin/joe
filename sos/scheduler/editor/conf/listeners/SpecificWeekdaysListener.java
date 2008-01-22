package sos.scheduler.editor.conf.listeners;

 
import java.util.Iterator;
import java.util.List;
import java.util.StringTokenizer;
import java.util.TreeMap;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.TreeItem;
import org.jdom.Element;

import sos.scheduler.editor.app.Editor;
import sos.scheduler.editor.app.Options;
import sos.scheduler.editor.app.TreeData;
import sos.scheduler.editor.app.Utils;
import sos.scheduler.editor.conf.SchedulerDom;

class Weekday {
	protected String day;
	protected String which;
	protected TreeMap elements = new TreeMap();
}

public class SpecificWeekdaysListener {
    public static final int   WEEKDAYS     = 0;

    public static final int   MONTHDAYS    = 1;

    public static final int   ULTIMOS      = 2;

    private SchedulerDom      _dom;

    public static String[]   _daynames = {"First","Second","Third","Fourth","Last","Second Last","Third Last","Fourth Last"};
    private String[]           _usedDays    = null;
    private Element           _runtime;


  
    public SpecificWeekdaysListener(SchedulerDom dom, Element runtime) {
        _dom = dom;
        _runtime = runtime;
    }

 

 
    public void addDay(String day, String which) {
    	  boolean found=false;
    	  int index=0;
    	  for (int i = 0;i<_daynames.length;i++) {
    	  	if (_daynames[i].equals(which)) index=i+1;
    	  }
    	  if (index>4)index=(-1)*(index-4);
    	  which = String.valueOf(index);
        Element daylist = _runtime.getChild("monthdays");
        if (daylist == null) {
            daylist = new Element("monthdays");
            _runtime.addContent(daylist);
        }
        
      
        if (daylist != null) {
            List list = daylist.getChildren("weekday");
            Iterator it = list.iterator();
            while (it.hasNext()) {
                Element e = (Element) it.next();
                if (e.getAttributeValue("day") != null && e.getAttributeValue("day").equalsIgnoreCase(day) && 
                		e.getAttributeValue("which") != null && e.getAttributeValue("which").equalsIgnoreCase(which)) {
                    found=true;
                }
            }
        }
     

       if (!found) {
        	 Element w = new Element("weekday").setAttribute("day", day.toLowerCase());
           w.setAttribute("which", which);
           daylist.addContent(w);
           _dom.setChanged(true);
           if(_runtime != null && _runtime.getParentElement() != null )
           	_dom.setChangedForDirectory("job", Utils.getAttributeValue("name",_runtime.getParentElement()), SchedulerDom.MODIFY);
       }
    }
  


    public void deleteDay(String day_string) {
    	  String day = "";
    	  String which = "";
    	  int index=0;
    	  
        StringTokenizer t = new StringTokenizer(day_string.toLowerCase(), ".");
        // ----------------------------------------------------------

        //String token = "";
      
        if (t.hasMoreTokens())  which = t.nextToken();
        if (t.hasMoreTokens())  day = t.nextToken();
             
     	  for (int i = 0;i<_daynames.length;i++) {
    	  	if (_daynames[i].equalsIgnoreCase(which)) index = i+1;
    	  }
     	  /*	 First       1=0
     	 Second      2=1
     	 Third       3=2
     	 Fourth      4=3
     	 Last       -1=4  5
     	 <--Second  -2=5  6
     	 <--Third=  -3=6  7
     	 <--Fourth  -4=7  8    */          	 
     	 
     	  if (index > 4)index = (index-4)*(-1); 
     	  
     	  which = String.valueOf(index);
   
        Element daylist = _runtime.getChild("monthdays");
        if (daylist != null) {
            List list = daylist.getChildren("weekday");
            Iterator it = list.iterator();
            while (it.hasNext()) {
                Element e = (Element) it.next();
                if (e.getAttributeValue("day") != null && e.getAttributeValue("day").equalsIgnoreCase(day) && 
                		e.getAttributeValue("which") != null && e.getAttributeValue("which").equals(which)) {
                    e.detach();

                    // remove empty tag
                    if (list.size() == 0)
                        _runtime.removeChild("monthdays");
                    _dom.setChanged(true);
                    if(_runtime != null && _runtime.getParentElement() != null )
                       	_dom.setChangedForDirectory("job", Utils.getAttributeValue("name",_runtime.getParentElement()), SchedulerDom.MODIFY);
                    break;
                }
            }
        }
    }
    
    
   public String[] getDays() {
    	
     	
     	TreeMap days = new TreeMap();
      String day = "";   
      String which = "";
      Weekday w = null;
        if (_runtime != null && _runtime.getChild("monthdays") != null) {
          Element daylist = _runtime.getChild("monthdays");
          List list = daylist.getChildren("weekday");
          int size = list.size();
          _usedDays=new String[size];

          Iterator it = list.iterator();
          int i = 0;
          int index = 0;
          while (it.hasNext()) {
              Element e = (Element) it.next();
              try {
                  day = e.getAttributeValue("day");
                  day = day.substring(0,1).toUpperCase() + day.substring(1).toLowerCase();
                  which = e.getAttributeValue("which");
                  w = (Weekday) days.get(day);
                  if (w == null) w = new Weekday();
                  
                  w.which = w.which+","+which;
                  w.day = day;
                  w.elements.put(which,e);
                  
                  days.put(day,w);
              } catch (Exception ex) {
                  System.out.println("Invalid weekday element in monthdays:"+ex.getMessage());
              }
          }

          Iterator weekdayV_it = days.values().iterator();
          i = 0;
          while (weekdayV_it.hasNext()) {
            w =  (Weekday) weekdayV_it.next();
            
            StringTokenizer t = new StringTokenizer(w.which, ",");
            // ----------------------------------------------------------

            String token = "";
       
            while (t.hasMoreTokens()) {
                 token = t.nextToken();
                 if (!token.equals("") && token != null && !token.equals("null")) {
                	 i = Integer.parseInt(token)-1;
                	 
                /*	 First       1=0
                	 Second      2=1
                	 Third       3=2
                	 Fourth      4=3
                	 Last       -1=4
                	 <--Second  -2=5
                	 <--Third=  -3=6
                	 <--Fourth  -4=7     */          	 
                	 
               	   if (i < 0) i = 3 + (-1)*(i+1);
                    _usedDays[index] = _daynames[i] + "." + w.day;
                     index++;
                  }
                 }
            }
       }else {
      	 _usedDays = new String[0];
       }
        return _usedDays;
    }

    public void fillTreeDays(TreeItem parent, boolean expand) {
    	
    	//1.Reading Node "monthdays"
    	//2.for each day making instance
    	//3.             setting which (e.g. 1,3,-4)
    	//4.Iterate all found days 
    	//5.Create nodes for tree (parsing with tokenizer)
    
     	parent.removeAll();
     	
     	TreeMap days = new TreeMap();
      String day = "";   
      String which = "";
      Weekday w = null;
        if (_runtime != null && _runtime.getChild("monthdays") != null) {
          Element daylist = _runtime.getChild("monthdays");
          List list = daylist.getChildren("weekday");
          //int size = list.size();
       

          Iterator it = list.iterator();
          int i = 0;
          while (it.hasNext()) {
              Element e = (Element) it.next();
              try {
                  day = e.getAttributeValue("day");
                  day = day.substring(0,1).toUpperCase() + day.substring(1).toLowerCase();

                  which = e.getAttributeValue("which");
                  w = (Weekday) days.get(day);
                  if (w == null) w = new Weekday();
                  
                  w.which = w.which+","+which;
                  w.day = day;
                  w.elements.put(which,e);
                  
                  days.put(day,w);
              } catch (Exception ex) {
                  System.out.println("Invalid weekday element in monthdays:"+ex.getMessage());
              }
          }

          Iterator weekdayV_it = days.values().iterator();
          while (weekdayV_it.hasNext()) {
            w =  (Weekday) weekdayV_it.next();
            TreeItem itemDay = new TreeItem(parent, SWT.NONE);
            itemDay.setText(w.day);
            if(!Utils.isElementEnabled("job", _dom, _runtime)) {
            	itemDay.setForeground(Display.getCurrent().getSystemColor(SWT.COLOR_GRAY));
            }
            StringTokenizer t = new StringTokenizer(w.which, ",");
            // ----------------------------------------------------------

            String token = "";
  
            while (t.hasMoreTokens()) {
                 token = t.nextToken();
                 if (!token.equals("") && token != null && !token.equals("null")) {
                	  TreeItem item = new TreeItem(itemDay, SWT.NONE);
                	  i = Integer.parseInt(token)-1;
                	  if (i < 0) i = (i+1)*(-1) + 3;
                   	  
                    item.setText(_daynames[i]);
                    item.setData(new TreeData(Editor.PERIODS, (Element)w.elements.get(token), Options.getHelpURL("periods")));
                    
                    if(!Utils.isElementEnabled("job", _dom, _runtime)) {
                    	item.setForeground(Display.getCurrent().getSystemColor(SWT.COLOR_GRAY));
                    }
                 }
             }
            itemDay.setExpanded(expand);
          }
        parent.setExpanded(expand);
       }
    }
   
}
