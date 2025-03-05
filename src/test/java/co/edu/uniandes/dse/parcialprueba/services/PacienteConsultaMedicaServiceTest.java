package co.edu.uniandes.dse.parcialprueba.services;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import co.edu.uniandes.dse.parcialprueba.entities.ConsultaMedicaEntity;
import co.edu.uniandes.dse.parcialprueba.entities.PacienteEntity;
import co.edu.uniandes.dse.parcialprueba.exceptions.EntityNotFoundException;
import jakarta.transaction.Transactional;
import uk.co.jemos.podam.api.PodamFactory;
import uk.co.jemos.podam.api.PodamFactoryImpl;

@DataJpaTest
@Transactional
@Import(PacienteConsultaMedicaService.class)
public class PacienteConsultaMedicaServiceTest {

    @Autowired
	private PacienteConsultaMedicaService pacienteConsultaService;

	private PodamFactory factory = new PodamFactoryImpl();

	private List<PacienteEntity> pacienteList = new ArrayList<>();
	private List<ConsultaMedicaEntity> consultaMedicaList = new ArrayList<>();

    @Test
	void testAddColsulta() throws EntityNotFoundException {
		PacienteEntity entity = pacienteList.get(0);
		ConsultaMedicaEntity consultaMedicaEntity = consultaMedicaList.get(1);
		ConsultaMedicaEntity response = pacienteConsultaService.addConsulta(consultaMedicaEntity.getId(), entity.getId());

		assertNotNull(response);
		assertEquals(consultaMedicaEntity.getId(), response.getId());
	}

    @Test
	void testAddConsultaFechaInvalida() {
		assertThrows(EntityNotFoundException.class, () -> {
			PacienteEntity entity = pacienteList.get(0);
            ConsultaMedicaEntity consultaMedicaEntity = consultaMedicaList.get(1);
            consultaMedicaEntity.setFecha(new Date(0));
            ConsultaMedicaEntity consultaMedicaEntity2 = consultaMedicaList.get(2);
            consultaMedicaEntity2.setFecha(new Date(0));
            pacienteConsultaService.addConsulta(consultaMedicaEntity.getId(), entity.getId());
            pacienteConsultaService.addConsulta(consultaMedicaEntity2.getId(), entity.getId());
		});
	}
}
