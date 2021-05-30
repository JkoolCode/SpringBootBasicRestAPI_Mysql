package com.practice.apirest.models.services;
import java.sql.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.practice.apirest.models.daos.IClienteDAO;
import com.practice.apirest.models.dto.ClienteFindDTO;
import com.practice.apirest.models.entities.Cliente;
import com.practice.apirest.models.entities.Region;

@Service
public class ClienteServiceImpl implements IClienteService {
	@Autowired //para aplicar la inyeccion de dependencia
	private  IClienteDAO clienteDao;
	
	@Override
	@Transactional(readOnly = true)
	public List<Cliente> findAll() {
		return (List<Cliente>) clienteDao.findAll();
	}

	@Override
	@Transactional(readOnly = true)
	public Page<Cliente> findAll(Pageable pageable) {
		return clienteDao.findAll(pageable);
	}

	@Override
	@Transactional(readOnly = true)
	public Cliente findById(Long id) {
		return clienteDao.findById(id).orElse(null);
	}
	
	@Override
	@Transactional(readOnly = true)
	public Cliente findByDni(String dni) {
		return clienteDao.findByDni(dni);
	}

	@Override
	@Transactional //por default readOnly es false
	public Cliente save(Cliente cliente) {
		return clienteDao.save(cliente);
	}
	
	@Override
	@Transactional //por default readOnly es false
	public void delete(Long id) {
		clienteDao.deleteById(id);
	}

	@Override
	@Transactional(readOnly = true)
	public List<Region> findAllRegiones() {
		return clienteDao.findAllRegiones();
	}

	@Override
	@Transactional(readOnly = true)
	public List<Cliente> getClientesByRgion(ClienteFindDTO clienteFindDTO) {
		return clienteDao.getClientesByRgion(clienteFindDTO.getRegion_id(), clienteFindDTO.getPuntos());
	}
	
}
