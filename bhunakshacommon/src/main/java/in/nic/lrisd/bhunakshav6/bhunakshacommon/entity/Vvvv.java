package in.nic.lrisd.bhunakshav6.bhunakshacommon.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "vvvv")
public class Vvvv {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "gis_code")
    private String gisCode;

    @Column(name = "default_scale")
    private Short defaultScale;

    private String fmbstatus;
    private String fmbswversion;
    private String georefpoints;
    private String geotransform;
    private Short mapscale;

    @Column(name = "scale_units")
    private String scaleUnits;

    @Column(name = "ulpin_generated")
    private String ulpinGenerated;

    @Column(name = "village_en")
    private String villageEn;

    @Column(name = "village_hi")
    private String villageHi;

    private String vsrno;

    @Column(name = "wkb_geometry")
    private String wkbGeometry;
}