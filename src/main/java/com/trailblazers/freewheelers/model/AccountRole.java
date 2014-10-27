package com.trailblazers.freewheelers.model;

public class AccountRole {

    private String accountName;
    private String role;

    public AccountRole() {
    }

    public AccountRole setAccountName(String accountName) {
        this.accountName = accountName;
        return this;
    }

    public String getRole() {
        return role;
    }

    public AccountRole setRole(String role) {
        this.role = role;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        AccountRole that = (AccountRole) o;

        if (!accountName.equals(that.accountName)) return false;
        if (!role.equals(that.role)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = accountName.hashCode();
        result = 31 * result + role.hashCode();
        return result;
    }
}
