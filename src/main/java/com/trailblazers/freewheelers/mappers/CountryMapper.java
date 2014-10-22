package com.trailblazers.freewheelers.mappers;

import com.trailblazers.freewheelers.model.Country;
import org.apache.ibatis.annotations.*;

import java.util.List;

public interface CountryMapper {
    @Insert(
            "INSERT INTO country (country_name) " +
                    "VALUES (#{name})"
    )
    @Options(keyProperty = "country_id", useGeneratedKeys = true)
    Integer insert(Country country);

    @Select(
            "SELECT country_id, country_name " +
                    "FROM country " +
                    "WHERE country_id = #{country_id}"
    )
    @ConstructorArgs({
            @Arg(column = "country_id", javaType = Integer.class),
            @Arg(column = "country_name", javaType = String.class)
    })
    Country getById(@Param("country_id") Integer country_id);

    @Select(
            "SELECT country_id, country_name " +
            "FROM country " +
            "WHERE country_name= #{name}"
    )
    @ConstructorArgs({
            @Arg(column = "country_id", javaType = Integer.class),
            @Arg(column = "country_name", javaType = String.class)
    })
    Country getByName(@Param("name") String name);

    @Select(
            "SELECT country_id, country_name" +
                    " FROM country " +
                    "ORDER BY country_name"
    )
    @ConstructorArgs({
            @Arg(column = "country_id", javaType = Integer.class),
            @Arg(column = "country_name", javaType = String.class)
    })
    List<Country> findAll();
}
