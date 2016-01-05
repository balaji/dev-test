package com.balaji.goeuro.service;

import com.balaji.goeuro.domain.Position;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.log4j.Log4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.FileWriter;
import java.io.IOException;
import java.net.URI;

/*
 * Service class to Parse data from API endpoint and to create CSV file
 */
@Service
@Log4j
public class PositionService {
    @Autowired
    ObjectMapper mapper;

    @Value(value = "${api.url}")
    String apiUrl;

    private Position[] getPositionData(String queryString) throws IOException {
        URI uri = URI.create(String.format("%s/position/suggest/en/%s", apiUrl, queryString));
        return mapper.readValue(uri.toURL(), Position[].class);
    }

    /**
     * Creates a CSV file in the current directory using the data
     * from the API endpoint queried with the query string
     *
     * @param queryString to search in the API
     * @return
     */
    public boolean createCSVForPositionData(String queryString) {
        Position[] positions;
        try {
            positions = getPositionData(queryString);
        } catch (IOException e) {
            log.error("Error in fetching details.", e);
            return false;
        }
        String[] csvStrings = new String[positions.length + 1];

        //CSV Header information
        csvStrings[0] = "_id,name,type,latitude,longitude";

        for (int i = 0; i < positions.length; i++) {
            Position position = positions[i];
            csvStrings[i + 1] = String.format("%s,%s,%s,%s,%s", position.getId(),
                    position.getName(), position.getType(), position.getGeoPosition().getLatitude(),
                    position.getGeoPosition().getLongitude());
        }
        return createFile(csvStrings);
    }

    /**
     * Method creates CSV file from a given array of lines.
     * @param data lines for CSV file
     * @return boolean to indicate whether the operation was successful
     */
    private boolean createFile(String[] data) {
        FileWriter fileWriter = null;
        try {
            fileWriter = new FileWriter("output.csv", false);
            for (String line : data) {
                fileWriter.write(line + "\n");
            }
            return true;
        } catch (IOException e) {
            log.error("Error in creating CSV file.", e);
            return false;
        } finally {
            if (fileWriter != null) {
                try {
                    fileWriter.close();
                } catch (IOException ignored) {
                }
            }
        }
    }
}
