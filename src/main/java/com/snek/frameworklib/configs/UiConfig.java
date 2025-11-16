package com.snek.frameworklib.configs;

import com.snek.frameworkconfig.ConfigFile;
import com.snek.frameworkconfig.fields.ValueConfigField;








public class UiConfig implements ConfigFile {
    public final ValueConfigField<Float> z_layer_spacing = new ValueConfigField<>(
        new String[] {
            "The distance between UI Z-Layers.",
            "Must be between 0.0005 and 0.1",
        },
        0.001f
    );








    @Override
    public void validate() {

        // Check z layer spacing
        if(z_layer_spacing.getValue() < 0.0005f) throw new IllegalStateException("Z-Layer spacing must be >= 0.0005");
        if(z_layer_spacing.getValue() > 0.1f)    throw new IllegalStateException("Z-Layer spacing must be <= 0.1");
    }
}
