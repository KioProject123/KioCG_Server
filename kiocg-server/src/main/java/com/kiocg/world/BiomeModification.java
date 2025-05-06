package com.kiocg.world;

import it.unimi.dsi.fastutil.Pair;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.core.Registry;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.placement.OrePlacements;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.BiomeGenerationSettings;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.carver.ConfiguredWorldCarver;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BiomeModification {
    private final Map<Biome, List<Pair<Holder<PlacedFeature>, GenerationStep.Decoration>>> modification = new HashMap<>();
    private final RegistryAccess registryAccess;

    public BiomeModification(RegistryAccess registryAccess) {
        this.registryAccess = registryAccess;
    }

    public RegistryAccess getRegistry() {
        return this.registryAccess;
    }

    public void addCustom() {
        final Registry<Biome> biomeRegistry = registryAccess.lookupOrThrow(Registries.BIOME);
        final List<Biome> list = biomeRegistry.stream()
                                              .filter(biome -> {
                                                  final List<HolderSet<PlacedFeature>> features = biome.getGenerationSettings().features();
                                                  if (features.size() > GenerationStep.Decoration.UNDERGROUND_ORES.ordinal()) {
                                                      boolean hasDiamondOre = false;
                                                      final HolderSet<PlacedFeature> holders = features.get(GenerationStep.Decoration.UNDERGROUND_ORES.ordinal());
                                                      for (Holder<PlacedFeature> holder : holders) {
                                                          if (holder.is(OrePlacements.ORE_EMERALD)) {
                                                              return false;
                                                          }
                                                          hasDiamondOre = hasDiamondOre || holder.is(OrePlacements.ORE_DIAMOND);
                                                      }
                                                      return hasDiamondOre;
                                                  }
                                                  return false;
                                              })
                                              .toList();
        final Registry<PlacedFeature> placedFeatureRegistry = registryAccess.lookupOrThrow(Registries.PLACED_FEATURE);
        final ResourceLocation resourceLocation = ResourceLocation.fromNamespaceAndPath("kiocg", "ore_emerald_lucky");
        final Holder.Reference<PlacedFeature> placedFeature = placedFeatureRegistry.getOrThrow(ResourceKey.create(Registries.PLACED_FEATURE, resourceLocation));
        add(list, placedFeature, GenerationStep.Decoration.UNDERGROUND_ORES);
    }

    public void add(Biome biome, Holder<PlacedFeature> placedFeature, GenerationStep.Decoration decoration) {
        modification.computeIfAbsent(biome, k -> new ArrayList<>()).add(Pair.of(placedFeature, decoration));
    }

    public void add(List<Biome> biomes, Holder<PlacedFeature> placedFeature, GenerationStep.Decoration decoration) {
        biomes.forEach(biomeHolder -> add(biomeHolder, placedFeature, decoration));
    }

    public void add(Iterable<Holder<Biome>> biomes, Holder<PlacedFeature> placedFeature, GenerationStep.Decoration decoration) {
        biomes.forEach(biomeHolder -> add(biomeHolder.value(), placedFeature, decoration));
    }

    public void applyModification() {
        modification.forEach((biome, pairs) -> {
            final BiomeGenerationSettings generationSettings = biome.getGenerationSettings();
            final List<HolderSet<PlacedFeature>> features = generationSettings.features();
            final Iterable<Holder<ConfiguredWorldCarver<?>>> carvers = generationSettings.getCarvers();

            final BiomeGenerationSettings.PlainBuilder builder = new BiomeGenerationSettings.PlainBuilder();
            for (int i = 0; i < features.size(); i++) {
                for (Holder<PlacedFeature> placedFeature : features.get(i)) {
                    builder.addFeature(i, placedFeature);
                }
            }
            for (Holder<ConfiguredWorldCarver<?>> carver : carvers) {
                builder.addCarver(carver);
            }

            pairs.forEach(pair -> builder.addFeature(pair.right(), pair.left()));
            biome.generationSettings = builder.build();
        });
    }
}
