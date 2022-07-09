package core.dataloader.persistence.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "ally")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Ally {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "ally_id")
    private Integer allyId;

    private String name;
    private String tag;
    private Integer members;
    private Integer villages;
    private Long points;

    @OneToMany(mappedBy = "ally", cascade = CascadeType.ALL)
    private List<Player> player;
}
