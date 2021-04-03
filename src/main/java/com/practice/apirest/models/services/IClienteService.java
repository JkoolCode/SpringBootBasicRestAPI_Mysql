package com.practice.apirest.models.services;

import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;
import com.practice.apirest.models.entities.Cliente;

public interface IClienteService {
	
	public List<Cliente> findAll();
	
	public Page<Cliente> findAll(Pageable pageable); //para la paginacion
	
	public Cliente findById(Long id);
	
	public Cliente findByDni(String dni);
	
	public Cliente save(Cliente cliente);
	
	public void delete(Long id);
	
}
