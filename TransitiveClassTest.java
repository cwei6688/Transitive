package mainClass;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.Set;
import java.util.TreeSet;

/**
 * Created on 7/10/16.
 */
public class TransitiveClassTest {
    @Before
    public void setUp() throws Exception {

    }

    @After
    public void tearDown() throws Exception {

    }


    @Test
    public void testTransitive(){
        Transitive test = new Transitive();
        try {
            test.addDirectDependencies("A", "BC");
            test.addDirectDependencies("B", "CE");
            test.addDirectDependencies("C", "G");
            test.addDirectDependencies("D", "AF");
            test.addDirectDependencies("E", "F");
            test.addDirectDependencies("F", "H");

            /*test.addDirectDependencies("A", "B");
            test.addDirectDependencies("B", "C");
            test.addDirectDependencies("C", "A"); */

            test.buildAllDependencies();

            Set<String> baseSet = new TreeSet<String>(test.getAllDependencies().keySet());

            for (String base: baseSet) {
                System.out.print(base + ": ");
                for (String dependent: test.getAllDependencies().get(base)) {
                    System.out.print(dependent);
                }
                System.out.println();
            }

        } catch (Transitive.CyclicDependencyException e) {
            System.out.println("dependency can not be processed due to cyclic dependency");
        }
    }
}
