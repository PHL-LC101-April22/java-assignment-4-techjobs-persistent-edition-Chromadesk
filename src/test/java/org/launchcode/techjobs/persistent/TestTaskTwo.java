package org.launchcode.techjobs.persistent;

import mockit.Expectations;
import mockit.Mocked;
import org.junit.jupiter.api.Test;
import org.launchcode.techjobs.persistent.controllers.UserController;
import org.launchcode.techjobs.persistent.controllers.SkillController;
import org.launchcode.techjobs.persistent.controllers.UserController;
import org.launchcode.techjobs.persistent.models.User;
import org.launchcode.techjobs.persistent.models.Skill;
import org.launchcode.techjobs.persistent.models.User;
import org.launchcode.techjobs.persistent.models.data.UserRepository;
import org.launchcode.techjobs.persistent.models.data.SkillRepository;
import org.launchcode.techjobs.persistent.models.data.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.ui.ExtendedModelMap;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.*;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Created by LaunchCode
 */
public class TestTaskTwo extends AbstractTest {

    // --- BEGIN AbstractEntity TESTS --- //

    /*
    * Verifies that AbstractEntity has @MappedSuperclass
    * */
    @Test
    public void testAbstractEntityHasCorrectAnnotation () throws ClassNotFoundException {
        Class abstractEntityClass = getClassByName("models.AbstractEntity");
        Annotation annotation = abstractEntityClass.getAnnotation(MappedSuperclass.class);
        assertNotNull(annotation, "AbstractEntity must have @MappedSuperclass annotation");
    }

    /*
    * Verifies that AbstractEntity.id has correct annotations
    * */
    @Test
    public void testIdFieldHasCorrectAnnotations () throws ClassNotFoundException {
        Class abstractEntityClass = getClassByName("models.AbstractEntity");
        Field idField = null;
        try {
            idField = abstractEntityClass.getDeclaredField("id");
        } catch (NoSuchFieldException e) {
            fail("AbstractEntity does not have an id field");
        }

        Annotation idAnnotation = idField.getAnnotation(Id.class);
        assertNotNull(idAnnotation, "id field must have @Id annotation");

        Annotation generatedValueAnnotation = idField.getAnnotation(GeneratedValue.class);
        assertNotNull(generatedValueAnnotation, "id field must have @GeneratedValue annotation");
    }

    /*
    * Verifies that AbstractEntity.name has correct annotations
    * */
    @Test
    public void testNameFieldHasCorrectAnnotations () throws ClassNotFoundException {
        Class abstractEntityClass = getClassByName("models.AbstractEntity");
        Field nameField = null;
        try {
            nameField = abstractEntityClass.getDeclaredField("name");
        } catch (NoSuchFieldException e) {
            fail("AbstractEntity does not have a name field");
        }

        Annotation sizeAnnotation = nameField.getAnnotation(Size.class);
        assertNotNull(sizeAnnotation, "name field must use @Size to validate input");

        // we allow for either @NotBlank or @NotNull to ensure the field is not empty
        Annotation notEmptyAnnotation = nameField.getAnnotation(NotNull.class);
        if (notEmptyAnnotation == null) {
            notEmptyAnnotation = nameField.getAnnotation(NotBlank.class);
        }

        assertNotNull(notEmptyAnnotation, "name must have an annotation to ensure the field is not empty");
    }

    // --- END AbstractEntity TESTS --- //

    // --- BEGIN User TESTS --- //

    /*
    * Verifies that User has the persistence annotation
    * */
    @Test
    public void testUserHasPersistenceAnnotation () throws ClassNotFoundException {
        Class userClass = getClassByName("models.User");
        Annotation entityAnnotation = userClass.getAnnotation(Entity.class);
        assertNotNull(entityAnnotation, "User must have the @Entity persistence annotation");
    }

    /*
    * Verifies that User has a no-arg/default constructor
    * */
    @Test
    public void testUserHasDefaultConstructor () throws ClassNotFoundException {
        Class userClass = getClassByName("models.User");
        try {
            Constructor defaultConstructor = userClass.getDeclaredConstructor();
        } catch (NoSuchMethodException e) {
            fail("User has no no-arg/default constructor");
        }
    }

    // --- END User TESTS --- //

    // --- START Skill TESTS --- //

    /*
     * Verifies that Skill has a description field
     * */
    @Test
    public void testSkillHasDescriptionField () throws ClassNotFoundException {
        Class skillClass = getClassByName("models.Skill");
        Field descriptionField = null;
        try {
            descriptionField = skillClass.getDeclaredField("description");
        } catch (NoSuchFieldException e) {
            fail("Skill class has no description field");
        }

        Class descriptionClass = descriptionField.getType();
        assertEquals(String.class, descriptionClass);
    }

