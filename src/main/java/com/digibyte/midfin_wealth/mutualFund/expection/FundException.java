package com.digibyte.midfin_wealth.mutualFund.expection;

/**
 * @author NaveenDhanasekaran
 *
 * History:
 * -19-02-2025 <NaveenDhanasekaran> FundException
 *      - InitialVersion
 */

public class FundException extends RuntimeException{

    public FundException(String message){
        super(message);
    }
}
