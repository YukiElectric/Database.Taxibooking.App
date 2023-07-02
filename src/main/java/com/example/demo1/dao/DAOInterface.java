package com.example.demo1.dao;

public abstract class DAOInterface {
	public abstract int CheckUser(String user);
	public abstract boolean CheckPass(String user, String password);
	public abstract int CheckAccountType(int ID);
}
