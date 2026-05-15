package com.nicico.committee.controller;

import com.nicico.committee.dto.SessionAttendanceCreateDto;
import com.nicico.committee.dto.SessionAttendanceDto;
import com.nicico.committee.dto.SessionAttendanceUpdateDto;
import com.nicico.committee.mapper.SessionAttendanceMapper;
import com.nicico.committee.service.SessionAttendanceService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/sessionattendance")
@Tag(name = "SessionAttendance", description = "عملیات CRUD برای SessionAttendance")
public class SessionAttendanceController {

    private final SessionAttendanceService service;
    private final SessionAttendanceMapper mapper;

    public SessionAttendanceController(SessionAttendanceService service, SessionAttendanceMapper mapper) {
        this.service = service;
        this.mapper = mapper;
    }

    @GetMapping
    @Operation(summary = "دریافت لیست")
    public ResponseEntity<List<SessionAttendanceDto>> findAll() {
        return ResponseEntity.ok(service.findAll().stream().map(mapper::toDto).collect(Collectors.toList()));
    }

    @GetMapping("/{id}")
    @Operation(summary = "دریافت بر اساس شناسه")
    public ResponseEntity<SessionAttendanceDto> findById(@PathVariable String id) {
        return ResponseEntity.ok(mapper.toDto(service.findById(id)));
    }

    @PostMapping
    @Operation(summary = "ایجاد رکورد")
    public ResponseEntity<SessionAttendanceDto> create(@RequestBody SessionAttendanceCreateDto request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(mapper.toDto(service.create(mapper.toEntity(request))));
    }

    @PutMapping("/{id}")
    @Operation(summary = "به‌روزرسانی")
    public ResponseEntity<SessionAttendanceDto> update(@PathVariable String id, @RequestBody SessionAttendanceUpdateDto request) {
        return ResponseEntity.ok(mapper.toDto(service.update(id, mapper.toEntity(request))));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "حذف رکورد")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
