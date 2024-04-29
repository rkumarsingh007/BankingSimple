package dev.codescreen.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.codescreen.Exception.CurrencyMismatch;
import dev.codescreen.Exception.UserNotFoundException;
import dev.codescreen.Repository.AuthorizationRequestRepository;
import dev.codescreen.Repository.UserBalanceRepository;
import dev.codescreen.Service.TransactionServiceImpl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


import dev.codescreen.Entity.*;
import org.springframework.test.web.servlet.ResultActions;

import static org.mockito.Mockito.*;


@WebMvcTest(controllers = TransactionController.class)
@AutoConfigureMockMvc(addFilters = false)
class TransactionControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    UserBalanceRepository userBalanceRepository;
    @MockBean
    AuthorizationRequestRepository authorizationRequestRepository;
    @MockBean
    TransactionServiceImpl transactionService;
    @Autowired
    TransactionController transactionController = new TransactionController(transactionService);

    @Test
    void testLoadFunds_Success() throws Exception {

        LoadRequest loadRequest = new LoadRequest("test-messageId", "test-user",
                new TransactionAmount("100.0", "USD", DebitCredit.CREDIT));
        LoadResponse loadResponse = new LoadResponse(new TransactionAmount("200.0", "USD", DebitCredit.CREDIT),
                "test-user", "test-messageId");

        // Mocking the behavior of the transactionService  - considering our service to be working fine!
        when(transactionService.loadFunds(any())).thenReturn(loadResponse);


        // Performing the HTTP PUT request
        ResultActions actions = mockMvc.perform(put("/load/test-messageId")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loadRequest)));
        actions.andExpect(status().isCreated())
                .andExpect(jsonPath("$.balance.amount").value("200.0"))
                .andExpect(jsonPath("$.balance.currency").value("USD"))
                .andExpect(jsonPath("$.balance.debitOrCredit").value("CREDIT"))
                .andExpect(jsonPath("$.userId").value("test-user"))
                .andExpect(jsonPath("$.messageId").value("test-messageId"));

    }

    @Test
    void testLoadFunds_UserNotFound() throws Exception {
        LoadRequest loadRequest = new LoadRequest("test-messageId", "test-user",
                new TransactionAmount("100.0", "USD", DebitCredit.CREDIT));
        LoadResponse loadResponse = new LoadResponse(new TransactionAmount("100.0", "USD", DebitCredit.CREDIT),
                "test-user", "test-messageId");

        // Mocking the behavior of the transactionService to throw UserNotFoundException
        when(transactionService.loadFunds(any())).thenThrow(new UserNotFoundException(loadResponse));

        // Performing the HTTP PUT request
        ResultActions  ra = mockMvc.perform(put("/load/test-messageId")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loadRequest)));

        ra.andExpect(status().isConflict()) // Expecting HTTP 409
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.messageId").value("test-messageId"))
                .andExpect(jsonPath("$.userId").value("test-user"))
                .andExpect(jsonPath("$.balance.amount").value("100.0"))
                .andExpect(jsonPath("$.balance.currency").value("USD"))
                .andExpect(jsonPath("$.balance.debitOrCredit").value("CREDIT"));
    }

    @Test
    void testAuthorizeTransaction_Success() throws Exception {
        // Create a mock authorization request
        AuthorizationRequest authorizationRequest = new AuthorizationRequest(
                "test-user",
                new TransactionAmount("50.0", "USD", DebitCredit.DEBIT),
                "test-messageId"
        );

        // Create a mock authorization response
        AuthorizationResponse authorizationResponse = new AuthorizationResponse(
                "test-messageId",
                "test-user",
                ResponseCode.APPROVED,
                new TransactionAmount("200.0", "USD", DebitCredit.DEBIT)
        );

        // Mock the behavior of transactionService.authorizeTransaction
        when(transactionService.authorizeTransaction(any())).thenReturn(authorizationResponse);

        // Perform a PUT request to the /authorization/{messageId} endpoint
        mockMvc.perform(put("/authorization/test-messageId")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(authorizationRequest)))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.userId").value("test-user"))
                .andExpect(jsonPath("$.messageId").value("test-messageId"))
                .andExpect(jsonPath("$.responseCode").value("APPROVED"))
                .andExpect(jsonPath("$.balance.amount").value("200.0"))
                .andExpect(jsonPath("$.balance.currency").value("USD"))
                .andExpect(jsonPath("$.balance.debitOrCredit").value("DEBIT"));
    }

    @Test
    void testAuthorizeTransaction_Declined() throws Exception {
        // Create a mock authorization request
        UserBalance userBalance = new UserBalance(100.0,"test-user","test-messageId");
        when(userBalanceRepository.findByUserId("test-user")).thenReturn(userBalance);

        AuthorizationRequest authorizationRequest = new AuthorizationRequest(
                "test-user",
                new TransactionAmount("150.0", "USD", DebitCredit.DEBIT),
                "test-messageId"
        );

        // Create a mock authorization response
        AuthorizationResponse authorizationResponse = new AuthorizationResponse(
                "test-messageId",
                "test-user",
                ResponseCode.DECLINED,
                new TransactionAmount("100.0", "USD", DebitCredit.DEBIT)
        );
        authorizationResponse.setDescription("Insufficient Funds ");

        // Mock the behavior of transactionService.authorizeTransaction
        when(transactionService.authorizeTransaction(any())).thenReturn(authorizationResponse);

        // Perform a PUT request to the /authorization/{messageId} endpoint
        mockMvc.perform(put("/authorization/test-messageId")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(authorizationRequest)))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.userId").value("test-user"))
                .andExpect(jsonPath("$.messageId").value("test-messageId"))
                .andExpect(jsonPath("$.responseCode").value("DECLINED"))
                .andExpect(jsonPath("$.balance.amount").value("100.0"))
                .andExpect(jsonPath("$.balance.currency").value("USD"))
                .andExpect(jsonPath("$.balance.debitOrCredit").value("DEBIT"));
    }


    @Test
    void testAuthorizeTransaction_UserNotFound() throws Exception {
        // Create a mock authorization request
        AuthorizationRequest authorizationRequest = new AuthorizationRequest(
                "test-user", // User that does not exist
                new TransactionAmount("100.0", "USD", DebitCredit.DEBIT),
                "test-messageId"
        );

        authorizationRequest.setDescription("user: test-user does not exists");
        // Mock the behavior of transactionService.authorizeTransaction to throw UserNotFoundException
        when(transactionService.authorizeTransaction(any())).thenThrow(new UserNotFoundException(authorizationRequest));

        // Perform a PUT request to the /authorization/{messageId} endpoint
        mockMvc.perform(put("/authorization/test-messageId")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(authorizationRequest)))
                .andExpect(status().isConflict()) // Expecting HTTP 409
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.description").value("user: test-user does not exists"));

    }


    @Test
    void testAuthorizeTransaction_CurrencyMismatch() throws Exception {
        // Create a mock authorization request
        AuthorizationRequest authorizationRequest = new AuthorizationRequest(
                "test-user",
                new TransactionAmount("100.0", "EUR", DebitCredit.DEBIT), // Incorrect currency
                "test-messageId"
        );
        authorizationRequest.setDescription("Currency Mismatch");

        // Mock the behavior of transactionService.authorizeTransaction to throw CurrencyMismatch
        when(transactionService.authorizeTransaction(any())).thenThrow(new CurrencyMismatch(authorizationRequest));

        // Perform a PUT request to the /authorization/{messageId} endpoint
        mockMvc.perform(put("/authorization/test-messageId")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(authorizationRequest)))
                .andExpect(status().isNotAcceptable()) // Expecting HTTP 406
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.description").value("Currency Mismatch"));

    }

}
