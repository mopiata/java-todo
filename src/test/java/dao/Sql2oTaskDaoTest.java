package dao;

import models.Task;
import org.sql2o.*;
import org.junit.*;
import java.util.List;
import static org.junit.Assert.*;

public class Sql2oTaskDaoTest{
    private Sql2oTaskDao taskDao;
    private Connection conn;

    @Before
    public void setUp() throws Exception {
        String connectionString="jdbc:h2:mem:testing;INIT=RUNSCRIPT from 'classpath:db/create.sql'";
        Sql2o sql2o = new Sql2o(connectionString,"","");
        taskDao = new Sql2oTaskDao(sql2o);
        conn=sql2o.open();
    }

    @After
    public void tearDown() throws Exception {
        conn.close();
    }

    @Test
    public void addingTaskSetsId() throws Exception {
        Task task=new Task("mow the lawn");
        int originalTaskId=task.getId();
        taskDao.add(task);
        assertNotEquals(originalTaskId, task.getId());
    }

    @Test
    public void existingTasksCanBeFoundById() throws Exception {
        Task task = new Task("mow the lawn");
        taskDao.add(task);
        Task foundTask=taskDao.findById(task.getId());

        assertEquals(task, foundTask);
    }

    @Test
    public void getAll_returnsNothingIfNoTasks() throws Exception  {
        List<Task> tasks=taskDao.getAll();
        assertTrue(tasks.size()==0);
    }

    @Test
    public void getAll_returnsAllTaskInstances() throws Exception  {
        Task task = new Task("mow the lawn");
        Task otherTask= new Task("wash the car");

        taskDao.add(task);
        taskDao.add(otherTask);

        List<Task> tasks=taskDao.getAll();

        assertTrue(tasks.size()==2);
        assertTrue(tasks.contains(task));
        assertTrue(tasks.contains(otherTask));
    }
}