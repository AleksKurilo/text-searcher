import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class TextSearcherApp {

    static Logger logger = Logger.getLogger(TextSearcherApp.class.getName());

    private static final SearchEngineService SEARCH_ENGINE_SERVICE = SearchBuilder.searchService();
    private static final AggregatorService AGGREGATOR_SERVICE = AggregatorBuilder.aggregatorService();
    private static final OutputService OUTPUT_SERVICE = AggregatorBuilder.outputService();

    /**
     * Run application.
     * This will initiate searching input data from "source/input.txt" in text which is stored in "source/big.txt" file.
     */
    public static void main(String[] args) throws IOException, URISyntaxException {
        logger.info("Starting application");
        List<String> search = FileUtils.readClasspathFileAsList("source/input.txt");
        List<Map<String, Set<DataSearchInfo>>> resultList = new ArrayList<>();

        try (Stream<String> stream = Files.lines(Paths.get(ClassLoader.getSystemResource("source/big-1.txt").toURI()))) {

            AtomicInteger partition = new AtomicInteger(0);
            final List<String> lines = new ArrayList<>();

            stream.forEach(line -> {
                lines.add(line);

                if (lines.size() == ConfigurationDefaults.LINE_LIMIT) {
                    String text = lines.stream().collect(Collectors.joining("\n"));
                    Map<String, Set<DataSearchInfo>> result = SEARCH_ENGINE_SERVICE.match(partition.get(), search, text);
                    resultList.add(result);
                    partition.incrementAndGet();
                    lines.clear();
                }

            });

            String text = lines.stream().collect(Collectors.joining("\n"));
            Map<String, Set<DataSearchInfo>> result = SEARCH_ENGINE_SERVICE.match(partition.get(), search, text);
            resultList.add(result);
        }

        Map<String, Set<DataSearchInfo>> commonResult = AGGREGATOR_SERVICE.merge(resultList.toArray(new Map[]{}));
        OUTPUT_SERVICE.print(commonResult);

        logger.info("Run success");
    }
}
