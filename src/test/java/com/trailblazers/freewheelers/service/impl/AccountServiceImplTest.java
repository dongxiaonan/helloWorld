package com.trailblazers.freewheelers.service.impl;

import com.trailblazers.freewheelers.mappers.AccountMapper;
import com.trailblazers.freewheelers.mappers.AccountRoleMapper;
import com.trailblazers.freewheelers.model.Account;
import com.trailblazers.freewheelers.model.AccountRole;
import com.trailblazers.freewheelers.model.Country;
import com.trailblazers.freewheelers.service.AccountService;
import com.trailblazers.freewheelers.service.ServiceResult;
import org.apache.ibatis.exceptions.PersistenceException;
import org.apache.ibatis.session.SqlSession;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.initMocks;

public class AccountServiceImplTest {

    AccountService accountService;

    @Mock
    SqlSession sqlSession;
    @Mock
    AccountMapper accountMapper;
    @Mock
    AccountRoleMapper accountRoleMapper;

    @Before
    public void setUp() throws Exception {
        initMocks(this);
        when(sqlSession.getMapper(AccountMapper.class)).thenReturn(accountMapper);
        when(sqlSession.getMapper(AccountRoleMapper.class)).thenReturn(accountRoleMapper);

        accountService = new AccountServiceImpl(sqlSession);
    }

    @Test
    public void shouldCreateAccountWhenThereAreNoValidationErrors(){
        Account account = getAccountWithoutErrors();

        ServiceResult<Account> serviceResult = accountService.createAccount(account);

        verify(accountMapper, times(1)).insert(account);
        verify(accountRoleMapper, times(1)).insert(any(AccountRole.class));
        verify(sqlSession, times(1)).commit();
        assertFalse(serviceResult.hasErrors());
    }

    @Test
    public void shouldCallAccountRoleMapperToRetrieveTheAccountRole() {
        accountService.getAccountRoleByName("admin");

        verify(accountRoleMapper).getRoleByAccountName("admin");
    }

    @Test
    public void shouldReturnAccountRoleObjectWhichIsRetrievedFromAccountRoleMapper(){
        AccountRole expectedAccountRole = new AccountRole();
        when(accountRoleMapper.getRoleByAccountName("Some Name")).thenReturn(expectedAccountRole);

        AccountRole accountRole = accountService.getAccountRoleByName("Some Name");

        assertThat(accountRole, is(expectedAccountRole));
    }

    @Test
    public void shouldHaveErrorForKeyEmailWhenCreatingAccountWithExistingEmail() {
        PersistenceException persistenceException = new PersistenceException("account_email_address");
        when(accountMapper.insert(any(Account.class))).thenThrow(persistenceException);

        ServiceResult<Account> serviceResult = accountService.createAccount(getAccountWithoutErrors());

        assertThat(serviceResult.getErrors().get("email"), is("Email address is already being used."));
    }

    @Test
    public void shouldGetTheAccountByEmailID() throws Exception {

        accountService.getAccountIdByEmail("some.body@email.com");

        verify(accountMapper,times(1)).getByEmail(eq("some.body@email.com"));
    }

    private Account getAccountWithoutErrors() {
        return new Account()
                .setEmail_address("example@example.com")
                .setCountry(new Country(1, "United Kingdom"))
                .setPassword("V3y Secure!")
                .setStreet1("Greenwood Avenue")
                .setStreet2("Apartment 202")
                .setCity("London")
                .setState_Province("Somewhere")
                .setPostcode("12453")
                .setAccount_name("Example Person")
                .setPhoneNumber("1234567890");
    }
}
