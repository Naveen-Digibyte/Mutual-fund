package com.digibyte.midfin_wealth.mutualFund.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.*;
import lombok.*;

/**
 * @author NaveenDhanasekaran
 *
 * History:
 * -19-02-2025 <NaveenDhanasekaran> AMCDetails
 *      - InitialVersion
 */

@Entity
@Getter
@Setter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name="mf_006_t_amcDetails")
@JsonInclude(value = JsonInclude.Include.NON_NULL)
public class AMCDetails {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name ="mf_006_amcDetailId")
    private long id;

    @Column(name ="mf_006_website")
    private String website;

    @Column(name ="mf_006_phoneNumber")
    private String phoneNumber;

    @Column(name ="mf_006_email")
    private String email;

    @Column(name ="mf_006_address")
    private String address;

    @Column(name = "mf_006_amcImage")
    private String imageUrl;
}
