package ryansattler;

import java.math.BigDecimal;
import java.util.Optional;

public class InputParser {

    /*
        This is probably an unneccessary layer of complexity for this simple exercise, but I included
        it to demonstrate separation of concerns. With more complex input, there could potentially
        be more complex parsing and error handling.
     */
    public Optional<ConversionRequest> parseInput(String input) {
        try {
            var parts = input.trim().split("\\s+");
            var base = parts[0].trim();
            var amount = new BigDecimal(parts[1]);
            var terms = parts[3];

            return Optional.of(new ConversionRequest(base, terms, amount));
        } catch (Exception e) {
            return Optional.empty();
        }
    }


}
