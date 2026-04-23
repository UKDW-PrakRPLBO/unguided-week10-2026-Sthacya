package org.rplbo.app.ug8.Controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import org.rplbo.app.ug8.UmbrellaApp;
import org.rplbo.app.ug8.UmbrellaDBManager;

public class LoginController {
    @FXML private TextField txtId;
    @FXML private PasswordField txtPasscode;

    @FXML
    private void handleLogin(ActionEvent event) {
        String username = txtId.getText().trim();
        String password = txtPasscode.getText().trim();

        UmbrellaDBManager db = new UmbrellaDBManager();
        String fullName = db.validateUser(username, password);

        if (fullName != null) {
            try {
                UmbrellaApp.loggedInUser = fullName;
                UmbrellaApp.switchScene("umbrella-view.fxml");
            } catch (Exception e) {
                e.printStackTrace();
                new Alert(Alert.AlertType.ERROR, "FAILED TO LOAD MAIN PAGE").showAndWait();
            }
        } else {
            new Alert(Alert.AlertType.ERROR, "AUTHENTICATION FAILED").showAndWait();
            txtPasscode.clear();
            txtPasscode.requestFocus();
        }
    }


        // ==============================================================================
        // TODO 1: PROSES AUTENTIKASI (LOGIN)
        // ==============================================================================
        // 1. Ambil input teks dari txtUsername dan txtPassword.
        // 2. Buat instansiasi dari class UmbrellaDBManager.
        // 3. Panggil metode validateUser() dari db manager tersebut.
        // 4. Jika hasil validasi berhasil (tidak null):
        //    a. Simpan nama user ke variabel statis UmbrellaApp.loggedInUser.
        //    b. Pindah ke halaman "umbrella-view.fxml" menggunakan UmbrellaApp.switchScene().
        // 5. Jika gagal, tampilkan pesan error "AUTHENTICATION FAILED" pada lblStatus.
        // ==============================================================================

        // --- TULIS KODE ANDA DI BAWAH INI ---



}