    /*
     * Verifies that Skill.description has public accessors
     * */
    @Test
    public void testDescriptionFieldHasPublicAccessors () throws ClassNotFoundException, NoSuchFieldException {
        Class skillClass = getClassByName("models.Skill");
        Field descriptionField = skillClass.getDeclaredField("description");

        Method getDescriptionMethod = null;
        try {
            getDescriptionMethod = skillClass.getDeclaredMethod("getDescription");
        } catch (NoSuchMethodException e) {
            fail("Skill class has no getDescription method");
        }
        int getDescriptionModifier = getDescriptionMethod.getModifiers();
        assertEquals(Modifier.PUBLIC, getDescriptionModifier, "getDescription must be public");

        Method setDescriptionMethod = null;
        try {
            setDescriptionMethod = skillClass.getDeclaredMethod("setDescription", String.class);
        } catch (NoSuchMethodException e) {
            fail("Skill class has no setDescription method");
        }

        int setDescriptionModifier = setDescriptionMethod.getModifiers();
        assertEquals(Modifier.PUBLIC, setDescriptionModifier, "setDescription must be public");
    }

    /*
     * Verifies that Skill has the persistence annotation
     * */
    @Test
    public void testSkillHasPersistenceAnnotation () throws ClassNotFoundException {
        Class skillClass = getClassByName("models.Skill");
        Annotation entityAnnotation = skillClass.getAnnotation(Entity.class);
        assertNotNull(entityAnnotation, "Skill must have the @Entity persistence annotation");
    }

    /*
     * Verifies that Skill has a no-arg/default constructor
     * */
    @Test
    public void testSkillHasDefaultConstructor () throws ClassNotFoundException {
        Class skillClass = getClassByName("models.Skill");
        try {
            Constructor defaultConstructor = skillClass.getDeclaredConstructor();
        } catch (NoSuchMethodException e) {
            fail("Skill has no no-arg/default constructor");
        }
    }

    // --- END Skill TESTS --- //

    // --- BEGIN DATA LAYER TESTS --- //

    /*
    * Verifies that UserRepository exists
    * */
    @Test
    public void testUserRepositoryExists () {
        try {
            Class userRepositoryClass = getClassByName("models.data.UserRepository");
        } catch (ClassNotFoundException e) {
            fail("UserRepository does not exist");
        }
    }

    /*
     * Verifies that UserRepository implements CrudRepository
     * */
    @Test
    public void testUserRepositoryImplementsJpaInterface () throws ClassNotFoundException {
        Class userRepositoryClass = getClassByName("models.data.UserRepository");
        Class[] interfaces = userRepositoryClass.getInterfaces();
        assertTrue(Arrays.asList(interfaces).contains(CrudRepository.class), "UserRepository must implement CrudRepository");
    }

    /*
     * Verifies that UserRepository has @Repository
     * */
    @Test
    public void testUserRepositoryHasRepositoryAnnotation () throws ClassNotFoundException {
        Class userRepositoryClass = getClassByName("models.data.UserRepository");
        Annotation annotation = userRepositoryClass.getAnnotation(Repository.class);
    }

    /*
     * Verifies that SkillRepository exists
     * */
    @Test
    public void testSkillRepositoryExists () {
        try {
            Class skillRepositoryClass = getClassByName("models.data.SkillRepository");
        } catch (ClassNotFoundException e) {
            fail("SkillRepository does not exist");
        }
    }

    /*
     * Verifies that SkillRepository implements CrudRepository
     * */
    @Test
    public void testSkillRepositoryImplementsJpaInterface () throws ClassNotFoundException {
        Class skillRepositoryClass = getClassByName("models.data.SkillRepository");
        Class[] interfaces = skillRepositoryClass.getInterfaces();
        assertTrue(Arrays.asList(interfaces).contains(CrudRepository.class), "SkillRepository must implement CrudRepository");
    }

    /*
     * Verifies that SkillRepository has @Repository
     * */
    @Test
    public void testSkillRepositoryHasRepositoryAnnotation () throws ClassNotFoundException {
        Class skillRepositoryClass = getClassByName("models.data.SkillRepository");
        Annotation annotation = skillRepositoryClass.getAnnotation(Repository.class);
    }

    // --- END DATA LAYER TESTS --- //

    // --- BEGIN CONTROLLER TESTS --- //

