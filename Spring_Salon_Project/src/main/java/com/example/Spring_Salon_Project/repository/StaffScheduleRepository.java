package com.example.Spring_Salon_Project.repository;

import com.example.Spring_Salon_Project.dto.StaffScheduleDTO;
import com.example.Spring_Salon_Project.entity.StaffSchedule;
import com.example.Spring_Salon_Project.enumiration.StaffScheduleStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.DayOfWeek;
import java.util.List;
import java.util.Optional;

public interface StaffScheduleRepository extends JpaRepository<StaffSchedule, Long> {
    @Query("""
        SELECT new com.example.Spring_Salon_Project.dto.StaffScheduleDTO(
            sc.scheduleId,
            s.staffId,
            u.userName,
            sc.dayOfWeek,
            sc.startTime,
            sc.endTime,
            sc.scheduleStatus
        )
        FROM StaffSchedule sc
        LEFT JOIN sc.staff s
        LEFT JOIN s.user u
        WHERE sc.scheduleId = :scheduleId
    """)
    Optional<StaffScheduleDTO> selectStaffSchedule(@Param("scheduleId") Long scheduleId);

    @Query("""
        SELECT new com.example.Spring_Salon_Project.dto.StaffScheduleDTO(
            sc.scheduleId,
            s.staffId,
            u.userName,
            sc.dayOfWeek,
            sc.startTime,
            sc.endTime,
            sc.scheduleStatus
        )
        FROM StaffSchedule sc
        LEFT JOIN sc.staff s
        LEFT JOIN s.user u
        WHERE sc.scheduleId = :scheduleId
    """)
    StaffScheduleDTO findByScheduleId(@Param("scheduleId") Long scheduleId);

    @Query("""
        SELECT new com.example.Spring_Salon_Project.dto.StaffScheduleDTO(
            sc.scheduleId,
            s.staffId,
            u.userName,
            sc.dayOfWeek,
            sc.startTime,
            sc.endTime,
            sc.scheduleStatus
        )
        FROM StaffSchedule sc
        LEFT JOIN sc.staff s
        LEFT JOIN s.user u
        ORDER BY sc.scheduleId DESC
    """)
    List<StaffScheduleDTO> getAllSchedule();

    @Query("""
        SELECT new com.example.Spring_Salon_Project.dto.StaffScheduleDTO(
            sc.scheduleId,
            s.staffId,
            u.userName,
            sc.dayOfWeek,
            sc.startTime,
            sc.endTime,
            sc.scheduleStatus
        )
        FROM StaffSchedule sc
        LEFT JOIN sc.staff s
        LEFT JOIN s.user u
        WHERE (:scheduleId = 0L OR sc.scheduleId = :scheduleId)
          AND (:staffName IS NULL OR :staffName = '' OR LOWER(u.userName) LIKE LOWER(CONCAT('%', :staffName, '%')))
          AND (:dayOfWeek IS NULL OR sc.dayOfWeek = :dayOfWeek)
          AND (:status IS NULL OR sc.scheduleStatus = :status)
        ORDER BY sc.scheduleId DESC
    """)
    List<StaffScheduleDTO> filterStaffSchedules(
            @Param("scheduleId") long scheduleId,
            @Param("staffName") String staffName,
            @Param("dayOfWeek") DayOfWeek dayOfWeek,
            @Param("status") StaffScheduleStatus status
    );

    @Query("""
        SELECT new com.example.Spring_Salon_Project.dto.StaffScheduleDTO(
            sc.scheduleId,
            s.staffId,
            u.userName,
            sc.dayOfWeek,
            sc.startTime,
            sc.endTime,
            sc.scheduleStatus
        )
        FROM StaffSchedule sc
        LEFT JOIN sc.staff s
        LEFT JOIN s.user u
        WHERE s.staffId = :staffId
        ORDER BY sc.dayOfWeek ASC
    """)
    List<StaffScheduleDTO> getStaffSchedulesByStaffId(@Param("staffId") Long staffId);
}
