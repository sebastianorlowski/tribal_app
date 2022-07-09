package core.dataloader.service;

import com.mysql.cj.x.protobuf.MysqlxCrud;
import core.dataloader.persistence.model.Ally;
import core.dataloader.persistence.model.Unit;
import core.dataloader.persistence.model.Village;
import core.dataloader.persistence.model.Player;
import core.dataloader.persistence.repository.AllyRepository;
import core.dataloader.persistence.repository.UnitRepository;
import core.dataloader.persistence.repository.VillageRepository;
import core.dataloader.persistence.repository.PlayerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class DataLoaderService {

    private final String VILLAGE_URL = "https://pl174.plemiona.pl/map/village.txt";
    private final String PLAYER_URL = "https://pl174.plemiona.pl/map/player.txt";
    private final String ALLY_URL = "https://pl174.plemiona.pl/map/ally.txt";

    private final VillageRepository villageRepository;
    private final PlayerRepository playerRepository;
    private final AllyRepository allyRepository;
    private final UnitRepository unitRepository;

    @Autowired
    public DataLoaderService(PlayerRepository playerRepository,
                             VillageRepository villageRepository,
                             AllyRepository allyRepository,
                             UnitRepository unitRepository) {
        this.playerRepository = playerRepository;
        this.villageRepository = villageRepository;
        this.allyRepository = allyRepository;
        this.unitRepository = unitRepository;
    }

    public void uploadTribalWarsData() {
        try {
            playerRepository.saveAllAndFlush(separateDataForPlayer());
            villageRepository.saveAllAndFlush(separateDataForVillage());
        } catch (Exception e) {
            throw new RuntimeException("Cannot load data from website");
        }
    }

    public void uploadAllyData() {
        try {
            allyRepository.saveAllAndFlush(separateDataForAlly());
        } catch (Exception e) {
            throw new  RuntimeException("Cannot load data from website");
        }
    }

    public void removeTribalWarsData() {
        villageRepository.deleteAllInBatch();
        playerRepository.deleteAllInBatch();
        allyRepository.deleteAllInBatch();
        unitRepository.deleteAllInBatch();
    }

    private List<Unit> uploadUnitData(String value) {
        List<String> dataList = Stream.of(value.split("\n")).skip(1).collect(Collectors.toList());
        List<Village> villages = villageRepository.findAll();
        return dataList.stream().map(data -> {
            String[] dataLine = data.split(",");
            Unit unit = new Unit();
            unit.setVillage(findVillageByCoordinate(villages, dataLine[0]));
            unit.setSpear(Integer.parseInt(dataLine[2]));
            unit.setSword(Integer.parseInt(dataLine[3]));
            unit.setAxe(Integer.parseInt(dataLine[4]));
            unit.setSpy(Integer.parseInt(dataLine[5]));
            unit.setLight(Integer.parseInt(dataLine[6]));
            unit.setHeavy(Integer.parseInt(dataLine[7]));
            unit.setRam(Integer.parseInt(dataLine[8]));
            unit.setCatapult(Integer.parseInt(dataLine[9]));
            unit.setKnight(Integer.parseInt(dataLine[10]));
            unit.setSnob(Integer.parseInt(dataLine[11]));
            return unit;
        }).collect(Collectors.toList());
    }

    public void loadUnitData(String value) {
        unitRepository.saveAllAndFlush(uploadUnitData(value));
    }

    private Village findVillageByCoordinate(List<Village> villages, String coord) {
        int x = Integer.parseInt(coord.substring(0, 3));
        int y = Integer.parseInt(coord.substring(4, 7));
        return villages.stream()
                .filter(village -> village.getX() == x && village.getY() == y)
                .findFirst()
                .orElse(null);
    }

    private List<String> loadData(String path) throws Exception {
        URL url = new URL(path);
        BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream()));
        String inputLine;
        List<String> data = new ArrayList<>();
        while ((inputLine = reader.readLine()) != null) {
            data.add(inputLine);
        }
        return data;
    }

    private List<Player> separateDataForPlayer() throws Exception {
        List<String> dataFromUrl = loadData(PLAYER_URL);
        List<Player> players = new ArrayList<>();
        List<Ally> allies = allyRepository.findAll();
        dataFromUrl.forEach(data -> {
            String[] dataArray = data.split(",");
            Player player = new Player();
            player.setUserId(dataArray[0]);
            player.setPlayerName(URLDecoder.decode(dataArray[1], StandardCharsets.UTF_8)
                    .replace("+", " "));
            Optional<Ally> ally = allies.stream()
                            .filter(a -> a.getAllyId().equals(Integer.parseInt(dataArray[2])))
                            .findFirst();
            ally.ifPresent(player::setAlly);
            players.add(player);
        });

        return players;
    }

    private List<Village> separateDataForVillage() throws Exception {
        List<String> dataFromUrl = loadData(VILLAGE_URL);
        List<Player> players = playerRepository.findAll();
        List<Village> villages = new ArrayList<>();
        dataFromUrl.forEach(data -> {
            String[] dataArray = data.split(",");
            Optional<Player> player = players.stream()
                    .filter(person -> person.getUserId().equals(dataArray[4]))
                    .findFirst();
            Village village = new Village();
            village.setVillageId(dataArray[0]);
            village.setX(Integer.parseInt(dataArray[2]));
            village.setY(Integer.parseInt(dataArray[3]));
            player.ifPresent(village::setPlayer);
            villages.add(village);
        });

        return villages;
    }

    private List<Ally> separateDataForAlly() throws Exception {
        List<String> dataFromUrl = loadData(ALLY_URL);
        List<Ally> allies = new ArrayList<>();
        dataFromUrl.forEach(data -> {
            String[] dataArray = data.split(",");
            Ally ally = new Ally();
            ally.setAllyId(Integer.parseInt(dataArray[0]));
            ally.setName(URLDecoder.decode(dataArray[1], StandardCharsets.UTF_8));
            ally.setTag(URLDecoder.decode(dataArray[2], StandardCharsets.UTF_8));
            ally.setMembers(Integer.parseInt(dataArray[3]));
            ally.setVillages(Integer.parseInt(dataArray[4]));
            ally.setPoints(Long.parseLong(dataArray[5]));
            allies.add(ally);
        });

        return allies;
    }
}
