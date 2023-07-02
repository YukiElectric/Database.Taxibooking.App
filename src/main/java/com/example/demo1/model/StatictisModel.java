package com.example.demo1.model;

public class StatictisModel {
    private String date;
    private String supplementary;
    private String salary;
    private String earning;

    public StatictisModel(String date, String supplementary, String salary, String earning) {
        this.date = date;
        this.supplementary = supplementary;
        this.salary = salary;
        this.earning = earning;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getSupplementary() {
        return supplementary;
    }

    public void setSupplementary(String supplementary) {
        this.supplementary = supplementary;
    }

    public String getSalary() {
        return salary;
    }

    public void setSalary(String salary) {
        this.salary = salary;
    }

    public String getEarning() {
        return earning;
    }

    public void setEarning(String earning) {
        this.earning = earning;
    }
}
