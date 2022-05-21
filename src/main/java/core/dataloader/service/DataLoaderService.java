package core.dataloader.service;

import com.mysql.cj.x.protobuf.MysqlxCrud;
import core.dataloader.persistence.model.Village;
import core.dataloader.persistence.model.Player;
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

@Service
public class DataLoaderService {

    private final String VILLAGE_URL = "https://pl174.plemiona.pl/map/village.txt";
    private final String PLAYER_URL = "https://pl174.plemiona.pl/map/player.txt";

    private static final List<Player> PLAYERS = new ArrayList<>();
    private static final List<Village> VILLAGES = new ArrayList<>();

    private final VillageRepository villageRepository;
    private final PlayerRepository playerRepository;

    @Autowired
    public DataLoaderService(PlayerRepository playerRepository, VillageRepository villageRepository) {
        this.playerRepository = playerRepository;
        this.villageRepository = villageRepository;
    }

    public void uploadTribalWarsData() {
        try {
            playerRepository.saveAllAndFlush(separateDataForPlayer());
            villageRepository.saveAllAndFlush(separateDataForVillage());
        } catch (Exception e) {
            throw new RuntimeException("Cannot load data from website");
        }
    }

    public void removeTribalWarsData() {
        playerRepository.deleteAll();
        villageRepository.deleteAll();
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

    protected List<Player> separateDataForPlayer() throws Exception {
        List<String> dataFromUrl = loadData(PLAYER_URL);
        List<Player> players = new ArrayList<>();
        dataFromUrl.forEach(data -> {
            String[] dataArray = data.split(",");
            Player player = new Player();
            player.setUserId(dataArray[0]);
            player.setPlayerName(URLDecoder.decode(dataArray[1], StandardCharsets.UTF_8)
                    .replace("+", " "));
            players.add(player);
        });

        return players;
    }

    protected List<Village> separateDataForVillage() throws Exception {
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
            village.setX(dataArray[2]);
            village.setY(dataArray[3]);
            player.ifPresent(village::setPlayer);
            villages.add(village);
        });

        return villages;
    }
}
