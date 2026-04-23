package org.rplbo.app.ug8.Controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.rplbo.app.ug8.InventoryItem;
import org.rplbo.app.ug8.UmbrellaApp;
import org.rplbo.app.ug8.UmbrellaDBManager;

import java.net.URL;
import java.util.ResourceBundle;

public class UmbrellaController implements Initializable {
    @FXML private TextField txtItem, txtAcquired, txtUsed;
    @FXML private TableView<InventoryItem> tableInventory;
    @FXML private TableColumn<InventoryItem, String> colName;
    @FXML private TableColumn<InventoryItem, Integer> colAcquired, colUsed, colStock;

    private UmbrellaDBManager db;
    private ObservableList<InventoryItem> masterData = FXCollections.observableArrayList();
    private InventoryItem selectedItem;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        db = new UmbrellaDBManager();
        System.out.println("LOG: OPERATIVE " + UmbrellaApp.loggedInUser + " ACCESS GRANTED.");

        colName.setCellValueFactory(new PropertyValueFactory<>("itemName"));
        colAcquired.setCellValueFactory(new PropertyValueFactory<>("acquired"));
        colUsed.setCellValueFactory(new PropertyValueFactory<>("used"));
        colStock.setCellValueFactory(new PropertyValueFactory<>("totalStock"));

        db = new UmbrellaDBManager();
        System.out.println("LOG: OPERATIVE " + UmbrellaApp.loggedInUser + " ACCESS GRANTED.");

        // ==============================================================================
        // TODO 1: MENGHUBUNGKAN KOLOM TABEL (TABLE COLUMN MAPPING)
        // ==============================================================================
        colName.setCellValueFactory(new PropertyValueFactory<>("itemName"));
        colAcquired.setCellValueFactory(new PropertyValueFactory<>("acquired"));
        colUsed.setCellValueFactory(new PropertyValueFactory<>("used"));
        colStock.setCellValueFactory(new PropertyValueFactory<>("totalStock"));

        // ==============================================================================
        // TODO 2: LISTENER KLIK BARIS TABEL (SELECTION MODEL)
        // ==============================================================================
        tableInventory.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                selectedItem = newVal;
                txtItem.setText(newVal.getItemName());
                txtAcquired.setText(String.valueOf(newVal.getAcquired()));
                txtUsed.setText(String.valueOf(newVal.getUsed()));
                txtItem.setDisable(true); // Primary key tidak boleh diubah
            }
        });

        refreshTable();
    }

    @FXML
    private void handleSave() {
        if (selectedItem != null) {
            try {
                int acquired = Integer.parseInt(txtAcquired.getText());
                int used = Integer.parseInt(txtUsed.getText());
                int stock = acquired - used;

                InventoryItem updatedItem = new InventoryItem(
                        selectedItem.getItemName(),
                        acquired,
                        used,
                        stock
                );

                if (db.updateItem(updatedItem)) {
                    refreshTable();
                    clearFields();
                }
            } catch (NumberFormatException e) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Input Error");
                alert.setHeaderText(null);
                alert.setContentText("Acquired dan Used harus berupa angka.");
                alert.showAndWait();
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Warning");
            alert.setHeaderText(null);
            alert.setContentText("Pilih item yang ingin diupdate terlebih dahulu.");
            alert.showAndWait();
        }
    }

    @FXML
    private void handleAdd() {
        try {
            int acquired = Integer.parseInt(txtAcquired.getText());
            int used = Integer.parseInt(txtUsed.getText());
            int stock = acquired - used;
            String itemName = txtItem.getText();

            InventoryItem item = new InventoryItem(itemName, acquired, used, stock);
            db.addItem(item);
            refreshTable();
            clearFields();
        } catch (NumberFormatException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Input Error");
            alert.setHeaderText(null);
            alert.setContentText("Acquired dan Used harus berupa angka.");
            alert.showAndWait();
        }
    }

    @FXML
    private void handleDelete() {
        InventoryItem item = tableInventory.getSelectionModel().getSelectedItem();

        if (item != null) {
            Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
            confirm.setTitle("Konfirmasi Hapus");
            confirm.setHeaderText(null);
            confirm.setContentText("Yakin ingin menghapus item: " + item.getItemName() + "?");

            if (confirm.showAndWait().orElse(ButtonType.CANCEL) == ButtonType.OK) {
                if (db.deleteItem(item.getItemName())) {
                    masterData.remove(item);
                    clearFields();
                }
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Warning");
            alert.setHeaderText(null);
            alert.setContentText("Pilih item yang ingin dihapus terlebih dahulu.");
            alert.showAndWait();
        }
    }

    @FXML
    private void handleLogout() {
        try {
            UmbrellaApp.switchScene("login-view.fxml");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void clearFields() {
        txtItem.clear();
        txtAcquired.clear();
        txtUsed.clear();
        txtItem.setDisable(false);
        txtUsed.setDisable(false);
        selectedItem = null;
    }

    @FXML
    private void refreshTable() {
        masterData.setAll(db.getAllItems());
        tableInventory.setItems(masterData);
    }
}