package com.nanum.config;

public enum MoveState {
    WAITING("대기"),
    PROGRESS("진행"),
    COMPLETION("완료");

    private String moveState;

    MoveState(String moveState) {
        this.moveState = moveState;
    }

    public String getMoveState() {
        return moveState;
    }
}
