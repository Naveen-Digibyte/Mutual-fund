package com.digibyte.midfin_wealth.mutualFund.entity;


import com.digibyte.midfin_wealth.mutualFund.enums.Status;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;
import java.util.List;

/**
 * @author NaveenDhanasekaran
 *
 * History:
 * -19-02-2025 <NaveenDhanasekaran> AMCFund
 *      - InitialVersion
 */

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name="mf_003_t_amcFund")
@JsonInclude(value = JsonInclude.Include.NON_NULL)
public class AMCFund {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "mf_003_fundId")
    private long id;
    
    @Column(name = "mf_003_fundCode")
    private String code;

    @Column(name = "mf_003_fundName")
    private String fundName;

    @Column(name = "mf_003_fundNavName")
    private String fundNavName;

    @Column(name = "mf_003_fundMinimumAmount")
    private String minimumAmount;

    @Column(name = "mf_003_fundLaunchDate")
    private LocalDate launchDate;

    @Column(name = "mf_003_fundClosureDate")
    private LocalDate closureDate;

    @Column(name = "mf_003_fundPayout")
    private String isinDivPayout;

    @Column(name = "mf_003_fundReinvestment")
    private String isinDivReInvestment;

    @Column(name = "mf_003_fundStaus")
    @Enumerated(EnumType.STRING)
    private Status status;
    
    @ManyToOne
    @JoinColumn(name = "mf_003_assetManagementCompany", referencedColumnName = "mf_001_amcId")
    @JsonBackReference
    private AssetManagementCompany assetManagementCompany;
    
    @ManyToOne
    @JoinColumn(name = "mf_003_schemeCategory", referencedColumnName = "mf_002_id")
    @JsonBackReference
    private SchemeCategory schemeCategory;

    @ManyToOne
    @JoinColumn(name = "mf_003_schemeSubCategory", referencedColumnName = "mf_007_id")
    @JsonBackReference
    private SchemeSubCategory schemeSubCategory;

    @ManyToOne
    @JoinColumn(name = "mf_003_schemeTYpe", referencedColumnName = "mf_004_id")
    @JsonBackReference
    private SchemeType schemeType;

    @OneToMany(mappedBy = "amcFund", cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<NavData> navDatas;
}
