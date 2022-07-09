package core.villagegenerator.api.dto;

import core.dataloader.persistence.model.Player;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.StringJoiner;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class VillagePlayerCountDto {

    private String playerName;
    private Integer villageCount;
    private String coords;
    private List<String> players;

    @Override
    public String toString() {
        return playerName + " -> " + villageCount + "\n";
    }

    public String toStringVillagePlayers() {
        return coords + " -> " + playerList() + "\n" + "--------------------------------------------------\n";
    }

    public String playerList() {
        StringJoiner joiner = new StringJoiner(" & ");
        players.forEach(player -> joiner.add("[player]" + player + "[/player]"));
        return joiner.toString();
    }

    public String toStringVillagePlayerName() {
        return coords + " -> " + "[player]" + playerName + "[/player]" + "\n";
    }
}
