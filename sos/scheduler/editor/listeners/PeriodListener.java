package sos.scheduler.editor.listeners;

import org.jdom.Element;

import sos.scheduler.editor.app.DomParser;
import sos.scheduler.editor.app.Utils;

public class PeriodListener {

	private DomParser _dom;

	private Element _period;

	public PeriodListener(DomParser dom) {
		_dom = dom;
	}

	public void setPeriod(Element period) {
		_period = period;
	}

	public Element getPeriod() {
		return _period;
	}

	public String getBegin() {
		return Utils.getTime(getBeginHours(), getBeginMinutes(),
				getBeginSeconds(), false);
	}

	public String getEnd() {
		return Utils.getTime(getEndHours(), getEndMinutes(), getEndSeconds(),
				false);
	}

	public String getRepeat() {
		return Utils.getTime(getRepeatHours(), getRepeatMinutes(),
				getRepeatSeconds(), true);
	}

	public String getSingle() {
		return Utils.getTime(getSingleHours(), getSingleMinutes(),
				getSingleSeconds(), false);
	}

	public int getBeginHours() {
		return Utils.getHours(_period.getAttributeValue("begin"), 0);
	}

	public void setBeginHours(int hours) {
		_period.setAttribute("begin", Utils.getTime(hours, getBeginMinutes(),
				getBeginSeconds(), false));
	}

	public int getBeginMinutes() {
		return Utils.getMinutes(_period.getAttributeValue("begin"), 0);
	}

	public void setBeginMinutes(int minutes) {
		_period.setAttribute("begin", Utils.getTime(getBeginHours(), minutes,
				getBeginSeconds(), false));
	}

	public int getBeginSeconds() {
		return Utils.getSeconds(_period.getAttributeValue("begin"), 0);
	}

	public void setBeginSeconds(int seconds) {
		_period.setAttribute("begin", Utils.getTime(getBeginHours(),
				getBeginMinutes(), seconds, false));
	}

	public int getEndHours() {
		return Utils.getHours(_period.getAttributeValue("end"), 24);
	}

	public void setEndHours(int hours) {
		_period.setAttribute("end", Utils.getTime(hours, getEndMinutes(),
				getEndSeconds(), false));
	}

	public int getEndMinutes() {
		return Utils.getMinutes(_period.getAttributeValue("end"), 0);
	}

	public void setEndMinutes(int minutes) {
		_period.setAttribute("end", Utils.getTime(getEndHours(), minutes,
				getEndSeconds(), false));
	}

	public int getEndSeconds() {
		return Utils.getSeconds(_period.getAttributeValue("end"), 0);
	}

	public void setEndSeconds(int seconds) {
		_period.setAttribute("end", Utils.getTime(getEndHours(),
				getEndMinutes(), seconds, false));
	}

	public int getRepeatHours() {
		return Utils.getHours(_period.getAttributeValue("repeat"), 0);
	}

	public void setRepeatHours(int hours) {
		_period.setAttribute("repeat", Utils.getTime(hours, getRepeatMinutes(),
				getRepeatSeconds(), true));
	}

	public int getRepeatMinutes() {
		return Utils.getMinutes(_period.getAttributeValue("repeat"), 0);
	}

	public void setRepeatMinutes(int minutes) {
		_period.setAttribute("repeat", Utils.getTime(getRepeatHours(), minutes,
				getRepeatSeconds(), true));
	}

	public int getRepeatSeconds() {
		return Utils.getSeconds(_period.getAttributeValue("repeat"), 0);
	}

	public void setRepeatSeconds(int seconds) {
		_period.setAttribute("repeat", Utils.getTime(getRepeatHours(),
				getRepeatMinutes(), seconds, true));
	}

	public int getSingleHours() {
		return Utils.getHours(_period.getAttributeValue("single_start"), 0);
	}

	public void setSingleHours(int hours) {
		_period.setAttribute("single_start", Utils.getTime(hours,
				getSingleMinutes(), getSingleSeconds(), false));
	}

	public int getSingleMinutes() {
		return Utils.getMinutes(_period.getAttributeValue("single_start"), 0);
	}

	public void setSingleMinutes(int minutes) {
		_period.setAttribute("single_start", Utils.getTime(getSingleHours(),
				minutes, getSingleSeconds(), false));
	}

	public int getSingleSeconds() {
		return Utils.getSeconds(_period.getAttributeValue("single_start"), 0);
	}

	public void setSingleSeconds(int seconds) {
		_period.setAttribute("single_start", Utils.getTime(getSingleHours(),
				getSingleMinutes(), seconds, false));
	}

	public boolean getLetRun() {
		String letrun = _period.getAttributeValue("let_run");
		return letrun == null ? false : letrun.equalsIgnoreCase("yes");
	}

	public void setLetRun(boolean letrun) {
		_period.setAttribute("let_run", letrun ? "yes" : "no");
		//_dom.setChanged(true);
	}
	
	public boolean getRunOnce() {
		return Utils.isAttributeValue("once", _period);
	}
	
	public void setRunOnce(boolean once) {
		Utils.setAttribute("once", once, false, _period, _dom);
	}
}
