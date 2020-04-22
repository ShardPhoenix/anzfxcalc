package ryansattler;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Map;
import java.util.Optional;
import java.util.Properties;
import java.util.Scanner;
import java.util.stream.Collectors;

public class CommandLine {

    public static void main(String[] args) throws Exception {

        Scanner scanner = new Scanner(System.in);

        /* Normally these values would come from a database or another service,
           and for the sake of keeping the exercise simple there is no validation of these files and they
           are assumed to be correct and complete.
         */
        Properties ratesProps = getProperties("rates.properties");

        // I would normally use a CSV for this if not a DB, but CSVs are a bit clunky to read without a library
        Properties crossProps = getProperties("crosses.properties");

        Properties decimalProps = getProperties("decimals.properties");

        Map<String, BigDecimal> rates = ratesProps.entrySet().stream()
                .collect(Collectors.toMap(e -> e.getKey().toString(), e -> new BigDecimal(e.getValue().toString())));

        Map<String, String> crossTable =
                crossProps.entrySet().stream().collect(Collectors.toMap(e -> e.getKey().toString(), e -> e.getValue().toString()));

        Map<String, Integer> decimals = decimalProps.entrySet().stream().collect(
                Collectors.toMap(e -> e.getKey().toString(), e -> Integer.parseInt(e.getValue().toString())));

        CurrencyConverter converter = new CurrencyConverter(rates, crossTable);

        InputParser parser = new InputParser();

        System.out.println("Rates loaded, ready to convert currency:");

        while (true) {
            if (scanner.hasNextLine()) {
                var input = scanner.nextLine();

                if (input.toLowerCase().trim().equals("exit")) {
                    break;
                }

                Optional<ConversionRequest> request = parser.parseInput(input);

                if (request.isEmpty()) {
                    System.out.println("Malformed request - format is like 'AUD 100.00 in USD'");
                } else {

                    var base = request.get().getBase();
                    var terms = request.get().getTerms();
                    var amount = request.get().getAmount();

                    Optional<BigDecimal> conversion = converter.convert(amount, base, terms);

                    if (conversion.isEmpty()) {
                        System.out.println(String.format("Unable to find rate for %s/%s", base, terms));
                    } else {
                        int decimalPlaces = decimals.get(terms);
                        System.out.println(String.format("%s %s = %." + decimalPlaces + "f %s", base, amount, conversion.get(), terms));
                    }
                }
            }
        }
    }

    private static Properties getProperties(String path) throws IOException {
        Properties props = new Properties();
        props.load(CommandLine.class.getClassLoader().getResourceAsStream(path));
        return props;
    }
}
