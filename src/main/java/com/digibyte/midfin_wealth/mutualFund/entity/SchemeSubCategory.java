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
@Table(name="mf_007_t_schemeSubCategory")
@JsonInclude(value = JsonInclude.Include.NON_NULL)
public class SchemeSubCategory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "mf_007_id")
    private long id;

    @Column(name = "mf_007_schemeSubCategory")
    private String subCategory;

    @OneToMany(mappedBy = "schemeSubCategory", cascade = CascadeType.ALL)
    @JsonManagedReference
    @JsonIgnore
    private List<AMCFund> amcFundList;
}
