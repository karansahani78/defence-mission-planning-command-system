package com.karan.mission_planning_system.exception;

public class CommandNotFoundException extends RuntimeException {
    public CommandNotFoundException(String message){
        super(message);
    }
}
