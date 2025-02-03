package br.com.fga;

import br.com.fga.utils.ThreadUtils;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class DemoApplication {

	public static void main(String[] args) throws InterruptedException {
		SpringApplication.run(DemoApplication.class, args);

//		Thread serverThread = new Thread(() -> SpringApplication.run(DemoApplication.class, args));
//		Thread agentsThread = new Thread(StartAgentApplication::bootstrap);
//
//		serverThread.start();
//		// Espera o servidor subir
//		ThreadUtils.sleep(5);
//
//		agentsThread.start();
	}

}
