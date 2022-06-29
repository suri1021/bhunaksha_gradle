package in.nic.lrisd.bhunakshav6.bhunakshacommon.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import in.nic.lrisd.bhunakshav6.bhunakshacommon.usertype.JSONBDataUserType;
import lombok.*;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;

import javax.persistence.*;
import java.sql.Timestamp;

/**
 * The persistent class for the khasramap database table.
 */
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@TypeDef(name = "JsonDataUserType", typeClass = JSONBDataUserType.class)
@Table(name = "Khasramap")
public class Khasramap {

    @Id
    @Column(columnDefinition = "uuid", updatable = false)
    @GeneratedValue(generator = "uuid")
    private String id;
    @Column(name = "wkb_geometry")
    private String wkbGeometry;
    @Column(name = "bhucode")
    private String bhucode;
    @Column(name = "kide")
    private String kide;
    @Column(name = "div_id")
    private String divId;
    @JsonIgnore
    @Column(name = "attributes")
    private String attributes;
    @Column(name = "last_updated")
    private Timestamp lastUpdated;
    @Type(type = "JsonDataUserType")
    @Column(name = "attributes_json")
    private String attributes_json;
    @Column(name = "pniu")
    private String pniu;
    @JsonIgnore
    @Column(name = "pnil")
    private String pnil;
    @Column(name = "interior_point")
    private String interiorPoint;
    @Column(name = "ulpin_generation_date")
    private Timestamp ulpinGenerationDate;
    @Column(name = "ulpin_mode")
    private String ulpinMode;
    @JsonIgnore
    @Column(name = "signature")
    private String signature;
    @JsonIgnore
    @Column(name = "signkey")
    private String signkey;
    @JsonIgnore
    @Column(name = "dscno")
    private String dscno;
    @JsonIgnore
    @Column(name = "signdate")
    private Timestamp signdate;
    @Type(type = "JsonDataUserType")
    @Column(name = "attributesjson")
    private String attributesjson;
    @Column(name = "attributejson")
    private String attributejson;
}