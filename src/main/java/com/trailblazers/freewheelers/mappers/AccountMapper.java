package com.trailblazers.freewheelers.mappers;

import com.trailblazers.freewheelers.model.Account;

import com.trailblazers.freewheelers.model.Country;
import org.apache.ibatis.annotations.*;

import java.util.List;

public interface AccountMapper {

    @Insert(

        "INSERT INTO account (account_name, email_address, password, street1, street2, city, state_province, country_id, postcode, phone_number, enabled) " +
        "VALUES (#{account_name}, #{emailAddress}, #{password}, #{street1}, #{street2}, #{city}, #{stateProvince}, #{country.countryId}, #{postcode}, #{phoneNumber}, #{enabled})"
    )
    @Options(keyProperty = "account_id", useGeneratedKeys = true)
    Integer insert(Account account);

    @Select(

        "SELECT account_id, account_name, email_address, password, street1, street2, city, state_province, country_id, postcode, phone_number, enabled " +
        "FROM account " +
        "WHERE account_id = #{account_id} "
    )
    @Results(value = {
            @Result(property="account_id"),
            @Result(property="account_name"),
            @Result(property="emailAddress", column="email_address"),
            @Result(property="password"),
            @Result(property ="country", column="country_id", javaType = Country.class,
                    one = @One(select = "com.trailblazers.freewheelers.mappers.CountryMapper.getById")),
            @Result(property="phoneNumber", column="phone_number"),
            @Result(property="enabled")
    })
    Account getById(Long account_id);

    @Select(

        "SELECT account_id, account_name, email_address, password, street1, street2, city, state_province, country_id, postcode, phone_number, enabled " +
        "FROM account " +
        "WHERE account_name = #{account_name} " +
        "LIMIT 1 "

    )
    @Results(value = {
            @Result(property="account_id"),
            @Result(property="account_name"),
            @Result(property="emailAddress", column="email_address"),
            @Result(property="password"),
            @Result(property="country", column = "country_id", javaType = Country.class,
                    one = @One(select = "com.trailblazers.freewheelers.mappers.CountryMapper.getById")),
            @Result(property="street1", column="street1"),
            @Result(property="street2", column="street2"),
            @Result(property="city", column = "city"),
            @Result(property="stateProvince", column="state_province"),
            @Result(property="postcode", column = "postcode"),
            @Result(property="phoneNumber", column="phone_number"),
            @Result(property="enabled")
    })
    Account getByName(String accountName);

    @Update(
        "UPDATE account " +
        "SET account_name=#{account_name}, email_address=#{emailAddress}, street1=#{street1}, street2=#{street2}, city=#{city}, state_province=#{stateProvince}, country_id=#{country.countryId}, postcode=#{postcode}, phone_number=#{phoneNumber}, enabled=#{enabled} " +
        "WHERE account_id=#{account_id}"
    )
    void update(Account account);

    @Select(
        "SELECT account_id, account_name, email_address, password, street1, street2, city, state_province, country_id, postcode, phone_number, enabled FROM account"
    )
    @Results(value = {
            @Result(property="account_id"),
            @Result(property="account_name"),
            @Result(property="emailAddress", column="email_address"),
            @Result(property="password"),
            @Result(property="country", column = "country_id", javaType = Country.class,
                    one = @One(select = "com.trailblazers.freewheelers.mappers.CountryMapper.getById")),
            @Result(property="street1", column="street1"),
            @Result(property="street2", column="street2"),
            @Result(property="city", column = "city"),
            @Result(property="stateProvince", column="state_province"),
            @Result(property="postcode", column = "postcode"),
            @Result(property="phoneNumber", column="phone_number"),
            @Result(property="enabled")
    })
    public List<Account> findAll();

    @Delete(
        "DELETE FROM account WHERE account_id = #{account_id}"
    )
    @Options(flushCache = true)
    void delete(Account account);

    @Select(

            "SELECT account_id, account_name, email_address, password, street1, street2, city, state_province, country_id, postcode, phone_number, enabled " +
                    "FROM account " +
                    "WHERE email_address = #{email} "
    )
    @Results(value = {
            @Result(property="account_id"),
            @Result(property="account_name"),
            @Result(property="emailAddress", column="email_address"),
            @Result(property="password"),
            @Result(property ="country", column="country_id", javaType = Country.class,
                    one = @One(select = "com.trailblazers.freewheelers.mappers.CountryMapper.getById")),
            @Result(property="phoneNumber", column="phone_number"),
            @Result(property="enabled")
    })
    Account getByEmail(String email);
}
