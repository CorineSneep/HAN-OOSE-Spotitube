package nl.oose.dea.corine.utility;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TokenGeneratorTest {

    @Test
    public void createTokenTest(){
        int tokenlength = 14;
        TokenGenerator tokenGenerator = new TokenGenerator();

        String token = tokenGenerator.createToken();

        assertEquals(tokenlength, token.length());

    }
}
