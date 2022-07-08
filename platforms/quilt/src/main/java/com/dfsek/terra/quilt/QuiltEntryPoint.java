/*
 * This file is part of Terra.
 *
 * Terra is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Terra is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Terra.  If not, see <https://www.gnu.org/licenses/>.
 */

package com.dfsek.terra.quilt;

import org.quiltmc.loader.api.ModContainer;
import org.quiltmc.qsl.base.api.entrypoint.ModInitializer;

import com.dfsek.terra.lifecycle.LifecycleEntryPoint;


public class QuiltEntryPoint extends LifecycleEntryPoint implements ModInitializer {
    private static final QuiltPlatform TERRA_PLUGIN = new QuiltPlatform();
    
    @Override
    public void onInitialize(ModContainer container) {
        initialize("Quilt", TERRA_PLUGIN);
    }
}
