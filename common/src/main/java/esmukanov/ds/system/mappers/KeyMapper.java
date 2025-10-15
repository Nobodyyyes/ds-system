package esmukanov.ds.system.mappers;

import esmukanov.ds.system.components.base.BaseMapper;
import esmukanov.ds.system.entities.UserKeyEntity;
import esmukanov.ds.system.models.UserKey;
import org.springframework.stereotype.Component;

@Component
public class KeyMapper implements BaseMapper<UserKey, UserKeyEntity> {

    @Override
    public UserKey toModel(UserKeyEntity entity) {
        if (entity == null) return null;

        return UserKey.builder()
                .id(entity.getId())
                .userId(entity.getUserId())
                .privateKey(entity.getPrivateKey())
                .publicKey(entity.getPublicKey())
                .build();
    }

    @Override
    public UserKeyEntity toEntity(UserKey model) {
        if (model == null) return null;

        return UserKeyEntity.builder()
                .id(model.getId())
                .userId(model.getUserId())
                .privateKey(model.getPrivateKey())
                .publicKey(model.getPublicKey())
                .build();
    }
}
