package com.digibyte.midfin_wealth.mutualFund.entity;

import com.digibyte.midfin_wealth.mutualFund.enums.Status;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author NaveenDhanasekaran
 *
 * History:
 * -19-02-2025 <NaveenDhanasekaran> AssetManagementCompany
 *      - InitialVersion
 */

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name="mf_001_t_amc")
@JsonInclude(value = JsonInclude.Include.NON_NULL)
public class AssetManagementCompany {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "mf_001_amcId")
    private long id;
    
    @Column(name = "mf_001_amcName")
    private String name;

    @Column(name = "mf_001_amcValueNo")
    @JsonIgnore
    private int amfiId;

    @Column(name = "mf_001_amcStatus")
    @Enumerated(EnumType.STRING)
    private Status status;

    @OneToMany(mappedBy = "assetManagementCompany", cascade = CascadeType.ALL)
    @JsonManagedReference
    @JsonIgnore
    private List<AMCFund> amcFunds;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "mf_001_details", referencedColumnName = "mf_006_amcDetailId")
    private AMCDetails amcDetails;
}
