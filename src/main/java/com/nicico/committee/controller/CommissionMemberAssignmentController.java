package com.nicico.committee.controller;

import com.nicico.committee.entities.CommissionEntities.CommissionMemberAssignment;
import com.nicico.committee.service.CommissionMemberAssignmentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/commissionmemberassignment")
@Tag(name = "CommissionMemberAssignment", description = "عملیات CRUD برای CommissionMemberAssignment")
public class CommissionMemberAssignmentController {

    private final CommissionMemberAssignmentService service;

    public CommissionMemberAssignmentController(CommissionMemberAssignmentService service) {
        this.service = service;
    }

    @GetMapping
    @Operation(summary = "دریافت لیست")
    public ResponseEntity<List<CommissionMemberAssignment>> findAll() {
        return ResponseEntity.ok(service.findAll());
    }

    @GetMapping("/{id}")
    @Operation(summary = "دریافت بر اساس شناسه")
    public ResponseEntity<CommissionMemberAssignment> findById(@PathVariable String id) {
        return ResponseEntity.ok(service.findById(id));
    }

    @PostMapping
    @Operation(summary = "ایجاد رکورد")
    public ResponseEntity<CommissionMemberAssignment> create(@RequestBody CommissionMemberAssignment request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.create(request));
    }

    @PutMapping("/{id}")
    @Operation(summary = "به‌روزرسانی")
    public ResponseEntity<CommissionMemberAssignment> update(@PathVariable String id, @RequestBody CommissionMemberAssignment request) {
        return ResponseEntity.ok(service.update(id, request));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "حذف رکورد")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
