package uniandes.dse.examen1.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;
import uniandes.dse.examen1.entities.CourseEntity;
import uniandes.dse.examen1.entities.StudentEntity;
import uniandes.dse.examen1.entities.RecordEntity;
import uniandes.dse.examen1.exceptions.InvalidRecordException;
import uniandes.dse.examen1.exceptions.RepeatedCourseException;
import uniandes.dse.examen1.repositories.CourseRepository;
import uniandes.dse.examen1.repositories.StudentRepository;
import uniandes.dse.examen1.repositories.RecordRepository;

@Slf4j
@Service
public class RecordService {

    @Autowired
    StudentRepository studentRepository;

    @Autowired
    CourseRepository courseRepository;

    @Autowired
    RecordRepository recordRepository;

    public RecordEntity createRecord(String loginStudent, String courseCode, Double grade, String semester)
            throws InvalidRecordException {
        log.info("Inicia proceso de creación del registro");
        if (studentRepository.findByLogin(loginStudent) == null)
            throw new InvalidRecordException("El estudiante no existe");
        if (courseRepository.findByCourseCode(courseCode) == null)
            throw new InvalidRecordException("El curso no existe");
        if (grade < 1.5 || grade > 5.0)
            throw new InvalidRecordException("La nota debe estar entre 1.5 y 5");
        RecordEntity newRecord = new RecordEntity();
        StudentEntity estudiante = new StudentEntity();
        CourseEntity curso = new CourseEntity();
        newRecord.setEstudiante(estudiante);
        newRecord.setCurso(curso);
        newRecord.setFinalGrade(grade);
        newRecord.setSemester(semester);
        log.info("Termina proceso de creación del registro");
        return recordRepository.save(newRecord);
    }
}
