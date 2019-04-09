package com.logpie.dba.dao;

import java.util.Locale;

public enum Join {
    INNER, LEFT, RIGHT, FULL;

    public static Join fromString(String value)
    {
        return Join.valueOf(value.toLowerCase(Locale.US));
    }

    @Override
    public String toString()
    {
        switch(this)
        {
            case INNER:
                return "INNER JOIN";
            case LEFT:
                return "LEFT JOIN";
            case RIGHT:
                return "RIGHT JOIN";
            case FULL:
                return "FULL OUTER JOIN";
            default:
                return "";
        }
    }
}
