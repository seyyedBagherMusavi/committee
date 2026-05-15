package com.nicico.committee.controller;

import com.nicico.committee.entities.CommissionEntities.CommissionPeriodicitySettings;
import com.nicico.committee.service.CommissionPeriodicitySettingsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/commissionperiodicitysettings")
@Tag(name = "CommissionPeriodicitySettings", description = "عملیات CRUD برای CommissionPeriodicitySettings")
public class CommissionPeriodicitySettingsController {

    private final CommissionPeriodicitySettingsService service;

    public CommissionPeriodicitySettingsController(CommissionPeriodicitySettingsService service) {
        this.service = service;
    }

    @GetMapping
    @Operation(summary = "دریافت لیست")
    public ResponseEntity<List<CommissionPeriodicitySettings>> findAll() {
        return ResponseEntity.ok(service.findAll());
    }

    @GetMapping("/{id}")
    @Operation(summary = "دریافت بر اساس شناسه")
    public ResponseEntity<CommissionPeriodicitySettings> findById(@PathVariable String id) {
        return ResponseEntity.ok(service.findById(id));
    }

    @PostMapping
    @Operation(summary = "ایجاد رکورد")
    public ResponseEntity<CommissionPeriodicitySettings> create(@RequestBody CommissionPeriodicitySettings request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.create(request));
    }

    @PutMapping("/{id}")
    @Operation(summary = "به‌روزرسانی")
    public ResponseEntity<CommissionPeriodicitySettings> update(@PathVariable String id, @RequestBody CommissionPeriodicitySettings request) {
        return ResponseEntity.ok(service.update(id, request));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "حذف رکورد")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
