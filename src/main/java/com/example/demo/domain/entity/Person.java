package com.example.demo.domain.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;
import java.time.Period;

import static com.fasterxml.jackson.annotation.JsonProperty.Access.READ_ONLY;

@Entity(name="person")
@Table(name="persons")
@Getter
@Setter
@ToString(exclude = "user")
// TODO field validation via regexp
public class Person {

    @Id
    @JsonProperty(access = READ_ONLY)
    private long user_id;
    @Column(unique = true,nullable = false)
    private int passport;
    @Column(nullable = false)
    private String first_name;
    @Column(nullable = false)
    private String surname;
    @Column(nullable = false)
    private LocalDate date_of_birth;
    @Column(nullable = false)
    private String gender;
    @Transient
    private Integer age;
    @OneToOne(fetch = FetchType.LAZY)
    @MapsId
    @JoinColumn(name = "user_id", foreignKey = @ForeignKey(name ="person_foreign_key"))
    private AppUser user;

    public Integer getAge() {
        return Period.between(this.date_of_birth,LocalDate.now()).getYears();
    }

    public void setAge(Integer age) {
        this.age = age;
    }

}
