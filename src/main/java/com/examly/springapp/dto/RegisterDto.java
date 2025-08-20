package com.examly.springapp.dto;

import com.examly.springapp.model.Role;
import com.fasterxml.jackson.annotation.JsonProperty;

public class RegisterDto {
    private String name;
    private String email;
    private String password;
    @JsonProperty("role")
    private Role role;  // New field for role

    // Getters and Setters
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    public Role getRole() { return role; }
    public void setRole(Role role) { this.role = role; }
}
