package in.nic.lrisd.bhunakshav6.bhunakshacommon.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name="map_styles")
public class MapStyles implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    private String id;

    private String code;

    private String comment;

    @Column(name = "geometry_type")
    private String geometryType;

    @Column(name = "last_updated")
    private Timestamp lastUpdated;

    @Column(name = "sld_symbolizers")
    private String sldSymbolizers;
}


