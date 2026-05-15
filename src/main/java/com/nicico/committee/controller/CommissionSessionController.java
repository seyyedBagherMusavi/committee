package com.nicico.committee.controller;

import com.nicico.committee.dto.CommissionSessionCreateDto;
import com.nicico.committee.dto.CommissionSessionDto;
import com.nicico.committee.dto.CommissionSessionUpdateDto;
import com.nicico.committee.mapper.CommissionSessionMapper;
import com.nicico.committee.service.CommissionSessionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/commissionsession")
@Tag(name = "CommissionSession", description = "عملیات CRUD برای CommissionSession")
public class CommissionSessionController {

    private final CommissionSessionService service;
    private final CommissionSessionMapper mapper;

    public CommissionSessionController(CommissionSessionService service, CommissionSessionMapper mapper) {
        this.service = service;
        this.mapper = mapper;
    }

    @GetMapping
    @Operation(summary = "دریافت لیست")
    public ResponseEntity<List<CommissionSessionDto>> findAll() {
        return ResponseEntity.ok(service.findAll().stream().map(mapper::toDto).collect(Collectors.toList()));
    }

    @GetMapping("/{id}")
    @Operation(summary = "دریافت بر اساس شناسه")
    public ResponseEntity<CommissionSessionDto> findById(@PathVariable String id) {
        return ResponseEntity.ok(mapper.toDto(service.findById(id)));
    }

    @PostMapping
    @Operation(summary = "ایجاد رکورد")
    public ResponseEntity<CommissionSessionDto> create(@RequestBody CommissionSessionCreateDto request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(mapper.toDto(service.create(mapper.toEntity(request))));
    }

    @PutMapping("/{id}")
    @Operation(summary = "به‌روزرسانی")
    public ResponseEntity<CommissionSessionDto> update(@PathVariable String id, @RequestBody CommissionSessionUpdateDto request) {
        return ResponseEntity.ok(mapper.toDto(service.update(id, mapper.toEntity(request))));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "حذف رکورد")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
