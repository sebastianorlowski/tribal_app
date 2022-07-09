package core.dataloader.persistence.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Cascade;

import javax.persistence.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "villages")
public class Village {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "village_id")
    private String villageId;

    @Column(name = "x_position")
    private Integer x;

    @Column(name = "y_position")
    private Integer y;

    @ManyToOne(cascade = CascadeType.REMOVE)
    private Player player;

    @OneToOne(mappedBy = "village")
    private Unit unit;
}
