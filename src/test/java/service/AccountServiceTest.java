package service;

import entity.Bill;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.math.BigDecimal;

import static org.junit.Assert.*;
import static org.mockito.Mockito.spy;

@RunWith(MockitoJUnitRunner.class)
public class AccountServiceTest {

    @Test
    public void cashInValidAmountBalanceUpdated() {
        AccountService accountService = new AccountService();
        BigDecimal initialBalance = accountService.getBalance();

        BigDecimal amountToAdd = new BigDecimal("1000");
        accountService.cashIn(amountToAdd);

        assertEquals(initialBalance.add(amountToAdd), accountService.getBalance());
    }

    @Test
    public void cashInNegativeAmountErrorReturned() {
        AccountService accountService = new AccountService();

        BigDecimal amountToAdd = new BigDecimal("-500");
        accountService.cashIn(amountToAdd);

        assertEquals(BigDecimal.ZERO, accountService.getBalance());
    }

    @Test
    public void findBillByIdExisted() {
        AccountService accountService = spy(AccountService.class);
        Bill bill = new Bill();
        bill.setBillId(1);
        Mockito.when(accountService.findBillById(1)).thenReturn(bill);

        assertEquals(1, accountService.findBillById(1).getBillId());
    }

    @Test
    public void findBillByIdNotFound() {
        AccountService accountService = spy(AccountService.class);
        Mockito.when(accountService.findBillById(1)).thenReturn(null);

        assertNull(accountService.findBillById(1));
    }
}
