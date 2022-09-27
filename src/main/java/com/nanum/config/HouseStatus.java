package com.nanum.config;

public enum HouseStatus {
    BEFORE_APPROVAL("승인 전"),
    COMPLETED_APPROVAL("승인 완료");

    private String houseStatus;

    HouseStatus(String houseStatus) {
        this.houseStatus = houseStatus;
    }

    public String getHouseStatus() {
        return houseStatus;
    }
}
