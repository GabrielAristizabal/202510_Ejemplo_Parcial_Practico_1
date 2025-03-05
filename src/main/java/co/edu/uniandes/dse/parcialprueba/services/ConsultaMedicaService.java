package co.edu.uniandes.dse.parcialprueba.services;

import java.util.Date;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import co.edu.uniandes.dse.parcialprueba.entities.ConsultaMedicaEntity;
import co.edu.uniandes.dse.parcialprueba.entities.PacienteEntity;
import co.edu.uniandes.dse.parcialprueba.exceptions.IllegalOperationException;
import co.edu.uniandes.dse.parcialprueba.repositories.ConsultaMedicaRepository;
import co.edu.uniandes.dse.parcialprueba.repositories.PacienteRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class ConsultaMedicaService {

    @Autowired
	ConsultaMedicaRepository consultaMedicaRepository;

    @Autowired
	PacienteRepository pacienteRepository;
    
    @Transactional
	public ConsultaMedicaEntity createConsultaMedica(ConsultaMedicaEntity consultaMedicaEntity) throws IllegalOperationException {
		log.info("Inicia proceso de creación de la consulta");
        if (consultaMedicaEntity.getPaciente() == null)
			throw new IllegalOperationException("Paciente no valido");
		
        Optional<PacienteEntity> pacienteEntity = pacienteRepository.findById(consultaMedicaEntity.getPaciente().getId());
        if (pacienteEntity.isEmpty())
			throw new IllegalOperationException("Paciente no valido");

		if (consultaMedicaEntity.getFecha().after(new Date()) == false)
			throw new IllegalOperationException("Fecha no valida");

		consultaMedicaEntity.setPaciente(pacienteEntity.get());
        log.info("Termina proceso de creación de la consulta");
		return consultaMedicaRepository.save(consultaMedicaEntity);
	}
}
