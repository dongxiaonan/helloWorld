package com.trailblazers.freewheelers.web;

import com.trailblazers.freewheelers.model.Account;
import com.trailblazers.freewheelers.model.Country;
import com.trailblazers.freewheelers.service.AccountService;
import com.trailblazers.freewheelers.service.CountryService;
import com.trailblazers.freewheelers.service.ServiceResult;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.springframework.ui.ExtendedModelMap;
import org.springframework.ui.ModelMap;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
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

    private Account getEmptyUserAccount() {
        return new Account()
                .setEmail_address("")
                .setPassword("")
                .setAccount_name("")
                .setCountry(null)
                .setPhoneNumber("")
                .setEnabled(false);
    }

    @Test
    public void shouldShowTheCreateAccountForm() throws Exception {
        ExtendedModelMap model = new ExtendedModelMap();
        ExtendedModelMap expectedModelMap =  new ExtendedModelMap();
        expectedModelMap.addAttribute("validationMessage",new ExtendedModelMap());
        expectedModelMap.addAttribute("countries",countryService.getAllCountries());
        expectedModelMap.addAttribute("account",getEmptyUserAccount());
        expectedModelMap.addAttribute("confirmedPassword","");
        ModelAndView accountForm = accountController.createAccountForm(model);

        assertThat(accountForm.getViewName(),is("account/create"));
        assertThat(accountForm.getModel(),is(expectedModelMap.asMap()));
    }

    @Test
    public void successfulAccountCreationShouldShowSuccess() throws Exception {
        ServiceResult<Account> success = new ServiceResult<Account>(new HashMap<String, String>(), getEmptyUserAccount().setAccount_name("john smith"));
        when(accountService.createAccount(any(Account.class))).thenReturn(success);
        HttpServletRequest mockRequest = mock(HttpServletRequest.class);

        when(mockRequest.getParameter("password")).thenReturn("somePassword");
        when(mockRequest.getParameter("confirmedPassword")).thenReturn("somePassword");
        ModelAndView createView = accountController.processCreate(mockRequest);

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
        when(request.getParameter("confirmedPassword")).thenReturn("password");
        when(request.getParameter("name")).thenReturn("john smith");
        when(request.getParameter("country")).thenReturn("United Kingdom");
        when(request.getParameter("phoneNumber")).thenReturn("123456789");
        when(countryService.getCountryByName("United Kingdom")).thenReturn(new Country(1, "United Kingdom"));

        accountController.processCreate(request);

        ArgumentCaptor<Account> captor = ArgumentCaptor.forClass(Account.class);
        verify(accountService).createAccount(captor.capture());

        Account account = captor.getValue();
        assertThat(account.getEmail_address(), is("email@fake.com"));
        assertThat(account.getPassword(), is("password"));
        assertThat(account.getAccount_name(), is("john smith"));
        assertThat(account.getCountry(),is(new Country(1,"United Kingdom")));
        assertThat(account.getPhoneNumber(), is("123456789"));
        assertThat(account.isEnabled(), is(true));
        assertThat(accountController.getConfirmedPassword(), is("password"));
    }

    @Test
    public void shouldReturnErrorWhenPasswordNotMatch() throws Exception {
        HashMap<String, String> errors = new HashMap<String, String>();
        errors.put("confirmedPassword", "Must have matching password!");

        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getParameter("email")).thenReturn("email@fake.com");
        when(request.getParameter("password")).thenReturn("password");
        when(request.getParameter("confirmedPassword")).thenReturn("confirmedPassword");
        when(request.getParameter("name")).thenReturn("john smith");
        when(request.getParameter("country")).thenReturn("UK");
        when(request.getParameter("phoneNumber")).thenReturn("123456789");

        ModelAndView createView = accountController.processCreate(request);

        assertTrue(createView.getModel().toString().contains(errors.toString()));

    }

    @Test
    public void accountCreationFailureShouldShowError() throws Exception {
        HashMap<String, String> errors = new HashMap<String, String>();
        errors.put("some key", "some error message");
        ServiceResult<Account> failure = new ServiceResult<Account>(errors, new Account());
        when(accountService.createAccount(any(Account.class))).thenReturn(failure);
        HttpServletRequest mockRequest = mock(HttpServletRequest.class);
        when(mockRequest.getParameter("confirmedPassword")).thenReturn("somePassword");
        when(mockRequest.getParameter("password")).thenReturn("somePassword");

        ModelAndView createView = accountController.processCreate(mockRequest);

        ModelMap model = new ModelMap();
        model.put("errors", errors);
        ExtendedModelMap expectedModelMap = new ExtendedModelMap();
        expectedModelMap.addAttribute("validationMessage", model);

        expectedModelMap.addAttribute("account", new Account().setPassword("somePassword").setEnabled(true));
        expectedModelMap.addAttribute("countries", countryService.getAllCountries());

        assertThat(createView.getViewName(), is("account/create"));
        assertThat(createView.getModel(), is(expectedModelMap.asMap()));
   }

    @Test
    public void accountCreationExceptionShouldShowError() throws Exception {
        when(accountService.createAccount(any(Account.class))).thenThrow(new RuntimeException("validation errors"));

        HttpServletRequest mockRequest = mock(HttpServletRequest.class);
        when(mockRequest.getParameter("confirmedPassword")).thenReturn("somePassword");
        when(mockRequest.getParameter("password")).thenReturn("somePassword");

        ModelAndView createView = accountController.processCreate(mockRequest);

        assertThat(createView.getViewName(), is("account/createFailure"));
    }

    @Test
    public void shouldRetainTheCorrectDataWhenThereIsSomeError() throws Exception {
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getParameter("email")).thenReturn("");
        when(request.getParameter("password")).thenReturn("password");
        when(request.getParameter("confirmedPassword")).thenReturn("password");
        when(request.getParameter("name")).thenReturn("john smith");
        when(request.getParameter("country")).thenReturn("United Kingdom");
        when(request.getParameter("phoneNumber")).thenReturn("123456789");
        when(countryService.getCountryByName("United Kingdom")).thenReturn(new Country(1, "United Kingdom"));

        accountController.processCreate(request);

        ArgumentCaptor<Account> captor = ArgumentCaptor.forClass(Account.class);
        verify(accountService).createAccount(captor.capture());

        Account account = captor.getValue();
        assertThat(account.getEmail_address(), is(""));
        assertThat(account.getPassword(), is("password"));
        assertThat(account.getAccount_name(), is("john smith"));
        assertThat(account.getCountry(),is(new Country(1,"United Kingdom")));
        assertThat(account.getPhoneNumber(), is("123456789"));
        assertThat(account.isEnabled(), is(true));

    }


}
