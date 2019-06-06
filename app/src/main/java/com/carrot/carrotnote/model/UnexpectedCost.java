package com.carrot.carrotnote.model;

public class UnexpectedCost extends Cost {
    private final static String UNEXPECTED_EXPENSE = "Unexpected expense";

    private String mReason;
    


    public String getReason() {
        return mReason;
    }

    public void setReason(String reason) {
        mReason = reason;
    }
}
