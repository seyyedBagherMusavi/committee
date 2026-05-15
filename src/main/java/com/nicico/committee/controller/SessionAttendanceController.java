package com.nicico.committee.controller;

import com.nicico.committee.entities.CommissionEntities.SessionAttendance;
import com.nicico.committee.service.SessionAttendanceService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/sessionattendance")
@Tag(name = "SessionAttendance", description = "عملیات CRUD برای SessionAttendance")
public class SessionAttendanceController {

    private final SessionAttendanceService service;

    public SessionAttendanceController(SessionAttendanceService service) {
        this.service = service;
    }

    @GetMapping
    @Operation(summary = "دریافت لیست")
    public ResponseEntity<List<SessionAttendance>> findAll() {
        return ResponseEntity.ok(service.findAll());
    }

    @GetMapping("/{id}")
    @Operation(summary = "دریافت بر اساس شناسه")
    public ResponseEntity<SessionAttendance> findById(@PathVariable String id) {
        return ResponseEntity.ok(service.findById(id));
    }

    @PostMapping
    @Operation(summary = "ایجاد رکورد")
    public ResponseEntity<SessionAttendance> create(@RequestBody SessionAttendance request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.create(request));
    }

    @PutMapping("/{id}")
    @Operation(summary = "به‌روزرسانی")
    public ResponseEntity<SessionAttendance> update(@PathVariable String id, @RequestBody SessionAttendance request) {
        return ResponseEntity.ok(service.update(id, request));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "حذف رکورد")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
