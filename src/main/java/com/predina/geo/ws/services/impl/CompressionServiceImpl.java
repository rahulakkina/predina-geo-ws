package com.predina.geo.ws.services.impl;


import com.predina.geo.ws.services.CompressionService;
import org.apache.commons.compress.archivers.sevenz.SevenZArchiveEntry;
import org.apache.commons.compress.archivers.sevenz.SevenZOutputFile;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.GZIPOutputStream;

/**
 * @author Rahul Anand Akkina
 * @version v1.0
 */
@Component
public class CompressionServiceImpl implements CompressionService {

    private static final Logger logger = Logger.getLogger(CompressionServiceImpl.class);

    public CompressionServiceImpl(){}

    public String gzipIt(String sourceFile) {

        byte[] buffer = new byte[1024];
        GZIPOutputStream gzos = null;
        FileInputStream in = null;
        final String zippedFile = sourceFile + "." + "gz";

        try {

            gzos = new GZIPOutputStream(new FileOutputStream(zippedFile));
            in = new FileInputStream(sourceFile);

            int len;
            while ((len = in.read(buffer)) > 0) {
                gzos.write(buffer, 0, len);
            }

            gzos.finish();

            logger.info(sourceFile + " File (gz) compressed with size "+fileSizeInKb(zippedFile)+" KB.");

        } catch (IOException e) {
            logger.error(e.getMessage(), e);
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    logger.error(e.getMessage(), e);
                }
            }

            if (gzos != null) {
                try {
                    gzos.close();
                } catch (IOException e) {
                    logger.error(e.getMessage(), e);
                }
            }
        }
        return zippedFile;
    }

    public String lzmaIt(String sourceFile) {

        byte[] buffer = new byte[1024];
        FileInputStream in = null;
        SevenZOutputFile sevenZOutput = null;
        SevenZArchiveEntry entry = null;

        final String zippedFile = sourceFile + "." + "7z";

        try {
            sevenZOutput = new SevenZOutputFile(new File(zippedFile));
            entry = sevenZOutput.createArchiveEntry(new File(sourceFile), getSimpleFileName(sourceFile));
            sevenZOutput.putArchiveEntry(entry);

            in = new FileInputStream(sourceFile);

            int len;
            while ((len = in.read(buffer)) > 0) {
                sevenZOutput.write(buffer, 0, len);
            }

            sevenZOutput.closeArchiveEntry();
            sevenZOutput.finish();

            logger.info(sourceFile + " File (7z) compressed with size "+fileSizeInKb(zippedFile)+" KB.");

        } catch (IOException e) {
            logger.error(e.getMessage(), e);
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    logger.error(e.getMessage(), e);
                }
            }

            if (sevenZOutput != null) {
                try {
                    sevenZOutput.close();
                } catch (IOException e) {
                    logger.error(e.getMessage(), e);
                }
            }

            if (sevenZOutput != null) {
                try {
                    sevenZOutput.close();
                } catch (IOException e) {
                    logger.error(e.getMessage(), e);
                }
            }
        }
        return zippedFile;
    }
}
