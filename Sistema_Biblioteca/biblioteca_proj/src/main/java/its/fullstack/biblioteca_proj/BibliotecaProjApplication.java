package its.fullstack.biblioteca_proj;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController  // Mantieni @RestController qui
@SpringBootApplication
public class BibliotecaProjApplication {

	public static void main(String[] args) {
		SpringApplication.run(BibliotecaProjApplication.class, args);
	}

	@GetMapping("/")  // Questo va bene
	public String home() {
		return "Benvenuto nel Sistema Biblioteca";
	}
}