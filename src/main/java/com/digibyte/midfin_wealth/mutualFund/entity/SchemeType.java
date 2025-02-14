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
@Table(name="mf_004_t_schemeType")
@JsonInclude(value = JsonInclude.Include.NON_NULL)
public class SchemeType {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "mf_004_id")
    private long id;

    @Column(name = "mf_002_schemeType")
    private String type;

    @OneToMany(mappedBy = "schemeType", cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<AMCFund> amcFundList;
    
}
