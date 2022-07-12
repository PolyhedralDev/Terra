package com.dfsek.terra.quilt;

import net.gudenau.minecraft.asm.api.v1.AsmInitializer;
import net.gudenau.minecraft.asm.api.v1.AsmRegistry;

import com.dfsek.terra.lifecycle.asm.FertilizableASM;
import com.dfsek.terra.lifecycle.util.LoaderUtil;
import com.dfsek.terra.quilt.util.QuiltLoaderUtil;


public class QuiltASMEntryPoint implements AsmInitializer {
    @Override
    public void onInitializeAsm() {
        LoaderUtil.INSTANCE = new QuiltLoaderUtil();
        AsmRegistry.getInstance().registerTransformer(new FertilizableASM());
    }
}
