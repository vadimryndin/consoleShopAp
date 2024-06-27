package pl.coherentsolutions.service.catalog.service;

import pl.coherentsolutions.core.catalog.Catalog;
import pl.coherentsolutions.core.sorting.SortingCriterion;
import pl.coherentsolutions.service.exception.LoadCategoryException;

import java.util.List;
import java.util.Map;

public interface CatalogService {
    Catalog createCatalog() throws LoadCategoryException;

    Catalog sortCatalog(Catalog catalog, List<SortingCriterion> criteria);

    Map<String, Double> findMostExpensiveProducts(Catalog catalog, int range);
}
