package com.example.kcco.csmap.ClassroomSchedule;

import java.util.ArrayList;

/**
 * Class that represents a weekly schedule for Monday-Friday. Has functions
 * fill in and retrieve the data in the schedule.
 * @author David Luu
 */

public class WeekdayTimetable {

	private static final int ARRSIZE = 144; //(6 10-minute intervals in an hour) * (24 hours) = 144
	
	private String monday[];
	private String tuesday[];
	private String wednesday[];
	private String thursday[];
	private String friday[];
	
	
	/**
	 * Default constructor for a new WeekdayTimetable object. Initializes all
	 * arrays to default value (false) to represent an empty timetable.
	 */
	public WeekdayTimetable() {
		this.monday = new String[ARRSIZE];
		this.tuesday = new String[ARRSIZE];
		this.wednesday = new String[ARRSIZE];
		this.thursday = new String[ARRSIZE];
		this.friday = new String[ARRSIZE];
		
		for(int i = 0; i < ARRSIZE; ++i) {
			this.monday[i] = "";
			this.tuesday[i] = "";
			this.wednesday[i] = "";
			this.thursday[i] = "";
			this.friday[i] = "";
		}
	}
	
	
	/**
	 * Fills in the array for the time period described by the input variables.
	 * Delegates to timeToIdx to find the index range to fill in.
	 * @param classTitle Name of the class that is in the room for the given period
	 * @param days Days of the week to fill in in the form "MTuWThF"
	 * @param time String representation of the time period in the form "hh:mm(a/p)-hh:mm(a/p)"
	 */
	public void fillInTimePeriod(String classTitle, String days, String time) {
		String startTime = time.substring(0, time.indexOf('-'));
		String endTime = time.substring(time.indexOf('-')+1);
		
		int startIdx = timeToIdx(startTime);
		int endIdx = timeToIdx(endTime);
		
		for(int i = startIdx; i < endIdx; ++i) {
			if(days.contains("M"))  this.monday[i] = classTitle;
			if(days.contains("Tu")) this.tuesday[i] = classTitle;
			if(days.contains("W"))  this.wednesday[i] = classTitle;
			if(days.contains("Th")) this.thursday[i] = classTitle;
			if(days.contains("F"))  this.friday[i] = classTitle;
		}
	}
	
	
	/**
	 * Helper function for fillInTimePeriod. Takes a string in the form
	 * "hh:mm(a/p)" and returns the array index corresponding to that time.
	 * @param time The string representation of the time to get the array index for
	 * @return Int representing the array index corresponding to the time
	 */
	private int timeToIdx(String time) {
		int hour = Integer.parseInt(time.substring(0, time.indexOf(':')));
		if(time.indexOf('p') != -1 && hour != 12) {
			hour += 12;
		}
		if(time.indexOf('a') != -1 && hour == 12) {
			hour -= 12;
		}
		
		int minute = Integer.parseInt(time.substring(time.indexOf(':') + 1, time.length() - 1));
		
		return (6 * hour) + (minute / 10);
	}
	
	
	/**
	 * Returns the string representation of the WeeklyTimetable. The format is:
	 * "Monday:BlockedTime1,BlockedTime2;Tuesday:BlockedTime1,BlockedTime2....".
	 * Delegates to toStringForDay to get the string representation for
	 * individual days.
	 * @return String representing this WeekdayTimetable object
	 */
	@Override
	public String toString () {
		String str = "";
		
		str += toStringForDay("Monday",this.monday) + "\n";
		str += toStringForDay("Tuesday",this.tuesday) + "\n";
		str += toStringForDay("Wednesday",this.wednesday) + "\n";
		str += toStringForDay("Thursday",this.thursday) + "\n";
		str += toStringForDay("Friday",this.friday);
		
		return str;
	}
	
	
	/**
	 * Helper function for toString. Returns the string representation of the
	 * schedule for the inputed day.
	 * @param day The name of the day to generate a string for
	 * @param dayArr The array containing the information about the day
	 * @return String representing of the schedule for the inputed day
	 */
	private String toStringForDay (String day, String[] dayArr) {
		String str = "\t" + day + ":";
		boolean first = true;
		boolean tracking = false;
		String currName = "";

		for(int i = 0; i < ARRSIZE; ++i) {
			//Continuous period of time, different class
			if(tracking && !dayArr[i].equals(currName)) {
				tracking = false;
				str += idxToTime(i);
			}
			//New period of occupancy
			if(!tracking && !dayArr[i].equals("")) {
				str += '\n';
				if (first) {
					first = false;
				}
				tracking = true;
				currName = dayArr[i];
				str += "\t\t" + dayArr[i] + " -- " + idxToTime(i) + "-";
			}
			//End of period of occupancy
			if(tracking && dayArr[i].equals("")) {
				tracking = false;
				str += idxToTime(i) + '\n';
			}
		}

		return str;
	}
	
	
	/**
	 * Helper function for toStringForDay. Returns the string representation
	 * of the time corresponding to the inputed index. Format is "hh:mm(a/p)"
	 * @param idx Index corresponding the an index in one of the day arrays
	 * @return String representing the time corresponding to the inputed index
	 */
	private String idxToTime (int idx) {
		String str = "";
		
		int hour = idx / 6;
		int minute = (idx % 6) * 10;
		
		if(hour >= 12) {
			if (hour != 12) hour -= 12;
			str += hour + ":";
			
			if(minute == 0) {
				str += "00p";
			}
			else {
				str += minute + "p";
			}
		}
		else {
			if (hour == 0) hour += 12;
			str += hour + ":";
			
			if(minute == 0) {
				str += "00a";
			}
			else {
				str += minute + "a";
			}
		}
		
		return str;
	}

	public ArrayList<ScheduleInfo> toParcelableList() {
		ArrayList<ScheduleInfo> out = new ArrayList<ScheduleInfo>();

		out.addAll(toListForDay("Monday", this.monday));
		out.addAll(toListForDay("Tuesday", this.tuesday));
		out.addAll(toListForDay("Wednesday", this.wednesday));
		out.addAll(toListForDay("Thursday", this.thursday));
		out.addAll(toListForDay("Friday", this.friday));

		return out;
	}

	private ArrayList<ScheduleInfo> toListForDay (String day, String[] dayArr) {
		ArrayList<ScheduleInfo> out = new ArrayList<ScheduleInfo>();

		boolean tracking = false;
		String currName = "";
		String currTime = "";

		for(int i = 0; i < ARRSIZE; ++i) {
			//Continuous period of time, different class
			if(tracking && !dayArr[i].equals(currName)) {
				tracking = false;

				out.add(new ScheduleInfo(day, currTime + idxToTime(i), currName));
				currName = "";
				currTime = "";
			}
			//New period of occupancy
			if(!tracking && !dayArr[i].equals("")) {
				tracking = true;
				currName = dayArr[i];
				currTime +=  idxToTime(i) + "-";
			}
			//End of period of occupancy
			if(tracking && dayArr[i].equals("")) {
				tracking = false;

				out.add(new ScheduleInfo(day, currTime + idxToTime(i), currName));
				currName = "";
				currTime = "";

				currTime += idxToTime(i);
			}
		}

		return out;
	}
}
