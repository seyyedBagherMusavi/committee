package com.nicico.companies.entities.CommissionEntities;

import com.nicico.copper.common.domain.Auditable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

/**
 * Stores the detailed periodicity settings for a commission calendar.
 */
@Entity
@Table(name = "commission_periodicity_settings")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CommissionPeriodicitySettings extends Auditable {

    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @Column(columnDefinition = "uuid")
    private String id;

    // ----- Calendar FK (one-to-one owner) -----
    @Column(name = "calendar_id", unique = true, nullable = false)
    private String calendarId;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "calendar_id", insertable = false, updatable = false)
    private CommissionCalendar calendar;

    // ----- IntervalValue -----
    @Column(name = "interval_value")
    private Integer intervalValue;

    // ----- WeekdayMask -----
    @Column(name = "weekday_mask")
    private String weekdayMask;

    // ----- DayOfMonth -----
    @Column(name = "day_of_month")
    private String dayOfMonth;

    // ----- WeekOfMonth -----
    @Column(name = "week_of_month")
    private String weekOfMonth;

    // ----- MonthMask -----
    @Column(name = "month_mask")
    private String monthMask;

    // ----- HolidayRule -----
    @Column(name = "holiday_rule", length = 1)
    private String holidayRule;
}
