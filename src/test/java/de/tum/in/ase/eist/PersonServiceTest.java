package de.tum.in.ase.eist;

import de.tum.in.ase.eist.model.Person;
import de.tum.in.ase.eist.repository.PersonRepository;
import de.tum.in.ase.eist.service.PersonService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureTestDatabase
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@AutoConfigureMockMvc
class PersonServiceTest {
    @Autowired
    private PersonService personService;
    @Autowired
    private PersonRepository personRepository;

    @Test
    void testAddPerson() {
        var person = new Person();
        person.setFirstName("Max");
        person.setLastName("Mustermann");
        person.setBirthday(LocalDate.now());

        personService.save(person);

        assertEquals(1, personRepository.findAll().size());
    }

    @Test
    void testDeletePerson() {
        var person = new Person();
        person.setFirstName("Max");
        person.setLastName("Mustermann");
        person.setBirthday(LocalDate.now());

        person = personRepository.save(person);

        personService.delete(person);

        assertTrue(personRepository.findAll().isEmpty());
    }

    // TODO: Add more test cases here
    @Test
    void testAddParent() {
        var child = new Person();
        var parent = new Person();
        personRepository.save(child);
        personRepository.save(parent);
        PersonService ps = new PersonService(personRepository);
        ps.addParent(child, parent);
        assertEquals(2, personRepository.findAll().size());
    }
    @Test
    void testAddThreeParents() {
        var child = new Person();
        var parent1 = new Person();
        var parent2 = new Person();
        var parent3 = new Person();
        personRepository.save(child);
        personRepository.save(parent1);
        personRepository.save(parent2);
        personRepository.save(parent3);
        PersonService ps = new PersonService(personRepository);
        ps.addParent(child, parent1);
        ps.addParent(child, parent2);
        assertEquals(4, personRepository.findAll().size());
        assertEquals(2, child.getParents().size());
        personService.addParent(child, parent3);
        assertThrows(ResponseStatusException.class, () -> {
            personService.addParent(child, parent3);
        }, "Adding more than two parents is not allowed.");
    }

}
