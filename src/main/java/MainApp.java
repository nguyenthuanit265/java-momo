import constants.CommandService;
import entity.Bill;
import entity.User;
import enums.BillState;
import enums.BillType;
import service.AccountService;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;

public class MainApp {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        // Define user
        User user = new User();
        user.setUserId(1);
        user.setUsername("user1");
        user.setEmail("user1@gmail.com");

        // Define account
        AccountService account = new AccountService(user);

        // Define bills
        List<Bill> bills = initBills();
        account.setBills(bills);

        while (true) {
            System.out.print("$ ");
            String input = scanner.nextLine().trim();
            String[] commands = input.split("\\s++");
            String commandService = commands.length > 0 ? commands[0] : null;
            System.out.println(commandService);
            switch (Objects.requireNonNull(commandService)) {
                case CommandService.SERVICE_CASH_IN -> {
                    BigDecimal amount = new BigDecimal(commands[1]);
                    account.cashIn(amount);
                }
                case CommandService.SERVICE_LIST_BILL -> {
                    account.listBills();
                }
                case CommandService.SERVICE_PAY -> {
                    List<Integer> billIds = new ArrayList<>();
                    for (int i = 1; i < commands.length; i++) {
                        billIds.add(Integer.parseInt(commands[i]));
                    }
                    account.payBill(billIds);
                }
                case CommandService.SERVICE_DUE_DATE -> {
                    account.listDueBills();
                }
                case CommandService.SERVICE_SCHEDULE -> {
                    int billId = Integer.parseInt(commands[1]);
                    String scheduledDate = commands[2];
                    account.schedulePayment(billId, scheduledDate);
                }
                case CommandService.SERVICE_LIST_PAYMENT -> {
                    account.listPayments();
                }
                case CommandService.SERVICE_SEARCH_BILL_BY_PROVIDER -> {
                    String provider = commands[1];
                    account.searchBillsByProvider(provider);
                }
                case CommandService.SERVICE_EXIT -> {
                    System.exit(0);
                }
                default -> System.out.println("Service is not supported!!!");
            }
        }
    }

    private static List<Bill> initBills() {
        Bill bill = new Bill();
        bill.setBillId(1);
        bill.setBillType(BillType.ELECTRIC);
        bill.setAmount(200000);
        bill.setDueDate(LocalDate.of(2020, 10, 25));
        bill.setBillState(BillState.NOT_PAID);
        bill.setProvider("EVN HCM");
        bill.setPaid(false);

        Bill bill2 = new Bill();
        bill2.setBillId(2);
        bill2.setBillType(BillType.WATER);
        bill2.setAmount(175000);
        bill2.setDueDate(LocalDate.of(2020, 10, 30));
        bill2.setBillState(BillState.NOT_PAID);
        bill2.setProvider("SAVACO HCM");
        bill2.setPaid(false);

        Bill bill3 = new Bill();
        bill3.setBillId(3);
        bill3.setBillType(BillType.INTERNET);
        bill3.setAmount(800000);
        bill3.setDueDate(LocalDate.of(2020, 11, 30));
        bill3.setBillState(BillState.NOT_PAID);
        bill3.setProvider("VNPT");
        bill3.setPaid(false);

        return new ArrayList<>(Arrays.asList(bill, bill2, bill3));
    }
}
