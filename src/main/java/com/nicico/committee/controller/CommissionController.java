package com.nicico.committee.controller;

import com.nicico.committee.dto.CommissionCreateDto;
import com.nicico.committee.dto.CommissionDto;
import com.nicico.committee.dto.CommissionUpdateDto;
import com.nicico.committee.mapper.CommissionMapper;
import com.nicico.committee.service.CommissionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/commission")
@Tag(name = "Commission", description = "عملیات CRUD برای Commission")
public class CommissionController {

    private final CommissionService service;
    private final CommissionMapper mapper;

    public CommissionController(CommissionService service, CommissionMapper mapper) {
        this.service = service;
        this.mapper = mapper;
    }

    @GetMapping
    @Operation(summary = "دریافت لیست")
    public ResponseEntity<List<CommissionDto>> findAll() {
        return ResponseEntity.ok(service.findAll().stream().map(mapper::toDto).collect(Collectors.toList()));
    }

    @GetMapping("/{id}")
    @Operation(summary = "دریافت بر اساس شناسه")
    public ResponseEntity<CommissionDto> findById(@PathVariable String id) {
        return ResponseEntity.ok(mapper.toDto(service.findById(id)));
    }

    @PostMapping
    @Operation(summary = "ایجاد رکورد")
    public ResponseEntity<CommissionDto> create(@RequestBody CommissionCreateDto request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(mapper.toDto(service.create(mapper.toEntity(request))));
    }

    @PutMapping("/{id}")
    @Operation(summary = "به‌روزرسانی")
    public ResponseEntity<CommissionDto> update(@PathVariable String id, @RequestBody CommissionUpdateDto request) {
        return ResponseEntity.ok(mapper.toDto(service.update(id, mapper.toEntity(request))));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "حذف رکورد")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
