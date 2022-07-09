package core.dataloader.persistence.repository;

import core.dataloader.persistence.model.Unit;
import core.dataloader.persistence.model.UnitInfo;
import core.dataloader.persistence.model.Village;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UnitRepository extends JpaRepository<Unit, Long> {

    @Query("SELECT u FROM Unit u WHERE u.spear > 100 " +
            "AND u.village.x < ?1 " +
            "AND u.village.x > ?2")
    List<Unit> findUnitsByVillageCoord(Integer x, Integer y);

    @Query("SELECT u FROM Unit u " +
            "WHERE u.spear >= ?1 " +
            "AND u.sword >= ?2 " +
            "AND u.heavy >= ?3 " +
            "AND u.village.x < ?4")
    List<Unit> findVillagesByUnits(Integer spear,
                                   Integer sword,
                                   Integer heavy,
                                   Integer coordX);


    @Query("SELECT u FROM Unit u " +
            "WHERE u.spear >= ?1 " +
            "AND u.sword >= ?2 " +
            "AND u.heavy >= ?3 " +
            "AND u.village.x < ?4 " +
            "AND u.village.y > ?5")
    List<Unit> findVillagesBetweenPointsByUnits(Integer spear,
                                                Integer sword,
                                                Integer heavy,
                                                Integer coordX,
                                                Integer coordY);
}
