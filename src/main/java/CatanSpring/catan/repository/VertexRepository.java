package CatanSpring.catan.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import CatanSpring.catan.model.Vertex;

public interface VertexRepository extends JpaRepository<Vertex, Long> {}
