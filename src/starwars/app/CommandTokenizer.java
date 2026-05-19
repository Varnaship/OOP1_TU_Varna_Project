package starwars.app;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Tokenizes command lines, treating quoted text as a single token.
 * <p>
 * Токенизира командни редове, като текст в кавички се приема за един токен.
 */
public final class CommandTokenizer {
    private static final Pattern TOKEN_PATTERN = Pattern.compile("\"([^\"]*)\"|(\\S+)");

    private CommandTokenizer() {}

    /**
     * Splits a line into tokens. Quoted parts are kept together.
     *
     * @param line input line
     * @return list of tokens (may be empty)
     */
    public static List<String> tokenize(String line) {
        List<String> tokens = new ArrayList<>();
        Matcher matcher = TOKEN_PATTERN.matcher(line);
        while (matcher.find()) {
            if (matcher.group(1) != null) {
                tokens.add(matcher.group(1));
            } else {
                tokens.add(matcher.group(2));
            }
        }
        return tokens;
    }
}