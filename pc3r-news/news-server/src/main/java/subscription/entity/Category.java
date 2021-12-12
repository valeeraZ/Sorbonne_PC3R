package subscription.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import user.entity.User;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@ToString
@Table(name = "category")
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id_category;

    private String name_category;

    /*@ManyToMany(cascade = CascadeType.ALL, fetch=FetchType.EAGER)
    @JoinTable(name = "subscription",
            joinColumns = {@JoinColumn(name="id_category")},
            inverseJoinColumns = {@JoinColumn(name="id_user")})
    private List<User> users = new ArrayList<>();*/

}
