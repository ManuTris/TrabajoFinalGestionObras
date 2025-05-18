
package controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.control.TextField;
import javafx.scene.control.PasswordField;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import util.FirebaseService;

public class LoginController {

    @FXML private TextField usernameField;
    @FXML private PasswordField passwordField;

    @FXML
    private void handleLogin() {
        String user = usernameField.getText();
        String pass = passwordField.getText();

        if (util.FirebaseService.verificarCredenciales(user, pass)) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/MainView.fxml"));
                Parent root = loader.load();
                Stage stage = (Stage) usernameField.getScene().getWindow();
                Scene scene = new Scene(root);

                stage.setScene(scene);
                stage.setTitle("Panel Principal");

           
                stage.setFullScreen(true);
                stage.setFullScreenExitHint("");

                stage.show();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            showAlert("Error", "Credenciales incorrectas.");
        }
    }


    private void showAlert(String title, String message) {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
