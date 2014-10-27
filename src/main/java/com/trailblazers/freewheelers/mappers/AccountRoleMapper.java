package com.trailblazers.freewheelers.mappers;

import com.trailblazers.freewheelers.model.AccountRole;
import org.apache.ibatis.annotations.*;

public interface AccountRoleMapper {

    @Insert(
        "INSERT INTO account_role (account_name, role) VALUES (#{accountName}, #{role})"
    )
    @Options(keyProperty = "roleId", useGeneratedKeys = true)
    int insert(AccountRole accountRole);


    @Select(
            "SELECT account_name, role FROM account_role WHERE account_name = #{userName} LIMIT 1"
    )
    @Results(value={
        @Result(property = "roleId", column = "role_id"),
        @Result(property="accountName", column = "account_name"),
        @Result(property="role", column = "role")
    })
    AccountRole getRoleByAccountName(String userName);
}
