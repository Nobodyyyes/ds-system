package esmukanov.ds.system.components;

import esmukanov.ds.system.exceptions.NotFoundException;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public class BaseCrudOperationImpl<M, E, ID> implements BaseCrudOperation<M, ID> {

    protected final JpaRepository<E, ID> repository;

    protected final BaseMapper<M, E> baseMapper;

    public BaseCrudOperationImpl(JpaRepository<E, ID> repository, BaseMapper<M, E> baseMapper) {
        this.repository = repository;
        this.baseMapper = baseMapper;
    }

    /**
     * Retrieves all entities from the repository and maps them to their corresponding models.
     *
     * @return a list of models representing all entities in the repository
     */
    @Override
    public List<M> getAll() {
        return baseMapper.toModels(repository.findAll());
    }

    /**
     * Retrieves an entity by its ID and maps it to its corresponding model.
     *
     * @param id the ID of the entity to retrieve
     * @return an Optional containing the model representation of the entity,
     * or an empty Optional if the entity is not found
     */
    @Override
    public M getById(ID id) {
        return repository.findById(id)
                .map(baseMapper::toModel)
                .orElseThrow(() -> new NotFoundException("Not found by ID [%s]".formatted(id)));
    }

    /**
     * Saves a model by converting it to an entity and persisting it in the repository.
     *
     * @param model the model to save
     * @return the saved model, converted back from the persisted entity
     */
    @Override
    public M save(M model) {
        return baseMapper.toModel(repository.save(baseMapper.toEntity(model)));
    }

    /**
     * Updates an existing entity by its ID with the provided model data.
     *
     * @param id    the ID of the entity to update
     * @param model the model containing updated data
     * @return the updated model, converted back from the persisted entity
     * @throws IllegalArgumentException if the provided ID is null
     * @throws NotFoundException        if no entity with the given ID exists
     */
    @Override
    public M update(ID id, M model) {

        if (id == null) {
            throw new IllegalArgumentException("ID must be null");
        }

        if (!repository.existsById(id)) {
            throw new NotFoundException("Entity with ID [%s] not found".formatted(id));
        }

        return save(model);
    }

    /**
     * Deletes an entity by its ID.
     *
     * @param id the ID of the entity to delete
     * @throws IllegalArgumentException if the provided ID is null
     */
    @Override
    public void delete(ID id) {
        repository.deleteById(id);
    }
}
