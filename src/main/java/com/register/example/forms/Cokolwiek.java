package com.register.example.forms;

public abstract class Cokolwiek {
    private String name;

    private String lastName;


    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Cokolwiek{" +
                "lastName='" + lastName + '\'' +
                ", name='" + name + '\'' +
                '}';
    }
}
