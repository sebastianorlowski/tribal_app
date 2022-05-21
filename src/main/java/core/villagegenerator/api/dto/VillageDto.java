package core.villagegenerator.api.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class VillageDto {

    private String coords;
    private String playerName;

    @Override
    public String toString() {
        return coords + "\n";
    }
}
