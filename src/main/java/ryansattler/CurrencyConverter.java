package ryansattler;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.Map;
import java.util.Optional;

public class CurrencyConverter {

    private final Map<String, BigDecimal> rates;
    private final Map<String, String> crossTable;

    public CurrencyConverter(Map<String, BigDecimal> rates, Map<String, String> crossTable) {
        this.rates = rates;
        this.crossTable = crossTable;
    }

    public Optional<BigDecimal> convert(BigDecimal amount, String base, String terms) {
        return getCrossRate(base, terms).map(amount::multiply);
    }

    /*
        Note: in this exercise it would be possible improve performance by pre-computing rates for all currency pairs.
        However, it seemed more in the spirit of the instructions to calculate rates in realtime.
     */
    private Optional<BigDecimal> getCrossRate(String base, String terms) {

        var conversion = base + terms;
        var cross = crossTable.get(conversion);
        if (cross == null) {
            return Optional.empty();
        }
        switch (cross) {
            case "D":
                return Optional.of(rates.get(conversion));
            case "1:1":
                return Optional.of(BigDecimal.ONE);
            case "Inv":
                BigDecimal reversedRate = rates.get(terms + base);
                return Optional.of(BigDecimal.ONE.divide(reversedRate, MathContext.DECIMAL64));
            default:
                // 'cross' should now be a currency string value
                Optional<BigDecimal> toCrossRate = getCrossRate(base, cross);
                Optional<BigDecimal> fromCrossRate = getCrossRate(cross, terms);
                if (toCrossRate.isPresent() && fromCrossRate.isPresent()) {
                    return Optional.of(toCrossRate.get().multiply(fromCrossRate.get()));
                } else {
                    return Optional.empty();
                }
        }
    }
}
