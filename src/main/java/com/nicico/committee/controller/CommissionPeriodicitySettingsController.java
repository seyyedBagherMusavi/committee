package com.nicico.committee.controller;

import com.nicico.committee.dto.CommissionPeriodicitySettingsCreateDto;
import com.nicico.committee.dto.CommissionPeriodicitySettingsDto;
import com.nicico.committee.dto.CommissionPeriodicitySettingsUpdateDto;
import com.nicico.committee.mapper.CommissionPeriodicitySettingsMapper;
import com.nicico.committee.service.CommissionPeriodicitySettingsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/commissionperiodicitysettings")
@Tag(name = "CommissionPeriodicitySettings", description = "عملیات CRUD برای CommissionPeriodicitySettings")
public class CommissionPeriodicitySettingsController {

    private final CommissionPeriodicitySettingsService service;
    private final CommissionPeriodicitySettingsMapper mapper;

    public CommissionPeriodicitySettingsController(CommissionPeriodicitySettingsService service, CommissionPeriodicitySettingsMapper mapper) {
        this.service = service;
        this.mapper = mapper;
    }

    @GetMapping
    @Operation(summary = "دریافت لیست")
    public ResponseEntity<List<CommissionPeriodicitySettingsDto>> findAll() {
        return ResponseEntity.ok(service.findAll().stream().map(mapper::toDto).collect(Collectors.toList()));
    }

    @GetMapping("/{id}")
    @Operation(summary = "دریافت بر اساس شناسه")
    public ResponseEntity<CommissionPeriodicitySettingsDto> findById(@PathVariable String id) {
        return ResponseEntity.ok(mapper.toDto(service.findById(id)));
    }

    @PostMapping
    @Operation(summary = "ایجاد رکورد")
    public ResponseEntity<CommissionPeriodicitySettingsDto> create(@RequestBody CommissionPeriodicitySettingsCreateDto request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(mapper.toDto(service.create(mapper.toEntity(request))));
    }

    @PutMapping("/{id}")
    @Operation(summary = "به‌روزرسانی")
    public ResponseEntity<CommissionPeriodicitySettingsDto> update(@PathVariable String id, @RequestBody CommissionPeriodicitySettingsUpdateDto request) {
        return ResponseEntity.ok(mapper.toDto(service.update(id, mapper.toEntity(request))));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "حذف رکورد")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
