package com.voluum.framework.serialization;

/**
 * Mock object for JSON serializtion
 */
public class CountryObject {
    public String code;

    public CountryObject(String code) {
        this.code = code;
    }

    public static CountryObject getPoland() {
        return new CountryObject("PL");
    }
}
