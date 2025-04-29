package com.yungnickyoung.minecraft.yungsextras.module;

import com.yungnickyoung.minecraft.yungsextras.YungsExtrasCommon;
import it.unimi.dsi.fastutil.Pair;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.core.Registry;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.BiomeGenerationSettings;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.carver.ConfiguredWorldCarver;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;

import java.util.ArrayList;
import java.util.List;

public class BiomeModificationModuleFabric {
    private static final int NUM_SWAMP_PILLARS = 3;
    private static final int NUM_SWAMP_CUBBIES = 8;
    private static final int NUM_SWAMP_ARCHES = 11;
    private static final int NUM_SWAMP_DOUBLE_ARCHES = 22;

    public static void init(RegistryAccess registries) {
        new BiomeModificationModuleFabric().addFeaturesToBiomes(registries);
    }

    final List<Pair<String, GenerationStep.Decoration>> desert = new ArrayList<>();
    final List<Pair<String, GenerationStep.Decoration>> swamp = new ArrayList<>();

    private void addFeaturesToBiomes(RegistryAccess registries) {
        // Wells
        addToDesertBiome("desert/wells/desert_well_sm", GenerationStep.Decoration.SURFACE_STRUCTURES);
        addToDesertBiome("desert/wells/desert_well_md", GenerationStep.Decoration.SURFACE_STRUCTURES);
        addToDesertBiome("desert/wells/desert_well_lg", GenerationStep.Decoration.SURFACE_STRUCTURES);
        addToDesertBiome("desert/wells/desert_dry_well_sm", GenerationStep.Decoration.SURFACE_STRUCTURES);
        addToDesertBiome("desert/wells/desert_dry_well_md", GenerationStep.Decoration.SURFACE_STRUCTURES);
        addToDesertBiome("desert/wells/desert_dry_well_lg", GenerationStep.Decoration.SURFACE_STRUCTURES);
        addToDesertBiome("desert/wells/desert_wishing_well_sm", GenerationStep.Decoration.SURFACE_STRUCTURES);
        addToDesertBiome("desert/wells/desert_wishing_well_md", GenerationStep.Decoration.SURFACE_STRUCTURES);
        addToDesertBiome("desert/wells/desert_wishing_well_lg", GenerationStep.Decoration.SURFACE_STRUCTURES);

        // Obelisks
        addToDesertBiome("desert/obelisks/desert_obelisk", GenerationStep.Decoration.SURFACE_STRUCTURES);
        addToDesertBiome("desert/obelisks/desert_obelisk_creeper", GenerationStep.Decoration.SURFACE_STRUCTURES);
        addToDesertBiome("desert/obelisks/desert_obelisk_ruined", GenerationStep.Decoration.SURFACE_STRUCTURES);
        addToDesertBiome("desert/obelisks/desert_obelisk_rare", GenerationStep.Decoration.SURFACE_STRUCTURES);

        // Misc
        addToDesertBiome("desert/misc/desert_giant_torch", GenerationStep.Decoration.SURFACE_STRUCTURES);
        addToDesertBiome("desert/misc/desert_ruins_0", GenerationStep.Decoration.SURFACE_STRUCTURES);
        addToDesertBiome("desert/misc/desert_chillzone", GenerationStep.Decoration.SURFACE_STRUCTURES);

        /* Swamp Features */
        // Pillars
        for (int i = 0; i < NUM_SWAMP_PILLARS; i++) {
            addToSwampBiome("swamp/pillars/swamp_pillar_" + i, GenerationStep.Decoration.SURFACE_STRUCTURES);
        }

        // Cubbies
        for (int i = 0; i < NUM_SWAMP_CUBBIES; i++) {
            addToSwampBiome("swamp/cubbies/swamp_cubby_" + i, GenerationStep.Decoration.SURFACE_STRUCTURES);
        }

        // Arches
        for (int i = 0; i < NUM_SWAMP_ARCHES; i++) {
            addToSwampBiome("swamp/arches/swamp_arch_" + i, GenerationStep.Decoration.SURFACE_STRUCTURES);
        }

        // Double Arches
        for (int i = 0; i < NUM_SWAMP_DOUBLE_ARCHES; i++) {
            addToSwampBiome("swamp/double_arches/swamp_double_arch_" + i, GenerationStep.Decoration.SURFACE_STRUCTURES);
        }

        // Misc
        addToSwampBiome("swamp/misc/swamp_church", GenerationStep.Decoration.SURFACE_STRUCTURES);
        addToSwampBiome("swamp/misc/swamp_ogre", GenerationStep.Decoration.SURFACE_STRUCTURES);

        this.build(registries, "has_structure/desert_decorations", this.desert);
        this.build(registries, "has_structure/swamp_structures", this.swamp);
    }

    private void addToDesertBiome(String featurePath, GenerationStep.Decoration step) {
        // BiomeModifications.create(ResourceLocation.fromNamespaceAndPath(YungsExtrasCommon.MOD_ID, featurePath))
        //         .add(ModificationPhase.ADDITIONS,
        //                 context -> context.hasTag(TagKey.create(Registries.BIOME, ResourceLocation.fromNamespaceAndPath(YungsExtrasCommon.MOD_ID, "has_structure/desert_decorations"))),
        //                 context -> context.getGenerationSettings().addFeature(step, ResourceKey.create(Registries.PLACED_FEATURE, ResourceLocation.fromNamespaceAndPath(YungsExtrasCommon.MOD_ID, featurePath))));
        this.desert.add(Pair.of(featurePath, step));
    }

    private void addToSwampBiome(String featurePath, GenerationStep.Decoration step) {
        // BiomeModifications.create(ResourceLocation.fromNamespaceAndPath(YungsExtrasCommon.MOD_ID, featurePath))
        //         .add(ModificationPhase.ADDITIONS,
        //                 context -> context.hasTag(TagKey.create(Registries.BIOME, ResourceLocation.fromNamespaceAndPath(YungsExtrasCommon.MOD_ID, "has_structure/swamp_structures"))),
        //                 context -> context.getGenerationSettings().addFeature(step, ResourceKey.create(Registries.PLACED_FEATURE, ResourceLocation.fromNamespaceAndPath(YungsExtrasCommon.MOD_ID, featurePath))));
        this.swamp.add(Pair.of(featurePath, step));
    }

    private void build(RegistryAccess registries, String biomesPath, List<Pair<String, GenerationStep.Decoration>> addFeatures) {
        final Registry<Biome> biomeRegistry = registries.lookupOrThrow(Registries.BIOME);
        final Iterable<Holder<Biome>> biomes = biomeRegistry.getTagOrEmpty(TagKey.create(Registries.BIOME, ResourceLocation.fromNamespaceAndPath(YungsExtrasCommon.MOD_ID, biomesPath)));
        biomes.forEach(biomeHolder -> {
            final BiomeGenerationSettings generationSettings = biomeHolder.value().getGenerationSettings();
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

            final Registry<PlacedFeature> placedFeatureRegistry = registries.lookupOrThrow(Registries.PLACED_FEATURE);
            addFeatures.forEach(pair -> {
                final ResourceLocation location = ResourceLocation.fromNamespaceAndPath(YungsExtrasCommon.MOD_ID, pair.left());
                final Holder.Reference<PlacedFeature> orThrow = placedFeatureRegistry.getOrThrow(ResourceKey.create(Registries.PLACED_FEATURE, location));
                builder.addFeature(pair.right(), orThrow);
            });

            biomeHolder.value().generationSettings = builder.build();
        });
    }
}
