package com.example.laburgueseriabackend.model.dao;

import com.example.laburgueseriabackend.model.entity.Empleado;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface EmpleadoDao  extends JpaRepository<Empleado, Integer> {
    //BUSCAR empleados por nombre
    @Query("SELECT e FROM Empleado e WHERE e.nombre LIKE %:nombre%")
    List<Empleado> findEmpleadosByNombre(String nombre);
    //BUSCAR POR DOCUMENTO
    @Query("SELECT e FROM Empleado e WHERE e.documento = :documento")
    Empleado findEmpleadosByDocumento(Long documento);

    //Cambiar estado activo o inactivo de un empleado
    @Query("UPDATE Empleado e SET e.estado = :estado WHERE e.id = :id")
    Empleado actualizarEstado(Boolean estado, Integer id);
}
