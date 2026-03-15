package cs151.application;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
/**
 * Controller class for the application's Home screen.
 * This class handles navigation logic for studying, managing decks, and quitting.
 * * @author Jailyn
 * @author Naman Kumar (JavaDoc Documentation)
 * @version 0.2
 */
public class HomeController {
    /**
     * Handles the event when the Study button is clicked.
     * @param event The action event triggered by the user.
     */
    @FXML
    private void onStudyClicked(ActionEvent event) {
        System.out.println("Study clicked (TODO: navigate to study screen)");
    }
    /**
     * Navigates the user to the Deck Management (Define Deck) view.
     * @param event The action event triggered by the user.
     * @throws IOException If the FXML file for the deck view cannot be found or loaded.
     */
    @FXML
    private void onManageDecksClicked(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(Main.class.getResource("define-deck-view.fxml"));
        Scene scene = new Scene(loader.load(), 800, 500);

        Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
        stage.setScene(scene);
    }
    /**
     * Terminates the application.
     * @param event The action event triggered by the user.
     */
    @FXML
    private void onQuitClicked(ActionEvent event) {
        Platform.exit();
    }
}