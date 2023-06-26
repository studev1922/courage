package courage.model.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository {

   interface AvoidRepository<E, K> extends JpaRepository<E, K> {
      // @formatter:off
      default <S extends E> S save(S entity){throw exception();}
      default <S extends E> List<S> saveAll(Iterable<S> entities){throw exception();}
      default <S extends E> S saveAndFlush(S entity){throw exception();}
      default <S extends E> List<S> saveAllAndFlush(Iterable<S> entities) {throw exception();}
      default void delete(E entity){throw exception();}
      default void deleteAll(){throw exception();}
      default void deleteAll(Iterable<? extends E> entities){throw exception();}
      default void deleteAllById(Iterable<? extends K> ids){throw exception();}
      default void deleteAllByIdInBatch(Iterable<K> ids){throw exception();}
      default void deleteAllInBatch(){throw exception();}
      default void deleteAllInBatch(Iterable<E> entities){throw exception();}
      default void deleteById(K id){throw exception();}
      default void deleteInBatch(Iterable<E> entities){throw exception();}

      default IllegalAccessError exception(){return new IllegalAccessError("This method not supported!");}
   }
}