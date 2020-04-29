package com.toechsle.spritwatcher.exceptions;


public class JsonException extends Exception {
    public JsonException() {
       super("This is a general JSON error. "
               + "Check logs for details.)");
    }
}
