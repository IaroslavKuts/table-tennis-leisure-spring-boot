package com.example.demo.DTO;

import java.util.List;


/**
 * @param dateOfGame expected to be received from front-end -  "YYYY-MM-DD"
 * @param chosenTimePeriods expected to be received from front-end -  ["09:30-10:00", "10:00-10:30", ... ]
 */
public record OrdersBulkDTO (String dateOfGame, List<String> chosenTimePeriods){}
