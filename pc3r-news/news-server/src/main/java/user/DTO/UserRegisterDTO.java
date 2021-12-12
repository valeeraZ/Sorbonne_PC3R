package user.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import user.entity.User;

import javax.validation.constraints.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class UserRegisterDTO {
    @NotBlank(message = "The name cannot be blank")
    @Size(min = 2, max = 16, message = "The name must be between 2 and 16 characters")
    private String name;

    @NotBlank(message = "The password must not be blank")
    @Pattern(regexp = "^(?=.*?[A-Z])(?=.*?[a-z])(?=.*?[0-9])(?=.*?[#?!@$%^&*-]).{8,}$",
            message = "The password must be at least 8 characters long and contain at least an upper case, a number and a special symbol")
    private String password;

    @NotBlank(message = "Please provide a valid email address")
    @Email(message = "Please provide a valid email address")
    private String email;

    public User toUser(){
        return User.builder()
                .email(this.email)
                .password(this.password)
                .name(this.name)
                .build();
    }
}
