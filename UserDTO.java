
package com.cafeteria.app.dto;


public class UserDTO {
    private Long id;
    private String email;
    private String name;
    private String phone;
 

    
    //DTO contructor for user
    public UserDTO(Long id, String email, String name, String phone) {
        this.id = id;
        this.email = email;
        this.name = name;
        this.phone = phone;
       
    }

    //empty contructor
    public UserDTO() {}

    // getters and setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }


    
}
