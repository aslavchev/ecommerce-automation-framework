package io.github.aslavchev.utils;

/**
 * UserData - POJO for test user expected data
 * Loaded from user-data.csv for test assertions
 */
public class UserData {
    public final String email;
    public final String fullName;
    public final String street;
    public final String cityStatePostcode;
    public final String country;
    public final String phone;

    public UserData(String email, String fullName, String street,
                    String cityStatePostcode, String country, String phone) {
        this.email = email;
        this.fullName = fullName;
        this.street = street;
        this.cityStatePostcode = cityStatePostcode;
        this.country = country;
        this.phone = phone;
    }
}
