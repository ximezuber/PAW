package ar.edu.itba.paw.webapp.dto;

import ar.edu.itba.paw.model.User;

public class UserDto {
    private String firstName;
    private String lastName;
    private String email;
    private String password;

    public static UserDto fromUser(User user) {
        UserDto userDto = new UserDto();
        userDto.firstName = user.getFirstName();
        userDto.lastName = user.getLastName();
        userDto.email = user.getEmail();
        return userDto;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
