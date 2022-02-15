import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class SearchBuilder {

    public static SearchEngineService searchService(){
        return new SearchEngineServiceImpl();
    }
}
