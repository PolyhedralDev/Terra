package com.dfsek.terra.world.attribute.platform;

import org.junit.jupiter.api.Test;

import com.dfsek.terra.api.world.attribute.Attribute;
import com.dfsek.terra.api.world.attribute.AttributeEntry;
import com.dfsek.terra.api.world.attribute.AttributeHandle;
import com.dfsek.terra.api.world.attribute.AttributeKey;
import com.dfsek.terra.world.attribute.AttributeHandleImpl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;


class AttributeSlotsTest {
    @Test
    void outOfRangeIsNull() {
        AttributeHandle handle = new AttributeHandleImpl();
        Attribute<Float> attribute = null;
        for(int i = 0; i <= 5; i++) {
            attribute = handle.attributeBuilder(AttributeKey.<Float>parse("test:a" + i), handle.types().floatType(), 0f).build();
        }
        assertEquals(5, attribute.index());

        assertNull(AttributeSlots.of(new AttributeEntry<?>[1]).get(attribute));
    }
}