    /*
    * Verifies that the userRepository field is correctly defined
    * */
    @Test
    public void testUserRepositoryDefinition () throws ClassNotFoundException {
        Class userController = getClassByName("controllers.UserController");
        Field userRepositoryField = null;

        try {
            userRepositoryField = userController.getDeclaredField("userRepository");
        } catch (NoSuchFieldException e) {
            fail("UserController does not have an userRepository field");
        }

        assertEquals(UserRepository.class, userRepositoryField.getType(), "userRepository must be of type UserRepository");
        assertNotNull(userRepositoryField.getAnnotation(Autowired.class), "userRepository must have the @Autowired annotation");
    }

    /*
    * Verifies that UserController.index is properly defined
    * */
    @Test
    public void testUserControllerIndexMethodDefinition (@Mocked UserRepository userRepository) throws ClassNotFoundException, NoSuchMethodException, InvocationTargetException, IllegalAccessException, NoSuchFieldException {
        Class userControllerClass = getClassByName("controllers.UserController");
        Method indexMethod = null;

        // Verify that the index method exists
        try {
            indexMethod = userControllerClass.getMethod("index", Model.class);
        } catch (NoSuchMethodException e) {
            fail("UserController must have an index method that takes a parameter of type Model");
        }

        Annotation annotation = indexMethod.getDeclaredAnnotation(RequestMapping.class);

        // Verify that index has a routing annotation. We need to accommodate
        // both @RequestMapping and @GetMapping.
        if (annotation == null) {
            annotation = indexMethod.getDeclaredAnnotation(GetMapping.class);
        }

        assertNotNull(annotation, "index method must have a routing annotation");

        Method annotationValueMethod = annotation.getClass().getMethod("value");
        String[] values = ((String[]) annotationValueMethod.invoke(annotation));
        assertEquals(1, values.length, "The routing annotation for index must have a value");
        assertEquals("", values[0], "The value parameter for the routing annotation must be the empty string");

        // Verify that index calls userRepository.findAll()
        new Expectations() {{
            userRepository.findAll();
        }};

        Model model = new ExtendedModelMap();
        UserController userController = new UserController();
        Field userRepositoryField = userControllerClass.getDeclaredField("userRepository");
        userRepositoryField.setAccessible(true);
        userRepositoryField.set(userController, userRepository);
        indexMethod.invoke(userController, model);
    }

    /*
    * Verify that processAddUserForm saves a new user to the database
    * */
    @Test
    public void testNewUserIsSaved (@Mocked UserRepository userRepository, @Mocked Errors errors) throws ClassNotFoundException, NoSuchMethodException, NoSuchFieldException, IllegalAccessException, InvocationTargetException {
        Class userControllerClass = getClassByName("controllers.UserController");
        Method processAddUserFormMethod = userControllerClass.getMethod("processAddUserForm", User.class, Errors.class, Model.class);
        Method saveMethod = UserRepository.class.getMethod("save", Object.class);

        User user = new User();
        user.setName("LaunchCode");

        new Expectations() {{
            saveMethod.invoke(userRepository, user);
        }};

        Model model = new ExtendedModelMap();
        UserController userController = new UserController();
        Field userRepositoryField = userControllerClass.getDeclaredField("userRepository");
        userRepositoryField.setAccessible(true);
        userRepositoryField.set(userController, userRepository);
        processAddUserFormMethod.invoke(userController, user, errors, model);
    }

    /*
    * Verifies that displayViewUser calls findById to retrieve an user object
    * */
    @Test
    public void testDisplayViewUserCallsFindById (@Mocked UserRepository userRepository) throws ClassNotFoundException, NoSuchMethodException, NoSuchFieldException, IllegalAccessException, InvocationTargetException {
        Class userControllerClass = getClassByName("controllers.UserController");
        Method displayViewUserMethod = userControllerClass.getMethod("displayViewUser", Model.class, int.class);

        new Expectations() {{
           userRepository.findById(1);
        }};

        Model model = new ExtendedModelMap();
        UserController userController = new UserController();
        Field userRepositoryField = userControllerClass.getDeclaredField("userRepository");
        userRepositoryField.setAccessible(true);
        userRepositoryField.set(userController, userRepository);
        displayViewUserMethod.invoke(userController, model, 1);
    }

    /*
     * Verifies that the skillRepository field is correctly defined
     * */
    @Test
    public void testSkillRepositoryDefinition () throws ClassNotFoundException {
        Class skillController = getClassByName("controllers.SkillController");
        Field skillRepositoryField = null;

        try {
            skillRepositoryField = skillController.getDeclaredField("skillRepository");
        } catch (NoSuchFieldException e) {
            fail("SkillController does not have an skillRepository field");
        }

        assertEquals(SkillRepository.class, skillRepositoryField.getType(), "skillRepository must be of type SkillRepository");
        assertNotNull(skillRepositoryField.getAnnotation(Autowired.class), "skillRepository must have the @Autowired annotation");
    }

