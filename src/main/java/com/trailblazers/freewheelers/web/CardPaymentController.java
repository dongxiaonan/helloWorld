package com.trailblazers.freewheelers.web;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.BasicHttpEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/cardPayment")
public class CardPaymentController {
    public static final int[] MONTHS = new int[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12};


    @RequestMapping(value = {"/payment"}, method = {RequestMethod.GET, RequestMethod.POST})
    public String payment(Model model, HttpServletRequest request) {
        model.addAttribute("months", MONTHS);
        model.addAttribute("years", getYears());
        model.addAttribute("totalPrice", request.getParameter("price"));
        String address = (String)request.getSession().getAttribute("account_address");
        if (address != null){
            model.addAttribute("address", address.replaceAll("\\n", "<br/>"));
        }
        return "payment";
    }

    @RequestMapping(value = {"/submitPayment"}, method = RequestMethod.POST)
    public String submitPayment(Model model, HttpServletRequest request) throws Exception {
        String cardNumber = request.getParameter("cardNumber");
        String ccv = request.getParameter("CCV");
        String expiryDateMonth = request.getParameter("expiryDateMonth");
        String expiryDateYear = request.getParameter("expiryDateYear");
        String amount = request.getParameter("price");

        Map<String, String> errors = validCardInput(cardNumber, ccv, expiryDateMonth, expiryDateYear);
        if (!errors.isEmpty()) {
            model.addAttribute("errors", errors);
            return payment(model, request);
        }

        String paymentResult = makeCardpayment(cardNumber, ccv, expiryDateMonth, expiryDateYear, amount);
        if (paymentResult.equals("SUCCESS")) {
            return "redirect:/shoppingCart/confirmCheckout";
        }

        model.addAttribute("paymentErrorMessage", paymentResult);
        return payment(model, request);
    }

    private String makeCardpayment(String cardNumber, String ccv, String expiryDateMonth, String expiryDateYear, String amount) {
        HttpPost post = new HttpPost("http://ops.university.thoughtworks.com:5000/authorize");
        post.setHeader("Content-type", "application/xml");

        String content = generateXMLContent(cardNumber, ccv, expiryDateMonth + "-" + expiryDateYear, amount);
        post.setEntity(constructRequestContent(content));
        HttpClient httpClient = new DefaultHttpClient();

        try {
            HttpResponse response = httpClient.execute(post);
            BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
            String responseData = rd.readLine();
            return paymentResult(responseData);
        } catch (IOException e) {
            return "Unknown error has occured";
        }
    }

    private String paymentResult(String responseData) {
        if (responseData.contains("SUCCESS")){
            return "SUCCESS";
        } else if (responseData.contains("UNAUTH")){
            return "The card has been declined";
        } else if(responseData.contains("RVK_CARD")){
            return "The card has been revoked";
        } else{
            return "Unknown error has occured";
        }
    }


    private BasicHttpEntity constructRequestContent(String content) {
        byte[] contentBytes = content.getBytes(StandardCharsets.UTF_8);
        InputStream stream = new ByteArrayInputStream(contentBytes);
        BasicHttpEntity entity = new BasicHttpEntity();
        entity.setContent(stream);
        entity.setContentLength(contentBytes.length);
        return entity;
    }

    private String generateXMLContent(String cardNumber, String ccv, String expirayDate, String amount) {
        return "<authorization-request>\n"
                + "<param key=\"CC_NUM\" value=\"" + cardNumber + "\"/>\n"
                + "<param key=\"CC_EXPIRY\" value=\"" + expirayDate + "\" />\n"
                + "<param key=\"CC_CSC\" value=\"" + ccv + "\" />\n"
                + "<param key=\"AMT\" value=\"" + amount + "\" />\n"
                + "</authorization-request>";
    }

    private Map<String, String> validCardInput(String cardNumber, String CCV, String month, String year) {
        Map<String, String> errors = new HashMap<String, String>();

        if (cardNumber.length()!=16) {
            errors.put("cardNumber", "The length of card number must be 16");
        }

        if (!cardNumber.matches("^[0-9]+$")) {
            errors.put("cardNumber", "Only numbers are allowed");
        }

        if (!(CCV.length()==3||CCV.length()==4)) {
            errors.put("CCV", "The length of CCV must be 3 or 4");
        }

        if (!CCV.matches("^[0-9]+$")) {
            errors.put("CCV", "Only numbers are allowed");
        }

        if (month.isEmpty() || year.isEmpty()) {
            errors.put("expirationDate", "Month and year must be selected");
        }

        SimpleDateFormat formatter = new SimpleDateFormat("MM-yyyy");
        try {
            Date currentNow = formatter.parse(formatter.format(new Date()));
            Date inputDate = formatter.parse(month + "-" + year);
            if (inputDate.compareTo(currentNow)<0) {
                errors.put("expirationDate", "Month and year must be selected");
            }
        } catch (ParseException e) {
            errors.put("expirationDate", "You need to select month and year again");
        }

        return errors;
    }

    private int[] getYears() {
        Calendar now = Calendar.getInstance();   // Gets the current date and time
        int year = now.get(Calendar.YEAR);
        int[] years = new int[12];
        for (int i = 0; i < years.length; i++) {
            years[i] = year;
            year++;
        }
        return years;
    }
}
