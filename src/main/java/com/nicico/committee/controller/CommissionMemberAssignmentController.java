package com.nicico.committee.controller;

import com.nicico.committee.dto.CommissionMemberAssignmentCreateDto;
import com.nicico.committee.dto.CommissionMemberAssignmentDto;
import com.nicico.committee.dto.CommissionMemberAssignmentUpdateDto;
import com.nicico.committee.mapper.CommissionMemberAssignmentMapper;
import com.nicico.committee.service.CommissionMemberAssignmentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/commissionmemberassignment")
@Tag(name = "CommissionMemberAssignment", description = "عملیات CRUD برای CommissionMemberAssignment")
public class CommissionMemberAssignmentController {

    private final CommissionMemberAssignmentService service;
    private final CommissionMemberAssignmentMapper mapper;

    public CommissionMemberAssignmentController(CommissionMemberAssignmentService service, CommissionMemberAssignmentMapper mapper) {
        this.service = service;
        this.mapper = mapper;
    }

    @GetMapping
    @Operation(summary = "دریافت لیست")
    public ResponseEntity<List<CommissionMemberAssignmentDto>> findAll() {
        return ResponseEntity.ok(service.findAll().stream().map(mapper::toDto).collect(Collectors.toList()));
    }

    @GetMapping("/{id}")
    @Operation(summary = "دریافت بر اساس شناسه")
    public ResponseEntity<CommissionMemberAssignmentDto> findById(@PathVariable String id) {
        return ResponseEntity.ok(mapper.toDto(service.findById(id)));
    }

    @PostMapping
    @Operation(summary = "ایجاد رکورد")
    public ResponseEntity<CommissionMemberAssignmentDto> create(@RequestBody CommissionMemberAssignmentCreateDto request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(mapper.toDto(service.create(mapper.toEntity(request))));
    }

    @PutMapping("/{id}")
    @Operation(summary = "به‌روزرسانی")
    public ResponseEntity<CommissionMemberAssignmentDto> update(@PathVariable String id, @RequestBody CommissionMemberAssignmentUpdateDto request) {
        return ResponseEntity.ok(mapper.toDto(service.update(id, mapper.toEntity(request))));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "حذف رکورد")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
