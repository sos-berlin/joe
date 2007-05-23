package sos.scheduler.editor.conf.listeners;

import org.eclipse.swt.widgets.Button;
import org.jdom.Element;

import sos.scheduler.editor.app.Utils;
import sos.scheduler.editor.conf.SchedulerDom;

public class PeriodListener {

    private SchedulerDom _dom;

    private Element      _period;

    private Element      _at;

    public PeriodListener(SchedulerDom dom) {
        _dom = dom;
    }


    public void setPeriod(Element period) {
    	if(period != null && period.getName().equals("at"))
    		_at = period;
    	
        _period = period;    	
    }


    public Element getPeriod() {
        return _period;
    }


    public String getBegin() {
        return Utils.getTime(getBeginHours(), getBeginMinutes(), getBeginSeconds(), false);
    }


    public String getEnd() {
        return Utils.getTime(getEndHours(), getEndMinutes(), getEndSeconds(), false);
    }


    public String getRepeat() {
        return Utils.getTime(getRepeatHours(), getRepeatMinutes(), getRepeatSeconds(), true);
    }


    public String getSingle() {
        return Utils.getTime(getSingleHours(), getSingleMinutes(), getSingleSeconds(), false);
    }


    public String getBeginHours() {
        return Utils.getIntegerAsString(Utils.getHours(_period.getAttributeValue("begin"), -999));
    }


    public void setPeriodTime(int maxHour, Button bApply, String node, String hours, String minutes, String seconds) {
      
    	if (_period != null ){
    		//if(_period.getName().equals("period")) {
    			//Utils.setAttribute(node, Utils.getTime(maxHour, hours, minutes, seconds, false), _period, _dom);
    		//} else 
    		if (node.equals("single_start") && _period.getName().equals("at")) {
    			Utils.setAttribute("at", Utils.getAttributeValue("at", _period).substring(0, 10) + " " + Utils.getTime(maxHour, hours, minutes, seconds, false), _period, _dom);
    		} else if (!node.equals("single_start") && _period.getName().equals("at") && Utils.getAttributeValue("at", _period).length() < 11) {    			
            		//getDatePeriod();
    			if(_period.getParentElement() != null) {
    	    		java.util.List rt = _period.getParentElement().getChildren("date");
    	    		String sDate = Utils.getAttributeValue("at", _period).substring(0, 10);
    	    		for(int i = 0; i < rt.size(); i++) {    	    			
    	    			Element el = (Element)rt.get(i);
    	    			
    	    			if(Utils.getAttributeValue("date", el).equals(sDate)){
    	    				removeAtElement(Utils.getAttributeValue("at", _period));
    	    				_period = new Element("period");
    	    				el.addContent(_period);
    	    				Utils.setAttribute(node, Utils.getTime(maxHour, hours, minutes, seconds, false), _period, _dom);		
    	    				_dom.setChanged(true);
    	    				break;    	    				
    	    			}
    	    		}
    	    	}
            		
    		} else {
    			Utils.setAttribute(node, Utils.getTime(maxHour, hours, minutes, seconds, false), _period, _dom);
    		}
          if (bApply != null) {
              bApply.setEnabled(true);
          }
    	}     
  }



    public void setRepeatSeconds(Button bApply, String seconds) {
        Utils.setAttribute("repeat", seconds, _period, _dom);
        if (bApply != null) {
            bApply.setEnabled(true);
        }
    }


    public String getBeginMinutes() {
        return Utils.getIntegerAsString(Utils.getMinutes(_period.getAttributeValue("begin"), -999));
    }


    public String getBeginSeconds() {
        return Utils.getIntegerAsString(Utils.getSeconds(_period.getAttributeValue("begin"), -999));
    }


    public String getEndHours() {
      return Utils.getIntegerAsString(Utils.getHours(_period.getAttributeValue("end"), -999));
   }
    
   


