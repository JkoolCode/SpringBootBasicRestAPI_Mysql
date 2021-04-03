package com.practice.apirest.controllers;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.practice.apirest.models.entities.Cliente;
import com.practice.apirest.models.services.IClienteService;

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
			clienteUpdated = clienteService.save(clienteActual);
		} catch (DataAccessException e) {
			response.put("mensaje", "Error al modificar un cliente en la base de datos!");
			response.put("error", e.getMessage());
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		return new ResponseEntity<Cliente>(clienteUpdated, HttpStatus.CREATED);
	}
	
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
	
}
	