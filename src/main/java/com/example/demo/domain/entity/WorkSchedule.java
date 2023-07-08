package com.example.demo.domain.entity;

import com.example.demo.DTO.IWorkDaysOpenCloseTime;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

import static com.fasterxml.jackson.annotation.JsonProperty.Access.READ_ONLY;

@Entity(name = "work_schedule")
@Table(name = "work_schedule")
@NoArgsConstructor
@Getter
@Setter
@ToString
// TODO field validation via regexp
public class WorkSchedule implements IWorkDaysOpenCloseTime {
    @Id
    @JsonProperty(access = READ_ONLY)
    private Long day_id;
    @Column(columnDefinition = "char(5)")
    private String open;
    @Column(columnDefinition = "char(5)")
    private String close;
    @Column(columnDefinition = "char(10)")
    private String dayName;
}
