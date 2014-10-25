package com.trailblazers.freewheelers.web;

import com.trailblazers.freewheelers.model.Account;
import com.trailblazers.freewheelers.model.AccountRole;
import com.trailblazers.freewheelers.model.Item;
import com.trailblazers.freewheelers.service.AccountService;
import com.trailblazers.freewheelers.service.ItemService;
import com.trailblazers.freewheelers.service.ReserveOrderService;
import org.junit.Before;
import org.junit.Test;
import org.springframework.ui.Model;

import java.security.Principal;
import java.util.ArrayList;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.*;

public class UserProfileControllerTest {

    private UserProfileController userProfileController;
    private Principal principal;
    private Model model;
    private Account userAccount;
    private Account adminAccount;

    @Before
    public void setUp() throws Exception {
        userProfileController = new UserProfileController();
        userProfileController.accountService = mock(AccountService.class);
        userProfileController.reserveOrderService = mock(ReserveOrderService.class);
        userProfileController.itemService = mock(ItemService.class);
        principal = mock(Principal.class);
        model = mock(Model.class);
        userAccount = new Account().setAccount_name("user");
        adminAccount = new Account().setAccount_name("admin");

    }

    @Test
    public void shouldReturnUserProfileViewWhenUserAccessesHisProfile(){
        when(userProfileController.accountService.getAccountByName("user")).thenReturn(userAccount);
        when(principal.getName()).thenReturn("user");

        String returnedView = userProfileController.get("user", model, principal);

        verify(model).addAttribute("items", new ArrayList<Item>(0));
        verify(model).addAttribute("userDetail", userAccount);
        assertThat(returnedView, is("userProfile"));
    }

    @Test
    public void shouldReturnUserProfileViewWhenUserNameParamIsNotGiven(){
        when(userProfileController.accountService.getAccountByName("user")).thenReturn(userAccount);
        when(principal.getName()).thenReturn("user");

        String returnedView = userProfileController.get(null, model, principal);

        verify(model).addAttribute("items", new ArrayList<Item>(0));
        verify(model).addAttribute("userDetail", userAccount);
        assertThat(returnedView, is("userProfile"));
    }


    @Test
    public void shouldReturnAccessDeniedViewWhenAUserAccessesOtherUserProfile(){
        when(principal.getName()).thenReturn("user");
        AccountRole userRole = new AccountRole().setRole("ROLE_USER");
        when(userProfileController.accountService.getAccountRoleByName("user")).thenReturn(userRole);
        String retunredView = userProfileController.get("otherUser", model, principal);

        assertThat(retunredView, is("accessDenied"));
    }

    @Test
    public void shouldReturnUserProfileWhenAdminAccessesUserAccount(){
        AccountRole adminRole = new AccountRole().setRole("ROLE_ADMIN");
        when(userProfileController.accountService.getAccountByName("user")).thenReturn(userAccount);
        when(userProfileController.accountService.getAccountRoleByName("admin")).thenReturn(adminRole);
        when(principal.getName()).thenReturn("admin");


        String returnedView = userProfileController.get("user", model, principal);

        verify(model).addAttribute("items", new ArrayList<Item>(0));
        verify(model).addAttribute("userDetail", userAccount);
        assertThat(returnedView, is("userProfile"));
    }

}