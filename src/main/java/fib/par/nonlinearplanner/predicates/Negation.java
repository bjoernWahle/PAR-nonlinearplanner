package fib.par.nonlinearplanner.predicates;

public class Negation extends Predicate {
    public Predicate predicate;

    public Negation(Predicate predicate) {
        this.predicate = predicate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Negation negation = (Negation) o;

        return predicate != null ? predicate.equals(negation.predicate) : negation.predicate == null;
    }

    @Override
    public int hashCode() {
        return predicate != null ? predicate.hashCode() : 0;
    }

    @Override
    public String toString() {
        return super.toString();
    }
}
