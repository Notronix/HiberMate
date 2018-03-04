package com.notronix.hibermate;

class PersistenceException extends Exception
{
    PersistenceException(String message, Throwable cause) {
        super(message, cause);
    }
}
