package uniandes.dse.examen1.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.fail;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;

import jakarta.transaction.Transactional;
import uk.co.jemos.podam.api.PodamFactory;
import uk.co.jemos.podam.api.PodamFactoryImpl;
import uniandes.dse.examen1.entities.CourseEntity;
import uniandes.dse.examen1.exceptions.RepeatedCourseException;
import uniandes.dse.examen1.services.CourseService;

@DataJpaTest
@Transactional
@Import(CourseService.class)
public class CourseServiceTest {

    @Autowired
    private CourseService courseService;

    @Autowired
    private TestEntityManager entityManager;

    private PodamFactory factory = new PodamFactoryImpl();

    private CourseEntity course;

    @BeforeEach
    void setUp() {
        
    }

    @Test
    void testCreateCourse() {
        course = factory.manufacturePojo(CourseEntity.class);
        try {
            CourseEntity storedEntity = courseService.createCourse(course);
            CourseEntity retrieved = entityManager.find(CourseEntity.class, storedEntity.getId());
            assertEquals(course.getCourseCode(), retrieved.getCourseCode(), "El codigo del curso no es correcto");
        } catch (RepeatedCourseException e) {
            fail("No exception should be thrown: " + e.getMessage());
        }
    }

    @Test
    void testCreateRepeatedCourse() {
        CourseEntity firstEntity = factory.manufacturePojo(CourseEntity.class);
        String courseCode = firstEntity.getCourseCode();

        CourseEntity repeatedEntity = new CourseEntity();
        repeatedEntity.setCourseCode(courseCode);
        repeatedEntity.setName(firstEntity.getName());

        try {
            courseService.createCourse(firstEntity);
            courseService.createCourse(repeatedEntity);
            fail("An exception must be thrown");
        } catch (RepeatedCourseException e) {
            assertEquals(courseCode, repeatedEntity.getCourseCode(), "El codigo del curso no es correcto");
        }
    }
}
