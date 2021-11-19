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
    private final Map<BaseAddon, Boolean> visited = new HashMap<>();
    
    private final List<BaseAddon> addonList = new ArrayList<>();
    
    public void add(BaseAddon addon) {
        addons.put(addon.getID(), addon);
        visited.put(addon, false);
        addonList.add(addon);
    }
    
    private void sortDependencies(BaseAddon addon, List<BaseAddon> sort) {
        addon.getDependencies().forEach((id, range) -> {
            if(!addons.containsKey(id)) {
                throw new DependencyException("Addon " + addon.getID() + " specifies dependency on " + id + ", versions " + range + ", but no such addon is installed.");
            }
            
            BaseAddon dependency = addons.get(id);
            
            if(!range.isSatisfiedBy(dependency.getVersion())) {
                throw new DependencyVersionException("Addon " + addon.getID() + " specifies dependency on " + id + ", versions " + range + ", but non-matching version " + dependency.getVersion() + " is installed..");
            }
            
            if(!visited.get(dependency)) { // if we've not visited it yet
                visited.put(dependency, true); // we've visited it now
                
                if(!dependency.getDependencies().isEmpty()) { // if this addon has dependencies...
                    sortDependencies(dependency, sort); // sort them first.
                }
                
                if(sort.contains(dependency)) {
                    throw new CircularDependencyException("Addon " + addon.getID() + " has circular dependency beginning with " + dependency.getID());
                }
                
                sort.add(dependency); // add it to the list.
            }
        });
    }
    
    public List<BaseAddon> sort() {
        List<BaseAddon> sorted = new ArrayList<>();
        
        for(int i = addonList.size() - 1; i >= 0; i--) {
            BaseAddon addon = addonList.get(i);
            addonList.remove(i);
            
            if(!visited.get(addon)) { // if we've not visited it yet
                visited.put(addon, true); // we've visited it now
                if(!addon.getDependencies().isEmpty()) { // if this addon has dependencies...
                    sortDependencies(addon, sorted);
                }
            }
            
            sorted.add(addon);
        }
        
        return sorted;
    }
}
