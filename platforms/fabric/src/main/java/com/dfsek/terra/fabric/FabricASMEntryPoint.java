package com.dfsek.terra.fabric;

import net.gudenau.minecraft.asm.api.v1.AsmInitializer;
import net.gudenau.minecraft.asm.api.v1.AsmRegistry;

import com.dfsek.terra.fabric.util.FabricLoaderUtil;
import com.dfsek.terra.lifecycle.asm.FertilizableASM;
import com.dfsek.terra.lifecycle.util.LoaderUtil;


public class FabricASMEntryPoint implements AsmInitializer {
    @Override
    public void onInitializeAsm() {
        LoaderUtil.INSTANCE = new FabricLoaderUtil();
        AsmRegistry.getInstance().registerTransformer(new FertilizableASM());
    }
}
