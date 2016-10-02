package com.register.example.forms;

public class JakiKolwiekForm extends Cokolwiek {
    private String jakis;

    public String getJakis() {
        return jakis;
    }

    public void setJakis(String jakis) {
        this.jakis = jakis;
    }

    @Override
    public String toString() {
        return "JakiKolwiekForm{" +
                "jakis='" + jakis + '\'' +
                "} " + super.toString();
    }
}
