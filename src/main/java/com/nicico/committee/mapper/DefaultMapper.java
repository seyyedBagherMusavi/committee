package com.nicico.committee.mapper;

public interface DefaultMapper<D, U, C, E> {
    D toDto(E entity);
    E toEntity(C createDto);
    E toEntity(U updateDto);
}
