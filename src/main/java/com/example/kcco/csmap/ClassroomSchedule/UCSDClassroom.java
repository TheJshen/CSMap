package com.example.kcco.csmap.ClassroomSchedule;

/**
 * Class that represents a single classroom. A classroom has an identifying
 * room number and a weekday schedule for the room.
 * @author David Luu
 */

public class UCSDClassroom {
	
	private String roomNumber; //Number associated with this classroom
	private WeekdayTimetable roomSchedule; //Weekly schedule associated with this classroom
	
	
	/**
	 * Constructor for a new UCSDClassroom object. A UCSDClassroom can only be
	 * created with a roomNumber identifier.
	 * @param roomNumber The number of the room
	 * @author David Luu
	 */
	public UCSDClassroom(String roomNumber) {
		this.roomNumber = roomNumber;
		this.roomSchedule = new WeekdayTimetable();
	}
	
	
	/**
	 * Adds a class to the schedule for this UCSDClassroom object.
	 * @param className The name of the class
	 * @param days The days when the class section meets
	 * @param time String representation of the time period in the form "hh:mm(a/p)-hh:mm(a/p)"
	 * @author David Luu
	 */
	public void addClass(String className, String days, String time) {
		roomSchedule.fillInTimePeriod(className, days, time);
	}
	
	
	/**
	 * Returns the string representation of the UCSDClassroom.
	 * @return String representing the classroom object and its schedule
	 * @author David Luu
	 */
	@Override
	public String toString() {
		return this.roomNumber + "\n" + roomSchedule.toString() + "\n";
	}
	
}
