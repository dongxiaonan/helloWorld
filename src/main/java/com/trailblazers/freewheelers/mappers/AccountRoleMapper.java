package com.trailblazers.freewheelers.mappers;

import com.trailblazers.freewheelers.model.AccountRole;
import org.apache.ibatis.annotations.*;

public interface AccountRoleMapper {

    @Insert(
        "INSERT INTO account_role (account_name, role) VALUES (#{account_name}, #{role})"
    )
    @Options(keyProperty = "role_id", useGeneratedKeys = true)
    void insert(AccountRole accountRole);


    @Select(
            "SELECT account_name, role FROM account_role WHERE account_name = #{userName} LIMIT 1"
    )
    @Results(value={
        @Result(property="account_name", column = "account_name"),
        @Result(property="role", column = "role")
    })
    AccountRole getRoleByAccountName(String userName);
}