    /*
     * Verifies that SkillController.index is properly defined
     * */
    @Test
    public void testSkillControllerIndexMethodDefinition (@Mocked SkillRepository skillRepository) throws ClassNotFoundException, NoSuchMethodException, InvocationTargetException, IllegalAccessException, NoSuchFieldException {
        Class skillControllerClass = getClassByName("controllers.SkillController");
        Method indexMethod = null;

        // Verify that the index method exists
        try {
            indexMethod = skillControllerClass.getMethod("index", Model.class);
        } catch (NoSuchMethodException e) {
            fail("SkillController must have an index method that takes a parameter of type Model");
        }

        Annotation annotation = indexMethod.getDeclaredAnnotation(RequestMapping.class);

        // Verify that index has a routing annotation. We need to accommodate
        // both @RequestMapping and @GetMapping.
        if (annotation == null) {
            annotation = indexMethod.getDeclaredAnnotation(GetMapping.class);
        }

        assertNotNull(annotation, "index method must have a routing annotation");

        Method annotationValueMethod = annotation.getClass().getMethod("value");
        String[] values = ((String[]) annotationValueMethod.invoke(annotation));
        assertEquals(1, values.length, "The routing annotation for index must have a value");
        assertEquals("", values[0], "The value parameter for the routing annotation must be the empty string");

        // Verify that index calls skillRepository.findAll()
        new Expectations() {{
            skillRepository.findAll();
        }};

        Model model = new ExtendedModelMap();
        SkillController skillController = new SkillController();
        Field skillRepositoryField = skillControllerClass.getDeclaredField("skillRepository");
        skillRepositoryField.setAccessible(true);
        skillRepositoryField.set(skillController, skillRepository);
        indexMethod.invoke(skillController, model);
    }

    /*
     * Verify that processAddSkillForm saves a new skill to the database
     * */
    @Test
    public void testNewSkillIsSaved (@Mocked SkillRepository skillRepository, @Mocked Errors errors) throws ClassNotFoundException, NoSuchMethodException, NoSuchFieldException, IllegalAccessException, InvocationTargetException {
        Class skillControllerClass = getClassByName("controllers.SkillController");
        Method processAddSkillFormMethod = skillControllerClass.getMethod("processAddSkillForm", Skill.class, Errors.class, Model.class);
        Method saveMethod = SkillRepository.class.getMethod("save", Object.class);

        Skill skill = new Skill();
        skill.setName("Java");

        new Expectations() {{
            saveMethod.invoke(skillRepository, skill);
        }};

        Model model = new ExtendedModelMap();
        SkillController skillController = new SkillController();
        Field skillRepositoryField = skillControllerClass.getDeclaredField("skillRepository");
        skillRepositoryField.setAccessible(true);
        skillRepositoryField.set(skillController, skillRepository);
        processAddSkillFormMethod.invoke(skillController, skill, errors, model);
    }

    /*
     * Verifies that displayViewSkill calls findById to retrieve an skill object
     * */
    @Test
    public void testDisplayViewSkillCallsFindById (@Mocked SkillRepository skillRepository) throws ClassNotFoundException, NoSuchMethodException, NoSuchFieldException, IllegalAccessException, InvocationTargetException {
        Class skillControllerClass = getClassByName("controllers.SkillController");
        Method displayViewSkillMethod = skillControllerClass.getMethod("displayViewSkill", Model.class, int.class);

        new Expectations() {{
            skillRepository.findById(1);
        }};

        Model model = new ExtendedModelMap();
        SkillController skillController = new SkillController();
        Field skillRepositoryField = skillControllerClass.getDeclaredField("skillRepository");
        skillRepositoryField.setAccessible(true);
        skillRepositoryField.set(skillController, skillRepository);
        displayViewSkillMethod.invoke(skillController, model, 1);
    }

    // --- END CONTROLLER TESTS --- //

    /*
    * Tests SQL query for task 2
    * */
    @Test
    public void testSqlQuery() throws IOException {
        String queryFileContents = getFileContents("queries.sql");

        Pattern queryPattern = Pattern.compile("SELECT\\s+name\\s+FROM\\s+job\\s+WHERE\\s+location\\s+=\\s+\"St.\\s+Louis\\s+City\";", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE);
        Matcher queryMatcher = queryPattern.matcher(queryFileContents);
        boolean queryFound = queryMatcher.find();
        assertTrue(queryFound, "Task 2 SQL query is incorrect. Test your query against your database to find the error.");
    }
}
