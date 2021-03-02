package com.iths.labbspringboot.dtos;

public class CatDto {

    private int id;
    private String name;
    private String type;
    private String gender;
    private double weight;

    public CatDto(int id, String name, String type, String gender, double weight) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.gender = gender;
        this.weight = weight;
    }

    public CatDto() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }
}
