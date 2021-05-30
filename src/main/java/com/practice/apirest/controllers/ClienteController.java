package com.practice.apirest.controllers;
import java.lang.reflect.Field;
import java.sql.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.util.ReflectionUtils;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.google.common.net.MediaType;
import com.practice.apirest.models.dto.ClienteFindDTO;
import com.practice.apirest.models.entities.Cliente;
import com.practice.apirest.models.entities.Region;
import com.practice.apirest.models.services.IClienteService;

import springfox.documentation.annotations.ApiIgnore;

@RestController
@RequestMapping("/api")
public class ClienteController {
	@Autowired
	private IClienteService clienteService;
	
	@GetMapping("/clientes")
	public List<Cliente> getClientes(){
		return clienteService.findAll();
	}
	
	@GetMapping("/clientes/{id}")
	public ResponseEntity<?> getCliente(@PathVariable(required = true, name = "id") Long id){
		Map<String, Object> response = new HashMap<>();
		Cliente cliente = null;
		
		try {
			cliente = clienteService.findById(id);
		} catch (DataAccessException e) {
			response.put("mensaje", "Error al realizar consulta en la base de datos!");
			response.put("error", e.getMessage());
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		if(cliente == null) {
			response.put("mesaje", "No se encontro en la base de datos el cliente con ID: " + id);
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);	
		}
		
		return new ResponseEntity<Cliente>(cliente, HttpStatus.OK);
	}
	
	@Secured({"ROLE_USER", "ROLE_ADMIN"})
	@PostMapping("/clientes")
	public ResponseEntity<?> create(@Valid @RequestBody Cliente cliente){
		Map<String, Object> response = new HashMap<>();
		Cliente clienteNuevo = null;
		
		try {
			clienteNuevo = clienteService.save(cliente);
		} catch (DataAccessException e) {
			response.put("mensaje", "Error al registrar en la base de datos!");
			response.put("error", e.getMessage());
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		if(clienteNuevo == null) {
			response.put("mesaje", "No se pudo crear el nuevo cliente");
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_MODIFIED);	
		}
		
		return new ResponseEntity<Cliente>(clienteNuevo, HttpStatus.CREATED);
	}
	
	@Secured({"ROLE_USER", "ROLE_ADMIN"})
	@PutMapping("/clientes/{id}")
	public ResponseEntity<?> update(@RequestBody Cliente cliente
								   ,@PathVariable(required = true, name="id") Long id){
		Map<String, Object> response = new HashMap<>();
		Cliente clienteActual = clienteService.findById(id);
		Cliente clienteUpdated = null;
		
		if(clienteActual == null) {
			response.put("mesaje", "No se encontro en la base de datos el cliente con ID: " + id);
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);	
		}
		
		try {
			clienteActual.setSaldo(clienteActual.getSaldo() + cliente.getSaldo());
			clienteActual.setPuntos(clienteActual.getPuntos() + cliente.getPuntos());
			clienteActual.setRegion(cliente.getRegion());
			clienteUpdated = clienteService.save(clienteActual);
		} catch (DataAccessException e) {
			response.put("mensaje", "Error al modificar un cliente en la base de datos!");
			response.put("error", e.getMessage());
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		return new ResponseEntity<Cliente>(clienteUpdated, HttpStatus.CREATED);
	}
	
	@Secured({"ROLE_USER", "ROLE_ADMIN"})
	@PutMapping("/clientes/dni/{dni}")
	public ResponseEntity<?> abonarCliente(@RequestBody Cliente cliente
										 , @PathVariable(required=true, name="dni") String dni){
		Map<String, Object> response = new HashMap<>();
		Cliente clienteActual = clienteService.findByDni(dni);
		Cliente clienteUpdated = null;
		
		if(clienteActual == null) {
			response.put("mesaje", "No se encontro en la base de datos el cliente con DNI: " + dni);
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);	
		}
		
