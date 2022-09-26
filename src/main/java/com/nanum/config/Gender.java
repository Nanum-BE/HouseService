package com.nanum.config;

public enum Gender {
    MALE("남성"),
    FEMALE("여성"),
    COMMON("공용");

    private String houseGender;

    Gender(String houseGender) {
        this.houseGender = houseGender;
    }

    public String getHouseGender() {
        return houseGender;
    }
}
