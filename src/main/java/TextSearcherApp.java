import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

public class TextSearcherApp {

    static Logger logger = Logger.getLogger(TextSearcherApp.class.getName());

    private static final SearchEngineService SEARCH_ENGINE_SERVICE = SearchBuilder.searchService();
    private static final AggregatorService AGGREGATOR_SERVICE = AggregatorBuilder.aggregatorService();
    private static final OutputService OUTPUT_SERVICE = AggregatorBuilder.outputService();

    /**
     * Run application.
     * This will initiate searching input data from "source/input.txt" in text which is stored in "source/big.txt" file.
     */
    public static void main(String[] args) {
        logger.info("Starting application");
        String text = FileUtils.readClasspathFile("source/big.txt");
        List<String> search = FileUtils.readClasspathFileAsList("source/input.txt");

        Map<String, List<DataSearchInfo>> result = SEARCH_ENGINE_SERVICE.match(search, text);

        Map<String, List<DataSearchInfo>> commonResult = AGGREGATOR_SERVICE.merge(result);
        OUTPUT_SERVICE.print(commonResult);

        logger.info("Run success");
    }
}
