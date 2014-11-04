package com.trailblazers.freewheelers.service;

import com.trailblazers.freewheelers.model.Account;
import com.trailblazers.freewheelers.model.AccountRole;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface AccountService {

    List<Account> findAll();

    Account getAccountByName(String userName);

    Account get(Long account_id);

    void delete(Account account);

    void createAdmin(Account account);

    ServiceResult<Account> createAccount(Account account);

    AccountRole getAccountRoleByName(String admin);

    void updateAccount(Account account);

    Account getAccountIdByEmail(String emailAddress);

}
