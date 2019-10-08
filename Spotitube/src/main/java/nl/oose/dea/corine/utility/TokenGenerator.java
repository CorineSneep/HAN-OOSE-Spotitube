package nl.oose.dea.corine.utility;

import java.util.Random;

public class TokenGenerator {
    public String createToken(){
        Random rand = new Random();
        String number1 = String.format("%04d", rand.nextInt(10000));
        String number2 = String.format("%04d", rand.nextInt(10000));
        String number3 = String.format("%04d", rand.nextInt(10000));
        String token = number1 + "-" + number2 + "-" + number3;
        return token;
    }
}
