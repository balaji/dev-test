package com.balaji.goeuro.service;

import com.balaji.goeuro.domain.GeoPosition;
import com.balaji.goeuro.domain.Position;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.util.ReflectionTestUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.nullValue;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.when;

public class PositionServiceTest {

    PositionService positionService;

    @Mock
    ObjectMapper mockObjectMapper;

    @Before
    public void setUp() {
        positionService = new PositionService();
        MockitoAnnotations.initMocks(this);
        ReflectionTestUtils.setField(positionService, "mapper", mockObjectMapper);
        ReflectionTestUtils.setField(positionService, "apiUrl", "http://some.url");
    }

    @Test
    public void shouldFailWhenApiEndpointDoesNotRespond() throws IOException {
        when(mockObjectMapper.readValue(any(URL.class), eq(Position[].class))).thenThrow(new IOException());
        boolean result = positionService.createCSVForPositionData("queryData");
        assertThat(result, is(false));
    }

    @Test
    public void shouldCreateCSVFileIfApiEndpointWorks() throws IOException {
        when(mockObjectMapper.readValue(any(URL.class), eq(Position[].class))).thenReturn(new Position[]{});
        boolean result = positionService.createCSVForPositionData("queryData");
        assertThat(result, is(true));
        assertThat(new File("output.csv").exists(), is(true));
    }

    @Test
    public void shouldCreateCSVFileWithCorrectData() throws IOException {
        float expectedLatitude = 12F;
        float expectedLongitude = 16F;
        String expectedId = "_id";
        String expectedName = "name";
        String expectedType = "type";

        GeoPosition geoPosition = new GeoPosition();
        geoPosition.setLatitude(expectedLatitude);
        geoPosition.setLongitude(expectedLongitude);
        Position position = new Position();
        position.setId(expectedId);
        position.setName(expectedName);
        position.setType(expectedType);
        position.setGeoPosition(geoPosition);

        when(mockObjectMapper.readValue(any(URL.class), eq(Position[].class))).thenReturn(new Position[]{position});
        boolean result = positionService.createCSVForPositionData("queryData");
        assertThat(result, is(true));
        File actualFile = new File("output.csv");

        assertThat(actualFile.exists(), is(true));

        BufferedReader fileReader = new BufferedReader(new FileReader(actualFile));
        String line1 = fileReader.readLine();
        String line2 = fileReader.readLine();
        String line3 = fileReader.readLine();

        assertThat(line1, is(equalTo("_id,name,type,latitude,longitude")));
        assertThat(line2, is(equalTo(String.format("%s,%s,%s,%s,%s", expectedId, expectedName, expectedType, expectedLatitude, expectedLongitude))));
        assertThat(line3, is(nullValue()));

        fileReader.close();
    }
}