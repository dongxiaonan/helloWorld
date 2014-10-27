package integration.com.trailblazers.freewheelers.persistence;

import com.trailblazers.freewheelers.mappers.AccountRoleMapper;
import com.trailblazers.freewheelers.model.AccountRole;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

public class AccountRoleMapperTest extends MapperTestBase {

    private AccountRoleMapper accountRoleMapper;

    @Before
    public void setUp() throws Exception {
        super.setUp();
        accountRoleMapper = getSqlSession().getMapper(AccountRoleMapper.class);
    }

    @Test
    public void shouldInsertAnAccountRole() throws Exception {
        AccountRole accountRole = new AccountRole();
        accountRole.setAccountName("Some Name");
        accountRole.setRole("Some Role");

        Integer accountId = accountRoleMapper.insert(accountRole);

        assertThat(accountId, is(not(nullValue())));
    }

    @Test
    public void shouldFetchAccountRoleByAccountName() throws Exception {
        AccountRole accountRole = new AccountRole();
        accountRole.setAccountName("Some Name");
        accountRole.setRole("Some Role");
        accountRoleMapper.insert(accountRole);

        AccountRole role = accountRoleMapper.getRoleByAccountName("Some Name");

        AccountRole expectedAccountRole = new AccountRole().setRole("Some Role").setAccountName("Some Name");
        assertThat(role, is(expectedAccountRole));
    }

}
