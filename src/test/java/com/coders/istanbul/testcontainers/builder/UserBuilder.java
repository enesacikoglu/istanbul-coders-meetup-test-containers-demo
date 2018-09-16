package com.coders.istanbul.testcontainers.builder;

import com.coders.istanbul.testcontainers.domain.User;

public final class UserBuilder {
    private User user;

    private UserBuilder() {
        user = new User();
    }

    public static UserBuilder anUser() {
        return new UserBuilder();
    }

    public UserBuilder id(Long id) {
        user.setId(id);
        return this;
    }

    public UserBuilder name(String name) {
        user.setName(name);
        return this;
    }

    public UserBuilder surName(String surName) {
        user.setSurName(surName);
        return this;
    }

    public UserBuilder email(String email) {
        user.setEmail(email);
        return this;
    }

    public UserBuilder phone(String phone) {
        user.setPhone(phone);
        return this;
    }

    public User build() {
        return user;
    }
}
