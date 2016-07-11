package mainClass;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

/**
 * Created on 7/10/16.
 * Charles Wei
 */
public class Transitive {
    private Map<String, Set<String>> directDependencies = new HashMap<String, Set<String>>();
    private Map<String, Set<String>> allDependencies = new HashMap<String, Set<String>>();

    private Set<String> dependentsChain = new TreeSet<String>();

    public Map<String, Set<String>> getAllDependencies() {
        return allDependencies;
    }

    public class CyclicDependencyException extends Throwable {
    }

    /**
     * adding direct dependencies
     * @param base
     * @param dependencies
     * @throws CyclicDependencyException if any dependency is same as the base
     */
    public void addDirectDependencies(String base, String dependencies) throws CyclicDependencyException {
        Set<String> dependencySet = directDependencies.get(base);
        if (dependencySet == null) { // direct dependency does not exist yet
            dependencySet = new TreeSet<String>();
        }

        //add dependency
        for (int i=0; i< dependencies.length(); i++) {
            String nextDependent = dependencies.substring(i, (i+1));
            if (base.equalsIgnoreCase(nextDependent)) { //dependency can not be itself to avoid cyclic dependency
                throw new CyclicDependencyException();
            } else {
                dependencySet.add(nextDependent);
            }
        }

        //update dependency
        directDependencies.put(base, dependencySet);
    }

    /**
     * build dependencies for all the base from input
     * @throws CyclicDependencyException
     */
    public void buildAllDependencies() throws CyclicDependencyException {
        for (String base: directDependencies.keySet()) {
            Set<String> allDependenciesOfThis = findAllDependencies(base);
            allDependencies.put(base, allDependenciesOfThis);
        }
    }

    /**
     * Recursively finding all dependents of a base
     * @param base
     * @return all dependents
     * @throws CyclicDependencyException when any dependent is the base itself
     */
    public Set<String> findAllDependencies(String base) throws CyclicDependencyException {
        Set<String> directDependencySet = directDependencies.get(base);
        if (directDependencySet == null || directDependencySet.isEmpty()) { // direct dependency does not exist yet
            return directDependencySet;
        }else {
            if (! dependentsChain.add(base)) {
                // if the base already appeared in the chain of dependents search, this is a cyclic dependency
                throw new CyclicDependencyException();
            }
            Set<String> allDependencySet = new TreeSet<String>(directDependencySet);
            for (String directDependent : directDependencySet) {
                Set<String> grandDependentSet = findAllDependencies(directDependent);
                if (grandDependentSet == null || grandDependentSet.isEmpty()) {
                    continue;
                } else if (grandDependentSet.contains(base)) {
                    throw new CyclicDependencyException();
                }else {
                    allDependencySet.addAll(grandDependentSet);
                }

            }
            dependentsChain.clear(); //this dependency search is successful
            return allDependencySet;
        }
    }

    /*public static void main(String args[]) {
        Transitive test = new Transitive();
        try {
            test.addDirectDependencies("A", "BC");
            test.addDirectDependencies("B", "CE");
            test.addDirectDependencies("C", "G");
            test.addDirectDependencies("D", "AF");
            test.addDirectDependencies("E", "F");
            test.addDirectDependencies("F", "H");

            //cyclic dependency
            test.addDirectDependencies("A", "B");
            test.addDirectDependencies("B", "C");
            test.addDirectDependencies("C", "A");

            test.buildAllDependencies();

            Set<String> baseSet = new TreeSet<String>(test.allDependencies.keySet());

            for (String base: baseSet) {
                System.out.print(base);
                for (String dependent: test.allDependencies.get(base)) {
                    System.out.print(dependent);
                }
                //System.out.println();
            }

        } catch (CyclicDependencyException e) {
            System.out.println("dependency can not be processed due to cyclic dependency");
        }
    }*/


}
