package io.hhplus.tdd.exception;

import org.springframework.stereotype.Component;

@Component
public class ErrorMessages {
    public static final String USER_NOT_FOUND = "User not found";
    public static final String POINT_NOT_FOUND = "Point not found";
    public static final String HISTORY_NOT_FOUND = "History not found";
    public static final String POINT_NOT_APPLY = "This point cannot be applied.";
    public static final String POINT_MAXIUM = "Points you have is the maximum.";
    public static final String POINT_NOT_ENOUGH = "Not enough point.";
    public static final String POINT_ONLY_POSITIVE = "Points can only be positive numbers";


}
