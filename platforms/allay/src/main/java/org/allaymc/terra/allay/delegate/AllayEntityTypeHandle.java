package org.allaymc.terra.allay.delegate;

import com.dfsek.terra.api.entity.EntityType;


/**
 * Terra Project 2024/6/16
 *
 * 我们暂时不支持实体，因为端本身都没实体ai，生成实体没有意义
 *
 * @author daoge_cmd
 */
public record AllayEntityTypeHandle(String id) implements EntityType {
    @Override
    public String getHandle() {
        return id;
    }
}
