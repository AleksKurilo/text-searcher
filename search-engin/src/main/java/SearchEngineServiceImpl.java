import com.google.common.collect.Lists;

import java.util.*;
import java.util.concurrent.ForkJoinPool;
import java.util.stream.Collectors;

class SearchEngineServiceImpl implements SearchEngineService{

    private static int numberOfProcessors = Runtime.getRuntime().availableProcessors();
    private static ForkJoinPool forkJoinPool = new ForkJoinPool(numberOfProcessors);

    @Override
    public Map<String, Set<DataSearchInfo>> match(int partition, List<String> search, String text) {
        List<String> lines = Arrays.stream(text.split("\n")).collect(Collectors.toList());
        List<List<String>> source = Lists.partition(lines, ConfigurationDefaults.LINE_LIMIT);

        SearchingTask searchingTask = SearchingTask.builder()
                .partitionNumber(partition)
                .dataSearchList(search)
                .subSource(Collections.emptyList())
                .source(source)
                .matcherServiceImpl(new SearchServiceImpl())
                .build();

        return forkJoinPool.invoke(searchingTask);
    }

}
