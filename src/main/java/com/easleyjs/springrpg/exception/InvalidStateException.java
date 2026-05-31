package com.easleyjs.springrpg.exception;

public class InvalidGameActionException extends RuntimeException {
    public InvalidStateException(String message) {
        super(message);
    }
}
