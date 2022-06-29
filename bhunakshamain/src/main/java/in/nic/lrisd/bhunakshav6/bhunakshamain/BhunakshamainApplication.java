package in.nic.lrisd.bhunakshav6.bhunakshamain;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@ComponentScan(basePackages = {"in.nic.lrisd.bhunakshav6.bhunakshacommon"})
@ComponentScan(basePackages = {"in.nic.lrisd.bhunakshav6.bhunakshamain"})
@ComponentScan(basePackages = {"in.nic.lrisd.bhunakshav6.state.statedataprovider"})
@SpringBootApplication
public class BhunakshamainApplication {

	public static void main(String[] args) {
		SpringApplication.run(BhunakshamainApplication.class, args);
	}
}
