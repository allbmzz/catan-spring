package CatanSpring.catan.config;

import CatanSpring.catan.repository.*;
import CatanSpring.catan.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class CarregaInicial implements CommandLineRunner {
    //Aqui va la funcó que crida als csvs
    @Autowired
    private PartidaService service;

    @Autowired
    private PartidaRepository pRepository;

	@Override
	public void run(String... args) throws Exception {
		//Mira si la BBDD ja té registres, si ja en té no carrega els CSVs
		if (pRepository.count() > 0) {
			System.out.println("La BD ja té dades: no es carreguen els CSV");
			return;
		}
		//Comprovar com cal afegir els elements del CSV, el taulell s'hauria de carregar amb la partida ja creada o durant la creació, no abans.
		//int n = service.carregarDades();
		//System.out.println("Taulell carregat dels CSV: " + n);
		
	}
}