package CatanSpring.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import CatanSpring.repository.JugadorRepository;



@Service
public class JugadorService {

	@Autowired 
	private JugadorRepository repo;
	
	//public JugadorService(JugadorRepository repo) {
	//	this.repo = repo;
	// } --> Una alternativa
	
	
	public List</*Classe*/> lista(){
		return repo.findAll();
	}
	
	
	public Optional</*nom_classe*/> buscar(Long id){
		return repo.findById(id);
	}
	
	
}
