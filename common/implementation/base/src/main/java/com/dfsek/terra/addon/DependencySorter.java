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

package com.dfsek.terra.addon;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.dfsek.terra.addon.dependency.CircularDependencyException;
import com.dfsek.terra.addon.dependency.DependencyException;
import com.dfsek.terra.addon.dependency.DependencyVersionException;
import com.dfsek.terra.api.addon.BaseAddon;


public class DependencySorter {
    private final Map<String, BaseAddon> addons = new HashMap<>();
    private final Map<String, Boolean> visited = new HashMap<>();
    
    private final List<BaseAddon> addonList = new ArrayList<>();
    
    public void add(BaseAddon addon) {
        addons.put(addon.getID(), addon);
        visited.put(addon.getID(), false);
        addonList.add(addon);
    }
    
    private void sortDependencies(BaseAddon addon, List<BaseAddon> sort) {
        addon.getDependencies().forEach((id, range) -> {
            BaseAddon dependency = get(id, addon);
            
            if(!range.isSatisfiedBy(dependency.getVersion())) {
                throw new DependencyVersionException(
                        "Addon " + addon.getID() + " specifies dependency on " + id + ", versions " + range.getFormatted() +
                        ", but non-matching version " + dependency.getVersion().getFormatted() + " is installed.");
            }
            
            if(!visited.get(dependency.getID())) { // if we've not visited it yet
                visited.put(dependency.getID(), true); // we've visited it now
                
                sortDependencies(dependency, sort);
                
                sort.add(dependency); // add it to the list.
            }
        });
    }
    
    private BaseAddon get(String id, BaseAddon addon) {
        if(!addons.containsKey(id)) {
            throw new DependencyException("Addon " + addon.getID() + " specifies dependency on " + id + ", versions " +
                                          addon.getDependencies().get(id).getFormatted() +
                                          ", but no such addon is installed.");
        }
        return addons.get(id);
    }
    
    private void checkDependencies(BaseAddon base, BaseAddon current) {
        current.getDependencies().forEach((id, range) -> {
            BaseAddon dependency = get(id, current);
            if(dependency.getID().equals(base.getID())) {
                throw new CircularDependencyException(
                        "Addon " + base.getID() + " has circular dependency beginning with " + dependency.getID());
            }
            checkDependencies(base, dependency);
        });
    }
    
    public List<BaseAddon> sort() {
        List<BaseAddon> sorted = new ArrayList<>();
        
        for(int i = addonList.size() - 1; i >= 0; i--) {
            BaseAddon addon = addonList.get(i);
            
            checkDependencies(addon, addon);
            
            addonList.remove(i);
            
            if(!visited.get(addon.getID())) {
                sortDependencies(addon, sorted);
            }
            
            if(!visited.get(addon.getID())) {
                sorted.add(addon);
                visited.put(addon.getID(), true);
            }
        }
        
        return sorted;
    }
}
