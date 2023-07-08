package com.example.demo.domain.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import static com.fasterxml.jackson.annotation.JsonProperty.Access.READ_ONLY;


@Entity(name = "order")
@Table(name = "orders")
@Getter
@Setter
@NoArgsConstructor
@ToString(exclude = {"user"})
// TODO field validation via regexp
public class AppOrder {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonProperty(access = READ_ONLY)
    private long order_id;


    @ManyToOne
    @JoinColumn(nullable = false,name = "user_id",referencedColumnName = "user_id",foreignKey = @ForeignKey(name ="user_id_foreign_key"))
    private AppUser appUser;

    @Column(nullable = false)
    private String date_of_game;
    @Column(columnDefinition = "CHAR(5)", nullable = false)
    private String start_time;
    @Column(columnDefinition = "CHAR(5)", nullable = false)
    private String end_time;
    @Column(nullable = false)
    private int payment;
    @Column(columnDefinition = "BOOLEAN", nullable = false)
    private int payment_status;


}
