package cn.satc.order;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.context.annotation.Configuration;

/**
 * @author 林浩捷
 */
@SpringBootApplication
@ConfigurationPropertiesScan
public class AutoOrderApplication {

	public static void main(String[] args) {
		SpringApplication.run(AutoOrderApplication.class, args);
	}

}
