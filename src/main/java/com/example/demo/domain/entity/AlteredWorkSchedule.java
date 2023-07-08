package com.example.demo.domain.entity;

import com.example.demo.DTO.IWorkDaysOpenCloseTime;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.*;

import static com.fasterxml.jackson.annotation.JsonProperty.Access.READ_ONLY;

@Entity(name = "altered_work_schedule")
@Table(name = "altered_work_schedule")
@NoArgsConstructor
@Getter
@Setter
@ToString
// TODO field validation via regexp
public class AlteredWorkSchedule implements IWorkDaysOpenCloseTime {
    //  - add mutual interface for open/close
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonProperty(access = READ_ONLY)
    private Long id;

    @Column(columnDefinition = "char(10)")
    private String date;
    @Column(columnDefinition = "char(5)")
    private String open;
    @Column(columnDefinition = "char(5)")
    private String close;
}