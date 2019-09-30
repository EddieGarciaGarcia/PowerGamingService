package com.eddie.exceptions;

public class DuplicateInstanceException extends InstanceException {

    public DuplicateInstanceException(Object key, String className) {
        super("Instancia Duplicada ", key, className);
    }    

}
