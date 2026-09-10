package com.example.Spring_Salon_Project.dto;

import com.example.Spring_Salon_Project.enumiration.StaffScheduleStatus;
import jakarta.validation.constraints.NotNull;
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

    @NotNull(message = "Staff ID is required")
    private Long staffId;
    private String staffName;

    @NotNull(message = "Day of week is required")
    private DayOfWeek dayOfWeek;

    @NotNull(message = "Start time is required")
    private LocalTime startTime;

    @NotNull(message = "End time is required")
    private LocalTime endTime;
    private StaffScheduleStatus scheduleStatus;

}
