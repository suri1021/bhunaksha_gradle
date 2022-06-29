package in.nic.lrisd.bhunakshav6.bhunakshacommon.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.sql.Timestamp;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name="layer_master")
public class LayerMaster implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    private String id;

    @Column(name="auto_show_layer")
    private String autoShowLayer;

    @Column(name="extra_attributes")
    private String extraAttributes;

    @Column(name="geometry_type")
    private String geometryType;

    @Column(name="hide_from_list")
    private String hideFromList;

    private String iseditable;

    @Column(name="last_updated")
    private Timestamp lastUpdated;

    @Column(name="layer_description")
    private String layerDescription;

    @Column(name="layer_type")
    private String layerType;

    @Column(name="sld_symbolizers")
    private String sldSymbolizers;
}