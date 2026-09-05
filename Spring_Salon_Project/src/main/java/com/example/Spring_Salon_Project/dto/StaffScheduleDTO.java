package com.example.Spring_Salon_Project.dto;

import com.example.Spring_Salon_Project.enumiration.StaffScheduleStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.DayOfWeek;
import java.time.LocalTime;


@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class StaffScheduleDTO {
    private Long scheduleId;
    private Long staffId;
    private String staffName;
    private DayOfWeek dayOfWeek;
    private LocalTime startTime;
    private LocalTime endTime;
    private StaffScheduleStatus scheduleStatus;

}
