package com.trailblazers.freewheelers.web;

import com.trailblazers.freewheelers.model.Account;
import com.trailblazers.freewheelers.model.Country;
import com.trailblazers.freewheelers.service.AccountService;
import com.trailblazers.freewheelers.service.CountryService;
import com.trailblazers.freewheelers.service.EmailSender;
import com.trailblazers.freewheelers.service.ServiceResult;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.ui.ExtendedModelMap;
import org.springframework.ui.ModelMap;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class AccountControllerTest {

    @Mock
    private AccountService accountService;

    @Mock
    private CountryService countryService;

    @Mock
    private EmailSender emailSender;

    @InjectMocks
    private AccountController accountController = new AccountController();

    private Account getEmptyUserAccount() {
        return new Account()
                .setEmail_address("")
                .setPassword("")
                .setAccount_name("")
                .setCountry(null)
                .setStreet1("")
                .setStreet2("")
                .setCity("")
                .setState_Province("")
                .setPostcode("")
                .setPhoneNumber("")
                .setEnabled(false);
    }

    @Test
    public void shouldShowTheCreateAccountForm() throws Exception {
        ExtendedModelMap model = new ExtendedModelMap();
        ExtendedModelMap expectedModelMap =  new ExtendedModelMap();
        expectedModelMap.addAttribute("validationMessage",new ExtendedModelMap());
        expectedModelMap.addAttribute("countries", countryService.getAllCountries());
        expectedModelMap.addAttribute("confirmedPassword", "");

        ModelAndView accountForm = accountController.createAccountForm(model);

        assertThat(accountForm.getViewName(),is("account/create"));
        assertThat(accountForm.getModel(),is(expectedModelMap.asMap()));
    }

    @Test
    public void successfulAccountCreationShouldShowSuccess() throws Exception {
        ServiceResult<Account> success = new ServiceResult<Account>(new HashMap<String, String>(), getEmptyUserAccount().setAccount_name("john smith"));
        when(accountService.createAccount(any(Account.class))).thenReturn(success);
        HttpServletRequest mockRequest = mock(HttpServletRequest.class);

        when(mockRequest.getParameter("password")).thenReturn("V3rySecure!");
        when(mockRequest.getParameter("confirmedPassword")).thenReturn("V3rySecure!");
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
        Account expectedAccount = getSomeAccount();
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getParameter("email")).thenReturn("email@fake.com");
        when(request.getParameter("password")).thenReturn("V3rySecure!");
        when(request.getParameter("confirmedPassword")).thenReturn("V3rySecure!");
        when(request.getParameter("name")).thenReturn("john smith");
        when(request.getParameter("country")).thenReturn("United Kingdom");
        when(request.getParameter("street1")).thenReturn("Greenwood Avenue");
        when(request.getParameter("street2")).thenReturn("Apartment 202");
        when(request.getParameter("city")).thenReturn("London");
        when(request.getParameter("stateProvince")).thenReturn("Somewhere");
        when(request.getParameter("postcode")).thenReturn("12453");
        when(request.getParameter("phoneNumber")).thenReturn("123456789");
        when(countryService.getCountryByName("United Kingdom")).thenReturn(new Country(1, "United Kingdom"));
        when(accountService.createAccount(expectedAccount)).thenReturn(new ServiceResult<Account>(new HashMap<String, String>(),expectedAccount));

        ModelAndView resultModelAndView = accountController.processCreate(request);

        ArgumentCaptor<Account> captor = ArgumentCaptor.forClass(Account.class);
        verify(accountService).createAccount(captor.capture());
        Account account= captor.getValue();
        ModelMap expectedModelMap = new ModelMap();
        expectedModelMap.put("name",expectedAccount.getAccount_name());
        ModelAndView expectedModelAndView  = new ModelAndView("account/createSuccess", "postedValues", expectedModelMap);

        assertThat(account,is(expectedAccount));
        assertThat(resultModelAndView.getModel(), is((Object)expectedModelAndView.getModel()));
    }

    private Account getSomeAccount() {
        return new Account()
                .setEmail_address("email@fake.com")
                .setPassword("V3rySecure!")
                .setAccount_name("john smith")
                .setCity("London")
                .setStreet1("Greenwood Avenue")
                .setStreet2("Apartment 202")
                .setCountry(new Country(1,"United Kingdom"))
                .setState_Province("Somewhere")
                .setPostcode("12453")
                .setPhoneNumber("123456789")
                .setEnabled(false);
    }

    @Test
    public void shouldReturnErrorWhenPasswordNotMatch() throws Exception {
        HashMap<String, String> errors = new HashMap<String, String>();
        errors.put("confirmedPassword", "Must have matching password!");
        ModelMap modelMap = new ModelMap();
        modelMap.put("errors",errors);
        ExtendedModelMap expectedModelMap = new ExtendedModelMap();
        expectedModelMap.addAttribute("validationMessage",modelMap);
        expectedModelMap.addAttribute("countries",countryService.getAllCountries());
        when(countryService.getCountryByName("Canada")).thenReturn(new Country(3,"Canada"));

        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getParameter("email")).thenReturn("email@fake.com");
        when(request.getParameter("password")).thenReturn("Pa$sw0rd");
        when(request.getParameter("confirmedPassword")).thenReturn("confirmedPassword");
        when(request.getParameter("name")).thenReturn("john smith");
        when(request.getParameter("country")).thenReturn("Canada");
        when(request.getParameter("phoneNumber")).thenReturn("123456789");
        when(request.getParameter("street1")).thenReturn("123456789");
        when(request.getParameter("street2")).thenReturn("123456789");
        when(request.getParameter("city")).thenReturn("123456789");
        when(request.getParameter("stateProvince")).thenReturn("123456789");
        when(request.getParameter("postcode")).thenReturn("123456789");


        when(countryService.getCountryByName("Canada")).thenReturn(new Country(3,"Canada"));

        ModelAndView createView = accountController.processCreate(request);

        ModelMap model = new ModelMap();
        model.put("errors", errors);
        expectedModelMap.addAttribute("countries", countryService.getAllCountries());

        assertThat(createView.getModel(), is(expectedModelMap.asMap()));

    }

    @Test
    public void accountCreationFailureShouldShowError() throws Exception {
        HashMap<String, String> errors = new HashMap<String, String>();
        errors.put("some key", "some error message");
        ServiceResult<Account> failure = new ServiceResult<Account>(errors, new Account());
        when(accountService.createAccount(any(Account.class))).thenReturn(failure);
        HttpServletRequest mockRequest = mock(HttpServletRequest.class);
        when(mockRequest.getParameter("confirmedPassword")).thenReturn("V3rySecure!");
        when(mockRequest.getParameter("password")).thenReturn("V3rySecure!");

        ModelAndView createView = accountController.processCreate(mockRequest);

        ModelMap model = new ModelMap();
        model.put("errors", errors);
        ExtendedModelMap expectedModelMap = new ExtendedModelMap();
        expectedModelMap.addAttribute("validationMessage", model);
        expectedModelMap.addAttribute("countries", countryService.getAllCountries());

        assertThat(createView.getViewName(), is("account/create"));
        assertThat(createView.getModel(), is(expectedModelMap.asMap()));
   }

    @Test
    public void accountCreationExceptionShouldShowError() throws Exception {
        when(accountService.createAccount(any(Account.class))).thenThrow(new RuntimeException("validation errors"));

        HttpServletRequest mockRequest = mock(HttpServletRequest.class);
        when(mockRequest.getParameter("confirmedPassword")).thenReturn("V3rySecure!");
        when(mockRequest.getParameter("password")).thenReturn("V3rySecure!");

        ModelAndView createView = accountController.processCreate(mockRequest);

        assertThat(createView.getViewName(), is("account/createFailure"));
    }

    @Test
    public void shouldRetainTheCorrectDataWhenThereIsSomeError() throws Exception {
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getParameter("email")).thenReturn("");
        when(request.getParameter("password")).thenReturn("V3rySecure!");
        when(request.getParameter("confirmedPassword")).thenReturn("V3rySecure!");
        when(request.getParameter("name")).thenReturn("john smith");
        when(request.getParameter("country")).thenReturn("United Kingdom");
        when(request.getParameter("street1")).thenReturn("Greenwood Avenue");
        when(request.getParameter("street2")).thenReturn("Apartment 202");
        when(request.getParameter("city")).thenReturn("London");
        when(request.getParameter("stateProvince")).thenReturn("Somewhere");
        when(request.getParameter("postcode")).thenReturn("12453");
        when(request.getParameter("phoneNumber")).thenReturn("123456789");
        when(countryService.getCountryByName("United Kingdom")).thenReturn(new Country(1, "United Kingdom"));

        accountController.processCreate(request);

        ArgumentCaptor<Account> captor = ArgumentCaptor.forClass(Account.class);
        verify(accountService).createAccount(captor.capture());

        Account account = captor.getValue();
        assertThat(account,is(getSomeAccount().setEmail_address("")));
    }

    @Test
    public void shouldCallEmailSenderWithRightAddressAndAccount() throws Exception {
        ServiceResult<Account> success = new ServiceResult<Account>(new HashMap<String, String>(), getEmptyUserAccount().setAccount_name("john smith"));
        when(accountService.createAccount(any(Account.class))).thenReturn(success);

        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getParameter("email")).thenReturn("ssr.957@gmail.com");
        when(request.getParameter("password")).thenReturn("V3rySecure!");
        when(request.getParameter("name")).thenReturn("Sammy");
        when(request.getParameter("confirmedPassword")).thenReturn("V3rySecure!");

        accountController.processCreate(request);

        verify(emailSender, times(1)).sendVerificationEmail("null:0", new Account().setAccount_name("Sammy").setPassword("V3rySecure!").setEmail_address("ssr.957@gmail.com"));
    }


}
