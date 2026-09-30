package br.com.mauricio.contas;

import static org.mockito.Mockito.mockStatic;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.springframework.boot.SpringApplication;

class ContasApiApplicationTest {

	@Test
	@DisplayName("Deve iniciar a aplicação Spring")
	void deveIniciarAplicacaoSpring() {
		String[] argumentos = {};

		try (MockedStatic<SpringApplication> springApplication = mockStatic(SpringApplication.class)) {
			ContasApiApplication.main(argumentos);

			springApplication.verify(() -> SpringApplication.run(ContasApiApplication.class, argumentos));
		}
	}
}
