package pl.coherentsolutions.service.product;

import pl.coherentsolutions.core.product.Product;
import pl.coherentsolutions.core.sorting.Sorting;
import pl.coherentsolutions.core.sorting.SortingCriterion;

import java.lang.reflect.Field;
import java.util.Comparator;
import java.util.List;

public class ProductComparator implements Comparator<Product> {
    private final List<SortingCriterion> sortingCriteria;

    public ProductComparator(List<SortingCriterion> sortingCriteria) {
        this.sortingCriteria = sortingCriteria;
    }

    @Override
    public int compare(Product p1, Product p2) {
        for (SortingCriterion sortingCriterion : sortingCriteria) {
            String criterionName = sortingCriterion.getSortingCriterion();
            int comparison;

            try {
                comparison = compareCriterion(p1, p2, criterionName);
            } catch (NoSuchFieldException | IllegalAccessException e) {
                throw new RuntimeException("Error comparing products: " + e.getMessage(), e);
            }

            if (comparison != 0) {
                return (sortingCriterion.getSorting() == Sorting.ASC) ? comparison : -comparison;
            }
        }
        return 0;
    }

    private int compareCriterion(Product p1, Product p2, String criterion) throws NoSuchFieldException, IllegalAccessException {
        Field criterionField = Product.class.getDeclaredField(criterion);
        criterionField.setAccessible(true);

        Object fieldValue1 = criterionField.get(p1);
        Object fieldValue2 = criterionField.get(p2);

        if (fieldValue1 instanceof Comparable<?> && fieldValue2 instanceof Comparable<?>) {
            return ((Comparable) fieldValue1).compareTo(fieldValue2);
        } else {
            return String.valueOf(fieldValue1).compareTo(String.valueOf(fieldValue2));
        }
    }

}
