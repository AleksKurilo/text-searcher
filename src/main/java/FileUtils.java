import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
import java.util.stream.Collectors;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class FileUtils {

    /**
     * Read and return content of file available in classpath.
     */
    public static String readClasspathFile(String fileNameInClasspath) {
        final InputStream resourceAsStream = FileUtils.class.getClassLoader().getResourceAsStream(fileNameInClasspath);
        if (resourceAsStream == null) {
            throw new RuntimeException(String.format("Can't find file '%s'", fileNameInClasspath));
        }
        return new BufferedReader(new InputStreamReader(resourceAsStream))
                .lines()
                .collect(Collectors.joining("\n"));
    }

    /**
     * Read and return content of file available in classpath as list.
     */
    public static List<String> readClasspathFileAsList(String fileNameInClasspath) {
        final InputStream resourceAsStream = FileUtils.class.getClassLoader().getResourceAsStream(fileNameInClasspath);
        if (resourceAsStream == null) {
            throw new RuntimeException(String.format("Can't find file '%s'", fileNameInClasspath));
        }
        return new BufferedReader(new InputStreamReader(resourceAsStream))
                .lines()
                .collect(Collectors.toList());
    }
}
