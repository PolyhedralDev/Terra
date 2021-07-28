package com.dfsek.terra.addons.feature.distributor.config;

import com.dfsek.tectonic.annotations.Value;
import com.dfsek.tectonic.config.ValidatedConfigTemplate;
import com.dfsek.tectonic.exception.ValidationException;
import com.dfsek.tectonic.loading.object.ObjectTemplate;
import com.dfsek.terra.api.config.meta.Meta;
import com.dfsek.terra.api.structure.feature.Distributor;

import java.util.List;

public class AndDistributorTemplate implements ObjectTemplate<Distributor>, ValidatedConfigTemplate {
    @Value("distributors")
    private @Meta List<@Meta Distributor> distributors;


    @Override
    public Distributor get() {
        Distributor current = distributors.remove(0);
        while(!distributors.isEmpty()) {
            current = current.and(distributors.remove(0));
        }
        return current;
    }

    @Override
    public boolean validate() throws ValidationException {
        if(distributors.isEmpty()) throw new ValidationException("AND Distributor must specify at least 1 distributor.");
        return true;
    }
}
