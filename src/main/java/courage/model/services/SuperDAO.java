package courage.model.services;


public interface SuperDAO<E, K> {

    /**
     * save one entity
     * 
     * @param entity
     * @return the new entity if success
     */
    E save(E entity);

    /**
     * save one entity
     * 
     * @param entities
     * @return the new entities if success
     */
    Iterable<E> saveAll(Iterable<E> iterable);
}
