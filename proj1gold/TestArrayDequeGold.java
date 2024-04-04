import static org.junit.Assert.*;
import org.junit.Test;

public class TestArrayDequeGold {

    @Test
    public void testStudentArrayDeque() {
        StudentArrayDeque<Integer> student = new StudentArrayDeque<>();
        ArrayDequeSolution<Integer> correct = new ArrayDequeSolution<>();

        String message = "";

        int numTest = 200;
        int numRange = 100;
        /* @source StudentArrayDequeLauncher */
        for (int i = 0; i < numTest; i += 1) {
            double numberBetweenZeroAndOne = StdRandom.uniform();
            int testRandomInt = StdRandom.uniform(numRange); // add num ranging from 0 to numRange
            if (numberBetweenZeroAndOne < 0.25) {
                student.addLast(testRandomInt);
                correct.addLast(testRandomInt);
                message += "addLast(" + testRandomInt + ")\n";
            } else if (numberBetweenZeroAndOne < 0.5) {
                student.addFirst(testRandomInt);
                correct.addFirst(testRandomInt);
                message += "addFirst(" + testRandomInt + ")\n";
            } else if (numberBetweenZeroAndOne < 0.75 && !student.isEmpty()) {
                Integer studentRemoveFirst = student.removeFirst();
                Integer correctRemoveFirst = correct.removeFirst();
                message += "removeFirst()\n";
                assertEquals(message, studentRemoveFirst, correctRemoveFirst);
            } else if (numberBetweenZeroAndOne < 1 && !student.isEmpty()) {
                Integer studentRemoveLast = student.removeLast();
                Integer correctRemoveLast = correct.removeLast();
                message += "removeLast()\n";
                assertEquals(message, studentRemoveLast, correctRemoveLast);
            }
        }
    }
}
