package co.edu.uniandes.dse.parcialprueba.services;

import java.sql.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import co.edu.uniandes.dse.parcialprueba.entities.ConsultaMedicaEntity;
import co.edu.uniandes.dse.parcialprueba.entities.PacienteEntity;
import co.edu.uniandes.dse.parcialprueba.exceptions.EntityNotFoundException;
import co.edu.uniandes.dse.parcialprueba.repositories.ConsultaMedicaRepository;
import co.edu.uniandes.dse.parcialprueba.repositories.PacienteRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class PacienteConsultaMedicaService {

    @Autowired
	private ConsultaMedicaRepository consultaMedicaRepository;

	@Autowired
	private PacienteRepository pacienteRepository;

    @Transactional
	public ConsultaMedicaEntity addConsulta(Long consultaId, Long pacienteId) throws EntityNotFoundException {
		log.info("Inicia proceso de agregarle una consulta al paciente con id = {0}", pacienteId);
		
		Optional<ConsultaMedicaEntity> consultaMedicaEntity = consultaMedicaRepository.findById(consultaId);
		if(consultaMedicaEntity.isEmpty())
			throw new EntityNotFoundException("Consulta no encontrada");
		
		Optional<PacienteEntity> pacienteEntity = pacienteRepository.findById(pacienteId);
		if(pacienteEntity.isEmpty())
			throw new EntityNotFoundException("Paciente no encontrado");
		
		consultaMedicaEntity.get().setPaciente(pacienteEntity.get());
		log.info("Termina proceso de agregarle una consulta al paciente con id = {0}", pacienteId);
		return consultaMedicaEntity.get();
	}

    @Transactional
	public List<ConsultaMedicaEntity> getConsultasProgramadas(Long pacienteId) throws EntityNotFoundException {
		log.info("Inicia proceso de consultar las consultas programas asociadas con el paciente con id = {0}", pacienteId);
		Optional<PacienteEntity> pacienteEntity = pacienteRepository.findById(pacienteId);
		if(pacienteEntity.isEmpty())
			throw new EntityNotFoundException("Paciente no encontrado");
        
        List<ConsultaMedicaEntity> consultas = pacienteEntity.get().getConsultasMedicas();
        for (ConsultaMedicaEntity consulta : consultas) {
            if (consulta.getFecha().before(new Date(pacienteId))) {
                consultas.remove(consulta);
            }
        }
        log.info("Termina proceso de consultar las consultas programas asociadas con el paciente con id = {0}", pacienteId);
		return consultas;
	}
}
