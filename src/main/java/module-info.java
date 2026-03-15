/**
 * Module configuration for the Flashcards application.
 * Defines the necessary JavaFX dependencies and package visibility for Version 0.2.
 * * @author Jailyn
 * @author Naman Kumar (JavaDoc Documentation)
 */
module cs151.application {
    requires javafx.controls;
    requires javafx.fxml;

    requires com.dlsc.formsfx;

    opens cs151.application to javafx.fxml;
    exports cs151.application;
}