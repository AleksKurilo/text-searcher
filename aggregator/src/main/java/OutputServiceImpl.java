import java.util.List;
import java.util.Map;

class OutputServiceImpl implements OutputService{

    @Override
    public void print(Map<String, List<DataSearchInfo>> map) {
        map.forEach((key, value) -> {
            System.out.println(key + "-->");
            value.forEach(dataSearchInfo -> {
                System.out.println("\t lineOffset: " + dataSearchInfo.lineOffset);
                System.out.println("\t charOffset: " + dataSearchInfo.charOffset);
            });
        });
    }
}
