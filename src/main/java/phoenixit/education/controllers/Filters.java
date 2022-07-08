package phoenixit.education.controllers;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.domain.Sort;

import java.io.Serializable;
import java.util.List;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Filters implements Serializable {

    private String sortField;
    private Sort.Direction sortDir;
    private List<Filter> filters;

}
