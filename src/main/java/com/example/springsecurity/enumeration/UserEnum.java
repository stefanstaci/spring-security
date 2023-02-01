package com.example.springsecurity.enumeration;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public enum UserEnum {
        USER_NOT_FOUND_EXCEPTION("User not found!");
        private String message;
    }

