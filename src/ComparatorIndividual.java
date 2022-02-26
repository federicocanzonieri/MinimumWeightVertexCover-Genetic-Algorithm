import java.util.Comparator;

public class ComparatorIndividual implements Comparator<Individual> {
    @Override
    public int compare(Individual a, Individual b) {
        return a.compareTo(b);
    }
}