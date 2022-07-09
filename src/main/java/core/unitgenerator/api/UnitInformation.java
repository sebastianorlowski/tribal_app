package core.unitgenerator.api;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UnitInformation {

    private String playerName;
    private Integer packages;

    @Override
    public String toString() {
        return playerName + " " + packages + "\n";
    }
}
