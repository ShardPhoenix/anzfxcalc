package ryansattler;

import org.junit.Test;

import java.math.BigDecimal;
import java.util.Optional;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;

public class InputParserTest {

    @Test
    public void handleWellFormedInput() {

        InputParser parser = new InputParser();

        var result = parser.parseInput("AUD 100.0 in USD");

        assertThat(result, is(Optional.of(new ConversionRequest("AUD", "USD", new BigDecimal("100.0")))));
    }

    @Test
    public void handleMalformedInput() {

        InputParser parser = new InputParser();

        var result = parser.parseInput("AUD100.0 in USD");

        assertThat(result, is(Optional.empty()));
    }

}