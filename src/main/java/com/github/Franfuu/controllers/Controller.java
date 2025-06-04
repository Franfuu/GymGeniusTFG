package com.github.Franfuu.controllers;

import com.github.Franfuu.App;



public abstract class Controller {
    /**
     * Represents an abstract base class for controllers in the application.
     * Defines abstract methods for handling opening and closing events.
     * Provides a method to set the application instance.
     *
     * @param app the instance of the main application
     */
    App app;

    /**
     * Sets the instance of the main application.
     *
     * @param app the instance of the main application
     */
    public void setApp(App app) {
        this.app = app;
    }

    /**
     * Defines the method to be implemented for handling the opening event.
     *
     * @param input the input data for the opening event
     * @throws Exception if there's an issue during event handling
     */
    public abstract void onOpen(Object input) throws Exception;

    /**
     * Defines the method to be implemented for handling the closing event.
     *
     * @param output the output data for the closing event
     */
    public abstract void onClose(Object output);

}


