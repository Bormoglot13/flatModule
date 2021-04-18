package com.example.demo;

import com.example.demo.dao.ModuleRepository;
import com.example.demo.model.Module;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.transaction.annotation.Transactional;
import java.text.SimpleDateFormat;

import java.util.stream.Stream;

@SpringBootApplication
@ComponentScan(basePackages = "com.*")
public class DemoApplication {

	//private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
	}

	/*@Bean
	public CommandLineRunner demo(ModuleRepository repository) {
		return new CommandLineRunner() {
			// readOnly for cancelling autoclose session
			@Override
			@Transactional(readOnly = true)
			public void run(String... args) throws Exception {

				System.out.println("\n1.findAll()...");
				for (Module module : repository.findAll()) {
					System.out.println(module);
				}

				System.out.println("\n3.findByDate(Date date)...");
				for (Module module : repository.findByDate(sdf.parse("2018-03-22"))) {
					System.out.println(module);
				}

				System.out.println("\n4.findByEmailReturnStream(@Param(\"email\") String email)...");
				try (Stream<Module> stream = repository.findByNameReturnStream("333@leodev.ru")) {
					stream.forEach(System.out::println);
				}

				System.out.println("Done!");
			}
		};
	}*/
}
