package com.trailblazers.freewheelers.web;

import com.trailblazers.freewheelers.model.Account;
import com.trailblazers.freewheelers.model.AccountRole;
import com.trailblazers.freewheelers.model.Item;
import com.trailblazers.freewheelers.service.AccountService;
import com.trailblazers.freewheelers.service.ReserveOrderService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.ui.Model;
import sun.security.acl.PrincipalImpl;

import java.security.Principal;
import java.util.ArrayList;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class UserProfileControllerTest {
    @InjectMocks
    private UserProfileController userProfileController;
    private Model model;
    @Mock
    private Account userAccount;
    @Mock
    private AccountService accountService;
    @Mock
    private ReserveOrderService reserveOrderService;

    @Before
    public void setUp() throws Exception {
        model = mock(Model.class);
        userAccount = new Account().setAccount_name("user");
    }

    @Test
    public void shouldReturnUserProfileViewWhenUserAccessesHisProfile(){
        when(accountService.getAccountByName("user")).thenReturn(userAccount);
        Principal principal = new PrincipalImpl("user");

        String returnedView = userProfileController.get("user", model, principal);

        verify(model).addAttribute("items", new ArrayList<Item>(0));
        verify(model).addAttribute("userDetail", userAccount);
        assertThat(returnedView, is("userProfile"));
    }

    @Test
    public void shouldReturnUserProfileViewWhenUserNameParamIsNotGiven(){
        when(accountService.getAccountByName("user")).thenReturn(userAccount);
        Principal principal = new PrincipalImpl("user");

        String returnedView = userProfileController.get(null, model, principal);

        verify(model).addAttribute("items", new ArrayList<Item>(0));
        verify(model).addAttribute("userDetail", userAccount);
        assertThat(returnedView, is("userProfile"));
    }


    @Test
    public void shouldReturnAccessDeniedViewWhenAUserAccessesOtherUserProfile(){
        Principal principal = new PrincipalImpl("user");
        AccountRole userRole = new AccountRole().setRole("ROLE_USER");
        when(accountService.getAccountRoleByName("user")).thenReturn(userRole);

        String returnedView = userProfileController.get("otherUser", model, principal);

        assertThat(returnedView, is("accessDenied"));
    }

    @Test
    public void shouldReturnUserProfileWhenAdminAccessesUserAccount(){
        AccountRole adminRole = new AccountRole().setRole("ROLE_ADMIN");
        when(accountService.getAccountByName("user")).thenReturn(userAccount);
        when(accountService.getAccountRoleByName("admin")).thenReturn(adminRole);
        Principal principal = new PrincipalImpl("admin");

        String returnedView = userProfileController.get("user", model, principal);

        verify(model).addAttribute("items", new ArrayList<Item>(0));
        verify(model).addAttribute("userDetail", userAccount);
        assertThat(returnedView, is("userProfile"));
    }

}