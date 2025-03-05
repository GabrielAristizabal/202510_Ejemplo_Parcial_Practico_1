package co.edu.uniandes.dse.parcialprueba.services;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;

import co.edu.uniandes.dse.parcialprueba.entities.ConsultaMedicaEntity;
import co.edu.uniandes.dse.parcialprueba.entities.PacienteEntity;
import co.edu.uniandes.dse.parcialprueba.exceptions.EntityNotFoundException;
import co.edu.uniandes.dse.parcialprueba.exceptions.IllegalOperationException;
import jakarta.transaction.Transactional;
import uk.co.jemos.podam.api.PodamFactory;
import uk.co.jemos.podam.api.PodamFactoryImpl;

@DataJpaTest
@Transactional
@Import(ConsultaMedicaService.class)
public class ConsultaMedicaServiceTest {

    @Autowired
	private ConsultaMedicaService consultaMedicaService;

	@Autowired
	private TestEntityManager entityManager;

	private PodamFactory factory = new PodamFactoryImpl();

	private List<ConsultaMedicaEntity> consultaMedicaList = new ArrayList<>();
	private PacienteEntity pacienteEntity;

	@BeforeEach
	void setUp() {
		clearData();
		insertData();
	}

	private void clearData() {
		entityManager.getEntityManager().createQuery("delete from ConsultaMedicaEntity");
		entityManager.getEntityManager().createQuery("delete from PacienteEntity");
	}

	private void insertData() {
		
		pacienteEntity = factory.manufacturePojo(PacienteEntity.class);
		entityManager.persist(pacienteEntity);

		for (int i = 0; i < 3; i++) {
			ConsultaMedicaEntity consultaMedicaEntity = factory.manufacturePojo(ConsultaMedicaEntity.class);
			consultaMedicaEntity.setPaciente(pacienteEntity);
			entityManager.persist(pacienteEntity);
			consultaMedicaList.add(consultaMedicaEntity);
		}
    }

    @Test
	void testCreateConsulta() throws EntityNotFoundException, IllegalOperationException {
		ConsultaMedicaEntity newEntity = factory.manufacturePojo(ConsultaMedicaEntity.class);
		newEntity.setPaciente(pacienteEntity);
		ConsultaMedicaEntity result = consultaMedicaService.createConsultaMedica(newEntity);
		assertNotNull(result);
		ConsultaMedicaEntity entity = entityManager.find(ConsultaMedicaEntity.class, result.getId());
		assertEquals(newEntity.getId(), entity.getId());
		assertEquals(newEntity.getCausa(), entity.getCausa());
        assertEquals(newEntity.getFecha(), entity.getFecha());
        assertEquals(newEntity.getPaciente(), entity.getPaciente());
	}
}
