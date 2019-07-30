package com.example.thisorthat.model;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Vote {
    public static final int INCREMENT_THIS = 0;
    public static final int INCREMENT_THAT = 1;
    @SerializedName("thisCount")
    @Expose
    private IncrementByOne incrementByOneThis;
    @SerializedName("thatCount")
    @Expose
    private IncrementByOne incrementByOneThat;

    public Vote(int TAG) {
        if (TAG == INCREMENT_THIS) {
            incrementByOneThis = new IncrementByOne();
        }
        if (TAG == INCREMENT_THAT) {
            incrementByOneThat = new IncrementByOne();
        }
    }


}


class IncrementByOne {

    @SerializedName("__op")
    @Expose
    private String op = "Increment";
    @SerializedName("amount")
    @Expose
    private Integer amount = 1;

    public String getOp() {
        return op;
    }


    public Integer getAmount() {
        return amount;
    }

}