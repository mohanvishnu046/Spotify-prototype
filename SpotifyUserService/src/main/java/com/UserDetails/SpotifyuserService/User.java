package com.UserDetails.SpotifyuserService;


import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.validation.annotation.Validated;

@Validated
@Document(collection = "Users")
public class User {
    @Email(message = "enter valid email")
    @Schema(description = "email should be valid", example="email@gmail.com")
    @Id
    private String email;
    @NotBlank
    @Pattern(regexp = "^(?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[a-zA-Z]).{8,}$", message = "Password is not strong")
    @Schema(description ="Password should meets standards", example ="Pass@123")
    private String password;
    @Schema(description ="userID is UUID auto generator", example ="UUID auto generator" )
    private String userId;

    public User() {
    }

    public User(String email, String password,String userId) {
        this.email = email;
        this.password = password;
        this.userId = userId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return "User{" +
                "email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", userId='" + userId + '\'' +
                '}';
    }
}
