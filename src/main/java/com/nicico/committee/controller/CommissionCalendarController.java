package com.nicico.committee.controller;

import com.nicico.committee.dto.CommissionCalendarCreateDto;
import com.nicico.committee.dto.CommissionCalendarDto;
import com.nicico.committee.dto.CommissionCalendarUpdateDto;
import com.nicico.committee.mapper.CommissionCalendarMapper;
import com.nicico.committee.service.CommissionCalendarService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/commissioncalendar")
@Tag(name = "CommissionCalendar", description = "عملیات CRUD برای CommissionCalendar")
public class CommissionCalendarController {

    private final CommissionCalendarService service;
    private final CommissionCalendarMapper mapper;

    public CommissionCalendarController(CommissionCalendarService service, CommissionCalendarMapper mapper) {
        this.service = service;
        this.mapper = mapper;
    }

    @GetMapping
    @Operation(summary = "دریافت لیست")
    public ResponseEntity<List<CommissionCalendarDto>> findAll() {
        return ResponseEntity.ok(service.findAll().stream().map(mapper::toDto).collect(Collectors.toList()));
    }

    @GetMapping("/{id}")
    @Operation(summary = "دریافت بر اساس شناسه")
    public ResponseEntity<CommissionCalendarDto> findById(@PathVariable String id) {
        return ResponseEntity.ok(mapper.toDto(service.findById(id)));
    }

    @PostMapping
    @Operation(summary = "ایجاد رکورد")
    public ResponseEntity<CommissionCalendarDto> create(@RequestBody CommissionCalendarCreateDto request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(mapper.toDto(service.create(mapper.toEntity(request))));
    }

    @PutMapping("/{id}")
    @Operation(summary = "به‌روزرسانی")
    public ResponseEntity<CommissionCalendarDto> update(@PathVariable String id, @RequestBody CommissionCalendarUpdateDto request) {
        return ResponseEntity.ok(mapper.toDto(service.update(id, mapper.toEntity(request))));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "حذف رکورد")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
