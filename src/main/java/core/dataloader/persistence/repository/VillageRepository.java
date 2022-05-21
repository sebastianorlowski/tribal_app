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
}
