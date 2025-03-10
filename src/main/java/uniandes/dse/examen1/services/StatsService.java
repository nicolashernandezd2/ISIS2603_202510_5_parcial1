package uniandes.dse.examen1.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;
import uniandes.dse.examen1.entities.CourseEntity;
import uniandes.dse.examen1.entities.RecordEntity;
import uniandes.dse.examen1.entities.StudentEntity;
import uniandes.dse.examen1.repositories.CourseRepository;
import uniandes.dse.examen1.repositories.StudentRepository;
import uniandes.dse.examen1.repositories.RecordRepository;

@Slf4j
@Service
public class StatsService {

    @Autowired
    StudentRepository estudianteRepository;

    @Autowired
    CourseRepository cursoRepository;

    @Autowired
    RecordRepository inscripcionRepository;

    public Double calculateStudentAverage(String login) {
        Optional<StudentEntity> estudiante = estudianteRepository.findByLogin(login);
        List<RecordEntity> registros = estudiante.get().getRegistros();
        Double suma_notas = 0.0;
        int numero_notas = 0;
        for (RecordEntity registro : registros) {
            suma_notas += registro.getFinalGrade();
            numero_notas++;
        }
        Double promedio = suma_notas / numero_notas;
        return promedio;
    }

    public Double calculateCourseAverage(String courseCode) {
        Optional<CourseEntity> curso = cursoRepository.findByCourseCode(courseCode);
        List<StudentEntity> estudiantes = curso.get().getEstudiantes();
        Double suma_notas = 0.0;
        int numero_notas = 0;
        for (StudentEntity estudiante : estudiantes) {
            List<RecordEntity> registros = estudiante.getRegistros();
            for (RecordEntity registro : registros) {
                if (registro.getCurso().getCourseCode().equals(courseCode)) {
                    suma_notas += registro.getFinalGrade();
                    numero_notas++;
                }
            }
        }
        Double promedio = suma_notas / numero_notas;
        return promedio;
    }

}
