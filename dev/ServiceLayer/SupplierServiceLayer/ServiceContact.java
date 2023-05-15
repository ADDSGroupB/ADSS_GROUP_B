package ServiceLayer.SupplierServiceLayer;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
public class ServiceContact {

    //private int supplierID;
    private String name;
    private String email;
    private String phoneNumber;

    public ServiceContact(String name, String email, String phoneNumber) {
        this.name = name;
        this.email = email;
        this.phoneNumber = phoneNumber;
    }
    public ServiceContact(){}


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }


    public  boolean isValidEmail(String email) {
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\."+
                "[a-zA-Z0-9_+&*-]+)*@" +
                "(?:[a-zA-Z0-9-]+\\.)+[a-z" +
                "A-Z]{2,7}$";
        Pattern pattern = Pattern.compile(emailRegex);
        if (email == null) {
            return false;
        }
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    public boolean validatePhoneNumber(String number) {
        String pattern = "^05[02485]-\\d{7}$";
        return Pattern.matches(pattern, number);
    }



}


