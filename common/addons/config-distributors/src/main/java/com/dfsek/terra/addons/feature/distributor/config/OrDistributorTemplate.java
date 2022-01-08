/*
 * Copyright (c) 2020-2021 Polyhedral Development
 *
 * The Terra Core Addons are licensed under the terms of the MIT License. For more details,
 * reference the LICENSE file in this module's root directory.
 */

package com.dfsek.terra.addons.feature.distributor.config;

import com.dfsek.tectonic.api.config.template.ValidatedConfigTemplate;
import com.dfsek.tectonic.api.config.template.annotations.Value;
import com.dfsek.tectonic.api.config.template.object.ObjectTemplate;
import com.dfsek.tectonic.api.exception.ValidationException;

import java.util.List;

import com.dfsek.terra.api.config.meta.Meta;
import com.dfsek.terra.api.structure.feature.Distributor;


public class OrDistributorTemplate implements ObjectTemplate<Distributor>, ValidatedConfigTemplate {
    @Value("distributors")
    private @Meta List<@Meta Distributor> distributors;
    
    
    @Override
    public Distributor get() {
        Distributor current = distributors.remove(0);
        while(!distributors.isEmpty()) {
            current = current.or(distributors.remove(0));
        }
        return current;
    }
    
    @Override
    public boolean validate() throws ValidationException {
        if(distributors.isEmpty()) throw new ValidationException("OR Distributor must specify at least 1 distributor.");
        return true;
    }
}
