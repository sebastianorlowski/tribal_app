package core.villagegenerator.api;

import core.dataloader.persistence.model.Ally;
import core.dataloader.persistence.model.Player;
import core.dataloader.persistence.model.Village;
import core.villagegenerator.api.dto.VillagePlayerCountDto;
import core.villagegenerator.api.dto.VillageDto;
import core.villagegenerator.api.mapper.VillageMapper;
import core.villagegenerator.service.AllyService;
import core.villagegenerator.service.VillageService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/villages")
@Api(tags = "Villages")
public class VillageController {

    private final VillageService villageService;
    private final VillageMapper villageMapper;
    private final AllyService allyService;

    @Autowired
    public VillageController(VillageService villageService,
                             VillageMapper villageMapper,
                             AllyService allyService) {
        this.villageService = villageService;
        this.villageMapper = villageMapper;
        this.allyService = allyService;
    }

    @GetMapping(value = "/list", produces = MediaType.TEXT_PLAIN_VALUE)
    private String retrieveVillagesAsTextFile(@RequestParam String playerName) {
        List<Village> villages = villageService.findPlayerVillages(playerName);
        List<VillageDto> villageDto = villageMapper.listVillageDto(villages);
        return villageDto.toString().replaceAll("[\\]\\[\\,\\ ]", "");
    }

    @PostMapping(value = "/list/completed", produces = MediaType.TEXT_PLAIN_VALUE)
    private String retrieveVillagesAsTextFileFromPlayers(@RequestBody String players) {
        List<String> listPlayers = List.of(players.split("\n"));
        List<Village> villages = villageService.findPlayersVillages(listPlayers);
        List<VillageDto> villageDto = villageMapper.listVillageDto(villages);
        return villageDto.toString().replaceAll("[\\]\\[\\,\\ ]", "");
    }

    /*** Provided time is count as minutes. ***/
    @GetMapping(value = "/aroundVillage", produces = MediaType.TEXT_PLAIN_VALUE)
    private String retrievePlayersAroundVillage(@RequestParam Integer time,
                                                @RequestParam String coords,
                                                @RequestParam Integer unitTime) {
        Map<Player, Integer> villages = villageService.findVillagesAroundVillage(time, coords, unitTime);
        List<VillagePlayerCountDto> villagePlayerCountDtoList = villageMapper.asPlayerVillageCountDto(villages);

        return villagePlayerCountDtoList.toString().replaceAll("[\\]\\[\\,]", "");
    }

    @GetMapping(value = "/generate_attackers", produces = MediaType.TEXT_PLAIN_VALUE)
    private String retrieveAttackers(@RequestParam String playerName,
                                     @RequestParam Integer time,
                                     @RequestParam Integer unitTime,
                                     @RequestParam Integer countOfVillage) {
        Map<Village, List<Player>> playersToVillage = villageService.generatePlayersAroundVillages(
                playerName, time, unitTime, countOfVillage);
        List<VillagePlayerCountDto> villagePlayerCountDtoList = villageMapper.asVillagePlayerDto(playersToVillage);
        List<String> villagePlayerList = new ArrayList<>();
        villagePlayerCountDtoList.forEach(villagePlayerCountDto -> {
            villagePlayerList.add(villagePlayerCountDto.toStringVillagePlayers());
        });
        return villagePlayerList.toString().replace(",", "");
    }

    /*** Allies are seperated by semicolon ... soon ***/
    @GetMapping(value = "/generate_attackers/complete", produces = MediaType.TEXT_PLAIN_VALUE)
    private String retrieveCompleteAttackers(@RequestParam String playerName,
                                             @RequestParam Integer time,
                                             @RequestParam Integer unitTime,
                                             @RequestParam Integer countOfVillage,
                                             @RequestParam String allies) {
        Map<Village, Player> playerToVillage = villageService
                .generateAttackerToVillage(playerName, time, unitTime, countOfVillage);
        List<VillagePlayerCountDto> villagePlayerCountDtoList = villageMapper.asPlayerVillageList(playerToVillage);
        List<String> playerToVillageList = new ArrayList<>();
        villagePlayerCountDtoList.forEach(villagePlayerCountDto -> {
            playerToVillageList.add(villagePlayerCountDto.toStringVillagePlayerName());
        });
        return playerToVillageList.toString().replace(",", "");
    }

    /***  ***/

    @GetMapping(value = "/generate_villages/", produces = MediaType.TEXT_PLAIN_VALUE)
    private String retrieveVillagesFromAlly(@RequestParam String allyTag,
                                            @RequestParam Integer x) {
        Ally ally = allyService.getAlly(allyTag);
        List<Village> villages = villageService.villagesByCoords(ally, x);
        List<String> villageList = villageMapper.asCoordsList(villages);
        StringBuffer stringBuffer = new StringBuffer();
        villageList.forEach(village -> stringBuffer.append(village).append("\n"));
        return stringBuffer.toString();
//        return villageList.toString().replaceAll("[\\]\\[\\,]", "");

    }
}
