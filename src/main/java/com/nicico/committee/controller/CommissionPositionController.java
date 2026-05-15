package com.nicico.committee.controller;

import com.nicico.committee.dto.CommissionPositionCreateDto;
import com.nicico.committee.dto.CommissionPositionDto;
import com.nicico.committee.dto.CommissionPositionUpdateDto;
import com.nicico.committee.mapper.CommissionPositionMapper;
import com.nicico.committee.service.CommissionPositionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/commissionposition")
@Tag(name = "CommissionPosition", description = "عملیات CRUD برای CommissionPosition")
public class CommissionPositionController {

    private final CommissionPositionService service;
    private final CommissionPositionMapper mapper;

    public CommissionPositionController(CommissionPositionService service, CommissionPositionMapper mapper) {
        this.service = service;
        this.mapper = mapper;
    }

    @GetMapping
    @Operation(summary = "دریافت لیست")
    public ResponseEntity<List<CommissionPositionDto>> findAll() {
        return ResponseEntity.ok(service.findAll().stream().map(mapper::toDto).collect(Collectors.toList()));
    }

    @GetMapping("/{id}")
    @Operation(summary = "دریافت بر اساس شناسه")
    public ResponseEntity<CommissionPositionDto> findById(@PathVariable String id) {
        return ResponseEntity.ok(mapper.toDto(service.findById(id)));
    }

    @PostMapping
    @Operation(summary = "ایجاد رکورد")
    public ResponseEntity<CommissionPositionDto> create(@RequestBody CommissionPositionCreateDto request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(mapper.toDto(service.create(mapper.toEntity(request))));
    }

    @PutMapping("/{id}")
    @Operation(summary = "به‌روزرسانی")
    public ResponseEntity<CommissionPositionDto> update(@PathVariable String id, @RequestBody CommissionPositionUpdateDto request) {
        return ResponseEntity.ok(mapper.toDto(service.update(id, mapper.toEntity(request))));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "حذف رکورد")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
