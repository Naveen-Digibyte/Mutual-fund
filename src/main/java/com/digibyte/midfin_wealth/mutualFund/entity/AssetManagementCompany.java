package com.digibyte.midfin_wealth.mutualFund.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

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

    @OneToMany(mappedBy = "assetManagementCompany", cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<AMCFund> amcFunds;
}
