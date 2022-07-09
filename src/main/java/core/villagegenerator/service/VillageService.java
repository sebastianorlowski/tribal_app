package core.villagegenerator.service;

import core.dataloader.persistence.model.Ally;
import core.dataloader.persistence.model.Player;
import core.dataloader.persistence.model.Village;
import core.dataloader.persistence.repository.PlayerRepository;
import core.dataloader.persistence.repository.VillageRepository;
import core.dataloader.service.CalculateDistance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class VillageService {

    private final VillageRepository villageRepository;
    private final PlayerRepository playerRepository;
    private final CalculateDistance calculateDistance;

    @Autowired
    public VillageService(VillageRepository villageRepository,
                          PlayerRepository playerRepository,
                          CalculateDistance calculateDistance) {
        this.villageRepository = villageRepository;
        this.playerRepository = playerRepository;
        this.calculateDistance = calculateDistance;
    }

    public List<Village> findPlayersVillages(List<String> players) {
        List<Player> findPlayers = playerRepository.findPlayersByPlayerNameIn(players);
        List<Village> villages = new ArrayList<>();
        findPlayers.forEach(player ->
            villages.addAll(villageRepository.findVillageByPlayerId(player.getId())));

        return villages;
    }

    public List<Village> findPlayerVillages(String name) {
        return playerRepository.findPlayerByPlayerName(name)
                .map(player -> villageRepository.findVillageByPlayerId(player.getId()))
                .orElse(null);
    }

    public Map<Player, Integer> findVillagesAroundVillage(Integer time, String coords, Integer unitTime) {
        return calculateVillagesByTime(villageRepository.findAll(), coords, unitTime, time);
    }

    public Map<Village, List<Player>> generatePlayersAroundVillages(String playerName, Integer time,
                                                                    Integer unitTime, Integer countOfVillage) {
        List<Village> villages = villageRepository.findAll();
        List<Village> villagesFromPlayer = new ArrayList<>();

        villages.forEach(village -> {
            if (village.getPlayer() != null) {
                if (playerName.equals(village.getPlayer().getPlayerName())) {
                    villagesFromPlayer.add(village);
                }
            }
        });

        Map<Village, List<Player>> playersToVillage = new HashMap<>();
        villagesFromPlayer.forEach(village -> {
            playersToVillage.put(village,
                    retrievePlayersNearVillage(playerName,
                            calculateVillagesByTime(villages, village.getX() + "|" +
                                    village.getY(), unitTime, time), countOfVillage));
        });
        return playersToVillage;
    }


    public Map<Village, Player> generateAttackerToVillage(String playerName, Integer time, Integer unitTime,
                                                          Integer countOfVillage) {
        Map<Village, List<Player>> playersAroundVillage =
                generatePlayersAroundVillages(playerName, time, unitTime, countOfVillage);
        Map<Village, Player> playerToVillage = new HashMap<>();
        playersAroundVillage.forEach((village, players) -> {
            if (players.size() == 0) {
                playerToVillage.put(village, null);
            } else {
                Random random = new Random();
                playerToVillage.put(village, players.get(random.nextInt(players.size())));
            }
        });
        return playerToVillage;
    }

    public List<Village> villagesByCoords(Ally ally, Integer x) {
        List<Village> villages = villagesByPlayers(ally, x);
        return villages.stream()
                .filter(village -> village.getX() < x)
                .collect(Collectors.toList());
    }

    /// Ally -> Get player By ally
    private List<Village> villagesByPlayers(Ally ally, Integer x) {
        List<Player> players = playerRepository.findPlayersByAlly(ally);
        List<Village> villages = new ArrayList<>();
        players.forEach(player -> villages.addAll(player.getVillages()));
        return villages;
    }

    private List<Player> retrievePlayersNearVillage(String playerName, Map<Player, Integer> villagesWithCount, Integer countOfVillage) {
        List<Player> players = new ArrayList<>();
        villagesWithCount.forEach(((player, integer) -> {
            if (integer >= countOfVillage && !player.getPlayerName().equals(playerName)) {
                players.add(player);
            }
        }));
        return players;
    }

    private Map<Player, Integer> calculateVillagesByTime(List<Village> villages, String coords,
                                                         Integer unitTime, Integer time) {
        Map<Village, Integer> villageTimes = new HashMap<>();
        villages.forEach(village -> {
            villageTimes.put(village, calculateDistance.calculate(coords,
                    village.getX() + "|" + village.getY(), unitTime));
        });
        return retrieveVillagesInProvidedDistance(villageTimes, time);
    }

    private Map<Player, Integer> retrieveVillagesInProvidedDistance(Map<Village, Integer> villagesTime, Integer time) {
        List<Village> villages = new ArrayList<>();
        villagesTime.forEach((village, integer) -> {
            if (integer < time * 60) {
                villages.add(village);
            }
        });
        return retrievePlayersAroundVillage(villages);
    }

    private Map<Player, Integer> retrievePlayersAroundVillage(List<Village> villages) {
        List<Player> players = new ArrayList<>();
        Set<Player> nonDuplicatedPlayers = new HashSet<>();
        villages.forEach(village -> {
            players.add(village.getPlayer());
            nonDuplicatedPlayers.add(village.getPlayer());
        });
        return playerWithVillages(players, nonDuplicatedPlayers);
    }

    private Map<Player, Integer> playerWithVillages(List<Player> players, Set<Player> nonDuplicatedPlayers) {
        Map<Player, Integer> playerFrequency = new HashMap<>();
        nonDuplicatedPlayers.remove(null);
        nonDuplicatedPlayers.forEach(player -> {
            int value = Collections.frequency(players, player);
            playerFrequency.put(player, value);
        });
        return playerFrequency;
    }
}
