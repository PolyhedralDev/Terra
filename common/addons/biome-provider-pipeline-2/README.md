# biome-provider-pipeline-2

The second version of the Biome Pipeline, a procedural biome provider that uses a series
of "stages" to apply "mutations" to a 2D grid of biomes.

Version 2 is a re-implementation of the original addon with the primary goal of providing
consistent scaling for noise relative to the world
(See https://github.com/PolyhedralDev/Terra/issues/264 for more details), and has been
included as a separate addon to maintain parity with packs utilizing the first version. 

This addon registers the `PIPELINE` biome provider type, and all associated
configurations.