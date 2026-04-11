package nl.fontys.newswebapplication.repositories;

public interface Filter<T> {
    T[] useFilter(T[] data, String filter);
}
