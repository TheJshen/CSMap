package com.example.kcco.csmap.DAO;

import com.parse.ParseObject;

/**
 * Created by KaChan on 4/30/2015.
 * Describe: This class is designed to handle any usage of Parse data objects,
 *           so this project has easy and limited access for the database. It
 *           is easy because it can simply call function and fill out the args.
 *           It is limited because some data are required to fill. This set up
 *           can ensure any other programmer won't create some extra unexpected
 *           column in Parse data objects. The main usage functions would be
 *           create entry, read/search entry, update entry, or delete entry.
 *           Each entry represent a row in the Parse data objects
 * Updated:
 *          -04/30/15: Ka: created file
 *          -04/30/15: Ka: add constant APP_ID, CLINT_ID
 *          -04/30/15: Ka: add constant column name for Parse Routes Object
 */


public class ParseConstant {

    //These two are used for all Data Objects
    public static final String APP_ID = "zMyzvSTcUfr00FshI09tszXQTtEz42hpFLria99T";
    public static final String CLINT_ID = "pVbhGzQ8FzvHSPFptJ1GHA4CLuLtQWCQ3AUjc7vM";

    //These are column (key) names for Parse Routes Object
    public static final String ROUTES = "Routes";
    public static final String ROUTES_ROUTE_ID = "routeId";
    public static final String ROUTES_STRLOC = "startLoc";
    public static final String ROUTES_ENDLOC = "endLoc";
    public static final String ROUTES_CREATEDBY = "createBy";
    public static final String ROUTES_TIMESPENT = "timeSpent";

    //These are column (key) names for Parse Route# Object
    public static final String ROUTE_NUM_INDEX = "index";
    public static final String ROUTE_NUM_X = "x";
    public static final String ROUTE_NUM_Y = "y";

    //These are column (key names for Parse PossibleEdges Objects
    //All three tables share the same columns name
    public static final String POSSIBLE = "PossibleEdges";
    public static final String POSSIBLE_X1 = "x1";
    public static final String POSSIBLE_Y1 = "y1";
    public static final String POSSIBLE_X2 = "x2";
    public static final String POSSIBLE_Y2 = "y2";
    public static final String POSSIBLE_BLOCKED = "blocked";
    public static final String POSSIBLE_REASON = "reason";

    //These are column (key) names for Parse Places Objects
    public static final String PLACES = "Places";
    public static final String PLACES_PLACE_ID = "placeId";
    public static final String PLACES_NAME = "name";
    public static final String PLACES_CREATED_BY = "createdBy";
    public static final String PLACES_X1 = "x1";
    public static final String PLACES_Y1 = "y1";
    public static final String PLACES_X2 = "x2";
    public static final String PLACES_Y2 = "y2";
    public static final String PLACES_X3 = "x3";
    public static final String PLACES_Y3 = "y3";
    public static final String PLACES_X4 = "x4";
    public static final String PLACES_Y4 = "y4";

    //These are column (key) names for Parse Classroom Objects
    public static final String CLASS = "Classroom";
    public static final String CLASS_NAME = "name";
    public static final String CLASS_PLACE_ID = "placeId";

    //These are column (key) names for Parse Events Objects
    public static final String EVENTS = "Event";
    public static final String EVENTS_NAME = "name";
    public static final String EVENTS_START_TIME = "startTime";
    public static final String EVENTS_END_TIME = "endTime";
    public static final String EVENTS_LOCATION = "location";
    public static final String EVENTS_EVENT_ID = "eventId";

    //These are column (key) names for Parse User Objects
    public static final String USER = "_User";
    public static final String USER_USERNAME = "username";
    public static final String USER_EMAIL = "email";
    public static final String USER_PASSWORD = "password";
    public static final String USER_NAME = "fullName";
    public static final String USER_USER_ID = "userId";
    public static final String USER_PHONE = "phone";

    //These are column (key) names for Parse Rating Objects
    public static final String RATING = "Rating";
    public static final String RATING_ROUTE_ID = "routeId";
    public static final String RATING_RATING = "rating";
    public static final String RATING_RATE_BY = "rateBy";
    public static final String RATING_COMMENT = "comment";

    //These are column (key) names for Parse Bookmark Objects
    public static final String BOOKMARK = "Bookmark";
    public static final String BOOKMARK_ROUTE_ID = "routeId";
    public static final String BOOKMARK_USER_ID = "userId";

    //These are column (key) names for Parse History Objects
    public static final String HISTORY = "History";
    public static final String HISTORY_ROUTE_ID = "routeId";
    public static final String HISTORY_USER_ID = "userId";
    public static final String HISTORY_CREATED_AT = "createdAt";

}
