package core.dataloader.persistence.repository;

import core.dataloader.persistence.model.Player;
import core.dataloader.persistence.model.Village;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VillageRepository extends JpaRepository<Village, Long> {

    List<Village> findVillageByPlayerId(Long id);
//    @Query("SELECT v FROM Village v WHERE v.player.playerName = ?1")
//    List<Village> findAllVillagesByPlayerName(String name);

    @Query("SELECT COUNT(v) FROM Village v WHERE v.x >= 500 AND v.player IS NOT NULL")
    Integer eastSideWithoutBarbarian();

    @Query("SELECT COUNT(v) FROM Village v WHERE v.x >= 500")
    Integer eastSideTotal();

    @Query("SELECT COUNT(v) FROM Village v WHERE v.x < 500 AND v.player IS NOT NULL")
    Integer westSideWithoutBarbarian();

    @Query("SELECT COUNT(v) FROM Village v WHERE v.x < 500")
    Integer westSideTotal();

//    @Query("SELECT v FROM village v WHERE ")
//    List<Village> findVillagesByCoords();
//    Integer villagesFromSpecificContinent(int x, int y, String )
}
