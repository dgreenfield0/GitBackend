package domain;

import com.google.gson.Gson;

public class User {

    public String name;
    public String email;
    public String age;
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public String getEmail() {
        return email;
    }
    
    public void setEmail(String email) {
        this.email = email;
    }
    
    public String getAge() {
        return age;
    }
    
    public void setAge(String age) {
        this.age = age;
    }
    
    public String retrieveJson(){
        Gson gson = new Gson();
        return gson.toJson(this);
    }
 }
