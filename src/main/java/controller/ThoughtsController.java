package controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

/**
 * @author: Manas Sharma
 * Includes logic for REST
 * end-points to handle
 * calls from client
 */
@Service
@RestController
@RequestMapping("thoughts")
public class ThoughtsController {

    private Logger log = LoggerFactory.getLogger(ThoughtsController.class);

    @Value("#{${thoughts}}")
    Map<String, String> thoughtsData;

    @RequestMapping(value = "get", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Map<String, String>> getThought(HttpServletRequest request) {

        // TODO: Include more complex logic of filtering thoughts by person/author once db/content is ready

        log.info("Request info --> user {}, uri {} ", request.getRemoteUser(), request.getRequestURI());
        SortedMap<String, String> sortedThoughts = new TreeMap<>();
        sortedThoughts.putAll(thoughtsData);
        int dayOfMonth = LocalDateTime.now().getDayOfMonth();
        Map<String, String> response = new HashMap<>();

        int counter = 0;
        for (String author : sortedThoughts.keySet()) {
            if (counter == sortedThoughts.keySet().size() % dayOfMonth) {
                response.put("author", author);
                response.put("thought", sortedThoughts.get(author));
                break;
            }
        }
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
