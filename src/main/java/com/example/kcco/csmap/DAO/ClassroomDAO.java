package com.example.kcco.csmap.DAO;

import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseException;

import java.util.ArrayList;

/**
 * Created by David on 5/26/2015.
 */
public class ClassroomDAO {

    private ParseObject thisClass;

    public ClassroomDAO() {
        thisClass = new ParseObject("UCSDClass");
    }

    public ClassroomDAO(ParseObject obj) {
        thisClass = obj;
    }

    public void save() {
        try {
            thisClass.save();
        }
        catch (ParseException e) {
            System.err.println("Error: Failed to save current object");
        }
    }

    public static ArrayList<ClassroomDAO> query(String buildingName) {
        ArrayList<ClassroomDAO> results = new ArrayList<ClassroomDAO>();

        ParseQuery<ParseObject> query = ParseQuery.getQuery("UCSDClass");
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
