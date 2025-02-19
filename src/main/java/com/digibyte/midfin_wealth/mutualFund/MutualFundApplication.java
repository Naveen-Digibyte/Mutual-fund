package com.digibyte.midfin_wealth.mutualFund;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * @author NaveenDhanasekaran
 *
 * History:
 * -19-02-2025 <NaveenDhanasekaran> MutualFundApplication
 *      - InitialVersion
 */

@SpringBootApplication
@EnableScheduling
public class MutualFundApplication {

	public static void main(String[] args) {
		SpringApplication.run(MutualFundApplication.class, args);
	}

}
