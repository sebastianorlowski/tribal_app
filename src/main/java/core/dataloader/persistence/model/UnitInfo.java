package core.dataloader.persistence.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "unit_info")
public class UnitInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @OneToOne
    private Village coord;
    private Integer spear;
    private Integer sword;
    private Integer axe;
    private Integer spy;
    private Integer light;
    private Integer heavy;
    private Integer ram;
    private Integer catapult;
    private Integer knight;
    private Integer snob;
}
