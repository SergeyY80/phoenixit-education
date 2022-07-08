package phoenixit.education.models;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Внутренний статус звонка
 */
public enum CallInternalStatus {
    NEW,
    SUCCESS,
    ERROR,
    RETRYING;

    public static List<String> asList() {
        return Arrays.stream(CallInternalStatus.values()).map(Enum::toString).collect(Collectors.toList());
    }

}
