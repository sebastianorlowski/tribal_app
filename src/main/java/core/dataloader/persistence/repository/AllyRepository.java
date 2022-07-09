package core.dataloader.persistence.repository;

import core.dataloader.persistence.model.Ally;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AllyRepository extends JpaRepository<Ally, Long> {

    List<Ally> findByTagIn(List<String> tags);

    Optional<Ally> findAllyByTag(String tag);
}
