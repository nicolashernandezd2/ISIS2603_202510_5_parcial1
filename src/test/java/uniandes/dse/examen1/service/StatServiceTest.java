package uniandes.dse.examen1.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import jakarta.transaction.Transactional;
import uk.co.jemos.podam.api.PodamFactory;
import uk.co.jemos.podam.api.PodamFactoryImpl;
import uniandes.dse.examen1.entities.CourseEntity;
import uniandes.dse.examen1.entities.RecordEntity;
import uniandes.dse.examen1.entities.StudentEntity;
import uniandes.dse.examen1.exceptions.InvalidRecordException;
import uniandes.dse.examen1.exceptions.RepeatedCourseException;
import uniandes.dse.examen1.exceptions.RepeatedStudentException;
import uniandes.dse.examen1.repositories.CourseRepository;
import uniandes.dse.examen1.repositories.StudentRepository;
import uniandes.dse.examen1.services.CourseService;
import uniandes.dse.examen1.services.RecordService;
import uniandes.dse.examen1.services.StatsService;
import uniandes.dse.examen1.services.StudentService;

@DataJpaTest
@Transactional
@Import({ RecordService.class, CourseService.class, StudentService.class, StatsService.class })
public class StatServiceTest {

    @Autowired
    private RecordService recordService;

    @Autowired
    private CourseService courseService;

    @Autowired
    private StudentService studentService;

    @Autowired
    StudentRepository studentRepository;

    @Autowired
    private StatsService statsService;

    @Autowired
    CourseRepository courseRepository;

    private PodamFactory factory = new PodamFactoryImpl();

    @BeforeEach
    void setUp() throws RepeatedCourseException, RepeatedStudentException {
        
        
    }

    @Test
    void testCalculateStudentAverage() throws RepeatedStudentException, RepeatedCourseException, InvalidRecordException {
        StudentEntity estudiante = factory.manufacturePojo(StudentEntity.class);
        studentService.createStudent(estudiante);

        CourseEntity curso = factory.manufacturePojo(CourseEntity.class);
        courseService.createCourse(curso);
        CourseEntity curso2 = factory.manufacturePojo(CourseEntity.class);
        courseService.createCourse(curso2);

        
        RecordEntity record1 = recordService.createRecord(estudiante.getLogin(), curso.getCourseCode(), 4.5, "202010");

        RecordEntity record2 = recordService.createRecord(estudiante.getLogin(), curso2.getCourseCode(), 3.5, "202010");
        List<RecordEntity> registros = new ArrayList<>();
        registros.add(record1);
        registros.add(record2);
        estudiante.setRegistros(registros);
        Double average = statsService.calculateStudentAverage(estudiante.getLogin());
        assertEquals(4.0, average, "El promedio del estudiante no es correcto");
    }

    @Test
    void testCalculateCourseAverage() throws RepeatedStudentException, RepeatedCourseException, InvalidRecordException {
        StudentEntity estudiante = factory.manufacturePojo(StudentEntity.class);
        studentService.createStudent(estudiante);
        StudentEntity estudiante2 = factory.manufacturePojo(StudentEntity.class);
        studentService.createStudent(estudiante2);
        List<StudentEntity> estudiantes = new ArrayList<>();
        estudiantes.add(estudiante);
        estudiantes.add(estudiante2);
        CourseEntity curso = factory.manufacturePojo(CourseEntity.class);
        courseService.createCourse(curso);
        curso.setEstudiantes(estudiantes);
        
        RecordEntity record1 = recordService.createRecord(estudiante.getLogin(), curso.getCourseCode(), 4.8, "202010");

        RecordEntity record2 = recordService.createRecord(estudiante2.getLogin(), curso.getCourseCode(), 4.4, "202010");
        List<RecordEntity> registros = new ArrayList<>();
        registros.add(record1);
        registros.add(record2);
        estudiante.setRegistros(registros);
        Double average = statsService.calculateCourseAverage(curso.getCourseCode());
        assertEquals(4.6, average, "El promedio del curso no es correcto");
    }
}
