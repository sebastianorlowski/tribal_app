package core.villagegenerator.api.mapper;

import core.dataloader.persistence.model.Player;
import core.dataloader.persistence.model.Village;
import core.dataloader.persistence.repository.PlayerRepository;
import core.villagegenerator.api.dto.VillageDto;
import core.villagegenerator.api.dto.VillagePlayerCountDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface VillageMapper {

    @Mapping(target = "coords", expression = "java(asCoords(village))")
    @Mapping(target = "playerName", source = "player.playerName")
    VillageDto villageDto(Village village);

    default List<VillageDto> listVillageDto(List<Village> villages) {
        return villages.stream().map(this::villageDto).collect(Collectors.toList());
    }

    default List<String> asCoordsList(List<Village> villagesList) {
        return villagesList.stream()
                .map(this::asCoords)
                .collect(Collectors.toList());
    }

    default String asCoords(Village village) {
        return String.format("%s|%s", village.getX(), village.getY());
    }

    @Mapping(target = "playerName", source = "player.playerName")
    @Mapping(target = "villageCount", source = "count")
    @Mapping(target = "coords", ignore = true)
    @Mapping(target = "players", ignore = true)
    VillagePlayerCountDto asPlayerCountDto(Player player, Integer count);

    default List<VillagePlayerCountDto> asPlayerVillageCountDto(Map<Player, Integer> players) {
        List<VillagePlayerCountDto> villagePlayerCountDtoList = new ArrayList<>();
        players.forEach(((player, integer) -> {
            villagePlayerCountDtoList.add(asPlayerCountDto(player, integer));
        }));
        return villagePlayerCountDtoList;
    }

    @Mapping(target = "playerName", ignore = true)
    @Mapping(target = "villageCount", ignore = true)
    VillagePlayerCountDto asVillagePlayerDto(String coords, List<String> players);

    default List<VillagePlayerCountDto> asVillagePlayerDto(Map<Village, List<Player>> villages) {
        List<VillagePlayerCountDto> villagePlayerCountDtoList = new ArrayList<>();
        villages.forEach((village, players) -> {
            villagePlayerCountDtoList.add(asVillagePlayerDto(village.getX() + "|" + village.getY(), asPlayers(players)));
        });

        return villagePlayerCountDtoList;
    }

    default List<String> asPlayers(List<Player> players) {
        List<String> playerNames = new ArrayList<>();
        players.forEach(player -> {
            playerNames.add(player.getPlayerName());
        });
        return playerNames;
    }

    @Mapping(target = "villageCount", ignore = true)
    @Mapping(target = "players", ignore = true)
    VillagePlayerCountDto asPlayerName(String coords, String playerName);

    default List<VillagePlayerCountDto> asPlayerVillageList(Map<Village, Player> playerToVillage) {
        List<VillagePlayerCountDto> villagePlayerCountDtoList = new ArrayList<>();
        playerToVillage.forEach(((village, player) -> {
            villagePlayerCountDtoList.add(asPlayerName(village.getX() + "|" + village.getY(), player.getPlayerName()));
        }));
        return villagePlayerCountDtoList;
    }
}
