package in.nic.lrisd.bhunakshav6.bhunakshacommon.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;

/**
 * The persistent class for the symbol_master database table.
 *
 */
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name="symbol_master")
//@NamedQuery(name="SymbolMaster.findAll", query="SELECT s FROM SymbolMaster s")
public class SymbolMaster implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    private String id;

    private String image;

    @Column(name="image_type")
    private String imageType;

    @Column(name="symbol_code")
    private String symbolCode;

    @Column(name="symbol_name")
    private String symbolName;
}