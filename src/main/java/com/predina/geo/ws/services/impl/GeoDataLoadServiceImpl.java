package com.predina.geo.ws.services.impl;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.CSVWriter;

import com.predina.geo.ws.db.GeoCoordinateRepository;
import com.predina.geo.ws.db.impl.GeoCoordinateRepositoryImpl;
import com.predina.geo.ws.model.GeoCoordinate;
import com.predina.geo.ws.model.GeoMapLocation;
import com.predina.geo.ws.services.CompressionService;
import com.predina.geo.ws.exception.GeoDataException;
import com.predina.geo.ws.services.GeoDataLoadService;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author Rahul Anand Akkina
 * @version v1.0
 */
@Component
public class GeoDataLoadServiceImpl implements GeoDataLoadService {

    private static Logger logger = Logger.getLogger(GeoDataLoadServiceImpl.class);

    private final GeoCoordinateRepository geoCoordinateRepository;

    private final CompressionService compressionService;

    @Value("${geo.data.load.location}")
    private String geoDataLoadLocation;

    @Value("${geo.data.store.header}")
    private String geoDataStoreHeader;

    @Autowired
    public GeoDataLoadServiceImpl(final GeoCoordinateRepository geoCoordinateRepository, CompressionService compressionService){
        this.geoCoordinateRepository = geoCoordinateRepository;
        this.compressionService = compressionService;
    }

    @Override
    public GeoMapLocation loadWithoutRiskScore(final GeoCoordinate geoCoordinate) {
        return geoCoordinateRepository.updateWithoutRiskScore(geoCoordinate);
    }

    @Override
    public GeoMapLocation loadWithRiskScore(final GeoCoordinate geoCoordinate, final Integer riskScore) {
        return geoCoordinateRepository.updateWithRiskScore(geoCoordinate, riskScore);
    }

    @PostConstruct
    @Override
    public List<GeoMapLocation> load() throws GeoDataException {
        final List<GeoMapLocation> resultSet = Lists.newArrayList();
        final File file = getMostRecentFile();
        try(final CSVReader csvReader = new CSVReaderBuilder(Files.newBufferedReader(Paths.get(file.toURI()))).withSkipLines(1).build();) {

            if(logger.isInfoEnabled()) {
                logger.info(String.format("Loading the Coordinates File : %s", file.getName()));
            }

            String[] nextRecord;
            while ((nextRecord = csvReader.readNext()) != null) {
                if (nextRecord.length > 2) {
                    if(StringUtils.isNotBlank(nextRecord[0]) && StringUtils.isNotBlank(nextRecord[1]) && StringUtils.isNotBlank(nextRecord[2])) {
                        resultSet.add(loadWithRiskScore(new GeoCoordinate(new BigDecimal(nextRecord[0]).doubleValue(), new BigDecimal(nextRecord[1]).doubleValue()), Integer.parseInt(nextRecord[2])));
                    }
                } else if (nextRecord.length == 2) {
                    if(StringUtils.isNotBlank(nextRecord[0]) && StringUtils.isNotBlank(nextRecord[1])) {
                        resultSet.add(loadWithoutRiskScore(new GeoCoordinate(new BigDecimal(nextRecord[0]).doubleValue(), new BigDecimal(nextRecord[1]).doubleValue())));
                    }
                }
            }

            if(logger.isInfoEnabled()) {
                logger.info(String.format("Loaded the Coordinates File : %s", file.getName()));
            }
        }catch(IOException ie){
            logger.error("load error : "+ie.getMessage(),ie);
            throw new GeoDataException("Coordinates File Load Error", ie);
        }

        return ImmutableList.copyOf(resultSet);
    }

    @Scheduled(cron = "${geo.data.store.schedule}")
    @PreDestroy
    @Override
    public void store() throws GeoDataException{

        final String fileName = String.format("%s/%s.csv",geoDataLoadLocation, "Coordinates");

        if(logger.isInfoEnabled()) {
            logger.info(String.format("Storing Coordinates File : %s", fileName));
        }

        try(CSVWriter csvWriter = new CSVWriter(new FileWriter(fileName));){

            final List<GeoMapLocation> coordinateRepositoryAll = geoCoordinateRepository.findAll();

           if(coordinateRepositoryAll != null && !coordinateRepositoryAll.isEmpty()){

               if(logger.isDebugEnabled()) {
                   logger.debug(String.format("Storing %d records ", coordinateRepositoryAll.size()));
               }

               csvWriter.writeNext(geoDataStoreHeader.split(","));

               for(GeoMapLocation entry:coordinateRepositoryAll){
                   csvWriter.writeNext(Arrays
                           .asList(String.valueOf(entry.getCoords().getLat()), String.valueOf(entry.getCoords().getLng()), String.valueOf(entry.getRs()))
                           .toArray(new String[3]));
               }
           }

            if(logger.isInfoEnabled()) {
                logger.info(String.format("Stored Coordinates File : %s with %d records.", fileName, coordinateRepositoryAll.size()));
            }

        }catch(IOException ie){
            logger.error("store error : "+ie.getMessage(),ie);
            throw new GeoDataException("Coordinates File Store Error", ie);
        }
    }

    private String getDateString() {
        final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return sdf.format(Calendar.getInstance().getTime());
    }

    private File getMostRecentFile(){
        final Path parentFolder = Paths.get(geoDataLoadLocation);
        return Arrays
                .stream(parentFolder.toFile().listFiles())
                .filter(f -> f.isFile())
                .filter(f -> (f.getName().endsWith(".csv") || f.getName().endsWith(".CSV")))
                .max((f1, f2) -> Long.compare(f1.lastModified(),
                                f2.lastModified())).get();
    }
}
