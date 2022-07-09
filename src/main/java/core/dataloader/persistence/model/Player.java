package core.dataloader.persistence.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "players")
public class Player {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "user_id")
    private String userId;

    @Column(name = "player_name")
    private String playerName;

    @OneToMany(mappedBy = "player", cascade = CascadeType.ALL)
    private List<Village> villages;

    @ManyToOne(cascade = CascadeType.ALL)
    private Ally ally;
}
