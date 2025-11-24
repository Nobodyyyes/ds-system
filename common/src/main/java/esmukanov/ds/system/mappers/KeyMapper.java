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
                .privateKeyEncrypted(entity.getPrivateKeyEncrypted())
                .privateKeyIv(entity.getPrivateKeyIv())
                .publicKey(entity.getPublicKey())
                .createdAt(entity.getCreatedAt())
                .isRevoked(entity.isRevoked())
                .revokedAt(entity.getRevokedAt())
                .version(entity.getVersion())
                .build();
    }

    @Override
    public UserKeyEntity toEntity(UserKey model) {
        if (model == null) return null;

        return UserKeyEntity.builder()
                .id(model.getId())
                .userId(model.getUserId())
                .privateKeyEncrypted(model.getPrivateKeyEncrypted())
                .privateKeyIv(model.getPrivateKeyIv())
                .publicKey(model.getPublicKey())
                .createdAt(model.getCreatedAt())
                .isRevoked(model.isRevoked())
                .revokedAt(model.getRevokedAt())
                .version(model.getVersion())
                .build();
    }
}
