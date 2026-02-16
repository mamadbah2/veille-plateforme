package sn.ssi.veille;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import sn.ssi.veille.config.RSAKeysConfig;

@SpringBootApplication
@EnableScheduling
@EnableAsync
@EnableConfigurationProperties(RSAKeysConfig.class)
public class VeilleApplication {

	public static void main(String[] args) {
		SpringApplication.run(VeilleApplication.class, args);
	}

}
