package com.voluum.framework.serialization;

public class CountryObject {
    public String code;

    public CountryObject(String code) {
        this.code = code;
    }

    public static CountryObject getPoland() {
        return new CountryObject("PL");
    }
}
