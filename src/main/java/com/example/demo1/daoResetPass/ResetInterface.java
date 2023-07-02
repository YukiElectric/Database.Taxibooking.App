package com.example.demo1.daoResetPass;

public abstract class ResetInterface {
    public abstract int CheckEmail(String email);
    public abstract boolean ResetPass(int ID, String password);
}
