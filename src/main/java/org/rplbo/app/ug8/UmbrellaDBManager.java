package org.rplbo.app.ug8;

import java.sql.*;
import java.util.ArrayList;

public class UmbrellaDBManager {
    // Menggunakan variabel ini secara konsisten untuk Grup B
    private static final String URL = "jdbc:sqlite:umbrella_inventory_b.db";

    public UmbrellaDBManager() {
        try (Connection conn = DriverManager.getConnection(URL);
             Statement stmt = conn.createStatement()) {

            // Tabel Users (Sama dengan Grup A)
            stmt.execute("CREATE TABLE IF NOT EXISTS users (" +
                    "id_users INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "nama_lengkap TEXT, " +
                    "no_id_card TEXT, " +
                    "username TEXT UNIQUE, " +
                    "password TEXT)");

            // Tabel Inventory (KHUSUS GRUP B)
            stmt.execute("CREATE TABLE IF NOT EXISTS inventory (" +
                    "item_name TEXT PRIMARY KEY, " +
                    "initial_stock INTEGER, " +
                    "new_supply INTEGER, " +
                    "final_stock INTEGER)");

            // Data Awal User Sector B (Jika tabel kosong)
            ResultSet rs = stmt.executeQuery("SELECT COUNT(*) FROM users");
            if (rs.next() && rs.getInt(1) == 0) {
                stmt.execute("INSERT INTO users (nama_lengkap, no_id_card, username, password) " +
                        "VALUES ('HUNK (U.S.S. Alpha)', 'USS-001', 'hunk', '123')");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Fitur Login: Validasi User dari Database
    public String validateUser(String user, String pass) {
        String sql = "SELECT nama_lengkap FROM users WHERE username = ? AND password = ?";
        try (Connection conn = DriverManager.getConnection(URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, user);
            pstmt.setString(2, pass);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return rs.getString("nama_lengkap");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // Fitur Tambah Data (Grup B)
    public void addItem(InventoryItem item) {
        String sql = "INSERT INTO inventory (item_name, initial_stock, new_supply, final_stock) VALUES(?,?,?,?)";
        try (Connection conn = DriverManager.getConnection(URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, item.getItemName());
            pstmt.setInt(2, item.getAcquired());
            pstmt.setInt(3, item.getUsed());
            pstmt.setInt(4, item.getTotalStock());
            pstmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Fitur Tampil Data (Grup B)
    public ArrayList<InventoryItem> getAllItems() {
        ArrayList<InventoryItem> list = new ArrayList<>();
        String sql = "SELECT * FROM inventory";

        try (Connection conn = DriverManager.getConnection(URL);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                list.add(new InventoryItem(
                        rs.getString("item_name"),
                        rs.getInt("initial_stock"),
                        rs.getInt("new_supply"),
                        rs.getInt("final_stock")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    // Fitur Update Data (Grup B)
    public boolean updateItem(InventoryItem item) {
        String sql = "UPDATE inventory SET initial_stock = ?, new_supply = ?, final_stock = ? WHERE item_name = ?";
        try (Connection conn = DriverManager.getConnection(URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, item.getAcquired());
            pstmt.setInt(2, item.getUsed());
            pstmt.setInt(3, item.getTotalStock());
            pstmt.setString(4, item.getItemName());

            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Fitur Hapus Data (Grup B)
    public boolean deleteItem(String itemName) {
        String sql = "DELETE FROM inventory WHERE item_name = ?";
        try (Connection conn = DriverManager.getConnection(URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, itemName);
            int rowsAffected = pstmt.executeUpdate();

            // Mengembalikan true jika ada baris yang berhasil dihapus
            return rowsAffected > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}