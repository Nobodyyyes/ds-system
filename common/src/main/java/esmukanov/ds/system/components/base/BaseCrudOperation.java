package esmukanov.ds.system.components.base;

import java.util.List;

public interface BaseCrudOperation<M, ID> {

    List<M> getAll();

    M getById(ID id);

    M save(M model);

    M update(ID id, M update);

    void delete(ID id);
}
