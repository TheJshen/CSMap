/**
 * Data Access Object for a UCSD class. Provides methods to access and retrieve data
 * from the database.
 * @author David Luu
 */

package com.example.kcco.csmap.DAO;

import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseException;

import java.util.ArrayList;

/**
 * Created by David on 5/26/2015.
 */
public class ClassroomDAO {

    public static final int QUERY_LIMIT = 9000;

    private ParseObject thisClass;


    /**
     * Constructor for new UCSDClass_DAO objects. Initializes thisClass as a
     * new ParseObject.
     */
    public ClassroomDAO() {
        thisClass = new ParseObject("UCSDClass");
    }


    /**
     * Constructor for existing UCSDClass_DAO objects. This constructor is intended
     * to be used in query to encapsulate the ParseObject(s) returned.
     * @param obj The ParseObject to encapsulate
     */
    public ClassroomDAO(ParseObject obj) {
        thisClass = obj;
    }


    /**
     * Saves the current parse object in the database.
     */
    public void save() {
        try {
            thisClass.save();
        }
        catch (ParseException e) {
            System.err.println("Error: Failed to save current object");
        }
    }


    /**
     * Queries the database for classes within the inputted building.
     * @param buildingName The building to return classes for - buildings are stored
     *                     by building codes.
     * @return An arraylist of ClassroomDAOs where each DAO represents a single
     * class
     */
    public static ArrayList<ClassroomDAO> query(String buildingName) {
        ArrayList<ClassroomDAO> results = new ArrayList<ClassroomDAO>();

        ParseQuery<ParseObject> query = ParseQuery.getQuery("UCSDClass");
        query.setLimit(QUERY_LIMIT); //Bypass default limit of 100
        query.whereEqualTo("BuildingName", buildingName);

        try {
            ArrayList<ParseObject> resultList = (ArrayList<ParseObject>) query.find();

            if(resultList == null) {
                System.err.println("Error: Query returned nothing");
            }
            else {
                for(ParseObject currObj : resultList) {
                    results.add(new ClassroomDAO(currObj));
                }
            }
        }
        catch(ParseException e) {
            System.err.println("Error: Failed to retrieve object");
        }

        return results;
    }


    /* Setters and Getters to access the ParseObject */

    public void setBuildingName(String buildingName) {
        thisClass.put("BuildingName", buildingName);
    }

    public String getBuildingName() {
        String buildingName = thisClass.getString("BuildingName");

        if(buildingName == null) {
            System.err.println("Error: Database access failed");
            return "";
        }
        else {
            return buildingName;
        }
    }



    public void setClassroomNumber(String classroomNumber) {
        thisClass.put("ClassroomNumber", classroomNumber);
    }

    public String getClassroomNumber() {
        String classroomNumber = thisClass.getString("ClassroomNumber");

        if(classroomNumber == null) {
            System.err.println("Error: Database access failed");
            return "";
        }
        else {
            return classroomNumber;
        }
    }



    public void setCourseCode(String courseCode) {
        thisClass.put("CourseCode", courseCode);
    }

    public String getCourseCode() {
        String courseCode = thisClass.getString("CourseCode");

        if(courseCode == null) {
            System.err.println("Error: Database access failed");
            return "";
        }
        else {
            return courseCode;
        }
    }




    public void setCourseName(String courseName) {
        thisClass.put("CourseName", courseName);
    }

    public String getCourseName() {
        String courseName = thisClass.getString("CourseName");

        if(courseName == null) {
            System.err.println("Error: Database access failed");
            return "";
        }
        else {
            return courseName;
        }
    }



    public void setMeetingType(String meetingType) {
        thisClass.put("MeetingType", meetingType);
    }

    public String getMeetingType() {
        String courseCode = thisClass.getString("MeetingType");

        if(courseCode == null) {
            System.err.println("Error: Database access failed");
            return "";
        }
        else {
            return courseCode;
        }
    }



    public void setDaysOfWeek(String daysOfWeek) {
        thisClass.put("DaysOfWeek", daysOfWeek);
    }

    public String getDaysOfWeek() {
        String daysOfWeek = thisClass.getString("DaysOfWeek");

        if(daysOfWeek == null) {
            System.err.println("Error: Database access failed");
            return "";
        }
        else {
            return daysOfWeek;
        }
    }



    public void setStartTime(String startTime) {
        thisClass.put("StartTime", startTime);
    }

    public String getStartTime() {
        String startTime = thisClass.getString("StartTime");

        if(startTime == null) {
            System.err.println("Error: Database access failed");
            return "";
        }
        else {
            return startTime;
        }
    }



    public void setEndTime(String endTime) {
        thisClass.put("EndTime", endTime);
    }

    public String getEndTime() {
        String endTime = thisClass.getString("EndTime");

        if(endTime == null) {
            System.err.println("Error: Database access failed");
            return "";
        }
        else {
            return endTime;
        }
    }

}
