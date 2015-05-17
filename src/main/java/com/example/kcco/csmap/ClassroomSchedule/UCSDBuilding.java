package com.example.kcco.csmap.ClassroomSchedule;

import java.util.HashMap;

/**
 * Class that represents a single building. A building has a name and a set
 * of classrooms that are identified by their classroom numbers
 * @author David Luu
 */

public class UCSDBuilding {
	
	private String name; //Name associated with the building
	HashMap<String,UCSDClassroom> classrooms; //Maps classroom names to classroom objects
	
	
	/**
	 * Constructor for a new UCSDBuilding object. A UCSDBuilding can only be
	 * created with a name identifier.
	 * @param name The name of the building
	 * @author David Luu
	 */
	public UCSDBuilding(String name) {
		this.name = name;
		this.classrooms = new HashMap<String,UCSDClassroom>();
	}
	
	
	/**
	 * Makes a new classroom if it has not been previously created then
	 * delegates to UCSDClassroom's addClass().
	 * @param classroomName The room where the class is held
	 * @param courseTitle The name of the class
	 * @param days The days when the class section meets
	 * @param timePeriod String representation of the time period in the form "hh:mm(a/p)-hh:mm(a/p)"
	 * @author David Luu
	 */
	public void addClass(String classroomName, String courseTitle, String days, String timePeriod) {
		//Previously created classroom, just call addClass
		if(this.classrooms.get(classroomName) != null) {
			UCSDClassroom currC = this.classrooms.get(classroomName);
			
			currC.addClass(courseTitle, days, timePeriod);
		}
		//Create new classroom then call addClass
		else {
			UCSDClassroom newC = new UCSDClassroom(classroomName);
			this.classrooms.put(classroomName, newC);
			
			newC.addClass(courseTitle, days, timePeriod);
		}
	}
	
	
	/**
	 * Returns the string representation of the UCSDClassroom.
	 * @return String representing the building object and its classrooms
	 * @author David Luu
	 */
	@Override
	public String toString() {
		String str = "";
		
		//Loop through all classrooms, print out the name and schedules of each classroom encountered
		for(UCSDClassroom currC : this.classrooms.values()) {
			str += this.name + " " + currC.toString();
			str += "\n";
		}
		
		return str;
	}
	
}