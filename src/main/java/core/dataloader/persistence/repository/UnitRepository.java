package core.dataloader.persistence.repository;

import core.dataloader.persistence.model.UnitInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UnitRepository extends JpaRepository<UnitInfo, Long> {

}
