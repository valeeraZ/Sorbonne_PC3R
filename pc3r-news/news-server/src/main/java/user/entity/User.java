package user.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.NaturalId;
import subscription.entity.Category;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@ToString
@Table(name = "user")
@GenericGenerator(name = "uuid", strategy = "org.hibernate.id.UUIDGenerator")
public class User {
    @Id
    @GeneratedValue(generator = "uuid")
    private String id_user;

    @NaturalId(mutable = true)
    @Column(nullable = false)
    private String email;

    @JsonIgnore
    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String name;

    @ManyToMany(cascade = CascadeType.ALL, fetch=FetchType.EAGER)
    @JoinTable(name = "subscription",
            joinColumns = {@JoinColumn(name="id_user")},
            inverseJoinColumns = {@JoinColumn(name="id_category")})
    private List<Category> subscriptions = new ArrayList<>();
}
