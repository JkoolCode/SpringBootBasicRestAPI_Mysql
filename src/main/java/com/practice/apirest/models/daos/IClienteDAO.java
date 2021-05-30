package com.practice.apirest.models.daos;
import java.sql.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.practice.apirest.models.entities.Cliente;
import com.practice.apirest.models.entities.Region;

public interface IClienteDAO extends JpaRepository<Cliente, Long>{
	
	@Query(value = "SELECT * FROM cliente c WHERE c.dni=?1"
		  ,nativeQuery = true)
	public Cliente findByDni(String dni);
	
	
	@Query("FROM Region") //Jpql Query
	public List<Region> findAllRegiones();
	
	//Procedimientos almacenados
	@Query(value = "{ call stp_sel_ClientesByRegion(:region_id, :puntos) }"
			 , nativeQuery = true)
	public List<Cliente> getClientesByRgion(@Param("region_id") Long region_id
											  ,@Param("puntos") Long puntos);
	
}
