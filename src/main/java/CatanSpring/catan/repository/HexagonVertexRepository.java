package CatanSpring.catan.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import CatanSpring.catan.model.HexagonVertex;

public interface HexagonVertexRepository extends JpaRepository<HexagonVertex, Long> {}
