package CatanSpring.config;

import CatanSpring.repository.JugadorRepository;
import CatanSpring.repository.PartidaRepository;
import CatanSpring.service.JugadorService;
import CatanSpring.service.PartidaService;
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
    @Autowired
    private JugadorService service;

    @Autowired
    private JugadorRepository pRepository;

//    @Override
//    public void run(String... args) throws Exception {
//        if (pRepository.count() > 0) {
//            System.out.println("La BD ja té dades: no es carreguen els CSV");
//            return;
//        }
//        int n = service.carregarDades();
//        System.out.println("Videojoc carregats dels CSV: " + n);
//    }

}