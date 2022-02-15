import com.google.common.collect.Lists;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ForkJoinPool;
import java.util.stream.Collectors;

class SearchEngineServiceImpl implements SearchEngineService{

    private static int numberOfProcessors = Runtime.getRuntime().availableProcessors();
    private static ForkJoinPool forkJoinPool = new ForkJoinPool(numberOfProcessors);

    @Override
    public Map<String, List<DataSearchInfo>> match(List<String> search, String text) {
        List<String> lines = Arrays.stream(text.split("\n")).collect(Collectors.toList());
        List<List<String>> partitions = Lists.partition(lines, ConfigurationDefaults.LINE_LIMIT);

        SearchingTask searchingTask = SearchingTask.builder()
                .partitionNumber(0)
                .dataSearchList(search)
                .subSource(Collections.emptyList())
                .source(partitions)
                .matcherServiceImpl(new SearchServiceImpl())
                .build();

        return forkJoinPool.invoke(searchingTask);
    }

}
