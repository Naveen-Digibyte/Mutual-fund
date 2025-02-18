package com.digibyte.midfin_wealth.mutualFund.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
@Table(name="mf_002_t_schemeCategory")
@JsonInclude(value = JsonInclude.Include.NON_NULL)
public class SchemeCategory {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "mf_002_id")
    private long id;
    
    @Column(name = "mf_002_schemeCategory")
    private String category;

    @OneToMany(mappedBy = "schemeCategory", cascade = CascadeType.ALL)
    @JsonManagedReference
    @JsonIgnore
    private List<AMCFund> amcFundList;
}
