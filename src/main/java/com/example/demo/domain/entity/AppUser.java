package com.example.demo.domain.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;

import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;


import java.util.Collection;
import java.util.Collections;
import java.util.List;

import static com.fasterxml.jackson.annotation.JsonProperty.Access.WRITE_ONLY;
import static com.fasterxml.jackson.annotation.JsonProperty.Access.READ_ONLY;

@Entity(name = "user")
@Table(name = "users")
@NoArgsConstructor
@Data
// TODO field validation via regexp
public class AppUser implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonProperty(access = READ_ONLY)
    private long user_id;
    @Column(unique = true, nullable = false)
    private String email;
    @JsonProperty(access = WRITE_ONLY)
    @Column(nullable = false)
    private String password;
    @Enumerated(EnumType.STRING)
    private Role role;

    @Enumerated(EnumType.STRING)
    private Theme theme;
    private Boolean locked;

    private Boolean enabled;

    private String refreshToken;


    @OneToOne(mappedBy = "user",
            cascade = {CascadeType.ALL},
            optional = false)
    private Person person;
    @OneToOne
    @JoinColumn(name = "abonement",referencedColumnName = "abonement_id",
            foreignKey = @ForeignKey(name = "abonement_foreign_key"))
    private Abonement abonement;

    @OneToMany(mappedBy = "appUser", cascade = CascadeType.REMOVE)
    private List<AppOrder> appOrders;

    @PrePersist
    public void prePersist(){
        this.role = Role.ROLE_USER;
        this.theme =Theme.light ;
        this.abonement = new Abonement();
        this.abonement.setAbonement_id(1L);
        this.enabled = true;
        this.locked = false;
        PasswordEncoder encoder = new BCryptPasswordEncoder();
        this.password = encoder.encode(password);

    }
    @JsonIgnore
    public Collection<? extends GrantedAuthority> getAuthorities() {
        SimpleGrantedAuthority authority =
                new SimpleGrantedAuthority(role.name());
        return Collections.singletonList(authority);
    }


    public void setPerson(Person person) {
        this.person = person;
        person.setUser(this);

    }

    @JsonIgnore
    @Override
    public String getUsername() {
        return email;
    }
    @JsonIgnore
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }
    @JsonIgnore
    @Override
    public boolean isAccountNonLocked() {
        return !locked;
    }
    @JsonIgnore
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }
    @JsonIgnore
    @Override
    public boolean isEnabled() {
        return enabled;
    }

    public long getUser_id() {
        return user_id;
    }

    public String getEmail() {
        return email;
    }

    @Override
    public String getPassword() {
        return password;
    }
    @JsonIgnore
    public Role getRole() {
        return role;
    }

    public Theme getTheme() {
        return theme;
    }
    @JsonIgnore
    public Boolean getLocked() {
        return locked;
    }
    @JsonIgnore
    public String getRefreshToken() {
        return refreshToken;
    }

    public Person getPerson() {
        return person;
    }

    public Abonement getAbonement() {
        return abonement;
    }

    public List<AppOrder> getAppOrders() {
        return appOrders;
    }




    public enum Role implements GrantedAuthority {

        ROLE_USER("User"), ROLE_ADMIN("Admin");

        private String roleName;

        Role(String roleName) {
            this.roleName = roleName;
        }

        @Override
        public String toString() {
            return roleName;
        }

        @Override
        public String getAuthority() {
            return name();
        }
    }

    public enum Theme{
        dark("dark"), light("light");
        private  String theme;
        Theme(String theme) {
            this.theme = theme;
        }

        @Override
        public String toString() {
            return theme;
        }
    }


}
