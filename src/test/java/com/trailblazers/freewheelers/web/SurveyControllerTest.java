package com.trailblazers.freewheelers.web;

import com.trailblazers.freewheelers.model.Account;
import com.trailblazers.freewheelers.model.NpsReport;
import com.trailblazers.freewheelers.model.SurveyComments;
import com.trailblazers.freewheelers.model.SurveyEntry;
import com.trailblazers.freewheelers.service.AccountService;
import com.trailblazers.freewheelers.service.SurveyService;
import com.trailblazers.freewheelers.web.forms.SurveyEntryForm;
import org.apache.http.auth.BasicUserPrincipal;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatcher;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.Cookie;
import java.security.Principal;
import java.util.ArrayList;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.initMocks;

public class SurveyControllerTest {

    @Mock
    SurveyService surveyService;
    @Mock
    AccountService accountService;
    @InjectMocks
    SurveyController surveyController;

    Principal principal = new BasicUserPrincipal("john");
    Account userAccount = new Account();
    String comment = "comment";

    @Before
    public void setUp() {
        initMocks(this);
        userAccount.setAccount_id(12L);
        when(accountService.getAccountByName("john")).thenReturn(userAccount);
    }

    @Test
    public void shouldSetCookie() {
        MockHttpServletResponse mockResponse = new MockHttpServletResponse();
        surveyController.get(mockResponse);

        Cookie surveyCookie = mockResponse.getCookie("SurveyTaken");
        assertEquals(surveyCookie.getValue(), "true");
        assertEquals(surveyCookie.getMaxAge(), 86400);
    }

    @Test
    public void shouldSubmitSurveyForUser() throws Exception {
        BindingResult bindingResult = mock(BindingResult.class);
        when(bindingResult.hasErrors()).thenReturn(false);

        ModelAndView modelAndView = surveyController.post(principal, new SurveyEntryForm(10, comment), bindingResult);

        verify(surveyService).submitSurvey(eq(12L), argThat(new ArgumentMatcher<SurveyEntry>() {
            @Override
            public boolean matches(Object argument) {
                SurveyEntry surveyEntry = (SurveyEntry) argument;
                return "comment".equals(surveyEntry.getComment()) &&
                        10 == surveyEntry.getRating();
            }
        }));

        assertEquals("survey/confirmation", modelAndView.getViewName());
        verify(bindingResult).hasErrors();
    }

    @Test
    public void shouldShowValidationMessageIfSurveyRatingIsNull() throws Exception {
        BindingResult bindingResult = mock(BindingResult.class);
        when(bindingResult.hasErrors()).thenReturn(true);

        ModelAndView modelAndView = surveyController.post(principal, new SurveyEntryForm(null, comment), bindingResult);
        verifyValidationHappens(modelAndView);
        verify(bindingResult).hasErrors();
    }

    @Test
    public void shouldShowReport() {
        NpsReport expectedNpsReport = new NpsReport(1, 2, 3, 6);
        SurveyComments expectedSurveyComments = new SurveyComments(new ArrayList<String>(), new ArrayList<String>(), new ArrayList<String>());
        when(surveyService.generateNpsReport()).thenReturn(expectedNpsReport);
        when(surveyService.getSurveyComments()).thenReturn(expectedSurveyComments);

        ModelAndView modelAndView = surveyController.getReport();

        assertEquals("survey/report", modelAndView.getViewName());
        assertEquals(expectedNpsReport, modelAndView.getModelMap().get("npsReport"));
        assertEquals(expectedSurveyComments, modelAndView.getModelMap().get("surveyComments"));
    }

    private void verifyValidationHappens(ModelAndView modelAndView) throws Exception {
        verify(surveyService, never()).submitSurvey(anyLong(), any(SurveyEntry.class));
        ModelMap modelMap = (ModelMap) modelAndView.getModelMap().get("validation");
        Boolean mandatoryFieldMissing = (Boolean) modelMap.get("mandatoryFieldMissing");
        assertTrue(mandatoryFieldMissing);
        assertEquals("survey/form", modelAndView.getViewName());
    }
}
