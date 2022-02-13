package dimo.applycaran.Models;

import java.util.ArrayList;

public class UnvisitedUser {
    public String name,imageUrl,uid;
    public UnvisitedUser(String n,String i){
        name = n;
        imageUrl = i;
    }

    @Override
    public boolean equals(Object o) {

        if (o == this) {
            return true;
        }

        if (!(o instanceof UnvisitedUser)) {
            return false;
        }

        UnvisitedUser c = (UnvisitedUser) o;
        return uid.equals(c.uid);
    }
}
