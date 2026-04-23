package org.rplbo.app.ug8;

/**
 * Model class untuk item inventaris Sector B.
 * Digunakan untuk mapping data dari database SQLite ke TableView JavaFX.
 */
public class InventoryItem {
    private String itemName;
    private int acquired;    // jumlah masuk
    private int used;        // jumlah terpakai
    private int totalStock;  // stok akhir

    /**
     * Constructor untuk inisialisasi objek InventoryItem.
     */
    public InventoryItem(String itemName, int acquired, int used, int totalStock) {
        this.itemName = itemName;
        this.acquired = acquired;
        this.used = used;
        this.totalStock = totalStock;
    }

    // Getter dan Setter untuk Item Name
    public String getItemName() {
        return itemName;
    }
    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    // Getter dan Setter untuk Acquired
    public int getAcquired() {
        return acquired;
    }
    public void setAcquired(int acquired) {
        this.acquired = acquired;
    }

    // Getter dan Setter untuk Used
    public int getUsed() {
        return used;
    }
    public void setUsed(int used) {
        this.used = used;
    }

    // Getter dan Setter untuk Total Stock
    public int getTotalStock() {
        return totalStock;
    }
    public void setTotalStock(int totalStock) {
        this.totalStock = totalStock;
    }
}
