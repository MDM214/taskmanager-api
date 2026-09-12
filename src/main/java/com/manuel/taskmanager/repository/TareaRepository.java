package com.manuel.taskmanager.repository;

import com.manuel.taskmanager.entity.Tarea;

import org.springframework.data.jpa.repository.JpaRepository;

public interface TareaRepository extends JpaRepository<Tarea, Long> {

}
