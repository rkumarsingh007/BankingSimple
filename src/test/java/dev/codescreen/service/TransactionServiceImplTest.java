package dev.codescreen.service;


import dev.codescreen.Entity.*;
import dev.codescreen.Exception.CurrencyMismatch;
import dev.codescreen.Exception.UserNotFoundException;
import dev.codescreen.Repository.AuthorizationRequestRepository;
import dev.codescreen.Repository.UserBalanceRepository;
import dev.codescreen.Service.TransactionService;
import dev.codescreen.Service.TransactionServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TransactionServiceImplTest {

    @Mock
    private UserBalanceRepository userBalanceRepository;

    @Mock
    private AuthorizationRequestRepository authorizationRequestRepository;

    @Mock
    private TransactionService transactionService;


    @BeforeEach
    public void init() {
        transactionService = new TransactionServiceImpl(userBalanceRepository, authorizationRequestRepository);
    }
    // Load Fund Service
    @Test
    void testLoadFunds_Success() throws UserNotFoundException, CurrencyMismatch {
        // Mocking the user balance
        UserBalance userBalance = new UserBalance(100.0, "test-user", "USD");
        when(userBalanceRepository.findByUserId("test-user")).thenReturn(userBalance);

        // Creating a load request
        LoadRequest loadRequest = new LoadRequest("test-messageId", "test-user",
                new TransactionAmount("50.0", "USD", DebitCredit.CREDIT));

        // Calling the loadFunds method
        LoadResponse loadResponse = transactionService.loadFunds(loadRequest);

        // Verifying the response
        assertNotNull(loadResponse);
        assertEquals("150.0", loadResponse.getBalance().getAmount());
        assertEquals("USD", loadResponse.getBalance().getCurrency());
        assertEquals(DebitCredit.CREDIT, loadResponse.getBalance().getDebitOrCredit());

    }

    @Test
    void testLoadFunds_CurrencyMismatch() {
        // Mocking the user balance with a different currency
        UserBalance userBalance = new UserBalance(100.0,"test-user",  "EUR");
        when(userBalanceRepository.findByUserId("test-user")).thenReturn(userBalance);

        // Creating a load request with USD currency
        LoadRequest loadRequest = new LoadRequest("test-messageId", "test-user",
                new TransactionAmount("50.0", "USD", DebitCredit.CREDIT));

        // Verifying that CurrencyMismatch exception is thrown
        assertThrows(CurrencyMismatch.class, () -> {
            transactionService.loadFunds(loadRequest);
        });
        // No change in balance
        assertEquals(100.00, userBalanceRepository.findByUserId("test-user").getBalance());
    }

    @Test
    void testLoadFunds_UserNotFound() {
        // Mocking the user balance as null (user not found)
        when(userBalanceRepository.findByUserId("test-user")).thenReturn(null);

        // Creating a load request
        LoadRequest loadRequest = new LoadRequest("test-messageId", "test-user",
                new TransactionAmount("50.0", "USD", DebitCredit.CREDIT));

        // Verifying that UserNotFoundException exception is thrown
        assertThrows(UserNotFoundException.class, () -> {
            transactionService.loadFunds(loadRequest);
        });
    }


    // Authorizations Transaction Service

    @Test
    void testAuthorizeTransaction_Declined() throws UserNotFoundException, CurrencyMismatch {
        // Mocking the user balance with a balance lower than the requested transaction amount
        UserBalance userBalance = new UserBalance(50.0, "test-user", "USD");
        when(userBalanceRepository.findByUserId("test-user")).thenReturn(userBalance);

        // Creating an authorization request
        AuthorizationRequest authRequest = new AuthorizationRequest("test-user",
                new TransactionAmount("100.0", "USD", DebitCredit.DEBIT),"test-messageId");

        // Calling the authorizeTransaction method
        AuthorizationResponse authResponse = transactionService.authorizeTransaction(authRequest);

        // Verifying the response
        assertNotNull(authResponse);
        assertEquals(ResponseCode.DECLINED, authResponse.getResponseCode());
        assertEquals("50.0", authResponse.getBalance().getAmount());
        assertTrue(authResponse.getDescription().contains("Insufficient funds"));

        // Check if the transaction was saved of not
        verify(authorizationRequestRepository, times(1)).save(argThat(authResp ->
                authResp.getMessageId().equals("test-messageId") &&
                        authResp.getUserId().equals("test-user") &&
                        authResp.getResponseCode() == ResponseCode.DECLINED &&
                        authResp.getBalance().getAmount().equals("50.0")));

    }

    @Test
    void testAuthorizeTransaction_SuccessfulTransaction() throws UserNotFoundException, CurrencyMismatch {
        // Mocking the user balance
        UserBalance userBalance = new UserBalance(100.0, "test-user", "USD");
        when(userBalanceRepository.findByUserId("test-user")).thenReturn(userBalance);

        // Creating an authorization request
        AuthorizationRequest authRequest = new AuthorizationRequest("test-user",
                new TransactionAmount("50.0", "USD", DebitCredit.CREDIT), "test-messageId");

        // Calling the authorizeTransaction method
        AuthorizationResponse authResponse = transactionService.authorizeTransaction(authRequest);

        // Verifying the response
        assertNotNull(authResponse);
        assertEquals(ResponseCode.APPROVED, authResponse.getResponseCode());
        assertEquals("150.0", authResponse.getBalance().getAmount());
        assertNull(authResponse.getDescription()); // No description for successful transaction

        // Check if the transaction was saved
        verify(authorizationRequestRepository, times(1)).save(argThat(authResp ->
                authResp.getMessageId().equals("test-messageId") &&
                        authResp.getUserId().equals("test-user") &&
                        authResp.getResponseCode() == ResponseCode.APPROVED &&
                        authResp.getBalance().getAmount().equals("150.0")));
    }


    @Test
    void testAuthorizeTransaction_UserNotFound() {
        // Mocking the user balance as null (user not found)
        when(userBalanceRepository.findByUserId("test-user")).thenReturn(null);

        // Creating an authorization request
        AuthorizationRequest authRequest = new AuthorizationRequest("test-user",
                new TransactionAmount("50.0", "USD", DebitCredit.DEBIT),"test-messageId");

        // Verifying that UserNotFoundException exception is thrown
        assertThrows(UserNotFoundException.class, () -> {
             transactionService.authorizeTransaction(authRequest);
        });

    }



}
