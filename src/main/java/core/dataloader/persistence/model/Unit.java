package core.dataloader.persistence.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "units")
public class Unit {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @OneToOne(cascade = CascadeType.ALL)
    private Village village;

    @Column(name = "spear")
    private Integer spear;

    @Column(name = "sword")
    private Integer sword;

    @Column(name = "axe")
    private Integer axe;

    @Column(name = "spy")
    private Integer spy;

    @Column(name = "light")
    private Integer light;

    @Column(name = "heavy")
    private Integer heavy;

    @Column(name = "ram")
    private Integer ram;

    @Column(name = "catapult")
    private Integer catapult;

    @Column(name = "knight")
    private Integer knight;

    @Column(name = "snob")
    private Integer snob;

}
