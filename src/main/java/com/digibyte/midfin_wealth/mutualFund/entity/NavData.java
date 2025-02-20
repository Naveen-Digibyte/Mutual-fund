package com.digibyte.midfin_wealth.mutualFund.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

/**
 * @author NaveenDhanasekaran
 *
 * History:
 * -19-02-2025 <NaveenDhanasekaran> NavData
 *      - InitialVersion
 */

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name="mf_005_t_nav")
@JsonInclude(value = JsonInclude.Include.NON_NULL)
public class NavData {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "mf_005_navId")
    @JsonIgnore
    private long id;

    @ManyToOne
    @JoinColumn(name = "mf_005_amcFund", referencedColumnName = "mf_003_fundId")
    @JsonBackReference
    private AMCFund amcFund;

    @Column(name = "mf_005_date")
    private LocalDate date;
    
    @Column(name = "mf_005_navValue")
    private float value;
}
