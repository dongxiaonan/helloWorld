package com.trailblazers.freewheelers.web;

import com.trailblazers.freewheelers.model.Account;
import com.trailblazers.freewheelers.service.AccountService;
import com.trailblazers.freewheelers.service.CountryService;
import com.trailblazers.freewheelers.service.ServiceResult;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.springframework.ui.ExtendedModelMap;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.initMocks;

public class AccountControllerTest {

    private AccountController accountController;

    @Mock
    private AccountService accountService;

    @Mock
    private CountryService countryService;

    @Before
    public void setUp() throws Exception {
        initMocks(this);

        accountController = new AccountController();
        accountController.accountService = accountService;
        accountController.countryService = countryService;
    }

    @Test
    public void shouldShowTheCreateAccountForm() throws Exception {
        ExtendedModelMap model = new ExtendedModelMap();
        ExtendedModelMap expectedModelMap =  new ExtendedModelMap();
        expectedModelMap.addAttribute("validationMessage",new ExtendedModelMap());
        expectedModelMap.addAttribute("countries",countryService.getCountries());
        ModelAndView accountForm = accountController.createAccountForm(model);

        assertThat(accountForm.getViewName(),is("account/create"));
        assertThat(accountForm.getModel(),is(expectedModelMap.asMap()));
    }

    @Test
    public void successfulAccountCreationShouldShowSuccess() throws Exception {
        Account account = new Account();
        account.setAccount_name("john smith");
        ServiceResult<Account> success = new ServiceResult<Account>(new HashMap<String, String>(), account);
        when(accountService.createAccount(any(Account.class))).thenReturn(success);

        ModelAndView createView = accountController.processCreate(mock(HttpServletRequest.class));

        ModelMap model = new ModelMap();
        model.put("name", "john smith");
        ExtendedModelMap expectedModel = new ExtendedModelMap();
        expectedModel.put("postedValues", model);
        assertThat(createView.getViewName(), is("account/createSuccess"));
        assertThat(createView.getModel(), is(expectedModel.asMap()));
    }

    @Test
    public void shouldCreateAnAccountFromTheHttpRequest() throws Exception {
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getParameter("email")).thenReturn("email@fake.com");
        when(request.getParameter("password")).thenReturn("password");
        when(request.getParameter("name")).thenReturn("john smith");
        when(request.getParameter("country")).thenReturn("UK");
        when(request.getParameter("phoneNumber")).thenReturn("123456789");

        accountController.processCreate(request);

        ArgumentCaptor<Account> captor = ArgumentCaptor.forClass(Account.class);
        verify(accountService).createAccount(captor.capture());

        Account account = captor.getValue();
        assertThat(account.getEmail_address(), is("email@fake.com"));
        assertThat(account.getPassword(), is("password"));
        assertThat(account.getAccount_name(), is("john smith"));
        assertThat(account.getCountry(),is("UK"));
        assertThat(account.getPhoneNumber(), is("123456789"));
        assertThat(account.isEnabled(), is(true));
    }

    @Test
    public void accountCreationFailureShouldShowError() throws Exception {
        HashMap<String, String> errors = new HashMap<String, String>();
        errors.put("some key", "some error message");
        ServiceResult<Account> failure = new ServiceResult<Account>(errors, new Account());
        when(accountService.createAccount(any(Account.class))).thenReturn(failure);

        ModelAndView createView = accountController.processCreate(mock(HttpServletRequest.class));

        ModelMap model = new ModelMap();
        model.put("errors", errors);
        ExtendedModelMap expectedModel = new ExtendedModelMap();
        expectedModel.put("validationMessage", model);
        expectedModel.put("countries",countryService.getCountries());
        assertThat(createView.getViewName(), is("account/create"));
        assertThat(createView.getModel(), is(expectedModel.asMap()));
    }

    @Test
    public void accountCreationExceptionShouldShowError() throws Exception {
        when(accountService.createAccount(any(Account.class))).thenThrow(new RuntimeException("validation errors"));

        ModelAndView createView = accountController.processCreate(mock(HttpServletRequest.class));

        assertThat(createView.getViewName(), is("account/createFailure"));
    }
}
