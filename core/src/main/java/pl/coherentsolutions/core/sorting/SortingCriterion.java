package pl.coherentsolutions.core.sorting;

public class SortingCriterion {
    private final String criterion;
    private final Sorting sorting;

    public SortingCriterion(String sortingCriterion, Sorting sorting) {
        this.criterion = sortingCriterion;
        this.sorting = sorting;
    }

    public String getSortingCriterion() {
        return criterion;
    }

    public Sorting getSorting() {
        return sorting;
    }

    @Override
    public String toString() {
        return "SortingCriterion: {" +
                "criterion='" + criterion + '\'' +
                ", sorting=" + sorting +
                '}';
    }
}
