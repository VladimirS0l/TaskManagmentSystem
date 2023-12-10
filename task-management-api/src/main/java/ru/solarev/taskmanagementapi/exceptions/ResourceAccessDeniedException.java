package ru.solarev.taskmanagementapi.exceptions;

public class ResourceAccessDeniedException extends RuntimeException{
    public ResourceAccessDeniedException(String msg) {
        super(msg);
    }
}
