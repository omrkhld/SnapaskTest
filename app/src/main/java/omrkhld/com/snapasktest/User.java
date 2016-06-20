package omrkhld.com.snapasktest;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

/**
 * Created by Omar on 19/6/2016.
 */
public class User implements Serializable {

    public User() {
    }

    private int ID;
    private String gender;

    public static class Name implements Serializable {

        public Name() {
        }

        private String first, last;

        public String getFirst() { return first; }
        public String getLast() { return last; }

        public void setFirst(String f) { first = f; }
        public void setLast(String l) { last = l; }
    }

    private Name name;
    private String email, username;
    private int registered, dob;
    private String school, country_code, phone, imgURL;

    public static class Role implements Serializable {

        public Role() {
        }

        private int ID;
        private String name;

        public int getID() { return ID; }
        public String getName() { return name; }

        public void setID(int id) { ID = id; }
        public void setName(String n) { name = n; }
    }

    private Role role;
    private int rating, rating_total;

    public int getID() { return ID; }
    public String getGender() { return gender; }
    public Name getName() { return name; }
    public String getEmail() { return email; }
    public String getUsername() { return username; }
    public int getRegistered() { return registered; }
    public int getDOB() { return dob; }
    public String getSchool() { return school; }
    @JsonProperty("country_code")
    public String getCountryCode() { return country_code; }
    public String getPhone() { return phone; }
    @JsonProperty("profile_pic_url")
    public String getImgURL() { return imgURL; }
    public Role getRole() { return role; }
    public int getRating() { return rating; }
    @JsonProperty("rating_total")
    public int getRatingTotal() { return rating_total; }

    public void setID(int i) { ID = i; }
    public void setGender(String g) { gender = g; }
    public void setName(Name n) { name = n; }
    public void setEmail(String e) { email = e; }
    public void setUsername(String u) { username = u; }
    public void setRegistered(int r) { registered = r; }
    public void setDOB(int d) { dob = d; }
    public void setSchool(String s) { school = s; }
    public void setCountryCode(String c) { country_code = c; }
    public void setPhone(String p) { phone = p; }
    public void setImgURL(String i) { imgURL = i; }
    public void setRole(Role r) { role = r; }
    public void setRating(int r) { rating = r; }
    public void setRatingTotal(int rt) { rating_total = rt; }
}
