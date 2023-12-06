package ru.solarev.taskmanagementapi.dto.mapper;

import java.util.List;

public interface Mappable<E, D> {
    List<D> toDto(List<E> entity);
    D toDto(E entity);
    List<E> toEntity(List<D> dto);
    E toEntity(D dto);
}
