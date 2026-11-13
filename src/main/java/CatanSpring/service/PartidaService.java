package CatanSpring.service;

import CatanSpring.repository.PartidaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


@Service
public class PartidaService {

	@Autowired 
	private PartidaRepository repo;
	
	//public PartidaService(PartidaRepository repo) {
	//	this.repo = repo;
	// } --> Una alternativa
	
	
	public List</*Classe*/> lista(){
		return repo.findAll();
	}
	
	
	public Optional</*nom_classe*/> buscar(Long id){
		return repo.findById(id);
	}
	
	
}
