package com.trailblazers.freewheelers.web;

import org.junit.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.ui.ExtendedModelMap;
import org.springframework.ui.Model;

import java.util.Map;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.*;

public class CardPaymentControllerTest {

    private CardPaymentController cardPaymentController = new CardPaymentController();

    @Test
    public void shouldReturnModelWithYearsFromCurrentYear(){
        Model model = new ExtendedModelMap();

        cardPaymentController.payment(model, new MockHttpServletRequest());

        assertThat((int[])model.asMap().get("years"), is(new int [] {2014, 2015, 2016, 2017, 2018, 2019, 2020, 2021, 2022, 2023, 2024, 2025}));
    }


    @Test
    public void shouldReturnModelWithErrorKeyWhenCardNumberContainsNoneNumbersChars() throws Exception{
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setParameter("cardNumber", "190282ejglwef");
        request.setParameter("expiryDateMonth", "12");
        request.setParameter("expiryDateYear", "2014");
        request.setParameter("CCV", "1224");
        ExtendedModelMap modelMap = new ExtendedModelMap();

        cardPaymentController.submitPayment(modelMap, request);

        Map<String, String> errors = (Map<String, String>) modelMap.asMap().get("errors");

        assertTrue(errors.size() == 1);
        assertTrue(errors.containsKey("cardNumber"));
    }


    @Test
    public void shouldReturnModelWithErrorKeyWhenCCVContainsNoneNumbersChars() throws Exception{
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setParameter("cardNumber", "4111111111111111");
        request.setParameter("expiryDateMonth", "11");
        request.setParameter("expiryDateYear", "2020");
        request.setParameter("CCV", "13rj1");
        ExtendedModelMap modelMap = new ExtendedModelMap();

        cardPaymentController.submitPayment(modelMap, request);

        Map<String, String> errors = (Map<String, String>) modelMap.asMap().get("errors");

        assertTrue(errors.size() == 1);
        assertTrue(errors.containsKey("CCV"));
    }

    @Test
    public void shouldReturnModelWithErrorKeyWhenMonthIsNotSelected() throws Exception{
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setParameter("cardNumber", "4111111111111111");
        request.setParameter("expiryDateMonth", "");
        request.setParameter("expiryDateYear", "2020");
        request.setParameter("CCV", "534");
        ExtendedModelMap modelMap = new ExtendedModelMap();

        cardPaymentController.submitPayment(modelMap, request);

        Map<String, String> errors = (Map<String, String>) modelMap.asMap().get("errors");

        assertTrue(errors.size() == 1);
        assertTrue(errors.containsKey("expirationDate"));
    }


    @Test
    public void shouldReturnModelWithErrorKeyWhenYearIsNotSelected() throws Exception{
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setParameter("cardNumber", "4111111111111111");
        request.setParameter("expiryDateMonth", "11");
        request.setParameter("expiryDateYear", "");
        request.setParameter("CCV", "534");
        ExtendedModelMap modelMap = new ExtendedModelMap();

        cardPaymentController.submitPayment(modelMap, request);

        Map<String, String> errors = (Map<String, String>) modelMap.asMap().get("errors");

        assertTrue(errors.size() == 1);
        assertTrue(errors.containsKey("expirationDate"));
    }

    @Test
    public void shouldReturnModelWithErrorKeyWhenCardNumberLengthIsNot16() throws Exception{
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setParameter("cardNumber", "37828224631000511");
        request.setParameter("expiryDateMonth", "09");
        request.setParameter("expiryDateYear", "2025");
        request.setParameter("CCV", "7997");
        ExtendedModelMap modelMap = new ExtendedModelMap();

        cardPaymentController.submitPayment(modelMap, request);

        Map<String, String> errors = (Map<String, String>) modelMap.asMap().get("errors");

        assertTrue(errors.size() == 1);
        assertTrue(errors.containsKey("cardNumber"));
    }

    @Test
    public void shouldReturnModelWithErrorKeyWhenCVVLengthIsNot3NOR4() throws Exception{
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setParameter("cardNumber", "4111111111111111");
        request.setParameter("expiryDateMonth", "11");
        request.setParameter("expiryDateYear", "2020");
        request.setParameter("CCV", "53");
        ExtendedModelMap modelMap = new ExtendedModelMap();

        cardPaymentController.submitPayment(modelMap, request);

        Map<String, String> errors = (Map<String, String>) modelMap.asMap().get("errors");

        assertTrue(errors.size() == 1);
        assertTrue(errors.containsKey("CCV"));
    }

