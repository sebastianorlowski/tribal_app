package core.unitgenerator.service;

import core.dataloader.persistence.model.Player;
import core.dataloader.persistence.model.Unit;
import core.dataloader.persistence.model.Village;
import core.dataloader.persistence.repository.PlayerRepository;
import core.dataloader.persistence.repository.UnitRepository;
import core.dataloader.persistence.repository.VillageRepository;
import core.unitgenerator.api.UnitInformation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class UnitService {

    private final UnitRepository unitRepository;
    private final VillageRepository villageRepository;
    private final PlayerRepository playerRepository;

    private final Integer UNITS = 20000;

    @Autowired
    public UnitService(UnitRepository unitRepository, VillageRepository villageRepository, PlayerRepository playerRepository) {
        this.unitRepository = unitRepository;
        this.villageRepository = villageRepository;
        this.playerRepository = playerRepository;
    }

    public Map<String, Integer> retrieveInformationAboutArmy(Integer spear, Integer sword, Integer heavy, Integer coordX) {
        List<Unit> units = unitRepository.findVillagesByUnits(spear, sword, heavy, coordX);
        List<Village> villages = units.stream()
                .map(Unit::getVillage)
                .collect(Collectors.toList());
        Set<Player> players = new HashSet<>();
        villages.forEach(village -> players.add(village.getPlayer()));
        Map<Player, List<Unit>> unitInformationMap = new HashMap<>();
        players.forEach(player -> {
                    List<Unit> units1 = villages.stream()
                            .filter(village -> village.getPlayer().equals(player))
                            .collect(Collectors.toList())
                            .stream()
                            .map(Village::getUnit)
                            .collect(Collectors.toList());
                    unitInformationMap.put(player, units1);
                }
        );
        Map<String, Integer> unitCalculation = new HashMap<>();
        unitInformationMap.forEach((key, value1) -> {
            String playerName = key.getPlayerName();
            Integer value = (value1.stream().mapToInt(Unit::getSword).sum() +
                    value1.stream().mapToInt(Unit::getSpear).sum() +
                    (value1.stream().mapToInt(Unit::getHeavy).sum() * 3)) / 200;
            unitCalculation.put(playerName, value);
        });

        return unitCalculation;
    }

    public Map<String, Integer> retrievePlayersAbleToProduceArmy(Integer spear, Integer sword, Integer heavy, Integer coordX) {
        List<Unit> units = unitRepository.findVillagesByUnits(spear, sword, heavy, coordX);
        List<Village> villages = units.stream()
                .map(Unit::getVillage)
                .collect(Collectors.toList());
        Set<Player> players = new HashSet<>();
        villages.forEach(village -> players.add(village.getPlayer()));
        Map<Player, List<Unit>> unitInformationMap = new HashMap<>();
        players.forEach(player -> {
                    List<Unit> units1 = villages.stream()
                            .filter(village -> village.getPlayer().equals(player))
                            .collect(Collectors.toList())
                            .stream()
                            .map(Village::getUnit)
                            .collect(Collectors.toList());
                    unitInformationMap.put(player, units1);
                }
        );
        Map<String, Integer> unitCalculation = new HashMap<>();
        unitInformationMap.forEach((key, value1) -> {
            String playerName = key.getPlayerName();
            Integer value = (value1.stream().mapToInt(Unit::getSword).sum() +
                    value1.stream().mapToInt(Unit::getSpear).sum() +
                    (value1.stream().mapToInt(Unit::getHeavy).sum() * 3)) / 200;
            unitCalculation.put(playerName, value);
        });

        Map<String, Integer> unitLeftCalculation = new HashMap<>();
        unitInformationMap.forEach((key, value1) -> {
            String playerName = key.getPlayerName();
            Integer packages = (value1.stream().mapToInt(Unit::getSword).sum() +
                    value1.stream().mapToInt(Unit::getSpear).sum() +
                    (value1.stream().mapToInt(Unit::getHeavy).sum() * 3)) / 200;
            Integer value = (((value1.size() * UNITS) / 200) - packages);
            unitLeftCalculation.put(playerName, value);
        });

        return unitLeftCalculation;
    }

    public Integer retrieveInformationAboutPackages(Integer coordX, Integer coordY) {
        List<Unit> units = unitRepository.findUnitsByVillageCoord(coordX, coordY);
        Integer spearSword = units.stream().mapToInt(Unit::getSpear).sum() +
                units.stream().mapToInt(Unit::getSword).sum();
        Integer heavy = units.stream().mapToInt(Unit::getHeavy).sum() * 3;

        return (spearSword + heavy) / 200;
    }
}
