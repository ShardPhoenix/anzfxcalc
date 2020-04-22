package ryansattler;

import org.junit.Test;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;

public class CurrencyConverterTest {

    @Test
    public void convertsCurrencyWhenRateAvailable() {

        var rates = Map.of("AUDUSD", BigDecimal.valueOf(65, 2));
        var crossRates = Map.of("AUDUSD", "D");
        CurrencyConverter converter = new CurrencyConverter(rates, crossRates);

        var result = converter.convert(BigDecimal.valueOf(101.50), "AUD", "USD");

        assertThat(result, is(Optional.of(BigDecimal.valueOf(65.975))));
    }

    @Test
    public void returnsEmptyWhenRateNotAvailable() {
        var rates = new HashMap<String, BigDecimal>();
        var crossRates = new HashMap<String, String>();
        CurrencyConverter converter = new CurrencyConverter(rates, crossRates);

        var result = converter.convert(BigDecimal.valueOf(101.50), "AUD", "USD");

        assertThat(result, is(Optional.empty()));
    }

    @Test
    public void calculatesSelfRate() {
        var rates = new HashMap<String, BigDecimal>();
        var cross = Map.of("AUDAUD", "1:1");
        CurrencyConverter converter = new CurrencyConverter(rates, cross);

        var result = converter.convert(BigDecimal.valueOf(101.50), "AUD", "AUD");

        assertThat(result, is(Optional.of(BigDecimal.valueOf(101.50))));
    }

    @Test
    public void calculatesInverseRate() {
        var rates = Map.of("AUDUSD", BigDecimal.valueOf(3.0));

        var crossRates = Map.of("USDAUD", "Inv");

        CurrencyConverter converter = new CurrencyConverter(rates, crossRates);

        var result = converter.convert(BigDecimal.valueOf(100), "USD", "AUD");

        assertThat(result, is (Optional.of(new BigDecimal("33.3333333333333300"))));
    }

    @Test
    public void calculatesCrossRate() {

        var rates = Map.of(
                "AUDUSD", BigDecimal.valueOf(0.8371),
                "USDJPY", BigDecimal.valueOf(119.95));

        var crossRates = Map.of(
                "AUDUSD", "D",
                "USDAUD", "Inv",
                "USDJPY", "D",
                "AUDJPY", "USD");

        CurrencyConverter converter = new CurrencyConverter(rates, crossRates);

        var result = converter.convert(BigDecimal.valueOf(1.0), "AUD", "JPY");

        assertThat(result, is(Optional.of(new BigDecimal("100.4101450"))));
    }

    @Test
    public void calculatesNOKUSD() {

        var rates = Map.of(
                "EURUSD", BigDecimal.valueOf(1.2315),
                "EURNOK", BigDecimal.valueOf(8.6651));

        var crossRates = Map.of(
                "NOKUSD", "EUR",
                "EURUSD", "D",
                "NOKEUR", "Inv");

        CurrencyConverter converter = new CurrencyConverter(rates, crossRates);

        var result = converter.convert(BigDecimal.valueOf(1.0), "NOK", "USD");

        assertThat(result, is(Optional.of(new BigDecimal("0.142121845102768636950"))));

    }

}