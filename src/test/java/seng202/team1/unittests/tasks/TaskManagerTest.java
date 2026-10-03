package seng202.team1.unittests.tasks;

import org.apache.commons.collections.list.SynchronizedList;
import org.junit.jupiter.api.BeforeEach;
import seng202.team1.tasks.GenericTask;
import seng202.team1.tasks.Task;
import seng202.team1.tasks.TaskManager;
import seng202.team1.unittests.TrashAppTest;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import static org.assertj.core.api.Fail.fail;
import static org.junit.jupiter.api.Assertions.*;

//@todo find better way to do task completeion tasking - waitFor(id)?

public class TaskManagerTest extends TrashAppTest {

    private TaskManager taskManager;

    @BeforeEach
    void init(){
        taskManager = new TaskManager(state);
    }

     @Test
    void runTaskShouldExecuteTheTaskFunction() throws Exception {

        // Run task
        AtomicBoolean ran = new AtomicBoolean(false);
        long testA = taskManager.runTask("a", m -> { ran.set(true);});

        Thread.sleep(500);
        assertTrue(ran.get());

     }

    @Test
    void runTaskShouldReturnDistinctIDsForEachTask() {
        long testA = taskManager.runTask("a", m -> {});
        long testB = taskManager.runTask("b", m -> {});

        assertNotEquals(testA, testB);

    }

    @Test
    void taskShouldNotBeRemainingAfterCompletion() throws Exception {


        // Run task
        AtomicBoolean ran = new AtomicBoolean(false);
        long testA = taskManager.runTask("a", m -> { ran.set(true);});
        assertTrue(taskManager.hasTasksUncompleted());


        Thread.sleep(500);
        assertFalse(taskManager.hasTasksUncompleted());

    }

    @Test
    void dependentTaskShouldNotStartUntilDependencyFinishes() throws Exception {

        List<String> order = Collections.synchronizedList(new ArrayList<>());

        // A takes longer so in theory if b doesnt wait it should be firstin list
        long testA = taskManager.runTask("a", m -> {
            try {
                Thread.sleep(200);

            } catch (InterruptedException ignored) {

            }
            order.add("a");
        });
        taskManager.runTask("b", m -> order.add("b"), testA);

        Thread.sleep(1000);
        assertEquals(List.of("a", "b"), order);
    }

    @Test
    void dependentTaskShouldStartImmediatelyIfDependencyAlreadyFinished() throws Exception {

        // Run task till end
        long testA = taskManager.runTask("a", m -> {});
        Thread.sleep(500);
        assertFalse(taskManager.hasTasksUncompleted());

        // B should run instead of waiting
        AtomicBoolean ran = new AtomicBoolean(false);
        taskManager.runTask("b", m -> { ran.set(true);}, testA);

        Thread.sleep(500);
        assertTrue(ran.get());
    }


    @Test
    void instantiateShouldAssignTheGivenID() {
        Task prefabA = new GenericTask(taskManager, "a", m -> {});
        Task instance = prefabA.instantiate(255L);

        assertEquals(255L, instance.getID());

    }


    @Test
    void instantiateShouldKeepTheTaskName() {
         Task prefabA = new GenericTask(taskManager, "a", m -> {});
        Task instance = prefabA.instantiate(255L);

        assertEquals("a", instance.getTaskName());

    }
}