    @Test
    public void shouldReturnModelWithErrorKeyWhenDateIsNotFuture() throws Exception{
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setParameter("cardNumber", "4111111111111111");
        request.setParameter("expiryDateMonth", "11");
        request.setParameter("expiryDateYear", "2000");
        request.setParameter("CCV", "534");
        ExtendedModelMap modelMap = new ExtendedModelMap();

        cardPaymentController.submitPayment(modelMap, request);

        Map<String, String> errors = (Map<String, String>) modelMap.asMap().get("errors");

        assertTrue(errors.size() == 1);
        assertTrue(errors.containsKey("expirationDate"));
    }

    @Test
    public void shouldReturnModelWithErrorKeyWhenDateHasAnInvalidFormat() throws Exception{
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setParameter("cardNumber", "4111111111111111");
        request.setParameter("expiryDateMonth", "ABC");
        request.setParameter("expiryDateYear", "DEF");
        request.setParameter("CCV", "534");
        ExtendedModelMap modelMap = new ExtendedModelMap();

        cardPaymentController.submitPayment(modelMap, request);

        Map<String, String> errors = (Map<String, String>) modelMap.asMap().get("errors");

        assertTrue(errors.size() == 1);
        assertTrue(errors.containsKey("expirationDate"));
    }

    @Test
    public void shouldNotReturnModelWithErrorKeyWhenDateIsFuture() throws Exception{
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setParameter("cardNumber", "4111111111111111");
        request.setParameter("expiryDateMonth", "11");
        request.setParameter("expiryDateYear", "2100");
        request.setParameter("CCV", "534");
        ExtendedModelMap modelMap = new ExtendedModelMap();

        cardPaymentController.submitPayment(modelMap, request);

        assertFalse(modelMap.containsKey("error"));
    }

    @Test
    public void shouldNotContainErrorKeyInTheModelWhenCardDetailsInputIsCorrect() throws Exception{
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setParameter("cardNumber", "4111111111111111");
        request.setParameter("CCV", "534");
        request.setParameter("expiryDateMonth", "11");
        request.setParameter("expiryDateYear", "2020");
        request.setParameter("price", "200");
        ExtendedModelMap modelMap = new ExtendedModelMap();

        cardPaymentController.submitPayment(modelMap, request);

        assertFalse(modelMap.asMap().containsKey("errors"));
    }

    @Test
    public void shouldRedirectToConfirmCheckoutWhenUsingValidCard() throws Exception {
        ExtendedModelMap model = new ExtendedModelMap();
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setParameter("cardNumber", "4111111111111111");
        request.setParameter("CCV", "534");
        request.setParameter("expiryDateMonth", "11");
        request.setParameter("expiryDateYear", "2020");
        request.setParameter("price", "200");


        String confirmationPage = cardPaymentController.submitPayment(model, request);

        assertThat(confirmationPage, is("redirect:/shoppingCart/confirmCheckout"));
    }

    @Test
    public void shouldReturnModelWithErrorMessageForInvalidCardPayment() throws Exception {
        ExtendedModelMap model = new ExtendedModelMap();
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setParameter("cardNumber", "4111111111111111");
        request.setParameter("CCV", "534");
        request.setParameter("expiryDateMonth", "11");
        request.setParameter("expiryDateYear", "2039");
        request.setParameter("price", "200");

        String paymentPage = cardPaymentController.submitPayment(model, request);

        assertThat(paymentPage, is("payment"));
        assertThat((String)model.asMap().get("paymentErrorMessage"), is("The card has been declined"));
    }

    @Test
    public void shouldReturnModelWithErrorMessageForRevokedCard() throws Exception {
        ExtendedModelMap model = new ExtendedModelMap();
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setParameter("cardNumber", "4111111111111116");
        request.setParameter("CCV", "534");
        request.setParameter("expiryDateMonth", "11");
        request.setParameter("expiryDateYear", "2020");
        request.setParameter("price", "200");

        String paymentPage = cardPaymentController.submitPayment(model, request);

        assertThat(paymentPage, is("payment"));
        assertThat((String)model.asMap().get("paymentErrorMessage"), is("The card has been revoked"));
    }
}