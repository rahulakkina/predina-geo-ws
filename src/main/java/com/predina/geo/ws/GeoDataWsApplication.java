package com.predina.geo.ws;

import org.apache.log4j.Logger;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * @author Rahul Anand Akkina
 * @version v1.0
 */
@SpringBootApplication(exclude = SecurityAutoConfiguration.class)
@EnableScheduling
public class GeoDataWsApplication {

	private static Logger logger = Logger.getLogger(GeoDataWsApplication.class);

	public static void main(String[] args) {
		final ApplicationContext ctx = SpringApplication.run(GeoDataWsApplication.class, args);
	}
}
