package courage.model.repositories.UserRepositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import courage.model.entities.User;

public interface UAccessRepository extends JpaRepository<User.Access, Integer>  {
   // @formatter:off

   default <S extends User.Access> S save(S entity){throw exception();}
   default <S extends User.Access> List<S> saveAll(Iterable<S> entities){throw exception();}
   default <S extends User.Access> S saveAndFlush(S entity){throw exception();}
   default <S extends User.Access> List<S> saveAllAndFlush(Iterable<S> entities) {throw exception();}
   default void delete(User.Access entity){throw exception();}
   default void deleteAll(){throw exception();}
   default void deleteAll(Iterable<? extends User.Access> entities){throw exception();}
   default void deleteAllById(Iterable<? extends Integer> ids){throw exception();}
   default void deleteAllByIdInBatch(Iterable<Integer> ids){throw exception();}
   default void deleteAllInBatch(){throw exception();}
   default void deleteAllInBatch(Iterable<User.Access> entities){throw exception();}
   default void deleteById(Integer id){throw exception();}
   default void deleteInBatch(Iterable<User.Access> entities){throw exception();}
   default IllegalAccessError exception(){return new IllegalAccessError("This method not supported!");}
}