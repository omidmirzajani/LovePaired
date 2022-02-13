package dimo.applycaran.Models;

import java.util.ArrayList;

public class User {
    private String name,email,imageUrl,gender,looking_for;
    private int age;
    private ArrayList<String> favorites;

    public User(){
        age = 0;
        imageUrl = "";
        gender = "";
        looking_for = "";
        favorites = new ArrayList<String>();
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getName() {
        return name;
    }

    public void setName(String username) {
        this.name = username;
    }


    public ArrayList<String> getFavorites() {
        return favorites;
    }

    public void setFavorites(ArrayList<String> favorites) {
        this.favorites = favorites;
    }

    public void addFavorite(String fav){
        this.favorites.add(fav);
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getLooking_for() {
        return looking_for;
    }

    public void setLooking_for(String looking_for) {
        this.looking_for = looking_for;
    }
}
