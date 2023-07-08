package com.example.demo.domain.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import static com.fasterxml.jackson.annotation.JsonProperty.Access.READ_ONLY;


@Entity(name = "abonement")
@Table(name = "abonements")
@Getter
@Setter
@ToString
// TODO field validation via regexp
public class Abonement {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonProperty(access = READ_ONLY)
    private long abonement_id;
    @Column(nullable = false)
    private String name_of_abonement;
    @Column(nullable = false)
    private String description;
    @Column(nullable = false, columnDefinition = "INT(11) UNSIGNED ")
    private int price;

//    @OneToOne(mappedBy = "abonement")
//    private AppUser user;

}
