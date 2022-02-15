import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Logger;
import java.util.stream.Stream;

public class TextSearcherApp {

    static Logger logger = Logger.getLogger(TextSearcherApp.class.getName());

    private static final SearchEngineService SEARCH_ENGINE_SERVICE = new SearchEngineServiceImpl();
    private static final AggregatorService AGGREGATOR_SERVICE = new AggregatorServiceImpl();
    private static final OutputService OUTPUT_SERVICE = new OutputConsoleService();

    /**
     * Run application.
     * This will initiate searching input data from "source/input.txt" in text which is stored in "source/big.txt" file.
     */
    public static void main(String[] args) throws IOException, URISyntaxException {
        logger.info("Starting application");
        List<String> search = FileUtils.readClasspathFileAsList("source/input.txt");
        List<Map<String, Set<DataSearchInfo>>> result = new ArrayList<>();
        Path source = Paths.get(ClassLoader.getSystemResource("source/big.txt").toURI());

        streamSearching(search, source, result);

        Map<String, Set<DataSearchInfo>> commonResult = AGGREGATOR_SERVICE.merge(result.toArray(new Map[]{}));
        OUTPUT_SERVICE.print(commonResult);

        logger.info("Run success");
    }

    private static void streamSearching(List<String> search, Path source, List<Map<String, Set<DataSearchInfo>>> result) throws IOException {
        try (Stream<String> stream = Files.lines(source)) {
            AtomicInteger partition = new AtomicInteger(0);
            final List<String> lines = new ArrayList<>();

            stream.forEach(line -> {
                lines.add(line);

                if (lines.size() == ConfigurationDefaults.LINE_LIMIT) {
                    String text = String.join("\n", lines);
                    result.add(SEARCH_ENGINE_SERVICE.match(partition.get(), search, text));
                    partition.incrementAndGet();
                    lines.clear();
                }

            });

            String text = String.join("\n", lines);
            result.add(SEARCH_ENGINE_SERVICE.match(partition.get(), search, text));
        }
    }
}
