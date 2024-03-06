package service;

import entity.Bill;
import entity.Payment;
import entity.User;
import enums.BillState;
import enums.PaymentState;
import utils.DateUtils;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class AccountService {
    private String accountId;
    private User user;
    private BigDecimal balance;
    private List<Bill> bills;
    private List<Payment> payments;

    public AccountService() {
        this.balance = BigDecimal.ZERO;
        this.bills = new ArrayList<>();
        this.payments = new ArrayList<>();
    }

    public AccountService(User user) {
        this.user = user;
        this.balance = BigDecimal.ZERO;
        this.bills = new ArrayList<>();
        this.payments = new ArrayList<>();
    }

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public List<Bill> getBills() {
        return bills;
    }

    public void setBills(List<Bill> bills) {
        this.bills = bills;
    }

    public List<Payment> getPayments() {
        return payments;
    }

    public void setPayments(List<Payment> payments) {
        this.payments = payments;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void cashIn(BigDecimal amount) {
        if (amount.compareTo(BigDecimal.ZERO) < 0) {
            System.out.println("Amount is negative number!!!");
            return;
        }

        this.balance = this.balance.add(amount);
        System.out.println("Your available balance: " + this.balance);

    }

    public void listBills() {
        System.out.println("Bill No. Type Amount Due Date State PROVIDER");
        this.bills.forEach(bill -> {
            System.out.println(
                    bill.getBillId() + ". " +
                            bill.getBillType() + " " +
                            bill.getAmount() + " " +
                            bill.getDueDate() + " " +
                            bill.getBillState().toString().toUpperCase() + " " +
                            bill.getProvider()
            );
        });

    }

    public void payBill(List<Integer> billIds) {
        // Validate bill
        BigDecimal totalBillAmount = BigDecimal.ZERO;
        Map<Integer, Integer> mapBillDup = new ConcurrentHashMap<>();
        for (int billId : billIds) {
            if (mapBillDup.containsKey(billId) && mapBillDup.get(billId) >= 1) {
                System.out.println(String.format("Sorry! Bill %s is duplicated", billId));
                return;
            } else {
                mapBillDup.put(billId, 1);
            }

            Bill bill = findBillById(billId);
            if (bill == null) {
                System.out.println("Sorry! Not found a bill with such id: " + billId);
                return;
            }

            if (bill.getBillState().equals(BillState.PAID)) {
                System.out.printf("Sorry! Existed bill %s is paid%n", bill.getBillId());
                return;
            }

            if (bill.getBillState().equals(BillState.NOT_PAID)) {
                totalBillAmount = totalBillAmount.add(BigDecimal.valueOf(bill.getAmount()));
            }
        }

        // Check fund
        if (balance.compareTo(totalBillAmount) < 0) {
            System.out.println("Sorry! Not enough fund to proceed with payment");
            return;
        }

        for (int billId : billIds) {
            Bill bill = findBillById(billId);
            if (balance.compareTo(BigDecimal.valueOf(bill.getAmount())) > 0) {
                Payment payment = new Payment();
                payment.setBill(bill);
                payment.setPaymentDate(LocalDate.now());
                payment.setAmount(bill.getAmount());
                payment.setPaymentState(PaymentState.PROCESSED);
                payments.add(payment);

                this.balance = this.balance.subtract(BigDecimal.valueOf(bill.getAmount()));
                bill.setBillState(BillState.PAID);
                System.out.println("Payment has been completed for Bill with id " + billId);
                System.out.println("Your current balance is: " + balance);
            }
        }
    }

    public void listDueBills() {
        System.out.println("Bill No. Type Amount Due Date State PROVIDER");
        // Sort
        List<Bill> billsNotPaid = getBillNotPaid();
        Collections.sort(billsNotPaid, Comparator.comparing(Bill::getDueDate));

        for (Bill bill : billsNotPaid) {
            if (bill.getBillState().equals(BillState.NOT_PAID)) {
                System.out.println(
                        bill.getBillId() + ". " +
                                bill.getBillType() + " " +
                                bill.getAmount() + " " +
                                bill.getDueDate() + " " +
                                bill.getBillState() + " " +
                                bill.getProvider()
                );
            }
        }
    }

    public void schedulePayment(int billId, String scheduledDate) {
        Bill bill = findBillById(billId);
        if (bill != null && bill.getBillState().equals(BillState.NOT_PAID) && balance.compareTo(BigDecimal.valueOf(bill.getAmount())) > 0) {
            Payment payment = new Payment();
            payment.setBill(bill);
            payment.setPaymentDate(bill.getDueDate());
            payment.setScheduledDate(DateUtils.convertStringToDate(scheduledDate));
            payment.setAmount(bill.getAmount());
            payment.setPaymentState(PaymentState.PENDING);
            payments.add(payment);

            System.out.println("Payment for bill id " + billId + " is scheduled on " + scheduledDate);
        } else {
            System.out.println("Sorry! Unable to schedule payment for Bill with id " + billId);
        }
    }

    public void listPayments() {
        System.out.println("No. Amount Payment Date State Bill Id");
        for (int i = 0; i < payments.size(); i++) {
            Payment payment = payments.get(i);
            String billId = payment.getBill() != null ? String.valueOf(payment.getBill().getBillId()) : "";
            System.out.println(
                    (i + 1) + ". " +
                            payment.getAmount() + " " +
                            payment.getPaymentDate() + " " +
                            payment.getPaymentState() + " " +
                            billId
            );
        }
    }

    public void searchBillsByProvider(String provider) {
        System.out.println("Bill No. Type Amount Due Date State PROVIDER");
        for (Bill bill : bills) {
            if (bill.getProvider().equals(provider)) {
                System.out.println(
                        bill.getBillId() + ". " +
                                bill.getBillType() + " " +
                                bill.getAmount() + " " +
                                bill.getDueDate() + " " +
                                bill.getBillState() + " " +
                                bill.getProvider()
                );
            }
        }
    }

    public Bill findBillById(int billId) {
        if (billId < 0) {
            return null;
        }

        for (Bill bill : bills) {
            if (bill.getBillId() == billId) {
                return bill;
            }
        }

        return null;
    }

    public List<Bill> getBillNotPaid() {
        List<Bill> billsNotPaid = new ArrayList<>();
        for (Bill bill : bills) {
            if (bill.getBillState().equals(BillState.NOT_PAID)) {
                billsNotPaid.add(bill);
            }
        }

        return billsNotPaid;
    }


}
