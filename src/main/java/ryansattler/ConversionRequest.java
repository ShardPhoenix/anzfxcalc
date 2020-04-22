package ryansattler;

import java.math.BigDecimal;
import java.util.Objects;

public class ConversionRequest {

    private final String base;
    private final String terms;
    private final BigDecimal amount;

    public ConversionRequest(String base, String terms, BigDecimal amount) {
        this.base = base;
        this.terms = terms;
        this.amount = amount;
    }

    public String getBase() {
        return base;
    }

    public String getTerms() {
        return terms;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ConversionRequest that = (ConversionRequest) o;
        return Objects.equals(base, that.base) &&
                Objects.equals(terms, that.terms) &&
                Objects.equals(amount, that.amount);
    }

    @Override
    public int hashCode() {
        return Objects.hash(base, terms, amount);
    }
}
