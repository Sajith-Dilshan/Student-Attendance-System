package sajithdilshan.me.sas.controller;


import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import sajithdilshan.me.sas.db.DBConnection;
import sajithdilshan.me.sas.security.Principal;
import sajithdilshan.me.sas.security.SecurityContextHolder;


import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class LoginFormController {
    public TextField txtUsexrName;
    public PasswordField txtPassword;
    public Button btnSignIn;

    public void btnSignIn_OnAction(ActionEvent actionEvent) {
        if(!isValidated()){
            new Alert(Alert.AlertType.ERROR,"Invalid username or password").show();
            txtUsexrName.requestFocus();
            txtUsexrName.selectAll();
            return;
        }


        try {

            Connection connection = DBConnection.getInstance().getConnection();
            PreparedStatement pst = connection.prepareStatement("SELECT name, role FROM user WHERE username=? AND password=?");
            pst.setString(1,txtUsexrName.getText().trim());
            pst.setString(2,txtPassword.getText().trim());
            ResultSet rst = pst.executeQuery();

            if (rst.next()){
                SecurityContextHolder.setPrincipal(new Principal(
                        txtUsexrName.getText(),
                        rst.getString("name"),
                        Principal.UserRole.valueOf(rst.getString("role"))));
                String path = null;

                if (rst.getString("role").equals("ADMIN")){
                    System.out.println("get addmin");
                    path = "/view/AdminHomeForm.fxml";
                }else{
                    path = "/view/UserHomeForm.fxml";
                }

                FXMLLoader fxmlLoader = new FXMLLoader(this.getClass().getResource(path));
                AnchorPane root = fxmlLoader.load();
                Scene homeScene = new Scene(root);
                Stage primaryStage = (Stage)(btnSignIn.getScene().getWindow());
                primaryStage.setScene(homeScene);
                primaryStage.setTitle("Student Attendance System: Home");
                Platform.runLater(()-> {
                    primaryStage.sizeToScene();
                    primaryStage.centerOnScreen();
                });



            }else{
                new Alert(Alert.AlertType.ERROR, "Invalid username or password").show();
                txtUsexrName.requestFocus();
                txtUsexrName.selectAll();
            }



        } catch (SQLException | IOException e) {
            new Alert(Alert.AlertType.ERROR,"Something went wrong, please try again").show();
            e.printStackTrace();
        }


    }


    private boolean isValidated() {
        String username = txtUsexrName.getText().trim();
        String password = txtPassword.getText().trim();

        return !(username.length() < 4 || !username.matches("[A-Za-z0-9]+") || password.length() < 4);
    }
}
