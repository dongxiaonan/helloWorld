package com.trailblazers.freewheelers.service.impl;

import com.trailblazers.freewheelers.mappers.AccountMapper;
import com.trailblazers.freewheelers.mappers.AccountRoleMapper;
import com.trailblazers.freewheelers.mappers.MyBatisUtil;
import com.trailblazers.freewheelers.model.Account;
import com.trailblazers.freewheelers.model.AccountRole;
import com.trailblazers.freewheelers.model.AccountValidation;
import com.trailblazers.freewheelers.service.AccountService;
import com.trailblazers.freewheelers.service.ServiceResult;
import org.apache.ibatis.exceptions.PersistenceException;
import org.apache.ibatis.session.SqlSession;
import org.springframework.security.authentication.encoding.Md5PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class AccountServiceImpl implements AccountService {

    public static final String USER = "ROLE_USER";
    public static final String ADMIN = "ROLE_ADMIN";

    private final AccountRoleMapper accountRoleMapper;
    private SqlSession sqlSession;
    private AccountMapper accountMapper;

    public AccountServiceImpl() {
        this(MyBatisUtil.getSqlSessionFactory().openSession());
    }

    public AccountServiceImpl(SqlSession sqlSession) {
        this.sqlSession= sqlSession;
        this.accountMapper = sqlSession.getMapper(AccountMapper.class);
        this.accountRoleMapper = sqlSession.getMapper(AccountRoleMapper.class);
    }

    @Override
    public List<Account> findAll() {
        return accountMapper.findAll();
    }

    @Override
    public Account getAccountByName(String userName) {
        return accountMapper.getByName(userName);
    }

    @Override
    public Account get(Long account_id) {
        return accountMapper.getById(account_id);
    }

    @Override
    public void delete(Account account) {
        accountMapper.delete(account);
        sqlSession.commit();
    }

    @Override
    public void createAdmin(Account account) {
        create(account, ADMIN);
    }

    @Override
    public ServiceResult<Account> createAccount(Account account){
        Map<String, String> errors = new AccountValidation().verifyInputs(account);

        if(errors.isEmpty()) {
            try {
                create(account, USER);
            }catch (PersistenceException e){
                sqlSession.rollback();
               if( e.getMessage().contains("email")){
                   errors.put("email", "Email address is already being used.");
               }
            }
        }

        return new ServiceResult(errors, account);
    }

    @Override
    public AccountRole getAccountRoleByName(String userName) {

        return accountRoleMapper.getRoleByAccountName(userName);
    }

    @Override
    public void updateAccount(Account account) {
        accountMapper.update(account);
        sqlSession.commit();
    }

    @Override
    public Account getAccountIdByEmail(String emailAddress) {
        return accountMapper.getByEmail(emailAddress);
    }

    private void create(Account account, String role) {

        Md5PasswordEncoder md5PasswordEncoder = new Md5PasswordEncoder();
        account.setPassword(md5PasswordEncoder.encodePassword(account.getPassword(), null));
        accountMapper.insert(account);
        accountRoleMapper.insert(roleFor(account, role));
        sqlSession.commit();
    }

    private AccountRole roleFor(Account account, String role) {
        return new AccountRole()
                .setAccountName(account.getAccount_name())
                .setRole(role);
    }

    @Override
    public boolean enabledAccountByEmail(String emailAddress) {
            Account account = getAccountIdByEmail(emailAddress);
            if (account != null) {
                account.setEnabled(true);
                updateAccount(account);
                return true;
            }
        return false;
    }
}