    public String getEndMinutes() {
        return Utils.getIntegerAsString(Utils.getMinutes(_period.getAttributeValue("end"), -999));
    }


    public String getEndSeconds() {
        return Utils.getIntegerAsString(Utils.getSeconds(_period.getAttributeValue("end"), -999));
    }


    public String getRepeatHours() {
        return Utils.getIntegerAsString(Utils.getHours(_period.getAttributeValue("repeat"), -999));
    }


    public String getRepeatMinutes() {
        return Utils.getIntegerAsString(Utils.getMinutes(_period.getAttributeValue("repeat"), -999));
    }


    public String getRepeatSeconds() {
        return Utils.getIntegerAsString(Utils.getSeconds(_period.getAttributeValue("repeat"), -999));
    }


    public String getSingleHours() {
    	if(_period!=null && _period.getName().equals("at")) 
    		return Utils.getIntegerAsString(Utils.getHours(_period.getAttributeValue("at") != null ? _period.getAttributeValue("at").split(" ")[1] : "0", -999));
    	else
    		return Utils.getIntegerAsString(Utils.getHours(_period.getAttributeValue("single_start"), -999));
    }


    public String getSingleMinutes() {
    	if(_period!=null && _period.getName().equals("at"))
    		return Utils.getIntegerAsString(Utils.getMinutes(_period.getAttributeValue("at") != null? _period.getAttributeValue("at").split(" ")[1]:"0", -999));
    	else
    		return Utils.getIntegerAsString(Utils.getMinutes(_period.getAttributeValue("single_start"), -999));
    }


    public String getSingleSeconds() {
    	if(_period!=null && _period.getName().equals("at"))
    		return Utils.getIntegerAsString(Utils.getSeconds(_period.getAttributeValue("at") != null ? _period.getAttributeValue("at").split(" ")[1] :"0", -999));
    	else
    		return Utils.getIntegerAsString(Utils.getSeconds(_period.getAttributeValue("single_start"), -999));
    }


    public boolean getLetRun() {
        String letrun = _period.getAttributeValue("let_run");
        return letrun == null ? false : letrun.equalsIgnoreCase("yes");
    }


    public void setLetRun(boolean letrun) {
        _period.setAttribute("let_run", letrun ? "yes" : "no");
        _dom.setChanged(true);
    }


    public boolean getRunOnce() {
        return Utils.isAttributeValue("once", _period);
    }


    public void setRunOnce(boolean once) {
        Utils.setAttribute("once", once, false, _period);
        _dom.setChanged(true);
    }

    public void setAtElement(Element at) {    	    	
    	_period=null;
		_at = at;	
	}
    
    public Element getAtElement() {
        return _at;
    }
    
   //Liefert den Date Knoten, wenn der at Knoten zu date Knoten umgewandselt werden soll
    /*public void getDatePeriod() {
    	
    	
    	if(_period.getName().equals("at") && _period.getParentElement() != null) {
    		java.util.List rt = _period.getParentElement().getChildren("date");
    		String sDate = Utils.getAttributeValue("at", _period).substring(0, 10);
    		for(int i = 0; i < rt.size(); i++) {
    			
    			Element el = (Element)rt.get(i);
    			
    			if(Utils.getAttributeValue("date", el).equals(sDate)){
    				_period = new Element("period");
    				el.addContent(_period);
    				
    				
    				//removeAtElement(Utils.getAttributeValue("at", _period));
    				//setPeriod(el);
    				
    			}
    		}
    	} else  {
    		
    	}
    	
    }
    */
    private void removeAtElement(String date) {
    	if(_period.getName().equals("at") && _period.getParentElement() != null) {
    		java.util.List rt = _period.getParentElement().getChildren("at");
    		for(int i = 0; i < rt.size(); i++) {
    			Element el = (Element)rt.get(i);
    			if(Utils.getAttributeValue("at", el).equals(date)) {
    				rt.remove(i);
    				return;
    			}
    			
    		}
    	}
    	
    }
   
}