		try {
			clienteActual.setSaldo(clienteActual.getSaldo() + cliente.getSaldo());
			if(cliente.getSaldo() > 1000) 
				clienteActual.setPuntos(clienteActual.getPuntos() + 200);
			else
				clienteActual.setPuntos(clienteActual.getPuntos() + 50);
			
			clienteUpdated = clienteService.save(clienteActual);
		} catch (DataAccessException e) {
			response.put("mensaje", "Error al modificar un cliente en la base de datos!");
			response.put("error", e.getMessage());
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		return new ResponseEntity<Cliente>(clienteUpdated, HttpStatus.CREATED);
	}
	
	@Secured({"ROLE_USER", "ROLE_ADMIN"})
	@PatchMapping("/clientes/{id}")
	public ResponseEntity<?> actualizarDniNombre(@RequestBody Map<String, Object> fields
										 , @PathVariable(required=true, name="id") Long id){
		Map<String, Object> response = new HashMap<>();
		Cliente clienteActual = clienteService.findById(id);
		Cliente clienteUpdated = null;
		
		if(clienteActual == null) {
			response.put("mesaje", "No se encontro en la base de datos el cliente con ID: " + id);
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);	
		}
		
		try {
			fields.forEach((k, v) -> {
				Field field = ReflectionUtils.findField(Cliente.class, k);
				field.setAccessible(true);
				ReflectionUtils.setField(field, clienteActual, v);
			});
			
			clienteUpdated = clienteService.save(clienteActual);
		} catch (DataAccessException e) {
			response.put("mensaje", "Error al modificar un cliente en la base de datos!");
			response.put("error", e.getMessage());
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		return new ResponseEntity<Cliente>(clienteUpdated, HttpStatus.CREATED);
	}
	
	@Secured({"ROLE_USER", "ROLE_ADMIN"})
	@PatchMapping("/clientes/dni/{dni}")
	public ResponseEntity<?> actualizarRegion(@RequestBody Region region
										 , @PathVariable(required=true, name="dni") String dni){
		Map<String, Object> response = new HashMap<>();
		Cliente clienteActual = clienteService.findByDni(dni);
		Cliente clienteUpdated = null;
		
		if(clienteActual == null) {
			response.put("mesaje", "No se encontro en la base de datos el cliente con DNI: " + dni);
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);	
		}
		
		try {
			Region regionNueva = new Region();
			regionNueva.setId(region.getId());
			regionNueva.setNombre(region.getNombre());
			clienteActual.setRegion(regionNueva);
			
			clienteUpdated = clienteService.save(clienteActual);
		} catch (DataAccessException e) {
			response.put("mensaje", "Error al modificar un cliente en la base de datos!");
			response.put("error", e.getMessage());
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		return new ResponseEntity<Cliente>(clienteUpdated, HttpStatus.CREATED);
	}
	
	@Secured("ROLE_ADMIN")
	@DeleteMapping("/clientes/{id}")
	public ResponseEntity<?> delete(@PathVariable(required=true, name="id") Long id) {
		Map<String, Object> response = new HashMap<>();
		
		try {
			clienteService.delete(id);
		} catch (DataAccessException e) {
			response.put("mensaje", "Error al eliminar el cliente en la base de datos!");
			response.put("error", e.getMessage());
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		response.put("mensaje", "Se elimino el cliente con éxito!");
		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.OK); 
	}
	
	@GetMapping("/clientes/regiones")
	public List<Region> getRegiones(){
		return clienteService.findAllRegiones();
	}
	
	@GetMapping("/clientes/find")
	public ResponseEntity<?> getClientesByRgion(@Valid @RequestBody ClienteFindDTO clienteFindDTO){
		Map<String, Object> response = new HashMap<>();
		
		List<Cliente> clientes = null;
		try {
			clientes = clienteService.getClientesByRgion(clienteFindDTO);
		} catch (DataAccessException e) {
			response.put("mensaje", "Error al obtener clientes de la RegionID: " + clienteFindDTO.getRegion_id());
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
		}
		
		if(clientes == null) {
			response.put("mensaje", "No existen clientes con RegionID: " + clienteFindDTO.getRegion_id());
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
		}
		
		return new ResponseEntity<List<Cliente>>(clientes, HttpStatus.OK);
	}
	
}
	