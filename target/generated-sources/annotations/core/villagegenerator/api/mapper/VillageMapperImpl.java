package core.villagegenerator.api.mapper;

import core.dataloader.persistence.model.Player;
import core.dataloader.persistence.model.Village;
import core.villagegenerator.api.dto.VillageDto;
import core.villagegenerator.api.dto.VillagePlayerCountDto;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2022-06-22T21:21:46+0200",
    comments = "version: 1.4.2.Final, compiler: javac, environment: Java 11.0.15 (Private Build)"
)
@Component
public class VillageMapperImpl implements VillageMapper {

    @Override
    public VillageDto villageDto(Village village) {
        if ( village == null ) {
            return null;
        }

        VillageDto villageDto = new VillageDto();

        villageDto.setPlayerName( villagePlayerPlayerName( village ) );

        villageDto.setCoords( asCoords(village) );

        return villageDto;
    }

    @Override
    public VillagePlayerCountDto asPlayerCountDto(Player player, Integer count) {
        if ( player == null && count == null ) {
            return null;
        }

        VillagePlayerCountDto villagePlayerCountDto = new VillagePlayerCountDto();

        if ( player != null ) {
            villagePlayerCountDto.setPlayerName( player.getPlayerName() );
        }
        if ( count != null ) {
            villagePlayerCountDto.setVillageCount( count );
        }

        return villagePlayerCountDto;
    }

    @Override
    public VillagePlayerCountDto asVillagePlayerDto(String coords, List<String> players) {
        if ( coords == null && players == null ) {
            return null;
        }

        VillagePlayerCountDto villagePlayerCountDto = new VillagePlayerCountDto();

        if ( coords != null ) {
            villagePlayerCountDto.setCoords( coords );
        }
        if ( players != null ) {
            List<String> list = players;
            if ( list != null ) {
                villagePlayerCountDto.setPlayers( new ArrayList<String>( list ) );
            }
        }

        return villagePlayerCountDto;
    }

    @Override
    public VillagePlayerCountDto asPlayerName(String coords, String playerName) {
        if ( coords == null && playerName == null ) {
            return null;
        }

        VillagePlayerCountDto villagePlayerCountDto = new VillagePlayerCountDto();

        if ( coords != null ) {
            villagePlayerCountDto.setCoords( coords );
        }
        if ( playerName != null ) {
            villagePlayerCountDto.setPlayerName( playerName );
        }

        return villagePlayerCountDto;
    }

    private String villagePlayerPlayerName(Village village) {
        if ( village == null ) {
            return null;
        }
        Player player = village.getPlayer();
        if ( player == null ) {
            return null;
        }
        String playerName = player.getPlayerName();
        if ( playerName == null ) {
            return null;
        }
        return playerName;
    }
}
