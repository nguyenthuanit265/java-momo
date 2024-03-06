package entity;


import enums.BillState;
import enums.BillType;

import java.time.LocalDate;

public class Bill {
    private int billId;
    private BillType billType;
    private double amount;
    private LocalDate dueDate;
    private BillState billState;
    private String provider;
    private boolean isPaid;

    public Bill() {
    }

    public int getBillId() {
        return billId;
    }

    public void setBillId(int billId) {
        this.billId = billId;
    }

    public BillType getBillType() {
        return billType;
    }

    public void setBillType(BillType billType) {
        this.billType = billType;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public LocalDate getDueDate() {
        return dueDate;
    }

    public void setDueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
    }

    public BillState getBillState() {
        return billState;
    }

    public void setBillState(BillState billState) {
        this.billState = billState;
    }

    public boolean isPaid() {
        return isPaid;
    }

    public void setPaid(boolean paid) {
        isPaid = paid;
    }

    public String getProvider() {
        return provider;
    }

    public void setProvider(String provider) {
        this.provider = provider;
    }
}
