package com.practice.apirest.models.daos;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.practice.apirest.models.entities.Cliente;

public interface IClienteDAO extends JpaRepository<Cliente, Long>{
	
	@Query(value = "SELECT * FROM cliente c WHERE c.dni=?1"
		  ,nativeQuery = true)
	public Cliente findByDni(String dni);
	
}
